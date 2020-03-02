package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: fanyitai
 * @Date: 2020/1/25 19:52
 * @Version 1.0
 */
//玩家黑市类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlackMarket {

    private String id; //id
    private String userId; //道具id
    private String propName; //道具名称
    private String propDescribe; //道具描述
    private String propPrice; //上架价格
    private String propNumber; //上架数量
    private Date shelvesTime; //上架时间
}
