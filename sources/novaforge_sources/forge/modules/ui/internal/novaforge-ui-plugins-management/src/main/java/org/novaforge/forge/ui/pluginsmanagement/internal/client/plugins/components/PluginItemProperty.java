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

import com.vaadin.ui.ComboBox;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;

/**
 * This enum defines the property id available for each item in the project combobox.
 * 
 * @author Jeremy Casery
 */
public enum PluginItemProperty
{
  /**
   * Refere to {@link PluginMetadata#getUUID()}
   * 
   * @see PluginMetadata#getId()
   */
  ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "UUID";
    }
  },
  /**
   * Refere to {@link PluginMetadata#getType()}
   * 
   * @see PluginMetadata#getType()
   */
  TYPE
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "type";
    }
  },
  /**
   * Refere to {@link PluginMetadata#getCatogry().getName()}
   * 
   * @see PluginMetadata#getCatogry().getName()
   */
  CATEGORY_ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "category_id";
    }
  },
  /**
   * Refere to {@link PluginMetadata#getCatogry().getName(Locale)}
   * 
   * @see PluginMetadata#getCatogry().getName(Locale)
   */
  CATEGORY_NAME
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "category_name";
    }
  },
  /**
   * Refere to {@link PluginMetadata#getDescription()}
   * 
   * @see PluginMetadata#getStatus().getDescription()
   */
  STATUS_DECRIPTION
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "status_description";
    }
  },
  /**
   * Refere to {@link PluginMetadata#getStatus().getLabel(Locale)}
   * 
   * @see PluginMetadata#getStatus().getLabel(Locale)
   */
  STATUS_LABEL
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "status_label";
    }
  },
  /**
   * Refere to {@link PluginMetadata#getStatus().getLabel()}
   * 
   * @see PluginMetadata#getStatus().getLabel()
   */
  STATUS_ID
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "status_id";
    }
  },
  /**
   * Refere to {@link PluginMetadata#getVersion()}
   * 
   * @see PluginMetadata#getVersion()
   */
  VERSION
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "version";
    }
  },
  /**
   * Refere to {@link PluginMetadata#isAvailable()}
   * 
   * @see PluginMetadata#isAvailable()
   */
  ACTIONS
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "actions";
    }
  },
  /**
   * Generated property
   */
  EVENTS
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public String getPropertyId()
    {
      return "plugin_events";
    }
  };
  /**
   * Get ItemPropertyId used by {@link ComboBox}
   * 
   * @return property id
   */
  public abstract String getPropertyId();

}
