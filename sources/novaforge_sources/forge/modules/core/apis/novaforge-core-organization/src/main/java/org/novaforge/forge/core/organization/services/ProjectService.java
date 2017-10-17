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

import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectInfo;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;

import java.util.List;
import java.util.Set;

/**
 * @author sbenoist
 */
public interface ProjectService
{

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
   * @param pAuthor
   * @param author
   * @throws ProjectServiceException
   * @throws UserServiceException
   * @throws GroupServiceException
   */
  void createSystemProject(Project project, String pAuthor) throws ProjectServiceException;

  /**
   * This method allows to create a user project
   * 
   * @param project
   * @param pAuthor
   * @param pTemplateId
   * @param author
   * @throws ProjectServiceException
   */
  void createProject(final Project project, final String pAuthor, final String pTemplateId)
      throws ProjectServiceException;

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
   * This method returns the validated projects on which ones user is member
   * 
   * @param pLogin
   *          the user login
   * @param pProjectOptions
   *          the options used to retrieve project objects
   * @return {@link List} of {@link Project}
   * @throws ProjectServiceException
   */
  List<Project> getValidatedProjects(final String pLogin, final ProjectOptions pProjectOptions)
      throws ProjectServiceException;

  /**
   * This method returns the validated projects on which ones user is not member yet
   * 
   * @param pLogin
   *          the user login
   * @param pProjectOptions
   *          the options used to retrieve project objects
   * @return {@link List} of {@link Project}
   * @throws ProjectServiceException
   */
  List<Project> getPublicProjects(final String pLogin, final ProjectOptions pProjectOptions)
      throws ProjectServiceException;

  /**
   * Return project id on which ones user is member directly or throw a group relationship
   * 
   * @param pLogin
   *          user login
   * @param pProjectOptions
   *          the options used to retrieve project objects
   * @return {@link List} of {@link Project}
   * @throws ProjectServiceException
   */
  Set<String> getProjectsId(final String pLogin, final ProjectOptions pProjectOptions)
      throws ProjectServiceException;

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
   * @param pAuthor
   *          the author of the request
   * @throws ProjectServiceException
   */
  void updateProject(final String pOldName, final Project pProject, final String pAuthor)
      throws ProjectServiceException;

  /**
   * This method allows to delete a project
   * 
   * @param projectId
   * @param pAuthor
   * @param author
   *          the username of the user which make the action
   * @throws ProjectServiceException
   */
  void deleteProject(String projectId, String pAuthor) throws ProjectServiceException;

  /**
   * This method allows to validate a project
   * 
   * @param projectId
   * @param author
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
   * This method allows to check if project id given referer to the forge project
   * 
   * @param pProjectId
   * @return true if it's forge project
   */
  boolean isForgeProject(String pProjectId);

}
