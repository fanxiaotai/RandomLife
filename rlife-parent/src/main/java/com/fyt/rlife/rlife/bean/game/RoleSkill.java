package com.fyt.rlife.rlife.bean.game;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * @Author: fanyitai
 * @Date: 2020/1/24 14:14
 * @Version 1.0
 */
//角色与技能的关联实体类
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleSkill {

    @Id
    private String id;
    private String roleId;
    private String SkillId;
}
