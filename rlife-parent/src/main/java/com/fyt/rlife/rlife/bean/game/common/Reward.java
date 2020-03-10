package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: fanyitai
 * @Date: 2020/3/7 20:56
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reward {

    private Integer rewardNo;
    /**
     * 奖励对应的编号
     * 1、血量恢复
     * 2、魔法恢复
     * 3、经验
     * 4、金钱
     * 5、随机道具
     * ...
     */

    private String rewardName;
    private String rewardTitle;
}
