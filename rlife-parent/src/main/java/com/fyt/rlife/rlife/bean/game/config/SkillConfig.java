package com.fyt.rlife.rlife.bean.game.config;

import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.game.factory.SkillFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/3/3 19:10
 * @Version 1.0
 */
@Configuration
public class SkillConfig {

    public static class SkillMap{
        private static Map<String, Skill> skillMap;

        public static Map<String, Skill> getSkillMap() {
            return skillMap;
        }

        public static Skill getSkillById(String skillId){
            try {
                return (Skill)skillMap.get(skillId).clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /**
     * 技能数据原型池
     */
    @Bean
    public SkillConfig.SkillMap getSkillPool(){
        SkillConfig.SkillMap skillMap = new SkillConfig.SkillMap();
        SkillConfig.SkillMap.skillMap = new HashMap<>();

        Class<SkillFactory> monsterFactoryClass = SkillFactory.class;
        Method[] declaredMethods = monsterFactoryClass.getDeclaredMethods();
        for (Method declaredMethod : declaredMethods) {
            try {
                Skill invoke = (Skill)declaredMethod.invoke(null,1);
                SkillConfig.SkillMap.skillMap.put(invoke.getId(),invoke);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return skillMap;
    }
}
