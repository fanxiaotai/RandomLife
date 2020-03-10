package com.fyt.rlife.rlife.bean.skill;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.game.common.State;
import com.fyt.rlife.rlife.bean.game.factory.StateFactory;
import com.fyt.rlife.rlife.game.gamestate.GameState;
import com.fyt.rlife.rlife.util.GameUtils;

import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 15:14
 * @Version 1.0
 */
//火球术，施加火球状态
public class FireballRat extends Skill {

    /**
     * 添加火球和烧伤状态
     */
    @Override
    public boolean use(Role role,StringBuilder stringBuilder) {
        //判断角色魔法值是否足够使用技能
        boolean b = GameUtils.skillRoleMagic(this, role);
        if (!b){
            return false;
        }

        int skillLeave = getSkillLeave();
        Map<String, State> fightBeforeStateMap = role.getFightBeforeStateMap();
        State fireball = StateFactory.getFireball(skillLeave);
        GameState.addState(fireball,fightBeforeStateMap,stringBuilder);

        State burns = StateFactory.getBurns(null);
        Map<String, State> fightStateAttackMap = role.getFightStateAttackMap();
        GameState.addState(burns,fightStateAttackMap,stringBuilder);
        return true;
    }

    @Override
    public void skillConsumeAdjust() {
        setSkillConsume(5+getSkillLeave());
    }
}
