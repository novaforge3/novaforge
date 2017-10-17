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
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine;

BeaverEngine beaverEngine = engine

def phpbbHome = beaverEngine.getResource("home")
def httpdGroup = beaverEngine.getResource("httpd", "group")
def httpdUser = beaverEngine.getResource("httpd", "user")
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")

//customize the file config.php phpbb
def mysqlHost = beaverEngine.getResource("mariadb", "host")
def mysqlUser = beaverEngine.getResource("sqlUser")
def mysqlPwd = beaverEngine.getResource("sqlPwd")
def prefix = beaverEngine.getResource("prefix")

def configFile = phpbbHome + "/config.php"
beaverEngine.replaceElement(configFile, "@HOSTPHPBB", mysqlHost)
beaverEngine.replaceElement(configFile, "@PHPBBUSER", mysqlUser)
beaverEngine.replaceElement(configFile, "@PASSPHPBB", mysqlPwd)
beaverEngine.replaceElement(configFile, "@PHPBBPREFIX", prefix)

def phpbbAlias = beaverEngine.getResource("phpbbAlias")
def phpbbName = beaverEngine.getResource("phpbbName")

//createRulesHttp
//local
def localPhpbbisServ = processTmpPath + "/resources/apache/local/phpbb.service"
def apacheLocalServices = beaverEngine.getResource("httpService")
beaverEngine.replaceElement(localPhpbbisServ, "@ALIAS@", phpbbAlias)
beaverEngine.replaceElement(localPhpbbisServ,"@PHPBB_HOME@",phpbbHome)
beaverEngine.replaceElement(localPhpbbisServ, "@PHPBB_NAME@", phpbbName)
beaverEngine.copyToFile(localPhpbbisServ ,apacheLocalServices)
beaverEngine.setOwner(false, localGroup, localUser, apacheLocalServices)
beaverEngine.setPermissionsOnFiles(false,"664",apacheLocalServices)

// Set owner and rights
beaverEngine.setOwner(true, httpdGroup, httpdUser, phpbbHome)
beaverEngine.setPermissionsOnFiles(true,"775", phpbbHome)
