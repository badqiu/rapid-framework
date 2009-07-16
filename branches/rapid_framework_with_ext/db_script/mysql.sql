CREATE TABLE AUTHORITIES
(
    ID                          INT(10) NOT NULL,
    NAME                        VARCHAR(20) NOT NULL,
    DISPLAY_NAME                VARCHAR(20) NOT NULL,
    CONSTRAINT SQL090603110639200 PRIMARY KEY (ID)
)

CREATE TABLE RESOURCES
(
    ID                          INT(10) NOT NULL,
    RESOURCE_TYPE               VARCHAR(20) NOT NULL,
    VALUE                       VARCHAR(255) NOT NULL,
    ORDER_NUM                   INT(10) NOT NULL,
    CONSTRAINT SQL090603110639480 PRIMARY KEY (ID)
)

CREATE TABLE RESOURCES_AUTHORITIES
(
    AUTHORITY_ID                INT(10) NOT NULL,
    RESOURCE_ID                 INT(10) NOT NULL,
    CONSTRAINT SQL090603110639560 FOREIGN KEY (AUTHORITY_ID) REFERENCES AUTHORITIES (ID),
    CONSTRAINT SQL090603110639561 FOREIGN KEY (RESOURCE_ID) REFERENCES RESOURCES (ID)
)


CREATE TABLE ROLES
(
    ID                          INT(10) NOT NULL,
    NAME                        VARCHAR(20) NOT NULL,
    CONSTRAINT SQL090603110638930 PRIMARY KEY (ID),
    CONSTRAINT SQL090603110638931 UNIQUE (NAME)
)


CREATE TABLE ROLES_AUTHORITIES
(
    ROLE_ID                     INT(10) NOT NULL,
    AUTHORITY_ID                INT(10) NOT NULL,
    CONSTRAINT SQL090603110639370 FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ID),
    CONSTRAINT SQL090603110639371 FOREIGN KEY (AUTHORITY_ID) REFERENCES AUTHORITIES (ID)
)


CREATE TABLE USERS
(
    ID                          INT(10) NOT NULL,
    LOGIN_NAME                  VARCHAR(20) NOT NULL,
    PASSWORD                    VARCHAR(20),
    NAME                        VARCHAR(20),
    EMAIL                       VARCHAR(30),
    CONSTRAINT SQL090603110638730 PRIMARY KEY (ID),
    CONSTRAINT SQL090603110638731 UNIQUE (LOGIN_NAME)
)


CREATE TABLE USERS_ROLES
(
    USER_ID                     INT(10) NOT NULL,
    ROLE_ID                     INT(10) NOT NULL,
    CONSTRAINT SQL090603110639060 FOREIGN KEY (ROLE_ID) REFERENCES ROLES (ID),
    CONSTRAINT SQL090603110639061 FOREIGN KEY (USER_ID) REFERENCES USERS (ID)
)

INSERT INTO AUTHORITIES ( ID, NAME, DISPLAY_NAME ) VALUES (1,'A_VIEW_USER','查看用户');

INSERT INTO AUTHORITIES ( ID, NAME, DISPLAY_NAME ) VALUES (2,'A_MODIFY_USER','管理用户');

INSERT INTO AUTHORITIES ( ID, NAME, DISPLAY_NAME ) VALUES (3,'A_VIEW_ROLE','查看角色');

INSERT INTO AUTHORITIES ( ID, NAME, DISPLAY_NAME ) VALUES (4,'A_MODIFY_ROLE','管理角色');

INSERT INTO RESOURCES ( ID, RESOURCE_TYPE, VALUE, ORDER_NUM ) VALUES (1,'url','/pages/Users/save*',1);

INSERT INTO RESOURCES ( ID, RESOURCE_TYPE, VALUE, ORDER_NUM ) VALUES (2,'url','/pages/Users/delete*',2);

INSERT INTO RESOURCES ( ID, RESOURCE_TYPE, VALUE, ORDER_NUM ) VALUES (3,'url','/pages/Users/*',3);

INSERT INTO RESOURCES ( ID, RESOURCE_TYPE, VALUE, ORDER_NUM ) VALUES (4,'url','/pages/Roles/save*',4);

INSERT INTO RESOURCES ( ID, RESOURCE_TYPE, VALUE, ORDER_NUM ) VALUES (5,'url','/pages/Roles/delete*',5);

INSERT INTO RESOURCES ( ID, RESOURCE_TYPE, VALUE, ORDER_NUM ) VALUES (6,'url','/pages/Roles/*',6);


INSERT INTO RESOURCES_AUTHORITIES ( AUTHORITY_ID, RESOURCE_ID ) VALUES (2,1);

INSERT INTO RESOURCES_AUTHORITIES ( AUTHORITY_ID, RESOURCE_ID ) VALUES (2,2);

INSERT INTO RESOURCES_AUTHORITIES ( AUTHORITY_ID, RESOURCE_ID ) VALUES (1,3);

INSERT INTO RESOURCES_AUTHORITIES ( AUTHORITY_ID, RESOURCE_ID ) VALUES (4,4);

INSERT INTO RESOURCES_AUTHORITIES ( AUTHORITY_ID, RESOURCE_ID ) VALUES (4,5);

INSERT INTO RESOURCES_AUTHORITIES ( AUTHORITY_ID, RESOURCE_ID ) VALUES (3,6);

INSERT INTO ROLES ( ID, NAME ) VALUES (1,'管理员');

INSERT INTO ROLES ( ID, NAME ) VALUES (2,'用户');

INSERT INTO ROLES_AUTHORITIES ( ROLE_ID, AUTHORITY_ID ) VALUES (1,1);

INSERT INTO ROLES_AUTHORITIES ( ROLE_ID, AUTHORITY_ID ) VALUES (1,2);

INSERT INTO ROLES_AUTHORITIES ( ROLE_ID, AUTHORITY_ID ) VALUES (1,3);

INSERT INTO ROLES_AUTHORITIES ( ROLE_ID, AUTHORITY_ID ) VALUES (1,4);

INSERT INTO ROLES_AUTHORITIES ( ROLE_ID, AUTHORITY_ID ) VALUES (2,1);

INSERT INTO ROLES_AUTHORITIES ( ROLE_ID, AUTHORITY_ID ) VALUES (2,3);

INSERT INTO USERS ( ID, LOGIN_NAME, PASSWORD, NAME, EMAIL ) VALUES (1,'admin','admin','Admin','admin@springside.org.cn');

INSERT INTO USERS ( ID, LOGIN_NAME, PASSWORD, NAME, EMAIL ) VALUES (2,'user','user','User','user@springside.org.cn');

INSERT INTO USERS ( ID, LOGIN_NAME, PASSWORD, NAME, EMAIL ) VALUES (3,'user2','user2','Jack','jack@springside.org.cn');

INSERT INTO USERS ( ID, LOGIN_NAME, PASSWORD, NAME, EMAIL ) VALUES (4,'user3','user3','Kate','kate@springside.org.cn');

INSERT INTO USERS ( ID, LOGIN_NAME, PASSWORD, NAME, EMAIL ) VALUES (5,'user4','user4','Sawyer','sawyer@springside.org.cn');

INSERT INTO USERS ( ID, LOGIN_NAME, PASSWORD, NAME, EMAIL ) VALUES (6,'user5','user5','Ben','ben@springside.org.cn');

INSERT INTO USERS_ROLES ( USER_ID, ROLE_ID ) VALUES (1,1);

INSERT INTO USERS_ROLES ( USER_ID, ROLE_ID ) VALUES (1,2);

INSERT INTO USERS_ROLES ( USER_ID, ROLE_ID ) VALUES (2,2);

INSERT INTO USERS_ROLES ( USER_ID, ROLE_ID ) VALUES (3,2);

INSERT INTO USERS_ROLES ( USER_ID, ROLE_ID ) VALUES (4,2);

INSERT INTO USERS_ROLES ( USER_ID, ROLE_ID ) VALUES (5,2);

INSERT INTO USERS_ROLES ( USER_ID, ROLE_ID ) VALUES (6,2);

