-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le : Mar 04 Juin 2013 à 14:29
-- Version du serveur: 5.5.31
-- Version de PHP: 5.3.10-1ubuntu3.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `plugin_dokuwiki`
--

-- --------------------------------------------------------

--
-- Structure de la table `OPENJPA_SEQUENCE_TABLE`
--
-- Création: Mar 04 Juin 2013 à 12:29
--

DROP TABLE IF EXISTS `OPENJPA_SEQUENCE_TABLE`;
CREATE TABLE IF NOT EXISTS `OPENJPA_SEQUENCE_TABLE` (
  `ID` tinyint(4) NOT NULL,
  `SEQUENCE_VALUE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `plugin_instance`
--
-- Création: Mar 04 Juin 2013 à 12:29
--

DROP TABLE IF EXISTS `plugin_instance`;
CREATE TABLE IF NOT EXISTS `plugin_instance` (
  `id` bigint(20) NOT NULL,
  `configuration_id` varchar(255) NOT NULL,
  `forge_id` varchar(255) NOT NULL,
  `forge_project_id` varchar(255) NOT NULL,
  `instance_id` varchar(255) NOT NULL,
  `tool_project_id` varchar(255) DEFAULT NULL,
  `tool_instance_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_PLGNTNC_INSTANCE_ID` (`instance_id`),
  KEY `tool_instance_id` (`tool_instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `plugin_instance`:
--   `tool_instance_id`
--       `plugin_tool` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `plugin_roles_mapping`
--
-- Création: Mar 04 Juin 2013 à 12:29
--

DROP TABLE IF EXISTS `plugin_roles_mapping`;
CREATE TABLE IF NOT EXISTS `plugin_roles_mapping` (
  `id` bigint(20) NOT NULL,
  `forge_role` varchar(255) NOT NULL,
  `tool_role` varchar(255) NOT NULL,
  `instance_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `instance_id` (`instance_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `plugin_roles_mapping`:
--   `instance_id`
--       `plugin_instance` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `plugin_tool`
--
-- Création: Mar 04 Juin 2013 à 12:29
--

DROP TABLE IF EXISTS `plugin_tool`;
CREATE TABLE IF NOT EXISTS `plugin_tool` (
  `id` bigint(20) NOT NULL,
  `alias` varchar(255) NOT NULL,
  `base_url` varchar(255) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `shareable` bit(1) NOT NULL,
  `tool_id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_PLGN_TL_BASE_URL` (`base_url`),
  UNIQUE KEY `U_PLGN_TL_NAME` (`name`),
  UNIQUE KEY `U_PLGN_TL_TOOL_ID` (`tool_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `plugin_uuid`
--
-- Création: Mar 04 Juin 2013 à 12:29
--

DROP TABLE IF EXISTS `plugin_uuid`;
CREATE TABLE IF NOT EXISTS `plugin_uuid` (
  `uuid` varchar(255) NOT NULL,
  PRIMARY KEY (`uuid`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `plugin_instance`
--
ALTER TABLE `plugin_instance`
  ADD CONSTRAINT `plugin_instance_ibfk_1` FOREIGN KEY (`tool_instance_id`) REFERENCES `plugin_tool` (`id`);

--
-- Contraintes pour la table `plugin_roles_mapping`
--
ALTER TABLE `plugin_roles_mapping`
  ADD CONSTRAINT `plugin_roles_mapping_ibfk_1` FOREIGN KEY (`instance_id`) REFERENCES `plugin_instance` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
