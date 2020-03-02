package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * @Author: fanyitai
 * @Date: 2020/1/23 22:04
 * @Version 1.0
 */
//道具限制类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PropLimit {
    @Id
    private String id;
    private String roleId;
    private String propId;
    private Integer theNumber;
}
