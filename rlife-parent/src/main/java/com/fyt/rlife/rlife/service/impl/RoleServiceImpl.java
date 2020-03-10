package com.fyt.rlife.rlife.service.impl;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.RoleSkill;
import com.fyt.rlife.rlife.bean.game.common.PropLimit;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.game.config.SkillConfig;
import com.fyt.rlife.rlife.mapper.PropLimitMapper;
import com.fyt.rlife.rlife.mapper.RoleMapper;
import com.fyt.rlife.rlife.mapper.SkillMapper;
import com.fyt.rlife.rlife.service.RoleService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/1/17 10:39
 * @Version 1.0
 */
@Service
public class RoleServiceImpl implements RoleService {

    @Resource
    RoleMapper roleMapper;
    @Resource
    SkillMapper skillMapper;
    @Resource
    PropLimitMapper propLimitMapper;

    @Override
    public void updateSurviveByRoleId(Role role,int survive) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("id",role.getId());
        role = new Role();
        role.setSurvive(survive);
        roleMapper.updateByExampleSelective(role,example);
    }

    @Override
    public List<Role> getRoleListByUserId(String userId){
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("memberId",userId);
        example.setOrderByClause("role_leave DESC");

        return roleMapper.selectByExample(example);
    }

    @Override
    public Role getRoleByRoleId(String roleId) {
        Role role = roleMapper.selectByPrimaryKey(roleId);
        String id = role.getId();

        //加载已拥有的技能
        List<RoleSkill> roleSkills = skillMapper.selectByRoleId(id);
        Map<String,Skill> skillMap = new HashMap<>();
        if (roleSkills!=null&&roleSkills.size()!=0){
            for (RoleSkill roleSkill : roleSkills) {
                Skill skill = SkillConfig.SkillMap.getSkillById(roleSkill.getId());
                if (skill!=null){
                    skill.setSkillLeave(roleSkill.getSkillLeave());
                    skill.skillConsumeAdjust();
                }else {
                    throw new RuntimeException("获取技能失败");
                }
                skillMap.put(roleSkill.getId(),skill);
            }
        }
        role.setSkillMap(skillMap);

        //加载道具限制
        Example examplePropLimit = new Example(PropLimit.class);
        examplePropLimit.createCriteria().andEqualTo("roleId",roleId);
        List<PropLimit> propLimits = propLimitMapper.selectByExample(examplePropLimit);
        if (propLimits!=null&&propLimits.size()!=0){
            HashMap<String, Integer> objectObjectHashMap = new HashMap<>();
            String k = null;
            Integer v = null;
            for (PropLimit propLimit : propLimits) {
                k = propLimit.getPropId();
                v = propLimit.getTheNumber();
                objectObjectHashMap.put(k,v);
            }
            role.setPropLimitMap(objectObjectHashMap);
        }

        return role;
    }

    @Override
    public void updateRoleByKey(Role role) {
        roleMapper.updateByPrimaryKeySelective(role);
    }

    @Override
    public List<Role> updateDefaultRole(String defaultRoleId,String userId) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("memberId",userId);
        example.setOrderByClause("role_leave DESC");
        List<Role> roles = roleMapper.selectByExample(example);
        for (Role role : roles) {
            String id = role.getId();
            if (role.getDefaultRole()==1){
                role.setDefaultRole(0);
                roleMapper.updateByPrimaryKey(role);
            }
            if (id.equals(defaultRoleId)){
                role.setDefaultRole(1);
                roleMapper.updateByPrimaryKey(role);
            }
        }
        return roles;
    }

    @Override
    public void insertRole(Role role) {
        roleMapper.insert(role);
    }

    @Override
    public void deleteRoleByRoleId(String roleId) {
        roleMapper.deleteByPrimaryKey(roleId);
    }

    @Override
    public Role getDefaultRole(String memberId){
        List<Role> roleListByUserId = getRoleListByUserId(memberId);
        for (Role role : roleListByUserId) {
            if (role.getDefaultRole()==1){
                role = getRoleByRoleId(role.getId());
                return role;
            }
        }
        return null;
    }
}
