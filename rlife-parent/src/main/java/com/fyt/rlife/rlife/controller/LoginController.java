package com.fyt.rlife.rlife.controller;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.service.UserService;
import com.fyt.rlife.rlife.util.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

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

    /**
     * 退出登陆，删除cookie
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/delLogin")
    public String delLogin(HttpServletRequest request, HttpServletResponse response) {

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
        CookieUtil.setCookieNotMicroService(response,"oldToken",jwt,60*60*5,true);

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
        CookieUtil.setCookieNotMicroService(response,"oldToken",jwt,60*60*5,true);
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

        str = userService.insert(user, randomPhone);

        return str;
    }

    /**
     * QQ社交登陆 通过Authorization Code获取Access Token
     * @return 成功信息
     */
    @RequestMapping(value = "/qq_login",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    public String random(String code,String message, ModelMap modelMap,HttpServletResponse response){

        //授权码换取access_token
        String s = "https://graph.qq.com/oauth2.0/token?";
        Map<String, Object> access_map = Oauth2Util.getAccess_token(code,s);
        String access_token = (String)access_map.get("access_token");

        //使用Access Token来获取用户的OpenID
        String callback = HttpclientUtil.doGet("https://graph.qq.com/oauth2.0/me?access_token=" + access_token);
        Map<String,String> map = JSON.parseObject(callback, Map.class);
        if (map!=null){
            String client_id = map.get("client_id");
            String openid = map.get("openid");

            //使用Access Token以及OpenID来访问和修改用户数据
            String user_info = HttpclientUtil.doGet("https://graph.qq.com/user/get_user_info?access_token="+access_token+"&oauth_consumer_key="+client_id+"&openid="+openid);
            Map<String,String> userMap = JSON.parseObject(user_info, Map.class);
            if (userMap!=null){
                String ret = userMap.get("ret");
                if (Integer.parseInt(ret)<0){
                    modelMap.put("errorMessage",userMap.get("msg"));
                    return "error";
                }else {
                    String nickname = userMap.get("nickname");
                    String gender = userMap.get("gender");
                   /* User user = new User();
                    user.setUsername(nickname);
                    user.setSex(gender);
                    user.setUserLeave(1);

                    String str = userService.insertOAuth2(user);
                    if (str.equals("success")){
                        User user1 = userService.loginUser(nickname, null);
                        JwtUtil jwtUtil = new JwtUtil();
                        String jwt = jwtUtil.createJWT(user1.getId(), nickname, 1 + "");
                        CookieUtil.setCookieNotMicroService(response,"oldToken",jwt,60*60*5,true);
                        return "index";
                    }else {
                        System.out.println("社交登陆保存出错");
                        return "error";
                    }*/
                }
            }

        }

        System.out.println("社交登陆出错");
        return "error";
    }



}
