package com.fyt.rlife.rlife.game.gamestate;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.game.RoleAttribute;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author: fanyitai
 * @Date: 2020/1/17 10:28
 * @Version 1.0
 */
public class GameState {

    /**
     * 战斗前状态
     */
    public static StringBuilder fightBeforeStateMap(StringBuilder fighting, Role role, Monster monster){
        Map<String, Integer> fightBeforeStateMap = role.getFightBeforeStateMap();
        if (fightBeforeStateMap==null){
            return fighting;
        }
        mapIterator(fightBeforeStateMap,fighting,role,monster);
        return fighting;
    }

    /**
     * 战斗时状态(攻击替换)判定
     */
    public static String fightStateAttackMap(StringBuilder fighting, Role role, int random,Integer defMonster){
        Map<String, Integer> fightStateMap = role.getFightStateAttackMap();
        if (fightStateMap==null||fightStateMap.size()==0){
            return "false";
        }
        Set<String> strings = fightStateMap.keySet();
        Iterator<String> iterator = strings.iterator();
        int a = 0;
        while (iterator.hasNext()){
            String stringState = iterator.next();
            //状态比对
            String s = stateRoleFight(fighting, role, stringState, random, defMonster);
            if (!s.equals("false")){
                a += Integer.parseInt(s);
            }
        }
        if (a==0){//没有替换
            return "false";
        }else {
            return a+"";
        }
    }

    /**
     * 战斗后状态
     */
    public static StringBuilder fightAfterStateMap(StringBuilder fighting, Role role,Monster monster){
        Map<String, Integer> fightAfterStateMap = role.getFightAfterStateMap();
        if (fightAfterStateMap==null){
            return fighting;
        }
        mapIterator(fightAfterStateMap,fighting,role,monster);
        return fighting;
    }

    /**
     * 移动状态
     */
    public static StringBuilder moveStateMap(StringBuilder fighting, Role role,Monster monster){
        Map<String, Integer> moveStateMap = role.getMoveStateMap();
        if (moveStateMap==null){
            return fighting;
        }
        mapIterator(moveStateMap,fighting,role,monster);
        return fighting;
    }

    /**
     * 状态map迭代
     */
    public static void mapIterator(Map<String,Integer> map,StringBuilder fighting, Role role,Monster monster){
        Set<String> strings = map.keySet();
        Iterator<String> iterator = strings.iterator();
        while (iterator.hasNext()){
            String stringState = iterator.next();
            //状态比对
            stateRoleNoFight(fighting,role,stringState,monster);
            //回合数减1
            mapState(map,stringState,fighting,role);
        }

    }

    /**
     * 非战斗时状态比对
     */
    public static void stateRoleNoFight(StringBuilder fighting,Role role,String stringState,Monster monster){
        if (stringState.startsWith("痊愈")){
            Integer physical = role.getPhysical();
            RoleAttribute.lifeRange(fighting,role,5+physical); // 回复5+体质点生命
        }else if (stringState.startsWith("愈合")){
            RoleAttribute.lifeRange(fighting,role,1); // 回复1点生命
        }else if (stringState.startsWith("强击")){
            if (stringState.endsWith("LV1")){
                RoleAttribute.attackAmplificationRange(role,20);
            }
        }else if (stringState.startsWith("火球术")){
            if (stringState.endsWith("LV1")){
                int i = role.getMind() * 2 + 10;
                fighting.append("火球术");
                fighting.append("对");
                fighting.append(monster.getNickname());
                fighting.append("造成");
                fighting.append(i);
                fighting.append("点伤害");
                monster.setLife(monster.getLife()-(role.getMind()*2+10));
            }
        }
    }

    /**
     * 战斗时状态比对(攻击替换)
     */
    public static String stateRoleFight(StringBuilder fighting,Role role,String stringState,int random,Integer defMonster){
        Integer attack = role.getAttack();
        Integer power = role.getPower();
        if (stringState.startsWith("连击")){
            if (stringState.endsWith("LV1")){
                if (role.getAgility()*2>random){
                    attack = (int)Math.rint(attack * (0.5+power *0.02));
                    int i = (attack - defMonster) * 2;
                    if (i<1){
                        i=1;
                    }
                    fighting.append("连击发动&#10;");
                    return i+"";
                }else {
                    return "false";
                }
            }else if (stringState.endsWith("LV2")){
                if (role.getAgility()*3>random){
                    attack = (int)Math.rint(attack * (0.5+power *0.03));
                    int i = (attack - defMonster) * 2;
                    if (i<1){
                        i=1;
                    }
                    fighting.append("连击发动&#10;");
                    return i+"";
                }else {
                    return "false";
                }
            }else if (stringState.endsWith("LV3")){
                if (role.getAgility()*4>random){
                    attack = (int)Math.rint(attack * (0.5+power *0.04));
                    int i = (attack - defMonster) * 2;
                    if (i<1){
                        i=1;
                    }
                    fighting.append("连击发动&#10;");
                    return i+"";
                }else {
                    return "false";
                }
            }else if (stringState.endsWith("LV4")){
                if (role.getAgility()*5>random){
                    attack = (int)Math.rint(attack * (0.5+power *0.05));
                    int i = (attack - defMonster) * 2;
                    if (i<1){
                        i=1;
                    }
                    fighting.append("连击发动&#10;");
                    return i+"";
                }else {
                    return "false";
                }
            }else if (stringState.endsWith("LV5")){
                if (role.getAgility()*5>random){
                    attack = (int)Math.rint(attack * (0.5+power *0.05));
                    int i = (attack - defMonster) * 2;
                    if (i<1){
                        i=1;
                    }
                    if (power*5>random){
                        i = i *2;
                    }
                    fighting.append("连击发动&#10;");
                    return i+"";
                }else {
                    return "false";
                }
            }
        }
        return "0";
    }

    /**
     * 回合数减-1
     */
    public static StringBuilder mapState(Map<String,Integer> fightBeforeStateMap,String stringState,StringBuilder fighting,Role role){
        Integer integer = fightBeforeStateMap.get(stringState);
        if (integer==-10){
            return fighting;
        }else {
            integer--;
            if (integer<=0){
                fighting.append(stringState);
                fighting.append("持续时间已结束&#10;");
                stateRoleNot(role,stringState);
                fightBeforeStateMap.remove(stringState);
                return fighting;
            }else {
                fightBeforeStateMap.put(stringState,integer);//将状态回合数-1，然后保存回map
                fighting.append(stringState);
                fighting.append("还剩余");
                fighting.append(integer);
                fighting.append("个回合&#10;");
                return fighting;
            }
        }
    }

    /**
     * 状态消退
     */
    public static void stateRoleNot(Role role,String stringState){
        if (stringState.startsWith("强击")){
            RoleAttribute.attackAmplificationRange(role,-20);
        }
    }
}
