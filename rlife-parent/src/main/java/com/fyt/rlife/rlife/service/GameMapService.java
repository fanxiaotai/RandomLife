package com.fyt.rlife.rlife.service;

import com.fyt.rlife.rlife.bean.Role;

/**
 * @Author: fanyitai
 * @Date: 2019/12/31 17:03
 * @Version 1.0
 */
public interface GameMapService {
    Role selectRoleByMemberId(String memberId);
    
}
