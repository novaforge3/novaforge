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
package org.novaforge.forge.ui.pluginsmanagement.internal.module;

import net.engio.mbassy.listener.Handler;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.event.OpenInstancesListEvent;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.event.OpenPluginsListEvent;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.event.OpenRequestsListEvent;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.InstancesListPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.InstancesListViewImpl;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.PluginsListPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.PluginsListViewImpl;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.RequestsListPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.requests.RequestsListViewImpl;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalComponent;

/**
 * @author Guillaume Lamirand
 */
public class PluginsComponent extends AbstractPortalComponent
{
  private PluginsListPresenter   pluginsListPresenter;
  private InstancesListPresenter instanceListPresenter;
  private RequestsListPresenter  requestsListPresenter;

  public PluginsComponent(final PortalContext pPortalContext)
  {
    super(pPortalContext);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    // Add default view
    openPluginsList(null);
  }

  /**
   * Open the plugins list view
   * 
   * @param pEvent
   */
  @Handler
  public void openPluginsList(final OpenPluginsListEvent pEvent)
  {
    if (pluginsListPresenter == null)
    {
      pluginsListPresenter = new PluginsListPresenter(new PluginsListViewImpl(), getPortalContext());
    }
    setContent(pluginsListPresenter.getComponent());
    pluginsListPresenter.refresh();
  }

  /**
   * Open the instances list view
   * 
   * @param pEvent
   */
  @Handler
  public void openPluginInstancesList(final OpenInstancesListEvent pEvent)
  {
    if (instanceListPresenter == null)
    {
      instanceListPresenter = new InstancesListPresenter(new InstancesListViewImpl(), getPortalContext());
    }
    setContent(instanceListPresenter.getComponent());
    instanceListPresenter.refresh(pEvent.getPluginID());
  }

  /**
   * Open the requests list view
   * 
   * @param pEvent
   */
  @Handler
  public void openPluginRequestsList(final OpenRequestsListEvent pEvent)
  {
    if (requestsListPresenter == null)
    {
      requestsListPresenter = new RequestsListPresenter(new RequestsListViewImpl(), getPortalContext());
    }
    setContent(requestsListPresenter.getComponent());
    requestsListPresenter.refresh(pEvent.getPluginID());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return PluginsModule.getPortalModuleId();
  }

}