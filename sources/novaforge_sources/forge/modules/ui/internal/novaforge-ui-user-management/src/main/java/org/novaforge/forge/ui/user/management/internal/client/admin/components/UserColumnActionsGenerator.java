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
package org.novaforge.forge.ui.user.management.internal.client.admin.components;

import com.vaadin.event.MouseEvents;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table.ColumnGenerator;
import com.vaadin.ui.UI;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.user.management.internal.client.admin.UserManagementPresenter;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;

import java.util.Locale;

/**
 * The user actions column generator
 * 
 * @author Jeremy Casery
 */
public class UserColumnActionsGenerator implements ColumnGenerator
{
  /**
   * Default serial version UID
   */
  private static final long             serialVersionUID = 9021868368632161289L;
  /**
   * The {@link UserManagementPresenter} associated to this generator
   */
  private final UserManagementPresenter presenter;

  /**
   * Default Constructor
   * 
   * @param pPresenter
   *          the presenter of the plugins table
   */
  public UserColumnActionsGenerator(final UserManagementPresenter pPresenter)
  {
    super();
    presenter = pPresenter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object generateCell(final com.vaadin.ui.Table source, final Object itemId, final Object columnId)
  {
    final String currentUserLogin = (String) itemId;
    // Get Locale
    final Locale locale = UI.getCurrent().getLocale();
    // ACTION view profile
    final Embedded viewProfileIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_USER_INFO));
    viewProfileIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    viewProfileIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    viewProfileIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    viewProfileIcon.setDescription(AdminModule.getPortalMessages().getMessage(locale,
        Messages.USERMANAGEMENT_ACTION_VIEW_USER));
    viewProfileIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -1678357398179400014L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.actionViewProfileClicked(currentUserLogin);
      }
    });

    // ACTION edit profile
    final Embedded editProfileIcon = new Embedded(null, new ThemeResource(NovaForgeResources.ICON_USER_EDIT));
    editProfileIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    editProfileIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    editProfileIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    editProfileIcon.setDescription(AdminModule.getPortalMessages().getMessage(locale,
        Messages.USERMANAGEMENT_ACTION_EDIT_USER));
    editProfileIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 4799439581656895309L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.actionEditProfileClicked(currentUserLogin);

      }
    });

    // ACTION delete user
    final Embedded deleteProfileIcon = new Embedded(null, new ThemeResource(
        NovaForgeResources.ICON_USER_TRASH));
    deleteProfileIcon.setWidth(NovaForge.ACTION_ICON_SIZE);
    deleteProfileIcon.setHeight(NovaForge.ACTION_ICON_SIZE);
    deleteProfileIcon.setStyleName(NovaForge.BUTTON_IMAGE);
    deleteProfileIcon.setDescription(AdminModule.getPortalMessages().getMessage(locale,
        Messages.USERMANAGEMENT_ACTION_DELETE_USER));
    deleteProfileIcon.addClickListener(new MouseEvents.ClickListener()
    {

      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 671779180324183911L;

      @Override
      public void click(final com.vaadin.event.MouseEvents.ClickEvent event)
      {
        presenter.actionDeleteUserClicked(currentUserLogin);

      }
    });
    if (currentUserLogin.equals(AdminModule.getAuthentificationService().getCurrentUser()))
    {
      deleteProfileIcon.setVisible(false);
    }
    // Add buttons to layout
    final HorizontalLayout actionsLayout = new HorizontalLayout();
    actionsLayout.addComponent(viewProfileIcon);
    actionsLayout.addComponent(editProfileIcon);
    actionsLayout.addComponent(deleteProfileIcon);
    actionsLayout.setComponentAlignment(viewProfileIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(editProfileIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setComponentAlignment(deleteProfileIcon, Alignment.MIDDLE_CENTER);
    actionsLayout.setSizeUndefined();

    return actionsLayout;
  }
}
