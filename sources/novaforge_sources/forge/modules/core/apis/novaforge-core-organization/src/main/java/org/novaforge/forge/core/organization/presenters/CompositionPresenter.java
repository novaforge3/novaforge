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
package org.novaforge.forge.core.organization.presenters;

import org.novaforge.forge.core.organization.exceptions.CompositionServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.organization.model.Project;

import java.util.List;
import java.util.Map;

/**
 * @author lamirang
 */
public interface CompositionPresenter
{
  /**
   * @return a new instance of a composition object
   */
  Composition newComposition();

  /**
   * Allow to create a {@link Composition} according to a {@link Project}, source {@link Application}, and
   * target {@link Application}
   * 
   * @param pProjectId
   *          represents the project id
   * @param pSourceUri
   *          represents the source application uri
   * @param pTargetUri
   *          represents the target application uri
   * @param pTemplate
   *          represents the template
   * @return composition created
   * @throws CompositionServiceException
   */
  Composition createCompositionWithTemplate(String pProjectId, Composition pComposition, String pSourceUri,
      String pTargetUri, final Map<String, String> pTemplate) throws CompositionServiceException;

  /**
   * Allow to create a {@link Composition} according to a {@link Project}, source {@link Application}, and
   * target {@link Application}
   * 
   * @param pProjectId
   *          represents the project id
   * @param pSourceUri
   *          represents the source application uri
   * @param pTargetUri
   *          represents the target application uri
   * @return composition created
   * @throws CompositionServiceException
   */
  Composition createComposition(String pProjectId, Composition pComposition, String pSourceUri,
      String pTargetUri) throws CompositionServiceException;

  /**
   * Allow to remove a composition from its specific uuid
   * 
   * @param pProject
   *          represents the project id
   * @param pUUID
   *          represents composition's uuid
   * @return true if succeed
   * @throws CompositionServiceException
   */
  boolean deleteComposition(final String pProjectId, final String pUUID) throws CompositionServiceException;

  /**
   * Find {@link Composition} according to a {@link Project}
   * 
   * @param pProject
   *          represents the project id
   * @return the list of application communication entity existing
   * @throws CompositionServiceException
   */
  List<Composition> getComposition(final String pProjectId) throws CompositionServiceException;

  /**
   * Find {@link Composition} according to a {@link Application} and event string
   * 
   * @param pProjectId
   * @param pSourceInstance
   * @param pSourceType
   * @param pSourceName
   * @returnthe list of {@link Composition}
   * @throws CompositionServiceException
   */
  List<Composition> getCompositionFromSource(final String pProjectId, final String pSourceInstance,
      final CompositionType pSourceType, final String pSourceName) throws CompositionServiceException;

  /**
   * Find a list of {@link Composition} according to an {@link Application}
   * 
   * @param pProjectId
   * @param pSourceInstance
   * @return a list of {@link Composition}
   * @throws CompositionServiceException
   */
  List<Composition> getCompositionFromSource(String pProjectId, String pSourceInstance)
      throws CompositionServiceException;

  /**
   * Update the template of a composition according to its uuid
   * 
   * @param pProjectId
   * @param pUUID
   * @param pTemplate
   * @throws CompositionServiceException
   */
  void updateCompositionTemplate(final String pProjectId, final String pUUID,
      final Map<String, String> pTemplate) throws CompositionServiceException;

  /**
   * Update the composition status
   * 
   * @param pProjectId
   * @param pUUID
   * @param pStatus
   * @throws CompositionServiceException
   */
  void setCompositionStatus(final String pProjectId, final String pUUID, final boolean pStatus)
      throws CompositionServiceException;

}