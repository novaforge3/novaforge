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

import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * This interface describes a service used by the ui to manage {@link ProjectApplication}
 * 
 * @author sbenoist
 * @see ProjectApplication
 */
public interface ApplicationPresenter
{

  /**
   * This method allows to add an application to a project
   * 
   * @param pProjectId
   *          the project where to add application
   * @param pParentNodeUri
   *          the parent uri
   * @param pApplicationLabel
   *          the application name
   * @param pDescription
   *          the application description
   * @param pPluginUUID
   * @param pRolesMapping
   * @return {@link ProjectApplication} created
   * @throws ApplicationServiceException
   */
  ProjectApplication addApplication(final String pProjectId, final String pParentNodeUri,
      final String pApplicationLabel, final String pDescription, final UUID pPluginUUID,
      final Map<String, String> pRolesMapping) throws ApplicationServiceException;

  /**
   * This method allows to remove an application from a project
   * 
   * @param pProjectId
   * @param pApplicationUri
   * @param pDescription
   * @return {@link ProjectApplication} updated
   * @throws ApplicationServiceException
   */
  ProjectApplication updateDescription(String pProjectId, String pApplicationUri, String pDescription)
      throws ApplicationServiceException;

  /**
   * This method allows to update the description of an application from a project
   * 
   * @param pProjectId
   * @param pApplicationUri
   * @throws ApplicationServiceException
   */
  void removeApplication(final String pProjectId, final String pApplicationUri)
      throws ApplicationServiceException;

  /**
   * This method returns an application by its uri
   * 
   * @param pProjectId
   * @param pApplicationUri
   * @return ProjectApplication
   * @throws ApplicationServiceException
   */
  ProjectApplication getApplication(final String pProjectId, final String pApplicationUri)
      throws ApplicationServiceException;

  /**
   * This method returns an application by its instance uuid
   * 
   * @param pProjectId
   * @param pInstanceUUID
   * @return ProjectApplication
   * @throws ApplicationServiceException
   */
  ProjectApplication getApplication(final String pProjectId, final UUID pInstanceUUID)
      throws ApplicationServiceException;

  /**
   * This method returns all the applications of a project
   * 
   * @param projectId
   * @return List<Application>
   * @throws ApplicationServiceException
   */
  List<ProjectApplication> getAllProjectApplications(String projectId) throws ApplicationServiceException;

  /**
   * This method returns all the applications of a project for the given list of plugin's uuid
   * 
   * @param pProjectId
   *          the project id
   * @param pPluginUUID
   *          list of plugins' uuid
   * @return List<ProjectApplication>
   * @throws ApplicationServiceException
   */
  List<ProjectApplication> getAllProjectApplications(String pProjectId, String... pPluginUUID)
      throws ApplicationServiceException;

  /**
   * This method allows to get the role mapping of a project and specific
   * application
   * 
   * @param pProjectId
   * @param pApplicationUri
   * @param pUserName
   * @return map of roles
   * @throws ApplicationServiceException
   */
  Map<String, String> getRoleMapping(String pProjectId, String pApplicationUri)
      throws ApplicationServiceException;

  /**
   * This method allows to update the role mapping of a project and specific
   * application
   * 
   * @param pProjectId
   * @param pApplicationUri
   * @param pRoleMapping
   * @param pUserName
   * @throws ApplicationServiceException
   */
  void updateRoleMapping(final String pProjectId, final String pApplicationUri,
      Map<String, String> pRoleMapping) throws ApplicationServiceException;

  // methods which concern space
  /**
   * This method returns all the applications of a space
   * 
   * @param pSpaceNodeUri
   * @param pProjectId
   * @return List<Application>
   * @throws ApplicationServiceException
   */
  List<ProjectApplication> getAllSpaceApplications(final String pSpaceNodeUri, final String pProjectId)
      throws ApplicationServiceException;

  /**
   * This method will allow an role to acces with specific action [ to an
   * application
   * 
   * @param pProjectId
   * @param pRoleName
   * @param pApplicationUri
   * @param pActions
   * @throws ApplicationServiceException
   */
  void addApplicationAccessToRole(final String pProjectId, final String pRoleName,
      final String pApplicationUri, final PermissionAction... pActions) throws ApplicationServiceException;

  /**
   * @param pProjectId
   * @param pRoleName
   * @param pApplicationUri
   * @param pActions
   * @throws ApplicationServiceException
   */
  void updateApplicationAccessToRole(final String pProjectId, final String pRoleName,
      final String pApplicationUri, final PermissionAction... pActions) throws ApplicationServiceException;

  /**
   * @param pProjectId
   * @param pRoleName
   * @return set of {@link ProjectApplication} found
   * @throws ApplicationServiceException
   */
  Set<ProjectApplication> getApplicationAccessForRole(String pProjectId, String pRoleName)
      throws ApplicationServiceException;

}
