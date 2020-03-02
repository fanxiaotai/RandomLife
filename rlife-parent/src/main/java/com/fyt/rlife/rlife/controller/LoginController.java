package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.service.UserService;
import com.fyt.rlife.rlife.util.CookieUtil;
import com.fyt.rlife.rlife.util.JwtUtil;
import com.fyt.rlife.rlife.util.ResultEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: fanyitai
 * @Date: 2019/12/9 17:12
 * @Version 1.0
 */
@Controller
@Transactional(rollbackFor = Exception.class)
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/delLogin")
    public String delLogin(HttpServletRequest request, HttpServletResponse response,@RequestParam("phone") String phone, @RequestParam("randomPhone") String randomPhone) {

        CookieUtil.deleteCookie(request,response,"oldToken");

        return "index";
    }

        /**
         * 手机号登陆
         * @param phone 手机号
         * @param randomPhone 验证码
         * @return 成功信息
         */
    @RequestMapping("/loginPhone")
    @ResponseBody
    public ResultEntity<User> loginPhone(HttpServletRequest request, HttpServletResponse response,@RequestParam("phone") String phone, @RequestParam("randomPhone") String randomPhone){
        ResultEntity<User> userResultEntity = userService.loginPhone(phone, randomPhone);
        User data = userResultEntity.getData();
        if (data==null){
            return userResultEntity;
        }
        String username = data.getUsername();
        String id = data.getId();
        Integer userLeave = data.getUserLeave();
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.createJWT(id, username, userLeave + "");
        CookieUtil.setCookie(request,response,"oldToken",jwt,60*60*5,true);

        return userResultEntity;
    }

    /**
     * 用户名密码登陆
     * @param username 用户名
     * @param userpswd 用户密码
     * @return 成功信息
     */
    @RequestMapping("/login")
    @ResponseBody
    public ResultEntity<User> login(HttpServletRequest request, HttpServletResponse response,@RequestParam("username") String username, @RequestParam("userpswd") String userpswd){
        User user = userService.loginUser(username, userpswd);
        if (user==null){
            return ResultEntity.failed("用户名或密码不正确");
        }
        int leave = user.getUserLeave();
        String id = user.getId();
        String username1 = user.getUsername();
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.createJWT(id, username1, leave + "");
        CookieUtil.setCookie(request,response,"oldToken",jwt,60*60*5,true);
        return ResultEntity.successWithData(user);
    }

    /**
     * 获得短信验证码
     * @param phone 手机号
     * @return 成功信息
     */
    @RequestMapping("/reg")
    @ResponseBody
    public String reg(@RequestParam("phone") String phone){
        String str = "";
            String a = userService.random(phone);
            System.out.println(a);
            if (StringUtils.isNotBlank(a)){
                return str = "success";
            }
            return str = "defeated";
    }

    /**
     * 进行注册
     * @param user 要注册的用户信息
     * @param randomPhone 获得的短信验证码
     * @return 成功信息
     */
    @RequestMapping(value = "/random",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @ResponseBody
    public String random(User user, @RequestParam("randomPhone") String randomPhone){
        String str = null;

        str = userService.inster(user, randomPhone);

        return str;
    }

}
