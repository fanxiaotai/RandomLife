package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.service.gameService.BlackMarketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @Author: fanyitai
 * @Date: 2020/1/25 18:32
 * @Version 1.0
 */
@Controller
public class BlackMarketController {

    @Resource
    BlackMarketService blackMarketService;

    @RequestMapping(value = "/toBuyProp")
    @RoleRequire(roles = 1)
    public String toBuyProp(HttpServletRequest request, HttpSession session, ModelMap modelMap){


        return "blackMarket/buyProp";
    }
}
