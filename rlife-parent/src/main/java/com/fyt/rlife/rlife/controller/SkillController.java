package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.skill.SkillUse;
import com.fyt.rlife.rlife.service.gameService.SkillService;
import com.fyt.rlife.rlife.util.ResultEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: fanyitai
 * @Date: 2020/1/16 21:18
 * @Version 1.0
 */
@RestController
@Transactional(rollbackFor = Exception.class)
public class SkillController {

    @Resource
    SkillService skillService;

    @RequestMapping(value = "/skillUse",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    public ResultEntity<String> skillUse(HttpSession session, String roleId, String skillId){
        Role role = (Role) session.getAttribute("role" + roleId);
        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);

        List<Skill> skillList = role.getSkillList();
        Set<String> skillIdSet = new HashSet<>();
        for (Skill skill : skillList) {
            skillIdSet.add(skill.getId());
        }

        if(!skillIdSet.contains(skillId)){
            return ResultEntity.failed("无该技能，使用失败");
        }

        //技能是否使用成功
        String s = SkillUse.skillUse(role, game1GameMapLists, skillId);
        if ("success".equals(s)){
            session.setAttribute("role" + roleId,role);
            session.setAttribute("game1GameMapLists" + roleId,game1GameMapLists);
            return ResultEntity.successWithData("使用成功");
        }else {
            return ResultEntity.failed(s);
        }
    }
}
