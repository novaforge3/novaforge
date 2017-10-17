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
package org.novaforge.beaver.deployment.plugin.deploy.engine.simulate;

import java.util.Arrays;

import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

import com.google.common.base.Strings;

/**
 * @author Guillaume Lamirand
 */
public class UserServiceSimulateImpl implements UserService
{

  private static final String ADD_USER_COMMAND = "adduser";
  private static final String DEL_USER_COMMAND = "deluser";

  /**
   * {@inheritDoc}
   */
  @Override
  public void createLinuxUser(final String pLogin, final String pPassword) throws BeaverException
  {
    createLinuxUser(pLogin, pPassword, pLogin);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createLinuxUser(final String pLogin, final String pPassword, final String pMainGroup,
      final String... pAdditionalGroups) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Create linux user with [login=%S, password=%s, group=%s, additional_groups=%s]",
            pLogin, pPassword, pMainGroup, pAdditionalGroups));

    if ((Strings.isNullOrEmpty(pLogin)) || (Strings.isNullOrEmpty(pPassword))
        || (Strings.isNullOrEmpty(pMainGroup)))
    {

      throw new BeaverException(
          String
              .format(
                  "Unable to create linux user with given information [login=%S, password=%s, group=%s, additional_groups=%s]; Verify that you have admin rights!",
                  pLogin, pPassword, pMainGroup, pAdditionalGroups));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeUser(final String pLogin) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(String.format("Remove user with [login=%S]", pLogin));

    if (Strings.isNullOrEmpty(pLogin))
    {
      throw new BeaverException(String.format("Unable to remove user with given information [login=%S]",
          pLogin));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addGroupsToUser(final String pLogin, final String... pGroups) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(
        String.format("Add groups [%s] to user the [%S]", Arrays.toString(pGroups), pLogin));

    if (Strings.isNullOrEmpty(pLogin))
    {

      throw new BeaverException(String.format(
          "Unable to update user groups with given information [login=%S]", pLogin));
    }
  }
}
