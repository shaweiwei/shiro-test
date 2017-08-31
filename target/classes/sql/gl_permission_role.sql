/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50614
Source Host           : 127.0.0.1:3312
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2017-08-24 09:05:40
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for gl_permission_role
-- ----------------------------
DROP TABLE IF EXISTS `gl_permission_role`;
CREATE TABLE `gl_permission_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `permission_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `gl_permission_role_permission` (`permission_id`),
  KEY `gl_permission_role_role` (`role_id`),
  CONSTRAINT `gl_permission_role_permission` FOREIGN KEY (`permission_id`) REFERENCES `permission` (`id`),
  CONSTRAINT `gl_permission_role_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of gl_permission_role
-- ----------------------------
INSERT INTO `gl_permission_role` VALUES ('1', '1', '1');
INSERT INTO `gl_permission_role` VALUES ('2', '2', '2');
INSERT INTO `gl_permission_role` VALUES ('3', '1', '2');
INSERT INTO `gl_permission_role` VALUES ('4', '2', '1');
