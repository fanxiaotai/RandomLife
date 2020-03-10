package com.fyt.rlife.rlife.bean.monster;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.State;
import com.fyt.rlife.rlife.bean.game.config.GamePropConfig;
import com.fyt.rlife.rlife.bean.game.factory.StateFactory;
import com.fyt.rlife.rlife.game.gamestate.GameState;
import com.fyt.rlife.rlife.util.GameUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @Author: fanyitai
 * @Date: 2020/3/3 20:13
 * @Version 1.0
 */
//小蜘蛛
@Getter
@Setter
public class SmallSpider extends Monster{

    /**
     * 移动范围 1
     */
    @Override
    public void move(int i,int j,StringBuilder stringBuilder, Role role,List<Monster> monsterList,Random random) {
        int a = GameUtils.distanceXY(x, y, i, j);
        if (a<=1){
            //与角色距离小于1，触发战斗
            fighting(role,stringBuilder,random);
        }else {
            //随机移动
            GameUtils.noIntentionMove(this,1,random);
        }
    }

    @Override
    public int monsterAttack(int roleLife,int roleDefense,StringBuilder stringBuilder) {
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
        List<GameProp> gameProp = new ArrayList<>();
        int i = random.nextInt(100);
        if (i>90){
            gameProp.add(GamePropConfig.GamePropMap.getPropById(3+""));
        }else if (i>70){
            gameProp.add(GamePropConfig.GamePropMap.getPropById(2+""));
        }else if (i>50){
            gameProp.add(GamePropConfig.GamePropMap.getPropById(1+""));
        }
        GameUtils.monsterDeath(role,this,stringBuilder,gameProp);

        //使角色中毒
        Map<String, State> moveStateMap = role.getMoveStateMap();
        State poisoning = StateFactory.getPoisoning(role.getLayerNumber()/5+1);
        GameState.addState(poisoning,moveStateMap,stringBuilder);

    }
}
