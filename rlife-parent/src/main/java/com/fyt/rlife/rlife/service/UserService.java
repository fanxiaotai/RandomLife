package com.fyt.rlife.rlife.service;

import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.util.ResultEntity;

/**
 * @Author: fanyitai
 * @Date: 2019/12/9 17:14
 * @Version 1.0
 */
public interface UserService {

    /**
     * 验证用户密码是否正确
     * @param name
     * @param pswd
     * @return
     */
    User loginUser(String name, String pswd);

    /**
     * 获取验证码
     * @param phone
     * @return
     */
    String random(String phone);

    /**
     * 用户注册验证
     * @param user
     * @param randomPhone
     * @return
     */
    String insert(User user,String randomPhone);

    /**
     * 验证手机号登陆与手机验证码
     * @param phone
     * @param randomPhone
     * @return
     */
    ResultEntity<User> loginPhone(String phone, String randomPhone);

    /**
     * 根据id查询用户信息
     * @param memberId
     * @return
     */
    User getUserByUserId(String memberId);

    /**
     * 更新用户积分
     * @param integral
     */
    void updateUserIntegral(User user,Integer integral);

    /**
     * 社交登陆
     * @param user
     * @return
     */
    String insertOAuth2(User user);
}
