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
package org.novaforge.forge.plugins.mailinglist.sympa.internal;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListBean;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListCategoryService;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListExceptionCode;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListServiceException;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListSubscriber;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.categories.beans.MailingListBeanImpl;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapClient;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapConnector;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapException;
import org.novaforge.forge.plugins.mailinglist.sympa.dao.UserDAO;
import org.novaforge.forge.plugins.mailinglist.sympa.model.User;
import org.novaforge.forge.plugins.mailinglist.sympa.services.SympaConfigurationService;
import org.novaforge.forge.plugins.mailinglist.sympa.services.SympaSubscriptionDelegate;
import org.novaforge.forge.plugins.mailinglist.sympa.utils.Utils;

import javax.persistence.NoResultException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * @author rols-p
 */
public class SympaCategoryServiceImpl extends AbstractPluginCategoryService implements
    MailingListCategoryService
{

  private static final String       PROPERTY_FILE = "sympa";

  private SympaSoapClient           sympaSoapClient;

  private SympaConfigurationService sympaConfigurationService;
  
  private ForgeConfigurationService forgeConfigurationService;

  private InstanceConfigurationDAO  instanceConfigurationDAO;

  private UserDAO                   userDAO;

  private SympaSubscriptionDelegate subscriptionDelegate;

  @Override
  public String getApplicationAccessInfo(final String pInstanceId, final Locale pLocale)
      throws PluginServiceException
  {
    return getMessage(KEY_ACCESS_INFO, pLocale);
  }

  @Override
  protected String getPropertyFileName()
  {
    return PROPERTY_FILE;
  }

  public void setSympaSoapClient(final SympaSoapClient pSympaSoapClient)
  {
    sympaSoapClient = pSympaSoapClient;
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

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MailingListBean> getMailingLists(final String pForgeId, final String pInstanceId,
      final String pCurrentUser) throws MailingListServiceException
  {
    final String projectId = instanceConfigurationDAO.findByInstanceId(pInstanceId).getForgeProjectId();
    final SympaSoapConnector connector = getConnector(pInstanceId);
    try
    {
      final List<MailingListBean> lists = sympaSoapClient.getMailingLists(connector, projectId);

      for (final MailingListBean list : lists)
      {
        setLockOnMailingList(projectId, list);
        // we need to define users's properties for each list
        checkMailingListsOwners(list);
        checkMailingListsSubscribers(list);
        // we need to redefine for each susbcriber if he owns a group subscription or not
        list.setSubscribers(subscriptionDelegate.checkGroupSubscriptions(connector, list.getSubscribers(),
            list.getName()));
      }

      return lists;
    }
    catch (final SympaSoapException e)
    {
      throw new MailingListServiceException(String.format(
          "an error occurred during getting mailinglists from SOAP client with [topic=%s]", projectId), e);
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(String.format(
          "an error occurred during getting mailinglists with [topic=%s]", projectId), e);
    }
  }

  public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    instanceConfigurationDAO = pInstanceConfigurationDAO;
  }

  public void setUserDAO(final UserDAO pUserDAO)
  {
    userDAO = pUserDAO;
  }

  private void setLockOnMailingList(final String pProjectId, final MailingListBean pList)
  {
    // Check if current mailing is not default one
    final String buildDefaultListname = Utils.buildDefaultListname(pProjectId,
        sympaConfigurationService.getDefaultListnameSuffix());
    if ((pList != null) && (pList.getName() != null))
    {
      if (pList.getName().startsWith(buildDefaultListname + "@"))
      {
        pList.setLocked(true);
      }
    }
  }

  public void setSubscriptionDelegate(final SympaSubscriptionDelegate pSubscriptionDelegate)
  {
    this.subscriptionDelegate = pSubscriptionDelegate;
  }

  private void checkMailingListsOwners(final MailingListBean pList)
  {
    if ((pList.getOwners() != null) && (pList.getOwners().size() > 0))
    {
      for (final MailingListUser user : pList.getOwners())
      {
        checkIfUserIsInternal(user);
      }
    }
  }



  private void checkMailingListsSubscribers(final MailingListBean pList)
  {
    if ((pList.getSubscribers() != null) && (pList.getSubscribers().size() > 0))
    {
      for (final MailingListSubscriber user : pList.getSubscribers())
      {
        checkIfUserIsInternal((MailingListUser) user);
      }
    }
  }



  private void checkIfUserIsInternal(final MailingListUser pUser)
  {
    try
    {
      final User user = userDAO.findByEmail(pUser.getEmail());
      pUser.setLogin(user.getLogin());
      pUser.setExternal(false);
    }
    catch (final NoResultException e)
    {
      pUser.setExternal(true);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MailingListBean> getVisibleMailingLists(final String pForgeId, final String pInstanceId,
      final String pCurrentUser) throws MailingListServiceException
  {
    final List<MailingListBean> returned = new ArrayList<MailingListBean>();
    final List<MailingListBean> mailingLists = getMailingLists(pForgeId, pInstanceId, pCurrentUser);
    final String superadminLogin = forgeConfigurationService.getSuperAdministratorLogin();
    
    
    if (mailingLists != null)
    {
      for (final MailingListBean mailingList : mailingLists)
      {
        if (mailingList.getType().isVisible() || isOwnerOrSubscriber(pCurrentUser, mailingList)|| superadminLogin.equals(pCurrentUser))
        {
          returned.add(mailingList);
        }
      }
    }
    return returned;
  }

  private boolean isOwnerOrSubscriber(final String pLogin, final MailingListBean pMailingListBean)
  {
    boolean returned = false;
    final List<MailingListUser> ownersAndSubscribers = new ArrayList<MailingListUser>();
    final List<MailingListSubscriber> subscribers = pMailingListBean.getSubscribers();
    for (final MailingListSubscriber subscriber : subscribers)
    {
      if (subscriber instanceof MailingListUser)
      {
        ownersAndSubscribers.add((MailingListUser) subscriber);
      }
    }
    ownersAndSubscribers.addAll(pMailingListBean.getOwners());
    for (final MailingListUser user : ownersAndSubscribers)
    {
      if ((!user.isExternal()) && (user.getLogin().equals(pLogin)))
      {
        returned = true;
        break;
      }
    }
    return returned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existMailingList(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pListname) throws MailingListServiceException
  {
    boolean returnValue = false;

    // Get the email of the current user and future listowner
    final User owner = userDAO.findByLogin(pCurrentUser);

    try
    {
      final SympaSoapConnector listOwnerConnector = getListOwnerConnector(pInstanceId, owner.getEmail());
      returnValue = sympaSoapClient.existList(listOwnerConnector, pListname);
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(String.format(
          "an error occurred during creating mailinglist with [mailinglistname=%s]", pListname), e);
    }
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createMailingList(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final MailingListBean pMailingList) throws MailingListServiceException
  {
    final String topicName = instanceConfigurationDAO.findByInstanceId(pInstanceId).getForgeProjectId();

    // Get the email of the current user and future listowner
    final User owner = userDAO.findByLogin(pCurrentUser);

    final SympaSoapConnector connector = getConnector(pInstanceId);
    try
    {
      final SympaSoapConnector listOwnerConnector = getListOwnerConnector(pInstanceId, owner.getEmail());
      final boolean existList = sympaSoapClient.existList(listOwnerConnector, pMailingList.getName());
      if (!existList)
      {
        sympaSoapClient.createList(listOwnerConnector, pMailingList.getName(), pMailingList.getSubject(),
            Utils.toTemplate(pMailingList.getType()), pMailingList.getDescription(), topicName);
      }
      // subscribe the owner to the list
      sympaSoapClient.addSubscriber(connector, pMailingList.getName(), owner.getEmail(), null, false);
    }
    catch (final SympaSoapException e)
    {
      if ("List already exists".equals(e.getMessage().trim()))
      {
        throw new MailingListServiceException(
            MailingListExceptionCode.ERR_CREATE_MAILING_LIST_ALREADY_EXISTS,
            "the listname is already used for any mailinglist;");
      }
      else
      {
        throw new MailingListServiceException(
            String.format(
                "an error occurred during creating mailinglist from SOAP client with [topic=%s, mailinglist name=%s]",
                topicName, pMailingList.getName()), e);
      }
    }
    catch (final PluginServiceException e)
    {
      throw new MailingListServiceException(String.format(
          "an error occurred during getting sympa connector with [instanceId=%s]", pInstanceId), e);
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(String.format(
          "an error occurred during creating mailinglist with [topic=%s, mailinglistname=%s]", topicName,
          pMailingList.getName()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateMailingList(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final MailingListBean pMailingList) throws MailingListServiceException
  {
    // TODO Auto-generated method stub
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void closeMailingList(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pListname) throws MailingListServiceException
  {
    final SympaSoapConnector connector = getConnector(pInstanceId);
    try
    {
      sympaSoapClient.closeList(connector, pListname);
    }
    catch (final SympaSoapException e)
    {
      throw new MailingListServiceException(String.format(
          "an error occurred during closing mailinglist from SOAP client with [mailinglist name=%s]",
          pListname), e);
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(String.format(
          "a technical error occurred during closing mailinglist with [mailinglist name=%s]", pListname), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addSubscriber(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pListname, final MailingListUser pSubscriber) throws MailingListServiceException
  {
    final SympaSoapConnector connector = getConnector(pInstanceId);
    subscriptionDelegate.addUserSubscription(connector, pListname, pSubscriber);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MailingListUser> addSubscribers(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pListname, final List<MailingListUser> pSubscribers,
      final boolean pQuietMode) throws MailingListServiceException
  {
    final SympaSoapConnector connector = getConnector(pInstanceId);
    return subscriptionDelegate.addUsersSubscriptions(connector, pListname, pSubscribers, pQuietMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> addExternalSubscribers(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pListname, final String pEmails, final boolean pQuietMode)
      throws MailingListServiceException
  {
    final SympaSoapConnector connector = getConnector(pInstanceId);
    return subscriptionDelegate.addExternalSubscriptions(connector, pListname, pEmails, pQuietMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeSubscriber(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pListName, final MailingListUser pSubscriber, final boolean pQuietMode)
      throws MailingListServiceException
  {
    final SympaSoapConnector connector = getConnector(pInstanceId);
    subscriptionDelegate.removeUserSubscription(connector, pListName, pSubscriber, pQuietMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addOwner(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pListname, final MailingListUser pSubscriber) throws MailingListServiceException
  {
    // TODO Auto-generated method stub
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeOwner(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pListname, final MailingListUser pSubscriber) throws MailingListServiceException
  {
    // TODO Auto-generated method stub
  }

  private SympaSoapConnector getConnector(final String pInstanceId) throws MailingListServiceException
  {
    try
    {
      // get the instance
      final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

      // get the baseURL for the connector
      final URL baseURL = instance.getToolInstance().getBaseURL();

      // get the connector

      return sympaSoapClient.getConnector(
          sympaConfigurationService.getClientURL(baseURL), sympaConfigurationService.getClientAdmin(),
          sympaConfigurationService.getClientPwd(), baseURL.getHost(),
          sympaConfigurationService.getListmaster());
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(String.format("Unable to get connector with [instanceId=%s]",
          pInstanceId), e);
    }
  }

  private SympaSoapConnector getListOwnerConnector(final String pInstanceId, final String pListOwner)
      throws SympaSoapException, PluginServiceException
  {
    // get the instance
    final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

    // get the baseURL for the connector
    final URL baseURL = instance.getToolInstance().getBaseURL();

    // get the connector

    return sympaSoapClient.getConnector(
        sympaConfigurationService.getClientURL(baseURL), sympaConfigurationService.getClientAdmin(),
        sympaConfigurationService.getClientPwd(), baseURL.getHost(), pListOwner);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addGroupSubscription(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pListName, final MailingListGroup pGroup,
      final boolean pQuietMode) throws MailingListServiceException
  {
    final SympaSoapConnector connector = getConnector(pInstanceId);
    subscriptionDelegate.addGroupsSubscriptions(connector, pListName, pGroup, pQuietMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addGroupsSubscriptions(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pListName, final List<MailingListGroup> pGroups,
      final boolean pQuietMode) throws MailingListServiceException
  {
    final SympaSoapConnector connector = getConnector(pInstanceId);
    for (final MailingListGroup group : pGroups)
    {
      subscriptionDelegate.addGroupsSubscriptions(connector, pListName, group, pQuietMode);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeGroupSubscription(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pListName, final MailingListGroup pGroup,
      final boolean pQuietMode) throws MailingListServiceException
  {
    final SympaSoapConnector connector = getConnector(pInstanceId);
    subscriptionDelegate.removeGroupSubscription(connector, pListName, pGroup, pQuietMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateGroupSubscription(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pListName, final MailingListGroup pGroup,
      final boolean pQuietMode) throws MailingListServiceException
  {
    final SympaSoapConnector connector = getConnector(pInstanceId);
    subscriptionDelegate.updateGroupSubscription(connector, pListName, pGroup, pQuietMode);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MailingListGroup getGroupSubscriber(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pListName, final UUID pGroupUUID)
      throws MailingListServiceException
  {
    return subscriptionDelegate.getGroupSubscriber(pListName, pGroupUUID);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MailingListBean newMailingList()
  {
    return new MailingListBeanImpl();
  }

}
