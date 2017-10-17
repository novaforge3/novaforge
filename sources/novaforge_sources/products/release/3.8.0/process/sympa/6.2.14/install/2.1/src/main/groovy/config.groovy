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
import org.apache.commons.codec.digest.DigestUtils
import org.codehaus.plexus.util.StringUtils
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

BeaverEngine beaverEngine = engine
def systemdService = engine.getSystemdService()

def user = beaverEngine.getResource("user")
def group = beaverEngine.getResource("group")

def mainHost = beaverEngine.getResource("main:host")
def home = beaverEngine.getResource("home")
def datas = beaverEngine.getResource("datas")
def logs = beaverEngine.getResource("logs")
def execDir = beaverEngine.getResource("execDir")
def bin = beaverEngine.getResource("bin")

def adminEmail = beaverEngine.getResource("novaforge-connector-sympa", "listmaster")
def defaultAlias = beaverEngine.getResource("main", "novaforge-connector-sympa", "defaultAlias")
def clientAdmin = beaverEngine.getResource("main", "novaforge-connector-sympa", "clientAdmin")
def clientPwd = beaverEngine.getResource("main", "novaforge-connector-sympa", "clientPwd")

def constantFile = beaverEngine.getResource("constantFile")
def sympaConf = beaverEngine.getResource("sympaConf")
def rsyslogConf = beaverEngine.getResource("rsyslogConf")
def httpdConf = beaverEngine.getResource("httpdConf")
def fcgiConf = beaverEngine.getResource("fcgiConf")

def list_data = beaverEngine.getResource("list_data")
def listAliasesTemplate = beaverEngine.getResource("listAliases")
def aliases = beaverEngine.getResource("aliases")
def bounce = beaverEngine.getResource("bounce")
def arc = beaverEngine.getResource("arc")
def maxSize = beaverEngine.getResource("maxSize")

def database = beaverEngine.getResource("database")
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def mariadbHost = beaverEngine.getResource("mariadb", "host")

// Configure logs
// Add log into log directory
def sympaLogFile = logs + "/sympa.log"
Path sympaLogFilePath = Paths.get(sympaLogFile)
if (!Files.exists(sympaLogFilePath))
{
	beaverEngine.copyToFile(processTmpPath + "/resources/log/sympa.log", sympaLogFile)
}
beaverEngine.replaceExpression(rsyslogConf, "(local1\\.\\*\\s+).*", "\\1"+sympaLogFile)
// Remove log from /var/log/messages
def rsyslogDefaultConf = "/etc/rsyslog.conf"
def sympaNone = "local1\\.none.*/var/log/messages"
def configuredLog = beaverEngine.getValueFromRegex(rsyslogDefaultConf, sympaNone)
if (StringUtils.isBlank(configuredLog)) {
	beaverEngine.replaceExpression(rsyslogDefaultConf, "(\\S+)(\\s+/var/log/messages)", "\\1;local1.none\\2")
}
// Restart rsyslog
def rsyslogService = "rsyslog"
systemdService.stopService(rsyslogService)
systemdService.startService(rsyslogService)

// Configure httpd
def oldHttpdConf = "/etc/httpd/conf.d/sympa.conf";
Path homePath = Paths.get(oldHttpdConf)
if (Files.exists(homePath))
{
	beaverEngine.move(oldHttpdConf, oldHttpdConf+".old")
}

beaverEngine.copyToFile(processTmpPath + "/resources/httpd/fcgi.conf", fcgiConf)
beaverEngine.copyToFile(processTmpPath + "/resources/httpd/sympa.conf", httpdConf)
beaverEngine.replaceElement(httpdConf, "@DEFAULT_ALIAS@", defaultAlias)
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(false, localGroup, localUser, httpdConf)
beaverEngine.setPermissionsOnFiles(false,"664",httpdConf)

// Configure sympa constant
beaverEngine.replaceExpression(constantFile, "(\nuse constant CONFIG\\s*=>\\s*)'.*';", "\\1'" + sympaConf + "';")
beaverEngine.replaceExpression(constantFile, "(\nuse constant SYSCONFDIR\\s*=>\\s*)'.*';", "\\1'" + home + "';")
beaverEngine.replaceExpression(constantFile, "(\nuse constant LIBEXECDIR\\s*=>\\s*)'.*';", "\\1'" + bin + "';")
beaverEngine.replaceExpression(constantFile, "(\nuse constant EXPLDIR\\s*=>\\s*)'.*';", "\\1'" + list_data + "';")
beaverEngine.replaceExpression(constantFile, "(\nuse constant SENDMAIL_ALIASES\\s*=>\\s*)'.*';", "\\1'" + aliases + "';")
beaverEngine.replaceExpression(constantFile, "(\nuse constant ARCDIR\\s*=>\\s*)'.*';", "\\1'" + arc + "';")
beaverEngine.replaceExpression(constantFile, "(\nuse constant BOUNCEDIR\\s*=>\\s*)'.*';", "\\1'" + bounce + "';")
// Configure sympa
beaverEngine.copyToFile(processTmpPath + "/resources/sympa/sympa.conf", sympaConf)
beaverEngine.replaceExpression(sympaConf, "(\ndomain\\s*).*", "\\1" + mainHost)
beaverEngine.replaceExpression(sympaConf, "(\nlistmaster\\s*).*", "\\1" + adminEmail)
beaverEngine.replaceExpression(sympaConf, "(\nwwsympa_url\\s*).*", "\\1" + "http://"+mainHost+"/sympa-default/sympa")
beaverEngine.replaceExpression(sympaConf, "(\nsoap_url\\s*).*", "\\1" + "http://"+mainHost+"/sympa-default/sympasoap")
beaverEngine.replaceExpression(sympaConf, "(\netc\\s*).*", "\\1" + home)
beaverEngine.replaceExpression(sympaConf, "(\ncreate_list\\s*).*", "\\1" + "novaforge")
beaverEngine.replaceExpression(sympaConf, "(\nhome\\s*).*", "\\1" + list_data)
beaverEngine.replaceExpression(sympaConf, "(\nmax_size\\s*).*", "\\1" + maxSize)
beaverEngine.replaceExpression(sympaConf, "(\ndb_name\\s*).*", "\\1" + database)
beaverEngine.replaceExpression(sympaConf, "(\ndb_user\\s*).*", "\\1" + sqlUser)
beaverEngine.replaceExpression(sympaConf, "(\ndb_passwd\\s*).*", "\\1" + sqlPwd)
beaverEngine.replaceExpression(sympaConf, "(\ndkim_signer_domain\\s*).*", "\\1" + mainHost)
beaverEngine.replaceExpression(sympaConf, "(\nhttp_host\\s*).*", "\\1" + "http://"+mainHost)
beaverEngine.replaceExpression(sympaConf, "(\nstatic_content_url\\s*).*", "\\1" + "/"+defaultAlias+"/static-sympa")
beaverEngine.replaceExpression(sympaConf, "(\nbounce_path\\s*).*", "\\1" + bounce)
beaverEngine.replaceExpression(sympaConf, "(\narc_path\\s*).*", "\\1" + arc)

def trusted_applications = home + "/trusted_applications.conf"
def md5password = DigestUtils.md5Hex(clientPwd);
beaverEngine.copyToFile(processTmpPath + "/resources/sympa/trusted_applications.conf", trusted_applications)
beaverEngine.replaceExpression(trusted_applications, "(\nname\\s*).*", "\\1" + clientAdmin)
beaverEngine.replaceExpression(trusted_applications, "(\nmd5password\\s*).*", "\\1" + md5password)

def topicsConf = home + "/topics.conf"
beaverEngine.copyToFile(processTmpPath + "/resources/sympa/topics.conf", topicsConf)

beaverEngine.setOwner(true, user, group, sympaConf)
beaverEngine.setOwner(true, user, group, trusted_applications)
beaverEngine.setOwner(true, user, group, topicsConf)

// Sendmail config
def sendmailConfig = beaverEngine.getResource("sendmail","configFile")

File sendmailConfigFile = new File(sendmailConfig)
boolean contains = false
if (beaverEngine.isSimulateMode() == false) {
	sendmailConfigFile.eachLine { line -> if (line.contains(aliases)) contains=true }
}
if (contains == false){
	def tokenBefore = "define(`ALIAS_FILE', `/etc/aliases')dnl"
	def tokenAfter = "define(`ALIAS_FILE', `/etc/aliases" + "," + aliases + "')dnl"
	beaverEngine.replaceElement(sendmailConfig, tokenBefore, tokenAfter)
}

// sympa robot aliases config
def robotAliases = beaverEngine.getResource("robotAliasesFile")
def hostname = beaverEngine.getResource("local:host")
beaverEngine.copyToFile(processTmpPath + "/resources/aliases/aliases", robotAliases)
beaverEngine.replaceElement(robotAliases, "@SYMPA_EXEC_DIR@", bin)
beaverEngine.replaceElement(robotAliases, "@ADMIN_EMAIL@", adminEmail)
beaverEngine.replaceElement(robotAliases, "@HOSTNAME@", hostname)

Path listAliasesTemplatePath = Paths.get(listAliasesTemplate)

if (Files.exists(listAliasesTemplatePath))
{
	beaverEngine.replaceExpression(listAliasesTemplate, execDir, bin)
}

// execute newaliases command
def newAliasesCommand = beaverEngine.getResource("newAliases")
beaverEngine.executeCommand(newAliasesCommand)

// reconfigure sendmail
def reconfigureCommand = beaverEngine.getResource("sendmail", "reconfigureCommand")
beaverEngine.executeCommand(reconfigureCommand)

// Create sympa database
def sqlFile = processTmpPath + "/resources/sql/sympa.sql"

String createdbCommand = "sympa.pl"
String[] createdbArgs = new String[1]
createdbArgs[0] = "--health_check"

beaverEngine.replaceElement(sqlFile, "@DATABASE@", database)
beaverEngine.replaceElement(sqlFile, "@MARIADB_HOST@", mariadbHost)
beaverEngine.replaceElement(sqlFile, "@DATABASE@", database)
beaverEngine.replaceElement(sqlFile, "@SYMPA_DB_USER@", sqlUser)
beaverEngine.replaceElement(sqlFile, "@SYMPA_DB_PWD@", sqlPwd)

def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
def mariadbUser = beaverEngine.getResource("mariadb", "rootUser")
def mariadbPwd = beaverEngine.getResource("mariadb", "rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def mariadbBin = beaverEngine.getResource("mariadb", "bin")
systemdService.startService(mariadbService)
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, sqlFile)
beaverEngine.executeCommand(createdbCommand, createdbArgs)
systemdService.stopService(mariadbService)