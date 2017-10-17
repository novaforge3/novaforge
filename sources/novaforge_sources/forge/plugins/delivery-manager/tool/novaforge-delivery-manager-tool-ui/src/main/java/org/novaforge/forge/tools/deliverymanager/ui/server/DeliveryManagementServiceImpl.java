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
package org.novaforge.forge.tools.deliverymanager.ui.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.exceptions.TemplateReportServiceException;
import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.BugTrackerIssue;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;
import org.novaforge.forge.tools.deliverymanager.model.ECMNode;
import org.novaforge.forge.tools.deliverymanager.model.Folder;
import org.novaforge.forge.tools.deliverymanager.model.Node;
import org.novaforge.forge.tools.deliverymanager.model.SCMNode;
import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;
import org.novaforge.forge.tools.deliverymanager.ui.client.service.DeliveryManagementService;
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
import org.novaforge.forge.tools.deliverymanager.ui.shared.exceptions.ExceptionCode;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * The server side implementation of the RPC service.
 */
public class DeliveryManagementServiceImpl extends RemoteServiceServlet implements DeliveryManagementService
{

  /**
   *
   */
  private static final long serialVersionUID = -8645913663558049472L;

  private static final Log log = LogFactory.getLog(DeliveryManagementServiceImpl.class);

  /**
   * {@inheritDoc}
   */
  @Override
  public String saveDelivery(final String pProjectId, final String pReference, final DeliveryDTO pDelivery)
      throws DeliveryManagementServiceException
  {

    String returnedDeliveryId = null;
    try
    {
      if ((pReference != null) && (!"".equals(pReference)))
      {
        final Delivery delivery = OSGiServiceGetter.getDeliveryManager().getDelivery(pProjectId, pReference);
        delivery.setReference(pDelivery.getReference());
        if (!pReference.equals(pDelivery.getReference()))
        {
          OSGiServiceGetter.getDeliveryRepositoryService().updateDeliveryReferenceDirectoryName(pProjectId, pReference,
                                                                                                pDelivery
                                                                                                    .getReference());
        }
        delivery.setName(pDelivery.getName());
        delivery.setVersion(pDelivery.getVersion());
        final Delivery deliveryReturned = OSGiServiceGetter.getDeliveryManager().updateDelivery(pReference, delivery,
                                                                                                pDelivery.getType());

        returnedDeliveryId = deliveryReturned.getReference();

      }
      else
      {
        final Delivery delivery = OSGiServiceGetter.getDeliveryManager().newDelivery();
        delivery.setReference(pDelivery.getReference());
        delivery.setName(pDelivery.getName());
        delivery.setVersion(pDelivery.getVersion());
        delivery.setProjectId(pProjectId);
        final Delivery deliveryReturned = OSGiServiceGetter.getDeliveryManager().createDelivery(delivery,
                                                                                                DeliveryStatus.CREATED,
                                                                                                pDelivery.getType());
        returnedDeliveryId = deliveryReturned.getReference();
      }
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to create delivery with [project_id=%s, reference=%s, delivery=%s",
                                    pProjectId, pReference, pDelivery), e);
    }

    // Build content map
    final Map<ContentType, String>           contentTypes = new HashMap<ContentType, String>();
    final Set<Entry<ContentTypeDTO, String>> entrySet     = pDelivery.getContents().entrySet();
    for (final Entry<ContentTypeDTO, String> entry : entrySet)
    {
      contentTypes.put(ContentType.getById(entry.getKey().getId()), entry.getValue());
    }

    // Get bug version from service (not displayed neither modified in detailed view)
    if (contentTypes.containsKey(ContentType.getById(ContentTypeDTO.BUG.getId())))
    {
      contentTypes.put(ContentType.getById(ContentTypeDTO.BUG.getId()), getBugVersion(pProjectId,
                                                                                      pDelivery.getReference()));
    }

    // Update content associated to the delivery
    updateContents(pProjectId, returnedDeliveryId, new ArrayList<ContentType>(contentTypes.keySet()));

    // Update content node
    updateContentNode(pProjectId, returnedDeliveryId, contentTypes);

    // return new id
    return returnedDeliveryId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteDelivery(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException
  {

    try
    {
      OSGiServiceGetter.getDeliveryManager().deleteDelivery(pProjectId, pReference);
    }
    catch (final Exception e)
    {
      manageException("Unable to delete delivery", e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public DeliveryDTO getDelivery(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException
  {

    DeliveryDTO dto = null;
    try
    {
      // Build delivery DTO
      final Delivery delivery = OSGiServiceGetter.getDeliveryManager().getDelivery(pProjectId, pReference);
      dto = Resources.buildDTO(delivery);

      // Build content DTO
      final List<Content> contents = OSGiServiceGetter.getDeliveryManager().getContents(pProjectId, pReference);
      final Map<ContentTypeDTO, String> map = new HashMap<ContentTypeDTO, String>();
      for (final Content content : contents)
      {
        final ContentTypeDTO typeDTO = Resources.buildDTO(content.getType());
        String name = typeDTO.getId();
        if (content.getNode() != null)
        {
          name = content.getNode().getName();
        }
        map.put(typeDTO, name);
      }
      dto.setContents(map);

    }
    catch (final Exception e)
    {
      manageException("Unable to get delivery", e);
    }
    return dto;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<NodeDTO> getAvailablesDocuments(final String pProjectId) throws DeliveryManagementServiceException
  {
    final List<NodeDTO> nodes = new ArrayList<NodeDTO>();
    try
    {
      final String remoteUser = getThreadLocalRequest().getRemoteUser();
      final ECMNode ecmNode = OSGiServiceGetter.getDeliveryPluginService().getECMNode(pProjectId, remoteUser);
      nodes.addAll(Resources.buildECMDTO(ecmNode.getChildren(), "/"));
    }
    catch (final Exception e)
    {
      manageException("Unable to get availables documents", e);
    }
    return nodes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<NodeDTO> getAvailablesSources(final String pProjectId) throws DeliveryManagementServiceException
  {
    final List<NodeDTO> nodes = new ArrayList<NodeDTO>();
    try
    {
      final String remoteUser = getThreadLocalRequest().getRemoteUser();
      final SCMNode scmNode = OSGiServiceGetter.getDeliveryPluginService().getSCMNode(pProjectId, remoteUser);
      nodes.addAll(Resources.buildSCMDTO(scmNode.getChildren(), "/"));
    }
    catch (final Exception e)
    {
      manageException("Unable to get availables documents", e);
    }
    return nodes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DeliveryStatusDTO> getStatus() throws DeliveryManagementServiceException
  {

    final List<DeliveryStatusDTO> listStatus = new ArrayList<DeliveryStatusDTO>();
    try
    {
      final DeliveryStatusDTO[] values = DeliveryStatusDTO.values();
      Collections.addAll(listStatus, values);
    }
    catch (final Exception e)
    {
      manageException("Unable to get status", e);
    }

    return listStatus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> getAllTypes(final String pProjectId) throws DeliveryManagementServiceException
  {

    final List<String> listTypes = new ArrayList<String>();
    try
    {
      listTypes.addAll(OSGiServiceGetter.getDeliveryManager().getTypes(pProjectId));
    }
    catch (final Exception e)
    {
      manageException("Unable to get types", e);
    }

    return listTypes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<DeliveryDTO> getDeliveryList(final String pProjectId) throws DeliveryManagementServiceException
  {

    final List<DeliveryDTO> deliveriesList = new ArrayList<DeliveryDTO>();
    try
    {
      final List<Delivery> deliveries = OSGiServiceGetter.getDeliveryManager().getDeliveries(pProjectId);
      if (deliveries != null)
      {
        for (final Delivery delivery : deliveries)
        {
          // Build delivery DTO
          final DeliveryDTO dto = Resources.buildDTO(delivery);

          // Build content DTO
          final List<Content> contents = OSGiServiceGetter.getDeliveryManager().getContents(pProjectId,
                                                                                            delivery.getReference());
          final Map<ContentTypeDTO, String> map = new HashMap<ContentTypeDTO, String>();
          for (final Content content : contents)
          {
            final ContentTypeDTO typeDTO = Resources.buildDTO(content.getType());
            String name = typeDTO.getId();
            if (content.getNode() != null)
            {
              name = content.getNode().getName();
            }
            map.put(typeDTO, name);
          }
          dto.setContents(map);
          deliveriesList.add(dto);
        }
      }
    }
    catch (final Exception e)
    {
      manageException("Unable to get Deliveries", e);
    }

    return deliveriesList;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean generateDelivery(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException
  {
    boolean success = false;
    try
    {
      final String remoteUser = getThreadLocalRequest().getRemoteUser();
      success = OSGiServiceGetter.getDeliveryManager().generateDelivery(pProjectId, pReference, remoteUser);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String.format("Unable to generate delivery with [delivery_id=%s]", pReference), e);
    }
    return success;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProjectId(final String pInstanceId) throws DeliveryManagementServiceException
  {

    String result = null;

    try
    {
      result = OSGiServiceGetter.getDeliveryPluginService().getProjectId(pInstanceId);
    }
    catch (final DeliveryServiceException e)
    {
      manageException("Unable to get ProjectId", e);
    }

    return result;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ContentDTO getContent(final String pProjectId, final String pReference, final ContentTypeDTO pType)
      throws DeliveryManagementServiceException
  {
    ContentDTO dto = null;
    try
    {
      final Content content = OSGiServiceGetter.getDeliveryManager().getContent(pProjectId, pReference,
                                                                                Resources.build(pType));
      dto = Resources.buildDTO(content);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String.format("Unable to get content with [project_id=%s, reference=%s, type=%s", pProjectId,
                                    pReference, pType), e);
    }
    return dto;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getExternalFile(final String pProjectId, final String pReference, final String pUrl,
                                 final String pFileName) throws DeliveryManagementServiceException
  {
    try
    {
      OSGiServiceGetter.getDeliveryManager().getExternalFile(pProjectId, pReference, pUrl, pFileName);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String
                          .format("Unable to get external file with [project_id=%s, reference=%s, url=%s, filename=%s]",
                                  pProjectId, pReference, pUrl, pFileName), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteFileArtefact(final String pProjectId, final String pReference, final String pFieldName)
      throws DeliveryManagementServiceException
  {
    try
    {
      final Content content = OSGiServiceGetter.getDeliveryManager().getContent(pProjectId, pReference,
                                                                                ContentType.FILE);
      final boolean removeArtefact = removeArtefact(pFieldName, pProjectId, pReference, content);
      final File uploadedFile = new File(OSGiServiceGetter.getDeliveryManager().getContentPath(pProjectId, pReference,
                                                                                               ContentType.FILE),
                                         pFieldName);

      if ((removeArtefact) && (uploadedFile.exists()))
      {
        uploadedFile.delete();
      }
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String.format("Unable to delete file artefact with [project=%s, reference=%s, file_name=%s]",
                                    pProjectId, pReference, pFieldName), e);
    }
    return true;
  }

  /**
   * @param pFieldName
   * @param projectId
   * @param reference
   * @param content
   * @param folder
   * @param childNodes
   *
   * @throws DeliveryServiceException
   */
  private boolean removeArtefact(final String pFieldName, final String projectId, final String reference,
                                 final Content content) throws DeliveryServiceException
  {
    boolean found = false;
    final Folder folder = (Folder) content.getNode();
    for (final Node node : new ArrayList<Node>(folder.getChildNodes()))
    {
      if (node instanceof Artefact)
      {
        if ((((Artefact) node).getIdentifiant().equals(pFieldName)) && ((((Artefact) node).getName()
                                                                                          .equals(pFieldName))))
        {
          folder.removeChildNode(node);
          found = true;
          break;
        }
      }
    }
    OSGiServiceGetter.getDeliveryManager().updateContentNode(projectId, reference, null, content);

    return found;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existFile(final String pProjectId, final String pReference, final String pFileName)
      throws DeliveryManagementServiceException
  {
    boolean isExist = false;
    try
    {
      isExist = OSGiServiceGetter.getDeliveryManager().existFile(pProjectId, pReference, pFileName);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String
                          .format("Unable to check if the filename is already used external file with [project_id=%s, reference=%s, filename=%s]",
                                  pProjectId, pReference, pFileName), e);
    }
    return isExist;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateFileArtefact(final String pProjectId, final String pReference, final String pName,
                                    final String pNewName) throws DeliveryManagementServiceException
  {
    try
    {
      OSGiServiceGetter.getDeliveryManager().updateFileArtefact(pProjectId, pReference, pName, pNewName);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String
                          .format("Unable to update file artefact with [project_id=%s, reference=%s, old_name=%s, new_name=%s]",
                                  pProjectId, pReference, pName, pNewName), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<BugTrackerIssueDTO> getIssues(final String pProjectId, final String pReference, final String pVersion)
      throws DeliveryManagementServiceException
  {
    final List<BugTrackerIssueDTO> returnedIssues = new ArrayList<BugTrackerIssueDTO>();
    try
    {
      final String remoteUser = getThreadLocalRequest().getRemoteUser();
      final List<BugTrackerIssue> issues = OSGiServiceGetter.getDeliveryPluginService().getIssues(pProjectId, pVersion,
                                                                                                  remoteUser);
      if (issues != null)
      {
        for (final BugTrackerIssue bugTrackerIssue : issues)
        {
          returnedIssues.add(Resources.buildDTO(bugTrackerIssue));
        }
      }
    }
    catch (final Exception e)
    {
      manageException("Unable to get list of version on your bugtracker", e);
    }

    return returnedIssues;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IssueContentDTO getIssueContent(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException
  {
    final IssueContentDTO issueContentDTO = new IssueContentDTO();
    // Getting bug content
    final ContentDTO content = getContent(pProjectId, pReference, ContentTypeDTO.BUG);
    issueContentDTO.setContent(content);
    try
    {
      final String remoteUser = getThreadLocalRequest().getRemoteUser();
      List<String> issuesVersion = OSGiServiceGetter.getDeliveryPluginService().getIssuesVersion(pProjectId,
                                                                                                 remoteUser);
      if (issuesVersion == null)
      {
        issuesVersion = new ArrayList<String>();
      }
      issueContentDTO.setVersions(issuesVersion);
    }
    catch (final Exception e)
    {
      manageException("Unable to get list of version on your bugtracker", e);
    }
    return issueContentDTO;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateBugsVersion(final String pProjectId, final String pReference, final String pVersion)
      throws DeliveryManagementServiceException
  {
    String oldName = null;
    try
    {
      final Content content = OSGiServiceGetter.getDeliveryManager().getContent(pProjectId, pReference,
                                                                                ContentType.BUG);
      if (content.getNode() == null)
      {
        final Folder newFolder = OSGiServiceGetter.getDeliveryManager().newFolder();
        newFolder.setName(pVersion);
        newFolder.setPath("/");
        content.setNode(newFolder);
      }
      else
      {
        oldName = content.getNode().getName();
        content.getNode().setName(pVersion);

      }
      OSGiServiceGetter.getDeliveryManager().updateContentNode(pProjectId, pReference, oldName, content);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String
                          .format("Unable to update bug version choosen with [project_id=%s, reference=%s, old_version=%s, new_version=%s]",
                                  pProjectId, pReference, oldName, pVersion), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateDocuments(final String pProjectId, final String pReference, final FolderNode pDocumentsTree)
      throws DeliveryManagementServiceException
  {
    try
    {
      final Content content = OSGiServiceGetter.getDeliveryManager().getContent(pProjectId, pReference,
                                                                                ContentType.ECM);
      final String oldName = content.getNode().getName();
      content.setNode(Resources.build(pDocumentsTree));
      OSGiServiceGetter.getDeliveryManager().updateContentNode(pProjectId, pReference, oldName, content);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String.format("Unable to update documents choosen with [project_id=%s, reference=%s]", pProjectId,
                                    pReference), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateSources(final String pProjectId, final String pReference, final FolderNode pDocumentsTree)
      throws DeliveryManagementServiceException
  {
    try
    {
      final Content content = OSGiServiceGetter.getDeliveryManager().getContent(pProjectId, pReference,
                                                                                ContentType.SCM);
      final String oldName = content.getNode().getName();
      content.setNode(Resources.build(pDocumentsTree));
      OSGiServiceGetter.getDeliveryManager().updateContentNode(pProjectId, pReference, oldName, content);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String.format("Unable to update sources choosen with [project_id=%s, reference=%s]", pProjectId,
                                    pReference), e);
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<TemplateDTO> getTemplateReportList(final String pProjectId) throws DeliveryManagementServiceException
  {

    final List<TemplateDTO> templateList = new ArrayList<TemplateDTO>();
    try
    {
      final List<TemplateReport> templates = OSGiServiceGetter.getTemplateReportManager().getTemplateReport(pProjectId);

      templateList.addAll(Resources.buildDTO(templates));
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to get templates for project with  [project_id=%s]", pProjectId), e);
    }

    return templateList;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existTemplateFile(final String pProjectId, final String pPreviousName, final String pFileName)
      throws DeliveryManagementServiceException
  {
    boolean isExist = false;
    try
    {
      isExist = OSGiServiceGetter.getTemplateReportManager().exitTemplateFile(pProjectId, pPreviousName, pFileName);
    }
    catch (final TemplateReportServiceException e)
    {
      manageException(String
                          .format("Unable to check if the filename is already used by a template with [project_id=%s, filename=%s]",
                                  pProjectId, pFileName), e);
    }
    return isExist;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteTemplateReport(final String pProjectId, final String pTemplateName)
      throws DeliveryManagementServiceException
  {
    try
    {
      OSGiServiceGetter.getTemplateReportManager().deleteTemplateReport(pProjectId, pTemplateName);
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to delete template with [project_id=%s, template_name=%s]", pProjectId,
                                    pTemplateName), e);
    }
    return true;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateTemplateReport(final String pTemplateName, final TemplateDTO pTemplate)
      throws DeliveryManagementServiceException
  {
    final TemplateReport template = Resources.build(pTemplate);
    try
    {
      OSGiServiceGetter.getTemplateReportManager().updateTemplateReport(pTemplateName, template);
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to delete template with [template_name=%s]", pTemplateName), e);
    }
    return true;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean saveTemplateReport(final String pProjectId, final String pTemplateName, final TemplateDTO pTemplate)
      throws DeliveryManagementServiceException
  {
    final TemplateReport template = Resources.build(pTemplate);
    template.setProjectId(pProjectId);
    try
    {
      if ((pTemplateName != null) && (!"".equals(pTemplateName)))
      {
        OSGiServiceGetter.getTemplateReportManager().updateTemplateReport(pTemplateName, template);
      }
      else
      {
        OSGiServiceGetter.getTemplateReportManager().createTemplateReport(template);

      }
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to save template with [template=%s]", pTemplate), e);
    }
    return true;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NoteContentDTO getNoteContent(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException
  {
    final NoteContentDTO templateContentDTO = new NoteContentDTO();
    // Getting note content
    final ContentDTO content = getContent(pProjectId, pReference, ContentTypeDTO.NOTE);
    templateContentDTO.setContent(content);
    try
    {
      List<TemplateReport> templates = OSGiServiceGetter.getTemplateReportManager().getTemplateReport(pProjectId);
      if (templates == null)
      {
        templates = new ArrayList<>();
      }
      templateContentDTO.setTemplates(Resources.buildDTO(templates));

    }
    catch (final Exception e)
    {
      manageException("Unable to get note report contentt of version on your bugtracker", e);
    }
    return templateContentDTO;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateNoteTemplate(final String pProjectId, final String pReference, final String pTemplateName,
                                    final Map<String, String> pFields) throws DeliveryManagementServiceException
  {

    String oldName = null;
    try
    {
      final Content content = OSGiServiceGetter.getDeliveryManager().getContent(pProjectId, pReference,
                                                                                ContentType.NOTE);
      oldName = content.getNode().getName();
      final Artefact newArtefact = OSGiServiceGetter.getDeliveryManager().newArtefact();
      // Build artefact from template
      newArtefact.setName(pTemplateName);
      newArtefact.setIdentifiant(pTemplateName);
      newArtefact.setPath(content.getNode().getPath());
      // Build artefact parameter from fields
      newArtefact.setParameters(Resources.build(pFields));

      if (content.getNode() instanceof Folder)
      {
        final Folder folder = (Folder) content.getNode();
        if (folder.getChildNodes() != null)
        {
          final List<Node> newList = new ArrayList<Node>(folder.getChildNodes());
          for (final Node node : newList)
          {
            folder.removeChildNode(node);
          }
        }
        folder.addChildNode(newArtefact);
      }
      OSGiServiceGetter.getDeliveryManager().updateContentNode(pProjectId, pReference, oldName, content);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String
                          .format("Unable to update the template note choosen with [project_id=%s, reference=%s, old_template=%s, new_template=%s]",
                                  pProjectId, pReference, oldName, pTemplateName), e);
    }
    return true;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<ContentTypeDTO> getAvailableContent(final String pProjectId) throws DeliveryManagementServiceException
  {

    final List<ContentTypeDTO> returnList = new ArrayList<ContentTypeDTO>();
    try
    {
      if (OSGiServiceGetter.getDeliveryPluginService().hasECMAvailable(pProjectId))
      {
        returnList.add(ContentTypeDTO.ECM);
      }
      if (OSGiServiceGetter.getDeliveryPluginService().hasSCMAvailable(pProjectId))
      {
        returnList.add(ContentTypeDTO.SCM);
      }
      if (OSGiServiceGetter.getDeliveryPluginService().hasBugtrackerAvailable(pProjectId))
      {
        returnList.add(ContentTypeDTO.BUG);
      }
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String
                          .format("Unable to find any instance according to the project id givenn with [project_id=%s]",
                                  pProjectId), e);

    }
    // The followings are always available
    returnList.add(ContentTypeDTO.NOTE);
    returnList.add(ContentTypeDTO.FILE);
    return returnList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean lockDelivery(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException
  {
    boolean success = false;
    try
    {
      success = OSGiServiceGetter.getDeliveryManager().changeDeliveryStatus(pProjectId, pReference,
                                                                            DeliveryStatus.DELIVERED);
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to lock delivery with [delivery_id=%s]", pReference), e);
    }
    return success;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean generateDeliveryStatus(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException
  {
    boolean success = false;
    try
    {
      success = OSGiServiceGetter.getDeliveryManager().changeDeliveryStatus(pProjectId, pReference,
                                                                            DeliveryStatus.GENERATING);
    }
    catch (final DeliveryServiceException e)
    {
      manageException(String.format("Unable to change delivery status with [delivery_id=%s]", pReference), e);
    }
    return success;
  }

  /*
   * Template Report methods
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canEdit(final String pProjectId) throws DeliveryManagementServiceException
  {
    String  currentUser = null;
    boolean returned    = false;
    try
    {
      // get the current user
      currentUser = getThreadLocalRequest().getRemoteUser();
      returned = OSGiServiceGetter.getDeliveryPermissionService().canEdit(currentUser, pProjectId);
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to know if user has edit permissions with [project_id=%s, login=%s]",
                                    pProjectId, currentUser), e);
    }
    return returned;
  }

  /**
   * This method manages exceptions in order to write log error and get ForgeCodeException
   *
   * @param pMessage
   * @param e
   *
   * @throws UserServiceException
   */
  private void manageException(final String pMessage, final Exception e) throws DeliveryManagementServiceException
  {
    // handle functional exceptions
    log.error(pMessage, e);
    if ((e instanceof DeliveryServiceException) && (((DeliveryServiceException) e).getCode() != null))
    {
      final DeliveryServiceException fe = (DeliveryServiceException) e;
      final ExceptionCode error = ExceptionCode.valueOf(fe.getCode().toString());
      throw new DeliveryManagementServiceException(error, e);
    }
    else if ((e instanceof TemplateReportServiceException) && (((TemplateReportServiceException) e).getCode() != null))
    {
      final TemplateReportServiceException fe = (TemplateReportServiceException) e;
      final ExceptionCode error = ExceptionCode.valueOf(fe.getCode().toString());
      throw new DeliveryManagementServiceException(error, e);
    }
    else
    {
      log.error("An error occured during calling server side", e);
      throw new DeliveryManagementServiceException(ExceptionCode.TECHNICAL_ERROR, e);
    }
  }

  private String getBugVersion(final String pProjectId, final String pReference)
      throws DeliveryManagementServiceException
  {
    String bugVersion = "BUG";
    try
    {
      final List<Content> contents = OSGiServiceGetter.getDeliveryManager().getContents(pProjectId, pReference);
      for (final Content content : contents)
      {
        if (ContentType.BUG.equals(content.getType()))
        {
          bugVersion = content.getNode().getName();
          break;
        }
      }
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to get contents with [project_id=%s, reference=%s", pProjectId, pReference),
                      e);
    }

    return bugVersion;
  }

  private void updateContentNode(final String pProjectId, final String pReference,
                                 final Map<ContentType, String> contentTypes) throws DeliveryManagementServiceException
  {
    try
    {
      final List<Content> contents = OSGiServiceGetter.getDeliveryManager().getContents(pProjectId, pReference);
      for (final Content content : contents)
      {
        final String name = contentTypes.get(content.getType());
        String oldName = null;
        if (content.getNode() == null)
        {
          final Folder newFolder = OSGiServiceGetter.getDeliveryManager().newFolder();
          newFolder.setName(name);
          newFolder.setPath("/");
          content.setNode(newFolder);
        }
        else
        {
          oldName = content.getNode().getName();
          content.getNode().setName(name);
          if ((oldName != null) && (!"".equals(oldName)) && (!oldName.equals(name)))
          {
            updateNodePath("/", content.getNode());

          }
        }
        OSGiServiceGetter.getDeliveryManager().updateContentNode(pProjectId, pReference, oldName, content);

      }
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to update contents node with [project_id=%s, reference=%s, contents=%s",
                                    pProjectId, pReference, contentTypes), e);
    }
  }

  /**
   * @param pNode
   */
  private void updateNodePath(final String pCurrentPath, final Node pNode)
  {
    final StringBuilder path = new StringBuilder(pCurrentPath);
    if (!pCurrentPath.endsWith("/"))
    {
      path.append("/");
    }
    pNode.setPath(path.toString());
    if (pNode instanceof Folder)
    {

      path.append(pNode.getName());
      final Folder folder = (Folder) pNode;
      for (final Node node : folder.getChildNodes())
      {
        updateNodePath(path.toString(), node);
      }

    }
  }

  /*
   * Exception management
   */

  private void updateContents(final String pProjectId, final String pReference, final List<ContentType> contentTypes)
      throws DeliveryManagementServiceException
  {
    try
    {

      OSGiServiceGetter.getDeliveryManager().updateDeliveryContents(pProjectId, pReference, contentTypes);
    }
    catch (final Exception e)
    {
      manageException(String.format("Unable to update delivery contents with [project_id=%s, reference=%s, contents=%s",
                                    pProjectId, pReference, contentTypes), e);
    }
  }

}
