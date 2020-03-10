package com.fyt.rlife.rlife.bean.game.factory.game1;

import com.fyt.rlife.rlife.bean.game.common.Box;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.config.GamePropConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/3/6 10:22
 * @Version 1.0
 */
public class BoxGame1Factory {

    public static Box getBox1(){
        Box box = new Box();
        box.setId(1+"");
        box.setBoxLeave(1);
        GameProp propById = GamePropConfig.GamePropMap.getPropById(4 + "");
        List<GameProp> gameProps = new ArrayList<>();
        gameProps.add(propById);
        box.setPropLists(gameProps);
        box.setBoxDescribe("前辈放置的旅行者宝箱,里面或许有着前辈留下的道具");
        return box;
    }

}
