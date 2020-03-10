package com.fyt.rlife.rlife.bean.game.common;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: fanyitai
 * @Date: 2020/3/3 19:27
 * @Version 1.0
 */
//状态类
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class State implements Serializable {
    private String id;
    private String name; //名称
    private Integer time; //持续时长
    private Integer attributeId; //变化属性的编号
    /**
     * 1、血量
     * 2、魔法
     * 3、攻击
     * 4、防御
     */
    private Integer value; //变化属性的值
    private int leave; //等级

    //状态触发
    public abstract void trigger(Role role, Monster monster,StringBuilder stringBuilder);
    //状态消散
    public abstract void dissipate(Role role, Monster monster,StringBuilder stringBuilder);
}
