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

BeaverEngine beaverEngine = engine
RepositoryService repositoryService = engine.getRepositoryService()

// Remove previous mod_ssl
repositoryService.removeRPMs("mod_ssl")
// Install new RPMs
repositoryService.installRPMs(dataFile, "httpd", "httpd-tools")

def home = beaverEngine.getResource("home")
def confd = beaverEngine.getResource("confd")
def servicesProxy = beaverEngine.getResource("servicesProxy")
def servicesLocal = beaverEngine.getResource("servicesLocal")
def modules = beaverEngine.getResource("modules")
def logs = beaverEngine.getResource("logs")

Path homePath = Paths.get(home)
if (Files.exists(homePath))
{
  beaverEngine.delete(home)
}
beaverEngine.createDirectory(home)

Path confdPath = Paths.get(confd)
if (Files.exists(confdPath))
{
  beaverEngine.delete(confd)
}
beaverEngine.createDirectory(confd)

Path servicesProxyPath = Paths.get(servicesProxy)
if (Files.exists(servicesProxyPath))
{
  beaverEngine.delete(servicesProxy)
}
beaverEngine.createDirectory(servicesProxy)

Path servicesLocalPath = Paths.get(servicesLocal)
if (Files.exists(servicesLocalPath))
{
  beaverEngine.delete(servicesLocal)
}
beaverEngine.createDirectory(servicesLocal)

Path modulesPath = Paths.get(modules)
if (Files.exists(modulesPath))
{
  beaverEngine.delete(modules)
}
beaverEngine.createDirectory(modules)

Path logsPath = Paths.get(logs)
if (Files.exists(logsPath))
{
  beaverEngine.delete(logs)
}
beaverEngine.createDirectory(logs)

// Set permission to rwxrwxr-x
beaverEngine.setPermissions(false, "775", servicesProxy)
beaverEngine.setPermissions(false, "775", servicesLocal)
beaverEngine.setPermissions(false, "775", modules)