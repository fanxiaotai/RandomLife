package com.fyt.rlife.rlife.bean.game.factory.game1;

import com.fyt.rlife.rlife.bean.game.common.GameProp;

/**
 * @Author: fanyitai
 * @Date: 2020/3/4 10:34
 * @Version 1.0
 */
//获取道具
public class PropAllGame1Factory {

    public static GameProp getMonsterMeat(){//怪物肉
        String name = "怪物肉";
        String describe = "怪物身上掉落的肉，或许你应该试着烹饪一下,饱食+30，生命-10";
        return new GameProp("1",name,describe,1,3);
    }

    public static GameProp getSpiderSilk(){//蜘蛛丝
        String name = "蜘蛛丝";
        String describe = "做防具的材料，你试着拉扯了一下，觉得韧性十足";
        return new GameProp("2",name,describe,1,2);
    }

    public static GameProp getSpiderVenom(){//蜘蛛毒液
        String name = "蜘蛛毒液";
        String describe = "涂抹在武器上，攻击时使敌人中毒，别舔！，重复使用，使中毒等级+1，持续1回合";
        return new GameProp("3",name,describe,1,5);
    }

    public static GameProp getTorches(){//火把
        String name = "火把";
        String describe = "烧伤敌人，处理食材，此乃生命之源";
        return new GameProp("4",name,describe,1,10);
    }

    public static GameProp getCookedMeat(){//熟肉
        String name = "熟肉";
        String describe = "恢复20点生命，增加30点饱食度";
        return new GameProp("5",name,describe,1,15);
    }

    public static GameProp getFangs(){//犬牙
        String name = "犬牙";
        String describe = "做武器的材料，异常锋利";
        return new GameProp("6",name,describe,1,2);
    }

    public static GameProp getFrogsLegs(){//青蛙腿
        String name = "青蛙腿";
        String describe = "饱食度+15，生命-2";
        return new GameProp("7",name,describe,1,5);
    }

    public static GameProp getHoney(){//蜂蜜
        String name = "蜂蜜";
        String describe = "魔法+20，饱食度+5";
        return new GameProp("8",name,describe,1,15);
    }

    public static GameProp getBeeNeedle(){//蜂针
        String name = "蜂针";
        String describe = "对下个敌人照成10点伤害";
        return new GameProp("9",name,describe,1,3);
    }
}
