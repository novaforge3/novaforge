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
package org.novaforge.forge.reference.internal;

import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.configuration.services.properties.ForgeCfgService;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.presenters.ApplicationPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.ProjectRolePresenter;
import org.novaforge.forge.core.organization.presenters.SpacePresenter;
import org.novaforge.forge.reference.exceptions.ReferenceFacadeException;
import org.novaforge.forge.reference.facade.ReferenceService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of {@link ReferenceService}
 * 
 * @author Guillaume Lamirand
 */
public class ReferenceServiceImpl implements ReferenceService
{
  /**
   * Reference to {@link ForgeCfgService} service injected by the container
   */
  private ForgeConfigurationService forgeConfigurationService;
  /**
   * Reference to {@link ProjectPresenter} service injected by the container
   */
  private ProjectPresenter          projectPresenter;
  /**
   * Reference to {@link SpacePresenter} service injected by the container
   */
  private SpacePresenter            spacePresenter;
  /**
   * Reference to {@link ApplicationPresenter} service injected by the container
   */
  private ApplicationPresenter      applicationPresenter;
  /**
   * Reference to {@link ProjectRolePresenter} service injected by the container
   */
  private ProjectRolePresenter      projectRolePresenter;

  /**
   * {@inheritDoc}
   */
  @Override
  public Project getProjectReference() throws ReferenceFacadeException
  {
    Project returnProject = null;
    try
    {
      returnProject = projectPresenter.getProject(getReferentielProjectId(), false);
    }
    catch (final ProjectServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return returnProject;
  }

  private String getReferentielProjectId()
  {
    return forgeConfigurationService.getReferentielProjectId();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateProjectReference(final String pOldName, final Project pProject)
      throws ReferenceFacadeException
  {
    try
    {
      projectPresenter.updateProject(pOldName, pProject);
    }
    catch (final ProjectServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   * 
   * @throws ReferenceFacadeException
   */
  @Override
  public Space getSpace(final String pSpaceNodeUri) throws ReferenceFacadeException
  {
    Space returnSpace = null;
    try
    {
      returnSpace = spacePresenter.getSpace(getReferentielProjectId(), pSpaceNodeUri);
    }
    catch (final SpaceServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return returnSpace;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean createSpace(final String pSpaceName) throws ReferenceFacadeException
  {
    final Space space = spacePresenter.newSpace();
    space.setName(pSpaceName);
    try
    {
      spacePresenter.addSpace(getReferentielProjectId(), space);
    }
    catch (final SpaceServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateSpace(final String pOldName, final Space pSpace) throws ReferenceFacadeException
  {
    try
    {
      spacePresenter.updateSpace(getReferentielProjectId(), pOldName, pSpace);
    }
    catch (final SpaceServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteSpace(final String pSpaceUri) throws ReferenceFacadeException
  {
    try
    {
      spacePresenter.removeSpace(getReferentielProjectId(), pSpaceUri);
    }
    catch (final SpaceServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean createApplication(final String pSpaceUri, final String pApplicationName,
      final UUID pPluginUUID, final Map<String, String> pRolesMapping) throws ReferenceFacadeException
  {
    try
    {
      applicationPresenter.addApplication(getReferentielProjectId(), pSpaceUri, pApplicationName,
          pApplicationName, pPluginUUID, pRolesMapping);
    }
    catch (final ApplicationServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateApplication(final String pUri, final Map<String, String> pRolesMapping)
      throws ReferenceFacadeException
  {
    try
    {
      applicationPresenter.updateRoleMapping(getReferentielProjectId(), pUri, pRolesMapping);
    }
    catch (final ApplicationServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteApplication(final String pApplicationUri) throws ReferenceFacadeException
  {
    try
    {
      applicationPresenter.removeApplication(getReferentielProjectId(), pApplicationUri);
    }
    catch (final ApplicationServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Space> getSpaces() throws ReferenceFacadeException
  {

    final List<Space> spaces = new ArrayList<Space>();
    try
    {
      spaces.addAll(spacePresenter.getAllSpaces(getReferentielProjectId()));
    }
    catch (final SpaceServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return spaces;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectApplication> getAllApplicationsForSpace(final String pSpaceUri)
      throws ReferenceFacadeException
  {

    final List<ProjectApplication> apps = new ArrayList<ProjectApplication>();
    try
    {
      apps.addAll(applicationPresenter.getAllSpaceApplications(pSpaceUri, getReferentielProjectId()));
    }
    catch (final ApplicationServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return apps;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ProjectRole> getRoles() throws ReferenceFacadeException
  {

    final List<ProjectRole> roles = new ArrayList<ProjectRole>();
    try
    {
      roles.addAll(projectRolePresenter.getAllRoles(getReferentielProjectId()));
    }
    catch (final ProjectServiceException e)
    {
      throw new ReferenceFacadeException(e);
    }
    return roles;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProjectReferenceCreated()
  {
    return forgeConfigurationService.isReferentielCreated();
  }

  /**
   * Use by container to inject {@link ForgeCfgService} implementation
   * 
   * @param pForgeConfigurationService
   *          the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * Use by container to inject {@link ProjectPresenter} implementation
   * 
   * @param pProjectPresenter
   *          the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  /**
   * Use by container to inject {@link SpacePresenter} implementation
   * 
   * @param pSpacePresenter
   *          the spacePresenter to set
   */
  public void setSpacePresenter(final SpacePresenter pSpacePresenter)
  {
    spacePresenter = pSpacePresenter;
  }

  /**
   * Use by container to inject {@link ApplicationPresenter} implementation
   * 
   * @param pApplicationPresenter
   *          the applicationPresenter to set
   */
  public void setApplicationPresenter(final ApplicationPresenter pApplicationPresenter)
  {
    applicationPresenter = pApplicationPresenter;
  }

  /**
   * Use by container to inject {@link ProjectRolePresenter} implementation
   * 
   * @param pProjectRolePresenter
   *          the projectRolePresenter to set
   */
  public void setProjectRolePresenter(final ProjectRolePresenter pProjectRolePresenter)
  {
    projectRolePresenter = pProjectRolePresenter;
  }

}
