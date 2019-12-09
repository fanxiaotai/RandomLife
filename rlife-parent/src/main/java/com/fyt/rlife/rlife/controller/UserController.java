package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: fanyitai
 * @Date: 2019/12/9 17:12
 * @Version 1.0
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("login")
    public String login(String name,String pswd){
        int a = userService.getUser(name,pswd);
        if (a==1){
            return "index";
        }
        return "index2";
    }
}
