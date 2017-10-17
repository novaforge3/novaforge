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
package org.novaforge.forge.core.organization.services;

import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;

import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public interface SpaceService
{

  /**
   * This method returns all the spaces of a project
   * 
   * @param projectId
   * @return List<Space>
   * @throws SpaceServiceException
   */
  List<Space> getAllSpaces(final String projectId) throws SpaceServiceException;

  /**
   * This method returns all the spaces and their applications of a project
   * 
   * @param pProjectId
   *          the project id
   * @return list of
   * @throws SpaceServiceException
   */
  Map<Space, List<ProjectApplication>> getAllSpacesWithApplications(final String pProjectId)
      throws SpaceServiceException;

  /**
   * This method returns a space by its uri
   * 
   * @param spaceNodeUri
   * @return Space
   * @throws SpaceServiceException
   */
  Space getSpace(final String projectId, String spaceNodeUri) throws SpaceServiceException;

  /**
   * This method allows to update a space
   * 
   * @param pProjectId
   * @param pOldName
   * @param pSpace
   * @return {@link Space}
   * @throws SpaceServiceException
   */
  Space updateSpace(final String pProjectId, final String pOldName, Space pSpace)
      throws SpaceServiceException;

  /**
   * This method allows to remove a space
   * 
   * @param spaceNodeUri
   * @throws SpaceServiceException
   */
  void deleteSpace(final String projectId, String spaceNodeUri) throws SpaceServiceException;

  /**
   * This method allows to add a space to another space
   * 
   * @param parentNodeUri
   * @param space
   * @return
   * @throws SpaceServiceException
   */
  Space addSpace(final String projectId, final Space pSpace) throws SpaceServiceException;

  /**
   * This method allows to instanciate a space
   * 
   * @return Space
   */
  Space newSpace();
}
