DROP DATABASE IF EXISTS `@DATABASE@`;
CREATE DATABASE `@DATABASE@`;
GRANT ALL ON `@DATABASE@`.* TO '@ALFRESCO_USER@'@'@MARIADB_HOST@' IDENTIFIED BY '@ALFRESCO_PWD@' WITH GRANT OPTION MAX_QUERIES_PER_HOUR 0 MAX_CONNECTIONS_PER_HOUR 0 MAX_UPDATES_PER_HOUR 0 MAX_USER_CONNECTIONS 0;
