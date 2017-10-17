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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

BeaverEngine beaverEngine = engine

def dataVersion = beaverEngine.getResource("dataVersion")
// ************** Create tmp directory
def tmpDirectory = processTmpPath + "/tmp"
beaverEngine.createDirectory(tmpDirectory)

// ************** Extract Polarion WAR
def karafRepository = beaverEngine.getResource("karaf", "repository")
def version = beaverEngine.getDataVersion()
def polarionWar = karafRepository + "/org/novaforge/products/data/polarion-webclient/" + dataVersion + "/polarion-webclient-" + dataVersion + ".war"
def polarionWarTmp = tmpDirectory + "/polarion"
beaverEngine.unpackFile(polarionWar,polarionWarTmp)

// ************** Configuration Polarion
def polarionWebXml = polarionWarTmp + "/WEB-INF/web.xml"
beaverEngine.copyToFile(processTmpPath + "/resources/web.xml", polarionWebXml)

// Configuration CAS
def casBaseUrl = beaverEngine.getResource("main", "cas", "baseUrl")
def casUri = beaverEngine.getResource("main", "cas", "uri")
def casLogoutUri = beaverEngine.getResource("main", "cas", "logoutUri")
def casLoginUri = beaverEngine.getResource("main", "cas", "loginUri")
beaverEngine.replaceElement(polarionWebXml, "@CAS_URL@", casBaseUrl)
beaverEngine.replaceElement(polarionWebXml, "@CAS_URI@", casUri)
beaverEngine.replaceElement(polarionWebXml, "@CAS_LOGIN_URI@", casLoginUri)
beaverEngine.replaceElement(polarionWebXml, "@CAS_LOGOUT_URI@", casLogoutUri)
// Configuration Server
def mainBaseUrl = beaverEngine.getResource("main","httpd","baseUrl")
def svnDatas = beaverEngine.getResource("datas")
beaverEngine.replaceElement(polarionWebXml, "@SVN_DATA@", svnDatas)
beaverEngine.replaceElement(polarionWebXml, "@EXTERNAL_URL@", mainBaseUrl)

// Configuration MariaDB
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def datasourceUser = beaverEngine.getResource("datasourceUser")
def datasourcePwd = beaverEngine.getResource("datasourcePwd")
beaverEngine.replaceElement(polarionWebXml, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(polarionWebXml, "@MARIADB_PORT@", mariadbPort)
beaverEngine.replaceElement(polarionWebXml, "@NOVAFORGE_USER@", datasourceUser)
beaverEngine.replaceElement(polarionWebXml, "@NOVAFORGE_PWD@", datasourcePwd)

// Pack Polarion
beaverEngine.pack(polarionWarTmp, polarionWar)

// Set up permissions
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")
beaverEngine.setOwner(true,localGroup,localUser,karafRepository)
beaverEngine.setOwner(false,localGroup,localUser,polarionWar)
