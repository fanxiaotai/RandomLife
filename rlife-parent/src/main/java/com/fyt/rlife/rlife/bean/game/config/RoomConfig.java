package com.fyt.rlife.rlife.bean.game.config;

import com.fyt.rlife.rlife.bean.game.common.Room;
import com.fyt.rlife.rlife.bean.game.factory.game1.RoomGame1Factory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/3/6 14:55
 * @Version 1.0
 */
@Configuration
public class RoomConfig {

    public static class RoomMap{
        Map<String, Room> roomMap;

        public Map<String, Room> getRoomMap() {
            return roomMap;
        }
    }

    /**
     * 房间数据原型池
     */
    @Bean
    public RoomConfig.RoomMap getRoomGame1Pool(){
        RoomConfig.RoomMap roomMap = new RoomConfig.RoomMap();
        roomMap.roomMap = new HashMap<>();

        Class<RoomGame1Factory> monsterFactoryClass = RoomGame1Factory.class;
        Method[] declaredMethods = monsterFactoryClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            try {
                Room invoke = (Room)declaredMethod.invoke(null);
                roomMap.roomMap.put(invoke.getId(),invoke);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return roomMap;
    }
}
