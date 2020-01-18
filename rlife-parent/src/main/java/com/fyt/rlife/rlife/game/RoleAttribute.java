package com.fyt.rlife.rlife.game;

import com.fyt.rlife.rlife.bean.Role;

/**
 * @Author: fanyitai
 * @Date: 2020/1/7 19:21
 * @Version 1.0
 */
public class RoleAttribute {

    /**
     * 基础体质变化
     */
    public static void basePhysicalRange(Role role, int range){
        Integer basePhysical = role.getBasePhysical();
        int basePhysicalNew = basePhysical + range;
        int baseLifeNew = basePhysicalNew * 10;



        int defenseMax = basePhysical / 5 + basePhysical / 5 * role.getDefenseAmplification() / 100;
        int defenseNewMax = basePhysicalNew / 5 + basePhysicalNew / 5 * role.getDefenseAmplification() / 100;
        double a = 1.0*role.getDefense()/defenseMax;
        role.setBasePhysical(basePhysicalNew);
        role.setBaseLife(baseLifeNew);
        role.setBaseDefense(basePhysicalNew/5);

        role.setLife(role.getLife()+(range*10*role.getLifeAmplification()+range*10));
        role.setPhysical(basePhysicalNew);
        role.setDefense((int)Math.rint(defenseNewMax*a));
    }

    /**
     * 基础力量变化
     */
    public static void basePowerRange(Role role,int range){
        Integer power = role.getBasePower();
        Integer agility = role.getBaseAgility();
        int powerNew = power+range;



        int baseAttackMax = power+agility/3 + (power+agility/3)*role.getAttackAmplification()/100;
        int baseAttackNewMax = powerNew+agility/3 + (powerNew+agility/3)*role.getAttackAmplification()/100;
        double a = 1.0*role.getAttack()/baseAttackMax;
        role.setBaseAttack(powerNew+agility/3);
        role.setBasePower(powerNew);

        role.setPower(powerNew);
        role.setAttack((int)Math.rint(baseAttackNewMax*a));
    }

    /**
     * 基础敏捷变化
     */
    public static void baseAgilityRange(Role role,int range){
        Integer agility = role.getBaseAgility();
        Integer power = role.getBasePower();
        int agilityNew = agility+range;
        int baseAttack = power+agilityNew/3;
        double baseAttackSpeedNew = agilityNew * 0.05+ agilityNew*0.05*role.getAttackSpeedAmplification()/100;



        int baseAttackMax = power+agility/3 + (power+agility/3)*role.getAttackAmplification()/100;
        int baseAttackNewMax = power+agilityNew/3 + (power+agilityNew/3)*role.getAttackAmplification()/100;
        double a = 1.0*role.getAttack()/baseAttackMax;



        double baseAttackSpeed = agility *0.05 + agility*0.05*role.getAttackSpeedAmplification()/100;
        long round = Math.round(baseAttackSpeed * 100);
        double baseAttackSpeed2 = 1.0*round/100;
        double b = role.getAttackSpeed() / baseAttackSpeed2;

        role.setBaseAttack(baseAttack);
        role.setBaseAttackSpeed((double)Math.round(agilityNew * 0.05*100)/100);
        role.setBaseAgility(agilityNew);
        role.setAgility(agilityNew);
        role.setAttack((int)Math.rint(baseAttackNewMax*a));
        role.setAttackSpeed(baseAttackSpeedNew*b);
    }

    /**
     * 基础精神变化
     */
    public static void baseMindRange(Role role,int range){
        Integer mind = role.getBaseMind();
        int MindNew = mind+range;
        int magic = MindNew*10;
        int defNew = MindNew/5;



        int defMax = mind / 5 + mind / 5 * role.getDefAmplification() / 100;
        int defNewMax = mind / 5 + mind / 5 * role.getDefAmplification() / 100;
        double a = 1.0*role.getDef()/defMax;
        role.setBaseDef(defNew);
        role.setBaseMagic(magic);
        role.setBaseMind(MindNew);
        role.setMind(MindNew);

        role.setMagic(role.getMagic() + (range*10*role.getMagicAmplification()+range*10));
        role.setDef((int)Math.rint(defNewMax*a));
    }

    /**
     * 全属性增幅
     */
    public static void amplificationRange(Role role,int range){
        Integer amplificationNew = role.getAmplification()+range;
        role.setAmplification(amplificationNew);
        magicAmplificationRange(role,range);
        attackAmplificationRange(role,range);
        defenseAmplificationRange(role, range);
        attackSpeedAmplificationRange(role, range);
        moveSpeedAmplificationRange(role, range);
        lifeAmplificationRange(role, range);
        defAmplificationRange(role, range);
/*        Integer attackAmplification = role.getAttackAmplification();
        Integer attackSpeedAmplification = role.getAttackSpeedAmplification();
        Integer defenseAmplification = role.getDefenseAmplification();
        Integer defAmplification = role.getDefAmplification();
        Integer lifeAmplification = role.getLifeAmplification();
        Integer moveSpeedAmplification = role.getMoveSpeedAmplification();
        Integer magicAmplification = role.getMagicAmplification();*/
    }

    /**
     * 魔法增幅变化
     */
    public static void magicAmplificationRange(Role role,int range){
        Integer magicAmplificationNew = role.getMagicAmplification()+range;

        Integer baseMagic = role.getBaseMagic();
        int baseMagicMax = baseMagic + baseMagic * role.getMagicAmplification() / 100;
        double a = (double) role.getMagic()/baseMagicMax;
        role.setMagicAmplification(magicAmplificationNew);
        role.setMagic((int)Math.rint((baseMagic + 1.0*baseMagic * magicAmplificationNew / 100)*a));
    }

    /**
     * 攻击增幅变化
     */
    public static void attackAmplificationRange(Role role,int range){
        Integer attackAmplificationNew = role.getAttackAmplification()+range;

        Integer attack = role.getBaseAttack();
        int baseAttackMax = attack + attack * role.getAttackAmplification() / 100;
        double a = (double) role.getAttack()/baseAttackMax;
        role.setAttackAmplification(attackAmplificationNew);
        role.setAttack((int)Math.rint((attack + 1.0*attack * attackAmplificationNew / 100)*a));
    }

    /**
     * 防御增幅变化
     */
    public static void defenseAmplificationRange(Role role,int range){
        Integer defenseAmplificationNew = role.getDefenseAmplification()+range;

        Integer defense = role.getBaseDefense();
        int baseDefenseMax = defense + defense * role.getDefenseAmplification() / 100;
        double a = (double) role.getDefense()/baseDefenseMax;
        role.setDefenseAmplification(defenseAmplificationNew);
        role.setDefense((int)Math.rint((defense + 1.0*defense * defenseAmplificationNew / 100)*a));
    }

    /**
     * 攻速增幅变化
     */
    public static void attackSpeedAmplificationRange(Role role, int range){
        Integer attackSpeedAmplificationNew = role.getAttackSpeedAmplification()+range;

        Double attackSpeed = role.getBaseAttackSpeed();
        double baseAttackSpeedMax = attackSpeed + attackSpeed * role.getAttackSpeedAmplification() / 100;
        double a = role.getAttackSpeed()/baseAttackSpeedMax;
        role.setAttackSpeedAmplification(attackSpeedAmplificationNew);
        role.setAttackSpeed(Math.rint((attackSpeed + 1.0*attackSpeed * attackSpeedAmplificationNew / 100)*a));
    }

    /**
     * 移速增幅变化
     */
    public static void moveSpeedAmplificationRange(Role role,int range){
        Integer moveSpeedAmplificationNew = role.getMoveSpeedAmplification()+range;

        Integer moveSpeed = role.getBaseMoveSpeed();
        int baseMoveSpeedMax = moveSpeed + moveSpeed * role.getMoveSpeedAmplification() / 100;
        double a = (double) role.getMoveSpeed()/baseMoveSpeedMax;
        role.setMoveSpeedAmplification(moveSpeedAmplificationNew);
        role.setMoveSpeed((int)Math.rint((moveSpeed + 1.0*moveSpeed * moveSpeedAmplificationNew / 100)*a));
    }

    /**
     * 生命增幅变化
     */
    public static void lifeAmplificationRange(Role role,int range){
        Integer lifeAmplificationNew = role.getLifeAmplification()+range;

        Integer life = role.getBaseLife();
        int baseLifeMax = life + life * role.getLifeAmplification() / 100;
        double a = (double) role.getLife()/baseLifeMax;
        role.setLifeAmplification(lifeAmplificationNew);
        role.setLife((int)Math.rint((life + 1.0*life * lifeAmplificationNew / 100)*a));
    }

    /**
     * 魔防增幅变化
     */
    public static void defAmplificationRange(Role role,int range){
        Integer defAmplificationNew = role.getDefAmplification()+range;

        Integer def = role.getBaseDef();
        int baseDefMax = def + def * role.getDefAmplification() / 100;
        double a = (double) role.getDef()/baseDefMax;
        role.setDefAmplification(defAmplificationNew);
        role.setDef((int)Math.rint((def + 1.0*def * defAmplificationNew / 100)*a));
    }

    /**
     * 生命回复
     */
    public static void lifeRange(StringBuilder fighting,Role role,int range){
        Integer life = role.getLife();
        int lifeNew = life + range;
        Integer baseLifeMax = role.getBaseLife() + role.getBaseLife()*role.getLifeAmplification()/100;
        if (lifeNew>=baseLifeMax){
            role.setLife(baseLifeMax);
            fighting.append("你回复了");
            fighting.append(range-(lifeNew-baseLifeMax));
            fighting.append("点生命&#10;");
        }else {
            role.setLife(lifeNew);
            fighting.append("你回复了");
            fighting.append(range);
            fighting.append("点生命&#10;");
        }
    }

    /**
     * 魔法回复
     */
    public static void magicRange(StringBuilder fighting,Role role,int range){
        Integer magic = role.getMagic();
        int magicNew = magic + range;
        int baseMagicMax = role.getBaseMagic() + role.getBaseMagic()*role.getMagicAmplification()/100;
        if (magicNew>=baseMagicMax){
            role.setLife(baseMagicMax);
            fighting.append("你回复了");
            fighting.append(range-(magicNew-baseMagicMax));
            fighting.append("点魔法&#10;");
        }else {
            role.setLife(magicNew);
            role.setLife(baseMagicMax);
            fighting.append("你回复了");
            fighting.append(range);
            fighting.append("点魔法&#10;");
        }
    }
}
