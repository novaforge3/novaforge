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

import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateApplication;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;
import org.novaforge.forge.core.plugins.domain.core.PluginMetadata;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.PluginDTO;
import org.novaforge.forge.ui.forge.reference.shared.ProjectReferenceDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.RootNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.SpaceNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateLightDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateRootNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateSpaceNodeDTO;

import java.util.Date;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author lamirang
 */
public class BuildResources
{

  public static RoleDTO buildRoleDTO(final Role pRole)
  {
    final RoleDTO role = new RoleDTO();
    role.setName(pRole.getName());
    role.setDescription(pRole.getDescription());
    role.setOrder(pRole.getOrder());
    if (RealmType.SYSTEM.equals(pRole.getRealmType()))
    {
      role.setMandatory(true);
    }
    else
    {
      role.setMandatory(false);
    }
    return role;

  }

  public static ProjectReferenceDTO buildProjectDTO(final Project pProject)
  {
    final ProjectReferenceDTO referenceDTO = new ProjectReferenceDTO();
    referenceDTO.setId(pProject.getProjectId());
    referenceDTO.setName(pProject.getName());
    referenceDTO.setDescription(pProject.getDescription());
    return referenceDTO;
  }

  public static RootNodeDTO buildRootNodeDTO(final String pProjectId)
  {
    return new RootNodeDTO(pProjectId, pProjectId);
  }

  public static SpaceNodeDTO buildSpaceNodeDTO(final Space pSpaceNode, final String pProjectId)
  {
    return new SpaceNodeDTO(pSpaceNode.getUri(), pSpaceNode.getName());
  }

  public static ApplicationNodeDTO buildApplicationNodeDTO(final ProjectApplication pApplicationNode,
      final String pProjectId)
  {
    final ApplicationNodeDTO applicationNodeDTO = new ApplicationNodeDTO(pApplicationNode.getUri(),
        pApplicationNode.getName());
    applicationNodeDTO.setPluginUUID(pApplicationNode.getPluginUUID().toString());
    applicationNodeDTO.setInstanceId(pApplicationNode.getPluginInstanceUUID().toString());
    return applicationNodeDTO;
  }

  public static TemplateRootNodeDTO buildTemplateRootNodeDTO(final String pProjectId)
  {
    return new TemplateRootNodeDTO(pProjectId, pProjectId);
  }

  public static TemplateSpaceNodeDTO buildTemplateSpaceNodeDTO(final Space pSpaceNode, final String pProjectId)
  {
    return new TemplateSpaceNodeDTO(pSpaceNode.getUri(),
        pSpaceNode.getName());
  }

  public static TemplateApplicationNodeDTO buildTemplateApplicationNodeDTO(
      final TemplateApplication pApplicationNode, final String pTemplateId)
  {
    final TemplateApplicationNodeDTO applicationNodeDTO = new TemplateApplicationNodeDTO(
        pApplicationNode.getUri(), pApplicationNode.getName());
    applicationNodeDTO.setPluginUUID(pApplicationNode.getPluginUUID().toString());
    final Set<Entry<String, String>> entrySet = pApplicationNode.getRolesMapping().entrySet();
    for (final Entry<String, String> entry : entrySet)
    {
      applicationNodeDTO.getRolesMapping().put(entry.getKey(), entry.getValue());

    }
    return applicationNodeDTO;
  }

  /**
   * @param pPluginsMetadata
   * @return PluginDTO built
   */
  public static PluginDTO buildPluginDTO(final PluginMetadata pPluginsMetadata, final Set<String> pList)
  {
    final PluginDTO pluginDTO = new PluginDTO();
    pluginDTO.setPluginId(pPluginsMetadata.getUUID());
    pluginDTO.setPluginType(pPluginsMetadata.getType());
    pluginDTO.setPluginCategory(pPluginsMetadata.getCategory());
    pluginDTO.setRoles(pList);
    return pluginDTO;
  }

  /**
   * @param pTemplate
   * @return TemplateDTO built
   */
  public static TemplateDTO buildTemplateDTO(final Template pTemplate)
  {
    final TemplateDTO templateDTO = new TemplateDTO();
    templateDTO.setId(pTemplate.getElementId());
    templateDTO.setName(pTemplate.getName());
    templateDTO.setDescription(pTemplate.getDescription());
    templateDTO.setCreatedDate(new Date(pTemplate.getCreated().getTime()));
    if (TemplateProjectStatus.IN_PROGRESS.equals(pTemplate.getStatus()))
    {
      templateDTO.setStatus(TemplateDTO.TEMPLATE_STATUS_IN_PROGRESS);
    }
    else if (TemplateProjectStatus.ENABLE.equals(pTemplate.getStatus()))
    {
      templateDTO.setStatus(TemplateDTO.TEMPLATE_STATUS_ENABLE);
    }
    return templateDTO;
  }

  /**
   * @param pTemplate
   * @return TemplateLightDTO built
   */
  public static TemplateLightDTO buildTemplateLightDTO(final Template pTemplate)
  {
    final TemplateLightDTO templateDTO = new TemplateLightDTO();
    templateDTO.setId(pTemplate.getElementId());
    templateDTO.setName(pTemplate.getName());
    return templateDTO;
  }
}
