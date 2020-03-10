package com.fyt.rlife.rlife.bean.game.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2020/1/5 15:30
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Packsack implements Serializable {

    @Id
    private String id;
    @Column
    private Integer roleId; //背包对应的角色的Id
    @Transient
    private List<GameProp> propLists;
    @Transient
    private boolean UserPacksack; //是否为用户背包
}
