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
package org.novaforge.forge.ui.user.management.internal.client.admin;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.client.component.DeleteConfirmWindow;
import org.novaforge.forge.ui.portal.data.container.CommonItemProperty;
import org.novaforge.forge.ui.portal.data.container.UserItemProperty;
import org.novaforge.forge.ui.portal.data.container.UserProfileItemProperty;
import org.novaforge.forge.ui.portal.data.validator.LoginValidator;
import org.novaforge.forge.ui.portal.data.validator.NameValidator;
import org.novaforge.forge.ui.portal.data.validator.PasswordValidator;
import org.novaforge.forge.ui.user.management.internal.client.admin.components.UserFieldFactory;
import org.novaforge.forge.ui.user.management.internal.module.AdminModule;
import org.vaadin.haijian.ExcelExporter;

import java.util.Collection;
import java.util.Locale;

/**
 * The UserManagementView implementation
 * 
 * @author Jeremy Casery
 */
public class UserManagementViewImpl extends VerticalLayout implements UserManagementView
{
  /**
   * Serial version id used for serialization
   */
  private static final long      serialVersionUID       = -2220073303146264084L;
  /**
   * Filter constant for field
   */
  private static final String    FILTER_FIELD           = "filter";
  /**
   * The admin actions Layout
   */
  private final HorizontalLayout adminActionsLayout     = new HorizontalLayout();
  /**
   * {@link Form} containing filter and its table
   */
  private final Form             usersForm              = new Form();
  /**
   * The users filters TextField
   */
  private final TextField        filterTextField        = new TextField();
  /**
   * The users table
   */
  private final Table            usersTable             = new Table();
  /**
   * The excel exporter
   */
  private final ExcelExporter    excelExporter          = new ExcelExporter();
  /**
   * The create user button
   */
  private final Button           createUserWindowButton = new Button();
  /**
   * The create user window
   */
  private final Window           createUserWindow       = new Window();
  /**
   * The create user confirm button
   */
  private final Button           createUserButton       = new Button();
  /**
   * Create user form
   */
  private final Form             createUserForm         = new Form();
  /**
   * The delete user window
   */
  private DeleteConfirmWindow deleteUserWindow;
  /**
   * The user field factory
   */
  private UserFieldFactory    userFieldFactory;

  /**
   * Default constructor
   */
  public UserManagementViewImpl()
  {
    // Init view
    setMargin(new MarginInfo(true));

    // Init contents
    final Component adminActions = initAdminActions();
    final Component filter       = initFilter();
    final Component content      = initTable();
    final Component export       = initExport();
    addComponent(adminActions);
    addComponent(filter);
    addComponent(content);
    addComponent(export);
    initPopups();
  }

  /**
   * initialize admin actions
   */
  private Component initAdminActions()
  {
    adminActionsLayout.setWidth(100, Unit.PERCENTAGE);
    adminActionsLayout.setMargin(new MarginInfo(false, false, true, false));
    createUserButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    createUserButton.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_ADD));
    adminActionsLayout.addComponent(createUserButton);
    adminActionsLayout.setComponentAlignment(createUserButton, Alignment.TOP_LEFT);
    return adminActionsLayout;
  }

  /**
   * Initialize the filters Layout
   */
  private Component initFilter()
  {
    usersForm.setImmediate(true);
    usersForm.setInvalidCommitted(false);
    filterTextField.setImmediate(true);
    usersForm.addField(FILTER_FIELD, filterTextField);
    return usersForm;
  }

  /**
   * Initialize the table Layout
   */
  private Component initTable()
  {
    usersTable.setSelectable(true);
    usersTable.setPageLength(10);
    usersTable.setWidth(100, Unit.PERCENTAGE);
    usersTable.setStyleName(Reindeer.TABLE_STRONG);

    addLayoutClickListener(new LayoutClickListener()
    {

      /**
       *
       */
      private static final long serialVersionUID = -6038779390241752234L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void layoutClick(final LayoutClickEvent event)
      {

        // Get the child component which was clicked
        final Component child = event.getChildComponent();

        if (child == null || !child.equals(usersTable))
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

  /**
   * Initialize the export Layout
   */
  private Component initExport()
  {
    final HorizontalLayout layout = new HorizontalLayout();
    excelExporter.setStyleName(NovaForge.BUTTON_LINK);
    layout.addComponent(excelExporter);
    return layout;
  }

  /**
   * Initialize the popups
   */
  private void initPopups()
  {
    initDeleteUserWindow();
    initCreateUserWindow();
  }

  /**
   * Initialize the delete user window
   */
  private void initDeleteUserWindow()
  {
    deleteUserWindow = new DeleteConfirmWindow(Messages.USERMANAGEMENT_DELETE_USER_CONFIRM);
  }

  /**
   * Initialize create user window
   */
  private void initCreateUserWindow()
  {
    // Init window
    createUserWindow.setModal(true);
    createUserWindow.setResizable(false);
    createUserWindow.setWidth(425, Unit.PIXELS);
    createUserWindow.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_ADD));
    final HorizontalLayout createUserWindowLayout = new HorizontalLayout();
    createUserWindowLayout.setMargin(true);
    createUserWindowLayout.setSpacing(true);
    createUserWindowLayout.setWidth(100, Unit.PERCENTAGE);
    createUserWindow.setContent(createUserWindowLayout);

    // Init user form
    userFieldFactory = new UserFieldFactory();
    createUserForm.setFormFieldFactory(userFieldFactory);
    final VerticalLayout createUserFormButtonLayout = new VerticalLayout();
    createUserFormButtonLayout.setWidth(100, Unit.PERCENTAGE);
    createUserWindowButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    createUserWindowButton.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_ADD));
    createUserForm.setFooter(createUserFormButtonLayout);
    createUserFormButtonLayout.addComponent(createUserWindowButton);
    createUserFormButtonLayout.setComponentAlignment(createUserWindowButton, Alignment.MIDDLE_CENTER);
    createUserWindowLayout.addComponent(createUserForm);
    createUserWindowLayout.setComponentAlignment(createUserForm, Alignment.MIDDLE_CENTER);

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
    usersForm.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
                                                                    Messages.USERMANAGEMENT_ADMIN_TITLE_USERS));
    createUserButton.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.USERMANAGEMENT_CREATE_USER));
    createUserWindowButton.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
                                                                                 Messages.USERMANAGEMENT_CREATE_USER));
    filterTextField.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
                                                                          Messages.USERMANAGEMENT_FILTER_FIELDS));
    // Define columns header text
    usersTable.setColumnHeader(UserItemProperty.LOGIN.getPropertyId(),
                               AdminModule.getPortalMessages().getMessage(pLocale,
                                                                          Messages.USERMANAGEMENT_FIELD_LOGIN));
    usersTable.setColumnHeader(UserItemProperty.FIRSTNAME.getPropertyId(),
                               AdminModule.getPortalMessages().getMessage(pLocale,
                                                                          Messages.USERMANAGEMENT_FIELD_FIRSTNAME));
    usersTable.setColumnHeader(UserItemProperty.LASTNAME.getPropertyId(),
                               AdminModule.getPortalMessages().getMessage(pLocale,
                                                                          Messages.USERMANAGEMENT_FIELD_LASTNAME));
    usersTable.setColumnHeader(UserItemProperty.EMAIL.getPropertyId(),
                               AdminModule.getPortalMessages().getMessage(pLocale,
                                                                          Messages.USERMANAGEMENT_FIELD_EMAIL));
    usersTable.setColumnHeader(CommonItemProperty.ACTIONS.getPropertyId(),
                               AdminModule.getPortalMessages().getMessage(pLocale,
                                                                          Messages.USERMANAGEMENT_FIELD_ACTIONS));
    excelExporter.setCaption(AdminModule.getPortalMessages().getMessage(pLocale, Messages.ACTIONS_EXPORT_EXCEL));
    createUserWindow.setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
                                                                           Messages.USERMANAGEMENT_CREATE_USER));
    deleteUserWindow.refreshLocale(pLocale);
    if (userFieldFactory.getLogin() != null)
    {
      userFieldFactory.getLogin().setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
                                                                                        Messages.PUBLIC_REGISTER_FORM_LOGIN));

      LoginValidator loginValidator = new LoginValidator("");
      String message = AdminModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LOGIN_TOOLTIP,
                                                                  loginValidator.getMinLength(),
                                                                  loginValidator.getMaxLength());
      loginValidator.setErrorMessage(message);
      userFieldFactory.getLogin().setRequiredError(message);

      userFieldFactory.getLogin().removeAllValidators();
      userFieldFactory.getLogin().addValidator(loginValidator);
    }
    if (userFieldFactory.getLastName() != null)
    {
      userFieldFactory.getLastName().setCaption(AdminModule.getPortalMessages().getMessage(pLocale,
                                                                                           Messages.PUBLIC_REGISTER_FORM_LASTNAME));
      userFieldFactory.getLastName().setRequiredError(AdminModule.getPortalMessages().getMessage(pLocale,
                                                                                                 Messages.PUBLIC_REGISTER_FORM_LASTNAME_TOOLTIP,
                                                                                                 NameValidator.MIN_LENGTH,
                                                                                                 NameValidator.MAX_LENGTH));
      userFieldFactory.getLastName().removeAllValidators();
      userFieldFactory.getLastName().addValidator(new NameValidator(AdminModule.getPortalMessages().getMessage(pLocale,
                                                                                                               Messages.PUBLIC_REGISTER_FORM_LASTNAME_TOOLTIP,
                                                                                                               NameValidator.MIN_LENGTH,
              NameValidator.MAX_LENGTH)));
    }

    if (userFieldFactory.getFirstName() != null)
    {
      userFieldFactory.getFirstName().setCaption(
          AdminModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_FIRSTNAME));
      userFieldFactory.getFirstName().setRequiredError(
          AdminModule.getPortalMessages().getMessage(pLocale,
              Messages.PUBLIC_REGISTER_FORM_FIRSTNAME_TOOLTIP, NameValidator.MIN_LENGTH,
              NameValidator.MAX_LENGTH));
      userFieldFactory.getFirstName().removeAllValidators();
      userFieldFactory.getFirstName().addValidator(
          new NameValidator(AdminModule.getPortalMessages().getMessage(pLocale,
              Messages.PUBLIC_REGISTER_FORM_FIRSTNAME_TOOLTIP, NameValidator.MIN_LENGTH,
              NameValidator.MAX_LENGTH)));
    }

    if (userFieldFactory.getEmail() != null)
    {
      userFieldFactory.getEmail().setCaption(
          AdminModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_EMAIL));
      userFieldFactory.getEmail().setRequiredError(
          AdminModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_EMAIL_TOOLTIP));
      userFieldFactory.getEmail().removeAllValidators();
      userFieldFactory.getEmail().addValidator(
          new EmailValidator(AdminModule.getPortalMessages().getMessage(pLocale,
              Messages.PUBLIC_REGISTER_FORM_EMAIL_TOOLTIP)));
    }

    if (userFieldFactory.getLanguages() != null)
    {
      userFieldFactory.getLanguages()
          .setCaption(
              AdminModule.getPortalMessages().getMessage(pLocale,
                  Messages.PUBLIC_REGISTER_FORM_LANGUAGE_TOOLTIP));
      userFieldFactory.getLanguages()
          .setInputPrompt(
              AdminModule.getPortalMessages().getMessage(pLocale,
                  Messages.PUBLIC_REGISTER_FORM_LANGUAGE_TOOLTIP));
      userFieldFactory.getLanguages().setCaption(
          AdminModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LANGUAGE));
      userFieldFactory.getLanguages()
          .setRequiredError(
              AdminModule.getPortalMessages().getMessage(pLocale,
                  Messages.PUBLIC_REGISTER_FORM_LANGUAGE_TOOLTIP));
    }

    if (userFieldFactory.getPassword() != null)
    {
      userFieldFactory.getPassword().setCaption(
          AdminModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_PASSWORD));
      userFieldFactory.getPassword()
          .setRequiredError(
              AdminModule.getPortalMessages().getMessage(pLocale,
                  Messages.PUBLIC_REGISTER_FORM_PASSWORD_TOOLTIP));
      userFieldFactory.getPassword().removeAllValidators();
      userFieldFactory.getPassword().addValidator(
          new PasswordValidator(AdminModule.getPortalMessages().getMessage(pLocale,
              Messages.PUBLIC_REGISTER_FORM_PASSWORD_TOOLTIP), AdminModule.getForgeConfigurationService()
              .getPasswordValidationRegex()));
    }
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
  public TextField getFilterTextField()
  {
    return filterTextField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUserTableVisibleColumns()
  {
    // Define visibles columns
    usersTable.setVisibleColumns(UserProfileItemProperty.LOGIN.getPropertyId(),
        UserProfileItemProperty.FIRSTNAME.getPropertyId(), UserProfileItemProperty.LASTNAME.getPropertyId(),
        UserProfileItemProperty.EMAIL.getPropertyId(), CommonItemProperty.ACTIONS.getPropertyId());
    // Define special column width
    usersTable.setColumnWidth(CommonItemProperty.ACTIONS.getPropertyId(), 90);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeleteConfirmWindow getDeleteUserWindow()
  {
    return deleteUserWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCreateUserButton()
  {
    return createUserButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getCreateUserWindow()
  {
    return createUserWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getCreateUserWindowButton()
  {
    return createUserWindowButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Form getCreateUserForm()
  {
    return createUserForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserFieldFactory getUserFieldFactory()
  {
    return userFieldFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ExcelExporter getExcelExporter()
  {
    return excelExporter;
  }

}
