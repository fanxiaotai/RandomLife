package com.fyt.rlife.rlife.controller;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.PackVo;
import com.fyt.rlife.rlife.bean.game.common.Packsack;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.prop.PropUse;
import com.fyt.rlife.rlife.service.RoleService;
import com.fyt.rlife.rlife.service.UserService;
import com.fyt.rlife.rlife.service.gameService.*;
import com.fyt.rlife.rlife.util.RedisUtil;
import com.fyt.rlife.rlife.util.ResultEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author: fanyitai
 * @Date: 2020/1/9 17:07
 * @Version 1.0
 */
@Controller
@Transactional(rollbackFor = Exception.class)
public class GamePropController {

    @Resource
    PacksackService packsackService;
    @Resource
    RedisUtil redisUtil;
    @Resource
    RoleService roleService;
    @Resource
    SkillService skillService;
    @Resource
    PropLimitService propLimitService;
    @Resource
    UserService userService;
    @Resource
    PropService propService;
    @Resource
    RoleSkillService roleSkillService;

    /**
     * 使用道具
     * @param session
     * @param request
     * @param roleId
     * @param attrId
     * @return
     */
    @RequestMapping(value = "/propUse",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> propUse(HttpSession session, HttpServletRequest request, String roleId,String attrId){

        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return ResultEntity.failed("系统繁忙，请稍后再试");
        }

        //验证用户是否拥有该角色
        List<Role> roleListByUserId = roleService.getRoleListByUserId(memberId);
        boolean a = false;
        for (Role role : roleListByUserId) {
            if (role.getId().equals(roleId)){
                a = true;
            }
        }
        if (!a){
            return ResultEntity.failed("???作弊封号");
        }

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        String[] attrIds = JSON.parseObject(attrId, String[].class);
        StringBuilder fighting = new StringBuilder();
        Packsack packsackByRoleId = packsackService.getPacksackByRoleId(role.getId());
        List<GameProp> propLists = packsackByRoleId.getPropLists();
        Set<String> stringSet = new HashSet<>();

        for (String id : attrIds) {
            //使用道具
            String s = PropUse.propUse(fighting,role, game1GameMapLists, id);
            if (!s.equals("success")){
                return ResultEntity.failed(s);
            }
        }

        for (GameProp propList : propLists) {
            stringSet.add(propList.getId() + "");
        }
        boolean b = propUpdate(attrIds, packsackService, packsackByRoleId,stringSet,propLists);
        if (!b){
            return ResultEntity.failed("该道具不存在");
        }

        session.setAttribute("game1GameMapLists" + roleId,game1GameMapLists);
        session.setAttribute("role" + roleId,role);
        ResultEntity<Role> roleResultEntity = ResultEntity.successWithData(role);
        roleResultEntity.setMessage(fighting.toString());
        return roleResultEntity;
    }

    /**
     * 使用用户道具
     * @param session
     * @param request
     * @param roleId
     * @param attrId
     * @return
     */
    @RequestMapping(value = "/userPropUse",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<String> userPropUse(HttpSession session, HttpServletRequest request, String roleId,String attrId){
        String[] attrIds = JSON.parseObject(attrId, String[].class);
        if (attrIds==null||attrIds.length==0){
            return ResultEntity.failed("使用失败");
        }
        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return ResultEntity.failed("系统繁忙，请稍后再试");
        }
        Role role = roleService.getRoleByRoleId(roleId);
        Packsack userPacksackByUserId = packsackService.getUserPacksackByUserId(memberId);
        List<GameProp> propLists = userPacksackByUserId.getPropLists();
        Set<String> stringSet = new HashSet<>();

        Map<String,Integer> map = new HashMap<>();
        for (String id : attrIds) {
            //使用道具
            String s = PropUse.userPropUse(role,id);
            if(s.equals("改名")){
                Jedis jedis = null;
                try {
                    jedis = redisUtil.getJedis();
                    jedis.setex("user:"+memberId+"reRoleName",15*60*1000,roleId);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultEntity.failed("系统繁忙，请稍后再试");
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                return ResultEntity.successWithData("改名");
            }else if (s.startsWith("checkPropLimit")){
                map.put("checkPropLimit"+id,Integer.parseInt(s.substring(14)));
            }else if (s.startsWith("skillUP")){
                map.put(s,0);
            }else if (s.equals("该道具不能使用")){
                return ResultEntity.failed(s);
            }
        }

        boolean b = false;
        Set<String> strings = map.keySet();
        for (String string : strings) {
            if (string.startsWith("checkPropLimit")){
                String propId = string.substring(14);
                b = PropUse.checkPropLimit(role.getPropLimitMap(),propId,2,propLimitService,role,roleService);
                if (!b){
                    return ResultEntity.failed("道具使用到上限");
                }
            }else if (string.startsWith("skillUP")){
                String skillId = string.substring(7);
                b = PropUse.checkRoleSkill(role,skillId,roleSkillService);
                if (!b){
                    return ResultEntity.failed("你已学习过该技能");
                }
            }
        }

        for (GameProp propList : propLists) {
            stringSet.add(propList.getId() + "");
        }

        b = userPropUpdate(attrIds, packsackService, userPacksackByUserId,stringSet,propLists);
        if (!b){
            throw new RuntimeException("该道具不存在");
        }
        roleService.updateRoleByKey(role);

        return ResultEntity.successNoData();
    }

    /**
     * 道具合成
     * @param session
     * @param request
     * @param roleId
     * @param attrId
     * @return
     */
    @RequestMapping(value = "/propSynthetic",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<String> propSynthetic(HttpSession session, HttpServletRequest request, String roleId,String attrId){
        String[] attrIds = JSON.parseObject(attrId, String[].class);
        StringBuilder key = new StringBuilder();
        for (String string : attrIds) {
            if (!key.toString().equals("")){
                key.append(",");
            }
            key.append(string);
        }
        Jedis jedis = redisUtil.getJedis();
        String propSynthetic = jedis.get("propSynthetic");
        Map map = JSON.parseObject(propSynthetic, Map.class);
        Packsack packsackByRoleId = packsackService.getPacksackByRoleId(roleId);
        List<GameProp> propLists = packsackByRoleId.getPropLists();
        Set<String> stringSet = new HashSet<>();

        for (GameProp propList : propLists) {
            stringSet.add(propList.getId()+"");
        }
        String s = key.toString();
        if (map.containsKey(s)){
            //道具合成
            //减少道具数量道具
            boolean b = propUpdate(attrIds, packsackService, packsackByRoleId,stringSet,propLists);
            if (!b){
                return ResultEntity.failed("该道具不存在");
            }
            //新增道具
            String propId =(String)map.get(s);
            if (stringSet.contains(propId)){ //道具数量+1
                packsackService.updateByRoleIdOrPropIdUP(packsackByRoleId.getId()+"",propId);
            }else { //新增道具
                packsackService.instProp(packsackByRoleId.getId()+"",propId);
            }

        }else {
            return ResultEntity.failed("合成配方不正确，无法合成");
        }
        return ResultEntity.successNoData();
    }

    /**
     * 获取角色背包
     * @param session
     * @param request
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/getPacksack",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<List<List<GameProp>>> propUse(HttpSession session, HttpServletRequest request, String roleId){
        Packsack packsackByRoleId = packsackService.getPacksackByRoleId(roleId);
        if (packsackByRoleId==null){
            return ResultEntity.failed("背包为空");
        }
        PackVo packVo = new PackVo();
        GameMapController.packsackVO2Po(packsackByRoleId,packVo);
        return ResultEntity.successWithData(packVo.getPropLists());
    }

    /**
     * 获取用户背包
     * @param request
     * @return
     */
    @RequestMapping(value = "/getUserPacksack",method = RequestMethod.POST)
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<List<List<GameProp>>> getUserPacksack(HttpServletRequest request){
        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return ResultEntity.failed("系统繁忙，请稍后再试");
        }
        Packsack packsackByRoleId = packsackService.getUserPacksackByUserId(memberId);
        if (packsackByRoleId==null){
            return ResultEntity.failed("背包为空");
        }
        PackVo packVo = new PackVo();
        GameMapController.packsackVO2Po(packsackByRoleId,packVo);
        return ResultEntity.successWithData(packVo.getPropLists());
    }

    /**
     * 丢弃道具
     * @param session
     * @param request
     * @param roleId
     * @param attrId
     * @return
     */
    @RequestMapping(value = "/propDiscarded",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<String> porpDiscarded(HttpSession session, HttpServletRequest request, String roleId,String attrId){
        String[] attrIds = JSON.parseObject(attrId, String[].class);
        Packsack packsackByRoleId = packsackService.getPacksackByRoleId(roleId);
        List<GameProp> propLists = packsackByRoleId.getPropLists();
        Set<String> stringSet = new HashSet<>();
        for (GameProp propList : propLists) {
            stringSet.add(propList.getId()+"");
        }
        for (String id : attrIds) {
            if (!stringSet.contains(id)){
                return ResultEntity.failed("没有"+id+"号道具");
            }else {
                packsackService.deleteByRoleIdOrPropId(packsackByRoleId.getId()+"",id);
            }
        }
        return ResultEntity.successWithData("删除成功");
    }

    /**
     * 丢弃用户道具
     * @param session
     * @param request
     * @param attrId
     * @return
     */
    @RequestMapping(value = "/userPropDiscarded",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<String> userPropDiscarded(HttpSession session, HttpServletRequest request,String attrId){
        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return ResultEntity.failed("系统繁忙，请稍后再试");
        }
        String[] attrIds = JSON.parseObject(attrId, String[].class);
        Packsack packsackByRoleId = packsackService.getUserPacksackByUserId(memberId);
        List<GameProp> propLists = packsackByRoleId.getPropLists();
        Set<String> stringSet = new HashSet<>();
        for (GameProp propList : propLists) {
            stringSet.add(propList.getId()+"");
        }
        for (String id : attrIds) {
            if (!stringSet.contains(id)){
                return ResultEntity.failed("没有"+id+"号道具");
            }else {
                User user = userService.getUserByUserId(memberId);
                Jedis jedis = null;
                try {
                    jedis = redisUtil.getJedis();
                    GameProp gameProp = JSON.parseObject(jedis.get("GameProp:" + id), GameProp.class);
                    if (gameProp==null){
                        gameProp = propService.getPropByPropId(id);
                    }
                    Integer propPrice = gameProp.getPropPrice();
                    user.setIntegral(user.getIntegral()+propPrice);
                    userService.updateUserIntegral(user);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResultEntity.failed("系统繁忙，请稍后再试");
                } finally {
                    if (jedis != null) {
                        jedis.close();
                    }
                }
                packsackService.deleteByUserIdOrPropId(packsackByRoleId.getId()+"",id);
            }
        }
        return ResultEntity.successWithData("出售成功");
    }

    /**
     * 更新或者存放道具合成表
     * @param strings
     * @param propId
     */
    public static void redisPropSynthetic(String [] strings,String propId){
        RedisUtil redisUtil = new RedisUtil();
        Jedis jedis = redisUtil.getJedis();
        String propSynthetic = jedis.get("propSynthetic");
        Map map = JSON.parseObject(propSynthetic, Map.class);
        StringBuilder key = new StringBuilder();
        for (String string : strings) {
            if (!key.toString().equals("")){
                key.append(",");
            }
            key.append(string);
        }
        map.put(key.toString(),propId);
        String s = JSON.toJSONString(map);
        jedis.set("propSynthetic",s);
        jedis.close();
    }

    /**
     * 道具数量更新
     * @param attrIds
     * @param packsackService
     * @param packsackByRoleId
     * @param stringSet
     * @param propLists
     * @return
     */
    public static boolean propUpdate(String[] attrIds,PacksackService packsackService,Packsack packsackByRoleId,Set<String> stringSet,List<GameProp> propLists){
        for (String id : attrIds) {
            if (!stringSet.contains(id)){
                return false;
            }
        }

        for (String id : attrIds) {
            for (GameProp propList : propLists) {
                if ((propList.getId()+"").equals(id)){
                    Integer theNumber = propList.getTheNumber();
                    theNumber--;
                    if (theNumber <= 0) {
                        packsackService.deleteByRoleIdOrPropId(packsackByRoleId.getId()+"",id);
                    } else {
                        packsackService.updateByRoleIdOrPropIdLess(packsackByRoleId.getId()+"",id);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 多个用户道具数量更新
     * @param attrIds
     * @param packsackService
     * @param packsackByRoleId
     * @param stringSet
     * @param propLists
     * @return
     */
    public boolean userPropUpdate(String[] attrIds,PacksackService packsackService,Packsack packsackByRoleId,Set<String> stringSet,List<GameProp> propLists){
        for (String id : attrIds) {
            if (!stringSet.contains(id)){
                return false;
            }
        }

        for (String id : attrIds) {
            for (GameProp propList : propLists) {
                if ((propList.getId()+"").equals(id)){
                    Integer theNumber = propList.getTheNumber();
                    theNumber--;
                    if (theNumber <= 0) {
                        packsackService.deleteByUserIdOrPropId(packsackByRoleId.getId()+"",id);
                    } else {
                        packsackService.updateByUserIdOrPropIdLess(packsackByRoleId.getId()+"",id);
                    }
                }
            }
        }
        return true;
    }

    /**
     * 单个用户道具数量更新
     * @param packsackService
     * @param packsackByRoleId
     * @param propLists
     * @return
     */
    public boolean userPropUpdate(String propId,PacksackService packsackService,Packsack packsackByRoleId,List<GameProp> propLists){

        for (GameProp propList : propLists) {
            if ((propList.getId()+"").equals(propId)){
                Integer theNumber = propList.getTheNumber();
                theNumber--;
                if (theNumber <= 0) {
                    packsackService.deleteByUserIdOrPropId(packsackByRoleId.getId()+"",propId);
                } else {
                    packsackService.updateByUserIdOrPropIdLess(packsackByRoleId.getId()+"",propId);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * 改名rename
     */
    @RequestMapping(value = "/rename",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<String> rename(HttpServletRequest request,String roleName){
        String memberId = (String) request.getAttribute("memberId");
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String roleId = jedis.get("user:" + memberId + "reRoleName");
            if (roleId==null){
                return ResultEntity.failed("没有需要改名的角色");
            }
            List<Role> roleListByUserId = roleService.getRoleListByUserId(memberId);
            Packsack userPacksackByUserId = packsackService.getUserPacksackByUserId(memberId);
            List<GameProp> propLists = userPacksackByUserId.getPropLists();
            for (Role role : roleListByUserId) {
                if (role.getId().equals(roleId)){
                    role.setNickname(roleName);
                    userPropUpdate("40002",packsackService, userPacksackByUserId,propLists);
                    roleService.updateRoleByKey(role);
                    return ResultEntity.successWithData("修改成功");
                }
            }

            return ResultEntity.failed("你没有该角色");

        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed("系统繁忙，请稍后再试");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }
}
