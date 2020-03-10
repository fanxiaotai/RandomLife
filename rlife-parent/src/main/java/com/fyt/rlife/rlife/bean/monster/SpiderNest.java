package com.fyt.rlife.rlife.bean.monster;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.config.GamePropConfig;
import com.fyt.rlife.rlife.bean.game.factory.game1.MonsterGame1Factory;
import com.fyt.rlife.rlife.bean.monster.suffix.Layer;
import com.fyt.rlife.rlife.util.GameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 17:27
 * @Version 1.0
 */
//蜘蛛巢
public class SpiderNest extends Monster {

    public int a = 0;

    @Override
    public void move(int i, int j, StringBuilder stringBuilder, Role role,List<Monster> monsterList,Random random) {
        int distance = GameUtils.distanceXY(x, y, i, j);
        if (distance == 0){
            fighting(role, stringBuilder,random);
        }
        if (a==2){
            Monster smallSpider = MonsterGame1Factory.getSmallSpider();
            smallSpider.x = this.x;
            smallSpider.y = this.y;
            Layer layer = new Layer(role.getLayerNumber(),smallSpider);
            //根据难度额外进行固定后缀加持
            //...
            layer.move(i, j, stringBuilder, role,monsterList,random);
            if (layer.getMonster().life>0){
                monsterList.add(layer);
            }

            a = 0;
        }else {
            a++;
        }
    }

    @Override
    public int monsterAttack(int roleLife, int roleDefense, StringBuilder stringBuilder) {
        int i = getAttack() - roleDefense;
        if (i<=0){
            i = 1;
        }
        roleLife -= i;
        GameUtils.fightingString(getNickname(),"你",i,stringBuilder);
        return roleLife;
    }

    @Override
    public void monsterDeath(StringBuilder stringBuilder, Role role, Random random) {
        List<GameProp> gameProp = new ArrayList<>();
        gameProp.add(GamePropConfig.GamePropMap.getPropById(2+""));
        GameUtils.monsterDeath(role,this,stringBuilder,gameProp);

        //死亡召唤小蜘蛛

        Monster smallSpider = MonsterGame1Factory.getSmallSpider();
        Layer layer = new Layer(role.getLayerNumber(),smallSpider);
        //根据难度额外进行固定后缀加持
        //...
        smallSpider.fighting(role,stringBuilder,random);
    }
}
