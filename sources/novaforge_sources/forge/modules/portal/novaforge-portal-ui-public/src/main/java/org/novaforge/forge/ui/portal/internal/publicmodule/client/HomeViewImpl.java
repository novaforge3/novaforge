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
package org.novaforge.forge.ui.portal.internal.publicmodule.client;

import java.util.Locale;

import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.data.validator.LoginValidator;
import org.novaforge.forge.ui.portal.data.validator.NameValidator;
import org.novaforge.forge.ui.portal.data.validator.PasswordValidator;
import org.novaforge.forge.ui.portal.internal.publicmodule.client.components.CustomLoginForm;
import org.novaforge.forge.ui.portal.internal.publicmodule.client.components.UserRegisterForm;
import org.novaforge.forge.ui.portal.internal.publicmodule.module.PublicModule;

import com.vaadin.data.validator.EmailValidator;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;

/**
 * This view describes the portal home
 *
 * @author Guillaume Lamirand
 * @author Jeremy Casery
 */
public class HomeViewImpl extends HorizontalLayout implements HomeView
{
  /**
   * Serial version id used for serialization
   */
  private static final long serialVersionUID = -4574859677488062848L;
  /**
   * Represents the login form element
   */
  private CustomLoginForm   loginForm;
  /**
   * Represents the lost account button
   */
  private Button            lostAccountButton;
  /**
   * Represents the title icon on the left side
   */
  private Embedded          titleIcon;
  /**
   * Represents the middle separator
   */
  private VerticalLayout    separator;
  /**
   * Represents the right side layout
   */
  private VerticalLayout    registerLayout;

  private Window            confirmRegistrationWindow;
  private Label             confirmRegistrationLabelInfo;
  private Label             confirmRegistrationLabelLogin;
  private Label             confirmRegistrationLabelEmail;
  private Button            confirmRegistrationButton;

  /**
   * Represents the register form
   */
  private UserRegisterForm  registerForm;
  private Panel             registerPanel;
  private Label             novaforgeDescriptionLabel;
  private Panel             loginPanel;

  /**
   * Default constructor
   */
  public HomeViewImpl()
  {
    // Init left panel :: Login layout
    initLoginLayout();

    // Init middle separator
    initSeparator();

    // Init right panel :: register layout
    initRegisterLayout();

    // By default, add separator and register panel
    enableCreateUser(true);

    // Init home view
    setSizeFull();
    setStyleName(NovaForge.HOME);

    initConfirmRegistrationWindow();
  }

  /**
   * Initialize Login Layout
   */
  private void initLoginLayout()
  {
    // Layout
    final VerticalLayout leftLayout = new VerticalLayout();
    leftLayout.setMargin(true);
    leftLayout.setSpacing(true);

    // Icon and title
    final VerticalLayout titleLayout = new VerticalLayout();
    titleLayout.setMargin(true);
    titleLayout.setSpacing(true);
    titleLayout.setWidth(600, Unit.PIXELS);
    titleIcon = new Embedded();
    titleIcon.setWidth(150, Unit.PIXELS);
    titleIcon.setHeight(150, Unit.PIXELS);
    titleLayout.addComponent(titleIcon);
    titleLayout.setComponentAlignment(titleIcon, Alignment.MIDDLE_CENTER);

    // Description
    novaforgeDescriptionLabel = new Label();
    titleLayout.addComponent(novaforgeDescriptionLabel);
    titleLayout.setComponentAlignment(novaforgeDescriptionLabel, Alignment.MIDDLE_CENTER);

    // Login panel and form
    final VerticalLayout loginFormLayout = new VerticalLayout();
    loginFormLayout.setSizeFull();
    loginFormLayout.setMargin(new MarginInfo(false, false, true, false));
    loginPanel = new Panel();
    loginPanel.setStyleName(NovaForge.HOME);
    loginPanel.setWidth(330, Unit.PIXELS);

    // Login Form
    loginForm = new CustomLoginForm();

    // Lost account button
    lostAccountButton = new Button();
    lostAccountButton.setStyleName(NovaForge.HOME);
    lostAccountButton.addStyleName(BaseTheme.BUTTON_LINK);

    // Login form layout
    loginFormLayout.addComponent(loginForm);
    loginFormLayout.setComponentAlignment(loginForm, Alignment.MIDDLE_CENTER);
    loginFormLayout.addComponent(lostAccountButton);
    loginFormLayout.setComponentAlignment(lostAccountButton, Alignment.MIDDLE_CENTER);
    loginPanel.setContent(loginFormLayout);

    // Add Components
    leftLayout.addComponent(titleLayout);
    leftLayout.addComponent(loginPanel);
    addComponent(leftLayout);

    // Set Alignment
    leftLayout.setComponentAlignment(titleLayout, Alignment.TOP_CENTER);
    leftLayout.setComponentAlignment(loginPanel, Alignment.BOTTOM_CENTER);
    setComponentAlignment(leftLayout, Alignment.MIDDLE_CENTER);
    setExpandRatio(leftLayout, 1f);
  }

  /**
   * Initialize Separator Layout
   */
  private void initSeparator()
  {
    separator = new VerticalLayout();
    separator.setWidth(2, Unit.PIXELS);
    separator.setHeight(80, Unit.PERCENTAGE);
    separator.setStyleName(Reindeer.LAYOUT_BLACK);
  }

  /**
   * Initialize Register Layout
   */
  private void initRegisterLayout()
  {
    // Register layout
    registerLayout = new VerticalLayout();
    registerLayout.setSpacing(true);
    registerLayout.setMargin(true);

    // Register panel
    registerPanel = new Panel();
    registerPanel.setWidth(450, Unit.PIXELS);
    registerPanel.setStyleName(NovaForge.HOME);

    // Create the Form
    registerForm = new UserRegisterForm();
    registerForm.setMargin(true);

    // Add components
    registerPanel.setContent(registerForm);
    registerLayout.addComponent(registerPanel);
    registerLayout.setComponentAlignment(registerPanel, Alignment.MIDDLE_CENTER);

    registerPanel.setSizeUndefined();
  }

  private void initConfirmRegistrationWindow()
  {
    confirmRegistrationWindow = new Window();
    confirmRegistrationWindow.setClosable(false);
    confirmRegistrationWindow.setResizable(false);
    confirmRegistrationWindow.setModal(true);
    confirmRegistrationWindow.setWidth(350, Unit.PIXELS);
    final VerticalLayout layout = new VerticalLayout();
    layout.setWidth(100, Unit.PERCENTAGE);
    layout.setMargin(true);
    layout.setSpacing(true);

    confirmRegistrationLabelInfo = new Label();
    confirmRegistrationLabelInfo.setContentMode(ContentMode.HTML);
    layout.addComponent(confirmRegistrationLabelInfo);
    layout.setComponentAlignment(confirmRegistrationLabelInfo, Alignment.MIDDLE_CENTER);

    confirmRegistrationLabelLogin = new Label();
    confirmRegistrationLabelLogin.setContentMode(ContentMode.HTML);
    layout.addComponent(confirmRegistrationLabelLogin);
    layout.setComponentAlignment(confirmRegistrationLabelLogin, Alignment.MIDDLE_CENTER);

    confirmRegistrationLabelEmail = new Label();
    confirmRegistrationLabelEmail.setContentMode(ContentMode.HTML);
    layout.addComponent(confirmRegistrationLabelEmail);
    layout.setComponentAlignment(confirmRegistrationLabelEmail, Alignment.MIDDLE_CENTER);

    confirmRegistrationButton = new Button();
    confirmRegistrationButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    confirmRegistrationButton.setIcon(new ThemeResource(NovaForgeResources.ICON_GO_IN));
    layout.addComponent(confirmRegistrationButton);
    layout.setComponentAlignment(confirmRegistrationButton, Alignment.MIDDLE_CENTER);
    confirmRegistrationWindow.setContent(layout);
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
  public Button getLostAccountButton()
  {
    return lostAccountButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public CustomLoginForm getLoginForm()
  {
    return loginForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserRegisterForm getRegisterForm()
  {
    return registerForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Embedded getTitleIcon()
  {
    return titleIcon;
  }

  @Override
  public void showConfirmRegistrationWindow(final String login, final Locale pLocale)
  {
    if (confirmRegistrationWindow.getParent() == null)
    {
      UI.getCurrent().addWindow(confirmRegistrationWindow);
    }

    confirmRegistrationWindow.setCaption(
        PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_CONFIRM_WINDOW_TITLE));
    confirmRegistrationButton.setCaption(
        PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_CONFIRM_WINDOW_BUTTON));
    confirmRegistrationLabelEmail.setValue(PublicModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_REGISTER_CONFIRM_WINDOW_LABEL_EMAIL));
    confirmRegistrationLabelInfo.setValue(PublicModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_REGISTER_CONFIRM_WINDOW_LABEL_INFO));
    confirmRegistrationLabelLogin.setValue(PublicModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_REGISTER_CONFIRM_WINDOW_LABEL_LOGIN, login));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enableCreateUser(final boolean isEnable)
  {
    registerLayout.setVisible(isEnable);
    separator.setVisible(isEnable);
    if (!isEnable)
    {
      removeComponent(separator);
      removeComponent(registerLayout);
    }
    else
    {
      if (separator.getParent() == null)
      {
        addComponent(separator);
        setComponentAlignment(separator, Alignment.MIDDLE_CENTER);
        setExpandRatio(separator, 0.1f);
      }
      if (registerLayout.getParent() == null)
      {
        addComponent(registerLayout);
        setComponentAlignment(registerLayout, Alignment.MIDDLE_CENTER);
        setExpandRatio(registerLayout, 1f);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void enableUpdateUser(final boolean isEnable)
  {
    lostAccountButton.setVisible(isEnable);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Window getConfirmRegistrationWindow()
  {
    return confirmRegistrationWindow;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getConfirmRegistrationButton()
  {
    return confirmRegistrationButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void refreshLocale(final Locale pLocale)
  {

    String messageErrorValidator = PublicModule.getPortalMessages().getMessage(pLocale,
        Messages.PUBLIC_REGISTER_FORM_LASTNAME_TOOLTIP, NameValidator.MIN_LENGTH, NameValidator.MAX_LENGTH);

    loginPanel.setCaption(PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_LOGIN_LABEL));
    loginForm.refreshLocale(pLocale);
    lostAccountButton
        .setCaption(PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_LOGIN_LOSTACCOUNT));

    novaforgeDescriptionLabel.setValue(
        PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_NOVAFORGE_DESCRIPTION));
    novaforgeDescriptionLabel.setStyleName(NovaForge.HOME_DESCRIPTION);
    novaforgeDescriptionLabel.setWidth(500, Unit.PIXELS);

    registerPanel
        .setCaption(PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_LABEL));

    if (registerForm.getLogin() != null)
    {
      registerForm.getLogin().setCaption(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LOGIN));
      registerForm.getLogin().setDescription(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LOGIN));
      registerForm.getLogin().removeAllValidators();
      final LoginValidator loginValidator = new LoginValidator(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LOGIN_TOOLTIP));
      registerForm.getLogin().addValidator(loginValidator);
      registerForm.getLogin()
          .setRequiredError(PublicModule.getPortalMessages().getMessage(pLocale,
              Messages.PUBLIC_REGISTER_FORM_LOGIN_TOOLTIP, loginValidator.getMinLength(),
              loginValidator.getMaxLength()));
    }
    if (registerForm.getLastName() != null)
    {
      registerForm.getLastName().setCaption(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LASTNAME));
      registerForm.getLastName().setDescription(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LASTNAME));
      registerForm.getLastName().removeAllValidators();
      final NameValidator nameValidator = new NameValidator(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_LASTNAME_TOOLTIP,
              NameValidator.MIN_LENGTH, NameValidator.MAX_LENGTH));
      registerForm.getLastName().addValidator(nameValidator);
      registerForm.getLastName()
          .setRequiredError(PublicModule.getPortalMessages().getMessage(pLocale,
              Messages.PUBLIC_REGISTER_FORM_LASTNAME_TOOLTIP, NameValidator.MIN_LENGTH,
              NameValidator.MAX_LENGTH));
    }

    if (registerForm.getFirstName() != null)
    {
      registerForm.getFirstName().setCaption(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_FIRSTNAME));
      registerForm.getFirstName().setDescription(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_FIRSTNAME));
      registerForm.getFirstName().removeAllValidators();
      final NameValidator nameValidator = new NameValidator(PublicModule.getPortalMessages().getMessage(
          pLocale, Messages.PUBLIC_REGISTER_FORM_FIRSTNAME_TOOLTIP, NameValidator.MIN_LENGTH,
          NameValidator.MAX_LENGTH));
      registerForm.getFirstName().addValidator(nameValidator);
      registerForm.getFirstName()
          .setRequiredError(PublicModule.getPortalMessages().getMessage(pLocale,
              Messages.PUBLIC_REGISTER_FORM_FIRSTNAME_TOOLTIP, NameValidator.MIN_LENGTH,
              NameValidator.MAX_LENGTH));
    }

    if (registerForm.getEmail() != null)
    {
      registerForm.getEmail().setCaption(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_EMAIL));
      registerForm.getEmail().setDescription(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_EMAIL));
      registerForm.getEmail().setRequiredError(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_EMAIL_TOOLTIP));
      registerForm.getEmail().removeAllValidators();
      registerForm.getEmail().addValidator(new EmailValidator(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_EMAIL_TOOLTIP)));
    }

    if (registerForm.getLanguages() != null)
    {
      registerForm.getLanguages().setInputPrompt(PublicModule.getPortalMessages().getMessage(pLocale,
          Messages.PUBLIC_REGISTER_FORM_LANGUAGE_TOOLTIP));
      registerForm.getLanguages().setRequiredError(PublicModule.getPortalMessages().getMessage(pLocale,
          Messages.PUBLIC_REGISTER_FORM_LANGUAGE_TOOLTIP));
    }

    if (registerForm.getPassword() != null)
    {
      registerForm.getPassword().setCaption(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_PASSWORD));
      registerForm.getPassword().setDescription(
          PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_FORM_PASSWORD));
      registerForm.getPassword().setRequiredError(PublicModule.getPortalMessages().getMessage(pLocale,
          Messages.PUBLIC_REGISTER_FORM_PASSWORD_TOOLTIP));
      registerForm.getPassword().removeAllValidators();
      registerForm.getPassword()
          .addValidator(new PasswordValidator(
              PublicModule.getPortalMessages().getMessage(pLocale,
                  Messages.PUBLIC_REGISTER_FORM_PASSWORD_TOOLTIP),
          PublicModule.getForgeConfigurationService().getPasswordValidationRegex()));
    }

    registerForm.getRegisterButton()
        .setCaption(PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_REGISTER_LABEL));
  }

}
