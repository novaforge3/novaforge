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
package org.novaforge.forge.ui.portal.client.i18n;

import com.vaadin.server.CustomizedSystemMessages;
import com.vaadin.server.SystemMessagesInfo;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.ui.portal.osgi.OSGiHelper;
import org.novaforge.forge.ui.portal.shared.PortalContentUri;

import java.util.Locale;

/**
 * {@link CustomizedSystemMessages} extension that relies on a properties file.
 * This class also maintains the language for each user, so that different users
 * can get the message in their own language (once the session is under way).
 * 
 * @author Guillaume Lamirand
 */
public class LocalizedSystemMessages extends CustomizedSystemMessages
{
  /**
   * Serial version id
   */
  private static final long serialVersionUID = 295825699005962978L;
  /**
   * Contains the locale used to return correct system message
   */
  private final Locale currentLocale;
  /**
   * Contains the url used to redirect the user on error
   */
  private final String publicUrl;

  /**
   * Default constructor with {@link SystemMessagesInfo} used to retrieve locale and context
   *
   * @param pSystemMessagesInfo
   *          the {@link SystemMessagesInfo}
   */
  public LocalizedSystemMessages(final SystemMessagesInfo pSystemMessagesInfo)
  {
    if (pSystemMessagesInfo.getRequest() != null)
    {
      publicUrl = pSystemMessagesInfo.getRequest().getContextPath() + PortalContentUri.PRIVATE.getUri();
    }
    else
    {
      publicUrl = "/portal/private";
    }
    currentLocale = pSystemMessagesInfo.getLocale();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSessionExpiredURL()
  {
    return publicUrl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSessionExpiredCaption()
  {
    return (sessionExpiredNotificationEnabled ?
                OSGiHelper.getPortalMessages().getMessage(currentLocale, Messages.PORTAL_SYSTEM_SESSIONEXPIRED_TITLE) :
                null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSessionExpiredMessage()
  {
    return (sessionExpiredNotificationEnabled ?
                OSGiHelper.getPortalMessages().getMessage(currentLocale,
                                                          Messages.PORTAL_SYSTEM_SESSIONEXPIRED_MESSAGE) :
                null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommunicationErrorURL()
  {
    return publicUrl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommunicationErrorCaption()
  {
    return (communicationErrorNotificationEnabled ?
                OSGiHelper.getPortalMessages().getMessage(currentLocale,
                                                          Messages.PORTAL_SYSTEM_COMMUNICATIONERROR_TITLE) :
                null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCommunicationErrorMessage()
  {
    return (communicationErrorNotificationEnabled ?
                OSGiHelper.getPortalMessages().getMessage(currentLocale,
                                                          Messages.PORTAL_SYSTEM_COMMUNICATIONERROR_MESSAGE) :
                null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthenticationErrorURL()
  {
    return publicUrl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthenticationErrorCaption()
  {
    return (authenticationErrorNotificationEnabled ?
                OSGiHelper.getPortalMessages().getMessage(currentLocale,
                                                          Messages.PORTAL_SYSTEM_AUTHENTIFICATIONERROR_TITLE) :
                null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthenticationErrorMessage()
  {
    return (authenticationErrorNotificationEnabled ? OSGiHelper.getPortalMessages().getMessage(currentLocale,
                                                                                               Messages.PORTAL_SYSTEM_AUTHENTIFICATIONERROR_MESSAGE) :
                null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getInternalErrorURL()
  {
    return publicUrl;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getInternalErrorCaption()
  {
    return (internalErrorNotificationEnabled ?
                OSGiHelper.getPortalMessages().getMessage(currentLocale, Messages.PORTAL_SYSTEM_INTERNALERROR_TITLE) :
                null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getInternalErrorMessage()
  {
    return (internalErrorNotificationEnabled ?
                OSGiHelper.getPortalMessages().getMessage(currentLocale, Messages.PORTAL_SYSTEM_INTERNALERROR_MESSAGE) :
                null);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public String getCookiesDisabledURL()
  {
    return publicUrl;
  }

}
