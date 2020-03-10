package com.fyt.rlife.rlife.bean.room;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.Room;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 16:52
 * @Version 1.0
 */
//刀刃陷阱房
//受到当前生命值30的伤害
public class SwordRoom extends Room {

    @Override
    public void roomTrigger(Role role, Monster monster, StringBuilder stringBuilder) {
        if (role!=null){
            int i = role.getLife() * 7 / 10;
            int j = role.getLife() - i;
            role.setLife(i);
            stringBuilder.append("你受到");
            stringBuilder.append(j);
            stringBuilder.append("点伤害&#10;");
        }else {
            int i = monster.x * 7 / 10;
            int j = monster.x-i;
            monster.x = i;

            stringBuilder.append(monster.getNickname());
            stringBuilder.append("受到");
            stringBuilder.append(j);
            stringBuilder.append("点伤害&#10;");
        }
    }
}
