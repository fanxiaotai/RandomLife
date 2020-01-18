package com.fyt.rlife.rlife.game.prop;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.GameCommon;
import com.fyt.rlife.rlife.game.RoleAttribute;

/**
 * @Author: fanyitai
 * @Date: 2020/1/9 17:33
 * @Version 1.0
 */
public class PropUse {

    /**
     * 使用道具
     * @param role 角色
     * @param gameMap 游戏地图
     * @param propId 道具id
     */
    public static String propUse(StringBuilder fighting,Role role, GameMap<Game1>[][] gameMap,String propId){

        if (propId.startsWith("1")){
            if (propId.startsWith("100")){
                if (propId.equals("10001")){ //道具编号10001,回复20生命
                    RoleAttribute.lifeRange(fighting,role,20);
                }else if (propId.equals("10002")){
                    RoleAttribute.magicRange(fighting,role,10);
                }else if (propId.equals("10003")){
                    RoleAttribute.lifeRange(fighting,role,5);
                }else if (propId.equals("10004")){
                    //RoleAttribute.magicRange(role,5);
                }else if (propId.equals("10005")){
                    RoleAttribute.lifeRange(fighting,role,20);
                }
            }else if (propId.startsWith("101")){

            }else {

            }
            GameCommon.roleUpdate(gameMap,role);
            return "success";
        }else if(propId.startsWith("2")){
            if (propId.startsWith("200")){

            }else if (propId.startsWith("201")){

            }else {

            }
            GameCommon.roleUpdate(gameMap,role);
            return "success";
        }else {
            return "该道具不能单独使用";
        }
    }
}
