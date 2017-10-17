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

import com.google.common.base.Strings;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.changepwd.internal.module.ChangePwdModule;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.portal.event.LoginRequestedEvent;
import org.novaforge.forge.ui.portal.i18n.ExceptionCodeHandler;

import java.io.Serializable;
import java.util.Locale;

/**
 * THis presenter handle the change password view.
 * 
 * @author Guillaume Lamirand
 */
public class ChangePwdPresenter extends AbstractPortalPresenter implements Serializable, PortalComponent
{

  /**
   * Serial version id
   */
  private static final long   serialVersionUID = -918089974053639548L;
  /**
   * Log element
   */
  private static final Log LOG = LogFactory.getLog(ChangePwdPresenter.class);
  /**
   * Content the workspace view
   */
  private final ChangePwdView view;
  /**
   * Contains a <code>String</code> with ticket information
   */
  private final String        recoveryTicket;
  /**
   * Contains a <code>String</code> with user's login
   */
  private final String        login;
  /**
   * Contains a <code>String</code> with user's pwd
   */
  private final String        pwd;

  /**
   * Default constructor.
   *
   * @param pView
   *          the view associated to this presenter
   * @param pPortalContext
   */
  public ChangePwdPresenter(final ChangePwdView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    recoveryTicket = pPortalContext.getAttributes().get(PortalContext.KEY.TICKET);
    login = pPortalContext.getAttributes().get(PortalContext.KEY.LOGIN);
    pwd = pPortalContext.getAttributes().get(PortalContext.KEY.PWD);
    addListeners();
  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getHomeLink().addClickListener(new ClickListener()
    {

      /**
       * Serial version id
       */
      private static final long serialVersionUID = 5359979104314324398L;

      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        final UI ui = UI.getCurrent();
        ui.close();
      }
    });

    view.getNewPasswd().addTextChangeListener(new TextChangeListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = 45171870510918700L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void textChange(final TextChangeEvent event)
      {
        checkNewPassword(event.getText());
      }
    });
    view.getNewAgainPasswd().addTextChangeListener(new TextChangeListener()
    {
      /**
       * Default serial version UID
       */
      private static final long serialVersionUID = -1097799932417970269L;

      /**
       * {@inheritDoc}
       */
      @Override
      public void textChange(final TextChangeEvent event)
      {
        checkNewPwdAgainMatchNewPwd(event.getText());
      }
    });

    view.getApply().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = 5359979104314324398L;

      @Override
      public void buttonClick(final ClickEvent pEvent)
      {
        try
        {
          final String login = view.getLogin().getValue();
          final String currentPwd = view.getCurrentPasswd().getValue();
          final String newPwd = view.getNewPasswd().getValue();

          if ((recoveryTicket != null) && (!"".equals(recoveryTicket)))
          {
            updateUserPwdFromTicket(login, newPwd);
          }
          else
          {
            // Get the user and check its password again
            final User user = getUser(login);
            final String currentPwdHashed = ChangePwdModule.getAuthentificationService().hashPassword(currentPwd);
            if ((user != null) && (user.getPassword().equals(currentPwdHashed)))
            {
              updateUserPwd(login, newPwd);
            }
            else
            {
              showErrorValid();
            }
          }
          doLogin(login, newPwd);
        }
        catch (final Exception e)
        {
          // Let's the form handle the exception
          LOG.error("Unable to update user password", e);
        }

      }
    });
  }

  /**
   * Check if new password match security rules
   */
  private void checkNewPassword(final String pNewPasswd)
  {
    boolean isNewPasswordValid = true;
    try
    {
      view.getNewPasswd().setValue(pNewPasswd);
      view.getNewPasswd().validate();
    }
    catch (final Exception e)
    {
      isNewPasswordValid = false;
      // Let's form handle this
    }
    final boolean isNewPasswordEmpty = (pNewPasswd == null) || ("".equals(pNewPasswd));

    view.setNewPasswdValid(isNewPasswordEmpty, isNewPasswordValid);

    final String newPasswordAgainValue = view.getNewAgainPasswd().getValue();
    final boolean isNewPasswordAgainEmpty = (newPasswordAgainValue == null) || ("".equals(newPasswordAgainValue));
    boolean isNewPasswordAgainValid = false;
    if (!isNewPasswordAgainEmpty)
    {
      try
      {
        // Need to send change event
        view.getNewAgainPasswd().setValue("");
        view.getNewAgainPasswd().setValue(newPasswordAgainValue);
        view.getNewAgainPasswd().validate();
        isNewPasswordAgainValid = true;
      }
      catch (final Exception e)
      {
        isNewPasswordAgainValid = false;
        // Let's form handle this
      }
    }
    view.setNewPasswdMatching(isNewPasswordAgainEmpty, isNewPasswordAgainValid);
    final boolean isUpdatable = isNewPasswordValid && isNewPasswordAgainValid;
    view.getApply().setEnabled(isUpdatable);
  }

  /**
   * Check if new passwd again match new passwd
   */
  private void checkNewPwdAgainMatchNewPwd(final String pNewPwdAgain)
  {
    boolean isNewPasswordAgainValid = true;
    try
    {
      view.getNewAgainPasswd().setValue(pNewPwdAgain);
      view.getNewAgainPasswd().validate();
    }
    catch (final Exception e)
    {
      // Let's form handle this
      isNewPasswordAgainValid = false;
    }

    final boolean isNewPasswordValid = view.getNewPasswd().isValid();
    final String  newPasswordValue   = view.getNewPasswd().getValue();
    final boolean isNewPasswordEmpty = (newPasswordValue == null) || ("".equals(newPasswordValue));

    final boolean isNewPasswordAgainEmpty = (pNewPwdAgain == null) || ("".equals(pNewPwdAgain));

    view.setNewPasswdMatching(isNewPasswordEmpty && isNewPasswordAgainEmpty, isNewPasswordAgainValid);
    final boolean isUpdatable = isNewPasswordValid && isNewPasswordAgainValid;
    view.getApply().setEnabled(isUpdatable);
  }

  /**
   * Update the user pwd from ticket request with an hash of the pwd given in paremeter
   *
   * @param pCleanNewPwd
   *     clean new pwd
   */
  private void updateUserPwdFromTicket(final String pLogin, final String pCleanNewPwd)
  {
    final String newPwdHashed = ChangePwdModule.getAuthentificationService().hashPassword(pCleanNewPwd);
    try
    {
      ChangePwdModule.getUserPresenter().updatePasswordFromRecovery(recoveryTicket, newPwdHashed);
    }
    catch (final UserServiceException e)
    {
      LOG.error(String.format("Unable to update user password according to parameter [ticket=%s]", recoveryTicket), e);

      ExceptionCodeHandler.showNotificationError(ChangePwdModule.getPortalMessages(), e, UI.getCurrent().getLocale());
    }
  }

  /**
   * Retrieve the user object from its username
   *
   * @param pLogin
   *          user's login
   * @return user found or null if login is unexisting
   */
  private User getUser(final String pLogin)
  {
    User user = null;
    try
    {
      user = ChangePwdModule.getUserPresenter().getUser(pLogin);
    }
    catch (final UserServiceException e)
    {
      LOG.error(String.format("Unable to retrieve user according to parameter [user=%s]", pLogin), e);
    }
    return user;
  }

  /**
   * Update the user pwd with an hash of the pwd given in paremeter
   *
   * @param pUserLogin
   *          user login
   * @param pCleanNewPwd
   *          clean new pwd
   */
  private void updateUserPwd(final String pUserLogin, final String pCleanNewPwd)
  {
    final String newPwdHashed = ChangePwdModule.getAuthentificationService().hashPassword(pCleanNewPwd);
    try
    {

      ChangePwdModule.getUserPresenter().updatePassword(pUserLogin, newPwdHashed);
    }
    catch (final UserServiceException e)
    {
      LOG.error(String.format("Unable to update user according to parameter [user_login=%s]", pUserLogin), e);
      ExceptionCodeHandler.showNotificationError(ChangePwdModule.getPortalMessages(), e, UI.getCurrent()
          .getLocale());
    }
  }

  /**
   * It will just show an notification error
   */
  private void showErrorValid()
  {
    final Locale locale = getCurrentLocale();
    final Notification error = new Notification(ChangePwdModule.getPortalMessages().getMessage(locale,
                                                                                               Messages.ERROR_CREDENTIAL_MATHING_TITLE),
                                                ChangePwdModule.getPortalMessages().getMessage(locale,
                                                                                               Messages.ERROR_CREDENTIAL_MATHING_DESC),
                                                Type.ERROR_MESSAGE);
    error.show(Page.getCurrent());
  }

  /**
   * Send {@link LoginRequestedEvent} to redirect the user
   *
   * @param pLogin
   *          user's login
   * @param pPwd
   *          user's pwd
   */
  private void doLogin(final String pLogin, final String pPwd)
  {
    getEventBus().publish(new LoginRequestedEvent(pLogin.toLowerCase(), pPwd));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Component getComponent()
  {
    return view;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshContent()
  {
    init();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    refreshLocalized(getCurrentLocale());
  }

  /**
   * It will init the view according to the parameter.
   *
   * @param pLocale
   *     user's locale used for i18n
   * @param pLogin
   *     the user login
   * @param pPwd
   *     the user pwd
   */
  public void init(final Locale pLocale, final String pLogin, final String pPwd)
  {
    view.getPasswdSecurityRuleLabel().setValue
    (ChangePwdModule.getPortalMessages().getMessage(pLocale,Messages.CHANGEPWD_FORM_NEW_TOOLTIP));
    
    if (!Strings.isNullOrEmpty(pLogin))
    {
      view.getLogin().setValue(pLogin);
      view.getLogin().setReadOnly(true);
    }
    if (!Strings.isNullOrEmpty(pPwd))
    {
      view.getCurrentPasswd().setVisible(false);
      view.getCurrentPasswd().setValue(pPwd);
    }
    view.getApply().setVisible(true);
    view.getHomeLink().setVisible(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {

    if (!Strings.isNullOrEmpty(recoveryTicket))
    {

      User user;
      try
      {
        user = ChangePwdModule.getUserPresenter().getUserFromRecoveryPasswordToken(recoveryTicket);
        init(user.getLanguage().getLocale(), user.getLogin(), user.getPassword());
      }
      catch (final UserServiceException e)
      {
        //Home Link
        view.getHomeLink().setVisible(true);
        view.getPasswdSecurityRuleLabel().setValue(ChangePwdModule.getPortalMessages().getMessage(pLocale,Messages.CHANGEPWD_RECOVERY_TICKET));
        view.getLogin().setVisible(false);
        view.getCurrentPasswd().setVisible(false);
        view.getNewPasswd().setVisible(false);
        view.getNewAgainPasswd().setVisible(false);
        //Button
        view.getApply().setVisible(false);  
      }
    }
    else
    {
      init(pLocale, login, pwd);
    }

    // Update password reg ex
    view.getPasswordRegexValidator().setValidationRegex(ChangePwdModule.getForgeConfigurationService()
                                                                       .getPasswordValidationRegex());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return ChangePwdModule.getPortalModuleId();
  }
}
