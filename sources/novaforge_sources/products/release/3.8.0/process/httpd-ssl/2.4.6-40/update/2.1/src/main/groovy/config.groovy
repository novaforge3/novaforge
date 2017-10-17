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

BeaverEngine beaverEngine = engine

// Reconfigure novaforge.conf
// To reenable the CustomLog property
def localHost = beaverEngine.getResource("local:host")
def novaforgeConf = beaverEngine.getResource("httpd", "novaforgeConf")
def servicesProxy = beaverEngine.getResource("httpd", "servicesProxy")
def servicesLocal = beaverEngine.getResource("httpd", "servicesLocal")
def modules = beaverEngine.getResource("httpd", "modules")
def logs = beaverEngine.getResource("httpd", "logs")

beaverEngine.copyToFile(processTmpPath + "/resources/novaforge.conf", novaforgeConf)
beaverEngine.replaceElement(novaforgeConf,"@HOSTNAME@", localHost)
beaverEngine.replaceElement(novaforgeConf,"@HTTPD_MODULES@", modules)
beaverEngine.replaceElement(novaforgeConf,"@HTTPD_LOGS@", logs)
beaverEngine.replaceElement(novaforgeConf,"@HTTPD_SERVICES_PROXY@", servicesProxy)
beaverEngine.replaceElement(novaforgeConf,"@HTTPD_SERVICES_LOCAL@", servicesLocal)

// Set owner
def httpdUser = beaverEngine.getResource("httpd", "user")
def httpdGroup = beaverEngine.getResource("httpd", "group")
beaverEngine.setOwner(false,httpdGroup,httpdUser,novaforgeConf)
