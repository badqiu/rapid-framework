CREATE TABLE `template` (
  id bigint NOT NULL PRIMARY KEY,
  template_name varchar(255) ,
  template_content varchar(255) ,
  last_modified timestamp NULL 
);
INSERT INTO template VALUES ('1', 'test/template.ftl', 'test ${username} ${sex}', '2009-08-26 11:16:44');
