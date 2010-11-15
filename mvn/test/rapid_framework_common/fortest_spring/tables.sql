/*
MySQL Data Transfer
Source Host: localhost
Source Database: test
Target Host: localhost
Target Database: test
Date: 2009-8-15 0:44:19
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for role
-- ----------------------------
CREATE TABLE `role` (
  `role_id` bigint(20) NOT NULL auto_increment,
  `role_name` varchar(50) default NULL,
  `resource_id` bigint(20) default NULL,
  PRIMARY KEY  (`role_id`),
  KEY `fk92828` (`resource_id`),
  CONSTRAINT `fk92828` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`resource_id`)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'role1', '1');
INSERT INTO `role` VALUES ('2', null, null);


/*
MySQL Data Transfer
Source Host: localhost
Source Database: test
Target Host: localhost
Target Database: test
Date: 2009-8-15 0:44:13
*/

-- ----------------------------
-- Table structure for resource
-- ----------------------------
CREATE TABLE `resource` (
  `resource_id` bigint(20) NOT NULL auto_increment,
  `resource_name` varchar(50) default NULL,
  PRIMARY KEY  (`resource_id`)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `resource` VALUES ('1', 'resource_1');
INSERT INTO `resource` VALUES ('2', null);
