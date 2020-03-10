package com.fyt.rlife.rlife.bean.state;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.State;

/**
 * @Author: fanyitai
 * @Date: 2020/3/10 19:21
 * @Version 1.0
 */
//使怪物中毒，战斗替换状态
public class MonsterPoisoning extends State {


    @Override
    public void trigger(Role role, Monster monster, StringBuilder stringBuilder) {
        int a = getLeave()*3;
        monster.life -= a;
        stringBuilder.append(monster.getNickname());
        stringBuilder.append("受到了");
        stringBuilder.append(a);
        stringBuilder.append("点毒伤害&#10;");
    }

    @Override
    public void dissipate(Role role, Monster monster, StringBuilder stringBuilder) {

    }
}
