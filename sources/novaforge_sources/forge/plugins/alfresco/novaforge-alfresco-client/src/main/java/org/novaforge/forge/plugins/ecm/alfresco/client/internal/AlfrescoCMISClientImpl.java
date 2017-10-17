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
package org.novaforge.forge.plugins.ecm.alfresco.client.internal;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.Session;
import org.apache.chemistry.opencmis.client.api.SessionFactory;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.UnfileObject;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.model.AlfrescoDocumentImpl;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.model.AlfrescoFolderImpl;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.model.AlfrescoRepositoryImpl;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISClient;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISException;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISHelper;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocument;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocumentContent;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoFolder;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoRepository;
import org.novaforge.forge.plugins.ecm.alfresco.library.AlfrescoCMISHelperCustom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is an implementation of AlfrescoRestClientCustom interface.
 * 
 * @see org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestClient
 * @author cadetr
 * @author salvat-a
 */
public class AlfrescoCMISClientImpl implements AlfrescoCMISClient
{

  /**
   * Constant which defined the store id
   */
  private static final String STORE = "workspace://SpacesStore/";

  private SessionFactory      sessionFactory;

  private void buildRepository(final AlfrescoRepository repository, final Tree<FileableCmisObject> tree)
  {
    final FileableCmisObject item = tree.getItem();

    if (item.getBaseTypeId() == BaseTypeId.CMIS_FOLDER)
    {
      final AlfrescoFolder folder = new AlfrescoFolderImpl();
      folder.setPath(((Folder) item).getPath());
      folder.setName(item.getName());
      folder.setType(item.getType().getId());
      folder.setParentPath(item.getParents().get(0).getPath());
      repository.getFolders().add(folder);

      for (final Tree<FileableCmisObject> child : tree.getChildren())
      {
        buildRepository(repository, child);
      }
    }
    else if (item.getBaseTypeId() == BaseTypeId.CMIS_DOCUMENT)
    {
      final AlfrescoDocument document = new AlfrescoDocumentImpl();
      document.setPath(item.getPaths().get(0));
      document.setName(item.getName());
      document.setType(item.getType().getId());
      document.setVersionLabel(((Document) item).getVersionLabel());
      document.setParentPath(item.getParents().get(0).getPath());

      final ContentStream contentStream = ((Document) item).getContentStream();
      document.setContentStreamFileName(contentStream.getFileName());
      document.setContentStreamLength(contentStream.getLength());
      document.setContentStreamMimeType(contentStream.getMimeType());
      repository.getDocuments().add(document);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AlfrescoCMISHelper getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
  {
    return new AlfrescoCMISHelperCustom(pBaseUrl, pUsername, pPassword);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Tree<FileableCmisObject>> getDescendants(final AlfrescoCMISHelper pConnector, final String pCMISId,
                                                       final int pDepth) throws AlfrescoCMISException
  {
    final Folder root = getCmisFolder(pConnector, pCMISId);
    return root.getDescendants(pDepth);
  }

  @Override
  public Document getCmisDocument(final AlfrescoCMISHelper connector, final String id) throws AlfrescoCMISException
  {
    final CmisObject cmisObject = getCMISObject(connector.getParamters(), id);
    if (cmisObject instanceof Document)
    {
      return (Document) cmisObject;
    }
    else
    {
      throw new AlfrescoCMISException(String.format("Cannot get the object %s, it is not a document", id));
    }
  }

  @Override
  public Folder getCmisFolder(final AlfrescoCMISHelper connector, final String id) throws AlfrescoCMISException
  {
    final CmisObject cmisObject = getCMISObject(connector.getParamters(), id);
    if (cmisObject instanceof Folder)
    {
      return (Folder) cmisObject;
    }
    else
    {
      throw new AlfrescoCMISException(String.format("Cannot get the object %s, it is not a folder", id));
    }
  }

  @Override
  public AlfrescoRepository getRepository(final AlfrescoCMISHelper connector, final String rootNodeId, final int depth)
      throws AlfrescoCMISException
  {
    final List<Tree<FileableCmisObject>> repositoryTree = getDescendants(connector, rootNodeId, depth);

    final AlfrescoRepository repository = new AlfrescoRepositoryImpl();

    for (final Tree<FileableCmisObject> subTree : repositoryTree)
    {
      buildRepository(repository, subTree);
    }
    return repository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AlfrescoDocument getDocument(final AlfrescoCMISHelper pConnector, final String pCMISId)
      throws AlfrescoCMISException
  {
    final Document doc = getCmisDocument(pConnector, pCMISId);
    final AlfrescoDocument document = new AlfrescoDocumentImpl();
    document.setPath(doc.getPaths().get(0));
    document.setName(doc.getName());
    document.setType(doc.getType().getId());
    document.setVersionLabel(doc.getVersionLabel());
    document.setParentPath(doc.getParents().get(0).getPath());

    final ContentStream contentStream = doc.getContentStream();
    document.setContentStreamFileName(contentStream.getFileName());
    document.setContentStreamLength(contentStream.getLength());
    document.setContentStreamMimeType(contentStream.getMimeType());
    return document;
  }

  @Override
  public void copyDocumentContent(final AlfrescoCMISHelper connector, final String id, final String localFile)
      throws AlfrescoCMISException
  {
    final Document document = getCmisDocument(connector, id);
    copyDocument(localFile, document.getContentStream().getStream());
  }

  private void copyDocument(final String target, final InputStream inputStream) throws AlfrescoCMISException
  {
    try
    {
      final OutputStream outputStream = new FileOutputStream(new File(target));

      final byte buf[] = new byte[1024];
      int len;
      while ((len = inputStream.read(buf)) > 0)
      {
        outputStream.write(buf, 0, len);
      }
      outputStream.close();
      inputStream.close();
    }
    catch (final FileNotFoundException e)
    {
      throw new AlfrescoCMISException(String.format("The target file is not found with [target=%s]", target),
          e);
    }
    catch (final IOException e)
    {
      throw new AlfrescoCMISException(String.format("Unable to write the target file with [target=%s]",
          target), e);
    }
  }

  @Override
  public boolean createDocument(final AlfrescoCMISHelper connector, final AlfrescoDocumentContent document)
      throws AlfrescoCMISException
  {
    Validate.notNull(document);

    final Folder parentFolder = getCmisFolder(connector, document.getParentPath());
    try
    {
      final Map<String, Object> properties = new HashMap<String, Object>();
      properties.put(PropertyIds.OBJECT_TYPE_ID, document.getType());
      properties.put(PropertyIds.NAME, document.getName());

      final InputStream inputStream = new FileInputStream(new File(document.getLocalFile()));
      final ContentStream contentStream = new ContentStreamImpl(document.getContentStreamFileName(),
          BigInteger.valueOf(document.getContentStreamLength()), document.getContentStreamMimeType(),
          inputStream);

      final Document newDocument = parentFolder.createDocument(properties, contentStream,
          VersioningState.MINOR);

      return newDocument != null;
    }
    catch (final FileNotFoundException e)
    {
      throw new AlfrescoCMISException(String.format("Cannot create the document, local file %s not found",
          document.getLocalFile()));
    }
  }

  @Override
  public boolean updateDocument(final AlfrescoCMISHelper connector, final AlfrescoDocumentContent document,
      final String checkinComment) throws AlfrescoCMISException
  {
    Validate.notNull(document);

    final Document doc = getCmisDocument(connector, document.getPath());

    final Map<String, Object> properties = new HashMap<String, Object>();
    properties.put(PropertyIds.OBJECT_TYPE_ID, document.getType());
    properties.put(PropertyIds.NAME, document.getName());
    doc.updateProperties(properties);

    final ObjectId pwcId = doc.checkOut();
    final Document pwc = (Document) getCMISObject(connector.getParamters(), pwcId.getId());
    try
    {
      final InputStream inputStream = new FileInputStream(new File(document.getLocalFile()));
      final ContentStream contentStream = new ContentStreamImpl(document.getContentStreamFileName(),
          BigInteger.valueOf(document.getContentStreamLength()), document.getContentStreamMimeType(),
          inputStream);

      final ObjectId newVersionId = pwc.checkIn(true, null, contentStream, checkinComment);

      return newVersionId != null;
    }
    catch (final FileNotFoundException e)
    {
      throw new AlfrescoCMISException(String.format("Cannot update the document, local file %s not found",
          document.getLocalFile()));
    }
  }

  @Override
  public boolean deleteDocument(final AlfrescoCMISHelper connector, final String docId)
      throws AlfrescoCMISException
  {
    boolean deleted = false;

    if (documentExists(connector, docId))
    {
      final Document doc = getCmisDocument(connector, docId);
      doc.deleteAllVersions();
      if (!documentExists(connector, docId))
      {
        deleted = true;
      }
    }
    else
    {
      deleted = true;
    }

    return deleted;
  }

  @Override
  public AlfrescoFolder getFolder(final AlfrescoCMISHelper pConnector, final String pId)
      throws AlfrescoCMISException
  {
    final Folder folder = getCmisFolder(pConnector, pId);

    final AlfrescoFolder result = new AlfrescoFolderImpl();
    result.setPath(folder.getPath());
    result.setName(folder.getName());
    result.setParentPath(folder.getParents().get(0).getPath());
    result.setType(folder.getType().getId());
    return result;
  }

  @Override
  public boolean createFolder(final AlfrescoCMISHelper connector, final AlfrescoFolder folder)
      throws AlfrescoCMISException
  {
    Validate.notNull(folder);

    final Folder parentFolder = getCmisFolder(connector, folder.getParentPath());

    final Map<String, Object> folderProperties = new HashMap<String, Object>();
    folderProperties.put(PropertyIds.NAME, folder.getName());
    folderProperties.put(PropertyIds.OBJECT_TYPE_ID, folder.getType());

    final Folder newFolder = parentFolder.createFolder(folderProperties);
    return newFolder != null;
  }

  @Override
  public boolean updateFolder(final AlfrescoCMISHelper connector, final AlfrescoFolder folder)
      throws AlfrescoCMISException
  {
    final Folder cmisFolder = getCmisFolder(connector, folder.getPath());

    final Map<String, Object> folderProperties = new HashMap<String, Object>();
    folderProperties.put(PropertyIds.NAME, folder.getName());
    folderProperties.put(PropertyIds.BASE_TYPE_ID, folder.getType());

    final Folder updatedFolder = (Folder) cmisFolder.updateProperties(folderProperties);
    return StringUtils.isNotEmpty(updatedFolder.getId());
  }

  @Override
  public boolean deleteFolder(final AlfrescoCMISHelper connector, final String folderId)
      throws AlfrescoCMISException
  {
    final Folder folder = getCmisFolder(connector, folderId);
    final List<String> failures = folder.deleteTree(true, UnfileObject.DELETE, true);
    return ((failures != null) && (failures.size() > 0));
  }

  private CmisObject getCMISObject(final Map<String, String> pParam, final String pCMISObjectId)
  {
    final Session session = sessionFactory.getRepositories(pParam).get(0).createSession();

    if (pCMISObjectId.startsWith("/"))
    {
      return session.getObjectByPath(pCMISObjectId);
    }
    else
    {
      return session.getObject(buildCMISId(pCMISObjectId));
    }
  }

  private String buildCMISId(final String pUUID)
  {
    final StringBuilder cmisId = new StringBuilder();
    if (!pUUID.startsWith(STORE) && !pUUID.startsWith("/"))
    {
      cmisId.append(STORE);
    }
    cmisId.append(pUUID);
    return cmisId.toString();
  }

  private boolean documentExists(final AlfrescoCMISHelper connector, final String id)
      throws AlfrescoCMISException
  {
    boolean exists = true;
    try
    {
      getCmisDocument(connector, id);
    }
    catch (final CmisObjectNotFoundException e)
    {
      exists = false;
    }
    return exists;
  }

  /**
   * Used by container to inject {@link SessionFactory} reference
   * 
   * @param pSessionFactory
   *          the sessionFactory to set
   */
  public void setSessionFactory(final SessionFactory pSessionFactory)
  {
    sessionFactory = pSessionFactory;
  }
}
