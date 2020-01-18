package com.fyt.rlife.rlife.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

/**
 * @Author: fanyitai
 * @Date: 2019/12/30 19:58
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Monster {

    @Id
    private String id;
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
    private Integer magic; //魔法
    private Integer baseMagic; //基础魔法
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
    private Integer magicAmplification;  // '基础魔法增幅',
    private Integer defenseAmplification;  // '基础防御增幅',
    private Integer attackSpeedAmplification;  // '基础攻速增幅',
    private Integer moveSpeedAmplification;  // '基础移速增幅',
    private Integer lifeAmplification;  // '基础生命增幅',
    private Integer defAmplification;  // '基础魔防增幅',
    private Integer monsterExp; //击败后可获得经验
    private Integer monsterLeave; //怪物等级
}
