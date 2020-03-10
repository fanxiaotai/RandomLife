package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 道具类
 * @Author: fanyitai
 * @Date: 2020/1/5 15:31
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameProp implements Cloneable, Serializable {

    private String id;
    private String propName; //道具名称
    private String propDescribe; //道具描述
    private Integer theNumber; //道具数量
    private Integer propPrice; //道具价格

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
