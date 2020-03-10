package com.fyt.rlife.rlife.bean.state;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.State;
import com.fyt.rlife.rlife.game.RoleAttribute;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 15:08
 * @Version 1.0
 */
//愈合，战斗结束后状态
public class Healing extends State {

    /**
     * 当愈合状态小于5时，每级回复3点，大于等于5后，每级回复生命值1%
     */
    @Override
    public void trigger(Role role, Monster monster, StringBuilder stringBuilder) {
        int leave = getLeave();
        if (leave<5){
            RoleAttribute.lifeRange(stringBuilder,role,leave*3);
        }else {
            RoleAttribute.lifeRange(stringBuilder,role,role.getLifeMax()*leave/100);
        }
    }

    @Override
    public void dissipate(Role role, Monster monster, StringBuilder stringBuilder) {

    }
}
