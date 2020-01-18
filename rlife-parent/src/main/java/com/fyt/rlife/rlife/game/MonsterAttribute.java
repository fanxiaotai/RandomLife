package com.fyt.rlife.rlife.game;

import com.fyt.rlife.rlife.bean.Monster;

/**
 * @Author: fanyitai
 * @Date: 2020/1/7 16:20
 * @Version 1.0
 */
public class MonsterAttribute {

    /**
     * 基础体质变化
     */
    public static void basePhysicalRange(Monster monster,int range){
        Integer basePhysical = monster.getBasePhysical();
        int basePhysicalNew = basePhysical + range;

        monster.setBasePhysical(basePhysicalNew);
        monster.setBaseLife(basePhysicalNew*10);
        monster.setBaseDefense(basePhysicalNew/5);
        monster.setLife(basePhysicalNew*10 + basePhysicalNew*10*(monster.getLifeAmplification()/100));
        monster.setDefense(basePhysicalNew/5 + basePhysicalNew/5*monster.getDefAmplification()/100);
    }

    /**
     * 基础力量变化
     */
    public static void basePowerRange(Monster monster,int range){
        Integer power = monster.getBasePower();
        Integer agility = monster.getBaseAgility();
        int powerNew = power+range;
        int baseAttack = powerNew+agility/3;

        monster.setBaseAttack(baseAttack);
        monster.setAttack(baseAttack + baseAttack*monster.getAttackAmplification()/100);
    }

    /**
     * 基础敏捷变化
     */
    public static void baseAgilityRange(Monster monster,int range){
        Integer agility = monster.getBaseAgility();
        Integer power = monster.getBasePower();
        int agilityNew = agility+range;
        int baseAttack = power+agilityNew/3;
        double baseAttackSpeed = agilityNew * 0.05;

        monster.setBaseAttack(baseAttack);
        monster.setAttack(baseAttack + baseAttack*monster.getAttackAmplification()/100);

        monster.setBaseAttackSpeed(baseAttackSpeed);
        monster.setAttackSpeed(baseAttackSpeed + baseAttackSpeed*monster.getAttackSpeedAmplification()/100);
    }

    /**
     * 基础精神变化
     */
    public static void baseMindRange(Monster monster,int range){
        Integer Mind = monster.getBaseMind();
        int MindNew = Mind+range;
        int magic = MindNew*10;
        int def = MindNew/5;

        monster.setBaseMagic(magic);
        monster.setMagic(magic + magic*monster.getMagicAmplification()/100);

        monster.setBaseDef(def);
        monster.setDef(def + def*monster.getDefAmplification()/100);
    }

    /**
     * 全属性增幅
     */
    public static void amplificationRange(Monster monster,int range){
        Integer amplificationNew = monster.getAmplification()+range;
        monster.setAmplification(amplificationNew);
        magicAmplificationRange(monster,range);
        attackAmplificationRange(monster,range);
        defenseAmplificationRange(monster, range);
        attackSpeedAmplificationRange(monster, range);
        moveSpeedAmplificationRange(monster, range);
        lifeAmplificationRange(monster, range);
        defAmplificationRange(monster, range);

        Integer monsterLeave = monster.getMonsterLeave();
        int monsterExpNew = monsterLeave * 2;
        monster.setMonsterExp(monsterExpNew + monsterExpNew * amplificationNew/100);
/*        Integer attackAmplification = monster.getAttackAmplification();
        Integer attackSpeedAmplification = monster.getAttackSpeedAmplification();
        Integer defenseAmplification = monster.getDefenseAmplification();
        Integer defAmplification = monster.getDefAmplification();
        Integer lifeAmplification = monster.getLifeAmplification();
        Integer moveSpeedAmplification = monster.getMoveSpeedAmplification();
        Integer magicAmplification = monster.getMagicAmplification();*/
    }

    /**
     * 魔法增幅变化
     */
    public static void magicAmplificationRange(Monster monster,int range){
        Integer magicAmplificationNew = monster.getMagicAmplification()+range;
        monster.setMagicAmplification(magicAmplificationNew);
        Integer baseMagic = monster.getBaseMagic();
        monster.setMagic(baseMagic + baseMagic*magicAmplificationNew/100);
    }

    /**
     * 攻击增幅变化
     */
    public static void attackAmplificationRange(Monster monster,int range){
        Integer attackAmplificationNew = monster.getAttackAmplification()+range;
        monster.setAttackAmplification(attackAmplificationNew);
        Integer attack = monster.getBaseAttack();
        monster.setAttack(attack + attack*attackAmplificationNew/100);
    }

    /**
     * 防御增幅变化
     */
    public static void defenseAmplificationRange(Monster monster,int range){
        Integer defenseAmplificationNew = monster.getDefenseAmplification()+range;
        monster.setDefenseAmplification(defenseAmplificationNew);
        Integer defense = monster.getBaseDefense();
        monster.setDefense(defense + defense*defenseAmplificationNew/100);
    }

    /**
     * 攻速增幅变化
     */
    public static void attackSpeedAmplificationRange(Monster monster,int range){
        Integer attackSpeedAmplificationNew = monster.getAttackSpeedAmplification()+range;
        monster.setAttackSpeedAmplification(attackSpeedAmplificationNew);
        Double attackSpeed = monster.getBaseAttackSpeed();
        monster.setAttackSpeed(attackSpeed + attackSpeed*attackSpeedAmplificationNew/100);
    }

    /**
     * 移速增幅变化
     */
    public static void moveSpeedAmplificationRange(Monster monster,int range){
        Integer moveSpeedAmplificationNew = monster.getMoveSpeedAmplification()+range;
        monster.setMoveSpeedAmplification(moveSpeedAmplificationNew);
        Integer moveSpeed = monster.getBaseMoveSpeed();
        monster.setMoveSpeed(moveSpeed + moveSpeed*moveSpeedAmplificationNew/100);
    }

    /**
     * 生命增幅变化
     */
    public static void lifeAmplificationRange(Monster monster,int range){
        Integer lifeAmplificationNew = monster.getLifeAmplification()+range;
        monster.setLifeAmplification(lifeAmplificationNew);
        Integer life = monster.getBaseLife();
        monster.setLife(life + life*lifeAmplificationNew/100);
    }

    /**
     * 魔防增幅变化
     */
    public static void defAmplificationRange(Monster monster,int range){
        Integer defAmplificationNew = monster.getDefAmplification()+range;
        monster.setDefAmplification(defAmplificationNew);
        Integer def = monster.getBaseDef();
        monster.setDef(def + def*defAmplificationNew/100);
    }

    /**
     * 等级提升所带来的属性变化、经验变化,每级提升30%属性和2点经验
     */
    public static void monsterLeaveRange(Monster monster,int range){
        int monsterLeave = monster.getMonsterLeave() + range;
        monster.setMonsterLeave(monsterLeave);

        amplificationRange(monster, range*30);
    }

}
