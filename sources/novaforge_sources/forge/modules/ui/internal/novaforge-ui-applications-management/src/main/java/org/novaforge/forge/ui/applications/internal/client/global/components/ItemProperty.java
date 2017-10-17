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
package org.novaforge.forge.ui.applications.internal.client.global.components;

import com.vaadin.ui.Tree;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;

/**
 * This enum defines the property id available for each item.
 * 
 * @author Guillaume Lamirand
 */
public enum ItemProperty
{
  NODE
  {
    @Override
    public String getPropertyId()
    {
      return "node";
    }
  },
  /**
   * Refere to {@link ProjectApplication#getName()} or {@link Space#getName()}
   * 
   * @see ProjectApplication#getName()
   * @see Space#getName()
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
   * Refere to {@link ProjectApplication#getDescription()} or {@link Space#getDescription()}
   * 
   * @see ProjectApplication#getDescription()
   * @see Space#getDescription()
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
   * Refere to plugin availability
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
   * Contains plugin icon
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
   * Describes the configuration column
   */
  SETUP
  {
    @Override
    public String getPropertyId()
    {
      return "setup";
    }
  },
  COMPATIBLE
  {
    @Override
    public String getPropertyId()
    {
      return "compatible";
    }
  },
  ENABLE
  {
    @Override
    public String getPropertyId()
    {
      return "enable";
    }
  },
  ASSOCIATIONS
  {
    @Override
    public String getPropertyId()
    {
      return "associations";
    }
  },
  COMPOSITION
  {
    @Override
    public String getPropertyId()
    {
      return "composition";
    }
  },
  SOURCE_NAME
  {
    @Override
    public String getPropertyId()
    {
      return "source_name";
    }
  },
  TARGET_NAME
  {
    @Override
    public String getPropertyId()
    {
      return "target_name";
    }
  },
  VALUE
  {
    @Override
    public String getPropertyId()
    {
      return "value";
    }
  };
  /**
   * Get ItemPropertyId used by {@link Tree}
   * 
   * @return property id
   */
  public abstract String getPropertyId();

}
