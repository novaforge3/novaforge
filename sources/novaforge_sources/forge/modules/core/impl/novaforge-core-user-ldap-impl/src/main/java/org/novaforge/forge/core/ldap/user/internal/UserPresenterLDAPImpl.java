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

import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Attribute;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;
import org.novaforge.forge.core.organization.presenters.UserPresenter;
import org.novaforge.forge.core.organization.services.UserService;

import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class UserPresenterLDAPImpl implements UserPresenter
{
  /**
   * Reference to {@link UserService} service injected by the container
   */
  private UserService userService;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteUserRecoveryPassword(String pUserEmail) throws UserServiceException
  {
    return userService.deleteUserRecoveryPassword(pUserEmail);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User newUser()
  {
    return userService.newUser();
  }

  /**
   * {@inheritDoc}
   */
  @Historization(type = EventType.CREATE_USER)
  @Override
  public void createUser(@HistorizableParam(label = "user") final User pUser) throws UserServiceException
  {
    userService.createUser(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSuperUser(final User pUser) throws UserServiceException
  {
    userService.createSuperUser(pUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUser(final String pLogin) throws UserServiceException
  {
    return userService.getUser(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getUserCredential(final String pLogin) throws UserServiceException
  {
    return userService.getUserCredential(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Historization(type = EventType.DELETE_USER)
  @Override
  public void deleteUser(@HistorizableParam(label = "login") final String pLogin) throws UserServiceException
  {
    userService.deleteUser(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void activateUser(final String pLogin, final String pAuthor) throws UserServiceException
  {
    userService.activateUser(pLogin, pAuthor);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void desactivateUser(final String pLogin, final String pAuthor) throws UserServiceException
  {
    userService.desactivateUser(pLogin, pAuthor);
  }

  /**
   * {@inheritDoc}
   */

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.GET_ALL_USERS)
  public List<User> getAllUsers(final boolean pWithSystem) throws UserServiceException
  {
    return userService.getAllUsers(pWithSystem);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.GET_ALL_USERPROFILES)
  public List<UserProfile> getAllUserProfiles(final boolean pWithSystem) throws UserServiceException
  {
    return userService.getAllUserProfiles(pWithSystem);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BlacklistedUser> getAllBlacklistedUsers() throws UserServiceException
  {
    return userService.getAllBlacklistedUsers();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<User> searchUsersByCriterias(final Map<String, Object> pLikeCriterias,
                                           final Map<String, Object> pEqualCriterias) throws UserServiceException
  {
    return userService.searchUsersByCriterias(pLikeCriterias, pEqualCriterias);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.RECOVER_PASSWORD)
  public void recoverUserPassword(@HistorizableParam(label = "email") final String pUserEmail)
      throws UserServiceException
  {
    userService.recoverUserPassword(pUserEmail);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isCandidateForUpdatingPassword(final String pLogin) throws UserServiceException
  {
    return userService.isCandidateForUpdatingPassword(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAuthentificationType() throws UserServiceException
  {
    return userService.getAuthentificationType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizeCreateUser()
  {
    return userService.isAuthorizeCreateUser();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizeUpdateUser()
  {
    return userService.isAuthorizeUpdateUser();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizedToUpdateUserLanguage() throws UserServiceException
  {
    return userService.isAuthorizedToUpdateUserLanguage();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public User getUserFromRecoveryPasswordToken(final String pToken) throws UserServiceException
  {
    return userService.getUserFromRecoveryPasswordToken(pToken);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePasswordFromRecovery(final String pToken, final String pPassword)
      throws UserServiceException
  {
    userService.updatePasswordFromRecovery(pToken, pPassword);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updatePassword(final String pLogin, final String pPassword) throws UserServiceException
  {
    userService.updatePassword(pLogin, pPassword);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile getUserProfile(final String pLogin) throws UserServiceException
  {
    return userService.getUserProfile(pLogin);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfile getUserProfileFromEmail(final String pEmail) throws UserServiceException
  {
    return userService.getUserProfileFromEmail(pEmail);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.UPDATE_USER)
  public UserProfile updateUserProfile(final UserProfile pUserProfile) throws UserServiceException
  {
    return userService.updateUserProfile(pUserProfile);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BinaryFile newUserIcon()
  {
    return userService.newUserIcon();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileContact newUserProfileContact()
  {
    return userService.newUserProfileContact();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UserProfileWork newUserProfileWork()
  {
    return userService.newUserProfileWork();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Attribute newUserProfileProjects()
  {
    return userService.newUserProfileProjects();
  }

}
