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
import org.codehaus.plexus.util.StringUtils

BeaverEngine beaverEngine = engine
UserService userService = beaverEngine.getUserService();

// Configure httpd.conf
def httpdConf = "/etc/httpd/conf/httpd.conf"
def user = beaverEngine.getResource("user")
def group = beaverEngine.getResource("group")
beaverEngine.replaceExpression(httpdConf, "^User (.*)", "User " + user)
beaverEngine.replaceExpression(httpdConf, "^Group (.*)", "Group " + group)

def listen = "Listen 80"
def isListenCommented = beaverEngine.getValueFromRegex(httpdConf, "#" + listen)
if (StringUtils.isBlank(isListenCommented)) {
  beaverEngine.commentLines(httpdConf, listen, listen, "shell")
}

def serverAdmin = "ServerAdmin root@localhost"
def isServerAdminCommented = beaverEngine.getValueFromRegex(httpdConf, "#" + serverAdmin)
if (StringUtils.isBlank(isServerAdminCommented)) {
  beaverEngine.commentLines(httpdConf, serverAdmin, serverAdmin, "shell")
}
def errorLog = "ErrorLog \"logs/error_log\""
def isErrorLogCommented = beaverEngine.getValueFromRegex(httpdConf, "#" + errorLog)
if (StringUtils.isBlank(isErrorLogCommented)) {
  beaverEngine.commentLines(httpdConf, errorLog, errorLog, "shell")
}

def existingConfD = beaverEngine.getValueFromRegex(httpdConf, "Include (.*)/httpd/conf.d/\\*\\.conf")
if (StringUtils.isNotBlank(existingConfD)) {
  beaverEngine.deleteLines(httpdConf, existingConfD, existingConfD)
}

def confd = beaverEngine.getResource("confd")
def customConfD = "Include " + confd + "/*.conf"
def isCustomConfD = beaverEngine.getValueFromRegex(httpdConf, "Include " + confd + "(.*)")
if (StringUtils.isBlank(isCustomConfD)) {
  beaverEngine.addLineToFile(httpdConf, "Include conf.modules.d/", customConfD)
}


// Configure novaforge.conf
def novaforgeConf = beaverEngine.getResource("novaforgeConf")
def localHost = beaverEngine.getResource("local:host")
def servicesProxy = beaverEngine.getResource("servicesProxy")
def servicesLocal = beaverEngine.getResource("servicesLocal")
def modules = beaverEngine.getResource("modules")
def logs = beaverEngine.getResource("logs")

beaverEngine.copyToFile(processTmpPath + "/resources/novaforge.conf" ,novaforgeConf)
beaverEngine.replaceElement(novaforgeConf,"@HOSTNAME@", localHost)
beaverEngine.replaceElement(novaforgeConf,"@HTTPD_MODULES@", modules)
beaverEngine.replaceElement(novaforgeConf,"@HTTPD_LOGS@", logs)
beaverEngine.replaceElement(novaforgeConf,"@HTTPD_SERVICES_PROXY@", servicesProxy)
beaverEngine.replaceElement(novaforgeConf,"@HTTPD_SERVICES_LOCAL@", servicesLocal)

// Configure apache user
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")
if (user.equals(localUser) == false){
  userService.addGroupsToUser(localUser, group);
  userService.addGroupsToUser(user, localGroup);
}

// Set owner
def home = beaverEngine.getResource("home")
beaverEngine.setOwner(true,group,user,home)
beaverEngine.setOwner(true,group,user,logs)