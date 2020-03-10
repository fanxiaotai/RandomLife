package com.fyt.rlife.rlife.controller;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.Store;
import com.fyt.rlife.rlife.service.UserService;
import com.fyt.rlife.rlife.service.gameService.PacksackService;
import com.fyt.rlife.rlife.service.gameService.PropService;
import com.fyt.rlife.rlife.service.gameService.StoreService;
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
import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/1/22 12:47
 * @Version 1.0
 */
@Controller
@Transactional(rollbackFor = Exception.class)
public class StoreController {

    @Resource
    PropService propService;
    @Resource
    RedisUtil redisUtil;
    @Resource
    StoreService storeService;
    @Resource
    UserService userService;
    @Resource
    PacksackService packsackService;

    @RequestMapping(value = "/toStoreIndex")
    @RoleRequire(roles = 1)
    public String toStoreIndex(HttpServletRequest request, HttpSession session, ModelMap modelMap){
        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return "error";
        }
        String nickname = (String) request.getAttribute("nickname");

        modelMap.put("nickname",nickname);
        modelMap.put("memberId",memberId);

        return "store/storeIndex";
    }

    @RequestMapping(value = "/getStoreByUserId")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Store> getStoreByUserId(HttpServletRequest request,String userId){

        return null;
    }

    @RequestMapping(value = "/buyProp")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<String> buyProp(HttpServletRequest request,String propId,String theNumber){
        return null;
    }

    /**
     * 刷新商店
     */
    public Store refreshStore(String userId,String nowHours){
        return null;
    }
    /**
     * 根据道具id从redis中获取道具内容
     */
    public void setGamePropsByPropId(String propId,int gamePropsIndex,GameProp[] gameProps,Jedis jedis){
        return;
    }

    /**
     * 根据store对象从获取道具对象id数组
     */
    public String[] getStorePropIds(Store store){
        return null;
    }

    /**
     * 向redis中保存商店可能刷新的道具商品
     */
    @RequestMapping(value = "/getPropsByPropIdsToRedis")
    @ResponseBody
    public List<GameProp> getPropsByPropIdsToRedis() {
        String[] propIds = {"40001","40002",
                "40101","40102","40103","40104","40105",
                "40201","40202","40203","40204","40205",
                "40301","40302","40303","40304","40305",
                "40401","40402","40403","40404","40405",
                "41001","41002",
                "42001","42002","42003",
                "42101","42102"};
        //根据Id获取道具信息集合
        List<GameProp> propList = propService.getPropsByPropIds(propIds);

        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            for (GameProp gameProp : propList) {
                jedis.set("GameProp:"+gameProp.getId(), JSON.toJSONString(gameProp));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return propList;
    }
}
