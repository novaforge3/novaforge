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

import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.organization.model.Project;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to {@link Composition} data from persistence
 * 
 * @author Guillaume Lamirand
 * @see Composition
 */
public interface CompositionDAO
{
  /**
   * This method will return a new entity detach of persistence context
   * 
   * @return new {@link Composition}
   */
  Composition newComposition();

  /**
   * Find a {@link Composition} from {@link Application}'s uri source
   * 
   * @param pInstance
   *          represents the instance of source application
   * @return {@link List} of {@link Composition} according to parameter
   */
  List<Composition> findBySource(final String pInstance);

  /**
   * Find a {@link Composition} from {@link Project}'s projectId
   * 
   * @param pProjectId
   *          represents the projectId of the project
   * @return {@link List} of {@link Composition} according to parameter
   */
  List<Composition> findByProject(final String pProjectId);

  /**
   * Find {@link Composition} according to a {@link Application} and event string
   * 
   * @param pInstance
   *          represents the instance uuid of the source application
   * @param pType
   *          represents the type of source association
   * @param pName
   *          represents the name of source association
   * @return a {@link List} of {@link Composition}
   */
  List<Composition> findBySourceAndAssociation(final String pInstance, CompositionType pType,
      final String pName);

  /**
   * Find {@link Composition} according to its uuid
   * 
   * @param pUUID
   *          represents the composition uuid to find
   * @return {@link Composition} found
   * @throws NoResultException
   *           thrown if no {@link Composition} are existing for the login given
   */
  Composition findByUUID(final String pUUID) throws NoResultException;

  /**
   * This will update {@link Composition} into persistence context
   * 
   * @param pComposition
   *          the composition to update
   * @return {@link Composition} updated and attach to persistence context
   */
  Composition update(final Composition pComposition);
}