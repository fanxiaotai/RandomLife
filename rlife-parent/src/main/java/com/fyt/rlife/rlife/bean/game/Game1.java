package com.fyt.rlife.rlife.bean.game;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.Box;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 游戏1格子上的东西，每个类代表一个格子上存在的东西
 * @Author: fanyitai
 * @Date: 2020/1/4 11:45
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game1 {

    private int terrain;//地形
    private List<Monster> monsters;//怪物
    private Role role;//角色
    //可能还有建筑物等以后再添加
    private Box box;

    public Game1(int terrain) {
        this.terrain = terrain;
    }

    public Game1(List<Monster> monsters) {
        this.monsters = monsters;
    }

    public Game1(Role role) {
        this.role = role;
    }

    public Game1(Box box) {
        this.box = box;
    }

}
