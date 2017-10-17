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

import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

/**
 * This class defines methods to access to {@link ProjectApplicationRequest} data from persistence
 * 
 * @author Guillaume Lamirand
 * @see ProjectApplicationRequest
 */
public interface ProjectApplicationRequestDAO
{
  /**
   * This method will return a new entity detach of persistence context
   * 
   * @return new {@link ProjectApplicationRequest}
   */
  ProjectApplicationRequest newProjectApplicationRequest();

  /**
   * This method allows to find {@link ProjectApplicationRequest} for a project and an application instance
   * 
   * @param pProjectId
   *          represents the project id
   * @param pInstanceUUID
   *          represents the application instance uuid
   * @return {@link List} of {@link ProjectApplicationRequest} found
   * @throws NoResultException
   */
  ProjectApplicationRequest findByProjectAndApp(final String pProjectId, final UUID pInstanceUUID)
      throws NoResultException;

  /**
   * This method allows to find {@link ProjectApplicationRequest} for a project
   * 
   * @param pProjectId
   *          represents the project id
   * @return {@link List} of {@link ProjectApplicationRequest} found
   */
  List<ProjectApplicationRequest> findByProject(final String pProjectId);

  /**
   * This method allows to find all {@link ProjectApplicationRequest} for a plugin uuid
   * 
   * @param pUUID
   *          represents the plugin uuid
   * @return {@link List} of {@link ProjectApplicationRequest} found
   */
  List<ProjectApplicationRequest> findByPlugin(final UUID pUUID);

  /**
   * This method allows to find the oldest {@link ProjectApplicationRequest}
   * 
   * @param pPluginUUID
   *          represents the plugin uuid
   * @return {@link List} of {@link ProjectApplicationRequest} found
   * @throws NoResultException
   *           thrown if no {@link ProjectApplicationRequest} are existing for the plugin uuid given
   */
  ProjectApplicationRequest findOldestByPlugin(final UUID pPluginUUID) throws NoResultException;

  /**
   * This method allows to find all {@link ProjectApplicationRequest} existing for a projcet and an plugin
   * 
   * @param pProjectId
   *          represents the project id
   * @param pUUID
   *          represents the plugin uuid
   * @return {@link List} of {@link ProjectApplicationRequest} found
   */
  List<ProjectApplicationRequest> findByProjectAndPlugin(final String pProjectId, final UUID pUUID);

  /**
   * This method will persist the object given in parameter
   * 
   * @param pProjectApplicationRequest
   *          the request to persist
   * @return {@link ProjectApplicationRequest} attached to persistence context
   */
  ProjectApplicationRequest persist(final ProjectApplicationRequest pProjectApplicationRequest);

  /**
   * This method will delete the object given in parameter
   * 
   * @param pProjectApplicationRequest
   *          the request to persist
   */
  void delete(final ProjectApplicationRequest pProjectApplicationRequest);

  /**
   * Return a n ew request entity
   * 
   * @return a new request entity
   */
  ProjectApplicationRequest newRequest();

  /**
   * Check if a request for the project and plugin is already existing
   * 
   * @param pProjectId
   *          the project id
   * @param pPluginUUID
   *          the plugin uuid
   * @return true if one exists, false otherwise
   */
  boolean existRequest(String pProjectId, UUID pPluginUUID);

}
