package com.fyt.rlife.rlife.controller;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.Game;
import com.fyt.rlife.rlife.game.GameCommon;
import com.fyt.rlife.rlife.game.RoleAttribute;
import com.fyt.rlife.rlife.game.gamestate.GameState;
import com.fyt.rlife.rlife.service.RoleService;
import com.fyt.rlife.rlife.util.RedisUtil;
import com.fyt.rlife.rlife.util.ResultEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: fanyitai
 * @Date: 2020/1/5 17:40
 * @Version 1.0
 */
@Controller
public class GameController {

    @Autowired
    RedisUtil redisUtil;

    @Resource
    RoleService roleService;

    /**
     * 游戏下一步
     * @param session
     * @param request
     * @param attr
     * @param roleId 角色Id
     * @param roleMoveSpeed 角色的移速
     * @return
     * @throws Exception
     */
    @RequestMapping("/next")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<List<List<GameMap<String>>>> next(HttpSession session,HttpServletRequest request, String attr, String roleId, String roleMoveSpeed) throws Exception {

        //角色标记
        Role role = new Role();
        //角色坐标标记
        String roleXY = null;
        //所有怪物的坐标标记
        Map<String,List<Monster>> mapMonster = new HashMap<>(200);
        Jedis jedis = redisUtil.getJedis();
        String[] split = attr.split(",");
        int i = Integer.parseInt(split[0]);
        int j = Integer.parseInt(split[1]);

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);

        if (game1GameMapLists==null){
            String game1GameMapListsStr = jedis.get("game1GameMapLists" + roleId);
            if(StringUtils.isNotBlank(game1GameMapListsStr)){
                game1GameMapLists = Game.getMapByRedis(game1GameMapListsStr);
            }
        }

        if (game1GameMapLists!=null){
            for (int x = 0;x<game1GameMapLists.length;x++){
                for (int y = 0;y<game1GameMapLists[x].length;y++){
                    //找到角色所在位置
                    if (game1GameMapLists[x][y].getData().getRole()!=null){
                        role = game1GameMapLists[x][y].getData().getRole();
                        roleXY = x+","+y;
                        if (Game.move(i,j,x,y,roleMoveSpeed)){ //删除角色
                            game1GameMapLists[x][y].getData().setRole(null);
                        }else {
                            //防止在计算超出距离前，先对地图的数据进行删除，所以需要先把存入集合的地址重新映射回地图
                            Game.mapMonster(mapMonster,game1GameMapLists);
                            return ResultEntity.failed("移动距离超出移速限制");
                        }
                    }
                    //找到所有怪物位置并进行删除
                    if (game1GameMapLists[x][y].getData().getMonsters()!=null&&game1GameMapLists[x][y].getData().getMonsters().size()!=0){
                        String monsterStr = x + "," + y;
                        mapMonster.put(monsterStr,game1GameMapLists[x][y].getData().getMonsters());
                        game1GameMapLists[x][y].getData().setMonsters(null);
                    }
                }
            }

            HashMap<String, Integer> aaa = new HashMap<>();
            aaa.put("愈合",-10);
            role.setMoveStateMap(aaa);

            int rounds = role.getRound();
            //怪物繁衍和进化
            if (rounds!=0&&rounds%30==0){
                Game.ReproductionAndEvolution(mapMonster);
            }
            role.setRound(rounds+1);

            //进行怪物随机移动
            StringBuilder fighting = new StringBuilder();
            mapMonster = Game.randomMove(mapMonster, roleXY,role,fighting);
            if (mapMonster==null){ //战斗判定
                jedis.del("game1GameMapLists" + roleId);
                jedis.del("role" + roleId);
                roleService.updateSurviveByRoleId(role);
                return ResultEntity.failed("角色死亡，游戏结束");
            }
            //根据字符串坐标进行地图映射
            Game.mapMonster(mapMonster,game1GameMapLists);

            //在新位置上添加角色
            game1GameMapLists[i][j].getData().setRole(role);

            GameState.moveStateMap(fighting,role);

            //保存到redis中
            String s = JSON.toJSONString(game1GameMapLists);
            jedis.set("game1GameMapLists"+roleId,s);
            String s1 = JSON.toJSONString(role);
            jedis.set("role" + roleId,s1);

            //保存到session中
            session.setAttribute("game1GameMapLists" + roleId,game1GameMapLists);
            session.setAttribute("role" + roleId,role);
            session.setMaxInactiveInterval(60*60*5);

            ResultEntity<List<List<GameMap<String>>>> listResultEntity = ResultEntity.successWithData(Game.game2gameMap(game1GameMapLists));
            listResultEntity.setMessage(fighting.toString());
            jedis.close();
            return listResultEntity;
        }else {
            jedis.close();
            return ResultEntity.failed("游戏记录不存在，请开启新游戏");
        }
    }

    /**
     * 前端获取角色数据
     * @param session
     * @param request
     * @param roleId
     * @return
     */
    @RequestMapping("/getRole")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> getRole(HttpSession session,HttpServletRequest request,String roleId){

        Role role = (Role)session.getAttribute("role" + roleId);
        return ResultEntity.successWithData(role);
    }

    /**
     * 对角色进行升级
     * @param session
     * @param request
     * @param roleId
     * @return
     */
    @RequestMapping("/leaveUP")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> leaveUP(HttpSession session,HttpServletRequest request,String roleId){

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);
        Integer exp = role.getExp();
        Integer leaveExp = role.getLeaveExp();
        if (exp>=leaveExp){
            exp = exp-leaveExp;
            role.setExp(exp);
            role.setRoleLeave(role.getRoleLeave()+1);
            role.setLeaveExp(role.getRoleLeave()*role.getRoleLeave()*10);
            role.setFreelyDistributable(role.getFreelyDistributable()+4);
        }else {
            return ResultEntity.failed("当前经验不足");
        }

        GameCommon.roleUpdate(game1GameMapLists,role);
        session.setAttribute("game1GameMapLists" + roleId,game1GameMapLists);
        session.setAttribute("role" + roleId,role);
        return ResultEntity.successWithData(role);
    }

    /**
     * 体质加点
     * @param session
     * @param request
     * @param roleId
     * @return
     */
    @RequestMapping("/physicalLittle")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> physicalLittle(HttpSession session,HttpServletRequest request,String roleId){

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        Integer freelyDistributable = role.getFreelyDistributable();
        if (freelyDistributable>0){
            freelyDistributable--;
            RoleAttribute.basePhysicalRange(role,1);
            role.setFreelyDistributable(freelyDistributable);
        }else {
            return ResultEntity.failed("可分配的自由点不足");
        }

        GameCommon.roleUpdate(game1GameMapLists,role);
        session.setAttribute("game1GameMapLists" + roleId,game1GameMapLists);
        session.setAttribute("role" + roleId,role);
        return ResultEntity.successWithData(role);
    }

    /**
     * 力量加点
     * @param session
     * @param request
     * @param roleId
     * @return
     */
    @RequestMapping("/powerLittle")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> powerLittle(HttpSession session,HttpServletRequest request,String roleId){

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        Integer freelyDistributable = role.getFreelyDistributable();
        if (freelyDistributable>0){
            freelyDistributable--;
            RoleAttribute.basePowerRange(role,1);
            role.setFreelyDistributable(freelyDistributable);
        }else {
            return ResultEntity.failed("可分配的自由点不足");
        }

        GameCommon.roleUpdate(game1GameMapLists,role);
        session.setAttribute("game1GameMapLists" + roleId,game1GameMapLists);
        session.setAttribute("role" + roleId,role);
        return ResultEntity.successWithData(role);
    }

    /**
     * 敏捷加点
     * @param session
     * @param request
     * @param roleId
     * @return
     */
    @RequestMapping("/agilityLittle")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> agilityLittle(HttpSession session,HttpServletRequest request,String roleId){

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        Integer freelyDistributable = role.getFreelyDistributable();
        if (freelyDistributable>0){
            freelyDistributable--;
            RoleAttribute.baseAgilityRange(role,1);
            role.setFreelyDistributable(freelyDistributable);
        }else {
            return ResultEntity.failed("可分配的自由点不足");
        }

        GameCommon.roleUpdate(game1GameMapLists,role);
        session.setAttribute("game1GameMapLists" + roleId,game1GameMapLists);
        session.setAttribute("role" + roleId,role);
        return ResultEntity.successWithData(role);
    }

    /**
     * 精神加点
     * @param session
     * @param request
     * @param roleId
     * @return
     */
    @RequestMapping("/mindLittle")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> mindLittle(HttpSession session,HttpServletRequest request,String roleId){

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        Integer freelyDistributable = role.getFreelyDistributable();
        if (freelyDistributable>0){
            freelyDistributable--;
            RoleAttribute.baseMindRange(role,1);
            role.setFreelyDistributable(freelyDistributable);
        }else {
            return ResultEntity.failed("可分配的自由点不足");
        }

        GameCommon.roleUpdate(game1GameMapLists,role);
        session.setAttribute("game1GameMapLists" + roleId,game1GameMapLists);
        session.setAttribute("role" + roleId,role);
        return ResultEntity.successWithData(role);
    }

}
