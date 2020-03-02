package com.fyt.rlife.rlife.service.gameService;

import com.fyt.rlife.rlife.bean.game.common.GameProp;

import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/1/22 17:33
 * @Version 1.0
 */
public interface PropService {
    List<GameProp> getPropsByPropIds(String[] propIds);

    GameProp getPropByPropId(String id);

}
