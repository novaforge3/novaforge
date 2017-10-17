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
import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.RoleItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.roles.update.RolesPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

/**
 * @author Guillaume Lamirand
 */
public class RoleColumnActionsGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long    serialVersionUID = 8736106711161577296L;

  private final RolesPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the plugins table
   */
  public RoleColumnActionsGenerator(final RolesPresenter pPresenter)
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
    // ACTION edit group
    final Embedded editIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_KEY_EDIT));
    editIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    editIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    editIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    editIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_ROLES_EDIT_INFO));
    editIcon.addClickListener(new MouseEvents.ClickListener()
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
        presenter.onClickActionEdit(pItemId);

      }
    });

    // ACTION edit app mapping
    final Embedded appIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_KEY_ADMIN));
    appIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    appIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    appIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    appIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_ROLES_EDIT_MAPPING));
    appIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * serial version id
       */
      private static final long serialVersionUID = -1712699522491483659L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.onClickActionApp(pItemId);

      }
    });
    // ACTION delete role
    final Embedded deleteIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_KEY_TRASH));
    deleteIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    deleteIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    deleteIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    final Item item = pSource.getItem(pItemId);
    final Role role = (Role) item.getItemProperty(RoleItemProperty.ROLE.getPropertyId()).getValue();
    if (RealmType.SYSTEM.equals(role.getRealmType()))
    {
      deleteIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MEMBERSHIPS_ROLES_EDIT_DELETE_SYSTEM));
      deleteIcon.setEnabled(false);
      deleteIcon.setVisible(false);
    }
    else
    {
      deleteIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MEMBERSHIPS_ROLES_EDIT_DELETE));
    }
    deleteIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * serial version id
       */
      private static final long serialVersionUID = -1712699522491483659L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.onClickActionDelete(pItemId);

      }
    });

    // Add buttons to layout
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setSpacing(true);
    actionsLayout.addComponent(editIcon);
    actionsLayout.addComponent(appIcon);
    actionsLayout.addComponent(deleteIcon);
    actionsLayout.setComponentAlignment(editIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(appIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(deleteIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setSizeUndefined();

    return actionsLayout;
  }
}
