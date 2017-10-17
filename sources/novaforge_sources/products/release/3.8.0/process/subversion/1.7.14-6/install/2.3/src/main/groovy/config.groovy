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
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService

BeaverEngine beaverEngine = engine

// exec base64.sql
def base64SqlFile = processTmpPath + "/resources/sql/base64.sql"
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mariadbUser = beaverEngine.getResource("mariadb", "rootUser")
def mariadbPwd = beaverEngine.getResource("mariadb", "rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def mariadbBin = beaverEngine.getResource("mariadb", "bin")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, base64SqlFile)
systemdService.stopService(mariadbService)

// Configure HTTPD in distributed mode
def serverId = beaverEngine.getServerId()
if ("svn".equals(serverId)) {
  def novaforgeConf = beaverEngine.getResource("httpd", "novaforgeConf")
  beaverEngine.deleteLines(novaforgeConf, "<Proxy *>", "<VirtualHost 127.0.0.1:80>")
}


// Configure  HTTPD subversion.service local
def subversionService = processTmpPath + "/resources/subversion.service"
def datasourceUser = beaverEngine.getResource("datasourceUser")
def datasourcePwd = beaverEngine.getResource("datasourcePwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def repoPath = beaverEngine.getResource("datas")
beaverEngine.replaceElement(subversionService, "@NOVAFORGE_USER@", datasourceUser)
beaverEngine.replaceElement(subversionService, "@NOVAFORGE_PWD@", datasourcePwd)
beaverEngine.replaceElement(subversionService, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(subversionService, "@MARIADB_PORT@", mariadbPort)
beaverEngine.replaceElement(subversionService, "@SVN_REPOS_PATH@", repoPath)

// Copy HTTPD subversion.service local
def httpService = beaverEngine.getResource("httpService")
beaverEngine.copyToFile(subversionService,httpService)

// Copy HTTPD subversion.module
def httpModule = beaverEngine.getResource("httpModule")
beaverEngine.copyToFile(processTmpPath + "/resources/subversion.conf",httpModule)

// Set owner and rights
def httpdUser = beaverEngine.getResource("httpd", "user")
def httpdGroup = beaverEngine.getResource("httpd", "group")
beaverEngine.setOwner(false, httpdGroup, httpdUser, httpModule)
beaverEngine.setPermissionsOnFiles(false,"664",httpModule)
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(false, localGroup, localUser, httpService)
beaverEngine.setPermissionsOnFiles(false,"664",httpService)

