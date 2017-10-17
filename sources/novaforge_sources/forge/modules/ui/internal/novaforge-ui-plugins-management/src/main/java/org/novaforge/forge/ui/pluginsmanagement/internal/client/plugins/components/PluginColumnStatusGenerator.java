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
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;

import java.util.Locale;

/**
 * @author caseryj
 */
public class PluginColumnStatusGenerator implements ColumnGenerator
{

  /**
   * SerialUID
   */
  private static final long serialVersionUID = 6883903185976106504L;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final Table pSource, final Object pItemId, final Object pColumnId)
  {
    // Get item and item's infos
    final Item item = pSource.getItem(pItemId);
    final boolean isAvailable = (Boolean) item.getItemProperty(PluginItemProperty.ACTIONS.getPropertyId())
        .getValue();
    final String statusID = (String) item.getItemProperty(PluginItemProperty.STATUS_ID.getPropertyId())
        .getValue();
    final String statusName = (String) item.getItemProperty(PluginItemProperty.STATUS_LABEL.getPropertyId())
        .getValue();
    final String statusDescription = (String) item.getItemProperty(
        PluginItemProperty.STATUS_DECRIPTION.getPropertyId()).getValue();
    // Init cell layout
    final HorizontalLayout statusLayout = new HorizontalLayout();
    final GridLayout statusGrid = new GridLayout(2, 1);
    final Label statusNamelabel = new Label(statusName);
    final Embedded statusIcon = new Embedded(null);
    statusIcon.setWidth(20, Unit.PIXELS);
    try
    {
      final PluginService pluginService = PluginsModule.getPluginsManager().getPluginService(
          (String) item.getItemProperty(PluginItemProperty.ID.getPropertyId()).getValue());
      if ((pluginService == null) || !isAvailable)
      {
        final Locale locale = UI.getCurrent().getLocale();
        statusNamelabel.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE));
        statusNamelabel.setStyleName(NovaForge.LABEL_STRIKE);
        statusIcon.setSource(StatusContainer.getStatusIcon(""));
        statusIcon.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE));
      }
      else
      {
        statusNamelabel.setDescription(statusDescription);
        statusIcon.setSource(StatusContainer.getStatusIcon(statusID));
        statusIcon.setDescription(statusDescription);
      }
    }
    catch (final PluginManagerException e)
    {
      // Nothing to do here
    }
    statusGrid.addComponent(statusIcon, 0, 0);
    statusGrid.addComponent(statusNamelabel, 1, 0);
    statusGrid.setComponentAlignment(statusIcon, Alignment.MIDDLE_CENTER);
    statusGrid.setComponentAlignment(statusNamelabel, Alignment.MIDDLE_CENTER);
    statusLayout.addComponent(statusGrid);
    statusLayout.setSizeUndefined();
    return statusLayout;
  }
}
