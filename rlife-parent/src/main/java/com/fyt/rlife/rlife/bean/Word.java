package com.fyt.rlife.rlife.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: fanyitai
 * @Date: 2019/12/31 15:45
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word {

    private String id;
    private String wordName; //'世界名称
    private String wordDescribe; // '世界描述
    private String award;//  '奖励',
    private String heat;//'热度',
    private String good;//'点赞数',

}
