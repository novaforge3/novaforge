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
import com.vaadin.server.ThemeResource;
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
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.pluginsmanagement.internal.client.plugins.PluginsListPresenter;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;
import org.novaforge.forge.ui.portal.client.util.ResourceUtils;

import java.util.Locale;

/**
 * @author caseryj
 */
public class PluginColumnTypeGenerator implements ColumnGenerator
{

  /**
   * SerialUID
   */
  private static final long          serialVersionUID = 5976374242146981756L;
  private final PluginsListPresenter pluginsListPresenter;

  /**
   * @param pPluginsListPresenter
   */
  public PluginColumnTypeGenerator(final PluginsListPresenter pPluginsListPresenter)
  {
    pluginsListPresenter = pPluginsListPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final Table source, final Object itemId, final Object columnId)
  {
    final HorizontalLayout statusLayout = new HorizontalLayout();
    final GridLayout grid = new GridLayout(2, 1);
    final Embedded pluginIconEM = new Embedded(null);
    pluginIconEM.setType(Embedded.TYPE_IMAGE);
    pluginIconEM.setWidth(20, Unit.PIXELS);
    pluginIconEM.setHeight(20, Unit.PIXELS);
    final Item item = source.getItem(itemId);
    final String pluginName = (String) item.getItemProperty(PluginItemProperty.TYPE.getPropertyId())
        .getValue();
    final Label pluginNameLabel = new Label(pluginName);
    final boolean isAvailable = (Boolean) item.getItemProperty(PluginItemProperty.ACTIONS.getPropertyId())
        .getValue();
    byte[] pluginIcon = null;
    try
    {
      final PluginService pluginService = PluginsModule.getPluginsManager().getPluginService(
          (String) item.getItemProperty(PluginItemProperty.ID.getPropertyId()).getValue());
      if ((pluginService == null) || !isAvailable)
      {
        final Locale locale = UI.getCurrent().getLocale();
        pluginIconEM.setSource(new ThemeResource(NovaForgeResources.ICON_ERROR));
        pluginNameLabel.setStyleName(NovaForge.LABEL_STRIKE);
        pluginIconEM.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE));
        pluginNameLabel.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE));
      }
      else
      {
        pluginIcon = pluginService.getPluginIcon();
        pluginIconEM.setSource(ResourceUtils.buildImageResource(pluginIcon, pluginName));
      }
      grid.addComponent(pluginIconEM, 0, 0);
      grid.addComponent(pluginNameLabel, 1, 0);
      grid.setComponentAlignment(pluginIconEM, Alignment.MIDDLE_CENTER);
      grid.setComponentAlignment(pluginNameLabel, Alignment.MIDDLE_CENTER);
      grid.setSizeUndefined();
      statusLayout.addComponent(grid);
      statusLayout.setSizeUndefined();

    }
    catch (final PluginManagerException e)
    {
      e.printStackTrace();
    }
    return statusLayout;
  }

}
