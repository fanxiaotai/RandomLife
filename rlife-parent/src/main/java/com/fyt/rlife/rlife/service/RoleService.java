package com.fyt.rlife.rlife.service;

import com.fyt.rlife.rlife.bean.Role;

import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/1/17 10:38
 * @Version 1.0
 */
public interface RoleService {

    /**
     * 修改角色存活状态
     * @param role
     * @param survive
     */
    void updateSurviveByRoleId(Role role, int survive);

    /**
     * 根据用户id获取用户的所有角色
     * @return
     */
    List<Role> getRoleListByUserId(String memberId);

    /**
     * 根据角色id查询角色信息
     * @param roleId
     * @return
     */
    Role getRoleByRoleId(String roleId);

    /**
     * 更新角色
     * @param role
     */
    void updateRoleByKey(Role role);

    /**
     * 修改默认角色
     * @param defaultRoleId
     * @param userId
     * @return
     */
    List<Role> updateDefaultRole(String defaultRoleId, String userId);

    /**
     * 添加角色
     * @param role
     */
    void insertRole(Role role);

    /**
     * 根据角色id删除角色
     * @param roleId
     */
    void deleteRoleByRoleId(String roleId);

    /**
     * 查询用户的默认角色
     * @param memberId
     * @return
     */
    Role getDefaultRole(String memberId);
}
