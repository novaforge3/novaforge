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
import org.novaforge.beaver.deployment.plugin.deploy.engine.RepositoryService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// Install RPMs
BeaverEngine beaverEngine = engine
RepositoryService repositoryService = engine.getRepositoryService()
repositoryService.installRPMs(dataFile, "subversion", "mod_dav_svn", "apr-util-mysql")

// Create directories
def datas = beaverEngine.getResource("datas")
beaverEngine.createDirectory(datas)

// Copy HTTPD mod_authz_svn_db_mysql
def httpEtcModule = beaverEngine.getResource("httpEtcModule")
beaverEngine.unpackFile(dataFile, processTmpPath)
def modAuthz = processTmpPath + "/" + "mod_authz_svn_db_mysql.so"
Path httpEtcModulePath = Paths.get(httpEtcModule);
if (Files.exists(httpEtcModulePath) == false)
{
	beaverEngine.copyToFile(modAuthz, httpEtcModule)
	beaverEngine.setPermissionsOnFiles(false,"755",httpEtcModule)
}

// Remove default RPM's configuration installation
def defaultConfModule = beaverEngine.getResource("defaultConfModule")
Path defaultConfModulePath = Paths.get(defaultConfModule);
if (Files.exists(defaultConfModulePath))
{
	beaverEngine.delete(defaultConfModule)
}

// Set owner and rights
def localGroup = beaverEngine.getResource("local:group")
def localUser = beaverEngine.getResource("local:user")
beaverEngine.setOwner(true, localGroup, localUser, datas)
beaverEngine.setPermissionsOnFiles(true,"775",datas)