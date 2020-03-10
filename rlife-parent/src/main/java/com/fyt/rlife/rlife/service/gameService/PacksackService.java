package com.fyt.rlife.rlife.service.gameService;

import com.fyt.rlife.rlife.bean.game.common.Packsack;

/**
 * @Author: fanyitai
 * @Date: 2020/1/8 21:32
 * @Version 1.0
 */
public interface PacksackService {

    Packsack getPacksackByRoleId(String roleId);

    void deleteByRoleIdOrPropId(String packId, String propId);
    void deleteByUserIdOrPropId(String packId, String propId);

    //道具数量减1
    void updateByRoleIdOrPropIdLess(String packId, String propId);
    void updateByUserIdOrPropIdLess(String packId, String propId);

    //道具数量加1
    void updateByRoleIdOrPropIdUP(String packId, String propId);
    void updateByUserIdOrPropIdUP(String packId, String propId);
    void updateByUserIdOrPropIdUP(String packId, String propId, int theNumber);

    //增加道具
    void instProp(String packId, String propId);
    void instPropUserId(String packId, String propId);
    void instPropUserId(String packId, String propId, int theNumber);

    Packsack getUserPacksackByUserId(String userId);
}
