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
package org.novaforge.forge.plugins.mailinglist.sympa.model;

import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 *         This interface handles the group subscription for mailing lists
 */
public interface GroupSubscription
{
  /**
   * Returns the uuid of the group
   * 
   * @return UUID the uuid of the group
   */
  UUID getGroupUUID();

  /**
   * Allows to set the uuid of the group
   * 
   * @param pUUID
   */
  void setGroupUUID(final UUID pUUID);

  /**
   * Returns the name of the group
   * 
   * @return the name of the group
   */
  String getGroupName();

  /**
   * Allows to set the name of the group
   * 
   * @param pName
   */
  void setGroupName(String pName);

  /**
   * Returns the name of the list linked to the group subscription
   * 
   * @return the name of the list
   */
  String getListname();

  /**
   * Allows to set the name of the list
   * 
   * @param pName
   */
  void setListname(String pName);

  /**
   * Return the list of relative members of the group according to the list (if a user is already subscriber
   * of the list before the group subscription, he doesn't own this list)
   * 
   * @return the list of relative members of the group according to the list
   */
  Set<User> getMembers();

  /**
   * Allows to set the members of the list
   * 
   * @param pMembers
   */
  void setMembers(Set<User> pMembers);

  /**
   * Allows to add a relative member
   * 
   * @param pMember
   *          the member to add
   */
  void addMember(User pMember);

  /**
   * Allows to remove a relative member
   * 
   * @param pMember
   *          the member to remove
   */
  void removeMember(User pMember);

}
