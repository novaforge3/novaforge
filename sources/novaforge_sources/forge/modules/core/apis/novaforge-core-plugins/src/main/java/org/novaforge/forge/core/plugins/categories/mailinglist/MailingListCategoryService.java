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
package org.novaforge.forge.core.plugins.categories.mailinglist;

import org.novaforge.forge.core.plugins.categories.PluginCategoryService;

import java.util.List;
import java.util.UUID;

/**
 * Interface containing all the functional services specific to this category.
 * 
 * @author benoist-s
 */
public interface MailingListCategoryService extends PluginCategoryService
{
  /**
   * This method allows to get all mailing lists associated to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @return List<MailingListBean>
   * @throws MailingListServiceException
   */
  List<MailingListBean> getMailingLists(String pForgeId, String pInstanceId, String pCurrentUser)
      throws MailingListServiceException;

  /**
   * This method allows to get the visible mailing lists associated to an instance in function of their
   * visibility and if the current user is subscribing to it
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @return List<MailingListBean>
   * @throws MailingListServiceException
   */
  List<MailingListBean> getVisibleMailingLists(String pForgeId, String pInstanceId, String pCurrentUser)
      throws MailingListServiceException;

  /**
   * This method allows to check if the given mailing list is existing
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @return true or false
   * @throws MailingListServiceException
   */
  boolean existMailingList(String pForgeId, String pInstanceId, String pCurrentUser, String pListname)
      throws MailingListServiceException;

  /**
   * This method allows to create a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pMailingList
   * @throws MailingListServiceException
   */
  void createMailingList(String pForgeId, String pInstanceId, String pCurrentUser,
      MailingListBean pMailingList) throws MailingListServiceException;

  /**
   * This method allows to update a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pMailingList
   * @throws MailingListServiceException
   */
  void updateMailingList(String pForgeId, String pInstanceId, String pCurrentUser,
      MailingListBean pMailingList) throws MailingListServiceException;

  /**
   * This method allows to close a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @throws MailingListServiceException
   */
  void closeMailingList(String pForgeId, String pInstanceId, String pCurrentUser, String pListname)
      throws MailingListServiceException;

  /**
   * This method allows to add a subscriber (member of the forge) to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pSubscriber
   * @throws MailingListServiceException
   */
  void addSubscriber(String pForgeId, String pInstanceId, String pCurrentUser, String pListname,
      MailingListUser pSubscriber) throws MailingListServiceException;

  /**
   * This method allows to add a group subscriber to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pSubscriber
   * @param pQuietMode
   * @throws MailingListServiceException
   */
  void addGroupSubscription(String pForgeId, String pInstanceId, String pCurrentUser, String pListname,
      MailingListGroup pGroupSubscriber, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method allows to add a list of group subscriber to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pGroupSubscribers
   * @param pQuietMode
   * @throws MailingListServiceException
   */
  void addGroupsSubscriptions(String pForgeId, String pInstanceId, String pCurrentUser, String pListname,
      List<MailingListGroup> pGroupSubscribers, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method allows to update a group subscriber to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pSubscriber
   * @param pQuietMode
   * @throws MailingListServiceException
   */
  void updateGroupSubscription(String pForgeId, String pInstanceId, String pCurrentUser, String pListname,
      MailingListGroup pGroupSubscriber, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method allows to return mailinglist group subscribed to a listname
   * 
   * @param pListName
   * @param pGroupUUID
   * @return MailingListGroup
   */
  MailingListGroup getGroupSubscriber(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, String pListName, UUID pGroupUUID) throws MailingListServiceException;

  /**
   * This method allows to add a list of subscribers (members of the forge) to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pSubscribers
   * @param pQuietMode
   * @return List<MailingListUser> the users on error during the subscription
   * @throws MailingListServiceException
   */
  List<MailingListUser> addSubscribers(String pForgeId, String pInstanceId, String pCurrentUser,
      String pListname, List<MailingListUser> pSubscribers, boolean pQuietMode)
      throws MailingListServiceException;

  /**
   * This method allows to add a list of external (not members of the forge) subscribers to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pEmails
   *          a semicolon separated value of emails
   * @param pQuietMode
   * @return List<String> the users on error during the subscription
   * @throws MailingListServiceException
   */
  List<String> addExternalSubscribers(String pForgeId, String pInstanceId, String pCurrentUser,
      String pListname, String pEmails, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method allows to remove a subscriber to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pSubscriber
   * @param pQuietMode
   * @throws MailingListServiceException
   */
  void removeSubscriber(String pForgeId, String pInstanceId, String pCurrentUser, String pListname,
      MailingListUser pSubscriber, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method allows to remove a group subscriber to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pSubscriber
   * @param pQuietMode
   * @throws MailingListServiceException
   */
  void removeGroupSubscription(String pForgeId, String pInstanceId, String pCurrentUser, String pListname,
      MailingListGroup pGroupSubscriber, boolean pQuietMode) throws MailingListServiceException;

  /**
   * This method allows to add an owner to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pSubscriber
   * @throws MailingListServiceException
   */
  void addOwner(String pForgeId, String pInstanceId, String pCurrentUser, String pListname,
      MailingListUser pSubscriber) throws MailingListServiceException;

  /**
   * This method allows to remove an owner to a mailing list linked to an instance
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pListname
   * @param pSubscriber
   * @throws MailingListServiceException
   */
  void removeOwner(String pForgeId, String pInstanceId, String pCurrentUser, String pListname,
      MailingListUser pSubscriber) throws MailingListServiceException;

  /**
   * * This method allows to instanciate a MailingListBean
   * 
   * @return MailingListBean
   */
  MailingListBean newMailingList();
}
