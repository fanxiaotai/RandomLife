/*
 Navicat MySQL Data Transfer

 Source Server         : 222
 Source Server Type    : MySQL
 Source Server Version : 50646
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50646
 File Encoding         : 65001

 Date: 02/03/2020 20:46:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for black_market
-- ----------------------------
DROP TABLE IF EXISTS `black_market`;
CREATE TABLE `black_market`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_id` int(11) NULL DEFAULT NULL COMMENT '道具id',
  `user_id` int(11) NULL DEFAULT NULL COMMENT '所属用户的id',
  `prop_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `prop_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '道具描述',
  `prop_price` int(10) NULL DEFAULT NULL COMMENT '道具上架价格',
  `prop_number` int(11) NULL DEFAULT NULL COMMENT '上架数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for box
-- ----------------------------
DROP TABLE IF EXISTS `box`;
CREATE TABLE `box`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `box_leave` int(10) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of box
-- ----------------------------
INSERT INTO `box` VALUES (1, 1);
INSERT INTO `box` VALUES (2, 2);

-- ----------------------------
-- Table structure for box_prop
-- ----------------------------
DROP TABLE IF EXISTS `box_prop`;
CREATE TABLE `box_prop`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `box_id` int(11) NULL DEFAULT NULL,
  `prop_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of box_prop
-- ----------------------------
INSERT INTO `box_prop` VALUES (1, 1, 1);
INSERT INTO `box_prop` VALUES (2, 1, 2);
INSERT INTO `box_prop` VALUES (3, 2, 1);

-- ----------------------------
-- Table structure for game_prop
-- ----------------------------
DROP TABLE IF EXISTS `game_prop`;
CREATE TABLE `game_prop`  (
  `id` int(11) NOT NULL,
  `prop_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '道具名称',
  `prop_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '道具描述',
  `prop_price` int(20) NULL DEFAULT NULL COMMENT '道具价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of game_prop
-- ----------------------------
INSERT INTO `game_prop` VALUES (10001, '肉丸', '回复20点血量', 0);
INSERT INTO `game_prop` VALUES (10002, '蜂蜜', '回复10点精神', 0);
INSERT INTO `game_prop` VALUES (10003, '小回复药', '回复5点血量', 0);
INSERT INTO `game_prop` VALUES (10005, '中回复药', '回复20点血量', 0);
INSERT INTO `game_prop` VALUES (30004, '金银花', '有着回复效果的药材', 0);
INSERT INTO `game_prop` VALUES (40001, '九转还魂丹', '复活一个已死亡的角色', 30);
INSERT INTO `game_prop` VALUES (40002, '改名卡', '为一个角色更改名字', 3000);
INSERT INTO `game_prop` VALUES (40101, '体质丸LV1', '为一个角色+1体质，只能使用2次，两次之后使用无效', 500);
INSERT INTO `game_prop` VALUES (40102, '体质丸LV2', '为一个角色+2体质，只能使用2次，两次之后使用无效', 1000);
INSERT INTO `game_prop` VALUES (40103, '体质丸LV3', '为一个角色+3体质，只能使用2次，两次之后使用无效', 1500);
INSERT INTO `game_prop` VALUES (40104, '体质丸LV4', '为一个角色+4体质，只能使用2次，两次之后使用无效', 2000);
INSERT INTO `game_prop` VALUES (40105, '体质丸LV5', '为一个角色+5体质，只能使用2次，两次之后使用无效', 2500);
INSERT INTO `game_prop` VALUES (40201, '力量丸LV1', '为一个角色+1力量，只能使用2次，两次之后使用无效', 500);
INSERT INTO `game_prop` VALUES (40202, '力量丸LV2', '为一个角色+2力量，只能使用2次，两次之后使用无效', 1000);
INSERT INTO `game_prop` VALUES (40203, '力量丸LV3', '为一个角色+3力量，只能使用2次，两次之后使用无效', 1500);
INSERT INTO `game_prop` VALUES (40204, '力量丸LV4', '为一个角色+4力量，只能使用2次，两次之后使用无效', 2000);
INSERT INTO `game_prop` VALUES (40205, '力量丸LV5', '为一个角色+5力量，只能使用2次，两次之后使用无效', 2500);
INSERT INTO `game_prop` VALUES (40301, '敏捷丸LV1', '为一个角色+1敏捷，只能使用2次，两次之后使用无效', 500);
INSERT INTO `game_prop` VALUES (40302, '敏捷丸LV2', '为一个角色+2敏捷，只能使用2次，两次之后使用无效', 1000);
INSERT INTO `game_prop` VALUES (40303, '敏捷丸LV3', '为一个角色+3敏捷，只能使用2次，两次之后使用无效', 1500);
INSERT INTO `game_prop` VALUES (40304, '敏捷丸LV4', '为一个角色+4敏捷，只能使用2次，两次之后使用无效', 2000);
INSERT INTO `game_prop` VALUES (40305, '敏捷丸LV5', '为一个角色+5敏捷，只能使用2次，两次之后使用无效', 2500);
INSERT INTO `game_prop` VALUES (40401, '精神丸LV1', '为一个角色+1精神，只能使用2次，两次之后使用无效', 500);
INSERT INTO `game_prop` VALUES (40402, '精神丸LV2', '为一个角色+2精神，只能使用2次，两次之后使用无效', 1000);
INSERT INTO `game_prop` VALUES (40403, '精神丸LV3', '为一个角色+3精神，只能使用2次，两次之后使用无效', 1500);
INSERT INTO `game_prop` VALUES (40404, '精神丸LV4', '为一个角色+4精神，只能使用2次，两次之后使用无效', 2000);
INSERT INTO `game_prop` VALUES (40405, '精神丸LV5', '为一个角色+5精神，只能使用2次，两次之后使用无效', 2500);
INSERT INTO `game_prop` VALUES (41001, '升级卡', '使一个角色提升1级', 4000);
INSERT INTO `game_prop` VALUES (41002, '转生卡', '使一个角色全属性减半，但等级变为1级', 20000);
INSERT INTO `game_prop` VALUES (42001, '连击LV1', '使一个角色学习连击LV1技能', 2000);
INSERT INTO `game_prop` VALUES (42002, '强攻LV1', '使一个角色学习强攻LV1技能', 2000);
INSERT INTO `game_prop` VALUES (42003, '火球术LV1', '使一个角色学习火球LV1技能', 2000);
INSERT INTO `game_prop` VALUES (42101, '痊愈LV1', '使一个角色学习痊愈技能', 10000);
INSERT INTO `game_prop` VALUES (42102, '愈合LV1', '使一个角色学习愈合技能', 10000);

-- ----------------------------
-- Table structure for monster
-- ----------------------------
DROP TABLE IF EXISTS `monster`;
CREATE TABLE `monster`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `base_attack` int(30) NULL DEFAULT NULL COMMENT '基础攻击',
  `physical` int(30) NULL DEFAULT NULL COMMENT '体质',
  `power` int(30) NULL DEFAULT NULL COMMENT '力量',
  `agility` int(30) NULL DEFAULT NULL COMMENT '敏捷',
  `mind` int(30) NULL DEFAULT NULL COMMENT '精神',
  `base_defense` int(30) NULL DEFAULT NULL COMMENT '基础防御',
  `base_attack_speed` double(30, 2) NULL DEFAULT NULL COMMENT '基础攻速',
  `base_move_speed` int(30) NULL DEFAULT NULL COMMENT '基础移速',
  `base_life` int(30) NULL DEFAULT NULL COMMENT '基础生命',
  `base_magic` int(255) NULL DEFAULT NULL COMMENT '基础魔法',
  `base_physical` int(30) NULL DEFAULT NULL COMMENT '基础体质',
  `base_power` int(30) NULL DEFAULT NULL COMMENT '基础力量',
  `base_agility` int(30) NULL DEFAULT NULL COMMENT '基础敏捷',
  `base_mind` int(30) NULL DEFAULT NULL COMMENT '基础精神',
  `attack` int(30) NULL DEFAULT NULL COMMENT '攻击',
  `defense` int(30) NULL DEFAULT NULL COMMENT '防御',
  `attack_speed` double(30, 2) NULL DEFAULT NULL COMMENT '攻速',
  `move_speed` int(30) NULL DEFAULT NULL COMMENT '移速',
  `life` int(30) NULL DEFAULT NULL COMMENT '生命',
  `magic` int(255) NULL DEFAULT NULL COMMENT '魔法',
  `def` int(30) NULL DEFAULT NULL COMMENT '魔防',
  `base_def` int(30) NULL DEFAULT NULL COMMENT '基础魔防',
  `tenacity` int(30) NULL DEFAULT NULL COMMENT '韧性',
  `amplification` int(30) NULL DEFAULT NULL COMMENT '全属性增幅',
  `attack_amplification` int(30) NULL DEFAULT NULL COMMENT '基础攻击增幅',
  `defense_amplification` int(30) NULL DEFAULT NULL COMMENT '基础防御增幅',
  `attack_speed_amplification` int(30) NULL DEFAULT NULL COMMENT '基础攻速增幅',
  `move_speed_amplification` int(30) NULL DEFAULT NULL COMMENT '基础移速增幅',
  `life_amplification` int(30) NULL DEFAULT NULL COMMENT '基础生命增幅',
  `magic_amplification` int(255) NULL DEFAULT NULL COMMENT '基础魔法增幅',
  `def_amplification` int(30) NULL DEFAULT NULL COMMENT '基础魔防增幅',
  `monster_exp` int(30) NULL DEFAULT NULL COMMENT '击败后可获得经验',
  `monster_leave` int(30) NULL DEFAULT NULL COMMENT '怪物等级',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of monster
-- ----------------------------
INSERT INTO `monster` VALUES (1, '小蜘蛛', 3, 2, 1, 3, 0, 0, 0.15, 1, 20, 0, 2, 1, 3, 0, 3, 0, 0.15, 1, 20, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 1);
INSERT INTO `monster` VALUES (2, '牛', 8, 10, 7, 2, 1, 2, 0.10, 1, 100, 10, 10, 7, 2, 1, 8, 2, 0.10, 1, 100, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 4);
INSERT INTO `monster` VALUES (3, '猪人', 10, 8, 8, 8, 8, 2, 0.40, 1, 80, 80, 8, 8, 8, 8, 10, 2, 0.40, 1, 80, 80, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 4);
INSERT INTO `monster` VALUES (4, '触手', 8, 10, 8, 1, 5, 2, 0.05, 1, 100, 50, 10, 8, 1, 5, 8, 2, 0.05, 1, 100, 50, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 8, 4);

-- ----------------------------
-- Table structure for packsack
-- ----------------------------
DROP TABLE IF EXISTS `packsack`;
CREATE TABLE `packsack`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(30) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of packsack
-- ----------------------------
INSERT INTO `packsack` VALUES (1, 1);

-- ----------------------------
-- Table structure for packsack_prop
-- ----------------------------
DROP TABLE IF EXISTS `packsack_prop`;
CREATE TABLE `packsack_prop`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `packsack_id` int(11) NULL DEFAULT NULL,
  `prop_id` int(11) NULL DEFAULT NULL,
  `the_number` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of packsack_prop
-- ----------------------------
INSERT INTO `packsack_prop` VALUES (1, 1, 10001, 3);
INSERT INTO `packsack_prop` VALUES (2, 1, 10002, 1);

-- ----------------------------
-- Table structure for packsack_user
-- ----------------------------
DROP TABLE IF EXISTS `packsack_user`;
CREATE TABLE `packsack_user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(30) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of packsack_user
-- ----------------------------
INSERT INTO `packsack_user` VALUES (1, 1);

-- ----------------------------
-- Table structure for packsack_user_prop
-- ----------------------------
DROP TABLE IF EXISTS `packsack_user_prop`;
CREATE TABLE `packsack_user_prop`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `packsack_user_id` int(11) NULL DEFAULT NULL,
  `prop_id` int(11) NULL DEFAULT NULL,
  `the_number` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of packsack_user_prop
-- ----------------------------
INSERT INTO `packsack_user_prop` VALUES (1, 1, 40001, 8);

-- ----------------------------
-- Table structure for prop_limit
-- ----------------------------
DROP TABLE IF EXISTS `prop_limit`;
CREATE TABLE `prop_limit`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) NULL DEFAULT NULL,
  `prop_id` int(11) NULL DEFAULT NULL,
  `the_number` int(11) NULL DEFAULT NULL COMMENT '已使用数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of prop_limit
-- ----------------------------
INSERT INTO `prop_limit` VALUES (1, 1, 40101, 2);

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `nickname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '角色名称',
  `role_leave` int(30) NULL DEFAULT NULL COMMENT '角色等级',
  `exp` int(30) NULL DEFAULT NULL COMMENT '经验值',
  `base_attack` int(30) NULL DEFAULT NULL COMMENT '基础攻击',
  `physical` int(30) NULL DEFAULT NULL COMMENT '体质',
  `power` int(30) NULL DEFAULT NULL COMMENT '力量',
  `agility` int(30) NULL DEFAULT NULL COMMENT '敏捷',
  `mind` int(30) NULL DEFAULT NULL COMMENT '精神',
  `freely_distributable` int(30) NULL DEFAULT NULL COMMENT '自由可分配属性点',
  `base_defense` int(30) NULL DEFAULT NULL COMMENT '基础防御',
  `base_attack_speed` double(30, 2) NULL DEFAULT NULL COMMENT '基础攻速',
  `base_move_speed` int(30) NULL DEFAULT NULL COMMENT '基础移速',
  `base_life` int(30) NULL DEFAULT NULL COMMENT '基础生命',
  `base_magic` int(30) NULL DEFAULT NULL COMMENT '基础魔法',
  `base_physical` int(30) NULL DEFAULT NULL COMMENT '基础体质',
  `base_power` int(30) NULL DEFAULT NULL COMMENT '基础力量',
  `base_agility` int(30) NULL DEFAULT NULL COMMENT '基础敏捷',
  `base_mind` int(30) NULL DEFAULT NULL COMMENT '基础精神',
  `attack` int(30) NULL DEFAULT NULL COMMENT '攻击',
  `defense` int(30) NULL DEFAULT NULL COMMENT '防御',
  `attack_speed` double(30, 2) NULL DEFAULT NULL COMMENT '攻速',
  `move_speed` int(30) NULL DEFAULT NULL COMMENT '移速',
  `life` int(30) NULL DEFAULT NULL COMMENT '生命',
  `magic` int(30) NULL DEFAULT NULL COMMENT '魔法',
  `def` int(30) NULL DEFAULT NULL COMMENT '魔防',
  `base_def` int(30) NULL DEFAULT NULL COMMENT '基础魔防',
  `tenacity` int(30) NULL DEFAULT NULL COMMENT '韧性',
  `amplification` int(30) NULL DEFAULT NULL COMMENT '全属性增幅',
  `attack_amplification` int(30) NULL DEFAULT NULL COMMENT '基础攻击增幅',
  `defense_amplification` int(30) NULL DEFAULT NULL COMMENT '基础防御增幅',
  `attack_speed_amplification` int(30) NULL DEFAULT NULL COMMENT '基础攻速增幅',
  `move_speed_amplification` int(30) NULL DEFAULT NULL COMMENT '基础移速增幅',
  `life_amplification` int(30) NULL DEFAULT NULL COMMENT '基础生命增幅',
  `magic_amplification` int(30) NULL DEFAULT NULL COMMENT '基础魔法增幅',
  `def_amplification` int(30) NULL DEFAULT NULL COMMENT '基础魔防增幅',
  `user_id` int(30) NULL DEFAULT NULL COMMENT '用户id',
  `default_role` int(1) NULL DEFAULT 0 COMMENT '是否为默认角色',
  `survive` int(30) NULL DEFAULT 0 COMMENT '0为可用，1为已死亡',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES (1, '测试角色', 1, 0, 4, 4, 3, 3, 3, 4, 0, 0.15, 1, 40, 30, 4, 3, 3, 3, 4, 0, 0.15, 1, 40, 30, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 1, 0);
INSERT INTO `role` VALUES (2, '测试角色2', 1, 0, 6, 5, 5, 5, 5, 0, 1, 0.25, 1, 50, 50, 5, 5, 5, 5, 6, 1, 0.25, 1, 50, 50, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0);
INSERT INTO `role` VALUES (3, '测试角色3', 1, 0, 6, 5, 5, 5, 5, 0, 1, 0.25, 1, 50, 50, 5, 5, 5, 5, 6, 1, 0.25, 1, 50, 50, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0);
INSERT INTO `role` VALUES (4, '测试角色4', 1, 0, 6, 5, 5, 5, 5, 0, 1, 0.25, 1, 50, 50, 5, 5, 5, 5, 6, 1, 0.25, 1, 50, 50, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0);
INSERT INTO `role` VALUES (5, 'aaa', 1, 0, 6, 5, 5, 5, 5, 0, 1, 0.25, 1, 50, 50, 5, 5, 5, 5, 6, 1, 0.25, 1, 50, 50, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0);

-- ----------------------------
-- Table structure for role_skill
-- ----------------------------
DROP TABLE IF EXISTS `role_skill`;
CREATE TABLE `role_skill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(30) NULL DEFAULT NULL COMMENT '角色id',
  `skill_id` int(11) NULL DEFAULT NULL COMMENT '技能id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role_skill
-- ----------------------------
INSERT INTO `role_skill` VALUES (1, 1, 101);
INSERT INTO `role_skill` VALUES (2, 1, 201);
INSERT INTO `role_skill` VALUES (3, 1, 301);
INSERT INTO `role_skill` VALUES (4, 1, 501);
INSERT INTO `role_skill` VALUES (5, 1, 401);

-- ----------------------------
-- Table structure for role_state
-- ----------------------------
DROP TABLE IF EXISTS `role_state`;
CREATE TABLE `role_state`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `state_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态名称',
  `state_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '状态描述',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 9 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of role_state
-- ----------------------------
INSERT INTO `role_state` VALUES (1, '连击LV1', '攻击有（敏捷*2%）的几率造成两次伤害，每次伤害为总攻击的（50%+力量*2%）');
INSERT INTO `role_state` VALUES (2, '连击LV2', '攻击有（敏捷*3%）的几率造成两次伤害，每次伤害为总攻击的（60%+力量*3%）');
INSERT INTO `role_state` VALUES (3, '连击LV3', '攻击有（敏捷*4%）的几率造成两次伤害，每次伤害为总攻击的（70%+力量*4%）');
INSERT INTO `role_state` VALUES (4, '连击LV4', '攻击有（敏捷*5%）的几率造成两次伤害，每次伤害为总攻击的（80%+力量*5%）');
INSERT INTO `role_state` VALUES (5, '连击LV5', '攻击有（敏捷*5%）的几率造成两次伤害，每次伤害为总攻击的（100%+力量*5%）,并且有（力量*5%几率产生暴击）');
INSERT INTO `role_state` VALUES (6, '痊愈LV1', '战斗结束后恢复(5+体质)点生命值');
INSERT INTO `role_state` VALUES (7, '愈合LV1', '每次移动恢复一点生命值');
INSERT INTO `role_state` VALUES (8, '强击LV1', '下三次攻击，增加20点攻击力');

-- ----------------------------
-- Table structure for skill
-- ----------------------------
DROP TABLE IF EXISTS `skill`;
CREATE TABLE `skill`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `skill_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '技能名称',
  `skill_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '技能描述',
  `skill_leave` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '技能等级',
  `skill_initiative` varchar(1) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '0' COMMENT '是否是主动技能：1是主动，0是被动',
  `skill_consume` int(30) NULL DEFAULT NULL COMMENT '技能消耗',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 502 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of skill
-- ----------------------------
INSERT INTO `skill` VALUES (101, '连击', '下次战斗时，攻击有（敏捷*2%）的几率造成两次伤害，每次伤害为总攻击的（50%+力量*2%）', '1', '1', 10);
INSERT INTO `skill` VALUES (102, '连击', '下次战斗时，攻击有（敏捷*3%）的几率造成两次伤害，每次伤害为总攻击的（60%+力量*3%）', '2', '1', 12);
INSERT INTO `skill` VALUES (103, '连击', '下次战斗时，攻击有（敏捷*4%）的几率造成两次伤害，每次伤害为总攻击的（70%+力量*4%）', '3', '1', 14);
INSERT INTO `skill` VALUES (104, '连击', '下次战斗时，攻击有（敏捷*5%）的几率造成两次伤害，每次伤害为总攻击的（80%+力量*5%）', '4', '1', 16);
INSERT INTO `skill` VALUES (105, '连击', '下次战斗时，攻击有（敏捷*5%）的几率造成两次伤害，每次伤害为总攻击的（100%+力量*5%），并且有（力量*5%几率产生暴击）', '5', '1', 20);
INSERT INTO `skill` VALUES (201, '痊愈', '每次战斗结束后，恢复(5+体质)点生命', '1', '0', 0);
INSERT INTO `skill` VALUES (301, '愈合', '每次移动后，恢复1点生命', '1', '0', 0);
INSERT INTO `skill` VALUES (401, '强攻', '下3次战斗，攻击增加20%', '1', '1', 10);
INSERT INTO `skill` VALUES (501, '火球术', '下次战斗时，先对敌人造成10+（精神*2）的伤害', '1', '1', 5);

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(30) NOT NULL COMMENT '用户id',
  `prop_id1` int(30) NULL DEFAULT NULL,
  `prop_id2` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `prop_id3` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `prop_id4` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `prop_id5` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `prop_id6` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NULL DEFAULT NULL,
  `last_update` datetime(0) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of store
-- ----------------------------
INSERT INTO `store` VALUES (7, 1, 40204, '40301', '40204', '40002', '42003', '40202', '2020-01-25 18:00:00');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '姓名',
  `userpswd` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `email` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '电话',
  `sex` int(1) NULL DEFAULT NULL COMMENT '性别',
  `birthday` datetime(0) NULL DEFAULT NULL COMMENT '出生年月日',
  `last_date` datetime(0) NULL DEFAULT NULL COMMENT '最后登陆日期',
  `reg_date` datetime(0) NULL DEFAULT NULL COMMENT '注册日期',
  `user_leave` int(8) NULL DEFAULT NULL COMMENT '等级',
  `integral` int(30) NULL DEFAULT NULL COMMENT '轮回积分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'aaa', '202cb962ac59075b964b07152d234b70', '1296603385@qq.com', '15918793234', NULL, NULL, NULL, '2019-12-29 19:19:33', 1, 5000);

-- ----------------------------
-- Table structure for word
-- ----------------------------
DROP TABLE IF EXISTS `word`;
CREATE TABLE `word`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '世界名称',
  `word_describe` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '世界描述',
  `award` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '奖励',
  `heat` int(50) NULL DEFAULT NULL COMMENT '热度',
  `good` int(50) NULL DEFAULT NULL COMMENT '点赞数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of word
-- ----------------------------
INSERT INTO `word` VALUES (1, '饥荒冒险', '永远昏沉的天空，一闪而过的黑影，饥寒的压迫，耳边的杂鸣，强壮的怪物，是敌人，还是伙伴？ 火焰，寒冰还有科技，在这样的世界中，你，能生存多久？', '经验，角色卡牌，道具', 3, 1);
INSERT INTO `word` VALUES (2, '暂无', '暂时没有故事，等你来创作', '无', 1, 0);
INSERT INTO `word` VALUES (3, '暂无', '暂时没有故事，等你来创作', '无', 2, 0);
INSERT INTO `word` VALUES (4, '暂无', '暂时没有故事，等你来创作', '无', 2, 0);

SET FOREIGN_KEY_CHECKS = 1;
