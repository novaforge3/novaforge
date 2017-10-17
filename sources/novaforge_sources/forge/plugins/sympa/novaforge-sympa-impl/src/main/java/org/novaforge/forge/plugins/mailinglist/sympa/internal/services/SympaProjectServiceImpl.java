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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapClient;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapConnector;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapException;
import org.novaforge.forge.plugins.mailinglist.sympa.dao.UserDAO;
import org.novaforge.forge.plugins.mailinglist.sympa.entity.UserEntity;
import org.novaforge.forge.plugins.mailinglist.sympa.services.SympaConfigurationService;
import org.novaforge.forge.plugins.mailinglist.sympa.utils.Utils;

import java.net.URL;
import java.util.List;

/**
 * @author sbenoist
 */
public class SympaProjectServiceImpl extends AbstractPluginProjectService implements PluginProjectService
{

  private static final Log LOG = LogFactory.getLog(SympaProjectServiceImpl.class);
  /*
   * Service injection declaration
   */
  private SympaSoapClient           sympaSoapClient;
  private UserDAO                   userDAO;
  private SympaConfigurationService sympaConfigurationService;
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pPluginProject, final List<PluginMembership> pPluginMembership)
      throws PluginServiceException
  {
    String toolProjectId = null;
    try
    {
      // create the topic for the application
      toolProjectId = pInstanceConfiguration.getForgeProjectId();
      sympaSoapClient.createTopic(getConnector(pInstanceConfiguration), toolProjectId, "noconceal");
      addToolUsers(pInstanceConfiguration, pPluginMembership);

      // add default mailing list
      if (pPluginMembership != null)
      {
        addDefaultMailingList(pInstanceConfiguration, pPluginProject.getAuthor(), pPluginMembership,
            toolProjectId);
      }
    }
    catch (final SympaSoapException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create tool project with InstanceConfiguration=%s and PluginProject=%s",
          pInstanceConfiguration.toString(), pPluginProject.toString()), e);
    }
    return toolProjectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean deleteToolProject(final InstanceConfiguration pInstance) throws PluginServiceException
  {
    boolean      success       = false;
    final String toolProjectId = pInstance.getToolProjectId();
    try
    {
      success = sympaSoapClient.deleteTopic(getConnector(pInstance), toolProjectId);
    }
    catch (final SympaSoapException e)
    {
      throw new PluginServiceException(String.format("Unable to delete tool project with id=%s", toolProjectId), e);

    }
    return success;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void archiveProject(final String pInstanceId) throws PluginServiceException
  {
    // TODO Auto-generated method stub

  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolProject(final InstanceConfiguration pInstanceConfiguration, final PluginProject pProject)
      throws PluginServiceException
  {
    // NOT USED
    return true;
  }

  private SympaSoapConnector getConnector(final InstanceConfiguration pInstanceConfiguration)
      throws SympaSoapException, PluginServiceException
  {
    // get the baseURL for the connector
    final URL baseURL = pInstanceConfiguration.getToolInstance().getBaseURL();

    // get the connector

    return sympaSoapClient.getConnector(sympaConfigurationService.getClientURL(baseURL),
                                                                      sympaConfigurationService.getClientAdmin(),
                                                                      sympaConfigurationService.getClientPwd(),
                                                                      baseURL.getHost(),
                                                                      sympaConfigurationService.getListmaster());
  }

  private void addToolUsers(final InstanceConfiguration pInstanceConfiguration,
                            final List<PluginMembership> pPluginMembership) throws PluginServiceException
  {
    PluginUser pluginUser = null;
    for (final PluginMembership pluginMembership : pPluginMembership)
    {
      pluginUser = pluginMembership.getPluginUser();
      try
      {
        if (!userDAO.existUser(pluginUser.getLogin()))
        {
          addToolUser(pInstanceConfiguration, pluginMembership.getPluginUser());
        }
      }
      catch (final Exception e)
      {
        throw new PluginServiceException(String.format("Unable to check if forge user is sympa user with login=%s",
                                                       pluginUser.getLogin()), e);
      }
    }
  }

  private void addDefaultMailingList(final InstanceConfiguration pInstanceConfiguration,
      final String pAuthor, final List<PluginMembership> pPluginMemberships, final String pToolProjectId)
      throws PluginServiceException
  {
    try
    {
      final String listOwner = getAuthorEmail(pAuthor, pPluginMemberships);

      // build default mailing list
      final String listname = Utils.buildDefaultListname(pInstanceConfiguration.getForgeProjectId(),
          sympaConfigurationService.getDefaultListnameSuffix());

      final SympaSoapConnector connector = getConnector(pInstanceConfiguration);

      if (sympaSoapClient.existList(connector, listname))
      {
        LOG.info(String.format("the default mailinglist already exists with [toolProjectId=%s]",
            pToolProjectId));
      }
      else
      {
        // if the email author is null, ie if tha project author is no more member of the project, the
        // listowner of the list will be the listmaster
        SympaSoapConnector createListConnector = null;
        if (listOwner != null)
        {
          createListConnector = getListOwnerConnector(pInstanceConfiguration, listOwner);
        }
        else
        {
          createListConnector = getConnector(pInstanceConfiguration);
        }

        sympaSoapClient.createList(createListConnector, listname,
            sympaConfigurationService.getDefaultListnameSubject(),
            sympaConfigurationService.getDefaultListnameTemplate(),
            sympaConfigurationService.getDefaultListnameDescription(), pToolProjectId);

        // subscribe the members of the project to the list
        final String subscribers = toEmailsStr(pPluginMemberships);
        if (subscribers != null)
        {
          sympaSoapClient.addSubscribers(getConnector(pInstanceConfiguration), listname,
              toEmailsStr(pPluginMemberships), true);
        }
      }
    }
    catch (final Exception e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create default mailinglist and add subscribers with projectId=%s",
          pInstanceConfiguration.getForgeProjectId()), e);
    }
  }

  private void addToolUser(final InstanceConfiguration pInstanceConfiguration, final PluginUser pPluginUser)
      throws PluginServiceException
  {
    try
    {
      final SympaSoapConnector connector = getConnector(pInstanceConfiguration);

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
    }
    catch (final SympaSoapException e)
    {
      throw new PluginServiceException(String.format("Unable to add sympa user with login=%s",
          pPluginUser.getLogin()), e);
    }
  }

  private String getAuthorEmail(final String pAuthor, final List<PluginMembership> pPluginMembership)
  {
    String email = null;
    for (final PluginMembership membership : pPluginMembership)
    {
      if (membership.getPluginUser().getLogin().equals(pAuthor))
      {
        email = membership.getPluginUser().getEmail();
        break;
      }
    }
    return email;
  }

  private SympaSoapConnector getListOwnerConnector(final InstanceConfiguration pInstanceConfiguration,
                                                   final String pListOwner)
      throws SympaSoapException, PluginServiceException
  {
    // get the baseURL for the connector
    final URL baseURL = pInstanceConfiguration.getToolInstance().getBaseURL();

    // get the connector

    return sympaSoapClient.getConnector(
        sympaConfigurationService.getClientURL(baseURL), sympaConfigurationService.getClientAdmin(),
        sympaConfigurationService.getClientPwd(), baseURL.getHost(), pListOwner);
  }

  private String toEmailsStr(final List<PluginMembership> pPluginMemberships)
  {
    String              result          = null;
    final StringBuilder emails          = new StringBuilder();
    final String        superadminLogin = forgeConfigurationService.getSuperAdministratorLogin();
    for (final PluginMembership membership : pPluginMemberships)
    {
      // exclude superadmin from subscribers list
      if (!superadminLogin.equals(membership.getPluginUser().getLogin()))
      {
        emails.append(membership.getPluginUser().getEmail()).append(";");
      }
    }

    if (emails.length() > 0)
    {
      result = emails.substring(0, emails.length() - 1);
    }

    return result;
  }

  private String getToolProjectId(final String pInstanceId) throws PluginServiceException
  {
    return instanceConfigurationDAO.findByInstanceId(pInstanceId).getToolProjectId();
  }

  public void setSympaSoapClient(final SympaSoapClient pSympaSoapClient)
  {
    sympaSoapClient = pSympaSoapClient;
  }

  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }

  public void setSympaConfigurationService(final SympaConfigurationService pSympaConfigurationService)
  {
    sympaConfigurationService = pSympaConfigurationService;
  }

  /**
   * @param forgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService forgeConfigurationService)
  {
    this.forgeConfigurationService = forgeConfigurationService;
  }

}
