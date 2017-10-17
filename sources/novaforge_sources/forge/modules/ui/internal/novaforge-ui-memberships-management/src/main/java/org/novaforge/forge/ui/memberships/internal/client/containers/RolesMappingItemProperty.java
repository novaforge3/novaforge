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
package org.novaforge.forge.ui.memberships.internal.client.containers;

import com.vaadin.ui.Tree;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;

/**
 * This enum defines the property id available for each item in role mapping view.
 * 
 * @author Guillaume Lamirand
 */
public enum RolesMappingItemProperty
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
  DESCRIPTION
  {
    @Override
    public String getPropertyId()
    {
      return "description";
    }
  },
  AVAILABILITY
  {
    @Override
    public String getPropertyId()
    {
      return "availability";
    }
  },
  ICON
  {
    @Override
    public String getPropertyId()
    {
      return "icon";
    }
  },
  ROLE_MAPPED
  {
    @Override
    public String getPropertyId()
    {
      return "role_mapped";
    }
  },
  ROLES
  {
    @Override
    public String getPropertyId()
    {
      return "roles";
    }
  };
  /**
   * Get ItemPropertyId used by {@link Tree}
   * 
   * @return property id
   */
  public abstract String getPropertyId();

}
