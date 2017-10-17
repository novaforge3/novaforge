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
package org.novaforge.forge.tools.deliverymanager.internal.presenter;

import org.novaforge.forge.commons.reporting.exceptions.ReportingException;
import org.novaforge.forge.commons.reporting.model.OutputFormat;
import org.novaforge.forge.commons.reporting.services.ReportingService;
import org.novaforge.forge.commons.technical.file.FileMeta;
import org.novaforge.forge.commons.technical.file.FileService;
import org.novaforge.forge.commons.technical.file.FileServiceException;
import org.novaforge.forge.commons.technical.validation.ValidationService;
import org.novaforge.forge.commons.technical.validation.ValidatorResponse;
import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.exceptions.ExceptionCode;
import org.novaforge.forge.tools.deliverymanager.exceptions.TemplateReportServiceException;
import org.novaforge.forge.tools.deliverymanager.facades.DeliveryService;
import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;
import org.novaforge.forge.tools.deliverymanager.model.Folder;
import org.novaforge.forge.tools.deliverymanager.model.Node;
import org.novaforge.forge.tools.deliverymanager.model.SourceFile;
import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPluginService;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryPresenter;
import org.novaforge.forge.tools.deliverymanager.services.DeliveryRepositoryService;
import org.novaforge.forge.tools.deliverymanager.services.TemplateReportPresenter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class DeliveryPresenterImpl implements DeliveryPresenter
{

  private ValidationService         validationService;
  private DeliveryService           deliveryService;
  private DeliveryRepositoryService deliveryRepositoryService;
  private FileService               fileService;
  private DeliveryPluginService     deliveryPluginService;
  private TemplateReportPresenter   templateReportPresenter;
  private ReportingService          reportingService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Delivery newDelivery()
  {
    return deliveryService.newDelivery();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Artefact newArtefact()
  {
    return deliveryService.newArtefact();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ArtefactParameter newArtefactParameter()
  {
    return deliveryService.newArtefactParameter();
  }
  
  /**
   * {@inheritDoc}
   */
  @Override
  public Folder newFolder()
  {
    return deliveryService.newFolder();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Delivery> getDeliveries(final String pProjectId) throws DeliveryServiceException
  {
    return deliveryService.getDeliveries(pProjectId);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean generateDelivery(final String pProjectId, final String pReference, final String pUser)
      throws DeliveryServiceException
  {
    final Delivery delivery = getDelivery(pProjectId, pReference);
    deliveryService.changeStatus(pProjectId, pReference, DeliveryStatus.GENERATING);
    try
    {
      final List<Content> deliveryContents = getContents(pProjectId, pReference);
      for (final Content deliveryContent : deliveryContents)
      {
        if (deliveryContent.getType().equals(ContentType.ECM))
        {
          final Folder deliveryECMRootFolder = ((Folder) getContent(pProjectId, pReference, ContentType.ECM).getNode());
          final List<Node> deliveryECMDocuments = deliveryECMRootFolder.getChildNodes();
          deliveryRepositoryService.emptyDeliveryTempContentDirectory(pProjectId, pReference,
                                                                      deliveryECMRootFolder.getName());
          getECMDocumentsToDelivery(deliveryECMDocuments, pProjectId, pReference, pUser);
        }
        else if (deliveryContent.getType().equals(ContentType.SCM))
        {
          final Folder deliverySCMRootFolder = ((Folder) getContent(pProjectId, pReference, ContentType.SCM).getNode());
          final List<Node> deliverySCMDocuments = deliverySCMRootFolder.getChildNodes();
          deliveryRepositoryService.emptyDeliveryTempContentDirectory(pProjectId, pReference,
                                                                      deliverySCMRootFolder.getName());
          getSCMDocumentsToDelivery(deliverySCMDocuments, pProjectId, pReference, pUser);
        }
        else if (deliveryContent.getType().equals(ContentType.NOTE))
        {
          final Content deliveryNote = getContent(pProjectId, pReference, ContentType.NOTE);
          final Folder deliveryNoteNode = ((Folder) deliveryNote.getNode());
          TemplateReport deliveryTemplate;
          final Map<String, Object> mapParameters = new HashMap<String, Object>();
          if (deliveryNoteNode.getChildNodes().isEmpty())
          {
            deliveryTemplate = templateReportPresenter.getTemplateReport(pProjectId, templateReportPresenter
                                                                                         .getTemplateDefaultName());
          }
          else
          {
            Node node = deliveryNoteNode.getChildNodes().get(0);
            if (node instanceof Artefact)
            {
              Artefact artefact = (Artefact) node;
              List<ArtefactParameter> parameters = artefact.getParameters();
              if (parameters != null)
              {
                for (ArtefactParameter artefactParameter : parameters)
                {
                  mapParameters.put(artefactParameter.getKey(), artefactParameter.getValue());
                }
              }
            }
            deliveryTemplate = templateReportPresenter.getTemplateReport(pProjectId, node.getName());
          }

          deliveryRepositoryService.emptyDeliveryTempContentDirectory(pProjectId, pReference,
                                                                      deliveryNoteNode.getName());
          generateDeliveryNote(pProjectId, pReference, pUser, deliveryTemplate.getFileName(), delivery.getReference(),
                               mapParameters);
        }
      }
      // Create the archive
      deliveryRepositoryService.createArchive(pProjectId, pReference);
      deliveryService.changeStatus(pProjectId, pReference, DeliveryStatus.GENERATED);
      return true;
    }
    catch (final Exception e)
    {
      deliveryService.changeStatus(pProjectId, pReference, DeliveryStatus.CREATED);
      throw new DeliveryServiceException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Delivery createDelivery(final Delivery pDelivery, final DeliveryStatus pStatus, final String pType)
      throws DeliveryServiceException
  {
    // Checking parameters
    checkDelivery(pDelivery, null, pType);

    // Building status
    DeliveryStatus status = DeliveryStatus.CREATED;
    if (pStatus != null)
    {
      status = pStatus;
    }

    // Persist delivery
    final Delivery delivery = deliveryService.createDelivery(pDelivery, status, pType);

    // create delivery repositories
    final String projectId = pDelivery.getProjectId();
    deliveryRepositoryService.createProjectDirectory(projectId);
    deliveryRepositoryService.createDeliveryDirectories(projectId, pDelivery.getReference());

    // return object created
    return delivery;

  }

  /**
   * Check if a delivery is correct to be created
   *
   * @throws DeliveryServiceException
   */
  private void checkDelivery(final Delivery pDelivery, final String pReference, final String pType)
      throws DeliveryServiceException
  {
    // validate the bean
    final ValidatorResponse response = validationService.validate(Delivery.class, pDelivery);
    if (!response.isValid())
    {
      throw new DeliveryServiceException(response.getMessage());
    }

    // check the existing reference
    if ((pReference == null) || (!pReference.equals(pDelivery.getReference())))
    {
      final boolean existReference = deliveryService.existReference(pDelivery.getProjectId(),
          pDelivery.getReference());
      if (existReference)
      {
        throw new DeliveryServiceException(String.format(
            "The reference id given is already used with [id=%s]", pReference),
            ExceptionCode.REFERENCE_ALREADY_EXISTS);
      }
    }

    if ((pReference == null) && (pType == null))
    {
      throw new DeliveryServiceException("The delivery type shouldn't be null for a new delivery");
    }

  }

  /**
   * {@inheritDoc} mci
   */
  @Override
  public List<String> getTypes(final String pProjectId) throws DeliveryServiceException
  {
    return deliveryService.getTypes(pProjectId);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean deleteDelivery(final String pProjectId, final String pReference) throws DeliveryServiceException
  {
    // Delete persiste information
    deliveryService.deleteDelivery(pProjectId, pReference);

    // Delete tmp directory
    final String path = deliveryRepositoryService.getDeliveryTemporaryPath(pProjectId, pReference);
    try
    {
      fileService.deleteFile(new File(path));
    }
    catch (final FileServiceException e)
    {
      throw new DeliveryServiceException(String
                                             .format("Unable to delete the temporary directory of the delivery with [project_id=%S, reference=%s, path=%s]",
                                                     pProjectId, pReference, path), e);
    }
    return true;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Delivery getDelivery(final String pProjectId, final String pReference)
      throws DeliveryServiceException
  {
    return deliveryService.getDelivery(pProjectId, pReference);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Delivery updateDelivery(final String pReference, final Delivery pDelivery, final String pType)
      throws DeliveryServiceException
  {
    // Checking parameters
    checkDelivery(pDelivery, pReference, pType);

    return deliveryService.updateDelivery(pDelivery, pType);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Content getContent(final String pProjectId, final String pReference, final ContentType pType)
      throws DeliveryServiceException
  {
    return deliveryService.getContent(pProjectId, pReference, pType);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateDeliveryContents(final String pProjectId, final String pReference,
      final List<ContentType> pType) throws DeliveryServiceException
  {
    final List<Content> contents = getContents(pProjectId, pReference);

    // Create content which is not existing
    createUnexistingContent(pProjectId, pReference, pType, contents);

    // Delete content which is not asking and existing
    deleteUnexistingContent(pProjectId, pReference, pType, contents);
    return true;
  }

  private void createUnexistingContent(final String pProjectId, final String pReference,
      final List<ContentType> pType, final List<Content> contents) throws DeliveryServiceException
  {
    for (final ContentType type : pType)
    {
      if (!existingType(type, contents))
      {
        deliveryService.createContent(pProjectId, pReference, type);
      }
    }
  }

  private void deleteUnexistingContent(final String pProjectId, final String pReference,
      final List<ContentType> pType, final List<Content> contents) throws DeliveryServiceException
  {
    for (final Content content : contents)
    {
      if (!unExistingType(content.getType(), pType))
      {
        deliveryService.deleteContent(pProjectId, pReference, content.getType());
        deliveryRepositoryService.deleteDeliveryTempContentDirectory(pProjectId, pReference, content
            .getNode().getName());
      }
    }
  }

  private boolean existingType(final ContentType pType, final List<Content> pContents)
  {
    boolean returnValue = false;
    for (final Content content : pContents)
    {
      if (content.getType().equals(pType))
      {
        returnValue = true;
      }
    }
    return returnValue;
  }

  private boolean unExistingType(final ContentType pType, final List<ContentType> pTypes)
  {
    boolean returnValue = false;
    for (final ContentType type : pTypes)
    {
      if (type.equals(pType))
      {
        returnValue = true;
      }
    }
    return returnValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Content updateContentNode(final String pProjectId, final String pReference,
      final String pPreviousNode, final Content pContent) throws DeliveryServiceException
  {
    final String tmp = deliveryRepositoryService.getDeliveryTemporaryPath(pProjectId, pReference);
    if ((pPreviousNode != null) && (pContent.getNode() != null) && (!pPreviousNode.equals(pContent.getNode()
                                                                                                  .getName())))
    {
      final File previous = new File(tmp, pPreviousNode);
      previous.renameTo(new File(tmp, pContent.getNode().getName()));
    }
    else if (pContent.getNode() != null)
    {
      final File newOne = new File(tmp, pContent.getNode().getName());
      newOne.mkdirs();

    }
    return deliveryService.updateContentNode(pProjectId, pReference, pContent);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Content> getContents(final String pProjectId, final String pReference)
      throws DeliveryServiceException
  {
    return deliveryService.getContents(pProjectId, pReference);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getContentPath(final String pProjectId, final String pReference, final ContentType pContentType)
      throws DeliveryServiceException
  {
    final Content content = getContent(pProjectId, pReference, pContentType);
    return this.getContentPath(pProjectId, pReference, content);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean getExternalFile(final String pProjectId, final String pReference, final String pUrl,
      final String pFileName) throws DeliveryServiceException
  {

    // Download and copy file
    FileMeta downloadFile;
    try
    {
      downloadFile = fileService.downloadFile(pUrl);
      final String contentPath = this.getContentPath(pProjectId, pReference, ContentType.FILE);
      fileService.storeFile(downloadFile.getFile(), new File(contentPath, pFileName));
    }
    catch (final FileServiceException e)
    {
      throw new DeliveryServiceException(
          String.format("Unable to download external file with [url=%s]", pUrl), e);
    }

    // Add artefact to content
    final Content content = getContent(pProjectId, pReference, ContentType.FILE);
    final Node node = content.getNode();
    final Artefact newArtefact = buildArtefact(node, pFileName, downloadFile);
    final Folder folder = (Folder) node;
    folder.addChildNode(newArtefact);
    updateContentNode(pProjectId, pReference, null, content);
    return true;
  }

  private Artefact buildArtefact(final Node node, final String pFileName, final FileMeta pFileMeta)
  {
    // Build artefact object
    final Artefact newArtefact = newArtefact();
    newArtefact.setIdentifiant(pFileName);
    newArtefact.setName(pFileName);

    // Build parameters
    final List<ArtefactParameter> fields = new ArrayList<ArtefactParameter>();

    final ArtefactParameter type = newArtefactParameter();
    type.setKey("type");
    type.setValue(pFileMeta.getType());
    fields.add(type);

    final ArtefactParameter size = newArtefactParameter();
    size.setKey("size");
    size.setValue(pFileMeta.getSize());
    fields.add(size);

    final ArtefactParameter source = newArtefactParameter();
    source.setKey("source");
    source.setValue(SourceFile.REMOTE.getId());
    fields.add(source);

    final ArtefactParameter url = newArtefactParameter();
    url.setKey("url");
    url.setValue(pFileMeta.getUrl());
    fields.add(url);

    newArtefact.setParameters(fields);

    // Building artefact path
    final StringBuilder path = new StringBuilder(node.getPath());
    if (!path.toString().endsWith("/"))
    {
      path.append("/");
    }
    path.append(node.getName());
    newArtefact.setPath(path.toString());

    // Return result
    return newArtefact;
  }

  private String getContentPath(final String pProjectId, final String pReference, final Content pContent)
      throws DeliveryServiceException
  {
    final Node node = pContent.getNode();
    final StringBuilder tmp = new StringBuilder();

    tmp.append(deliveryRepositoryService.getDeliveryTemporaryPath(pProjectId, pReference));
    if (!tmp.toString().endsWith("/"))
    {
      tmp.append("/");
    }
    tmp.append(node.getName());
    return tmp.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean existFile(final String pProjectId, final String pReference, final String pFileName)
      throws DeliveryServiceException
  {
    boolean returnValue = false;
    if (pFileName != null)
    {
      final Content content = getContent(pProjectId, pReference, ContentType.FILE);
      final Folder node = (Folder) content.getNode();
      if (node != null)
      {
        for (final Node child : node.getChildNodes())
        {
          if (child instanceof Artefact)
          {
            final Artefact artefact = (Artefact) child;
            if (artefact.getIdentifiant().equals(pFileName) && artefact.getName().equals(pFileName))
            {
              returnValue = true;
              break;
            }
          }
        }
        if (returnValue)
        {
          final String path = this.getContentPath(pProjectId, pReference, content);
          final File file = new File(path, pFileName);
          returnValue = fileService.existFile(file);
        }
      }
    }
    return returnValue;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean updateFileArtefact(final String pProjectId, final String pReference, final String pName,
      final String pNewName) throws DeliveryServiceException
  {
    if (!pName.equals(pNewName))
    {
      final Content content = getContent(pProjectId, pReference, ContentType.FILE);
      if (!existFile(pProjectId, pReference, pNewName))
      {
        if ((content != null) && (content.getNode() != null))
        {
          final Folder folder = (Folder) content.getNode();
          boolean foundOne = false;
          for (final Node child : folder.getChildNodes())
          {
            if (child instanceof Artefact)
            {
              final Artefact artefact = (Artefact) child;
              if (artefact.getIdentifiant().equals(pName) && artefact.getName().equals(pName))
              {
                artefact.setIdentifiant(pNewName);
                artefact.setName(pNewName);
                updateContentNode(pProjectId, pReference, null, content);
                final String path = this.getContentPath(pProjectId, pReference, content);
                try
                {
                  fileService.renameFile(new File(path, pName), new File(path, pNewName));
                }
                catch (final FileServiceException e)
                {
                  throw new DeliveryServiceException(String.format(
                      "unable to rename source artefact with [path=%s, source=%s, filename=%s]", path, pName,
                      pNewName));
                }
                foundOne = true;
              }
            }

          }
          if (!foundOne)
          {
            throw new DeliveryServiceException(String.format(
                "Unable to find the artefact link to the filename with [filename=%s]", pNewName));
          }
        }
      }
      else
      {
        throw new DeliveryServiceException(String.format(
            "The target file name is already existing, unable to used it with [filename=%s]", pNewName));

      }
    }
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean changeDeliveryStatus(final String pProjectId, final String pReference, final DeliveryStatus pStatus)
      throws DeliveryServiceException
  {
    final Delivery delivery = deliveryService.changeStatus(pProjectId, pReference, pStatus);
    return delivery.getStatus().equals(pStatus);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void createDefaultReportTemplate(final String pProjectId) throws DeliveryServiceException
  {
    try
    {
      deliveryRepositoryService.createDefaultTemplateFile(pProjectId);
      templateReportPresenter.createDefaultTemplate(pProjectId);
    }
    catch (final TemplateReportServiceException e)
    {
      throw new DeliveryServiceException(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteDefaultReportTemplate(final String pProjectId) throws DeliveryServiceException
  {
    try
    {
      templateReportPresenter.deleteTemplateReport(pProjectId, null);
    }
    catch (final TemplateReportServiceException e)
    {
      throw new DeliveryServiceException(e);
    }
  }

  private void generateDeliveryNote(final String pProjectId, final String pReference, final String pUser,
                                    final String pNoteTemplateFileName, final String pDeliveryReference,
                                    final Map<String, Object> pParameters)
      throws ReportingException, DeliveryServiceException
  {
    final String deliveryTemplateRTPFile =
        deliveryRepositoryService.getTemplateDirectory(pProjectId) + File.separatorChar + pNoteTemplateFileName;
    final String deliveryOutputFile = this.getContentPath(pProjectId, pReference, ContentType.NOTE) + File.separatorChar
                                          + pDeliveryReference + ".pdf";
    final Map<String, Object> map = new HashMap<String, Object>();
    map.put("projectId", pProjectId);
    map.put("reference", pReference);
    map.put("user", pUser);
    map.putAll(pParameters);
    reportingService.renderReport(deliveryTemplateRTPFile, OutputFormat.PDF, deliveryOutputFile, map);
  }

  private void getECMDocumentsToDelivery(final List<Node> pTree, final String pProjectId, final String pReference,
                                         final String pUser) throws DeliveryServiceException
  {
    if (pTree != null)
    {
      final List<Artefact> documentToGet = new ArrayList<Artefact>();
      final Iterator<Node> it = pTree.iterator();
      String currentPath = null;
      while (it.hasNext())
      {
        final Node treeNode = it.next();
        currentPath = treeNode.getPath();
        if (treeNode instanceof Artefact)
        {
          documentToGet.add((Artefact) treeNode);
        }
        else if (treeNode instanceof Folder)
        {
          final String folderPath = treeNode.getPath() + File.separatorChar + treeNode.getName();
          deliveryRepositoryService.createDirectoryInDeliveryContent(pProjectId, pReference, folderPath);
          getECMDocumentsToDelivery(((Folder) treeNode).getChildNodes(), pProjectId, pReference, pUser);
        }
      }
      if (!documentToGet.isEmpty())
      {
        final String destAbsolutPath = deliveryRepositoryService.getDeliveryTemporaryPath(pProjectId, pReference)
                                           + File.separatorChar + currentPath;
        deliveryPluginService.getECMDocumentToDelivery(pProjectId, pReference, destAbsolutPath, documentToGet, pUser);
      }
    }
  }

  private void getSCMDocumentsToDelivery(final List<Node> pTree, final String pProjectId, final String pReference,
                                         final String pUser) throws DeliveryServiceException
  {
    if (pTree != null)
    {
      final List<Artefact> documentToGet = new ArrayList<Artefact>();
      final Iterator<Node> it = pTree.iterator();
      String currentPath = null;
      while (it.hasNext())
      {
        final Node treeNode = it.next();
        currentPath = treeNode.getPath();
        if (treeNode instanceof Artefact)
        {
          documentToGet.add((Artefact) treeNode);
        }
        else if (treeNode instanceof Folder)
        {
          final String folderPath = treeNode.getPath() + File.separatorChar + treeNode.getName();
          deliveryRepositoryService.createDirectoryInDeliveryContent(pProjectId, pReference, folderPath);
          getSCMDocumentsToDelivery(((Folder) treeNode).getChildNodes(), pProjectId, pReference, pUser);
        }
      }
      if (!documentToGet.isEmpty())
      {
        final String destAbsolutPath = deliveryRepositoryService.getDeliveryTemporaryPath(pProjectId, pReference)
                                           + File.separatorChar + currentPath;
        deliveryPluginService.getSCMDocumentToDelivery(pProjectId, pReference, destAbsolutPath, documentToGet, pUser);
      }
    }
  }

  /**
   * @param pValidationService
   *          the validationService to set
   */
  public void setValidationService(final ValidationService pValidationService)
  {
    validationService = pValidationService;
  }

  /**
   * @param pDeliveryService
   *          the deliveryService to set
   */
  public void setDeliveryService(final DeliveryService pDeliveryService)
  {
    deliveryService = pDeliveryService;
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
   * @param pFileService
   *          the fileService to set
   */
  public void setFileService(final FileService pFileService)
  {
    fileService = pFileService;
  }

  /**
   * @param pDeliveryPluginService
   *          the deliveryPluginService to set
   */
  public void setDeliveryPluginService(final DeliveryPluginService pDeliveryPluginService)
  {
    deliveryPluginService = pDeliveryPluginService;
  }

  /**
   * @param pTemplateReportPresenter
   *          the templateReportPresenter to set
   */
  public void setTemplateReportPresenter(final TemplateReportPresenter pTemplateReportPresenter)
  {
    templateReportPresenter = pTemplateReportPresenter;
  }

  /**
   * @param pReportingService
   *          the reportingService to set
   */
  public void setReportingService(final ReportingService pReportingService)
  {
    reportingService = pReportingService;
  }

}
