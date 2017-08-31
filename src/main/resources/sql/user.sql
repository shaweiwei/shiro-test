/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50614
Source Host           : 127.0.0.1:3312
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50614
File Encoding         : 65001

Date: 2017-08-24 09:06:29
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `uname` varchar(255) DEFAULT NULL,
  `islock` int(11) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', '7player', '0', 'c5b7e4bf2f0a42e83085b039e156248a');
INSERT INTO `user` VALUES ('2', 'liubei', '0', 'c5b7e4bf2f0a42e83085b039e156248a');
