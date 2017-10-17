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
package org.novaforge.forge.core.organization.services;

import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
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
 * Service used to share some common parts of code for the different UserManager implementations.
 * 
 * @author sbenoist
 * @author lamirang
 */
public interface CommonUserService
{

  /**
   * Returns a new user
   * 
   * @return a new user
   */
  User newUser();

  /**
   * Tells if a user corresponding to the given login already exists.
   * 
   * @param login
   *          the login to look for.
   * @return true if a user corresponds to the login, false otherwise.
   * @throws UserServiceException
   */
  boolean existUser(String login) throws UserServiceException;

  /**
   * Create a new user
   * 
   * @param user
   *          the user to create
   * @throws UserServiceException
   */
  void createUser(User user) throws UserServiceException;

  /**
   * Create a super user. This method must be called only during the Forge initialization.
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
  User getUser(final String pLogin) throws UserServiceException;

  /**
   * @param login
   *          the user's login
   * @return true if the login is blacklisted (meaning that it has been already taken by a deleted user),
   *         false otherwise.
   * @throws UserServiceException
   */
  boolean isBlacklistedUser(String login) throws UserServiceException;

  /**
   * Get user's credential
   * 
   * @param pLogin
   *          user's login
   * @return the user's cendential
   * @throws UserServiceException
   */
  String getUserCredential(final String pLogin) throws UserServiceException;

  /**
   * Delete a user
   * 
   * @param pLogin
   * @throws UserServiceException
   * @throws ProjectServiceException
   */
  void deleteUser(String pLogin) throws UserServiceException;

  /**
   * @return the list of blacklisted users (users already deleted from the forge)
   * @throws UserServiceException
   */
  List<BlacklistedUser> getBlacklistedUsers() throws UserServiceException;

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
  
  boolean deleteUserRecoveryPassword(String pUserEmail) throws UserServiceException; 

  /**
   * @param pLogin
   * @return true if user associated to the login has to update its password
   * @throws UserServiceException
   */
  boolean isCandidateForUpdatingPassword(String pLogin) throws UserServiceException;

  /**
   * Verify User data integrity.
   * 
   * @param user
   * @throws UserServiceException
   */
  void checkUser(User user) throws UserServiceException;

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
   * Get a user profile
   * 
   * @param pLogin
   *          the user login
   * @return {@link UserProfile} the userprofile
   * @throws UserServiceException
   */
  UserProfile getUserProfile(String pLogin) throws UserServiceException;

  /**
   * Get a user profile from user's email
   * 
   * @param pEmail
   *          the user email
   * @return {@link UserProfile} the userprofile
   * @throws UserServiceException
   */
  UserProfile getUserProfileFromEmail(String pEmail) throws UserServiceException;

  /**
   * This method allows to update UserProfile
   * 
   * @param pUserProfile
   *          the {@link UserProfile} to update
   * @return {@link UserProfile} updated
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
