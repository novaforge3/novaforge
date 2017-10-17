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
package org.novaforge.forge.core.organization.presenters;

import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Attribute;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;

import java.util.List;
import java.util.Map;

/**
 * @pAuthor sbenoist
 */
public interface UserPresenter
{

  /**
   * Returns a new user
   * 
   * @return a new user
   */
  User newUser();

  /**
   * Create a new user
   * 
   * @param user
   *          the user to create
   * @throws UserServiceException
   */
  void createUser(User user) throws UserServiceException;

  /**
   * Create a super user
   * 
   * @param user
   *          the user to create
   * @throws UserServiceException
   */
  void createSuperUser(User user) throws UserServiceException;

  /**
   * Get a user
   * 
   * @param pLogin
   * @return the user
   * @throws UserServiceException
   */
  User getUser(String pLogin) throws UserServiceException;

  /**
   * Get user's credential
   * 
   * @param pLogin
   *          user's pLogin
   * @return the user's cendential
   * @throws UserServiceException
   */
  String getUserCredential(final String pLogin) throws UserServiceException;

  /**
   * Delete a user
   * 
   * @param pLogin
   * @throws UserServiceException
   */
  void deleteUser(String pLogin) throws UserServiceException;

  /**
   * Validate a user
   * 
   * @param pLogin
   * @param pAuthor
   * @throws UserServiceException
   */
  void activateUser(String pLogin, String pAuthor) throws UserServiceException;

  /**
   * Desactivate a user
   * 
   * @param pLogin
   * @param pAuthor
   * @throws UserServiceException
   */
  void desactivateUser(String pLogin, String pAuthor) throws UserServiceException;

  /**
   * This method returns all the forge users
   * 
   * @param pWithSystem
   * @return List<User>
   * @throws UserServiceException
   */
  List<User> getAllUsers(final boolean pWithSystem) throws UserServiceException;

  /**
   * This method returns all the forge user profiles
   * 
   * @param pWithSystem
   * @return List<UserProfile>
   * @throws UserServiceException
   */
  List<UserProfile> getAllUserProfiles(final boolean pWithSystem) throws UserServiceException;

  /**
   * This method returns all the forge blacklisted users
   * 
   * @param pWithSystem
   * @return List<BlacklistedUser>
   * @throws UserServiceException
   */
  List<BlacklistedUser> getAllBlacklistedUsers() throws UserServiceException;

  /**
   * This method allows to make a search on multi criteria "% Like %" or/and on Equal Criterias. The search
   * keys are USER_XXX_SEARCH_CRITERIA_KEY
   * 
   * @param pLikeCriterias
   * @param pEqualCriterias
   * @return List<User>
   * @throws UserServiceException
   */
  List<User> searchUsersByCriterias(Map<String, Object> pLikeCriterias, Map<String, Object> pEqualCriterias)
      throws UserServiceException;

  /**
   * This method allows to find an user by email and send hime a new generated password in order to change it
   * 
   * @param pUserEmail
   * @throws UserServiceException
   */
  void recoverUserPassword(String pUserEmail) throws UserServiceException;
  
  /**
   * This method allows to delete unused recovery password linked to a user email
   * 
   * @param pUserEmail
   * @return true if recovery password is deleted
   * @throws UserServiceException
   */
  boolean deleteUserRecoveryPassword (String pUserEmail) throws UserServiceException;

  /**
   * This method returns if the user is candidate to update his password or not
   * 
   * @param pLogin
   * @return boolean
   * @throws UserServiceException
   */
  boolean isCandidateForUpdatingPassword(String pLogin) throws UserServiceException;

  /**
   * This method returns authentification type : JPA ot LDAP
   * 
   * @return String : JPA ot LDAP
   * @throws UserServiceException
   */
  String getAuthentificationType() throws UserServiceException;

  /**
   * This method return true if when can create a user
   * 
   * @return true if possible
   */
  boolean isAuthorizeCreateUser();

  /**
   * This method return true if when can update a user
   * 
   * @return true if possible
   */
  boolean isAuthorizeUpdateUser();

  /**
   * This method return true if the user is authorized to update language
   * 
   * @return true if possible
   * @throws UserServiceException
   */
  boolean isAuthorizedToUpdateUserLanguage() throws UserServiceException;

  /**
   * This method returns the user from recoveryPassword token. it throws Exception if the token is disabled,
   * expired or unexisting
   * 
   * @param pToken
   * @return {@link User} found
   * @throws UserServiceException
   */
  User getUserFromRecoveryPasswordToken(String pToken) throws UserServiceException;

  /**
   * This method allows to update password from the recovery page
   * 
   * @param pToken
   * @param pPassword
   * @throws UserServiceException
   */
  void updatePasswordFromRecovery(String pToken, String pPassword) throws UserServiceException;

  /**
   * This method allows to update password for a login
   * 
   * @param pLogin
   * @param pPassword
   * @throws UserServiceException
   */
  void updatePassword(final String pLogin, final String pPassword) throws UserServiceException;

  /**
   * Get the {@link UserProfile} for given pLogin
   * 
   * @param pLogin
   *          The user pLogin
   * @return the {@link UserProfile}
   * @throws UserServiceException
   */
  UserProfile getUserProfile(String pLogin) throws UserServiceException;

  /**
   * Get the {@link UserProfile} for given email
   * 
   * @param pEmail
   *          The user email
   * @return the {@link UserProfile}
   * @throws UserServiceException
   */
  UserProfile getUserProfileFromEmail(String pEmail) throws UserServiceException;

  /**
   * This method allow to update the {@link UserProfile}
   * 
   * @param pUserProfile
   *          the {@link UserProfile} to update
   * @return the {@link UserProfile} updated
   * @throws UserServiceException
   */
  UserProfile updateUserProfile(UserProfile pUserProfile) throws UserServiceException;

  /**
   * Returns a new User Icon
   * 
   * @return a new {@link BinaryFile}
   */
  BinaryFile newUserIcon();

  /**
   * Returns a new UserProfileContact
   * 
   * @return a new {@link UserProfileContact}
   */
  UserProfileContact newUserProfileContact();

  /**
   * Returns a new UserProfileWork
   * 
   * @return a new {@link UserProfileWork}
   */
  UserProfileWork newUserProfileWork();

  /**
   * Returns a new UserProfileProjects
   * 
   * @return a new {@link Attribute}
   */
  Attribute newUserProfileProjects();
}
