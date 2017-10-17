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

// CFG diretories
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def pluginsRepository = karafRepository + "/org/novaforge/forge/plugins/"

def nexusCfg = pluginsRepository + "novaforge-nexus-cfg/" + dataVersion + "/novaforge-nexus-cfg-" + dataVersion + "-plugins.cfg"

// The following cannot be changed
def clientAdmin = beaverEngine.getResource("clientAdmin")
def clientPwd = beaverEngine.getResource("clientPwd")
if (("admin".equals(clientAdmin) == false) || ("admin123".equals(clientPwd) == false) )
{
  throw new  Exception("The properties 'clientAdmin' and 'clientPwd' cannot be changed.");
}


def description = beaverEngine.getResource("description")
def clientHost = beaverEngine.getResource("clientHost")
def clientPort = beaverEngine.getResource("clientPort")
def defaultAlias = beaverEngine.getResource("defaultAlias")
def toolAlias = beaverEngine.getResource("toolAlias")
def serverConf = beaverEngine.getResource("serverConf")

beaverEngine.replaceElement(nexusCfg, "@DESCRIPTION", description)
beaverEngine.replaceElement(nexusCfg, "@CLIENT_HOST", clientHost)
beaverEngine.replaceElement(nexusCfg, "@CLIENT_PORT", clientPort)
beaverEngine.replaceElement(nexusCfg, "@CLIENT_ADMIN", clientAdmin)
beaverEngine.replaceElement(nexusCfg, "@CLIENT_PWD", clientPwd)
beaverEngine.replaceElement(nexusCfg, "@DEFAULT_ALIAS", defaultAlias)
beaverEngine.replaceElement(nexusCfg, "@TOOL_ALIAS", toolAlias)
beaverEngine.replaceElement(nexusCfg, "@SERVER_CONF", serverConf)

// Copy Nexus cfg to novaforge runtime
beaverEngine.copyToFile(nexusCfg, pluginConf + "/plugins.nexus.cfg")

// Configure Nexus datasource
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")
def nexusDatasource = pluginsRepository + "novaforge-nexus-impl/" + dataVersion + "/novaforge-nexus-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(nexusDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(nexusDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(nexusDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(nexusDatasource, "password=root", "password=" + datasourcePwd)

// Copy Nexus datasource to novaforge runtime
beaverEngine.copyToFile(nexusDatasource, datasourceConf + "/datasource.nexus.cfg")