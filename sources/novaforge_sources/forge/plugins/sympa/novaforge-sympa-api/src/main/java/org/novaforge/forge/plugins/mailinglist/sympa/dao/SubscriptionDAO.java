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
package org.novaforge.forge.plugins.mailinglist.sympa.dao;

import org.novaforge.forge.plugins.mailinglist.sympa.model.GroupSubscription;
import org.novaforge.forge.plugins.mailinglist.sympa.model.UserSubscription;

import java.util.List;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface SubscriptionDAO
{
  /**
   * Returns a GroupSubscription reference
   * 
   * @param pGroupUUID
   * @param pGroupName
   * @param pListName
   * @return a GroupSubscription reference
   */
  GroupSubscription newGroupSubscription(UUID pGroupUUID, String pGroupName, String pListName);

  /**
   * Returns the list of group subscriptions for a given group
   * 
   * @param pGroupUUID
   * @return the list of group subscriptions found
   */
  List<GroupSubscription> findAllByGroup(UUID pGroupUUID);

  /**
   * Returns the list of group subscription for a given list
   * 
   * @param pListName
   * @return the list of group subscriptions found
   */
  List<GroupSubscription> findAllByList(String pListName);

  /**
   * Returns the list of group subscriptions for which owns a user for a given list
   * 
   * @param pListName
   * @return the list of group subscriptions found
   */
  List<GroupSubscription> findAllByListAndUser(String pListName, String pLogin);

  /**
   * Allows to persist the group subscription
   * 
   * @param pGroupSubscription
   * @return GroupSubscription the group subscription to persist
   */
  GroupSubscription persist(GroupSubscription pGroupSubscription);

  /**
   * Allows to delete the group subscription
   * 
   * @param pGroupSubscription
   *          the group subscription to delete
   */
  void delete(GroupSubscription pGroupSubscription);

  /**
   * Allows to update the group subscription
   * 
   * @param pGroupSubscription
   * @return GroupSubscription the group subscription to update
   */
  GroupSubscription update(GroupSubscription pGroupSubscription);

  /**
   * Returns the group subscription according to a listname and a group
   * 
   * @param pListName
   * @param pGroupUUID
   * @return GroupSubscription the group subscription
   */
  GroupSubscription findByListAndGroup(String pListName, UUID pGroupUUID);

  /**
   * Returns true if the group subscription exists, false elsewhere
   * 
   * @param pListName
   * @param pGroupUUID
   * @return true if the group subscription exists, false elsewhere
   */
  boolean existGroupSubscription(String pListName, UUID pGroupUUID);

  /**
   * Returns a UserSubscription reference
   * 
   * @param pLogin
   * @param pListName
   * @return a UserSubscription reference
   */
  UserSubscription newUserSubscription(String pLogin, String pListName);

  /**
   * Allows to persist the user subscription
   * 
   * @param pUserSubscription
   * @return UserSubscription the user subscription to persist
   */
  UserSubscription persist(UserSubscription pUserSubscription);

  /**
   * Allows to delete the user subscription
   * 
   * @param pUserSubscription
   *          the user subscription to delete
   */
  void delete(UserSubscription pUserSubscription);

  /**
   * Returns the user subscription according to a listname and a user login
   * 
   * @param pListName
   * @param pLogin
   * @return UserSubscription the user subscription
   */
  UserSubscription findByListAndUser(String pListName, String pLogin);

  /**
   * Returns true if the user subscription exists, false elsewhere
   * 
   * @param pListName
   * @param pLogin
   * @return true if the user subscription exists, false elsewhere
   */
  boolean existUserSubscription(String pListName, String pLogin);
}
