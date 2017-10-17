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
/**
 * 
 */
package org.novaforge.forge.commands.mail.command;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.commons.technical.mail.MailService;

/**
 * @author sbenoist
 *
 */
@Command(scope = "mail", name = "send", description="Send an email")
public class SendmailShellCommand extends OsgiCommandSupport
{  
   @Argument(index = 0, name = "from", description = "The sender of the email", required = true, multiValued = false)
   private String from = null;
   
   @Argument(index = 1, name = "to", description = "The recipient of the email", required = true, multiValued = false)
   private String to = null;
   
   @Argument(index = 2, name = "subject", description = "The subject of the email", required = true, multiValued = false)
   private String subject = null;
   
   @Argument(index = 3, name = "body", description = "The body of the email", required = true, multiValued = false)
   private String body = null;
   
   private MailService mailService;
   
   public void setMailService(final MailService pMailService)
   {
      this.mailService = pMailService;
   }

   @Override
   protected Object doExecute() throws Exception
   {
      mailService.sendMail(from, to, subject, body);
      return null;
   }

}
