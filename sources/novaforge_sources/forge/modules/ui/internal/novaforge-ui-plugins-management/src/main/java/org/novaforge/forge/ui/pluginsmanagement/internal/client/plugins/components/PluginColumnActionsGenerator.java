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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.components;

import com.vaadin.data.Item;
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.PluginsListPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;

import java.util.Locale;

/**
 * @author caseryj
 */
public class PluginColumnActionsGenerator implements ColumnGenerator
{
  /**
   * SerialUID
   */
  private static final long          serialVersionUID = -5622712099242926767L;
  private final PluginsListPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the plugins table
   */
  public PluginColumnActionsGenerator(final PluginsListPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("serial")
  @Override
  public Object generateCell(final com.vaadin.ui.Table source, final Object itemId, final Object columnId)
  {
    final Locale locale = UI.getCurrent().getLocale();

    // Get the item and the plugin service associated
    final Item item = source.getItem(itemId);
    final boolean isAvailable = (Boolean) item.getItemProperty(PluginItemProperty.ACTIONS.getPropertyId())
        .getValue();

    // Init actions buttons
    final Embedded actionManageStatus = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_STATUS));
    actionManageStatus.setWidth(NovaForge.ACTION_ICON_SIZE);
    actionManageStatus.setStyleName(NovaForge.BUTTON_IMAGE);
    actionManageStatus.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_PLUGIN_ACTIONS_STATUS));
    actionManageStatus.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.manageStatusClicked(itemId);
      }
    });

    final Embedded actionManageInstance = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_INSTANCE_COLORED));
    actionManageInstance.setWidth(NovaForge.ACTION_ICON_SIZE);
    actionManageInstance.setStyleName(NovaForge.BUTTON_IMAGE);
    actionManageInstance.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_PLUGIN_ACTIONS_INSTANCE));
    actionManageInstance.addClickListener(new MouseEvents.ClickListener()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.manageInstanceClicked(itemId);
      }
    });
    final Embedded actionManageRequest = new Embedded(null,
        new ThemeResource(NovaForgeResources.ICON_REQUEST));
    actionManageRequest.setWidth(NovaForge.ACTION_ICON_SIZE);
    actionManageRequest.setStyleName(NovaForge.BUTTON_IMAGE);
    actionManageRequest.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
        Messages.PLUGINSMANAGEMENT_PLUGIN_ACTIONS_REQUESTS));
    actionManageRequest.addClickListener(new MouseEvents.ClickListener()
    {
      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.manageRequestsClicked(itemId);
      }
    });
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.addComponent(actionManageStatus);
    actionsLayout.addComponent(actionManageInstance);
    actionsLayout.addComponent(actionManageRequest);
    actionsLayout.setComponentAlignment(actionManageStatus, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(actionManageInstance, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(actionManageRequest, Alignment.MIDDLE_CENTER);
    actionsLayout.setSizeUndefined();

    if (!isAvailable)
    {
      actionManageRequest.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
          Messages.PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE));
      actionManageRequest.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
          Messages.PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE));
      actionManageRequest.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
          Messages.PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE));
    }
    // Disable buttons if plugin isn't available
    actionManageStatus.setEnabled(isAvailable);
    actionManageInstance.setEnabled(isAvailable);
    actionManageRequest.setEnabled(isAvailable);

    return actionsLayout;
  }

}
