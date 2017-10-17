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

import com.ejt.vaadin.loginform.LoginForm;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.internal.publicmodule.module.PublicModule;

import java.util.Locale;

/**
 * @author Guillaume Lamirand
 */
public class CustomLoginForm extends LoginForm
{

  /**
   * @param pUserNameField
   * @param pPasswordField
   * @param pLoginButton
   *
   * @return
   */
  @Override
  protected Component createContent(final TextField pUserNameField, final PasswordField pPasswordField,
                                    Button pLoginButton)
  {
    setImmediate(true);
    VerticalLayout layout = new VerticalLayout();

    layout.setMargin(new MarginInfo(true, false, false, false));
    layout.setSpacing(true);

    layout.addComponent(pUserNameField);
    layout.addComponent(pPasswordField);
    layout.addComponent(pLoginButton);
    layout.setComponentAlignment(pUserNameField, Alignment.TOP_CENTER);
    layout.setComponentAlignment(pPasswordField, Alignment.MIDDLE_CENTER);
    layout.setComponentAlignment(pLoginButton, Alignment.BOTTOM_CENTER);
    return layout;
  }


  @Override
  protected TextField createUserNameField()
  {
    TextField userNameField = new TextField();
    userNameField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    userNameField.setIcon(new ThemeResource(NovaForgeResources.TEXTFIELD_ICON_LOGIN));
    userNameField.focus();
    return userNameField;
  }

  @Override
  protected PasswordField createPasswordField()
  {
    final PasswordField passwordField = new PasswordField();
    passwordField.setWidth(NovaForge.FORM_FIELD_WIDTH);
    passwordField.setIcon(new ThemeResource(NovaForgeResources.TEXTFIELD_ICON_PWD));
    return passwordField;
  }

  @Override
  protected Button createLoginButton()
  {
    final Button loginButton = new Button();
    loginButton.setIcon(new ThemeResource(NovaForgeResources.ICON_LOGIN));
    loginButton.setWidth(NovaForge.FORM_FIELD_WIDTH);
    loginButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    return loginButton;
  }

  @Override
  public void attach()
  {
    super.attach();
    refreshLocale(getLocale());
  }

  public void refreshLocale(final Locale pLocale)
  {
    getUserNameField().setCaption(PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_USERNAME));
    getPasswordField().setCaption(PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_PASSWORD));
    getLoginButton().setCaption(PublicModule.getPortalMessages().getMessage(pLocale, Messages.PUBLIC_LOGIN_LABEL));

  }

  protected Button getLoginButton()
  {
    return (Button) this.getState().loginButtonConnector;
  }

  protected TextField getUserNameField()
  {
    return (TextField) this.getState().userNameFieldConnector;
  }

  protected PasswordField getPasswordField()
  {
    return (PasswordField) this.getState().passwordFieldConnector;
  }

}
