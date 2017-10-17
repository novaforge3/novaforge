GRANT USAGE ON *.* TO '@userJiraMysql@'@'localhost';
DROP USER '@userJiraMysql@'@'localhost';
CREATE USER '@userJiraMysql@'@'localhost' IDENTIFIED BY '@passJiraMysql@';

DROP DATABASE IF EXISTS @databaseJiraMysql@;
CREATE DATABASE @databaseJiraMysql@ CHARACTER SET utf8 COLLATE utf8_bin;

GRANT SELECT,INSERT,UPDATE,DELETE,CREATE,DROP,ALTER,INDEX on @databaseJiraMysql@.* TO '@userJiraMysql@'@'localhost' IDENTIFIED BY '@passJiraMysql@';
FLUSH PRIVILEGES;
