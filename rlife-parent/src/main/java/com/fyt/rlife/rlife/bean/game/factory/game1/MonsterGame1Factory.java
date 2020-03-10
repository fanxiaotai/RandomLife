package com.fyt.rlife.rlife.bean.game.factory.game1;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.monster.*;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 12:06
 * @Version 1.0
 */
public class MonsterGame1Factory {

    public static Monster getSmallSpider(){
        Monster monster = new SmallSpider();
        monster.setId("1");
        monster.setNickname("小蜘蛛");
        monster.monsterDescribe = "随处可见的小蜘蛛，死亡时溅射大量毒液";
        monster.setMonsterLeave(1);
        monster.setAttack(5);
        monster.setDefense(1);
        monster.setLife(25);
        monster.setMonsterExp(2);
        monster.setMonsterGold(3);
        return monster;
    }

    public static Monster getSpiderNest(){
        Monster monster = new SpiderNest();
        monster.setId("2");
        monster.setNickname("蜘蛛巢");
        monster.monsterDescribe = "正在孕育者小蜘蛛，死亡会召唤小蜘蛛";
        monster.setMonsterLeave(1);
        monster.setAttack(1);
        monster.setDefense(2);
        monster.setLife(30);
        monster.setMonsterExp(2);
        monster.setMonsterGold(3);
        return monster;
    }

    public static Monster getHound(){
        Monster monster = new Hound();
        monster.setId("3");
        monster.setNickname("猎犬");
        monster.monsterDescribe = "凶恶的怪物，具有强烈的攻击性";
        monster.setMonsterLeave(1);
        monster.setAttack(10);
        monster.setDefense(2);
        monster.setLife(40);
        monster.setMonsterExp(6);
        monster.setMonsterGold(8);
        return monster;
    }

    public static Monster getFrog(){
        Monster monster = new Frog();
        monster.setId("4");
        monster.setNickname("青蛙");
        monster.monsterDescribe = "具有极强的弹跳能力，十字形攻击范围";
        monster.setMonsterLeave(1);
        monster.setAttack(6);
        monster.setDefense(3);
        monster.setLife(20);
        monster.setMonsterExp(3);
        monster.setMonsterGold(5);
        return monster;
    }

    public static Monster getBees(){
        Monster monster = new Bees();
        monster.setId("5");
        monster.setNickname("蜜蜂");
        monster.monsterDescribe = "远程攻击";
        monster.setMonsterLeave(1);
        monster.setAttack(4);
        monster.setDefense(2);
        monster.setLife(25);
        monster.setMonsterExp(2);
        monster.setMonsterGold(3);
        return monster;
    }
}
