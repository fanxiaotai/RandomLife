package com.fyt.rlife.rlife.controller;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.game.RoleAttribute;
import com.fyt.rlife.rlife.service.RoleService;
import com.fyt.rlife.rlife.service.UserService;
import com.fyt.rlife.rlife.util.RedisUtil;
import com.fyt.rlife.rlife.util.ResultEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * @Author: fanyitai
 * @Date: 2020/1/18 17:24
 * @Version 1.0
 */
@Controller
@Transactional(rollbackFor = Exception.class)
public class UserController {

    @Resource
    UserService userService;
    @Resource
    RoleService roleService;
    @Resource
    RedisUtil redisUtil;


    @RequestMapping(value = "/toUser")
    @RoleRequire(roles = 1)
    public String toUser(HttpServletRequest request, HttpSession session, ModelMap modelMap){
        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return "error";
        }
        String nickname = (String) request.getAttribute("nickname");

        User user = userService.getUserByUserId(memberId);
        if (user == null){
            return "error";
        }

        List<Role> roleListByUserId = roleService.getRoleListByUserId(user.getId());

        modelMap.put("roleListByUserId",roleListByUserId);
        modelMap.put("user",user);
        modelMap.put("nickname",nickname);
        modelMap.put("memberId",memberId);

        return "user/user";
    }

    @RequestMapping(value = "/toUserRole")
    @RoleRequire(roles = 1)
    public String toUserRole(HttpServletRequest request, HttpSession session, ModelMap modelMap,int pageNum,int pageSize){
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
        modelMap.put("pageSize",pageSize);
        modelMap.put("pageNum",pageNum);

        return "user/userRole";
    }

    @RequestMapping(value = "/getUserRole")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<List<Role>> getUserRole(HttpServletRequest request,int pageNum,int pageSize){
        String memberId = (String) request.getAttribute("memberId");

        List<Role> roleListByUserId = roleService.getRoleListByUserId(memberId);
        List<Role> roles = new ArrayList<>();
        int i = pageSize * (pageNum-1);
        while (i < pageSize * pageNum&&i<roleListByUserId.size()){
            roles.add(roleListByUserId.get(i));
            i++;
        }
        int totalsize = roleListByUserId.size()/pageSize+1;
        if (roleListByUserId.size()%pageSize==0){
            totalsize --;
        }

        ResultEntity<List<Role>> listResultEntity = ResultEntity.successWithData(roles);
        listResultEntity.setMessage(totalsize+"");
        return listResultEntity;
    }

    @RequestMapping(value = "/updateDefaultRole")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<List<Role>> updateDefaultRole(HttpServletRequest request,String defaultRoleId,int pageNum){
        String memberId = (String) request.getAttribute("memberId");
        List<Role> roleListByUserId = roleService.updateDefaultRole(defaultRoleId, memberId);
        List<Role> roles = new ArrayList<>();
        int i = 5 * (pageNum-1);
        while (i < 5 * pageNum&&i<roleListByUserId.size()){
            roles.add(roleListByUserId.get(i));
            i++;
        }
        return ResultEntity.successWithData(roles);
    }

    @RequestMapping(value = "/newRole")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> newRole(HttpServletRequest request){
        //随机新角色的属性，生命最大80，魔法最大50，攻击最多10，防御最多5
        String memberId = (String) request.getAttribute("memberId");
        Role role = new Role();
        int a = 0,b=0,c=0,d=0;
        int count6,count4;
        boolean x = true;
        Random random = new Random();
        while (x){
            x = false;
            a = random.nextInt(8)+1;
            b = random.nextInt(5)+1;
            c = random.nextInt(10)+1;
            d = random.nextInt(5)+1;
            if (a+b+c+d<20){
                x = true;
            }
        }
        role.setLifeMax(a*10);
        role.setAttack(b);
        role.setDefense(c);
        role.setMagicMax(d*10);
        role.setFreelyDistributable(0);

        Jedis jedis = redisUtil.getJedis();
        try {
            String s = JSON.toJSONString(role);
            jedis.set("newRole"+memberId,s);
            return ResultEntity.successWithData(role);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        } finally {
            jedis.close();
        }
    }

    @RequestMapping(value = "/createRole")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<List<Role>> createRole(HttpServletRequest request,String roleName,int pageNum){
        String memberId = (String) request.getAttribute("memberId");
        List<Role> roleListByUserId = roleService.getRoleListByUserId(memberId);
        if (roleListByUserId.size()>=10){
            return ResultEntity.failed("你最多只能拥有十个角色");
        }

        Jedis jedis = redisUtil.getJedis();

        Role role = null;
        try {
            String s = jedis.get("newRole" + memberId);
            role = JSON.parseObject(s, Role.class);
            Integer lifeMax = role.getLifeMax();
            Integer attack = role.getAttack();
            Integer defense = role.getDefense();
            Integer magicMax = role.getMagicMax();

            //给新角色添加属性
            RoleAttribute.newRole(role,lifeMax,attack,defense,magicMax,roleName,memberId);
            //保存新角色
            roleService.insertRole(role);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed("创建失败");
        } finally {
            jedis.close();
        }

        if (role!=null){
            roleListByUserId.add(role);
        }

        List<Role> roles = new ArrayList<>();
        int i = 5 * (pageNum-1);
        while (i < 5 * pageNum&&i<roleListByUserId.size()){
            roles.add(roleListByUserId.get(i));
            i++;
        }

        int totalsize = roleListByUserId.size()/5+1;
        if (roleListByUserId.size()%5==0){
            totalsize --;
        }
        ResultEntity<List<Role>> listResultEntity = ResultEntity.successWithData(roles);
        listResultEntity.setMessage(totalsize+"");
        return listResultEntity;
    }

    @RequestMapping(value = "/delRole")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<List<Role>> delRole(HttpServletRequest request,String roleId,int pageNum){
        String memberId = (String) request.getAttribute("memberId");
        List<Role> roleListByUserId = roleService.getRoleListByUserId(memberId);
        boolean a = false;
        Iterator<Role> iterator = roleListByUserId.iterator();
        while (iterator.hasNext()&&!a){
            if (iterator.next().getId().equals(roleId)){
                iterator.remove();
                a = true;
            }
        }
        if (!a){
            return ResultEntity.failed("该用户没有这个角色");
        }

        Jedis jedis = null;
        try {
            roleService.deleteRoleByRoleId(roleId);
            jedis = redisUtil.getJedis();
            jedis.del("game1GameMapLists" + roleId);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();
        }

        List<Role> roles = new ArrayList<>();
        int i = 5 * (pageNum-1);
        while (i < 5 * pageNum&&i<roleListByUserId.size()){
            roles.add(roleListByUserId.get(i));
            i++;
        }

        return ResultEntity.successWithData(roles);
    }
}
