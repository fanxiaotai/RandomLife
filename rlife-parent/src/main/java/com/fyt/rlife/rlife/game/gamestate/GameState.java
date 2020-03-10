package com.fyt.rlife.rlife.game.gamestate;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.State;
import com.fyt.rlife.rlife.bean.monster.suffix.Suffix;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/1/17 10:28
 * @Version 1.0
 */
public class GameState {

    /**
     * 返回怪物的x坐标
     */
    public static int monsterX(Monster monster){
        Suffix suffix = null;
        while (monster instanceof Suffix){
            suffix = (Suffix) monster;
            monster = suffix.getMonster();
        }
        return monster.x;
    }

    /**
     * 返回怪物的y坐标
     */
    public static int monsterY(Monster monster){
        Suffix suffix = null;
        while (monster instanceof Suffix){
            suffix = (Suffix) monster;
            monster = suffix.getMonster();
        }
        return monster.y;
    }

    /**
     * 战斗前状态
     */
    public static void fightBeforeStateList(Role role, Monster monster,StringBuilder stringBuilder){
        Map<String, State> fightBeforeStateMap = role.getFightBeforeStateMap();
        if (fightBeforeStateMap==null||fightBeforeStateMap.size()==0){
            return;
        }
        mapIterator(fightBeforeStateMap,role,monster,stringBuilder);
        stateDissipate(fightBeforeStateMap,role,monster,stringBuilder);
    }

    /**
     * 战斗时状态(攻击替换)判定
     */
    public static void fightStateAttackList(Role role,Monster monster,StringBuilder stringBuilder){
        Map<String, State> fightStateAttackMap = role.getFightStateAttackMap();
        if (fightStateAttackMap==null||fightStateAttackMap.size()==0){
            role.roleNormalAttack(monster,stringBuilder);
            return;
        }
        mapIterator(fightStateAttackMap,role,monster,stringBuilder);
    }

    /**
     * 战斗后状态
     */
    public static void fightAfterStateList(Role role,StringBuilder stringBuilder){
        Map<String, State> fightAfterStateMap = role.getFightAfterStateMap();
        if (fightAfterStateMap==null||fightAfterStateMap.size()==0){
            return;
        }
        mapIterator(fightAfterStateMap,role,null,stringBuilder);
        stateDissipate(fightAfterStateMap,role,null,stringBuilder);
    }

    /**
     * 移动状态
     */
    public static void moveStateList(StringBuilder fighting, Role role,Monster monster){
        Map<String, State> moveStateMap = role.getMoveStateMap();
        if (moveStateMap==null||moveStateMap.size()==0){
            return;
        }
        mapIterator(moveStateMap,role,monster,fighting);
        stateDissipate(moveStateMap,role,monster,fighting);
    }

    /**
     * 状态触发
     */
    public static void mapIterator(Map<String,State> map,Role role,Monster monster,StringBuilder stringBuilder){
        Collection<State> list = map.values();
        for (State state : list) {
            state.trigger(role,monster,stringBuilder);
        }
    }

    /**
     * 状态消散
     */
    public static void stateDissipate(Map<String,State> map, Role role, Monster monster, StringBuilder stringBuilder){
        if (map==null){
            return;
        }
        Collection<State> list = map.values();
        Iterator<State> iterator = list.iterator();
        while (iterator.hasNext()){
            State state = iterator.next();
            Integer time = state.getTime();
            time--;
            if (time<=0){
                stringBuilder.append("你失去了");
                stringBuilder.append(state.getName());
                stringBuilder.append("&#10;");
                state.dissipate(role,monster,stringBuilder);
                iterator.remove();
            }
            state.setTime(time);
        }
    }

    /**
     * 添加状态
     */
    public static void addState(State state,Map<String,State> map,StringBuilder stringBuilder){
        if (map==null){
            return;
        }
        if (map.containsKey(state.getId())){
            State state1 = map.get(state.getId());
            state1.setTime(state1.getTime()+state.getTime());
        }else {
            map.put(state.getId(),state);
        }
        stringBuilder.append("你获得了");
        stringBuilder.append(state.getName());
        stringBuilder.append("状态");
        stringBuilder.append("&#10;");
    }
}
