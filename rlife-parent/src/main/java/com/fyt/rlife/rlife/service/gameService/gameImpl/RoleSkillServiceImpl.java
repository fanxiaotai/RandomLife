package com.fyt.rlife.rlife.service.gameService.gameImpl;

import com.fyt.rlife.rlife.bean.game.RoleSkill;
import com.fyt.rlife.rlife.mapper.RoleSkillMapper;
import com.fyt.rlife.rlife.service.gameService.RoleSkillService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: fanyitai
 * @Date: 2020/1/24 14:16
 * @Version 1.0
 */
@Service
public class RoleSkillServiceImpl implements RoleSkillService {

    @Resource
    RoleSkillMapper roleSkillMapper;

    @Override
    public void insertSkillByRoleId(String id, String skillId) {
        RoleSkill roleSkill = new RoleSkill();
        roleSkill.setRoleId(id);
        roleSkill.setSkillId(skillId);
        roleSkillMapper.insertSelective(roleSkill);
    }
}
