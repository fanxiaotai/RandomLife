package com.fyt.rlife.rlife.service.impl;

import com.fyt.rlife.rlife.mapper.PropLimitMapper;
import com.fyt.rlife.rlife.mapper.RoleMapper;
import com.fyt.rlife.rlife.mapper.SkillMapper;
import com.fyt.rlife.rlife.service.GameMapService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: fanyitai
 * @Date: 2019/12/31 17:04
 * @Version 1.0
 */
@Service
public class GameMapServiceImpl implements GameMapService {

    @Resource
    RoleMapper roleMapper;
    @Resource
    SkillMapper skillMapper;
    @Resource
    PropLimitMapper propLimitMapper;
}
