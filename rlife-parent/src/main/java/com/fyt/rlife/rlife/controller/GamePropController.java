package com.fyt.rlife.rlife.controller;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.PackVo;
import com.fyt.rlife.rlife.bean.game.common.Packsack;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.prop.PropUse;
import com.fyt.rlife.rlife.service.gameService.PacksackService;
import com.fyt.rlife.rlife.util.RedisUtil;
import com.fyt.rlife.rlife.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author: fanyitai
 * @Date: 2020/1/9 17:07
 * @Version 1.0
 */
@Controller
public class GamePropController {

    @Autowired
    PacksackService packsackService;
    @Autowired
    RedisUtil redisUtil;

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

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        String[] attrIds = JSON.parseObject(attrId, String[].class);
        StringBuilder fighting = new StringBuilder();
        Packsack packsackByRoleId = packsackService.getPacksackByRoleId(role.getId());
        List<GameProp> propLists = packsackByRoleId.getPropLists();
        Set<String> stringSet = new HashSet<>();

        for (GameProp propList : propLists) {
            stringSet.add(propList.getId() + "");
        }
        boolean b = propUpdate(attrIds, packsackService, packsackByRoleId,stringSet,propLists);
        if (!b){
            return ResultEntity.failed("该道具不存在");
        }

        for (String id : attrIds) {
            //使用道具
            String s = PropUse.propUse(fighting,role, game1GameMapLists, id);
            if (!s.equals("success")){
                return ResultEntity.failed(s);
            }
        }

        PackVo packVo = new PackVo();
        GameMapController.packsackVO2Po(packsackByRoleId,packVo);
        session.setAttribute("game1GameMapLists" + roleId,game1GameMapLists);
        session.setAttribute("role" + roleId,role);
        ResultEntity<Role> roleResultEntity = ResultEntity.successWithData(role);
        roleResultEntity.setMessage(fighting.toString());
        return roleResultEntity;
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
     * @param session
     * @param request
     * @param userId
     * @return
     */
    @RequestMapping(value = "/getUserPacksack",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<List<List<GameProp>>> getUserPacksack(HttpSession session, HttpServletRequest request, String userId){
        Packsack packsackByRoleId = packsackService.getUserPacksackByUserId(userId);
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
}
