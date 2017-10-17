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
package org.novaforge.forge.core.organization.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.exceptions.ForgeException;
import org.novaforge.forge.commons.technical.validation.ValidationService;
import org.novaforge.forge.commons.technical.validation.ValidatorResponse;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.dao.ProjectDAO;
import org.novaforge.forge.core.organization.dao.UserDAO;
import org.novaforge.forge.core.organization.delegates.MailDelegate;
import org.novaforge.forge.core.organization.delegates.MessageDelegate;
import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.MailDelegateException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.Attribute;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.GroupInfo;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.RecoveryPassword;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.UserStatus;
import org.novaforge.forge.core.organization.services.ApplicationService;
import org.novaforge.forge.core.organization.services.CommonUserService;
import org.novaforge.forge.core.organization.services.GroupService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.plugins.domain.route.PluginQueueAction;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;

import javax.persistence.NoResultException;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Implementation of {@link CommonUserService}
 * 
 * @author sbenoist
 * @see CommonUserService
 */
public class CommonUserServiceImpl implements CommonUserService
{
  private static final Log log             = LogFactory.getLog(CommonUserServiceImpl.class);
  private static final int LOGIN_MAXLENGTH = 30;
  /**
   * Reference to {@link UserDAO} service injected by the container
   */
  private UserDAO                   userDAO;
  /**
   * Reference to {@link MembershipDAO} service injected by the container
   */
  private MembershipDAO             membershipDAO;
  /**
   * Reference to {@link ProjectDAO} service injected by the container
   */
  private ProjectDAO                projectDAO;
  /**
   * Reference to {@link MailDelegate} service injected by the container
   */
  private MailDelegate              mailDelegate;
  /**
   * Reference to {@link MessageDelegate} service injected by the container
   */
  private MessageDelegate           messageDelegate;
  /**
   * Reference to {@link ValidationService} service injected by the container
   */
  private ValidationService         validationService;
  /**
   * Reference to {@link ForgeCfgService} service injected by the
   * container
   */
  private ForgeConfigurationService forgeConfigurationService;
  /**
   * Reference to {@link ProjectService} service injected by the
   * container
   */
  private ProjectService            projectService;
  /**
   * Reference to {@link ApplicationService} service injected by the container
   */
  private ApplicationService        applicationService;
  /**
   * Reference to {@link PluginsManager} service injected by the container
   */
  private PluginsManager            pluginsManager;
  /**
   * Reference to {@link SecurityDelegate} service injected by the
   * container
   */
  private SecurityDelegate          securityDelegate;
  /**
   * Reference to {@link ForgeCfgService} service injected by the
   * container
   */
  private GroupService              groupService;

  /**
   * {@inheritDoc}
   */
  @Override
  public User newUser()
  {
    return userDAO.newUser();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existUser(final String pLogin) throws UserServiceException
  {
    try
    {
      return userDAO.existLogin(pLogin);
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createUser(final User pUser) throws UserServiceException
  {
    try
    {
      // Generate the user login
      if (forgeConfigurationService.isUserLoginGenerated())
      {
        final String generatedUserLogin = generateUserLogin(pUser);
        pUser.setLogin(generatedUserLogin);
      }
      else
      // check forbidden logins for non generated logins
      {
        final List<String> forbiddenLogins = forgeConfigurationService.getForbiddenLogins();
        if (forbiddenLogins.contains(pUser.getLogin()))
        {
          throw new UserServiceException(ExceptionCode.ERR_CREATE_USER_FORBIDDEN_LOGIN,
              "this login is part of forbidden login's list");
        }
      }

      // Check the informations about the user
      checkUser(pUser);

      // the status is not directly activated in case of superusers creation
      pUser.setStatus(UserStatus.TO_BE_ACTIVATED);

      // persist the user and create associated UserProfile
      createProfileForUser(pUser);

      // add the user to Forge and Referentiel Projects
      addSharedProjectsMembership(pUser.getUuid(), RealmType.USER);

      final Locale locale = pUser.getLanguage().getLocale();
      mailDelegate.sendNewUserAccount(pUser, locale);

      // notify administration for new email registration
      mailDelegate.sendNewEmailAdminNotification(pUser.getLogin(), pUser.getEmail(), locale);
    }
    catch (final MailDelegateException e)
    {
      if (e.getCode().equals(ExceptionCode.ERR_NEW_EMAIL_ADMIN_NOTIFICATION))
      {
        log.error(String.format(
            "unable to send email to notify admin for new email with login=%s and email=%s",
            pUser.getLogin(), pUser.getEmail()), e);
      }
      else if (e.getCode().equals(ExceptionCode.ERR_NEW_ACCOUNT_SENDING_MAIL))
      {
        log.error(
            String.format("unable to send email for new user account with login=%s and email=%s",
                pUser.getLogin(), pUser.getEmail()), e);
      }
    }
    catch (final Exception e)
    {
      if (e instanceof UserServiceException)
      {
        throw (UserServiceException) e;
      }
      else
      {
        throw new UserServiceException(String.format(
            "a technical error occured during user creation with [login=%s]", pUser.getLogin()), e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSuperUser(final User pUser) throws UserServiceException
  {
    try
    {
      // Check the informations about the user
      checkUser(pUser);

      // the status is directly activated in case of superusers creation
      pUser.setStatus(UserStatus.TO_BE_ACTIVATED);
      pUser.setRealmType(RealmType.SYSTEM);

      // persist and create associated UserProfile
      createProfileForUser(pUser);

      // add the user to Forge and Referentiel Projects
      addSharedProjectsMembership(pUser.getUuid(), RealmType.SYSTEM);
    }
    catch (final Exception e)
    {
      throw new UserServiceException(String.format(
          "a technical error occured during superuser creation with [login=%s]", pUser.getLogin()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser(final String pLogin) throws UserServiceException
  {
    try
    {
      return userDAO.findByLogin(pLogin);
    }
    catch (final NoResultException e)
    {
      throw new UserServiceException(String.format("no user found with [login=%s]", pLogin), e);
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBlacklistedUser(final String pLogin) throws UserServiceException
  {
    boolean ret = false;

    try
    {
      ret = userDAO.findBlacklistedByLogin(pLogin) != null;
    }
    catch (final NoResultException e)
    {
      // no exception propagated
      // the user isn't blacklisted
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }

    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUserCredential(final String pLogin) throws UserServiceException
  {
    try
    {
      return userDAO.findPassword(pLogin);
    }
    catch (final NoResultException e)
    {
      throw new UserServiceException(String.format("Unable to find any credential for the given login : %s",
          pLogin), e);
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteUser(final String pLogin) throws UserServiceException
  {
    try
    {
      final User user = getUser(pLogin);

      final ProjectOptions projectOptions = projectService.newProjectOptions();
      final Set<String> userProjects = projectService.getProjectsId(pLogin, projectOptions);

      // If the user is the author of one or more projects, the ownership is
      // updated to the
      // logged in user hence de forge administrator.
      updateProjectsOwnership(user, userProjects);

      // remove the user from all its groups
      deleteUserFromGroups(user);

      // Delete all the memberships of the user
      deleteUserMemberships(user, userProjects);

      // Delete all the memberships requestof the user
      deleteUserMemberships(user);

      // Delete all the recovery passwords done by the user
      deleteUserRecoveryPassword(user.getEmail());
      
      // Delete UserProfile associated and user
      final UserProfile userProfile = userDAO.findProfileByLogin(pLogin);
      userDAO.delete(userProfile);

      // Add the login to the blacklist
      final BlacklistedUser blacklistedUser = userDAO.newBlacklistedUser();
      blacklistedUser.setLogin(user.getLogin());
      blacklistedUser.setEmail(user.getEmail());
      userDAO.persist(blacklistedUser);

      // propagate the delete for all the applications of all the projects
      deleteUserFromApplications(user, userProjects);
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  private void updateProjectsOwnership(final User user, final Set<String> userProjects)
      throws UserServiceException
  {
    final String loggedInUser = securityDelegate.getCurrentUser();
    for (final String projectId : userProjects)
    {
      final Project currentProject = projectDAO.findByProjectId(projectId);
      if (user.getLogin().equals(currentProject.getAuthor()))
      {
        currentProject.setAuthor(loggedInUser);
        projectDAO.update(currentProject);
      }
    }
  }

  private void deleteUserFromGroups(final User user) throws GroupServiceException
  {
    final List<GroupInfo> groupsInfos = groupService.getAllUserGroups(user.getUuid());
    if ((groupsInfos != null) && (groupsInfos.size() > 0))
    {
      for (final GroupInfo groupInfo : groupsInfos)
      {
        final Group group = groupInfo.getGroup();
        group.removeUser(user);
        groupService.updateGroup(groupInfo.getProjectId(), group.getName(), group,
            forgeConfigurationService.getSuperAdministratorLogin());
      }
    }
  }

  private void deleteUserMemberships(final User user, final Set<String> userProjects)
      throws UserServiceException
  {
    for (final String projectId : userProjects)
    {
      final List<Membership> memberships = membershipDAO.findByProjectAndActor(projectId, user.getUuid());
      for (final Membership membership : memberships)
      {
        membershipDAO.delete(membership);
      }
    }
  }

  private void deleteUserMemberships(final User user) throws UserServiceException
  {
    final List<MembershipRequest> membershipsRequest = membershipDAO.findAllRequestByUser(user.getLogin());
    for (final MembershipRequest request : membershipsRequest)
    {
      membershipDAO.delete(request);
    }
  }

  private void deleteUserFromApplications(final User user, final Set<String> userProjects)
      throws UserServiceException
  {
    /**
     * Nowadays, the plugin is responsible for propagation. We need to send
     * multiple messages to reach action. It is a way to propagate the event
     * naughty because we put a lot of business code here ... To avoid send a
     * message to the same tool instance, we check this one before with the
     * PluginService. see later if we can improve it.
     */
    try
    {
      final Set<UUID> toolsUUID = new HashSet<UUID>();

      for (final String projectId : userProjects)
      {
        final List<ProjectApplication> userApplications = applicationService
            .getAllProjectApplications(projectId);

        for (final ProjectApplication userApplication : userApplications)
        {
          try
          {
            if (ApplicationStatus.ACTIVE.equals(userApplication.getStatus()))
            {
              final PluginService pluginService = pluginsManager.getPluginService(userApplication
                  .getPluginUUID().toString());
              final UUID toolUUID = pluginService.getToolInstanceProvisioningService()
                  .getToolInstanceByApplication(userApplication.getPluginInstanceUUID().toString()).getUUID();
              if (!toolsUUID.contains(toolUUID))
              {
                messageDelegate
                    .sendUserMessage(userApplication.getPluginUUID().toString(), userApplication
                        .getPluginInstanceUUID().toString(), projectId, user, PluginQueueAction.DELETE
                        .getLabel());
                toolsUUID.add(toolUUID);
              }
            }
          }
          catch (final Exception e)
          {
            log.error(
                String.format("unable to propagate delete of user with login=%s for all his applications",
                    user.getLogin()), e);
          }
        }
      }
    }
    catch (final ForgeException e)
    {
      throw new UserServiceException(String.format(
          "unable to propagate delete of user with login=%s for all his applications", user.getLogin()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BlacklistedUser> getBlacklistedUsers() throws UserServiceException
  {
    try
    {
      return userDAO.findAllBlacklisted();
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void activateUser(final String pLogin, final String pAuthor) throws UserServiceException
  {
    try
    {
      changeUserStatus(UserStatus.ACTIVATED, pLogin);
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void desactivateUser(final String pLogin, final String pAuthor) throws UserServiceException
  {
    changeUserStatus(UserStatus.DESACTIVATED, pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<User> getAllUsers(final boolean pWithSystem) throws UserServiceException
  {
    try
    {
      final List<User> returnUsers = new ArrayList<User>();
      final List<User> allUsers = userDAO.findAllUser();
      for (final User user : allUsers)
      {
        if ((!RealmType.SYSTEM.equals(user.getRealmType()))
            || (RealmType.SYSTEM.equals(user.getRealmType()) == pWithSystem))
        {
          returnUsers.add(user);
        }
      }
      return returnUsers;
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UserProfile> getAllUserProfiles(final boolean pWithSystem) throws UserServiceException
  {
    try
    {
      final List<UserProfile> returnUserProfiles = new ArrayList<UserProfile>();
      final List<UserProfile> allUserProfiles = userDAO.findAllUserProfile();
      for (final UserProfile userProfile : allUserProfiles)
      {
        if ((!RealmType.SYSTEM.equals(userProfile.getUser().getRealmType()))
            || (RealmType.SYSTEM.equals(userProfile.getUser().getRealmType()) == pWithSystem))
        {
          returnUserProfiles.add(userProfile);
        }
      }
      return returnUserProfiles;
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<User> searchUsersByCriterias(final Map<String, Object> pLikeCriterias,
      final Map<String, Object> pEqualCriterias) throws UserServiceException
  {
    try
    {
      return userDAO.searchUserByCriterias(pLikeCriterias, pEqualCriterias);
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void recoverUserPassword(final String pUserEmail) throws UserServiceException
  {
    User user = null;
    try
    {
      // find the user by his email
      user = userDAO.findByEmail(pUserEmail);

      // Create a recovery password
      final RecoveryPassword recoveryPassword = userDAO.newRecoveryPassword();
      recoveryPassword.setUser(user);
      userDAO.persist(recoveryPassword);

      // send an email with the recovery password token url
      final Locale locale = user.getLanguage().getLocale();
      mailDelegate.sendUserPasswordRecovery(user, recoveryPassword.getToken().toString(), locale);
    }
    catch (final NoResultException e)
    {
      throw new UserServiceException(ExceptionCode.ERR_RECOVER_PASSWORD_EMAIL_UNKNOWN, String.format(
          "email=%s", pUserEmail), e);
    }
    catch (final MailDelegateException e)
    {
      throw new UserServiceException(String.format(
          "unable to send email for password recovery to user with login=%s and email=%s", user.getLogin(),
          user.getEmail()), e);
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteUserRecoveryPassword(String pUserEmail) throws UserServiceException
  {
    User user = null;
    boolean isDeleted = false;
    try
    {
      // find the user by its email
      user = userDAO.findByEmail(pUserEmail);
      
      final List <RecoveryPassword> recoveryPasswordList = userDAO.findByUser(user);
      for (final RecoveryPassword recoveryPassword : recoveryPasswordList)
      {
        
        if (recoveryPassword.isEnabled() == true){
             userDAO.delete(recoveryPassword);
             isDeleted = true;
        }
      }
      return isDeleted;
    }
    catch (final NoResultException e)
    {
      throw new UserServiceException(ExceptionCode.ERR_RECOVER_PASSWORD_EMAIL_UNKNOWN, String.format(
          "email=%s", pUserEmail), e);
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCandidateForUpdatingPassword(final String pLogin) throws UserServiceException
  {
    boolean is;
    try
    {
      is = false;
      final int changePasswordTime = forgeConfigurationService.getPasswordModificationTime();
      final int passwordLifeTime = forgeConfigurationService.getPasswordLifeTime();
      if ((changePasswordTime != 0) && (passwordLifeTime != 0)
          && ((passwordLifeTime - changePasswordTime) > 0))
      {
        final User user = getUser(pLogin);
        Date lastPasswordUpdated = user.getLastPasswordUpdated();
        // If user has never updated its password use its created date
        if (lastPasswordUpdated == null)
        {
          lastPasswordUpdated = user.getCreated();
        }
        final Calendar changePasswordCalendar = new GregorianCalendar();
        changePasswordCalendar.setTime(lastPasswordUpdated);
        changePasswordCalendar.add(Calendar.HOUR_OF_DAY, passwordLifeTime - changePasswordTime);
        if (changePasswordCalendar.before(new GregorianCalendar()))
        {
          is = true;
        }

      }
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }

    return is;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void checkUser(final User pUser) throws UserServiceException
  {
    // validate the bean
    final ValidatorResponse response = validationService.validate(User.class, pUser);
    if (!response.isValid())
    {
      log.error(ExceptionCode.ERR_VALIDATION_BEAN.toString() + " : " + response.getMessage());
      throw new UserServiceException(ExceptionCode.ERR_VALIDATION_BEAN, response.getMessage());
    }

    // check that the login is unique
    if (!checkLogin(pUser.getLogin()))
    {
      throw new UserServiceException(ExceptionCode.ERR_CREATE_USER_LOGIN_ALREADY_EXISTS, String.format(
          "login=%s", pUser.getLogin()));
    }

    // check that the email is unique
    if (userDAO.existEmail(pUser.getEmail()))
    {
      throw new UserServiceException(ExceptionCode.ERR_CREATE_USER_EMAIL_ALREADY_EXISTS, String.format(
          "login=%s, email=%s", pUser.getLogin(), pUser.getEmail()));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUserFromRecoveryPasswordToken(final String pToken) throws UserServiceException
  {
    Date             now;
    RecoveryPassword recoveryPassword;
    try
    {
      now = new Date();
      recoveryPassword = userDAO.findByToken(UUID.fromString(pToken));
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }

    if (!recoveryPassword.isEnabled())
    {
      throw new UserServiceException(ExceptionCode.ERR_RECOVER_PASSWORD_TOKEN_DISABLED,
          "the token is disabled");
    }
    else if (recoveryPassword.getExpirationDate().before(now))
    {
      throw new UserServiceException(ExceptionCode.ERR_RECOVER_PASSWORD_TOKEN_EXPIRED, "the token is expired");
    }

    return recoveryPassword.getUser();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePasswordFromRecovery(final String pToken, final String pPassword)
      throws UserServiceException
  {
    try
    {
      // update the password
      final User user = getUserFromRecoveryPasswordToken(pToken);
      user.setPassword(pPassword);
      user.setLastPasswordUpdated(new Date());
      // merge the user
      userDAO.update(user);

      // propagate the modificaiton of the user on projects and applications
      sendUpdateMessage(user);

      // find and delete the token
      final RecoveryPassword recoveryPassword = userDAO.findByToken(UUID.fromString(pToken));
      userDAO.delete(recoveryPassword);
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePassword(final String pLogin, final String pPassword) throws UserServiceException
  {
    // update the password
    final User user = userDAO.findByLogin(pLogin);
    user.setPassword(pPassword);
    user.setLastPasswordUpdated(new Date());
    // merge the user
    userDAO.update(user);
    // propagate the modificaiton of the user on projects and applications
    sendUpdateMessage(user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile getUserProfile(final String pLogin) throws UserServiceException
  {
    UserProfile returnProfile;
    try
    {
      returnProfile = userDAO.findProfileByLogin(pLogin);
    }
    catch (final NoResultException e)
    {
      if (userDAO.existLogin(pLogin))
      {
        final User user = userDAO.findByLogin(pLogin);
        returnProfile = createProfileForUser(user);
      }
      else
      {
        throw new UserServiceException(String.format(
            "Unable to retrieve or create profile for the user [login=%s] because he doesnt exist.", pLogin),
            e);
      }
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
    return returnProfile;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile getUserProfileFromEmail(final String pEmail) throws UserServiceException
  {
    UserProfile returnProfile;
    try
    {
      returnProfile = userDAO.findProfileByEmail(pEmail);
    }
    catch (final NoResultException e)
    {
      if (userDAO.existEmail(pEmail))
      {
        final User user = userDAO.findByEmail(pEmail);
        returnProfile = createProfileForUser(user);
      }
      else
      {
        throw new UserServiceException(String.format(
            "Unable to retrieve or create profile for the user [email=%s] because he doesnt exist.", pEmail),
            e);
      }
    }
    catch (final Exception e)
    {
      throw new UserServiceException("a technical error occurred", e);
    }
    return returnProfile;
  }

  /**
   * {@inheritDoc}
   *
   * @throws UserServiceException
   */
  @Override
  public UserProfile updateUserProfile(final UserProfile pUserProfile) throws UserServiceException
  {
    // validate the bean
    final ValidatorResponse response = validationService.validate(User.class, pUserProfile.getUser());
    if (!response.isValid())
    {
      log.error(ExceptionCode.ERR_VALIDATION_BEAN.toString() + " : " + response.getMessage());
      throw new UserServiceException(ExceptionCode.ERR_VALIDATION_BEAN, response.getMessage());
    }

    // validate a new email
    final User user = getUser(pUserProfile.getUser().getLogin());
    if (!user.getEmail().equals(pUserProfile.getUser().getEmail()))
    {
      // check that the email is unique
      if (userDAO.existEmail(pUserProfile.getUser().getEmail()))
      {
        throw new UserServiceException(ExceptionCode.ERR_CREATE_USER_EMAIL_ALREADY_EXISTS,
                                       String.format("login=%s, email=%s", pUserProfile.getUser().getLogin(),
                                                     pUserProfile.getUser().getEmail()));
      }

      // notify administration for new email registration
      final Locale locale = pUserProfile.getUser().getLanguage().getLocale();
      try
      {
        mailDelegate.sendNewEmailAdminNotification(user.getLogin(), pUserProfile.getUser().getEmail(), locale);
      }
      catch (final MailDelegateException e)
      {
        log.error(String.format("unable to send email to notify admin for new email with login=%s and email=%s",
                                user.getLogin(), pUserProfile.getUser().getEmail()), e);
      }
      catch (final Exception e)
      {
        if (e instanceof UserServiceException)
        {
          throw (UserServiceException) e;
        }
        else
        {
          throw new UserServiceException(String.format("a technical error occured during user creation with [login=%s]",
                                                       user.getLogin()), e);
        }
      }
    }

    // check if the password is updated
    if (!user.getPassword().equals(pUserProfile.getUser().getPassword()))
    {
      user.setLastPasswordUpdated(new Date());
    }

    // merge the userprofile
    final UserProfile userProfileMerged = userDAO.update(pUserProfile);

    // propagate the modification of the user on projects and applications
    sendUpdateMessage(userProfileMerged.getUser());
    return userProfileMerged;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BinaryFile newUserIcon()
  {
    return userDAO.newUserIcon();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileContact newUserProfileContact()
  {
    return userDAO.newUserProfileContact();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileWork newUserProfileWork()
  {
    return userDAO.newUserProfileWork();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute newUserProfileProjects()
  {
    return userDAO.newUserProfileProjects();
  }

  private void sendUpdateMessage(final User pUser) throws UserServiceException
  {
    try
    {
      // get all the applications
      final ProjectOptions projectOptions = projectService.newProjectOptions();
      final Set<String> findProjectsIdByUser = projectService.getProjectsId(pUser.getLogin(), projectOptions);
      for (final String projectId : findProjectsIdByUser)
      {
        final List<ProjectApplication> userApplications = applicationService.getAllProjectApplications(projectId);
        // send a message for each application
        for (final ProjectApplication userApplication : userApplications)
        {
          if (ApplicationStatus.ACTIVE.equals(userApplication.getStatus()))
          {
            messageDelegate.sendUserMessage(userApplication.getPluginUUID().toString(),
                                            userApplication.getPluginInstanceUUID().toString(), projectId, pUser,
                                            PluginQueueAction.UPDATE.getLabel());
          }
        }
      }
    }
    catch (final Exception e)
    {
      throw new UserServiceException(String
                                         .format("unable to propagate update of user with login=%s for all his applications",
                                                 pUser.getLogin()), e);
    }
  }

  private void changeUserStatus(final UserStatus pNewStatus, final String pLogin) throws UserServiceException
  {
    final User user = getUser(pLogin);
    if (pNewStatus.equals(user.getStatus()))
    {
      throw new UserServiceException(ExceptionCode.ERR_CHANGE_USER_STATUS_STATUS_ALREADY_CHANGED,
                                     String.format("login=%s, new status=%s", pLogin, pNewStatus.getLabel()));

    }

    user.setStatus(pNewStatus);
    userDAO.update(user);
  }

  /**
   * Create a UserProfile for given User
   *
   * @param pUser
   *     The user to associate with new UserProfile
   */
  private UserProfile createProfileForUser(final User pUser)
  {
    final UserProfile userProfile = userDAO.newUserProfile(pUser);
    userDAO.persist(userProfile);
    return userProfile;
  }

  private void addSharedProjectsMembership(final UUID pUserUUID, final RealmType pRealmType)
  {
    try
    {
      // add the user to Forge
      messageDelegate.sendSharedProjectMessage(forgeConfigurationService.getForgeProjectId(), pUserUUID, pRealmType);

      // add the user to Referentiel Projects
      if (forgeConfigurationService.isReferentielCreated())
      {
        messageDelegate.sendSharedProjectMessage(forgeConfigurationService.getReferentielProjectId(), pUserUUID,
                                                 pRealmType);
      }
    }
    catch (final Exception e)
    {
      log.error(String
                    .format("an error occured during sending message to add membership to transverse projects for user's uuid [%s]",
                            pUserUUID), e);
    }
  }

  private boolean checkLogin(final String login) throws UserServiceException
  {
    return !existUser(login) && !isBlacklistedUser(login);
  }

  private String generateUserLogin(final User user) throws UserServiceException
  {
    int firstNameNbLetters = 1;

    final String firstName = user.getFirstName().toLowerCase();
    final String lastName  = user.getName().toLowerCase();

    final StringBuilder loginBuilder = new StringBuilder();
    loginBuilder.append(normalize(lastName));
    loginBuilder.append("-");
    loginBuilder.append(normalize(firstName.substring(0, firstNameNbLetters)));

    while (!checkLogin(loginBuilder.toString()) && (firstNameNbLetters < firstName.length()))
    {
      loginBuilder.append(normalize(firstName.substring(firstNameNbLetters, firstNameNbLetters + 1)));
      firstNameNbLetters++;
    }

    if (!checkLogin(loginBuilder.toString()))
    {
      int prefixDigit = 1;
      loginBuilder.append("-");
      loginBuilder.append(prefixDigit);

      while (!checkLogin(loginBuilder.toString()))
      {
        loginBuilder.replace(loginBuilder.lastIndexOf("-") + 1, loginBuilder.length(), String.valueOf(prefixDigit + 1));
        prefixDigit++;
      }
    }

    // check the login maxlength
    if (loginBuilder.length() > LOGIN_MAXLENGTH)
    {
      final String suffix = loginBuilder.substring(loginBuilder.lastIndexOf("-"), loginBuilder.length());
      int cutLastname = LOGIN_MAXLENGTH - suffix.length();
      loginBuilder.delete(cutLastname, loginBuilder.lastIndexOf("-"));
      while (!checkLogin(loginBuilder.toString()))
      {
        cutLastname--;
        loginBuilder.delete(cutLastname, loginBuilder.lastIndexOf("-"));
      }
    }

    return loginBuilder.toString();
  }

  /**
   * Will replace all specials caracters and spaces
   *
   * @param pSource
   *     source string
   *
   * @return normalize string
   */
  private String normalize(final String pSource)
  {
    final String normalized = Normalizer.normalize(pSource, Normalizer.Form.NFD);

    return normalized.replaceAll("[^\\w]", "").toLowerCase();
  }

  /**
   * Use by container to inject {@link UserDAO} implementation
   * 
   * @param pUserDAO
   *          the applicuserDAOationHandler to set
   */
  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }

  /**
   * Use by container to inject {@link MembershipDAO} implementation
   * 
   * @param pMembershipDAO
   *          the membershipDAO to set
   */
  public void setMembershipDAO(final MembershipDAO pMembershipDAO)
  {
    membershipDAO = pMembershipDAO;
  }

  /**
   * Use by container to inject {@link ProjectService} implementation
   * 
   * @param pProjectService
   *          the projectService to set
   */
  public void setProjectService(final ProjectService pProjectService)
  {
    projectService = pProjectService;
  }

  /**
   * Use by container to inject {@link ProjectDAO} implementation
   * 
   * @param pProjectDAO
   *          the projectDAO to set
   */
  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  /**
   * Use by container to inject {@link MailDelegate} implementation
   * 
   * @param pMailDelegate
   *          the mailDelegate to set
   */
  public void setMailDelegate(final MailDelegate pMailDelegate)
  {
    mailDelegate = pMailDelegate;
  }

  /**
   * Use by container to inject {@link MessageDelegate} implementation
   * 
   * @param pMessageDelegate
   *          the messageDelegate to set
   */
  public void setMessageDelegate(final MessageDelegate pMessageDelegate)
  {
    messageDelegate = pMessageDelegate;
  }

  /**
   * Use by container to inject {@link ValidationService} implementation
   * 
   * @param pValidationService
   *          the validationService to set
   */
  public void setValidationService(final ValidationService pValidationService)
  {
    validationService = pValidationService;
  }

  /**
   * Use by container to inject {@link ForgeCfgService} implementation
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * Use by container to inject {@link ApplicationService} implementation
   * 
   * @param pApplicationService
   *          the applicationService to set
   */
  public void setApplicationService(final ApplicationService pApplicationService)
  {
    applicationService = pApplicationService;
  }

  /**
   * Use by container to inject {@link PluginsManager} implementation
   * 
   * @param pPluginsManager
   *          the pluginsManager to set
   */
  public void setPluginsManager(final PluginsManager pPluginsManager)
  {
    pluginsManager = pPluginsManager;
  }

  /**
   * Use by container to inject {@link SecurityDelegate} implementation
   * 
   * @param pSecurityDelegate
   *          the securityDelegate to set
   */
  public void setSecurityDelegate(final SecurityDelegate pSecurityDelegate)
  {
    securityDelegate = pSecurityDelegate;
  }

  /**
   * @param groupService
   *          the groupService to set
   */
  public void setGroupService(final GroupService groupService)
  {
    this.groupService = groupService;
  }

}
