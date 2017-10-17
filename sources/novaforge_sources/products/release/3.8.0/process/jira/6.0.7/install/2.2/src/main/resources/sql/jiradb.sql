use @databaseJiraMysql@;

-- MySQL dump 10.11
--
-- Host: localhost    Database: jiradb
-- ------------------------------------------------------
-- Server version	5.0.95

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `AO_4AEACD_WEBHOOK_DAO`
--

DROP TABLE IF EXISTS `AO_4AEACD_WEBHOOK_DAO`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_4AEACD_WEBHOOK_DAO` (
  `ENABLED` tinyint(1) default NULL,
  `ENCODED_EVENTS` text collate utf8_bin,
  `FILTER` text collate utf8_bin,
  `ID` int(11) NOT NULL auto_increment,
  `LAST_UPDATED` datetime NOT NULL,
  `LAST_UPDATED_USER` varchar(255) collate utf8_bin NOT NULL,
  `NAME` text collate utf8_bin NOT NULL,
  `REGISTRATION_METHOD` varchar(255) collate utf8_bin NOT NULL,
  `URL` text collate utf8_bin NOT NULL,
  `EXCLUDE_ISSUE_DETAILS` tinyint(1) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_4AEACD_WEBHOOK_DAO`
--

LOCK TABLES `AO_4AEACD_WEBHOOK_DAO` WRITE;
/*!40000 ALTER TABLE `AO_4AEACD_WEBHOOK_DAO` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_4AEACD_WEBHOOK_DAO` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_563AEE_ACTIVITY_ENTITY`
--

DROP TABLE IF EXISTS `AO_563AEE_ACTIVITY_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_563AEE_ACTIVITY_ENTITY` (
  `ACTIVITY_ID` bigint(20) NOT NULL auto_increment,
  `ACTOR_ID` int(11) default NULL,
  `CONTENT` text collate utf8_bin,
  `GENERATOR_DISPLAY_NAME` varchar(255) collate utf8_bin default NULL,
  `GENERATOR_ID` varchar(767) collate utf8_bin default NULL,
  `ICON_ID` int(11) default NULL,
  `ID` varchar(767) collate utf8_bin default NULL,
  `ISSUE_KEY` varchar(255) collate utf8_bin default NULL,
  `OBJECT_ID` int(11) default NULL,
  `POSTER` varchar(255) collate utf8_bin default NULL,
  `PROJECT_KEY` varchar(255) collate utf8_bin default NULL,
  `PUBLISHED` datetime default NULL,
  `TARGET_ID` int(11) default NULL,
  `TITLE` varchar(255) collate utf8_bin default NULL,
  `URL` varchar(767) collate utf8_bin default NULL,
  `USERNAME` varchar(255) collate utf8_bin default NULL,
  `VERB` varchar(767) collate utf8_bin default NULL,
  PRIMARY KEY  (`ACTIVITY_ID`),
  KEY `index_ao_563aee_act1642652291` (`OBJECT_ID`),
  KEY `index_ao_563aee_act1978295567` (`TARGET_ID`),
  KEY `index_ao_563aee_act972488439` (`ICON_ID`),
  KEY `index_ao_563aee_act995325379` (`ACTOR_ID`),
  CONSTRAINT `fk_ao_563aee_activity_entity_actor_id` FOREIGN KEY (`ACTOR_ID`) REFERENCES `AO_563AEE_ACTOR_ENTITY` (`ID`),
  CONSTRAINT `fk_ao_563aee_activity_entity_icon_id` FOREIGN KEY (`ICON_ID`) REFERENCES `AO_563AEE_MEDIA_LINK_ENTITY` (`ID`),
  CONSTRAINT `fk_ao_563aee_activity_entity_object_id` FOREIGN KEY (`OBJECT_ID`) REFERENCES `AO_563AEE_OBJECT_ENTITY` (`ID`),
  CONSTRAINT `fk_ao_563aee_activity_entity_target_id` FOREIGN KEY (`TARGET_ID`) REFERENCES `AO_563AEE_TARGET_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_563AEE_ACTIVITY_ENTITY`
--

LOCK TABLES `AO_563AEE_ACTIVITY_ENTITY` WRITE;
/*!40000 ALTER TABLE `AO_563AEE_ACTIVITY_ENTITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_563AEE_ACTIVITY_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_563AEE_ACTOR_ENTITY`
--

DROP TABLE IF EXISTS `AO_563AEE_ACTOR_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_563AEE_ACTOR_ENTITY` (
  `FULL_NAME` varchar(255) collate utf8_bin default NULL,
  `ID` int(11) NOT NULL auto_increment,
  `PROFILE_PAGE_URI` varchar(767) collate utf8_bin default NULL,
  `PROFILE_PICTURE_URI` varchar(767) collate utf8_bin default NULL,
  `USERNAME` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_563AEE_ACTOR_ENTITY`
--

LOCK TABLES `AO_563AEE_ACTOR_ENTITY` WRITE;
/*!40000 ALTER TABLE `AO_563AEE_ACTOR_ENTITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_563AEE_ACTOR_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_563AEE_MEDIA_LINK_ENTITY`
--

DROP TABLE IF EXISTS `AO_563AEE_MEDIA_LINK_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_563AEE_MEDIA_LINK_ENTITY` (
  `DURATION` int(11) default NULL,
  `HEIGHT` int(11) default NULL,
  `ID` int(11) NOT NULL auto_increment,
  `URL` varchar(767) collate utf8_bin default NULL,
  `WIDTH` int(11) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_563AEE_MEDIA_LINK_ENTITY`
--

LOCK TABLES `AO_563AEE_MEDIA_LINK_ENTITY` WRITE;
/*!40000 ALTER TABLE `AO_563AEE_MEDIA_LINK_ENTITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_563AEE_MEDIA_LINK_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_563AEE_OBJECT_ENTITY`
--

DROP TABLE IF EXISTS `AO_563AEE_OBJECT_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_563AEE_OBJECT_ENTITY` (
  `CONTENT` varchar(255) collate utf8_bin default NULL,
  `DISPLAY_NAME` varchar(255) collate utf8_bin default NULL,
  `ID` int(11) NOT NULL auto_increment,
  `IMAGE_ID` int(11) default NULL,
  `OBJECT_ID` varchar(767) collate utf8_bin default NULL,
  `OBJECT_TYPE` varchar(767) collate utf8_bin default NULL,
  `SUMMARY` varchar(255) collate utf8_bin default NULL,
  `URL` varchar(767) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `index_ao_563aee_obj696886343` (`IMAGE_ID`),
  CONSTRAINT `fk_ao_563aee_object_entity_image_id` FOREIGN KEY (`IMAGE_ID`) REFERENCES `AO_563AEE_MEDIA_LINK_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_563AEE_OBJECT_ENTITY`
--

LOCK TABLES `AO_563AEE_OBJECT_ENTITY` WRITE;
/*!40000 ALTER TABLE `AO_563AEE_OBJECT_ENTITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_563AEE_OBJECT_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_563AEE_TARGET_ENTITY`
--

DROP TABLE IF EXISTS `AO_563AEE_TARGET_ENTITY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_563AEE_TARGET_ENTITY` (
  `CONTENT` varchar(255) collate utf8_bin default NULL,
  `DISPLAY_NAME` varchar(255) collate utf8_bin default NULL,
  `ID` int(11) NOT NULL auto_increment,
  `IMAGE_ID` int(11) default NULL,
  `OBJECT_ID` varchar(767) collate utf8_bin default NULL,
  `OBJECT_TYPE` varchar(767) collate utf8_bin default NULL,
  `SUMMARY` varchar(255) collate utf8_bin default NULL,
  `URL` varchar(767) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `index_ao_563aee_tar521440921` (`IMAGE_ID`),
  CONSTRAINT `fk_ao_563aee_target_entity_image_id` FOREIGN KEY (`IMAGE_ID`) REFERENCES `AO_563AEE_MEDIA_LINK_ENTITY` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_563AEE_TARGET_ENTITY`
--

LOCK TABLES `AO_563AEE_TARGET_ENTITY` WRITE;
/*!40000 ALTER TABLE `AO_563AEE_TARGET_ENTITY` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_563AEE_TARGET_ENTITY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_E8B6CC_CHANGESET_MAPPING`
--

DROP TABLE IF EXISTS `AO_E8B6CC_CHANGESET_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_E8B6CC_CHANGESET_MAPPING` (
  `AUTHOR` varchar(255) collate utf8_bin default NULL,
  `AUTHOR_EMAIL` varchar(255) collate utf8_bin default NULL,
  `BRANCH` varchar(255) collate utf8_bin default NULL,
  `DATE` datetime default NULL,
  `FILES_DATA` text collate utf8_bin,
  `ID` int(11) NOT NULL auto_increment,
  `ISSUE_KEY` varchar(255) collate utf8_bin default NULL,
  `MESSAGE` text collate utf8_bin,
  `NODE` varchar(255) collate utf8_bin default NULL,
  `PARENTS_DATA` varchar(255) collate utf8_bin default NULL,
  `PROJECT_KEY` varchar(255) collate utf8_bin default NULL,
  `RAW_AUTHOR` varchar(255) collate utf8_bin default NULL,
  `RAW_NODE` varchar(255) collate utf8_bin default NULL,
  `REPOSITORY_ID` int(11) default '0',
  `SMARTCOMMIT_AVAILABLE` tinyint(1) default NULL,
  `VERSION` int(11) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_E8B6CC_CHANGESET_MAPPING`
--

LOCK TABLES `AO_E8B6CC_CHANGESET_MAPPING` WRITE;
/*!40000 ALTER TABLE `AO_E8B6CC_CHANGESET_MAPPING` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_E8B6CC_CHANGESET_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_E8B6CC_ISSUE_TO_CHANGESET`
--

DROP TABLE IF EXISTS `AO_E8B6CC_ISSUE_TO_CHANGESET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_E8B6CC_ISSUE_TO_CHANGESET` (
  `CHANGESET_ID` int(11) default NULL,
  `ID` int(11) NOT NULL auto_increment,
  `ISSUE_KEY` varchar(255) collate utf8_bin default NULL,
  `PROJECT_KEY` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `index_ao_e8b6cc_iss1229805759` (`CHANGESET_ID`),
  CONSTRAINT `fk_ao_e8b6cc_issue_to_changeset_changeset_id` FOREIGN KEY (`CHANGESET_ID`) REFERENCES `AO_E8B6CC_CHANGESET_MAPPING` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_E8B6CC_ISSUE_TO_CHANGESET`
--

LOCK TABLES `AO_E8B6CC_ISSUE_TO_CHANGESET` WRITE;
/*!40000 ALTER TABLE `AO_E8B6CC_ISSUE_TO_CHANGESET` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_E8B6CC_ISSUE_TO_CHANGESET` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_E8B6CC_ORGANIZATION_MAPPING`
--

DROP TABLE IF EXISTS `AO_E8B6CC_ORGANIZATION_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_E8B6CC_ORGANIZATION_MAPPING` (
  `ACCESS_TOKEN` varchar(255) collate utf8_bin default NULL,
  `ADMIN_PASSWORD` varchar(255) collate utf8_bin default NULL,
  `ADMIN_USERNAME` varchar(255) collate utf8_bin default NULL,
  `AUTOLINK_NEW_REPOS` tinyint(1) default NULL,
  `DEFAULT_GROUPS_SLUGS` varchar(255) collate utf8_bin default NULL,
  `DVCS_TYPE` varchar(255) collate utf8_bin default NULL,
  `HOST_URL` varchar(255) collate utf8_bin default NULL,
  `ID` int(11) NOT NULL auto_increment,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `OAUTH_KEY` varchar(255) collate utf8_bin default NULL,
  `OAUTH_SECRET` varchar(255) collate utf8_bin default NULL,
  `SMARTCOMMITS_FOR_NEW_REPOS` tinyint(1) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_E8B6CC_ORGANIZATION_MAPPING`
--

LOCK TABLES `AO_E8B6CC_ORGANIZATION_MAPPING` WRITE;
/*!40000 ALTER TABLE `AO_E8B6CC_ORGANIZATION_MAPPING` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_E8B6CC_ORGANIZATION_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_E8B6CC_REPOSITORY_MAPPING`
--

DROP TABLE IF EXISTS `AO_E8B6CC_REPOSITORY_MAPPING`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_E8B6CC_REPOSITORY_MAPPING` (
  `DELETED` tinyint(1) default NULL,
  `ID` int(11) NOT NULL auto_increment,
  `LAST_CHANGESET_NODE` varchar(255) collate utf8_bin default NULL,
  `LAST_COMMIT_DATE` datetime default NULL,
  `LINKED` tinyint(1) default NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `ORGANIZATION_ID` int(11) default '0',
  `SLUG` varchar(255) collate utf8_bin default NULL,
  `SMARTCOMMITS_ENABLED` tinyint(1) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_E8B6CC_REPOSITORY_MAPPING`
--

LOCK TABLES `AO_E8B6CC_REPOSITORY_MAPPING` WRITE;
/*!40000 ALTER TABLE `AO_E8B6CC_REPOSITORY_MAPPING` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_E8B6CC_REPOSITORY_MAPPING` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `AO_E8B6CC_REPO_TO_CHANGESET`
--

DROP TABLE IF EXISTS `AO_E8B6CC_REPO_TO_CHANGESET`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AO_E8B6CC_REPO_TO_CHANGESET` (
  `CHANGESET_ID` int(11) default NULL,
  `ID` int(11) NOT NULL auto_increment,
  `REPOSITORY_ID` int(11) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `index_ao_e8b6cc_rep1082901832` (`REPOSITORY_ID`),
  KEY `index_ao_e8b6cc_rep922992576` (`CHANGESET_ID`),
  CONSTRAINT `fk_ao_e8b6cc_repo_to_changeset_changeset_id` FOREIGN KEY (`CHANGESET_ID`) REFERENCES `AO_E8B6CC_CHANGESET_MAPPING` (`ID`),
  CONSTRAINT `fk_ao_e8b6cc_repo_to_changeset_repository_id` FOREIGN KEY (`REPOSITORY_ID`) REFERENCES `AO_E8B6CC_REPOSITORY_MAPPING` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `AO_E8B6CC_REPO_TO_CHANGESET`
--

LOCK TABLES `AO_E8B6CC_REPO_TO_CHANGESET` WRITE;
/*!40000 ALTER TABLE `AO_E8B6CC_REPO_TO_CHANGESET` DISABLE KEYS */;
/*!40000 ALTER TABLE `AO_E8B6CC_REPO_TO_CHANGESET` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OS_CURRENTSTEP`
--

DROP TABLE IF EXISTS `OS_CURRENTSTEP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OS_CURRENTSTEP` (
  `ID` decimal(18,0) NOT NULL,
  `ENTRY_ID` decimal(18,0) default NULL,
  `STEP_ID` decimal(9,0) default NULL,
  `ACTION_ID` decimal(9,0) default NULL,
  `OWNER` varchar(60) collate utf8_bin default NULL,
  `START_DATE` datetime default NULL,
  `DUE_DATE` datetime default NULL,
  `FINISH_DATE` datetime default NULL,
  `STATUS` varchar(60) collate utf8_bin default NULL,
  `CALLER` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `wf_entryid` (`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OS_CURRENTSTEP`
--

LOCK TABLES `OS_CURRENTSTEP` WRITE;
/*!40000 ALTER TABLE `OS_CURRENTSTEP` DISABLE KEYS */;
/*!40000 ALTER TABLE `OS_CURRENTSTEP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OS_CURRENTSTEP_PREV`
--

DROP TABLE IF EXISTS `OS_CURRENTSTEP_PREV`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OS_CURRENTSTEP_PREV` (
  `ID` decimal(18,0) NOT NULL,
  `PREVIOUS_ID` decimal(18,0) NOT NULL,
  PRIMARY KEY  (`ID`,`PREVIOUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OS_CURRENTSTEP_PREV`
--

LOCK TABLES `OS_CURRENTSTEP_PREV` WRITE;
/*!40000 ALTER TABLE `OS_CURRENTSTEP_PREV` DISABLE KEYS */;
/*!40000 ALTER TABLE `OS_CURRENTSTEP_PREV` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OS_HISTORYSTEP`
--

DROP TABLE IF EXISTS `OS_HISTORYSTEP`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OS_HISTORYSTEP` (
  `ID` decimal(18,0) NOT NULL,
  `ENTRY_ID` decimal(18,0) default NULL,
  `STEP_ID` decimal(9,0) default NULL,
  `ACTION_ID` decimal(9,0) default NULL,
  `OWNER` varchar(60) collate utf8_bin default NULL,
  `START_DATE` datetime default NULL,
  `DUE_DATE` datetime default NULL,
  `FINISH_DATE` datetime default NULL,
  `STATUS` varchar(60) collate utf8_bin default NULL,
  `CALLER` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `historystep_entryid` (`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OS_HISTORYSTEP`
--

LOCK TABLES `OS_HISTORYSTEP` WRITE;
/*!40000 ALTER TABLE `OS_HISTORYSTEP` DISABLE KEYS */;
/*!40000 ALTER TABLE `OS_HISTORYSTEP` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OS_HISTORYSTEP_PREV`
--

DROP TABLE IF EXISTS `OS_HISTORYSTEP_PREV`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OS_HISTORYSTEP_PREV` (
  `ID` decimal(18,0) NOT NULL,
  `PREVIOUS_ID` decimal(18,0) NOT NULL,
  PRIMARY KEY  (`ID`,`PREVIOUS_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OS_HISTORYSTEP_PREV`
--

LOCK TABLES `OS_HISTORYSTEP_PREV` WRITE;
/*!40000 ALTER TABLE `OS_HISTORYSTEP_PREV` DISABLE KEYS */;
/*!40000 ALTER TABLE `OS_HISTORYSTEP_PREV` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `OS_WFENTRY`
--

DROP TABLE IF EXISTS `OS_WFENTRY`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `OS_WFENTRY` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `INITIALIZED` decimal(9,0) default NULL,
  `STATE` decimal(9,0) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `OS_WFENTRY`
--

LOCK TABLES `OS_WFENTRY` WRITE;
/*!40000 ALTER TABLE `OS_WFENTRY` DISABLE KEYS */;
/*!40000 ALTER TABLE `OS_WFENTRY` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SEQUENCE_VALUE_ITEM`
--

DROP TABLE IF EXISTS `SEQUENCE_VALUE_ITEM`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `SEQUENCE_VALUE_ITEM` (
  `SEQ_NAME` varchar(60) collate utf8_bin NOT NULL,
  `SEQ_ID` decimal(18,0) default NULL,
  PRIMARY KEY  (`SEQ_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SEQUENCE_VALUE_ITEM`
--

LOCK TABLES `SEQUENCE_VALUE_ITEM` WRITE;
/*!40000 ALTER TABLE `SEQUENCE_VALUE_ITEM` DISABLE KEYS */;
INSERT INTO `SEQUENCE_VALUE_ITEM` VALUES ('ApplicationUser','10100'),('Avatar','10200'),('ConfigurationContext','10100'),('ExternalEntity','10'),('FieldConfigScheme','10100'),('FieldConfigSchemeIssueType','10200'),('FieldConfiguration','10100'),('FieldLayout','10100'),('FieldLayoutItem','10100'),('FieldScreen','10000'),('FieldScreenLayoutItem','10200'),('FieldScreenScheme','10000'),('FieldScreenSchemeItem','10100'),('FieldScreenTab','10100'),('GadgetUserPreference','10100'),('GenericConfiguration','10100'),('Group','10010'),('IssueLinkType','10200'),('IssueTypeScreenSchemeEntity','10100'),('ListenerConfig','10100'),('Membership','10010'),('Notification','10200'),('NotificationScheme','10100'),('OAuthConsumer','10100'),('OSGroup','10100'),('OSMembership','10100'),('OSPropertyEntry','10700'),('OSUser','10100'),('OptionConfiguration','10200'),('PluginVersion','10500'),('PortalPage','10100'),('PortletConfiguration','10100'),('ProjectRole','10100'),('ProjectRoleActor','10100'),('SchemePermissions','10500'),('ServiceConfig','10500'),('SharePermissions','10100'),('UpgradeHistory','10300'),('UpgradeVersionHistory','10100'),('User','10010'),('UserAttribute','110'),('UserHistoryItem','10100');
/*!40000 ALTER TABLE `SEQUENCE_VALUE_ITEM` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `app_user`
--

DROP TABLE IF EXISTS `app_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `app_user` (
  `ID` decimal(18,0) NOT NULL,
  `user_key` varchar(255) collate utf8_bin default NULL,
  `lower_user_name` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `uk_user_key` (`user_key`),
  UNIQUE KEY `uk_lower_user_name` (`lower_user_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `app_user`
--

LOCK TABLES `app_user` WRITE;
/*!40000 ALTER TABLE `app_user` DISABLE KEYS */;
INSERT INTO `app_user` VALUES ('10000','administrator','administrator');
/*!40000 ALTER TABLE `app_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `avatar`
--

DROP TABLE IF EXISTS `avatar`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `avatar` (
  `ID` decimal(18,0) NOT NULL,
  `filename` varchar(255) collate utf8_bin default NULL,
  `contenttype` varchar(255) collate utf8_bin default NULL,
  `avatartype` varchar(60) collate utf8_bin default NULL,
  `owner` varchar(255) collate utf8_bin default NULL,
  `systemavatar` decimal(9,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `avatar_index` (`avatartype`,`owner`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `avatar`
--

LOCK TABLES `avatar` WRITE;
/*!40000 ALTER TABLE `avatar` DISABLE KEYS */;
INSERT INTO `avatar` VALUES ('10000','codegeist.png','image/png','project',NULL,'1'),('10001','eamesbird.png','image/png','project',NULL,'1'),('10002','jm_black.png','image/png','project',NULL,'1'),('10003','jm_brown.png','image/png','project',NULL,'1'),('10004','jm_orange.png','image/png','project',NULL,'1'),('10005','jm_red.png','image/png','project',NULL,'1'),('10006','jm_white.png','image/png','project',NULL,'1'),('10007','jm_yellow.png','image/png','project',NULL,'1'),('10008','monster.png','image/png','project',NULL,'1'),('10009','rainbow.png','image/png','project',NULL,'1'),('10010','kangaroo.png','image/png','project',NULL,'1'),('10011','rocket.png','image/png','project',NULL,'1'),('10100','Avatar-1.png','image/png','user',NULL,'1'),('10101','Avatar-2.png','image/png','user',NULL,'1'),('10102','Avatar-3.png','image/png','user',NULL,'1'),('10103','Avatar-4.png','image/png','user',NULL,'1'),('10104','Avatar-5.png','image/png','user',NULL,'1'),('10105','Avatar-6.png','image/png','user',NULL,'1'),('10106','Avatar-7.png','image/png','user',NULL,'1'),('10107','Avatar-8.png','image/png','user',NULL,'1'),('10108','Avatar-9.png','image/png','user',NULL,'1'),('10109','Avatar-10.png','image/png','user',NULL,'1'),('10110','Avatar-11.png','image/png','user',NULL,'1'),('10111','Avatar-12.png','image/png','user',NULL,'1'),('10112','Avatar-13.png','image/png','user',NULL,'1'),('10113','Avatar-14.png','image/png','user',NULL,'1'),('10114','Avatar-15.png','image/png','user',NULL,'1'),('10115','Avatar-16.png','image/png','user',NULL,'1'),('10116','Avatar-17.png','image/png','user',NULL,'1'),('10117','Avatar-18.png','image/png','user',NULL,'1'),('10118','Avatar-19.png','image/png','user',NULL,'1'),('10119','Avatar-20.png','image/png','user',NULL,'1'),('10120','Avatar-21.png','image/png','user',NULL,'1'),('10121','Avatar-22.png','image/png','user',NULL,'1'),('10122','Avatar-default.png','image/png','user',NULL,'1'),('10123','Avatar-unknown.png','image/png','user',NULL,'1'),('10124','cloud.png','image/png','project',NULL,'1'),('10125','config.png','image/png','project',NULL,'1'),('10126','disc.png','image/png','project',NULL,'1'),('10127','finance.png','image/png','project',NULL,'1'),('10128','hand.png','image/png','project',NULL,'1'),('10129','new_monster.png','image/png','project',NULL,'1'),('10130','power.png','image/png','project',NULL,'1'),('10131','refresh.png','image/png','project',NULL,'1'),('10132','servicedesk.png','image/png','project',NULL,'1'),('10133','settings.png','image/png','project',NULL,'1'),('10134','storm.png','image/png','project',NULL,'1'),('10135','travel.png','image/png','project',NULL,'1');
/*!40000 ALTER TABLE `avatar` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `changegroup`
--

DROP TABLE IF EXISTS `changegroup`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `changegroup` (
  `ID` decimal(18,0) NOT NULL,
  `issueid` decimal(18,0) default NULL,
  `AUTHOR` varchar(255) collate utf8_bin default NULL,
  `CREATED` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `chggroup_issue` (`issueid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `changegroup`
--

LOCK TABLES `changegroup` WRITE;
/*!40000 ALTER TABLE `changegroup` DISABLE KEYS */;
/*!40000 ALTER TABLE `changegroup` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `changeitem`
--

DROP TABLE IF EXISTS `changeitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `changeitem` (
  `ID` decimal(18,0) NOT NULL,
  `groupid` decimal(18,0) default NULL,
  `FIELDTYPE` varchar(255) collate utf8_bin default NULL,
  `FIELD` varchar(255) collate utf8_bin default NULL,
  `OLDVALUE` longtext collate utf8_bin,
  `OLDSTRING` longtext collate utf8_bin,
  `NEWVALUE` longtext collate utf8_bin,
  `NEWSTRING` longtext collate utf8_bin,
  PRIMARY KEY  (`ID`),
  KEY `chgitem_chggrp` (`groupid`),
  KEY `chgitem_field` (`FIELD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `changeitem`
--

LOCK TABLES `changeitem` WRITE;
/*!40000 ALTER TABLE `changeitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `changeitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `columnlayout`
--

DROP TABLE IF EXISTS `columnlayout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `columnlayout` (
  `ID` decimal(18,0) NOT NULL,
  `USERNAME` varchar(255) collate utf8_bin default NULL,
  `SEARCHREQUEST` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `cl_searchrequest` (`SEARCHREQUEST`),
  KEY `cl_username` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `columnlayout`
--

LOCK TABLES `columnlayout` WRITE;
/*!40000 ALTER TABLE `columnlayout` DISABLE KEYS */;
/*!40000 ALTER TABLE `columnlayout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `columnlayoutitem`
--

DROP TABLE IF EXISTS `columnlayoutitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `columnlayoutitem` (
  `ID` decimal(18,0) NOT NULL,
  `COLUMNLAYOUT` decimal(18,0) default NULL,
  `FIELDIDENTIFIER` varchar(255) collate utf8_bin default NULL,
  `HORIZONTALPOSITION` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `columnlayoutitem`
--

LOCK TABLES `columnlayoutitem` WRITE;
/*!40000 ALTER TABLE `columnlayoutitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `columnlayoutitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `component`
--

DROP TABLE IF EXISTS `component`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `component` (
  `ID` decimal(18,0) NOT NULL,
  `PROJECT` decimal(18,0) default NULL,
  `cname` varchar(255) collate utf8_bin default NULL,
  `description` text collate utf8_bin,
  `URL` varchar(255) collate utf8_bin default NULL,
  `LEAD` varchar(255) collate utf8_bin default NULL,
  `ASSIGNEETYPE` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `component`
--

LOCK TABLES `component` WRITE;
/*!40000 ALTER TABLE `component` DISABLE KEYS */;
/*!40000 ALTER TABLE `component` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `configurationcontext`
--

DROP TABLE IF EXISTS `configurationcontext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `configurationcontext` (
  `ID` decimal(18,0) NOT NULL,
  `PROJECTCATEGORY` decimal(18,0) default NULL,
  `PROJECT` decimal(18,0) default NULL,
  `customfield` varchar(255) collate utf8_bin default NULL,
  `FIELDCONFIGSCHEME` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `confcontext` (`PROJECTCATEGORY`,`PROJECT`,`customfield`),
  KEY `confcontextprojectkey` (`PROJECT`,`customfield`),
  KEY `confcontextfieldconfigscheme` (`FIELDCONFIGSCHEME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `configurationcontext`
--

LOCK TABLES `configurationcontext` WRITE;
/*!40000 ALTER TABLE `configurationcontext` DISABLE KEYS */;
INSERT INTO `configurationcontext` VALUES ('10000',NULL,NULL,'issuetype','10000');
/*!40000 ALTER TABLE `configurationcontext` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customfield`
--

DROP TABLE IF EXISTS `customfield`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customfield` (
  `ID` decimal(18,0) NOT NULL,
  `CUSTOMFIELDTYPEKEY` varchar(255) collate utf8_bin default NULL,
  `CUSTOMFIELDSEARCHERKEY` varchar(255) collate utf8_bin default NULL,
  `cfname` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `defaultvalue` varchar(255) collate utf8_bin default NULL,
  `FIELDTYPE` decimal(18,0) default NULL,
  `PROJECT` decimal(18,0) default NULL,
  `ISSUETYPE` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customfield`
--

LOCK TABLES `customfield` WRITE;
/*!40000 ALTER TABLE `customfield` DISABLE KEYS */;
/*!40000 ALTER TABLE `customfield` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customfieldoption`
--

DROP TABLE IF EXISTS `customfieldoption`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customfieldoption` (
  `ID` decimal(18,0) NOT NULL,
  `CUSTOMFIELD` decimal(18,0) default NULL,
  `CUSTOMFIELDCONFIG` decimal(18,0) default NULL,
  `PARENTOPTIONID` decimal(18,0) default NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  `customvalue` varchar(255) collate utf8_bin default NULL,
  `optiontype` varchar(60) collate utf8_bin default NULL,
  `disabled` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `cf_cfoption` (`CUSTOMFIELD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customfieldoption`
--

LOCK TABLES `customfieldoption` WRITE;
/*!40000 ALTER TABLE `customfieldoption` DISABLE KEYS */;
/*!40000 ALTER TABLE `customfieldoption` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customfieldvalue`
--

DROP TABLE IF EXISTS `customfieldvalue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customfieldvalue` (
  `ID` decimal(18,0) NOT NULL,
  `ISSUE` decimal(18,0) default NULL,
  `CUSTOMFIELD` decimal(18,0) default NULL,
  `PARENTKEY` varchar(255) collate utf8_bin default NULL,
  `STRINGVALUE` varchar(255) collate utf8_bin default NULL,
  `NUMBERVALUE` decimal(18,6) default NULL,
  `TEXTVALUE` longtext collate utf8_bin,
  `DATEVALUE` datetime default NULL,
  `VALUETYPE` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `cfvalue_issue` (`ISSUE`,`CUSTOMFIELD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customfieldvalue`
--

LOCK TABLES `customfieldvalue` WRITE;
/*!40000 ALTER TABLE `customfieldvalue` DISABLE KEYS */;
/*!40000 ALTER TABLE `customfieldvalue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_application`
--

DROP TABLE IF EXISTS `cwd_application`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_application` (
  `ID` decimal(18,0) NOT NULL,
  `application_name` varchar(255) collate utf8_bin default NULL,
  `lower_application_name` varchar(255) collate utf8_bin default NULL,
  `created_date` datetime default NULL,
  `updated_date` datetime default NULL,
  `active` decimal(9,0) default NULL,
  `description` varchar(255) collate utf8_bin default NULL,
  `application_type` varchar(255) collate utf8_bin default NULL,
  `credential` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `uk_application_name` (`lower_application_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_application`
--

LOCK TABLES `cwd_application` WRITE;
/*!40000 ALTER TABLE `cwd_application` DISABLE KEYS */;
INSERT INTO `cwd_application` VALUES ('1','crowd-embedded','crowd-embedded','2013-08-27 16:59:34','2013-08-27 16:59:34','1','','CROWD','X');
/*!40000 ALTER TABLE `cwd_application` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_application_address`
--

DROP TABLE IF EXISTS `cwd_application_address`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_application_address` (
  `application_id` decimal(18,0) NOT NULL,
  `remote_address` varchar(255) collate utf8_bin NOT NULL,
  `encoded_address_binary` varchar(255) collate utf8_bin default NULL,
  `remote_address_mask` decimal(9,0) default NULL,
  PRIMARY KEY  (`application_id`,`remote_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_application_address`
--

LOCK TABLES `cwd_application_address` WRITE;
/*!40000 ALTER TABLE `cwd_application_address` DISABLE KEYS */;
/*!40000 ALTER TABLE `cwd_application_address` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_directory`
--

DROP TABLE IF EXISTS `cwd_directory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_directory` (
  `ID` decimal(18,0) NOT NULL,
  `directory_name` varchar(255) collate utf8_bin default NULL,
  `lower_directory_name` varchar(255) collate utf8_bin default NULL,
  `created_date` datetime default NULL,
  `updated_date` datetime default NULL,
  `active` decimal(9,0) default NULL,
  `description` varchar(255) collate utf8_bin default NULL,
  `impl_class` varchar(255) collate utf8_bin default NULL,
  `lower_impl_class` varchar(255) collate utf8_bin default NULL,
  `directory_type` varchar(60) collate utf8_bin default NULL,
  `directory_position` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `uk_directory_name` (`lower_directory_name`),
  KEY `idx_directory_active` (`active`),
  KEY `idx_directory_impl` (`lower_impl_class`),
  KEY `idx_directory_type` (`directory_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_directory`
--

LOCK TABLES `cwd_directory` WRITE;
/*!40000 ALTER TABLE `cwd_directory` DISABLE KEYS */;
INSERT INTO `cwd_directory` VALUES ('1','JIRA Internal Directory','jira internal directory','2013-08-27 16:59:34','2013-08-27 16:59:34','1','JIRA default internal directory','com.atlassian.crowd.directory.InternalDirectory','com.atlassian.crowd.directory.internaldirectory','INTERNAL','0');
/*!40000 ALTER TABLE `cwd_directory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_directory_attribute`
--

DROP TABLE IF EXISTS `cwd_directory_attribute`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_directory_attribute` (
  `directory_id` decimal(18,0) NOT NULL,
  `attribute_name` varchar(255) collate utf8_bin NOT NULL,
  `attribute_value` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`directory_id`,`attribute_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_directory_attribute`
--

LOCK TABLES `cwd_directory_attribute` WRITE;
/*!40000 ALTER TABLE `cwd_directory_attribute` DISABLE KEYS */;
INSERT INTO `cwd_directory_attribute` VALUES ('1','user_encryption_method','atlassian-security');
/*!40000 ALTER TABLE `cwd_directory_attribute` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_directory_operation`
--

DROP TABLE IF EXISTS `cwd_directory_operation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_directory_operation` (
  `directory_id` decimal(18,0) NOT NULL,
  `operation_type` varchar(60) collate utf8_bin NOT NULL,
  PRIMARY KEY  (`directory_id`,`operation_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_directory_operation`
--

LOCK TABLES `cwd_directory_operation` WRITE;
/*!40000 ALTER TABLE `cwd_directory_operation` DISABLE KEYS */;
INSERT INTO `cwd_directory_operation` VALUES ('1','CREATE_GROUP'),('1','CREATE_ROLE'),('1','CREATE_USER'),('1','DELETE_GROUP'),('1','DELETE_ROLE'),('1','DELETE_USER'),('1','UPDATE_GROUP'),('1','UPDATE_GROUP_ATTRIBUTE'),('1','UPDATE_ROLE'),('1','UPDATE_ROLE_ATTRIBUTE'),('1','UPDATE_USER'),('1','UPDATE_USER_ATTRIBUTE');
/*!40000 ALTER TABLE `cwd_directory_operation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_group`
--

DROP TABLE IF EXISTS `cwd_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_group` (
  `ID` decimal(18,0) NOT NULL,
  `group_name` varchar(255) collate utf8_bin default NULL,
  `lower_group_name` varchar(255) collate utf8_bin default NULL,
  `active` decimal(9,0) default NULL,
  `local` decimal(9,0) default NULL,
  `created_date` datetime default NULL,
  `updated_date` datetime default NULL,
  `description` varchar(255) collate utf8_bin default NULL,
  `lower_description` varchar(255) collate utf8_bin default NULL,
  `group_type` varchar(60) collate utf8_bin default NULL,
  `directory_id` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `uk_group_name_dir_id` (`lower_group_name`,`directory_id`),
  KEY `idx_group_active` (`lower_group_name`,`active`),
  KEY `idx_group_dir_id` (`directory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_group`
--

LOCK TABLES `cwd_group` WRITE;
/*!40000 ALTER TABLE `cwd_group` DISABLE KEYS */;
INSERT INTO `cwd_group` VALUES ('10000','jira-administrators','jira-administrators','1','0','2013-08-27 16:59:34','2013-08-27 16:59:34','',NULL,'GROUP','1'),('10001','jira-developers','jira-developers','1','0','2013-08-27 16:59:34','2013-08-27 16:59:34','',NULL,'GROUP','1'),('10002','jira-users','jira-users','1','0','2013-08-27 16:59:34','2013-08-27 16:59:34','',NULL,'GROUP','1');
/*!40000 ALTER TABLE `cwd_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_group_attributes`
--

DROP TABLE IF EXISTS `cwd_group_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_group_attributes` (
  `ID` decimal(18,0) NOT NULL,
  `group_id` decimal(18,0) default NULL,
  `directory_id` decimal(18,0) default NULL,
  `attribute_name` varchar(255) collate utf8_bin default NULL,
  `attribute_value` varchar(255) collate utf8_bin default NULL,
  `lower_attribute_value` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `uk_group_attr_name_lval` (`group_id`,`attribute_name`,`lower_attribute_value`),
  KEY `idx_group_attr_dir_name_lval` (`directory_id`,`attribute_name`,`lower_attribute_value`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_group_attributes`
--

LOCK TABLES `cwd_group_attributes` WRITE;
/*!40000 ALTER TABLE `cwd_group_attributes` DISABLE KEYS */;
/*!40000 ALTER TABLE `cwd_group_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_membership`
--

DROP TABLE IF EXISTS `cwd_membership`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_membership` (
  `ID` decimal(18,0) NOT NULL,
  `parent_id` decimal(18,0) default NULL,
  `child_id` decimal(18,0) default NULL,
  `membership_type` varchar(60) collate utf8_bin default NULL,
  `group_type` varchar(60) collate utf8_bin default NULL,
  `parent_name` varchar(255) collate utf8_bin default NULL,
  `lower_parent_name` varchar(255) collate utf8_bin default NULL,
  `child_name` varchar(255) collate utf8_bin default NULL,
  `lower_child_name` varchar(255) collate utf8_bin default NULL,
  `directory_id` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `uk_mem_parent_child_type` (`parent_id`,`child_id`,`membership_type`),
  KEY `idx_mem_dir_parent_child` (`lower_parent_name`,`lower_child_name`,`membership_type`,`directory_id`),
  KEY `idx_mem_dir_parent` (`lower_parent_name`,`membership_type`,`directory_id`),
  KEY `idx_mem_dir_child` (`lower_child_name`,`membership_type`,`directory_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_membership`
--

LOCK TABLES `cwd_membership` WRITE;
/*!40000 ALTER TABLE `cwd_membership` DISABLE KEYS */;
INSERT INTO `cwd_membership` VALUES ('10000','10000','10000','GROUP_USER',NULL,'jira-administrators','jira-administrators','Administrator','administrator','1'),('10001','10001','10000','GROUP_USER',NULL,'jira-developers','jira-developers','Administrator','administrator','1'),('10002','10002','10000','GROUP_USER',NULL,'jira-users','jira-users','Administrator','administrator','1');
/*!40000 ALTER TABLE `cwd_membership` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_user`
--

DROP TABLE IF EXISTS `cwd_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_user` (
  `ID` decimal(18,0) NOT NULL,
  `directory_id` decimal(18,0) default NULL,
  `user_name` varchar(255) collate utf8_bin default NULL,
  `lower_user_name` varchar(255) collate utf8_bin default NULL,
  `active` decimal(9,0) default NULL,
  `created_date` datetime default NULL,
  `updated_date` datetime default NULL,
  `first_name` varchar(255) collate utf8_bin default NULL,
  `lower_first_name` varchar(255) collate utf8_bin default NULL,
  `last_name` varchar(255) collate utf8_bin default NULL,
  `lower_last_name` varchar(255) collate utf8_bin default NULL,
  `display_name` varchar(255) collate utf8_bin default NULL,
  `lower_display_name` varchar(255) collate utf8_bin default NULL,
  `email_address` varchar(255) collate utf8_bin default NULL,
  `lower_email_address` varchar(255) collate utf8_bin default NULL,
  `CREDENTIAL` varchar(255) collate utf8_bin default NULL,
  `deleted_externally` decimal(9,0) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `uk_user_name_dir_id` (`lower_user_name`,`directory_id`),
  KEY `idx_first_name` (`lower_first_name`),
  KEY `idx_last_name` (`lower_last_name`),
  KEY `idx_display_name` (`lower_display_name`),
  KEY `idx_email_address` (`lower_email_address`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_user`
--

LOCK TABLES `cwd_user` WRITE;
/*!40000 ALTER TABLE `cwd_user` DISABLE KEYS */;
INSERT INTO `cwd_user` VALUES ('10000','1','Administrator','administrator','1','2013-08-27 16:59:34','2013-08-27 16:59:34','','','','','Administrator','administrator','novaforge_administrator@bull.net','novaforge_administrator@bull.net','{PKCS5S2}TFYhmhiwcfUtWOAf+Jn66G4nIvm72NxcMWWwE9GHX5jSJNFwXxkOlHw9Q1ykW2jU',NULL);
/*!40000 ALTER TABLE `cwd_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `cwd_user_attributes`
--

DROP TABLE IF EXISTS `cwd_user_attributes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cwd_user_attributes` (
  `ID` decimal(18,0) NOT NULL,
  `user_id` decimal(18,0) default NULL,
  `directory_id` decimal(18,0) default NULL,
  `attribute_name` varchar(255) collate utf8_bin default NULL,
  `attribute_value` varchar(255) collate utf8_bin default NULL,
  `lower_attribute_value` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `idx_user_attr_dir_name_lval` (`directory_id`,`attribute_name`,`lower_attribute_value`),
  KEY `uk_user_attr_name_lval` (`user_id`,`attribute_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cwd_user_attributes`
--

LOCK TABLES `cwd_user_attributes` WRITE;
/*!40000 ALTER TABLE `cwd_user_attributes` DISABLE KEYS */;
INSERT INTO `cwd_user_attributes` VALUES ('10','10000','1','invalidPasswordAttempts','0','0'),('11','10000','1','requiresPasswordChange','false','false'),('12','10000','1','lastAuthenticated','1378882868503','1378882868503'),('13','10000','1','login.currentFailedCount','0','0'),('14','10000','1','login.lastLoginMillis','1378882868513','1378882868513'),('15','10000','1','login.count','8','8'),('16','10000','1','login.previousLoginMillis','1378882845047','1378882845047');
/*!40000 ALTER TABLE `cwd_user_attributes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `draftworkflowscheme`
--

DROP TABLE IF EXISTS `draftworkflowscheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `draftworkflowscheme` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `WORKFLOW_SCHEME_ID` decimal(18,0) default NULL,
  `LAST_MODIFIED_DATE` datetime default NULL,
  `LAST_MODIFIED_USER` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `draft_workflow_scheme_parent` (`WORKFLOW_SCHEME_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `draftworkflowscheme`
--

LOCK TABLES `draftworkflowscheme` WRITE;
/*!40000 ALTER TABLE `draftworkflowscheme` DISABLE KEYS */;
/*!40000 ALTER TABLE `draftworkflowscheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `draftworkflowschemeentity`
--

DROP TABLE IF EXISTS `draftworkflowschemeentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `draftworkflowschemeentity` (
  `ID` decimal(18,0) NOT NULL,
  `SCHEME` decimal(18,0) default NULL,
  `WORKFLOW` varchar(255) collate utf8_bin default NULL,
  `issuetype` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `draft_workflow_scheme` (`SCHEME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `draftworkflowschemeentity`
--

LOCK TABLES `draftworkflowschemeentity` WRITE;
/*!40000 ALTER TABLE `draftworkflowschemeentity` DISABLE KEYS */;
/*!40000 ALTER TABLE `draftworkflowschemeentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `external_entities`
--

DROP TABLE IF EXISTS `external_entities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `external_entities` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `entitytype` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `ext_entity_name` (`NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `external_entities`
--

LOCK TABLES `external_entities` WRITE;
/*!40000 ALTER TABLE `external_entities` DISABLE KEYS */;
/*!40000 ALTER TABLE `external_entities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `externalgadget`
--

DROP TABLE IF EXISTS `externalgadget`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `externalgadget` (
  `ID` decimal(18,0) NOT NULL,
  `GADGET_XML` text collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `externalgadget`
--

LOCK TABLES `externalgadget` WRITE;
/*!40000 ALTER TABLE `externalgadget` DISABLE KEYS */;
/*!40000 ALTER TABLE `externalgadget` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `favouriteassociations`
--

DROP TABLE IF EXISTS `favouriteassociations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `favouriteassociations` (
  `ID` decimal(18,0) NOT NULL,
  `USERNAME` varchar(255) collate utf8_bin default NULL,
  `entitytype` varchar(60) collate utf8_bin default NULL,
  `entityid` decimal(18,0) default NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `favourite_index` (`USERNAME`,`entitytype`,`entityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `favouriteassociations`
--

LOCK TABLES `favouriteassociations` WRITE;
/*!40000 ALTER TABLE `favouriteassociations` DISABLE KEYS */;
/*!40000 ALTER TABLE `favouriteassociations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feature`
--

DROP TABLE IF EXISTS `feature`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `feature` (
  `ID` decimal(18,0) NOT NULL,
  `FEATURE_NAME` varchar(255) collate utf8_bin default NULL,
  `FEATURE_TYPE` varchar(10) collate utf8_bin default NULL,
  `USER_KEY` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `feature_id_userkey` (`ID`,`USER_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feature`
--

LOCK TABLES `feature` WRITE;
/*!40000 ALTER TABLE `feature` DISABLE KEYS */;
/*!40000 ALTER TABLE `feature` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldconfigscheme`
--

DROP TABLE IF EXISTS `fieldconfigscheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldconfigscheme` (
  `ID` decimal(18,0) NOT NULL,
  `configname` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `FIELDID` varchar(60) collate utf8_bin default NULL,
  `CUSTOMFIELD` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fcs_fieldid` (`FIELDID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldconfigscheme`
--

LOCK TABLES `fieldconfigscheme` WRITE;
/*!40000 ALTER TABLE `fieldconfigscheme` DISABLE KEYS */;
INSERT INTO `fieldconfigscheme` VALUES ('10000','Default Issue Type Scheme','Default issue type scheme is the list of global issue types. All newly created issue types will automatically be added to this scheme.','issuetype',NULL);
/*!40000 ALTER TABLE `fieldconfigscheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldconfigschemeissuetype`
--

DROP TABLE IF EXISTS `fieldconfigschemeissuetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldconfigschemeissuetype` (
  `ID` decimal(18,0) NOT NULL,
  `ISSUETYPE` varchar(255) collate utf8_bin default NULL,
  `FIELDCONFIGSCHEME` decimal(18,0) default NULL,
  `FIELDCONFIGURATION` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fcs_issuetype` (`ISSUETYPE`),
  KEY `fcs_scheme` (`FIELDCONFIGSCHEME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldconfigschemeissuetype`
--

LOCK TABLES `fieldconfigschemeissuetype` WRITE;
/*!40000 ALTER TABLE `fieldconfigschemeissuetype` DISABLE KEYS */;
INSERT INTO `fieldconfigschemeissuetype` VALUES ('10100',NULL,'10000','10000');
/*!40000 ALTER TABLE `fieldconfigschemeissuetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldconfiguration`
--

DROP TABLE IF EXISTS `fieldconfiguration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldconfiguration` (
  `ID` decimal(18,0) NOT NULL,
  `configname` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `FIELDID` varchar(60) collate utf8_bin default NULL,
  `CUSTOMFIELD` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fc_fieldid` (`FIELDID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldconfiguration`
--

LOCK TABLES `fieldconfiguration` WRITE;
/*!40000 ALTER TABLE `fieldconfiguration` DISABLE KEYS */;
INSERT INTO `fieldconfiguration` VALUES ('10000','Default Configuration for Type de demande','Default configuration generated by JIRA','issuetype',NULL);
/*!40000 ALTER TABLE `fieldconfiguration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldlayout`
--

DROP TABLE IF EXISTS `fieldlayout`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldlayout` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` varchar(255) collate utf8_bin default NULL,
  `layout_type` varchar(255) collate utf8_bin default NULL,
  `LAYOUTSCHEME` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldlayout`
--

LOCK TABLES `fieldlayout` WRITE;
/*!40000 ALTER TABLE `fieldlayout` DISABLE KEYS */;
INSERT INTO `fieldlayout` VALUES ('10000','Default Field Configuration','The default field configuration','default',NULL);
/*!40000 ALTER TABLE `fieldlayout` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldlayoutitem`
--

DROP TABLE IF EXISTS `fieldlayoutitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldlayoutitem` (
  `ID` decimal(18,0) NOT NULL,
  `FIELDLAYOUT` decimal(18,0) default NULL,
  `FIELDIDENTIFIER` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `VERTICALPOSITION` decimal(18,0) default NULL,
  `ISHIDDEN` varchar(60) collate utf8_bin default NULL,
  `ISREQUIRED` varchar(60) collate utf8_bin default NULL,
  `RENDERERTYPE` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldlayoutitem`
--

LOCK TABLES `fieldlayoutitem` WRITE;
/*!40000 ALTER TABLE `fieldlayoutitem` DISABLE KEYS */;
INSERT INTO `fieldlayoutitem` VALUES ('10000','10000','summary',NULL,NULL,'false','true','jira-text-renderer'),('10001','10000','issuetype',NULL,NULL,'false','true','jira-text-renderer'),('10002','10000','security',NULL,NULL,'false','false','jira-text-renderer'),('10003','10000','priority',NULL,NULL,'false','false','jira-text-renderer'),('10004','10000','duedate',NULL,NULL,'false','false','jira-text-renderer'),('10005','10000','components',NULL,NULL,'false','false','frother-control-renderer'),('10006','10000','versions',NULL,NULL,'false','false','frother-control-renderer'),('10007','10000','fixVersions',NULL,NULL,'false','false','frother-control-renderer'),('10008','10000','assignee',NULL,NULL,'false','false','jira-text-renderer'),('10009','10000','reporter',NULL,NULL,'false','true','jira-text-renderer'),('10010','10000','environment','Par exemple, le système d\'exploitation, la plate-forme logicielle et/ou les caractéristiques matérielles (en fonction de la demande).',NULL,'false','false','atlassian-wiki-renderer'),('10011','10000','description',NULL,NULL,'false','false','atlassian-wiki-renderer'),('10012','10000','timetracking','Estimation du travail restant jusqu\'à la résolution de la demande.<br>Le format utilisé est \' *w *d *h *m \' (représentant les semaines, jours, heures et minutes - où * peut être tout nombre)<br>Exemples&nbsp;: 4d, 5h 30m, 60m et 3w.<br>',NULL,'false','false','jira-text-renderer'),('10013','10000','resolution',NULL,NULL,'false','false','jira-text-renderer'),('10014','10000','attachment',NULL,NULL,'false','false','jira-text-renderer'),('10015','10000','comment',NULL,NULL,'false','false','atlassian-wiki-renderer'),('10016','10000','labels',NULL,NULL,'false','false','jira-text-renderer'),('10017','10000','worklog','Permet de consigner le travail tout en créant, modifiant ou en faisant transiter les demandes.',NULL,'false','false','atlassian-wiki-renderer'),('10018','10000','issuelinks',NULL,NULL,'false','false','jira-text-renderer');
/*!40000 ALTER TABLE `fieldlayoutitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldlayoutscheme`
--

DROP TABLE IF EXISTS `fieldlayoutscheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldlayoutscheme` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldlayoutscheme`
--

LOCK TABLES `fieldlayoutscheme` WRITE;
/*!40000 ALTER TABLE `fieldlayoutscheme` DISABLE KEYS */;
/*!40000 ALTER TABLE `fieldlayoutscheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldlayoutschemeassociation`
--

DROP TABLE IF EXISTS `fieldlayoutschemeassociation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldlayoutschemeassociation` (
  `ID` decimal(18,0) NOT NULL,
  `ISSUETYPE` varchar(255) collate utf8_bin default NULL,
  `PROJECT` decimal(18,0) default NULL,
  `FIELDLAYOUTSCHEME` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fl_scheme_assoc` (`PROJECT`,`ISSUETYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldlayoutschemeassociation`
--

LOCK TABLES `fieldlayoutschemeassociation` WRITE;
/*!40000 ALTER TABLE `fieldlayoutschemeassociation` DISABLE KEYS */;
/*!40000 ALTER TABLE `fieldlayoutschemeassociation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldlayoutschemeentity`
--

DROP TABLE IF EXISTS `fieldlayoutschemeentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldlayoutschemeentity` (
  `ID` decimal(18,0) NOT NULL,
  `SCHEME` decimal(18,0) default NULL,
  `issuetype` varchar(255) collate utf8_bin default NULL,
  `FIELDLAYOUT` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fieldlayout_scheme` (`SCHEME`),
  KEY `fieldlayout_layout` (`FIELDLAYOUT`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldlayoutschemeentity`
--

LOCK TABLES `fieldlayoutschemeentity` WRITE;
/*!40000 ALTER TABLE `fieldlayoutschemeentity` DISABLE KEYS */;
/*!40000 ALTER TABLE `fieldlayoutschemeentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldscreen`
--

DROP TABLE IF EXISTS `fieldscreen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldscreen` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldscreen`
--

LOCK TABLES `fieldscreen` WRITE;
/*!40000 ALTER TABLE `fieldscreen` DISABLE KEYS */;
INSERT INTO `fieldscreen` VALUES ('1','Ecran Par défaut','Permet de mettre à jour tous les champs du système.'),('2','Ecran de flux de travaux','Cet écran est utilisé dans le flux de travaux est vous permet d\'attribuer les demandes'),('3','Ecran Résoudre la demande','Permet de régler la résolution, de changer les versions corrigées et d\'attribuer une demande.');
/*!40000 ALTER TABLE `fieldscreen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldscreenlayoutitem`
--

DROP TABLE IF EXISTS `fieldscreenlayoutitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldscreenlayoutitem` (
  `ID` decimal(18,0) NOT NULL,
  `FIELDIDENTIFIER` varchar(255) collate utf8_bin default NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  `FIELDSCREENTAB` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fieldscitem_tab` (`FIELDSCREENTAB`),
  KEY `fieldscreen_field` (`FIELDIDENTIFIER`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldscreenlayoutitem`
--

LOCK TABLES `fieldscreenlayoutitem` WRITE;
/*!40000 ALTER TABLE `fieldscreenlayoutitem` DISABLE KEYS */;
INSERT INTO `fieldscreenlayoutitem` VALUES ('10000','summary','0','10000'),('10001','issuetype','1','10000'),('10002','security','2','10000'),('10003','priority','3','10000'),('10004','duedate','4','10000'),('10005','components','5','10000'),('10006','versions','6','10000'),('10007','fixVersions','7','10000'),('10008','assignee','8','10000'),('10009','reporter','9','10000'),('10010','environment','10','10000'),('10011','description','11','10000'),('10012','timetracking','12','10000'),('10013','attachment','13','10000'),('10014','assignee','0','10001'),('10015','resolution','0','10002'),('10016','fixVersions','1','10002'),('10017','assignee','2','10002'),('10018','worklog','3','10002'),('10100','labels','14','10000');
/*!40000 ALTER TABLE `fieldscreenlayoutitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldscreenscheme`
--

DROP TABLE IF EXISTS `fieldscreenscheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldscreenscheme` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldscreenscheme`
--

LOCK TABLES `fieldscreenscheme` WRITE;
/*!40000 ALTER TABLE `fieldscreenscheme` DISABLE KEYS */;
INSERT INTO `fieldscreenscheme` VALUES ('1','Système d\'écran Par défaut','Système d\'écran Par défaut');
/*!40000 ALTER TABLE `fieldscreenscheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldscreenschemeitem`
--

DROP TABLE IF EXISTS `fieldscreenschemeitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldscreenschemeitem` (
  `ID` decimal(18,0) NOT NULL,
  `OPERATION` decimal(18,0) default NULL,
  `FIELDSCREEN` decimal(18,0) default NULL,
  `FIELDSCREENSCHEME` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `screenitem_scheme` (`FIELDSCREENSCHEME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldscreenschemeitem`
--

LOCK TABLES `fieldscreenschemeitem` WRITE;
/*!40000 ALTER TABLE `fieldscreenschemeitem` DISABLE KEYS */;
INSERT INTO `fieldscreenschemeitem` VALUES ('10000',NULL,'1','1');
/*!40000 ALTER TABLE `fieldscreenschemeitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fieldscreentab`
--

DROP TABLE IF EXISTS `fieldscreentab`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fieldscreentab` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` varchar(255) collate utf8_bin default NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  `FIELDSCREEN` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fieldscreen_tab` (`FIELDSCREEN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fieldscreentab`
--

LOCK TABLES `fieldscreentab` WRITE;
/*!40000 ALTER TABLE `fieldscreentab` DISABLE KEYS */;
INSERT INTO `fieldscreentab` VALUES ('10000','Field Tab',NULL,'0','1'),('10001','Field Tab',NULL,'0','2'),('10002','Field Tab',NULL,'0','3');
/*!40000 ALTER TABLE `fieldscreentab` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `fileattachment`
--

DROP TABLE IF EXISTS `fileattachment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fileattachment` (
  `ID` decimal(18,0) NOT NULL,
  `issueid` decimal(18,0) default NULL,
  `MIMETYPE` varchar(255) collate utf8_bin default NULL,
  `FILENAME` varchar(255) collate utf8_bin default NULL,
  `CREATED` datetime default NULL,
  `FILESIZE` decimal(18,0) default NULL,
  `AUTHOR` varchar(255) collate utf8_bin default NULL,
  `zip` decimal(9,0) default NULL,
  `thumbnailable` decimal(9,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `attach_issue` (`issueid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `fileattachment`
--

LOCK TABLES `fileattachment` WRITE;
/*!40000 ALTER TABLE `fileattachment` DISABLE KEYS */;
/*!40000 ALTER TABLE `fileattachment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `filtersubscription`
--

DROP TABLE IF EXISTS `filtersubscription`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `filtersubscription` (
  `ID` decimal(18,0) NOT NULL,
  `FILTER_I_D` decimal(18,0) default NULL,
  `USERNAME` varchar(60) collate utf8_bin default NULL,
  `groupname` varchar(60) collate utf8_bin default NULL,
  `LAST_RUN` datetime default NULL,
  `EMAIL_ON_EMPTY` varchar(10) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `subscrpt_user` (`FILTER_I_D`,`USERNAME`),
  KEY `subscrptn_group` (`FILTER_I_D`,`groupname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `filtersubscription`
--

LOCK TABLES `filtersubscription` WRITE;
/*!40000 ALTER TABLE `filtersubscription` DISABLE KEYS */;
/*!40000 ALTER TABLE `filtersubscription` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `gadgetuserpreference`
--

DROP TABLE IF EXISTS `gadgetuserpreference`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `gadgetuserpreference` (
  `ID` decimal(18,0) NOT NULL,
  `PORTLETCONFIGURATION` decimal(18,0) default NULL,
  `USERPREFKEY` varchar(255) collate utf8_bin default NULL,
  `USERPREFVALUE` longtext collate utf8_bin,
  PRIMARY KEY  (`ID`),
  KEY `userpref_portletconfiguration` (`PORTLETCONFIGURATION`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gadgetuserpreference`
--

LOCK TABLES `gadgetuserpreference` WRITE;
/*!40000 ALTER TABLE `gadgetuserpreference` DISABLE KEYS */;
INSERT INTO `gadgetuserpreference` VALUES ('10000','10002','isConfigured','true'),('10001','10003','keys','__all_projects__'),('10002','10003','isConfigured','true'),('10003','10003','title','NovaForge'),('10004','10003','numofentries','5');
/*!40000 ALTER TABLE `gadgetuserpreference` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genericconfiguration`
--

DROP TABLE IF EXISTS `genericconfiguration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `genericconfiguration` (
  `ID` decimal(18,0) NOT NULL,
  `DATATYPE` varchar(60) collate utf8_bin default NULL,
  `DATAKEY` varchar(60) collate utf8_bin default NULL,
  `XMLVALUE` text collate utf8_bin,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `type_key` (`DATATYPE`,`DATAKEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genericconfiguration`
--

LOCK TABLES `genericconfiguration` WRITE;
/*!40000 ALTER TABLE `genericconfiguration` DISABLE KEYS */;
INSERT INTO `genericconfiguration` VALUES ('10000','DefaultValue','10000','<string>1</string>');
/*!40000 ALTER TABLE `genericconfiguration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `groupbase`
--

DROP TABLE IF EXISTS `groupbase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `groupbase` (
  `ID` decimal(18,0) NOT NULL,
  `groupname` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `osgroup_name` (`groupname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `groupbase`
--

LOCK TABLES `groupbase` WRITE;
/*!40000 ALTER TABLE `groupbase` DISABLE KEYS */;
/*!40000 ALTER TABLE `groupbase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuelink`
--

DROP TABLE IF EXISTS `issuelink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issuelink` (
  `ID` decimal(18,0) NOT NULL,
  `LINKTYPE` decimal(18,0) default NULL,
  `SOURCE` decimal(18,0) default NULL,
  `DESTINATION` decimal(18,0) default NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `issuelink_src` (`SOURCE`),
  KEY `issuelink_dest` (`DESTINATION`),
  KEY `issuelink_type` (`LINKTYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuelink`
--

LOCK TABLES `issuelink` WRITE;
/*!40000 ALTER TABLE `issuelink` DISABLE KEYS */;
/*!40000 ALTER TABLE `issuelink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuelinktype`
--

DROP TABLE IF EXISTS `issuelinktype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issuelinktype` (
  `ID` decimal(18,0) NOT NULL,
  `LINKNAME` varchar(255) collate utf8_bin default NULL,
  `INWARD` varchar(255) collate utf8_bin default NULL,
  `OUTWARD` varchar(255) collate utf8_bin default NULL,
  `pstyle` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `linktypename` (`LINKNAME`),
  KEY `linktypestyle` (`pstyle`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuelinktype`
--

LOCK TABLES `issuelinktype` WRITE;
/*!40000 ALTER TABLE `issuelinktype` DISABLE KEYS */;
INSERT INTO `issuelinktype` VALUES ('10000','Blocks','is blocked by','blocks',NULL),('10001','Cloners','is cloned by','clones',NULL),('10002','Duplicate','is duplicated by','duplicates',NULL),('10003','Relates','relates to','relates to',NULL),('10100','jira_subtask_link','jira_subtask_inward','jira_subtask_outward','jira_subtask');
/*!40000 ALTER TABLE `issuelinktype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuesecurityscheme`
--

DROP TABLE IF EXISTS `issuesecurityscheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issuesecurityscheme` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `DEFAULTLEVEL` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuesecurityscheme`
--

LOCK TABLES `issuesecurityscheme` WRITE;
/*!40000 ALTER TABLE `issuesecurityscheme` DISABLE KEYS */;
/*!40000 ALTER TABLE `issuesecurityscheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuestatus`
--

DROP TABLE IF EXISTS `issuestatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issuestatus` (
  `ID` varchar(60) collate utf8_bin NOT NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  `pname` varchar(60) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `ICONURL` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuestatus`
--

LOCK TABLES `issuestatus` WRITE;
/*!40000 ALTER TABLE `issuestatus` DISABLE KEYS */;
INSERT INTO `issuestatus` VALUES ('1','1','Open','The issue is open and ready for the assignee to start work on it.','/images/icons/statuses/open.png'),('3','3','In Progress','This issue is being actively worked on at the moment by the assignee.','/images/icons/statuses/inprogress.png'),('4','4','Reopened','This issue was once resolved, but the resolution was deemed incorrect. From here issues are either marked assigned or resolved.','/images/icons/statuses/reopened.png'),('5','5','Resolved','A resolution has been taken, and it is awaiting verification by reporter. From here issues are either reopened, or are closed.','/images/icons/statuses/resolved.png'),('6','6','Closed','The issue is considered finished, the resolution is correct. Issues which are closed can be reopened.','/images/icons/statuses/closed.png');
/*!40000 ALTER TABLE `issuestatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuetype`
--

DROP TABLE IF EXISTS `issuetype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issuetype` (
  `ID` varchar(60) collate utf8_bin NOT NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  `pname` varchar(60) collate utf8_bin default NULL,
  `pstyle` varchar(60) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `ICONURL` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuetype`
--

LOCK TABLES `issuetype` WRITE;
/*!40000 ALTER TABLE `issuetype` DISABLE KEYS */;
INSERT INTO `issuetype` VALUES ('1','1','Bug',NULL,'A problem which impairs or prevents the functions of the product.','/images/icons/issuetypes/bug.png'),('2','2','New Feature',NULL,'A new feature of the product, which has yet to be developed.','/images/icons/issuetypes/newfeature.png'),('3','3','Task',NULL,'A task that needs to be done.','/images/icons/issuetypes/task.png'),('4','4','Improvement',NULL,'An improvement or enhancement to an existing feature or task.','/images/icons/issuetypes/improvement.png'),('5','0','Sub-task','jira_subtask','The sub-task of the issue','/images/icons/issuetypes/subtask_alternate.png');
/*!40000 ALTER TABLE `issuetype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuetypescreenscheme`
--

DROP TABLE IF EXISTS `issuetypescreenscheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issuetypescreenscheme` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuetypescreenscheme`
--

LOCK TABLES `issuetypescreenscheme` WRITE;
/*!40000 ALTER TABLE `issuetypescreenscheme` DISABLE KEYS */;
INSERT INTO `issuetypescreenscheme` VALUES ('1','Système d\'écran Type de demande par défaut','Le système d\'écran Type de demande par défaut');
/*!40000 ALTER TABLE `issuetypescreenscheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issuetypescreenschemeentity`
--

DROP TABLE IF EXISTS `issuetypescreenschemeentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `issuetypescreenschemeentity` (
  `ID` decimal(18,0) NOT NULL,
  `ISSUETYPE` varchar(255) collate utf8_bin default NULL,
  `SCHEME` decimal(18,0) default NULL,
  `FIELDSCREENSCHEME` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fieldscreen_scheme` (`FIELDSCREENSCHEME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issuetypescreenschemeentity`
--

LOCK TABLES `issuetypescreenschemeentity` WRITE;
/*!40000 ALTER TABLE `issuetypescreenschemeentity` DISABLE KEYS */;
INSERT INTO `issuetypescreenschemeentity` VALUES ('10000',NULL,'1','1');
/*!40000 ALTER TABLE `issuetypescreenschemeentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jiraaction`
--

DROP TABLE IF EXISTS `jiraaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jiraaction` (
  `ID` decimal(18,0) NOT NULL,
  `issueid` decimal(18,0) default NULL,
  `AUTHOR` varchar(255) collate utf8_bin default NULL,
  `actiontype` varchar(255) collate utf8_bin default NULL,
  `actionlevel` varchar(255) collate utf8_bin default NULL,
  `rolelevel` decimal(18,0) default NULL,
  `actionbody` longtext collate utf8_bin,
  `CREATED` datetime default NULL,
  `UPDATEAUTHOR` varchar(255) collate utf8_bin default NULL,
  `UPDATED` datetime default NULL,
  `actionnum` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `action_issue` (`issueid`,`actiontype`),
  KEY `action_authorcreated` (`issueid`,`AUTHOR`,`CREATED`),
  KEY `action_authorupdated` (`issueid`,`AUTHOR`,`UPDATED`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jiraaction`
--

LOCK TABLES `jiraaction` WRITE;
/*!40000 ALTER TABLE `jiraaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `jiraaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jiradraftworkflows`
--

DROP TABLE IF EXISTS `jiradraftworkflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jiradraftworkflows` (
  `ID` decimal(18,0) NOT NULL,
  `PARENTNAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTOR` longtext collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jiradraftworkflows`
--

LOCK TABLES `jiradraftworkflows` WRITE;
/*!40000 ALTER TABLE `jiradraftworkflows` DISABLE KEYS */;
/*!40000 ALTER TABLE `jiradraftworkflows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jiraeventtype`
--

DROP TABLE IF EXISTS `jiraeventtype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jiraeventtype` (
  `ID` decimal(18,0) NOT NULL,
  `TEMPLATE_ID` decimal(18,0) default NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `event_type` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jiraeventtype`
--

LOCK TABLES `jiraeventtype` WRITE;
/*!40000 ALTER TABLE `jiraeventtype` DISABLE KEYS */;
INSERT INTO `jiraeventtype` VALUES ('1',NULL,'Demande créée','Il s\'agit de l\'événement \'demande créée\'.','jira.system.event.type'),('2',NULL,'Demande mise à jour','Il s\'agit de l\'événement \'demande mise à jour\'.','jira.system.event.type'),('3',NULL,'Demande attribuée','Il s\'agit de l\'événement \'demande attribuée\'.','jira.system.event.type'),('4',NULL,'Demande résolue','Il s\'agit de l\'événement \'demande résolue\'.','jira.system.event.type'),('5',NULL,'Demande fermée','Il s\'agit de l\'événement \'demande fermée\'.','jira.system.event.type'),('6',NULL,'Demande commentée','Il s\'agit de l\'événement \'demande commentée\'.','jira.system.event.type'),('7',NULL,'Demande rouverte','Il s\'agit de l\'événement \'demande rouverte\'.','jira.system.event.type'),('8',NULL,'Demande supprimée','Il s\'agit de l\'événement \'demande supprimée\'.','jira.system.event.type'),('9',NULL,'Demande déplacée','Il s\'agit de l\'événement \'demande déplacée\'.','jira.system.event.type'),('10',NULL,'Travail consigné pour la demande','Il s\'agit de l\'événement \'travail consigné pour la demande\'.','jira.system.event.type'),('11',NULL,'Travail commencé pour la demande','Il s\'agit de l\'événement \'travail commencé pour la demande\'.','jira.system.event.type'),('12',NULL,'Travail arrêté pour la demande','Il s\'agit de l\'événement \'travail arrêté pour la demande\'.','jira.system.event.type'),('13',NULL,'Evénement générique','Il s\'agit de l\'événement \'évènement générique\'.','jira.system.event.type'),('14',NULL,'Commentaire de demande édité','Il s\'agit de l\'événement \'commentaire de demande édité\'.','jira.system.event.type'),('15',NULL,'Mise à jour du journal de travail de la demande','Il s\'agit de l\'événement \'Mise à jour du journal de travail de la demande\'.','jira.system.event.type'),('16',NULL,'Suppression du journal de travail de la demande','Il s\'agit de l\'événement \'suppression du journal de travail de la demande\'.','jira.system.event.type');
/*!40000 ALTER TABLE `jiraeventtype` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jiraissue`
--

DROP TABLE IF EXISTS `jiraissue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jiraissue` (
  `ID` decimal(18,0) NOT NULL,
  `pkey` varchar(255) collate utf8_bin default NULL,
  `PROJECT` decimal(18,0) default NULL,
  `REPORTER` varchar(255) collate utf8_bin default NULL,
  `ASSIGNEE` varchar(255) collate utf8_bin default NULL,
  `issuetype` varchar(255) collate utf8_bin default NULL,
  `SUMMARY` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` longtext collate utf8_bin,
  `ENVIRONMENT` longtext collate utf8_bin,
  `PRIORITY` varchar(255) collate utf8_bin default NULL,
  `RESOLUTION` varchar(255) collate utf8_bin default NULL,
  `issuestatus` varchar(255) collate utf8_bin default NULL,
  `CREATED` datetime default NULL,
  `UPDATED` datetime default NULL,
  `DUEDATE` datetime default NULL,
  `RESOLUTIONDATE` datetime default NULL,
  `VOTES` decimal(18,0) default NULL,
  `WATCHES` decimal(18,0) default NULL,
  `TIMEORIGINALESTIMATE` decimal(18,0) default NULL,
  `TIMEESTIMATE` decimal(18,0) default NULL,
  `TIMESPENT` decimal(18,0) default NULL,
  `WORKFLOW_ID` decimal(18,0) default NULL,
  `SECURITY` decimal(18,0) default NULL,
  `FIXFOR` decimal(18,0) default NULL,
  `COMPONENT` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `issue_key` (`pkey`),
  KEY `issue_proj_status` (`PROJECT`,`issuestatus`),
  KEY `issue_updated` (`UPDATED`),
  KEY `issue_assignee` (`ASSIGNEE`),
  KEY `issue_workflow` (`WORKFLOW_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jiraissue`
--

LOCK TABLES `jiraissue` WRITE;
/*!40000 ALTER TABLE `jiraissue` DISABLE KEYS */;
/*!40000 ALTER TABLE `jiraissue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jiraperms`
--

DROP TABLE IF EXISTS `jiraperms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jiraperms` (
  `ID` decimal(18,0) NOT NULL,
  `permtype` decimal(18,0) default NULL,
  `projectid` decimal(18,0) default NULL,
  `groupname` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jiraperms`
--

LOCK TABLES `jiraperms` WRITE;
/*!40000 ALTER TABLE `jiraperms` DISABLE KEYS */;
/*!40000 ALTER TABLE `jiraperms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `jiraworkflows`
--

DROP TABLE IF EXISTS `jiraworkflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `jiraworkflows` (
  `ID` decimal(18,0) NOT NULL,
  `workflowname` varchar(255) collate utf8_bin default NULL,
  `creatorname` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTOR` longtext collate utf8_bin,
  `ISLOCKED` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `jiraworkflows`
--

LOCK TABLES `jiraworkflows` WRITE;
/*!40000 ALTER TABLE `jiraworkflows` DISABLE KEYS */;
/*!40000 ALTER TABLE `jiraworkflows` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `label` (
  `ID` decimal(18,0) NOT NULL,
  `FIELDID` decimal(18,0) default NULL,
  `ISSUE` decimal(18,0) default NULL,
  `LABEL` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `label_fieldissue` (`ISSUE`,`FIELDID`),
  KEY `label_fieldissuelabel` (`ISSUE`,`FIELDID`,`LABEL`),
  KEY `label_label` (`LABEL`),
  KEY `label_issue` (`ISSUE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `label`
--

LOCK TABLES `label` WRITE;
/*!40000 ALTER TABLE `label` DISABLE KEYS */;
/*!40000 ALTER TABLE `label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `listenerconfig`
--

DROP TABLE IF EXISTS `listenerconfig`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `listenerconfig` (
  `ID` decimal(18,0) NOT NULL,
  `CLAZZ` varchar(255) collate utf8_bin default NULL,
  `listenername` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listenerconfig`
--

LOCK TABLES `listenerconfig` WRITE;
/*!40000 ALTER TABLE `listenerconfig` DISABLE KEYS */;
INSERT INTO `listenerconfig` VALUES ('10000','com.atlassian.jira.event.listeners.mail.MailListener','Mail Listener'),('10001','com.atlassian.jira.event.listeners.history.IssueAssignHistoryListener','Issue Assignment Listener'),('10002','com.atlassian.jira.event.listeners.search.IssueIndexListener','Issue Index Listener');
/*!40000 ALTER TABLE `listenerconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `mailserver`
--

DROP TABLE IF EXISTS `mailserver`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mailserver` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `mailfrom` varchar(255) collate utf8_bin default NULL,
  `PREFIX` varchar(60) collate utf8_bin default NULL,
  `smtp_port` varchar(60) collate utf8_bin default NULL,
  `protocol` varchar(60) collate utf8_bin default NULL,
  `server_type` varchar(60) collate utf8_bin default NULL,
  `SERVERNAME` varchar(255) collate utf8_bin default NULL,
  `JNDILOCATION` varchar(255) collate utf8_bin default NULL,
  `mailusername` varchar(255) collate utf8_bin default NULL,
  `mailpassword` varchar(255) collate utf8_bin default NULL,
  `ISTLSREQUIRED` varchar(60) collate utf8_bin default NULL,
  `TIMEOUT` decimal(18,0) default NULL,
  `socks_port` varchar(60) collate utf8_bin default NULL,
  `socks_host` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `mailserver`
--

LOCK TABLES `mailserver` WRITE;
/*!40000 ALTER TABLE `mailserver` DISABLE KEYS */;
/*!40000 ALTER TABLE `mailserver` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `managedconfigurationitem`
--

DROP TABLE IF EXISTS `managedconfigurationitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `managedconfigurationitem` (
  `ID` decimal(18,0) NOT NULL,
  `ITEM_ID` varchar(255) collate utf8_bin default NULL,
  `ITEM_TYPE` varchar(255) collate utf8_bin default NULL,
  `MANAGED` varchar(10) collate utf8_bin default NULL,
  `ACCESS_LEVEL` varchar(255) collate utf8_bin default NULL,
  `SOURCE` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION_KEY` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `managedconfigitem_id_type_idx` (`ITEM_ID`,`ITEM_TYPE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `managedconfigurationitem`
--

LOCK TABLES `managedconfigurationitem` WRITE;
/*!40000 ALTER TABLE `managedconfigurationitem` DISABLE KEYS */;
/*!40000 ALTER TABLE `managedconfigurationitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `membershipbase`
--

DROP TABLE IF EXISTS `membershipbase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `membershipbase` (
  `ID` decimal(18,0) NOT NULL,
  `USER_NAME` varchar(255) collate utf8_bin default NULL,
  `GROUP_NAME` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `mshipbase_user` (`USER_NAME`),
  KEY `mshipbase_group` (`GROUP_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `membershipbase`
--

LOCK TABLES `membershipbase` WRITE;
/*!40000 ALTER TABLE `membershipbase` DISABLE KEYS */;
/*!40000 ALTER TABLE `membershipbase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `moved_issue_key`
--

DROP TABLE IF EXISTS `moved_issue_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `moved_issue_key` (
  `ID` decimal(18,0) NOT NULL,
  `OLD_ISSUE_KEY` varchar(255) collate utf8_bin default NULL,
  `ISSUE_ID` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `idx_old_issue_key` (`OLD_ISSUE_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `moved_issue_key`
--

LOCK TABLES `moved_issue_key` WRITE;
/*!40000 ALTER TABLE `moved_issue_key` DISABLE KEYS */;
/*!40000 ALTER TABLE `moved_issue_key` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nodeassociation`
--

DROP TABLE IF EXISTS `nodeassociation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nodeassociation` (
  `SOURCE_NODE_ID` decimal(18,0) NOT NULL,
  `SOURCE_NODE_ENTITY` varchar(60) collate utf8_bin NOT NULL,
  `SINK_NODE_ID` decimal(18,0) NOT NULL,
  `SINK_NODE_ENTITY` varchar(60) collate utf8_bin NOT NULL,
  `ASSOCIATION_TYPE` varchar(60) collate utf8_bin NOT NULL,
  `SEQUENCE` decimal(9,0) default NULL,
  PRIMARY KEY  (`SOURCE_NODE_ID`,`SOURCE_NODE_ENTITY`,`SINK_NODE_ID`,`SINK_NODE_ENTITY`,`ASSOCIATION_TYPE`),
  KEY `node_source` (`SOURCE_NODE_ID`,`SOURCE_NODE_ENTITY`),
  KEY `node_sink` (`SINK_NODE_ID`,`SINK_NODE_ENTITY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nodeassociation`
--

LOCK TABLES `nodeassociation` WRITE;
/*!40000 ALTER TABLE `nodeassociation` DISABLE KEYS */;
/*!40000 ALTER TABLE `nodeassociation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `nodeindexcounter`
--

DROP TABLE IF EXISTS `nodeindexcounter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `nodeindexcounter` (
  `ID` decimal(18,0) NOT NULL,
  `NODE_ID` varchar(60) collate utf8_bin default NULL,
  `INDEX_OPERATION_ID` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `node_id_idx` (`NODE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `nodeindexcounter`
--

LOCK TABLES `nodeindexcounter` WRITE;
/*!40000 ALTER TABLE `nodeindexcounter` DISABLE KEYS */;
/*!40000 ALTER TABLE `nodeindexcounter` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notification`
--

DROP TABLE IF EXISTS `notification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notification` (
  `ID` decimal(18,0) NOT NULL,
  `SCHEME` decimal(18,0) default NULL,
  `EVENT` varchar(60) collate utf8_bin default NULL,
  `EVENT_TYPE_ID` decimal(18,0) default NULL,
  `TEMPLATE_ID` decimal(18,0) default NULL,
  `notif_type` varchar(60) collate utf8_bin default NULL,
  `notif_parameter` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `ntfctn_scheme` (`SCHEME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notification`
--

LOCK TABLES `notification` WRITE;
/*!40000 ALTER TABLE `notification` DISABLE KEYS */;
INSERT INTO `notification` VALUES ('10000','10000',NULL,'1',NULL,'Current_Assignee',NULL),('10001','10000',NULL,'1',NULL,'Current_Reporter',NULL),('10002','10000',NULL,'1',NULL,'All_Watchers',NULL),('10003','10000',NULL,'2',NULL,'Current_Assignee',NULL),('10004','10000',NULL,'2',NULL,'Current_Reporter',NULL),('10005','10000',NULL,'2',NULL,'All_Watchers',NULL),('10006','10000',NULL,'3',NULL,'Current_Assignee',NULL),('10007','10000',NULL,'3',NULL,'Current_Reporter',NULL),('10008','10000',NULL,'3',NULL,'All_Watchers',NULL),('10009','10000',NULL,'4',NULL,'Current_Assignee',NULL),('10010','10000',NULL,'4',NULL,'Current_Reporter',NULL),('10011','10000',NULL,'4',NULL,'All_Watchers',NULL),('10012','10000',NULL,'5',NULL,'Current_Assignee',NULL),('10013','10000',NULL,'5',NULL,'Current_Reporter',NULL),('10014','10000',NULL,'5',NULL,'All_Watchers',NULL),('10015','10000',NULL,'6',NULL,'Current_Assignee',NULL),('10016','10000',NULL,'6',NULL,'Current_Reporter',NULL),('10017','10000',NULL,'6',NULL,'All_Watchers',NULL),('10018','10000',NULL,'7',NULL,'Current_Assignee',NULL),('10019','10000',NULL,'7',NULL,'Current_Reporter',NULL),('10020','10000',NULL,'7',NULL,'All_Watchers',NULL),('10021','10000',NULL,'8',NULL,'Current_Assignee',NULL),('10022','10000',NULL,'8',NULL,'Current_Reporter',NULL),('10023','10000',NULL,'8',NULL,'All_Watchers',NULL),('10024','10000',NULL,'9',NULL,'Current_Assignee',NULL),('10025','10000',NULL,'9',NULL,'Current_Reporter',NULL),('10026','10000',NULL,'9',NULL,'All_Watchers',NULL),('10027','10000',NULL,'10',NULL,'Current_Assignee',NULL),('10028','10000',NULL,'10',NULL,'Current_Reporter',NULL),('10029','10000',NULL,'10',NULL,'All_Watchers',NULL),('10030','10000',NULL,'11',NULL,'Current_Assignee',NULL),('10031','10000',NULL,'11',NULL,'Current_Reporter',NULL),('10032','10000',NULL,'11',NULL,'All_Watchers',NULL),('10033','10000',NULL,'12',NULL,'Current_Assignee',NULL),('10034','10000',NULL,'12',NULL,'Current_Reporter',NULL),('10035','10000',NULL,'12',NULL,'All_Watchers',NULL),('10036','10000',NULL,'13',NULL,'Current_Assignee',NULL),('10037','10000',NULL,'13',NULL,'Current_Reporter',NULL),('10038','10000',NULL,'13',NULL,'All_Watchers',NULL),('10100','10000',NULL,'14',NULL,'Current_Assignee',NULL),('10101','10000',NULL,'14',NULL,'Current_Reporter',NULL),('10102','10000',NULL,'14',NULL,'All_Watchers',NULL),('10103','10000',NULL,'15',NULL,'Current_Assignee',NULL),('10104','10000',NULL,'15',NULL,'Current_Reporter',NULL),('10105','10000',NULL,'15',NULL,'All_Watchers',NULL),('10106','10000',NULL,'16',NULL,'Current_Assignee',NULL),('10107','10000',NULL,'16',NULL,'Current_Reporter',NULL),('10108','10000',NULL,'16',NULL,'All_Watchers',NULL);
/*!40000 ALTER TABLE `notification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificationinstance`
--

DROP TABLE IF EXISTS `notificationinstance`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notificationinstance` (
  `ID` decimal(18,0) NOT NULL,
  `notificationtype` varchar(60) collate utf8_bin default NULL,
  `SOURCE` decimal(18,0) default NULL,
  `emailaddress` varchar(255) collate utf8_bin default NULL,
  `MESSAGEID` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `notif_source` (`SOURCE`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificationinstance`
--

LOCK TABLES `notificationinstance` WRITE;
/*!40000 ALTER TABLE `notificationinstance` DISABLE KEYS */;
/*!40000 ALTER TABLE `notificationinstance` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificationscheme`
--

DROP TABLE IF EXISTS `notificationscheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notificationscheme` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificationscheme`
--

LOCK TABLES `notificationscheme` WRITE;
/*!40000 ALTER TABLE `notificationscheme` DISABLE KEYS */;
INSERT INTO `notificationscheme` VALUES ('10000','Système de notification par défaut',NULL);
/*!40000 ALTER TABLE `notificationscheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauthconsumer`
--

DROP TABLE IF EXISTS `oauthconsumer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauthconsumer` (
  `ID` decimal(18,0) NOT NULL,
  `CREATED` datetime default NULL,
  `consumername` varchar(255) collate utf8_bin default NULL,
  `CONSUMER_KEY` varchar(255) collate utf8_bin default NULL,
  `consumerservice` varchar(255) collate utf8_bin default NULL,
  `PUBLIC_KEY` text collate utf8_bin,
  `PRIVATE_KEY` text collate utf8_bin,
  `DESCRIPTION` text collate utf8_bin,
  `CALLBACK` text collate utf8_bin,
  `SIGNATURE_METHOD` varchar(60) collate utf8_bin default NULL,
  `SHARED_SECRET` text collate utf8_bin,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `oauth_consumer_index` (`CONSUMER_KEY`),
  UNIQUE KEY `oauth_consumer_service_index` (`consumerservice`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauthconsumer`
--

LOCK TABLES `oauthconsumer` WRITE;
/*!40000 ALTER TABLE `oauthconsumer` DISABLE KEYS */;
INSERT INTO `oauthconsumer` VALUES ('10000','2013-08-27 16:49:53','JIRA','jira:13978630','__HOST_SERVICE__','MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCGhONpSQm8RC9JkRtMHUYqft9c9Kmtn7x9FXLW7b4AYl7mVYwwKfAJslWe1KYobZvaaBHfb/BuQdTwgLHc4aZT9Y7lwJHB3/j+GGKOUv2fy4Km9u8aK0GzeCvzsrYvQM8F1he1fjORVY8F8aK9D5PAztoERvj+yvrXMUFJP6ZMbwIDAQAB','MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAIaE42lJCbxEL0mRG0wdRip+31z0qa2fvH0VctbtvgBiXuZVjDAp8AmyVZ7Upihtm9poEd9v8G5B1PCAsdzhplP1juXAkcHf+P4YYo5S/Z/Lgqb27xorQbN4K/Oyti9AzwXWF7V+M5FVjwXxor0Pk8DO2gRG+P7K+tcxQUk/pkxvAgMBAAECgYA0LixA1RIvs4tcUCt0lQ54wYpHdqizLvlg1AceFE7Vu1LwqlKdBEeWm1TXLcneeNYyHJiCQdvUCTleqa1U7zsjWmM9SP1wjOiU4ubRhzpZepvJjG0oqVLnEPNfH7iNeHsq+jfLFcWRR20SxOYB5AGR234LsncJI/Acd2VvekdNIQJBAL5oDKKJJFUFA3aK+ncDO3LDXc1plYCGJTt5Q0sVx5PonSydoThqHSw6dXqyW530MD6EIfApIzflNVbRyLAvYBcCQQC03CHClzD8ByeHj/SNFz7bPey8x0O13906USuXI+MkvzjGc1wECxRdjMXvyqFZGSGfLf6w7mTb/7xnHVzBGhVpAkEArQYcRS7ehyvRI83TIIcdZIJIITuLEYnVF6BwGDEodS3ydfKf6IX1EJXm36JoQrb9iDHGLVkUmdx9hfGIryG97QJAdNECVUeGciLE24CVulPbW4yhzkT+bQjNQY+QEi0x11hTeYqlUk89tjbo2jsNABmFZ+UzbmBscNS7gfarJGaOkQJBALZzMcL7I6zkgERzfSqOhLW5PJvVNtcdYCJfLFtP2+7gyRIcu9TF2hcL+RFyVNZEocdPgZUz2nKxiElQtxA/m/U=','Atlassian JIRA at null',NULL,'RSA_SHA1',NULL);
/*!40000 ALTER TABLE `oauthconsumer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauthconsumertoken`
--

DROP TABLE IF EXISTS `oauthconsumertoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauthconsumertoken` (
  `ID` decimal(18,0) NOT NULL,
  `CREATED` datetime default NULL,
  `TOKEN_KEY` varchar(255) collate utf8_bin default NULL,
  `TOKEN` varchar(255) collate utf8_bin default NULL,
  `TOKEN_SECRET` varchar(255) collate utf8_bin default NULL,
  `TOKEN_TYPE` varchar(60) collate utf8_bin default NULL,
  `CONSUMER_KEY` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `oauth_consumer_token_key_index` (`TOKEN_KEY`),
  KEY `oauth_consumer_token_index` (`TOKEN`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauthconsumertoken`
--

LOCK TABLES `oauthconsumertoken` WRITE;
/*!40000 ALTER TABLE `oauthconsumertoken` DISABLE KEYS */;
/*!40000 ALTER TABLE `oauthconsumertoken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauthspconsumer`
--

DROP TABLE IF EXISTS `oauthspconsumer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauthspconsumer` (
  `ID` decimal(18,0) NOT NULL,
  `CREATED` datetime default NULL,
  `CONSUMER_KEY` varchar(255) collate utf8_bin default NULL,
  `consumername` varchar(255) collate utf8_bin default NULL,
  `PUBLIC_KEY` text collate utf8_bin,
  `DESCRIPTION` text collate utf8_bin,
  `CALLBACK` text collate utf8_bin,
  `TWO_L_O_ALLOWED` varchar(60) collate utf8_bin default NULL,
  `EXECUTING_TWO_L_O_USER` varchar(255) collate utf8_bin default NULL,
  `TWO_L_O_IMPERSONATION_ALLOWED` varchar(60) collate utf8_bin default NULL,
  `THREE_L_O_ALLOWED` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `oauth_sp_consumer_index` (`CONSUMER_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauthspconsumer`
--

LOCK TABLES `oauthspconsumer` WRITE;
/*!40000 ALTER TABLE `oauthspconsumer` DISABLE KEYS */;
/*!40000 ALTER TABLE `oauthspconsumer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `oauthsptoken`
--

DROP TABLE IF EXISTS `oauthsptoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `oauthsptoken` (
  `ID` decimal(18,0) NOT NULL,
  `CREATED` datetime default NULL,
  `TOKEN` varchar(255) collate utf8_bin default NULL,
  `TOKEN_SECRET` varchar(255) collate utf8_bin default NULL,
  `TOKEN_TYPE` varchar(60) collate utf8_bin default NULL,
  `CONSUMER_KEY` varchar(255) collate utf8_bin default NULL,
  `USERNAME` varchar(255) collate utf8_bin default NULL,
  `TTL` decimal(18,0) default NULL,
  `spauth` varchar(60) collate utf8_bin default NULL,
  `CALLBACK` text collate utf8_bin,
  `spverifier` varchar(255) collate utf8_bin default NULL,
  `spversion` varchar(60) collate utf8_bin default NULL,
  `SESSION_HANDLE` varchar(255) collate utf8_bin default NULL,
  `SESSION_CREATION_TIME` datetime default NULL,
  `SESSION_LAST_RENEWAL_TIME` datetime default NULL,
  `SESSION_TIME_TO_LIVE` datetime default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `oauth_sp_token_index` (`TOKEN`),
  KEY `oauth_sp_consumer_key_index` (`CONSUMER_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `oauthsptoken`
--

LOCK TABLES `oauthsptoken` WRITE;
/*!40000 ALTER TABLE `oauthsptoken` DISABLE KEYS */;
/*!40000 ALTER TABLE `oauthsptoken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `optionconfiguration`
--

DROP TABLE IF EXISTS `optionconfiguration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `optionconfiguration` (
  `ID` decimal(18,0) NOT NULL,
  `FIELDID` varchar(60) collate utf8_bin default NULL,
  `OPTIONID` varchar(60) collate utf8_bin default NULL,
  `FIELDCONFIG` decimal(18,0) default NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `fieldid_optionid` (`FIELDID`,`OPTIONID`),
  KEY `fieldid_fieldconf` (`FIELDID`,`FIELDCONFIG`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `optionconfiguration`
--

LOCK TABLES `optionconfiguration` WRITE;
/*!40000 ALTER TABLE `optionconfiguration` DISABLE KEYS */;
INSERT INTO `optionconfiguration` VALUES ('10100','issuetype','1','10000','0'),('10101','issuetype','2','10000','1'),('10102','issuetype','3','10000','2'),('10103','issuetype','4','10000','3'),('10104','issuetype','5','10000','4');
/*!40000 ALTER TABLE `optionconfiguration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissionscheme`
--

DROP TABLE IF EXISTS `permissionscheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `permissionscheme` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissionscheme`
--

LOCK TABLES `permissionscheme` WRITE;
/*!40000 ALTER TABLE `permissionscheme` DISABLE KEYS */;
INSERT INTO `permissionscheme` VALUES ('0','Système d\'autorisation par défaut','Il s\'agit du système d\'autorisation par défaut. Ce système sera attribué à tout nouveau projet créé.');
/*!40000 ALTER TABLE `permissionscheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pluginstate`
--

DROP TABLE IF EXISTS `pluginstate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pluginstate` (
  `pluginkey` varchar(255) collate utf8_bin NOT NULL,
  `pluginenabled` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`pluginkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pluginstate`
--

LOCK TABLES `pluginstate` WRITE;
/*!40000 ALTER TABLE `pluginstate` DISABLE KEYS */;
/*!40000 ALTER TABLE `pluginstate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pluginversion`
--

DROP TABLE IF EXISTS `pluginversion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pluginversion` (
  `ID` decimal(18,0) NOT NULL,
  `pluginname` varchar(255) collate utf8_bin default NULL,
  `pluginkey` varchar(255) collate utf8_bin default NULL,
  `pluginversion` varchar(255) collate utf8_bin default NULL,
  `CREATED` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pluginversion`
--

LOCK TABLES `pluginversion` WRITE;
/*!40000 ALTER TABLE `pluginversion` DISABLE KEYS */;
INSERT INTO `pluginversion` VALUES ('10000','ActiveObjects Plugin - OSGi Bundle','com.atlassian.activeobjects.activeobjects-plugin','0.19.16.1','2013-08-27 16:49:55'),('10001','JIRA Active Objects SPI implementation','com.atlassian.activeobjects.jira.spi','0.19.16.1','2013-08-27 16:49:55'),('10002','Atlassian - Administration - Quick Search - JIRA','com.atlassian.administration.atlassian-admin-quicksearch-jira','1.3.1','2013-08-27 16:49:55'),('10003','Applinks Product Plugin','com.atlassian.applinks.applinks-plugin','3.11.0-m8','2013-08-27 16:49:55'),('10004','Atlassian UI Plugin','com.atlassian.auiplugin','5.0.1','2013-08-27 16:49:55'),('10005','ICU4J','com.atlassian.bundles.icu4j-3.8.0.1','3.8.0.1','2013-08-27 16:49:55'),('10006','JSON Library','com.atlassian.bundles.json-20070829.0.0.1','20070829.0.0.1','2013-08-27 16:49:55'),('10007','Neko HTML','com.atlassian.bundles.nekohtml-1.9.12.1','1.9.12.1','2013-08-27 16:49:55'),('10008','Atlassian Embedded Crowd - Administration Plugin','com.atlassian.crowd.embedded.admin','1.6','2013-08-27 16:49:55'),('10009','Gadget Dashboard Plugin','com.atlassian.gadgets.dashboard','3.2.0-m26','2013-08-27 16:49:55'),('10010','Gadget Directory Plugin','com.atlassian.gadgets.directory','3.2.0-m26','2013-08-27 16:49:55'),('10011','Embedded Gadgets Plugin','com.atlassian.gadgets.embedded','3.2.0-m26','2013-08-27 16:49:55'),('10012','Atlassian Gadgets OAuth Service Provider Plugin','com.atlassian.gadgets.oauth.serviceprovider','3.2.0-m26','2013-08-27 16:49:55'),('10013','Opensocial Plugin','com.atlassian.gadgets.opensocial','3.2.0-m26','2013-08-27 16:49:55'),('10014','Gadget Spec Publisher Plugin','com.atlassian.gadgets.publisher','3.2.0-m26','2013-08-27 16:49:55'),('10015','Atlassian HealthCheck Common Module','com.atlassian.healthcheck.atlassian-healthcheck','1.0.0','2013-08-27 16:49:55'),('10016','JIRA Core Project Templates Plugin','com.atlassian.jira-core-project-templates','2.18','2013-08-27 16:49:55'),('10017','JIRA Issue Collector Plugin','com.atlassian.jira.collector.plugin.jira-issue-collector-plugin','1.4.0-m3','2013-08-27 16:49:55'),('10018','RPC JIRA Plugin','com.atlassian.jira.ext.rpc','6.0-m08','2013-08-27 16:49:55'),('10019','JIRA iCalendar Plugin','com.atlassian.jira.extra.jira-ical-feed','1.0.16','2013-08-27 16:49:55'),('10020','Atlassian JIRA - Plugins - Gadgets Plugin','com.atlassian.jira.gadgets','6.0.7','2013-08-27 16:49:55'),('10021','Atlassian JIRA - Plugins - Admin Summary Component','com.atlassian.jira.jira-admin-summary-plugin','6.0.7','2013-08-27 16:49:55'),('10022','Atlassian JIRA - Plugins - Application Properties','com.atlassian.jira.jira-application-properties-plugin','6.0.7','2013-08-27 16:49:55'),('10023','JIRA Base URL Plugin','com.atlassian.jira.jira-baseurl-plugin','1.9','2013-08-27 16:49:55'),('10024','JIRA Feedback Plugin','com.atlassian.jira.jira-feedback-plugin','1.9','2013-08-27 16:49:55'),('10025','Atlassian JIRA - Plugins - Header Plugin','com.atlassian.jira.jira-header-plugin','6.0.7','2013-08-27 16:49:55'),('10026','Atlassian JIRA - Plugins - Invite User','com.atlassian.jira.jira-invite-user-plugin','1.10','2013-08-27 16:49:55'),('10027','Atlassian JIRA - Plugins - Common AppLinks Based Issue Link Plugin','com.atlassian.jira.jira-issue-link-applinks-common-plugin','6.0.7','2013-08-27 16:49:55'),('10028','Atlassian JIRA - Plugins - Confluence Link','com.atlassian.jira.jira-issue-link-confluence-plugin','6.0.7','2013-08-27 16:49:55'),('10029','Atlassian JIRA - Plugins - Remote JIRA Link','com.atlassian.jira.jira-issue-link-remote-jira-plugin','6.0.7','2013-08-27 16:49:55'),('10030','Atlassian JIRA - Plugins - Issue Web Link','com.atlassian.jira.jira-issue-link-web-plugin','6.0.7','2013-08-27 16:49:55'),('10031','Atlassian JIRA - Plugins - Issue Navigation','com.atlassian.jira.jira-issue-nav-plugin','6.0.7','2013-08-27 16:49:55'),('10032','English (United Kingdom) Language Pack','com.atlassian.jira.jira-languages.en_UK','6.0.7','2013-08-27 16:49:55'),('10033','English (United States) Language Pack','com.atlassian.jira.jira-languages.en_US','6.0.7','2013-08-27 16:49:55'),('10034','Atlassian JIRA - Plugins - Mail Plugin','com.atlassian.jira.jira-mail-plugin','6.0.7','2013-08-27 16:49:55'),('10035','JIRA Monitoring Plugin','com.atlassian.jira.jira-monitoring-plugin','05.5.4','2013-08-27 16:49:55'),('10036','Atlassian JIRA - Plugins - My JIRA Home','com.atlassian.jira.jira-my-home-plugin','6.0.7','2013-08-27 16:49:55'),('10037','Atlassian JIRA - Plugins - Project Config Plugin','com.atlassian.jira.jira-project-config-plugin','6.0.7','2013-08-27 16:49:55'),('10038','Atlassian JIRA - Plugins - Quick Edit Plugin','com.atlassian.jira.jira-quick-edit-plugin','1.0.80','2013-08-27 16:49:56'),('10039','JIRA REST Java Client - Plugin','com.atlassian.jira.jira-rest-java-client-plugin','2.0.0-m16','2013-08-27 16:49:56'),('10040','Atlassian JIRA - Plugins - Share Content Component','com.atlassian.jira.jira-share-plugin','6.0.7','2013-08-27 16:49:56'),('10041','Atlassian JIRA - Plugins - Closure Template Renderer','com.atlassian.jira.jira-soy-plugin','6.0.7','2013-08-27 16:49:56'),('10042','JIRA Time Zone Detection plugin','com.atlassian.jira.jira-tzdetect-plugin','1.8','2013-08-27 16:49:56'),('10043','Atlassian JIRA - Plugins - User Profile Plugin','com.atlassian.jira.jira-user-profile-plugin','1.1.1','2013-08-27 16:49:56'),('10044','Atlassian JIRA - Plugins - View Issue Panels','com.atlassian.jira.jira-view-issue-plugin','6.0.7','2013-08-27 16:49:56'),('10045','Atlassian JIRA - Plugins - Look And Feel Logo Upload Plugin','com.atlassian.jira.lookandfeel','6.0.7','2013-08-27 16:49:56'),('10046','JIRA Mobile','com.atlassian.jira.mobile','1.0','2013-08-27 16:49:56'),('10047','Atlassian JIRA - Plugins - OAuth Consumer SPI','com.atlassian.jira.oauth.consumer','6.0.7','2013-08-27 16:49:56'),('10048','Atlassian JIRA - Plugins - OAuth Service Provider SPI','com.atlassian.jira.oauth.serviceprovider','6.0.7','2013-08-27 16:49:56'),('10049','JIRA Bamboo Plugin','com.atlassian.jira.plugin.ext.bamboo','6.1.7','2013-08-27 16:49:56'),('10050','Custom Field Types & Searchers','com.atlassian.jira.plugin.system.customfieldtypes','1.0','2013-08-27 16:49:56'),('10051','Issue Operations Plugin','com.atlassian.jira.plugin.system.issueoperations','1.0','2013-08-27 16:49:56'),('10052','Issue Tab Panels Plugin','com.atlassian.jira.plugin.system.issuetabpanels','1.0','2013-08-27 16:49:56'),('10053','Renderer Plugin','com.atlassian.jira.plugin.system.jirarenderers','1.0','2013-08-27 16:49:56'),('10054','Project Panels Plugin','com.atlassian.jira.plugin.system.project','1.0','2013-08-27 16:49:56'),('10055','Project Role Actors Plugin','com.atlassian.jira.plugin.system.projectroleactors','1.0','2013-08-27 16:49:56'),('10056','Wiki Renderer Macros Plugin','com.atlassian.jira.plugin.system.renderers.wiki.macros','1.0','2013-08-27 16:49:56'),('10057','Reports Plugin','com.atlassian.jira.plugin.system.reports','1.0','2013-08-27 16:49:56'),('10058','Workflow Plugin','com.atlassian.jira.plugin.system.workflow','1.0','2013-08-27 16:49:56'),('10059','Content Link Resolvers Plugin','com.atlassian.jira.plugin.wiki.contentlinkresolvers','1.0','2013-08-27 16:49:56'),('10060','Renderer Component Factories Plugin','com.atlassian.jira.plugin.wiki.renderercomponentfactories','1.0','2013-08-27 16:49:56'),('10061','JIRA Agile Marketing Plugin','com.atlassian.jira.plugins.greenhopper-marketing-plugin','1.0.6','2013-08-27 16:49:56'),('10062','Atlassian JIRA - Admin Helper Plugin','com.atlassian.jira.plugins.jira-admin-helper-plugin','1.10.1','2013-08-27 16:49:56'),('10063','JIRA DVCS Connector Plugin','com.atlassian.jira.plugins.jira-bitbucket-connector-plugin','1.4.0.1','2013-08-27 16:49:56'),('10064','JIRA Importers Plugin (JIM)','com.atlassian.jira.plugins.jira-importers-plugin','6.0.18','2013-08-27 16:49:56'),('10065','Redmine Importers Plugin for JIM','com.atlassian.jira.plugins.jira-importers-redmine-plugin','2.0.2','2013-08-27 16:49:56'),('10066','Workflow Designer Plugin','com.atlassian.jira.plugins.jira-workflow-designer','3.0.0-m25','2013-08-27 16:49:56'),('10067','JIRA WebHooks Plugin','com.atlassian.jira.plugins.webhooks.jira-webhooks-plugin','1.0.13','2013-08-27 16:49:56'),('10068','JIRA Workflow Sharing Plugin','com.atlassian.jira.plugins.workflow.sharing.jira-workflow-sharing-plugin','1.1.7','2013-08-27 16:49:56'),('10069','Project Templates Plugin','com.atlassian.jira.project-templates-plugin','2.18','2013-08-27 16:49:56'),('10070','Atlassian JIRA - Plugins - REST Plugin','com.atlassian.jira.rest','6.0.7','2013-08-27 16:49:56'),('10071','JIRA JSON-RPC Plugin','com.atlassian.jira.rpc.jira-json-rpc-plugin','1.0.4','2013-08-27 16:49:56'),('10072','JIRA Welcome Plugin','com.atlassian.jira.welcome.jira-welcome-plugin','1.1.43','2013-08-27 16:49:56'),('10073','FishEye Plugin','com.atlassian.jirafisheyeplugin','6.0.2','2013-08-27 16:49:56'),('10074','Atlassian Bot Session Killer','com.atlassian.labs.atlassian-bot-killer','1.7.5','2013-08-27 16:49:56'),('10075','Confluence XML-RPC Java Client - Plugin','com.atlassian.labs.confluence-xmlrpc-java-client-plugin','0.9.8','2013-08-27 16:49:56'),('10076','HipChat','com.atlassian.labs.hipchat.hipchat-for-jira-plugin','1.1.14','2013-08-27 16:49:56'),('10077','Atlassian Remotable Plugins - Plugin','com.atlassian.labs.remoteapps-plugin','0.8.1068','2013-08-27 16:49:57'),('10078','Workbox - Common Plugin','com.atlassian.mywork.mywork-common-plugin','1.1.27','2013-08-27 16:49:57'),('10079','Workbox - JIRA Provider Plugin','com.atlassian.mywork.mywork-jira-provider-plugin','1.1.27','2013-08-27 16:49:57'),('10080','Atlassian OAuth Admin Plugin','com.atlassian.oauth.admin','1.8.0','2013-08-27 16:49:57'),('10081','Atlassian OAuth Consumer SPI','com.atlassian.oauth.atlassian-oauth-consumer-spi-1.8.0','1.8.0','2013-08-27 16:49:57'),('10082','Atlassian OAuth Service Provider SPI','com.atlassian.oauth.atlassian-oauth-service-provider-spi-1.8.0','1.8.0','2013-08-27 16:49:57'),('10083','Atlassian OAuth Consumer Plugin','com.atlassian.oauth.consumer','1.8.0','2013-08-27 16:49:57'),('10084','Atlassian OAuth Service Provider Plugin','com.atlassian.oauth.serviceprovider','1.8.0','2013-08-27 16:49:57'),('10085','Atlassian Navigation Links Plugin','com.atlassian.plugins.atlassian-nav-links-plugin','3.2.2.1','2013-08-27 16:49:57'),('10086','jira-help-tips','com.atlassian.plugins.helptips.jira-help-tips','0.31','2013-08-27 16:49:57'),('10087','Atlassian Remotable Plugins API - Public API','com.atlassian.plugins.remotable-plugins-api-0.8.2','0.8.2','2013-08-27 16:49:57'),('10088','Atlassian Remotable Plugins - Sisu Extender','com.atlassian.plugins.remotable-plugins-sisu-extender-0.8.1068','0.8.1068','2013-08-27 16:49:57'),('10089','Atlassian Remotable Plugins API - Public SPI','com.atlassian.plugins.remotable-plugins-spi-0.8.2','0.8.2','2013-08-27 16:49:57'),('10090','Atlassian REST - Module Types','com.atlassian.plugins.rest.atlassian-rest-module','2.8.0-m6','2013-08-27 16:49:57'),('10091','Atlassian Pretty URLs Plugin','com.atlassian.prettyurls.atlassian-pretty-urls-plugin','1.8','2013-08-27 16:49:57'),('10092','Atlassian JIRA - Plugins - SAL Plugin','com.atlassian.sal.jira','6.0.7','2013-08-27 16:49:57'),('10093','Atlassian Soy Templates - Plugin','com.atlassian.soy.soy-template-plugin','2.1.0','2013-08-27 16:49:57'),('10094','Streams Plugin','com.atlassian.streams','5.3.0-m7','2013-08-27 16:49:57'),('10095','Streams Inline Actions Plugin','com.atlassian.streams.actions','5.3.0-m7','2013-08-27 16:49:57'),('10096','Streams Core Plugin','com.atlassian.streams.core','5.3.0-m7','2013-08-27 16:49:57'),('10097','JIRA Streams Inline Actions Plugin','com.atlassian.streams.jira.inlineactions','5.3.0-m7','2013-08-27 16:49:57'),('10098','Streams API','com.atlassian.streams.streams-api-5.3.0.m7','5.3.0.m7','2013-08-27 16:49:57'),('10099','JIRA Activity Stream Plugin','com.atlassian.streams.streams-jira-plugin','5.3.0-m7','2013-08-27 16:49:57'),('10100','Streams SPI','com.atlassian.streams.streams-spi-5.3.0.m7','5.3.0.m7','2013-08-27 16:49:57'),('10101','Streams Third Party Provider Plugin','com.atlassian.streams.streams-thirdparty-plugin','5.3.0-m7','2013-08-27 16:49:57'),('10102','Support Tools Plugin','com.atlassian.support.stp','3.5.2','2013-08-27 16:49:57'),('10103','Atlassian Template Renderer API','com.atlassian.templaterenderer.api','1.4.4-m1','2013-08-27 16:49:57'),('10104','Atlassian Template Renderer Velocity 1.6 Plugin','com.atlassian.templaterenderer.atlassian-template-renderer-velocity1.6-plugin','1.4.4-m1','2013-08-27 16:49:57'),('10105','Atlassian Universal Plugin Manager Plugin','com.atlassian.upm.atlassian-universal-plugin-manager-plugin','2.10.1','2013-08-27 16:49:57'),('10106','Atlassian WebHooks Plugin','com.atlassian.webhooks.atlassian-webhooks-plugin','0.14.1','2013-08-27 16:49:57'),('10107','JSON.simple','com.googlecode.json-simple-1.1.1','1.1.1','2013-08-27 16:49:57'),('10108','ROME: RSS/Atom syndication and publishing tools','com.springsource.com.sun.syndication-0.9.0','0.9.0','2013-08-27 16:49:57'),('10109','JDOM DOM Processor','com.springsource.org.jdom-1.0.0','1.0.0','2013-08-27 16:49:57'),('10110','Crowd REST API','crowd-rest-application-management','1.0','2013-08-27 16:49:57'),('10111','Crowd REST API','crowd-rest-plugin','1.0','2013-08-27 16:49:57'),('10112','Crowd System Password Encoders','crowd.system.passwordencoders','1.0','2013-08-27 16:49:57'),('10113','JIRA Footer','jira.footer','1.0','2013-08-27 16:49:57'),('10114','Issue Views Plugin','jira.issueviews','1.0','2013-08-27 16:49:57'),('10115','JQL Functions','jira.jql.function','1.0','2013-08-27 16:49:57'),('10116','Keyboard Shortcuts Plugin','jira.keyboard.shortcuts','1.0','2013-08-27 16:49:57'),('10117','Top Navigation Bar','jira.top.navigation.bar','1.0','2013-08-27 16:49:57'),('10118','JIRA Usage Hints','jira.usage.hints','1.0','2013-08-27 16:49:57'),('10119','User Format','jira.user.format','1.0','2013-08-27 16:49:57'),('10120','User Profile Panels','jira.user.profile.panels','1.0','2013-08-27 16:49:57'),('10121','Admin Menu Sections','jira.webfragments.admin','1.0','2013-08-27 16:49:57'),('10122','Browse Project Operations Sections','jira.webfragments.browse.project.links','1.0','2013-08-27 16:49:57'),('10123','Preset Filters Sections','jira.webfragments.preset.filters','1.0','2013-08-27 16:49:57'),('10124','User Navigation Bar Sections','jira.webfragments.user.navigation.bar','1.0','2013-08-27 16:49:57'),('10125','User Profile Links','jira.webfragments.user.profile.links','1.0','2013-08-27 16:49:57'),('10126','View Project Operations Sections','jira.webfragments.view.project.operations','1.0','2013-08-27 16:49:57'),('10127','Web Resources Plugin','jira.webresources','1.0','2013-08-27 16:49:57'),('10128','Joda-Time','joda-time-1.6','1.6','2013-08-27 16:49:57'),('10129','Apache HttpClient OSGi bundle','org.apache.httpcomponents.httpclient-4.0','4.0','2013-08-27 16:49:57'),('10130','Apache HttpCore OSGi bundle','org.apache.httpcomponents.httpcore-4.0','4.0','2013-08-27 16:49:57'),('10131','Apache ServiceMix :: Bundles :: javax.inject','org.apache.servicemix.bundles.javax-inject-1.0.0.1','1.0.0.1','2013-08-27 16:49:57'),('10132','jettison','org.codehaus.jettison.jettison-1.1','1.1','2013-08-27 16:49:57'),('10133','Sisu-Inject','org.eclipse.sisu.inject-0.0.0.atlassian6','0.0.0.atlassian6','2013-08-27 16:49:57'),('10134','ASM','org.objectweb.asm-3.3.1.v201105211655','3.3.1.v201105211655','2013-08-27 16:49:57'),('10135','sisu-guice','org.sonatype.sisu.guice-3.1.3','3.1.3','2013-08-27 16:49:57'),('10136','ROME, RSS and atOM utilitiEs for Java','rome.rome-1.0','1.0','2013-08-27 16:49:57'),('10137','JIRA German (Germany) Language Pack','tac.jira.languages.de_DE','6.0.6-v2r6750-2013-08-12','2013-08-27 16:49:57'),('10138','JIRA Spanish (Spain) Language Pack','tac.jira.languages.es_ES','6.0.6-v2r16837-2013-08-12','2013-08-27 16:49:57'),('10139','JIRA French (France) Language Pack','tac.jira.languages.fr_FR','6.0.6-v2r2893-2013-08-06','2013-08-27 16:49:57'),('10140','JIRA Japanese (Japan) Language Pack','tac.jira.languages.ja_JP','6.0.6-v2r18668-2013-08-06','2013-08-27 16:49:57'),('10400','Remotable Plugins I18n plugin','remotable.plugins.i18n','1','2013-09-11 08:50:07');
/*!40000 ALTER TABLE `pluginversion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portalpage`
--

DROP TABLE IF EXISTS `portalpage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portalpage` (
  `ID` decimal(18,0) NOT NULL,
  `USERNAME` varchar(255) collate utf8_bin default NULL,
  `PAGENAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` varchar(255) collate utf8_bin default NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  `FAV_COUNT` decimal(18,0) default NULL,
  `LAYOUT` varchar(255) collate utf8_bin default NULL,
  `ppversion` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `ppage_username` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portalpage`
--

LOCK TABLES `portalpage` WRITE;
/*!40000 ALTER TABLE `portalpage` DISABLE KEYS */;
INSERT INTO `portalpage` VALUES ('10000',NULL,'System Dashboard',NULL,'0','0','AA','0');
/*!40000 ALTER TABLE `portalpage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `portletconfiguration`
--

DROP TABLE IF EXISTS `portletconfiguration`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `portletconfiguration` (
  `ID` decimal(18,0) NOT NULL,
  `PORTALPAGE` decimal(18,0) default NULL,
  `PORTLET_ID` varchar(255) collate utf8_bin default NULL,
  `COLUMN_NUMBER` decimal(9,0) default NULL,
  `positionseq` decimal(9,0) default NULL,
  `GADGET_XML` text collate utf8_bin,
  `COLOR` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `portletconfiguration`
--

LOCK TABLES `portletconfiguration` WRITE;
/*!40000 ALTER TABLE `portletconfiguration` DISABLE KEYS */;
INSERT INTO `portletconfiguration` VALUES ('10000','10000',NULL,'0','0','rest/gadgets/1.0/g/com.atlassian.jira.gadgets:introduction-gadget/gadgets/introduction-gadget.xml',NULL),('10001','10000',NULL,'0','1','rest/gadgets/1.0/g/com.atlassian.jira.gadgets:admin-gadget/gadgets/admin-gadget.xml',NULL),('10002','10000',NULL,'1','0','rest/gadgets/1.0/g/com.atlassian.jira.gadgets:assigned-to-me-gadget/gadgets/assigned-to-me-gadget.xml',NULL),('10003','10000',NULL,'1','1','rest/gadgets/1.0/g/com.atlassian.streams.streams-jira-plugin:activitystream-gadget/gadgets/activitystream-gadget.xml',NULL);
/*!40000 ALTER TABLE `portletconfiguration` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `priority`
--

DROP TABLE IF EXISTS `priority`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `priority` (
  `ID` varchar(60) collate utf8_bin NOT NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  `pname` varchar(60) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `ICONURL` varchar(255) collate utf8_bin default NULL,
  `STATUS_COLOR` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `priority`
--

LOCK TABLES `priority` WRITE;
/*!40000 ALTER TABLE `priority` DISABLE KEYS */;
INSERT INTO `priority` VALUES ('1','1','Blocker','Blocks development and/or testing work, production could not run.','/images/icons/priorities/blocker.png','#cc0000'),('2','2','Critical','Crashes, loss of data, severe memory leak.','/images/icons/priorities/critical.png','#ff0000'),('3','3','Major','Major loss of function.','/images/icons/priorities/major.png','#009900'),('4','4','Minor','Minor loss of function, or other problem where easy workaround is present.','/images/icons/priorities/minor.png','#006600'),('5','5','Trivial','Cosmetic problem like misspelt words or misaligned text.','/images/icons/priorities/trivial.png','#003300');
/*!40000 ALTER TABLE `priority` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project` (
  `ID` decimal(18,0) NOT NULL,
  `pname` varchar(255) collate utf8_bin default NULL,
  `URL` varchar(255) collate utf8_bin default NULL,
  `LEAD` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `pkey` varchar(255) collate utf8_bin default NULL,
  `ORIGINALKEY` varchar(255) collate utf8_bin default NULL,
  `pcounter` decimal(18,0) default NULL,
  `ASSIGNEETYPE` decimal(18,0) default NULL,
  `AVATAR` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `idx_project_key` (`pkey`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_key`
--

DROP TABLE IF EXISTS `project_key`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `project_key` (
  `ID` decimal(18,0) NOT NULL,
  `PROJECT_ID` decimal(18,0) default NULL,
  `PROJECT_KEY` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `idx_all_project_keys` (`PROJECT_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_key`
--

LOCK TABLES `project_key` WRITE;
/*!40000 ALTER TABLE `project_key` DISABLE KEYS */;
/*!40000 ALTER TABLE `project_key` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectcategory`
--

DROP TABLE IF EXISTS `projectcategory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectcategory` (
  `ID` decimal(18,0) NOT NULL,
  `cname` varchar(255) collate utf8_bin default NULL,
  `description` text collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectcategory`
--

LOCK TABLES `projectcategory` WRITE;
/*!40000 ALTER TABLE `projectcategory` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectcategory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectrole`
--

DROP TABLE IF EXISTS `projectrole`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectrole` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectrole`
--

LOCK TABLES `projectrole` WRITE;
/*!40000 ALTER TABLE `projectrole` DISABLE KEYS */;
INSERT INTO `projectrole` VALUES ('10000','Users','A project role that represents users in a project'),('10001','Developers','A project role that represents developers in a project'),('10002','Administrators','A project role that represents administrators in a project');
/*!40000 ALTER TABLE `projectrole` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectroleactor`
--

DROP TABLE IF EXISTS `projectroleactor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectroleactor` (
  `ID` decimal(18,0) NOT NULL,
  `PID` decimal(18,0) default NULL,
  `PROJECTROLEID` decimal(18,0) default NULL,
  `ROLETYPE` varchar(255) collate utf8_bin default NULL,
  `ROLETYPEPARAMETER` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `role_player_idx` (`PROJECTROLEID`,`PID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectroleactor`
--

LOCK TABLES `projectroleactor` WRITE;
/*!40000 ALTER TABLE `projectroleactor` DISABLE KEYS */;
INSERT INTO `projectroleactor` VALUES ('10000',NULL,'10000','atlassian-group-role-actor','jira-users'),('10001',NULL,'10001','atlassian-group-role-actor','jira-developers'),('10002',NULL,'10002','atlassian-group-role-actor','jira-administrators');
/*!40000 ALTER TABLE `projectroleactor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `projectversion`
--

DROP TABLE IF EXISTS `projectversion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `projectversion` (
  `ID` decimal(18,0) NOT NULL,
  `PROJECT` decimal(18,0) default NULL,
  `vname` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `SEQUENCE` decimal(18,0) default NULL,
  `RELEASED` varchar(10) collate utf8_bin default NULL,
  `ARCHIVED` varchar(10) collate utf8_bin default NULL,
  `URL` varchar(255) collate utf8_bin default NULL,
  `STARTDATE` datetime default NULL,
  `RELEASEDATE` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `projectversion`
--

LOCK TABLES `projectversion` WRITE;
/*!40000 ALTER TABLE `projectversion` DISABLE KEYS */;
/*!40000 ALTER TABLE `projectversion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propertydata`
--

DROP TABLE IF EXISTS `propertydata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `propertydata` (
  `ID` decimal(18,0) NOT NULL,
  `propertyvalue` blob,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propertydata`
--

LOCK TABLES `propertydata` WRITE;
/*!40000 ALTER TABLE `propertydata` DISABLE KEYS */;
/*!40000 ALTER TABLE `propertydata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propertydate`
--

DROP TABLE IF EXISTS `propertydate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `propertydate` (
  `ID` decimal(18,0) NOT NULL,
  `propertyvalue` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propertydate`
--

LOCK TABLES `propertydate` WRITE;
/*!40000 ALTER TABLE `propertydate` DISABLE KEYS */;
/*!40000 ALTER TABLE `propertydate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propertydecimal`
--

DROP TABLE IF EXISTS `propertydecimal`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `propertydecimal` (
  `ID` decimal(18,0) NOT NULL,
  `propertyvalue` decimal(18,6) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propertydecimal`
--

LOCK TABLES `propertydecimal` WRITE;
/*!40000 ALTER TABLE `propertydecimal` DISABLE KEYS */;
/*!40000 ALTER TABLE `propertydecimal` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propertyentry`
--

DROP TABLE IF EXISTS `propertyentry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `propertyentry` (
  `ID` decimal(18,0) NOT NULL,
  `ENTITY_NAME` varchar(255) collate utf8_bin default NULL,
  `ENTITY_ID` decimal(18,0) default NULL,
  `PROPERTY_KEY` varchar(255) collate utf8_bin default NULL,
  `propertytype` decimal(9,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `osproperty_all` (`ENTITY_ID`),
  KEY `osproperty_entityName` (`ENTITY_NAME`),
  KEY `osproperty_propertyKey` (`PROPERTY_KEY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propertyentry`
--

LOCK TABLES `propertyentry` WRITE;
/*!40000 ALTER TABLE `propertyentry` DISABLE KEYS */;
INSERT INTO `propertyentry` VALUES ('10000','jira.properties','1','webwork.i18n.encoding','5'),('10006','jira.properties','1','org.apache.shindig.common.crypto.BlobCrypter:key','5'),('10008','BambooServerProperties','1','bamboo.config.version','2'),('10009','jira.properties','1','jira.trustedapp.key.private.data','6'),('10010','jira.properties','1','jira.trustedapp.key.public.data','6'),('10011','jira.properties','1','jira.sid.key','5'),('10012','jira.properties','1','jira.trustedapp.uid','5'),('10013','jira.properties','1','AO_4AEACD_#','5'),('10014','jira.properties','1','jira.webresource.superbatch.flushcounter','5'),('10015','jira.properties','1','com.atlassian.jira.util.index.IndexingCounterManagerImpl.counterValue','3'),('10016','jira.properties','1','jira.i18n.language.index','5'),('10017','jira.properties','1','jira.title','5'),('10018','jira.properties','1','jira.baseurl','5'),('10019','jira.properties','1','jira.mode','5'),('10020','jira.properties','1','jira.path.index.use.default.directory','1'),('10021','jira.properties','1','jira.option.indexing','1'),('10022','jira.properties','1','jira.path.attachments','5'),('10023','jira.properties','1','jira.path.attachments.use.default.directory','1'),('10024','jira.properties','1','jira.option.allowattachments','1'),('10025','ServiceConfig','10001','USE_DEFAULT_DIRECTORY','5'),('10026','jira.properties','1','jira.path.backup','5'),('10027','jira.properties','1','License20','6'),('10028','jira.properties','1','jira.edition','5'),('10037','jira.properties','1','jira.setup','5'),('10038','jira.properties','1','jira.option.allowunassigned','1'),('10039','jira.properties','1','jira.option.user.externalmanagement','1'),('10040','jira.properties','1','jira.option.voting','1'),('10041','jira.properties','1','jira.option.watching','1'),('10042','jira.properties','1','jira.option.issuelinking','1'),('10043','jira.properties','1','jira.option.emailvisible','5'),('10044','jira.properties','1','jira.version.patched','5'),('10045','jira.properties','1','jira.issue.desc.environment','5'),('10046','jira.properties','1','jira.issue.desc.timetrack','5'),('10047','jira.properties','1','jira.option.timetracking','1'),('10048','jira.properties','1','jira.timetracking.estimates.legacy.behaviour','1'),('10049','jira.properties','1','jira.timetracking.format','5'),('10050','jira.properties','1','jira.timetracking.default.unit','5'),('10051','jira.properties','1','jira.timetracking.days.per.week','5'),('10052','jira.properties','1','jira.timetracking.hours.per.day','5'),('10053','jira.properties','1','jira.issue.desc.original.timetrack','5'),('10100','jira.properties','1','jira.scheme.default.issue.type','5'),('10101','jira.properties','1','jira.constant.default.resolution','5'),('10102','jira.properties','1','webwork.multipart.maxSize','5'),('10103','jira.properties','1','jira.avatar.default.id','5'),('10104','jira.properties','1','jira.maximum.authentication.attempts.allowed','5'),('10105','jira.properties','1','jira.avatar.user.default.id','5'),('10200','jira.properties','1','jira.avatar.user.anonymous.id','5'),('10201','jira.properties','1','jira.whitelist.rules','6'),('10202','jira.properties','1','jira.whitelist.disabled','1'),('10203','jira.properties','1','jira.option.rpc.allow','1'),('10204','jira.properties','1','jira.clone.link.legacy.direction','1'),('10205','jira.properties','1','jira.projectkey.maxlength','5'),('10206','jira.properties','1','jira.admin.gadget.task.list.enabled','1'),('10207','jira.properties','1','jira.projectname.maxlength','5'),('10208','jira.properties','1','jira.version','5'),('10209','jira.properties','1','jira.downgrade.minimum.version','5'),('10300','jira.properties','1','jira.option.allowsubtasks','1'),('10302','fisheye-jira-plugin.properties','1','FISH-375-fixed','5'),('10303','fisheye-jira-plugin.properties','1','fisheye.ual.migration.complete','5'),('10304','fisheye-jira-plugin.properties','1','fisheye.ual.crucible.enabled.property.fix.complete','5'),('10306','jira.properties','1','com.atlassian.sal.jira:build','5'),('10308','jira.properties','1','com.atlassian.jira.jira-mail-plugin:build','5'),('10309','jira.properties','1','com.atlassian.jira.plugins.webhooks.jira-webhooks-plugin:build','5'),('10319','jira.properties','1','dvcs.connector.bitbucket.url','5'),('10320','jira.properties','1','dvcs.connector.github.url','5'),('10322','jira.properties','1','AO_E8B6CC_#','5'),('10323','jira.properties','1','com.atlassian.jira.plugins.jira-bitbucket-connector-plugin:build','5'),('10324','jira.properties','1','com.atlassian.jira.lookandfeel:isDefaultFavicon','5'),('10325','jira.properties','1','com.atlassian.jira.lookandfeel:usingCustomFavicon','5'),('10326','jira.properties','1','com.atlassian.jira.lookandfeel:customDefaultFaviconURL','5'),('10327','jira.properties','1','com.atlassian.jira.lookandfeel:customDefaultFaviconHiresURL','5'),('10328','jira.properties','1','com.atlassian.jira.lookandfeel:faviconWidth','5'),('10329','jira.properties','1','com.atlassian.jira.lookandfeel:faviconHeight','5'),('10330','jira.properties','1','jira.lf.favicon.url','5'),('10331','jira.properties','1','jira.webresource.flushcounter','5'),('10332','jira.properties','1','jira.lf.favicon.hires.url','5'),('10333','jira.properties','1','com.atlassian.jira.lookandfeel:build','5'),('10334','jira.properties','1','com.atlassian.jira.plugins.jira-importers-plugin:build','5'),('10335','jira.properties','1','com.atlassian.jira.plugin.ext.bamboo:build','5'),('10339','jira.properties','1','com.atlassian.upm.mail.impl.PluginSettingsUserEmailSettingsStore:use78b9c0b572719a68385340a04c056a94','5'),('10340','jira.properties','1','com.atlassian.upm:notifications:dismissal-plugin.request','5'),('10341','jira.properties','1','com.atlassian.upm:notifications:dismissal-update','5'),('10342','jira.properties','1','com.atlassian.upm:notifications:dismissal-evaluation.expired','5'),('10344','jira.properties','1','com.atlassian.upm:notifications:dismissal-edition.mismatch','5'),('10345','jira.properties','1','com.atlassian.upm:notifications:dismissal-maintenance.expired','5'),('10347','jira.properties','1','com.atlassian.upm:notifications:dismissal-new.licenses','5'),('10348','jira.properties','1','com.atlassian.upm:notifications:dismissal-updated.licenses','5'),('10349','jira.properties','1','com.atlassian.upm:notifications:dismissal-auto.updated.plugin','5'),('10350','jira.properties','1','com.atlassian.upm:notifications:dismissal-auto.updated.upm','5'),('10351','jira.properties','1','com.atlassian.upm.request.PluginSettingsPluginRequestStore:requests:requests_v2','5'),('10352','jira.properties','1','com.atlassian.upm.atlassian-universal-plugin-manager-plugin:build','5'),('10353','jira.properties','1','com.atlassian.jira.plugins.jira-workflow-designer:build','5'),('10357','jira.properties','1','jira-header-plugin.studio-tab-migration-complete','5'),('10379','jira.properties','1','plugins.lastVersion.server','5'),('10380','jira.properties','1','plugins.lastVersion.plugins','5'),('10605','jira.properties','1','com.atlassian.activeobjects.admin.ActiveObjectsPluginToTablesMapping','6'),('10607','ServiceConfig','10400','pluginJobName','5'),('10608','ServiceConfig','10400','repeatInterval','5'),('10609','ServiceConfig','10400','initiallyFired','5'),('10610','ServiceConfig','10401','pluginJobName','5'),('10611','ServiceConfig','10401','repeatInterval','5'),('10612','ServiceConfig','10401','initiallyFired','5'),('10613','ServiceConfig','10402','pluginJobName','5'),('10614','ServiceConfig','10402','repeatInterval','5'),('10615','ServiceConfig','10402','initiallyFired','5'),('10616','ServiceConfig','10403','pluginJobName','5'),('10617','ServiceConfig','10403','repeatInterval','5'),('10618','ServiceConfig','10403','initiallyFired','5'),('10619','ServiceConfig','10404','pluginJobName','5'),('10620','ServiceConfig','10404','repeatInterval','5'),('10621','ServiceConfig','10404','initiallyFired','5'),('10622','ServiceConfig','10405','pluginJobName','5'),('10623','ServiceConfig','10405','repeatInterval','5'),('10624','ServiceConfig','10405','initiallyFired','5'),('10625','ServiceConfig','10406','pluginJobName','5'),('10626','ServiceConfig','10406','repeatInterval','5'),('10627','ServiceConfig','10406','initiallyFired','5'),('10628','jira.properties','1','com.atlassian.upm.log.PluginSettingsAuditLogService:log:upm_audit_log_v3','6'),('10631','ServiceConfig','10407','pluginJobName','5'),('10632','ServiceConfig','10407','repeatInterval','5'),('10633','ServiceConfig','10407','initiallyFired','5'),('10634','ServiceConfig','10408','pluginJobName','5'),('10635','ServiceConfig','10408','repeatInterval','5'),('10636','ServiceConfig','10408','initiallyFired','5'),('10637','jira.properties','1','jira.plugin.state-.com.atlassian.jira.welcome.jira-welcome-plugin:show-whats-new-flag','5'),('10644','jira.properties','1','com.atlassian.upm:notifications:notification-edition.mismatch','5'),('10645','jira.properties','1','com.atlassian.upm:notifications:notification-evaluation.expired','5'),('10646','jira.properties','1','com.atlassian.upm:notifications:notification-evaluation.nearlyexpired','5'),('10647','jira.properties','1','com.atlassian.upm:notifications:notification-maintenance.expired','5'),('10648','jira.properties','1','com.atlassian.upm:notifications:notification-maintenance.nearlyexpired','5'),('10649','jira.properties','1','com.atlassian.upm:notifications:notification-plugin.request','5'),('10651','jira.properties','1','com.atlassian.upm.mail.impl.PluginSettingsLicenseEmailStorelicense-emails_v2','5'),('10652','jira.properties','1','jira.option.web.usegzip','1');
/*!40000 ALTER TABLE `propertyentry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propertynumber`
--

DROP TABLE IF EXISTS `propertynumber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `propertynumber` (
  `ID` decimal(18,0) NOT NULL,
  `propertyvalue` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propertynumber`
--

LOCK TABLES `propertynumber` WRITE;
/*!40000 ALTER TABLE `propertynumber` DISABLE KEYS */;
INSERT INTO `propertynumber` VALUES ('10008','22'),('10015','3'),('10020','1'),('10021','1'),('10023','1'),('10024','1'),('10038','0'),('10039','1'),('10040','1'),('10041','1'),('10042','1'),('10047','1'),('10048','0'),('10202','0'),('10203','1'),('10204','0'),('10206','1'),('10300','1'),('10652','0');
/*!40000 ALTER TABLE `propertynumber` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propertystring`
--

DROP TABLE IF EXISTS `propertystring`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `propertystring` (
  `ID` decimal(18,0) NOT NULL,
  `propertyvalue` text collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propertystring`
--

LOCK TABLES `propertystring` WRITE;
/*!40000 ALTER TABLE `propertystring` DISABLE KEYS */;
INSERT INTO `propertystring` VALUES ('10000','UTF-8'),('10006','8yPoqQfg3reqFWpBZ5rnOOap7rKqcMJ5NOZsU4waeQ4='),('10011','BIK0-38L9-2L35-EZ8D'),('10012','jira:13978630'),('10013','1'),('10014','6'),('10016','english-moderate-stemming'),('10017','NovaForge'),('10018',''),('10019','private'),('10022','/datas/jira-datas/data/attachments'),('10025','true'),('10026','/datas/jira-datas/export'),('10028','enterprise'),('10037','true'),('10043','hide'),('10044','6106'),('10045','For example operating system, software platform and/or hardware specifications (include as appropriate for the issue).'),('10046','An estimate of how much work remains until this issue will be resolved.<br>\nThe format of this is \' *w *d *h *m \' (representing weeks, days, hours and minutes - where * can be any number)<br>\nExamples: 4d, 5h 30m, 60m and 3w.'),('10049','pretty'),('10050','MINUTE'),('10051','5'),('10052','8'),('10053','This value can not be changed after work has begun on the issue.'),('10100','10000'),('10101','1'),('10102','10485760'),('10103','10011'),('10104','3'),('10105','10122'),('10200','10123'),('10205','80'),('10207','100'),('10208','6.0.7'),('10209','6.0.2'),('10302','1'),('10303','1'),('10304','1'),('10306','2'),('10308','2'),('10309','1'),('10319','https://bitbucket.org'),('10320','https://github.com'),('10322','12'),('10323','2'),('10324','false'),('10325','false'),('10326','/favicon.ico'),('10327','/images/64jira.png'),('10328','64'),('10329','64'),('10330','/favicon.ico'),('10331','3'),('10332','/images/64jira.png'),('10333','1'),('10334','1'),('10335','1'),('10339','#java.util.List\n'),('10340','#java.util.List\n'),('10341','#java.util.List\n'),('10342','#java.util.List\n'),('10344','#java.util.List\n'),('10345','#java.util.List\n'),('10347','#java.util.List\n'),('10348','#java.util.List\n'),('10349','#java.util.List\n'),('10350','#java.util.List\n'),('10351','#java.util.List\n'),('10352','2'),('10353','1'),('10357','migrated'),('10379','6106'),('10380','0.8.1068'),('10607','Service Provider Session Remover'),('10608','28800000'),('10609','false'),('10610','com.atlassian.jira.plugin.ext.bamboo.service.PlanStatusUpdateServiceImpl:job'),('10611','60000'),('10612','true'),('10613','LocalPluginLicenseNotificationPluginJob-job'),('10614','86400000'),('10615','true'),('10616','RemotePluginLicenseNotificationPluginJob-job'),('10617','3600000'),('10618','true'),('10619','PluginRequestCheckPluginJob-job'),('10620','3600000'),('10621','true'),('10622','PluginUpdateCheckPluginJob-job'),('10623','86400000'),('10624','false'),('10625','AddOnLicensingEmailNotificationsPluginJob-job'),('10626','14400000'),('10627','true'),('10631','com.atlassian.streams.internal.ActivityProviderConnectionMonitorImpl:activityProviderMonitor'),('10632','300000'),('10633','false'),('10634','com.atlassian.jira.plugins.dvcs.scheduler.DvcsScheduler:job'),('10635','3600000'),('10636','false'),('10637','true'),('10644','#java.util.List\n'),('10645','#java.util.List\n'),('10646','#java.util.List\n'),('10647','#java.util.List\n'),('10648','#java.util.List\n'),('10649','#java.util.List\n'),('10651','#java.util.List\n');
/*!40000 ALTER TABLE `propertystring` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `propertytext`
--

DROP TABLE IF EXISTS `propertytext`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `propertytext` (
  `ID` decimal(18,0) NOT NULL,
  `propertyvalue` longtext collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `propertytext`
--

LOCK TABLES `propertytext` WRITE;
/*!40000 ALTER TABLE `propertytext` DISABLE KEYS */;
INSERT INTO `propertytext` VALUES ('10009','MIIEwAIBADANBgkqhkiG9w0BAQEFAASCBKowggSmAgEAAoIBAQC1FPlLoGNX74ARk5EXIPJZD+jvNmTfeRzPFisYE1nOyr7kI6rWId+cJCYiyHmuH9LPxHaoZmxt9cviFz/uy4HMhagT42F8qF3P2Z/VbJJyYLwL8UYEpgFIMsc7hAdCYdXAuXF3mWeR+7Nj9XWrHfVUtGgATKMIFLt0BUeFbU02JLKyFvpF8rvTOf7Tvhun+Gn46BeZH5ZsSzO6cVKZkTAsMJ+prYCCw92P3YbO3nKIFAYuXPZfFAmgRcF6FNWqR87eFqafpTHwsv7HyT1Ab3waa/qSPgJTslHdeP0XdYZ/JRJROknLN7Qh4Y1f1p653y8qM3ZGNVXX62/G3BwxL+q9AgMBAAECggEBAKVumOZXjArNOkJUf2wS5HUrTNIl7ngfLIugpBQlsZlzrnu8fhmM7r22ZMTIRCoo1q6LvR/SGuNGeFKAjR76+wU/YjNxOyHoq3P1TMltnEk0SPfcH+ylQGyWP2AHGZ6yxNaCVMA//NfojJYYrLx87SHKg20qlpMOZcbz/8hM+AoqY4TkBbysovLHW9W/Lndsdg7aNlI9TDu/SA7+BgZY35dXPUBoDGun7wcCM1x5aS7t3eTAEpwg7rQPnWICbUc95g+o89gy79MGZdpT9wtFKLbbyaeWvtCKuQbTXFHZte8M1HDeghxuks6AqE5AaM8q2Rh7CRjqVuI0Pxrk1QUpTP0CgYEA/5z7ZNIkinGJiV+fr1twfNi82RyX0TNT2hB1ixTqrBOMWw3h6Uq5tn2Au8/E6iVbsU6gDXqZtlWAq7QrVbH+pNDpaI/KjkPVGDl9ZyFgWLsABwWkORUzkmZ8/RdO1UUeoAREArBDlgA8kIGT2C/QkYa7P/0gGoLUGLLKePHPdD8CgYEAtVsey+Rf+rb7+Di/BJ+AZiGiY/YPiQQWEnWVkXrl4eRJRzj5P18Mvrxl8QAdMtGKyZBvtLKtHgilAKCDYs+WuXcWD8W6FX2gstIisrnW1wIj96x2L0zLiqhHB5fmhfQBziPZ6ajlWkX5J/R6iyAwAgxPQbtWNwhwmUQRgFCi8gMCgYEA7jvITjJRXjrtffyCnvQxRhrW/99XW+Jjv3ThqKvpAFID2YV5zW4Y4o3cIq9FjxkceYeXQpjGlJriHqYAUZFgjQos0Ihbt/ZesII8b5c67dfz8Zd/dLhe5Y7h5R0JG1qdbXQsUvknNLGpwOEUA0SlNk0FTmFDv5G5zf8tGEX/HQ8CgYEAlHeoWpr3yDSNSsSJ/KrnXrUiUFtclHr35tzEOXk3QRq2ssv9oNebg3qQqh/Bx5yr1h+ck33DI9dH3Z+KcBYp+mN3eCoTEQzrxfkp1s81BogGEfjqOAbSiw0AOUMtwXPlFnFbMGLIgILQWtlWiRcstkCFkfZfumX6aD7FZRqeiykCgYEAxhsb514b2Auxjq0/4PrqT0oMrup+RaoHB13k4FKtL8j+bpJnyrKm/PjBvvxArBCFohOlQbcmJnRvXG7OXH1iawooRjZIl8UDBK1R0RANZ37iVWEZJ3szYdIDUCUiCIKFBokABK3Pd4Ph3Z7llhMuWlWF4W9QI3QHAlq7zCPSU9k='),('10010','MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtRT5S6BjV++AEZORFyDyWQ/o7zZk33kczxYrGBNZzsq+5COq1iHfnCQmIsh5rh/Sz8R2qGZsbfXL4hc/7suBzIWoE+NhfKhdz9mf1WyScmC8C/FGBKYBSDLHO4QHQmHVwLlxd5lnkfuzY/V1qx31VLRoAEyjCBS7dAVHhW1NNiSyshb6RfK70zn+074bp/hp+OgXmR+WbEszunFSmZEwLDCfqa2AgsPdj92Gzt5yiBQGLlz2XxQJoEXBehTVqkfO3hamn6Ux8LL+x8k9QG98Gmv6kj4CU7JR3Xj9F3WGfyUSUTpJyze0IeGNX9aeud8vKjN2RjVV1+tvxtwcMS/qvQIDAQAB'),('10027',''),('10201','http://www.atlassian.com/*\n'),('10605','{\"AO_38321B_CUSTOM_CONTENT_LINK\":{\"key\":\"com.atlassian.plugins.atlassian-nav-links-plugin\",\"name\":\"Atlassian Navigation Links Plugin\",\"version\":\"3.2.2.1\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_E8B6CC_ISSUE_TO_CHANGESET\":{\"key\":\"com.atlassian.jira.plugins.jira-bitbucket-connector-plugin\",\"name\":\"JIRA DVCS Connector Plugin\",\"version\":\"1.4.0.1\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_B9A0F0_APPLIED_TEMPLATE\":{\"key\":\"com.atlassian.jira.project-templates-plugin\",\"name\":\"Project Templates Plugin\",\"version\":\"2.18\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_563AEE_ACTIVITY_ENTITY\":{\"key\":\"com.atlassian.streams.streams-thirdparty-plugin\",\"name\":\"Streams Third Party Provider Plugin\",\"version\":\"5.3.0-m7\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_563AEE_TARGET_ENTITY\":{\"key\":\"com.atlassian.streams.streams-thirdparty-plugin\",\"name\":\"Streams Third Party Provider Plugin\",\"version\":\"5.3.0-m7\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_E8B6CC_REPO_TO_CHANGESET\":{\"key\":\"com.atlassian.jira.plugins.jira-bitbucket-connector-plugin\",\"name\":\"JIRA DVCS Connector Plugin\",\"version\":\"1.4.0.1\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_563AEE_MEDIA_LINK_ENTITY\":{\"key\":\"com.atlassian.streams.streams-thirdparty-plugin\",\"name\":\"Streams Third Party Provider Plugin\",\"version\":\"5.3.0-m7\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_4AEACD_WEBHOOK_DAO\":{\"key\":\"com.atlassian.jira.plugins.webhooks.jira-webhooks-plugin\",\"name\":\"JIRA WebHooks Plugin\",\"version\":\"1.0.13\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_E8B6CC_ORGANIZATION_MAPPING\":{\"key\":\"com.atlassian.jira.plugins.jira-bitbucket-connector-plugin\",\"name\":\"JIRA DVCS Connector Plugin\",\"version\":\"1.4.0.1\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_563AEE_ACTOR_ENTITY\":{\"key\":\"com.atlassian.streams.streams-thirdparty-plugin\",\"name\":\"Streams Third Party Provider Plugin\",\"version\":\"5.3.0-m7\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_563AEE_OBJECT_ENTITY\":{\"key\":\"com.atlassian.streams.streams-thirdparty-plugin\",\"name\":\"Streams Third Party Provider Plugin\",\"version\":\"5.3.0-m7\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_97EDAB_USERINVITATION\":{\"key\":\"com.atlassian.jira.jira-invite-user-plugin\",\"name\":\"Atlassian JIRA - Plugins - Invite User\",\"version\":\"1.10\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_E8B6CC_REPOSITORY_MAPPING\":{\"key\":\"com.atlassian.jira.plugins.jira-bitbucket-connector-plugin\",\"name\":\"JIRA DVCS Connector Plugin\",\"version\":\"1.4.0.1\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_563AEE_ACTIVITY_OBJ_ENTITY\":{\"key\":\"com.atlassian.streams.streams-thirdparty-plugin\",\"name\":\"Streams Third Party Provider Plugin\",\"version\":\"5.3.0-m7\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"},\"AO_E8B6CC_CHANGESET_MAPPING\":{\"key\":\"com.atlassian.jira.plugins.jira-bitbucket-connector-plugin\",\"name\":\"JIRA DVCS Connector Plugin\",\"version\":\"1.4.0.1\",\"vendorName\":\"Atlassian\",\"vendorUrl\":\"http://www.atlassian.com/\"}}'),('10628','#java.util.List\n{\"userKey\":\"JIRA\",\"date\":1378882493621,\"i18nKey\":\"upm.auditLog.upm.startup\",\"entryType\":\"UPM_STARTUP\",\"params\":[]}\n{\"userKey\":\"JIRA\",\"date\":1378882206596,\"i18nKey\":\"upm.auditLog.upm.startup\",\"entryType\":\"UPM_STARTUP\",\"params\":[]}\n{\"userKey\":\"JIRA\",\"date\":1378365277010,\"i18nKey\":\"upm.auditLog.upm.startup\",\"entryType\":\"UPM_STARTUP\",\"params\":[]}\n{\"userKey\":\"JIRA\",\"date\":1377615579548,\"i18nKey\":\"upm.auditLog.upm.startup\",\"entryType\":\"UPM_STARTUP\",\"params\":[]}');
/*!40000 ALTER TABLE `propertytext` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_calendars`
--

DROP TABLE IF EXISTS `qrtz_calendars`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_calendars` (
  `ID` decimal(18,0) default NULL,
  `CALENDAR_NAME` varchar(255) collate utf8_bin NOT NULL,
  `CALENDAR` text collate utf8_bin,
  PRIMARY KEY  (`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_calendars`
--

LOCK TABLES `qrtz_calendars` WRITE;
/*!40000 ALTER TABLE `qrtz_calendars` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_calendars` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_cron_triggers`
--

DROP TABLE IF EXISTS `qrtz_cron_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_cron_triggers` (
  `ID` decimal(18,0) NOT NULL,
  `trigger_id` decimal(18,0) default NULL,
  `cronExperssion` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_cron_triggers`
--

LOCK TABLES `qrtz_cron_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_cron_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_cron_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_fired_triggers`
--

DROP TABLE IF EXISTS `qrtz_fired_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_fired_triggers` (
  `ID` decimal(18,0) default NULL,
  `ENTRY_ID` varchar(255) collate utf8_bin NOT NULL,
  `trigger_id` decimal(18,0) default NULL,
  `TRIGGER_LISTENER` varchar(255) collate utf8_bin default NULL,
  `FIRED_TIME` datetime default NULL,
  `TRIGGER_STATE` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_fired_triggers`
--

LOCK TABLES `qrtz_fired_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_fired_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_fired_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_job_details`
--

DROP TABLE IF EXISTS `qrtz_job_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_job_details` (
  `ID` decimal(18,0) NOT NULL,
  `JOB_NAME` varchar(255) collate utf8_bin default NULL,
  `JOB_GROUP` varchar(255) collate utf8_bin default NULL,
  `CLASS_NAME` varchar(255) collate utf8_bin default NULL,
  `IS_DURABLE` varchar(60) collate utf8_bin default NULL,
  `IS_STATEFUL` varchar(60) collate utf8_bin default NULL,
  `REQUESTS_RECOVERY` varchar(60) collate utf8_bin default NULL,
  `JOB_DATA` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_job_details`
--

LOCK TABLES `qrtz_job_details` WRITE;
/*!40000 ALTER TABLE `qrtz_job_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_job_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_job_listeners`
--

DROP TABLE IF EXISTS `qrtz_job_listeners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_job_listeners` (
  `ID` decimal(18,0) NOT NULL,
  `JOB` decimal(18,0) default NULL,
  `JOB_LISTENER` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_job_listeners`
--

LOCK TABLES `qrtz_job_listeners` WRITE;
/*!40000 ALTER TABLE `qrtz_job_listeners` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_job_listeners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_simple_triggers`
--

DROP TABLE IF EXISTS `qrtz_simple_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_simple_triggers` (
  `ID` decimal(18,0) NOT NULL,
  `trigger_id` decimal(18,0) default NULL,
  `REPEAT_COUNT` decimal(9,0) default NULL,
  `REPEAT_INTERVAL` decimal(18,0) default NULL,
  `TIMES_TRIGGERED` decimal(9,0) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_simple_triggers`
--

LOCK TABLES `qrtz_simple_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_simple_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_simple_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_trigger_listeners`
--

DROP TABLE IF EXISTS `qrtz_trigger_listeners`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_trigger_listeners` (
  `ID` decimal(18,0) NOT NULL,
  `trigger_id` decimal(18,0) default NULL,
  `TRIGGER_LISTENER` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_trigger_listeners`
--

LOCK TABLES `qrtz_trigger_listeners` WRITE;
/*!40000 ALTER TABLE `qrtz_trigger_listeners` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_trigger_listeners` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `qrtz_triggers`
--

DROP TABLE IF EXISTS `qrtz_triggers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `qrtz_triggers` (
  `ID` decimal(18,0) NOT NULL,
  `TRIGGER_NAME` varchar(255) collate utf8_bin default NULL,
  `TRIGGER_GROUP` varchar(255) collate utf8_bin default NULL,
  `JOB` decimal(18,0) default NULL,
  `NEXT_FIRE` datetime default NULL,
  `TRIGGER_STATE` varchar(255) collate utf8_bin default NULL,
  `TRIGGER_TYPE` varchar(60) collate utf8_bin default NULL,
  `START_TIME` datetime default NULL,
  `END_TIME` datetime default NULL,
  `CALENDAR_NAME` varchar(255) collate utf8_bin default NULL,
  `MISFIRE_INSTR` decimal(9,0) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `qrtz_triggers`
--

LOCK TABLES `qrtz_triggers` WRITE;
/*!40000 ALTER TABLE `qrtz_triggers` DISABLE KEYS */;
/*!40000 ALTER TABLE `qrtz_triggers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `remembermetoken`
--

DROP TABLE IF EXISTS `remembermetoken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `remembermetoken` (
  `ID` decimal(18,0) NOT NULL,
  `CREATED` datetime default NULL,
  `TOKEN` varchar(255) collate utf8_bin default NULL,
  `USERNAME` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `remembermetoken_username_index` (`USERNAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `remembermetoken`
--

LOCK TABLES `remembermetoken` WRITE;
/*!40000 ALTER TABLE `remembermetoken` DISABLE KEYS */;
/*!40000 ALTER TABLE `remembermetoken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `remotelink`
--

DROP TABLE IF EXISTS `remotelink`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `remotelink` (
  `ID` decimal(18,0) NOT NULL,
  `ISSUEID` decimal(18,0) default NULL,
  `GLOBALID` varchar(255) collate utf8_bin default NULL,
  `TITLE` varchar(255) collate utf8_bin default NULL,
  `SUMMARY` text collate utf8_bin,
  `URL` text collate utf8_bin,
  `ICONURL` text collate utf8_bin,
  `ICONTITLE` text collate utf8_bin,
  `RELATIONSHIP` varchar(255) collate utf8_bin default NULL,
  `RESOLVED` char(1) collate utf8_bin default NULL,
  `STATUSICONURL` text collate utf8_bin,
  `STATUSICONTITLE` text collate utf8_bin,
  `STATUSICONLINK` text collate utf8_bin,
  `APPLICATIONTYPE` varchar(255) collate utf8_bin default NULL,
  `APPLICATIONNAME` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `remotelink_issueid` (`ISSUEID`,`GLOBALID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `remotelink`
--

LOCK TABLES `remotelink` WRITE;
/*!40000 ALTER TABLE `remotelink` DISABLE KEYS */;
/*!40000 ALTER TABLE `remotelink` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `replicatedindexoperation`
--

DROP TABLE IF EXISTS `replicatedindexoperation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `replicatedindexoperation` (
  `ID` decimal(18,0) NOT NULL,
  `INDEX_TIME` datetime default NULL,
  `NODE_ID` varchar(60) collate utf8_bin default NULL,
  `AFFECTED_INDEX` varchar(60) collate utf8_bin default NULL,
  `ENTITY_TYPE` varchar(60) collate utf8_bin default NULL,
  `AFFECTED_IDS` longtext collate utf8_bin,
  `OPERATION` varchar(10) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `node_operation_idx` (`NODE_ID`,`AFFECTED_INDEX`,`OPERATION`,`INDEX_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `replicatedindexoperation`
--

LOCK TABLES `replicatedindexoperation` WRITE;
/*!40000 ALTER TABLE `replicatedindexoperation` DISABLE KEYS */;
/*!40000 ALTER TABLE `replicatedindexoperation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `resolution`
--

DROP TABLE IF EXISTS `resolution`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `resolution` (
  `ID` varchar(60) collate utf8_bin NOT NULL,
  `SEQUENCE` decimal(18,0) default NULL,
  `pname` varchar(60) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `ICONURL` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `resolution`
--

LOCK TABLES `resolution` WRITE;
/*!40000 ALTER TABLE `resolution` DISABLE KEYS */;
INSERT INTO `resolution` VALUES ('1','1','Fixed','A fix for this issue is checked into the tree and tested.',NULL),('2','2','Won\'t Fix','The problem described is an issue which will never be fixed.',NULL),('3','3','Duplicate','The problem is a duplicate of an existing issue.',NULL),('4','4','Incomplete','The problem is not completely described.',NULL),('5','5','Cannot Reproduce','All attempts at reproducing this issue failed, or not enough information was available to reproduce the issue. Reading the code produces no clues as to why this behavior would occur. If more information appears later, please reopen the issue.',NULL);
/*!40000 ALTER TABLE `resolution` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schemeissuesecurities`
--

DROP TABLE IF EXISTS `schemeissuesecurities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schemeissuesecurities` (
  `ID` decimal(18,0) NOT NULL,
  `SCHEME` decimal(18,0) default NULL,
  `SECURITY` decimal(18,0) default NULL,
  `sec_type` varchar(255) collate utf8_bin default NULL,
  `sec_parameter` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `sec_scheme` (`SCHEME`),
  KEY `sec_security` (`SECURITY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schemeissuesecurities`
--

LOCK TABLES `schemeissuesecurities` WRITE;
/*!40000 ALTER TABLE `schemeissuesecurities` DISABLE KEYS */;
/*!40000 ALTER TABLE `schemeissuesecurities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schemeissuesecuritylevels`
--

DROP TABLE IF EXISTS `schemeissuesecuritylevels`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schemeissuesecuritylevels` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `SCHEME` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schemeissuesecuritylevels`
--

LOCK TABLES `schemeissuesecuritylevels` WRITE;
/*!40000 ALTER TABLE `schemeissuesecuritylevels` DISABLE KEYS */;
/*!40000 ALTER TABLE `schemeissuesecuritylevels` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schemepermissions`
--

DROP TABLE IF EXISTS `schemepermissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schemepermissions` (
  `ID` decimal(18,0) NOT NULL,
  `SCHEME` decimal(18,0) default NULL,
  `PERMISSION` decimal(18,0) default NULL,
  `perm_type` varchar(255) collate utf8_bin default NULL,
  `perm_parameter` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `prmssn_scheme` (`SCHEME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schemepermissions`
--

LOCK TABLES `schemepermissions` WRITE;
/*!40000 ALTER TABLE `schemepermissions` DISABLE KEYS */;
INSERT INTO `schemepermissions` VALUES ('10000',NULL,'0','group','jira-administrators'),('10001',NULL,'1','group','jira-users'),('10004','0','23','projectrole','10002'),('10005','0','10','projectrole','10000'),('10006','0','11','projectrole','10000'),('10007','0','15','projectrole','10000'),('10008','0','19','projectrole','10000'),('10009','0','13','projectrole','10001'),('10010','0','17','projectrole','10001'),('10011','0','14','projectrole','10001'),('10012','0','21','projectrole','10001'),('10013','0','12','projectrole','10001'),('10014','0','16','projectrole','10002'),('10015','0','18','projectrole','10001'),('10016','0','25','projectrole','10001'),('10017','0','28','projectrole','10001'),('10018','0','30','projectrole','10002'),('10019','0','20','projectrole','10001'),('10020','0','43','projectrole','10002'),('10021','0','42','projectrole','10000'),('10022','0','41','projectrole','10001'),('10023','0','40','projectrole','10000'),('10024','0','31','projectrole','10001'),('10025','0','32','projectrole','10002'),('10026','0','34','projectrole','10001'),('10027','0','35','projectrole','10000'),('10028','0','36','projectrole','10002'),('10029','0','37','projectrole','10000'),('10030','0','38','projectrole','10002'),('10031','0','39','projectrole','10000'),('10032',NULL,'22','group','jira-users'),('10033','0','29','projectrole','10001'),('10100',NULL,'33','group','jira-users'),('10101',NULL,'44','group','jira-administrators'),('10200','0','45','projectrole','10000'),('10300',NULL,'27','group','jira-users'),('10301',NULL,'24','group','jira-users'),('10400','0','10','projectrole','10002'),('10401','0','10','projectrole','10001'),('10402','0','29','projectrole','10002'),('10403','0','45','projectrole','10002'),('10404','0','45','projectrole','10001'),('10405','0','11','projectrole','10002'),('10406','0','11','projectrole','10001'),('10407','0','12','projectrole','10002'),('10408','0','28','projectrole','10002'),('10409','0','25','projectrole','10002'),('10410','0','13','projectrole','10002'),('10411','0','17','projectrole','10002'),('10412','0','14','projectrole','10002'),('10413','0','18','projectrole','10002'),('10414','0','21','projectrole','10002'),('10415','0','31','projectrole','10002'),('10416','0','15','projectrole','10001'),('10417','0','15','projectrole','10002'),('10418','0','34','projectrole','10002'),('10419','0','35','projectrole','10002'),('10420','0','35','projectrole','10001'),('10421','0','37','projectrole','10001'),('10422','0','37','projectrole','10002'),('10423','0','19','projectrole','10002'),('10424','0','19','projectrole','10001'),('10425','0','39','projectrole','10002'),('10426','0','39','projectrole','10001'),('10427','0','20','projectrole','10002'),('10428','0','40','projectrole','10002'),('10429','0','40','projectrole','10001'),('10430','0','41','projectrole','10002'),('10431','0','42','projectrole','10002'),('10432','0','42','projectrole','10001');
/*!40000 ALTER TABLE `schemepermissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `searchrequest`
--

DROP TABLE IF EXISTS `searchrequest`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `searchrequest` (
  `ID` decimal(18,0) NOT NULL,
  `filtername` varchar(255) collate utf8_bin default NULL,
  `authorname` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  `username` varchar(255) collate utf8_bin default NULL,
  `groupname` varchar(255) collate utf8_bin default NULL,
  `projectid` decimal(18,0) default NULL,
  `reqcontent` longtext collate utf8_bin,
  `FAV_COUNT` decimal(18,0) default NULL,
  `filtername_lower` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `sr_author` (`authorname`),
  KEY `searchrequest_filternameLower` (`filtername_lower`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `searchrequest`
--

LOCK TABLES `searchrequest` WRITE;
/*!40000 ALTER TABLE `searchrequest` DISABLE KEYS */;
/*!40000 ALTER TABLE `searchrequest` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `serviceconfig`
--

DROP TABLE IF EXISTS `serviceconfig`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `serviceconfig` (
  `ID` decimal(18,0) NOT NULL,
  `delaytime` decimal(18,0) default NULL,
  `CLAZZ` varchar(255) collate utf8_bin default NULL,
  `servicename` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `serviceconfig`
--

LOCK TABLES `serviceconfig` WRITE;
/*!40000 ALTER TABLE `serviceconfig` DISABLE KEYS */;
INSERT INTO `serviceconfig` VALUES ('10000','60000','com.atlassian.jira.service.services.mail.MailQueueService','Mail Queue Service'),('10001','43200000','com.atlassian.jira.service.services.export.ExportService','Service de sauvegarde'),('10400','28799999','com.atlassian.sal.jira.scheduling.JiraPluginSchedulerService','Service Provider Session Remover'),('10401','60000','com.atlassian.sal.jira.scheduling.JiraPluginSchedulerService','com.atlassian.jira.plugin.ext.bamboo.service.PlanStatusUpdateServiceImpl:job'),('10402','86400000','com.atlassian.sal.jira.scheduling.JiraPluginSchedulerService','LocalPluginLicenseNotificationPluginJob-job'),('10403','3600000','com.atlassian.sal.jira.scheduling.JiraPluginSchedulerService','RemotePluginLicenseNotificationPluginJob-job'),('10404','3600000','com.atlassian.sal.jira.scheduling.JiraPluginSchedulerService','PluginRequestCheckPluginJob-job'),('10405','58206421','com.atlassian.sal.jira.scheduling.JiraPluginSchedulerService','PluginUpdateCheckPluginJob-job'),('10406','14400000','com.atlassian.sal.jira.scheduling.JiraPluginSchedulerService','AddOnLicensingEmailNotificationsPluginJob-job'),('10407','899999','com.atlassian.sal.jira.scheduling.JiraPluginSchedulerService','com.atlassian.streams.internal.ActivityProviderConnectionMonitorImpl:activityProviderMonitor'),('10408','2286173','com.atlassian.sal.jira.scheduling.JiraPluginSchedulerService','com.atlassian.jira.plugins.dvcs.scheduler.DvcsScheduler:job');
/*!40000 ALTER TABLE `serviceconfig` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sharepermissions`
--

DROP TABLE IF EXISTS `sharepermissions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `sharepermissions` (
  `ID` decimal(18,0) NOT NULL,
  `entityid` decimal(18,0) default NULL,
  `entitytype` varchar(60) collate utf8_bin default NULL,
  `sharetype` varchar(10) collate utf8_bin default NULL,
  `PARAM1` varchar(255) collate utf8_bin default NULL,
  `PARAM2` varchar(60) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `share_index` (`entityid`,`entitytype`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sharepermissions`
--

LOCK TABLES `sharepermissions` WRITE;
/*!40000 ALTER TABLE `sharepermissions` DISABLE KEYS */;
INSERT INTO `sharepermissions` VALUES ('10000','10000','PortalPage','global',NULL,NULL);
/*!40000 ALTER TABLE `sharepermissions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trackback_ping`
--

DROP TABLE IF EXISTS `trackback_ping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trackback_ping` (
  `ID` decimal(18,0) NOT NULL,
  `ISSUE` decimal(18,0) default NULL,
  `URL` varchar(255) collate utf8_bin default NULL,
  `TITLE` varchar(255) collate utf8_bin default NULL,
  `BLOGNAME` varchar(255) collate utf8_bin default NULL,
  `EXCERPT` varchar(255) collate utf8_bin default NULL,
  `CREATED` datetime default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trackback_ping`
--

LOCK TABLES `trackback_ping` WRITE;
/*!40000 ALTER TABLE `trackback_ping` DISABLE KEYS */;
/*!40000 ALTER TABLE `trackback_ping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `trustedapp`
--

DROP TABLE IF EXISTS `trustedapp`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `trustedapp` (
  `ID` decimal(18,0) NOT NULL,
  `APPLICATION_ID` varchar(255) collate utf8_bin default NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `PUBLIC_KEY` text collate utf8_bin,
  `IP_MATCH` text collate utf8_bin,
  `URL_MATCH` text collate utf8_bin,
  `TIMEOUT` decimal(18,0) default NULL,
  `CREATED` datetime default NULL,
  `CREATED_BY` varchar(255) collate utf8_bin default NULL,
  `UPDATED` datetime default NULL,
  `UPDATED_BY` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `trustedapp_id` (`APPLICATION_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `trustedapp`
--

LOCK TABLES `trustedapp` WRITE;
/*!40000 ALTER TABLE `trustedapp` DISABLE KEYS */;
/*!40000 ALTER TABLE `trustedapp` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upgradehistory`
--

DROP TABLE IF EXISTS `upgradehistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upgradehistory` (
  `ID` decimal(18,0) default NULL,
  `UPGRADECLASS` varchar(255) collate utf8_bin NOT NULL,
  `TARGETBUILD` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`UPGRADECLASS`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upgradehistory`
--

LOCK TABLES `upgradehistory` WRITE;
/*!40000 ALTER TABLE `upgradehistory` DISABLE KEYS */;
INSERT INTO `upgradehistory` VALUES ('10000','com.atlassian.jira.upgrade.tasks.UpgradeTask1_2','6106'),('10002','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build10','6106'),('10101','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build101','6106'),('10102','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build102','6106'),('10003','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build11','6106'),('10103','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build130','6106'),('10104','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build132','6106'),('10105','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build150','6106'),('10106','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build151','6106'),('10107','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build152','6106'),('10108','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build175','6106'),('10109','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build176','6106'),('10110','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build205','6106'),('10111','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build207','6106'),('10112','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build257','6106'),('10113','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build258','6106'),('10004','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build27','6106'),('10114','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build296','6106'),('10115','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build317','6106'),('10116','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build325','6106'),('10005','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build35','6106'),('10117','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build412','6106'),('10118','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build418','6106'),('10119','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build437','6106'),('10120','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build438','6106'),('10006','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build47','6106'),('10007','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build48','6106'),('10008','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build51','6106'),('10121','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build520','6106'),('10122','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build551','6106'),('10123','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build554','6106'),('10009','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build56','6106'),('10124','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build572','6106'),('10010','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build60','6106'),('10212','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build6001','6106'),('10213','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build6005','6106'),('10214','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build6006','6106'),('10125','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build601','6106'),('10200','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build602','6106'),('10215','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build6040','6106'),('10201','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build608','6106'),('10216','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build6083','6106'),('10202','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build611','6106'),('10203','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build633','6106'),('10204','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build634','6106'),('10205','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build637','6106'),('10206','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build640','6106'),('10207','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build641','6106'),('10208','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build642','6106'),('10209','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build643','6106'),('10100','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build83','6106'),('10210','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build843','6106'),('10211','com.atlassian.jira.upgrade.tasks.UpgradeTask_Build849','6106'),('10001','com.atlassian.jira.upgrade.tasks.professional.UpgradeTask1_2_1','6106');
/*!40000 ALTER TABLE `upgradehistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `upgradeversionhistory`
--

DROP TABLE IF EXISTS `upgradeversionhistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `upgradeversionhistory` (
  `ID` decimal(18,0) default NULL,
  `TIMEPERFORMED` datetime default NULL,
  `TARGETBUILD` varchar(255) collate utf8_bin NOT NULL,
  `TARGETVERSION` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`TARGETBUILD`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `upgradeversionhistory`
--

LOCK TABLES `upgradeversionhistory` WRITE;
/*!40000 ALTER TABLE `upgradeversionhistory` DISABLE KEYS */;
INSERT INTO `upgradeversionhistory` VALUES ('10000','2013-08-27 16:59:35','6106','6.0.7');
/*!40000 ALTER TABLE `upgradeversionhistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userassociation`
--

DROP TABLE IF EXISTS `userassociation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userassociation` (
  `SOURCE_NAME` varchar(60) collate utf8_bin NOT NULL,
  `SINK_NODE_ID` decimal(18,0) NOT NULL,
  `SINK_NODE_ENTITY` varchar(60) collate utf8_bin NOT NULL,
  `ASSOCIATION_TYPE` varchar(60) collate utf8_bin NOT NULL,
  `SEQUENCE` decimal(9,0) default NULL,
  `CREATED` datetime default NULL,
  PRIMARY KEY  (`SOURCE_NAME`,`SINK_NODE_ID`,`SINK_NODE_ENTITY`,`ASSOCIATION_TYPE`),
  KEY `user_source` (`SOURCE_NAME`),
  KEY `user_sink` (`SINK_NODE_ID`,`SINK_NODE_ENTITY`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userassociation`
--

LOCK TABLES `userassociation` WRITE;
/*!40000 ALTER TABLE `userassociation` DISABLE KEYS */;
/*!40000 ALTER TABLE `userassociation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userbase`
--

DROP TABLE IF EXISTS `userbase`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userbase` (
  `ID` decimal(18,0) NOT NULL,
  `username` varchar(255) collate utf8_bin default NULL,
  `PASSWORD_HASH` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `osuser_name` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userbase`
--

LOCK TABLES `userbase` WRITE;
/*!40000 ALTER TABLE `userbase` DISABLE KEYS */;
/*!40000 ALTER TABLE `userbase` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userhistoryitem`
--

DROP TABLE IF EXISTS `userhistoryitem`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `userhistoryitem` (
  `ID` decimal(18,0) NOT NULL,
  `entitytype` varchar(10) collate utf8_bin default NULL,
  `entityid` varchar(60) collate utf8_bin default NULL,
  `USERNAME` varchar(255) collate utf8_bin default NULL,
  `lastviewed` decimal(18,0) default NULL,
  `data` longtext collate utf8_bin,
  PRIMARY KEY  (`ID`),
  UNIQUE KEY `uh_type_user_entity` (`entitytype`,`USERNAME`,`entityid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userhistoryitem`
--

LOCK TABLES `userhistoryitem` WRITE;
/*!40000 ALTER TABLE `userhistoryitem` DISABLE KEYS */;
INSERT INTO `userhistoryitem` VALUES ('10000','Dashboard','10000','administrator','1378882845121',NULL);
/*!40000 ALTER TABLE `userhistoryitem` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `versioncontrol`
--

DROP TABLE IF EXISTS `versioncontrol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `versioncontrol` (
  `ID` decimal(18,0) NOT NULL,
  `vcsname` varchar(255) collate utf8_bin default NULL,
  `vcsdescription` varchar(255) collate utf8_bin default NULL,
  `vcstype` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `versioncontrol`
--

LOCK TABLES `versioncontrol` WRITE;
/*!40000 ALTER TABLE `versioncontrol` DISABLE KEYS */;
/*!40000 ALTER TABLE `versioncontrol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `votehistory`
--

DROP TABLE IF EXISTS `votehistory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `votehistory` (
  `ID` decimal(18,0) NOT NULL,
  `issueid` decimal(18,0) default NULL,
  `VOTES` decimal(18,0) default NULL,
  `TIMESTAMP` datetime default NULL,
  PRIMARY KEY  (`ID`),
  KEY `votehistory_issue_index` (`issueid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `votehistory`
--

LOCK TABLES `votehistory` WRITE;
/*!40000 ALTER TABLE `votehistory` DISABLE KEYS */;
/*!40000 ALTER TABLE `votehistory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowscheme`
--

DROP TABLE IF EXISTS `workflowscheme`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowscheme` (
  `ID` decimal(18,0) NOT NULL,
  `NAME` varchar(255) collate utf8_bin default NULL,
  `DESCRIPTION` text collate utf8_bin,
  PRIMARY KEY  (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowscheme`
--

LOCK TABLES `workflowscheme` WRITE;
/*!40000 ALTER TABLE `workflowscheme` DISABLE KEYS */;
/*!40000 ALTER TABLE `workflowscheme` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflowschemeentity`
--

DROP TABLE IF EXISTS `workflowschemeentity`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflowschemeentity` (
  `ID` decimal(18,0) NOT NULL,
  `SCHEME` decimal(18,0) default NULL,
  `WORKFLOW` varchar(255) collate utf8_bin default NULL,
  `issuetype` varchar(255) collate utf8_bin default NULL,
  PRIMARY KEY  (`ID`),
  KEY `workflow_scheme` (`SCHEME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflowschemeentity`
--

LOCK TABLES `workflowschemeentity` WRITE;
/*!40000 ALTER TABLE `workflowschemeentity` DISABLE KEYS */;
/*!40000 ALTER TABLE `workflowschemeentity` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `worklog`
--

DROP TABLE IF EXISTS `worklog`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `worklog` (
  `ID` decimal(18,0) NOT NULL,
  `issueid` decimal(18,0) default NULL,
  `AUTHOR` varchar(255) collate utf8_bin default NULL,
  `grouplevel` varchar(255) collate utf8_bin default NULL,
  `rolelevel` decimal(18,0) default NULL,
  `worklogbody` longtext collate utf8_bin,
  `CREATED` datetime default NULL,
  `UPDATEAUTHOR` varchar(255) collate utf8_bin default NULL,
  `UPDATED` datetime default NULL,
  `STARTDATE` datetime default NULL,
  `timeworked` decimal(18,0) default NULL,
  PRIMARY KEY  (`ID`),
  KEY `worklog_issue` (`issueid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `worklog`
--

LOCK TABLES `worklog` WRITE;
/*!40000 ALTER TABLE `worklog` DISABLE KEYS */;
/*!40000 ALTER TABLE `worklog` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2013-09-11  9:07:52
