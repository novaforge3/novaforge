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

import java.util.List;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface MailingListGroup extends MailingListSubscriber
{
  /**
   * Returns the unique uuid of the group
   * 
   * @return UUID the unique uuid of the group
   */
  UUID getUUID();

  /**
   * Allows to set the uuid of the group
   * 
   * @param pUUID
   */
  void setUUID(final UUID pUUID);

  /**
   * Returns the name of the group
   * 
   * @return the name of the group
   */
  String getName();

  /**
   * Allows to set the name of the group
   * 
   * @param pName
   */
  void setName(String pName);

  /**
   * Return the list of members
   * 
   * @return the list of members
   */
  List<MailingListUser> getMembers();

  /**
   * Allows to set the list of members
   * 
   * @param pMembers
   */
  void setMembers(List<MailingListUser> pMembers);

  /**
   * Allows to add a member to the group
   * 
   * @param pMember
   *          the member to add
   */
  void addMember(MailingListUser pMember);

  /**
   * Allows to remove a member to the group
   * 
   * @param pMember
   *          the member to remove
   */
  void removeMember(MailingListUser pMember);
}
