package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 道具类
 * @Author: fanyitai
 * @Date: 2020/1/5 15:31
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameProp {

    @Id
    private Integer id;
    @Column
    private String propName; //道具名称
    @Column
    private String propDescribe; //道具描述
    @Column
    private Integer theNumber; //道具数量
}
