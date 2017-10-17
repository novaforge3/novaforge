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
package org.novaforge.forge.distribution.reference.internal.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeIdentificationService;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginDataService;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.distribution.reference.client.PluginDataClient;
import org.novaforge.forge.distribution.reference.exception.ReferenceServiceException;
import org.novaforge.forge.distribution.reference.model.ApplicationItemReferences;

import java.util.ArrayList;
import java.util.List;

/**
 * @author salvat-a
 */
public abstract class AbstractReferenceServiceImpl
{
  private static final Log             LOGGER = LogFactory.getLog(AbstractReferenceServiceImpl.class);

  protected ApplicationService         applicationService;
  protected PluginsManager             pluginsManager;
  protected ForgeIdentificationService forgeIdentificationService;
  protected PluginDataClient           pluginDataClient;

  protected List<ProjectApplication> getAllSynchronizableApplications(final String projectId)
  {
    final List<ProjectApplication> syncApplications = new ArrayList<ProjectApplication>();
    try
    {
      final List<ProjectApplication> applications = applicationService.getAllProjectApplications(projectId);
      for (final ProjectApplication application : applications)
      {
        if (isPluginSynchronizable(application.getPluginUUID().toString()))
        {
          syncApplications.add(application);
        }
      }
    }
    catch (final PluginManagerException e)
    {
      LOGGER.error(String.format("Unable to get the plugin service for project [%s]", projectId));
    }
    catch (final ApplicationServiceException e)
    {
      LOGGER.warn(String.format("No synchronizable plugin instances found for project [%s]", projectId));
    }
    return syncApplications;
  }

  private boolean isPluginSynchronizable(final String pluginId) throws PluginManagerException
  {
    final PluginService pluginService = pluginsManager.getPluginService(pluginId);
    return pluginService.getPluginDataServiceUri() != null;
  }

  protected ApplicationItemReferences getPluginItemReferences(final ProjectApplication application)
      throws ReferenceServiceException
  {
    final String forgeId = forgeIdentificationService.getForgeId().toString();

    final String pluginInstanceId = application.getPluginInstanceUUID().toString();

    final ApplicationItemReferences pluginItemReferences = new ApplicationItemReferences();
    pluginItemReferences.setNodeUri(application.getUri());

    final PluginDataService pluginDataService = pluginDataClient.getPluginDataService(application
        .getPluginUUID().toString());
    try
    {
      pluginItemReferences.setItemReferences(pluginDataService.getDataReferences(forgeId, pluginInstanceId));
    }
    catch (final PluginServiceException e)
    {
      throw new ReferenceServiceException(e);
    }
    return pluginItemReferences;
  }

  /**
   * @param pApplicationService
   *          the ApplicationService to set
   */
  public void setApplicationService(final ApplicationService pApplicationService)
  {
    applicationService = pApplicationService;
  }

  /**
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * @param pForgeIdentificationService
   *          the forgeIdentificationService to set
   */
  public void setForgeIdentificationService(final ForgeIdentificationService pForgeIdentificationService)
  {
    forgeIdentificationService = pForgeIdentificationService;
  }

  /**
   * @param pPluginDataClient
   *          the pluginDataClient to set
   */
  public void setPluginDataClient(final PluginDataClient pPluginDataClient)
  {
    pluginDataClient = pPluginDataClient;
  }

}
