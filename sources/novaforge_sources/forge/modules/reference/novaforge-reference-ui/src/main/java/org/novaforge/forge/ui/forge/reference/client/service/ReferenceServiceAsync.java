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

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.novaforge.forge.ui.forge.reference.shared.ApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.CategoryDTO;
import org.novaforge.forge.ui.forge.reference.shared.FileInfoDTO;
import org.novaforge.forge.ui.forge.reference.shared.PluginDTO;
import org.novaforge.forge.ui.forge.reference.shared.ProjectReferenceDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.SpaceNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateLightDTO;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * The async counterpart of <code>projectservice</code>.
 */
public interface ReferenceServiceAsync
{
  void getProjectReference(final AsyncCallback<ProjectReferenceDTO> callback);

  void updateProjectReference(final ProjectReferenceDTO pProject, final AsyncCallback<Boolean> callback);

  void getPluginsRoles(final Set<String> pPluginUUID, final AsyncCallback<Map<String, Set<String>>> callback);

  void getRoles(final AsyncCallback<List<RoleDTO>> callback);

  void createSpace(final SpaceNodeDTO pSpace, final AsyncCallback<Boolean> callback);

  void updateSpace(final SpaceNodeDTO pSpace, final AsyncCallback<Boolean> callback);

  void deleteSpace(final SpaceNodeDTO pSpace, final AsyncCallback<Boolean> callback);

  void createApplication(final ApplicationNodeDTO pApp, final Map<String, String> pRolesMapping,
      final AsyncCallback<Boolean> callback);

  void updateApplication(final ApplicationNodeDTO pApp, final Map<String, String> pRolesMapping,
      final AsyncCallback<Boolean> callback);

  void deleteApplication(final ApplicationNodeDTO pApp, final AsyncCallback<Boolean> callback);

  void getPluginCategories(final String pLocale, final AsyncCallback<List<CategoryDTO>> callback);

  void getPluginCategoriesForReference(final String pLocale, final AsyncCallback<List<CategoryDTO>> callback);

  void getPluginsByCategory(final String pCategory, final AsyncCallback<List<PluginDTO>> callback);

  void getRolesMapping(final String pUUID, final String pInstance,
      final AsyncCallback<Map<String, String>> callback);

  void getPluginInfo(final String pUUID, final AsyncCallback<PluginDTO> callback);

  void getTemplates(final AsyncCallback<List<TemplateLightDTO>> callback);

  void createTemplate(final TemplateDTO pTemplate, final AsyncCallback<Boolean> callback);

  void updateTemplate(final TemplateDTO pTemplate, final AsyncCallback<Boolean> callback);

  void enableTemplate(final String templateId, final AsyncCallback<Boolean> callback);

  void disableTemplate(final String templateId, final AsyncCallback<Boolean> callback);

  void deleteTemplate(final String pTemplateId, final AsyncCallback<Boolean> callback);

  void getTemplate(final String pTemplateId, final AsyncCallback<TemplateDTO> callback);

  void isProjectReferenceCreated(final AsyncCallback<Boolean> callback);

  void newTemplate(AsyncCallback<TemplateDTO> pCb);

  void existFile(String pName, String pVersion, String suffix, Boolean pIsPublic, AsyncCallback<Boolean> pCb);

  void getExternalFile(String pName, String pVersion, String suffix, Boolean pIsPublic, String pUrl,
      AsyncCallback<Boolean> pCb);

  void getPublicTools(AsyncCallback<List<FileInfoDTO>> callback);

  void deleteFile(String id, AsyncCallback<Boolean> pCb);

  void getPrivateTools(AsyncCallback<List<FileInfoDTO>> callback);

  void isAuthorizedUpdateReference(AsyncCallback<Boolean> callback);

  void preloadTemplates(final AsyncCallback<Void> callback);
}