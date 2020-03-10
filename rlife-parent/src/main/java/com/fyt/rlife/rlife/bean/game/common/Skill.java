package com.fyt.rlife.rlife.bean.game.common;

import com.fyt.rlife.rlife.bean.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: fanyitai
 * @Date: 2020/1/16 19:39
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Skill implements Cloneable, Serializable {

    private String id; //技能id
    private String skillName; //技能名称
    private String skillDescribe; //技能描述
    private int skillLeave; //技能等级
    private int skillInitiative; //是否为主动技能：0是被动，1是主动
    private Integer skillConsume; //技能消耗


    //技能使用效果
    protected abstract boolean use(Role role, StringBuilder stringBuilder);

    //调整技能消耗
    public abstract void skillConsumeAdjust();

    //技能使用对外接口
    public boolean skillUse(Role role, StringBuilder stringBuilder){
        skillConsumeAdjust();
        return use(role, stringBuilder);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
