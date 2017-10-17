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
package org.novaforge.forge.ui.portal.internal.publicmodule.client.components;

import com.vaadin.data.Item;
import com.vaadin.data.fieldgroup.FieldGroup;
import com.vaadin.data.fieldgroup.FieldGroup.CommitException;
import com.vaadin.data.fieldgroup.PropertyId;
import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;

/**
 * This layout is used to build a user form
 *
 * @author Jeremy Casery
 * @author Guillaume Lamirand
 */
public class UserRegisterForm extends VerticalLayout
{
  /**
   * Define the field name for user's firstname
   */
  public static final                       String           FIRSTNAME_FIELD     = "firstName";
  /**
   * Define the field name for user's email
   */
  public static final                       String           EMAIL_FIELD         = "email";
  /**
   * Define the field name for user's login
   */
  private static final                      String           LOGIN_FIELD         = "login";
  /**
   * Define the field name for user's name
   */
  private static final                      String           LASTNAME_FIELD      = "name";
  /**
   * Define the field name for user's language
   */
  private static final                      String           LANGUAGE_FIELD      = "language";
  /**
   * Define the field name for user's pwd
   */
  private static final                      String           PASSWORD_FIELD      = "password";
  /**
   * Serial version id for serialization
   */
  private static final                      long             serialVersionUID    = 9098820916889938106L;
  /**
   * Default representation for <code>null</code> value.
   */
  private static final                      String           NULL_REPRESENTATION = "";
  /**
   * Contains the Languages
   */
  @PropertyId(LANGUAGE_FIELD) private final ComboBox         languages           = new ComboBox();
  private final                             TextField        login               = new TextField();
  private final                             HorizontalLayout loginLayout         = new HorizontalLayout();
  @PropertyId(LASTNAME_FIELD) private final TextField        lastName            = new TextField();
  private final                             TextField        firstName           = new TextField();
  private final                             TextField        email               = new TextField();
  @PropertyId(PASSWORD_FIELD) private final PasswordField    password            = new PasswordField();
  private final                             Button           registerButton      = new Button();
  /**
   * The binder for form field
   */
  private FieldGroup userBinder;

  public UserRegisterForm()
  {
    setSpacing(true);
    login.setRequired(true);
    login.setImmediate(true);
    login.setNullRepresentation(NULL_REPRESENTATION);
    login.setWidth(NovaForge.FORM_FIELD_BIG_WIDTH);
    login.setIcon(new ThemeResource(NovaForgeResources.TEXTFIELD_ICON_LOGIN));

    lastName.setRequired(true);
    lastName.setImmediate(true);
    lastName.setNullRepresentation(NULL_REPRESENTATION);
    lastName.setWidth(NovaForge.FORM_FIELD_WIDTH);
    lastName.setIcon(new ThemeResource(NovaForgeResources.TEXTFIELD_ICON_NAME));

    firstName.setRequired(true);
    firstName.setImmediate(true);
    firstName.setNullRepresentation(NULL_REPRESENTATION);
    firstName.setWidth(NovaForge.FORM_FIELD_WIDTH);
    firstName.setIcon(new ThemeResource(NovaForgeResources.TEXTFIELD_ICON_NAME));

    email.setRequired(true);
    email.setImmediate(true);
    email.setNullRepresentation(NULL_REPRESENTATION);
    email.setNullSettingAllowed(true);
    email.setWidth(NovaForge.FORM_FIELD_BIG_WIDTH);
    email.setIcon(new ThemeResource(NovaForgeResources.TEXTFIELD_ICON_EMAIL));

    password.setRequired(true);
    password.setImmediate(true);
    password.setNullRepresentation(NULL_REPRESENTATION);
    password.setWidth(NovaForge.FORM_FIELD_WIDTH);
    password.setIcon(new ThemeResource(NovaForgeResources.TEXTFIELD_ICON_PWD));

    languages.setRequired(true);
    languages.setImmediate(true);
    languages.setTextInputAllowed(false);
    languages.setNullSelectionAllowed(false);
    languages.setWidth(NovaForge.FORM_FIELD_WIDTH);
    languages.setIcon(new ThemeResource(NovaForgeResources.TEXTFIELD_ICON_LANGUAGE));

    loginLayout.addComponent(login);

    final HorizontalLayout nameLayout = new HorizontalLayout();
    nameLayout.setSpacing(true);
    nameLayout.addComponent(firstName);
    nameLayout.addComponent(lastName);

    final HorizontalLayout pwdAndLanguagesLayout = new HorizontalLayout();
    pwdAndLanguagesLayout.setSpacing(true);
    pwdAndLanguagesLayout.addComponent(password);
    pwdAndLanguagesLayout.addComponent(languages);

    addComponent(nameLayout);
    addComponent(email);
    addComponent(pwdAndLanguagesLayout);

    registerButton.setIcon(new ThemeResource(NovaForgeResources.ICON_USER_ADD));
    registerButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    registerButton.setWidth(NovaForge.FORM_FIELD_BIG_WIDTH);

    addComponent(registerButton);

  }

  /**
   * Bind this form with given Item
   *
   * @param item
   *     the item to bind
   */
  public void bind(final Item item)
  {
    userBinder = new FieldGroup(item);
    userBinder.bindMemberFields(this);
  }

  /**
   * Commit the current form
   *
   * @throws CommitException
   */
  public void commit() throws CommitException
  {
    userBinder.commit();
  }

  /**
   * Define if login is generated (default value) or not
   * If not generated, user must fill is login on form
   *
   * @param pIsGenerated
   *     true if generated, false otherwise
   */
  public void setGeneratedLogin(final boolean pIsGenerated)
  {
    if (pIsGenerated)
    {
      userBinder.unbind(login);
      removeComponent(loginLayout);
    }
    else
    {
      addComponentAsFirst(loginLayout);
      userBinder.bind(login, LOGIN_FIELD);
    }
  }

  public void setValidationVisible(final boolean pIsVisible)
  {
    login.setValidationVisible(pIsVisible);
    firstName.setValidationVisible(pIsVisible);
    lastName.setValidationVisible(pIsVisible);
    email.setValidationVisible(pIsVisible);
    password.setValidationVisible(pIsVisible);
    languages.setValidationVisible(pIsVisible);
  }

  /**
   * Return the combobox used to display languages
   *
   * @return the {@link ComboBox} used for languages
   */
  public ComboBox getLanguages()
  {
    return languages;
  }

  /**
   * Get the login field
   *
   * @return the login {@link TextField}
   */
  public TextField getLogin()
  {
    return login;
  }

  /**
   * Get the lastname field
   *
   * @return the lastname {@link TextField}
   */
  public TextField getLastName()
  {
    return lastName;
  }

  /**
   * Get the firstname field
   *
   * @return the firstname {@link TextField}
   */
  public TextField getFirstName()
  {
    return firstName;
  }

  /**
   * Get the email field
   *
   * @return the email {@link TextField}
   */
  public TextField getEmail()
  {
    return email;
  }

  /**
   * Get the password field
   *
   * @return the password {@link PasswordField}
   */
  public PasswordField getPassword()
  {
    return password;
  }

  /**
   * Get the register button
   *
   * @return the register {@link Button}
   */
  public Button getRegisterButton()
  {
    return registerButton;
  }
}
