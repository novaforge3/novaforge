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
import org.novaforge.beaver.exception.BeaverException;

import org.novaforge.beaver.deployment.plugin.deploy.engine.BeaverEngine
import org.novaforge.beaver.deployment.plugin.deploy.engine.SSLService
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import org.codehaus.plexus.util.StringUtils

BeaverEngine beaverEngine = engine
SSLService sslService = engine.getSSLService()

// Create home directory
def home = beaverEngine.getResource("home")
beaverEngine.createDirectory(home)

// handle certificat creation
def isValidSettings = false;
def p12Source = beaverEngine.getResource("p12Source")
def generate = beaverEngine.getResource("generate")

def p12 = beaverEngine.getResource("p12")
def p12PwdIn = beaverEngine.getResource("p12PwdIn")
def p12PwdOut = beaverEngine.getResource("p12PwdOut")
def keysize = beaverEngine.getResource("keysize")

def key = beaverEngine.getResource("key")
def csr = beaverEngine.getResource("csr")

// Install new certificate if p12 file does not exist.
def p12Path = Paths.get(p12)
if (Files.exists(p12Path) == false) {
  if (Boolean.valueOf(generate)) {
    // Generate an auto-signed certificat
    def days = beaverEngine.getResource("days")
    def country = beaverEngine.getResource("country")
    def location = beaverEngine.getResource("location")
    def organization = beaverEngine.getResource("organization")
    def organizationUnit = beaverEngine.getResource("organizationUnit")
    def commonName = beaverEngine.getResource("commonName")
    def email = beaverEngine.getResource("email")

    sslService.generateKeyAndCSR(key, csr, Integer.valueOf(days), Integer.valueOf(keysize), country, location, organization, organizationUnit, commonName, email)
    sslService.buildPKCS12(p12, key, csr, commonName, p12PwdIn, p12PwdOut)
    isValidSettings = true;
    
  } else if (StringUtils.isNotBlank(p12Source)) {
    // Extract CRS and KEY from p12 given
    Path p12SourcePath = Paths.get(p12Source)
    if (Files.exists(p12SourcePath)) {

      beaverEngine.copyToFile(p12Source, p12)
      sslService.extractKeyAndCSR(key, csr, p12, p12PwdIn, p12PwdOut)   
      isValidSettings = true; 
      
    } else if (beaverEngine.isSimulateMode()) {
      isValidSettings = true;
    }
  }

  // Thrown exception in case of errors
  if (isValidSettings == false) {
    throw new BeaverException("You have set generated mode to false, so p12Source property has to refer an existing file.");
  }

  // Generate JKS
  def jks = beaverEngine.getResource("jks")
  def jksPwd = beaverEngine.getResource("jksPwd")
  if (jksPwd.length() < 6) {
    throw new BeaverException("The password used for JKS has to be greater than 6 characters.")
  }
  def jksAlias = beaverEngine.getResource("main", "certificat", "jksAlias")
  sslService.importPKCS12toJKS(p12, p12PwdOut, jksAlias, jks, jksPwd, Integer.valueOf(keysize))
  sslService.importJKStoJKS(processTmpPath + "/resources/cacerts", "changeit", jks, jksPwd)
}


// Set owner
def localUser = beaverEngine.getResource("local:user")
def localGroup = beaverEngine.getResource("local:group")
beaverEngine.setOwner(true,localGroup,localUser,home)