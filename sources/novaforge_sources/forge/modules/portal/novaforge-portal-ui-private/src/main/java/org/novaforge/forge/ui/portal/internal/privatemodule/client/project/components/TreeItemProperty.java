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
package org.novaforge.forge.ui.portal.internal.privatemodule.client.project.components;

import com.vaadin.ui.Tree;
import org.novaforge.forge.portal.models.PortalApplication;
import org.novaforge.forge.portal.models.PortalSpace;
import org.novaforge.forge.portal.models.PortalURI;

/**
 * This enum defines the property id available for each item in the navigation tree.
 * 
 * @author Guillaume Lamirand
 */
public enum TreeItemProperty
{
  /**
   * Refere to {@link PortalApplication#getId()} or {@link PortalSpace#getId()}
   * 
   * @see PortalApplication#getId()
   * @see PortalSpace#getId()
   */
  ID
  {
    @Override
    public String getPropertyId()
    {
      return "id";
    }
  },
  /**
   * Refere to {@link PortalApplication#getUniqueId}
   * 
   * @see PortalApplication#getUniqueId()
   */
  UUID
  {
    @Override
    public String getPropertyId()
    {
      return "uuid";
    }
  },
  /**
   * Refere to {@link PortalApplication#getName()} or {@link PortalSpace#getName()}
   * 
   * @see PortalApplication#getName()
   * @see PortalSpace#getName()
   */
  CAPTION
  {
    @Override
    public String getPropertyId()
    {
      return "caption";
    }
  },
  /**
   * Refere to {@link PortalApplication#getName()} or {@link PortalSpace#getDescription()}
   * 
   * @see PortalApplication#getName()
   * @see PortalSpace#getDescription()
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
   * Refere to {@link PortalApplication#isAvailable()}
   * 
   * @see PortalApplication#isAvailable()
   */
  AVAILABILITY
  {
    @Override
    public String getPropertyId()
    {
      return "availability";
    }
  },
  /**
   * Refere to {@link PortalApplication#isAvailable()}
   * 
   * @see PortalApplication#getStatus()
   */
  STATUS
  {
    @Override
    public String getPropertyId()
    {
      return "status";
    }
  },
  /**
   * Refere to {@link PortalApplication#getIcon()}
   * 
   * @see PortalApplication#getIcon()
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
   * Contains the module id to contact application
   * 
   * @see PortalURI#getModuleId()
   */
  MODULE_ID
  {
    @Override
    public String getPropertyId()
    {
      return "module_id";
    }
  },
  /**
   * Contains the absolute URL to contact application
   * 
   * @see PortalURI#getAbsoluteURL()
   */
  URL
  {
    @Override
    public String getPropertyId()
    {
      return "url";
    }
  },
  /**
   * Refere to {@link PortalApplication#isInternal()}
   * 
   * @see PortalApplication#isInternal()
   */
  INTERNAL
  {
    @Override
    public String getPropertyId()
    {
      return "internal";
    }
  };
  /**
   * Get ItemPropertyId used by {@link Tree}
   * 
   * @return property id
   */
  public abstract String getPropertyId();

}
