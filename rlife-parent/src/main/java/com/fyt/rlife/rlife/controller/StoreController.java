package com.fyt.rlife.rlife.controller;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.Packsack;
import com.fyt.rlife.rlife.bean.game.common.Store;
import com.fyt.rlife.rlife.service.UserService;
import com.fyt.rlife.rlife.service.gameService.PacksackService;
import com.fyt.rlife.rlife.service.gameService.PropService;
import com.fyt.rlife.rlife.service.gameService.StoreService;
import com.fyt.rlife.rlife.util.DateUtils;
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
import java.util.Date;
import java.util.List;
import java.util.Random;

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
        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return ResultEntity.failed("请先登陆");
        }
        Store store = storeService.getStoreByUserId(memberId);
        if (store==null){
            return ResultEntity.failed("系统繁忙，请稍后再试");
        }
        Date lastUpdate = store.getLastUpdate();
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String stringDate = DateUtils.getStringDate();
            int nowI = Integer.parseInt(stringDate.substring(11, 13));
            if (lastUpdate!=null){
                String stringDateShort = DateUtils.getStringDateShort();
                String dateToStrLong = DateUtils.dateToStrLong(store.getLastUpdate());
                int i = Integer.parseInt(dateToStrLong.substring(11, 13));
                if (DateUtils.dateToStr(store.getLastUpdate()).equals(stringDateShort)){//日期相等
                    //是否经过刷新时间
                    if (i==0){
                        if (nowI>=18){
                            //刷新商店
                            store = refreshStore(memberId,"18");
                        }else if (nowI>=12){
                            //刷新商店
                            store = refreshStore(memberId,"12");
                        }else if (nowI>=6){
                            //刷新商店
                            store = refreshStore(memberId,"06");
                        }
                    }else if (i==6){
                        if (nowI>=18){
                            //刷新商店
                            store = refreshStore(memberId,"18");
                        }else if (nowI>=12){
                            //刷新商店
                            store = refreshStore(memberId,"12");
                        }
                    }else if (i==12){
                        if (nowI>=18){
                            //刷新商店
                            store = refreshStore(memberId,"18");
                        }
                    }else {
                        //不刷新商店
                        GameProp[] gameProps = new GameProp[6];
                        String[] storePropIds = getStorePropIds(store);
                        for (int b = 0;b<gameProps.length;b++){
                            String storePropId = storePropIds[b];
                            setGamePropsByPropId(storePropId,b,gameProps,jedis);
                        }
                    }

                }else {//日期不相等
                    //刷新商店
                    if (nowI>=18){
                        //刷新商店
                        store = refreshStore(memberId,"18");
                    }else if (nowI>=12){
                        //刷新商店
                        store = refreshStore(memberId,"12");
                    }else if (nowI>=6){
                        //刷新商店
                        store = refreshStore(memberId,"06");
                    }else {
                        store = refreshStore(memberId,"00");
                    }
                }
            }else {
                storeService.insertStore(memberId);
                if (nowI>=18){
                    //刷新商店
                    store = refreshStore(memberId,"18");
                }else if (nowI>=12){
                    //刷新商店
                    store = refreshStore(memberId,"12");
                }else if (nowI>=6){
                    //刷新商店
                    store = refreshStore(memberId,"06");
                }else {
                    store = refreshStore(memberId,"00");
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return ResultEntity.successWithData(store);
    }

    @RequestMapping(value = "/buyProp")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<String> buyProp(HttpServletRequest request,String propId,String theNumber){
        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return ResultEntity.failed("系统繁忙，请稍后再试");
        }
        User user = userService.getUserByUserId(memberId);

        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            GameProp gameProp = JSON.parseObject(jedis.get("GameProp:" + propId),GameProp.class);
            Integer propPrice = gameProp.getPropPrice();
            if (propPrice==0){
                return ResultEntity.failed("该道具无法购买");
            }else{
                int priceSum = propPrice * Integer.parseInt(theNumber);
                Integer integral = user.getIntegral();
                if (integral<priceSum){
                    return ResultEntity.failed("你的轮回积分不足");
                }else {
                    integral -= priceSum;
                    user.setIntegral(integral);
                    userService.updateUserIntegral(user);
                    Packsack userPacksackByUserId = packsackService.getUserPacksackByUserId(memberId);
                    String packsackId = userPacksackByUserId.getId();
                    List<GameProp> propLists = userPacksackByUserId.getPropLists();
                    boolean b = false;
                    for (GameProp propList : propLists) {
                        if (Integer.parseInt(propId)==propList.getId()){
                            b=true;
                        }
                    }
                    if (b){//背包中已有该道具
                        packsackService.updateByUserIdOrPropIdUP(packsackId,propId,Integer.parseInt(theNumber));
                    }else {
                        packsackService.instPropUserId(packsackId,propId,Integer.parseInt(theNumber));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed("系统繁忙，请稍后再试");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return ResultEntity.successWithData("兑换成功");
    }

    /**
     * 刷新商店
     */
    public Store refreshStore(String userId,String nowHours){
        Store store = new Store();
        Jedis jedis = redisUtil.getJedis();
        Random random = new Random();
        GameProp[] gameProps = new GameProp[6];
        for (int a = 0;a<6;a++){
            int i = random.nextInt(100) + 1;
            int j = random.nextInt(100) + 1;
            int x = random.nextInt(4) + 1;
            GameProp gameProp = new GameProp();
            String propStr = null;
            if (i<50){
                if (j < 50){
                    if (x==1){
                        propStr = jedis.get("GameProp:40101");
                    }else if (x==2){
                        propStr = jedis.get("GameProp:40201");
                    }else if (x==3){
                        propStr = jedis.get("GameProp:40301");
                    }else if (x==4){
                        propStr = jedis.get("GameProp:40401");
                    }
                }else if (j<75){
                    if (x==1){
                        propStr = jedis.get("GameProp:40102");
                    }else if (x==2){
                        propStr = jedis.get("GameProp:40202");
                    }else if (x==3){
                        propStr = jedis.get("GameProp:40302");
                    }else if (x==4){
                        propStr = jedis.get("GameProp:40402");
                    }
                }else if (j<88){
                    if (x==1){
                        propStr = jedis.get("GameProp:40103");
                    }else if (x==2){
                        propStr = jedis.get("GameProp:40203");
                    }else if (x==3){
                        propStr = jedis.get("GameProp:40303");
                    }else if (x==4){
                        propStr = jedis.get("GameProp:40403");
                    }
                }else if (j<96){
                    if (x==1){
                        propStr = jedis.get("GameProp:40104");
                    }else if (x==2){
                        propStr = jedis.get("GameProp:40204");
                    }else if (x==3){
                        propStr = jedis.get("GameProp:40304");
                    }else if (x==4){
                        propStr = jedis.get("GameProp:40404");
                    }
                }else if (j<=100){
                    if (x==1){
                        propStr = jedis.get("GameProp:40105");
                    }else if (x==2){
                        propStr = jedis.get("GameProp:40205");
                    }else if (x==3){
                        propStr = jedis.get("GameProp:40305");
                    }else if (x==4){
                        propStr = jedis.get("GameProp:40405");
                    }
                }
            }else if (i<75){
                if (x==1||x==2){
                    propStr = jedis.get("GameProp:40001");
                }else if (x==3||x==4){
                    propStr = jedis.get("GameProp:40002");
                }
            }else if (i<88){
                if (j<33){
                    propStr = jedis.get("GameProp:42001");
                }else if (j<66){
                    propStr = jedis.get("GameProp:42002");
                }else {
                    propStr = jedis.get("GameProp:42003");
                }
            }else if (i<96){
                if (x==1||x==2){
                    propStr = jedis.get("GameProp:42101");
                }else if (x==3||x==4){
                    propStr = jedis.get("GameProp:42102");
                }
            }else if (i<=100){
                if (x==1||x==2){
                    propStr = jedis.get("GameProp:41001");
                }else if (x==3||x==4){
                    propStr = jedis.get("GameProp:41002");
                }
            }

            gameProp = JSON.parseObject(propStr,GameProp.class);
            gameProps[a] = gameProp;
            if (a==0){
                store.setPropId1(gameProp.getId()+"");
            }else if (a==1){
                store.setPropId2(gameProp.getId()+"");
            }else if (a==2){
                store.setPropId3(gameProp.getId()+"");
            }else if (a==3){
                store.setPropId4(gameProp.getId()+"");
            }else if (a==4){
                store.setPropId5(gameProp.getId()+"");
            }else if (a==5){
                store.setPropId6(gameProp.getId()+"");
            }
        }
        store.setUserId(userId);
        store.setGameProps(gameProps);
        String stringDate = DateUtils.getStringDate();
        String substring1 = stringDate.substring(0, 11);
        String substring2 = ":00:00";
        stringDate = substring1+nowHours+substring2;
        store.setLastUpdate(DateUtils.strToDateLong(stringDate));
        storeService.updateStore(store,userId);
        return store;
    }
    /**
     * 根据道具id从redis中获取道具内容
     */
    public void setGamePropsByPropId(String propId,int gamePropsIndex,GameProp[] gameProps,Jedis jedis){
        GameProp gameProp = JSON.parseObject(jedis.get("GameProp:" + propId), GameProp.class);
        gameProps[gamePropsIndex] = gameProp;
    }

    /**
     * 根据store对象从获取道具对象id数组
     */
    public String[] getStorePropIds(Store store){
        String[] propIds = new String[6];
        propIds[0] = store.getPropId1();
        propIds[1] = store.getPropId2();
        propIds[2] = store.getPropId3();
        propIds[3] = store.getPropId4();
        propIds[4] = store.getPropId5();
        propIds[5] = store.getPropId6();
        return propIds;
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
