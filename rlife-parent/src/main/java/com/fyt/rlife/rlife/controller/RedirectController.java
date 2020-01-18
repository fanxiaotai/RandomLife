package com.fyt.rlife.rlife.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Author: fanyitai
 * @Date: 2019/12/9 19:33
 * @Version 1.0
 */
@Controller
public class RedirectController {


    /**
     * 跳转到用户密码登陆页面
     * @return
     */
    @RequestMapping("/toLogin")
    public String toLogin(String ReturnUrl, ModelMap modelMap){
        modelMap.put("ReturnUrl",ReturnUrl);
        return "login/login";
    }

    /**
     * 跳转到手机登陆页面
     * @return
     */
    @RequestMapping("/toLoginPhone")
    public String toLoginPhone(){
        return "login/loginPhone";
    }

    /**
     * 跳转到注册页面
     * @return
     */
    @RequestMapping("/toReg")
    public String toReg(){
        return "login/reg";
    }

}
