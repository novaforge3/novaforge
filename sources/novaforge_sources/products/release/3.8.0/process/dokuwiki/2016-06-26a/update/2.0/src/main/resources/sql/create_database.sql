DROP DATABASE IF EXISTS `@DATABASE@`;
CREATE USER '@DOKUWIKI_USER@'@'@MARIADB_HOST@' IDENTIFIED BY  '@DOKUWIKI_PWD@';
CREATE DATABASE  `@DATABASE@`;
GRANT ALL PRIVILEGES ON  `@DATABASE@` . * TO  '@DOKUWIKI_USER@'@'@MARIADB_HOST@' WITH GRANT OPTION ;

use @DATABASE@;

CREATE TABLE IF NOT EXISTS `groups` (
  `gid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL DEFAULT '',
  PRIMARY KEY (`gid`),
  UNIQUE KEY `name` (`name`)
) ENGINE=MyISAM;


CREATE TABLE IF NOT EXISTS `usergroup` (
  `uid` int(10) unsigned NOT NULL DEFAULT '0',
  `gid` int(10) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`uid`,`gid`)
) ENGINE=MyISAM;

CREATE TABLE IF NOT EXISTS `users` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `login` varchar(50) NOT NULL DEFAULT '',
  `pass` varchar(60) NOT NULL DEFAULT '',
  `firstname` varchar(255) NOT NULL DEFAULT '',
  `lastname` varchar(255) NOT NULL DEFAULT '',
  `email` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `login` (`login`)
) ENGINE=MyISAM;

INSERT INTO `users` (`uid`, `login`, `pass`, `firstname`, `lastname`, `email`) VALUES
(1, 'root', '$1$$D50Z6XG8ULuZfwC6RdSVy.', 'Root', 'Root', 'user.name@bull.net');

INSERT INTO `groups` (`gid`, `name`) VALUES
(1, 'admin'),
(2, 'user');

INSERT INTO `usergroup` (`uid`, `gid`) VALUES
(1, 1),
(1, 2);
