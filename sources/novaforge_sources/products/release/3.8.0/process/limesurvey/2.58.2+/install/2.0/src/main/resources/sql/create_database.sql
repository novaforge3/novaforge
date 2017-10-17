DROP DATABASE IF EXISTS `@DATABASE@`;
CREATE USER '@LIMESURVEY_USER@'@'@MARIADB_HOST@' IDENTIFIED BY  '@LIMESURVEY_PWD@';
CREATE DATABASE  `@DATABASE@` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
GRANT ALL PRIVILEGES ON  `@DATABASE@` . * TO  '@LIMESURVEY_USER@'@'@MARIADB_HOST@' WITH GRANT OPTION ;

use @DATABASE@;
--
-- Table structure for table answers
--
CREATE TABLE `lime_answers` (
  `qid` int(11) NOT NULL default '0',
  `code` varchar(5) NOT NULL default '',
  `answer` text NOT NULL,
  `sortorder` int(11) NOT NULL,
  `assessment_value` int(11) NOT NULL default '0',
  `language` varchar(20) default 'en',
  `scale_id` int(11) NOT NULL default '0',
  PRIMARY KEY (`qid`,`code`,`language`,`scale_id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table assessments
--
CREATE TABLE `lime_assessments` (
  `id` int(11) NOT NULL auto_increment,
  `sid` int(11) NOT NULL default '0',
  `scope` varchar(5) NOT NULL default '',
  `gid` int(11) NOT NULL default '0',
  `name` text NOT NULL,
  `minimum` varchar(50) NOT NULL default '',
  `maximum` varchar(50) NOT NULL default '',
  `message` text NOT NULL,
  `language` varchar(20) NOT NULL default 'en',
  PRIMARY KEY (`id`,`language`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table conditions
--
CREATE TABLE `lime_conditions` (
  `cid` int(11) NOT NULL auto_increment,
  `qid` int(11) NOT NULL default '0',
  `cqid` int(11) NOT NULL default '0',
  `cfieldname` varchar(50) NOT NULL default '',
  `method` varchar(5) NOT NULL default '',
  `value` varchar(255) NOT NULL default '',
  `scenario` int(11) NOT NULL default '1',
  PRIMARY KEY (`cid`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table defaultvalues
--
CREATE TABLE `lime_defaultvalues` (
  `qid` int(11) NOT NULL default '0',
  `scale_id` int(11) NOT NULL default '0',
  `sqid` int(11) NOT NULL default '0',
  `language` varchar(20) NOT NULL,
  `specialtype` varchar(20) NOT NULL default '',
  `defaultvalue` text,
  PRIMARY KEY (`qid`, `specialtype`, `language`, `scale_id`, `sqid`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table expression_errors
--
CREATE TABLE `lime_expression_errors` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `errortime` varchar(50) DEFAULT NULL,
  `sid` int(11) DEFAULT NULL,
  `gid` int(11) DEFAULT NULL,
  `qid` int(11) DEFAULT NULL,
  `gseq` int(11) DEFAULT NULL,
  `qseq` int(11) DEFAULT NULL,
  `type` varchar(50),
  `eqn` text,
  `prettyprint` text,
  PRIMARY KEY (`id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Create failed_login_attempts
--
CREATE TABLE `lime_failed_login_attempts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(40) NOT NULL,
  `last_attempt` varchar(20) NOT NULL,
  `number_attempts` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table groups
--
CREATE TABLE `lime_groups` (
  `gid` int(11) NOT NULL auto_increment,
  `sid` int(11) NOT NULL default '0',
  `group_name` varchar(100) NOT NULL default '',
  `group_order` int(11) NOT NULL default '0',
  `description` text,
  `language` varchar(20) default 'en',
  `randomization_group` varchar(20) NOT NULL default '',
  `grelevance` text DEFAULT NULL,
  PRIMARY KEY (`gid`,`language`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table labels
--
CREATE TABLE `lime_labels` (
  `lid` int(11) NOT NULL default '0',
  `code` varchar(5) NOT NULL default '',
  `title` text,
  `sortorder` int(11) NOT NULL,
  `language` varchar(20) default 'en',
  `assessment_value` int(11) NOT NULL default '0',
  PRIMARY KEY (`lid`,`sortorder`,`language`),
  KEY `labels_code_idx` (`code`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table labelsets
--
CREATE TABLE `lime_labelsets` (
  `lid` int(11) NOT NULL auto_increment,
  `label_name` varchar(100) NOT NULL default '',
  `languages` varchar(200) default 'en',
  PRIMARY KEY (`lid`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table participant_attribute
--
CREATE TABLE `lime_participant_attribute` (
  `participant_id` varchar(50) NOT NULL,
  `attribute_id` int(11) NOT NULL,
  `value` text NOT NULL,
  PRIMARY KEY (`participant_id`,`attribute_id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table participant_attribute_names_lang
--
CREATE TABLE `lime_participant_attribute_names_lang` (
  `attribute_id` int(11) NOT NULL,
  `attribute_name` varchar(30) NOT NULL,
  `lang` varchar(20) NOT NULL,
  PRIMARY KEY (`attribute_id`,`lang`)
 ) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table participant_attribute_names
--
CREATE TABLE `lime_participant_attribute_names` (
  `attribute_id` int(11) NOT NULL AUTO_INCREMENT,
  `attribute_type` varchar(4) NOT NULL,
  `defaultname` varchar(50) NOT NULL,
  `visible` varchar(5) NOT NULL,
  PRIMARY KEY (`attribute_id`,`attribute_type`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table participant_attribute_values
--
CREATE TABLE `lime_participant_attribute_values` (
  `value_id` int(11) NOT NULL AUTO_INCREMENT,
  `attribute_id` int(11) NOT NULL,
  `value` text NOT NULL,
  PRIMARY KEY (`value_id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table participant_shares
--
CREATE TABLE `lime_participant_shares` (
  `participant_id` varchar(50) NOT NULL,
  `share_uid` int(11) NOT NULL,
  `date_added` datetime NOT NULL,
  `can_edit` varchar(5) NOT NULL,
  PRIMARY KEY (`participant_id`,`share_uid`)
 ) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table participants
--
CREATE TABLE `lime_participants` (
  `participant_id` varchar(50) NOT NULL,
  `firstname` varchar(150) DEFAULT NULL,
  `lastname` varchar(150) DEFAULT NULL,
  `email` text,
  `language` varchar(40) DEFAULT NULL,
  `blacklisted` varchar(1) NOT NULL,
  `owner_uid` int(11) NOT NULL,
  `created_by` int(11) NOT NULL,
  `created` datetime,
  `modified` datetime,
  PRIMARY KEY (`participant_id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table permissions
--
CREATE TABLE `lime_permissions` (
  `id` int(11) NOT NULL auto_increment,
  `entity` varchar(50) NOT NULL,
  `entity_id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `permission` varchar(100) NOT NULL,
  `create_p` int(11) NOT NULL default '0',
  `read_p` int(11) NOT NULL default '0',
  `update_p` int(11) NOT NULL default '0',
  `delete_p` int(11) NOT NULL default '0',
  `import_p` int(11) NOT NULL default '0',
  `export_p` int(11) NOT NULL default '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idxPermissions` (`entity_id`,`entity`,`permission`,`uid`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table plugins
--
CREATE TABLE `lime_plugins` (
  `id` int(11) NOT NULL auto_increment,
  `name` varchar(50) NOT NULL,
  `active` int(1) NOT NULL default '0',
  PRIMARY KEY (`id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table plugin_settings
--
CREATE TABLE `lime_plugin_settings` (
  `id` int(11) NOT NULL auto_increment,
  `plugin_id` int(11) NOT NULL,
  `model` varchar(50) NULL,
  `model_id` int(11) NULL,
  `key` varchar(50) NOT NULL,
  `value` text NULL,
  PRIMARY KEY (`id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table question_attributes
--
CREATE TABLE `lime_question_attributes` (
  `qaid` int(11) NOT NULL auto_increment,
  `qid` int(11) NOT NULL default '0',
  `attribute` varchar(50) default NULL,
  `value` text default NULL,
  `language` varchar(20) default NULL,
  PRIMARY KEY (`qaid`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table questions
--
CREATE TABLE `lime_questions` (
  `qid` int(11) NOT NULL auto_increment,
  `parent_qid` int(11) NOT NULL default '0',
  `sid` int(11) NOT NULL default '0',
  `gid` int(11) NOT NULL default '0',
  `type` varchar(1) NOT NULL default 'T',
  `title` varchar(20) NOT NULL default '',
  `question` text NOT NULL,
  `preg` text,
  `help` text,
  `other` varchar(1) NOT NULL default 'N',
  `mandatory` varchar(1) default NULL,
  `question_order` int(11) NOT NULL,
  `language` varchar(20) default 'en',
  `scale_id` int(11) NOT NULL default '0',
  `same_default` int(11) NOT NULL default '0' COMMENT 'Saves if user set to use the same default value across languages in default options dialog',
  `relevance` text,
  `modulename` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`qid`,`language`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table quota
--
CREATE TABLE `lime_quota` (
  `id` int(11) NOT NULL auto_increment,
  `sid` int(11) default NULL,
  `name` varchar(255) default NULL,
  `qlimit` int(11) default NULL,
  `action` int(11) default NULL,
  `active` int(11) NOT NULL default '1',
  `autoload_url` int(11) NOT NULL default '0',
  PRIMARY KEY (`id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table quota_languagesettings
--
CREATE TABLE `lime_quota_languagesettings` (
  `quotals_id` int(11) NOT NULL auto_increment,
  `quotals_quota_id` int(11) NOT NULL default '0',
  `quotals_language` varchar(45) NOT NULL default 'en',
  `quotals_name` varchar(255) default NULL,
  `quotals_message` text NOT NULL,
  `quotals_url` varchar(255),
  `quotals_urldescrip` varchar(255),
  PRIMARY KEY (`quotals_id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table quota_members
--
CREATE TABLE `lime_quota_members` (
  `id` int(11) NOT NULL auto_increment,
  `sid` int(11) default NULL,
  `qid` int(11) default NULL,
  `quota_id` int(11) default NULL,
  `code` varchar(11) default NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `sid` (`sid`,`qid`,`quota_id`,`code`)
)  ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table saved_control
--
CREATE TABLE `lime_saved_control` (
  `scid` int(11) NOT NULL auto_increment,
  `sid` int(11) NOT NULL default '0',
  `srid` int(11) NOT NULL default '0',
  `identifier` text NOT NULL,
  `access_code` text NOT NULL,
  `email` varchar(254),
  `ip` text NOT NULL,
  `saved_thisstep` text NOT NULL,
  `status` varchar(1) NOT NULL default '',
  `saved_date` datetime NOT NULL,
  `refurl` text,
  PRIMARY KEY (`scid`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table sessions
--
-- id : length set to 50 (previous = 32) to store session_id equals to phpCAS ticket
CREATE TABLE `lime_sessions`(
  `id` varchar(50) NOT NULL,
  `expire` int(11) DEFAULT NULL,
  `data` longblob,
  PRIMARY KEY (`id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table settings_global
--
CREATE TABLE `lime_settings_global` (
  `stg_name` varchar(50) NOT NULL default '',
  `stg_value` varchar(255) NOT NULL default '',
  PRIMARY KEY (`stg_name`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table survey_links
--
CREATE TABLE `lime_survey_links` (
  `participant_id` varchar(50) NOT NULL,
  `token_id` int(11) NOT NULL,
  `survey_id` int(11) NOT NULL,
  `date_created` datetime,
  `date_invited` datetime,
  `date_completed` datetime,
  PRIMARY KEY (`participant_id`,`token_id`,`survey_id`)
 ) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table survey_url_parameters
--
CREATE TABLE `lime_survey_url_parameters` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sid` int(11) NOT NULL,
  `parameter` varchar(50) NOT NULL,
  `targetqid` int(11) NULL,
  `targetsqid` int(11) NULL,
  PRIMARY KEY (`id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table surveys
--
CREATE TABLE `lime_surveys` (
  `sid` int(11) NOT NULL,
  `owner_id` int(11) NOT NULL,
  `admin` varchar(50) default NULL,
  `active` varchar(1) NOT NULL default 'N',
  `expires` datetime default NULL,
  `startdate` datetime default NULL,
  `adminemail` varchar(254) default NULL,
  `anonymized` varchar(1) NOT NULL default 'N',
  `faxto` varchar(20) default NULL,
  `format` varchar(1) default NULL,
  `savetimings` varchar(1) NOT NULL default 'N',
  `template` varchar(100) default 'default',
  `language` varchar(50) default NULL,
  `additional_languages` varchar(255) default NULL,
  `datestamp` varchar(1) NOT NULL default 'N',
  `usecookie` varchar(1) NOT NULL default 'N',
  `allowregister` varchar(1) NOT NULL default 'N',
  `allowsave` varchar(1) NOT NULL default 'Y',
  `autonumber_start` int(11) NOT NULL default '0',
  `autoredirect` varchar(1) NOT NULL default 'N',
  `allowprev` varchar(1) NOT NULL default 'N',
  `printanswers` varchar(1) NOT NULL default 'N',
  `ipaddr` varchar(1) NOT NULL default 'N',
  `refurl` varchar(1) NOT NULL default 'N',
  `datecreated` date default NULL,
  `publicstatistics` varchar(1) NOT NULL default 'N',
  `publicgraphs` varchar(1) NOT NULL default 'N',
  `listpublic` varchar(1) NOT NULL default 'N',
  `htmlemail` varchar(1) NOT NULL default 'N',
  `sendconfirmation` varchar(1) NOT NULL default 'Y',
  `tokenanswerspersistence` varchar(1) NOT NULL default 'N',
  `assessments` varchar(1) NOT NULL default 'N',
  `usecaptcha` varchar(1) NOT NULL default 'N',
  `usetokens` varchar(1) NOT NULL default 'N',
  `bounce_email` varchar(254) default NULL,
  `attributedescriptions` text,
  `emailresponseto` text default NULL,
  `emailnotificationto` text default NULL,
  `tokenlength` int(11) NOT NULL default '15',
  `showxquestions` varchar(1) default 'Y',
  `showgroupinfo` varchar(1) default 'B',
  `shownoanswer` varchar(1) default 'Y',
  `showqnumcode` varchar(1) default 'X',
  `bouncetime` int(11),
  `bounceprocessing` varchar(1) default 'N',
  `bounceaccounttype` varchar(4),
  `bounceaccounthost` varchar(200),
  `bounceaccountpass` varchar(100),
  `bounceaccountencryption` varchar(3),
  `bounceaccountuser` varchar(200),
  `showwelcome` varchar(1) default 'Y',
  `showprogress` varchar(1) default 'Y',
  `questionindex` int(11) default '0' NOT NULL,
  `navigationdelay` int(11) NOT NULL default '0',
  `nokeyboard` varchar(1) default 'N',
  `alloweditaftercompletion` varchar(1) default 'N',
  `googleanalyticsstyle` varchar(1) DEFAULT NULL,
  `googleanalyticsapikey` VARCHAR(25) DEFAULT NULL,
  PRIMARY KEY (`sid`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table surveys_languagesettings
--
CREATE TABLE `lime_surveys_languagesettings` (
  `surveyls_survey_id` int(11) NOT NULL,
  `surveyls_language` varchar(45) NOT NULL DEFAULT 'en',
  `surveyls_title` varchar(200) NOT NULL,
  `surveyls_description` TEXT NULL,
  `surveyls_welcometext` TEXT NULL,
  `surveyls_endtext` TEXT NULL,
  `surveyls_url` TEXT NULL,
  `surveyls_urldescription` varchar(255) NULL,
  `surveyls_email_invite_subj` varchar(255) NULL,
  `surveyls_email_invite` TEXT NULL,
  `surveyls_email_remind_subj` varchar(255) NULL,
  `surveyls_email_remind` TEXT NULL,
  `surveyls_email_register_subj` varchar(255) NULL,
  `surveyls_email_register` TEXT NULL,
  `surveyls_email_confirm_subj` varchar(255) NULL,
  `surveyls_email_confirm` TEXT NULL,
  `surveyls_dateformat` int(11) NOT NULL DEFAULT 1,
  `surveyls_attributecaptions` TEXT NULL,
  `email_admin_notification_subj` varchar(255) NULL,
  `email_admin_notification` TEXT NULL,
  `email_admin_responses_subj` varchar(255) NULL,
  `email_admin_responses` TEXT NULL,
  `surveyls_numberformat` INT NOT NULL DEFAULT 0,
  `attachments` text DEFAULT NULL,
  PRIMARY KEY (`surveyls_survey_id`, `surveyls_language`)
) ENGINE = MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table templates
--
CREATE TABLE `lime_templates` (
  `folder` varchar(50) NOT NULL,
  `creator` int(11) NOT NULL,
  PRIMARY KEY (`folder`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table user_groups
--
CREATE TABLE `lime_user_groups` (
  `ugid` int(11) NOT NULL auto_increment,
  `name` varchar(20) NOT NULL,
  `description` TEXT NOT NULL,
  `owner_id` int(11) NOT NULL,
  PRIMARY KEY (`ugid`),
  UNIQUE KEY `lug_name` (`name`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table user_in_groups
--
CREATE TABLE `lime_user_in_groups` (
  `ugid` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  PRIMARY KEY (`ugid`,`uid`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure for table users
--
CREATE TABLE `lime_users` (
  `uid` int(11) NOT NULL auto_increment,
  `users_name` varchar(64) NOT NULL default '',
  `password` BLOB NOT NULL,
  `full_name` varchar(50) NOT NULL,
  `parent_id` int(11) NOT NULL,
  `lang` varchar(20),
  `email` varchar(254),
  `htmleditormode` varchar(7) default 'default',
  `templateeditormode` varchar(7) NOT NULL default 'default',
  `questionselectormode` varchar(7) NOT NULL default 'default',
  `one_time_pw` BLOB,
  `dateformat` INT(11) NOT NULL DEFAULT 1,
  `created` datetime,
  `modified` datetime,
  PRIMARY KEY (`uid`),
  UNIQUE KEY `users_name` (`users_name`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


--
-- Table structure & data for table boxes
--
CREATE TABLE IF NOT EXISTS `lime_boxes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `position` int(11) DEFAULT NULL COMMENT 'position of the box',
  `url` text NOT NULL COMMENT 'URL the box points',
  `title` text NOT NULL COMMENT 'Box title',
  `ico` varchar(255) DEFAULT NULL COMMENT 'the ico name in font',
  `desc` text NOT NULL COMMENT 'Box description',
  `page` text NOT NULL COMMENT 'Page name where the box should be shown ',
  `usergroup` INT(11) NOT NULL COMMENT  'Those boxes will be shown for that user group',
  PRIMARY KEY (`id`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


INSERT INTO `lime_boxes` (`id`, `position`, `url`, `title`, `ico`, `desc`, `page`,`usergroup` ) VALUES
(1, 1, 'admin/survey/sa/newsurvey', 'Create survey', 'add', 'Create a new survey', 'welcome', '-2'),
(2, 2, 'admin/survey/sa/listsurveys', 'List surveys', 'list', 'List available surveys', 'welcome', '-1'),
(3, 3, 'admin/globalsettings', 'Global settings', 'settings', 'Edit global settings', 'welcome', '-2'),
(4, 4, 'admin/update', 'ComfortUpdate', 'shield', 'Stay safe and up to date', 'welcome', '-2'),
(5, 5, 'admin/labels/sa/view', 'Label sets', 'label', 'Edit label sets', 'welcome', '-2'),
(6, 6, 'admin/templates/sa/view', 'Template editor', 'templates', 'Edit LimeSurvey templates', 'welcome', '-2');
--
-- Secondary indexes
--
CREATE INDEX `answers_idx2` ON `lime_answers` (`sortorder`);
CREATE INDEX `assessments_idx2` ON `lime_assessments` (`sid`);
CREATE INDEX `assessments_idx3` ON `lime_assessments` (`gid`);
CREATE INDEX `conditions_idx2` ON `lime_conditions` (`qid`);
CREATE INDEX `conditions_idx3` ON `lime_conditions` (`cqid`);
CREATE INDEX `groups_idx2` ON `lime_groups` (`sid`);
CREATE INDEX `question_attributes_idx2` ON `lime_question_attributes` (`qid`);
CREATE INDEX `question_attributes_idx3` ON `lime_question_attributes` (`attribute`);
CREATE INDEX `questions_idx2` ON `lime_questions` (`sid`);
CREATE INDEX `questions_idx3` ON `lime_questions` (`gid`);
CREATE INDEX `questions_idx4` ON `lime_questions` (`type`);
CREATE INDEX `saved_control_idx2` ON `lime_saved_control` (`sid`);
CREATE INDEX `quota_idx2` ON `lime_quota` (`sid`);
CREATE INDEX `parent_qid_idx` ON `lime_questions` (`parent_qid`);

--
-- Notification table
--
CREATE TABLE IF NOT EXISTS `lime_notifications` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `entity` VARCHAR(15) NOT NULL COMMENT 'Should be either survey or user',
    `entity_id` INT(11) NOT NULL,
    `title` VARCHAR(255) NOT NULL,
    `message` TEXT NOT NULL,
    `status` VARCHAR(15) NOT NULL DEFAULT 'new' COMMENT 'new or read',
    `importance` INT(11) NOT NULL DEFAULT 1,
    `display_class` VARCHAR(31) DEFAULT 'default' COMMENT 'Bootstrap class, like warning, info, success',
    `created` DATETIME NOT NULL,
    `first_read` DATETIME DEFAULT NULL,
    PRIMARY KEY (`id`),
    INDEX(`entity`, `entity_id`, `status`)
) ENGINE=MYISAM CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

--
-- Version Info
--
INSERT INTO `lime_settings_global` VALUES ('DBVersion', '259');

--
-- Default Admin user & Global permissions
--
INSERT INTO `lime_users` (`users_name`, `password`, `full_name`, `parent_id`, `lang`, `email`)
VALUES ('admin', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', 'Administrator', 0, 'en', 'your-email@example.net');
INSERT INTO `lime_permissions` (`entity`, `entity_id`, `uid`, `permission`, `create_p`, `read_p`, `update_p`, `delete_p`, `import_p`, `export_p`)
VALUES ('global', 0, 1, 'superadmin', 0, 1, 0, 0, 0, 0);
--
-- CAS plugin
--
INSERT INTO `lime_plugins` (`name`, `active`)
VALUES ('AuthCAS', 1);
INSERT INTO `lime_plugin_settings` (`plugin_id`, `key`, `value`)
VALUES (1, 'casAuthServer', '"@CAS_HOST@"');
INSERT INTO `lime_plugin_settings` (`plugin_id`, `key`, `value`)
VALUES (1, 'casAuthPort', '"@CAS_PORT@"');
INSERT INTO `lime_plugin_settings` (`plugin_id`, `key`, `value`)
VALUES (1, 'casAuthUri', '"\@CAS_URI@\/"');
INSERT INTO `lime_plugin_settings` (`plugin_id`, `key`, `value`)
VALUES (1, 'casVersion', '"2.0"');
