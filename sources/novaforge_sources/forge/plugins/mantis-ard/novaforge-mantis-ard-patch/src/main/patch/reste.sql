use mantis_ard; 

CREATE TABLE mantis_config_table (
config_id                VARCHAR(64) NOT NULL,
project_id               INTEGER NOT NULL DEFAULT 0,
user_id                  INTEGER NOT NULL DEFAULT 0,
access_reqd              INTEGER DEFAULT 0,
type                     INTEGER DEFAULT 90,
value                    LONGTEXT NOT NULL,
                 PRIMARY KEY (config_id, project_id, user_id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_config_table ADD  INDEX idx_config  (config_id);

CREATE TABLE mantis_bug_file_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
bug_id                   INTEGER UNSIGNED NOT NULL DEFAULT 0,
title                    VARCHAR(250) NOT NULL DEFAULT '',
description              VARCHAR(250) NOT NULL DEFAULT '',
diskfile                 VARCHAR(250) NOT NULL DEFAULT '',
filename                 VARCHAR(250) NOT NULL DEFAULT '',
folder                   VARCHAR(250) NOT NULL DEFAULT '',
filesize                 INTEGER NOT NULL DEFAULT 0,
file_type                VARCHAR(250) NOT NULL DEFAULT '',
date_added               DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
content                  LONGBLOB NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_bug_file_table ADD  INDEX idx_bug_file_bug_id  (bug_id);

CREATE TABLE mantis_bug_history_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
bug_id                   INTEGER UNSIGNED NOT NULL DEFAULT 0,
date_modified            DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
field_name               VARCHAR(32) NOT NULL DEFAULT '',
old_value                VARCHAR(128) NOT NULL DEFAULT '',
new_value                VARCHAR(128) NOT NULL DEFAULT '',
type                     SMALLINT NOT NULL DEFAULT 0,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_bug_history_table ADD  INDEX idx_bug_history_bug_id  (bug_id);

ALTER TABLE mantis_bug_history_table ADD  INDEX idx_history_user_id  (user_id);

CREATE TABLE mantis_bug_monitor_table (
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
bug_id                   INTEGER UNSIGNED NOT NULL DEFAULT 0,
                 PRIMARY KEY (user_id, bug_id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_bug_relationship_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
source_bug_id            INTEGER UNSIGNED NOT NULL DEFAULT 0,
destination_bug_id       INTEGER UNSIGNED NOT NULL DEFAULT 0,
relationship_type        SMALLINT NOT NULL DEFAULT 0,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_bug_relationship_table ADD  INDEX idx_relationship_source  (source_bug_id);

ALTER TABLE mantis_bug_relationship_table ADD  INDEX idx_relationship_destination  (destination_bug_id);

CREATE TABLE mantis_bug_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
project_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
reporter_id              INTEGER UNSIGNED NOT NULL DEFAULT 0,
handler_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
duplicate_id             INTEGER UNSIGNED NOT NULL DEFAULT 0,
priority                 SMALLINT NOT NULL DEFAULT 30,
severity                 SMALLINT NOT NULL DEFAULT 50,
reproducibility          SMALLINT NOT NULL DEFAULT 10,
status                   SMALLINT NOT NULL DEFAULT 10,
resolution               SMALLINT NOT NULL DEFAULT 10,
projection               SMALLINT NOT NULL DEFAULT 10,
category                 VARCHAR(64) NOT NULL DEFAULT '',
date_submitted           DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
last_updated             DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
eta                      SMALLINT NOT NULL DEFAULT 10,
bug_text_id              INTEGER UNSIGNED NOT NULL DEFAULT 0,
os                       VARCHAR(32) NOT NULL DEFAULT '',
os_build                 VARCHAR(32) NOT NULL DEFAULT '',
platform                 VARCHAR(32) NOT NULL DEFAULT '',
version                  VARCHAR(64) NOT NULL DEFAULT '',
fixed_in_version         VARCHAR(64) NOT NULL DEFAULT '',
build                    VARCHAR(32) NOT NULL DEFAULT '',
profile_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
view_state               SMALLINT NOT NULL DEFAULT 10,
summary                  VARCHAR(128) NOT NULL DEFAULT '',
sponsorship_total        INTEGER NOT NULL DEFAULT 0,
sticky                   TINYINT NOT NULL DEFAULT '0',
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_bug_table ADD  INDEX idx_bug_sponsorship_total  (sponsorship_total);

ALTER TABLE mantis_bug_table ADD  INDEX idx_bug_fixed_in_version  (fixed_in_version);

ALTER TABLE mantis_bug_table ADD  INDEX idx_bug_status  (status);

ALTER TABLE mantis_bug_table ADD  INDEX idx_project  (project_id);

CREATE TABLE mantis_bug_text_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
description              LONGTEXT NOT NULL,
steps_to_reproduce       LONGTEXT NOT NULL,
additional_information   LONGTEXT NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_bugnote_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
bug_id                   INTEGER UNSIGNED NOT NULL DEFAULT 0,
reporter_id              INTEGER UNSIGNED NOT NULL DEFAULT 0,
bugnote_text_id          INTEGER UNSIGNED NOT NULL DEFAULT 0,
view_state               SMALLINT NOT NULL DEFAULT 10,
date_submitted           DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
last_modified            DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
note_type                INTEGER DEFAULT 0,
note_attr                VARCHAR(250) DEFAULT '',
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_bugnote_table ADD  INDEX idx_bug  (bug_id);

ALTER TABLE mantis_bugnote_table ADD  INDEX idx_last_mod  (last_modified);

CREATE TABLE mantis_bugnote_text_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
note                     LONGTEXT NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_custom_field_project_table (
field_id                 INTEGER NOT NULL DEFAULT 0,
project_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
sequence                 SMALLINT NOT NULL DEFAULT 0,
                 PRIMARY KEY (field_id, project_id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_custom_field_string_table (
field_id                 INTEGER NOT NULL DEFAULT 0,
bug_id                   INTEGER NOT NULL DEFAULT 0,
value                    VARCHAR(255) NOT NULL DEFAULT '',
                 PRIMARY KEY (field_id, bug_id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_custom_field_string_table ADD  INDEX idx_custom_field_bug  (bug_id);

CREATE TABLE mantis_custom_field_table (
id                       INTEGER NOT NULL AUTO_INCREMENT,
name                     VARCHAR(64) NOT NULL DEFAULT '',
type                     SMALLINT NOT NULL DEFAULT 0,
possible_values          VARCHAR(255) NOT NULL DEFAULT '',
default_value            VARCHAR(255) NOT NULL DEFAULT '',
valid_regexp             VARCHAR(255) NOT NULL DEFAULT '',
access_level_r           SMALLINT NOT NULL DEFAULT 0,
access_level_rw          SMALLINT NOT NULL DEFAULT 0,
length_min               INTEGER NOT NULL DEFAULT 0,
length_max               INTEGER NOT NULL DEFAULT 0,
advanced                 TINYINT NOT NULL DEFAULT '0',
require_report           TINYINT NOT NULL DEFAULT '0',
require_update           TINYINT NOT NULL DEFAULT '0',
display_report           TINYINT NOT NULL DEFAULT '0',
display_update           TINYINT NOT NULL DEFAULT '1',
require_resolved         TINYINT NOT NULL DEFAULT '0',
display_resolved         TINYINT NOT NULL DEFAULT '0',
display_closed           TINYINT NOT NULL DEFAULT '0',
require_closed           TINYINT NOT NULL DEFAULT '0',
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_custom_field_table ADD  INDEX idx_custom_field_name  (name);

CREATE TABLE mantis_filters_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
user_id                  INTEGER NOT NULL DEFAULT 0,
project_id               INTEGER NOT NULL DEFAULT 0,
is_public                TINYINT DEFAULT NULL,
name                     VARCHAR(64) NOT NULL DEFAULT '',
filter_string            LONGTEXT NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_news_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
project_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
poster_id                INTEGER UNSIGNED NOT NULL DEFAULT 0,
date_posted              DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
last_modified            DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
view_state               SMALLINT NOT NULL DEFAULT 10,
announcement             TINYINT NOT NULL DEFAULT '0',
headline                 VARCHAR(64) NOT NULL DEFAULT '',
body                     LONGTEXT NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_project_category_table (
project_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
category                 VARCHAR(64) NOT NULL DEFAULT '',
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
                 PRIMARY KEY (project_id, category)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_project_file_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
project_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
title                    VARCHAR(250) NOT NULL DEFAULT '',
description              VARCHAR(250) NOT NULL DEFAULT '',
diskfile                 VARCHAR(250) NOT NULL DEFAULT '',
filename                 VARCHAR(250) NOT NULL DEFAULT '',
folder                   VARCHAR(250) NOT NULL DEFAULT '',
filesize                 INTEGER NOT NULL DEFAULT 0,
file_type                VARCHAR(250) NOT NULL DEFAULT '',
date_added               DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
content                  LONGBLOB NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_project_hierarchy_table (
child_id                 INTEGER UNSIGNED NOT NULL,
parent_id                INTEGER UNSIGNED NOT NULL
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_project_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
name                     VARCHAR(128) NOT NULL DEFAULT '',
status                   SMALLINT NOT NULL DEFAULT 10,
enabled                  TINYINT NOT NULL DEFAULT '1',
view_state               SMALLINT NOT NULL DEFAULT 10,
access_min               SMALLINT NOT NULL DEFAULT 10,
file_path                VARCHAR(250) NOT NULL DEFAULT '',
description              LONGTEXT NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_project_table ADD  INDEX idx_project_id  (id);

ALTER TABLE mantis_project_table ADD  UNIQUE INDEX idx_project_name  (name);

ALTER TABLE mantis_project_table ADD  INDEX idx_project_view  (view_state);

CREATE TABLE mantis_project_user_list_table (
project_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
access_level             SMALLINT NOT NULL DEFAULT 10,
                 PRIMARY KEY (project_id, user_id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_project_user_list_table ADD  INDEX idx_project_user  (user_id);

CREATE TABLE mantis_project_version_table (
id                       INTEGER NOT NULL AUTO_INCREMENT,
project_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
version                  VARCHAR(64) NOT NULL DEFAULT '',
date_order               DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
description              LONGTEXT NOT NULL,
released                 TINYINT NOT NULL DEFAULT '1',
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_project_version_table ADD  UNIQUE INDEX idx_project_version  (project_id, version);

CREATE TABLE mantis_sponsorship_table (
id                       INTEGER NOT NULL AUTO_INCREMENT,
bug_id                   INTEGER NOT NULL DEFAULT 0,
user_id                  INTEGER NOT NULL DEFAULT 0,
amount                   INTEGER NOT NULL DEFAULT 0,
logo                     VARCHAR(128) NOT NULL DEFAULT '',
url                      VARCHAR(128) NOT NULL DEFAULT '',
paid                     TINYINT NOT NULL DEFAULT '0',
date_submitted           DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
last_updated             DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_sponsorship_table ADD  INDEX idx_sponsorship_bug_id  (bug_id);

ALTER TABLE mantis_sponsorship_table ADD  INDEX idx_sponsorship_user_id  (user_id);

CREATE TABLE mantis_tokens_table (
id                       INTEGER NOT NULL AUTO_INCREMENT,
owner                    INTEGER NOT NULL,
type                     INTEGER NOT NULL,
timestamp                DATETIME NOT NULL,
expiry                   DATETIME,
value                    LONGTEXT NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_user_pref_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
project_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
default_profile          INTEGER UNSIGNED NOT NULL DEFAULT 0,
default_project          INTEGER UNSIGNED NOT NULL DEFAULT 0,
advanced_report          TINYINT NOT NULL DEFAULT '0',
advanced_view            TINYINT NOT NULL DEFAULT '0',
advanced_update          TINYINT NOT NULL DEFAULT '0',
refresh_delay            INTEGER NOT NULL DEFAULT 0,
redirect_delay           TINYINT NOT NULL DEFAULT '0',
bugnote_order            VARCHAR(4) NOT NULL DEFAULT 'ASC',
email_on_new             TINYINT NOT NULL DEFAULT '0',
email_on_assigned        TINYINT NOT NULL DEFAULT '0',
email_on_feedback        TINYINT NOT NULL DEFAULT '0',
email_on_resolved        TINYINT NOT NULL DEFAULT '0',
email_on_closed          TINYINT NOT NULL DEFAULT '0',
email_on_reopened        TINYINT NOT NULL DEFAULT '0',
email_on_bugnote         TINYINT NOT NULL DEFAULT '0',
email_on_status          TINYINT NOT NULL DEFAULT '0',
email_on_priority        TINYINT NOT NULL DEFAULT '0',
email_on_priority_min_severity SMALLINT NOT NULL DEFAULT 10,
email_on_status_min_severity SMALLINT NOT NULL DEFAULT 10,
email_on_bugnote_min_severity SMALLINT NOT NULL DEFAULT 10,
email_on_reopened_min_severity SMALLINT NOT NULL DEFAULT 10,
email_on_closed_min_severity SMALLINT NOT NULL DEFAULT 10,
email_on_resolved_min_severity SMALLINT NOT NULL DEFAULT 10,
email_on_feedback_min_severity SMALLINT NOT NULL DEFAULT 10,
email_on_assigned_min_severity SMALLINT NOT NULL DEFAULT 10,
email_on_new_min_severity SMALLINT NOT NULL DEFAULT 10,
email_bugnote_limit      SMALLINT NOT NULL DEFAULT 0,
language                 VARCHAR(32) NOT NULL DEFAULT 'english',
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_user_print_pref_table (
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
print_pref               VARCHAR(27) NOT NULL DEFAULT '',
                 PRIMARY KEY (user_id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_user_profile_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
platform                 VARCHAR(32) NOT NULL DEFAULT '',
os                       VARCHAR(32) NOT NULL DEFAULT '',
os_build                 VARCHAR(32) NOT NULL DEFAULT '',
description              LONGTEXT NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_user_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
username                 VARCHAR(32) NOT NULL DEFAULT '',
realname                 VARCHAR(64) NOT NULL DEFAULT '',
email                    VARCHAR(64) NOT NULL DEFAULT '',
password                 VARCHAR(32) NOT NULL DEFAULT '',
date_created             DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
last_visit               DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
enabled                  TINYINT NOT NULL DEFAULT '1',
protected                TINYINT NOT NULL DEFAULT '0',
access_level             SMALLINT NOT NULL DEFAULT 10,
login_count              INTEGER NOT NULL DEFAULT 0,
lost_password_request_count SMALLINT NOT NULL DEFAULT 0,
failed_login_count       SMALLINT NOT NULL DEFAULT 0,
cookie_string            VARCHAR(64) NOT NULL DEFAULT '',
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_user_table ADD  UNIQUE INDEX idx_user_cookie_string  (cookie_string);

ALTER TABLE mantis_user_table ADD  UNIQUE INDEX idx_user_username  (username);

ALTER TABLE mantis_user_table ADD  INDEX idx_enable  (enabled);

ALTER TABLE mantis_user_table ADD  INDEX idx_access  (access_level);

INSERT INTO mantis_user_table(username, realname, email, password, date_created, last_visit, enabled, protected, access_level, login_count, lost_password_request_count, failed_login_count, cookie_string) VALUES
        ('administrator', '', 'root@localhost', '63a9f0ea7bb98050796b649e85481845', '2011-07-06 11:35:12', '2011-07-06 11:35:13', '1', '0', 90, 3, 0, 0, 'a30aeb6d3a7f71ec3a841816cb61b2d0b86b6ffd33a14bef3b00b6ea5c4838f7');

ALTER TABLE mantis_bug_history_table MODIFY COLUMN old_value VARCHAR(255) NOT NULL;

ALTER TABLE mantis_bug_history_table MODIFY COLUMN new_value VARCHAR(255) NOT NULL;

CREATE TABLE mantis_email_table (
email_id                 INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
email                    VARCHAR(64) NOT NULL DEFAULT '',
subject                  VARCHAR(250) NOT NULL DEFAULT '',
submitted                DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
metadata                 LONGTEXT NOT NULL,
body                     LONGTEXT NOT NULL,
                 PRIMARY KEY (email_id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_email_table ADD  INDEX idx_email_id  (email_id);

ALTER TABLE mantis_bug_table ADD target_version VARCHAR(64) NOT NULL DEFAULT '';

ALTER TABLE mantis_bugnote_table ADD time_tracking INTEGER UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE mantis_bug_file_table ADD  INDEX idx_diskfile  (diskfile);

ALTER TABLE mantis_user_print_pref_table MODIFY COLUMN print_pref VARCHAR(64) NOT NULL;

ALTER TABLE mantis_bug_history_table MODIFY COLUMN field_name VARCHAR(64) NOT NULL;

CREATE TABLE mantis_tag_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
name                     VARCHAR(100) NOT NULL DEFAULT '',
description              LONGTEXT NOT NULL,
date_created             DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
date_updated             DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
                 PRIMARY KEY (id, name)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_bug_tag_table (
bug_id                   INTEGER UNSIGNED NOT NULL DEFAULT 0,
tag_id                   INTEGER UNSIGNED NOT NULL DEFAULT 0,
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
date_attached            DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
                 PRIMARY KEY (bug_id, tag_id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_tokens_table ADD  INDEX idx_typeowner  (type, owner);

CREATE TABLE mantis_risk_criteria_table (
id 						INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
label 					VARCHAR(255) NOT NULL,
weight 					INTEGER UNSIGNED NOT NULL DEFAULT 0,
project_id 				INTEGER NOT NULL,
category_id 			INTEGER NOT NULL,
evaluation1 			VARCHAR(255) NOT NULL,
evaluation2				VARCHAR(255) NOT NULL,
evaluation3 			VARCHAR(255) NOT NULL,
evaluation4 			VARCHAR(255) NOT NULL,
evaluation5 			VARCHAR(255) NOT NULL DEFAULT '',
evaluation6 			VARCHAR(255) NOT NULL DEFAULT '',
evaluation 				TINYINT DEFAULT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_risk_criteria_table ADD  UNIQUE INDEX idx_label  (label);

CREATE TABLE mantis_risk_evaluation_history_table (
id 						INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
project_id 				INTEGER NOT NULL,
category_id 			INTEGER NOT NULL,
evaluation				FLOAT NOT NULL,
date_modified			DATE DEFAULT NULL,
  				PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

CREATE TABLE mantis_plugin_table (
basename                 VARCHAR(40) NOT NULL,
enabled                  TINYINT NOT NULL DEFAULT '0',
                 PRIMARY KEY (basename)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_user_pref_table MODIFY COLUMN redirect_delay INTEGER NOT NULL DEFAULT 0;

ALTER TABLE mantis_custom_field_table MODIFY COLUMN possible_values TEXT NOT NULL;

CREATE TABLE mantis_category_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
project_id               INTEGER NOT NULL DEFAULT 0,
user_id                  INTEGER UNSIGNED NOT NULL DEFAULT 0,
name                     VARCHAR(128) NOT NULL DEFAULT '',
status                   INTEGER UNSIGNED NOT NULL DEFAULT 0,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_category_table ADD  UNIQUE INDEX idx_category_project_name  (project_id, name);

INSERT INTO mantis_category_table
	( project_id, user_id, name, status ) VALUES
	( '0', '0', 'General', '0' ) ;

ALTER TABLE mantis_bug_table ADD category_id INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_table DROP COLUMN category;

DROP TABLE IF EXISTS mantis_project_category_table;

ALTER TABLE mantis_project_table ADD category_id INTEGER UNSIGNED NOT NULL DEFAULT 1;



INSERT INTO mantis_plugin_table
	( basename, enabled ) VALUES
	( 'MantisCoreFormatting', '1' );

ALTER TABLE mantis_project_table ADD inherit_global INTEGER UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE mantis_project_hierarchy_table ADD inherit_parent INTEGER UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE mantis_plugin_table ADD protected TINYINT NOT NULL DEFAULT '0';

ALTER TABLE mantis_plugin_table ADD priority INTEGER UNSIGNED NOT NULL DEFAULT 3;

ALTER TABLE mantis_project_version_table ADD obsolete TINYINT NOT NULL DEFAULT '0';

ALTER TABLE mantis_bug_table ADD due_date DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01';

ALTER TABLE mantis_custom_field_table ADD filter_by TINYINT NOT NULL DEFAULT '1';

CREATE TABLE mantis_bug_revision_table (
id                       INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
bug_id                   INTEGER UNSIGNED NOT NULL,
bugnote_id               INTEGER UNSIGNED NOT NULL DEFAULT 0,
user_id                  INTEGER UNSIGNED NOT NULL,
timestamp                DATETIME NOT NULL DEFAULT '1970-01-01 00:00:01',
type                     INTEGER UNSIGNED NOT NULL,
value                    LONGTEXT NOT NULL,
                 PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;

ALTER TABLE mantis_bug_revision_table ADD  INDEX idx_bug_rev_id_time  (bug_id, timestamp);

ALTER TABLE mantis_bug_revision_table ADD  INDEX idx_bug_rev_type  (type);

ALTER TABLE mantis_bug_table ADD date_submitted_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_table ADD due_date_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_table ADD last_updated_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_table DROP COLUMN date_submitted;

ALTER TABLE mantis_bug_table CHANGE COLUMN date_submitted_int date_submitted INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_table DROP COLUMN due_date;

ALTER TABLE mantis_bug_table CHANGE COLUMN due_date_int due_date INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_table DROP COLUMN last_updated;

ALTER TABLE mantis_bug_table CHANGE COLUMN last_updated_int last_updated INTEGER UNSIGNED NOT NULL DEFAULT 1;


ALTER TABLE mantis_bugnote_table ADD last_modified_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bugnote_table ADD date_submitted_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bugnote_table DROP COLUMN last_modified;

ALTER TABLE mantis_bugnote_table CHANGE COLUMN last_modified_int last_modified INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bugnote_table ADD  INDEX idx_last_mod  (last_modified);

ALTER TABLE mantis_bugnote_table DROP COLUMN date_submitted;

ALTER TABLE mantis_bugnote_table CHANGE COLUMN date_submitted_int date_submitted INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_file_table ADD date_added_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_file_table DROP COLUMN date_added;

ALTER TABLE mantis_bug_file_table CHANGE COLUMN date_added_int date_added INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_project_file_table ADD date_added_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_project_file_table DROP COLUMN date_added;

ALTER TABLE mantis_project_file_table CHANGE COLUMN date_added_int date_added INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_history_table ADD date_modified_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_history_table DROP COLUMN date_modified;

ALTER TABLE mantis_bug_history_table CHANGE COLUMN date_modified_int date_modified INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_user_table ADD last_visit_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_user_table ADD date_created_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_user_table DROP COLUMN date_created;

ALTER TABLE mantis_user_table CHANGE COLUMN date_created_int date_created INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_user_table DROP COLUMN last_visit;

ALTER TABLE mantis_user_table CHANGE COLUMN last_visit_int last_visit INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_email_table ADD submitted_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_email_table DROP COLUMN submitted;

ALTER TABLE mantis_email_table CHANGE COLUMN submitted_int submitted INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_tag_table ADD date_created_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_tag_table ADD date_updated_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_tag_table DROP COLUMN date_created;

ALTER TABLE mantis_tag_table CHANGE COLUMN date_created_int date_created INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_tag_table DROP COLUMN date_updated;

ALTER TABLE mantis_tag_table CHANGE COLUMN date_updated_int date_updated INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_tag_table ADD date_attached_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_tag_table DROP COLUMN date_attached;

ALTER TABLE mantis_bug_tag_table CHANGE COLUMN date_attached_int date_attached INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_tokens_table ADD timestamp_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_tokens_table ADD expiry_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_tokens_table DROP COLUMN timestamp;

ALTER TABLE mantis_tokens_table CHANGE COLUMN timestamp_int timestamp INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_tokens_table DROP COLUMN expiry;

ALTER TABLE mantis_tokens_table CHANGE COLUMN expiry_int expiry INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_news_table ADD last_modified_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_news_table ADD date_posted_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_news_table DROP COLUMN last_modified;

ALTER TABLE mantis_news_table CHANGE COLUMN last_modified_int last_modified INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_news_table DROP COLUMN date_posted;

ALTER TABLE mantis_news_table CHANGE COLUMN date_posted_int date_posted INTEGER UNSIGNED NOT NULL DEFAULT 1;


ALTER TABLE mantis_bug_revision_table ADD timestamp_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_bug_revision_table DROP COLUMN timestamp;

ALTER TABLE mantis_bug_revision_table CHANGE COLUMN timestamp_int timestamp INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_user_pref_table ADD timezone VARCHAR(32) NOT NULL DEFAULT '';

ALTER TABLE mantis_project_version_table ADD date_order_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_project_version_table DROP COLUMN date_order;

ALTER TABLE mantis_project_version_table CHANGE COLUMN date_order_int date_order INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_sponsorship_table ADD date_submitted_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_sponsorship_table ADD last_updated_int INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_sponsorship_table DROP COLUMN last_updated;

ALTER TABLE mantis_sponsorship_table CHANGE COLUMN last_updated_int last_updated INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_sponsorship_table DROP COLUMN date_submitted;

ALTER TABLE mantis_sponsorship_table CHANGE COLUMN date_submitted_int date_submitted INTEGER UNSIGNED NOT NULL DEFAULT 1;

ALTER TABLE mantis_project_file_table ADD user_id INTEGER UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE mantis_bug_file_table ADD user_id INTEGER UNSIGNED NOT NULL DEFAULT 0;

ALTER TABLE mantis_custom_field_table DROP COLUMN advanced;

ALTER TABLE mantis_user_pref_table DROP COLUMN advanced_report;

ALTER TABLE mantis_user_pref_table DROP COLUMN advanced_view;

ALTER TABLE mantis_user_pref_table DROP COLUMN advanced_update;

ALTER TABLE mantis_project_hierarchy_table ADD  INDEX idx_project_hierarchy_child_id  (child_id);

ALTER TABLE mantis_project_hierarchy_table ADD  INDEX idx_project_hierarchy_parent_id  (parent_id);

ALTER TABLE mantis_tag_table ADD  INDEX idx_tag_name  (name);

ALTER TABLE mantis_bug_tag_table ADD  INDEX idx_bug_tag_tag_id  (tag_id);


INSERT INTO mantis_config_table ( value, type, access_reqd, config_id, project_id, user_id ) VALUES ('183', 1, 90, 'database_version', 0, 0 );
