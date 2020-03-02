package com.fyt.rlife.rlife.game.prop;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.PropLimit;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.GameCommon;
import com.fyt.rlife.rlife.game.RoleAttribute;
import com.fyt.rlife.rlife.service.RoleService;
import com.fyt.rlife.rlife.service.gameService.PropLimitService;
import com.fyt.rlife.rlife.service.gameService.RoleSkillService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/1/9 17:33
 * @Version 1.0
 */
public class PropUse {

    /**
     * 使用角色道具
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

    /**
     * 使用用户道具
     * @param role 被使用角色
     * @param propId 道具id
     * @return
     */
    public static String userPropUse(Role role, String propId) {
        if (propId.startsWith("4")){
            if (propId.startsWith("400")){
                if (propId.equals("40001")){ //复活角色
                    if (role.getSurvive()==1){
                        role.setSurvive(0);
                    }else {
                        return "该角色未死亡";
                    }
                }else if (propId.equals("40002")){
                    return "改名";
                }
            }else if (propId.startsWith("401")){
                switch (propId) {
                    case "40101":  //角色体质+1
                        RoleAttribute.basePhysicalRange(role, 1);
                        return "checkPropLimit2";
                    case "40102":
                        RoleAttribute.basePhysicalRange(role, 2);
                        return "checkPropLimit2";
                    case "40103":
                        RoleAttribute.basePhysicalRange(role, 3);
                        return "checkPropLimit2";
                    case "40104":
                        RoleAttribute.basePhysicalRange(role, 4);
                        return "checkPropLimit2";
                    case "40105":
                        RoleAttribute.basePhysicalRange(role, 5);
                        return "checkPropLimit2";
                }
            }else if (propId.startsWith("402")){
                switch (propId) {
                    case "40201":  //角色力量+1
                        RoleAttribute.basePowerRange(role, 1);
                        return "checkPropLimit2";
                    case "40202":
                        RoleAttribute.basePowerRange(role, 2);
                        return "checkPropLimit2";
                    case "40203":
                        RoleAttribute.basePowerRange(role, 3);
                        return "checkPropLimit2";
                    case "40204":
                        RoleAttribute.basePowerRange(role, 4);
                        return "checkPropLimit2";
                    case "40205":
                        RoleAttribute.basePowerRange(role, 5);
                        return "checkPropLimit2";
                }
            }else if (propId.startsWith("403")){
                switch (propId) {
                    case "40301":  //角色敏捷+1
                        RoleAttribute.baseAgilityRange(role, 1);
                        return "checkPropLimit2";
                    case "40302":
                        RoleAttribute.baseAgilityRange(role, 2);
                        return "checkPropLimit2";
                    case "40303":
                        RoleAttribute.baseAgilityRange(role, 3);
                        return "checkPropLimit2";
                    case "40304":
                        RoleAttribute.baseAgilityRange(role, 4);
                        return "checkPropLimit2";
                    case "40305":
                        RoleAttribute.baseAgilityRange(role, 5);
                        return "checkPropLimit2";
                }
            }else if (propId.startsWith("404")){
                switch (propId) {
                    case "40401":  //角色精神+1
                        RoleAttribute.baseMindRange(role, 1);
                        return "checkPropLimit2";
                    case "40402":
                        RoleAttribute.baseMindRange(role, 2);
                        return "checkPropLimit2";
                    case "40403":
                        RoleAttribute.baseMindRange(role, 3);
                        return "checkPropLimit2";
                    case "40404":
                        RoleAttribute.baseMindRange(role, 4);
                        return "checkPropLimit2";
                    case "40405":
                        RoleAttribute.baseMindRange(role, 5);
                        return "checkPropLimit2";
                }
            }else if (propId.startsWith("410")){
                switch (propId) {
                    case "41001":  //角色升级卡
                        RoleAttribute.roleLeaveUp(role);
                        return "success";
                    case "41002"://角色转生卡
                        RoleAttribute.roleReincarnation(role);
                        return "success";
                }
            }else if (propId.startsWith("420")){
                switch (propId) {
                    case "42001":  //连击Lv1
                        return "skillUP101";
                    case "42002"://强攻Lv1
                        return "skillUP401";
                    case "42003"://火球术Lv1
                        return "skillUP501";
                    case "42101"://痊愈Lv1
                        return "skillUP201";
                    case "42102"://愈合Lv1
                        return "skillUP301";
                }
            }
            return "success";
        }else if(propId.startsWith("5")){

            return "success";
        }else {
            return "该道具不能使用";
        }
    }

    /**
     * 检查道具限制,如果超出限制返回false，如果没有超出限制，则对限制数量+1，并且保存进数据库
     */
    public static boolean checkPropLimit(Map<String, Integer> propLimitMap, String propId, int propLimitNumber, PropLimitService propLimitService, Role role, RoleService roleService){
        if (propLimitMap!=null){
            Integer integer = propLimitMap.get(propId);
            if (integer==null){
                propLimitMap.put(propId,1);
                PropLimit propLimit = new PropLimit();
                propLimit.setPropId(propId);
                propLimit.setRoleId(role.getId());
                propLimit.setTheNumber(1);
                propLimitService.insertPropLimitByRoleId(propLimit);
                roleService.updateRoleByKey(role);
                return true;
            }
            if (integer>=propLimitNumber){
                return false;
            }else if (integer==0){
                int a = integer+1;
                propLimitMap.put(propId,a);
                PropLimit propLimit = new PropLimit();
                propLimit.setPropId(propId);
                propLimit.setRoleId(role.getId());
                propLimit.setTheNumber(a);
                propLimitService.insertPropLimitByRoleId(propLimit);
                return true;
            }else {
                int a = integer+1;
                propLimitMap.put(propId,a);
                PropLimit propLimit = new PropLimit();
                propLimit.setPropId(propId);
                propLimit.setRoleId(role.getId());
                propLimit.setTheNumber(a);
                propLimitService.updatePropLimitByRoleId(propLimit);
                return true;
            }
        }else {
            propLimitMap = new HashMap<>();
            propLimitMap.put(propId,1);
            role.setPropLimitMap(propLimitMap);
            PropLimit propLimit = new PropLimit();
            propLimit.setPropId(propId);
            propLimit.setRoleId(role.getId());
            propLimit.setTheNumber(1);
            propLimitService.insertPropLimitByRoleId(propLimit);
            roleService.updateRoleByKey(role);
            return true;
        }
    }

    /**
     * 检查角色是否以及学习该技能，如果没有则学习，有则返回false
     */
    public static boolean checkRoleSkill(Role role, String skillId, RoleSkillService roleSkillService){
        List<Skill> skillList = role.getSkillList();
        if (skillList!=null&&skillList.size()!=0){
            for (Skill skill : skillList) {
                if (skill.getId().equals(skillId)){
                    return false;
                }
            }
            roleSkillService.insertSkillByRoleId(role.getId(),skillId);
        }else {
            //直接保存进数据库
            roleSkillService.insertSkillByRoleId(role.getId(),skillId);
        }
        return true;
    }
}
