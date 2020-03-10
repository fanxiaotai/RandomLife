package com.fyt.rlife.rlife.game;

import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.common.Reward;

/**
 * @Author: fanyitai
 * @Date: 2020/3/7 21:26
 * @Version 1.0
 */
public class RewardS {


    public static Reward getRewardByIdAndLayer(Integer rewardId, int layerNumber) {

        switch (rewardId){
            case 1:
                return new Reward(rewardId,"回血","恢复最大生命值40%的血量");
            case 2:
                return new Reward(rewardId,"回蓝","恢复最大魔法值40%的魔法");
            case 3:
                return new Reward(rewardId,"经验","获得"+layerNumber*2+"的经验");
            case 4:
                return new Reward(rewardId,"金钱","获得"+layerNumber*3+"的金钱");
            default:
                return new Reward(rewardId,"道具","获得一件随机的道具");
        }
    }

    public static void getRewardByIndex(Integer rewardNo, Role role, StringBuilder stringBuilder) {
        switch (rewardNo){
            case 1:
                RoleAttribute.lifeRange(stringBuilder,role,role.getLifeMax()*4/10);
                break;
            case 2:
                RoleAttribute.magicRange(stringBuilder,role,role.getMagicMax()*4/10);
                break;
            case 3:
                int i = role.getLayerNumber() * 2;
                stringBuilder.append("你获得了");
                stringBuilder.append(i);
                stringBuilder.append("点经验");
                role.setExp(role.getExp()+i);
                break;
            case 4:
                int j = role.getLayerNumber() * 3;
                stringBuilder.append("你获得了");
                stringBuilder.append(j);
                stringBuilder.append("金币");
                role.setGold(role.getGold()+j);
                break;
            default:
                break;
        }
    }
}
