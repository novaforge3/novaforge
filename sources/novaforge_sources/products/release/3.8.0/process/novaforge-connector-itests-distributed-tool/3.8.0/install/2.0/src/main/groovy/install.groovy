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
import org.novaforge.beaver.deployment.plugin.deploy.engine.SystemdService

BeaverEngine beaverEngine = engine

//////////////////////////////////////////////////
// grant remote DB access privilege for root user
//////////////////////////////////////////////////
def sqlFile = processTmpPath + "/resources/add_remote_access_for_root.sql"
def mariadbUser = beaverEngine.getResource("mariadb", "rootUser")
def mariadbPwd = beaverEngine.getResource("mariadb", "rootPwd")
def mariadbPort = beaverEngine.getResource("mariadb", "port")
def mariadbBin = beaverEngine.getResource("mariadb", "bin")
SystemdService systemdService = beaverEngine.getSystemdService()
def mariadbService = beaverEngine.getResource("mariadb", "systemdService")
systemdService.startService(mariadbService)
beaverEngine.executeMysqlScript(mariadbBin,mariadbPort, mariadbUser, mariadbPwd, sqlFile)
systemdService.stopService(mariadbService)