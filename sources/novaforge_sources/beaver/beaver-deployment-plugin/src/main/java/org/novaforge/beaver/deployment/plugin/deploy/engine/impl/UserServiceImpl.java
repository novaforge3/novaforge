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
package org.novaforge.beaver.deployment.plugin.deploy.engine.impl;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.novaforge.beaver.deployment.plugin.deploy.engine.UserService;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.ant.AntFacade;
import org.novaforge.beaver.deployment.plugin.deploy.engine.utils.exec.ExecFacade;
import org.novaforge.beaver.deployment.plugin.log.BeaverLogger;
import org.novaforge.beaver.exception.BeaverException;

/**
 * @author Guillaume Lamirand
 */
public class UserServiceImpl implements UserService
{

  private static final String ADD_USER_COMMAND = "adduser";
  private static final String DEL_USER_COMMAND = "deluser";
  private static final String MOD_USER_COMMAND = "usermod";

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
        String.format("Create linux user with [login=%s, password=%s, group=%s, additional_groups=%s]",
            pLogin, pPassword, pMainGroup, pAdditionalGroups));
    if (isExistUser(pLogin) == false)
    {
      final int executeAddUser = executeAddUser(pLogin, pMainGroup, pAdditionalGroups);
      boolean isSuccess = ExecFacade.isSuccess(executeAddUser);
      if (isSuccess)
      {
        isSuccess = executePasswd(pLogin, pPassword);
      }
      if (isSuccess == false)
      {
        throw new BeaverException(
            String
                .format(
                    "Unable to create linux user with given information [login=%s, password=%s, group=%s, additional_groups=%s]; Verify that you have admin rights!",
                    pLogin, pPassword, pMainGroup, Arrays.toString(pAdditionalGroups)));
      }
    }
    else
    {
      final int returnCode = ExecFacade.execCommandWithParams(MOD_USER_COMMAND, "-g", pMainGroup, pLogin);
      boolean isSuccess = ExecFacade.isSuccess(returnCode);
      if (isSuccess)
      {
        isSuccess = executePasswd(pLogin, pPassword);
      }
      if (isSuccess == false)
      {
        throw new BeaverException(String.format(
            "User already exists but unable to set its main group [login=%s, group=%s]", pLogin, pMainGroup));
      }
    }
  }

  private int executeAddUser(final String pLogin, final String pMainGroup, final String... pAdditionalGroups)
  {
    final List<String> params = new ArrayList<String>();
    params.add(pLogin);
    if (pLogin.equals(pMainGroup) == false)
    {
      params.add("-g " + pMainGroup);
    }
    if (pAdditionalGroups.length > 0)
    {
      final StringBuilder groups = new StringBuilder();
      for (final String group : pAdditionalGroups)
      {
        groups.append(group);
        groups.append(",");
      }
      final String additionnalGroups = groups.toString();
      params.add("-G " + additionnalGroups);
    }
    return ExecFacade.execCommandWithParams(ADD_USER_COMMAND, params);
  }

  /**
   * Change User's password by executing a script written in a temp file.
   * Apache executor can't handle pipe command.
   * 
   * @param pLogin
   *          user's login
   * @param pNewPassword
   *          user's new password
   * @return true if the operation is ok, else false
   */
  private boolean executePasswd(final String pLogin, final String pNewPassword)
  {
    boolean isSuccess = true;
    String commandLine = "echo \"" + pLogin + ":" + pNewPassword + "\" | chpasswd";
    try
    {
      File tempFile = File.createTempFile("temp", "file");
      PrintWriter writer = new PrintWriter(tempFile);
      writer.println(commandLine);
      writer.close();
      AntFacade.executeBashScript(tempFile);
      tempFile.delete();
    }
    catch (final BeaverException e)
    {
      BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getFilelogger().info(
          String.format("Error when modifying current [login=%S] password", pLogin));
      isSuccess = false;
    }
    catch (final IOException e)
    {
      BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
      BeaverLogger.getFilelogger().info(
          String.format("Error when modifying current [login=%S] password", pLogin));
      isSuccess = false;
    }
    return isSuccess;
  }

  private boolean isExistUser(final String pLogin)
  {
    final int returnCode = ExecFacade.execCommandWithParams("id", "-u", pLogin);
    return ExecFacade.isSuccess(returnCode);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeUser(final String pLogin) throws BeaverException
  {
    BeaverLogger.getFilelogger().info(BeaverLogger.SEPARATOR_TIRET);
    BeaverLogger.getFilelogger().info(String.format("Remove user with [login=%S]", pLogin));
    if (isExistUser(pLogin))
    {
      final int deluser = ExecFacade.execCommandLine(DEL_USER_COMMAND + pLogin);
      final boolean isSuccess = ExecFacade.isSuccess(deluser);
      if (isSuccess == false)
      {
        throw new BeaverException(String.format("Unable to remove user with given information [login=%s]",
            pLogin));
      }
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
        String.format("Add groups [%s] to user the [%s]", Arrays.toString(pGroups), pLogin));
    final StringBuilder groups = new StringBuilder();
    for (final String group : pGroups)
    {
      groups.append(group);
      groups.append(",");
    }
    String groupsList = groups.toString();
    if (groupsList.endsWith(","))
    {
      groupsList = groupsList.substring(0, groupsList.length() - 1);
    }
    final int deluser = ExecFacade.execCommandWithParams(MOD_USER_COMMAND, "-a", "-G", groupsList, pLogin);
    final boolean isSuccess = ExecFacade.isSuccess(deluser);
    if (isSuccess == false)
    {
      throw new BeaverException(String.format("Unable to remove user with given information [login=%s]",
          pLogin));
    }

  }
}
