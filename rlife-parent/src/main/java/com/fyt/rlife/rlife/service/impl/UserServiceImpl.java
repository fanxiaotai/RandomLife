package com.fyt.rlife.rlife.service.impl;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.mapper.UserMapper;
import com.fyt.rlife.rlife.service.UserService;
import com.fyt.rlife.rlife.util.MD5Util;
import com.fyt.rlife.rlife.util.RedisUtil;
import com.fyt.rlife.rlife.util.ResultEntity;
import com.fyt.rlife.rlife.util.RlifeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;

/**
 * @Author: fanyitai
 * @Date: 2019/12/9 17:15
 * @Version 1.0
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    /**
     * 查询用户名密码是否正确
     * @param name 用户名
     * @param pswd 密码
     * @return 用户对象
     */
    @Override
    public User loginUser(String name, String pswd) {
        //MD5加密
        try {
            String ps = MD5Util.digest(pswd);

            User user = new User();
            user.setUsername(name);
            user.setUserpswd(ps);
            User user1 = userMapper.selectOne(user);

            user1.setLastDate(new Date());
            userMapper.updateByPrimaryKey(user1);
            return user1;
        } catch (Exception e) {
            System.out.println("查询登陆异常");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 发送短信验证码
     * @param phone 要发送验证码的手机号
     * @return 成功信息
     */
    @Override
    public String random(String phone) {
        Jedis jedis = null;
        String code = "";

        try {
            code = RlifeUtil.randomCode(4);
            //用阿里发短信
            RlifeUtil.sendShortMessage("阿里云市场中调用API时识别身份的appCode",code,phone);
            jedis = redisUtil.getJedis();
            jedis.setex("random:"+phone,15*60,code);
            return code;
        } catch (Exception e) {
            System.out.println("获取验证码异常");
            e.printStackTrace();
            return null;
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 对要注册的用户信息进行验证和保存
     * @param user 要注册的用户信息
     * @param randomPhone 手机验证码
     * @return 成功信息
     */
    @Override
    public String insert(User user,String randomPhone) {
        Jedis jedis = null;
        String str = "";

        try {
            jedis = redisUtil.getJedis();
            String random = jedis.get("random:" + user.getPhone());
            if (random!=null&&random.equals(randomPhone)){
                Example examplePhone = new Example(User.class);
                examplePhone.createCriteria().andEqualTo("phone",user.getPhone());
                User userPhone = userMapper.selectOneByExample(examplePhone);
                if (userPhone==null){
                    Example exampleName = new Example(User.class);
                    exampleName.createCriteria().andEqualTo("username",user.getUsername());
                    User userName = userMapper.selectOneByExample(exampleName);
                    if (userName==null){
                        user.setRegDate(new Date());
                        user.setUserpswd(MD5Util.digest(user.getUserpswd()));
                        userMapper.insert(user);
                        str = "success";
                    }else {
                        str = "用户名已存在";
                    }
                }else {
                    str = "手机号已经注册";
                }
            }else {
                str = "验证码错误";
            }
            return str;
        } catch (Exception e) {
            System.out.println("获取验证码异常");
            e.printStackTrace();
            return "获取验证码异常，请稍后再重试";
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public ResultEntity<User> loginPhone(String phone, String randomPhone) {
        Jedis jedis = null;
        User user = null;
        try {
            jedis = redisUtil.getJedis();
            String random = jedis.get("random:" + phone);
            if (random!=null&&random.equals(randomPhone)){
                Example example = new Example(User.class);
                example.createCriteria().andEqualTo("phone",phone);
                user = userMapper.selectOneByExample(example);
                if (user==null){
                    return ResultEntity.failed("该手机号未注册");
                }
            }else {
                System.out.println("验证码不正确");
                return ResultEntity.failed("验证码不正确");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed("获取验证码失败");
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }

        user.setLastDate(new Date());
        userMapper.updateByPrimaryKey(user);

        return ResultEntity.successWithData(user);
    }

    /**
     * 根据用户id查询用户信息
     * @param memberId
     * @return
     */
    @Override
    public User getUserByUserId(String memberId) {

        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String userStr = jedis.get("user:" + memberId);
            if(StringUtils.isNotBlank(userStr)){
                return JSON.parseObject(userStr, User.class);
            }else {
                Example example = new Example(User.class);
                example.createCriteria().andEqualTo("id",memberId);
                User user = userMapper.selectOneByExample(example);
                jedis.setex("user:" + memberId,15*60,JSON.toJSONString(user));
                return user;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void updateUserIntegral(User user,Integer integral) {

        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            User userNew = new User();
            userNew.setIntegral(user.getIntegral()+integral);
            userNew.setId(user.getId());
            userMapper.updateByPrimaryKeySelective(userNew);
            jedis.del("user:" + user.getId());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public String insertOAuth2(User user) {
        int insert = userMapper.insert(user);
        if (insert==1){
            return "success";
        }else {
            return "fail";
        }
    }
}
