use %DATABASE%; 

ALTER TABLE  mantis_category_table MODIFY COLUMN project_id INTEGER NOT NULL DEFAULT 0;
	
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

ALTER TABLE mantis_risk_criteria_table ADD  UNIQUE INDEX idx_label  (label, project_id);

CREATE TABLE mantis_risk_evaluation_history_table (
id 						INTEGER UNSIGNED NOT NULL AUTO_INCREMENT,
project_id 				INTEGER NOT NULL,
category_id 			INTEGER NOT NULL,
evaluation				FLOAT NOT NULL,
date_modified			DATE DEFAULT NULL,
  				PRIMARY KEY (id)
)ENGINE=MyISAM DEFAULT CHARSET=utf8;