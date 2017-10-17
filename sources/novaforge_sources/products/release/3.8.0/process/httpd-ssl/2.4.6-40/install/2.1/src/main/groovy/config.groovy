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
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.Files

BeaverEngine beaverEngine = engine

// Configure novaforge.conf
def localHost = beaverEngine.getResource("local:host")
def confd = beaverEngine.getResource("httpd", "confd")
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


// Configure ssl.conf
def sslConf = beaverEngine.getResource("sslConf")
beaverEngine.copyToFile(processTmpPath + "/resources/ssl.conf", sslConf)
def sslData = beaverEngine.getResource("sslData")
def certificatCsr = beaverEngine.getResource("certificat","csr")
def certificatKey = beaverEngine.getResource("certificat","key")
beaverEngine.replaceElement(sslConf,"@SSl_DATA@", sslData)
beaverEngine.replaceElement(sslConf,"@CERTIFICAT_FILE@", certificatCsr)
beaverEngine.replaceElement(sslConf,"@CERTIFICAT_KEY_FILE@", certificatKey)

// Delete default ssl.conf if exists
def defaultSSL = "/etc/httpd/conf.d/ssl.conf"
Path defaultSSLPath = Paths.get(defaultSSL)
if (Files.exists(defaultSSLPath)) {
  beaverEngine.delete("/etc/httpd/conf.d/ssl.conf")
}

// Set owner
def httpdUser = beaverEngine.getResource("httpd", "user")
def httpdGroup = beaverEngine.getResource("httpd", "group")
beaverEngine.setOwner(true,httpdGroup,httpdUser,confd)
beaverEngine.setOwner(true,httpdGroup,httpdUser,sslData)