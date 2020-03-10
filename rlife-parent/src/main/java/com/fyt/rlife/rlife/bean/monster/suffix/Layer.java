package com.fyt.rlife.rlife.bean.monster.suffix;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Random;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 20:55
 * @Version 1.0
 */
@Getter
@Setter
public class Layer extends Suffix {

    public Layer(int layer, Monster monster) {
        super(layer, monster);
        this.layer = layer;
        this.monster = monster;
        this.name = "层数"+layer;
        double pow = Math.pow(1.1, layer);
        double d = layer*0.1+1;
        monster.setAttack((int)(monster.getAttack()*pow));
        monster.setDefense(monster.getDefense()+layer*2/5);
        monster.setLife((int)(monster.getLife()*pow));
        monster.setMonsterExp((int)(monster.getMonsterExp()*d));
        monster.setMonsterGold((int)(monster.getMonsterGold()*d));
    }

    @Override
    public void move(int i, int j, StringBuilder stringBuilder, Role role, List<Monster> monsterList,Random random) {
        monster.move(i, j, stringBuilder, role,monsterList,random);
    }

    @Override
    public int monsterAttack(int roleLife, int roleDefense, StringBuilder stringBuilder) {

        return monster.monsterAttack(roleLife, roleDefense, stringBuilder);
    }

    @Override
    public void monsterDeath(StringBuilder stringBuilder, Role role, Random random) {
        monster.monsterDeath(stringBuilder, role,random);
    }
}
