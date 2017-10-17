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

// Configure sendmail.mc file
def sendmailMCSource = processTmpPath + "/resources/sendmail.mc"
def sendmailMCTarget = beaverEngine.getResource("configFile")
def smtpProvider = beaverEngine.getResource("smtp","host")
def localIp = beaverEngine.getResource("local:ip")
def ucpMailerMaxSize = beaverEngine.getResource("ucpMailerMaxSize")
def queueLA = beaverEngine.getResource("queueLA")
def refuseLA = beaverEngine.getResource("refuseLA")
def reconfigureCommand = beaverEngine.getResource("reconfigureCommand")

beaverEngine.copyToFile(sendmailMCSource, sendmailMCTarget)
beaverEngine.replaceElement(sendmailMCTarget, "@SMTP_PROVIDER@", smtpProvider)
beaverEngine.replaceElement(sendmailMCTarget, "@LOCAL_IP@", localIp)
beaverEngine.replaceElement(sendmailMCTarget, "@UCP_MAILER_MAXSIZE@", ucpMailerMaxSize)
beaverEngine.replaceElement(sendmailMCTarget, "@QUEUE_LA@", queueLA)
beaverEngine.replaceElement(sendmailMCTarget, "@REFUSE_LA@", refuseLA)

// ReGenerate sendmail.cf
beaverEngine.executeCommand(reconfigureCommand)