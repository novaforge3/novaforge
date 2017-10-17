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
// Remove php-mysql due to incompatibility with php-mysqlnd
repositoryService.removeRPMs("php-mysql");
repositoryService.installRPMs(dataFile, "php", "php-mysqlnd", "php-xml", "php-gd", "php-xmlrpc", "php-soap", "php-mbstring", "curl", "curl-devel")

def home = beaverEngine.getResource("home")
def datas = beaverEngine.getResource("datas")
def session = beaverEngine.getResource("session")
def includes = beaverEngine.getResource("includes")

Path homePath = Paths.get(home)
if (Files.exists(homePath))
{
  beaverEngine.delete(home)  
}
beaverEngine.createDirectory(home)

Path datasPath = Paths.get(datas)
if (Files.exists(datasPath))
{
  beaverEngine.delete(datas)  
}
beaverEngine.createDirectory(datas)

Path sessionPath = Paths.get(session)
if (Files.exists(sessionPath))
{
  beaverEngine.delete(session)  
}
beaverEngine.createDirectory(session)

Path includesPath = Paths.get(includes)
if (Files.exists(includesPath))
{
  beaverEngine.delete(includes)  
}
beaverEngine.createDirectory(includes)
