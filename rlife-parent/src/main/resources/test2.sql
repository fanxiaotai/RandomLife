/*
 Navicat Premium Data Transfer

 Source Server         : mysql
 Source Server Type    : MySQL
 Source Server Version : 50639
 Source Host           : localhost:3306
 Source Schema         : test2

 Target Server Type    : MySQL
 Target Server Version : 50639
 File Encoding         : 65001

 Date: 22/06/2020 18:41:57
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for black_market
-- ----------------------------
DROP TABLE IF EXISTS `black_market`;
CREATE TABLE `black_market` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `prop_id` int(11) DEFAULT NULL COMMENT '道具id',
  `user_id` int(11) DEFAULT NULL COMMENT '所属用户的id',
  `prop_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `prop_describe` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '道具描述',
  `prop_price` int(10) DEFAULT NULL COMMENT '道具上架价格',
  `prop_number` int(11) DEFAULT NULL COMMENT '上架数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for game_prop
-- ----------------------------
DROP TABLE IF EXISTS `game_prop`;
CREATE TABLE `game_prop` (
  `id` int(11) NOT NULL,
  `prop_name` varchar(30) CHARACTER SET utf8 DEFAULT NULL COMMENT '道具名称',
  `prop_describe` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '道具描述',
  `prop_price` int(20) DEFAULT NULL COMMENT '道具价格',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of game_prop
-- ----------------------------
BEGIN;
INSERT INTO `game_prop` VALUES (1, '连击LV1', '使一个角色学习连击LV1技能', 2000);
INSERT INTO `game_prop` VALUES (2, '强攻LV1', '使一个角色学习强攻LV1技能', 2000);
INSERT INTO `game_prop` VALUES (3, '火球术LV1', '使一个角色学习火球LV1技能', 2000);
INSERT INTO `game_prop` VALUES (4, '痊愈LV1', '使一个角色学习痊愈技能', 10000);
INSERT INTO `game_prop` VALUES (5, '愈合LV1', '使一个角色学习愈合技能', 10000);
INSERT INTO `game_prop` VALUES (6, '复活丹', '使一个角色复活', 100);
COMMIT;

-- ----------------------------
-- Table structure for packsack_user
-- ----------------------------
DROP TABLE IF EXISTS `packsack_user`;
CREATE TABLE `packsack_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(30) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of packsack_user
-- ----------------------------
BEGIN;
INSERT INTO `packsack_user` VALUES (1, 1);
COMMIT;

-- ----------------------------
-- Table structure for packsack_user_prop
-- ----------------------------
DROP TABLE IF EXISTS `packsack_user_prop`;
CREATE TABLE `packsack_user_prop` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `packsack_user_id` int(11) DEFAULT NULL,
  `prop_id` int(11) DEFAULT NULL,
  `the_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of packsack_user_prop
-- ----------------------------
BEGIN;
INSERT INTO `packsack_user_prop` VALUES (1, 1, 6, 7);
COMMIT;

-- ----------------------------
-- Table structure for prop_limit
-- ----------------------------
DROP TABLE IF EXISTS `prop_limit`;
CREATE TABLE `prop_limit` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(11) DEFAULT NULL,
  `prop_id` int(11) DEFAULT NULL,
  `the_number` int(11) DEFAULT NULL COMMENT '已使用数量',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `member_id` int(11) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `life` int(30) DEFAULT NULL,
  `life_max` int(30) DEFAULT NULL,
  `magic` int(30) DEFAULT NULL,
  `magic_max` int(30) DEFAULT NULL,
  `attack` int(30) DEFAULT NULL,
  `defense` int(30) DEFAULT NULL,
  `role_leave` int(30) DEFAULT NULL,
  `exp` int(30) DEFAULT NULL,
  `freely_distributable` int(30) DEFAULT NULL,
  `gold` int(30) DEFAULT NULL,
  `skill_points` int(30) DEFAULT NULL,
  `default_role` int(30) DEFAULT NULL,
  `survive` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of role
-- ----------------------------
BEGIN;
INSERT INTO `role` VALUES (1, 1, 'aaa', 80, 80, 20, 20, 20, 10, 1, 0, 0, 0, 0, 1, 0);
INSERT INTO `role` VALUES (2, 1, 'bbb', 30, 30, 50, 50, 4, 9, 1, 0, 0, 0, 0, 0, 0);
COMMIT;

-- ----------------------------
-- Table structure for role_skill
-- ----------------------------
DROP TABLE IF EXISTS `role_skill`;
CREATE TABLE `role_skill` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role_id` int(30) DEFAULT NULL COMMENT '角色id',
  `skill_id` int(11) DEFAULT NULL COMMENT '技能id',
  `skill_leave` int(255) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of role_skill
-- ----------------------------
BEGIN;
INSERT INTO `role_skill` VALUES (1, 1, 1, 2);
COMMIT;

-- ----------------------------
-- Table structure for store
-- ----------------------------
DROP TABLE IF EXISTS `store`;
CREATE TABLE `store` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(30) NOT NULL COMMENT '用户id',
  `prop_id1` int(30) DEFAULT NULL,
  `prop_id2` varchar(255) DEFAULT NULL,
  `prop_id3` varchar(255) DEFAULT NULL,
  `prop_id4` varchar(255) DEFAULT NULL,
  `prop_id5` varchar(255) DEFAULT NULL,
  `prop_id6` varchar(255) DEFAULT NULL,
  `last_update` datetime DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of store
-- ----------------------------
BEGIN;
INSERT INTO `store` VALUES (7, 1, 40002, '40301', '40203', '40202', '40402', '42003', '2020-03-03 12:00:00');
COMMIT;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '姓名',
  `userpswd` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '密码',
  `email` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(50) CHARACTER SET utf8 DEFAULT NULL COMMENT '电话',
  `sex` int(1) DEFAULT NULL COMMENT '性别',
  `birthday` datetime DEFAULT NULL COMMENT '出生年月日',
  `last_date` datetime DEFAULT NULL COMMENT '最后登陆日期',
  `reg_date` datetime DEFAULT NULL COMMENT '注册日期',
  `user_leave` int(8) DEFAULT NULL COMMENT '等级',
  `integral` int(30) DEFAULT NULL COMMENT '轮回积分',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user
-- ----------------------------
BEGIN;
INSERT INTO `user` VALUES (1, 'aaa', '202cb962ac59075b964b07152d234b70', '1296603385@qq.com', '15918793234', NULL, NULL, '2020-06-18 11:13:28', '2019-12-29 19:19:33', 1, 36500);
INSERT INTO `user` VALUES (2, 'bbb', '456', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
INSERT INTO `user` VALUES (3, 'bbb', '456', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for word
-- ----------------------------
DROP TABLE IF EXISTS `word`;
CREATE TABLE `word` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `word_name` varchar(200) CHARACTER SET utf8 DEFAULT NULL COMMENT '世界名称',
  `word_describe` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '世界描述',
  `award` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '奖励',
  `heat` int(50) DEFAULT NULL COMMENT '热度',
  `good` int(50) DEFAULT NULL COMMENT '点赞数',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of word
-- ----------------------------
BEGIN;
INSERT INTO `word` VALUES (1, '饥荒冒险', '永远昏沉的天空，一闪而过的黑影，饥寒的压迫，耳边的杂鸣，强壮的怪物，是敌人，还是伙伴？ 火焰，寒冰还有科技，在这样的世界中，你，能生存多久？', '经验，角色卡牌，道具', 3, 1);
INSERT INTO `word` VALUES (2, '暂无', '暂时没有故事，等你来创作', '无', 1, 0);
INSERT INTO `word` VALUES (3, '暂无', '暂时没有故事，等你来创作', '无', 2, 0);
INSERT INTO `word` VALUES (4, '暂无', '暂时没有故事，等你来创作', '无', 2, 0);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
