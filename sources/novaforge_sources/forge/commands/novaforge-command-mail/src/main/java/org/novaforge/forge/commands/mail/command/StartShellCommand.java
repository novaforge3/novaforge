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

import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.apache.camel.karaf.commands.CamelController;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

/**
 * @author sbenoist
 */
@Command(scope = "mail", name = "start", description="Start the mail service")
public class StartShellCommand extends OsgiCommandSupport
{
   private CamelController camelController;

   public void setCamelController(final CamelController pCamelController)
   {
      this.camelController = pCamelController;
   }

   @Override
   protected Object doExecute() throws Exception
   {
      String route = "mailRoute";
      Route camelRoute = camelController.getRoute(route, null);
      if (camelRoute == null) {
          System.err.println("Camel route " + route + " not found.");
          return null;
      }
      CamelContext camelContext = camelRoute.getRouteContext().getCamelContext();
      camelContext.startRoute(route);
      
      return null;
   }

}
