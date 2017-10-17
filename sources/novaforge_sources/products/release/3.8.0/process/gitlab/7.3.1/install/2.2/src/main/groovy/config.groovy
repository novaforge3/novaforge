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
import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService
import org.codehaus.plexus.util.StringUtils
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

BeaverEngine beaverEngine = engine

// Get Resources
def localhost = beaverEngine.getResource("local:host")
def mainHost = beaverEngine.getResource("main:host")

def aliasDirectory = beaverEngine.getResource("aliasDirectory")
def datas = beaverEngine.getResource("datas")
def home = beaverEngine.getResource("home")
def logs = beaverEngine.getResource("logs")
def defaultAlias = beaverEngine.getResource("main", "novaforge-connector-gitlab", "defaultAlias")
def toolAlias = beaverEngine.getResource("main","novaforge-connector-gitlab", "toolAlias")
def clientToken = beaverEngine.getResource("main","novaforge-connector-gitlab", "clientToken")
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")
def baseUrl = beaverEngine.getResource("main","httpd", "baseUrl")
def sshPort = beaverEngine.getResource("sshPort")
def serverId = beaverEngine.getServerId()

// Define Resources
def binDir = "/opt/gitlab/embedded/bin"
def serviceDir = "/opt/gitlab/embedded/service"
def railsServiceDir = "/opt/gitlab/embedded/service/gitlab-rails"
def rubyGemsDir = serviceDir + "/gem/ruby/2.1.0/gems"
def defaultTemplatesDir = "/opt/gitlab/embedded/cookbooks/gitlab/templates/default"
def publicDir = "/opt/gitlab/embedded/service/gitlab-rails/public"
def gitlabRbFile = "/etc/gitlab/gitlab.rb"
def gitlabRbDir = "/etc/gitlab"
def patchDir = processTmpPath+"/resources/patch/"
def thirdparty = datas + "/thirdParty"

// Copy and configure gitlab.rb
def gitlabRbConf = processTmpPath + "/resources/conf/gitlab.rb"
beaverEngine.copy(gitlabRbConf, gitlabRbDir)
beaverEngine.replaceElement(gitlabRbFile, "@BASE_URL@", baseUrl)
beaverEngine.replaceElement(gitlabRbFile, "@DEFAULT_ALIAS@", defaultAlias)
beaverEngine.replaceElement(gitlabRbFile, "@TOOL_ALIAS@", toolAlias)
beaverEngine.replaceElement(gitlabRbFile, "@DATAS@", datas)
beaverEngine.replaceElement(gitlabRbFile, "@THIRDPARTY@", thirdparty)
beaverEngine.replaceElement(gitlabRbFile, "@MAIN_HOST@", mainHost)
beaverEngine.replaceElement(gitlabRbFile, "@USER@", localUser)
beaverEngine.replaceElement(gitlabRbFile, "@LOGS_DIR@", logs)

if ("svn".equals(serverId)) {
  beaverEngine.replaceElement(gitlabRbFile, "@SSH_PORT@", "" + sshPort)
}
else {
  beaverEngine.replaceElement(gitlabRbFile, "@SSH_PORT@", "22")
}

// Configuration STMP
def smtpHost = beaverEngine.getResource("main", "smtp", "host")
def smtpPort = beaverEngine.getResource("main", "smtp", "port")
def smtpUsername = beaverEngine.getResource("main", "smtp", "username")
def smtpPassword = beaverEngine.getResource("main", "smtp", "password")
def smtpNoReply = beaverEngine.getResource("main", "smtp","noReply")
beaverEngine.replaceElement(gitlabRbFile, "@SMTP_HOST@", smtpHost)
beaverEngine.replaceElement(gitlabRbFile, "@SMTP_PORT@", smtpPort)
beaverEngine.replaceElement(gitlabRbFile, "@SMTP_USER@", smtpUsername)
beaverEngine.replaceElement(gitlabRbFile, "@SMTP_PWD@", smtpPassword)
if ( (StringUtils.isBlank(smtpUsername)) && (StringUtils.isBlank(smtpPassword)) ) 
{
  beaverEngine.replaceElement(gitlabRbFile, "gitlab_rails['smtp_user_name']", "#gitlab_rails['smtp_user_name']")
  beaverEngine.replaceElement(gitlabRbFile, "gitlab_rails['smtp_password']", "#gitlab_rails['smtp_password']")
  beaverEngine.replaceElement(gitlabRbFile, "gitlab_rails['smtp_authentication']", "#gitlab_rails['smtp_authentication']")
}
// Set no reply email in gitlab.rb
beaverEngine.replaceElement(gitlabRbFile, "@E_MAIL@", smtpNoReply)

// Configure relative URL into unicorn.rb template
def unicornTemplateFilePath = defaultTemplatesDir + "/unicorn.rb.erb"
beaverEngine.replaceExpression(unicornTemplateFilePath, "(.*)RAILS_RELATIVE_URL_ROOT(.*)", "ENV['RAILS_RELATIVE_URL_ROOT'] = \"/" + defaultAlias + "/" + toolAlias + "\"")

// Configure relative URL into application.rb template
def applicationConfigFile = railsServiceDir +  "/config/application.rb"
beaverEngine.replaceExpression(applicationConfigFile, "config.relative_url_root(.*)", "config.relative_url_root = \"/" + defaultAlias + "/" + toolAlias + "\"")

// Install new gems and set root user private token
def bundleScript = processTmpPath+"/resources/sh/install_bundle.sh"
beaverEngine.setPermissionsOnFiles(false,"755", bundleScript)
beaverEngine.executeScript(bundleScript)

// Patch activerecord-session_store-0.1.0.gem
def patchFile = patchDir + "activerecord-session_store.patch"
beaverEngine.patch(patchFile, 0, rubyGemsDir, true)

// Patch omniauth-cas-1.1.0.gem
patchFile = patchDir + "omniauth-cas.patch"
beaverEngine.patch(patchFile, 0, rubyGemsDir, true)

// Patch header for CAS logout
def viewsLayoutDir = railsServiceDir + "/app/views/layouts"
patchFile = patchDir + "_head_panel.html.haml.patch"
beaverEngine.patch(patchFile, 0, viewsLayoutDir, true)
def casBaseUrl = beaverEngine.getResource("main","cas","baseUrl")
def casLogoutUri = beaverEngine.getResource("main","cas","logoutUri")
def casLogoutUrl = casBaseUrl + casLogoutUri
def viewsLayoutHeaderFile = viewsLayoutDir + "/_head_panel.html.haml"
beaverEngine.replaceElement(viewsLayoutHeaderFile, "@CAS_LOGOUT_URL@", casLogoutUrl)

// Update static files
staticsFiles = [
  publicDir + "/404.html",
  publicDir + "/422.html",
  publicDir + "/500.html",
  publicDir + "/502.html",
  publicDir + "/deploy.html"
]
staticsCSS = "/" + defaultAlias + "/" + toolAlias +"/static.css"
staticsFiles.each() {
  staticsFile->beaverEngine.replaceElement(staticsFile, "/static.css", staticsCSS)
}

// Configure  httpd gitlab.service local
def gitlabService = processTmpPath + "/resources/httpd/gitlab.service"
beaverEngine.replaceElement(gitlabService, "@LOCALHOST@", localhost)
beaverEngine.replaceElement(gitlabService, "@ALIAS_DIRECTORY@", aliasDirectory)
beaverEngine.replaceElement(gitlabService, "@DEFAULT_ALIAS@", defaultAlias)
beaverEngine.replaceElement(gitlabService, "@TOOL_ALIAS@", toolAlias)

// Copy httpd gitlab.service local
def httpService = beaverEngine.getResource("httpService")
beaverEngine.copyToFile(gitlabService,httpService)
beaverEngine.setOwner(false, localGroup, localUser, httpService)
beaverEngine.setPermissionsOnFiles(false,"664",httpService)

// Launching Gitlab logging services
beaverEngine.executeScript(processTmpPath + "/resources/sh/runsvdir-start.sh")

// Reconfigure gitlab
beaverEngine.executeCommand("gitlab-ctl", "reconfigure")

// Start postgresql
beaverEngine.executeCommand("gitlab-ctl", "start", "postgresql")

// Precompile assets, migrate sql and change root private token
def postConfigurationScript = processTmpPath + "/resources/sh/post_configuration_script.sh"
def sqlScript = processTmpPath+"/resources/sql/change_root_password.sql"
beaverEngine.replaceElement(sqlScript, "@CLIENT_TOKEN@", clientToken)
beaverEngine.replaceElement(postConfigurationScript, "@CHANGE_PASSWORD_FILE_PATH@", sqlScript)
beaverEngine.setPermissionsOnFiles(false,"755", postConfigurationScript)
beaverEngine.executeScript(postConfigurationScript)

// Stop Gitlab
beaverEngine.executeCommand("gitlab-ctl", "stop");

// Kill remaining runsvdir processes
beaverEngine.executeCommand("pkill", "-HUP", "-P", "1", "runsvdir*")

// Service configuration
def gitlabSystemDService = processTmpPath + "/resources/service/gitlab.service"
def httpdService = beaverEngine.getResource("httpd", "systemdService")
beaverEngine.replaceElement(gitlabSystemDService, "@HTTPD_SERVICE@", httpdService)

// Add dependency setting between runsvdir and gitlab services
def gitlabRunsvdirService = "/opt/gitlab/embedded/cookbooks/runit/files/default/gitlab-runsvdir.service"
def bintToGitlab = "BindsTo=gitlab.service"
def bindTo = beaverEngine.getValueFromRegex(gitlabRunsvdirService, bintToGitlab)
if (StringUtils.isBlank(bindTo)) {
  def lineAdded = beaverEngine.addLineToFile(gitlabRunsvdirService, "Description=", bintToGitlab)
}  

def gitlabSystemd = beaverEngine.getResource("systemdService")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.addService("multi-user.target", gitlabSystemDService, gitlabSystemd, true)

// Stop gitlab to terminate remaining processes
systemdService.stopService(gitlabSystemd)

// Configure ssh daemon
if ("svn".equals(serverId)) {
   // Create destination folder
   def systemFolder = beaverEngine.getResource("local:system")
   def sshdFolder = systemFolder + "/sshd/"
   beaverEngine.createDirectory(sshdFolder)
   beaverEngine.setOwner(true, localGroup, localUser, sshdFolder)
   
   // Setting SSH port in sshd conf
   def sshdConfFilename = "sshd_novaforge_config"
   def sshdConfPath = processTmpPath+"/resources/sshd/" + sshdConfFilename
   beaverEngine.replaceElement(sshdConfPath, "@SSH_PORT@", sshPort)
  
   // Copying sshd conf
   def sshdConfDestination = sshdFolder + sshdConfFilename
   beaverEngine.copyToFile(sshdConfPath, sshdConfDestination)
   
   // Setting values in SSHDaemon service
   def sshDaemonServiceName = beaverEngine.getResource("sshDaemonService")
   def sshDaemonServiceFilename = sshDaemonServiceName + ".service"
   def sshDaemonService = processTmpPath + "/resources/sshd/" + sshDaemonServiceFilename
   beaverEngine.replaceElement(sshDaemonService, "@SSHD_CONF_FILE@", sshdConfDestination)
   
   // Adding SSHDaemon service
   def systemd = beaverEngine.getResource("systemdService")
   systemdService.addService("multi-user.target", sshDaemonService, sshDaemonServiceName, true);
}
