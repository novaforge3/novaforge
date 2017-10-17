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
package org.novaforge.forge.plugins.mailinglist.sympa.services;

import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListGroup;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListServiceException;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListSubscriber;
import org.novaforge.forge.core.plugins.categories.mailinglist.MailingListUser;
import org.novaforge.forge.plugins.mailinglist.sympa.client.SympaSoapConnector;

import java.util.List;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface SympaSubscriptionDelegate
{
  /**
   * This method allows to add a group subscriber to a mailing list linked to an instance
   * 
   * @param pConnector
   * @param pListname
   * @param pSubscriber
   * @param pQuietMode
   * @throws MailingListServiceException
   */
  void addGroupsSubscriptions(SympaSoapConnector pConnector, String pListname,
      MailingListGroup pGroupSubscriber, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method allows to update a group subscriber to a mailing list linked to an instance
   * 
   * @param pConnector
   * @param pListname
   * @param pSubscriber
   * @param pQuietMode
   * @throws MailingListServiceException
   */
  void updateGroupSubscription(SympaSoapConnector pConnector, String pListname,
      MailingListGroup pGroupSubscriber, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method allows to remove a group subscriber to a mailing list linked to an instance
   * 
   * @param pConnector
   * @param pListname
   * @param pGroupSubscriber
   * @param pQuietMode
   * @throws MailingListServiceException
   */
  void removeGroupSubscription(SympaSoapConnector pConnector, String pListname,
      MailingListGroup pGroupSubscriber, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method allows to remove a user subscription to a mailing list linked to an instance
   * 
   * @param pConnector
   * @param pListname
   * @param pUserSubscriber
   * @param pQuietMode
   * @throws MailingListServiceException
   */
  void removeUserSubscription(SympaSoapConnector pConnector, String pListname,
      MailingListUser pUserSubscriberr, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method returns the subscribers with group subscriptions from a simple list of users
   * 
   * @param pConnector
   * @param pSubscribersIn
   * @param pListName
   * @return List<MailingListSubscriber> list of subscribers with GroupSubscriptions added
   * @throws MailingListServiceException
   */
  List<MailingListSubscriber> checkGroupSubscriptions(SympaSoapConnector pConnector,
      List<MailingListSubscriber> pSubscribersIn, String pListName) throws MailingListServiceException;

  /**
   * This method allows to return the group which subscribe to a mailing list
   * 
   * @param pListName
   * @param pGroupUUID
   * @return
   * @throws MailingListServiceException
   */
  MailingListGroup getGroupSubscriber(final String pListName, final UUID pGroupUUID)
      throws MailingListServiceException;

  /**
   * This method allows to add a new subscriber to a list
   * 
   * @param pConnector
   * @param pListname
   * @param pSubscriber
   * @throws MailingListServiceException
   */
  void addUserSubscription(SympaSoapConnector pConnector, final String pListname,
      final MailingListUser pSubscriber) throws MailingListServiceException;

  /**
   * This method allows to add external subscriptions
   * 
   * @param pConnector
   * @param pListname
   * @param pEmails
   * @param pQuietMode
   * @return the list of emails in error during subscription
   * @throws MailingListServiceException
   */
  List<String> addExternalSubscriptions(final SympaSoapConnector pConnector, final String pListname,
      final String pEmails, final boolean pQuietMode) throws MailingListServiceException;

  /**
   * @param pConnector
   * @param pListname
   * @param pSubscribers
   * @param pQuietMode
   * @return the list of MailingListUser in error during subscription
   * @throws MailingListServiceException
   */
  List<MailingListUser> addUsersSubscriptions(final SympaSoapConnector pConnector, final String pListname,
      final List<MailingListUser> pSubscribers, final boolean pQuietMode) throws MailingListServiceException;
}
