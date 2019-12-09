package com.fyt.rlife.rlife.service.impl;

import com.fyt.rlife.rlife.bean.TUser;
import com.fyt.rlife.rlife.mapper.UserMapper;
import com.fyt.rlife.rlife.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: fanyitai
 * @Date: 2019/12/9 17:15
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public int getUser(String name,String pswd) {
        TUser tUser = new TUser();
        tUser.setUsername(name);
        tUser.setUserpswd(pswd);
        TUser tUser1 = userMapper.selectOne(tUser);
        if (tUser1!=null){
            return 1;
        }
        return 0;
    }
}
