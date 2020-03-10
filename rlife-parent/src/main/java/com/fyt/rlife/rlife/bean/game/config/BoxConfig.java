package com.fyt.rlife.rlife.bean.game.config;

import com.fyt.rlife.rlife.bean.game.common.Box;
import com.fyt.rlife.rlife.bean.game.factory.game1.BoxGame1Factory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/3/6 10:19
 * @Version 1.0
 */
@Configuration
public class BoxConfig {

    public static class BoxMap{
        Map<String, Box> boxMap;

        public Map<String, Box> getBoxMap() {
            return boxMap;
        }
    }

    /**
     * 宝箱数据原型池
     */
    @Bean
    @DependsOn(value = "getGamePropPool")
    public BoxConfig.BoxMap getBoxGame1Pool(){
        BoxConfig.BoxMap boxMap = new BoxConfig.BoxMap();
        boxMap.boxMap = new HashMap<>();

        Class<BoxGame1Factory> monsterFactoryClass = BoxGame1Factory.class;
        Method[] declaredMethods = monsterFactoryClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            try {
                Box invoke = (Box)declaredMethod.invoke(null);
                boxMap.boxMap.put(invoke.getId(),invoke);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return boxMap;
    }
}
