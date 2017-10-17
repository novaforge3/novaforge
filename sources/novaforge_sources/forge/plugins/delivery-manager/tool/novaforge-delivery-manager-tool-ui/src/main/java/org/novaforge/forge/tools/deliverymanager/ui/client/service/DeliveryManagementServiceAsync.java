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
package org.novaforge.forge.tools.deliverymanager.ui.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import org.novaforge.forge.tools.deliverymanager.ui.shared.BugTrackerIssueDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ContentTypeDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.DeliveryStatusDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.FolderNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.IssueContentDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.NodeDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.NoteContentDTO;
import org.novaforge.forge.tools.deliverymanager.ui.shared.TemplateDTO;

import java.util.List;
import java.util.Map;

/**
 * The async counterpart of <code>DeliveryService</code>.
 */
/**
 * @author CASERY-J
 */
public interface DeliveryManagementServiceAsync
{
  /**
   * @param pProjectId
   * @param pReference
   * @param pDelivery
   * @param callback
   */
  void saveDelivery(final String pProjectId, final String pReference, final DeliveryDTO pDelivery,
      final AsyncCallback<String> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param callback
   */
  void getDelivery(final String pProjectId, final String pReference, AsyncCallback<DeliveryDTO> callback);

  /**
   * @param pProjectId
   * @param callback
   */
  void getAvailablesDocuments(final String pProjectId, AsyncCallback<List<NodeDTO>> callback);

  /**
   * @param pProjectId
   * @param callback
   */
  void getAvailablesSources(final String pProjectId, AsyncCallback<List<NodeDTO>> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param pDocumentsTree
   * @param callback
   */
  void updateDocuments(String pProjectId, String pReference, FolderNode pDocumentsTree,
      AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param pDocumentsTree
   * @param callback
   */
  void updateSources(String pProjectId, String pReference, FolderNode pDocumentsTree,
      AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param callback
   */
  void getAllTypes(final String pProjectId, AsyncCallback<List<String>> callback);

  /**
   * @param callback
   */
  void getStatus(AsyncCallback<List<DeliveryStatusDTO>> callback);

  /**
   * @param pProjectId
   * @param callback
   */
  void getDeliveryList(final String pProjectId, AsyncCallback<List<DeliveryDTO>> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param pDeliveryPrevisouStatus
   * @param pUser
   * @param callback
   */
  void generateDelivery(final String pProjectId, final String pReference, AsyncCallback<Boolean> callback);

  /**
   * @param pInstanceID
   * @param callback
   */
  void getProjectId(String pInstanceID, AsyncCallback<String> callback);

  /**
   * @param pProjectId
   * @param pDeliveryId
   * @param pCb
   */
  void deleteDelivery(final String pProjectId, final String pDeliveryId, final AsyncCallback<Boolean> pCb);

  /**
   * @param pProjectId
   * @param pReference
   * @param pType
   * @param callback
   */
  void getContent(String pProjectId, String pReference, ContentTypeDTO pType,
      AsyncCallback<ContentDTO> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param pUrl
   * @param pFileName
   * @param callback
   */
  void getExternalFile(String pProjectId, String pReference, String pUrl, String pFileName,
      AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param pFieldName
   * @param callback
   */
  void deleteFileArtefact(String pProjectId, String pReference, String pFieldName,
      AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param pFileName
   * @param callback
   */
  void existFile(String pProjectId, String pReference, String pFileName, AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param pName
   * @param pNewName
   * @param pCb
   */
  void updateFileArtefact(String pProjectId, String pReference, String pName, String pNewName,
      AsyncCallback<Boolean> pCb);

  /**
   * @param pProjectId
   * @param pReference
   * @param pVersion
   * @param callback
   */
  void getIssues(String pProjectId, String pReference, String pVersion,
      AsyncCallback<List<BugTrackerIssueDTO>> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param callback
   */
  void getIssueContent(String pProjectId, String pReference, AsyncCallback<IssueContentDTO> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param pVersion
   * @param callback
   */
  void updateBugsVersion(String pProjectId, String pReference, String pVersion,
      AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param pCb
   */
  void getTemplateReportList(String pProjectId, AsyncCallback<List<TemplateDTO>> pCb);

  /**
   * @param pProjectId
   * @param pPreviousName
   * @param pBasename
   * @param pCb
   */
  void existTemplateFile(String pProjectId, String pPreviousName, String pBasename, AsyncCallback<Boolean> pCb);

  /**
   * @param pProjectId
   * @param pTemplateName
   * @param callback
   */
  void deleteTemplateReport(String pProjectId, String pTemplateName, AsyncCallback<Boolean> callback);

  /**
   * @param pTemplateName
   * @param pTemplate
   * @param callback
   */
  void updateTemplateReport(String pTemplateName, TemplateDTO pTemplate, AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param pTemplateName
   * @param pTemplate
   * @param callback
   */
  void saveTemplateReport(String pProjectId, String pTemplateName, TemplateDTO pTemplate,
      AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param callback
   */
  void getNoteContent(String pProjectId, String pReference, AsyncCallback<NoteContentDTO> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param pTemplateName
   * @param pFields
   * @param callback
   */
  void updateNoteTemplate(String pProjectId, String pReference, String pTemplateName,
      Map<String, String> pFields, AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param callback
   */
  void getAvailableContent(String pProjectId, AsyncCallback<List<ContentTypeDTO>> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param callback
   */
  void lockDelivery(String pProjectId, String pReference, AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param pReference
   * @param callback
   */
  void generateDeliveryStatus(String pProjectId, String pReference, AsyncCallback<Boolean> callback);

  /**
   * @param pProjectId
   * @param callback
   */
  void canEdit(final String pProjectId, AsyncCallback<Boolean> callback);
}
