package com.fyt.rlife.rlife.bean.game.factory;

import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.skill.FireballRat;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 12:14
 * @Version 1.0
 */
public class SkillFactory {

    public static Skill getFireballRat(Integer i){
        Skill skill = new FireballRat();
        skill.setId("1");
        skill.setSkillName("火球术");
        skill.setSkillDescribe("对敌人造成伤害，并施加烧伤效果");
        skill.setSkillLeave(i);
        skill.setSkillInitiative(1);
        skill.setSkillConsume(5+i);
        return skill;
    }
}
