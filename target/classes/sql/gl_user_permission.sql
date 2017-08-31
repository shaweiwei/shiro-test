/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50614
Source Host           : 127.0.0.1:3312
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2017-08-24 09:05:48
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for gl_user_permission
-- ----------------------------
DROP TABLE IF EXISTS `gl_user_permission`;
CREATE TABLE `gl_user_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) DEFAULT NULL,
  `permission_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gl_user_permission_user` (`user_id`),
  KEY `gl_user_permission_permission` (`permission_id`),
  CONSTRAINT `gl_user_permission_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`),
  CONSTRAINT `gl_user_permission_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gl_user_permission
-- ----------------------------
INSERT INTO `gl_user_permission` VALUES ('1', '1', '1');
INSERT INTO `gl_user_permission` VALUES ('2', '2', '1');
