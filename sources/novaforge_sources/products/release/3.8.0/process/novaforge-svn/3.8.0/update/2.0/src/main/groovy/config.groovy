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
def runtimeConf = beaverEngine.getResource("novaforge-runtime","conf")
def karafRepository = beaverEngine.getResource("karaf","repository")
def karafHome = beaverEngine.getResource("karaf","home")

// Set up permissions
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")
beaverEngine.setOwner(true,localGroup,localUser,karafHome)
beaverEngine.setOwner(true,localGroup,localUser,karafRepository)
beaverEngine.setOwner(true,localGroup,localUser,runtimeConf)

// Service configuration
def novaforgeService = processTmpPath + "/resources/novaforge.service"

def karafBin = beaverEngine.getResource("karaf", "execFile")
def httpdService = beaverEngine.getResource("httpd", "systemdService")
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def gitlabService = beaverEngine.getResource("gitlab", "systemdService")
def gitlabDaemonService = beaverEngine.getResource("gitlab", "sshDaemonService")

beaverEngine.replaceElement(novaforgeService, "@KARAF_BIN@", karafBin)
beaverEngine.replaceElement(novaforgeService, "@HTTPD_SERVICE@", httpdService)
beaverEngine.replaceElement(novaforgeService, "@MARIADB_SERVICE@", mariadbService)
beaverEngine.replaceElement(novaforgeService, "@GITLAB_SERVICE@", gitlabService)
beaverEngine.replaceElement(novaforgeService, "@NOVAFORGE_USER@", localUser)
beaverEngine.replaceElement(novaforgeService, "@NOVAFORGE_GROUP@", localGroup)
beaverEngine.replaceElement(novaforgeService, "@GITLAB_DAEMON@", gitlabDaemonService)

def novaforgeSystemd = beaverEngine.getResource("systemdService")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.addService("multi-user.target", novaforgeService, novaforgeSystemd, true);