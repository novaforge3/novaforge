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

import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This interface describes a service which will manage application request
 * 
 * @author Guillaume Lamirand
 */
public interface ProjectApplicationRequestService
{

  /**
   * This method will create a new request for a {@link Project} according to its id and an
   * {@link ProjectApplication} according to its instance id.
   * 
   * @param pLogin
   *          represents the login of the user who mades the request
   * @param pProjectId
   *          represents the project id
   * @param pInstanceId
   *          represents the application instance id
   * @param pRoleMapping
   *          represents the role mapping defined
   * @throws ApplicationServiceException
   *           thrown if data access errors occured
   */
  void addRequest(String pLogin, final String pProjectId, final UUID pInstanceId,
      final Map<String, String> pRoleMapping) throws ApplicationServiceException;

  /**
   * This method will delete a request according to a {@link Project} and an {@link ProjectApplication}
   * 
   * @param pProjectId
   *          represents the project id
   * @param pInstanceId
   *          represents the application instance id
   * @throws ApplicationServiceException
   *           thrown if data access errors occured
   */
  void deleteRequest(final String pProjectId, final UUID pInstanceId) throws ApplicationServiceException;

  /**
   * This method will retrieve a {@link Set} of {@link ProjectApplicationRequest} according to a plugin
   * uuid given
   * 
   * @param pPluginUUID
   *          represents the plugin uuid
   * @return a {@link Set} of {@link ProjectApplicationRequest}
   * @throws ApplicationServiceException
   *           thrown if data access errors occured
   */
  List<ProjectApplicationRequest> getByPluginUUID(final UUID pPluginUUID) throws ApplicationServiceException;

  /**
   * This method will check if a request is already existing for a given project id and plugin uuid
   * 
   * @param pProjectId
   *          represents the project id
   * @param pPluginUUID
   *          represents the plugin uuid
   * @return a true if a request is existing, false otherwise
   * @throws ApplicationServiceException
   *           thrown if data access errors occured
   */
  boolean hasRequest(final String pProjectId, final UUID pPluginUUID) throws ApplicationServiceException;

  /**
   * This method allows to handle a request by link the application instance ID to the tool instance name.
   * Therefore the application is propagated to the plugin and the request is deleted.
   * 
   * @param pProjectApplicationRequest
   * @param pToolInstanceUUID
   * @throws ApplicationServiceException
   */
  void handleRequest(ProjectApplicationRequest pProjectApplicationRequest, UUID pToolInstanceUUID)
      throws ApplicationServiceException;

  /**
   * This method allows to handle the oldest request by link the application instance ID to the tool instance
   * ID. Therefore the application is propagated to the plugin and the request is deleted.
   * 
   * @param pToolInstanceUUID
   *          represents the tool instance id
   * @throws ApplicationServiceException
   *           thrown if data access errors occured
   */
  void handleOldestRequest(final UUID pToolInstanceUUID, final UUID pPluginUUID)
      throws ApplicationServiceException;
}
