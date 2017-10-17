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
package org.novaforge.forge.core.organization.dao;

import org.novaforge.forge.core.organization.model.ProjectElement;

import javax.persistence.NoResultException;

/**
 * This class defines methods to access to {@link ProjectElement} data from persistence
 * 
 * @author Guillaume Lamirand
 * @see ProjectElement
 */
public interface ProjectElementDAO
{
  /**
   * Will retrieve the {@link ProjectElement} associated to the element id given
   * 
   * @param pElementId
   *          the element id to found
   * @return a {@link ProjectElement} found
   * @throws NoResultException
   *           thrown if no {@link ProjectElement} are existing for the name given
   */
  ProjectElement findByElementId(final String pElementId) throws NoResultException;

  /**
   * Check if a {@link ProjectElement} with the given id is existing
   * 
   * @param pElementId
   *          the element id to check
   * @return true if a {@link ProjectElement} exists for the parameter given, false otherwise
   */
  boolean existId(final String pElementId);

  /**
   * Check if a {@link ProjectElement} with the given name is existing
   * 
   * @param pElementName
   *          the element name to check
   * @return true if a {@link ProjectElement} exists for the parameter given, false otherwise
   */
  boolean existName(final String pElementName);

  /**
   * Will only update the last modified date of {@link ProjectElement}
   * 
   * @param pElementId
   * @return number of entity
   */
  int updateLastModified(final String pElementId);

}
