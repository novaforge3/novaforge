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
package org.novaforge.forge.core.jpa.user.internal;

import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Attribute;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;
import org.novaforge.forge.core.organization.services.CommonUserService;
import org.novaforge.forge.core.organization.services.UserService;

import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class UserServiceJPAImpl implements UserService
{
  private CommonUserService commonUserService;


/**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteUserRecoveryPassword(String pUserEmail) throws UserServiceException
  {
    return commonUserService.deleteUserRecoveryPassword(pUserEmail);
  }


  /**
   * {@inheritDoc}
   */
  @Override
  public User newUser()
  {
    return commonUserService.newUser();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createUser(final User pUser) throws UserServiceException
  {
    commonUserService.createUser(pUser);
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
  public User getUser(final String login) throws UserServiceException
  {
    return commonUserService.getUser(login);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser(final String login, final String passwd) throws UserServiceException
  {
    return commonUserService.getUser(login);
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
    commonUserService.deleteUser(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void activateUser(final String login, final String author) throws UserServiceException
  {
    commonUserService.activateUser(login, author);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void desactivateUser(final String login, final String author) throws UserServiceException
  {
    commonUserService.desactivateUser(login, author);
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
    commonUserService.recoverUserPassword(pUserEmail);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCandidateForUpdatingPassword(final String pLogin) throws UserServiceException
  {
    return commonUserService.isCandidateForUpdatingPassword(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthentificationType() throws UserServiceException
  {
    return UserService.JPA_USER_AUTHENTIFICATION;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizeCreateUser()
  {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizeUpdateUser()
  {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizedToUpdateUserLanguage() throws UserServiceException
  {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUserFromRecoveryPasswordToken(final String pToken) throws UserServiceException
  {
    return commonUserService.getUserFromRecoveryPasswordToken(pToken);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePasswordFromRecovery(final String pToken, final String pPassword)
      throws UserServiceException
  {
    commonUserService.updatePasswordFromRecovery(pToken, pPassword);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePassword(final String pLogin, final String pPassword) throws UserServiceException
  {
    commonUserService.updatePassword(pLogin, pPassword);
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

  /**
   * Use by container to inject {@link CommonUserService} implementation
   *
   * @param pCommonUserService
   *          the commonUserService to set
   */
  public void setCommonUserService(final CommonUserService pCommonUserService)
  {
    commonUserService = pCommonUserService;
  }

}
