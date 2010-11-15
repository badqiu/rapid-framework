CREATE TABLE USER_INFO (
  user_id number(10) PRIMARY KEY,
  username varchar(50) NOT NULL UNIQUE,
  password varchar(50) DEFAULT NULL,
  birth_date timestamp DEFAULT NULL,
  sex number(10) DEFAULT NULL,
  age number(10) DEFAULT NULL UNIQUE
);
