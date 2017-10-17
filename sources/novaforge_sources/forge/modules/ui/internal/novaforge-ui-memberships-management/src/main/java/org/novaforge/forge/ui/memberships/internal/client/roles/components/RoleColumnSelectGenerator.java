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
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.memberships.internal.client.containers.RoleItemProperty;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

/**
 * This component is used to generate select column
 * 
 * @author Guillaume Lamirand
 */
public class RoleColumnSelectGenerator implements ColumnGenerator
{

  /**
   * Serial version id
   */
  private static final long           serialVersionUID = -468672684549374448L;
  /**
   * Reference to {@link RolesHandlerComponent}
   */
  private final RolesHandlerComponent handler;

  /**
   * Default Constructor
   * 
   * @param pHandler
   *          the presenter of the plugins table
   */
  public RoleColumnSelectGenerator(final RolesHandlerComponent pHandler)
  {
    super();
    handler = pHandler;
  }

  @Override
  public Object generateCell(final Table pSource, final Object pItemId, final Object pColumnId)
  {
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setWidth(100, Unit.PERCENTAGE);

    // Get the item and the plugin service associated
    final Item item = pSource.getItem(pItemId);
    final Property<?> selectProperty = item.getItemProperty(RoleItemProperty.SELECT.getPropertyId());
    final boolean isEnable = (selectProperty != null) && (selectProperty.getValue() != null)
        && ((Boolean) selectProperty.getValue());
    final Property<?> allowedProperty = item.getItemProperty(RoleItemProperty.ALLOWED.getPropertyId());
    final boolean isAllowed = (allowedProperty != null) && (allowedProperty.getValue() != null)
        && ((Boolean) allowedProperty.getValue());
    // Init actions buttons
    final CheckBox selectBox = new CheckBox();
    selectBox.setEnabled(isAllowed);
    selectBox.setImmediate(true);
    selectBox.setValue(isEnable);
    selectBox.setWidth(24, Unit.PIXELS);

    selectBox.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_ROLES_MEMBER_SELECT_DESCRIPTION));

    selectBox.addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3788718722439266733L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        handler.onClickSelectRole((String) pItemId);

      }
    });
    actionsLayout.addComponent(selectBox);
    actionsLayout.setComponentAlignment(selectBox, Alignment.MIDDLE_CENTER);

    return actionsLayout;
  }

}
