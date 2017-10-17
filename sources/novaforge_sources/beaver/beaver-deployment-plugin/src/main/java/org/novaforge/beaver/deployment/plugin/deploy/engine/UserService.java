/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.beaver.deployment.plugin.deploy.engine;

import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Guillaume Lamirand
 */
public interface UserService
{

  /**
   * Create a user on Linux system. The main will be equaled to user login.
   * 
   * @param pLogin
   *          the user login
   * @param pPassword
   *          the user password
   * @throws BeaverException
   */
  void createLinuxUser(final String pLogin, final String pPassword) throws BeaverException;

  /**
   * Create a user on Linux system with a specifique main group and additionnal groups.
   * 
   * @param pLogin
   *          the user login
   * @param pPassword
   * @param pMainGroup
   * @param pAdditionalGroups
   * @throws BeaverException
   */
  void createLinuxUser(final String pLogin, final String pPassword, final String pMainGroup,
      final String... pAdditionalGroups) throws BeaverException;

  /**
   * Remove a user on Linux system.
   * 
   * @param pLogin
   *          the user login
   * @throws BeaverException
   */
  void removeUser(final String pLogin) throws BeaverException;

  /**
   * Add use to the groups given
   * 
   * @param pLogin
   *          the user to update
   * @param pGroups
   *          the new group
   * @throws BeaverException
   */
  void addGroupsToUser(String pLogin, String... pGroups) throws BeaverException;
}
