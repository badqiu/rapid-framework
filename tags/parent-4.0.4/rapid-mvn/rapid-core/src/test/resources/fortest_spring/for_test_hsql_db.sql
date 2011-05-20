CREATE TABLE template (
  id bigint PRIMARY KEY,
  template_name varchar(255) ,
  template_content varchar(255) ,
  last_modified timestamp 
);
INSERT INTO template VALUES ('1', 'test/template.ftl', '用户名: ${username} 性别:${sex}', '2009-08-26 11:16:44');
