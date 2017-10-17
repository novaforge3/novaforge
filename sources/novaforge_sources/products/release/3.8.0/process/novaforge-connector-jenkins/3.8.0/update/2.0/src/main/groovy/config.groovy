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
BeaverEngine beaverEngine = engine

// Datasource information
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")

// CFG diretories
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def pluginsRepository = karafRepository + "/org/novaforge/forge/plugins/"

def jenkinsCfg = pluginsRepository + "novaforge-jenkins-cfg/" + dataVersion + "/novaforge-jenkins-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")
def clientPort = beaverEngine.getResource("clientPort")
def clientAdmin = beaverEngine.getResource("clientAdmin")
def clientPwd = beaverEngine.getResource("clientPwd")
def defaultAlias = beaverEngine.getResource("defaultAlias")
def serverConf = beaverEngine.getResource("serverConf")

def clientHost = beaverEngine.getResource("clientHost")
def maxAllowedProjectInstances = beaverEngine.getResource("maxAllowedProjectInstances")

beaverEngine.replaceElement(jenkinsCfg, "@DESCRIPTION", description)
beaverEngine.replaceElement(jenkinsCfg, "@CLIENT_PWD", clientPwd)
beaverEngine.replaceElement(jenkinsCfg, "@SERVER_CONF", serverConf)
//Default value for server configuration is pic
def server = "pic"

def jenkinsHostJSON = beaverEngine.parseJson(clientHost)
if ( jenkinsHostJSON in Map )
{
  clientHost = jenkinsHostJSON.get(server);
}

def jenkinsPortJSON = beaverEngine.parseJson(clientPort)
if ( jenkinsPortJSON in Map )
{
  clientPort = jenkinsPortJSON.get(server);
}
def jenkinsAliasJSON = beaverEngine.parseJson(defaultAlias);
if ( jenkinsAliasJSON in Map )
{
  defaultAlias = jenkinsAliasJSON.get(server);
}
def adminLoginJSON = beaverEngine.parseJson(clientAdmin);
if ( adminLoginJSON in Map )
{
  clientAdmin = adminLoginJSON.get(server);
}
beaverEngine.replaceElement(jenkinsCfg, "@CLIENT_HOST", clientHost)
beaverEngine.replaceElement(jenkinsCfg, "@CLIENT_PORT", clientPort)
beaverEngine.replaceElement(jenkinsCfg, "@CLIENT_ADMIN", clientAdmin)
beaverEngine.replaceElement(jenkinsCfg, "@DEFAULT_ALIAS", defaultAlias)
beaverEngine.replaceElement(jenkinsCfg, "maxAllowedProjectInstances=-1", "maxAllowedProjectInstances=" + maxAllowedProjectInstances)

// Copy Jenkins cfg to novaforge runtime
beaverEngine.copyToFile(jenkinsCfg, pluginConf + "/plugins.jenkins.cfg")

// Configure Jenkins datasource
def jenkinsDatasource = pluginsRepository + "novaforge-jenkins-impl/" + dataVersion + "/novaforge-jenkins-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(jenkinsDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(jenkinsDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(jenkinsDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(jenkinsDatasource, "password=root", "password=" + datasourcePwd)

// Copy Jenkins datasource to novaforge runtime
beaverEngine.copyToFile(jenkinsDatasource, datasourceConf + "/datasource.jenkins.cfg")