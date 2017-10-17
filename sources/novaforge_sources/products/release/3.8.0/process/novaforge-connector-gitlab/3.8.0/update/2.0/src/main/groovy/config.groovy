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


// CFG diretories
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def pluginsRepository = karafRepository + "/org/novaforge/forge/plugins/"

def gitlabCfg = pluginsRepository + "novaforge-gitlab-cfg/" + dataVersion + "/novaforge-gitlab-cfg-" + dataVersion + "-plugins.cfg"

def description = beaverEngine.getResource("description")
def clientAdmin = beaverEngine.getResource("clientAdmin")
def clientToken = beaverEngine.getResource("clientToken")
def defaultAlias = beaverEngine.getResource("defaultAlias")
def toolAlias = beaverEngine.getResource("toolAlias")
def serverConf = beaverEngine.getResource("serverConf")
def clientHost = beaverEngine.getResource("clientHost")

beaverEngine.replaceElement(gitlabCfg, "@DESCRIPTION@", description)
beaverEngine.replaceElement(gitlabCfg, "@CLIENT_HOST@", clientHost)
beaverEngine.replaceElement(gitlabCfg, "@CLIENT_ADMIN@", clientAdmin)
beaverEngine.replaceElement(gitlabCfg, "@CLIENT_PASSWORD@", clientToken)
beaverEngine.replaceElement(gitlabCfg, "@DEFAULT_ALIAS@", defaultAlias)
beaverEngine.replaceElement(gitlabCfg, "@TOOL_ALIAS@", toolAlias)
beaverEngine.replaceElement(gitlabCfg, "@SERVER_CONF@", serverConf)

// Copy Gitlab cfg to novaforge runtime
def karafGitlabCfg = pluginConf + "/plugins.gitlab.cfg"
beaverEngine.copyToFile(gitlabCfg, karafGitlabCfg)

// Configure ssh tunnel
def serverId = beaverEngine.getServerId()
if ("portal".equals(serverId)) {
   // Create destination folder
   def localUser = beaverEngine.getResource("local:user")
   def sshPort = beaverEngine.getResource("sshPort")
   
   // Setting values in Tunnel service
   def tunnelServiceFilename = "gitlabTunnel.service"
   def tunnelServicePath = processTmpPath + "/resources/sshd/" + tunnelServiceFilename
   beaverEngine.replaceElement(tunnelServicePath, "@SSH_PORT@", sshPort)
   beaverEngine.replaceElement(tunnelServicePath, "@DEV_HOST@", clientHost)
   beaverEngine.replaceElement(tunnelServicePath, "@NOVAFORGE_USER@", localUser)
   
   // Adding Tunnel service
   def gitalbTunnelServiceName = beaverEngine.getResource("gitalbTunnelService")
   SystemdService systemdService = beaverEngine.getSystemdService()
   systemdService.addService("multi-user.target", tunnelServicePath, gitalbTunnelServiceName, true);
}

// Configure Gitlab datasource
// Datasource information
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")
def gitlabDatasource = pluginsRepository + "novaforge-gitlab-impl/" + dataVersion + "/novaforge-gitlab-impl-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(gitlabDatasource, "localhost", mariadbHost)
beaverEngine.replaceElement(gitlabDatasource, "3306", mariadbPort)
beaverEngine.replaceElement(gitlabDatasource, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(gitlabDatasource, "password=root", "password=" + datasourcePwd)

def karafGitlabDatasource = datasourceConf + "/datasource.gitlab.cfg"

// Copy Gitlab datasource to novaforge runtime
beaverEngine.copyToFile(gitlabDatasource,karafGitlabDatasource )