package com.fyt.rlife.rlife.game;

import com.fyt.rlife.rlife.bean.Role;

/**
 * @Author: fanyitai
 * @Date: 2020/1/7 19:21
 * @Version 1.0
 */
public class RoleAttribute {

    /**
     * 生命回复
     */
    public static void lifeRange(StringBuilder fighting,Role role,int range){
        Integer life = role.getLife();
        int lifeNew = life + range;
        Integer baseLifeMax = role.getLifeMax();
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
        int baseMagicMax = role.getMagicMax();
        if (magicNew>=baseMagicMax){
            role.setMagic(baseMagicMax);
            fighting.append("你回复了");
            fighting.append(range-(magicNew-baseMagicMax));
            fighting.append("点魔法&#10;");
        }else {
            role.setMagic(magicNew);
            fighting.append("你回复了");
            fighting.append(range);
            fighting.append("点魔法&#10;");
        }
    }

    /**
     * 根据四围属性和角色名创建新角色
     */
    public static void newRole(Role role,Integer lifeMax,Integer attack,Integer defence,Integer magicMax,String roleName,String userId){
        role.setNickname(roleName);
        role.setRoleLeave(1);
        role.setExp(0);
        role.setLifeMax(lifeMax);
        role.setAttack(attack);
        role.setDefense(defence);
        role.setMagicMax(magicMax);
        role.setMemberId(userId);
        role.setGold(0);
        role.setLife(lifeMax);
        role.setMagic(magicMax);
        role.setSkillPoints(0);
        role.setDefaultRole(0);
        role.setSurvive(0);
    }

    /**
     * 对角色进行升级
     */
    public static void roleLeaveUp(Role role){
        role.setRoleLeave(role.getRoleLeave()+1);
        role.setFreelyDistributable(role.getFreelyDistributable()+4);
    }

    /**
     * 最大生命变化
     */
    public static void lifeMaxRange(Role role, int i) {
        role.setLifeMax(role.getLifeMax()+i);
        role.setLife(role.getLife()+i);
    }

    /**
     * 攻击变化
     */
    public static void attackRange(Role role, int i) {
        role.setAttack(role.getAttack()+i);
    }

    /**
     * 防御变化
     */
    public static void defenseRange(Role role, int i) {
        role.setDefense(role.getDefense()+i);
    }

    /**
     * 魔法回复
     */
    public static void magicMaxRange(Role role, int i) {
        role.setMagicMax(role.getMagicMax()+i);
        role.setMagic(role.getMagic()+i);
    }
}
