package com.fyt.rlife.rlife.service.impl;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.mapper.PropLimitMapper;
import com.fyt.rlife.rlife.mapper.RoleMapper;
import com.fyt.rlife.rlife.mapper.SkillMapper;
import com.fyt.rlife.rlife.service.GameMapService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public Role selectRoleByMemberId(String memberId) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("userId",memberId);
        List<Role> roles = roleMapper.selectByExample(example);
        for (Role role : roles) {
            if (role.getDefaultRole()==1){
                String id = role.getId();
                List<Skill> skills = skillMapper.selectByRoleId(id);
                role.setSkillList(skills);
                return role;
            }
        }
        return null;
    }
}
