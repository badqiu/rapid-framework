CREATE TABLE USER_INFO (
  user_id bigint PRIMARY KEY,
  username varchar(50) NOT NULL UNIQUE,
  password varchar(50) DEFAULT NULL,
  birth_date date DEFAULT NULL,
  sex TINYINT DEFAULT NULL,
  age INTEGER DEFAULT NULL UNIQUE
);
CREATE TABLE role (
  role_id bigint NOT NULL PRIMARY KEY,
  role_name varchar(50) default NULL,
  resource_id bigint default NULL,
  user_id bigint default NULL,
  role_desc varchar(20) default NULL
);
CREATE TABLE blog (
  blog_id bigint PRIMARY KEY,
  username varchar(50) NOT NULL UNIQUE,
  created date DEFAULT NULL,
  modified date DEFAULT NULL,
  title varchar(10) default NULL,
  content varchar(56) default NULL,
  content_length int default NULL,
  status varchar(56) default NULL
);