package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: fanyitai
 * @Date: 2020/1/16 19:39
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {

    private Integer id; //技能id
    private String skillName; //技能名称
    private String skillDescribe; //技能描述
    private String skillLeave; //技能等级
    private String skillInitiative; //是否为主动技能：0是被动，1是主动
    private Integer skillConsume; //技能消耗
}
