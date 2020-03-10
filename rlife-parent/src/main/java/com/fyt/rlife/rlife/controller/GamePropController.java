package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.PackVo;
import com.fyt.rlife.rlife.bean.game.common.Packsack;
import com.fyt.rlife.rlife.bean.game.config.SkillConfig;
import com.fyt.rlife.rlife.game.prop.PropUse;
import com.fyt.rlife.rlife.service.RoleService;
import com.fyt.rlife.rlife.service.UserService;
import com.fyt.rlife.rlife.service.gameService.PacksackService;
import com.fyt.rlife.rlife.service.gameService.PropLimitService;
import com.fyt.rlife.rlife.service.gameService.PropService;
import com.fyt.rlife.rlife.service.gameService.RoleSkillService;
import com.fyt.rlife.rlife.util.GameUtils;
import com.fyt.rlife.rlife.util.RedisUtil;
import com.fyt.rlife.rlife.util.ResultEntity;
import com.fyt.rlife.rlife.util.RlifeUtil;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    PropLimitService propLimitService;
    @Resource
    UserService userService;
    @Resource
    PropService propService;
    @Resource
    RoleSkillService roleSkillService;
    @Resource
    SkillConfig.SkillMap skillMap;

    /**
     * 使用游戏道具
     * @param session
     * @param request
     * @param roleId
     * @param attrId
     * @return
     */
    @RequestMapping(value = "/propUse",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<String> propUse(HttpSession session, HttpServletRequest request, String roleId,String attrId){
        if (RlifeUtil.userLoginCheckAndRole(request,roleId,roleService).getResult().equals(ResultEntity.SUCCESS)){
            Role role = (Role) session.getAttribute("role" + roleId);
            Map<String, GameProp> gamePropMap = role.getGamePropMap();
            StringBuilder stringBuilder = new StringBuilder();
            if (gamePropMap!=null&&gamePropMap.containsKey(attrId)){
                GameProp gameProp = gamePropMap.get(attrId);
                ResultEntity<String> stringResultEntity = PropUse.propUse(stringBuilder, role, attrId);
                if (stringResultEntity.getResult().equals(ResultEntity.SUCCESS)){
                    stringBuilder.append("你使用了");
                    stringBuilder.append(gameProp.getPropName());
                    stringBuilder.append("&#10;");
                    PropUse.propUseNumber(gamePropMap,attrId);
                    return ResultEntity.successWithData(stringBuilder.toString());
                }
                return ResultEntity.failed(stringResultEntity.getMessage());
            }
            return ResultEntity.failed("不存在道具");
        }
        return ResultEntity.failed("正在使用道具中");
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
        return null;
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
        return null;
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
        if (RlifeUtil.userLoginCheckAndRole(request,roleId,roleService).getResult().equals(ResultEntity.SUCCESS)){
            Role role = (Role) session.getAttribute("role" + roleId);
            Map<String, GameProp> gamePropMap = role.getGamePropMap();
            if (gamePropMap==null){
                return ResultEntity.failed("获取背包失败");
            }
            PackVo packVo = new PackVo();
            GameUtils.roleProp2Vo(gamePropMap,packVo);
            return ResultEntity.successWithData(packVo.getPropLists());
        }

        return ResultEntity.failed("正在加载背包中");
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
        return null;
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
        return null;
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
        return null;
    }

    /**
     * 更新或者存放道具合成表
     * @param strings
     * @param propId
     */
    public static void redisPropSynthetic(String [] strings,String propId){

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

        return false;
    }

    /**
     * 改名rename
     */
    @RequestMapping(value = "/rename",method = RequestMethod.POST,consumes="application/x-www-form-urlencoded")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<String> rename(HttpServletRequest request,String roleName){
        return null;
    }
}
