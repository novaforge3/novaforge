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
package org.novaforge.forge.plugins.management.managementmodule.internal.services;

import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.PluginMembership;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginProjectService;
import org.novaforge.forge.plugins.commons.services.AbstractPluginProjectService;
import org.novaforge.forge.tools.managementmodule.business.ManagementModuleManager;
import org.novaforge.forge.tools.managementmodule.business.ProjectPlanManager;
import org.novaforge.forge.tools.managementmodule.business.ReferentielManager;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.text.MessageFormat;
import java.util.List;

/**
 * @author efalsquelle
 */
public class ManagementModuleProjectServiceImpl extends AbstractPluginProjectService implements
    PluginProjectService
{
  /*
   * Service injection declaration
   */
  private ManagementModuleManager managementModuleManager;

  private ReferentielManager      referentielManager;

  private ProjectPlanManager      projectPlanManager;

  @Override
  protected String createToolProject(final InstanceConfiguration pInstanceConfiguration,
      final PluginProject pPluginProject, final List<PluginMembership> pMemberships)
      throws PluginServiceException
  {
    try
    {
      // creation des utilisateurs du projet dans l'outil de management de projets
      addToolUser(pMemberships);
      // génération du tool project id
      String toolProjectId = pPluginProject.getProjectId() + "_"
          + pInstanceConfiguration.getConfigurationId();
      toolProjectId = toolProjectId.toLowerCase();

      // creation du projet propage
      Project toolProject = managementModuleManager.newProject();
      Transformation transformation = referentielManager.getDefaultTransformation();
      transformation.setIdProject(toolProjectId);
      EstimationComponentSimple estimationComponentSimple = referentielManager
          .getDefaultEstimationComponentSimple();
      estimationComponentSimple.setIdProjet(toolProjectId);

      toolProject.setName(pPluginProject.getName());
      toolProject.setDescription(pPluginProject.getDescription());
      toolProject.setProjectId(toolProjectId);
      toolProject.setEstimationComponentSimple(estimationComponentSimple);
      toolProject.setTransformation(transformation);
      toolProject.setUnitTime(referentielManager.getDefaultUnitTime());
      toolProject.setDisciplines(referentielManager.getDefaultProjectDisciplines(toolProject));
      toolProject = managementModuleManager.creeteProject(toolProject);

      // ajout des utilisateurs du projet au projet dans l'outil avec leur
      // role
      addUsersToProject(pMemberships, pInstanceConfiguration.getInstanceId(), toolProjectId);
      // initial lot is created with the project Plan)
      projectPlanManager.creeteProjectPlan(toolProject.getProjectId());
      return toolProject.getProjectId();
    }
    catch (ManagementModuleException e)
    {
      throw new PluginServiceException(e.getMessage(), e);
    }
  }

  private void addToolUser(final List<PluginMembership> pUsers) throws PluginServiceException,
      ManagementModuleException
  {
    for (PluginMembership membership : pUsers)
    {
      if (!managementModuleManager.existUserLogin(membership.getPluginUser().getLogin()))
      {
        managementModuleManager.creeteUser(membership.getPluginUser().getFirstName(), membership
            .getPluginUser().getName(), membership.getPluginUser().getLogin(), membership.getPluginUser()
            .getLanguage());
      }
    }
  }

  private void addUsersToProject(final List<PluginMembership> pUsers, final String pInstanceId,
      final String projectToolId) throws PluginServiceException, PluginServiceException
  {
    try
    {
      for (PluginMembership membership : pUsers)
      {
        String roleForge = membership.getRole();

        final String toolRole = pluginRoleMappingService.getToolRole(pInstanceId, roleForge);

        if (toolRole != null)
        {
          managementModuleManager.addUserMembership(projectToolId, membership.getPluginUser().getLogin(),
              toolRole);
        }

      }
    }
    catch (ManagementModuleException e)
    {
      throw new PluginServiceException(MessageFormat.format(
          "Unable to add users to project with projectID ={0}", projectToolId), e);
    }
  }

  @Override
  protected boolean deleteToolProject(final InstanceConfiguration instanceConfiguration) throws PluginServiceException
  {
    String toolProjectId = instanceConfiguration.getToolProjectId();

    try
    {
      Project currentProject = managementModuleManager.getProject(toolProjectId);
      if (currentProject != null)
      {
        return managementModuleManager.deleteProject(toolProjectId);
      }
      else
      {
        throw new PluginServiceException(MessageFormat.format("Unable to delete project with projectID =%s",
                                                              toolProjectId));
      }
    }
    catch (ManagementModuleException e)
    {
      throw new PluginServiceException(MessageFormat.format("Unable to delete project with projectID =%s",
                                                            toolProjectId), e);
    }
  }

  @Override
  public void archiveProject(final String pInstanceId) throws PluginServiceException
  {
    // NOT IMPLEMENTED
  }

  @Override
  protected boolean updateToolProject(final InstanceConfiguration pInstanceConfiguration, final PluginProject pProject)
      throws PluginServiceException
  {

    try
    {
      Project currentProject = managementModuleManager.getFullProject(pInstanceConfiguration.getToolProjectId());
      if (currentProject != null)
      {
        currentProject.setName(pProject.getName());
        currentProject.setDescription(pProject.getDescription());
        managementModuleManager.updateProject(currentProject);
        return true;

      }
      else
      {
        throw new PluginServiceException(MessageFormat.format("Unable to update project with projectID={0}",
                                                              pProject.getProjectId()));
      }
    }
    catch (ManagementModuleException e)
    {
      throw new PluginServiceException(MessageFormat.format("Unable to update project with projectID={0}",
                                                            pProject.getProjectId()), e);
    }

  }

  public void setManagementModuleManager(final ManagementModuleManager pManagementModuleManager)
  {
    managementModuleManager = pManagementModuleManager;
  }

  public void setReferentielManager(final ReferentielManager pReferentielManager)
  {
    referentielManager = pReferentielManager;
  }

  public void setProjectPlanManager(final ProjectPlanManager pProjectPlanManager)
  {
    projectPlanManager = pProjectPlanManager;
  }

}