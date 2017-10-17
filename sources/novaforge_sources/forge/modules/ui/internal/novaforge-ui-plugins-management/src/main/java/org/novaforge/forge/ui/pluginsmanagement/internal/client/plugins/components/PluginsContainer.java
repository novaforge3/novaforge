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

import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.ui.pluginsmanagement.internal.module.PluginsModule;

import java.util.List;
import java.util.Locale;

/**
 * This component describes a specific {@link IndexedContainer} used to build projects combobox.
 * 
 * @author Jeremy Casery
 */
public class PluginsContainer extends IndexedContainer
{

  /**
   * SerialUID
   */
  private static final long serialVersionUID = -2561936415328606638L;

  /**
   * Default constructor. It will initialize plugin item property
   * 
   * @see PluginItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public PluginsContainer()
  {
    super();
    addContainerProperty(PluginItemProperty.ID.getPropertyId(), String.class, null);
    addContainerProperty(PluginItemProperty.TYPE.getPropertyId(), String.class, null);
    addContainerProperty(PluginItemProperty.CATEGORY_ID.getPropertyId(), String.class, null);
    addContainerProperty(PluginItemProperty.CATEGORY_NAME.getPropertyId(), String.class, null);
    addContainerProperty(PluginItemProperty.VERSION.getPropertyId(), String.class, null);
    addContainerProperty(PluginItemProperty.STATUS_ID.getPropertyId(), String.class, null);
    addContainerProperty(PluginItemProperty.STATUS_LABEL.getPropertyId(), String.class, null);
    addContainerProperty(PluginItemProperty.STATUS_DECRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(PluginItemProperty.ACTIONS.getPropertyId(), Boolean.class, null);
  }

  /**
   * Add plugins into container
   * 
   * @param pPlugins
   *          plugins to add
   * @param pLocale
   *          user's locale
   */
  @SuppressWarnings("unchecked")
  public void setPlugins(final List<PluginMetadata> pPlugins, final Locale pLocale)
  {
    removeAllItems();
    for (final PluginMetadata plugin : pPlugins)
    {
      addItem(plugin.getUUID());
      getContainerProperty(plugin.getUUID(), PluginItemProperty.ID.getPropertyId())
          .setValue(plugin.getUUID());
      getContainerProperty(plugin.getUUID(), PluginItemProperty.TYPE.getPropertyId()).setValue(
          plugin.getType());

      getContainerProperty(plugin.getUUID(), PluginItemProperty.CATEGORY_ID.getPropertyId()).setValue(
          plugin.getCategory());
      getContainerProperty(plugin.getUUID(), PluginItemProperty.CATEGORY_NAME.getPropertyId())
          .setValue(
              PluginsModule.getPluginsCategoryManager().getCategoryService(plugin.getCategory())
                  .getName(pLocale));
      getContainerProperty(plugin.getUUID(), PluginItemProperty.VERSION.getPropertyId()).setValue(
          plugin.getVersion());
      getContainerProperty(plugin.getUUID(), PluginItemProperty.STATUS_ID.getPropertyId()).setValue(
          plugin.getStatus().getLabel());
      getContainerProperty(plugin.getUUID(), PluginItemProperty.STATUS_LABEL.getPropertyId()).setValue(
          plugin.getStatus().getLabel(pLocale));
      getContainerProperty(plugin.getUUID(), PluginItemProperty.STATUS_DECRIPTION.getPropertyId()).setValue(
          plugin.getStatus().getDescription(pLocale));
      getContainerProperty(plugin.getUUID(), PluginItemProperty.ACTIONS.getPropertyId()).setValue(
          plugin.isAvailable());
    }
    sort(new Object[] { PluginItemProperty.TYPE.getPropertyId() }, new boolean[] { true });
  }

}
