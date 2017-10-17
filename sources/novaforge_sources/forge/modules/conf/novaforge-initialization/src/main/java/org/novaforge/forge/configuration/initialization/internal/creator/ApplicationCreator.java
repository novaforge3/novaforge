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
package org.novaforge.forge.configuration.initialization.internal.creator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.configuration.initialization.internal.properties.InitializationProperties;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.ToolInstanceProvisioningException;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.plugins.services.ToolInstanceProvisioningService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author sbenoist
 */
public class ApplicationCreator
{

  private static final Log LOGGER = LogFactory.getLog(ApplicationCreator.class);
  /**
   * Period time (in MilliSeconds)
   */
  private long                     period        = 30000;
  /**
   * initial delay for the first run (in MilliSeconds)
   */
  private long                     initialDelay  = 30000;
  /**
   * max trial delay until we stop the scheduler even if the application hasn't been created
   */
  private long                     maxTrialDelay = 180000;
  private AuthentificationService  authentificationService;
  private InitializationProperties initializationProperties;
  private ApplicationPresenter     applicationPresenter;
  private PluginsManager           pluginsManager;
  private HistorizationService     historizationService;
  private ProjectPresenter         projectPresenter;

  public void sheduleTask(final String pProjectId, final String pSpaceUri,
      final Map<String, String> pRoleMappings, final String pAppType, final String pAppName) throws Exception
  {
    LOGGER.info(String.format(
        "Start scheduler for application creation with [ projectId=%s,  appType=%s, appName=%s]", pProjectId,
        pAppType, pAppName));
    final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    final ApplicationCreationTask applicationCreationTask = new ApplicationCreationTask(executorService,
        pProjectId, pSpaceUri, pRoleMappings, pAppType, pAppName);
    executorService.scheduleAtFixedRate(applicationCreationTask, initialDelay, period, TimeUnit.MILLISECONDS);
  }

  public void setPeriod(final long pPeriod)
  {
    period = pPeriod;
  }

  public void setInitialDelay(final long pInitialDelay)
  {
    initialDelay = pInitialDelay;
  }

  public void setMaxTrialDelay(final long pMaxTrialDelay)
  {
    maxTrialDelay = pMaxTrialDelay;
  }

  public void setApplicationPresenter(final ApplicationPresenter pApplicationPresenter)
  {
    applicationPresenter = pApplicationPresenter;
  }

  public void setInitializationProperties(final InitializationProperties pInitializationProperties)
  {
    initializationProperties = pInitializationProperties;
  }

  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  public void setHistorizationService(final HistorizationService pHistorizationService)
  {
    historizationService = pHistorizationService;
  }

  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  private class ApplicationCreationTask implements Runnable
  {
    private final String                   projectId;

    private final String                   spaceUri;

    private final Map<String, String>      roleMappings;

    private final String                   appType;

    private final String                   appName;
    private final ScheduledExecutorService executorService;
    /**
     * number of runned trials
     */
    private long                           nbOfTrials = 0;

    public ApplicationCreationTask(final ScheduledExecutorService pExecutorService, final String pProjectId,
        final String pSpaceUri, final Map<String, String> pRoleMappings, final String pAppType,
        final String pAppName)
    {
      super();
      executorService = pExecutorService;
      projectId = pProjectId;
      spaceUri = pSpaceUri;
      roleMappings = pRoleMappings;
      appType = pAppType;
      appName = pAppName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
      nbOfTrials++;
      try
      {
        // disable historization
        historizationService.setActivatedMode(false);

        LOGGER.info(String.format("run application creation with [ projectId=%s,  appType=%s, appName=%s]",
            projectId, appType, appName));
        create(projectId, spaceUri, roleMappings, appType, appName);

      }
      catch (final Exception e)
      {
        LOGGER
            .error(
                String
                    .format(
                        "an error occurred during the application creation with [ projectId=%s,  appType=%s, appName=%s]",
                        projectId, appType, appName), e);
      }
      finally
      {
        if (historizationService != null)
        {
          // enable historization
          historizationService.setActivatedMode(true);
        }

        if (!executorService.isShutdown())
        {
          // stop the scheduler id the maxTrialTime is elapsed
          if ((nbOfTrials * period) > maxTrialDelay)
          {
            LOGGER.info(String.format(
                "after %s trials : stop scheduler for [ projectId=%s,  appType=%s, appName=%s]", nbOfTrials,
                projectId, appType, appName));
            executorService.shutdown();
          }
        }
      }
    }

    private void create(final String projectId, final String spaceUri,
        final Map<String, String> roleMappings, final String appType, final String appName)
        throws PluginManagerException, ProjectServiceException, ApplicationServiceException,
        ToolInstanceProvisioningException
    {
      final List<PluginMetadata> plugins = pluginsManager.getPluginsMetadataByType(appType);
      if ((plugins != null) && (plugins.size() == 1))
      {
        final PluginMetadata plugin = plugins.get(0);
        final String pluginUUID = plugin.getUUID();

        // check the availabilities of plugin
        if (plugin.isAvailable())
        {
          final ToolInstanceProvisioningService provisioningService = pluginsManager.getPluginService(
              pluginUUID).getToolInstanceProvisioningService();
          if (provisioningService.hasAvailableToolInstance())
          {
            // authenticate login
            if (!authentificationService.checkLogin())
            {
              authentificationService.login(initializationProperties.getSuperAdministratorLogin(),
                  initializationProperties.getSuperAdministratorPassword());
            }
            else if (!initializationProperties.getSuperAdministratorLogin().equals(authentificationService
                                                                                       .getCurrentUser()))
            {
              authentificationService.logout();
              authentificationService.login(initializationProperties.getSuperAdministratorLogin(),
                  initializationProperties.getSuperAdministratorPassword());
            }

            // create app
            applicationPresenter.addApplication(projectId, spaceUri, appName, appName,
                UUID.fromString(pluginUUID), roleMappings);

            // Update project to evict its cache
            final ProjectOptions newProjectOptions = projectPresenter.newProjectOptions(true, true, true);
            final Project project = projectPresenter.getProject(projectId, newProjectOptions);
            projectPresenter.updateProject(project.getName(), project);

            LOGGER.info(String.format("Application %s created.", appType));

            // we stop the scheduler when the task is done
            executorService.shutdown();
          }
        }
      }
    }
  }
}
