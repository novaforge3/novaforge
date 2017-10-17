use @DATABASE@;
--
-- Table structure for table sessions
--
-- id : length set to 50 (previous = 32) to store session_id equals to phpCAS ticket
ALTER TABLE lime_sessions MODIFY id varchar(50) NOT NULL;

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
