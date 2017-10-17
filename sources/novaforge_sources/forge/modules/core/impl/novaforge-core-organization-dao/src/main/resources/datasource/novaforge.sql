-- phpMyAdmin SQL Dump
-- version 3.4.10.1deb1
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le : Mar 04 Juin 2013 à 14:26
-- Version du serveur: 5.5.31
-- Version de PHP: 5.3.10-1ubuntu3.6

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Base de données: `novaforge`
--

-- --------------------------------------------------------

--
-- Structure de la table `ACTOR`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `ACTOR`;
CREATE TABLE IF NOT EXISTS `ACTOR` (
  `id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `login` varchar(255) NOT NULL,
  `DTYPE` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `visibility` tinyint(1) DEFAULT '0',
  `email` varchar(255) DEFAULT NULL,
  `firstname` varchar(255) DEFAULT NULL,
  `last_connected` datetime DEFAULT NULL,
  `last_password_updated` datetime DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `realm_type` smallint(6) DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `language_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `I_ACTOR_DTYPE` (`DTYPE`),
  KEY `project_id` (`project_id`),
  KEY `language_id` (`language_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `ACTOR`:
--   `language_id`
--       `LANGUAGE` -> `id`
--   `project_id`
--       `PROJECT_ELEMENT` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `ACTOR_ACTOR`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `ACTOR_ACTOR`;
CREATE TABLE IF NOT EXISTS `ACTOR_ACTOR` (
  `group_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  KEY `group_id` (`group_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `ACTOR_ACTOR`:
--   `user_id`
--       `ACTOR` -> `id`
--   `group_id`
--       `ACTOR` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `APP_REQUEST`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `APP_REQUEST`;
CREATE TABLE IF NOT EXISTS `APP_REQUEST` (
  `id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `login` varchar(255) DEFAULT NULL,
  `app_id` bigint(20) DEFAULT NULL,
  `element_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `app_id` (`app_id`),
  KEY `element_id` (`element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `APP_REQUEST`:
--   `element_id`
--       `PROJECT_ELEMENT` -> `id`
--   `app_id`
--       `NODE` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `ATTRIBUTE`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `ATTRIBUTE`;
CREATE TABLE IF NOT EXISTS `ATTRIBUTE` (
  `id` bigint(20) NOT NULL,
  `ispublic` bit(1) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `BINARY_FILE`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `BINARY_FILE`;
CREATE TABLE IF NOT EXISTS `BINARY_FILE` (
  `id` bigint(20) NOT NULL,
  `imageFile` blob,
  `mime_type` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `COMPOSITION`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `COMPOSITION`;
CREATE TABLE IF NOT EXISTS `COMPOSITION` (
  `id` bigint(20) NOT NULL,
  `activated` bit(1) NOT NULL,
  `name` varchar(255) NOT NULL,
  `source_name` varchar(255) NOT NULL,
  `target_name` varchar(255) NOT NULL,
  `template` varchar(255) DEFAULT NULL,
  `type` varchar(255) NOT NULL,
  `uuid` varchar(255) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `application_source` bigint(20) NOT NULL,
  `application_target` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_CMPSTON_NAME` (`name`,`project_id`),
  KEY `project_id` (`project_id`),
  KEY `application_source` (`application_source`),
  KEY `application_target` (`application_target`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `COMPOSITION`:
--   `application_target`
--       `NODE` -> `id`
--   `project_id`
--       `PROJECT_ELEMENT` -> `id`
--   `application_source`
--       `NODE` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `LANGUAGE`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `LANGUAGE`;
CREATE TABLE IF NOT EXISTS `LANGUAGE` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_LNGUAGE_NAME` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `MEMBERSHIP`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `MEMBERSHIP`;
CREATE TABLE IF NOT EXISTS `MEMBERSHIP` (
  `actor_id` bigint(20) NOT NULL,
  `project_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  `priority` bit(1) DEFAULT NULL,
  PRIMARY KEY (`actor_id`,`project_id`,`role_id`),
  KEY `project_id` (`project_id`),
  KEY `role_id` (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `MEMBERSHIP`:
--   `role_id`
--       `ROLE` -> `id`
--   `actor_id`
--       `ACTOR` -> `id`
--   `project_id`
--       `PROJECT_ELEMENT` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `MEMBERSHIP_REQUEST`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `MEMBERSHIP_REQUEST`;
CREATE TABLE IF NOT EXISTS `MEMBERSHIP_REQUEST` (
  `id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `message` varchar(255) DEFAULT NULL,
  `status` smallint(6) NOT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `project_id` (`project_id`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `MEMBERSHIP_REQUEST`:
--   `user_id`
--       `ACTOR` -> `id`
--   `project_id`
--       `PROJECT_ELEMENT` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `NODE`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `NODE`;
CREATE TABLE IF NOT EXISTS `NODE` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `uri` varchar(255) NOT NULL,
  `DTYPE` varchar(255) DEFAULT NULL,
  `plugin_uuid` varchar(255) DEFAULT NULL,
  `element_id` bigint(20) DEFAULT NULL,
  `plugin_instance_uuid` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_NODE_URI` (`uri`),
  KEY `I_NODE_DTYPE` (`DTYPE`),
  KEY `element_id` (`element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `NODE`:
--   `element_id`
--       `PROJECT_ELEMENT` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `OPENJPA_SEQUENCE_TABLE`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `OPENJPA_SEQUENCE_TABLE`;
CREATE TABLE IF NOT EXISTS `OPENJPA_SEQUENCE_TABLE` (
  `ID` tinyint(4) NOT NULL,
  `SEQUENCE_VALUE` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `ORGANIZATION`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `ORGANIZATION`;
CREATE TABLE IF NOT EXISTS `ORGANIZATION` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_RGNZTON_NAME` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `PERMISSION`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `PERMISSION`;
CREATE TABLE IF NOT EXISTS `PERMISSION` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_PRMSSON_NAME` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `plugin_jms_queues`
--
-- Création: Mar 04 Juin 2013 à 12:17
--

DROP TABLE IF EXISTS `plugin_jms_queues`;
CREATE TABLE IF NOT EXISTS `plugin_jms_queues` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `membership_queue` varchar(255) NOT NULL,
  `project_queue` varchar(255) NOT NULL,
  `roles_mapping_queue` varchar(255) NOT NULL,
  `user_queue` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_PLGN_QS_MEMBERSHIP_QUEUE` (`membership_queue`),
  UNIQUE KEY `U_PLGN_QS_PROJECT_QUEUE` (`project_queue`),
  UNIQUE KEY `U_PLGN_QS_ROLES_MAPPING_QUEUE` (`roles_mapping_queue`),
  UNIQUE KEY `U_PLGN_QS_USER_QUEUE` (`user_queue`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

-- --------------------------------------------------------

--
-- Structure de la table `plugin_metadata`
--
-- Création: Mar 04 Juin 2013 à 12:17
--

DROP TABLE IF EXISTS `plugin_metadata`;
CREATE TABLE IF NOT EXISTS `plugin_metadata` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `available` bit(1) NOT NULL,
  `category` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `status` varchar(255) NOT NULL,
  `type` varchar(255) DEFAULT NULL,
  `uuid` varchar(255) DEFAULT NULL,
  `version` varchar(255) DEFAULT NULL,
  `queues_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `queues_id` (`queues_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 AUTO_INCREMENT=1 ;

--
-- RELATIONS POUR LA TABLE `plugin_metadata`:
--   `queues_id`
--       `plugin_jms_queues` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `plugin_view`
--
-- Création: Mar 04 Juin 2013 à 12:17
--

DROP TABLE IF EXISTS `plugin_view`;
CREATE TABLE IF NOT EXISTS `plugin_view` (
  `PLUGINMETADATAENTITY_ID` bigint(20) DEFAULT NULL,
  `view` varchar(255) DEFAULT NULL,
  KEY `PLUGINMETADATAENTITY_ID` (`PLUGINMETADATAENTITY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `plugin_view`:
--   `PLUGINMETADATAENTITY_ID`
--       `plugin_metadata` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `PROJECT_ELEMENT`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `PROJECT_ELEMENT`;
CREATE TABLE IF NOT EXISTS `PROJECT_ELEMENT` (
  `id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `description` varchar(255) NOT NULL,
  `element_id` varchar(255) NOT NULL,
  `last_modified` datetime DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `DTYPE` varchar(255) DEFAULT NULL,
  `author` varchar(255) DEFAULT NULL,
  `licence_type` varchar(255) DEFAULT NULL,
  `private_visibility` tinyint(1) DEFAULT '0',
  `realm_type` smallint(6) DEFAULT NULL,
  `status` smallint(6) DEFAULT NULL,
  `IMAGE_ID` bigint(20) DEFAULT NULL,
  `organization_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_PRJCMNT_ELEMENT_ID` (`element_id`),
  UNIQUE KEY `U_PRJCMNT_NAME` (`name`),
  KEY `I_PRJCMNT_DTYPE` (`DTYPE`),
  KEY `IMAGE_ID` (`IMAGE_ID`),
  KEY `organization_id` (`organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `PROJECT_ELEMENT`:
--   `organization_id`
--       `ORGANIZATION` -> `id`
--   `IMAGE_ID`
--       `BINARY_FILE` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `RECOVERY_PASSWORD`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `RECOVERY_PASSWORD`;
CREATE TABLE IF NOT EXISTS `RECOVERY_PASSWORD` (
  `id` bigint(20) NOT NULL,
  `enabled` bit(1) NOT NULL,
  `expiration_date` datetime NOT NULL,
  `token` varchar(255) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_RCVRWRD_TOKEN` (`token`),
  KEY `user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `RECOVERY_PASSWORD`:
--   `user_id`
--       `ACTOR` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `REQUEST_ROLES_MAPPING`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `REQUEST_ROLES_MAPPING`;
CREATE TABLE IF NOT EXISTS `REQUEST_ROLES_MAPPING` (
  `request_id` bigint(20) DEFAULT NULL,
  `forge_role` varchar(255) NOT NULL,
  `application_role` varchar(255) DEFAULT NULL,
  KEY `request_id` (`request_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `REQUEST_ROLES_MAPPING`:
--   `request_id`
--       `APP_REQUEST` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `ROLE`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `ROLE`;
CREATE TABLE IF NOT EXISTS `ROLE` (
  `id` bigint(20) NOT NULL,
  `description` text,
  `name` varchar(250) NOT NULL,
  `role_order` int(11) NOT NULL,
  `realm_type` smallint(6) DEFAULT NULL,
  `DTYPE` varchar(255) DEFAULT NULL,
  `element_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_ROLE_NAME` (`name`,`element_id`),
  KEY `I_ROLE_DTYPE` (`DTYPE`),
  KEY `element_id` (`element_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `ROLE`:
--   `element_id`
--       `PROJECT_ELEMENT` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `ROLE_PERMISSION`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `ROLE_PERMISSION`;
CREATE TABLE IF NOT EXISTS `ROLE_PERMISSION` (
  `role_id` bigint(20) DEFAULT NULL,
  `permission_id` bigint(20) DEFAULT NULL,
  KEY `role_id` (`role_id`),
  KEY `permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `ROLE_PERMISSION`:
--   `permission_id`
--       `PERMISSION` -> `id`
--   `role_id`
--       `ROLE` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `SPACE_APPLICATION`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `SPACE_APPLICATION`;
CREATE TABLE IF NOT EXISTS `SPACE_APPLICATION` (
  `space_id` bigint(20) DEFAULT NULL,
  `application_id` bigint(20) DEFAULT NULL,
  KEY `space_id` (`space_id`),
  KEY `application_id` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `SPACE_APPLICATION`:
--   `application_id`
--       `NODE` -> `id`
--   `space_id`
--       `NODE` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `TEMPLATE_INSTANCE`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `TEMPLATE_INSTANCE`;
CREATE TABLE IF NOT EXISTS `TEMPLATE_INSTANCE` (
  `id` bigint(20) NOT NULL,
  `project_id` bigint(20) DEFAULT NULL,
  `template_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_TMPLTNC_PROJECT_ID` (`project_id`),
  KEY `template_id` (`template_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `TEMPLATE_INSTANCE`:
--   `template_id`
--       `PROJECT_ELEMENT` -> `id`
--   `project_id`
--       `PROJECT_ELEMENT` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `TEMPLATE_ROLES_MAPPING`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `TEMPLATE_ROLES_MAPPING`;
CREATE TABLE IF NOT EXISTS `TEMPLATE_ROLES_MAPPING` (
  `application_id` bigint(20) DEFAULT NULL,
  `forge_role` varchar(255) NOT NULL,
  `application_role` varchar(255) DEFAULT NULL,
  KEY `application_id` (`application_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `TEMPLATE_ROLES_MAPPING`:
--   `application_id`
--       `NODE` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `USER_BLACKLIST`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `USER_BLACKLIST`;
CREATE TABLE IF NOT EXISTS `USER_BLACKLIST` (
  `id` bigint(20) NOT NULL,
  `created` datetime NOT NULL,
  `email` varchar(255) NOT NULL,
  `login` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `U_SR_BLST_LOGIN` (`login`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Structure de la table `USER_PROFILE`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `USER_PROFILE`;
CREATE TABLE IF NOT EXISTS `USER_PROFILE` (
  `id` bigint(20) NOT NULL,
  `email_public` bit(1) DEFAULT NULL,
  `firstname_public` bit(1) DEFAULT NULL,
  `language_public` bit(1) DEFAULT NULL,
  `lastname_public` bit(1) DEFAULT NULL,
  `projects_public` bit(1) DEFAULT NULL,
  `IMAGE_ID` bigint(20) DEFAULT NULL,
  `user_id` bigint(20) NOT NULL,
  `profile_contact` bigint(20) DEFAULT NULL,
  `profile_work` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `IMAGE_ID` (`IMAGE_ID`),
  KEY `user_id` (`user_id`),
  KEY `profile_contact` (`profile_contact`),
  KEY `profile_work` (`profile_work`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `USER_PROFILE`:
--   `profile_work`
--       `USER_PROFILE_WORK` -> `id`
--   `IMAGE_ID`
--       `BINARY_FILE` -> `id`
--   `user_id`
--       `ACTOR` -> `id`
--   `profile_contact`
--       `USER_PROFILE_CONTACT` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `USER_PROFILE_CONTACT`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `USER_PROFILE_CONTACT`;
CREATE TABLE IF NOT EXISTS `USER_PROFILE_CONTACT` (
  `id` bigint(20) NOT NULL,
  `phone_mobile` bigint(20) DEFAULT NULL,
  `phone_work` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `phone_mobile` (`phone_mobile`),
  KEY `phone_work` (`phone_work`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `USER_PROFILE_CONTACT`:
--   `phone_work`
--       `ATTRIBUTE` -> `id`
--   `phone_mobile`
--       `ATTRIBUTE` -> `id`
--

-- --------------------------------------------------------

--
-- Structure de la table `USER_PROFILE_WORK`
--
-- Création: Mar 04 Juin 2013 à 12:18
--

DROP TABLE IF EXISTS `USER_PROFILE_WORK`;
CREATE TABLE IF NOT EXISTS `USER_PROFILE_WORK` (
  `id` bigint(20) NOT NULL,
  `company_address` bigint(20) DEFAULT NULL,
  `company_name` bigint(20) DEFAULT NULL,
  `company_office` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `company_address` (`company_address`),
  KEY `company_name` (`company_name`),
  KEY `company_office` (`company_office`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- RELATIONS POUR LA TABLE `USER_PROFILE_WORK`:
--   `company_office`
--       `ATTRIBUTE` -> `id`
--   `company_address`
--       `ATTRIBUTE` -> `id`
--   `company_name`
--       `ATTRIBUTE` -> `id`
--

--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `ACTOR`
--
ALTER TABLE `ACTOR`
  ADD CONSTRAINT `ACTOR_ibfk_2` FOREIGN KEY (`language_id`) REFERENCES `LANGUAGE` (`id`),
  ADD CONSTRAINT `ACTOR_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `PROJECT_ELEMENT` (`id`);

--
-- Contraintes pour la table `ACTOR_ACTOR`
--
ALTER TABLE `ACTOR_ACTOR`
  ADD CONSTRAINT `ACTOR_ACTOR_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `ACTOR` (`id`),
  ADD CONSTRAINT `ACTOR_ACTOR_ibfk_1` FOREIGN KEY (`group_id`) REFERENCES `ACTOR` (`id`);

--
-- Contraintes pour la table `APP_REQUEST`
--
ALTER TABLE `APP_REQUEST`
  ADD CONSTRAINT `APP_REQUEST_ibfk_2` FOREIGN KEY (`element_id`) REFERENCES `PROJECT_ELEMENT` (`id`),
  ADD CONSTRAINT `APP_REQUEST_ibfk_1` FOREIGN KEY (`app_id`) REFERENCES `NODE` (`id`);

--
-- Contraintes pour la table `COMPOSITION`
--
ALTER TABLE `COMPOSITION`
  ADD CONSTRAINT `COMPOSITION_ibfk_3` FOREIGN KEY (`application_target`) REFERENCES `NODE` (`id`),
  ADD CONSTRAINT `COMPOSITION_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `PROJECT_ELEMENT` (`id`),
  ADD CONSTRAINT `COMPOSITION_ibfk_2` FOREIGN KEY (`application_source`) REFERENCES `NODE` (`id`);

--
-- Contraintes pour la table `MEMBERSHIP`
--
ALTER TABLE `MEMBERSHIP`
  ADD CONSTRAINT `MEMBERSHIP_ibfk_3` FOREIGN KEY (`role_id`) REFERENCES `ROLE` (`id`),
  ADD CONSTRAINT `MEMBERSHIP_ibfk_1` FOREIGN KEY (`actor_id`) REFERENCES `ACTOR` (`id`),
  ADD CONSTRAINT `MEMBERSHIP_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `PROJECT_ELEMENT` (`id`);

--
-- Contraintes pour la table `MEMBERSHIP_REQUEST`
--
ALTER TABLE `MEMBERSHIP_REQUEST`
  ADD CONSTRAINT `MEMBERSHIP_REQUEST_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `ACTOR` (`id`),
  ADD CONSTRAINT `MEMBERSHIP_REQUEST_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `PROJECT_ELEMENT` (`id`);

--
-- Contraintes pour la table `NODE`
--
ALTER TABLE `NODE`
  ADD CONSTRAINT `NODE_ibfk_1` FOREIGN KEY (`element_id`) REFERENCES `PROJECT_ELEMENT` (`id`);

--
-- Contraintes pour la table `plugin_metadata`
--
ALTER TABLE `plugin_metadata`
  ADD CONSTRAINT `plugin_metadata_ibfk_1` FOREIGN KEY (`queues_id`) REFERENCES `plugin_jms_queues` (`id`);

--
-- Contraintes pour la table `plugin_view`
--
ALTER TABLE `plugin_view`
  ADD CONSTRAINT `plugin_view_ibfk_1` FOREIGN KEY (`PLUGINMETADATAENTITY_ID`) REFERENCES `plugin_metadata` (`id`);

--
-- Contraintes pour la table `PROJECT_ELEMENT`
--
ALTER TABLE `PROJECT_ELEMENT`
  ADD CONSTRAINT `PROJECT_ELEMENT_ibfk_2` FOREIGN KEY (`organization_id`) REFERENCES `ORGANIZATION` (`id`),
  ADD CONSTRAINT `PROJECT_ELEMENT_ibfk_1` FOREIGN KEY (`IMAGE_ID`) REFERENCES `BINARY_FILE` (`id`);

--
-- Contraintes pour la table `RECOVERY_PASSWORD`
--
ALTER TABLE `RECOVERY_PASSWORD`
  ADD CONSTRAINT `RECOVERY_PASSWORD_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `ACTOR` (`id`);

--
-- Contraintes pour la table `REQUEST_ROLES_MAPPING`
--
ALTER TABLE `REQUEST_ROLES_MAPPING`
  ADD CONSTRAINT `REQUEST_ROLES_MAPPING_ibfk_1` FOREIGN KEY (`request_id`) REFERENCES `APP_REQUEST` (`id`);

--
-- Contraintes pour la table `ROLE`
--
ALTER TABLE `ROLE`
  ADD CONSTRAINT `ROLE_ibfk_1` FOREIGN KEY (`element_id`) REFERENCES `PROJECT_ELEMENT` (`id`);

--
-- Contraintes pour la table `ROLE_PERMISSION`
--
ALTER TABLE `ROLE_PERMISSION`
  ADD CONSTRAINT `ROLE_PERMISSION_ibfk_2` FOREIGN KEY (`permission_id`) REFERENCES `PERMISSION` (`id`),
  ADD CONSTRAINT `ROLE_PERMISSION_ibfk_1` FOREIGN KEY (`role_id`) REFERENCES `ROLE` (`id`);

--
-- Contraintes pour la table `SPACE_APPLICATION`
--
ALTER TABLE `SPACE_APPLICATION`
  ADD CONSTRAINT `SPACE_APPLICATION_ibfk_2` FOREIGN KEY (`application_id`) REFERENCES `NODE` (`id`),
  ADD CONSTRAINT `SPACE_APPLICATION_ibfk_1` FOREIGN KEY (`space_id`) REFERENCES `NODE` (`id`);

--
-- Contraintes pour la table `TEMPLATE_INSTANCE`
--
ALTER TABLE `TEMPLATE_INSTANCE`
  ADD CONSTRAINT `TEMPLATE_INSTANCE_ibfk_2` FOREIGN KEY (`template_id`) REFERENCES `PROJECT_ELEMENT` (`id`),
  ADD CONSTRAINT `TEMPLATE_INSTANCE_ibfk_1` FOREIGN KEY (`project_id`) REFERENCES `PROJECT_ELEMENT` (`id`);

--
-- Contraintes pour la table `TEMPLATE_ROLES_MAPPING`
--
ALTER TABLE `TEMPLATE_ROLES_MAPPING`
  ADD CONSTRAINT `TEMPLATE_ROLES_MAPPING_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `NODE` (`id`);

--
-- Contraintes pour la table `USER_PROFILE`
--
ALTER TABLE `USER_PROFILE`
  ADD CONSTRAINT `USER_PROFILE_ibfk_4` FOREIGN KEY (`profile_work`) REFERENCES `USER_PROFILE_WORK` (`id`),
  ADD CONSTRAINT `USER_PROFILE_ibfk_1` FOREIGN KEY (`IMAGE_ID`) REFERENCES `BINARY_FILE` (`id`),
  ADD CONSTRAINT `USER_PROFILE_ibfk_2` FOREIGN KEY (`user_id`) REFERENCES `ACTOR` (`id`),
  ADD CONSTRAINT `USER_PROFILE_ibfk_3` FOREIGN KEY (`profile_contact`) REFERENCES `USER_PROFILE_CONTACT` (`id`);

--
-- Contraintes pour la table `USER_PROFILE_CONTACT`
--
ALTER TABLE `USER_PROFILE_CONTACT`
  ADD CONSTRAINT `USER_PROFILE_CONTACT_ibfk_2` FOREIGN KEY (`phone_work`) REFERENCES `ATTRIBUTE` (`id`),
  ADD CONSTRAINT `USER_PROFILE_CONTACT_ibfk_1` FOREIGN KEY (`phone_mobile`) REFERENCES `ATTRIBUTE` (`id`);

--
-- Contraintes pour la table `USER_PROFILE_WORK`
--
ALTER TABLE `USER_PROFILE_WORK`
  ADD CONSTRAINT `USER_PROFILE_WORK_ibfk_3` FOREIGN KEY (`company_office`) REFERENCES `ATTRIBUTE` (`id`),
  ADD CONSTRAINT `USER_PROFILE_WORK_ibfk_1` FOREIGN KEY (`company_address`) REFERENCES `ATTRIBUTE` (`id`),
  ADD CONSTRAINT `USER_PROFILE_WORK_ibfk_2` FOREIGN KEY (`company_name`) REFERENCES `ATTRIBUTE` (`id`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
