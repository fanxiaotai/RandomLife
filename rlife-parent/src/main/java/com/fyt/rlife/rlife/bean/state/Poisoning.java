package com.fyt.rlife.rlife.bean.state;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.State;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 14:28
 * @Version 1.0
 */
//中毒，移动状态
public class Poisoning extends State {

    /**
     * 当中毒状态小于5时，每级造成2点伤害，大于等于5后，每级造成最大生命值1%的伤害
     */
    @Override
    public void trigger(Role role, Monster monster, StringBuilder stringBuilder) {
        Integer life = role.getLife();
        int leave = getLeave();
        if (leave<5){
            life -= leave*2;
            role.setLife(life);
            stringBuilder.append("你受到");
            stringBuilder.append(leave*2);
            stringBuilder.append("毒伤害&#10;");
        }else {
            life -= role.getLifeMax()*leave/100;
            role.setLife(life);
            stringBuilder.append("你受到");
            stringBuilder.append(role.getLifeMax()*leave/100);
            stringBuilder.append("毒伤害&#10;");
        }
    }

    @Override
    public void dissipate(Role role, Monster monster, StringBuilder stringBuilder) {

    }
}
