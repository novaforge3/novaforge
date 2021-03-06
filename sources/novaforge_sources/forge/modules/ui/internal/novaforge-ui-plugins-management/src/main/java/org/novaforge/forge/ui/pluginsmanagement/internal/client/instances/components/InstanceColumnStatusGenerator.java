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
package org.novaforge.forge.ui.pluginsmanagement.internal.client.instances.components;

import com.vaadin.data.Item;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstanceStatus;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;

import java.util.Locale;

/**
 * @author Jeremy Casery
 */
public class InstanceColumnStatusGenerator implements ColumnGenerator
{

  /**
   * SerialUID
   */
  private static final long serialVersionUID = 2065938643936701458L;

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final Table pSource, final Object itemId, final Object columnId)
  {
    final Item item = pSource.getItem(itemId);
    final boolean isShareable = (Boolean) item.getItemProperty(
        InstancesItemProperty.SHAREABLE.getPropertyId()).getValue();
    final ToolInstanceStatus status = (ToolInstanceStatus) item.getItemProperty(
        InstancesItemProperty.STATUS.getPropertyId()).getValue();
    final Embedded icon = new Embedded(null);
    final Locale locale = UI.getCurrent().getLocale();
    if ((isShareable) || (ToolInstanceStatus.AVAILABLE.equals(status)))
    {
      icon.setSource(new ThemeResource(NovaForgeResources.ICON_VALIDATE_ROUND));
      icon.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
          Messages.PLUGINSMANAGEMENT_FIELD_STATUS_OK_DESC));
    }
    else if (ToolInstanceStatus.BUSY.equals(status))
    {
      icon.setSource(new ThemeResource(NovaForgeResources.ICON_BLOCKED_ROUND));
      icon.setDescription(PluginsModule.getPortalMessages().getMessage(locale,
          Messages.PLUGINSMANAGEMENT_FIELD_STATUS_KO_DESC));
    }

    icon.setWidth(NovaForge.ACTION_ICON_SIZE);
    return icon;

  }
}
