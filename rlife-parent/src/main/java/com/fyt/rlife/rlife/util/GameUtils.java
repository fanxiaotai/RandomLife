package com.fyt.rlife.rlife.util;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.*;
import com.fyt.rlife.rlife.bean.game.factory.GameVO;
import com.fyt.rlife.rlife.bean.monster.suffix.Suffix;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.gamestate.GameState;

import java.util.*;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 17:13
 * @Version 1.0
 */
public class GameUtils {

    /**
     * 判断角色魔法值是否足够使用技能
     */
    public static boolean skillRoleMagic(Skill skill,Role role){
        Integer skillConsume = skill.getSkillConsume();
        Integer magic = role.getMagic();
        if (magic>skillConsume){
            role.setMagic(magic-skillConsume);
            return true;
        }
        return false;
    }

    /**
     * 根据距离消耗角色饥饿度
     */
    public static void getFeedDegree(Role role,int x,int y,int roleX,int roleY,StringBuilder stringBuilder){
        Integer feedDegree = role.getFeedDegree();
        int i = GameUtils.distanceXY(x, y, roleX, roleY);
        if (feedDegree>=i){
            feedDegree -= i;
            role.setFeedDegree(feedDegree);
        }else {
            i -= feedDegree;
            stringBuilder.append("你因饥饿损失了");
            stringBuilder.append(i);
            stringBuilder.append("点生命");
            role.setLife(role.getLife()-i);
            role.setFeedDegree(0);
        }
    }

    /**
     * 比对两个坐标的距离
     */
    public static int distanceXY(int x,int y,int i,int j){
        int a = 0;
        if (i>x){
            a += i-x;
        }else {
            a += x-i;
        }
        if (j>y){
            a+= j-y;
        }else {
            a+= y-j;
        }

        return a;
    }

    /**
     * 接近目标坐标
     */
    public static String moveTarget(String sta,String end){
        String[] splitSta = sta.split(",");
        String[] splitEnd = end.split(",");
        int i = Integer.parseInt(splitSta[0]);
        int j = Integer.parseInt(splitEnd[0]);
        int b = Integer.parseInt(splitSta[1]);
        int c = Integer.parseInt(splitEnd[1]);
        if (i-j>0){
            i--;
            return i+","+b;
        }else if (i-j<0){
            i++;
            return i+","+b;
        }
        if (b-c>0){
            b--;
            return i+","+b;
        }else if(b-c<0){
            b++;
            return i+","+b;
        }
        return i+","+b;
    }

    /**
     * 无目标移动
     * @return
     */
    public static void noIntentionMove(Monster monster,int i,Random random){
        int a = monster.x;
        int b = monster.y;

        int c = 0;

        while (i>0){
            c = random.nextInt(4) + 1;
            switch (c){
                case 1:
                    monster.x = ++a;
                    break;
                case 2:
                    monster.x = --a;
                    break;
                case 3:
                    monster.y = ++b;
                    break;
                default:
                    monster.y = --b;
                    break;
            }
            i--;
        }

        if (monster.x<0){
            monster.x = 0;
        }

        if (monster.x>7){
            monster.x = 7;
        }

        if (monster.y<0){
            monster.y = 0;
        }

        if (monster.y>7){
            monster.y = 7;
        }
    }

    /**
     * 怪物集合形成地图映射
     * @param map
     * @param game1GameMapLists
     * @return
     */
    public static GameMap<Game1>[][] mapMonster(Map<String,List<Monster>> map, GameMap<Game1>[][] game1GameMapLists){

        Set<Map.Entry<String, List<Monster>>> entries = map.entrySet();
        Iterator<Map.Entry<String, List<Monster>>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, List<Monster>> monster = iterator.next();
            String monsterKey = monster.getKey();
            List<Monster> monsterList = monster.getValue();
            String[] splitSta = monsterKey.split(",");
            int i = Integer.parseInt(splitSta[0]);
            int b = Integer.parseInt(splitSta[1]);
            game1GameMapLists[i][b].getData().setMonsters(monsterList);
        }

        return game1GameMapLists;
    }

    /**
     * 从redis中获取地图
     */
    public static GameMap<Game1>[][] getMapByRedis(String game1GameMapListsStr){
        List<GameMap[]> gameMaps = JSON.parseArray(game1GameMapListsStr, GameMap[].class);
        GameMap<Game1>[][] game1GameMapLists = new GameMap[gameMaps.size()][];
        game1GameMapLists = gameMaps.toArray(game1GameMapLists);
        for (GameMap<Game1>[] game1GameMapList : game1GameMapLists) {
            for (GameMap<Game1> gameMap : game1GameMapList) {
                String str = JSON.toJSONString(gameMap.getData());
                Game1 game1 = JSON.parseObject(str, Game1.class);
                gameMap.setData(game1);
            }
        }
        return game1GameMapLists;
    }

    /**
     * 攻击行为
     */
    public static void fighting(Role role, Monster monster, StringBuilder stringBuilder,Random random){
        //触发战斗前状态
        GameState.fightBeforeStateList(role,monster,stringBuilder);
        int roleLife = role.getLife();
        int i = roleLife;
        Integer roleDefense = role.getDefense();


        while (monster.life>0&&roleLife>0){
            //角色攻击
            role.roleNormalAttack(monster,stringBuilder);
            GameState.fightStateAttackList(role,monster,stringBuilder);
            //怪物攻击
            roleLife = monster.monsterAttack(roleLife,roleDefense,stringBuilder);
        }
        if (roleLife<=0){
            role.setSurvive(1);
            return;
        }else {
            //怪物死亡
            monster.monsterDeath(stringBuilder,role,random);
            //修改战斗后的生命值
            role.setLife(roleLife);
            //触发战斗后状态
            GameState.fightAfterStateList(role,stringBuilder);
        }

    }

    /**
     * 死亡行为
     */
    public static void monsterDeath(Role role, Monster monster, StringBuilder stringBuilder,List<GameProp> gameProps){
        stringBuilder.append(monster.getNickname());
        stringBuilder.append("已死亡&#10;");

        //获得掉落物
        Map<String, GameProp> gamePropMap = role.getGamePropMap();
        if (gamePropMap==null){
            gamePropMap = new HashMap<>();
        }
        if (gameProps!=null){
            for (GameProp gameProp : gameProps) {
                String propId = gameProp.getId();
                if (gamePropMap.containsKey(propId)){
                    GameProp gameProp1 = gamePropMap.get(propId);
                    gameProp1.setTheNumber(gameProp1.getTheNumber()+1);
                }else {
                    gamePropMap.put(propId,gameProp);
                }
                stringBuilder.append("你获得了");
                stringBuilder.append(gameProp.getPropName());
                stringBuilder.append("&#10;");
            }
        }

        //获得怪物经验和金钱
        role.setExp(role.getExp()+monster.getMonsterExp());
        role.setGold(role.getGold()+monster.getMonsterGold());
    }

    /**
     * 战斗数据的显示
     */
    public static void fightingString(String a,String b,int i,StringBuilder stringBuilder){
        stringBuilder.append(a);
        stringBuilder.append("对");
        stringBuilder.append(b);
        stringBuilder.append("造成");
        stringBuilder.append(i);
        stringBuilder.append("点伤害&#10;");

    }

    /**
     * 角色道具转换为前端视图
     */
    public static void roleProp2Vo(Map<String, GameProp> gamePropMap, PackVo packVo) {
        Collection<GameProp> gameProps = gamePropMap.values();
        Iterator<GameProp> iterator = gameProps.iterator();
        int i = 0;

        List<List<GameProp>> propListss = new ArrayList<>();
        List<GameProp> props = new ArrayList<>();
        while (iterator.hasNext()){
            if (i!=0&&i%10==0){
                propListss.add(props);
                props = new ArrayList<>();
            }
            props.add(iterator.next());
            i++;
        }
        propListss.add(props);
        packVo.setPropLists(propListss);
    }

    /**
     * 获取经过因难度而添加后缀的对象
     * @param difficulty 难度
     * @param layer 层数
     * @param monster
     * @return
     */
    public static Monster getSuffixMonster(List<Class<Suffix>> classList,int difficulty,int layer,Monster monster) throws Exception {
        //高于难度1的额外难度都会多生成一个后缀
        Suffix suffix = null;
        for (int i = 0;i<difficulty-1;i++){
            suffix = getSuffix(classList, layer, monster);
            //java中引用对象的地址改变不会影响传参对象，值传递
            monster = suffix;
        }

        if (suffix==null){
            return monster;
        }
        return suffix;
    }

    /**
     * 随机获取后缀
     */
    public static Suffix getSuffix(List<Class<Suffix>> classList,int layer,Monster monster) throws Exception{
        if (classList.size()==0){
            throw new RuntimeException("集合中没有后缀的Class");
        }
        Random random = new Random();
        int i = random.nextInt(classList.size());
        return classList.get(i).getConstructor(Integer.class,Monster.class).newInstance(layer,monster);
    }

    /**
     * 转化为前端可视地图
     */
    public static GameMap<GameVO>[][] GameMapPO2VO(GameMap<Game1>[][] gameMaps){

        GameMap<GameVO>[][] gameVOMaps = new GameMap[gameMaps.length][gameMaps.length];

        int i = 0;
        int j = 0;
        for (GameMap<Game1>[] gameMap : gameMaps) {
            j=0;
            for (GameMap<Game1> game1GameMap : gameMap) {
                GameMap<GameVO> gameMap1 = new GameMap<>();
                GameVO gameVO = new GameVO();
                StringBuilder mapDescribe = new StringBuilder();
                StringBuilder mapTitle = new StringBuilder();

                Game1 data = game1GameMap.getData();
                //地图的怪物
                List<Monster> monsters = data.getMonsters();
                if (monsters!=null&&monsters.size()!=0){
                    for (Monster monster : monsters) {
                        while (monster instanceof Suffix){
                            Suffix suffix = (Suffix) monster;
                            mapDescribe.append("(");
                            mapDescribe.append(suffix.getName());
                            mapDescribe.append(")");
                            monster = suffix.getMonster();
                        }
                        mapDescribe.append(monster.getNickname());
                        mapDescribe.append("&#10;");

                        mapTitle.append("血量: ");
                        mapTitle.append(monster.life);
                        mapTitle.append("\n");
                        mapTitle.append("Lv: ");
                        mapTitle.append(monster.getMonsterLeave());
                        mapTitle.append("\n");
                        mapTitle.append("攻击: ");
                        mapTitle.append(monster.getAttack());
                        mapTitle.append("\n");
                        mapTitle.append("防御: ");
                        mapTitle.append(monster.getDefense());
                        mapTitle.append("\n");
                        mapTitle.append("\n");
                        mapTitle.append("\n");
                        mapTitle.append(monster.getMonsterDescribe());
                        mapTitle.append("\n");
                    }
                }

                //地图中的角色
                Role role = data.getRole();
                if (role!=null){
                    mapDescribe.append(role.getNickname());
                    mapDescribe.append("\n");
                }

                //地图中的宝箱
                Box box = data.getBox();
                if (box!=null){
                    mapDescribe.append("宝箱");
                    mapDescribe.append("\n");

                    mapTitle.append(box.getBoxLeave());
                    mapTitle.append("级宝箱");
                    mapTitle.append("\n");
                    mapTitle.append(box.getBoxDescribe());
                    mapTitle.append("\n");
                }

                //地图中的商店
                GameStore store = data.getStore();
                if (store!=null){
                    mapDescribe.append("商店");
                    mapDescribe.append("\n");
                }

                //地图中的房间
                Room room = data.getRoom();
                if (room!=null){
                    mapDescribe.append("房间");
                    mapDescribe.append("\n");
                }

                //地图中的下一层入口
                if (data.isNextLayer()){
                    mapDescribe.append("下一层");
                    mapDescribe.append("\n");
                }

                //封装视图对象
                gameVO.setMapDescribe(mapDescribe.toString());
                gameVO.setMapTitle(mapTitle.toString());
                gameVOMaps[i][j]=gameMap1;
                gameVOMaps[i][j].setData(gameVO);
                gameVOMaps[i][j].setX(i);
                gameVOMaps[i][j].setY(j);

                j++;
            }
            i++;
        }
        return gameVOMaps;
    }
}
