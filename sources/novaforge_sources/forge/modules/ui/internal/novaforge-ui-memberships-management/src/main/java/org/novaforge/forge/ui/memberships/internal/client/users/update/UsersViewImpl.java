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
package org.novaforge.forge.ui.memberships.internal.client.users.update;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.memberships.internal.client.containers.UserItemProperty;
import org.novaforge.forge.ui.memberships.internal.client.roles.components.RolesHandlerComponent;
import org.novaforge.forge.ui.memberships.internal.module.MembershipsModule;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;

import java.util.Collection;
import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class UsersViewImpl extends VerticalLayout implements UserView
{

  /**
   * Serialization id
   */
  private static final long     serialVersionUID = -4420222948069676402L;

  /**
   * Constant for filter field
   */
  private static final String   FILTER           = "filter";
  private Button                addButton;

  private TextField             filterTextField;
  private Form                  usersForm;
  private Table                 usersTable;
  private Window                deleteUserWindow;
  private Label                 deleteUserConfirmLabel;
  private CheckBox              deleteUserNotifCheck;
  private Button                deleteUserCancelButton;
  private Button                deleteUserConfirmButton;
  private Window                editRolesWindow;
  private RolesHandlerComponent editRolesComponent;

  /**
   * Default constructor.
   */
  public UsersViewImpl()
  {
    // Init view
    setMargin(true);

    // Init contents
    final Component headers = initHeaders();
    final Component filter = initFilter();
    final Component content = initContent();
    addComponent(headers);
    addComponent(filter);
    addComponent(content);

    // Init subwindows
    initDeleteUserWindow();
    initEditRolesWindow();
  }

  private Component initHeaders()
  {
    final HorizontalLayout headerButtons = new HorizontalLayout();
    headerButtons.setSpacing(true);
    headerButtons.setMargin(new MarginInfo(false, false, true, false));
    addButton = new Button();
    addButton.setStyleName(Reindeer.BUTTON_DEFAULT);
    addButton.setIcon(new ThemeResource(NovaForgeResources.ICON_PLUS));

    headerButtons.addComponent(addButton);
    return headerButtons;
  }

  private Component initFilter()
  {
    usersForm = new Form();
    usersForm.setImmediate(true);
    usersForm.setInvalidCommitted(false);

    filterTextField = new TextField();
    usersForm.addField(FILTER, filterTextField);
    return usersForm;
  }

  private Component initContent()
  {
    usersTable = new Table();
    usersTable.setSelectable(true);
    usersTable.setPageLength(10);
    usersTable.setWidth(100, Unit.PERCENTAGE);
    usersTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = -3559455760887782118L;

      @Override
      public void layoutClick(final LayoutClickEvent event)
      {

        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if ((child == null) || (!child.equals(usersTable)))
        {
          final Collection<?> itemIds = usersTable.getItemIds();
          for (final Object itemId : itemIds)
          {
            if (usersTable.isSelected(itemId))
            {
              usersTable.unselect(itemId);
              break;
            }
          }
        }
      }
    });
    return usersTable;
  }

  private void initDeleteUserWindow()
  {
    deleteUserWindow = new Window();
    deleteUserWindow.setModal(true);
    deleteUserWindow.setResizable(false);
    deleteUserWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_DELETE));

    deleteUserConfirmLabel = new Label();
    deleteUserConfirmLabel.setContentMode(ContentMode.HTML);

    // Configure the windws layout; by default a VerticalLayout
    final VerticalLayout windowLayout = new VerticalLayout();
    windowLayout.setMargin(true);
    windowLayout.setSpacing(true);
    windowLayout.setWidth(400, Unit.PIXELS);

    // Init notification bos
    deleteUserNotifCheck = new CheckBox();
    deleteUserNotifCheck.setValue(true);

    final HorizontalLayout buttons = new HorizontalLayout();
    buttons.setSpacing(true);
    deleteUserConfirmButton = new Button();
    deleteUserCancelButton = new Button();
    deleteUserCancelButton.setStyleName(NovaForge.BUTTON_LINK);
    deleteUserConfirmButton.setIcon(new ThemeResource(NovaForgeResources.ICON_CLOSE_RED));
    deleteUserConfirmButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    buttons.addComponent(deleteUserCancelButton);
    buttons.addComponent(deleteUserConfirmButton);
    buttons.setComponentAlignment(deleteUserCancelButton, Alignment.MIDDLE_CENTER);
    buttons.setComponentAlignment(deleteUserConfirmButton, Alignment.MIDDLE_CENTER);
    // Set window content
    windowLayout.addComponent(deleteUserConfirmLabel);
    windowLayout.addComponent(deleteUserNotifCheck);
    windowLayout.addComponent(buttons);
    windowLayout.setComponentAlignment(deleteUserConfirmLabel, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(deleteUserNotifCheck, Alignment.MIDDLE_CENTER);
    windowLayout.setComponentAlignment(buttons, Alignment.MIDDLE_CENTER);
    deleteUserWindow.setContent(windowLayout);

  }

  private void initEditRolesWindow()
  {
    editRolesWindow = new Window();
    editRolesWindow.setModal(true);
    editRolesWindow.setResizable(false);
    editRolesWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_RIGHTS));

    editRolesComponent = new RolesHandlerComponent();
    editRolesWindow.setContent(editRolesComponent);

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
    addButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_EDIT_ADD));
    usersForm.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_EDIT_TITLE));
    filterTextField.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_FILTER));
    usersTable.setColumnHeader(UserItemProperty.LOGIN.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_USERS_LOGIN));
    usersTable.setColumnHeader(UserItemProperty.FIRSTNAME.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_USERS_FIRSTNAME));
    usersTable.setColumnHeader(UserItemProperty.LASTNAME.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.MEMBERSHIPS_USERS_NAME));
    usersTable.setColumnHeader(UserItemProperty.EMAIL.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_USERS_EMAIL));
    usersTable.setColumnHeader(UserItemProperty.ROLES.getPropertyId(), MembershipsModule.getPortalMessages()
        .getMessage(pLocale, Messages.MEMBERSHIPS_USERS_EDIT_ROLES_COLUMN));
    usersTable.setColumnHeader(CommonItemProperty.ACTIONS.getPropertyId(), MembershipsModule
        .getPortalMessages().getMessage(pLocale, Messages.ACTIONS));

    deleteUserWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_EDIT_DELETE_TITLE));
    deleteUserConfirmLabel.setValue(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_EDIT_DELETE_CONFIRMLABEL));
    deleteUserNotifCheck.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_EDIT_DELETE_NOTIFICATION));
    deleteUserCancelButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.ACTIONS_CANCEL));
    deleteUserConfirmButton.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_EDIT_DELETE_CONFIRM));

    editRolesComponent.refreshLocale(pLocale);
    editRolesWindow.setCaption(MembershipsModule.getPortalMessages().getMessage(pLocale,
        Messages.MEMBERSHIPS_USERS_EDIT_ROLES_TITLE));
    editRolesComponent.getConfirmButton().setCaption(
        MembershipsModule.getPortalMessages().getMessage(pLocale,
            Messages.MEMBERSHIPS_USERS_EDIT_ROLES_CONFIRM));
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
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Table getUsersTable()
  {
    return usersTable;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getEditRolesWindow()
  {
    return editRolesWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RolesHandlerComponent getEditRolesComponent()
  {
    return editRolesComponent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getDeleteUserWindow()
  {
    return deleteUserWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getDeleteUserConfirmButton()
  {
    return deleteUserConfirmButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getDeleteUserCancelButton()
  {
    return deleteUserCancelButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CheckBox getDeleteUserNotifCheck()
  {
    return deleteUserNotifCheck;
  }

}
