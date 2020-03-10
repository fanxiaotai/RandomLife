package com.fyt.rlife.rlife.bean;

import com.fyt.rlife.rlife.game.gamestate.GameState;
import com.fyt.rlife.rlife.util.GameUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 * @Author: fanyitai
 * @Date: 2019/12/30 19:58
 * @Version 1.0
 */
@Data
public abstract class Monster implements Cloneable, Serializable {

    private String id;
    private String nickname; // '怪物名称',
    private Integer monsterLeave; //怪物等级
    public String monsterDescribe; //怪物描述
    private Integer attack;  // '攻击',
    private Integer defense;  // '防御',
    public Integer life;  // '生命',
    private Integer monsterExp; //击败后可获得经验
    private Integer monsterGold; //击败后可获得金币
    //private List<GameProp> gameProp; //携带的道具

    public int x; //横坐标
    public int y; //纵坐标

    /**
     * 移动行为
     * @param i 角色横坐标
     * @param j 角色纵坐标
     */
    public abstract void move(int i, int j, StringBuilder stringBuilder, Role role, List<Monster> monsterList,Random random);
    public void fighting(Role role,StringBuilder stringBuilder,Random random){
        GameUtils.fighting(role,this,stringBuilder,random);
        GameState.stateDissipate(role.getFightStateAttackMap(),role,this,stringBuilder);
    }
    //攻击行为(战斗行为)
    public abstract int monsterAttack(int roleLife,int roleDefense,StringBuilder stringBuilder);
    //死亡行为
    public abstract void monsterDeath(StringBuilder stringBuilder, Role role, Random random);

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
