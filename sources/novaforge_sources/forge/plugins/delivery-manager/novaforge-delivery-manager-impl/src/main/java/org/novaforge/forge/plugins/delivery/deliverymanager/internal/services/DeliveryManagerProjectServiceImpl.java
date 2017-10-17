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
package org.novaforge.forge.plugins.delivery.deliverymanager.internal.services;

import org.novaforge.forge.commons.technical.file.FileService;
import org.novaforge.forge.commons.technical.file.FileServiceException;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryOrganizationServiceException;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.Project;
import org.novaforge.forge.tools.deliverymanager.model.User;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryOrganizationService;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPresenter;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryRepositoryService;

import java.io.File;
import java.util.List;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @author Guillaume Lamirand
 * @date 25 juil. 2011
 */
public class DeliveryManagerProjectServiceImpl extends AbstractPluginProjectService implements
    PluginProjectService
{

  private DeliveryRepositoryService   deliveryRepositoryService;
  private DeliveryPresenter           deliveryPresenter;
  private FileService                 fileService;
  private DeliveryOrganizationService deliveryOrganizationService;

  /**
   * {@inheritDoc}
   */
  @Override
  protected String createToolProject(final InstanceConfiguration instanceConfiguration,
      final PluginProject pluginProject, final List<PluginMembership> pluginMemberships)
      throws PluginServiceException
  {
    final StringBuilder projectId = new StringBuilder(pluginProject.getProjectId());
    projectId.append("_").append(instanceConfiguration.getConfigurationId());
    try
    {
      deliveryRepositoryService.createProjectDirectory(projectId.toString());
      deliveryPresenter.createDefaultReportTemplate(projectId.toString());

      // create the project
      Project project = deliveryOrganizationService.buildProject(projectId.toString(),
          pluginProject.getName(), pluginProject.getDescription());
      deliveryOrganizationService.createProject(project);

      // Create the memberships for users
      for (PluginMembership pluginMembership : pluginMemberships)
      {
        String forgeRole = pluginMembership.getRole();
        String instanceId = instanceConfiguration.getInstanceId();
        if (pluginRoleMappingService.existToolRole(instanceId, forgeRole))
        {
          String toolRole = pluginRoleMappingService.getToolRole(instanceId, forgeRole);
          User user = deliveryOrganizationService.buildUser(pluginMembership.getPluginUser().getLogin(),
              pluginMembership.getPluginUser().getFirstName(), pluginMembership.getPluginUser().getName());
          deliveryOrganizationService.addMembership(user, toolRole, projectId.toString());
        }
      }
    }
    catch (final DeliveryServiceException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create temporary directory used to store delivery content with [project_id=%s]",
          projectId), e);
    }
    catch (DeliveryOrganizationServiceException e)
    {
      throw new PluginServiceException(String.format("Unable to create project with [project_id=%s]",
          projectId), e);
    }
    return projectId.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean deleteToolProject(final InstanceConfiguration pInstance) throws PluginServiceException
  {

    final String projectDirectory = deliveryRepositoryService.getProjectDirectory(pInstance
        .getToolProjectId());
    try
    {
      final List<Delivery> deliveries = deliveryPresenter.getDeliveries(pInstance.getToolProjectId());
      for (final Delivery delivery : deliveries)
      {
        deliveryPresenter.deleteDelivery(delivery.getProjectId(), delivery.getReference());
      }
      fileService.deleteFile(new File(projectDirectory));

      // delete all memberships for the project
      deliveryOrganizationService.deleteAllProjectMemberships(pInstance
    	        .getToolProjectId());

      // Delete the default template report
      deliveryPresenter.deleteDefaultReportTemplate(pInstance
  	        .getToolProjectId());

      // delete the project
      deliveryOrganizationService.deleteProject(pInstance
  	        .getToolProjectId());
    }
    catch (final FileServiceException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to clean project directory with [project_path=%s]", projectDirectory), e);
    }
    catch (final DeliveryServiceException e)
    {
      throw new PluginServiceException(
          String.format("Unable to delete all deliveries existing for the project with [project_path=%s]",
              projectDirectory), e);
    }
    catch (DeliveryOrganizationServiceException e)
    {
      throw new PluginServiceException(String.format("Unable to delete the project with [toolProjectId=%s]",
    		  pInstance
    	        .getToolProjectId()), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void archiveProject(final String pInstanceId) throws PluginServiceException
  {
    // TODO Auto-generated method stub
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean updateToolProject(final InstanceConfiguration instanceConfiguration,
                                      final PluginProject pluginProject) throws PluginServiceException
  {
    return true;
  }

  /**
   * @param pDeliveryRepositoryService
   *          the deliveryRepositoryService to set
   */
  public void setDeliveryRepositoryService(final DeliveryRepositoryService pDeliveryRepositoryService)
  {
    deliveryRepositoryService = pDeliveryRepositoryService;
  }

  /**
   * @param pDeliveryPresenter
   *          the deliveryPresenter to set
   */
  public void setDeliveryPresenter(final DeliveryPresenter pDeliveryPresenter)
  {
    deliveryPresenter = pDeliveryPresenter;
  }

  /**
   * @param pFileService
   *          the fileService to set
   */
  public void setFileService(final FileService pFileService)
  {
    fileService = pFileService;
  }

  public void setDeliveryOrganizationService(final DeliveryOrganizationService pDeliveryOrganizationService)
  {
    deliveryOrganizationService = pDeliveryOrganizationService;
  }

}
