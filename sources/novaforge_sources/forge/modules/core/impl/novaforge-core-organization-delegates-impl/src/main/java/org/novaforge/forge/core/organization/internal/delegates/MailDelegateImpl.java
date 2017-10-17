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
/**
 * 
 */
package org.novaforge.forge.core.organization.internal.delegates;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.mail.MailService;
import org.novaforge.forge.commons.technical.mail.MailServiceException;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.dao.UserDAO;
import org.novaforge.forge.core.organization.delegates.MailDelegate;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.MailDelegateException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum;
import org.novaforge.forge.core.organization.model.enumerations.MailMessage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author sbenoist
 */
public class MailDelegateImpl implements MailDelegate
{

  /**
   * 
   */
  private static final String         PLUGIN_TYPE              = "{PLUGIN_TYPE}";
  /**
   * Constant for /
   */
  private static final String         URL_SEPARATOR            = "/";
  /**
   * Logger
   */
  private static final Log            log                      = LogFactory.getLog(MailDelegateImpl.class);
  private static final String         RECOVERY_ALIAS           = "portal/recovery";
  private static final String         ROLES_NAME               = "{ROLES_NAME}";
  private static final String         MESSAGE                  = "{MESSAGE}";
  private static final String         PLUGIN_CATEGORY          = "{PLUGIN_CATEGORY}";
  private static final String         PORTAL_URL               = "{PORTAL_URL}";
  private static final String         REJECT_REASON            = "{REJECT_REASON}";
  private static final String         PLUGIN_VERSION           = "{PLUGIN_VERSION}";
  private static final String         PASSWORD_RECOVERY_LINK   = "{PASSWORD_RECOVERY_LINK}";
  private static final String         PROJECT_NAME             = "{PROJECT_NAME}";
  private static final String         PROJECT_ID               = "{PROJECT_ID}";
  private static final String         PLUGIN_STATUS            = "{PLUGIN_STATUS}";
  private static final String         USER_LOGIN               = "{USER_LOGIN}";
  private static final String         USER_EMAIL               = "{USER_EMAIL}";
  /**
   * The mail messages file
   */
  private static String NAVIGATION_MESSAGES_FILE = "messages";
  /**
   * The mail messages folder
   */
  private static String MAIL_MESSAGES_FOLDER     = "mails";
  /**
   * {@link MailService} injected by container
   */
  private MailService                 mailService;
  /**
   * {@link ForgeConfigurationService} injected by container
   */
  private ForgeConfigurationService   forgeConfigurationService;
  /**
   * {@link UserDAO} injected by container
   */
  private UserDAO                     userDAO;
  /**
   * Current i18n bundles ressource
   */
  private Map<String, ResourceBundle> resourceBundles;

  /**
   * This will read resource bundle available for french and english locale
   */
  public void init()
  {
    resourceBundles = new HashMap<String, ResourceBundle>();
    final String portalBundlePath =
        forgeConfigurationService.getForgeConfDirectory() + File.separatorChar + MAIL_MESSAGES_FOLDER;
    final File file = new File(portalBundlePath);
    try
    {
      final URL[] urls = { file.toURI().toURL() };
      final ClassLoader loader = new URLClassLoader(urls);
      try
      {
        resourceBundles.put(Locale.ENGLISH.getLanguage(),
            ResourceBundle.getBundle(NAVIGATION_MESSAGES_FILE, Locale.ENGLISH, loader));
      }
      catch (final MissingResourceException ex)
      {
        // Ignore this
      }
      try
      {
        resourceBundles.put(Locale.FRENCH.getLanguage(),
            ResourceBundle.getBundle(NAVIGATION_MESSAGES_FILE, Locale.FRENCH, loader));
      }
      catch (final MissingResourceException ex)
      {
        // Ignore this
      }
    }
    catch (final MalformedURLException ex)
    {
      // Ignore this
    }
  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendProjectCreationEmails(org.novaforge
   * .forge.core.organization.model.Project, java.lang.String, java.util.Locale)
   */
  @Override
  public void sendProjectCreationEmails(final Project pProject, final String pToEmail, final Locale pLocale)
      throws MailDelegateException
  {
    // send an email to the project author
    try
    {
      final String subject = getMailMessage(MailMessage.PROJECT_CREATED__SUBJECT, pLocale);
      final String body = getMailMessage(MailMessage.PROJECT_CREATED__BODY, pLocale);
      mailService.sendMail(pToEmail, subject, formatProjectMessage(body, pProject));
    }
    catch (final MailServiceException e)
    {
      log.error(
          MessageFormat
              .format(
                  "an error occured while sending an email to inform author for project creation (project={0}, administrator email={1})",
                  pProject.getProjectId(), pToEmail), e);
      throw new MailDelegateException(ExceptionCode.ERR_CREATE_PROJECT_SENDING_MAIL_AUTHOR, String.format(
          "projectId=%s", pProject.getProjectId()), e);
    }

    // send an email to the administrator to validate the project
    final User superAdmin = getSuperAdmin();
    final String adminEmail = superAdmin.getEmail();
    try
    {
      final String subject = getMailMessage(MailMessage.PROJECT_TOVALIDATE__SUBJECT, pLocale);
      final String body = getMailMessage(MailMessage.PROJECT_TOVALIDATE_BODY, pLocale);
      mailService.sendMail(adminEmail, subject, formatProjectMessage(body, pProject));
    }
    catch (final MailServiceException e)
    {
      log.error(
          MessageFormat
              .format(
                  "an error occured while sending an email to inform administrator for project creation (project={0}, administrator email={1})",
                  pProject.getProjectId(), adminEmail), e);
      throw new MailDelegateException(ExceptionCode.ERR_CREATE_PROJECT_SENDING_MAIL_ADMINISTRATOR,
          String.format("projectId=%s", pProject.getProjectId()), e);
    }

  }

  /**
   * Get internationnalized message for key and locale
   *
   * @param pKey
   *     the message key
   * @param pLocale
   *     the {@link #Locale}
   *
   * @return the message for the key in this locale, the key if not found
   */
  private String getMailMessage(final MailMessage pKey, final Locale pLocale)
  {
    String value = pKey.getKey();
    try
    {
      final ResourceBundle mailBundle = getMailBundle(pLocale);
      if (mailBundle != null)
      {
        value = mailBundle.getString(pKey.getKey());
      }
    }
    catch (final MissingResourceException e)
    {
      // ignore this, the value will be the key
    }
    return value;
  }

  private String formatProjectMessage(final String pMessage, final Project pProject)
  {
    String message = pMessage.replace(PORTAL_URL, getPortalUrl());
    message = message.replace(USER_LOGIN, pProject.getAuthor());
    message = message.replace(PROJECT_ID, pProject.getProjectId());
    message = message.replace(PROJECT_NAME, pProject.getName());
    return message;
  }

  private User getSuperAdmin()
  {
    final String adminLogin = forgeConfigurationService.getSuperAdministratorLogin();
    return userDAO.findByLogin(adminLogin);
  }

  /**
   * Get the Mail Resource bundle
   *
   * @param pLocale
   *
   * @return the mail {@link ResourceBundle}
   */
  private ResourceBundle getMailBundle(final Locale pLocale)
  {
    ResourceBundle resourceBundle = null;
    if (resourceBundles.containsKey(pLocale.getLanguage()))
    {
      resourceBundle = resourceBundles.get(pLocale.getLanguage());
    }
    return resourceBundle;
  }

  private String getPortalUrl()
  {
    return String.valueOf(forgeConfigurationService.getPublicUrl()) + forgeConfigurationService.getPortalEntryPoint();
  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendTemplateInitializationErrorEmail(org
   * .novaforge.forge.core.organization.model.Project, java.lang.String, java.util.Locale)
   */
  @Override
  public void sendTemplateInitializationErrorEmail(final Project pProject, final String pToEmail,
      final Locale pLocale) throws MailDelegateException
  {
    // send an email to the project author
    try
    {
      final String subject = getMailMessage(MailMessage.TEMPLATE_ERROR_SUBJECT, pLocale);
      final String body = getMailMessage(MailMessage.TEMPLATE_ERROR_BODY, pLocale);
      mailService.sendMail(pToEmail, subject, formatProjectMessage(body, pProject));
    }
    catch (final Exception e)
    {
      log.error(
          MessageFormat
              .format(
                  "an error occured while sending an email to inform user for project reject (project={0}, author email={1})",
                  pProject.getProjectId(), pToEmail), e);
      throw new MailDelegateException(ExceptionCode.ERR_REJECT_PROJECT_SENDING_MAIL_AUTHOR, e.toString());
    }
  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendProjectValidationEmail(org.novaforge
   * .forge.core.organization.model.Project, java.lang.String, java.util.Locale)
   */
  @Override
  public void sendProjectValidationEmail(final Project pProject, final String pToEmail, final Locale pLocale)
      throws MailDelegateException
  {
    // send an email to the project author
    try
    {
      final String subject = getMailMessage(MailMessage.PROJECT_VALID_SUBJECT, pLocale);
      final String body = getMailMessage(MailMessage.PROJECT_VALID_BODY, pLocale);
      mailService.sendMail(pToEmail, subject, formatProjectMessage(body, pProject));
    }
    catch (final MailServiceException e)
    {
      log.error(
          MessageFormat
              .format(
                  "an error occured while sending an email to inform user for project validation (project={0}, author email={1})",
                  pProject.getProjectId(), pToEmail), e);
      throw new MailDelegateException(ExceptionCode.ERR_VALIDATE_PROJECT_SENDING_MAIL_AUTHOR, String.format(
          "projectId=%s", pProject.getProjectId()), e);
    }

  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendUserPasswordRecovery(org.novaforge.
   * forge.core.organization.model.User, java.lang.String, java.util.Locale)
   */
  @Override
  public void sendUserPasswordRecovery(final User pUser, final String pPassword, final Locale pLocale)
      throws MailDelegateException
  {
    // send an email to the user
    try
    {
      final String subject = getMailMessage(MailMessage.PASSWORD_RECOVERY_SUBJECT, pLocale);
      final String body = getMailMessage(MailMessage.PASSWORD_RECOVERY_BODY, pLocale);
      mailService.sendMail(pUser.getEmail(), subject, formatUserMessage(body, pPassword));
    }
    catch (final MailServiceException e)
    {
      log.error(MessageFormat.format(
          "an error occured while sending an email for password recovery (user={0}, email={1})",
          pUser.getLogin(), pUser.getEmail()), e);
      throw new MailDelegateException(ExceptionCode.ERR_PASSWORD_RECOVERY_SENDING_MAIL, String.format(
          "login=%s, email=%s", pUser.getLogin(), pUser.getEmail()), e);
    }

  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendProjectMembershipUpdate(org.novaforge
   * .forge.core.organization.model.Project, org.novaforge.forge.core.organization.model.User, java.util.Set,
   * org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum, java.util.Locale)
   */
  @Override
  public void sendProjectMembershipUpdate(final Project pProject, final User pUser, final Set<Role> pRoles,
      final MailDelegateEnum pToDoMembership, final Locale pLocale) throws MailDelegateException
  {
    final String subject = getMailMessage(MailMessage.MEMBERSHIP_SUBJECT, pLocale);
    switch (pToDoMembership)
    {

      case ADD_MEMBERSHIP:
        try
        {
          final String body = getMailMessage(MailMessage.ADD_MEMBERSHIP_BODY, pLocale);
          mailService.sendMail(pUser.getEmail(), subject,
              formatMembershipMessage(body, pProject, pUser, pRoles, pLocale));
        }
        catch (final MailServiceException e)
        {
          log.error(MessageFormat.format(
              "an error occured while sending an email for adding membership (project={0}, user email={1})",
              pProject.getProjectId(), pUser.getEmail()), e);
          throw new MailDelegateException(ExceptionCode.ERR_ADD_PROJECT_MEMBERSHIP_SENDING_MAIL,
              String.format("projectId=%s, login=%s", pProject.getProjectId(), pUser.getLogin()), e);
        }
        break;
      case UPDATE_MEMBERSHIP:
        try
        {
          final String body = getMailMessage(MailMessage.UPDATE_MEMBERSHIP_BODY, pLocale);
          mailService.sendMail(pUser.getEmail(), subject,
              formatMembershipMessage(body, pProject, pUser, pRoles, pLocale));
        }
        catch (final MailServiceException e)
        {
          log.error(MessageFormat.format(
              "an error occured while sending an email for adding membership (project={0}, user email={1})",
              pProject.getProjectId(), pUser.getEmail()), e);
          throw new MailDelegateException(ExceptionCode.ERR_UPDATE_PROJECT_MEMBERSHIP_SENDING_MAIL,
              String.format("projectId=%s, login=%s", pProject.getProjectId(), pUser.getLogin()), e);
        }
        break;
      case REMOVE_MEMBERSHIP:
        try
        {
          final String body = getMailMessage(MailMessage.REMOVE_MEMBERSHIP_BODY, pLocale);
          mailService.sendMail(pUser.getEmail(), subject,
              formatMembershipMessage(body, pProject, pUser, pRoles, pLocale));
        }
        catch (final MailServiceException e)
        {
          log.error(MessageFormat.format(
              "an error occured while sending an email for adding membership (project={0}, user email={1})",
              pProject.getProjectId(), pUser.getEmail()), e);
          throw new MailDelegateException(ExceptionCode.ERR_REMOVE_PROJECT_MEMBERSHIP_SENDING_MAIL,
              String.format("projectId=%s, login=%s", pProject.getProjectId(), pUser.getLogin()), e);
        }
        break;
      default:
        break;
    }

  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendProjectRejectEmail(org.novaforge.forge
   * .core.organization.model.Project, java.lang.String, java.lang.String, java.util.Locale)
   */
  @Override
  public void sendProjectRejectEmail(final Project pProject, final String pEmail, final String pReason,
      final Locale pLocale) throws MailDelegateException
  {
    // send an email to the project author
    try
    {
      final String subject = getMailMessage(MailMessage.PROJECT_REJECT_SUBJECT, pLocale);
      final String body = getMailMessage(MailMessage.PROJECT_REJECT_BODY, pLocale);
      mailService.sendMail(pEmail, subject, formatRejectProjectMessage(body, pProject, pReason));
    }
    catch (final MailServiceException e)
    {
      log.error(
          MessageFormat
              .format(
                  "an error occured while sending an email to inform user for project reject (project={0}, author email={1})",
                  pProject.getProjectId(), pEmail), e);
      throw new MailDelegateException(ExceptionCode.ERR_REJECT_PROJECT_SENDING_MAIL_AUTHOR, String.format(
          "projectId=%s", pProject.getProjectId()), e);
    }
  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendProjectMembershipRequest(java.lang.
   * String, org.novaforge.forge.core.organization.model.Project,
   * org.novaforge.forge.core.organization.model.User, java.util.Set,
   * org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum, java.lang.String,
   * java.util.Locale)
   */
  @Override
  public void sendProjectMembershipRequest(final String pUserLogin, final Project pProject,
      final User pUserAdmin, final Set<String> pRoles, final MailDelegateEnum pToDoMembership,
      final String pMessage, final Locale pLocale) throws MailDelegateException
  {
    final String subject = getMailMessage(MailMessage.MEMBERSHIP_REQUEST_SUBJECT, pLocale);
    switch (pToDoMembership)
    {
      case MEMBERSHIP_REQUEST:
        try
        {
          final String body = getMailMessage(MailMessage.MEMBERSHIP_REQUEST_BODY, pLocale);
          mailService.sendMail(pUserAdmin.getEmail(), subject,
              formatMembershipRequestMessage(body, pProject, pUserLogin, pRoles, pMessage, pLocale));
        }
        catch (final Exception e)
        {
          log.error(
              MessageFormat
                  .format(
                      "an error occured while sending an email for validate membership request (project={0}, user email={1})",
                      pProject.getProjectId(), pUserAdmin.getEmail()), e);
          throw new MailDelegateException(
              ExceptionCode.ERR_PROJECT_MEMBERSHIP_REQUEST_SENDING_MAIL_TO_ADMINS, String.format(
                  "projectId=%s, login=%s", pProject.getProjectId(), pUserAdmin.getLogin()), e);
        }
        break;
      case VALIDATE_MEMBERSHIP_REQUEST:
        try
        {
          final String body = getMailMessage(MailMessage.VALIDATE_MEMBERSHIP_REQUEST_BODY, pLocale);
          mailService.sendMail(pUserAdmin.getEmail(), subject,
              formatMembershipRequestMessage(body, pProject, pUserLogin, pRoles, pMessage, pLocale));
        }
        catch (final Exception e)
        {
          log.error(
              MessageFormat
                  .format(
                      "an error occured while sending an email for validate membership request (project={0}, user email={1})",
                      pProject.getProjectId(), pUserAdmin.getEmail()), e);
          throw new MailDelegateException(
              ExceptionCode.ERR_PROJECT_MEMBERSHIP_REQUEST_SENDING_RESPONSE_MAIL_TO_USER, String.format(
                  "projectId=%s, login=%s", pProject.getProjectId(), pUserAdmin.getLogin()), e);
        }
        break;
      case INVALIDATE_MEMBERSHIP_REQUEST:
        try
        {
          final String body = getMailMessage(MailMessage.INVALIDATE_MEMBERSHIP_REQUEST_BODY, pLocale);
          mailService.sendMail(pUserAdmin.getEmail(), subject,
              formatMembershipRequestMessage(body, pProject, pUserLogin, pRoles, pMessage, pLocale));
        }
        catch (final Exception e)
        {
          log.error(
              MessageFormat
                  .format(
                      "an error occured while sending an email for validate membership request (project={0}, user email={1})",
                      pProject.getProjectId(), pUserAdmin.getEmail()), e);
          throw new MailDelegateException(
              ExceptionCode.ERR_PROJECT_MEMBERSHIP_REQUEST_SENDING_RESPONSE_MAIL_TO_USER, String.format(
                  "projectId=%s, login=%s", pProject.getProjectId(), pUserAdmin.getLogin()), e);
        }
        break;
      default:
        break;
    }

  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendNewUserAccount(org.novaforge.forge.
   * core.organization.model.User, java.util.Locale)
   */
  @Override
  public void sendNewUserAccount(final User pUser, final Locale pLocale) throws MailDelegateException
  {
    try
    {
      final String subject = getMailMessage(MailMessage.NEW_USER_ACCOUNT_SUBJECT, pLocale);
      final String body = getMailMessage(MailMessage.NEW_USER_ACCOUNT_BODY, pLocale);
      mailService.sendMail(pUser.getEmail(), subject, formatNewUserAccountMessage(body, pUser.getLogin()));
    }
    catch (final MailServiceException e)
    {
      log.error(MessageFormat.format(
          "an error occured while sending an email for new user account (user={0}, email={1})",
          pUser.getLogin(), pUser.getEmail()), e);
      throw new MailDelegateException(ExceptionCode.ERR_NEW_ACCOUNT_SENDING_MAIL, String.format(
          "login=%s, email=%s", pUser.getLogin(), pUser.getEmail()), e);
    }
  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendLifeCycleChangeNotification(java.lang
   * .String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.util.Locale)
   */
  @Override
  public void sendLifeCycleChangeNotification(final String pUserEmail, final String pCategory,
      final String pType, final String pVersion, final String pStatus, final Locale pLocale)
      throws MailDelegateException
  {
    final String subject = getMailMessage(MailMessage.LIFE_CYCLE_CHANGE_SUBJECT, pLocale);
    final String body = getMailMessage(MailMessage.LIFE_CYCLE_CHANGE_BODY, pLocale);
    try
    {
      mailService.sendMail(pUserEmail, subject,
          formatPluginLifeCycleChangeMessage(body, pCategory, pType, pVersion, pStatus));
    }
    catch (final MailServiceException e)
    {

      final String format = String.format(
          "an error occured while sending an email to notify changes on plugin (email=%s)", pUserEmail);
      log.error(format, e);
      throw new MailDelegateException(format, e);
    }

  }

  /*
   * (non-Javadoc)
   * @see
   * org.novaforge.forge.core.organization.delegates.MailDelegate#sendLifeCycleChangeRequest(java.lang.String
   * , java.lang.String, java.lang.String, java.lang.String, java.lang.String,
   * org.novaforge.forge.core.organization.model.enumerations.MailDelegateEnum, java.util.Locale)
   */
  @Override
  public void sendLifeCycleChangeRequest(final String pUserEmail, final String pCategory, final String pType,
      final String pVersion, final String pStatus, final MailDelegateEnum pRequest, final Locale pLocale)
      throws MailDelegateException
  {
    switch (pRequest)
    {
      case PLUGIN_STOP_REQUEST:
        try
        {
          final String subject = getMailMessage(MailMessage.REQUEST_STOP_SUBJECT, pLocale);
          final String body = getMailMessage(MailMessage.REQUEST_STOP_BODY, pLocale);

          mailService.sendMail(pUserEmail, subject,
              formatPluginLifeCycleChangeMessage(body, pCategory, pType, pVersion, pStatus));
        }
        catch (final Exception e)
        {

          final String format = String.format(
              "an error occured while sending an email to request stop change on plugin (email=%s)",
              pUserEmail);
          log.error(format, e);
          throw new MailDelegateException(format, e);
        }
        break;
      case PLUGIN_UNINSTALL_REQUEST:
        try
        {
          final String subject = getMailMessage(MailMessage.REQUEST_UNINSTALL_SUBJECT, pLocale);
          final String body = getMailMessage(MailMessage.REQUEST_UNINSTALL_BODY, pLocale);

          mailService.sendMail(pUserEmail, subject,
              formatPluginLifeCycleChangeMessage(body, pCategory, pType, pVersion, pStatus));
        }
        catch (final Exception e)
        {

          final String format = String.format(
              "an error occured while sending an email to request uninstall change on plugin (email=%s)",
              pUserEmail);
          log.error(format, e);
          throw new MailDelegateException(format, e);
        }
        break;
      default:
        break;
    }

  }

  /**
   * {@inheritDoc}
   *
   * @throws MailDelegateException
   */
  @Override
  public void sendProjectApplicationRequest(final String pProjectId, final String pLogin, final String pCategory,
                                            final String pType, final String pVersion) throws MailDelegateException
  {
    // send an email to the administrator to inform him about the project application request
    final User   superAdmin = getSuperAdmin();
    final Locale locale     = superAdmin.getLanguage().getLocale();
    try
    {
      final String subject = getMailMessage(MailMessage.REQUEST_APPLICATION_SUBJECT, locale);
      final String body = getMailMessage(MailMessage.REQUEST_APPLICATION_BODY, locale);
      mailService.sendMail(superAdmin.getEmail(), subject, formatProjectApplicationRequestMessage(body, pProjectId,
                                                                                                  pCategory, pType,
                                                                                                  pVersion, pLogin));
    }
    catch (final MailServiceException e)
    {
      log.error(MessageFormat
                    .format("an error occured while sending an email to inform administrator for project application request (project={0}, administrator email={1})",
                            pProjectId, superAdmin.getEmail()), e);
      throw new MailDelegateException(ExceptionCode.ERR_CREATE_PROJECT_SENDING_MAIL_ADMINISTRATOR,
                                      String.format("projectId=%s", pProjectId), e);
    }
  }

  private String formatProjectApplicationRequestMessage(final String pMessage, final String pProjectId,
                                                        final String pPluginCategory, final String pPluginType,
                                                        final String pPluginVersion, final String pLogin)
  {
    String message = pMessage.replace(PORTAL_URL, getPortalUrl());
    message = message.replace(PROJECT_ID, pProjectId);
    message = message.replace(PLUGIN_CATEGORY, pPluginCategory);
    message = message.replace(PLUGIN_TYPE, pPluginType);
    message = message.replace(PLUGIN_VERSION, pPluginVersion);
    message = message.replace(USER_LOGIN, pLogin);
    return message;
  }

  @Override
  public void sendNewEmailAdminNotification(final String pLogin, final String pEmail, final Locale pLocale)
      throws MailDelegateException
  {
    // send an email to the administrator to notify the registration of a new email
    final User   superAdmin = getSuperAdmin();
    final Locale locale     = superAdmin.getLanguage().getLocale();
    try
    {
      final String subject = getMailMessage(MailMessage.NEW_EMAIL_ADMIN_NOTIFICATION_SUBJECT, locale);
      final String body = getMailMessage(MailMessage.NEW_EMAIL_ADMIN_NOTIFICATION_BODY, locale);
      mailService.sendMail(superAdmin.getEmail(), subject, formatNewEmailAdminNotificationMessage(body, pLogin,
                                                                                                  pEmail));
    }
    catch (final Exception e)
    {
      log.error(MessageFormat
                    .format("an error occured while sending an email to notify administrator for new email registration (email={0}, login={1}, administrator email={2})",
                            pLogin, pEmail, superAdmin.getEmail()), e);
      throw new MailDelegateException(ExceptionCode.ERR_NEW_EMAIL_ADMIN_NOTIFICATION,
                                      String.format("login=%s, email=%s, administrator email=%s", pLogin, pEmail,
                                                    superAdmin.getEmail()), e);
    }
  }

  private String formatNewEmailAdminNotificationMessage(final String pMessage, final String pLogin, final String pEmail)
  {
    String message = pMessage.replace(USER_LOGIN, pLogin);
    message = message.replace(USER_EMAIL, pEmail);
    message = message.replace(PORTAL_URL, getPortalUrl());
    return message;
  }

  private String formatPluginLifeCycleChangeMessage(final String pMessage, final String pCategory,
      final String pType, final String pVersion, final String pStatus)
  {
    String message = pMessage.replace(PORTAL_URL, getPortalUrl());
    message = message.replace(PLUGIN_CATEGORY, pCategory);
    message = message.replace(PLUGIN_TYPE, pType);
    message = message.replace(PLUGIN_STATUS, pStatus);
    message = message.replace(PLUGIN_VERSION, pVersion);
    return message;
  }

  private String formatNewUserAccountMessage(final String pMessage, final String pLogin)
  {
    String message = pMessage.replace(USER_LOGIN, pLogin);
    message = message.replace(PORTAL_URL, getPortalUrl());
    return message;
  }

  private String formatMembershipRequestMessage(final String pMessage, final Project pProject, final String pLogin,
                                                final Set<String> pRoles, final String pAdditionalMessage,
                                                final Locale pLocale)
  {
    String message = pMessage.replace(PORTAL_URL, getPortalUrl());
    message = message.replace(USER_LOGIN, pLogin);
    message = message.replace(PROJECT_ID, pProject.getProjectId());
    message = message.replace(PROJECT_NAME, pProject.getName());
    message = message.replace(ROLES_NAME, getAllRoleNames(pRoles, pLocale));
    if (pAdditionalMessage != null)
    {
      message = message.replace(MESSAGE, pAdditionalMessage);
    }
    return message;
  }

  private String formatRejectProjectMessage(final String pMessage, final Project pProject,
      final String pReason)
  {
    String message = pMessage.replace(PORTAL_URL, getPortalUrl());
    message = message.replace(USER_LOGIN, pProject.getAuthor());
    message = message.replace(PROJECT_ID, pProject.getProjectId());
    message = message.replace(PROJECT_NAME, pProject.getName());
    message = message.replace(REJECT_REASON, pReason);
    return message;
  }

  private String formatMembershipMessage(final String pMessage, final Project pProject, final User pUser,
      final Set<Role> pRoles, final Locale pLocale)
  {
    String message = pMessage.replace(PORTAL_URL, getPortalUrl());
    message = message.replace(USER_LOGIN, pUser.getLogin());
    message = message.replace(PROJECT_ID, pProject.getProjectId());
    message = message.replace(PROJECT_NAME, pProject.getName());
    message = message.replace(ROLES_NAME, getAllRoles(pRoles, pLocale));
    return message;
  }

  private String getAllRoles(final Set<Role> pRoles, final Locale pLocale)
  {
    final Set<String> roleNames = new HashSet<String>();
    if (pRoles != null)
    {
      for (final Role role : pRoles)
      {
        roleNames.add(role.getName());
      }
    }
    return getAllRoleNames(roleNames, pLocale);
  }

  private String getAllRoleNames(final Set<String> pRoles, final Locale pLocale)
  {
    final StringBuilder allRoles = new StringBuilder("<br />");
    if ((pRoles != null) && (!pRoles.isEmpty()))
    {
      for (final String role : pRoles)
      {
        allRoles.append("- ").append(role).append("<br />");
      }
    }
    else
    {
      final String body = getMailMessage(MailMessage.NO_MORE_MEMBERSHIPS_LABEL, pLocale);
      allRoles.append(body);
    }
    return allRoles.toString();
  }

  private String formatUserMessage(final String pMessage, final String pToken)
  {
    // build the password recovery link
    String forgeUrl = forgeConfigurationService.getPublicUrl().toString();
    if (!forgeUrl.endsWith(URL_SEPARATOR))
    {
      forgeUrl = forgeUrl.concat(URL_SEPARATOR);
    }

    return pMessage.replace(PASSWORD_RECOVERY_LINK, forgeUrl + RECOVERY_ALIAS + URL_SEPARATOR + pToken);
  }

  /**
   * Use by container to inject {@link MailService}
   * 
   * @param pMailService
   *          the mailService to set
   */
  public void setMailService(final MailService pMailService)
  {
    mailService = pMailService;
  }

  /**
   * Use by container to inject {@link ForgeConfigurationService}
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * Use by container to inject {@link UserDAO}
   * 
   * @param pUserDAO
   *          the userDAO to set
   */
  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }
}
