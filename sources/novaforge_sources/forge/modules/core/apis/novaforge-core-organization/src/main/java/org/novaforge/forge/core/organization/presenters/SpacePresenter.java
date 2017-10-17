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

import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Space;

import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public interface SpacePresenter
{

  /**
   * This method returns all the spaces of a project
   * 
   * @param projectId
   * @return List<Space>
   * @throws ProjectServiceException
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
   * @throws ProjectServiceException
   */
  Map<Space, List<ProjectApplication>> getAllSpacesWithApplications(final String pProjectId)
      throws SpaceServiceException;

  /**
   * This method returns a space by its uri
   * 
   * @param pProjectId
   *          the project id where to find the space
   * @param pSpaceNodeUri
   *          the space uri to find
   * @return {@link Space} found
   * @throws SpaceServiceException
   */
  Space getSpace(final String pProjectId, String pSpaceNodeUri) throws SpaceServiceException;

  /**
   * This method allows to update a {@link Space}
   * 
   * @param pSpace
   * @param pProjectId
   * @param pOldName
   * @return {@link Space} updated
   * @throws SpaceServiceException
   */
  Space updateSpace(final String pProjectId, final String pOldName, Space pSpace)
      throws SpaceServiceException;

  /**
   * This method allows to remove a space
   * 
   * @param pProjectId
   * @param pSpaceNodeUri
   * @throws SpaceServiceException
   */
  void removeSpace(final String pProjectId, String pSpaceNodeUri) throws SpaceServiceException;

  /**
   * This method allows to add a space to another {@link Space}
   * 
   * @param pProjectId
   * @param pSpace
   * @return {@link Space} created
   * @throws SpaceServiceException
   */
  Space addSpace(final String pProjectId, final Space pSpace) throws SpaceServiceException;

  /**
   * This method allows to instanciate a space
   * 
   * @return Space
   */
  Space newSpace();
}
