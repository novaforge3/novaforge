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
import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService

BeaverEngine beaverEngine = engine
UserService userService = beaverEngine.getUserService();

// Configure my.conf
def mariadbConf = "/etc/my.cnf"
def confd = beaverEngine.getResource("confd")
beaverEngine.move(processTmpPath + "/resources/custom_my.cnf", mariadbConf)
beaverEngine.replaceElement(mariadbConf, "@MARIADB_CONFD@", confd)

// Configure novaforge.cnf
def novaforgeConf = beaverEngine.getResource("novaforgeConf")
def user = beaverEngine.getResource("user")
def datas = beaverEngine.getResource("datas")
def logs = beaverEngine.getResource("logs")
def port = beaverEngine.getResource("port")
def socket = beaverEngine.getResource("socket")
def maxConnection = beaverEngine.getResource("maxConnection")
def dumpMaxAllowed = beaverEngine.getResource("dumpMaxAllowed")
def maxAllowed = beaverEngine.getResource("maxAllowed")

beaverEngine.copyToFile(processTmpPath + "/resources/novaforge.cnf" ,novaforgeConf)
beaverEngine.replaceElement(novaforgeConf,"@MARIADB_DATAS@", datas)
beaverEngine.replaceElement(novaforgeConf,"@MARIADB_LOGS@", logs)
beaverEngine.replaceElement(novaforgeConf,"@MARIADB_PORT@", port)
beaverEngine.replaceElement(novaforgeConf,"@MARIADB_SOCKET@", socket)
beaverEngine.replaceElement(novaforgeConf,"@MARIADB_MAXCON@", maxConnection)
beaverEngine.replaceElement(novaforgeConf,"@DUMP_MAX_ALLOWED@", dumpMaxAllowed)
beaverEngine.replaceElement(novaforgeConf,"@MAX_ALLOWED@", maxAllowed)
beaverEngine.replaceElement(novaforgeConf,"@MARIADB_USER@", user)

// Configure mariadb user
def localUser = beaverEngine.getResource("local:user")
def group = beaverEngine.getResource("group")
if (user.equals(localUser) == false){
  userService.addGroupsToUser(localUser, group);
}
// Set owner
def home = beaverEngine.getResource("home")
beaverEngine.setOwner(true, group, user, home)
beaverEngine.setOwner(true, group, user, datas)
beaverEngine.setOwner(true, group, user, logs)

// Configure mariadb
beaverEngine.executeCommand("mysql_install_db","--user=" + user,"--datadir=" + datas)

def rootUser = beaverEngine.getResource("rootUser")
def rootPwd = beaverEngine.getResource("rootPwd")
def resetPasswordSql = processTmpPath + "/resources/resetPassword.sql"

beaverEngine.replaceElement(resetPasswordSql,"@USERNAME@", rootUser)
def resetPasswordScript = processTmpPath + "/resources/resetPassword.sh"
beaverEngine.setPermissions(false, "755", resetPasswordScript)
beaverEngine.executeScript(resetPasswordScript)

def mariadbService = beaverEngine.getResource("systemdService")
SystemdService systemdService = beaverEngine.getSystemdService()
systemdService.startService(mariadbService)
beaverEngine.executeCommand("mysqladmin","-u", rootUser, "password", "'" +rootPwd+ "'")
systemdService.stopService(mariadbService)
