/**
 * Copyright (c) 2011-2016, BULL SAS, NovaForge Version 3 and above.
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
 
import java.nio.file.Files 
import java.nio.file.Path 
import java.nio.file.Paths 
 
BeaverEngine beaverEngine = engine 
 
// Server variables 
def localUser = beaverEngine.getResource("local:user") 
def localGroup = beaverEngine.getResource("local:group") 
 
// Jenkins variables 
def jenkinsHome = beaverEngine.getResource("home") 
def jenkinsLogs = beaverEngine.getResource("logs") 
def jenkinsDatas = beaverEngine.getResource("datas") 
def jenkinsTmp = beaverEngine.getResource("tmp") 
def clientPort = beaverEngine.getResource("main", "novaforge-connector-jenkins", "clientPort") 
def defaultAlias = beaverEngine.getResource("main", "novaforge-connector-jenkins", "defaultAlias") 
 
// Cas configuration 
def jenkinsConfXml = jenkinsDatas + "/config.xml" 
def casBaseUrl = beaverEngine.getResource("main", "cas", "baseUrl") 
def casLogoutUri = beaverEngine.getResource("main", "cas", "logoutUri") 
def casLoginUri = beaverEngine.getResource("main", "cas", "loginUri") 
beaverEngine.replaceElement(jenkinsConfXml, "https://localhost/cas/", casBaseUrl + "/") 

// JDK configuration
def java7Home = beaverEngine.getResource("jdk7","home")
def java8Home = beaverEngine.getResource("jdk8","home")
beaverEngine.replaceElement(jenkinsConfXml, "@JDK7_HOME@", java7Home) 
beaverEngine.replaceElement(jenkinsConfXml, "@JDK8_HOME@", java8Home) 
 
// SMTP Configuration 
def mailConfXml = jenkinsDatas + "/hudson.tasks.Mailer.xml" 
def smtpHost = beaverEngine.getResource("main","smtp","host") 
def smtpUsername = beaverEngine.getResource("main", "smtp","username") 
def smtpPassword = beaverEngine.getResource("main", "smtp","password") 
def smtpNoReply = beaverEngine.getResource("main", "smtp","noReply") 
 
beaverEngine.replaceExpression(mailConfXml, "<smtpHost>(.*)</smtpHost>", "<smtpHost>" + smtpHost + "</smtpHost>") 
beaverEngine.replaceExpression(mailConfXml, "<smtpAuthUsername>(.*)</smtpAuthUsername>", "<smtpAuthUsername>" + smtpUsername + "</smtpAuthUsername>") 
beaverEngine.replaceExpression(mailConfXml, "<smtpAuthPassword>(.*)</smtpAuthPassword>", "<smtpAuthPassword>" + smtpPassword + "</smtpAuthPassword>") 
beaverEngine.replaceExpression(mailConfXml, "<adminAddress>(.*)</adminAddress>", "<adminAddress>" + smtpNoReply + "</adminAddress>") 
 
// SSL Configuration 
beaverEngine.replaceExpression(mailConfXml, "<useSsl>(.*)</useSsl>", "<useSsl>true</useSsl>") 
 
// Base Configuration 
def baseUrl = beaverEngine.getResource("main", "httpd", "baseUrl") 
def server = beaverEngine.getServerId() 
def jenkinsAliasJSON = beaverEngine.parseJson(defaultAlias); 
if ( jenkinsAliasJSON in Map ) 
{ 
  defaultAlias = jenkinsAliasJSON.get(server); 
} 
beaverEngine.replaceExpression(mailConfXml, "<hudsonUrl>(.*)</hudsonUrl>", "<hudsonUrl>" + baseUrl + "/" + defaultAlias + "</hudsonUrl>") 
 
// Maven configuration 
def mavenInstallation = jenkinsDatas + "/tools/hudson.tasks.Maven_MavenInstallation" 
beaverEngine.createDirectory(mavenInstallation) 
 
def globalMavenConfig = jenkinsDatas + "/jenkins.mvn.GlobalMavenConfig.xml" 
beaverEngine.copyToFile(processTmpPath + "/resources/plugins/jenkins.mvn.GlobalMavenConfig.xml", globalMavenConfig) 
beaverEngine.replaceElement(globalMavenConfig, "@JENKINS_M2_GLOBAL@", mavenInstallation) 
 
def m2Home = "/home/" + localUser + "/.m2" 
beaverEngine.createDirectory(m2Home) 
def settingsSecurity = m2Home + "/settings-security.xml" 
beaverEngine.copyToFile(processTmpPath + "/resources/plugins/settings-security.xml", settingsSecurity) 
beaverEngine.replaceElement(settingsSecurity, "@JENKINS_M2_GLOBAL@", mavenInstallation) 
 
def settings = mavenInstallation + "/settings.xml" 
beaverEngine.copyToFile(processTmpPath + "/resources/plugins/settings.xml", settings) 
beaverEngine.replaceElement(settings, "@JENKINS_M2_GLOBAL@", mavenInstallation) 
beaverEngine.replaceElement(settings, "@BASE_URL@", baseUrl) 
def nexusDefaultAlias = beaverEngine.getResource("main", "novaforge-connector-nexus", "defaultAlias") 
def nexusToolAlias = beaverEngine.getResource("main", "novaforge-connector-nexus", "toolAlias") 
beaverEngine.replaceElement(settings, "@NEXUS_DEFAULT@", nexusDefaultAlias) 
beaverEngine.replaceElement(settings, "@NEXUS_TOOL@", nexusToolAlias) 

// Maven installation
//  Deploy the maven /livraison/package/bin/apache-maven-3.0.5-bin.tar.gz to @JENKINS_M2_GLOBAL@
beaverEngine.unpackFile("/livraison/package/bin/apache-maven-3.0.5-bin.tar.gz",mavenInstallation)
beaverEngine.setPermissionsOnFiles(true, "755", mavenInstallation+"/apache-maven-3.0.5/bin") 
 
// Execute configuration 
def jksPwd = beaverEngine.getResource("certificat", "jksPwd") 
def jks = beaverEngine.getResource("certificat", "jks") 
def jreHome = beaverEngine.getResource("jre","home") 
def jenkinsBin = beaverEngine.getResource("bin") 
beaverEngine.copyToFile(processTmpPath + "/resources/jenkins", jenkinsBin) 

beaverEngine.replaceElement(jenkinsBin, "@JENKINS_HOME@", jenkinsHome) 
beaverEngine.replaceElement(jenkinsBin, "@JENKINS_DATAS@", jenkinsDatas) 
beaverEngine.replaceElement(jenkinsBin, "@JENKINS_LOGS@", jenkinsLogs) 
beaverEngine.replaceElement(jenkinsBin, "@JENKINS_TMP@", jenkinsTmp) 
beaverEngine.replaceElement(jenkinsBin, "@JENKINS_PORT@", clientPort) 
beaverEngine.replaceElement(jenkinsBin, "@DEFAULT_ALIAS@", defaultAlias) 
beaverEngine.replaceElement(jenkinsBin, "@JAVA_HOME@", jreHome) 
beaverEngine.replaceElement(jenkinsBin, "@JKS_PATH@", jks) 
beaverEngine.replaceElement(jenkinsBin, "@JKS_PWD@", jksPwd) 
 
// Service configuration 
def jenkinsService = processTmpPath + "/resources/jenkins.service" 
beaverEngine.replaceElement(jenkinsService, "@JENKINS_BIN@", jenkinsBin) 
beaverEngine.replaceElement(jenkinsService, "@NOVAFORGE_USER@", localUser) 
beaverEngine.replaceElement(jenkinsService, "@NOVAFORGE_GROUP@", localGroup) 
def jenkinsSystemd = beaverEngine.getResource("systemdService") 
SystemdService systemdService = beaverEngine.getSystemdService() 
systemdService.addService("multi-user.target",jenkinsService, jenkinsSystemd, true); 
 
// Define owner 
beaverEngine.setOwner(false, localGroup, localUser, jenkinsBin) 
beaverEngine.setPermissionsOnFiles(false, "755", jenkinsBin) 
beaverEngine.setOwner(true,localGroup,localUser,jenkinsHome) 
beaverEngine.setOwner(true,localGroup,localUser,jenkinsDatas) 
beaverEngine.setOwner(true,localGroup,localUser,jenkinsLogs) 
beaverEngine.setOwner(true,localGroup,localUser,jenkinsTmp) 

