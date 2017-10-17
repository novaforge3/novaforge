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
package org.novaforge.forge.tools.deliverymanager.dao;

import org.novaforge.forge.tools.deliverymanager.model.User;

/**
 * @author sbenoist
 */
public interface UserDAO
{
  /**
   * This method will return a new entity detach of persiste context
   * 
   * @return new {@link User}
   */
  User newUser();

  /**
   * Check if the login given is already used by another user
   * 
   * @param pLogin
   *          user's login
   * @return <code>true</code> if exists otherwise <code>false</code>
   * @throws DataAccessException
   */
  boolean exist(String pLogin);

  /**
   * This method returns an user found by its given login
   * 
   * @param pLogin
   *          user's login
   * @return User
   */
  User findByLogin(String pLogin);

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
}
