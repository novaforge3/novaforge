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
package org.novaforge.forge.ui.forge.reference.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.CategoryDTO;
import org.novaforge.forge.ui.forge.reference.shared.FileInfoDTO;
import org.novaforge.forge.ui.forge.reference.shared.PluginDTO;
import org.novaforge.forge.ui.forge.reference.shared.ProjectReferenceDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.SpaceNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.exceptions.ReferenceServiceException;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateLightDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("referenceservice")
public interface ReferenceService extends RemoteService
{
  ProjectReferenceDTO getProjectReference() throws ReferenceServiceException;

  boolean updateProjectReference(ProjectReferenceDTO pProject) throws ReferenceServiceException;

  List<RoleDTO> getRoles() throws ReferenceServiceException;

  Map<String, Set<String>> getPluginsRoles(final Set<String> pPluginUUID) throws ReferenceServiceException;

  boolean updateSpace(final SpaceNodeDTO pSpace) throws ReferenceServiceException;

  boolean createSpace(final SpaceNodeDTO pSpace) throws ReferenceServiceException;

  boolean deleteSpace(final SpaceNodeDTO pSpace) throws ReferenceServiceException;

  boolean createApplication(final ApplicationNodeDTO pApp, final Map<String, String> pRolesMapping)
      throws ReferenceServiceException;

  boolean updateApplication(final ApplicationNodeDTO pApp, final Map<String, String> pRolesMapping)
      throws ReferenceServiceException;

  boolean deleteApplication(final ApplicationNodeDTO pApp) throws ReferenceServiceException;

  List<CategoryDTO> getPluginCategoriesForReference(final String pLocale) throws ReferenceServiceException;

  List<CategoryDTO> getPluginCategories(final String pLocale) throws ReferenceServiceException;

  List<PluginDTO> getPluginsByCategory(final String pCategory) throws ReferenceServiceException;

  Map<String, String> getRolesMapping(final String pUUID, final String pInstance)
      throws ReferenceServiceException;

  PluginDTO getPluginInfo(final String pUUID) throws ReferenceServiceException;

  List<TemplateLightDTO> getTemplates() throws ReferenceServiceException;

  boolean createTemplate(final TemplateDTO pTemplate) throws ReferenceServiceException;

  boolean updateTemplate(final TemplateDTO pTemplate) throws ReferenceServiceException;

  boolean enableTemplate(final String templateId) throws ReferenceServiceException;

  boolean disableTemplate(final String templateId) throws ReferenceServiceException;

  boolean deleteTemplate(final String pTemplateId) throws ReferenceServiceException;

  TemplateDTO getTemplate(String pTemplateId) throws ReferenceServiceException;

  boolean isProjectReferenceCreated();

  TemplateDTO newTemplate();

  boolean existFile(String pName, String pVersion, String suffix, Boolean pIsPublic);

  boolean getExternalFile(String pName, String pVersion, String suffix, Boolean pIsPublic, String pUrl)
      throws ReferenceServiceException;

  List<FileInfoDTO> getPublicTools() throws ReferenceServiceException;

  List<FileInfoDTO> getPrivateTools() throws ReferenceServiceException;

  boolean deleteFile(String pFilename);

  boolean isAuthorizedUpdateReference();

  void preloadTemplates() throws ReferenceServiceException;
}
