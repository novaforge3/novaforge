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

import com.google.common.base.Strings;
import com.vaadin.data.Property;
import com.vaadin.data.util.IndexedContainer;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class RolesContainer extends IndexedContainer
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = 7245265409387407802L;

  /**
   * Default constructor. It will initialize roles item property
   * 
   * @see RoleItemProperty
   * @see IndexedContainer#IndexedContainer()
   */
  public RolesContainer()
  {
    super();
    addContainerProperty(RoleItemProperty.ORDER.getPropertyId(), Integer.class, null);
    addContainerProperty(RoleItemProperty.NAME.getPropertyId(), String.class, null);
    addContainerProperty(RoleItemProperty.DESCRIPTION.getPropertyId(), String.class, null);
    addContainerProperty(RoleItemProperty.DEFAULT.getPropertyId(), Boolean.class, false);
    addContainerProperty(RoleItemProperty.SELECT.getPropertyId(), Boolean.class, false);
    addContainerProperty(RoleItemProperty.ROLE.getPropertyId(), Role.class, null);
    addContainerProperty(RoleItemProperty.ALLOWED.getPropertyId(), Boolean.class, true);
    addContainerProperty(RoleItemProperty.MAX_ORDER.getPropertyId(), Boolean.class, false);
    addContainerProperty(RoleItemProperty.MIN_ORDER.getPropertyId(), Boolean.class, false);
  }

  /**
   * Add roles into container
   * 
   * @param pRoles
   *          roles to add
   */
  public void setRoles(final List<ProjectRole> pRoles)
  {
    removeAllItems();
    String maxRoleId = "";
    int maxOrder = 1;
    for (final ProjectRole role : pRoles)
    {
      final String itemId = role.getName();
      addItem(itemId);
      getContainerProperty(itemId, RoleItemProperty.ORDER.getPropertyId()).setValue(role.getOrder());
      getContainerProperty(itemId, RoleItemProperty.NAME.getPropertyId()).setValue(role.getName());
      getContainerProperty(itemId, RoleItemProperty.DESCRIPTION.getPropertyId()).setValue(
          role.getDescription());
      getContainerProperty(itemId, RoleItemProperty.ROLE.getPropertyId()).setValue(role);
      // Needed if role is already existing in the container
      getContainerProperty(itemId, RoleItemProperty.SELECT.getPropertyId()).setValue(false);
      getContainerProperty(itemId, RoleItemProperty.ALLOWED.getPropertyId()).setValue(true);
      // Define if the current role has the lowest order
      if (role.getOrder() == 1)
      {
        getContainerProperty(itemId, RoleItemProperty.MIN_ORDER.getPropertyId()).setValue(true);
      }
      else
      {
        getContainerProperty(itemId, RoleItemProperty.MIN_ORDER.getPropertyId()).setValue(false);

      }
      // Define if the current role has the highest order
      if (maxOrder <= role.getOrder())
      {
        maxOrder = role.getOrder();
        maxRoleId = itemId;
      }
      getContainerProperty(itemId, RoleItemProperty.MAX_ORDER.getPropertyId()).setValue(false);
    }
    if (!Strings.isNullOrEmpty(maxRoleId))
    {
      getContainerProperty(maxRoleId, RoleItemProperty.MAX_ORDER.getPropertyId()).setValue(true);
    }

    sort(new Object[] { RoleItemProperty.ORDER.getPropertyId() }, new boolean[] { true });
  }

  /**
   * This will disable user to delete its own right
   */
  public void isCurrentUser()
  {
    final String administratorRoleName = MembershipsModule.getForgeConfigurationService()
        .getForgeAdministratorRoleName();
    getContainerProperty(administratorRoleName, RoleItemProperty.ALLOWED.getPropertyId()).setValue(false);
    final String superAdministratorRoleName = MembershipsModule.getForgeConfigurationService()
        .getForgeSuperAdministratorRoleName();
    final Property<Boolean> superAdminProperty = getContainerProperty(superAdministratorRoleName,
        RoleItemProperty.ALLOWED.getPropertyId());
    if (superAdminProperty != null)
    {
      superAdminProperty.setValue(false);
    }

  }
}
