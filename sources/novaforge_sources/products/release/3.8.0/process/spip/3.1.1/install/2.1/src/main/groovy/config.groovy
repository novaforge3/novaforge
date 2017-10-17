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
def home = beaverEngine.getResource("home")
def datas = beaverEngine.getResource("datas")
def logs = beaverEngine.getResource("logs")
def localhost = beaverEngine.getResource("local:host")
def httpdGroup = beaverEngine.getResource("httpd", "group")
def httpdUser = beaverEngine.getResource("httpd", "user")
def defaultAlias = beaverEngine.getResource("novaforge-connector-spip", "defaultAlias")
def toolAlias = beaverEngine.getResource("novaforge-connector-spip", "toolAlias")
def sqlUser = beaverEngine.getResource("sqlUser")
def sqlPwd = beaverEngine.getResource("sqlPwd")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def adminLogin = beaverEngine.getResource("adminLogin")
def adminPwd = beaverEngine.getResource("adminPwd")
def adminName = beaverEngine.getResource("adminName")
def adminEmail = beaverEngine.getResource("novaforge-connector-forge", "adminEmail")
def karafPort = beaverEngine.getResource("karaf","port")
def casUri = beaverEngine.getResource("main", "cas", "uri")
def casExternalPort = beaverEngine.getResource("main", "cas", "externalPort")

// Configure .htaccess
def htAccessSource = processTmpPath + "/resources/files/.htaccess"
def htAccessTarget = home + "/.htaccess"
beaverEngine.copyToFile(htAccessSource, htAccessTarget)
beaverEngine.replaceElement(htAccessTarget, "@DEFAULT_ALIAS@", defaultAlias)
beaverEngine.replaceElement(htAccessTarget, "@TOOL_ALIAS@", toolAlias)

// Configure nf_forge_authentification.php
def nfForgeAuthenticationSource = processTmpPath + "/resources/files/nf_forge_authentification.php"
def nfForgeAuthenticationTarget = home + "/ecrire/nf_forge_authentification.php"
beaverEngine.copyToFile(nfForgeAuthenticationSource, nfForgeAuthenticationTarget)
beaverEngine.replaceElement(nfForgeAuthenticationTarget, "@DEFAULT_ALIAS@", defaultAlias)
beaverEngine.replaceElement(nfForgeAuthenticationTarget, "@TOOL_ALIAS@", toolAlias)

// Configure nf_config_inc.php
def configIncSource = processTmpPath + "/resources/files/nf_config_inc.php"
def configIncTarget = home + "/ecrire/nf_config_inc.php"
beaverEngine.copyToFile(configIncSource, configIncTarget)
beaverEngine.replaceElement(configIncTarget, "@HOSTNAME@", localhost)
beaverEngine.replaceElement(configIncTarget, "@MARIADB_PORT@", mariadbPort)
beaverEngine.replaceElement(configIncTarget, "@MARIADB_SPIP_USER@", sqlUser)
beaverEngine.replaceElement(configIncTarget, "@MARIADB_SPIP_PWD@", sqlPwd)
beaverEngine.replaceElement(configIncTarget, "@SPIP_ADMIN_LOGIN@", adminLogin)
beaverEngine.replaceElement(configIncTarget, "@SPIP_ADMIN_PWD@", adminPwd)
beaverEngine.replaceElement(configIncTarget, "@SPIP_ADMIN_NAME@", adminName)
beaverEngine.replaceElement(configIncTarget, "@SPIP_ADMIN_EMAIL@", adminEmail)
beaverEngine.replaceElement(configIncTarget, "@KARAF_PORT@", karafPort)

// Configure _config_cas.php
def configCasSource = processTmpPath + "/resources/files/_config_cas.php"
def configCasTarget = home + "/config/_config_cas.php"
beaverEngine.copyToFile(configCasSource, configCasTarget)
beaverEngine.replaceElement(configCasTarget,"@CAS_HOST@",localhost)
beaverEngine.replaceElement(configCasTarget,"@CAS_PORT@",casExternalPort)
beaverEngine.replaceElement(configCasTarget,"@CAS_URI@",casUri)

// Copy and configure HTTPD spip.service local
def spipService = processTmpPath + "/resources/spip.service"
def httpService = beaverEngine.getResource("httpService")
beaverEngine.copyToFile(spipService, httpService)
beaverEngine.replaceElement(httpService, "@DEFAULT_ALIAS@", defaultAlias)
beaverEngine.replaceElement(httpService, "@TOOL_ALIAS@", toolAlias)
beaverEngine.replaceElement(httpService, "@HOME@", home)

// Set owner and rights
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(false, localGroup, localUser, httpService)
beaverEngine.setPermissionsOnFiles(false,"664",httpService)
beaverEngine.setOwner(true, httpdGroup, httpdUser, home)
beaverEngine.setOwner(true, httpdGroup, httpdUser, logs)
beaverEngine.setOwner(true, httpdGroup, httpdUser, datas)