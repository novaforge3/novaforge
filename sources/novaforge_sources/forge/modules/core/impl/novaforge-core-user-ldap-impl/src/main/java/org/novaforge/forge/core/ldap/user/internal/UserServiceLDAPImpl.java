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
package org.novaforge.forge.core.ldap.user.internal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.ldap.exceptions.LdapException;
import org.novaforge.forge.commons.ldap.exceptions.LdapExceptionCode;
import org.novaforge.forge.commons.ldap.model.LdapUser;
import org.novaforge.forge.commons.ldap.services.LdapService;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Attribute;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;
import org.novaforge.forge.core.organization.services.CommonUserService;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.novaforge.forge.core.organization.services.UserService;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import java.util.List;
import java.util.Map;

/**
 * @author pettreto
 */
public class UserServiceLDAPImpl implements UserService
{
  private static final Log LOGGER = LogFactory.getLog(UserServiceLDAPImpl.class);
  private CommonUserService         commonUserService;
  private LdapService               ldapService;
  private AuthentificationService   authentificationService;
  private ForgeConfigurationService forgeConfigurationService;
  private LanguageService           languageService;

  @Override
  public User newUser()
  {
    if (ldapService.getAuthorizeCreateJpaAccess())
    {
      return commonUserService.newUser();
    }
    else
    {
      return null;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createUser(final User pUser) throws UserServiceException
  {
    if (ldapService.getAuthorizeCreateJpaAccess())
    {
      commonUserService.createUser(pUser);
    }
    else
    {
      throw new UserServiceException(ExceptionCode.ERR_METHOD_NOT_ALLOWED_FOR_LDAP, String.format(
          "Add user with [login=%s] to forge project is not allowed for LDAP", pUser.getLogin()));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSuperUser(final User pUser) throws UserServiceException
  {
    commonUserService.createSuperUser(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser(final String pLogin) throws UserServiceException
  {
    return commonUserService.getUser(pLogin);
  }

  @Override
  public User getUser(final String pLogin, final String pPasswd) throws UserServiceException
  {
    LdapUser ldapUser = null;
    User user = null;
    boolean authorizationJpaAccess = false;

    LOGGER.info(String.format("LDAP authentification : login=%s", pLogin));

    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug(String.format("Input : login=%s", pLogin));
    }

    try
    {
      ldapUser = ldapService.getUser(pLogin, pPasswd);
      final String userPassword = authentificationService.hashPassword(pPasswd);
      ldapUser.setUserPassword(userPassword);

    }
    catch (final LdapException na)
    {
      LOGGER.info(String.format("LDAP authentification echec : codeError=%s", na.getCode()));
      LOGGER.info(String.format("LDAP authentification echec : authorizeLdapAndJpaAccess=%s",
          ldapService.getAuthorizeCreateJpaAccess()));

      if (((na.getCode() == LdapExceptionCode.LDAP_ACCESS_ERROR) && (ldapService
          .getAuthorizeJpaAccessWhenLdapDown() || ldapService.getAuthorizeCreateJpaAccess()))
          || (((na.getCode() == LdapExceptionCode.LDAP_AUTHENTIFICATION_ERROR) || (na.getCode() == LdapExceptionCode.LDAP_USER_NOT_FOUND)) && ldapService
              .getAuthorizeCreateJpaAccess())
          || (pLogin.equals(forgeConfigurationService.getSuperAdministratorLogin())))
      {
        authorizationJpaAccess = true;
        user = commonUserService.getUser(pLogin);
      }
      if (user == null)
      {
        LOGGER.info("LDAP authentification user == null");
        throw new UserServiceException(ExceptionCode.ERR_SEARCH_LDAP, String.format(
            "Error when searching user with login=%s", pLogin), na);
      }
    }

    if (!authorizationJpaAccess)
    {
      // deploy or update user into novaforge database
      verifyJpaUser(ldapUser, pLogin, pPasswd);
      user = commonUserService.getUser(pLogin);
    }
    return (user);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isBlacklistedUser(final String login) throws UserServiceException
  {
    return commonUserService.isBlacklistedUser(login);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUserCredential(final String pLogin) throws UserServiceException
  {
    return commonUserService.getUserCredential(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteUser(final String pLogin) throws UserServiceException
  {
    if (ldapService.getAuthorizeCreateJpaAccess())
    {
      commonUserService.deleteUser(pLogin);
    }
    else
    {
      throw new UserServiceException(ExceptionCode.ERR_METHOD_NOT_ALLOWED_FOR_LDAP, String
                                                                                        .format("Delete user with [login=%s] to forge project is not allowed for LDAP",
                                                                                                pLogin));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void activateUser(final String login, final String author) throws UserServiceException
  {
    if (ldapService.getAuthorizeCreateJpaAccess())
    {
      commonUserService.activateUser(login, author);
    }
    else
    {
      throw new UserServiceException(ExceptionCode.ERR_METHOD_NOT_ALLOWED_FOR_LDAP, String
                                                                                        .format("Activate user with [login=%s] to forge project is not allowed for LDAP",
                                                                                                author));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void desactivateUser(final String login, final String author) throws UserServiceException
  {
    if (ldapService.getAuthorizeCreateJpaAccess())
    {
      commonUserService.desactivateUser(login, author);
    }
    else
    {
      throw new UserServiceException(ExceptionCode.ERR_METHOD_NOT_ALLOWED_FOR_LDAP, String
                                                                                        .format("Desctivate user with [login=%s] to forge project is not allowed for LDAP",
                                                                                                author));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<User> getAllUsers(final boolean pWithSystem) throws UserServiceException
  {
    return commonUserService.getAllUsers(pWithSystem);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<UserProfile> getAllUserProfiles(final boolean pWithSystem) throws UserServiceException
  {
    return commonUserService.getAllUserProfiles(pWithSystem);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BlacklistedUser> getAllBlacklistedUsers() throws UserServiceException
  {
    return commonUserService.getBlacklistedUsers();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<User> searchUsersByCriterias(final Map<String, Object> pLikeCriterias,
      final Map<String, Object> pEqualCriterias) throws UserServiceException
  {
    return commonUserService.searchUsersByCriterias(pLikeCriterias, pEqualCriterias);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void recoverUserPassword(final String pUserEmail) throws UserServiceException
  {
    throw new UserServiceException(ExceptionCode.ERR_METHOD_NOT_ALLOWED_FOR_LDAP, String.format(
        "Recover user with [login=%s] to forge project is not allowed for LDAP", pUserEmail));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCandidateForUpdatingPassword(final String pLogin) throws UserServiceException
  {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthentificationType() throws UserServiceException
  {
    return UserService.LDAP_USER_AUTHENTIFICATION;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizeCreateUser()
  {
    return ldapService.getAuthorizeCreateJpaAccess();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizeUpdateUser()
  {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizedToUpdateUserLanguage() throws UserServiceException
  {
    return ldapService.getAuthorizeToUpdateLanguage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUserFromRecoveryPasswordToken(final String pToken) throws UserServiceException
  {
    throw new UserServiceException(ExceptionCode.ERR_METHOD_NOT_ALLOWED_FOR_LDAP, String.format(
        "getUserFromRecoveryPasswordToken with [token=%s] is not allowed for LDAP", pToken));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePasswordFromRecovery(final String pToken, final String pPassword)
      throws UserServiceException
  {
    throw new UserServiceException(ExceptionCode.ERR_METHOD_NOT_ALLOWED_FOR_LDAP, String.format(
        "updatePasswordFromRecovery with [token=%s] is not allowed for LDAP", pToken));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePassword(final String pLogin, final String pPassword) throws UserServiceException
  {
    throw new UserServiceException(ExceptionCode.ERR_METHOD_NOT_ALLOWED_FOR_LDAP, String.format(
        "updatePassword with [login=%s] is not allowed for LDAP", pPassword));

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile getUserProfile(final String pLogin) throws UserServiceException
  {
    return commonUserService.getUserProfile(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile getUserProfileFromEmail(final String pEmail) throws UserServiceException
  {
    return commonUserService.getUserProfileFromEmail(pEmail);
  }

  /**
   * {@inheritDoc}
   *
   * @throws UserServiceException
   */
  @Override
  public UserProfile updateUserProfile(final UserProfile pUserProfile) throws UserServiceException
  {
    return commonUserService.updateUserProfile(pUserProfile);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BinaryFile newUserIcon()
  {
    return commonUserService.newUserIcon();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileContact newUserProfileContact()
  {
    return commonUserService.newUserProfileContact();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileWork newUserProfileWork()
  {
    return commonUserService.newUserProfileWork();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute newUserProfileProjects()
  {
    return commonUserService.newUserProfileProjects();
  }

  private void verifyJpaUser(final LdapUser ldapUser, final String pLogin, final String pPasswd)
      throws UserServiceException
  {
    User user = null;
    try
    {
      user = commonUserService.getUser(pLogin);
      verifyIfUserUpdated(pLogin, ldapUser);
    }
    catch (final Exception e)
    {
      // user is not into the forge base, we create the user
      final User userNew = commonUserService.newUser();
      userNew.setLogin(ldapUser.getUid());
      userNew.setPassword(ldapUser.getUserPassword());
      userNew.setName(ldapUser.getSurName());
      userNew.setFirstName(ldapUser.getGivenName());
      userNew.setEmail(ldapUser.getMail());
      try
      {
        final Language language = languageService.getLanguage(ldapUser.getPreferredLanguage());
        userNew.setLanguage(language);
      }
      catch (final LanguageServiceException lse)
      {
        throw new UserServiceException(String.format("Unable to get user language [language=%s]",
                                                     ldapUser.getPreferredLanguage()), lse);
      }
      commonUserService.createUser(userNew);
    }
  }

  private void verifyIfUserUpdated(final String pUserLogin, final LdapUser ldapUser) throws UserServiceException
  {
    final UserProfile userProfile = commonUserService.getUserProfile(pUserLogin);
    final User        user        = userProfile.getUser();
    boolean           update      = false;
    if (!user.getName().equals(ldapUser.getSurName()))
    {
      user.setName(ldapUser.getSurName());
      update = true;
    }
    if (!user.getFirstName().equals(ldapUser.getGivenName()))
    {
      user.setFirstName(ldapUser.getGivenName());
      update = true;
    }
    if (!user.getEmail().equals(ldapUser.getMail()))
    {
      user.setEmail(ldapUser.getMail());
      update = true;
    }

    if (!ldapService.getAuthorizeToUpdateLanguage())
    {
      if (!user.getLanguage().getName().equals(ldapUser.getPreferredLanguage()))
      {
        try
        {
          final Language language = languageService.getLanguage(ldapUser.getPreferredLanguage());
          user.setLanguage(language);
        }
        catch (final LanguageServiceException lse)
        {
          throw new UserServiceException(String.format("Unable to get user language [language=%s]",
                                                       ldapUser.getPreferredLanguage()), lse);
        }
        update = true;
      }
    }
    if (!user.getPassword().equals(ldapUser.getUserPassword()))
    {
      user.setPassword(ldapUser.getUserPassword());
      update = true;
    }
    if (update)
    {
      commonUserService.updateUserProfile(userProfile);
    }
  }

  /**
   * @param pCommonUserService
   *          the commonUserService to set
   */
  public void setCommonUserService(final CommonUserService pCommonUserService)
  {
    commonUserService = pCommonUserService;
  }

  /**
   * @param pLdapService
   *          the ldapService to set
   */
  public void setLdapService(final LdapService pLdapService)
  {
    ldapService = pLdapService;
  }

  /**
   * @param pAuthentificationService
   *          the authentificationService to set
   */
  public void setAuthentificationService(final AuthentificationService pAuthentificationService)
  {
    authentificationService = pAuthentificationService;
  }

  /**
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * @param pLanguageService
   *          the languageService to set
   */
  public void setLanguageService(final LanguageService pLanguageService)
  {
    languageService = pLanguageService;
  }

 @Override
  public boolean deleteUserRecoveryPassword(String pUserEmail) throws UserServiceException
  {
    return commonUserService.deleteUserRecoveryPassword(pUserEmail);
  }


}
