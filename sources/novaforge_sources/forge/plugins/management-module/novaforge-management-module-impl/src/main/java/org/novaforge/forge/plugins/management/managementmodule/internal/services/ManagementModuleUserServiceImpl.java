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
package org.novaforge.forge.plugins.management.managementmodule.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginUserService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginUserService;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.domain.Language;
import org.novaforge.forge.tools.managementmodule.domain.User;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.text.MessageFormat;

/**
 * @author efalsquelle
 */
public class ManagementModuleUserServiceImpl extends AbstractPluginUserService implements PluginUserService
{
  /*
   * Service injection declaration
   */
  private ManagementModuleManager managementModuleManager;

  @Override
  protected boolean updateToolUser(final InstanceConfiguration pInstanceConfiguration,
      final String pUserName, final PluginUser pPluginUser) throws PluginServiceException
  {
    boolean returnValue = false;
    try
    {
      User user = managementModuleManager.getUser(pPluginUser.getLogin());
      user.setFirstName(pPluginUser.getFirstName());
      user.setLastName(pPluginUser.getName());

      Language userLanguage = managementModuleManager.getLanguage(pPluginUser.getLanguage());
      if (userLanguage == null)
      {
        userLanguage = managementModuleManager.getLanguage(ManagementModuleConstants.LANGUAGE_FR);
      }
      user.setLanguage(userLanguage);

      returnValue = managementModuleManager.updateUser(user);
    }
    catch (ManagementModuleException e)
    {
      throw new PluginServiceException(String.format("Unable to update user's information with [user=%s]",
          pPluginUser), e);
    }
    return returnValue;
  }

  @Override
  protected boolean removeToolUser(final InstanceConfiguration pInstanceConfiguration, final PluginUser pPluginUser)
      throws PluginServiceException
  {
    try
    {
      User currentUser = managementModuleManager.getUser(pPluginUser.getLogin());
      if (currentUser == null)
      {
        throw new PluginServiceException(MessageFormat.format("User doesn't exist with login = %s",
                                                              pPluginUser.getLogin()));
      }

      return managementModuleManager.deleteUser(currentUser);
    }
    catch (ManagementModuleException e)
    {
      throw new PluginServiceException(MessageFormat.format("Unable to remove an user with login = %s ",
                                                            pPluginUser.getLogin()),e);
    }
  }

  @Override
  public void createAdministratorUser(final ToolInstance arg0, final PluginUser arg1)
      throws PluginServiceException
  {
    // TODO Not implemeted yet
  }

  public void setManagementModuleManager(final ManagementModuleManager pManagementModuleManager)
  {
    managementModuleManager = pManagementModuleManager;
  }

}
