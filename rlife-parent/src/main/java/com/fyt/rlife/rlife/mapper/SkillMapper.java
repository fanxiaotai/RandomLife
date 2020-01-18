package com.fyt.rlife.rlife.mapper;

import com.fyt.rlife.rlife.bean.game.common.Skill;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/1/16 19:49
 * @Version 1.0
 */
@Repository
public interface SkillMapper extends Mapper<Skill> {
    List<Skill> selectByRoleId(String roleId);
}
