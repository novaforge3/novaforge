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
package org.novaforge.forge.core.organization.model;

import org.novaforge.forge.commons.technical.historization.annotations.Historizable;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;

import java.io.Serializable;

/**
 * This is the behavior of a role into the forge
 * 
 * @author sbenoist
 * @author BILET-JC
 * @author lamirang
 */
public interface Role extends Comparable<Role>, Serializable
{
  /**
   * this method returns the name of the role
   * 
   * @return String
   */
  @Historizable(label = "name")
  String getName();

  /**
   * This method allows to set the name of the role
   * 
   * @param name
   */
  void setName(String name);

  /**
   * this method returns the name of the role
   * 
   * @return String
   */
  String getDescription();

  /**
   * This method allows to set the description of the role
   * 
   * @param description
   */
  void setDescription(String description);

  /**
   * this method returns the order of the role
   * 
   * @return Integer
   */
  Integer getOrder();

  /**
   * This method allows to set the order of the role
   * 
   * @param pOrder
   *          the order to set
   */
  void setOrder(Integer pOrder);

  /**
   * This method returns the type of role
   * 
   * @return type of role
   */
  RealmType getRealmType();

  /**
   * This method set the type of role
   * 
   * @param pRealmType
   *          the type to set
   */
  void setRealmType(final RealmType pRealmType);

}
