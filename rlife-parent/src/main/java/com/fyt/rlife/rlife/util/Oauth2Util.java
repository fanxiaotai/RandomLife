package com.fyt.rlife.rlife.util;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;

public class Oauth2Util {

    public static String getCode(){

        //获得授权码
        String s1 = HttpclientUtil.doGet("https://api.weibo.com/oauth2/authorize?client_id=YOUR_CLIENT_ID&response_type=code&redirect_uri=http://passport.gmall.com:8085/vlogin");

        //返回授权码到回调地址
        String s2 = "http://passport.gmall.com:8085/vlogin?code=??";
        return null;
    }

    public static Map<String,Object> getAccess_token(String code,String s3){

        //https://graph.qq.com/oauth2.0/token?grant_type=authorization_code&client_id=[YOUR_APP_ID]&client_secret=[YOUR_APP_Key]&code="+code+"&redirect_uri=[YOUR_REDIRECT_URI]

        /*String s3 = "https://graph.qq.com/oauth2.0/token?";*/
        Map<String,String> paramMap = new HashMap<>();
        paramMap.put("grant_type","authorization_code");
        paramMap.put("client_id","??");
        paramMap.put("client_secret","??");
        paramMap.put("redirect_uri","http://192.168.1.102/qq_login");
        paramMap.put("code",code);
        String access_token_json = HttpclientUtil.doPost(s3, paramMap);
        Map<String,Object> access_map = JSON.parseObject(access_token_json,Map.class);

        return access_map;
    }

    public static Map<String,Object> getUser_info(String access_token,String uid){

        String s4="http://api.weibo.com/2/users/show.json?access_token="+access_token+"&uid="+uid;
        String user_json = HttpclientUtil.doGet(s4);
        Map<String,Object> user_Map = JSON.parseObject(user_json,Map.class);

        return user_Map;
    }
}