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
/**
 * 
 */
package org.novaforge.forge.core.organization.model;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;

import java.util.List;

/**
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
public interface Group extends Actor
{

  /**
   * This method returns the name of the {@link Group}
   * 
   * @return String
   */
  @Historizable(label = "name")
  String getName();

  /**
   * This method allows to set the name of the {@link Group}
   * 
   * @param name
   */
  void setName(String name);

  /**
   * Returns the description of the group
   * 
   * @return String
   */
  String getDescription();

  /**
   * Allows to set the description of the group
   * 
   * @param description
   */
  void setDescription(String description);

  /**
   * Returns the visibility of the group
   * 
   * @return boolean
   */
  boolean isVisible();

  /**
   * Allows to set the visibility of the grop
   * 
   * @param visibility
   */
  void setVisible(boolean visibility);

  /**
   * Returns the list of users of the group
   * 
   * @return List<User>
   */
  @Historizable(label = "Users")
  List<User> getUsers();

  /**
   * Allows to add a user to the group
   * 
   * @param user
   */
  void addUser(User user);

  /**
   * Allows to remove a user to the group
   * 
   * @param user
   */
  void removeUser(User user);

  /**
   * Allows to clear the user list
   */
  void clearUsers();

}
