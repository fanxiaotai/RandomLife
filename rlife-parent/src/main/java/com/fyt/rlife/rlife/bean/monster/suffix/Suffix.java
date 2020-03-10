package com.fyt.rlife.rlife.bean.monster.suffix;

import com.fyt.rlife.rlife.bean.Monster;
import lombok.Getter;
import lombok.Setter;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 21:54
 * @Version 1.0
 */
@Getter
@Setter
public abstract class Suffix extends Monster{

    int layer = 1;
    Monster monster;
    String name;

    public Suffix(int layer,Monster monster) {
        this.monster = monster;
        this.layer = layer;
    }
}
