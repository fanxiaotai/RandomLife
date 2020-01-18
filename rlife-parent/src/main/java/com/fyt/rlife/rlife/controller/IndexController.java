package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Word;
import com.fyt.rlife.rlife.mapper.WordMapper;
import com.github.pagehelper.PageHelper;
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

    @RoleRequire(roles = 1)
    @RequestMapping("/index")
    public String index(ModelMap modelMap, HttpSession session, HttpServletRequest request){
        String memberId = (String) request.getAttribute("memberId");
        String nickname = (String) request.getAttribute("nickname");

        PageHelper.startPage(0,3);
        Example example = new Example(Word.class);
        example.setOrderByClause("heat DESC");
        List<Word> words = wordMapper.selectByExample(example);
        modelMap.put("words",words);

        modelMap.put("nickname",nickname);
        modelMap.put("memberId",memberId);

        return "index";
    }
}
