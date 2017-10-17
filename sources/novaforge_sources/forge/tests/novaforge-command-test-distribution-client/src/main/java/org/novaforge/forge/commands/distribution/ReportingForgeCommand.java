/*
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or modify it under
 * the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7.
 *
 * If you modify this Program, or any covered work, by linking or combining
 * it with libraries listed in COPYRIGHT file at the top-level directory of
 * this distribution (or a modified version of that libraries), containing parts
 * covered by the terms of licenses cited in the COPYRIGHT file, the licensors
 * of this Program grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.commands.distribution;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.distribution.reporting.client.ForgeReportingClient;
import org.novaforge.forge.distribution.reporting.domain.ihm.ForgeViewDTO;

import java.util.List;
import java.util.UUID;

/**
 * This command (launched onto CENTRAL forge) will extract reports like forge view.
 * 
 * @author Marc Blachon
 */
@Command(scope = "distribution", name = "reporting-forge", description = "get forge reporting")
public class ReportingForgeCommand extends OsgiCommandSupport
{
  @Argument(index = 0, name = "format", description = "optionnel argument: list", required = false,
      multiValued = false)
  String                             format = null;

  private AuthentificationService    authentificationService;

  private UserService                userService;

  private ForgeConfigurationService  forgeConfigurationService;

  private ForgeIdentificationService forgeIdentificationService;

  private ForgeReportingClient       forgeReportingClient;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
    final User user = userService.getUser(superAdministratorLogin);

    try
    {
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
      authentificationService.login(superAdministratorLogin, user.getPassword());
      final UUID forgeId = forgeIdentificationService.getForgeId();
      List<ForgeViewDTO> forgeViewList = forgeReportingClient.getForgeView(forgeId);
      if (forgeViewList != null)
      {
        // format the result as a simple list
        String rowLine = "";
        if ("list".equals(format))
        {
          for (ForgeViewDTO forgeView : forgeViewList)
          {
            rowLine = rowLine + forgeView.getForgeName() + "|";
            rowLine = rowLine + forgeView.getNumberProject().toString() + "|";
            rowLine = rowLine + forgeView.getNumberAccount().toString() + "|";
            rowLine = rowLine + forgeView.getLastUpdated();
            rowLine = rowLine + "\n";
          }
          // remove last feed line
          rowLine = rowLine.substring(0, rowLine.length() - 1);
          System.out.println(rowLine);
        }
        else
        {
          // TODO: add status ?
          final ShellTable table = new ShellTable();
          table.getHeader().add("forge");
          table.getHeader().add("projects Nb");
          table.getHeader().add("account Nb");
          table.getHeader().add("last updated date");

          for (ForgeViewDTO forgeView : forgeViewList)
          {
            final List<String> row = table.addRow();
            row.add(forgeView.getForgeName());
            row.add(forgeView.getNumberProject().toString());
            row.add(forgeView.getNumberAccount().toString());
            row.add(forgeView.getLastUpdated());
          }
          table.print();
        }
      }

      else
      {
        System.out.println("No forge found");
      }
      return null;
    }
    finally
    {
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setUserService(UserService userService)
  {
    this.userService = userService;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  public void setForgeIdentificationService(ForgeIdentificationService forgeIdentificationService)
  {
    this.forgeIdentificationService = forgeIdentificationService;
  }

  public void setForgeReportingClient(ForgeReportingClient forgeReportingClient)
  {
    this.forgeReportingClient = forgeReportingClient;
  }

}
