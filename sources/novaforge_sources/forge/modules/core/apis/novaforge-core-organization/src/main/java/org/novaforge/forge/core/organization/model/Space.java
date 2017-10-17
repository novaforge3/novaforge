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

import org.novaforge.forge.core.organization.model.enumerations.RealmType;

/**
 * This is the behavior of a space node into the forge
 * No more specific behavior for the moment...
 * 
 * @author sbenoist
 */
public interface Space extends Node
{
  /**
   * This method returns the project element associated
   *
   * @return {@link ProjectElement}
   */
  ProjectElement getProjectElement();

  /**
   * This method allows to set the uri of the node
   * 
   * @param uri
   */
  @Override
  void setUri(String uri);

  /**
   * This method returns the realm type associated to the space
   *
   * @return
   */
  RealmType getRealmType();

  /**
   * This method returns the uri of the node
   * 
   * @return String
   */
  @Override
  String getUri();

  /**
   * This method allows to set the realm type of the space
   *
   * @param pRealmType
   */
  void setRealmType(RealmType pRealmType);

  /**
   * This method allows to set the name of the node
   * 
   * @param name
   */
  @Override
  void setName(String name);

  /**
   * This method returns the name of the node
   * 
   * @return String
   */
  @Override
  String getName();

}
