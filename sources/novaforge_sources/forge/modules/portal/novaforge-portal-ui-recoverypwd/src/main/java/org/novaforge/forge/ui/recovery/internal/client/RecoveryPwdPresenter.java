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
package org.novaforge.forge.ui.recovery.internal.client;

import com.vaadin.server.Page;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.models.PortalComponent;
import org.novaforge.forge.portal.models.PortalContext;
import org.novaforge.forge.portal.models.PortalModuleId;
import org.novaforge.forge.ui.portal.client.modules.AbstractPortalPresenter;
import org.novaforge.forge.ui.recovery.internal.module.RecoveryModule;

import java.io.Serializable;
import java.util.Locale;

/**
 * THis presenter handle the recovery password view.
 * 
 * @author Guillaume Lamirand
 */
public class RecoveryPwdPresenter extends AbstractPortalPresenter implements Serializable, PortalComponent
{

  /**
   * Serial version id
   */
  private static final long     serialVersionUID = -918089974053639548L;
  /**
   * Log element
   */
  private static final Log      LOG              = LogFactory.getLog(RecoveryPwdPresenter.class);
  /**
   * Content the workspace view
   */
  private final RecoveryPwdView view;

  /**
   * Default constructor.
   * 
   * @param pView
   *          the view associated to this presenter
   * @param pPortalContext
   *          the portal context
   */
  public RecoveryPwdPresenter(final RecoveryPwdView pView, final PortalContext pPortalContext)
  {
    super(pPortalContext);
    // init the view
    view = pView;
    addListeners();

  }

  /**
   * It will add listeners to view components
   */
  private void addListeners()
  {
    view.getApply().addClickListener(new ClickListener()
    {
      /**
       * Serial version id
       */
      private static final long serialVersionUID = 5359979104314324398L;

      @Override
      public void buttonClick(final ClickEvent pEvent)
      {

        // Commit the form and let's the form handle the exception
        try
        {
          view.getForm().commit();
          final String email = view.getEmailField().getValue().toString();
          recoveryPwd(email);
        }
        catch (final Exception e)
        {
          // Let's the form handle the exception. the other one are catched.
        }
      }
    });
  }

  /**
   * Update the user pwd with an hash of the pwd given in paremeter
   * 
   * @param pEmail
   *          email enter by user
   */
  private void recoveryPwd(final String pEmail)
  {
    try
    {
      boolean isDeleted = RecoveryModule.getUserPresenter().deleteUserRecoveryPassword(pEmail);
      if (isDeleted){        
        final Notification notif = new Notification(RecoveryModule.getPortalMessages().getMessage(
            view.getLocale(), Messages.RECOVERY_REQUEST_TITLE), RecoveryModule.getPortalMessages().getMessage(
            view.getLocale(), Messages.RECOVERY_REQUEST_DESC), Type.WARNING_MESSAGE);        
        notif.setHtmlContentAllowed(true);
        notif.show(Page.getCurrent());
      }
            
      RecoveryModule.getUserPresenter().recoverUserPassword(pEmail);
      view.getForm().setVisible(false);
      view.getApply().setVisible(false);
      view.getDescriptionLabel().setValue(
          RecoveryModule.getPortalMessages().getMessage(view.getLocale(), Messages.RECOVERY_RESULT, pEmail));
    }
    catch (final UserServiceException e)
    {
      LOG.error(String.format("Unable to recovery user pwd according to parameter [email=%s]", pEmail), e);
      if ((e.getCode() != null) && (ExceptionCode.ERR_RECOVER_PASSWORD_EMAIL_UNKNOWN.equals(e.getCode())))
      {
        final Notification notif = new Notification(RecoveryModule.getPortalMessages().getMessage(
            view.getLocale(), Messages.RECOVERY_EMAIL_TITLE), RecoveryModule.getPortalMessages().getMessage(
            view.getLocale(), Messages.RECOVERY_EMAIL_DESC), Type.WARNING_MESSAGE);
        notif.setHtmlContentAllowed(true);
        notif.show(Page.getCurrent());
      }
      else
      {
        final Notification notif = new Notification(RecoveryModule.getPortalMessages().getMessage(
            view.getLocale(), Messages.ERROR_TECHNICAL_TITLE), RecoveryModule.getPortalMessages().getMessage(
            view.getLocale(), Messages.ERROR_TECHNICAL_DESC), Type.ERROR_MESSAGE);
        notif.setHtmlContentAllowed(true);
        notif.show(Page.getCurrent());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void init()
  {
    refreshContent();
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
    view.getForm().setVisible(true);
    view.getApply().setVisible(true);
    view.getDescriptionLabel().setValue(RecoveryModule.getPortalMessages().getMessage(view.getLocale(),
                                                                                      Messages.RECOVERY_DESC));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void refreshLocalized(final Locale pLocale)
  {
    refreshContent();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected PortalModuleId getModuleId()
  {
    return RecoveryModule.getPortalModuleId();
  }
}
