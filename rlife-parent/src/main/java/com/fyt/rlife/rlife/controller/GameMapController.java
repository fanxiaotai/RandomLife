package com.fyt.rlife.rlife.controller;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.PackVo;
import com.fyt.rlife.rlife.bean.game.common.Packsack;
import com.fyt.rlife.rlife.bean.game.common.Skill;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.Game;
import com.fyt.rlife.rlife.game.skill.SkillUse;
import com.fyt.rlife.rlife.service.GameMapService;
import com.fyt.rlife.rlife.service.gameService.MonsterService;
import com.fyt.rlife.rlife.service.gameService.PacksackService;
import com.fyt.rlife.rlife.util.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fanyitai
 * @Date: 2019/12/31 16:37
 * @Version 1.0
 */
@Controller
public class GameMapController {

    @Autowired
    GameMapService gameMapService;
    @Autowired
    MonsterService monsterService;
    @Autowired
    PacksackService packsackService;
    @Autowired
    RedisUtil redisUtil;

    @RequestMapping("/toGameMap")
    @RoleRequire(roles = 1)
    public String toGameMap(HttpServletRequest request, String wordId, ModelMap modelMap, HttpServletResponse response){
        Jedis jedis = redisUtil.getJedis();
        try {
            String memberId = (String) request.getAttribute("memberId");
            if (StringUtils.isBlank(memberId)){
                return "error";
            }
            String nickname = (String) request.getAttribute("nickname");
            //获取角色信息
            Role role = gameMapService.selectRoleByMemberId(memberId);
            //获取背包信息
            Packsack packsack = packsackService.getPacksackByRoleId(role.getId());
            PackVo packVo = new PackVo();
            packsackVO2Po(packsack,packVo);

            if (role == null||role.getSurvive()==1){
                //转发到个人中心,让他去创建自己的角色或者选择其他存活角色
                return "error";
            }else {
                role.setLeaveExp(role.getRoleLeave()*role.getRoleLeave()*10);
                String s = jedis.get("game1GameMapLists" + role.getId());
                if(StringUtils.isNotBlank(s)){
                    modelMap.put("wordId",wordId);
                    return "game/gameMap001";
                }else {
                    //被动技能的加载
                    List<Skill> skillList = role.getSkillList();
                    for (Skill skill : skillList) {
                        if (skill.getSkillInitiative().equals("0")){
                            if (!SkillUse.passiveSkillUse(role,skill)){
                                System.out.println("有技能加载失败");
                            }
                        }
                    }
                    List<List<GameMap<String>>> a = mapInitialize(wordId,request,role,monsterService);
                    modelMap.put("a",a);
                    role.setRound(0);
                    modelMap.put("rounds",0);
                }
            }

            modelMap.put("role",role);
            modelMap.put("packVo",packVo);
            modelMap.put("nickname",nickname);
            modelMap.put("memberId",memberId);
            return "game/gameMap";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }finally {
            jedis.close();
        }
    }

    @RequestMapping("/ThenGameMap")
    @RoleRequire(roles = 1)
    public String ThenGameMap(HttpServletRequest request,ModelMap modelMap){
        Jedis jedis = redisUtil.getJedis();
        try {
            String memberId = (String) request.getAttribute("memberId");
            if (StringUtils.isBlank(memberId)){
                return "error";
            }
            String nickname = (String) request.getAttribute("nickname");
            //获取角色信息
            Role role = gameMapService.selectRoleByMemberId(memberId);
            Packsack packsack = packsackService.getPacksackByRoleId(role.getId());
            PackVo packVo = new PackVo();
            packsackVO2Po(packsack,packVo);
            role = JSON.parseObject(jedis.get("role" + role.getId()),Role.class);
            String s = jedis.get("game1GameMapLists" + role.getId());
            GameMap<Game1>[][] mapByRedis = Game.getMapByRedis(s);
            List<List<GameMap<String>>> a = Game.game2gameMap(mapByRedis);

            modelMap.put("a",a);
            modelMap.put("rounds",role.getRound());
            modelMap.put("role",role);
            modelMap.put("packVo",packVo);
            modelMap.put("nickname",nickname);
            modelMap.put("memberId",memberId);
            return "game/gameMap";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        } finally {
            jedis.close();
        }
    }

    @RequestMapping("/NewGameMap")
    @RoleRequire(roles = 1)
    public String NewGameMap(HttpServletRequest request,ModelMap modelMap,String wordId){
        String memberId = (String) request.getAttribute("memberId");
        if (StringUtils.isBlank(memberId)){
            return "error";
        }
        String nickname = (String) request.getAttribute("nickname");
        //获取角色信息
        Role role = gameMapService.selectRoleByMemberId(memberId);
        Packsack packsack = packsackService.getPacksackByRoleId(role.getId());
        role.setLeaveExp(role.getRoleLeave()*role.getRoleLeave()*10);
        PackVo packVo = new PackVo();
        packsackVO2Po(packsack,packVo);

        List<List<GameMap<String>>> a = mapInitialize(wordId,request,role,monsterService);

        modelMap.put("a",a);
        role.setRound(0);
        modelMap.put("rounds",0);
        modelMap.put("role",role);
        modelMap.put("packVo",packVo);
        modelMap.put("nickname",nickname);
        modelMap.put("memberId",memberId);
        return "game/gameMap";
    }

    /**
     * 世界初始化
     */
    public static List<List<GameMap<String>>> mapInitialize(String wordId,HttpServletRequest request,Role role,MonsterService monsterService){
        if (wordId.equals("1")){
            Game game = new Game();
            return game.game1(request,role,monsterService);
        }
        return null;
    }

    /**
     * 背包转换
     * @param packsack
     * @param packVo
     */
    public static void packsackVO2Po(Packsack packsack,PackVo packVo){
        packVo.setId(packsack.getId());
        packVo.setRoleId(packsack.getRoleId());
        List<GameProp> propLists = packsack.getPropLists();
        List<List<GameProp>> propListss = new ArrayList<>();
        List<GameProp> props = new ArrayList<>();
        propListss.add(props);
        for (int i = 0;i<propLists.size();i++){
            if (i!=0&&i%10==0){
                propListss.add(props);
                props = new ArrayList<>();
            }
            GameProp gameProp = propLists.get(i);
            props.add(gameProp);
        }
        packVo.setPropLists(propListss);
        packVo.setUserPacksack(packsack.isUserPacksack());
    }

}
