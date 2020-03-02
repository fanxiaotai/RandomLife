package com.fyt.rlife.rlife.service.gameService;

import com.fyt.rlife.rlife.bean.game.common.PropLimit;

/**
 * @Author: fanyitai
 * @Date: 2020/1/23 22:25
 * @Version 1.0
 */
public interface PropLimitService {
    void insertPropLimitByRoleId(PropLimit propLimit);

    void updatePropLimitByRoleId(PropLimit propLimit);
}
