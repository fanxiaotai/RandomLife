package com.fyt.rlife.rlife.service.gameService.gameImpl;

import com.fyt.rlife.rlife.bean.game.common.PropLimit;
import com.fyt.rlife.rlife.mapper.PropLimitMapper;
import com.fyt.rlife.rlife.service.gameService.PropLimitService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author: fanyitai
 * @Date: 2020/1/23 22:25
 * @Version 1.0
 */
@Service
public class PropLimitServiceImpl implements PropLimitService {

    @Resource
    PropLimitMapper propLimitMapper;
    @Override
    public void insertPropLimitByRoleId(PropLimit propLimit) {
        propLimitMapper.insert(propLimit);
    }

    @Override
    public void updatePropLimitByRoleId(PropLimit propLimit) {
        Example example = new Example(PropLimit.class);
        example.createCriteria().andEqualTo("roleId",propLimit.getRoleId()).andEqualTo("propId",propLimit.getPropId());
        propLimitMapper.updateByExampleSelective(propLimit,example);
    }
}
