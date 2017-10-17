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
package org.novaforge.forge.ui.portal.i18n;

import com.vaadin.server.Page;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.exceptions.Codifiable;
import org.novaforge.forge.commons.exceptions.ForgeException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListExceptionCode;
import org.novaforge.forge.portal.i18n.Messages;
import org.novaforge.forge.portal.services.PortalMessages;

import java.util.Locale;

/**
 * This class will map a {@link ExceptionCode} to an error key
 * 
 * @author Guillaume Lamirand
 */
public class ExceptionCodeHandler
{
  /**
   * Used to log error when showing a user notification
   */
  private static final Log LOGGER = LogFactory.getLog(ExceptionCodeHandler.class);

  /**
   * This will display a notification error with correct message
   *
   * @param pPortalMessages
   *          the service implementation of {@link PortalMessages}
   * @param pException
   *          the source exception
   * @param pLocale
   *          the locale to use to retrieve message
   */
  public static void showNotificationError(final PortalMessages pPortalMessages, final Exception pException,
                                           final Locale pLocale)
  {

    // Retrieve caption and description key from exception
    String captionKey     = Messages.ERROR_TECHNICAL_TITLE;
    String descriptionKey = Messages.ERROR_TECHNICAL_DESC;
    if ((pException instanceof ForgeException) && (((ForgeException) pException).getCode() != null)
            && (((ForgeException) pException).getCode() instanceof Codifiable))
    {
      final ForgeException forgeException = (ForgeException) pException;
      captionKey = ExceptionCodeHandler.getKey(forgeException.getCode());
      descriptionKey = "";
    }
    // Log exception with default system locale
    if (Messages.ERROR_TECHNICAL_TITLE.equals(captionKey))
    {
      LOGGER.error(pPortalMessages.getMessage(Locale.getDefault(), captionKey), pException);
    }
    // Build the user notification
    final Notification notification = new Notification(pPortalMessages.getMessage(pLocale, captionKey),
                                                       pPortalMessages.getMessage(pLocale, descriptionKey),
                                                       Type.ERROR_MESSAGE);
    notification.setDelayMsec(2000);
    notification.setHtmlContentAllowed(true);
    notification.show(Page.getCurrent());
  }

  /**
   * Get a key message from {@link ExceptionCode}
   *
   * @param pError
   *          the exception code
   * @return the key associated
   */
  public static String getKey(final Codifiable pCodifiable)
  {
    String returnKey = Messages.ERROR_TECHNICAL_TITLE;

    if (pCodifiable != null)
    {
      if (pCodifiable instanceof ExceptionCode)
      {
        final ExceptionCode code = (ExceptionCode) pCodifiable;
        switch (code)
        {
        /*
         * Create project
         */
          case ERR_CREATE_PROJECT_PROJECT_NAME_ALREADY_EXIST:
            returnKey = Messages.ERROR_PROJECT_NAME_ALREADY_EXIST;
            break;

          case ERR_CREATE_PROJECT_PROJECTID_ALREADY_EXIST:
            returnKey = Messages.ERROR_PROJECTID_ALREADY_EXIST;
            break;

          case ERR_CREATE_PROJ_AUTHOR_NOT_EXIST:
            returnKey = Messages.ERROR_AUTHOR_NOT_EXIST;
            break;
          /*
           * Validate project
           */
          case ERR_CHANGE_PROJECT_STATUS_STATUS_ALREADY_CHANGED:
            returnKey = Messages.ERROR_PROJECT_STATUS_CHANGED;
            break;
          /*
           * Applications
           */
          case ERR_CREATE_SPACE_NAME_ALREADY_EXIST:
            returnKey = Messages.ERROR_SPACE_NAME_EXISTS;
            break;

          case ERR_CREATE_APP_NAME_ALREADY_EXIST:
            returnKey = Messages.ERROR_APPLICATION_NAME_EXISTS;
            break;
          case ERR_MAX_ALLOWED_PROJECT_APPLICATIONS:
            returnKey = Messages.ERROR_MAX_ALLOWED_PROJECT_APPLICATION;
            break;
          case ERR_PROJECT_APPLICATION_REQUEST_ALREADY_EXISTS:
            returnKey = Messages.ERROR_APPLICATION_REQUEST_EXISTS;
            break;
          /*
           * Associations
           */
          case ERR_REQUEST_COMPOSITION_ALREADY_EXISTS:
            returnKey = Messages.ERROR_ASSOCIATION_REQUEST_EXISTS;
            break;
          case ERR_CREATE_APP_COMP_EXIST:
            returnKey = Messages.ERROR_ASSOCIATION_EXISTS;
            break;
          /*
           * Memberships
           */
          case ERR_MEMBERSHIP_ACTOR_DOESNT_EXIST:
            returnKey = Messages.ERROR_MEMBERSHIP_ACTOR_NOTEXIST;
            break;
          case ERR_ADD_USER_MEMBERSHIP_MEMBERSHIP_ALREADY_EXIST:
            returnKey = Messages.ERROR_MEMBERSHIP_ROLE_EXISTS;
            break;
          case ERR_ADD_GROUP_MEMBERSHIP_GROUP_ISNT_PUBLIC:
            returnKey = Messages.ERROR_MEMBERSHIP_GROUP_NOTVISIBLE;
            break;
          case ERR_REMOVE_GROUP_MEMBERSHIP_GROUP_ISNT_PUBLIC:
            returnKey = Messages.ERROR_MEMBERSHIP_GROUP_NOTVISIBLE;
            break;
          /*
           * Group errors
           */
          case ERR_CREATE_GROUP_LOGIN_ALREADY_EXISTS:
            returnKey = Messages.ERROR_GROUP_NAME_EXISTS;
            break;
          case ERR_DELETE_GROUP_USER_WITH_MEMBERSHIP_EXIST:
            returnKey = Messages.ERROR_GROUP_MEMBERSHIP_EXISTS;
            break;
          /*
           * Roles
           */
          case ERR_CREATE_ROLE_ROLENAME_ALREADY_EXIST:
            returnKey = Messages.ERROR_ROLE_NAME_EXISTS;
            break;

          case ERR_DELETE_ROLE_USER_WITH_ROLE_EXIST:
            returnKey = Messages.ERROR_ROLE_USED;
            break;

          case ERR_UPDATE_OR_DELETE_SYSTEM_ROLE:
            returnKey = Messages.ERROR_ROLE_SYSTEM;
            break;

          /*
           * User
           */
          case ERR_CREATE_USER_EMAIL_ALREADY_EXISTS:
            returnKey = Messages.ERROR_USER_EMAIL_EXISTS;
            break;

          case ERR_CREATE_USER_LOGIN_ALREADY_EXISTS:
            returnKey = Messages.ERROR_USER_LOGIN_EXISTS;
            break;

          case ERR_CREATE_USER_FORBIDDEN_LOGIN:
            returnKey = Messages.ERROR_USER_FORBIDDEN_LOGIN;
            break;
          /*
           * sending mail
           */
          case ERR_CREATE_PROJECT_SENDING_MAIL_AUTHOR:
            returnKey = Messages.ERROR_SENDING_MAIL_AUTHOR;
            break;

          case ERR_VALIDATE_PROJECT_SENDING_MAIL_AUTHOR:
            returnKey = Messages.ERROR_SENDING_MAIL_AUTHOR;
            break;

          case ERR_CREATE_PROJECT_SENDING_MAIL_ADMINISTRATOR:
            returnKey = Messages.ERROR_SENDING_MAIL_ADMINISTRATOR;
            break;

          case ERR_ADD_PROJECT_MEMBERSHIP_SENDING_MAIL:
            returnKey = Messages.ERROR_SENDING_MAIL_MEMBER;
            break;

          case ERR_UPDATE_PROJECT_MEMBERSHIP_SENDING_MAIL:
            returnKey = Messages.ERROR_SENDING_MAIL_MEMBER;
            break;

          case ERR_REMOVE_PROJECT_MEMBERSHIP_SENDING_MAIL:
            returnKey = Messages.ERROR_SENDING_MAIL_MEMBER;
            break;
          case ERR_PROJECT_MEMBERSHIP_REQUEST_SENDING_RESPONSE_MAIL_TO_USER:
            returnKey = Messages.ERROR_SENDING_MAIL_MEMBER;
            break;
          /*
           * Commons errors
           */
          case ERR_VALIDATION_BEAN:
            returnKey = Messages.ERROR_VALIDATION_BEAN;
            break;
          case TECHNICAL_ERROR:
          default:
            returnKey = Messages.ERROR_TECHNICAL_TITLE;
            break;
        }
      }
      else if (pCodifiable instanceof MailingListExceptionCode)
      {
        final MailingListExceptionCode code = (MailingListExceptionCode) pCodifiable;
        switch (code)
        {
          case ERR_CREATE_MAILING_LIST_ALREADY_EXISTS:
            returnKey = Messages.ERROR_CREATE_MAILING_LIST_ALREADY_EXISTS;
            break;
          default:
            returnKey = Messages.ERROR_TECHNICAL_TITLE;
            break;
        }
      }
    }
    return returnKey;
  }
}
