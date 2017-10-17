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
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService;

BeaverEngine beaverEngine = engine

// Service configuration
def novaforgeService = processTmpPath + "/resources/novaforge.service"
if (beaverEngine.hasMavenProfile("novaforge-with-jira") == true) {
  novaforgeService = processTmpPath + "/resources/novaforge-with-jira.service"
  def jiraService = beaverEngine.getResource("jira", "systemdService")
  beaverEngine.replaceElement(novaforgeService, "@JIRA_SERVICE@", jiraService)
}

def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")
def karafBin = beaverEngine.getResource("karaf", "execFile")
def httpdService = beaverEngine.getResource("httpd", "systemdService")
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def alfrescoService = beaverEngine.getResource("alfresco", "systemdService")
def jenkinsService = beaverEngine.getResource("jenkins", "systemdService")
def sonarService = beaverEngine.getResource("sonar", "systemdService")
def nexusService = beaverEngine.getResource("nexus", "systemdService")
def casService = beaverEngine.getResource("cas", "systemdService")
def sympaService = beaverEngine.getResource("sympa", "systemdService")
def gitlabService = beaverEngine.getResource("gitlab", "systemdService")
def sendmailService = beaverEngine.getResource("sendmail", "systemdService")

beaverEngine.replaceElement(novaforgeService, "@KARAF_BIN@", karafBin)
beaverEngine.replaceElement(novaforgeService, "@HTTPD_SERVICE@", httpdService)
beaverEngine.replaceElement(novaforgeService, "@MARIADB_SERVICE@", mariadbService)
beaverEngine.replaceElement(novaforgeService, "@ALFRESCO_SERVICE@", alfrescoService)
beaverEngine.replaceElement(novaforgeService, "@JENKINS_SERVICE@", jenkinsService)
beaverEngine.replaceElement(novaforgeService, "@SONAR_SERVICE@", sonarService)
beaverEngine.replaceElement(novaforgeService, "@NEXUS_SERVICE@", nexusService)
beaverEngine.replaceElement(novaforgeService, "@CAS_SERVICE@", casService)
beaverEngine.replaceElement(novaforgeService, "@GITLAB_SERVICE@", gitlabService)
beaverEngine.replaceElement(novaforgeService, "@SYMPA_SERVICE@", sympaService)
beaverEngine.replaceElement(novaforgeService, "@SENDMAIL_SERVICE@", sendmailService)
beaverEngine.replaceElement(novaforgeService, "@NOVAFORGE_USER@", localUser)
beaverEngine.replaceElement(novaforgeService, "@NOVAFORGE_GROUP@", localGroup)


def novaforgeSystemd = beaverEngine.getResource("systemdService")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.addService("multi-user.target", novaforgeService, novaforgeSystemd, true);
