package com.fyt.rlife.rlife.bean.monster;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.config.GamePropConfig;
import com.fyt.rlife.rlife.util.GameUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 18:05
 * @Version 1.0
 */
//猎犬
public class Hound extends Monster {


    @Override
    public void move(int i, int j, StringBuilder stringBuilder, Role role,List<Monster> monsterList,Random random) {
        int a = GameUtils.distanceXY(x, y, i, j);
        if (a<=2){
            //与角色距离小于2，触发战斗
            fighting(role,stringBuilder,random);
        }else {
            //随机移动
            GameUtils.noIntentionMove(this,2,random);
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
        gameProp.add(GamePropConfig.GamePropMap.getPropById(1+""));
        if (random.nextInt(100)>50){
            gameProp.add(GamePropConfig.GamePropMap.getPropById(6+""));
        }
        GameUtils.monsterDeath(role,this,stringBuilder,gameProp);
    }
}
