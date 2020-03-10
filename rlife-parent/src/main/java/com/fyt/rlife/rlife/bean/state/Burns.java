package com.fyt.rlife.rlife.bean.state;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.State;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 13:37
 * @Version 1.0
 */
//烧伤类，每个战斗回合后怪物受到火焰伤害,伤害为角色攻击力的30%,战斗替换类
public class Burns extends State {

    @Override
    public void trigger(Role role, Monster monster, StringBuilder stringBuilder) {
        int attack = role.getAttack();
        int a =attack*3/10;
        monster.x -= a;
        stringBuilder.append("你对");
        stringBuilder.append(monster.getNickname());
        stringBuilder.append("造成了");
        stringBuilder.append(a);
        stringBuilder.append("点火焰伤害&#10;");
    }

    @Override
    public void dissipate(Role role, Monster monster, StringBuilder stringBuilder) {

    }
}
