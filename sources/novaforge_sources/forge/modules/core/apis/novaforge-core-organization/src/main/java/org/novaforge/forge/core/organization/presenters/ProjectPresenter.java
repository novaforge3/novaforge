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
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectInfo;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;

import java.util.List;

/**
 * @author sbenoist
 */
public interface ProjectPresenter
{
  /**
   * This method allows to instanciate a projects options
   * 
   * @param pRetrieveOrganization
   *          to return the organization entity attached to the project
   * @param pRetrieveImage
   *          to return the image binary entity attached to the project
   * @param pRetrieveSystem
   *          to return the system projects
   * @return {@link ProjectOptions}
   */
  ProjectOptions newProjectOptions(final boolean pRetrieveOrganization, final boolean pRetrieveImage,
      final boolean pRetrieveSystem);

  /**
   * This method allows to instanciate a projects options
   * 
   * @return {@link ProjectOptions}
   */
  ProjectOptions newProjectOptions();

  /**
   * This method allows to instanciate a project
   * 
   * @return Project
   */
  Project newProject();

  /**
   * This method allows to create a system project
   * 
   * @param project
   * @param author
   * @throws ProjectServiceException
   */
  void createSystemProject(Project project) throws ProjectServiceException;

  /**
   * This method allows to create a user project
   * 
   * @param project
   * @param pTemplateId
   * @param author
   * @throws ProjectServiceException
   */
  void createProject(Project project) throws ProjectServiceException;

  /**
   * This method allows to create a user project with a template id
   * 
   * @param project
   * @param pTemplateId
   * @param author
   * @throws ProjectServiceException
   */
  void createProjectFromTemplate(Project project, final String pTemplateId) throws ProjectServiceException;

  /**
   * This method returns all the validated projects in the forge
   * 
   * @param pWithIcon
   *          if true the projects returned will contain image binary
   * @return {@link List} of {@link Project}
   * @throws ProjectServiceException
   */
  List<Project> getAllProjects(final boolean pWithIcon) throws ProjectServiceException;

  /**
   * This method returns all the validated projects in the forge
   * 
   * @param pProjectOptions
   *          the options used to retrieve project objects
   * @return {@link List} of {@link Project}
   * @throws ProjectServiceException
   */
  List<Project> getAllProjects(final ProjectOptions pProjectOptions) throws ProjectServiceException;

  /**
   * This method returns a project by its projectId
   * 
   * @param pProjectId
   * @param pWithIcon
   *          if true the projects returned will contain image binary
   * @return {@link Project}
   * @throws ProjectServiceException
   */
  Project getProject(final String pProjectId, final boolean pWithIcon) throws ProjectServiceException;

  /**
   * This method returns a {@link Project} by its projectId
   * 
   * @param pProjectId
   * @param pProjectOptions
   *          the options used to retrieve project objects
   * @return {@link Project} found
   * @throws ProjectServiceException
   *           if data access errors occured
   */
  Project getProject(final String pProjectId, final ProjectOptions pProjectOptions)
      throws ProjectServiceException;

  /**
   * This method returns a project info by its projectId
   * 
   * @param projectId
   * @return ProjectInfo
   * @throws ProjectServiceException
   */
  ProjectInfo getProjectInfo(String projectId) throws ProjectServiceException;

  /**
   * This method allows to update a project
   * 
   * @param pOldName
   *          the previous name
   * @param pProject
   *          the new project
   * @throws ProjectServiceException
   */
  void updateProject(final String pOldName, final Project pProject) throws ProjectServiceException;

  /**
   * This method allows to delete a project
   * 
   * @param projectId
   * @throws ProjectServiceException
   */
  void deleteProject(String projectId) throws ProjectServiceException;

  /**
   * This method allows to validate a project
   * 
   * @param projectId
   * @throws ProjectServiceException
   */
  void validateProject(String projectId) throws ProjectServiceException;

  /**
   * This method allows to reject a project with a justified reason
   * 
   * @param pProjectId
   * @param pReason
   * @throws ProjectServiceException
   */
  void rejectProject(String pProjectId, String pReason) throws ProjectServiceException;

  /**
   * This method returns all the projects in the forge with the project status choosen
   * 
   * @param pProjectOptions
   *          the options used to retrieve project objects
   * @param pProjectStatus
   * @return {@link List} of {@link Project}
   * @throws ProjectServiceException
   */
  List<Project> getAllProjectsByStatus(final ProjectOptions pProjectOptions,
      final ProjectStatus... pProjectStatus) throws ProjectServiceException;

  /**
   * This method returns all the projects in the forge except those of the current user
   * 
   * @param pProjectOptions
   *          the options used to retrieve project objects
   * @return {@link List} of {@link Project}
   * @throws ProjectServiceException
   */
  List<Project> getValidatedProjects(final ProjectOptions pProjectOptions) throws ProjectServiceException;

  /**
   * This method returns all the projects in the forge for the current user
   * 
   * @param pProjectOptions
   *          the options used to retrieve project objects
   * @param pLogin
   *          login for current user
   * @return {@link List} of {@link Project}
   * @throws ProjectServiceException
   */
  List<Project> getValidatedProjects(final String pLogin, final ProjectOptions pProjectOptions)
      throws ProjectServiceException;

  /**
   * This method returns all the visible projects in the forge except those of the current user
   * 
   * @param pProjectOptions
   *          the options used to retrieve project objects
   * @return {@link List} of {@link Project}
   * @throws ProjectServiceException
   */
  List<Project> getPublicProjects(final ProjectOptions pProjectOptions) throws ProjectServiceException;

  /**
   * This method allows to check if current user has administration authorization, such as update project,
   * create and update membership, create and update role.
   * 
   * @param pProjectId
   * @return true is current user a admin rights on given project
   */
  boolean hasAdminAuthorizations(final String pProjectId);

  /**
   * This method allows to check if current user has access authorization on project given.
   * 
   * @param pProjectId
   * @return true is current user a access rights on given project
   */
  boolean hasAccessAuthorizations(final String pProjectId);

  /**
   * This method allows to check if project id given referer to the forge project
   * 
   * @param pProjectId
   * @return true if it's forge project
   */
  boolean isForgeProject(final String pProjectId);

}
