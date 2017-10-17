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

import org.apache.commons.codec.digest.DigestUtils
import org.codehaus.plexus.util.StringUtils
import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

BeaverEngine beaverEngine = engine

def escapePropertieValue(value) {
  def escp = value

  // Escape simple backslash (not double backslash nor \\u)
  escp = escp.replaceAll("([^\\\\]|^)\\\\([^\\\\u]|\$)", "\$1\\\\\\\\\\\\\\\\\$2")
  // Escape double backslash
  escp = escp.replaceAll("\\\\", "\\\\\\\\\\\\\\\\")
  // Escape comma and double quote
  escp = escp.replaceAll("([\",])", "\\\\\\\\\\\\\\\\\$1")
}

// CFG diretories
def forgeConf = beaverEngine.getResource("novaforge-runtime","forgeConf")
def pluginConf = beaverEngine.getResource("novaforge-runtime","pluginsConf")
def datasourceConf = beaverEngine.getResource("novaforge-runtime","datasourceConf")

// Repository information
def karafRepository = beaverEngine.getResource("karaf","repository")
def dataVersion = beaverEngine.getDataVersion()
def modulesRepository = karafRepository + "/org/novaforge/forge/modules/"

////////////////////////////////////////////////////////////////////
//////////////// NovaForge Core Configuration
////////////////////////////////////////////////////////////////////

def forgeProperties = modulesRepository + "novaforge-configuration-impl/" + dataVersion + "/novaforge-configuration-impl-" + dataVersion + ".properties"

// KARAF Configuration
def localHost = beaverEngine.getResource("local:host")
def karafPort = beaverEngine.getResource("karaf","port")
def cxfAlias = beaverEngine.getResource("karaf","cxfAlias")
def baseUrl = beaverEngine.getResource("httpd","baseUrl")
def portalUri = beaverEngine.getResource("portalUri")

beaverEngine.replaceElement(forgeProperties, "@PUBLIC_URL@", baseUrl)
beaverEngine.replaceElement(forgeProperties, "@KARAF_PORT@", karafPort)
beaverEngine.replaceElement(forgeProperties, "@SERVER_HOST@", localHost)
beaverEngine.replaceElement(forgeProperties, "@CXF_ALIAS@", cxfAlias)
if (StringUtils.isNotBlank(portalUri)) {
  if (portalUri.startsWith("/") == false) {
    portalUri = "/" + portalUri
  }
  beaverEngine.replaceExpression(forgeProperties, "portalEntryPoint=(.*)", "portalEntryPoint=" + portalUri)
}

// CAS Configuration
def casBaseUrl = beaverEngine.getResource("main", "cas","baseUrl")
beaverEngine.replaceElement(forgeProperties, "@CAS_URL@", casBaseUrl)

// Global configuration
def userLoginGenerated = beaverEngine.getResource("userLoginGenerated")
if (StringUtils.isNotBlank(userLoginGenerated)) {
  beaverEngine.replaceExpression(forgeProperties, "userLoginGenerated=(.*)", "userLoginGenerated=" + userLoginGenerated)
}
def passwordValidationRegEx = beaverEngine.getResource("passwordValidationRegEx")
if (StringUtils.isNotBlank(passwordValidationRegEx)) {
  def passwordValidationRegExEsc = escapePropertieValue(passwordValidationRegEx)
  beaverEngine.replaceExpression(forgeProperties, "passwordValidationRegEx=(.*)", "passwordValidationRegEx=" + passwordValidationRegExEsc)
}
def passwordLifeTime = beaverEngine.getResource("passwordLifeTime")
if (StringUtils.isNotBlank(passwordLifeTime)) {
  beaverEngine.replaceExpression(forgeProperties, "passwordLifeTime=(.*)", "passwordLifeTime=" + passwordLifeTime)
}
def passwordModificationTime = beaverEngine.getResource("passwordModificationTime")
if (StringUtils.isNotBlank(passwordModificationTime)) {
  beaverEngine.replaceExpression(forgeProperties, "passwordModificationTime=(.*)", "passwordModificationTime=" + passwordModificationTime)
}

// admin login configuration for forbidden logins
def adminLogin = beaverEngine.getResource("adminLogin")
def adminForbiddenLogins = beaverEngine.getResource("adminForbiddenLogins")
beaverEngine.replaceElement(forgeProperties, "@LOGIN_ADMIN_FORGE@", adminLogin)
beaverEngine.replaceElement(forgeProperties, "@FORBIDDEN_LOGINS@", adminForbiddenLogins)

// SMTP Configuration
def smtpHost = beaverEngine.getResource("smtp","host")
def smtpPort = beaverEngine.getResource("smtp","port")
def smtpUsername = beaverEngine.getResource("smtp","username")
def smtpPassword = beaverEngine.getResource("smtp","password")
def smtpNoReply = beaverEngine.getResource("smtp","noReply")
def adminEmail = beaverEngine.getResource("adminEmail")

beaverEngine.replaceElement(forgeProperties, "@SMTP_HOST@", smtpHost)
beaverEngine.replaceElement(forgeProperties, "@SMTP_PORT@",  smtpPort)
beaverEngine.replaceElement(forgeProperties, "@SMTP_USERNAME@", smtpUsername)
beaverEngine.replaceElement(forgeProperties, "@SMTP_PASSWORD@", smtpPassword)
beaverEngine.replaceElement(forgeProperties, "@SMTP_NOREPLY@", smtpNoReply)
beaverEngine.replaceElement(forgeProperties, "@MAIL_SYSTEM_FORGE@", adminEmail)

// Portail configuration
def portalFooter = beaverEngine.getResource("portalFooter")
if (StringUtils.isNotBlank(portalFooter)) {
  def portalFooterEsc = escapePropertieValue(portalFooter)
  beaverEngine.replaceExpression(forgeProperties, "portalFooter=(.*)", "portalFooter=" + portalFooterEsc)
}
def portalFooterUrl = beaverEngine.getResource("portalFooterUrl")
if (StringUtils.isNotBlank(portalFooterUrl)) {
  beaverEngine.replaceExpression(forgeProperties, "portalFooterWebSite=(.*)", "portalFooterWebSite=" + portalFooterUrl)
}
def uploadMaxSize = beaverEngine.getResource("uploadMaxSize")
if (StringUtils.isNotBlank(uploadMaxSize)) {
  beaverEngine.replaceExpression(forgeProperties, "uploadMaxSize=(.*)", "uploadMaxSize=" + uploadMaxSize)
}

def novaforgeProperties = forgeConf + "/novaforge.properties"
beaverEngine.copyToFile(forgeProperties, novaforgeProperties)

////////////////////////////////////////////////////////////////////
//////////////// NovaForge Initialization
////////////////////////////////////////////////////////////////////

// NovaForge Initialization
def iniProperties = modulesRepository + "novaforge-initialization/" + dataVersion + "/novaforge-initialization-" + dataVersion + ".properties"

// Forge project configuration
def forgeId = beaverEngine.getResource("forgeId")
def forgeName = beaverEngine.getResource("forgeName")
def forgeDescription = beaverEngine.getResource("forgeDescription")
def forgeLicence = beaverEngine.getResource("forgeLicence")

beaverEngine.replaceExpression(iniProperties, "forgeProjectId=(.*)", "forgeProjectId=" + forgeId)
beaverEngine.replaceExpression(iniProperties, "forgeProjectName=(.*)", "forgeProjectName=" + forgeName)
beaverEngine.replaceExpression(iniProperties, "forgeProjectDescription=(.*)", "forgeProjectDescription=" + forgeDescription)
beaverEngine.replaceExpression(iniProperties, "forgeProjectLicence=(.*)", "forgeProjectLicence=" + forgeLicence)

// Forge roles configuration
def forgeRoleMember = beaverEngine.getResource("forgeRoleMember")
def forgeRoleAdministrator = beaverEngine.getResource("forgeRoleAdministrator")
def forgeRoleSuperAdministrator = beaverEngine.getResource("forgeRoleSuperAdministrator")
beaverEngine.replaceExpression(iniProperties, "forgeMemberRoleName=(.*)", "forgeMemberRoleName=" + forgeRoleMember)
beaverEngine.replaceExpression(iniProperties, "forgeAdministratorRoleName=(.*)", "forgeAdministratorRoleName=" + forgeRoleAdministrator)
beaverEngine.replaceExpression(iniProperties, "forgeSuperAdministratorRoleName=(.*)", "forgeSuperAdministratorRoleName=" + forgeRoleSuperAdministrator)

// Admin configuration
def adminPwd = beaverEngine.getResource("adminPwd")
String sha1password = DigestUtils.sha1Hex(adminPwd);
def adminName = beaverEngine.getResource("adminName")
def adminFirstName = beaverEngine.getResource("adminFirstName")
def adminLanguage = beaverEngine.getResource("adminLanguage")

beaverEngine.replaceElement(iniProperties, "@LOGIN_ADMIN_FORGE@", adminLogin)
beaverEngine.replaceElement(iniProperties, "@PASSWORD_ADMIN@", sha1password)
beaverEngine.replaceElement(iniProperties, "@MAIL_ADMIN_FORGE@", adminEmail)
beaverEngine.replaceExpression(iniProperties, "forgeSuperAdministratorFirstName=(.*)", "forgeSuperAdministratorFirstName=" + adminFirstName)
beaverEngine.replaceExpression(iniProperties, "forgeSuperAdministratorName=(.*)", "forgeSuperAdministratorName=" + adminName)
beaverEngine.replaceExpression(iniProperties, "forgeSuperAdministratorLanguage=(.*)", "forgeSuperAdministratorLanguage=" + adminLanguage)

def novaforgeInitProperties = forgeConf + "/novaforge-init.properties"
beaverEngine.copyToFile(iniProperties, novaforgeInitProperties)

////////////////////////////////////////////////////////////////////
//////////////// NovaForge Vaadin Theme
////////////////////////////////////////////////////////////////////

// Retrieve vaadin theme jar
def portalTheme = karafRepository + "/org/novaforge/forge/modules/novaforge-ui-vaadin-theme/" + dataVersion + "/novaforge-ui-vaadin-theme-" + dataVersion
def portalThemeJar = portalTheme + ".jar"

//unpack actual vaadin jar
beaverEngine.unpackFile(portalThemeJar, portalTheme)

def portalVaadinTheme = portalTheme + "/VAADIN/themes/"

new File(portalVaadinTheme).eachFile() { currentTheme ->

  if (currentTheme.isDirectory()) {
    def currentThemePath = currentTheme.getAbsolutePath()

    //copy favicon.ico logo to vaadin theme
    def faviconLogo = beaverEngine.getResource("logoFavIcon")
    if (StringUtils.isNotBlank(faviconLogo)) {
      final Path faviconLogoPath = Paths.get(faviconLogo);
      if (Files.exists(faviconLogoPath)) {
        beaverEngine.copyToFile(faviconLogo, currentThemePath + "/favicon.ico")
      }
    }

    //copy logoHeader to vaadin theme
    def logoHeader = beaverEngine.getResource("logoHeader")
    if (StringUtils.isNotBlank(logoHeader)) {
      final Path logoHeaderPath = Paths.get(logoHeader);
      if (Files.exists(logoHeaderPath)) {
        beaverEngine.copyToFile(logoHeader, currentThemePath + "/modules/header/logo_novaforge.png")
      }
    }

    //copy logoFooter to vaadin theme
    def logoFooter = beaverEngine.getResource("logoFooter")
    if (StringUtils.isNotBlank(logoFooter)) {
      final Path logoFooterPath = Paths.get(logoFooter);
      if (Files.exists(logoFooterPath)) {
        beaverEngine.copyToFile(logoFooter, currentThemePath + "/modules/footer/logo_company.png")
      }
    }
  }

}

//pack actual vaadin jar
beaverEngine.pack(portalTheme, portalThemeJar)

//Clean tmp vaadin jar
beaverEngine.delete(portalTheme)

////////////////////////////////////////////////////////////////////
//////////////// NovaForge Vaadin Licences
////////////////////////////////////////////////////////////////////

// Add vaadin charts license
def vaadinLicenseKey = "-Dvaadin.charts.developer.license"
def vaadinLicenseValue = beaverEngine.getResource("vaadinChartLicense")
def karafBin = beaverEngine.getResource("karaf","bin")
def setEnv = karafBin + "/setenv"
def vaadinLicenseFull = beaverEngine.getValueFromRegex(setEnv, vaadinLicenseKey + "=(.*) ")
if (StringUtils.isNotBlank(vaadinLicenseFull)) {
  def existingLicense = vaadinLicenseFull.substring(vaadinLicenseFull.indexOf('=') + 1, vaadinLicenseFull.length() - 1)
  beaverEngine.replaceElement(setEnv, existingLicense, vaadinLicenseValue)
} else {
  beaverEngine.replaceElement(setEnv, "KARAF_OPTS=\"", "KARAF_OPTS=\"" + vaadinLicenseKey + "=" + vaadinLicenseValue + " ")
}

////////////////////////////////////////////////////////////////////
//////////////// NovaForge Datasource
////////////////////////////////////////////////////////////////////

// Datasource information
def mariadbHost = beaverEngine.getResource("mariadb","host")
def mariadbPort = beaverEngine.getResource("mariadb","port")
def datasourceUser = beaverEngine.getResource("novaforge-runtime","datasourceUser")
def datasourcePwd = beaverEngine.getResource("novaforge-runtime","datasourcePwd")

// NovaForge Datasource
def repoDatasourceXml = modulesRepository + "novaforge-core-organization-dao/" + dataVersion + "/novaforge-core-organization-dao-" + dataVersion + "-datasource.xml"
def karafDeploy = beaverEngine.getResource("karaf","deploy")
def datasourceXml = karafDeploy + "/datasource.novaforge.xml"
beaverEngine.copyToFile(repoDatasourceXml, datasourceXml)

// Configure novaforge datasource
def repoDatasourceCfg = modulesRepository + "novaforge-core-organization-dao/" + dataVersion + "/novaforge-core-organization-dao-" + dataVersion + "-datasource.cfg"
beaverEngine.replaceElement(repoDatasourceCfg, "localhost", mariadbHost)
beaverEngine.replaceElement(repoDatasourceCfg, "3306", mariadbPort)
beaverEngine.replaceElement(repoDatasourceCfg, "username=root", "username=" + datasourceUser)
beaverEngine.replaceElement(repoDatasourceCfg, "password=root", "password=" + datasourcePwd)

// Copy datasource cfg to novaforge runtime
beaverEngine.copyToFile(repoDatasourceCfg, datasourceConf + "/datasource.novaforge.cfg")

////////////////////////////////////////////////////////////////////
//////////////// Webserver Configuration
////////////////////////////////////////////////////////////////////
def webserverCfg = modulesRepository + "novaforge-commons-webserver-configuration-impl/" + dataVersion + "/novaforge-commons-webserver-configuration-impl-" + dataVersion + ".cfg"
def servicesProxy = beaverEngine.getResource("httpd", "servicesProxy")
beaverEngine.replaceElement(webserverCfg, "@HTTPD_SERVICE_PROXY@", servicesProxy)
// Sudoers settings
def sudoers = processTmpPath + "/resources/apachectl.sudoers"
def localUser = beaverEngine.getResource("local:user")
beaverEngine.replaceElement(sudoers, "@NOVAFORGE_USER@", localUser)
beaverEngine.copyToFile(sudoers, "/etc/sudoers.d/novaforge_apachectl")


