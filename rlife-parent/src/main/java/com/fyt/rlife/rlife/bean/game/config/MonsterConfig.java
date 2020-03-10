package com.fyt.rlife.rlife.bean.game.config;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.game.factory.SuffixFactory;
import com.fyt.rlife.rlife.bean.game.factory.game1.MonsterGame1Factory;
import com.fyt.rlife.rlife.bean.monster.suffix.Suffix;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/3/3 17:58
 * @Version 1.0
 */
@Configuration
public class MonsterConfig {

    public static class MonsterGame1Map{
        Map<String,Monster> monsterGame1Map;

        public Map<String, Monster> getMonsterGame1Map() {
            return monsterGame1Map;
        }
    }

    public static class SuffixClassList{
        List<Class<Suffix>> suffixClassList;

        public List<Class<Suffix>> getSuffixClassList() {
            return suffixClassList;
        }
    }

    /**
     * 怪物数据池
     */
    @Bean
    public MonsterConfig.MonsterGame1Map getMonsterGame1Pool(){
        MonsterGame1Map monsterGame1Map = new MonsterGame1Map();
        monsterGame1Map.monsterGame1Map = new HashMap<>();

        Class<MonsterGame1Factory> monsterFactoryClass = MonsterGame1Factory.class;
        Method[] declaredMethods = monsterFactoryClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            try {
                Monster invoke = (Monster)declaredMethod.invoke(null);
                monsterGame1Map.monsterGame1Map.put(invoke.getId(),invoke);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return monsterGame1Map;
    }

    @Bean
    public MonsterConfig.SuffixClassList getSuffixClassListPool(){
        SuffixClassList suffixClassList = new SuffixClassList();
        suffixClassList.suffixClassList = new ArrayList<>();

        Class<SuffixFactory> suffixFactoryClass = SuffixFactory.class;
        Method[] declaredMethods = suffixFactoryClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            try {
                Class<Suffix> invoke = (Class<Suffix>)declaredMethod.invoke(null);
                suffixClassList.suffixClassList.add(invoke);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return suffixClassList;
    }

}
