package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Transient;
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
public class Box {

    @Id
    private Integer id;
    private Integer boxLeave; //宝箱等级
    //道具/物品集合

    @Transient
    private List<GameProp> propLists;
}
