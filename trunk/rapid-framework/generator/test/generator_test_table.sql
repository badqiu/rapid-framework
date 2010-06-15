CREATE TABLE USER_INFO (
  username varchar(50) NOT NULL UNIQUE,
  password varchar(50) DEFAULT NULL,
  birth_date date DEFAULT NULL,
  sex TINYINT DEFAULT NULL,
  age INTEGER DEFAULT NULL UNIQUE,
  user_id bigint NOT NULL PRIMARY KEY
)

