package com.fyt.rlife.rlife.util.game;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.Box;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.GameStore;
import com.fyt.rlife.rlife.bean.game.common.Room;
import com.fyt.rlife.rlife.bean.game.config.BoxConfig;
import com.fyt.rlife.rlife.bean.game.config.GamePropConfig;
import com.fyt.rlife.rlife.bean.game.config.MonsterConfig;
import com.fyt.rlife.rlife.bean.game.config.RoomConfig;
import com.fyt.rlife.rlife.bean.monster.suffix.Layer;
import com.fyt.rlife.rlife.bean.monster.suffix.Suffix;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.gamestate.GameState;
import com.fyt.rlife.rlife.util.GameUtils;
import com.fyt.rlife.rlife.util.RedisUtil;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 20:05
 * @Version 1.0
 */
public class Game1Utils {

    /**
     * 将移动后的角色和怪物放回地图中，并触发相应地图的事件
     */
    public static String Game1NextGameMap(List<Monster> monsterList,Role role,StringBuilder stringBuilder,GameMap<Game1>[][] gameMaps,int x,int y){
        //放置角色
        Game1 data = gameMaps[x][y].getData();
        String info = GameMapTrigger(data,role,stringBuilder);

        //放置怪物
        for (Monster monster : monsterList) {
            Game1 game1 = gameMaps[GameState.monsterX(monster)][GameState.monsterY(monster)].getData();
            GameMapTrigger(game1,monster,stringBuilder);
        }

        return info;
    }

    /**
     * 地图事件触发
     */
    public static String GameMapTrigger(Game1 game1,Object object,StringBuilder stringBuilder){
        Monster monster = null;
        Role role = null;
        if (object instanceof Monster){
            monster = (Monster) object;
            game1.getMonsters().add(monster);
        }
        if (object instanceof Role){
            role =  (Role) object;
            game1.setRole(role);
        }

        Box box = game1.getBox();
        if (box!=null){
            if (role!=null){
                List<GameProp> propList = box.getPropLists();
                Map<String, GameProp> gamePropMap = role.getGamePropMap();
                for (GameProp prop : propList) {
                    gamePropMap.put(prop.getId(),prop);
                    stringBuilder.append("你获得了");
                    stringBuilder.append(prop.getPropName());
                    stringBuilder.append("&#10;");
                }
                game1.setBox(null);
            }
        }

        Room room = game1.getRoom();
        if (room!=null){
            if (role!=null){
                room.roomTrigger(role,null,stringBuilder);
                if (role.getLife()<=0){
                    role.setSurvive(1);
                }
            }else if (monster!=null){
                room.roomTrigger(null,monster,stringBuilder);
            }
            game1.setRoom(null);
        }

        GameStore store = game1.getStore();
        if (store!=null){
            if (role!=null){
                return "商店";
            }
        }

        if(game1.isNextLayer()){
            if (role!=null){
                return "下一层";
            }
        }

        return "success";
    }

    /**
     * 判断怪物剩余血量是否大于0
     */
    public static boolean monsterLifeGt0(Monster monster){
        while (monster instanceof Suffix){
            Suffix suffix = (Suffix) monster;
            monster = suffix.getMonster();
        }
        return monster.life>0;
    }

    /**
     * 游戏1后台数据保存
     */
    public static void saveInfo(HttpServletRequest request, Role role, RedisUtil redisUtil, GameMap<Game1>[][] gameMaps){
        HttpSession session = request.getSession();
        session.setAttribute("game1GameMapLists"+role.getId(),gameMaps);
        session.setAttribute("role" + role.getId(),role);
        session.setMaxInactiveInterval(60*60*5);//设置最大过期时间为2小时
        String game1GameMapStr = JSON.toJSONString(gameMaps);
        Jedis jedis = redisUtil.getJedis();
        jedis.set("game1GameMapLists"+role.getId(),game1GameMapStr);
    }

    //获取地图模型
    public static GameMap<Game1>[][] getGameMap() {
        GameMap<Game1>[][] game1GameMapLists = new GameMap[8][8];
        for (int i = 0;i<8;i++){
            for (int j = 0;j<8;j++){
                Game1 game1 = new Game1();
                GameMap<Game1> objectGameMap = new GameMap<>();
                objectGameMap.setData(game1);
                List<Monster> monsters = new ArrayList<>();
                game1.setMonsters(monsters);
                //生成坐标
                objectGameMap.setX(i);
                objectGameMap.setY(j);
                //地图封装
                objectGameMap.setData(game1);
                game1GameMapLists[i][j] = objectGameMap;
            }
        }
        return game1GameMapLists;
    }

    //生成地图场景
    public static void GridGeneration(Role role, GameMap<Game1>[][] game1GameMapLists, MonsterConfig.MonsterGame1Map monsterGame1Map,
                                      int difficulty, MonsterConfig.SuffixClassList suffixClassList, BoxConfig.BoxMap boxMap, RoomConfig.RoomMap roomMap) throws Exception {
        int layerNumber = role.getLayerNumber();
        //获取8个坐标
        Set<Integer> indexSet = new HashSet<>();
        while (indexSet.size()<10){
            indexSet.add(new Random().nextInt(64));
        }
        List<Integer> indexList = new ArrayList<>(indexSet);
        //生成7只怪物
        for (int i =0;i<7;i++){
            int integer = indexList.get(i);
            int x = integer / 8;
            int y = integer % 8;
            List<Monster> monsters = game1GameMapLists[x][y].getData().getMonsters();
            Monster monster = monsterGenerated(layerNumber,monsterGame1Map,difficulty,suffixClassList,x,y);
            monsters.add(monster);
        }

        //每五层生成一个宝箱2,7
        if (layerNumber%5==2){
            Integer integer = indexList.get(7);
            Game1 data = game1GameMapLists[integer / 8][integer % 8].getData();
            Box box = boxGenerated(layerNumber,difficulty,boxMap);
            data.setBox(box);
        }

        //每五层生成一个房间4,9
        if (layerNumber%5==4){
            Integer integer = indexList.get(7);
            Game1 data = game1GameMapLists[integer / 8][integer % 8].getData();
            Room room = roomGenerated(layerNumber,difficulty,roomMap);
            data.setRoom(room);
        }

        //每十层生成一个商店5
        if (layerNumber%10==5){
            Integer integer = indexList.get(7);
            Game1 data = game1GameMapLists[integer / 8][integer % 8].getData();
            GameStore store = storeGenerated();
            data.setStore(store);
        }

        //每十层生成一个boss10

        //生成角色
        Integer integerRole = indexList.get(8);
        Game1 dataRole = game1GameMapLists[integerRole / 8][integerRole % 8].getData();
        dataRole.setRole(role);

        //生成下一层的入口
        Integer next = indexList.get(9);
        Game1 dataNext = game1GameMapLists[next / 8][next % 8].getData();
        dataNext.setNextLayer(true);
    }

    /**
     * 商店生成策略
     */
    public static GameStore storeGenerated() {
        GameStore gameStore = new GameStore();
        GameProp[] gameProps = gameStore.getGameProps();
        Map<String, GameProp> gamePropMap = GamePropConfig.GamePropMap.getGamePropMap();
        int size = gamePropMap.size();
        for (int i = 0;i<6;i++){
            GameProp propById = GamePropConfig.GamePropMap.getPropById(new Random().nextInt(size)+1+"");
            gameProps[i] = propById;
        }
        return gameStore;
    }

    /**
     * 怪物生成策略
     */
    public static Monster monsterGenerated(int layerNumber, MonsterConfig.MonsterGame1Map monsterGame1Map,int difficulty,MonsterConfig.SuffixClassList suffixClassList,int x,int y) throws Exception {
        Map<String, Monster> monsterMap = monsterGame1Map.getMonsterGame1Map();

        Random random = new Random();
        int a = random.nextInt(100) + 1;
        Monster monster = null;
        if (layerNumber<=10){
            a%=3 ;
            a++;
            Monster monster1 = monsterMap.get(a + "");
            monster = (Monster) monster1.clone();
        }else if (layerNumber<=20){
            a%=5;
            a++;
            monster = (Monster) monsterMap.get(a + "").clone();
        }else if (layerNumber<=30){
            a%=7;
            a++;
            monster = (Monster) monsterMap.get(a + "").clone();
        }else {
            a%=monsterMap.size();
            a++;
            monster = (Monster) monsterMap.get(a + "").clone();
        }
        monster.x = x;
        monster.y = y;
        monster = GameUtils.getSuffixMonster(suffixClassList.getSuffixClassList(), difficulty, layerNumber, monster);
        return new Layer(layerNumber,monster);
    }

    /**
     * 宝箱生成策略
     */
    public static Box boxGenerated(int layerNumber,int difficulty,BoxConfig.BoxMap boxMap){
        return boxMap.getBoxMap().get("1");
    }

    /**
     * 房间生成策略
     */
    public static Room roomGenerated(int layerNumber, int difficulty, RoomConfig.RoomMap roomMap) {
        return roomMap.getRoomMap().get("2");
    }

}
