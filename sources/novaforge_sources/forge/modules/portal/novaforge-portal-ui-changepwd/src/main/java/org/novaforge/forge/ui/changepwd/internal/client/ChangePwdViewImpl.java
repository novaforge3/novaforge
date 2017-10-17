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
package org.novaforge.forge.ui.changepwd.internal.client;

import com.vaadin.server.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.BaseTheme;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.changepwd.internal.module.ChangePwdModule;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForge;
import org.novaforge.forge.ui.commons.vaadin.theme.NovaForgeResources;
import org.novaforge.forge.ui.portal.data.validator.DualPasswordValidator;
import org.novaforge.forge.ui.portal.data.validator.LoginValidator;
import org.novaforge.forge.ui.portal.data.validator.NewPasswordValidator;
import org.novaforge.forge.ui.portal.data.validator.PasswordValidator;

/**
 * This view describes the change pwd view
 * 
 * @author Guillaume Lamirand
 */
public class ChangePwdViewImpl extends VerticalLayout implements ChangePwdView
{

  /**
   * Serial version id
   */
  private static final long       serialVersionUID            = 38640145488243489L;

  /**
   * Represents the {@link Button} used on apply action
   */
  private final Button            applyButton;

  private final Button            homeLink;

  private final PasswordField     newAgainPasswd;

  private final PasswordField     newPasswd;

  private final PasswordField     currentPasswd;

  private final TextField         login                       = new TextField();

  /** The password security rule label */
  private final Label             passwdSecurityRuleLabel     = new Label();

  /** The new password matching security rule label */
  private final CssLayout         newPasswdRulesIcon          = new CssLayout();

  /** The new passwords matching label */
  private final CssLayout         newPasswdsAgainMatchingIcon = new CssLayout();

  /** The password regex validator */
  private final PasswordValidator passwordRegexValidator      = new PasswordValidator();

  /**
   * Contains true if the current view is opened in a sub-window
   */
  private final boolean           inSub;

  /**
   * Default constructor
   *
   * @param inSub
   *          true if this view is shown in a window
   */
  public ChangePwdViewImpl(final boolean inSub)
  {
    this.inSub = inSub;
    // Init
    setMargin(true);
    setSpacing(true);

    homeLink = new Button();
    homeLink.setStyleName(BaseTheme.BUTTON_LINK);
    addComponent(homeLink);

    // Add fields
    login.setRequired(true);
    currentPasswd = new PasswordField();
    currentPasswd.setRequired(true);
    newPasswd = new PasswordField();
    newPasswd.setRequired(true);
    newAgainPasswd = new PasswordField();
    newAgainPasswd.setRequired(true);

    newPasswdRulesIcon.setStyleName(NovaForge.TEXTFIELD_VALIDATION);
    newPasswdRulesIcon.addComponent(getEmptyIcon());
    newPasswdsAgainMatchingIcon.setStyleName(NovaForge.TEXTFIELD_VALIDATION);
    newPasswdsAgainMatchingIcon.addComponent(getEmptyIcon());

    final HorizontalLayout newPasswdLayout = new HorizontalLayout();
    newPasswdLayout.setSpacing(true);
    newPasswdLayout.addComponent(newPasswd);
    newPasswdLayout.addComponent(newPasswdRulesIcon);
    newPasswdLayout.setComponentAlignment(newPasswd, Alignment.MIDDLE_LEFT);
    newPasswdLayout.setComponentAlignment(newPasswdRulesIcon, Alignment.MIDDLE_LEFT);

    final HorizontalLayout newAgainPasswdLayout = new HorizontalLayout();
    newAgainPasswdLayout.setSpacing(true);
    newAgainPasswdLayout.addComponent(newAgainPasswd);
    newAgainPasswdLayout.addComponent(newPasswdsAgainMatchingIcon);
    newAgainPasswdLayout.setComponentAlignment(newAgainPasswd, Alignment.MIDDLE_LEFT);
    newAgainPasswdLayout.setComponentAlignment(newPasswdsAgainMatchingIcon, Alignment.MIDDLE_LEFT);

    final GridLayout passwdGrid = new GridLayout(1, 4);
    passwdGrid.addComponent(login, 0, 0);
    passwdGrid.addComponent(currentPasswd, 0, 1);
    passwdGrid.addComponent(newPasswdLayout, 0, 2);
    passwdGrid.addComponent(newAgainPasswdLayout, 0, 3);
    passwdGrid.setComponentAlignment(currentPasswd, Alignment.MIDDLE_LEFT);
    passwdGrid.setComponentAlignment(newPasswdLayout, Alignment.MIDDLE_CENTER);
    passwdGrid.setComponentAlignment(newAgainPasswdLayout, Alignment.MIDDLE_CENTER);

    // Add passwdGrid to layout
    passwdSecurityRuleLabel.setWidth(null);
    addComponent(passwdSecurityRuleLabel);
    addComponent(passwdGrid);
    setComponentAlignment(passwdGrid, Alignment.MIDDLE_CENTER);
    setComponentAlignment(passwdSecurityRuleLabel, Alignment.MIDDLE_CENTER);

    applyButton = new Button();
    applyButton.setStyleName(NovaForge.BUTTON_PRIMARY);
    applyButton.setIcon(new ThemeResource(NovaForgeResources.ICON_LOCK));
    addComponent(applyButton);
    setComponentAlignment(applyButton, Alignment.MIDDLE_CENTER);
  }

  private Embedded getEmptyIcon()
  {
    return new Embedded(null, new ThemeResource(NovaForgeResources.ICON_EMPTY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void attach()
  {
    super.attach();

    homeLink.setCaption(ChangePwdModule.getPortalMessages().getMessage(getLocale(),
        Messages.CHANGEPWD_RECOVERY_LINK));
    login.setCaption(ChangePwdModule.getPortalMessages().getMessage(getLocale(),
        Messages.CHANGEPWD_FORM_LOGIN));

    LoginValidator loginValidator = new LoginValidator("");
    String message = ChangePwdModule.getPortalMessages().getMessage(getLocale(),
        Messages.CHANGEPWD_FORM_LOGIN_TOOLTIP, loginValidator.getMinLength(), loginValidator.getMaxLength());
        loginValidator.setErrorMessage(message);
    login.setRequiredError(message);
    login.addValidator(loginValidator);

    currentPasswd.setCaption(ChangePwdModule.getPortalMessages().getMessage(getLocale(),
        Messages.CHANGEPWD_FORM_CURRENT));
    newPasswd.setCaption(ChangePwdModule.getPortalMessages().getMessage(getLocale(),
        Messages.CHANGEPWD_FORM_NEW));
    newAgainPasswd.setCaption(ChangePwdModule.getPortalMessages().getMessage(getLocale(),
        Messages.CHANGEPWD_FORM_RENEW));

    newPasswd.removeAllValidators();
    newPasswd.addValidator(new NewPasswordValidator(ChangePwdModule.getPortalMessages().getMessage(
        getLocale(), Messages.CHANGEPWD_FORM_NEW_TOOLTIP), currentPasswd));

    newPasswd.addValidator(passwordRegexValidator);
    passwordRegexValidator.setErrorMessage(ChangePwdModule.getPortalMessages().getMessage(getLocale(),
        Messages.PUBLIC_REGISTER_FORM_PASSWORD_TOOLTIP));
    newAgainPasswd.removeAllValidators();
    newAgainPasswd.addValidator(new DualPasswordValidator(ChangePwdModule.getPortalMessages().getMessage(
        getLocale(), Messages.CHANGEPWD_FORM_RENEW_ERROR), newPasswd));

    applyButton.setCaption(ChangePwdModule.getPortalMessages().getMessage(getLocale(),
        Messages.CHANGEPWD_FORM_APPLY));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detach()
  {
    super.detach();
    login.removeAllValidators();
    newPasswd.removeAllValidators();
    newAgainPasswd.removeAllValidators();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getApply()
  {
    return applyButton;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Button getHomeLink()
  {
    return homeLink;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNewPasswdValid(final boolean pIsEmpty, final boolean pIsValid)
  {
    newPasswdRulesIcon.removeAllComponents();
    if (!pIsEmpty)
    {
      if (pIsValid)
      {
        newPasswdRulesIcon.addComponent(getOKIcon());
      }
      else
      {
        newPasswdRulesIcon.addComponent(getKOIcon());
      }
    }
    else
    {
      newPasswdRulesIcon.addComponent(getEmptyIcon());
      newPasswdsAgainMatchingIcon.removeAllComponents();
      newPasswdsAgainMatchingIcon.addComponent(getEmptyIcon());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNewPasswdMatching(final boolean pOneEmpty, final boolean pIsMatching)
  {
    newPasswdsAgainMatchingIcon.removeAllComponents();
    if (!pOneEmpty)
    {
      if (pIsMatching)
      {
        newPasswdsAgainMatchingIcon.addComponent(getOKIcon());
      }
      else
      {
        newPasswdsAgainMatchingIcon.addComponent(getKOIcon());
      }
    }
    else
    {
      newPasswdsAgainMatchingIcon.addComponent(getEmptyIcon());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TextField getLogin()
  {
    return login;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PasswordField getCurrentPasswd()
  {
    return currentPasswd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PasswordField getNewPasswd()
  {
    return newPasswd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PasswordField getNewAgainPasswd()
  {
    return newAgainPasswd;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PasswordValidator getPasswordRegexValidator()
  {
    return passwordRegexValidator;
  }

  private Embedded getOKIcon()
  {
    return new Embedded(null, new ThemeResource(NovaForgeResources.ICON_VALIDATE_ROUND));
  }

  private Embedded getKOIcon()
  {
    return new Embedded(null, new ThemeResource(NovaForgeResources.ICON_ERROR));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Label getPasswdSecurityRuleLabel(){
    return  passwdSecurityRuleLabel;
  }

}
