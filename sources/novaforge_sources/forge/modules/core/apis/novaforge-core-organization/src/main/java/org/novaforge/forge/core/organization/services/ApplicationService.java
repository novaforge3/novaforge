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
import org.novaforge.forge.core.organization.model.ApplicationStatus;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface ApplicationService
{

  /**
   * This method allows to add an application to a project
   * 
   * @param projectId
   * @param parentNodeUri
   * @param applicationLabel
   * @param pDescription
   * @param pluginUUID
   * @param pRolesMapping
   * @param pUsername
   *          the username of the user which make the action
   * @return {@link ProjectApplication} created
   * @throws ApplicationServiceException
   */
  ProjectApplication addApplication(String projectId, String parentNodeUri, String applicationLabel,
      String pDescription, UUID pluginUUID, Map<String, String> pRolesMapping, String pUsername)
      throws ApplicationServiceException;

  /**
   * This method allows to remove an application from a project
   * 
   * @param projectId
   * @param applicationUri
   * @param pUsername
   *          the username of the user which make the action
   * @throws ApplicationServiceException
   */
  void deleteApplication(final String projectId, final String applicationUri, final String pUsername)
      throws ApplicationServiceException;

  /**
   * This method will be called when application has been successfuly deleted into plugin
   * 
   * @param pProjectId
   *          the project id
   * @param pInstanceUUID
   *          the application instance uuid
   * @param pToolInstanceUUID
   *          the tool instance uuid used by the application
   * @throws ApplicationServiceException
   *           thrown on any errors
   */
  void finalizeDeleteApplication(String pProjectId, String pInstanceUUID, String pToolInstanceUUID)
      throws ApplicationServiceException;

  /**
   * This method allows to remove an application from a project
   * 
   * @param pApplicationUri
   *          the application uri to update
   * @param pDescription
   *          the new description
   * @return {@link ProjectApplication} updated
   * @throws ApplicationServiceException
   */
  ProjectApplication updateDescription(final String pApplicationUri, final String pDescription)
      throws ApplicationServiceException;

  /**
   * This method returns an application by its uri
   * 
   * @param applicationUri
   * @return Application
   * @throws ApplicationServiceException
   */
  ProjectApplication getApplication(final String applicationUri) throws ApplicationServiceException;

  /**
   * This method returns an application by its instance uuid
   * 
   * @param pInstanceUUID
   * @return ProjectApplication
   * @throws ApplicationServiceException
   */
  ProjectApplication getApplication(final UUID pInstanceUUID) throws ApplicationServiceException;

  /**
   * This method returns all the applications of a project
   * 
   * @param projectId
   * @return List<ProjectApplication>
   * @throws ApplicationServiceException
   */
  List<ProjectApplication> getAllProjectApplications(String projectId) throws ApplicationServiceException;

  /**
   * This method returns all the applications of a project for the given list of plugin's uuid
   * 
   * @param pProjectId
   *          the project id
   * @param pPluginUUID
   *          plugin uuid
   * @return List<ProjectApplication>
   * @throws ApplicationServiceException
   */
  List<ProjectApplication> getAllProjectApplications(final String pProjectId, String... pPluginUUID)
      throws ApplicationServiceException;

  /**
   * This method returns applications with ACTIVE status and with an available plugin
   * 
   * @param pProjectId
   *          the project id
   * @return List<ProjectApplication> filtered
   * @throws ApplicationServiceException
   *           thrown if any errors occured
   */
  List<ProjectApplication> getAvailableApplications(String pProjectId) throws ApplicationServiceException;

  /**
   * This method allows to get the role mapping of a project and specific application
   * 
   * @param pApplicationUri
   * @param pUserName
   * @return map of roles
   * @throws ApplicationServiceException
   */
  Map<String, String> getRoleMapping(String pApplicationUri) throws ApplicationServiceException;

  /**
   * This method allows to update the role mapping of a project and specific application
   * 
   * @param pProjectId
   * @param pApplicationUri
   * @param pRoleMapping
   * @param pUserName
   * @throws ApplicationServiceException
   */
  void updateRoleMapping(final String pProjectId, final String pApplicationUri,
      Map<String, String> pRoleMapping, String pUserName) throws ApplicationServiceException;

  // methods which concern space
  /**
   * This method returns all the applications of a space
   * 
   * @param pSpaceNodeUri
   * @param pProjectId
   * @return List<ProjectApplication>
   * @throws ApplicationServiceException
   */
  List<ProjectApplication> getAllSpaceApplications(final String pSpaceNodeUri, String pProjectId)
      throws ApplicationServiceException;

  /**
   * This method will allow an role to acces with specific action [ to an application
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
   * @return {@link Set} of {@link ProjectApplication} found
   * @throws ApplicationServiceException
   */
  Set<ProjectApplication> getApplicationAccessForRole(String pProjectId, String pRoleName)
      throws ApplicationServiceException;

  /**
   * This method allows to change the application status
   * 
   * @param pApplicationStatus
   * @param pInstanceID
   * @throws ApplicationServiceException
   */
  void changeApplicationStatus(ApplicationStatus pApplicationStatus, String pInstanceID)
      throws ApplicationServiceException;

  /**
   * This method allows to move an application from an initial space to a target space
   * 
   * @param pFromSpaceUri
   *          the initial space
   * @param pToSpaceUri
   *          the target space
   * @param pApplicationUri
   *          the application uri
   * @throws ApplicationServiceException
   */
  void moveApplication(String pFromSpaceUri, String pToSpaceUri, String pApplicationUri)
      throws ApplicationServiceException;

}
