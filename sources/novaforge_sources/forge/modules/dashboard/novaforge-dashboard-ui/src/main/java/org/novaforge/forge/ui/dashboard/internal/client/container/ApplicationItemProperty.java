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
package org.novaforge.forge.ui.dashboard.internal.client.container;

import com.vaadin.ui.ComboBox;
import org.novaforge.forge.core.organization.model.ProjectApplication;

/**
 * @author Guillaume Lamirand
 */
public enum ApplicationItemProperty
{
  /**
   * Refere to {@link ProjectApplication#getName()}
   * 
   * @see ProjectApplication#getName()
   */
  NAME
  {
    @Override
    public String getPropertyId()
    {
      return "name";
    }
  },
  /**
   * Refere to {@link ProjectApplication#getDescription()}
   * 
   * @see ProjectApplication#getDescription()
   */
  DESCRIPTION
  {
    @Override
    public String getPropertyId()
    {
      return "description";
    }
  },
  /**
   * Refere to plugin icon
   */
  ICON
  {
    @Override
    public String getPropertyId()
    {
      return "icon";
    }
  },
  /**
   * Refere to {@link ProjectApplication#getPluginUUID()}
   * 
   * @see ProjectApplication#getPluginUUID()
   */
  INSTANCE_UUID
  {
    @Override
    public String getPropertyId()
    {
      return "instance_uuid";
    }
  },
  /**
   * Refere to {@link ProjectApplication#getPluginInstanceUUID()}
   * 
   * @see ProjectApplication#getPluginInstanceUUID()
   */
  PLUGIN_UUID
  {
    @Override
    public String getPropertyId()
    {
      return "plugin_uuid";
    }
  },
  /**
   * Refere to project on which one the application is attached
   */
  PROJECT_ID
  {
    @Override
    public String getPropertyId()
    {
      return "project_id";
    }
  },
  /**
   * Refere to the project name on which one the application is attached
   */
  PROJECT_NAME
  {
    @Override
    public String getPropertyId()
    {
      return "project_name";
    }
  },
  /**
   * Refere to the project icon on which one the application is attached
   */
  PROJECT_ICON
  {
    @Override
    public String getPropertyId()
    {
      return "project_icon";
    }
  };
  /**
   * Get ItemPropertyId used by {@link ComboBox}
   * 
   * @return property id
   */
  public abstract String getPropertyId();
}
