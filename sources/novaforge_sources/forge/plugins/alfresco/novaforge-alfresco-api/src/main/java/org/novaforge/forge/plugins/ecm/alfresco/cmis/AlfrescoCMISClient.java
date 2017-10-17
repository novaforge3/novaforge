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
package org.novaforge.forge.plugins.ecm.alfresco.cmis;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestException;

import java.util.List;

/**
 * This interface describes client service used to communicate with alfresco instance.
 * 
 * @author cadetr
 * @author salvat-a
 */
public interface AlfrescoCMISClient
{

   /**
    * Use to connect to alfresco instance with specific url, username and password.
    * 
    * @param baseUrl
    *           represents the url of alfresco instance
    * @param username
    *           represents username used to log in
    * @param password
    *           represents password used to log in
    * @throws AlfrescoRestException
    *            can occured if connection failed or client can be built
    */
   AlfrescoCMISHelper getConnector(String baseUrl, String username, String password);

   /**
    * @param pConnector
    * @param pId
    * @param pDepth
    * @return
    * @throws AlfrescoCMISException
    */
   List<Tree<FileableCmisObject>> getDescendants(final AlfrescoCMISHelper pConnector, final String pId,
         int pDepth) throws AlfrescoCMISException;

   /**
    * @param pConnector
    * @param pId
    * @return
    * @throws AlfrescoCMISException
    */
   Document getCmisDocument(AlfrescoCMISHelper pConnector, String pId) throws AlfrescoCMISException;

   /**
    * @param pConnector
    * @param pId
    * @return
    * @throws AlfrescoCMISException
    */
   Folder getCmisFolder(AlfrescoCMISHelper pConnector, String pId) throws AlfrescoCMISException;

   /**
    * @param connector
    * @param rootNodeId
    * @param depth
    * @return
    * @throws AlfrescoCMISException
    */
   AlfrescoRepository getRepository(AlfrescoCMISHelper connector, String rootNodeId, int depth)
         throws AlfrescoCMISException;

   /**
    * @param pConnector
    * @param pId
    * @return
    * @throws AlfrescoCMISException
    */
   AlfrescoDocument getDocument(AlfrescoCMISHelper pConnector, String pId) throws AlfrescoCMISException;

   /**
    * @param connector
    * @param id
    * @param localFile
    * @return the alfresco document local file name
    * @throws AlfrescoCMISException
    */
   void copyDocumentContent(AlfrescoCMISHelper connector, String id, String localFile) throws AlfrescoCMISException;

   /**
    * @param connector
    * @param document
    * @return true if the document has been created, false otherwise
    * @throws AlfrescoCMISException
    */
   boolean createDocument(AlfrescoCMISHelper connector, AlfrescoDocumentContent document)
   throws AlfrescoCMISException;

   /**
    * @param connector
    * @param document
    * @param checkinComment
    * @return true if the document has been updated, false otherwise
    * @throws AlfrescoCMISException
    */
   boolean updateDocument(AlfrescoCMISHelper connector, AlfrescoDocumentContent document, String checkinComment)
         throws AlfrescoCMISException;

   /**
    * @param connector
    * @param docId
    * @return true if the document has been deleted, false otherwise
    * @throws AlfrescoCMISException
    */
   boolean deleteDocument(AlfrescoCMISHelper connector, String docId) throws AlfrescoCMISException;

   /**
    * @param pConnector
    * @param pId
    * @return
    * @throws AlfrescoCMISException
    */
   AlfrescoFolder getFolder(AlfrescoCMISHelper pConnector, String pId) throws AlfrescoCMISException;

   /**
    * @param connector
    * @param folder
    * @return true if the folder has been created, false otherwise
    * @throws AlfrescoCMISException
    */
   boolean createFolder(AlfrescoCMISHelper connector, AlfrescoFolder folder)
   throws AlfrescoCMISException;

   /**
    * @param connector
    * @param folder
    * @return true if the folder has been updated, false otherwise
    * @throws AlfrescoCMISException
    */
   boolean updateFolder(AlfrescoCMISHelper connector, AlfrescoFolder folder)
         throws AlfrescoCMISException;

   /**
    * @param connector
    * @param folderId
    * @return true if the folder has been deleted, false otherwise
    * @throws AlfrescoCMISException
    */
   boolean deleteFolder(AlfrescoCMISHelper connector, String folderId)
         throws AlfrescoCMISException;
}