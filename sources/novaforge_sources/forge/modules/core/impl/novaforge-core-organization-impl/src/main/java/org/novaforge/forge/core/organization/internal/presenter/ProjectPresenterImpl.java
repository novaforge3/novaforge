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
package org.novaforge.forge.core.organization.internal.presenter;

import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.delegates.SecurityDelegate;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.internal.model.AuthorizationResourceImpl;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectInfo;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.security.authorization.PermissionAction;

import java.util.List;

/**
 * Implementation of {@link ProjectPresenter}
 * 
 * @author Guillaume Lamirand
 * @see ProjectPresenter
 */
public class ProjectPresenterImpl implements ProjectPresenter
{

  /**
   * Implementation of {@link ProjectService} injected by container
   */
  private ProjectService            projectService;

  /**
   * Implementation of {@link SecurityDelegate} injected by container
   */
  private SecurityDelegate          securityDelegate;

  /**
   * Implementation of {@link ForgeCfgService} injected by container
   */
  private ForgeConfigurationService forgeConfigurationService;

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectOptions newProjectOptions(final boolean pRetrieveOrganization, final boolean pRetrieveImage,
      final boolean pRetrieveSystem)
  {
    final ProjectOptions newProjectOptions = projectService.newProjectOptions();
    newProjectOptions.setRetrievedImage(pRetrieveImage);
    newProjectOptions.setRetrievedOrganization(pRetrieveOrganization);
    newProjectOptions.setRetrievedSystem(pRetrieveSystem);
    return newProjectOptions;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectOptions newProjectOptions()
  {
    return projectService.newProjectOptions();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project newProject()
  {
    return projectService.newProject();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createSystemProject(final Project pProject) throws ProjectServiceException
  {
    final String currentUser = securityDelegate.getCurrentUser();
    final String admin = forgeConfigurationService.getSuperAdministratorLogin();
    if ((admin != null) && (!admin.equals(currentUser)))
    {
      throw new ProjectServiceException(
          "The user has to be super-admin to be allowed to create a system project");
    }
    try
    {
      projectService.createSystemProject(pProject, currentUser);
    }
    finally
    {
      securityDelegate.clearPermissionCached(currentUser);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createProject(final Project pProject) throws ProjectServiceException
  {
    createProjectFromTemplate(pProject, null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.CREATE_PROJECT)
  public void createProjectFromTemplate(@HistorizableParam(label = "project") final Project pProject,
                                        final String pTemplateId) throws ProjectServiceException
  {
    securityDelegate.checkResource(new AuthorizationResourceImpl(Project.class, PermissionAction.CREATE));

    final String currentUser = securityDelegate.getCurrentUser();

    try
    {
      projectService.createProject(pProject, currentUser, pTemplateId);
    }
    finally
    {
      securityDelegate.clearPermissionCached(currentUser,
          forgeConfigurationService.getSuperAdministratorLogin());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> getAllProjects(final boolean pWithIcon) throws ProjectServiceException
  {
    final ProjectOptions projectOptions = projectService.newProjectOptions();
    projectOptions.setRetrievedSystem(true);
    projectOptions.setRetrievedImage(pWithIcon);
    projectOptions.setRetrievedOrganization(false);
    return projectService.getAllProjects(projectOptions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> getAllProjects(final ProjectOptions pProjectOptions) throws ProjectServiceException
  {
    return projectService.getAllProjects(pProjectOptions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project getProject(final String pProjectId, final boolean pWithIcon) throws ProjectServiceException
  {
    final ProjectOptions projectOptions = projectService.newProjectOptions();
    projectOptions.setRetrievedSystem(false);
    projectOptions.setRetrievedImage(pWithIcon);
    projectOptions.setRetrievedOrganization(true);
    return getProject(pProjectId, projectOptions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project getProject(final String pProjectId, final ProjectOptions pProjectOptions)
      throws ProjectServiceException
  {
    final Project project = projectService.getProject(pProjectId, pProjectOptions);
    // Check authorizations only if projet is private
    if (project.isPrivateVisibility())
    {
      securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Project.class, PermissionAction.READ));
    }
    return project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectInfo getProjectInfo(final String pProjectId) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Project.class, PermissionAction.READ,
                                                                             PermissionAction.READ));
    return projectService.getProjectInfo(pProjectId);
  }

  @Override
  @Historization(type = EventType.UPDATE_PROJECT)
  public void updateProject(final String pOldName, @HistorizableParam(label = "project") final Project pProject)
      throws ProjectServiceException

  {
    securityDelegate.checkResource(pProject.getProjectId(), new AuthorizationResourceImpl(Project.class,
                                                                                          PermissionAction.READ,
                                                                                          PermissionAction.UPDATE));
    final String currentUser = securityDelegate.getCurrentUser();

    projectService.updateProject(pOldName, pProject, currentUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.DELETE_PROJECT)
  public void deleteProject(@HistorizableParam(label = "projectId") final String pProjectId)
      throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Project.class, PermissionAction.DELETE));
    final String currentUser = securityDelegate.getCurrentUser();
    try
    {
      projectService.deleteProject(pProjectId, currentUser);
    }
    finally
    {
      securityDelegate.clearPermissionCached(currentUser, forgeConfigurationService.getSuperAdministratorLogin());
    }
  }

  @Override
  @Historization(type = EventType.VALIDATE_PROJECT)
  public void validateProject(@HistorizableParam(label = "projectId") final String pProjectId)
      throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Project.class, PermissionAction.READ,
                                                                             PermissionAction.UPDATE));
    projectService.validateProject(pProjectId);
  }

  @Override
  public void rejectProject(final String pProjectId, final String pReason) throws ProjectServiceException
  {
    securityDelegate.checkResource(pProjectId, new AuthorizationResourceImpl(Project.class, PermissionAction.DELETE));
    projectService.rejectProject(pProjectId, pReason);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> getAllProjectsByStatus(final ProjectOptions pProjectOptions,
                                              final ProjectStatus... pProjectStatus) throws ProjectServiceException
  {
    return projectService.getAllProjectsByStatus(pProjectOptions, pProjectStatus);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> getValidatedProjects(final ProjectOptions pProjectOptions) throws ProjectServiceException
  {
    return getValidatedProjects(securityDelegate.getCurrentUser(), pProjectOptions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> getValidatedProjects(final String pLogin, final ProjectOptions pProjectOptions)
      throws ProjectServiceException
  {
    return projectService.getValidatedProjects(pLogin, pProjectOptions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> getPublicProjects(final ProjectOptions pProjectOptions) throws ProjectServiceException
  {
    return projectService.getPublicProjects(securityDelegate.getCurrentUser(), pProjectOptions);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAdminAuthorizations(final String pProjectId)
  {
    return securityDelegate.isPermitted(pProjectId, new AuthorizationResourceImpl(Project.class, PermissionAction.ALL));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasAccessAuthorizations(final String pProjectId)
  {
    return securityDelegate.isPermitted(pProjectId, new AuthorizationResourceImpl(Project.class,
                                                                                  PermissionAction.READ));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isForgeProject(final String pProjectId)
  {
    return projectService.isForgeProject(pProjectId);
  }

  
  /**
   * Use by container to inject {@link ForgeConfigurationService} implementation
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService  pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * Use by container to inject {@link SecurityDelegate} implementation
   * 
   * @param pSecurityDelegate
   *          the securityDelegate to set
   */
  public void setSecurityDelegate(final SecurityDelegate pSecurityDelegate)
  {
    securityDelegate = pSecurityDelegate;
  }

  /**
   * Use by container to inject {@link ProjectService} implementation
   * 
   * @param pProjectService
   *          the projectService to set
   */
  public void setProjectService(final ProjectService pProjectService)
  {
    projectService = pProjectService;
  }
  
}
