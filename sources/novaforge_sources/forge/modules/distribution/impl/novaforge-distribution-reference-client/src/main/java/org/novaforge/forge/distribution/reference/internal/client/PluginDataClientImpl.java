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
package org.novaforge.forge.distribution.reference.internal.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginDataService;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.distribution.reference.client.PluginDataClient;
import org.novaforge.forge.distribution.reference.exception.ReferenceServiceException;

/**
 * @author rols-p
 */
public class PluginDataClientImpl implements PluginDataClient
{

  private static final Log log = LogFactory.getLog(PluginDataClientImpl.class);

  private PluginsManager   pluginsManager;

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginDataService getPluginDataService(final String pluginUUID) throws ReferenceServiceException
  {
    PluginDataService dataService = null;
    String serviceUri;
    try
    {
      final PluginService pluginService = pluginsManager.getPluginService(pluginUUID);
      serviceUri = pluginService.getPluginDataServiceUri();
      if (serviceUri != null)
      {
        final ClassLoader theGoodOne = ClientProxyFactoryBean.class.getClassLoader();
        final ClassLoader theOldOne = Thread.currentThread().getContextClassLoader();
        Thread.currentThread().setContextClassLoader(theGoodOne);
        try
        {
          final ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
          factory.setServiceClass(PluginDataService.class);
          factory.setAddress(serviceUri);
          dataService = (PluginDataService) factory.create();
        }
        finally
        {
          Thread.currentThread().setContextClassLoader(theOldOne);
        }
      }
    }
    catch (final PluginManagerException e)
    {
      log.error(String.format("Can not get PluginDataService for the pluginUUID=%s", pluginUUID), e);
      throw (new ReferenceServiceException(e));
    }
    if (dataService == null)
    {
      log.error(String.format("Can not get PluginDataService for the pluginUUID=%s", pluginUUID));
      throw new ReferenceServiceException(String.format("No PluginDataService found for the pluginUUID=%s",
          pluginUUID));
    }
    if (log.isDebugEnabled())
    {
      log.debug(String.format("getPluginDataService OK for the service uri=%s", serviceUri));
    }
    return dataService;
  }

  /**
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

}
