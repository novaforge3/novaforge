use @databaseJiraMysql@;

/* Active user external management */
UPDATE propertynumber SET propertyvalue = '1' WHERE ID = '10039';

/* Set to 100 the maxsize of project name */
UPDATE propertystring SET propertyvalue = '100' WHERE ID = '10207';

/* Set to 10 the maxsize of project key */
UPDATE propertystring SET propertyvalue = '80' WHERE ID = '10205';

/* Disable the e-mail visibility */
UPDATE propertystring SET propertyvalue = 'hide' WHERE ID = '10043';
	
/* Only administrator can create jira account */
UPDATE propertystring SET propertyvalue = 'private' WHERE ID = '10019';

/* Allow use of rest/soap API */
UPDATE propertynumber SET propertyvalue = '1' WHERE ID = '10203';

/* Update Jira licence */
UPDATE propertytext SET propertyvalue = '@jiraLicence@' WHERE ID = '10027';

/* Update Jira URL */
UPDATE propertystring SET propertyvalue = 'https://@forgeHostname@@aliasJira@' WHERE ID = '10018';

/* Set Jira SMTP */
INSERT INTO SEQUENCE_VALUE_ITEM VALUES ('MailServer','10100');
INSERT INTO mailserver VALUES ('10000','@mailServerName@','@mailServerDescription@','@mailExpeditor@','@mailSubject@','@mailServerPort@','smtp','smtp','@mailServerHost@',NULL,NULL,NULL,'false','10000',NULL,NULL);

/*Hide marketplace*/
INSERT INTO propertystring VALUES ('10987','true'),('10988','true'),('10989','true');

INSERT INTO propertyentry VALUES ('10987','jira.properties','1','com.atlassian.upm.SysPersisted:properties:upm.pac.disable','5'),('10988','jira.properties','1','com.atlassian.upm.SysPersisted:properties:upm.plugin.requests.disable','5'),('10989','jira.properties','1','com.atlassian.upm.SysPersisted:properties:upm.email.notifications.disable','5');


