package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.bean.Word;
import com.fyt.rlife.rlife.mapper.WordMapper;
import com.fyt.rlife.rlife.util.CookieUtil;
import com.fyt.rlife.rlife.util.JwtUtil;
import com.github.pagehelper.PageHelper;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2019/12/31 15:42
 * @Version 1.0
 */
@Controller
public class IndexController {

    @Autowired
    WordMapper wordMapper;

    @RequestMapping("/index")
    public String index(ModelMap modelMap, HttpSession session, HttpServletRequest request){

        PageHelper.startPage(0,3);
        Example example = new Example(Word.class);
        example.setOrderByClause("heat DESC");
        List<Word> words = wordMapper.selectByExample(example);
        modelMap.put("words",words);

        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);
        String nickname = null;
        String memberId = null;
        Claims claims = null;
        if (oldToken!=null){
            JwtUtil jwtUtil = new JwtUtil();
            try {
                claims = jwtUtil.parseJWT(oldToken);
            } catch (Exception e) {
                System.out.println("返回首页时jwt解析失败");
                modelMap.put("nickname",nickname);
                modelMap.put("memberId",memberId);
                return "index";
            }
        }
        if (claims!=null){
            memberId = claims.getId();
            nickname = claims.getSubject();
        }
        modelMap.put("nickname",nickname);
        modelMap.put("memberId",memberId);

        return "index";
    }

    @RequestMapping("/")
    public String index2(ModelMap modelMap, HttpSession session, HttpServletRequest request){

        PageHelper.startPage(0,3);
        Example example = new Example(Word.class);
        example.setOrderByClause("heat DESC");
        List<Word> words = wordMapper.selectByExample(example);
        modelMap.put("words",words);

        String oldToken = CookieUtil.getCookieValue(request,"oldToken",true);
        String nickname = null;
        String memberId = null;
        Claims claims = null;
        if (oldToken!=null){
            JwtUtil jwtUtil = new JwtUtil();
            try {
                claims = jwtUtil.parseJWT(oldToken);
            } catch (Exception e) {
                System.out.println("返回首页时jwt解析失败");
                modelMap.put("nickname",nickname);
                modelMap.put("memberId",memberId);
                return "index";
            }
        }
        if (claims!=null){
            memberId = claims.getId();
            nickname = claims.getSubject();
        }
        modelMap.put("nickname",nickname);
        modelMap.put("memberId",memberId);

        return "index";
    }
}
