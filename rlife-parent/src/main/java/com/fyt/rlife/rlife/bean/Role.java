package com.fyt.rlife.rlife.bean;

import com.fyt.rlife.rlife.bean.game.common.Skill;
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
  private String userId;
  private Integer exp; //经验
  @Transient
  private Integer leaveExp; //升级所需经验
  private Integer roleLeave; //角色等级
  private Integer freelyDistributable; //自由可分配的属性点
  private String nickname; // '角色名称',
  private Integer physical;  //'体质',
  private Integer power;  // '力量',
  private Integer agility;  // '敏捷',
  private Integer mind;  // '精神',
  private Integer attack;  // '攻击',
  private Integer defense;  // '防御',
  private Double attackSpeed; //'攻速',
  private Integer moveSpeed;  // '移速',
  private Integer life;  // '生命',
  private Integer def; // '魔防',
  private Integer tenacity;  // '韧性',
  private Integer baseAttack; //'基础攻击',
  private Integer baseDefense;  //'基础防御',
  private Double baseAttackSpeed; // '基础攻速',
  private Integer baseMoveSpeed;  //'基础移速',
  private Integer baseLife;  // '基础生命',
  private Integer basePhysical;  // '基础体质',
  private Integer basePower;   // '基础力量',
  private Integer baseAgility;  // '基础敏捷',
  private Integer baseMind; // '基础精神',
  private Integer baseDef;  //'基础魔防',
  private Integer amplification;  // '全属性增幅',
  private Integer attackAmplification;  // '基础攻击增幅',
  private Integer defenseAmplification;  // '基础防御增幅',
  private Integer attackSpeedAmplification;  // '基础攻速增幅',
  private Integer moveSpeedAmplification;  // '基础移速增幅',
  private Integer lifeAmplification;  // '基础生命增幅',
  private Integer defAmplification;  // '基础魔防增幅',
  private Integer defaultRole; //是否为默认角色，1是默认
  private Integer magic; //魔法
  private Integer baseMagic; //基础魔法
  private Integer magicAmplification;  // '基础魔法增幅',
  private Integer survive; //存活状态，1为已死亡

  //移动状态
  @Transient
  private Map<String,Integer> moveStateMap;//key 状态编号；value 剩余影响次数
  //战斗前状态
  @Transient
  private Map<String,Integer> fightBeforeStateMap;//key 状态编号；value 剩余影响次数
  //战斗时状态(攻击替换)
  @Transient
  private Map<String,Integer> fightStateAttackMap;//key 状态编号；value 剩余影响次数
  //战斗结束后状态
  @Transient
  private Map<String,Integer> fightAfterStateMap;//key 状态编号；value 剩余影响次数
  @Transient
  private List<Skill> skillList; //拥有的技能
  @Transient
  private int round; //目前所在游戏的回合数
  @Transient
  private Map<String,Integer> propLimitMap; //道具限制的map

}
