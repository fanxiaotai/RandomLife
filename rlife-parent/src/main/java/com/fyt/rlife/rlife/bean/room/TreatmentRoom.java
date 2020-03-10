package com.fyt.rlife.rlife.bean.room;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.Room;
import com.fyt.rlife.rlife.game.RoleAttribute;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 17:01
 * @Version 1.0
 */
//回复最大生命值的30%
public class TreatmentRoom extends Room {

    @Override
    public void roomTrigger(Role role, Monster monster, StringBuilder stringBuilder) {
        if (role!=null){
            int lifeMax = role.getLifeMax()*3/10;
            RoleAttribute.lifeRange(stringBuilder,role,lifeMax);
        }else {
            int i = monster.x*3/10;
            stringBuilder.append(monster.getNickname());
            stringBuilder.append("的最大生命增加了");
            stringBuilder.append(i);
            monster.x += i;
        }
    }
}
