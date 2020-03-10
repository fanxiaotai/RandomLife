package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.service.RoleService;
import com.fyt.rlife.rlife.util.ResultEntity;
import com.fyt.rlife.rlife.util.RlifeUtil;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/1/16 21:18
 * @Version 1.0
 */
@RestController
@Transactional(rollbackFor = Exception.class)
public class SkillController {

    @Resource
    RoleService roleService;

    @RequestMapping(value = "/skillUse",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    public ResultEntity<String> skillUse(HttpSession session, HttpServletRequest request,String roleId, String skillId){
        if (RlifeUtil.userLoginCheckAndRole(request,roleId,roleService).getResult().equals(ResultEntity.SUCCESS)){
            Role role = (Role)session.getAttribute("role" + roleId);
            Map<String, Skill> skillMap = role.getSkillMap();
            StringBuilder stringBuilder = new StringBuilder();
            if (skillMap.containsKey(skillId)){
                if (skillMap.get(skillId).skillUse(role, stringBuilder)){
                    return ResultEntity.successWithData(stringBuilder.toString());
                }
                return ResultEntity.failed("魔法值不够");
            }
            return ResultEntity.failed("你没有该技能");
        }
        return ResultEntity.failed("未登陆，技能使用失败");
    }
}
