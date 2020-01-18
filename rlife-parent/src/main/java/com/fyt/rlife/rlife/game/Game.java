package com.fyt.rlife.rlife.game;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.gamestate.GameState;
import com.fyt.rlife.rlife.service.gameService.MonsterService;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author: fanyitai
 * @Date: 2020/1/4 10:54
 * @Version 1.0
 */
public class Game {

    public List<List<GameMap<String>>> game1(HttpServletRequest request, Role role, MonsterService monsterService){

        //后台应用数据保存
        GameMap<Game1>[][] game1GameMapLists = new GameMap[15][15];
        for (int i = 0;i<15;i++){
            for (int j = 0;j<15;j++){
                Game1 game1 = new Game1();
                GameMap<Game1> objectGameMap = new GameMap<>();
                objectGameMap.setData(game1);

                List<Monster> monsters = new ArrayList<>();

                //生成地形
                int i1 = randomTerrain();
                game1.setTerrain(i1);

                //生成怪物
                Monster monster = randomMonster(3, i1,monsterService);
                if (monster!=null){
                    monsters.add(monster);
                }
                game1.setMonsters(monsters);

                //生成坐标
                objectGameMap.setX(i);
                objectGameMap.setY(j);

                //生成角色
                if (i ==7&&j==7){
                    game1.setRole(role);
                }

                //地图封装
                objectGameMap.setData(game1);

                game1GameMapLists[i][j] = objectGameMap;
            }

        }
        HttpSession session = request.getSession();
        session.setAttribute("game1GameMapLists"+role.getId(),game1GameMapLists);
        session.setAttribute("role" + role.getId(),role);
        session.setMaxInactiveInterval(60*60*5);//设置最大过期时间为2小时
        /*String game1GameMapStr = JSON.toJSONString(game1GameMapLists);
        Jedis jedis = redisUtil.getJedis();
        jedis.set("game1GameMapLists"+role.getId(),game1GameMapStr);*/

        return game2gameMap(game1GameMapLists);
    }

    /**
     * 根据地形随机生成怪物
     * @param i 生成几率-1
     * @param monsterId 怪物编号
     * @return 怪物集合
     */
    public Monster randomMonster(int i,int monsterId,MonsterService monsterService){
        Random random = new Random();
        int i1 = random.nextInt(100)+1;
        Monster monster = null;
        if (i1<i){
            switch (monsterId){
                case 2:
                    monster = monsterService.getMonsterById(2);
                    break;
                case 3:
                    monster = monsterService.getMonsterById(3);
                    break;
                case 5:
                    monster = monsterService.getMonsterById(4);
                    break;
                default:
                    monster = monsterService.getMonsterById(1);
            }
        }
        return monster;
    }

    /**
     * 随机生成地形
     * @return
     */
    public int randomTerrain(){
        Random random = new Random();
        int i1 = random.nextInt(5)+1;
        return i1;
    }

    /**
     * 将二维数组转化为前端所需的嵌套集合
     * @param game1GameMapLists
     * @return
     */
    public static List<List<GameMap<String>>> game2gameMap(GameMap<Game1>[][] game1GameMapLists){
        List<List<GameMap<String>>> gameMapLists = new ArrayList<>();
        int i = 0;
        for (GameMap<Game1>[] gameMaps : game1GameMapLists) {
            List<GameMap<String>> gameMapsList = new ArrayList<>();
            int j =0;
            for (GameMap<Game1> gameMap : gameMaps) {
                GameMap<String> gameMapStr = new GameMap<>();
                StringBuilder sbs = new StringBuilder();
                gameMapStr.setX(i);
                gameMapStr.setY(j);
                sbs.append(gameMap.getData().getTerrain());

                if (gameMap.getData().getMonsters()!=null&&gameMap.getData().getMonsters().size()!=0){
                    List<Monster> monsters = gameMap.getData().getMonsters();
                    StringBuilder sb = new StringBuilder();
                    for (Monster monster : monsters) {
                        if (sb!=null&&sb.length()!=0){
                            sb.append(",");
                        }
                        sb.append(monster.getNickname());
                    }
                    if (sb!=null&&sb.length()!=0){
                        sbs.append(","+sb.toString());
                    }
                }

                if (gameMap.getData().getRole()!=null){
                    sbs.append(","+gameMap.getData().getRole().getNickname());
                }

                if (gameMap.getData().getBox()!=null){
                    sbs.append(","+gameMap.getData().getBox().getBoxLeave()+"级宝箱");
                }
                j++;
                gameMapStr.setData(sbs.toString());
                gameMapsList.add(gameMapStr);
            }
            i++;
            gameMapLists.add(gameMapsList);
        }
        return gameMapLists;
    }

    /**
     * 比对两个坐标的距离
     */
    public static int distanceXY(String sta,String end){
        int a = 0;
        String[] splitSta = sta.split(",");
        String[] splitEnd = end.split(",");
        int i = Integer.parseInt(splitSta[0]);
        int j = Integer.parseInt(splitEnd[0]);
        if (i>j){
            a += i-j;
        }else {
            a += j-i;
        }
        int b = Integer.parseInt(splitSta[1]);
        int c = Integer.parseInt(splitEnd[1]);
        if (b>c){
            a+= b-c;
        }else {
            a+= c-b;
        }

        return a;
    }

    /**
     * 判断移动距离是否在移速范围内
     * @param i
     * @param j
     * @param x
     * @param y
     * @param roleMoveSpeed
     * @return
     */
    public static boolean move(int i,int j,Integer x,Integer y,String roleMoveSpeed){
        int a = 0;
        if (i>x){
            a += i-x;
        }else{
            a += x-i;
        }

        if (j>y){
            a += j-y;
        }else{
            a += y-j;
        }

        if (a <= Integer.valueOf(roleMoveSpeed)){
            return true;
        }
        return false;
    }

    /**
     * 怪物进行随机移动,对人物接近
     */
    public static Map<String,List<Monster>> randomMove(Map<String,List<Monster>> map,String roleXY,Role role,StringBuilder fighting){
        Map<String,List<Monster>> mapMove = new HashMap<>();
        String keyNew = null;

        Set<Map.Entry<String, List<Monster>>> entries = map.entrySet();
        Iterator<Map.Entry<String, List<Monster>>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<String, List<Monster>> monster = iterator.next();
            String monsterKey = monster.getKey();
            List<Monster> monsterList = monster.getValue();
            for (Monster monster1 : monsterList) { //对每个怪物都进行随机移动
                List<Monster> monsterListNew = new ArrayList<>();
                if(distanceXY(monsterKey,roleXY)<=1){
                    //战斗前状态
                    GameState.fightBeforeStateMap(fighting,role);
                    //进入战斗
                    StringBuilder stringBuilder = combatInformation(fighting, monster1, role);
                    if (stringBuilder.toString().startsWith("角色死亡，游戏结束")){
                        return null;
                    }
                    //战斗结束后状态
                    GameState.fightAfterStateMap(fighting, role);
                    continue;
                }else if (distanceXY(monsterKey,roleXY)<=3){
                    keyNew= moveTarget(monsterKey, roleXY); //对人物进行接近
                }else {
                    keyNew = noIntentionMove(monsterKey); //随机移动
                }
                //查key是否存在，如果存在，获取原来的怪物集合
                if (keyNew!=null&&mapMove.containsKey(keyNew)){
                    monsterListNew = mapMove.get(keyNew);
                }
                monsterListNew.add(monster1);
                mapMove.put(keyNew,monsterListNew);
            }
        }

        return mapMove;
    }

    /**
     * 一对一进入战斗(普通战斗，通过攻速，攻击-防御,战斗技能判定)
     */
    public static StringBuilder combatInformation(StringBuilder fighting,Monster monster,Role role){

        String a = "对";
        String b = "造成";
        String c = "伤害";
        String d = "剩余血量为";
        fighting.append("战斗信息&#10;");

        String nicknameMonster = monster.getNickname();
        Integer attackMonster = monster.getAttack();
        Double attackSpeedMonster = monster.getAttackSpeed();
        Integer defMonster = monster.getDef();
        Integer lifeMonster = monster.getLife();

        String nicknameRole = role.getNickname();
        Integer attackRole = role.getAttack();
        Double attackSpeedRole = role.getAttackSpeed();
        Integer defRole = role.getDef();
        Integer lifeRole = role.getLife();

        int i = attackMonster-defRole;
        if (i<=0) i=1;
        int j = attackRole-defMonster;
        if (j<=0) j=1;

        while (lifeMonster>0&&lifeRole>0){//当一方死亡
            while (attackSpeedMonster>=1||attackSpeedRole>=1){//当一方攻速大于1
                if (attackSpeedMonster>=1){
                    attackSpeedMonster--;
                    lifeRole -= i;
                    fighting.append(nicknameMonster);
                    fighting.append(a);
                    fighting.append(nicknameRole);
                    fighting.append(b);
                    fighting.append(i);
                    fighting.append(c);
                    fighting.append(d);
                    fighting.append(lifeRole);
                    fighting.append("&#10;");
                }
                if (attackSpeedRole>=1){
                    attackSpeedRole--;
                    //人物开始攻击(攻击替换状态发动)
                    Random random = new Random();
                    int i1 = random.nextInt(100) + 1;
                    String s = GameState.fightStateAttackMap(fighting, role, i1, defMonster);
                    if (!s.equals("false")){ //判定成功，攻击替换
                        int i2 = Integer.parseInt(s);
                        lifeMonster -= i2;
                        fighting.append(nicknameRole);
                        fighting.append(a);
                        fighting.append(nicknameMonster);
                        fighting.append(b);
                        fighting.append(i2);
                        fighting.append(c);
                        fighting.append(d);
                        fighting.append(lifeMonster);
                        fighting.append("&#10;");
                    }else {
                        lifeMonster -= j;
                        fighting.append(nicknameRole);
                        fighting.append(a);
                        fighting.append(nicknameMonster);
                        fighting.append(b);
                        fighting.append(j);
                        fighting.append(c);
                        fighting.append(d);
                        fighting.append(lifeMonster);
                        fighting.append("&#10;");
                    }
                }
            }
            attackSpeedMonster += monster.getAttackSpeed();
            attackSpeedRole += role.getAttackSpeed();
        }

        if (lifeMonster<=0){
            role.setLife(lifeRole);
            role.setExp(role.getExp()+monster.getMonsterExp());
            fighting.append(nicknameMonster);
            fighting.append("已死亡&#10;");
        }

        if (lifeRole<=0){
            return new StringBuilder("角色死亡，游戏结束");
        }

        //战斗时状态回合数-1
        Map<String, Integer> fightStateAttackMap = role.getFightStateAttackMap();
        if (fightStateAttackMap!=null){
            Set<String> strings = fightStateAttackMap.keySet();
            Iterator<String> iterator = strings.iterator();
            while (iterator.hasNext()){
                String next = iterator.next();
                GameState.mapState(fightStateAttackMap,next,fighting,role);
            }
        }

        //战斗前状态消散
        Map<String, Integer> fightBeforeStateMap = role.getFightBeforeStateMap();
        if (fightBeforeStateMap!=null){
            Set<String> strings = fightBeforeStateMap.keySet();
            Iterator<String> iterator = strings.iterator();
            while (iterator.hasNext()){
                String next = iterator.next();
                GameState.stateRoleNot(role,next);
            }
        }

        return fighting;
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
     * @param sta
     * @return
     */
    public static String noIntentionMove(String sta){
        String[] splitSta = sta.split(",");
        int i = Integer.parseInt(splitSta[0]);
        int b = Integer.parseInt(splitSta[1]);

        Random random = new Random();
        int i1 = random.nextInt(5) + 1;
        switch (i1){
            case 1:
                if (i<14)
                i++;
                break;
            case 2:
                if (i>0)
                i--;
                break;
            case 3:
                if (b<14)
                b++;
                break;
            case 4:
                if (b>0)
                b--;
                break;
        }
        return i+","+b;
    }

    /**
     * 怪物集合形成地图映射
     * @param map
     * @param game1GameMapLists
     * @return
     */
    public static GameMap<Game1>[][] mapMonster(Map<String,List<Monster>> map,GameMap<Game1>[][] game1GameMapLists){

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
     * 怪物的繁衍和进化
     */
    public static Map<String,List<Monster>> ReproductionAndEvolution(Map<String,List<Monster>> map)throws Exception{
        Set<Map.Entry<String, List<Monster>>> entries = map.entrySet();
        Iterator<Map.Entry<String, List<Monster>>> iterator = entries.iterator();
        while (iterator.hasNext()){
            List<Monster> monsterListNew = new ArrayList<>();
            Map.Entry<String, List<Monster>> monster = iterator.next();
            String monsterKey = monster.getKey();
            List<Monster> monsterList = monster.getValue();
            for (Monster monster1 : monsterList) {
                monsterListNew.add(monster1);
                Monster monsterNew = new Monster();
                BeanUtils.copyProperties(monsterNew,monster1);
                String nickname = monsterNew.getNickname();
                if (nickname.endsWith("+1")){
                    int i = nickname.indexOf("+1");
                    String substring = nickname.substring(0, i);
                    monsterNew.setNickname(substring+"+2");
                    MonsterAttribute.monsterLeaveRange(monsterNew,1);
                }else if(nickname.endsWith("+2")){
                    int i = nickname.indexOf("+2");
                    String substring = nickname.substring(0, i);
                    monsterNew.setNickname(substring+"+3");
                    MonsterAttribute.monsterLeaveRange(monsterNew,1);
                }else if (nickname.endsWith("+3")){
                    int i = nickname.indexOf("+3");
                    String substring = nickname.substring(0, i);
                    monsterNew.setNickname(substring+"+4");
                    MonsterAttribute.monsterLeaveRange(monsterNew,1);
                }else {
                    monsterNew.setNickname(nickname+"+1");
                    MonsterAttribute.monsterLeaveRange(monsterNew,1);

                }
                monsterListNew.add(monsterNew);
            }
            map.put(monsterKey,monsterListNew);
        }
        return map;
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

}
