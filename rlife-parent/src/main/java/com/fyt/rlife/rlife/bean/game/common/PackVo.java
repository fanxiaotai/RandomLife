package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/1/9 15:38
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PackVo {

    private List<List<GameProp>> propLists;

}
