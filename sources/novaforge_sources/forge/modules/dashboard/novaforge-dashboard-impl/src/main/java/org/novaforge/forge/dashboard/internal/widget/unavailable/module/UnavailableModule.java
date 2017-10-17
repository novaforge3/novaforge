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
package org.novaforge.forge.dashboard.internal.widget.unavailable.module;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.dashboard.internal.widget.unavailable.client.UnavailablePresenter;
import org.novaforge.forge.dashboard.internal.widget.unavailable.client.UnavailableView;
import org.novaforge.forge.dashboard.internal.widget.unavailable.client.UnavailableViewImpl;
import org.novaforge.forge.dashboard.model.DataSourceOptions;
import org.novaforge.forge.dashboard.model.WidgetAdminComponent;
import org.novaforge.forge.dashboard.model.WidgetComponent;
import org.novaforge.forge.dashboard.model.WidgetContext;
import org.novaforge.forge.dashboard.model.WidgetDataComponent;
import org.novaforge.forge.dashboard.model.WidgetModule;
import org.novaforge.forge.dashboard.service.DataSourceFactory;
import org.novaforge.forge.portal.services.PortalMessages;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This module is used when a required module is unavailable.
 * 
 * @author Guillaume Lamirand
 */
public class UnavailableModule implements WidgetModule
{

  /**
   * Logger component
   */
  private static final Log LOGGER = LogFactory.getLog(UnavailableModule.class);
  /**
   * Contains reference to {@link PortalMessages} implementation
   */
  private static PortalMessages    PORTAL_MESSAGES;
  /**
   * Reference to {@link DataSourceFactory} service injected by the container
   */
  private static DataSourceFactory DATASOURCEOPTIONS_FACTORY;

  /**
   * Get implementation of {@link PortalMessages}
   *
   * @return the {@link PortalMessages}
   */
  public static PortalMessages getPortalMessages()
  {
    return PORTAL_MESSAGES;
  }

  /**
   * Use by container to inject {@link PortalMessages}
   *
   * @param pPortalMessages
   *          the portalMessages to set
   */
  public void setPortalMessages(final PortalMessages pPortalMessages)
  {
    PORTAL_MESSAGES = pPortalMessages;
  }

  /**
   * Get implementation of {@link DataSourceFactory}
   *
   * @return the {@link DataSourceFactory}
   */
  public static DataSourceFactory getDataSourceFactory()
  {
    return DATASOURCEOPTIONS_FACTORY;
  }

  /**
   * Use by container to inject {@link DataSourceFactory}
   *
   * @param pDataSourceOptionsFactory
   *          the portalMessages to set
   */
  public void setDataSourceFactory(final DataSourceFactory pDataSourceOptionsFactory)
  {
    DATASOURCEOPTIONS_FACTORY = pDataSourceOptionsFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getKey()
  {
    return WidgetModule.UNAVAILABLE_KEY;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getIcon()
  {
    byte[] byteArray = null;
    try
    {
      final InputStream resource = this.getClass().getResourceAsStream("/error.png");
      byteArray = IOUtils.toByteArray(resource);
    }
    catch (final IOException e)
    {
      LOGGER.error("Unable to build a byte array from inputstream given", e);
    }
    return byteArray;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public byte[] getPreview()
  {
    return getIcon();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getCategories()
  {
    return new ArrayList<String>();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetDataComponent createDataComponent(final WidgetContext pWidgetContext)
  {
    return (WidgetDataComponent) createComponent(pWidgetContext);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public WidgetAdminComponent createAdminComponent(final WidgetContext pWidgetContext)
  {
    return (WidgetAdminComponent) createComponent(pWidgetContext);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DataSourceOptions getDataSourceOptions()
  {
    return DATASOURCEOPTIONS_FACTORY.buildOptions(true, true, true, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidDataSource(final Map<String, List<String>> pContext)
  {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isValidProperties(final String pProperties)
  {
    return true;
  }

  private WidgetComponent createComponent(final WidgetContext pWidgetContext)
  {
    final UnavailableView      view      = new UnavailableViewImpl();
    return new UnavailablePresenter(pWidgetContext, view);
  }

}
