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

import com.vaadin.data.Item;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DefaultFieldFactory;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormFieldFactory;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;

/**
 * This {@link FormFieldFactory} is used to build a user form
 * 
 * @author Jeremy Casery
 * @author Guillaume Lamirand
 */
public class UserFieldFactory extends DefaultFieldFactory
{
  /**
   * Define the field name for user's login
   */
  public static final String  LOGIN_FIELD         = "login";
  /**
   * Define the field name for user's firstname
   */
  public static final String  FIRSTNAME_FIELD     = "firstName";
  /**
   * Define the field name for user's name
   */
  public static final String  LASTNAME_FIELD      = "name";
  /**
   * Define the field name for user's email
   */
  public static final String  EMAIL_FIELD         = "email";
  /**
   * Define the field name for user's language
   */
  public static final String  LANGUAGE_FIELD      = "language";
  /**
   * Define the field name for user's pwd
   */
  public static final String  PASSWORD_FIELD      = "password";
  /**
   * Serial version id for serialization
   */
  private static final long   serialVersionUID    = -1037845417907888176L;
  /**
   * Default representation for <code>null</code> value.
   */
  private static final String NULL_REPRESENTATION = "";
  /**
   * Contains the Languages
   */
  private ComboBox            languages;
  private TextField           login;
  private TextField           lastName;
  private TextField           firstName;
  private TextField           email;
  private PasswordField       password;

  /**
   * {@inheritDoc}
   */
  @Override
  public Field createField(final Item item, final Object propertyId, final Component uiContext)
  {
    Field returnField = null;
    if (LOGIN_FIELD.equals(propertyId))
    {
      login = new TextField();
      login.setRequired(true);
      login.setNullRepresentation(NULL_REPRESENTATION);
      login.setNullSettingAllowed(true);
      login.setWidth(NovaForge.FORM_FIELD_WIDTH);
      returnField = login;
    }
    else if (LASTNAME_FIELD.equals(propertyId))
    {
      lastName = new TextField();
      lastName.setRequired(true);
      lastName.setNullRepresentation(NULL_REPRESENTATION);
      lastName.setWidth(NovaForge.FORM_FIELD_WIDTH);
      returnField = lastName;
    }
    else if (FIRSTNAME_FIELD.equals(propertyId))
    {
      firstName = new TextField();
      firstName.setRequired(true);
      firstName.setNullRepresentation(NULL_REPRESENTATION);
      firstName.setWidth(NovaForge.FORM_FIELD_WIDTH);
      returnField = firstName;
    }
    else if (EMAIL_FIELD.equals(propertyId))
    {
      email = new TextField();
      email.setRequired(true);
      email.setNullRepresentation(NULL_REPRESENTATION);
      email.setNullSettingAllowed(true);
      email.setWidth(NovaForge.FORM_FIELD_EMAIL_WIDTH);
      returnField = email;
    }
    else if (LANGUAGE_FIELD.equals(propertyId))
    {
      languages = new ComboBox();
      languages.setRequired(true);
      languages.setNullSelectionAllowed(false);
      languages.setWidth(NovaForge.FORM_FIELD_WIDTH);
      returnField = languages;
    }
    else if (PASSWORD_FIELD.equals(propertyId))
    {
      password = new PasswordField();
      password.setRequired(true);
      password.setNullRepresentation(NULL_REPRESENTATION);
      password.setWidth(NovaForge.FORM_FIELD_WIDTH);
      returnField = password;
    }
    return returnField;
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
   * Return the filed used to display user login
   * 
   * @return the {@link TextField} used for user login
   */
  public TextField getLogin()
  {
    return login;
  }

  /**
   * Return the filed used to display user lastname
   * 
   * @return the {@link TextField} used for user lastname
   */
  public TextField getLastName()
  {
    return lastName;
  }

  /**
   * Return the filed used to display user firstname
   * 
   * @return the {@link TextField} used for user firstname
   */
  public TextField getFirstName()
  {
    return firstName;
  }

  /**
   * Return the filed used to display user email
   * 
   * @return the {@link TextField} used for user email
   */
  public TextField getEmail()
  {
    return email;
  }

  /**
   * Return the filed used to display user password
   * 
   * @return the {@link PasswordField} used for user password
   */
  public PasswordField getPassword()
  {
    return password;
  }
}
