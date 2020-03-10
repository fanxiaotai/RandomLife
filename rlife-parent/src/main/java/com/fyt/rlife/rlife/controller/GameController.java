package com.fyt.rlife.rlife.controller;

import com.alibaba.fastjson.JSON;
import com.fyt.rlife.rlife.annotation.RoleRequire;
import com.fyt.rlife.rlife.bean.Monster;
import com.fyt.rlife.rlife.bean.Role;
import com.fyt.rlife.rlife.bean.User;
import com.fyt.rlife.rlife.bean.game.Game1;
import com.fyt.rlife.rlife.bean.game.common.GameProp;
import com.fyt.rlife.rlife.bean.game.common.GameStore;
import com.fyt.rlife.rlife.bean.game.common.Reward;
import com.fyt.rlife.rlife.bean.game.config.BoxConfig;
import com.fyt.rlife.rlife.bean.game.config.MonsterConfig;
import com.fyt.rlife.rlife.bean.game.config.RoomConfig;
import com.fyt.rlife.rlife.bean.game.factory.GameVO;
import com.fyt.rlife.rlife.bean.vo.GameMap;
import com.fyt.rlife.rlife.game.Game;
import com.fyt.rlife.rlife.game.GameCommon;
import com.fyt.rlife.rlife.game.RewardS;
import com.fyt.rlife.rlife.game.RoleAttribute;
import com.fyt.rlife.rlife.game.gamestate.GameState;
import com.fyt.rlife.rlife.service.RoleService;
import com.fyt.rlife.rlife.service.UserService;
import com.fyt.rlife.rlife.util.GameUtils;
import com.fyt.rlife.rlife.util.RedisUtil;
import com.fyt.rlife.rlife.util.ResultEntity;
import com.fyt.rlife.rlife.util.RlifeUtil;
import com.fyt.rlife.rlife.util.game.Game1Utils;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @Author: fanyitai
 * @Date: 2020/1/5 17:40
 * @Version 1.0
 */
@Controller
@Transactional(rollbackFor = Exception.class)
public class GameController {

    @Resource
    RedisUtil redisUtil;
    @Resource
    RoleService roleService;
    @Resource
    UserService userService;
    @Resource
    MonsterConfig.MonsterGame1Map monsterGame1Map;
    @Resource
    MonsterConfig.SuffixClassList suffixClassList;
    @Resource
    BoxConfig.BoxMap boxMap;
    @Resource
    RoomConfig.RoomMap roomMap;

    /**
     * 游戏下一步
     * @param session
     * @param request
     * @param attr
     * @param roleId 角色Id
     * @return
     * @throws Exception
     */
    @RequestMapping("/next")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<GameMap<GameVO>[][]> next(HttpSession session, HttpServletRequest request, String attr, String roleId) throws Exception {

        //检查用户是否登陆已经是否拥有该id的角色
        ResultEntity<String> stringResultEntity = RlifeUtil.userLoginCheckAndRole(request,roleId,roleService);
        if (!stringResultEntity.getResult().equals(ResultEntity.SUCCESS)){
            LoggerFactory.getLogger(getClass()).error(stringResultEntity.getMessage());//打印错误日志
            return ResultEntity.failed(stringResultEntity.getMessage());
        }

        //获取地图
        GameMap<Game1>[][] gameMaps = (GameMap<Game1>[][]) session.getAttribute("game1GameMapLists"+roleId);
        if (gameMaps==null){
            return ResultEntity.failed("游戏不存在");
        }
       Role role = (Role) session.getAttribute("role"+roleId);
        if (role==null){
            return ResultEntity.failed("角色遗失，请重新开始游戏");
        }

        //获得角色移动后的坐标
        String[] split = attr.split(",");
        int roleX = Integer.parseInt(split[0]);
        int roleY = Integer.parseInt(split[1]);

        //准备装怪物的集合
        List<Monster> monsterList = new ArrayList<>();
        //准备战斗信息显示的缓冲字符
        StringBuilder stringBuilder = new StringBuilder();
        //遍历地图取出怪物和角色
        Random random = new Random();
        for (GameMap<Game1>[] gameMap : gameMaps) {
            for (GameMap<Game1> game1GameMap : gameMap) {
                Game1 game1 = game1GameMap.getData();
                //取出移动后的怪物，并将存活的放入集合中
                List<Monster> monsters = game1.getMonsters();
                if (monsters!=null&&monsters.size()!=0){
                    Iterator<Monster> iterator = monsters.iterator();
                    while (iterator.hasNext()){
                        Monster monster = iterator.next();
                        monster.move(roleX,roleY,stringBuilder,role,monsterList,random);
                        if (Game1Utils.monsterLifeGt0(monster)){
                            monsterList.add(monster);
                        }
                        iterator.remove();
                    }
                }
                //取出角色
                if (game1.getRole()!=null){
                    role = game1.getRole();
                    //根据距离消耗角色饥饿度
                    GameUtils.getFeedDegree(role,game1GameMap.getX(),game1GameMap.getY(),roleX,roleY,stringBuilder);
                    game1.setRole(null);
                }
            }
        }

        //将移动后的角色和怪物放回地图中，并触发相应地图的事件
        String info = Game1Utils.Game1NextGameMap(monsterList, role, stringBuilder, gameMaps, roleX, roleY);
        //触发角色的移动状态
        GameState.moveStateList(stringBuilder,role,null);

        //判断角色是否存活
        if (role.getSurvive()==1){
            //角色已死亡,更新数据库信息
            roleService.updateSurviveByRoleId(role,1);
            String memberId = stringResultEntity.getData();
            User user = userService.getUserByUserId(memberId);
            int i = role.getLayerNumber() * (role.getLayerNumber() - 1) * role.getDifficulty(); //层数*（层数-1）*难度
            userService.updateUserIntegral(user,i);
            //删除游戏缓存
            session.removeAttribute("game1GameMapLists"+roleId);
            session.removeAttribute("role"+roleId);
            Jedis jedis = redisUtil.getJedis();
            jedis.del("game1GameMapLists"+roleId);
            return ResultEntity.failed("角色已死亡");
        }

        //判断是否触发商店和下一层
        ResultEntity<GameMap<GameVO>[][]> resultEntityInfo = ResultEntity.successWithData(GameUtils.GameMapPO2VO(gameMaps));
        resultEntityInfo.setMessage(stringBuilder.toString());
        if (info.equals("下一层")){
            if (monsterList.size()==0){
                //返回奖励
                role.setReward(true);
                ResultEntity<GameMap<GameVO>[][]> resultEntity = ResultEntity.successWithData(GameUtils.GameMapPO2VO(gameMaps));
                resultEntity.setSecondMessage("allClearReward");
                return resultEntity;
            }else {
                //加载下一层地图
                role.setLayerNumber(role.getLayerNumber()+1);
                GameMap<Game1>[][] gameMaps1 = Game.game1(role, role.getDifficulty(), monsterGame1Map, suffixClassList, boxMap, roomMap);
                //下一层时保存地图存档
                Game1Utils.saveInfo(request,role,redisUtil,gameMaps1);
                ResultEntity<GameMap<GameVO>[][]> resultEntity = ResultEntity.successWithData(GameUtils.GameMapPO2VO(gameMaps1));
                resultEntity.setMessage(stringBuilder.toString());
                return resultEntity;
            }
        }else if (info.equals("商店")){
            resultEntityInfo.setSecondMessage("store");
            return resultEntityInfo;
        }

        return resultEntityInfo;
    }

    /**
     * 商店购买
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/storeBuy")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Integer> storeBuy(HttpServletRequest request, HttpSession session, String roleId, String attrId){
        if (RlifeUtil.userLoginCheckAndRole(request,roleId,roleService).getResult().equals(ResultEntity.SUCCESS)){
            GameStore gameStore = (GameStore) session.getAttribute("gameStore" + roleId);
            Role role = (Role)session.getAttribute("role" + roleId);
            Integer gold = role.getGold();
            GameProp[] gameProps = gameStore.getGameProps();
            String[] gamePropIds = JSON.parseObject(attrId, String[].class);
            Map<String, GameProp> gamePropMap = role.getGamePropMap();
            StringBuilder stringBuilder = new StringBuilder();
            for (String gamePropId : gamePropIds) {
                int index = Integer.parseInt(gamePropId.charAt(gamePropId.length() - 1)+"");
                GameProp gameProp = gameProps[index];
                Integer propPrice = gameProp.getPropPrice();
                if (gold>=propPrice){
                    gold -= propPrice;
                    if (gamePropMap.containsKey(gameProp.getId())){
                        GameProp gameProp1 = gamePropMap.get(gameProp.getId());
                        gameProp.setTheNumber(gameProp1.getTheNumber()+1);
                    }else {
                        gamePropMap.put(gameProp.getId(),gameProp);
                    }
                    stringBuilder.append("你购买了");
                    stringBuilder.append(gameProp.getPropName());
                    stringBuilder.append("&#10;");
                }else {
                    role.setGold(gold);
                    ResultEntity<Integer> resultEntity = ResultEntity.failed("金钱不够了");
                    resultEntity.setMessage(stringBuilder.toString());
                    return resultEntity;
                }
            }
            role.setGold(gold);
            ResultEntity<Integer> resultEntity = ResultEntity.successWithData(role.getGold());
            resultEntity.setMessage(stringBuilder.toString());
            return resultEntity;

        }
        return ResultEntity.failed("购买失败，下次再来");
    }

    /**
     * 触发商店
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/getStore")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<GameProp[]> getStore(HttpServletRequest request, HttpSession session, String roleId, String attr){
        if (RlifeUtil.userLoginCheckAndRole(request,roleId,roleService).getResult().equals(ResultEntity.SUCCESS)){
            GameMap<Game1>[][] gameMaps = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
            String[] split = attr.split(",");
            Game1 data = gameMaps[Integer.parseInt(split[0])][Integer.parseInt(split[1])].getData();
            GameStore store = data.getStore();
            if (store!=null){
                session.setAttribute("gameStore" + roleId,store);
                data.setStore(null);
                return ResultEntity.successWithData(store.getGameProps());
            }
        }
        return ResultEntity.failed("商店正在进货");
    }

    /**
     * 获取全清奖励
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/triggerAllClearReward")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<List<Reward>> triggerAllClearReward(HttpServletRequest request, HttpSession session, String roleId){
        if (RlifeUtil.userLoginCheckAndRole(request,roleId,roleService).getResult().equals(ResultEntity.SUCCESS)){
            Role role = (Role)session.getAttribute("role" + roleId);
            if (role.isReward()){
                int layerNumber = role.getLayerNumber();
                List<Reward> rewardList = new ArrayList<>();
                Random random = new Random();
                Set<Integer> set = new HashSet<>();
                while (set.size()<3) {
                    int i = random.nextInt(4) + 1;
                    set.add(i);
                }

                Iterator<Integer> iterator = set.iterator();
                while (iterator.hasNext()){
                    Integer rewardId = iterator.next();
                    //根据奖励的id和层数返回Reward对象
                    Reward reward = RewardS.getRewardByIdAndLayer(rewardId, layerNumber);
                    rewardList.add(reward);
                }
                role.setRewardList(rewardList);
                role.setReward(false);
                return ResultEntity.successWithData(rewardList);
            }

        }
        return ResultEntity.failed("正在获取全清奖励");
    }

    /**
     * 领取全清奖励，并返回下一层地图
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/getAllClearReward")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<GameMap<GameVO>[][]> getAllClearReward(HttpServletRequest request, HttpSession session, String roleId,int index){
        if (RlifeUtil.userLoginCheckAndRole(request,roleId,roleService).getResult().equals(ResultEntity.SUCCESS)){
            Role role = (Role)session.getAttribute("role" + roleId);
            List<Reward> rewardList = role.getRewardList();
            if (rewardList!=null){
                Reward reward = rewardList.get(index);
                StringBuilder stringBuilder = new StringBuilder();
                RewardS.getRewardByIndex(reward.getRewardNo(),role,stringBuilder);
                stringBuilder.append("&#10;");
                role.setRewardList(null);

                //加载下一层地图
                role.setLayerNumber(role.getLayerNumber()+1);
                GameMap<Game1>[][] gameMaps1 = Game.game1(role, role.getDifficulty(), monsterGame1Map, suffixClassList, boxMap, roomMap);
                //下一层时保存地图存档
                Game1Utils.saveInfo(request,role,redisUtil,gameMaps1);
                ResultEntity<GameMap<GameVO>[][]> resultEntity = ResultEntity.successWithData(GameUtils.GameMapPO2VO(gameMaps1));
                resultEntity.setMessage(stringBuilder.toString());
                return resultEntity;
            }
        }
        return ResultEntity.failed("正在获取全清奖励");
    }

    /**
     * 前端获取角色数据
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/getRole")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> getRole(HttpServletRequest request,HttpSession session,String roleId){
        if (RlifeUtil.userLoginCheckAndRole(request,roleId,roleService).getResult().equals(ResultEntity.SUCCESS)){
            Role role = (Role)session.getAttribute("role" + roleId);
            return ResultEntity.successWithData(role);
        }
        return ResultEntity.failed("正在加载角色信息");
    }

    /**
     * 对角色进行升级
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/leaveUP")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> leaveUP(HttpSession session,String roleId){

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
     * 最大生命加点
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/physicalLittle")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> physicalLittle(HttpSession session,String roleId){

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        Integer freelyDistributable = role.getFreelyDistributable();
        if (freelyDistributable>0){
            freelyDistributable--;
            RoleAttribute.lifeMaxRange(role,5);
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
     * 攻击加点
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/powerLittle")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> powerLittle(HttpSession session,String roleId){

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        Integer freelyDistributable = role.getFreelyDistributable();
        if (freelyDistributable>0){
            freelyDistributable--;
            RoleAttribute.attackRange(role,2);
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
     * 防御加点
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/agilityLittle")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> agilityLittle(HttpSession session,String roleId){

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        Integer freelyDistributable = role.getFreelyDistributable();
        if (freelyDistributable>0){
            freelyDistributable--;
            RoleAttribute.defenseRange(role,1);
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
     * 最大魔法加点
     * @param session
     * @param roleId
     * @return
     */
    @RequestMapping("/mindLittle")
    @RoleRequire(roles = 1)
    @ResponseBody
    public ResultEntity<Role> mindLittle(HttpSession session,String roleId){

        GameMap<Game1>[][] game1GameMapLists = (GameMap<Game1>[][])session.getAttribute("game1GameMapLists" + roleId);
        Role role = (Role)session.getAttribute("role" + roleId);

        Integer freelyDistributable = role.getFreelyDistributable();
        if (freelyDistributable>0){
            freelyDistributable--;
            RoleAttribute.magicMaxRange(role,5);
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
