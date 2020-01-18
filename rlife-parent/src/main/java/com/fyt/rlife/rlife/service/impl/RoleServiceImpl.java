package com.fyt.rlife.rlife.service.impl;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.mapper.RoleMapper;
import com.fyt.rlife.rlife.service.RoleService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author: fanyitai
 * @Date: 2020/1/17 10:39
 * @Version 1.0
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    RoleMapper roleMapper;

    @Override
    public void updateSurviveByRoleId(Role role) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("id",role.getId());
        role = new Role();
        role.setSurvive(1);
        roleMapper.updateByExampleSelective(role,example);
    }
}
