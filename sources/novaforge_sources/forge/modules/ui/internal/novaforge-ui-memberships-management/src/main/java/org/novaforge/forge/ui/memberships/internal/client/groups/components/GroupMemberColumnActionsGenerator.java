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
package org.novaforge.forge.ui.memberships.internal.client.groups.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.GroupItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.groups.update.GroupsPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

/**
 * @author Guillaume Lamirand
 */
public class GroupMemberColumnActionsGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long     serialVersionUID = 8736106711161577296L;

  private final GroupsPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the plugins table
   */
  public GroupMemberColumnActionsGenerator(final GroupsPresenter pPresenter)
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
    // Add buttons to layout
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.setSpacing(true);
    actionsLayout.setSizeUndefined();

    // Editable or not
    final boolean isEditable = (Boolean) pSource.getContainerProperty(pItemId,
        GroupItemProperty.EDITABLE.getPropertyId()).getValue();
    if (isEditable)
    {
      // ACTION edit group
      final Embedded editIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_GROUP_EDIT));
      editIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      editIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      editIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      editIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MEMBERSHIPS_GROUPS_EDIT_DESCRIPTION));
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
      actionsLayout.addComponent(editIcon);
      actionsLayout.setComponentAlignment(editIcon, Alignment.MIDDLE_CENTER);

      // ACTION edit users group
      final Embedded usersIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_GROUP_USER));
      usersIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      usersIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      usersIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      usersIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MEMBERSHIPS_GROUPS_EDIT_USERS_DESCRIPTION));
      usersIcon.addClickListener(new MouseEvents.ClickListener()
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
          presenter.onClickActionUsers(pItemId);

        }
      });
      actionsLayout.addComponent(usersIcon);
      actionsLayout.setComponentAlignment(usersIcon, Alignment.MIDDLE_CENTER);
    }
    else
    {
      // Add a view user group button
      final Embedded viewUsersIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_GROUP_USER));
      viewUsersIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      viewUsersIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      viewUsersIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      viewUsersIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MEMBERSHIPS_GROUPS_VIEW_USERS_DESCRIPTION));
      viewUsersIcon.addClickListener(new MouseEvents.ClickListener()
      {
        /**
         * serial version id
         */
        private static final long serialVersionUID = 269520862033451016L;

        /**
         * {@inheritDoc}
         */
        @Override
        public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
        {
          pSource.select(pItemId);
          presenter.onClickActionViewUsers(pItemId);

        }
      });
      actionsLayout.addComponent(viewUsersIcon);
      actionsLayout.setComponentAlignment(viewUsersIcon, Alignment.MIDDLE_CENTER);

    }
    // ACTION edit role
    final Embedded editRoleIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_GROUP_RIGHTS));
    editRoleIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    editRoleIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    editRoleIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    editRoleIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_GROUPS_EDIT_ROLES_DESCRIPTION));
    editRoleIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = -2163520041301760959L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.onClickActionRoles(pItemId);

      }
    });
    actionsLayout.addComponent(editRoleIcon);
    actionsLayout.setComponentAlignment(editRoleIcon, Alignment.MIDDLE_CENTER);
    if (isEditable)
    {
      // ACTION delete group
      final Embedded deleteIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_GROUP_TRASH));
      deleteIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
      deleteIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
      deleteIcon.setStyleName(NovaForge.BUTTON_IMAGE);
      deleteIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
          Messages.MEMBERSHIPS_GROUPS_EDIT_DELETE_DESCRIPTION));
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
      actionsLayout.addComponent(deleteIcon);
      actionsLayout.setComponentAlignment(deleteIcon, Alignment.MIDDLE_CENTER);
    }

    return actionsLayout;
  }
}
