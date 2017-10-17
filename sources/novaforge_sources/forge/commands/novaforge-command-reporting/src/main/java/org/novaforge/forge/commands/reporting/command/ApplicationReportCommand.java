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
package org.novaforge.forge.commands.reporting.command;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.karaf.shell.commands.Argument;
import org.apache.karaf.shell.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

/**
 * @author sbenoist
 */
@Command(scope = "report", name = "countApps", description = "Report applications grouped by plugin")
public class ApplicationReportCommand extends OsgiCommandSupport
{
  @Argument(index = 0, name = "reportingFilePath", description = "The absolute reporting file path",
      required = true, multiValued = false)
  private String                    reportingFilePath = null;

  private ApplicationPresenter      applicationPresenter;

  private ProjectPresenter          projectPresenter;

  private PluginsManager            pluginsManager;

  private ForgeConfigurationService forgeConfigurationService;

  private AuthentificationService   authentificationService;

  private UserPresenter             userPresenter;
  
  private HistorizationService      historizationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object doExecute() throws Exception
  {
    // login
    login();
    
    // Disactivate historization if implemented
    historizationService.setActivatedMode(false);

    List<PluginMetadata> plugins = pluginsManager.getAllPlugins();

    Map<String, Integer> applicationsCountByPlugin = new HashMap<String, Integer>();

    // Get all the projects
    List<Project> projects = projectPresenter.getAllProjects(false);

    // count all applications by plugin and project
    for (PluginMetadata plugin : plugins)
    {
      applicationsCountByPlugin.put(plugin.getType(),
          countAllApplicationsByPlugin(projects, plugin.getUUID()));
    }

    // make the report
    buildReport(applicationsCountByPlugin);
    
    // Activate historization if implemented
    historizationService.setActivatedMode(true);

    // Logout
    authentificationService.logout();

    return null;
  }

  private int countAllApplicationsByPlugin(final List<Project> pProjects, final String pPluginUUID)
      throws Exception
  {
    List<ProjectApplication> applications = new ArrayList<ProjectApplication>();
    for (Project project : pProjects)
    {
      applications
          .addAll(applicationPresenter.getAllProjectApplications(project.getProjectId(), pPluginUUID));
    }

    return filterOnStatus(applications).size();
  }

  private List<ProjectApplication> filterOnStatus(final List<ProjectApplication> pApplications)
  {
    List<ProjectApplication> filtered = new ArrayList<ProjectApplication>();
    for (ProjectApplication application : pApplications)
    {
      if (application.getStatus().equals(ApplicationStatus.ACTIVE))
      {
        filtered.add(application);
      }
    }

    return filtered;
  }

  private void buildReport(final Map<String, Integer> applicationsCountByPlugin) throws Exception
  {
    Writer writer = null;

    try
    {
      writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(reportingFilePath), "utf-8"));
      for (Map.Entry<String, Integer> entry : applicationsCountByPlugin.entrySet())
      {
        writer.write(String.format("\n%s", entry.getKey()));
        writer.write(String.format("\n\t %s application(s)", entry.getValue()));
      }
    }
    finally
    {
      if (writer != null)
      {
          writer.close();
      }
    }
  }

  private void login() throws UserServiceException
  {
    final String superAdministratorLogin = forgeConfigurationService.getSuperAdministratorLogin();
    final User user = userPresenter.getUser(superAdministratorLogin);
    authentificationService.login(superAdministratorLogin, user.getPassword());
  }

  public void setApplicationPresenter(final ApplicationPresenter pApplicationPresenter)
  {
    applicationPresenter = pApplicationPresenter;
  }

  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setUserPresenter(final UserPresenter pUserPresenter)
  {
    userPresenter = pUserPresenter;
  }
  
  public void setHistorizationService(final HistorizationService pHistorizationService)
  {
    historizationService = pHistorizationService;
  }

}
