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
 * @Date: 2020/3/4 19:43
 * @Version 1.0
 */
//蜜蜂类
public class Bees extends Monster {

    private int a = 0;

    //具备远程攻击的能力
    @Override
    public void move(int i, int j, StringBuilder stringBuilder, Role role,List<Monster> monsterList,Random random) {
        int distance = GameUtils.distanceXY(x, y, i, j);
        if (distance<=1){
            fighting(role,stringBuilder,random);
            return;
        }

        if (a==2){
            monsterAttack(role.getLife(),role.getDefense(),stringBuilder);
            a=0;
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
    public void monsterDeath(StringBuilder stringBuilder, Role role,Random random) {
        List<GameProp> gameProps = new ArrayList<>();
        int i = random.nextInt(100) + 1;
        if (i>90){
            gameProps.add(GamePropConfig.GamePropMap.getPropById(8+""));
        }else if (i>50){
            gameProps.add(GamePropConfig.GamePropMap.getPropById(9+""));
        }
        GameUtils.monsterDeath(role,this,stringBuilder,gameProps);
    }
}
