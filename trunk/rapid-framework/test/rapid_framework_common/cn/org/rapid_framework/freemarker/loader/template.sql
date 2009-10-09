/*
MySQL Data Transfer
Source Host: localhost
Source Database: test
Target Host: localhost
Target Database: test
Date: 2009-10-9 11:24:59
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for template
-- ----------------------------
DROP TABLE IF EXISTS `template`;
CREATE TABLE `template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `template_name` varchar(255) DEFAULT NULL,
  `template_content` varchar(255) DEFAULT NULL,
  `last_modified` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `template` VALUES ('1', 'test/template.ftl', 'test ${username} ${sex}', '2009-08-26 11:16:44');
