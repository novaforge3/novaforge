-- phpMyAdmin SQL Dump
-- version 3.3.7deb5
-- http://www.phpmyadmin.net
--
-- Serveur: localhost
-- Généré le : Ven 17 Juin 2011 à 14:35
-- Version du serveur: 5.1.49
-- Version de PHP: 5.3.3-7+squeeze1

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `plugin_svn`
--

-- --------------------------------------------------------

--
-- Structure de la table `plugin_authentification`
--

CREATE TABLE IF NOT EXISTS `plugin_authentification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `auth_url` varchar(255) NOT NULL,
  `certificate` varchar(255) NOT NULL,
  `forge_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `forge_id` (`forge_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `plugin_instance`
--

CREATE TABLE IF NOT EXISTS `plugin_instance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `configuration_id` varchar(255) NOT NULL,
  `forge_id` varchar(255) NOT NULL,
  `forge_project_id` varchar(255) NOT NULL,
  `instance_id` varchar(255) NOT NULL,
  `tool_project_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `instance_id` (`instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `plugin_roles_mapping`
--

CREATE TABLE IF NOT EXISTS `plugin_roles_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `forge_role` varchar(255) NOT NULL,
  `tool_role` varchar(255) NOT NULL,
  `instance_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id` (`id`),
  UNIQUE KEY `forge_role` (`forge_role`),
  KEY `FK6F2B6D40917A4085` (`instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `plugin_uuid`
--

CREATE TABLE IF NOT EXISTS `plugin_uuid` (
  `uuid` varchar(255) NOT NULL,
  PRIMARY KEY (`uuid`),
  UNIQUE KEY `uuid` (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `SVN_GROUP`
--

CREATE TABLE IF NOT EXISTS `SVN_GROUP` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `SVN_GROUP_MEMBERSHIP`
--

CREATE TABLE IF NOT EXISTS `SVN_GROUP_MEMBERSHIP` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `group_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB64A092A119E3A82` (`user_id`),
  KEY `FKB64A092AD61EE9D8` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `SVN_GROUP_PERMISSION`
--

CREATE TABLE IF NOT EXISTS `SVN_GROUP_PERMISSION` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `read_permission` tinyint(4) NOT NULL,
  `recursive_permission` tinyint(4) NOT NULL,
  `write_permission` tinyint(4) NOT NULL,
  `group_id` bigint(20) NOT NULL,
  `repository_path_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKE75247237DF81977` (`repository_path_id`),
  KEY `FKE7524723D61EE9D8` (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `SVN_REPOSITORY`
--

CREATE TABLE IF NOT EXISTS `SVN_REPOSITORY` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `SVN_REPOSITORY_PATH`
--

CREATE TABLE IF NOT EXISTS `SVN_REPOSITORY_PATH` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `path` varchar(255) NOT NULL,
  `repository_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC4FCEE06DC69BB22` (`repository_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `SVN_USER`
--

CREATE TABLE IF NOT EXISTS `SVN_USER` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `SVN_USER_PERMISSION`
--

CREATE TABLE IF NOT EXISTS `SVN_USER_PERMISSION` (
  `repository_path_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `read_permission` tinyint(4) NOT NULL,
  `recursive_permission` tinyint(4) NOT NULL,
  `write_permission` tinyint(4) NOT NULL,
  PRIMARY KEY (`repository_path_id`,`user_id`),
  KEY `FK100B08CF7DF81977` (`repository_path_id`),
  KEY `FK100B08CF119E3A82` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `plugin_roles_mapping`
--
ALTER TABLE `plugin_roles_mapping`
  ADD CONSTRAINT `FK6F2B6D40917A4085` FOREIGN KEY (`instance_id`) REFERENCES `plugin_instance` (`id`);

--
-- Contraintes pour la table `SVN_GROUP_MEMBERSHIP`
--
ALTER TABLE `SVN_GROUP_MEMBERSHIP`
  ADD CONSTRAINT `FKB64A092A119E3A82` FOREIGN KEY (`user_id`) REFERENCES `SVN_USER` (`id`),
  ADD CONSTRAINT `FKB64A092AD61EE9D8` FOREIGN KEY (`group_id`) REFERENCES `SVN_GROUP` (`id`);

--
-- Contraintes pour la table `SVN_GROUP_PERMISSION`
--
ALTER TABLE `SVN_GROUP_PERMISSION`
  ADD CONSTRAINT `FKE75247237DF81977` FOREIGN KEY (`repository_path_id`) REFERENCES `SVN_REPOSITORY_PATH` (`id`),
  ADD CONSTRAINT `FKE7524723D61EE9D8` FOREIGN KEY (`group_id`) REFERENCES `SVN_GROUP` (`id`);

--
-- Contraintes pour la table `SVN_REPOSITORY_PATH`
--
ALTER TABLE `SVN_REPOSITORY_PATH`
  ADD CONSTRAINT `FKC4FCEE06DC69BB22` FOREIGN KEY (`repository_id`) REFERENCES `SVN_REPOSITORY` (`id`);

--
-- Contraintes pour la table `SVN_USER_PERMISSION`
--
ALTER TABLE `SVN_USER_PERMISSION`
  ADD CONSTRAINT `FK100B08CF119E3A82` FOREIGN KEY (`user_id`) REFERENCES `SVN_USER` (`id`),
  ADD CONSTRAINT `FK100B08CF7DF81977` FOREIGN KEY (`repository_path_id`) REFERENCES `SVN_REPOSITORY_PATH` (`id`);
