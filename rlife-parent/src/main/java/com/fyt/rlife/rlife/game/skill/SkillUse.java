package com.fyt.rlife.rlife.game.skill;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.GameCommon;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/1/16 21:30
 * @Version 1.0
 */
public class SkillUse {

    /**
     * 技能使用
     * @param role
     * @param gameMap
     * @param skillId
     * @return
     */
    public static String skillUse(Role role, GameMap<Game1>[][] gameMap, String skillId){
        if (skillId.startsWith("1")){
            if ("101".equals(skillId)){
                if (!magicSkillConsume(role,10)){
                    return "魔法不足";
                }
                if (!fightStateMapPut(role,"连击LV1",1)){
                    return "使用失败";
                }
            }
            if ("102".equals(skillId)){
                if (!magicSkillConsume(role,12)){
                    return "魔法不足";
                }
                if (!fightStateMapPut(role,"连击LV2",1)){
                    return "使用失败";
                }
            }
            if ("103".equals(skillId)){
                if (!magicSkillConsume(role,14)){
                    return "魔法不足";
                }
                if (!fightStateMapPut(role,"连击LV3",1)){
                    return "使用失败";
                }
            }
            if ("104".equals(skillId)){
                if (!magicSkillConsume(role,16)){
                    return "魔法不足";
                }
                if (!fightStateMapPut(role,"连击LV4",1)){
                    return "使用失败";
                }
            }
            if ("105".equals(skillId)){
                if (!magicSkillConsume(role,20)){
                    return "魔法不足";
                }
                if (!fightStateMapPut(role,"连击LV5",1)){
                    return "使用失败";
                }
            }
        }else if (skillId.startsWith("4")){
            if ("401".equals(skillId)){
                if (!magicSkillConsume(role,10)){
                    return "魔法不足";
                }
                if (!fightBeforeStateMapPut(role,"强击LV1",3)){
                    return "使用失败";
                };
            }
        }else if (skillId.startsWith("5")){
            if ("501".equals(skillId)){
                if (!magicSkillConsume(role,5)){
                    return "魔法不足";
                }
                if (!fightBeforeStateMapPut(role,"火球术LV1",1)){
                    return "使用失败";
                };
            }
        }
        GameCommon.roleUpdate(gameMap,role);
        return "success";
    }

    /**
     * 蓝量判断
     * @param role
     * @param consume
     * @return
     */
    public static boolean magicSkillConsume(Role role,int consume){
        Integer magic = role.getMagic();
        if (magic>=consume){
            magic -= consume;
            role.setMagic(magic);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 添加战斗时的状态
     * @param role
     * @param state
     * @param roundNum
     * @return
     */
    public static boolean fightStateMapPut(Role role,String state,int roundNum){
        try {
            Map<String,Integer> map = role.getFightStateAttackMap();
            if (map==null){
                map = new HashMap<>();
            }
            map.put(state,roundNum);
            role.setFightStateAttackMap(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加战斗前的状态
     * @param role
     * @param state
     * @param roundNum
     * @return
     */
    public static boolean fightBeforeStateMapPut(Role role,String state,int roundNum){
        try {
            Map<String,Integer> map = role.getFightBeforeStateMap();
            if (map==null){
                map = new HashMap<>();
            }
            map.put(state,roundNum);
            role.setFightBeforeStateMap(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加战斗后的状态
     * @param role
     * @param state
     * @param roundNum
     * @return
     */
    public static boolean fightAfterStateMapPut(Role role,String state,int roundNum){
        try {
            Map<String,Integer> map = role.getFightAfterStateMap();
            if (map==null){
                map = new HashMap<>();
            }
            map.put(state,roundNum);
            role.setFightAfterStateMap(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加移动后的状态
     */
    public static boolean moveStateMapPut(Role role,String state,int roundNum){
        try {
            Map<String,Integer> map = role.getMoveStateMap();
            if (map==null){
                map = new HashMap<>();
            }
            map.put(state,roundNum);
            role.setMoveStateMap(map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 添加被动技能
     */
    public static boolean passiveSkillUse(Role role, Skill skill){
        int id = Integer.parseInt(skill.getId());
        if (id<1000){
            if (id == 201){
                return fightAfterStateMapPut(role,"痊愈",-10);
            }
            if (id==301){
                return moveStateMapPut(role,"愈合",-10);
            }
        }

        return false;

    }
}
