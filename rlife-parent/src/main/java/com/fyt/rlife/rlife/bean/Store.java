package com.fyt.rlife.rlife.bean;

import com.fyt.rlife.rlife.bean.game.common.GameProp;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Author: fanyitai
 * @Date: 2020/1/22 18:52
 * @Version 1.0
 */
//商店
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Store {

    @Id
    private String id;
    private String userId;
    private String propId1;
    private String propId2;
    private String propId3;
    private String propId4;
    private String propId5;
    private String propId6;
    private Date lastUpdate;

    @Transient
    private GameProp[] gameProps;
}
