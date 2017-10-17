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

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
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
import org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions.DeliveryManagementServiceException;

import java.util.List;
import java.util.Map;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("deliverymanagementservice")
public interface DeliveryManagementService extends RemoteService
{
  /**
   * @param pProjectId
   * @param pReference
   * @param dto
   * @return
   * @throws DeliveryManagementServiceException
   */
  String saveDelivery(final String pProjectId, final String pReference, final DeliveryDTO dto)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean deleteDelivery(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @return
   * @throws DeliveryManagementServiceException
   */
  DeliveryDTO getDelivery(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @return
   * @throws DeliveryManagementServiceException
   */
  List<NodeDTO> getAvailablesDocuments(final String pProjectId) throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @return
   * @throws DeliveryManagementServiceException
   */
  List<NodeDTO> getAvailablesSources(final String pProjectId) throws DeliveryManagementServiceException;

  /**
   * @return
   * @throws DeliveryManagementServiceException
   */
  List<DeliveryStatusDTO> getStatus() throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @return
   * @throws DeliveryManagementServiceException
   */
  List<String> getAllTypes(final String pProjectId) throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @return
   * @throws DeliveryManagementServiceException
   */
  List<DeliveryDTO> getDeliveryList(final String pProjectId) throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pDeliveryPrevisouStatus
   * @param pUser
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean generateDelivery(String pProjectId, String pReference) throws DeliveryManagementServiceException;

  /**
   * @param pInstanceId
   * @return
   * @throws DeliveryManagementServiceException
   */
  String getProjectId(final String pInstanceId) throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pType
   * @return
   * @throws DeliveryManagementServiceException
   */
  ContentDTO getContent(String pProjectId, String pReference, ContentTypeDTO pType)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pUrl
   * @param pFileName
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean getExternalFile(String pProjectId, String pReference, String pUrl, String pFileName)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pFieldName
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean deleteFileArtefact(String pProjectId, String pReference, String pFieldName)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pFileName
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean existFile(String pProjectId, String pReference, String pFileName)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pName
   * @param pNewName
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean updateFileArtefact(String pProjectId, String pReference, String pName, String pNewName)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pVersion
   * @return
   * @throws DeliveryManagementServiceException
   */
  List<BugTrackerIssueDTO> getIssues(String pProjectId, String pReference, String pVersion)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @return
   * @throws DeliveryManagementServiceException
   */
  IssueContentDTO getIssueContent(String pProjectId, String pReference)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pVersion
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean updateBugsVersion(String pProjectId, String pReference, String pVersion)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pDocumentsTree
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean updateDocuments(String pProjectId, String pReference, FolderNode pDocumentsTree)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pDocumentsTree
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean updateSources(String pProjectId, String pReference, FolderNode pDocumentsTree)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @return
   * @throws DeliveryManagementServiceException
   */
  List<TemplateDTO> getTemplateReportList(final String pProjectId)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pPreviousName
   * @param pBasename
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean existTemplateFile(String pProjectId, String pPreviousName, String pBasename)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pTemplateName
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean deleteTemplateReport(String pProjectId, String pTemplateName)
      throws DeliveryManagementServiceException;

  /**
   * @param pTemplateName
   * @param pTemplate
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean updateTemplateReport(String pTemplateName, TemplateDTO pTemplate)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pTemplateName
   * @param pTemplate
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean saveTemplateReport(String pProjectId, String pTemplateName, TemplateDTO pTemplate)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @return
   * @throws DeliveryManagementServiceException
   */
  NoteContentDTO getNoteContent(String pProjectId, String pReference)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pTemplateName
   * @param pFields
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean updateNoteTemplate(String pProjectId, String pReference, String pTemplateName,
      Map<String, String> pFields) throws DeliveryManagementServiceException;

  /**
   * This will return the available content for the delivery application.
   * 
   * @param pProjectId
   *          current project id
   * @return list of {@link ContentTypeDTO} for a project
   * @throws DeliveryManagementServiceException
   */
  List<ContentTypeDTO> getAvailableContent(final String pProjectId) throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean lockDelivery(String pProjectId, String pReference) throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @param pReference
   * @param pStatus
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean generateDeliveryStatus(String pProjectId, String pReference)
      throws DeliveryManagementServiceException;

  /**
   * @param pProjectId
   * @return
   * @throws DeliveryManagementServiceException
   */
  boolean canEdit(final String pProjectId) throws DeliveryManagementServiceException;

}
