package com.fyt.rlife.rlife.controller;

import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.PackVo;
import com.fyt.rlife.rlife.bean.game.common.Packsack;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.game.config.BoxConfig;
import com.fyt.rlife.rlife.bean.game.config.MonsterConfig;
import com.fyt.rlife.rlife.bean.game.config.RoomConfig;
import com.fyt.rlife.rlife.bean.game.factory.GameVO;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.Game;
import com.fyt.rlife.rlife.service.GameMapService;
import com.fyt.rlife.rlife.service.RoleService;
import com.fyt.rlife.rlife.service.gameService.PacksackService;
import com.fyt.rlife.rlife.util.GameUtils;
import com.fyt.rlife.rlife.util.RedisUtil;
import com.fyt.rlife.rlife.util.ResultEntity;
import com.fyt.rlife.rlife.util.RlifeUtil;
import com.fyt.rlife.rlife.util.game.Game1Utils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author: fanyitai
 * @Date: 2019/12/31 16:37
 * @Version 1.0
 */
@Controller
@Transactional(rollbackFor = Exception.class)
public class GameMapController {

    @Autowired
    GameMapService gameMapService;
    @Autowired
    PacksackService packsackService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    MonsterConfig.MonsterGame1Map monsterGame1Map;
    @Autowired
    RoleService roleService;
    @Autowired
    BoxConfig.BoxMap boxMap;
    @Autowired
    RoomConfig.RoomMap roomMap;
    @Autowired
    MonsterConfig.SuffixClassList suffixClassList;

    @RequestMapping("/toGameMap")
    @RoleRequire(roles = 1)
    public String toGameMap(HttpServletRequest request, String wordId,String difficulty, ModelMap modelMap, HttpServletResponse response){

        //用户登陆检测并获取用户默认角色
        ResultEntity<Role> roleResultEntity = RlifeUtil.userLoginCheckGetDefaultRole(request, roleService);
        if (!roleResultEntity.getResult().equals(ResultEntity.SUCCESS)){
            modelMap.put("errorMessage",roleResultEntity.getMessage());
            LoggerFactory.getLogger(getClass()).error(roleResultEntity.getMessage());//打印错误日志
            return "error";
        }

        //检测redis或者session中是否有存档
        //...

        Role role = roleResultEntity.getData();
        role.setLeaveExp(role.getRoleLeave()*role.getRoleLeave()*10);
        role.setDifficulty(Integer.parseInt(difficulty));
        role.setFeedDegree(100);
        role.setGamePropMap(new HashMap<>());
        role.setMoveStateMap(new HashMap<>());
        role.setFightBeforeStateMap(new HashMap<>());
        role.setFightStateAttackMap(new HashMap<>());
        role.setFightAfterStateMap(new HashMap<>());

        modelMap.put("role",role);
        //技能显示
        Map<String, Skill> skillMap = role.getSkillMap();
        if (skillMap!=null){
            Collection<Skill> roleSkills = skillMap.values();
            modelMap.put("roleSkills",roleSkills);
        }else {
            modelMap.put("roleSkills",null);
        }

        //生成地图
        role.setLayerNumber(1);
        GameMap<Game1>[][] gameMaps = mapInitialize(wordId, role, monsterGame1Map, suffixClassList,boxMap, roomMap);
        if (gameMaps==null){
            modelMap.put("errorMessage","地图解析错误");
            return "error";
        }
        //转化为前端可视地图
        GameMap<GameVO>[][] gameVOGameMap = GameUtils.GameMapPO2VO(gameMaps);
        modelMap.put("gameMapSs",gameVOGameMap);

        //加载背包
        Map<String, GameProp> gamePropMap = role.getGamePropMap();
        if (gamePropMap==null){
            gamePropMap = new HashMap<>();
            role.setGamePropMap(gamePropMap);
            modelMap.put("gameProps",null);
        }else {
            PackVo packVo = new PackVo();
            GameUtils.roleProp2Vo(gamePropMap,packVo);
            //背包转换
            modelMap.put("gameProps",packVo);
        }

        //后台应用数据保存
        Game1Utils.saveInfo(request,role,redisUtil,gameMaps);

        return "game/gameMap";
    }

    /**
     * 继续游戏
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("/ThenGameMap")
    @RoleRequire(roles = 1)
    public String ThenGameMap(HttpServletRequest request,ModelMap modelMap){

        return "game/gameMap";
    }

    /**
     * 不继续，进行新游戏
     * @param request
     * @param modelMap
     * @param wordId
     * @return
     */
    @RequestMapping("/NewGameMap")
    @RoleRequire(roles = 1)
    public String NewGameMap(HttpServletRequest request,ModelMap modelMap,String wordId){

        return "game/gameMap";
    }

    /**
     * 世界初始化
     */
    public static GameMap<Game1>[][] mapInitialize(String wordId,Role role, MonsterConfig.MonsterGame1Map monsterGame1Map,MonsterConfig.SuffixClassList suffixClassList, BoxConfig.BoxMap boxMap, RoomConfig.RoomMap roomMap){

        if (wordId.equals("1")){
            return Game.game1(role, role.getDifficulty(), monsterGame1Map, suffixClassList, boxMap, roomMap);
        }
        return null;
    }

    /**
     * 背包转换
     * @param packVo
     */
    public static void packsackVO2Po(Packsack packsack, PackVo packVo){

        List<GameProp> propLists = packsack.getPropLists();
        List<List<GameProp>> propListss = new ArrayList<>();
        List<GameProp> props = new ArrayList<>();
        propListss.add(props);
        for (int i = 0;i<propLists.size();i++){
            if (i!=0&&i%10==0){
                props = new ArrayList<>();
                propListss.add(props);
            }
            GameProp gameProp = propLists.get(i);
            props.add(gameProp);
        }
        packVo.setPropLists(propListss);
    }

    /**
     *  被动技能加载
     */
/*    public static void PassiveSkillLoading(Role role){
        List<Skill> skillList = role.getSkillList();
        if (skillList!=null){
            for (Skill skill : skillList) {
                if (skill.getSkillInitiative().equals("0")){
                    if (!SkillUse.passiveSkillUse(role,skill)){
                        System.out.println("有技能加载失败");
                    }
                }
            }
        }
    }*/
}
