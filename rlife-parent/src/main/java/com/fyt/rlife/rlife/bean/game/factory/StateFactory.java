package com.fyt.rlife.rlife.bean.game.factory;

import com.fyt.rlife.rlife.bean.game.common.State;
import com.fyt.rlife.rlife.bean.state.*;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 12:11
 * @Version 1.0
 */
public class StateFactory {

    public static State getBurns(Integer i){
        State burns = new Burns();
        burns.setId("1");
        burns.setName("烧伤");
        burns.setTime(1);
        return burns;
    }

    public static State getPoisoning(Integer i){
        State poisoning = new Poisoning();
        poisoning.setId("2");
        poisoning.setName("中毒");
        poisoning.setTime(2);
        poisoning.setLeave(i);
        return poisoning;
    }

    public static State getFireball(Integer i){
        State state = new Fireball();
        state.setId("3");
        state.setName("火球");
        state.setTime(1);
        state.setLeave(i);
        return state;
    }

    public static State getHealing(Integer i){
        State state = new Healing();
        state.setId("4");
        state.setName("愈合");
        state.setTime(3);
        state.setLeave(i);
        return state;
    }

    public static State getMonsterPoisoning(Integer i){
        State state = new MonsterPoisoning();
        state.setId("5");
        state.setName("中毒");
        state.setTime(1);
        state.setLeave(i);
        return state;
    }

    public static State getDamageBeforeState(Integer i){
        DamageBeforeState state = new DamageBeforeState();
        state.setId("6");
        state.setName("先发制人");
        state.setTime(1);
        state.setDamageValue(i);
        return state;
    }
}
