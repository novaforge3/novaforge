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

import org.novaforge.forge.core.plugins.categories.PluginExchangeableBean;

import java.util.List;

/**
 * @author sbenoist
 */
public interface MailingListBean extends PluginExchangeableBean
{
  /**
   * This method gets the type of the mailinglist
   * 
   * @return MailingListType
   */
  MailingListType getType();

  /**
   * This method sets the type of the mailinglist
   * 
   * @param pType
   */
  void setType(MailingListType pType);

  /**
   * This method gets the name of the mailinglist
   * 
   * @return String
   */
  String getName();

  /**
   * This method sets the name of the mailinglist
   * 
   * @param pName
   */
  void setName(String pName);

  /**
   * This method gets the description of the mailinglist
   * 
   * @return String
   */
  String getDescription();

  /**
   * This method sets the description of the mailinglist
   * 
   * @param pDescription
   */
  void setDescription(String pDescription);

  /**
   * This method gets the subject of the mailinglist
   * 
   * @return String
   */
  String getSubject();

  /**
   * This method sets the subject of the mailing list
   * 
   * @param pSubject
   */
  void setSubject(String pSubject);

  /**
   * This method returns true if the list is locked, false elsewhere
   * 
   * @return boolean
   */
  boolean isLocked();

  /**
   * This method allows to set the lock on the list
   * 
   * @param pLocked
   */
  void setLocked(boolean pLocked);

  /**
   * This method gets the subscribers of the mailinglist
   * 
   * @return List<MailingListUser>
   */
  List<MailingListSubscriber> getSubscribers();

  /**
   * This method allows to set the subscribers
   * 
   * @param pSubscribers
   */
  void setSubscribers(List<MailingListSubscriber> pSubscribers);

  /**
   * This method gets the owners of the mailinglist
   * 
   * @return List<MailingListUser>
   */
  List<MailingListUser> getOwners();

  /**
   * This method allows to set the owners
   * 
   * @param pOwners
   */
  void setOwners(List<MailingListUser> pOwners);

  /**
   * This method allows to add a subscriber to the list of subscribers
   * 
   * @param pSubscriber
   */
  void addSubscriber(MailingListSubscriber pSubscriber);

  /**
   * This method allows to add a list of groups subscribers to the list of subscribers
   * 
   * @param pSubscriber
   */
  void addGroupsSubscribers(List<MailingListGroup> pGroupsSubscribers);

  /**
   * This method allows to remove a subscriber to the list of subscribers
   * 
   * @param pSubscriber
   */
  void removeSubscriber(MailingListSubscriber pSubscriber);

  /**
   * This method allows to add an owner to the list of owners
   * 
   * @param pUser
   */
  void addOwner(MailingListUser pUser);

  /**
   * This method allows to remove an owner to the list of owners
   * 
   * @param pUser
   */
  void removeOwner(MailingListUser pUser);

}
