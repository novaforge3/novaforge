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
package org.novaforge.forge.plugins.mailinglist.sympa.internal.delegates;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListServiceException;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListSubscriber;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.plugins.categories.beans.MailingListGroupImpl;
import org.novaforge.forge.plugins.categories.beans.MailingListUserImpl;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapClient;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapConnector;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapException;
import org.novaforge.forge.plugins.mailinglist.sympa.dao.SubscriptionDAO;
import org.novaforge.forge.plugins.mailinglist.sympa.dao.UserDAO;
import org.novaforge.forge.plugins.mailinglist.sympa.model.GroupSubscription;
import org.novaforge.forge.plugins.mailinglist.sympa.model.User;
import org.novaforge.forge.plugins.mailinglist.sympa.model.UserSubscription;
import org.novaforge.forge.plugins.mailinglist.sympa.services.SympaSubscriptionDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * @author sbenoist
 */
public class SympaSubscriptionDelegateImpl implements SympaSubscriptionDelegate
{
  private static final String  EMAIL_REGEX  = "^([a-zA-Z0-9_\\.\\-+])+@(([a-zA-Z0-9-])+\\.)+([a-zA-Z0-9]{2,4})+$";
  private static final Pattern emailPattern = Pattern.compile(EMAIL_REGEX);
  private static final Log     LOG          = LogFactory.getLog(SympaSubscriptionDelegateImpl.class);
  private SubscriptionDAO      subscriptionDAO;
  private UserDAO              userDAO;
  private SympaSoapClient      sympaSoapClient;

  /**
   * {@inheritDoc}
   */
  @Override
  public void addGroupsSubscriptions(final SympaSoapConnector pConnector, final String pListName,
      final MailingListGroup pGroup, final boolean pQuietMode) throws MailingListServiceException
  {

    final String listname = getShortListName(pListName);
    try
    {
      LOG.info(String.format("Add Group Subscription with [group=%s, listname=%s]", pGroup.getName(),
          listname));
      if (subscriptionDAO.existGroupSubscription(listname, pGroup.getUUID()))
      {
        throw new MailingListServiceException(
            String
                .format(
                    "Unable to add group subscription because the group subscription with [listname=%s, group name=%s, group uuid=%s] already exists",
                    listname, pGroup.getName(), pGroup.getUUID()));
      }

      // build the group subscription
      final GroupSubscription groupSubscription = subscriptionDAO.newGroupSubscription(pGroup.getUUID(),
          pGroup.getName(), listname);

      // set the members of the group subscription
      final List<MailingListUser> users = pGroup.getMembers();

      // manage the susbcription for each member of the group
      for (final MailingListUser user : users)
      {
        final String login = user.getLogin();
        groupSubscription.addMember(getUser(login, user.getEmail()));
      }

      // add the new subscribers to the list
      addSubscribersFromGroupSubscription(pConnector, pQuietMode, pListName, users);

      // persist the group subscription
      subscriptionDAO.persist(groupSubscription);
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(
          String.format(
              "an error occurred during adding group subscription with [listname=%s, group name=%s, group uuid=%s]",
              pListName, pGroup.getName(), pGroup.getUUID()), e);
    }

  }

  private String getShortListName(final String pFullListname)
  {
    String shortName = pFullListname;
    if (pFullListname.indexOf("@") != -1)
    {
      shortName = pFullListname.split("@")[0];
    }
    return shortName;
  }

  private User getUser(final String pLogin, final String pEmail)
  {
    User user = null;
    if (userDAO.existUser(pLogin))
    {
      user = userDAO.findByLogin(pLogin);
    }
    else
    {
      user = userDAO.newUser(pLogin, pEmail);
      user = userDAO.persist(user);
    }

    return user;
  }

  private void addSubscribersFromGroupSubscription(final SympaSoapConnector pConnector, final boolean pQuietMode,
                                                   final String pListName, final List<MailingListUser> pUsers)
      throws SympaSoapException
  {
    final List<MailingListUser> newEffectiveSubscribers = filterNewEffectiveSubscribers(pConnector, pListName, pUsers,
                                                                                        false);
    if (!newEffectiveSubscribers.isEmpty())
    {
      // add the new subscribers to the list
      sympaSoapClient.addSubscribers(pConnector, pListName, toEmailsStr(newEffectiveSubscribers), pQuietMode);
    }
  }

  private List<MailingListUser> filterNewEffectiveSubscribers(final SympaSoapConnector pConnector,
      final String pListName, final List<MailingListUser> pUsers, final boolean pAddUserSubscription)
      throws SympaSoapException
  {
    final String listname = getShortListName(pListName);
    final List<MailingListUser> newSubscribers = new ArrayList<MailingListUser>();
    for (final MailingListUser user : pUsers)
    {
      final String login = user.getLogin();
      // if the user subscribes already to the list and if he isn't member of any other group, we build a
      // user subscription
      if (sympaSoapClient.isSubscriber(pConnector, listname, user.getEmail()))
      {
        if (!hasUserSubscription(listname, login) && (!isMemberOfGroupSubscriptions(login, listname)
                                                          || pAddUserSubscription))
        {
          final UserSubscription userSubscription = subscriptionDAO.newUserSubscription(login, listname);
          subscriptionDAO.persist(userSubscription);
        }
      }
      else
      {
        newSubscribers.add(user);
        // save user if he doesn't exist
        getUser(login, user.getEmail());
      }
    }

    return newSubscribers;
  }

  private String toEmailsStr(final List<MailingListUser> pSubscribers)
  {
    final StringBuilder returned = new StringBuilder();
    for (final MailingListUser user : pSubscribers)
    {
      returned.append(user.getEmail()).append(";");
    }

    return returned.substring(0, returned.length() - 1);
  }

  private boolean hasUserSubscription(final String pListName, final String pLogin)
  {
    return subscriptionDAO.existUserSubscription(pListName, pLogin);
  }

  private boolean isMemberOfGroupSubscriptions(final String pLogin, final String pListName)
  {
    return (!subscriptionDAO.findAllByListAndUser(pListName, pLogin).isEmpty());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void updateGroupSubscription(final SympaSoapConnector pConnector, final String pListName,
      final MailingListGroup pGroup, final boolean pQuietMode) throws MailingListServiceException
  {
    LOG.info(String.format("Update Group Subscription with [group=%s, listname=%s]", pGroup.getName(),
        pListName));

    try
    {
      if (!subscriptionDAO.existGroupSubscription(pListName, pGroup.getUUID()))
      {
        throw new MailingListServiceException(
            String
                .format(
                    "Unable to update group subscription because the group subscription with [listname=%s, group name=%s, group uuid=%s] doesn't exists",
                    pListName, pGroup.getName(), pGroup.getUUID()));
      }

      final GroupSubscription groupSubscription = subscriptionDAO.findByListAndGroup(pListName,
          pGroup.getUUID());

      // compare the two list of members
      final List<MailingListUser> beforeMembers = new ArrayList<MailingListUser>();
      for (final User user : groupSubscription.getMembers())
      {
        beforeMembers.add(toListUser(user));
      }
      final List<MailingListUser> afterMembers = pGroup.getMembers();

      // Get the users to unsubscribe
      @SuppressWarnings("unchecked")
      final List<MailingListUser> toUnsubscribe = ListUtils.subtract(beforeMembers, afterMembers);
      if (!toUnsubscribe.isEmpty())
      {
        for (final MailingListUser listUser : toUnsubscribe)
        {
          groupSubscription.removeMember(getUser(listUser.getLogin(), listUser.getEmail()));
          // if the user doesn't own any other group and doesn't have any user subscription, we unsubscribe
          // him
          if (sympaSoapClient.isSubscriber(pConnector, pListName, listUser.getEmail()))
          {
            if ((!isMemberOfAnotherGroupSubscription(listUser.getLogin(), pListName))
                    && (!hasUserSubscription(pListName, listUser.getLogin())))
            {
              sympaSoapClient.removeSubscriber(pConnector, pListName, listUser.getEmail(), pQuietMode);
            }
          }
        }
      }

      // Get the users to subscribe
      @SuppressWarnings("unchecked")
      final List<MailingListUser> toSubscribe = ListUtils.subtract(afterMembers, beforeMembers);
      if (!toSubscribe.isEmpty())
      {
        for (final MailingListUser listUser : toSubscribe)
        {
          groupSubscription.addMember(getUser(listUser.getLogin(), listUser.getEmail()));
        }
      }

      // add the new subscribers to the list
      addSubscribersFromGroupSubscription(pConnector, pQuietMode, pListName, toSubscribe);

      // update the group subscription
      groupSubscription.setGroupName(pGroup.getName());
      subscriptionDAO.update(groupSubscription);
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(
          String.format(
              "an error occurred during adding group subscription with [listname=%s, group name=%s, group uuid=%s]",
              pListName, pGroup.getName(), pGroup.getUUID()), e);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeGroupSubscription(final SympaSoapConnector pConnector, final String pListName,
      final MailingListGroup pGroup, final boolean pQuietMode) throws MailingListServiceException
  {
    final String listname = getShortListName(pListName);
    LOG.info(String.format("Remove Group Subscription with [group=%s, listname=%s]", pGroup.getName(),
        listname));

    if (!subscriptionDAO.existGroupSubscription(listname, pGroup.getUUID()))
    {
      throw new MailingListServiceException(
          String
              .format(
                  "The group subscription with [listname=%s, group name=%s, group uuid=%s] can't be removed because it doesn't exist",
                  listname, pGroup.getName(), pGroup.getUUID()));
    }

    try
    {
      final GroupSubscription groupSubscription = subscriptionDAO.findByListAndGroup(listname,
          pGroup.getUUID());

      final Set<User> members = groupSubscription.getMembers();
      for (final User user : members)
      {
        if (sympaSoapClient.isSubscriber(pConnector, listname, user.getEmail()))
        {
          // if the user doesn't own any other group and doesn't have any user subscription, we unsubscribe
          // him
          if ((!isMemberOfAnotherGroupSubscription(user.getLogin(), listname)) && (!hasUserSubscription(listname,
                                                                                                        user.getLogin())))
          {
            sympaSoapClient.removeSubscriber(pConnector, listname, user.getEmail(), pQuietMode);
          }
        }
      }

      // remove the group subscription
      subscriptionDAO.delete(groupSubscription);
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(
          String.format(
              "an error occurred during removing group subscription with [listname=%s, group name=%s, group uuid=%s]",
              listname, pGroup.getName(), pGroup.getUUID()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeUserSubscription(final SympaSoapConnector pConnector, final String pListName,
      final MailingListUser pUserSubscriber, final boolean pQuietMode) throws MailingListServiceException
  {
    final String listname = getShortListName(pListName);
    LOG.info(String.format("Remove User Subscription with [login=%s, listname=%s]",
        pUserSubscriber.getLogin(), listname));

    try
    {
      // if the user has any user subscription, we remove it
      if (subscriptionDAO.existUserSubscription(listname, pUserSubscriber.getLogin()))
      {
        final UserSubscription userSubscription = subscriptionDAO.findByListAndUser(listname,
            pUserSubscriber.getLogin());
        subscriptionDAO.delete(userSubscription);
      }

      // if the user doesn't own any group, we unsubscribe him
      if (!isMemberOfGroupSubscriptions(pUserSubscriber.getLogin(), listname))
      {
        sympaSoapClient.removeSubscriber(pConnector, pListName, pUserSubscriber.getEmail(), pQuietMode);
      }
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(
          String.format(
              "a technical error occurred during removing user subscription with [subscriber=%s, mailinglist name=%s]",
              pUserSubscriber.getEmail(), listname), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MailingListSubscriber> checkGroupSubscriptions(final SympaSoapConnector pConnector,
                                                             final List<MailingListSubscriber> pSubscribersIn,
                                                             final String pListName) throws MailingListServiceException
  {
    final String listname = getShortListName(pListName);
    LOG.debug(String.format("Check Group Subscriptions with [listname=%s]", listname));

    final List<MailingListSubscriber> subscribersOut = new ArrayList<MailingListSubscriber>(pSubscribersIn);
    if ((subscribersOut != null) && (subscribersOut.size() > 0))
    {

      final List<MailingListGroup> groups = getGroupSubscriptions(listname, null);
      final boolean listHasGroups = (!groups.isEmpty());
      final List<MailingListSubscriber> subscribersToRemove = new ArrayList<MailingListSubscriber>();
      for (final MailingListSubscriber subscriber : subscribersOut)
      {
        final MailingListUser user = (MailingListUser) subscriber;
        // 1. we determine if user is internal
        if (listHasGroups && !user.isExternal())
        {
          // 2. if he is member of a group and if he hasn't user subscription, we remove him from the
          // subscribers list
          if (isMemberOfGroupSubscriptions(user.getLogin(), listname) && !hasUserSubscription(listname,
                                                                                              user.getLogin()))
          {
            subscribersToRemove.add(subscriber);
          }
        }
      }

      // we remove subscribers found in the loop
      subscribersOut.removeAll(subscribersToRemove);

      // We add the groups
      if (listHasGroups)
      {
        subscribersOut.addAll(groups);
      }
    }
    LOG.info(String.format("Subscribers for listname=%s are : %s", listname, subscribersOut));
    return subscribersOut;
  }

  private List<MailingListGroup> getGroupSubscriptions(final String pListName, final UUID excludeGroupUUID)
  {
    final List<MailingListGroup> groups = new ArrayList<MailingListGroup>();

    final List<GroupSubscription> groupSubscriptions = subscriptionDAO.findAllByList(pListName);
    for (final GroupSubscription groupSubscription : groupSubscriptions)
    {
      if ((excludeGroupUUID == null) || (excludeGroupUUID != null && !excludeGroupUUID.equals(groupSubscription
                                                                                                  .getGroupUUID())))
      {
        groups.add(toMailingListGroup(groupSubscription));
      }
    }

    return groups;
  }

  private MailingListGroup toMailingListGroup(final GroupSubscription pGroupSubscription)
  {
    final MailingListGroup mailingListGroup = new MailingListGroupImpl();
    mailingListGroup.setName(pGroupSubscription.getGroupName());
    mailingListGroup.setUUID(pGroupSubscription.getGroupUUID());
    for (final User user : pGroupSubscription.getMembers())
    {
      mailingListGroup.addMember(toListUser(user));
    }

    return mailingListGroup;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MailingListGroup getGroupSubscriber(final String pListName, final UUID pGroupUUID)
      throws MailingListServiceException
  {
    final String listname = getShortListName(pListName);

    final GroupSubscription groupSubscription = subscriptionDAO.findByListAndGroup(listname, pGroupUUID);

    final MailingListGroup group = new MailingListGroupImpl();
    group.setUUID(groupSubscription.getGroupUUID());
    group.setName(groupSubscription.getGroupName());
    for (final User member : groupSubscription.getMembers())
    {
      group.addMember(new MailingListUserImpl(member.getLogin(), member.getEmail()));
    }
    return group;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addUserSubscription(final SympaSoapConnector pConnector, final String pListname,
      final MailingListUser pSubscriber) throws MailingListServiceException
  {
    try
    {
      final List<MailingListUser> singleSubscriber = new ArrayList<MailingListUser>();
      singleSubscriber.add(pSubscriber);
      // filter the effective new subscribers (according to group subscriptions)
      final List<MailingListUser> newSubscribers = this.getNewEffectiveSubscribersWithUserSubscription(
          pConnector, pListname, singleSubscriber);
      if (!newSubscribers.isEmpty())
      {
        sympaSoapClient.addSubscriber(pConnector, pListname, pSubscriber.getEmail(), "", true);
      }
    }
    catch (final SympaSoapException e)
    {
      throw new MailingListServiceException(
          String.format(
              "an error occurred during adding subscriber from SOAP client with [subscriber=%s, mailinglist name=%s]",
              pSubscriber.getEmail(), pListname), e);
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(String.format(
          "a technical error occurred during adding subscriber with [subscriber=%s, mailinglist name=%s]",
          pSubscriber.getEmail(), pListname), e);
    }

  }

  private List<MailingListUser> getNewEffectiveSubscribersWithUserSubscription(final SympaSoapConnector pConnector,
                                                                               final String pListName,
                                                                               final List<MailingListUser> pUsers)
      throws SympaSoapException
  {
    return filterNewEffectiveSubscribers(pConnector, pListName, pUsers, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> addExternalSubscriptions(final SympaSoapConnector pConnector, final String pListname,
      final String pEmails, final boolean pQuietMode) throws MailingListServiceException
  {
    checkEmailsValidity(pEmails);
    try
    {
      return sympaSoapClient.addSubscribers(pConnector, pListname, pEmails, pQuietMode);
    }
    catch (final SympaSoapException e)
    {
      throw new MailingListServiceException(
          String.format(
              "an error occurred during adding subscribers from SOAP client with [subscribers=%s, mailinglist name=%s]",
              pEmails, pListname), e);
    }
  }

  private void checkEmailsValidity(final String pEmails) throws MailingListServiceException
  {
    try
    {
      final String[] emails = pEmails.split(";");
      Matcher matcher = null;
      for (final String email : emails)
      {
        matcher = emailPattern.matcher(email);
        if (!matcher.find())
        {
          throw new MailingListServiceException("At least one of the email doesn't support email regex.");
        }
      }
    }
    catch (final PatternSyntaxException e)
    {
      throw new MailingListServiceException("Invalid emails semicolon separated value.");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MailingListUser> addUsersSubscriptions(final SympaSoapConnector pConnector,
      final String pListname, final List<MailingListUser> pSubscribers, final boolean pQuietMode)
      throws MailingListServiceException
  {
    try
    {
      // filter the effective new subscribers (according to group subscriptions)
      final List<MailingListUser> newSubscribers = this.getNewEffectiveSubscribersWithUserSubscription(
          pConnector, pListname, pSubscribers);
      List<MailingListUser> returned = null;
      if (!newSubscribers.isEmpty())
      {
        final List<String> results = sympaSoapClient.addSubscribers(pConnector, pListname,
            toEmailsStr(newSubscribers), pQuietMode);
        if ((results != null) && (results.size() > 0))
        {
          returned = new ArrayList<MailingListUser>();
          for (final String result : results)
          {
            returned.add(getUser(newSubscribers, result));
          }
        }
      }
      return returned;
    }
    catch (final SympaSoapException e)
    {
      throw new MailingListServiceException(
          String.format(
              "an error occurred during adding subscribers from SOAP client with [subscribers=%s, mailinglist name=%s]",
              pSubscribers.toString(), pListname), e);
    }
    catch (final Exception e)
    {
      throw new MailingListServiceException(String.format(
          "a technical error occurred during adding subscriber with [subscribers=%s, mailinglist name=%s]",
          pSubscribers.toString(), pListname), e);
    }
  }

  private MailingListUser getUser(final List<MailingListUser> pUsers, final String pEmail)
  {
    MailingListUser returned = null;
    for (final MailingListUser user : pUsers)
    {
      if (user.getEmail().equals(pEmail))
      {
        returned = user;
        break;
      }
    }

    return returned;
  }

  private MailingListUser toListUser(final User pUser)
  {
    return new MailingListUserImpl(pUser.getLogin(), pUser.getEmail());
  }

  private boolean isMemberOfAnotherGroupSubscription(final String pLogin, final String pListName)
  {
    return (subscriptionDAO.findAllByListAndUser(pListName, pLogin).size() > 1);
  }

  /**
   * @param subscriptionDAO
   *     the subscriptionDAO to set
   */
  public void setSubscriptionDAO(final SubscriptionDAO pSubscriptionDAO)
  {
    this.subscriptionDAO = pSubscriptionDAO;
  }

  /**
   * @param userDAO
   *     the userDAO to set
   */
  public void setUserDAO(final UserDAO userDAO)
  {
    this.userDAO = userDAO;
  }

  /**
   * @param sympaSoapClient
   *     the sympaSoapClient to set
   */
  public void setSympaSoapClient(final SympaSoapClient sympaSoapClient)
  {
    this.sympaSoapClient = sympaSoapClient;
  }
}
