create database artifactory;
GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER,INDEX on artifactory.* TO 'artifactory_user'@'localhost' IDENTIFIED BY 'password';
flush privileges;

