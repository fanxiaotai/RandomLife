package com.fyt.rlife.rlife.bean.game.config;

import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.factory.game1.PropAllGame1Factory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 11:34
 * @Version 1.0
 */
@Configuration
public class GamePropConfig {

    public static class GamePropMap{
        private static Map<String, GameProp> gamePropMap;

        public static Map<String, GameProp> getGamePropMap() {
            return gamePropMap;
        }

        public static GameProp getPropById(String id){
            return gamePropMap.get(id);
        }
    }

    /**
     * 游戏道具原型池
     */
    @Bean
    public GamePropConfig.GamePropMap getGamePropPool(){
        GamePropMap gamePropMap = new GamePropMap();
        GamePropConfig.GamePropMap.gamePropMap = new HashMap<>();

        Class<PropAllGame1Factory> propFactoryClass = PropAllGame1Factory.class;
        Method[] declaredMethods = propFactoryClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            try {
                GameProp invoke = (GameProp)declaredMethod.invoke(null);
                GamePropConfig.GamePropMap.gamePropMap.put(invoke.getId(),invoke);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return gamePropMap;
    }

}
