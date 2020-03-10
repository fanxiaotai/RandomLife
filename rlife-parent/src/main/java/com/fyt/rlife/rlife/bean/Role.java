package com.fyt.rlife.rlife.bean;

import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.Reward;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.game.common.State;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2019/12/30 17:43
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role {

    @Id
    private String id;
    private String memberId;
    private String nickname; // '角色名称',

    private Integer life;  // '生命',
    private Integer lifeMax; // 最大生命
    private Integer magic; //魔法
    private Integer magicMax; //最大魔法
    private Integer attack;  // '攻击',
    private Integer defense;  // '防御',
    private Integer roleLeave; //角色等级
    private Integer exp; //经验
    @Transient
    private Integer leaveExp; //升级所需经验

    private Integer freelyDistributable; //自由可分配的属性点
    private Integer gold; //金钱
    private Integer skillPoints; //技能点

    private Integer defaultRole; //是否为默认角色，1是默认
    private Integer survive; //存活状态，1为已死亡

    @Transient
    private Integer feedDegree; //饱食度

    //移动状态
    @Transient
    private Map<String,State> moveStateMap;//key 状态编号；value 剩余影响次数
    //战斗前状态
    @Transient
    private Map<String,State> fightBeforeStateMap;//key 状态编号；value 剩余影响次数
    //战斗时状态(攻击替换)
    @Transient
    private Map<String,State> fightStateAttackMap;//key 状态编号；value 剩余影响次数
    //战斗结束后状态
    @Transient
    private Map<String,State> fightAfterStateMap;//key 状态编号；value 剩余影响次数
    @Transient
    private Map<String,Skill> skillMap; //拥有的技能
    @Transient
    private Map<String, GameProp> gamePropMap; //拥有的游戏道具

    @Transient
    private Map<String,Integer> propLimitMap; //道具限制的map

    @Transient
    private int layerNumber; //层数
    @Transient
    private int difficulty; //难度
    @Transient
    private List<Reward> rewardList; //全清奖励
    private boolean reward; //是否获得全清奖励

    public void roleNormalAttack(Monster monster,StringBuilder stringBuilder){
        Integer defense = monster.getDefense();
        int a = attack-defense;
        if (a<=0) a=1;
        monster.life -= a;
        stringBuilder.append("你对");
        stringBuilder.append(monster.getNickname());
        stringBuilder.append("造成");
        stringBuilder.append(a);
        stringBuilder.append("点伤害&#10;");
    }
}
