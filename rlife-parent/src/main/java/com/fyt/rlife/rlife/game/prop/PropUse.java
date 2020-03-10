package com.fyt.rlife.rlife.game.prop;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.PropLimit;
import com.fyt.rlife.rlife.bean.game.common.State;
import com.fyt.rlife.rlife.bean.game.factory.StateFactory;
import com.fyt.rlife.rlife.bean.state.DamageBeforeState;
import com.fyt.rlife.rlife.game.RoleAttribute;
import com.fyt.rlife.rlife.service.RoleService;
import com.fyt.rlife.rlife.service.gameService.PropLimitService;
import com.fyt.rlife.rlife.util.ResultEntity;

import java.util.HashMap;
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
     * @param propId 道具id
     */
    public static ResultEntity<String> propUse(StringBuilder fighting,Role role,String propId){
        int i;
        try {
            i = Integer.parseInt(propId);
        }catch (Exception e){
            return ResultEntity.failed("该道具不存在");
        }
        if (i<100){
            if (i<10){
                switch (i){
                    case 1:
                        role.setLife(role.getLife()-10);
                        role.setFeedDegree(role.getFeedDegree()+30);
                        break;
                    case 2 | 4 | 6:
                        return ResultEntity.failed("该道具不可使用");
                    case 3:
                        Map<String, State> fightStateAttackMap = role.getFightStateAttackMap();
                        if (fightStateAttackMap.containsKey("5")){
                            State state = fightStateAttackMap.get("5");
                            state.setLeave(state.getLeave()+1);
                        }else {
                            fightStateAttackMap.put("5", StateFactory.getMonsterPoisoning(1));
                        }
                        break;
                    case 5:
                        RoleAttribute.lifeRange(fighting,role,20);
                        role.setFeedDegree(role.getFeedDegree()+30);
                        break;
                    case 7:
                        role.setLife(role.getLife()-2);
                        role.setFeedDegree(role.getFeedDegree()+15);
                        break;
                    case 8:
                        role.setMagic(role.getMagic()+20);
                        role.setFeedDegree(role.getFeedDegree()+5);
                        break;
                    case 9:
                        Map<String, State> fightBeforeStateMap = role.getFightBeforeStateMap();
                        if (fightBeforeStateMap.containsKey("6")){
                            DamageBeforeState state = (DamageBeforeState) fightBeforeStateMap.get("6");
                            state.setDamageValue(state.getDamageValue()+10);
                        }else {
                            fightBeforeStateMap.put("6",StateFactory.getDamageBeforeState(10));
                        }
                }
            }
        }

        return ResultEntity.successNoData();
    }

    /**
     * 使用游戏道具，数量-1
     * @param propId 已验证存在map中的key
     */
    public static void propUseNumber(Map<String, GameProp> map,String propId){
        GameProp gameProp = map.get(propId);
        Integer theNumber = gameProp.getTheNumber();
        if (theNumber<=1){
            map.remove(propId);
        }else {
            gameProp.setTheNumber(theNumber-1);
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
}
