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
 * This component is used to generate default role column
 * 
 * @author Guillaume Lamirand
 */
public class RoleColumnDefaultGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long           serialVersionUID = 1182364902613851921L;
  /**
   * Reference to {@link RolesHandler}
   */
  private final RolesHandlerComponent handler;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the plugins table
   */
  public RoleColumnDefaultGenerator(final RolesHandlerComponent pPresenter)
  {
    super();
    handler = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final Table pSource, final Object pItemId, final Object pColumnId)
  {
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setWidth(100, Unit.PERCENTAGE);

    // Get the item and the plugin service associated
    final Item item = pSource.getItem(pItemId);
    final Property<?> defaultProperty = item.getItemProperty(RoleItemProperty.DEFAULT.getPropertyId());
    final boolean isDefault = (defaultProperty != null) && (defaultProperty.getValue() != null)
        && ((Boolean) defaultProperty.getValue());
    // Init actions buttons
    final CheckBox defaultBox = new CheckBox();
    defaultBox.setImmediate(true);
    defaultBox.setValue(isDefault);
    defaultBox.setWidth(24, Unit.PIXELS);
    defaultBox.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_ROLES_MEMBER_DEFAULT));

    defaultBox.addValueChangeListener(new ValueChangeListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 3893061306331722740L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void valueChange(final ValueChangeEvent pEvent)
      {
        handler.onClickDefaultRole((String) pItemId);

      }
    });
    actionsLayout.addComponent(defaultBox);
    actionsLayout.setComponentAlignment(defaultBox, Alignment.MIDDLE_CENTER);

    return actionsLayout;
  }
}
