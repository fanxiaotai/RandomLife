package com.fyt.rlife.rlife.bean.game.common;

import lombok.Getter;

import java.io.Serializable;

/**
 * @Author: fanyitai
 * @Date: 2020/3/6 15:21
 * @Version 1.0
 */
//游戏商店类
@Getter
public class GameStore implements Cloneable, Serializable {

    private GameProp[] gameProps = new GameProp[6];
}
