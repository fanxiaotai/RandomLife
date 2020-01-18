package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: fanyitai
 * @Date: 2020/1/18 17:24
 * @Version 1.0
 */
@Controller
public class UserController {

    @Resource
    UserService userService;

    @RequestMapping(value = "/toUser")
    @RoleRequire(roles = 1)
    public String skillUse(HttpServletRequest request, HttpSession session, ModelMap modelMap){
        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return "error";
        }
        String nickname = (String) request.getAttribute("nickname");

        User user = userService.getUserByUserId(memberId);
        if (user == null){
            return "error";
        }

        modelMap.put("user",user);
        modelMap.put("nickname",nickname);
        modelMap.put("memberId",memberId);

        return "user/user";
    }
}
