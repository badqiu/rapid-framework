CREATE TABLE user_info (
  user_id bigint NOT NULL PRIMARY KEY,
  username varchar(50) DEFAULT NULL,
  password varchar(50) DEFAULT NULL,
  birth_date date DEFAULT NULL,
  sex TINYINT DEFAULT NULL,
  age INTEGER DEFAULT NULL
);

