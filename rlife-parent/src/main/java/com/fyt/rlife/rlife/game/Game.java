package com.fyt.rlife.rlife.game;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.config.BoxConfig;
import com.fyt.rlife.rlife.bean.game.config.MonsterConfig;
import com.fyt.rlife.rlife.bean.game.config.RoomConfig;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.util.game.Game1Utils;

/**
 * @Author: fanyitai
 * @Date: 2020/1/4 10:54
 * @Version 1.0
 */
public class Game {

    /**
     * 地图1生成
     */
    public static GameMap<Game1>[][] game1(Role role, int difficulty,MonsterConfig.MonsterGame1Map monsterGame1Map,MonsterConfig.SuffixClassList suffixClassList,
                                    BoxConfig.BoxMap boxMap,RoomConfig.RoomMap roomMap){

        GameMap<Game1>[][] game1GameMaps = Game1Utils.getGameMap();

        //格子生成
        try {
            Game1Utils.GridGeneration(role,game1GameMaps,monsterGame1Map,difficulty,suffixClassList,boxMap,roomMap);
        } catch (Exception e) {
            //LoggerFactory.getLogger(Game.class).error(e.getMessage());
            e.printStackTrace();
        }

        return game1GameMaps;
    }

}
