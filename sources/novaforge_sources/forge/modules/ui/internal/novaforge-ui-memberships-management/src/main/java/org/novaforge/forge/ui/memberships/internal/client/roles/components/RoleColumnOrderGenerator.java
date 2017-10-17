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
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.RoleItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.roles.update.RolesPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

/**
 * @author Guillaume Lamirand
 */
public class RoleColumnOrderGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long    serialVersionUID = -1606291381131548710L;

  private final RolesPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the plugins table
   */
  public RoleColumnOrderGenerator(final RolesPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table pSource, final Object pItemId, final Object pColumnId)
  {
    // Get role orders
    final Item item = pSource.getItem(pItemId);
    final Property<?> maxProperty = item.getItemProperty(RoleItemProperty.MAX_ORDER.getPropertyId());
    final boolean isMax = (maxProperty != null) && (maxProperty.getValue() != null)
        && ((Boolean) maxProperty.getValue());

    final Property<?> minProperty = item.getItemProperty(RoleItemProperty.MIN_ORDER.getPropertyId());
    final boolean isMin = (minProperty != null) && (minProperty.getValue() != null)
        && ((Boolean) minProperty.getValue());

    // Build the down button
    final Embedded downIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_ARROW_BOTTOM2));
    downIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    downIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    downIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    downIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_ROLES_EDIT_ORDER_DOWN));
    downIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -1712699522491483659L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.onClickOrderDown(pItemId);

      }
    });

    // Build the up button
    final Embedded topIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_ARROW_TOP2));
    topIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    topIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    topIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    topIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_ROLES_EDIT_ORDER_UP));
    topIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -1712699522491483659L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.onClickOrderUp(pItemId);

      }
    });

    // Add buttons to layout
    final HorizontalLayout orderLayout = new HorizontalLayout();
    orderLayout.setSpacing(true);
    orderLayout.addComponent(downIcon);
    orderLayout.addComponent(topIcon);
    orderLayout.setComponentAlignment(downIcon, Alignment.MIDDLE_LEFT);
    orderLayout.setComponentAlignment(topIcon, Alignment.MIDDLE_RIGHT);
    // orderLayout.setSizeUndefined();
    orderLayout.setSizeFull();

    // Enable the button according the role order position
    if (isMax)
    {
      downIcon.setEnabled(false);
      downIcon.setVisible(false);
    }

    if (isMin)
    {
      topIcon.setEnabled(false);
      topIcon.setVisible(false);
    }

    return orderLayout;
  }
}
