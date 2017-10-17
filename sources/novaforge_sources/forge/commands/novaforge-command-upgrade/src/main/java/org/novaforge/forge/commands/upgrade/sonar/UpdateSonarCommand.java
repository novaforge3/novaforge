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
package org.novaforge.forge.commands.upgrade.sonar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
@Command(scope = "upgrade", name = "sonar", description = "Update Sonar Applications")
public class UpdateSonarCommand extends OsgiCommandSupport
{

  private static final Log LOGGER = LogFactory.getLog(UpdateSonarCommand.class);
  private ForgeConfigurationService forgeConfigurationService;
  private UserPresenter             userPresenter;
  private AuthentificationService   authentificationService;
  private HistorizationService      historizationService;
  private PluginsManager            pluginsManager;
  private ProjectPresenter          projectPresenter;
  private ApplicationPresenter      applicationPresenter;

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

    // Get sonar plugin UUID
    final String sonarPluginUUID = getSonarPluginUUID();

    // Get all the projects and update role mapping for Sonar

    final List<Project> projects = projectPresenter.getAllProjects(false);
    for (final Project project : projects)
    {
      final String projectId = project.getProjectId();
      final List<ProjectApplication> applications = applicationPresenter.getAllProjectApplications(projectId,
          sonarPluginUUID);

      if ((applications != null) && (!applications.isEmpty()))
      {
        LOGGER.info(String.format("Update Sonar role mapping for projectid:%s", project.getProjectId()));
        for (final ProjectApplication application : applications)
        {
          final String applicationURI = application.getUri();
          final Map<String, String> roleMapping = applicationPresenter.getRoleMapping(projectId,
              applicationURI);
          if (roleMapping != null)
          {
            applicationPresenter.updateRoleMapping(projectId, applicationURI, roleMapping);
          }
        }
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

    final org.novaforge.forge.core.organization.model.User user = userPresenter.getUser(superAdministratorLogin);
    authentificationService.login(superAdministratorLogin, user.getPassword());
  }

  private String getSonarPluginUUID() throws Exception
  {
    final List<PluginMetadata> plugins = pluginsManager
        .getAllInstantiablePluginsMetadataByCategory("quality");
    if (plugins.size() == 0)
    {
      throw new Exception("Cannot find any quality plugin.");
    }
    else if (plugins.size() > 1)
    {
      throw new Exception("There are too many quality plugins found.");
    }
    else if (!plugins.get(0).getType().trim().toLowerCase().equals("sonar"))
    {
      throw new Exception("One system plugin was found but it is not sonar.");
    }

    return plugins.get(0).getUUID();
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
   * @param userPresenter
   *          the userPresenter to set
   */
  public void setUserPresenter(final UserPresenter userPresenter)
  {
    this.userPresenter = userPresenter;
  }

  /**
   * @param authentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService authentificationService)
  {
    this.authentificationService = authentificationService;
  }

  /**
   * @param historizationService
   *          the historizationService to set
   */
  public void setHistorizationService(final HistorizationService historizationService)
  {
    this.historizationService = historizationService;
  }

  /**
   * @param pluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pluginsManager)
  {
    this.pluginsManager = pluginsManager;
  }

  /**
   * @param projectPresenter
   *          the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter projectPresenter)
  {
    this.projectPresenter = projectPresenter;
  }

  /**
   * @param applicationPresenter
   *          the applicationPresenter to set
   */
  public void setApplicationPresenter(final ApplicationPresenter applicationPresenter)
  {
    this.applicationPresenter = applicationPresenter;
  }

}
