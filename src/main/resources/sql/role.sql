/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50614
Source Host           : 127.0.0.1:3312
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2017-08-24 09:06:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `rname` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '上传文件');
INSERT INTO `role` VALUES ('2', '下载文件');
