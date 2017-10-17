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
package org.novaforge.forge.ui.memberships.internal.client.users.add;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RolesHandlerComponent;
import org.novaforge.forge.ui.memberships.internal.client.users.components.UserTableSelectable;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class AddUsersViewImpl extends VerticalLayout implements AddUserView
{

  /**
   * Serialization id
   */
  private static final long     serialVersionUID = -4420222948069676402L;

  private Button                returnButton;

  private UserTableSelectable   userTableSelectable;
  private Button                addButton;

  private Window                addRolesWindow;
  private RolesHandlerComponent addRolesComponent;

  /**
   * Default constructor.
   */
  public AddUsersViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component headers = initHeaders();
    final Component content = initContent();
    final Component footer = initFooter();
    addComponent(headers);
    addComponent(content);
    addComponent(footer);

    // Init subwindows
    initAddRolesWindow();
  }

  private Component initHeaders()
  {
    final HorizontalLayout headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    returnButton = new Button();
    returnButton.setIcon(new ThemeResource(NovaForgeResources.ICON_ARROW_LEFT));

    headerButtons.addComponent(returnButton);
    return headerButtons;
  }

  private Component initContent()
  {
    userTableSelectable = new UserTableSelectable();
    return userTableSelectable;
  }

  private Component initFooter()
  {
    final HorizontalLayout footerButtons = new HorizontalLayout();
    footerButtons.setSpacing(true);
    footerButtons.setMargin(new MarginInfo(true, false, true, true));
    addButton = new Button();
    addButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    addButton.setIcon(new ThemeResource(NovaForgeResources.ICON_KEY));

    footerButtons.addComponent(addButton);
    return footerButtons;
  }

  private void initAddRolesWindow()
  {
    addRolesWindow = new Window();
    addRolesWindow.setModal(true);
    addRolesWindow.setResizable(false);

    addRolesComponent = new RolesHandlerComponent();
    addRolesWindow.setContent(addRolesComponent);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {
    returnButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_ADD_BACK));
    userTableSelectable.refreshLocale(pLocale);
    addButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_ADD_CONFIRM));

    addRolesComponent.refreshLocale(pLocale);
    addRolesWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_ADD_ROLES_DESCRIPTION));
    addRolesComponent.getConfirmButton().setCaption(
        MembershipsModule.getPortalMessages().getMessage(pLocale,
            Messages.MEMBERSHIPS_USERS_ADD_ROLES_CONFIRM));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getReturnButton()
  {
    return returnButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getAddRolesWindow()
  {
    return addRolesWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RolesHandlerComponent getAddRolesComponent()
  {
    return addRolesComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getAddButton()
  {
    return addButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserTableSelectable getUserTableSelectable()
  {
    return userTableSelectable;
  }

}
