package com.fyt.rlife.rlife.game;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.vo.GameMap;

/**
 * @Author: fanyitai
 * @Date: 2020/1/9 17:38
 * @Version 1.0
 */
public class GameCommon {

    /**
     * 对地图的角色进行刷新
     */
    public static void roleUpdate(GameMap<Game1>[][] game1GameMapLists, Role role){
        if (game1GameMapLists!=null){
            for (int x = 0;x<game1GameMapLists.length;x++){
                for (int y = 0;y<game1GameMapLists[x].length;y++){
                    //找到角色所在位置
                    if (game1GameMapLists[x][y].getData().getRole()!=null){
                        game1GameMapLists[x][y].getData().setRole(role);
                    }
                }
            }
        }
    }
}
