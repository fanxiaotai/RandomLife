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
 * @Date: 2020/3/4 19:08
 * @Version 1.0
 */
//青蛙类
public class Frog extends Monster {

    /**
     * 青蛙横向或纵向的敌人直接发起攻击
     */
    @Override
    public void move(int i, int j, StringBuilder stringBuilder, Role role,List<Monster> monsterList,Random random) {
        if (x==i||y==j){
            fighting(role,stringBuilder,random);
        }else {
            GameUtils.noIntentionMove(this,1,random);
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
        List<GameProp> gameProps = new ArrayList<>();
        if (random.nextInt(100)>50) gameProps.add(GamePropConfig.GamePropMap.getPropById(7+""));
        GameUtils.monsterDeath(role,this,stringBuilder,gameProps);
    }
}
