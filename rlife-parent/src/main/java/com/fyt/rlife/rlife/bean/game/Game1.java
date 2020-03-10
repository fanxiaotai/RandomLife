package com.fyt.rlife.rlife.bean.game;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.Box;
import com.fyt.rlife.rlife.bean.game.common.GameStore;
import com.fyt.rlife.rlife.bean.game.common.Room;
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

    private List<Monster> monsters;//怪物
    private Role role;//角色
    //可能还有建筑物等以后再添加
    private Box box; //宝箱
    private GameStore store; //商店
    private Room room; //房间
    private boolean nextLayer; //是否为下一层入口

    public Game1(List<Monster> monsters) {
        this.monsters = monsters;
    }

    public Game1(Role role) {
        this.role = role;
    }

    public Game1(Box box) {
        this.box = box;
    }

    public Game1(GameStore store) {
        this.store = store;
    }

    public Game1(Room room) {
        this.room = room;
    }
}
