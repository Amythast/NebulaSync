/*
Navicat MySQL Data Transfer

Source Server         : 本机root
Source Server Version : 80016
Source Host           : localhost:3306
Source Database       : taroco-authentication

Target Server Type    : MYSQL
Target Server Version : 80016
File Encoding         : 65001

Date: 2019-07-26 17:10:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for flyway_schema_history
-- ----------------------------
DROP TABLE IF EXISTS `flyway_schema_history`;
CREATE TABLE `flyway_schema_history` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `flyway_schema_history_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of flyway_schema_history
-- ----------------------------
INSERT INTO `flyway_schema_history` VALUES ('1', '20190703.10.30', 'init', 'SQL', 'V20190703.10.30__init.sql', '-1596339199', 'taroco-authentication', '2019-07-16 17:18:45', '432', '1');
INSERT INTO `flyway_schema_history` VALUES ('2', '20190722.10.00', 'add RememberMe', 'SQL', 'V20190722.10.00__add_RememberMe.sql', '-1273418332', 'taroco-authentication', '2019-07-22 10:11:27', '1666', '1');

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details` (
  `client_id` varchar(40) NOT NULL,
  `app_name` varchar(50) NOT NULL,
  `resource_ids` varchar(256) DEFAULT NULL,
  `client_secret` varchar(256) DEFAULT NULL,
  `scope` varchar(256) DEFAULT NULL,
  `authorized_grant_types` varchar(256) DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) DEFAULT NULL,
  `authorities` varchar(256) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT NULL,
  `refresh_token_validity` int(11) DEFAULT NULL,
  `additional_information` varchar(4096) DEFAULT NULL,
  `autoapprove` varchar(256) DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details` VALUES ('5d22eb6e8b0c7ba066014398', 'taroco', null, '$2a$10$499/h5FHgttabN6PPbrXoOJCDdkvQCnPe3zAhwlZrKyudddXrQXhy', 'All', 'password,refresh_token', 'http://127.0.0.1:9000/login', null, null, null, null, 'true');
INSERT INTO `oauth_client_details` VALUES ('5d29de44b87ce391dd78ce59', '客户端1', null, '$2a$10$sACBWktOBUbVBBsX/ilFa.vnWeFpjuL1PMPmjZ32W/mBS4hcxVuiu', 'All', 'password,authorization_code,refresh_token,client_credentials,implicit', 'http://127.0.0.1:9003/client1/login', null, null, null, null, 'true');
INSERT INTO `oauth_client_details` VALUES ('5d29de5eb87ce391dd78ce5a', '客户端2', null, '$2a$10$.7lmismtyamElFZ4hk1lJOpLgjleA9In4BQ.E8UhnoyAGAf.c.nru', 'All', 'password,authorization_code,refresh_token,client_credentials,implicit', 'http://127.0.0.1:9004/client2/login', null, null, null, null, 'true');
INSERT INTO `oauth_client_details` VALUES ('5d29de7ab87ce391dd78ce5b', '资源服务器1', null, '$2a$10$Yb4MwXXi48sami3iIoUDlOpCabyjz0JcnyFinnRAakQ03ba3U7GM.', 'All', 'password,authorization_code,refresh_token,client_credentials,implicit', 'http://127.0.0.1:9005', null, null, null, null, 'true');

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins` (
  `username` varchar(64) NOT NULL,
  `series` varchar(64) NOT NULL,
  `token` varchar(64) NOT NULL,
  `last_used` timestamp NOT NULL,
  PRIMARY KEY (`series`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of persistent_logins
-- ----------------------------
