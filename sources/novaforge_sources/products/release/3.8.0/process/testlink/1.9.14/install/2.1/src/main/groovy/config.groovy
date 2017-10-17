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
def datas = beaverEngine.getResource("datas")
def localhost = beaverEngine.getResource("local:host")
def httpdGroup = beaverEngine.getResource("httpd", "group")
def httpdUser = beaverEngine.getResource("httpd", "user")

// Configure config_db.inc.php
def configDb = processTmpPath + "/resources/files/home/config_db.inc.php"
def configDbTarget = home + "/config_db.inc.php"
beaverEngine.copyToFile(configDb, configDbTarget)
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def database = beaverEngine.getResource("database")
beaverEngine.replaceElement(configDbTarget,"@TESTLINK_USER@",sqlUser)
beaverEngine.replaceElement(configDbTarget,"@TESTLINK_PWD@",sqlPwd)
beaverEngine.replaceElement(configDbTarget,"@MARIADB_HOST@",mariadbHost)
beaverEngine.replaceElement(configDbTarget,"@DATABASE@",database)

// Configure config.inc.php
def config = processTmpPath + "/resources/files/home/config.inc.php"
def configTarget = home + "/config.inc.php"
def smtpHost = beaverEngine.getResource("smtp","host")
def smtpPort = beaverEngine.getResource("smtp","port")
def smtpNoReply = beaverEngine.getResource("smtp","noReply")
def casBaseUrl = beaverEngine.getResource("main", "cas", "baseUrl")
def casUri = beaverEngine.getResource("main", "cas", "uri")
def casExternalPort = beaverEngine.getResource("main", "cas", "externalPort")
def casValidateUri = beaverEngine.getResource("main", "cas", "validateUri")
def testlinkLogs = beaverEngine.getResource("logs");
def adminEmail = beaverEngine.getResource("novaforge-connector-forge", "adminEmail")

beaverEngine.copyToFile(config, configTarget)
beaverEngine.replaceElement(configTarget,"@SMTP_HOST@",smtpHost)
beaverEngine.replaceElement(configTarget,"@SMTP_PORT@",smtpPort)
beaverEngine.replaceElement(configTarget,"@HOSTNAME@",localhost)
beaverEngine.replaceElement(configTarget,"@SMTP_NOREPLY@",smtpNoReply)
beaverEngine.replaceElement(configTarget, "@ADMIN_EMAIL@", adminEmail)
beaverEngine.replaceElement(configTarget,"@CAS_HOST@",localhost)
beaverEngine.replaceElement(configTarget,"@CAS_PORT@",casExternalPort)
beaverEngine.replaceElement(configTarget,"@CAS_URI@",casUri)
beaverEngine.replaceElement(configTarget,"@CAS_VALIDATE_URI@",casValidateUri)
beaverEngine.replaceElement(configTarget,"@TESTLINK_LOGS_DIRECTORY@",testlinkLogs)
beaverEngine.replaceElement(configTarget,"@UPAREA_DIR@",datas + "/upload_area/")         

// Copy and configure HTTPD testlink.service local
def testlinkService = processTmpPath + "/resources/testlink.service"
def httpService = beaverEngine.getResource("httpService")
beaverEngine.copyToFile(testlinkService, httpService)
def defaultAlias = beaverEngine.getResource("novaforge-connector-testlink", "defaultAlias")
def toolAlias = beaverEngine.getResource("novaforge-connector-testlink", "toolAlias")
beaverEngine.replaceElement(httpService, "@DEFAULT_ALIAS@", defaultAlias)
beaverEngine.replaceElement(httpService, "@TOOL_ALIAS@", toolAlias)
beaverEngine.replaceElement(httpService, "@HOME@", home)

// Set owner and rights
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(false, localGroup, localUser, httpService)
beaverEngine.setPermissionsOnFiles(false,"664",httpService)
beaverEngine.setOwner(true, httpdGroup, httpdUser, home)
beaverEngine.setPermissionsOnDirectories(true,"755", home + "/cfg")
beaverEngine.setPermissionsOnDirectories(true,"755", home + "/custom")
beaverEngine.setPermissionsOnDirectories(true,"755", home + "/docs")
beaverEngine.setPermissionsOnDirectories(true,"755", home + "/gui")
beaverEngine.setPermissionsOnDirectories(true,"755", home + "/lib")
beaverEngine.setPermissionsOnDirectories(true,"755", home + "/locale")
beaverEngine.setPermissionsOnDirectories(true,"755", home + "/third_party")
beaverEngine.setPermissionsOnDirectories(true,"755", datas + "/upload_area/")
