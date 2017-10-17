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
package org.novaforge.forge.commands.jms.command;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.commons.technical.jms.MessageService;

/**
 * @author sbenoist
 *
 */
@Command(scope = "jms", name = "send", description="Send a JMS text message to the samplequeue queue")
public class SendShellCommand extends OsgiCommandSupport
{  
   @Argument(index = 0, name = "text", description = "The text of the message", required = true, multiValued = false)
   private String text = null;
   
   private MessageService messageService;
   
   public void setMessageService(final MessageService messageService)
   {
      this.messageService = messageService;
   }

   @Override
   protected Object doExecute() throws Exception
   {
      messageService.sendMessage("samplequeue", text, null);
      return null;
   }

}
