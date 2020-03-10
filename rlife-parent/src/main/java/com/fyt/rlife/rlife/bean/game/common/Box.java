package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * 宝箱类
 * @Author: fanyitai
 * @Date: 2020/1/5 15:14
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Box implements Cloneable, Serializable {
    private String id;
    private Integer boxLeave; //宝箱等级
    private String boxDescribe; //宝箱描述
    //道具/物品集合
    @Transient
    private List<GameProp> propLists;
}
