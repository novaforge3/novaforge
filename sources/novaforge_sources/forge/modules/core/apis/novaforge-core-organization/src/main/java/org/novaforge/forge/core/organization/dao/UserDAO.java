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

package org.novaforge.forge.core.organization.dao;

import org.novaforge.forge.core.organization.model.Attribute;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.BlacklistedUser;
import org.novaforge.forge.core.organization.model.RecoveryPassword;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.UserProfile;
import org.novaforge.forge.core.organization.model.UserProfileContact;
import org.novaforge.forge.core.organization.model.UserProfileWork;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class defines methods to access to {@link BlacklistedUser} data from persistence
 * 
 * @author Guillaume Lamirand
 * @see BlacklistedUser
 * @see User
 */
public interface UserDAO
{

  /***************************************************
   * The following methods will manage User
   ***************************************************/

  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link User}
   */
  User newUser();

  /**
   * This method will get all {@link User} stored
   * 
   * @return the whole {@link List} of {@link User} stored
   */
  List<User> findAllUser();

  /**
   * This method will get all {@link UserProfile} stored
   * 
   * @return the whole {@link List} of {@link UserProfile} stored
   */
  List<UserProfile> findAllUserProfile();

  /**
   * Find a {@link User} by its login
   * 
   * @param pLogin
   *          the login used to seek user
   * @return {@link User} found
   * @throws NoResultException
   *           thrown if no {@link User} are existing for the login given
   */
  User findByLogin(final String pLogin) throws NoResultException;

  /**
   * Find a {@link User} by its uuid
   * 
   * @param pUserUUID
   *          the uuid used to seek user
   * @return {@link User} found
   * @throws NoResultException
   *           thrown if no {@link User} are existing for the uuid given
   */
  User findByUUID(final UUID pUserUUID);

  /**
   * Find a {@link User} by its email
   * 
   * @param pEmail
   *          the email used to seek user
   * @return {@link User} found
   * @throws NoResultException
   *           thrown if no {@link User} are existing for the email given
   */
  User findByEmail(String pEmail) throws NoResultException;

  /**
   * This method will return user's password found with its login
   * 
   * @param pLogin
   *          user's login
   * @return {@link User#getPassword()} found for the parameter given
   * @throws NoResultException
   *           thrown if no {@link User#getPassword()} cannot be found
   */
  String findPassword(final String pLogin) throws NoResultException;

  /**
   * Check if the email given is already used by another user
   * 
   * @param pEmail
   *          user's email
   * @return true if exits otherwise false
   */
  boolean existEmail(final String pEmail);

  /**
   * Check if the login given is already used by another user
   * 
   * @param pLogin
   *          user's login
   * @return <code>true</code> if exists otherwise <code>false</code>
   * @throws DataAccessException
   */
  boolean existLogin(String pLogin);

  /**
   * Check if the uuid given matches to a existing {@link User}
   * 
   * @param pUUID
   *          user's uuid
   * @return <code>true</code> if exists otherwise <code>false</code>
   * @throws DataAccessException
   */
  boolean existUserWith(final UUID pUUID);

  /**
   * This will persist {@link User} into persistence context
   * 
   * @param pUser
   *          the user to persist
   * @return {@link User} persisted and attach to persistence context
   */
  User persist(final User pUser);

  /**
   * This will update {@link User} into persistence context
   * 
   * @param pUser
   *          the user to update
   * @return {@link User} updated and attach to persistence context
   */
  User update(final User pUser);

  /**
   * This will delete {@link User} from persistence context
   * 
   * @param pUser
   *          the user to delete
   */
  void delete(final User pUser);

  /***************************************************
   * The following methods will manage BlacklistedUser
   ***************************************************/
  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link BlacklistedUser}
   */
  BlacklistedUser newBlacklistedUser();

  /**
   * This method will persist the object given in parameter
   * 
   * @param pBlacklistedUser
   *          the blacklistedUser to persist
   * @return {@link BlacklistedUser} attached to persistence context
   */
  BlacklistedUser persist(final BlacklistedUser pBlacklistedUser);

  /**
   * This method will return a {@link BlacklistedUser} stored from login parameter
   * 
   * @param pLogin
   *          the login used to find its {@link BlacklistedUser}
   * @return {@link BlacklistedUser} found
   * @throws NoResultException
   *           thrown if no {@link BlacklistedUser} are existing for the login given
   */
  BlacklistedUser findBlacklistedByLogin(final String pLogin) throws NoResultException;

  /**
   * This method will get all {@link BlacklistedUser} stored
   * 
   * @return the whole {@link List} of {@link BlacklistedUser} stored
   */
  List<BlacklistedUser> findAllBlacklisted();

  /***************************************************
   * The following methods will manage RecoveryPassword
   ***************************************************/
  /**
   * This method will return a {@link RecoveryPassword} stored from its token
   * 
   * @param pToken
   *          the token used to find its {@link RecoveryPassword}
   * @return {@link RecoveryPassword} found
   * @throws NoResultException
   *           thrown if no {@link RecoveryPassword} are existing for the token given
   */
  RecoveryPassword findByToken(final UUID pToken) throws NoResultException;
  
  /**
   * This method will return a list of {@link RecoveryPassword} stored from its user
   * 
   * @param pUser
   *          the user used to find its {@link RecoveryPassword}
   * @return list of {@link RecoveryPassword} found
   * @throws NoResultException
   *           thrown if no {@link RecoveryPassword} are existing for the token given
   */
  List<RecoveryPassword> findByUser(final User pUser) throws NoResultException;

  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link RecoveryPassword}
   */
  RecoveryPassword newRecoveryPassword();

  /**
   * This will persist {@link RecoveryPassword} into persistence context
   * 
   * @param pRecoveryPassword
   *          the recoveryPassword to persist
   * @return {@link RecoveryPassword} persisted and attach to persistence context
   */
  RecoveryPassword persist(final RecoveryPassword pRecoveryPassword);

  /**
   * This will update {@link RecoveryPassword} into persistence context
   * 
   * @param pRecoveryPassword
   *          the recoveryPassword to update
   * @return {@link RecoveryPassword} updated and attach to persistence context
   */
  RecoveryPassword update(final RecoveryPassword pRecoveryPassword);

  /**
   * This will delete {@link RecoveryPassword} from persistence context
   * 
   * @param pRecoveryPassword
   *          the recoveryPassword to delete
   */
  void delete(final RecoveryPassword pRecoveryPassword);

  /**
   * This method allows to search a user by like and equals criterias
   * 
   * @param pLikeCriterias
   * @param pEqualCriterias
   * @return
   */
  List<User> searchUserByCriterias(Map<String, Object> pLikeCriterias, Map<String, Object> pEqualCriterias);

  /**
   * This will persist {@link UserProfile} into persistence context
   * 
   * @param pUserProfile
   *          the {@link UserProfile} to persist
   * @return {@link UserProfile} persisted and attach to persistence context
   */
  UserProfile persist(UserProfile pUserProfile);

  /**
   * This will update {@link UserProfile} into persistence context
   * 
   * @param pUserProfile
   *          the userProfile to update
   * @return {@link UserProfile} updated and attach to persistence context
   */
  UserProfile update(UserProfile pUserProfile);

  /**
   * This will delete {@link UserProfile} from persistence context
   * 
   * @param pUserProfile
   *          the userProfile to delete
   */
  void delete(UserProfile pUserProfile);

  /**
   * This will delete {@link UserProfileContact} from persistence context
   * 
   * @param pUserProfileContact
   *          the userProfileContact to delete
   */
  void delete(UserProfileContact pUserProfileContact);

  /**
   * This will delete {@link UserProfileWork} from persistence context
   * 
   * @param pUserProfileWork
   *          the userProfileWork to delete
   */
  void delete(UserProfileWork pUserProfileWork);

  /**
   * This will delete {@link Attribute} from persistence context
   * 
   * @param pAttribute
   *          the attribute to delete
   */
  void delete(Attribute pAttribute);

  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link UserProfileWork}
   */
  UserProfileWork newUserProfileWork();

  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link UserProfileContact}
   */
  UserProfileContact newUserProfileContact();

  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link UserProfile}
   */
  UserProfile newUserProfile();

  /**
   * This method will return a new entity detach of persist context
   * 
   * @return new {@link BinaryFile}
   */
  BinaryFile newUserIcon();

  /**
   * Find an {@link UserProfile} associated to the user login given
   * 
   * @param pUserLogin
   *          the user login of the userprofile
   * @return {@link UserProfile} associated
   */
  UserProfile findProfileByLogin(String pUserLogin);

  /**
   * Find an {@link UserProfile} associated to the user email given
   * 
   * @param pUserEmail
   *          the user email of the userprofile
   * @return {@link UserProfile} associated
   */
  UserProfile findProfileByEmail(String pUserEmail);

  /**
   * This method will return a new entity detach of persiste context
   * 
   * @param pUser
   *          The {@link User} to associate to the new userProfile
   * @return new {@link UserProfile}
   */
  UserProfile newUserProfile(User pUser);

  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link Attribute}
   */
  Attribute newUserProfileProjects();

}
