package com.fyt.rlife.rlife.bean.state;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.State;
import com.fyt.rlife.rlife.util.GameUtils;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 14:59
 * @Version 1.0
 */
//火球状态，战斗前状态
public class Fireball extends State {

    /**
     * 当火球状态小于5时，每级造成5点伤害，大于等于5后，每级造成当前生命值2%的伤害
     */
    @Override
    public void trigger(Role role, Monster monster, StringBuilder stringBuilder) {
        int leave = getLeave();
        int a = 0;
        if (leave<5){
            a= leave*5;
        }else {
            a= monster.life*leave*2/100;
        }
        monster.life -= a;
        GameUtils.fightingString("你",monster.getNickname(),a,stringBuilder);
    }

    @Override
    public void dissipate(Role role, Monster monster, StringBuilder stringBuilder) {

    }
}
