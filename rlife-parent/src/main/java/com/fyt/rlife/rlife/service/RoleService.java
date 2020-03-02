package com.fyt.rlife.rlife.service;

import com.fyt.rlife.rlife.bean.Role;

import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/1/17 10:38
 * @Version 1.0
 */
public interface RoleService {

    void updateSurviveByRoleId(Role role,int survive);

    List<Role> getRoleListByUserId(String userId);

    Role getRoleByRoleId(String roleId);

    void updateRoleByKey(Role role);

    List<Role> updateDefaultRole(String defaultRoleId,String userId);

    void insertRole(Role role);

    void deleteRoleByRoleId(String roleId);
}
