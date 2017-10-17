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
package org.novaforge.forge.ui.memberships.internal.client.roles.components;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.memberships.internal.client.containers.RolesMappingItemProperty;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class RolesMappingRolesGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long           serialVersionUID = -3663414511836833615L;
  /**
   * Contains reference to {@link RolesMappingComponent}
   */
  private final RolesMappingComponent rolesMappingComponent;

  /**
   * Default constructor
   * 
   * @param pRolesMappingComponent
   *          the component associated
   */
  public RolesMappingRolesGenerator(final RolesMappingComponent pRolesMappingComponent)
  {
    rolesMappingComponent = pRolesMappingComponent;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings({ "unchecked" })
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    Component returnComponent = null;

    // Get the item and the plugin service associated
    final Item item = pSource.getItem(pItemId);
    final Property<?> nodeProperty = item.getItemProperty(RolesMappingItemProperty.NODE.getPropertyId());
    if ((nodeProperty != null) && (nodeProperty.getValue() != null)
        && (nodeProperty.getValue() instanceof ProjectApplication))
    {
      final Property<?> compProperty = item.getItemProperty(RolesMappingItemProperty.ROLES.getPropertyId());
      final Set<String> roles = (Set<String>) compProperty.getValue();
      final ComboBox roleBox = new ComboBox();
      if (roles != null)
      {
        for (final String pluginRoleName : roles)
        {
          roleBox.addItem(pluginRoleName);
        }
        roleBox.setInputPrompt(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
            Messages.MEMBERSHIPS_ROLES_NOROLE));
      }
      else
      {
        roleBox.setEnabled(false);
        roleBox.setInputPrompt(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
            Messages.MEMBERSHIPS_ROLES_UNAVAILABLE));
      }
      roleBox.setTextInputAllowed(!rolesMappingComponent.isAdminRole());
      roleBox.setNullSelectionAllowed(!rolesMappingComponent.isAdminRole());
      final Property<?> roleMappedProperty = item.getItemProperty(RolesMappingItemProperty.ROLE_MAPPED
          .getPropertyId());
      if ((roleMappedProperty != null) && (roleMappedProperty.getValue() != null))
      {
        roleBox.setValue(roleMappedProperty.getValue());
      }
      roleBox.addValueChangeListener(new ValueChangeListener()
      {

        /**
         * Serial version id
         */
        private static final long serialVersionUID = -3504194292044073892L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void valueChange(final ValueChangeEvent pEvent)
        {
          final String roleName = (String) pEvent.getProperty().getValue();
          item.getItemProperty(RolesMappingItemProperty.ROLE_MAPPED.getPropertyId()).setValue(roleName);
        }
      });
      returnComponent = roleBox;
    }

    return returnComponent;
  }
}
