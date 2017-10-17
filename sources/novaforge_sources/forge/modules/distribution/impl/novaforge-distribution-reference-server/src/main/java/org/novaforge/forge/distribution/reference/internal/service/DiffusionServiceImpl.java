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
package org.novaforge.forge.distribution.reference.internal.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.technical.historization.annotations.HistorizableParam;
import org.novaforge.forge.commons.technical.historization.annotations.Historization;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.scheduling.TimerSchedulerService;
import org.novaforge.forge.core.configuration.exceptions.ForgeConfigurationException;
import org.novaforge.forge.core.configuration.services.ForgeConfigurationService;
import org.novaforge.forge.core.organization.exceptions.ApplicationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Application;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateApplication;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;
import org.novaforge.forge.core.organization.presenters.ProjectPresenter;
import org.novaforge.forge.core.organization.presenters.TemplateNodePresenter;
import org.novaforge.forge.core.organization.presenters.TemplatePresenter;
import org.novaforge.forge.core.organization.presenters.TemplateRolePresenter;
import org.novaforge.forge.core.organization.services.ProjectRoleService;
import org.novaforge.forge.core.organization.services.SpaceService;
import org.novaforge.forge.core.plugins.data.ActionType;
import org.novaforge.forge.core.plugins.data.DataDTO;
import org.novaforge.forge.core.plugins.data.ItemDTO;
import org.novaforge.forge.core.plugins.domain.plugin.PluginServiceMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginDataService;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.distribution.reference.client.ForgeExtractionClient;
import org.novaforge.forge.distribution.reference.client.ReferentielSynchroClient;
import org.novaforge.forge.distribution.reference.exception.ReferenceServiceException;
import org.novaforge.forge.distribution.reference.model.ApplicationInError;
import org.novaforge.forge.distribution.reference.model.ApplicationItem;
import org.novaforge.forge.distribution.reference.model.ApplicationItemReferences;
import org.novaforge.forge.distribution.reference.model.ApplicationNode;
import org.novaforge.forge.distribution.reference.model.ApplicationRoleMapping;
import org.novaforge.forge.distribution.reference.model.DiffusionResult;
import org.novaforge.forge.distribution.reference.model.DiffusionTemplateResult;
import org.novaforge.forge.distribution.reference.model.ReferenceProject;
import org.novaforge.forge.distribution.reference.model.ReferenceTemplate;
import org.novaforge.forge.distribution.reference.model.ReferenceTemplate.RefTemplateStatus;
import org.novaforge.forge.distribution.reference.model.ReferenceTemplates;
import org.novaforge.forge.distribution.reference.model.RootNode;
import org.novaforge.forge.distribution.reference.model.SpaceNode;
import org.novaforge.forge.distribution.reference.model.SynchDiffered;
import org.novaforge.forge.distribution.reference.model.SynchronizationResult;
import org.novaforge.forge.distribution.reference.model.TargetForge;
import org.novaforge.forge.distribution.reference.model.TemplateRole;
import org.novaforge.forge.distribution.reference.model.TemplateSynchroResult;
import org.novaforge.forge.distribution.reference.model.ToolSynchronizationResult;
import org.novaforge.forge.distribution.reference.model.ToolsSynchronizationReport;
import org.novaforge.forge.distribution.reference.service.DiffusionService;
import org.novaforge.forge.distribution.register.domain.ForgeDTO;
import org.novaforge.forge.distribution.register.exceptions.ForgeDistributionException;
import org.novaforge.forge.distribution.register.services.ForgeDistributionService;
import org.novaforge.forge.reference.tool.ReferenceToolService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.novaforge.forge.distribution.reference.model.ToolSynchronizationResult.COMMAND_SUCCESS;

/**
 * @author rols-p
 */
public class DiffusionServiceImpl extends AbstractReferenceServiceImpl implements DiffusionService
{
  private static final Log log = LogFactory.getLog(DiffusionServiceImpl.class);

  private ForgeDistributionService  forgeDistributionService;
  private ReferentielSynchroClient  referentielSynchroClient;
  private ForgeExtractionClient     forgeExtractionClient;
  private SpaceService              spaceService;
  private ProjectPresenter          projectPresenter;
  private ProjectRoleService        projectRoleService;
  private ForgeConfigurationService forgeConfigurationService;
  private TimerSchedulerService     timerSchedulerService;

  private TemplatePresenter     templatePresenter;
  private TemplateNodePresenter templateNodePresenter;
  private TemplateRolePresenter templateRolePresenter;

  private ReferenceToolService referenceToolService;

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.PROPAGATE_REFERENCE_PROJECT_ALL_CHILD_FORGES, returnLabel = "Result",
                 ignoreCurrentUser = true)
  public DiffusionResult propagateReferenceProject() throws ReferenceServiceException
  {
    final List<SynchronizationResult> forgesResult = new ArrayList<SynchronizationResult>();
    try
    {
      final String refProjectId = forgeConfigurationService.getReferentielProjectId();
      final String forgeId = forgeIdentificationService.getForgeId().toString();

      // 0- Get the ReferenceProjectDTO that will be used for the propagation.
      final ReferenceProject refProjectDTO = buildReferenceProject(refProjectId);

      // 1- Get child forges
      final Set<ForgeDTO> children = getAllChildrenForgesTree(forgeId);

      if ((children != null) && !children.isEmpty())
      {
        for (final ForgeDTO child : children)
        {
          // 2- Propagate the reference project on each child forge.
          final SynchronizationResult forgeResult = propagateReferenceProjectOnForge(refProjectDTO, child);
          forgesResult.add(forgeResult);
        }
      }
      else
      {
        final String errorMsg = String
                                    .format("No child forge found for the forge id=%s. The Refentiel Project can not be propagated.",
                                            forgeId);
        log.warn(errorMsg);
        throw new ReferenceServiceException(errorMsg);
      }

    }
    catch (final ForgeDistributionException | ForgeConfigurationException e)
    {
      throw new ReferenceServiceException(e);
    }
    final DiffusionResult result = new DiffusionResult();
    result.setSynchronizationResults(forgesResult);
    return result;
  }

  /*
   * TODO when the method nodeProxyManager.getAllChildrenSpaces(String spaceUri, String projectId) will be
   * available, add the logic to build the spaces children of a given space.
   */
  private ReferenceProject buildReferenceProject(final String pProjectId)
      throws ReferenceServiceException, ForgeConfigurationException
  {

    ReferenceProject projectDTO;
    try
    {
      final Project project = projectPresenter.getProject(pProjectId, false);

      projectDTO = buildProject(project);
      final RootNode rootNodeDTO = buildRootNode(pProjectId);
      final List<Space> spaces = spaceService.getAllSpaces(pProjectId);

      for (final Space space : spaces)
      {
        final SpaceNode spaceNodeDTO = buildSpaceNode(space, pProjectId);
        final List<ProjectApplication> applications = applicationService.getAllSpaceApplications(space.getUri(),
                                                                                                 pProjectId);
        for (final ProjectApplication application : applications)
        {
          final PluginService pluginService = pluginsManager.getPluginService(application.getPluginUUID().toString());
          final Map<String, String> roleMappings = pluginService.getRolesMapping(application.getPluginInstanceUUID()
                                                                                            .toString());
          final PluginServiceMetadata pluginMetadata = pluginService.getMetadata();

          final ApplicationNode applicationDTO = buildApplicationNode(application, pProjectId, roleMappings,
                                                                      pluginMetadata);
          spaceNodeDTO.getApplications().add(applicationDTO);
        }
        rootNodeDTO.getSpaces().add(spaceNodeDTO);
      }
      projectDTO.setRootNode(rootNodeDTO);
    }
    catch (final ProjectServiceException | ApplicationServiceException | SpaceServiceException e)
    {
      throw new ReferenceServiceException(String
                                                                             .format("Unable to get project information with [project_id=%s]",
                                                                                     pProjectId), e);
    }
    catch (final PluginServiceException e)
    {
      throw new ReferenceServiceException(String
                                                                             .format("Unable to get application information with [project_id=%s]",
                                                                                     pProjectId), e);
    }
    catch (final PluginManagerException e)
    {
      throw new ReferenceServiceException(String
                                                                             .format("Unable to get role mapping information with [project_id=%s]",
                                                                                     pProjectId), e);
    }
    return projectDTO;
  }

  /**
   * @param forgeId
   *
   * @return a Set containing all the tree under this forge.
   *
   * @throws ReferenceServiceException
   * @throws ForgeDistributionException
   */
  Set<ForgeDTO> getAllChildrenForgesTree(final String forgeId)
      throws ReferenceServiceException, ForgeDistributionException
  {
    if (forgeDistributionService == null)
    {
      final String errorMsg = String.format("The distribution service is not available for the forge id=%s.", forgeId);
      log.warn(errorMsg);
      throw new ReferenceServiceException(errorMsg);
    }
    final ForgeDTO rootForgeDTO = forgeDistributionService.getForge(forgeId);
    Set<ForgeDTO>  children     = null;
    if (rootForgeDTO != null)
    {
      children = DistributionReferenceHelper.getChildrenForges(rootForgeDTO);
    }
    return children;
  }

  private SynchronizationResult propagateReferenceProjectOnForge(final ReferenceProject referenceProject,
                                                                 final ForgeDTO targetForge)
  {
    final String targetForgeBaseUrl = targetForge.getForgeUrl().toExternalForm();
    SynchronizationResult synchronizationResult = new SynchronizationResult(targetForge.getLabel());

    try
    {
      // Update the reference project on the targeted forge (the update deals with the project
      // configuration and templates)
      log.info(String.format("Updating reference project on %s ...", targetForgeBaseUrl));
      synchronizationResult = referentielSynchroClient.updateReferenceProject(referenceProject, targetForgeBaseUrl);

      // Get the reference project and its application references from the targeted forge
      log.info(String.format("Getting reference project from %s ...", targetForgeBaseUrl));
      final ReferenceProject targetReferenceProject = referentielSynchroClient.getReferenceProject(targetForgeBaseUrl);

      // Get the application references of the current reference project
      final List<ProjectApplication> applications = getAllSynchronizableApplications(referenceProject.getId());

      // Propagate each application to the child forge
      for (final ProjectApplication application : applications)
      {
        propagateReferenceApplication(synchronizationResult, application, targetReferenceProject, targetForgeBaseUrl);
        synchronizationResult.getApplicationsSynchronized().add(application.getName());
      }
    }
    catch (final Exception e)
    {
      synchronizationResult.setFailedMsg(e.getMessage());
    }
    log.info(String.format("The Refentiel Project has been successfully updated on the child forge %s.",
                           targetForge.getLabel()));

    return synchronizationResult;
  }

  private ReferenceProject buildProject(final Project pProject) throws ProjectServiceException
  {
    final ReferenceProject projectDTO = new ReferenceProject();
    projectDTO.setId(pProject.getProjectId());
    projectDTO.setName(pProject.getName());
    projectDTO.setDescription(pProject.getDescription());
    projectDTO.setLicence(pProject.getLicenceType());

    final Role memberRole = projectRoleService.getRole(forgeConfigurationService.getForgeMemberRoleName(),
                                                       pProject.getProjectId());
    if (memberRole != null)
    {
      projectDTO.setMemberRoleName(memberRole.getName());
    }

    return projectDTO;
  }

  private RootNode buildRootNode(final String pProjectId)
  {
    return new RootNode(pProjectId, pProjectId);
  }

  private SpaceNode buildSpaceNode(final Space pSpaceNode, final String pProjectId)
  {
    return new SpaceNode(pSpaceNode.getUri(), pSpaceNode.getName());
  }

  private ApplicationNode buildApplicationNode(final Application pApplicationNode, final String pProjectId,
                                               final Map<String, String> roleMappings,
                                               final PluginServiceMetadata pluginMetadata) throws PluginServiceException
  {
    final ApplicationNode applicationNodeDTO = new ApplicationNode(pApplicationNode.getUri(),
                                                                   pApplicationNode.getName());
    applicationNodeDTO.setType(pluginMetadata.getType());
    applicationNodeDTO.setCategory(pluginMetadata.getCategory());

    final Set<Map.Entry<String, String>> projectRoles = roleMappings.entrySet();
    for (final Map.Entry<String, String> projectRoleEntry : projectRoles)
    {
      final ApplicationRoleMapping roleMappingDTO = new ApplicationRoleMapping(projectRoleEntry.getKey(),
                                                                               projectRoleEntry.getValue());
      applicationNodeDTO.getRoleMappings().add(roleMappingDTO);
    }


    return applicationNodeDTO;
  }

  private void propagateReferenceApplication(final SynchronizationResult synchronizationResult,
                                             final ProjectApplication localApplication,
                                             final ReferenceProject targetReferenceProject,
                                             final String targetForgeBaseUrl)

  {
    try
    {
      log.info(String.format("Propagating application %s on %s", localApplication.getName(), targetForgeBaseUrl));

      final ApplicationItemReferences localItemReferences = getPluginItemReferences(localApplication);
      log.info(String.format("Found %s local item references", localItemReferences.getItemReferences().size()));

      final ApplicationItemReferences targetItemReferences = DistributionReferenceHelper
                                                                 .findApplicationItemReferences(localApplication
                                                                                                    .getUri(),
                                                                                                targetReferenceProject
                                                                                                    .getPluginItemReferences());
      log.info(String.format("Found %s target item references", targetItemReferences.getItemReferences().size()));

      final List<ItemDTO> diffItems = DistributionReferenceHelper.makeDiffItems(localItemReferences,
                                                                                targetItemReferences);
      log.info(String.format("Item references diff size: %s", diffItems.size()));

      final String localForgeId = forgeIdentificationService.getForgeId().toString();
      final String localPluginUUID = localApplication.getPluginUUID().toString();
      final String localPluginInstanceUUID = localApplication.getPluginInstanceUUID().toString();
      final PluginDataService localPluginDataService = pluginDataClient.getPluginDataService(localPluginUUID);

      for (final ItemDTO diffItem : diffItems)
      {
        DataDTO localData = new DataDTO();
        if ((diffItem.getAction() == ActionType.UPDATE) || (diffItem.getAction() == ActionType.CREATE))
        {
          localData = localPluginDataService.getData(localForgeId, localPluginInstanceUUID, diffItem);
        }
        else
        {
          localData.setItemDTO(diffItem);
        }

        final ApplicationItem applicationItem = new ApplicationItem();
        applicationItem.setUri(localApplication.getUri());
        applicationItem.setData(localData);

        final boolean updated = referentielSynchroClient.updateApplicationItem(applicationItem, targetForgeBaseUrl);
        log.debug(String.format("Item %s updated on target forge: %s",
                                localData.getItemDTO().getReference().getReferenceId(), updated));

        if (!updated)
        {
          synchronizationResult.getApplicationsInError().add(new ApplicationInError(localApplication.getName(), String
                                                                                                                    .format("The item [%s] has not been updated",
                                                                                                                            diffItem
                                                                                                                                .getReference())));
        }
      }
    }
    catch (final Exception e)
    {
      synchronizationResult.getApplicationsInError().add(new ApplicationInError(localApplication.getName(), String
                                                                                                                .format("Unexpected error when updating item: %s",
                                                                                                                        e.getMessage())));
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Historization(type = EventType.PROPAGATE_TEMPLATES_ALL_CHILD_FORGES, returnLabel = "Result",
                 ignoreCurrentUser = true)
  public DiffusionTemplateResult propagateTemplates() throws ReferenceServiceException
  {
    final List<TemplateSynchroResult> forgesResult = new ArrayList<TemplateSynchroResult>();
    try
    {
      final String forgeId = forgeIdentificationService.getForgeId().toString();

      // 0- Get the ReferenceProjectDTO that will be used for the propagation.
      final ReferenceTemplates templates = buildReferenceTemplates();

      // 1- Get child forges
      final Set<ForgeDTO> children = getAllChildrenForgesTree(forgeId);

      if ((children != null) && !children.isEmpty())
      {
        for (final ForgeDTO child : children)
        {
          final String childBaseUrl = child.getForgeUrl().toExternalForm();
          TemplateSynchroResult forgeResult;
          // We do not want to fail if one of the children has failed.
          try
          {
            // 3- Call on these forges the client webservice SynchronizationService.
            forgeResult = referentielSynchroClient.updateTemplates(templates, childBaseUrl);
          }
          catch (final Exception e)
          {
            forgeResult = new TemplateSynchroResult(child.getLabel());
            forgeResult.setFailedMsg(e.getMessage());
          }
          log.info(String.format("The Templates have been successfully updated on the child forge %s.",
                                 child.getLabel()));
          forgesResult.add(forgeResult);
        }
      }
      else
      {
        final String errorMsg = String
                                    .format("No child forge found for the forge id=%s. The Templates can not be propagated.",
                                            forgeId);
        log.warn(errorMsg);
        throw new ReferenceServiceException(errorMsg);
      }

    }
    catch (final ForgeDistributionException e)
    {
      throw new ReferenceServiceException(e);
    }
    final DiffusionTemplateResult result = new DiffusionTemplateResult();
    result.setResults(forgesResult);
    return result;
  }

  @Override
  @Historization(type = EventType.PROPAGATE_REFERENCE_TOOLS_ALL_CHILD_FORGES, returnLabel = "Result",
                 ignoreCurrentUser = true)
  public DiffusionResult propagateReferenceTools() throws ReferenceServiceException
  {
    final List<SynchronizationResult> forgesResult = new ArrayList<SynchronizationResult>();
    try
    {
      forgeConfigurationService.getReferentielProjectId();
      final String forgeId = forgeIdentificationService.getForgeId().toString();

      final Set<ForgeDTO> children = getAllChildrenForgesTree(forgeId);
      if ((children != null) && !children.isEmpty())
      {
        for (final ForgeDTO child : children)
        {
          final SynchronizationResult forgeResult = propagateReferenceToolsOnForge(child);
          forgesResult.add(forgeResult);
        }
      }
      else
      {
        final String errorMsg = String
                                    .format("No child forge found for the forge id=%s. The reference tools cannot be propagated.",
                                            forgeId);
        log.warn(errorMsg);
        throw new ReferenceServiceException(errorMsg);
      }
    }
    catch (final ForgeDistributionException e)
    {
      throw new ReferenceServiceException(e);
    }
    final DiffusionResult result = new DiffusionResult();
    result.setSynchronizationResults(forgesResult);
    return result;
  }

  private SynchronizationResult propagateReferenceToolsOnForge(final ForgeDTO targetForge)
  {
    final SynchronizationResult result = new SynchronizationResult(targetForge.getLabel());
    final StringBuilder output = new StringBuilder();

    try
    {
      String command = buildToolsPropagationCommand(targetForge.getForgeUrl().getHost(),
                                                    referenceToolService.getAdminStorageDirectory());
      final ToolSynchronizationResult adminSpaceResult = runSynchronization(command);
      if (adminSpaceResult.getReturnCode() != ToolSynchronizationResult.COMMAND_SUCCESS)
      {
        result.setFailedMsg(adminSpaceResult.getMessage());
      }
      log.info(String.format("Synchronizing the reference admin tools: %s", command));
      output.append(adminSpaceResult.getMessage());

      command = buildToolsPropagationCommand(targetForge.getForgeUrl().getHost(),
                                             referenceToolService.getUserStorageDirectory());
      final ToolSynchronizationResult userSpaceResult = runSynchronization(command);
      if (userSpaceResult.getReturnCode() != ToolSynchronizationResult.COMMAND_SUCCESS)
      {
        result.setFailedMsg(userSpaceResult.getMessage());
      }
      log.info(String.format("Synchronizing the reference user tools: %s", command));
      output.append(userSpaceResult.getMessage());

      // Send the output to the target forge
      final ToolsSynchronizationReport report = new ToolsSynchronizationReport();
      report.setReport(output.toString());
      final String targetForgeBaseUrl = targetForge.getForgeUrl().toExternalForm();
      referentielSynchroClient.sendReferenceToolsUpdateReport(report, targetForgeBaseUrl);
    }
    catch (final Exception e)
    {
      result.setFailedMsg(e.getMessage());
    }
    log.info(String.format("Synchronization done, result: %s", output.toString()));
    return result;
  }

  private String buildToolsPropagationCommand(final String targetForgeHost, final String directory)
  {
    return referenceToolService.getRsyncCommand() + " " + referenceToolService.getRsyncArguments() + " " + directory
               + " " + referenceToolService.getRsyncUser() + "@" + targetForgeHost + ":" + directory;
  }

  private ToolSynchronizationResult runSynchronization(final String command) throws IOException
  {
    String line;
    String output = "";

    BufferedReader input      = null;
    int            returnCode = COMMAND_SUCCESS;
    try
    {
      final Process p = Runtime.getRuntime().exec(command);
      p.waitFor();
      returnCode = p.exitValue();

      if (returnCode == COMMAND_SUCCESS)
      {
        input = new BufferedReader(new InputStreamReader(p.getInputStream()));
      }
      else
      {
        input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
        output = "Tools Synchronization failed: error = ";
      }
      while ((line = input.readLine()) != null)
      {
        output += (line + '\n');
      }
    }
    catch (final Exception e)
    {
      log.error(String.format("Error executing the rsync command: %s, exception=", command), e);
    }
    finally
    {
      if (input != null)
      {
        input.close();
      }
    }
    return new ToolSynchronizationResult(returnCode, output);
  }

  @Override
  @Historization(type = EventType.LAUNCH_EXTRACTION_ALL_CHILD_FORGES, ignoreCurrentUser = true)
  public void launchReportingExtraction() throws ReferenceServiceException
  {
    try
    {
      forgeConfigurationService.getReferentielProjectId();
      final String forgeId = forgeIdentificationService.getForgeId().toString();

      // 1- Get child forges
      final Set<ForgeDTO> forges = getAllForgesTree(forgeId);

      if ((forges != null) && !forges.isEmpty())
      {
        for (final ForgeDTO forge : forges)
        {
          final String forgeBaseUrl = forge.getForgeUrl().toExternalForm();
          // We do not want to fail if one of the children has failed.
          try
          {
            // 3- Call on these forges the client webservice SynchronizationService.
            forgeExtractionClient.startExtraction(forgeBaseUrl);
            log.info(String.format("The Extraction has been successfully launched on the child forge %s",
                                   forge.getLabel()));
          }
          catch (final Exception e)
          {
            log.error(String.format("The Extraction failed to be launched on the child forge %s", forge.getLabel()));
          }
        }
      }
      else
      {
        final String errorMsg = String
                                    .format("No child forge found for the forge id=%s. The Extraction can not be launched.",
                                            forgeId);
        log.warn(errorMsg);
        throw new ReferenceServiceException(errorMsg);
      }

    }
    catch (final ForgeDistributionException e)
    {
      throw new ReferenceServiceException(e);
    }
  }

  Set<ForgeDTO> getAllForgesTree(final String forgeId) throws ReferenceServiceException, ForgeDistributionException
  {
    if (forgeDistributionService == null)
    {
      final String errorMsg = String.format("The distribution service is not available for the forge id=%s.", forgeId);
      log.warn(errorMsg);
      throw new ReferenceServiceException(errorMsg);
    }
    final ForgeDTO      rootForgeDTO = forgeDistributionService.getForge(forgeId);
    final Set<ForgeDTO> children     = DistributionReferenceHelper.getChildrenForges(rootForgeDTO);
    children.add(rootForgeDTO);
    return children;
  }

  @Override
  @Historization(type = EventType.CONFIGURE_SCHEDULING)
  public void configureScheduling(@HistorizableParam(label = "hours") final String hours, @HistorizableParam(
                                                                                                                label = "minutes")
  final String minutes, @HistorizableParam(label = "period") final String period,
                                  @HistorizableParam(label = "propagate") final boolean canPropagate)
      throws ReferenceServiceException
  {
    if (canPropagate)
    {
      configureExtractionScheduling(hours, minutes, period);
    }
    try
    {
      timerSchedulerService.configureScheduling(hours, minutes, period);
    }
    catch (final Exception e)
    {
      log.error("The Update Scheduling failed to be launched on the parent forge ");
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<TargetForge> getTargetForges() throws ReferenceServiceException
  {
    final Set<TargetForge> forges = new HashSet<TargetForge>();
    try
    {
      final String forgeId = forgeIdentificationService.getForgeId().toString();
      // 1- Get child forges
      final Set<ForgeDTO> children = getAllChildrenForgesTree(forgeId);
      if ((children != null) && !children.isEmpty())
      {
        for (final ForgeDTO child : children)
        {
          final TargetForge targetForge = new TargetForge(child.getForgeId(), child.getLabel(), child.getDescription(),
                                                          child.getForgeLevel());
          forges.add(targetForge);
        }
      }

    }
    catch (final ForgeDistributionException e)
    {
      throw new ReferenceServiceException(e);
    }

    return forges;
  }

  @Override
  @Historization(type = EventType.DISABLE_SCHEDULING, ignoreCurrentUser = true)
  public void disableScheduling(@HistorizableParam(label = "propagate") final boolean canPropagate)
      throws ReferenceServiceException
  {
    if (canPropagate)
    {
      disableExtractionScheduling();
    }
    else
    {
      try
      {
        timerSchedulerService.disableScheduling();
      }
      catch (final Exception e)
      {
        log.error("The Disable Scheduling failed to be launched on the parent forge ");
      }
    }
  }

  private void disableExtractionScheduling() throws ReferenceServiceException
  {
    try
    {
      forgeConfigurationService.getReferentielProjectId();
      final String forgeId = forgeIdentificationService.getForgeId().toString();

      // 1- Get child forges
      final Set<ForgeDTO> children = getAllForgesTree(forgeId);

      if ((children != null) && !children.isEmpty())
      {
        for (final ForgeDTO child : children)
        {
          final String childBaseUrl = child.getForgeUrl().toExternalForm();
          // We do not want to fail if one of the children has failed.
          try
          {
            // 3- Call on these forges the client webservice SynchronizationService.
            forgeExtractionClient.disableExtractionScheduling(childBaseUrl);
            log.info(String.format("The disable Scheduling has been successfully launched on the child forge %s",
                                   child.getLabel()));
          }
          catch (final Exception e)
          {
            log.error(String.format("The disable Scheduling failed to be launched on the child forge %s",
                                    child.getLabel()));
          }
        }
      }
      else
      {
        final String errorMsg = String
                                    .format("No child forge found for the forge id=%s. The Update Scheduling can not be launched.",
                                            forgeId);
        log.warn(errorMsg);
        throw new ReferenceServiceException(errorMsg);
      }
    }
    catch (final ForgeDistributionException e)
    {
      throw new ReferenceServiceException(e);
    }
  }

  @Override
  public SynchDiffered getSchedulingConfig()
  {
    final SynchDiffered returned = new SynchDiffered();
    returned.setActive(timerSchedulerService.isActive());
    returned.setHours(timerSchedulerService.getStartHour());
    returned.setMinutes(timerSchedulerService.getStartMin());
    returned.setPeriod(timerSchedulerService.getPeriod());
    return returned;
  }

  private void configureExtractionScheduling(final String hours, final String minutes, final String period)
      throws ReferenceServiceException
  {
    try
    {
      forgeConfigurationService.getReferentielProjectId();
      final String forgeId = forgeIdentificationService.getForgeId().toString();

      // 1- Get child forges
      final Set<ForgeDTO> children = getAllForgesTree(forgeId);

      if ((children != null) && !children.isEmpty())
      {
        for (final ForgeDTO child : children)
        {
          final String childBaseUrl = child.getForgeUrl().toExternalForm();
          // We do not want to fail if one of the children has failed.
          try
          {
            // 3- Call on these forges the client webservice SynchronizationService.
            forgeExtractionClient.configureExtractionScheduling(childBaseUrl, hours, minutes, period);
            log.info(String.format("The Update Scheduling has been successfully launched on the child forge %s",
                                   child.getLabel()));
          }
          catch (final Exception e)
          {
            log.error(String.format("The Update Scheduling failed to be launched on the child forge %s",
                                    child.getLabel()));
          }
        }
      }
      else
      {
        final String errorMsg = String
                                    .format("No child forge found for the forge id=%s. The Update Scheduling can not be launched.",
                                            forgeId);
        log.warn(errorMsg);
        throw new ReferenceServiceException(errorMsg);
      }
    }
    catch (final ForgeDistributionException e)
    {
      throw new ReferenceServiceException(e);
    }
  }

  /**
   * @return
   */
  private ReferenceTemplates buildReferenceTemplates() throws ReferenceServiceException
  {
    final List<ReferenceTemplate> refTemplatesList = new ArrayList<ReferenceTemplate>();
    final ReferenceTemplates refTemplates = new ReferenceTemplates(refTemplatesList);
    try
    {
      final List<Template> templates = templatePresenter.getTemplates();
      if ((templates != null) && !templates.isEmpty())
      {
        for (final Template template : templates)
        {
          final ReferenceTemplate refTemplate = buildRefTemplate(template);
          refTemplatesList.add(refTemplate);
        }
      }
    }
    catch (final TemplateServiceException e)
    {
      throw new ReferenceServiceException("Unable to get the templates", e);
    }
    catch (final PluginManagerException e)
    {
      throw new ReferenceServiceException("Unable to build the templates", e);
    }
    catch (final PluginServiceException e)
    {
      throw new ReferenceServiceException("Unable to build the templates", e);
    }
    return refTemplates;

  }

  /**
   * @param template
   *
   * @return
   *
   * @throws TemplateNodeServiceException
   * @throws PluginManagerException
   * @throws PluginServiceException
   * @throws TemplateServiceException
   */
  private ReferenceTemplate buildRefTemplate(final Template template)
      throws ReferenceServiceException, PluginManagerException, PluginServiceException, TemplateServiceException
  {
    final String templateId = template.getTemplateId();

    final ReferenceTemplate refTemplate = new ReferenceTemplate();
    refTemplate.setName(template.getName());
    refTemplate.setId(templateId);
    refTemplate.setDescription(template.getDescription());
    final TemplateProjectStatus tempStatus = template.getStatus();
    RefTemplateStatus status;
    switch (tempStatus)
    {
      case ENABLE:
        status = RefTemplateStatus.ENABLE;
        break;

      default:
        status = RefTemplateStatus.IN_PROGRESS;
        break;
    }
    refTemplate.setStatus(status);
    final List<TemplateRole> templateRoles = buildTemplateRoles(template);
    refTemplate.setRoles(templateRoles);

    final RootNode    rootNodeDTO = buildRootNode(templateId);
    final List<Space> spaces      = templateNodePresenter.getAllSpaces(templateId);

    for (final Space space : spaces)
    {
      final SpaceNode spaceNodeDTO = buildSpaceNode(space, templateId);
      final List<TemplateApplication> applications = templateNodePresenter.getAllSpaceApplications(space.getUri(),
                                                                                                   templateId);
      for (final TemplateApplication application : applications)
      {
        final PluginService pluginService = pluginsManager.getPluginService(application.getPluginUUID().toString());
        final Map<String, String> roleMappings = application.getRolesMapping();
        final PluginServiceMetadata pluginMetadata = pluginService.getMetadata();

        final ApplicationNode applicationDTO = buildTemplateApplicationNode(application, templateId, roleMappings,
                                                                            pluginMetadata);

        spaceNodeDTO.getApplications().add(applicationDTO);
      }
      rootNodeDTO.getSpaces().add(spaceNodeDTO);
    }
    refTemplate.setRootNode(rootNodeDTO);

    return refTemplate;
  }

  /**
   * @param template
   *
   * @return
   *
   * @throws ReferenceServiceException
   */
  private List<TemplateRole> buildTemplateRoles(final Template template) throws ReferenceServiceException
  {
    final List<TemplateRole> templateRoles = new ArrayList<TemplateRole>();
    List<Role> roles;
    try
    {
      roles = templateRolePresenter.getAllRoles(template.getTemplateId());
    }
    catch (final TemplateServiceException e)
    {
      throw new ReferenceServiceException(String
                                                                             .format("Unable to get the roles for the template id=%s",
                                                                                     template.getTemplateId()), e);
    }
    if ((roles != null) && !roles.isEmpty())
    {
      for (final Role role : roles)
      {
        final TemplateRole templateRole = new TemplateRole(role.getName(), role.getDescription());
        if (RealmType.SYSTEM.equals(role.getRealmType()))
        {
          templateRole.setSystem(true);
        }
        templateRoles.add(templateRole);
      }
    }
    return templateRoles;
  }

  /**
   * @param application
   * @param templateId
   * @param roleMappings
   * @param pluginMetadata
   *
   * @return
   */
  private ApplicationNode buildTemplateApplicationNode(final TemplateApplication application, final String templateId,
                                                       final Map<String, String> roleMappings,
                                                       final PluginServiceMetadata pluginMetadata)
  {

    final ApplicationNode applicationNodeDTO = new ApplicationNode(application.getUri(), application.getName());
    applicationNodeDTO.setType(pluginMetadata.getType());
    applicationNodeDTO.setCategory(pluginMetadata.getCategory());

    final Set<Map.Entry<String, String>> projectRoles = roleMappings.entrySet();
    for (final Map.Entry<String, String> projectRoleEntry : projectRoles)
    {
      final ApplicationRoleMapping roleMappingDTO = new ApplicationRoleMapping(projectRoleEntry.getKey(),
                                                                               projectRoleEntry.getValue());
      applicationNodeDTO.getRoleMappings().add(roleMappingDTO);
    }

    return applicationNodeDTO;
  }

  /**
   * @param pForgeDistributionService
   *     the forgeDistributionClient to set
   */
  public void setForgeDistributionService(final ForgeDistributionService pForgeDistributionService)
  {
    forgeDistributionService = pForgeDistributionService;
  }

  /**
   * @param pReferentielSynchroClient
   *     the referentielSynchroClient to set
   */
  public void setReferentielSynchroClient(final ReferentielSynchroClient pReferentielSynchroClient)
  {
    referentielSynchroClient = pReferentielSynchroClient;
  }

  /**
   * @param pForgeExtractionClient
   *     the forgeExtractionClient to set
   */
  public void setForgeExtractionClient(final ForgeExtractionClient pForgeExtractionClient)
  {
    forgeExtractionClient = pForgeExtractionClient;
  }

  /**
   * @param pSpaceService
   *     the pSpaceService to set
   */
  public void setSpaceService(final SpaceService pSpaceService)
  {
    spaceService = pSpaceService;
  }

  /**
   * @param pProjectPresenter
   *     the projectPresenter to set
   */
  public void setProjectPresenter(final ProjectPresenter pProjectPresenter)
  {
    projectPresenter = pProjectPresenter;
  }

  /**
   * @param pProjectRoleService
   *     the pProjectRoleService to set
   */
  public void setProjectRoleService(final ProjectRoleService pProjectRoleService)
  {
    projectRoleService = pProjectRoleService;
  }

  /**
   * @param pForgeConfigurationService
   *     the forgeConfigurationService to set
   */
  public void setForgeConfigurationService(final ForgeConfigurationService pForgeConfigurationService)
  {
    forgeConfigurationService = pForgeConfigurationService;
  }

  /**
   * @param pTimerSchedulerService
   *     the timerSchedulerService to set
   */
  public void setTimerSchedulerService(final TimerSchedulerService pTimerSchedulerService)
  {
    timerSchedulerService = pTimerSchedulerService;
  }

  /**
   * @param pTemplatePresenter
   *     the templatePresenter to set
   */
  public void setTemplatePresenter(final TemplatePresenter pTemplatePresenter)
  {
    templatePresenter = pTemplatePresenter;
  }

  /**
   * @param pTemplateNodePresenter
   *     the templateNodePresenter to set
   */
  public void setTemplateNodePresenter(final TemplateNodePresenter pTemplateNodePresenter)
  {
    templateNodePresenter = pTemplateNodePresenter;
  }

  /**
   * @param pTemplateRolePresenter
   *     the templateRolePresenter to set
   */
  public void setTemplateRolePresenter(final TemplateRolePresenter pTemplateRolePresenter)
  {
    templateRolePresenter = pTemplateRolePresenter;
  }

  /**
   * @param pReferenceToolService
   *     the referenceToolService to set
   */
  public void setReferenceToolService(final ReferenceToolService pReferenceToolService)
  {
    referenceToolService = pReferenceToolService;
  }
}