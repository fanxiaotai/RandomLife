package com.fyt.rlife.rlife.bean.game.common;

import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 16:43
 * @Version 1.0
 */
//房间类
@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Room implements Cloneable, Serializable {

    private String id;
    private String roomName;

    public abstract void roomTrigger(Role role, Monster monster,StringBuilder stringBuilder);
}
