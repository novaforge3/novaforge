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
package org.novaforge.forge.ui.forge.reference.server;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.commons.exceptions.ForgeException;
import org.novaforge.forge.commons.technical.file.FileMeta;
import org.novaforge.forge.commons.technical.file.FileServiceException;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateApplication;
import org.novaforge.forge.core.organization.presenters.TemplatePresenter;
import org.novaforge.forge.core.organization.presenters.TemplateRolePresenter;
import org.novaforge.forge.core.plugins.categories.CategoryDefinitionService;
import org.novaforge.forge.core.plugins.categories.PluginRealm;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.core.plugins.exceptions.PluginManagerException;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginService;
import org.novaforge.forge.core.plugins.services.PluginsManager;
import org.novaforge.forge.core.security.authorization.Logical;
import org.novaforge.forge.reference.exceptions.ReferenceFacadeException;
import org.novaforge.forge.ui.forge.reference.client.service.ReferenceService;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.CategoryDTO;
import org.novaforge.forge.ui.forge.reference.shared.FileInfoDTO;
import org.novaforge.forge.ui.forge.reference.shared.PluginDTO;
import org.novaforge.forge.ui.forge.reference.shared.ProjectReferenceDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.RootNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.SpaceNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.exceptions.ErrorEnumeration;
import org.novaforge.forge.ui.forge.reference.shared.exceptions.ReferenceServiceException;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateLightDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateRootNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateSpaceNodeDTO;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * The server side implementation of the RPC service.
 * 
 * @author lamirang
 */
public class ReferenceServiceImpl extends RemoteServiceServlet implements ReferenceService
{

  /**
    * 
    */
  private static final long serialVersionUID = 1094713041847843152L;

  private static final Log  LOGGER           = LogFactory.getLog(ReferenceServiceImpl.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectReferenceDTO getProjectReference() throws ReferenceServiceException
  {
    final org.novaforge.forge.reference.facade.ReferenceService referenceFacade = OSGiHelper
        .getReferenceService();
    ProjectReferenceDTO projectDTO = null;
    String projectId;
    try
    {
      final Project project = referenceFacade.getProjectReference();
      projectId = project.getProjectId();
      projectDTO = BuildResources.buildProjectDTO(project);
      final RootNodeDTO rootNodeDTO = BuildResources.buildRootNodeDTO(projectId);
      final List<Space> spaces = referenceFacade.getSpaces();

      for (final Space space : spaces)
      {
        final SpaceNodeDTO spaceNodeDTO = BuildResources.buildSpaceNodeDTO(space, projectId);
        spaceNodeDTO.setRootNode(rootNodeDTO);
        final List<ProjectApplication> applications = referenceFacade.getAllApplicationsForSpace(space
            .getUri());
        for (final ProjectApplication application : applications)
        {
          final ApplicationNodeDTO applicationDTO = BuildResources.buildApplicationNodeDTO(application,
              projectId);
          applicationDTO.setSpaceParent(spaceNodeDTO);
          spaceNodeDTO.getApplications().add(applicationDTO);
        }
        rootNodeDTO.getSpaces().add(spaceNodeDTO);
      }
      projectDTO.setRootNode(rootNodeDTO);
    }
    catch (final ReferenceFacadeException e)
    {
      manageException("Unable to get forge reference information , pProject.getId()", e);

    }
    return projectDTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateProjectReference(final ProjectReferenceDTO pProject) throws ReferenceServiceException
  {
    final org.novaforge.forge.reference.facade.ReferenceService referenceFacade = OSGiHelper
        .getReferenceService();

    Project currentProject = null;
    try
    {
      currentProject = referenceFacade.getProjectReference();
      final String oldName = currentProject.getName();
      currentProject.setName(pProject.getName());
      currentProject.setDescription(pProject.getDescription());
      referenceFacade.updateProjectReference(oldName, currentProject);
    }
    catch (final ReferenceFacadeException e)
    {
      manageException(
          String.format("Unable to update project information with {project_id=%s}", pProject.getId()), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<RoleDTO> getRoles() throws ReferenceServiceException
  {
    final org.novaforge.forge.reference.facade.ReferenceService referenceFacade = OSGiHelper
        .getReferenceService();
    final List<RoleDTO> rolesList = new ArrayList<RoleDTO>();
    try
    {
      for (final ProjectRole role : referenceFacade.getRoles())
      {
        rolesList.add(BuildResources.buildRoleDTO(role));
      }
    }
    catch (final ReferenceFacadeException e)
    {
      manageException("Unable to get all roles for the forge reference project", e);
    }
    return rolesList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, Set<String>> getPluginsRoles(final Set<String> pPluginUUID) throws ReferenceServiceException
  {
    final Map<String, Set<String>> returnMap = new HashMap<String, Set<String>>();

    for (final String pluginId : pPluginUUID)
    {
      try
      {
        final PluginService pluginService = getPluginService(pluginId);
        if (pluginService != null)
        {
          returnMap.put(pluginId, pluginService.findRoles());
        }
      }
      catch (final PluginServiceException e)
      {
        manageException(String.format("Unable to get all roles for a plugin with [plugin_uuid=%s]", pluginId), e);
      }
    }
    return returnMap;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateSpace(final SpaceNodeDTO pSpace) throws ReferenceServiceException
  {

    final org.novaforge.forge.reference.facade.ReferenceService referenceFacade = OSGiHelper
        .getReferenceService();
    try
    {
      final Space space = referenceFacade.getSpace(pSpace.getUri());
      final String oldName = space.getName();
      space.setName(pSpace.getName());

      referenceFacade.updateSpace(oldName, space);
    }
    catch (final ReferenceFacadeException e)
    {
      manageException(String.format(
          "Unable to update space for forge reference project with [parent=%s, name=%s]", pSpace
              .getRootNode().getUri(), pSpace.getName()), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean createSpace(final SpaceNodeDTO pSpace) throws ReferenceServiceException
  {
    final org.novaforge.forge.reference.facade.ReferenceService referenceFacade = OSGiHelper.getReferenceService();

    try
    {
      referenceFacade.createSpace(pSpace.getName());
    }
    catch (final ReferenceFacadeException e)
    {
      manageException(String.format("Unable to create space for forge reference project with [parent=%s, name=%s]",
                                    pSpace.getRootNode().getUri(), pSpace.getName()), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteSpace(final SpaceNodeDTO pSpace) throws ReferenceServiceException
  {

    try
    {
      OSGiHelper.getReferenceService().deleteSpace(pSpace.getUri());
    }
    catch (final ReferenceFacadeException e)
    {
      manageException(String.format(
          "Unable to delete space for forge reference project with [parent=%s, name=%s]", pSpace
              .getRootNode().getUri(), pSpace.getName()), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean createApplication(final ApplicationNodeDTO pApp, final Map<String, String> pRolesMapping)
      throws ReferenceServiceException
  {

    try
    {
      OSGiHelper.getReferenceService().createApplication(pApp.getSpaceParent().getUri(), pApp.getName(),
          UUID.fromString(pApp.getPluginUUID()), pRolesMapping);
    }
    catch (final ReferenceFacadeException e)
    {
      manageException(
          String.format(
              "Unable to create application for forge reference project with [parent=%s, name=%s, plugin_uuid=%s]",
              pApp.getSpaceParent().getUri(), pApp.getName(), pApp.getPluginUUID()), e);
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateApplication(final ApplicationNodeDTO pApp, final Map<String, String> pRolesMapping)
      throws ReferenceServiceException
  {
    try
    {
      OSGiHelper.getReferenceService().updateApplication(pApp.getUri(), pRolesMapping);
    }
    catch (final ReferenceFacadeException e)
    {
      manageException(
          String.format(
              "Unable to update application for forge reference project with [parent=%s, name=%s, plugin_uuid=%s]",
              pApp.getSpaceParent().getUri(), pApp.getName(), pApp.getPluginUUID()), e);
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteApplication(final ApplicationNodeDTO pApp) throws ReferenceServiceException
  {
    try
    {
      OSGiHelper.getReferenceService().deleteApplication(pApp.getUri());
    }
    catch (final ReferenceFacadeException e)
    {
      manageException(
          String.format(
              "Unable to delete application for forge reference project with [parent=%s, name=%s, plugin_uuid=%s]",
              pApp.getSpaceParent().getUri(), pApp.getName(), pApp.getPluginUUID()), e);
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CategoryDTO> getPluginCategoriesForReference(final String pLocale)
      throws ReferenceServiceException
  {
    final List<CategoryDTO> categories = new ArrayList<CategoryDTO>();

    categories.addAll(filteredOnAvailableCategories(getPluginCategories(pLocale)));

    return categories;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<CategoryDTO> getPluginCategories(final String pLocale) throws ReferenceServiceException
  {
    final PluginsManager pluginsManager = OSGiHelper.getPluginsManager();
    final List<CategoryDTO> categories = new ArrayList<CategoryDTO>();
    try
    {
      final List<String> pluginsCategories = pluginsManager.getAllPluginCategories();
      if (pluginsCategories != null)
      {
        for (final String id : pluginsCategories)
        {
          final CategoryDefinitionService categoryService = OSGiHelper.getPluginsCategoryManager()
              .getCategoryService(id);

          // Don't take in account SYSTEM Categories
          if (categoryService.getRealm().equals(PluginRealm.SYSTEM))
          {
            continue;
          }

          final CategoryDTO cat = new CategoryDTO();
          cat.setId(id);

          String localizedName = id;
          if (categoryService != null)
          {
            localizedName = categoryService.getName(new Locale(pLocale));
          }

          cat.setName(localizedName);
          categories.add(cat);
        }
      }
    }
    catch (final PluginManagerException e)
    {
      manageException("Unable to get plugin's categories", e);
    }

    return categories;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<PluginDTO> getPluginsByCategory(final String pCategory) throws ReferenceServiceException
  {
    final PluginsManager pluginsManager = OSGiHelper.getPluginsManager();
    final List<PluginDTO> plugins = new ArrayList<PluginDTO>();
    try
    {
      final List<PluginMetadata> pluginMetadataList = pluginsManager
                                                          .getAllInstantiablePluginsMetadataByCategory(pCategory);
      if (pluginMetadataList != null)
      {
        for (final PluginMetadata plugin : pluginMetadataList)
        {
          final PluginService pluginService = pluginsManager.getPluginService(plugin.getUUID());
          final PluginDTO pluginDTO = new PluginDTO();
          pluginDTO.setPluginId(plugin.getUUID());
          pluginDTO.setPluginCategory(plugin.getCategory());
          pluginDTO.setPluginType(plugin.getType());
          try
          {
            pluginDTO.setRoles(pluginService.findRoles());
            plugins.add(pluginDTO);
          }
          catch (final PluginServiceException e)
          {
            LOGGER.error(String.format("Unable to get roles from plugin with [plugin=%s]", plugin.toString()), e);
          }
        }
      }

    }
    catch (final PluginManagerException e)
    {
      manageException(String.format("Unable to get plugin by category with [category=%s", pCategory), e);

    }

    return plugins;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getRolesMapping(final String pUUID, final String pInstance)
      throws ReferenceServiceException
  {
    Map<String, String> rolesMapping = null;
    try
    {
      final PluginService pluginService = getPluginService(pUUID);
      if (pluginService != null)
      {
        rolesMapping = pluginService.getRolesMapping(pInstance);
      }
    }
    catch (final PluginServiceException e)
    {
      manageException(String.format("Unable to get roles mapping for an instance of plugin with [uuid=%s, instance=%s]",
                                    pUUID, pInstance), e);
    }
    return rolesMapping;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginDTO getPluginInfo(final String pUUID) throws ReferenceServiceException
  {
    final PluginsManager pluginsManager = OSGiHelper.getPluginsManager();
    PluginDTO pluginDTO = null;
    try
    {
      final PluginService pluginService = pluginsManager.getPluginService(pUUID);
      final PluginMetadata pluginsMetadata = pluginsManager.getPluginMetadataByUUID(pUUID);
      pluginDTO = BuildResources.buildPluginDTO(pluginsMetadata, pluginService.findRoles());
    }
    catch (final PluginManagerException e)
    {
      manageException(String.format("Unable to get plugin information with [uuid=%s]", pUUID), e);

    }
    catch (final PluginServiceException e)
    {
      manageException(String.format("Unable to get plugin roles with [uuid=%s]", pUUID), e);
    }
    return pluginDTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TemplateLightDTO> getTemplates() throws ReferenceServiceException
  {
    final TemplatePresenter templatePresenter = OSGiHelper.getTemplatePresenter();
    final List<TemplateLightDTO> templates = new ArrayList<TemplateLightDTO>();
    try
    {
      final List<Template> listTemplates = templatePresenter.getTemplates();
      if (listTemplates != null)
      {
        for (final Template template : listTemplates)
        {
          templates.add(BuildResources.buildTemplateLightDTO(template));
        }
      }
    }
    catch (final TemplateServiceException e)
    {
      manageException("Unable to get the templates' list", e);
    }

    return templates;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean createTemplate(final TemplateDTO pTemplate) throws ReferenceServiceException
  {
    // TODO The following should be done in a service which composes each template proxy manager. In that
    // way we can change the way to create a template
    final String templateId = pTemplate.getId();
    final TemplatePresenter templatePresenter = OSGiHelper.getTemplatePresenter();

    try
    {
      // Created the template
      final Template currentTemplate = templatePresenter.newTemplate();
      currentTemplate.setDescription(pTemplate.getDescription());
      currentTemplate.setName(pTemplate.getName());
      currentTemplate.setTemplateId(templateId);
      templatePresenter.createTemplate(currentTemplate);

      // Add all roles to the created template
      createRole(pTemplate);

      // Add all spaces and application to the created template
      createSpacesAndApps(pTemplate);

    }
    catch (final TemplateServiceException e)
    {
      manageException(String.format("Unable to create template with [template=%s]", pTemplate), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateTemplate(final TemplateDTO pTemplate) throws ReferenceServiceException
  {

    try
    {
      deleteTemplate(pTemplate.getId());
      createTemplate(pTemplate);
    }
    catch (final ReferenceServiceException e)
    {
      manageException(String.format("Unable to update template with [template=%s]", pTemplate), e);
    }
    return true;
  }

  private boolean createRole(final TemplateDTO pTemplate) throws ReferenceServiceException
  {
    for (final RoleDTO roleDTO : pTemplate.getRoles())
    {
      final TemplateRolePresenter roleManager = OSGiHelper.getTemplateRolePresenter();
      final Role role = roleManager.newRole();
      role.setName(roleDTO.getName());
      role.setDescription(roleDTO.getDescription());

      try
      {
        if (roleDTO.isMandatory())
        {
          roleManager.createSystemRole(role, pTemplate.getId());
        }
        else
        {
          roleManager.createRole(role, pTemplate.getId());
        }

      }
      catch (final TemplateServiceException e)
      {
        manageException(String.format("Unable to create role with [template_id=%s, role_name=%s]",
            pTemplate.getId(), roleDTO.getName()), e);
      }
    }
    return true;
  }

  /**
   * @param pTemplate
   * @throws TemplateNodeServiceException
   */
  private void createSpacesAndApps(final TemplateDTO pTemplate) throws ReferenceServiceException
  {
    final String templateId = pTemplate.getId();
    try
    {
      for (final TemplateSpaceNodeDTO templateSpace : pTemplate.getRootNode().getSpaces())
      {
        final Space space = OSGiHelper.getTemplateNodePresenter().newSpace();
        space.setName(templateSpace.getName());
        final Space spaceAdded = OSGiHelper.getTemplateNodePresenter().addSpace(templateId, space);
        for (final TemplateApplicationNodeDTO templateApp : templateSpace.getApplications())
        {
          OSGiHelper.getTemplateNodePresenter().addApplication(templateId, spaceAdded.getUri(), templateApp.getName(),
                                                               UUID.fromString(templateApp.getPluginUUID()),
                                                               templateApp.getRolesMapping());
        }
      }
    }
    catch (final TemplateServiceException e)
    {
      manageException(String
                          .format("Unable to add spaces and applications defined to the template with [template_id=%s, spaces=%s]",
                                  templateId, pTemplate.getRootNode().getSpaces()), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean enableTemplate(final String templateId) throws ReferenceServiceException
  {
    final TemplatePresenter templatePresenter = OSGiHelper.getTemplatePresenter();

    boolean result = false;
    try
    {
      result = templatePresenter.enableTemplate(templateId);
    }
    catch (final TemplateServiceException e)
    {
      manageException(String.format("Unable to enable template with {template_id=%s}", templateId), e);
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean disableTemplate(final String templateId) throws ReferenceServiceException
  {
    final TemplatePresenter templatePresenter = OSGiHelper.getTemplatePresenter();

    boolean result = false;
    try
    {
      result = templatePresenter.disableTemplate(templateId);
    }
    catch (final TemplateServiceException e)
    {
      manageException(String.format("Unable to disable template with {template_id=%s}", templateId), e);
    }
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteTemplate(final String pTemplateId) throws ReferenceServiceException
  {
    final TemplatePresenter templatePresenter = OSGiHelper.getTemplatePresenter();
    try
    {
      templatePresenter.deleteTemplate(pTemplateId);
    }
    catch (final TemplateServiceException e)
    {
      manageException(String.format("Unable to delete template with {project_id=%s}", pTemplateId), e);
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateDTO getTemplate(final String pTemplateId) throws ReferenceServiceException
  {
    final TemplatePresenter templatePresenter = OSGiHelper.getTemplatePresenter();
    TemplateDTO returnTemplateDTO = null;
    try
    {
      final Template template = templatePresenter.getTemplate(pTemplateId);
      if (template != null)
      {
        returnTemplateDTO = buildTemplateDTO(template);
        returnTemplateDTO.setRoles(this.getRoles(pTemplateId));
      }
    }
    catch (final TemplateServiceException e)
    {
      manageException("Unable to get the templates'list", e);
    }

    return returnTemplateDTO;
  }

  private TemplateDTO buildTemplateDTO(final Template pTemplate) throws TemplateServiceException
  {
    final String              pTemplateId = pTemplate.getTemplateId();
    final TemplateDTO         templateDTO = BuildResources.buildTemplateDTO(pTemplate);
    final TemplateRootNodeDTO rootNodeDTO = BuildResources.buildTemplateRootNodeDTO(pTemplateId);
    final List<Space>         spaces      = OSGiHelper.getTemplateNodePresenter().getAllSpaces(pTemplateId);

    for (final Space space : spaces)
    {
      final TemplateSpaceNodeDTO spaceNodeDTO = BuildResources.buildTemplateSpaceNodeDTO(space, pTemplateId);
      spaceNodeDTO.setRootNode(rootNodeDTO);
      final List<TemplateApplication> applications = OSGiHelper.getTemplateNodePresenter().getAllSpaceApplications(space
                                                                                                                       .getUri(),
                                                                                                                   pTemplateId);
      for (final TemplateApplication application : applications)
      {
        final TemplateApplicationNodeDTO applicationDTO = BuildResources.buildTemplateApplicationNodeDTO(application,
                                                                                                         pTemplateId);
        applicationDTO.setSpaceParent(spaceNodeDTO);
        spaceNodeDTO.getApplications().add(applicationDTO);
      }
      rootNodeDTO.getSpaces().add(spaceNodeDTO);
    }
    templateDTO.setRootNode(rootNodeDTO);
    return templateDTO;
  }

  private List<RoleDTO> getRoles(final String pProjectId) throws ReferenceServiceException
  {
    final TemplateRolePresenter roleManager = OSGiHelper.getTemplateRolePresenter();
    final List<RoleDTO>         rolesList   = new ArrayList<RoleDTO>();
    try
    {
      for (final Role role : roleManager.getAllRoles(pProjectId))
      {
        rolesList.add(BuildResources.buildRoleDTO(role));
      }
    }
    catch (final TemplateServiceException e)
    {
      manageException(String.format("Unable to get all roles with [project_id=%s]", pProjectId), e);
    }
    return rolesList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isProjectReferenceCreated()
  {
    return OSGiHelper.getReferenceService().isProjectReferenceCreated();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateDTO newTemplate()
  {
    final TemplateDTO template = new TemplateDTO();
    final List<RoleDTO> rolesList = new ArrayList<RoleDTO>();
    // Getting the list of default roles for a new template
    final List<Role> defaultRoles = OSGiHelper.getTemplateRolePresenter().getDefaultRoles();
    for (final Role role : defaultRoles)
    {
      rolesList.add(BuildResources.buildRoleDTO(role));
    }
    template.setRoles(rolesList);
    return template;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existFile(final String pName, final String pVersion, final String pSuffix,
      final Boolean pIsPublic)
  {
    boolean returned = false;
    final String fileName = OSGiHelper.getReferenceToolService().getFileName(pName, pVersion, pSuffix,
        pIsPublic);
    returned = OSGiHelper.getFileService().existFile(new File(fileName));

    return returned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getExternalFile(final String pName, final String pVersion, final String pSuffix,
      final Boolean pIsPublic, final String url) throws ReferenceServiceException
  {
    boolean returned = false;
    if (!existFile(pName, pVersion, pSuffix, pIsPublic))
    {
      FileMeta fileMeta;
      try
      {
        fileMeta = OSGiHelper.getFileService().downloadFile(url);
        OSGiHelper.getFileService().storeFile(fileMeta.getFile(),
            new File(OSGiHelper.getReferenceToolService().getFileName(pName, pVersion, pSuffix, pIsPublic)));
        returned = true;
      }
      catch (final FileServiceException e)
      {
        manageException(e.getMessage(), e);
      }

    }
    return returned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<FileInfoDTO> getPublicTools() throws ReferenceServiceException
  {
    return getToolsList(true);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<FileInfoDTO> getPrivateTools() throws ReferenceServiceException
  {
    return getToolsList(false);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteFile(final String pFilename)
  {
    boolean    returned = false;
    final File file     = new File(pFilename);
    if (file.isFile())
    {
      returned = file.delete();
    }

    return returned;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAuthorizedUpdateReference()
  {
    final String      refProjectId = OSGiHelper.getForgeConfigurationService().getReferentielProjectId();
    final Set<String> permissions  = new HashSet<String>(1);
    permissions.add(OSGiHelper.getPermissionHandler().buildProjectAdminPermission(refProjectId));
    return OSGiHelper.getAuthorizationService().isExplicitlyPermitted(permissions, Logical.AND);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void preloadTemplates() throws ReferenceServiceException
  {
    try
    {
      OSGiHelper.getTemplateLoaderService().loadTemplates();
    }
    catch (final TemplateServiceException e)
    {
      manageException(e.getMessage(), e);
    }
  }

  private List<FileInfoDTO> getToolsList(final boolean isPublic) throws ReferenceServiceException
  {
    final List<FileInfoDTO> fileInfoList = new ArrayList<FileInfoDTO>();
    try
    {
      List<File> fileList;
      if (isPublic)
      {
        fileList = OSGiHelper.getFileService().getAllFiles(
            OSGiHelper.getReferenceToolService().getUserStorageDirectory());
      }
      else
      {
        fileList = OSGiHelper.getFileService().getAllFiles(
            OSGiHelper.getReferenceToolService().getAdminStorageDirectory());
      }

      for (final File file : fileList)
      {
        if (file.isFile())
        {
          final FileInfoDTO fileInfoDTO = new FileInfoDTO();
          fileInfoDTO.setId(file.getAbsolutePath());
          fileInfoDTO.setPublic(isPublic);
          fileInfoDTO.setPath(file.getParent());
          fileInfoDTO.setSize(file.length());

          final String[] str = file.getName().split("--");
          if (str[0] != null)
          {
            fileInfoDTO.setName(str[0]);
          }
          if (str.length > 1)
          {
            if (str[1].contains("."))
            {
              fileInfoDTO.setVersion(str[1].substring(0, str[1].lastIndexOf(".")));
              fileInfoDTO.setExtension(str[1].substring(str[1].lastIndexOf(".")));
            }
            else
            {
              fileInfoDTO.setVersion(str[1]);
            }
          }
          fileInfoList.add(fileInfoDTO);
        }
      }
    }
    catch (final FileServiceException e)
    {
      manageException(e.getMessage(), e);
    }
    return fileInfoList;
  }

  private Collection<CategoryDTO> filteredOnAvailableCategories(final List<CategoryDTO> pCategories)
  {
    final List<CategoryDTO> availableCategories = new ArrayList<CategoryDTO>();
    try
    {
      final List<PluginMetadata> pluginMetadataList = OSGiHelper.getPluginsManager().getAllPlugins();
      for (final PluginMetadata pluginMetadata : pluginMetadataList)
      {
        final PluginService pluginService = OSGiHelper.getPluginsManager().getPluginService(pluginMetadata.getUUID());
        if (pluginService.getPluginDataServiceUri() != null)
        {
          final CategoryDTO category = new CategoryDTO();
          category.setId(pluginMetadata.getCategory());
          availableCategories.add(category);
        }
      }
    }
    catch (final PluginManagerException e)
    {
      LOGGER.warn("Unable to get the plugins information, not filter done on available categories.");
    }

    return Collections2.filter(pCategories, new Predicate<CategoryDTO>()
    {
      @Override
      public boolean apply(final CategoryDTO pInput)
      {
        if (availableCategories.contains(pInput) == true)
        {
          return true;
        }
        else
        {
          return false;
        }
      }
    });
  }

  private PluginService getPluginService(final String pluginUUID) throws ReferenceServiceException
  {
    final PluginsManager pluginsManager = OSGiHelper.getPluginsManager();
    PluginService        pluginService  = null;
    try
    {
      if (pluginsManager.isAvailablePlugin(pluginUUID))
      {
        pluginService = pluginsManager.getPluginService(pluginUUID);
      }
    }
    catch (final PluginManagerException e)
    {
      manageException(String.format("Unable to get plugin service with [id=%s]", pluginUUID), e);

    }
    return pluginService;
  }

  /**
   * This method manages exceptions in order to write log error and get ForgeCodeException
   *
   * @param e
   * @throws ReferenceServiceException
   */
  private void manageException(final String pMessage, final Exception e) throws ReferenceServiceException
  {
    // handle functional exceptions

    LOGGER.error(pMessage + " : " + e.getMessage());
    if ((e instanceof ForgeException) && (((ForgeException) e).getCode() != null))
    {
      final ForgeException fe = (ForgeException) e;
      if (fe.getCode().equals(ExceptionCode.TECHNICAL_ERROR))
      {
        LOGGER.error(e.getMessage(), e);
      }
      final ErrorEnumeration error = ErrorEnumeration.valueOf(fe.getCode().getName());
      throw new ReferenceServiceException(error, e);
    }
    else
    {
      LOGGER.error(e.getMessage(), e);
      throw new ReferenceServiceException(ErrorEnumeration.TECHNICAL_ERROR, e);
    }
  }
}
