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
package org.novaforge.forge.core.organization.delegates;

import org.novaforge.forge.core.organization.exceptions.MailDelegateException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum;

import java.util.Locale;
import java.util.Set;

/**
 * The following service is used to send functional email to user
 * 
 * @author sbenoist
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
public interface MailDelegate
{
  /**
   * @param pProject
   * @param pToEmail
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendProjectCreationEmails(Project pProject, String pToEmail, final Locale pLocale)
      throws MailDelegateException;

  /**
   * @param pProject
   * @param pToEmail
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendTemplateInitializationErrorEmail(Project pProject, String pToEmail, final Locale pLocale)
      throws MailDelegateException;

  /**
   * @param pProject
   * @param pToEmail
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendProjectValidationEmail(final Project pProject, final String pToEmail, final Locale pLocale)
      throws MailDelegateException;

  /**
   * @param pUser
   * @param pPassword
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendUserPasswordRecovery(final User pUser, final String pPassword, final Locale pLocale)
      throws MailDelegateException;

  /**
   * @param pProject
   * @param pUser
   * @param pRoles
   * @param pToDoMembership
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendProjectMembershipUpdate(final Project pProject, final User pUser, final Set<Role> pRoles,
      final MailDelegateEnum pToDoMembership, final Locale pLocale) throws MailDelegateException;

  /**
   * @param pProject
   * @param pEmail
   * @param pReason
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendProjectRejectEmail(final Project pProject, final String pEmail, final String pReason,
      final Locale pLocale) throws MailDelegateException;

  /**
   * @param pUserLogin
   * @param pProject
   * @param pUserAdmin
   * @param pRoles
   * @param pToDoMembership
   * @param pMessage
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendProjectMembershipRequest(final String pUserLogin, final Project pProject, final User pUserAdmin,
      final Set<String> pRoles, final MailDelegateEnum pToDoMembership, final String pMessage,
      final Locale pLocale) throws MailDelegateException;

  /**
   * @param pUser
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendNewUserAccount(final User pUser, final Locale pLocale) throws MailDelegateException;

  /**
   * @param pUserEmail
   * @param pCategory
   * @param pType
   * @param pVersion
   * @param pStatus
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendLifeCycleChangeNotification(final String pUserEmail, final String pCategory, final String pType,
      final String pVersion, final String pStatus, final Locale pLocale) throws MailDelegateException;

  /**
   * @param pUserEmail
   * @param pCategory
   * @param pType
   * @param pVersion
   * @param pStatus
   * @param pRequest
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendLifeCycleChangeRequest(final String pUserEmail, final String pCategory, final String pType,
      final String pVersion, final String pStatus, final MailDelegateEnum pRequest, final Locale pLocale)
      throws MailDelegateException;

  /**
   * @param pProjectId
   * @param pLogin
   * @param pCategory
   * @param pType
   * @param pVersion
   * @throws MailDelegateException
   */
  void sendProjectApplicationRequest(final String pProjectId, final String pLogin, final String pCategory,
      final String pType, final String pVersion) throws MailDelegateException;

  /**
   * @param pLogin
   * @param pEmail
   * @param pLocale
   * @throws MailDelegateException
   */
  void sendNewEmailAdminNotification(final String pLogin, final String pEmail, final Locale pLocale)
      throws MailDelegateException;
}
