/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
 
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService

BeaverEngine beaverEngine = engine

def home = beaverEngine.getResource("home")
def httpdGroup = beaverEngine.getResource("httpd", "group")
def httpdUser = beaverEngine.getResource("httpd", "user")
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def database = beaverEngine.getResource("database")
def adminEmail = beaverEngine.getResource("novaforge-connector-forge", "adminEmail")
def adminName = beaverEngine.getResource("novaforge-connector-forge", "adminName")
def smtpHost = beaverEngine.getResource("smtp", "host")
def smtpPort = beaverEngine.getResource("smtp", "port")

// Configure config.php
def configPhpSource = processTmpPath + "/resources/files/conf/config.php"
def configPhpTarget = home + "/application/config/config.php"
beaverEngine.copyToFile(configPhpSource, configPhpTarget)
beaverEngine.replaceElement(configPhpTarget, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(configPhpTarget, "@DATABASE@", database)
beaverEngine.replaceElement(configPhpTarget, "@LIMESURVEY_USER@", sqlUser)
beaverEngine.replaceElement(configPhpTarget, "@LIMESURVEY_PWD@", sqlPwd)

// Configure email.php
def emailPhpSource = processTmpPath + "/resources/files/conf/email.php"
def emailPhpTarget = home + "/application/config/email.php"
beaverEngine.copyToFile(emailPhpSource, emailPhpTarget)
beaverEngine.replaceElement(emailPhpTarget, "@ADMIN_EMAIL@", adminEmail)
beaverEngine.replaceElement(emailPhpTarget, "@ADMIN_NAME@", adminName)
beaverEngine.replaceElement(emailPhpTarget, "@EMAIL_METHOD@", "smtp")
beaverEngine.replaceElement(emailPhpTarget, "@SMTP_HOST@", smtpHost)
beaverEngine.replaceElement(emailPhpTarget, "@SMTP_PORT@", smtpPort)

// HTTPD limesurvey.service local : same as for 3.7.0

// Set owner and rights
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(true, httpdGroup, httpdUser, home)
beaverEngine.setPermissionsOnFiles(true,"755", home)
