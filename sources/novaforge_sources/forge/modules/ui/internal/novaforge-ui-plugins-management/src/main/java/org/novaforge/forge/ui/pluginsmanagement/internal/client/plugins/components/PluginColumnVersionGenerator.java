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
 * @author Jeremy Casery
 */
public class PluginColumnVersionGenerator implements ColumnGenerator
{

  /**
   * SerialUID
   */
  private static final long serialVersionUID = 8445920526807026749L;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final Table source, final Object itemId, final Object columnId)
  {
    final String versionValue = (String) source.getItem(itemId)
        .getItemProperty(PluginItemProperty.VERSION.getPropertyId()).getValue();
    final boolean isAvailable = (Boolean) source.getItem(itemId)
        .getItemProperty(PluginItemProperty.ACTIONS.getPropertyId()).getValue();
    final Label versionLabel = new Label(versionValue);
    try
    {
      final PluginService pluginService = PluginsModule.getPluginsManager().getPluginService((String) itemId);

      if ((pluginService == null) || !isAvailable)
      {
        final Locale locale = UI.getCurrent().getLocale();
        versionLabel.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
            Messages.PLUGINSMANAGEMENT_PLUGIN_UNAVAILABLE));
        versionLabel.setStyleName(NovaForge.LABEL_STRIKE);
      }
    }
    catch (final PluginManagerException e)
    {
      // Nothing to do here
    }

    return versionLabel;
  }

}
