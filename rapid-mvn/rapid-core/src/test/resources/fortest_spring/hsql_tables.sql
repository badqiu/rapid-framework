-- ----------------------------
-- Table structure for resource
-- ----------------------------
CREATE TABLE resource (
  resource_id bigint NOT NULL,
  resource_name varchar(50) default NULL,
  PRIMARY KEY  (resource_id)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO resource VALUES ('1', 'resource_1');
INSERT INTO resource VALUES ('2', null);

-- ----------------------------
-- Table structure for role
-- ----------------------------
CREATE TABLE role (
  role_id bigint NOT NULL,
  role_name varchar(50) ,
  resource_id bigint ,
  PRIMARY KEY  (role_id),
  CONSTRAINT fk92828 FOREIGN KEY (resource_id) REFERENCES resource (resource_id)
);

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO role VALUES ('1', 'role1', '1');
INSERT INTO role VALUES ('2', null, null);


