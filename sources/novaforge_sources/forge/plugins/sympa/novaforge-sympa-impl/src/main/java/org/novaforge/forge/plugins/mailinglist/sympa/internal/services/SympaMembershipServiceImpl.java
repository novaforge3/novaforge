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
package org.novaforge.forge.plugins.mailinglist.sympa.internal.services;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginMembershipService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginMembershipService;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapClient;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapConnector;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapException;
import org.novaforge.forge.plugins.mailinglist.sympa.dao.UserDAO;
import org.novaforge.forge.plugins.mailinglist.sympa.entity.UserEntity;
import org.novaforge.forge.plugins.mailinglist.sympa.services.SympaConfigurationService;
import org.novaforge.forge.plugins.mailinglist.sympa.utils.Utils;

import java.net.URL;

/**
 * @author sbenoist
 */
public class SympaMembershipServiceImpl extends AbstractPluginMembershipService implements
    PluginMembershipService
{

  /*
   * Service injection declaration
   */
  private SympaSoapClient           sympaSoapClient;

  private UserDAO                   userDAO;

  private SympaConfigurationService sympaConfigurationService;

  private ForgeConfigurationService forgeConfigurationService;

  public void setSympaSoapClient(final SympaSoapClient pSympaSoapClient)
  {
    sympaSoapClient = pSympaSoapClient;
  }

  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean addToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    return addToolUser(pInstance, pUser);
  }

  public void setSympaConfigurationService(final SympaConfigurationService pSympaConfigurationService)
  {
    sympaConfigurationService = pSympaConfigurationService;
  }

  /**
   * @param forgeConfigurationService
   *     the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

  private boolean addToolUser(final InstanceConfiguration pInstance, final PluginUser pPluginUser)
      throws PluginServiceException
  {
    try
    {
      final SympaSoapConnector connector = getConnector(pInstance.getToolInstance());
      if (sympaSoapClient.isUser(connector, pPluginUser.getEmail()))
      {
        sympaSoapClient.updateUser(connector, pPluginUser.getEmail(),
            Utils.buildGecos(pPluginUser.getFirstName(), pPluginUser.getName()), pPluginUser.getPassword(),
            Utils.toSympaLanguage(pPluginUser.getLanguage()));
      }
      else
      {
        sympaSoapClient.createUser(connector, pPluginUser.getEmail(),
            Utils.buildGecos(pPluginUser.getFirstName(), pPluginUser.getName()), pPluginUser.getPassword(),
            Utils.toSympaLanguage(pPluginUser.getLanguage()));
      }

      if (!userDAO.existUser(pPluginUser.getLogin()))
      {
        userDAO.persist(new UserEntity(pPluginUser.getLogin(), pPluginUser.getEmail()));
      }

      // Add the user to default project mailinglist
      addUserToDefaultProjectMailingList(pInstance, pPluginUser);
    }
    catch (final SympaSoapException e)
    {
      throw new PluginServiceException(String.format("Unable to add sympa user with login=%s",
          pPluginUser.getLogin()), e);
    }
    return true;
  }



  private void addUserToDefaultProjectMailingList(final InstanceConfiguration pInstance,
      final PluginUser pPluginUser) throws PluginServiceException
  {
    try
    {
      final String superadminLogin = forgeConfigurationService.getSuperAdministratorLogin();

      final SympaSoapConnector connector = getConnector(pInstance.getToolInstance());
      final String listname = Utils.buildDefaultListname(pInstance.getForgeProjectId(),
          sympaConfigurationService.getDefaultListnameSuffix());

      // exclude superadmin from subscribers list
      if (!superadminLogin.equals(pPluginUser.getLogin()))
      {
        // check if the user is already subscriber of the default mailinglist
        if (!sympaSoapClient.isSubscriber(connector, listname, pPluginUser.getEmail()))
        {
          sympaSoapClient.addSubscriber(connector, listname, pPluginUser.getEmail(),
              Utils.buildGecos(pPluginUser.getFirstName(), pPluginUser.getName()), true);
        }
      }
    }
    catch (final Exception e)
    {
      throw new PluginServiceException(String.format(
          "Unable to add new subscriber to default project mailing list with login=%s",
          pPluginUser.getLogin()), e);
    }
  }



  private void removeUserToDefaultProjectMailingList(final InstanceConfiguration pInstance,
      final PluginUser pPluginUser) throws PluginServiceException
  {
    try
    {
      final SympaSoapConnector connector = getConnector(pInstance.getToolInstance());
      final String listname = Utils.buildDefaultListname(pInstance.getForgeProjectId(),
          sympaConfigurationService.getDefaultListnameSuffix());

      // check if the user is really subscriber of the default mailinglist
      if (sympaSoapClient.isSubscriber(connector, listname, pPluginUser.getEmail()))
      {
        sympaSoapClient.removeSubscriber(connector, listname, pPluginUser.getEmail(), true);
      }
    }
    catch (final Exception e)
    {
      throw new PluginServiceException(
          String.format("Unable to remove subscriber to default project mailing list with login=%s",
              pPluginUser.getLogin()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    return addToolUserMemberships(pInstance, pUser, pToolRole);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean removeToolUserMemberships(final InstanceConfiguration pInstance, final PluginUser pUser,
      final String pToolRole) throws PluginServiceException
  {
    removeUserToDefaultProjectMailingList(pInstance, pUser);
    return true;
  }

  private SympaSoapConnector getConnector(final ToolInstance pToolInstance) throws SympaSoapException,
      PluginServiceException
  {
    // get the baseURL for the connector
    final URL baseURL = pToolInstance.getBaseURL();

    // get the connector

    return sympaSoapClient.getConnector(
        sympaConfigurationService.getClientURL(baseURL), sympaConfigurationService.getClientAdmin(),
        sympaConfigurationService.getClientPwd(), baseURL.getHost(),
        sympaConfigurationService.getListmaster());
  }

}
