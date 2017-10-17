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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

BeaverEngine beaverEngine = engine

// Copy apache configuration
def serviceProxy = beaverEngine.getResource("serviceProxy")
def serviceLocal = beaverEngine.getResource("serviceLocal")
def alias = beaverEngine.getResource("alias")
beaverEngine.copyToFile(processTmpPath + "/resources/maintenance-page.service",serviceLocal)
beaverEngine.replaceElement(serviceLocal,"@MAINTENANCE_PAGE_ALIAS@",alias)
beaverEngine.replaceElement(serviceLocal,"@MAINTENANCE_PAGE_HOME@",home)
beaverEngine.copyToFile(processTmpPath + "/resources/maintenance-page-proxy.service",serviceProxy)
beaverEngine.replaceElement(serviceProxy,"@MAINTENANCE_PAGE_ALIAS@",alias)

// Set up permissions
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")
beaverEngine.setOwner(true,localGroup,localUser,home)
beaverEngine.setOwner(false, localGroup, localUser, serviceLocal)
beaverEngine.setOwner(false, localGroup, localUser, serviceProxy)


def maintenanceEnable = beaverEngine.getResource("enabled")
def disabledProxy = serviceProxy + ".disabled"
if (maintenanceEnable)
{
    Path disabledProxyPath = Paths.get(disabledProxy)
    if (Files.exists(disabledProxyPath))
    {
      beaverEngine.move(disabledProxy, serviceProxy)
    }
}
else
{
  beaverEngine.move(serviceProxy, disabledProxy)
  
}