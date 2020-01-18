package com.fyt.rlife.rlife.bean.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 游戏地图，每个类代表一个格子
 * @Author: fanyitai
 * @Date: 2020/1/3 21:15
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GameMap<T>{

    Integer x;
    Integer y;
    T data;
}
