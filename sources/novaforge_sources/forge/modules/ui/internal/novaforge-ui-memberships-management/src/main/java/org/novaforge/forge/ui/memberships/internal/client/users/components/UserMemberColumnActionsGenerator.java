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
package org.novaforge.forge.ui.memberships.internal.client.users.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.users.update.UsersPresenter;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

/**
 * @author Guillaume Lamirand
 */
public class UserMemberColumnActionsGenerator implements ColumnGenerator
{
  /**
   * Serial version id
   */
  private static final long    serialVersionUID = 8736106711161577296L;

  private final UsersPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the plugins table
   */
  public UserMemberColumnActionsGenerator(final UsersPresenter pPresenter)
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
    // ACTION view profile
    final Embedded viewProfileIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_USER_INFO));
    viewProfileIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    viewProfileIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    viewProfileIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    viewProfileIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_USERS_EDIT_PROFILE));
    viewProfileIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4799439581656895309L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        String userLogin = (String) pSource.getItem(pItemId)
            .getItemProperty(UserItemProperty.LOGIN.getPropertyId()).getValue();
        presenter.onClickActionProfile(userLogin);

      }
    });

    // ACTION edit roles
    final Embedded editRolesIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_USER_RIGHTS));
    editRolesIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    editRolesIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    editRolesIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    editRolesIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_USERS_EDIT_ROLES));
    editRolesIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 4799439581656895309L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        pSource.select(pItemId);
        presenter.onClickActionEdit(pItemId);

      }
    });

    // ACTION delete user
    final Embedded deleteMemberIcon = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_USER_DELETE));
    deleteMemberIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    deleteMemberIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    deleteMemberIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    deleteMemberIcon.setDescription(MembershipsModule.getPortalMessages().getMessage(pSource.getLocale(),
        Messages.MEMBERSHIPS_USERS_EDIT_DELETE));
    deleteMemberIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
			 * 
			 */
      private static final long serialVersionUID = 671779180324183911L;

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
    actionsLayout.addComponent(viewProfileIcon);
    actionsLayout.addComponent(editRolesIcon);
    actionsLayout.addComponent(deleteMemberIcon);
    actionsLayout.setComponentAlignment(viewProfileIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(editRolesIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(deleteMemberIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setSizeUndefined();

    return actionsLayout;
  }
}
