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

// Configure local.protected.php
def home = beaverEngine.getResource("home")
def httpdGroup = beaverEngine.getResource("httpd", "group")
def httpdUser = beaverEngine.getResource("httpd", "user")
def karafPort = beaverEngine.getResource("karaf", "port")
def casPort = beaverEngine.getResource("cas", "externalPort")
def localhost = beaverEngine.getResource("local:host")
def mariadbHost = beaverEngine.getResource("mariadb", "host")
def defaultAlias = beaverEngine.getResource("novaforge-connector-dokuwiki", "defaultAlias")
def toolAlias = beaverEngine.getResource("novaforge-connector-dokuwiki", "toolAlias")
def datadir = beaverEngine.getResource("datas")

localProtectedPhp = home + "/conf/local.protected.php"
beaverEngine.replaceElement(localProtectedPhp, "conf['basedir'] = '/dokuwiki-default/dokuwiki';", "conf['basedir'] = '/" + defaultAlias + "/" + toolAlias + "';")
beaverEngine.replaceElement(localProtectedPhp, "conf['plugin']['authplaincas']['serverBDD']   = 'localhost';", "conf['plugin']['authplaincas']['serverBDD']   = '" + mariadbHost + "';")
beaverEngine.replaceElement(localProtectedPhp, "conf['plugin']['authplaincas']['server'] = 'novadev';", "conf['plugin']['authplaincas']['server'] = '" + localhost + "';")
beaverEngine.replaceElement(localProtectedPhp, "conf['plugin']['authplaincas']['port'] = '443';", "conf['plugin']['authplaincas']['port'] = " + casPort + ";")
beaverEngine.replaceElement(localProtectedPhp, "http://127.0.0.1:8181/cxf/", "http://127.0.0.1:" + karafPort + "/cxf/")

// configure datadir
def localPhp = processTmpPath + "/resources/conf/local.php"
beaverEngine.copyToFile(localPhp, home + "/conf/local.php")
beaverEngine.replaceElement(home + "/conf/local.php", "@DATA_DIR@", datadir)

// Copy and configure HTTPD dokuwiki.service local
def dokuwikiService = processTmpPath + "/resources/dokuwiki.service"
def httpService = beaverEngine.getResource("httpService")
beaverEngine.copyToFile(dokuwikiService, httpService)
beaverEngine.replaceElement(httpService, "@DEFAULT_ALIAS@", defaultAlias)
beaverEngine.replaceElement(httpService, "@TOOL_ALIAS@", toolAlias)
beaverEngine.replaceElement(httpService, "@HOME@", home)

// Set owner and rights
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(false, localGroup, localUser, httpService)
beaverEngine.setPermissionsOnFiles(false,"664",httpService)
beaverEngine.setOwner(true, httpdGroup, httpdUser, home)
beaverEngine.setPermissionsOnFiles(true,"644", home)
beaverEngine.setOwner(true, httpdGroup, httpdUser, datadir)

// Set specific permissions on data and conf directories
beaverEngine.setPermissionsOnDirectories(true,"777", home + "/conf")
beaverEngine.setPermissionsOnDirectories(true,"777", datadir)
beaverEngine.setPermissionsOnFiles(true,"666", home + "/conf")