package com.fyt.rlife.rlife.service;

import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.util.ResultEntity;

/**
 * @Author: fanyitai
 * @Date: 2019/12/9 17:14
 * @Version 1.0
 */
public interface UserService {

    User loginUser(String name, String pswd);

    String random(String phone);

    String inster(User user,String randomPhone);

    ResultEntity<User> loginPhone(String phone, String randomPhone);

    User getUserByUserId(String memberId);
}
