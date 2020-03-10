package com.fyt.rlife.rlife.service.gameService.gameImpl;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.bean.Store;
import com.fyt.rlife.rlife.mapper.StoreMapper;
import com.fyt.rlife.rlife.service.gameService.StoreService;
import com.fyt.rlife.rlife.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author: fanyitai
 * @Date: 2020/1/22 18:59
 * @Version 1.0
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Resource
    StoreMapper storeMapper;
    @Resource
    RedisUtil redisUtil;

    /**
     * 根据用户id获取商店信息
     */
    @Override
    public Store getStoreByUserId(String memberId) {
        Store store = null;
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            String storeStr = jedis.get("Store:" + memberId);
            if(StringUtils.isNotBlank(storeStr)){
                store = JSON.parseObject(storeStr,Store.class);
            }else {
                Example example = new Example(Store.class);
                example.createCriteria().andEqualTo("userId",memberId);
                store = storeMapper.selectOneByExample(example);
                if (store==null){
                    return new Store();
                }
                jedis.set("Store:" + memberId,JSON.toJSONString(store));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return store;
    }

    @Override
    public void updateStore(Store store,String userId) {
        Example example = new Example(Store.class);
        example.createCriteria().andEqualTo("userId",userId);
        storeMapper.updateByExample(store,example);
        Jedis jedis = null;
        try {
            jedis = redisUtil.getJedis();
            if (store!=null){
                jedis.set("Store:"+userId,JSON.toJSONString(store));
            }
        }finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    @Override
    public void insertStore(String userId) {
        Store store = new Store();
        store.setUserId(userId);
        storeMapper.insert(store);
    }
}
