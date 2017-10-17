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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * This is the behavior of a project into the forge
 * 
 * @author sbenoist
 */
public interface ProjectElement extends Serializable
{

  /**
   * This method returns the projectId of the project
   * 
   * @return String
   */
  String getElementId();

  /**
   * This method allows to set the projectId of the project
   * 
   * @param pElementId
   */
  void setElementId(final String pElementId);

  /**
   * This method returns the name of the project
   * 
   * @return String
   */
  @Historizable(label = "projectId")
  String getName();

  /**
   * This method allows to set the name of the project
   * 
   * @param name
   */
  void setName(final String name);

  /**
   * This method returns the description of the project
   * 
   * @return String
   */
  String getDescription();

  /**
   * This method allows to set the description of the project
   * 
   * @param description
   */
  void setDescription(final String description);

  /**
   * This method returns the date of creation of the project
   * 
   * @return {@link Date} of creation
   */
  Date getCreated();

  /**
   * This method returns the date of the last modification of the project
   * 
   * @return Date
   */
  Date getLastModified();

  /**
   * Returns {@link List} of {@link Space}
   *
   * @return {@link List} of {@link Space}
   */
  List<Space> getSpaces();

  /**
   * Sets spaces list
   *
   * @param pSpaces
   *          the list to set
   */
  void setSpaces(final List<Space> pSpaces);

  /**
   * Add a space to project element
   * 
   * @param pSpace
   *          the space to add
   */
  void addSpace(final Space pSpace);

  /**
   * Remove a space from project element
   * 
   * @param pSpace
   *          the space to remove
   */
  void removeSpace(final Space pSpace);

}
