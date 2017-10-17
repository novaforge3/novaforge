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
package org.novaforge.forge.commands.upgrade.dashboards;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.dao.RoleDAO;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.security.authentification.AuthentificationService;
import org.novaforge.forge.core.security.authorization.PermissionAction;
import org.novaforge.forge.core.security.authorization.PermissionHandler;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.DashBoard.Type;
import org.novaforge.forge.dashboard.service.DashBoardService;

import java.util.List;
import java.util.Set;

/**
 * @author sbenoist
 */
@Command(scope = "upgrade", name = "dashboards", description = "Update roles with dashboard permissions")
public class UpdateDashboardsCommand extends OsgiCommandSupport
{
  private static final Log LOGGER = LogFactory.getLog(UpdateDashboardsCommand.class);
  private ForgeConfigurationService forgeConfigurationService;
  private UserPresenter             userPresenter;
  private AuthentificationService   authentificationService;
  private HistorizationService      historizationService;
  private ProjectPresenter          projectPresenter;
  private ProjectRolePresenter      projectRolePresenter;
  private PermissionHandler         permissionHandler;
  private RoleDAO                   roleDAO;
  private DashBoardService          dashBoardService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    // Disactivate historization if implemented
    historizationService.setActivatedMode(false);

    // login
    login();

    // get administrator role name
    final String administratorRoleName = forgeConfigurationService.getForgeAdministratorRoleName();

    // Get all the projects
    final List<Project> projects = projectPresenter.getAllProjects(false);
    for (final Project project : projects)
    {
      final String projectId = project.getProjectId();
      try
      {
        LOGGER.info(String.format("*** Create Dashboard for projectid:%s....", projectId));
        final DashBoard dashBoard = dashBoardService.getDashBoard(Type.PROJECT, projectId);
        if (dashBoard == null)
        {
          LOGGER.error(String.format("Unable to create default dashboard for projectid:%s....", projectId));
        }

        LOGGER.info(String.format("*** Create Dashboard permissions for projectid:%s....", projectId));

        // create dashboard permissions
        final Set<String> permissions = permissionHandler.buildProjectPermissions(project.getProjectId(),
                                                                                  DashBoard.class,
                                                                                  PermissionAction.values());

        for (final String name : permissions)
        {
          final Permission perm = roleDAO.newPermission(name);
          if (!roleDAO.existPermission(name))
          {
            LOGGER.info(String.format("create permission [with name=%s]", name));
            roleDAO.persist(perm);
          }
          else
          {
            LOGGER.info(String.format("permission [with name=%s] already exists", name));
          }
        }

        LOGGER.info(String.format("*** Update Dashboard roles for projectid:%s...", projectId));

        final List<ProjectRole> roles = projectRolePresenter.getAllRoles(projectId);
        for (final ProjectRole role : roles)
        {
          LOGGER.info(String.format("Update Dashboard role for role:%s", role.getName()));
          if (administratorRoleName.equals(role.getName()))
          {
            // Add dashboard all permissions for administrator role
            projectRolePresenter.addPermissionToRole(projectId, role.getName(), DashBoard.class,
                PermissionAction.ALL);
          }
          else
          {
            // Add dashborad read permissions for all others role
            projectRolePresenter.addPermissionToRole(projectId, role.getName(), DashBoard.class,
                PermissionAction.READ);
          }
        }
      }
      catch (final Exception e)
      {
        LOGGER.error(String.format("*** Update Dashboard for projectid:%s has failed", projectId));
      }
    }

    // Logout
    authentificationService.logout();

    // re-activate historization
    historizationService.setActivatedMode(true);

    return null;
  }

  private void login() throws UserServiceException
  {
    final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();

    final org.novaforge.forge.core.organization.model.User user = userPresenter
        .getUser(superAdministratorLogin);
    authentificationService.login(superAdministratorLogin, user.getPassword());
  }

  /**
   * @param forgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

  /**
   * @param pUserPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }

  /**
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  /**
   * @param pHistorizationService
   *          the historizationService to set
   */
  public void setHistorizationService(final HistorizationService pHistorizationService)
  {
    historizationService = pHistorizationService;
  }

  /**
   * @param pProjectPresenter
   *          the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  /**
   * @param pProjectRolePresenter
   *          the projectRolePresenter to set
   */
  public void setProjectRolePresenter(final ProjectRolePresenter pProjectRolePresenter)
  {
    projectRolePresenter = pProjectRolePresenter;
  }

  /**
   * @param pRoleDAO
   *          the roleDAO to set
   */
  public void setRoleDAO(final RoleDAO pRoleDAO)
  {
    roleDAO = pRoleDAO;
  }

  /**
   * @param pPermissionHandler
   *          the permissionHandler to set
   */
  public void setPermissionHandler(final PermissionHandler pPermissionHandler)
  {
    permissionHandler = pPermissionHandler;
  }

  /**
   * @param pDashBoardService
   *          the dashBoardService to set
   */
  public void setDashBoardService(final DashBoardService pDashBoardService)
  {
    dashBoardService = pDashBoardService;
  }
}
