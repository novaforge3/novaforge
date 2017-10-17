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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.services.MembershipService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.util.ArrayList;
import java.util.List;

/**
 * This command (launched on LOCAL/ZONAL or CENTRAL forge) will get the number of total accounts
 * 
 * @author Marc Blachon
 */
@Command(scope = "distribution", name = "reporting-check-account",
    description = "get the number of total accounts")
public class ReportingCheckAccountCommand extends OsgiCommandSupport
{
  private static final Log log = LogFactory.getLog(ReportingCheckAccountCommand.class);
  private AuthentificationService   authentificationService;
  private UserService               userService;
  private ProjectService            projectService;
  private MembershipService         membershipService;
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    try
    {
      final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
      final User user = userService.getUser(superAdministratorLogin);

      authentificationService.login(superAdministratorLogin, user.getPassword());

      userService.getAllUsers(false);
      final ArrayList<String> validUserList = new ArrayList<String>();

      final ProjectOptions projectOptions = projectService.newProjectOptions();
      projectOptions.setRetrievedImage(false);
      projectOptions.setRetrievedOrganization(false);
      projectOptions.setRetrievedSystem(false);
      // final List<Project> projects = projectService.getAllProjectsByStatus(projectOptions,
      // ProjectStatus.VALIDATED);
      final List<Project> projects = projectService.getAllProjects(projectOptions);
      for (final Project project : projects)
      {
        final RealmType rt = project.getRealmType();
        if (!rt.equals(RealmType.SYSTEM))
        {
          final List<Membership> memberships = membershipService
              .getAllEffectiveUserMembershipsForProject(project.getProjectId());
          for (final Membership membership : memberships)
          {
            final String login = ((User) membership.getActor()).getLogin();

            // if ((!validUserList.contains(login)) && (!"admin1".equals(login)))
            if ((!validUserList.contains(login)))
            {
              validUserList.add(login);
            }
          }
        }
      }

      System.out.println(validUserList.size());
    }
    finally
    {
      if (authentificationService.checkLogin())
      {
        authentificationService.logout();
      }
    }
    return null;
  }

  // ----------------- setter used by the container to inject services -------------------------------

  public void setAuthentificationService(final AuthentificationService authentificationService)
  {
    this.authentificationService = authentificationService;
  }

  public void setUserService(final UserService userService)
  {
    this.userService = userService;
  }

  public void setProjectService(final ProjectService projectService)
  {
    this.projectService = projectService;
  }

  public void setMembershipService(final MembershipService membershipService)
  {
    this.membershipService = membershipService;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }
}
