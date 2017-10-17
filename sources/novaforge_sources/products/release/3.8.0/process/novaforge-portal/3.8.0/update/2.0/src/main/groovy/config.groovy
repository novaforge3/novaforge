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

import org.codehaus.plexus.util.StringUtils
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService

BeaverEngine beaverEngine = engine

def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")

// Perform only needed updates
// Changes between 3.7.0 and 3.8.0 & needed actions to fix last 3.7.0 delivery :
// - Ticket 727 : novaforge.service : fix missing @MARIADB_SERVICE@ update (MariaDB 10.0 => mysql.service and no longer mariadb.service) 
// - Ticket 567 : updates in portal-config.xml which lead to the following modifications :
// <external-app id="distribution">  ->  <external-app id="distribution" permissions="#project:reference:*">
// <space roles="Super Administrator,Administrator,Member" id="sharetool"> -> <space roles="Super Administrator,Administrator,Member" id="sharetool" permissions="#project:reference:*">
// - Ticket 458 : Enabling of logrotate (for the moment : cas and httpd)

// => no dataFile deployment

// Update portal-config.xml file
// Only when distributed mode is not enabled
def portalConf = beaverEngine.getResource("portalConf")
def portalConfXml = portalConf + "/portal-config.xml"
if (!"safran".equals(localUser)) {
  beaverEngine.replaceElement(portalConfXml, "<external-app id=\"distribution\">", "<external-app id=\"distribution\" permissions=\"#project:reference:*\">")
  beaverEngine.replaceElement(portalConfXml, "<space roles=\"Super Administrator,Administrator,Member\" id=\"sharetool\">", "<space roles=\"Super Administrator,Administrator,Member\" id=\"sharetool\" permissions=\"#project:reference:*\">")
}

// Service configuration
def novaforgeService = processTmpPath + "/resources/novaforge.service"
def karafBin = beaverEngine.getResource("karaf", "execFile")
def httpdService = beaverEngine.getResource("httpd", "systemdService")
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def nexusService = beaverEngine.getResource("nexus", "systemdService")
def casService = beaverEngine.getResource("cas", "systemdService")
def sympaService = beaverEngine.getResource("sympa", "systemdService")
def sendmailService = beaverEngine.getResource("sendmail", "systemdService")
def gitlabTunnelService = beaverEngine.getResource("novaforge-connector-gitlab", "gitalbTunnelService")

beaverEngine.replaceElement(novaforgeService, "@KARAF_BIN@", karafBin)
beaverEngine.replaceElement(novaforgeService, "@HTTPD_SERVICE@", httpdService)
beaverEngine.replaceElement(novaforgeService, "@MARIADB_SERVICE@", mariadbService)
beaverEngine.replaceElement(novaforgeService, "@NEXUS_SERVICE@", nexusService)
beaverEngine.replaceElement(novaforgeService, "@CAS_SERVICE@", casService)
beaverEngine.replaceElement(novaforgeService, "@SYMPA_SERVICE@", sympaService)
beaverEngine.replaceElement(novaforgeService, "@NOVAFORGE_USER@", localUser)
beaverEngine.replaceElement(novaforgeService, "@NOVAFORGE_GROUP@", localGroup)
beaverEngine.replaceElement(novaforgeService, "@SENDMAIL_SERVICE@", sendmailService)
beaverEngine.replaceElement(novaforgeService, "@GITLAB_TUNNEL@", gitlabTunnelService)

def novaforgeSystemd = beaverEngine.getResource("systemdService")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.addService("multi-user.target", novaforgeService, novaforgeSystemd, true);

// Logrotate configuration
def logrotatePath = beaverEngine.getResource("logrotatePath")
def novaforgeLogrotate = logrotatePath + "/novaforge"
def httpdLogs = beaverEngine.getResource("httpd", "logs")
def casdLogs = beaverEngine.getResource("cas", "logs")

beaverEngine.copyToFile(processTmpPath + "/resources/novaforge.txt",novaforgeLogrotate)
beaverEngine.replaceElement(novaforgeLogrotate,"@CAS_LOGS@",casdLogs)
beaverEngine.replaceElement(novaforgeLogrotate,"@HTTPD_LOGS@",httpdLogs)

// Set up permissions
def runtimeConf = beaverEngine.getResource("novaforge-runtime","conf")
def httpService = beaverEngine.getResource("httpService")
def karafHome = beaverEngine.getResource("karaf","home")
beaverEngine.setOwner(true,localGroup,localUser,karafHome)
beaverEngine.setOwner(true,localGroup,localUser,runtimeConf)
beaverEngine.setOwner(true,localGroup,localUser,httpService)
beaverEngine.setPermissionsOnFiles(false,"664",httpService)
beaverEngine.setOwner(true,localGroup,localUser,portalConf)
