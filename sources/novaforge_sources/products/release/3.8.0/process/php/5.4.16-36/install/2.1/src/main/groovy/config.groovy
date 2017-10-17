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
import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService
import org.codehaus.plexus.util.StringUtils

BeaverEngine beaverEngine = engine
UserService userService = beaverEngine.getUserService();


// Configure novaforge.ini
def novaforgeIni = beaverEngine.getResource("novaforgeIni")
def session = beaverEngine.getResource("session")
def includes = beaverEngine.getResource("includes")

beaverEngine.copyToFile(processTmpPath + "/resources/novaforge.ini" ,novaforgeIni)

// Php Configuration
beaverEngine.replaceElement(novaforgeIni,"@PHP_SESSION@", session)
beaverEngine.replaceElement(novaforgeIni,"@PHP_INCLUDES@", includes)

// SMTP Configuration
def smtpHost = beaverEngine.getResource("smtp","host")
def smtpPort = beaverEngine.getResource("smtp","port")

beaverEngine.replaceElement(novaforgeIni, "@SMTP_HOST@", smtpHost)
beaverEngine.replaceElement(novaforgeIni, "@SMTP_PORT@",  smtpPort)


// Set owner
def home = beaverEngine.getResource("home")
def datas = beaverEngine.getResource("datas")
def localUser = beaverEngine.getResource("user")
def localGroup = beaverEngine.getResource("group")
beaverEngine.setOwner(true,localGroup,localUser,home)
beaverEngine.setOwner(true,localGroup,localUser,datas)
beaverEngine.setOwner(true,localGroup,localUser,session)
beaverEngine.setOwner(true,localGroup,localUser,includes)
beaverEngine.setPermissions(false, "775", home)
beaverEngine.setPermissions(false, "775", datas)
beaverEngine.setPermissions(false, "775", session)
beaverEngine.setPermissions(false, "775", includes)