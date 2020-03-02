package com.fyt.rlife.rlife.service.gameService.gameImpl;

import com.fyt.rlife.rlife.bean.game.common.Packsack;
import com.fyt.rlife.rlife.mapper.PacksackMapper;
import com.fyt.rlife.rlife.service.gameService.PacksackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: fanyitai
 * @Date: 2020/1/8 21:32
 * @Version 1.0
 */
@Service
public class PacksackServiceImpl implements PacksackService {

    @Autowired
    PacksackMapper packsackMapper;

    @Override
    public Packsack getPacksackByRoleId(String roleId) {

        return packsackMapper.selectPacksackByRoleId(roleId);
    }

    @Override
    public void deleteByRoleIdOrPropId(String packId,String propId) {
        packsackMapper.deleteByRoleIdOrPropId(packId,propId);
    }

    @Override
    public void deleteByUserIdOrPropId(String packId, String propId) {
        packsackMapper.deleteByUserIdOrPropId(packId,propId);
    }

    @Override
    public void updateByRoleIdOrPropIdLess(String packId,String propId) {
        packsackMapper.updateByRoleIdOrPropIdLess(packId,propId);
    }

    @Override
    public void updateByUserIdOrPropIdLess(String packId, String propId) {
        packsackMapper.updateByUserIdOrPropIdLess(packId, propId);
    }

    @Override
    public void updateByRoleIdOrPropIdUP(String packId, String propId) {
        packsackMapper.updateByRoleIdOrPropIdUP(packId,propId);
    }

    @Override
    public void updateByUserIdOrPropIdUP(String packId, String propId) {
        packsackMapper.updateByUserIdOrPropIdUP(packId, propId);
    }

    @Override
    public void updateByUserIdOrPropIdUP(String packId, String propId, int theNumber) {
        packsackMapper.updateByUserIdOrPropIdUPs(packId, propId,theNumber);
    }

    @Override
    public void instProp(String packId, String propId) {
        packsackMapper.instProp(packId,propId);
    }

    @Override
    public void instPropUserId(String packId, String propId) {
        packsackMapper.instPropUserId(packId, propId);
    }

    @Override
    public void instPropUserId(String packId, String propId, int theNumber) {
        packsackMapper.instPropUserIds(packId, propId,theNumber);
    }

    @Override
    public Packsack getUserPacksackByUserId(String userId) {
        Packsack packsack = packsackMapper.selectUserPacksackByUserId(userId);
        packsack.setUserPacksack(true);
        return packsack;
    }
}
