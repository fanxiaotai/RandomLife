package com.fyt.rlife.rlife.bean.state;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.State;
import lombok.Data;

/**
 * @Author: fanyitai
 * @Date: 2020/3/10 20:15
 * @Version 1.0
 */
//战斗前伤害状态 ,物理伤害
@Data
public class DamageBeforeState extends State {

    Integer damageValue;

    @Override
    public void trigger(Role role, Monster monster, StringBuilder stringBuilder) {
        monster.life -= damageValue;
        stringBuilder.append(monster.getNickname());
        stringBuilder.append("受到");
        stringBuilder.append(damageValue);
        stringBuilder.append("点伤害&#10;");
    }

    @Override
    public void dissipate(Role role, Monster monster, StringBuilder stringBuilder) {

    }
}
