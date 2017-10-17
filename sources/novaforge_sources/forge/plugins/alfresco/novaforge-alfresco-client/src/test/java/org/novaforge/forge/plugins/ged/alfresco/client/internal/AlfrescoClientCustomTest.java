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
package org.novaforge.forge.plugins.ged.alfresco.client.internal;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.FileableCmisObject;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.Tree;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.junit.Test;
import org.novaforge.forge.core.plugins.categories.ecm.ECMServiceException;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.AlfrescoCMISClientImpl;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.AlfrescoRestClientImpl;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.model.AlfrescoDocumentContentImpl;
import org.novaforge.forge.plugins.ecm.alfresco.client.internal.model.AlfrescoFolderImpl;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISClient;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISException;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoCMISHelper;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocument;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocumentContent;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoFolder;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoRepository;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestClient;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestHelper;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * @author salvat-a
 */
public class AlfrescoClientCustomTest
{

   /**
    * Project ID
    */
   //private static final String PROJECT_ID = "forge_alfresco";
   private static final String PROJECT_ID = "reference_AlfrescoReferentiel";

   /**
    * URL of alfresco instance
    */
   //private static String       endpoint   = "http://integsafran/alfresco/service";
   private static String       endpoint   = "http://vm-infra-10/alfresco/service";

   /**
    * Administrator's username
    */
   //private static String       admin_user = "admin_forge";
   private static String       admin_user = "admin";

   /**
    * Administrator's password
    */
   //private static String       admin_pwd  = "forge";
   private static String       admin_pwd  = "admin";

   private boolean             alfrescoProfileActivated;

   public AlfrescoClientCustomTest()
   {
      String property = System.getProperty("alfresco.profile");
      if ("true".equals(property))
      {
         alfrescoProfileActivated = true;
      }
   }

   @Test
   public void connectTest() throws AlfrescoRestException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoRestClient client = new AlfrescoRestClientImpl();
         client.getConnector(endpoint, admin_user, admin_pwd);
      }
   }

   @Test
   public void getSitesId() throws AlfrescoRestException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoRestClient client = new AlfrescoRestClientImpl();
         AlfrescoRestHelper connector = client.getConnector(endpoint, admin_user, admin_pwd);
         String siteUUID = client.getSiteId(connector, PROJECT_ID);
         assertNotNull(siteUUID);
      }
   }

   @Test
   public void getCMISDescendants() throws ECMServiceException, AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         List<Tree<FileableCmisObject>> descendants = client.getDescendants(connector,
               "349434fe-5b62-42e3-b9c3-6ae3d3f14a9a", -1);
         for (Tree<FileableCmisObject> obj : descendants)
         {
            List<Property<?>> props = obj.getItem().getProperties();

            for (Property<?> prop : props)
            {
               System.out.println(String.format("property name: %s", prop.getDefinition().getLocalName()));
               System.out.println(String.format("property value: %s", prop.getValueAsString()));
            }

            System.out.println("node type: " + obj.getItem().getBaseTypeId().name());
            List<Folder> parents = obj.getItem().getParents();
            for (Folder folder2 : parents)
            {
               System.out.println("node parent: " + folder2.getPath());
            }
            if (BaseTypeId.CMIS_FOLDER.equals(obj.getItem().getBaseTypeId()))
            {
               displayFolder(obj);
            }
            else if (BaseTypeId.CMIS_DOCUMENT.equals(obj.getItem().getBaseTypeId()))
            {
               ContentStream contentStream = ((Document) obj.getItem()).getContentStream();
               System.out.println(contentStream.getFileName());
               copyDocument(buildFinalName("C:\\bull\\novaforge\\tmp", contentStream.getFileName()),
                     contentStream.getStream());

            }
         }
      }
   }

   @Test
   public void getRepository() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         AlfrescoRepository repository = client.getRepository(connector, "b638ed6b-63e1-4428-8564-15da9468b7d9", -1);

         assertNotNull(repository);
      }
   }

   @Test
   public void getDocument() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         AlfrescoDocument document = client.getDocument(connector, "/Sites/reference_AlfrescoReferentiel/documentLibrary/Analyse des exigences");

         assertNotNull(document);
      }
   }

   @Test
   public void copyDocumentContent() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         client.copyDocumentContent(connector, "/Sites/reference_AlfrescoReferentiel/documentLibrary/Analyse des exigences/Collines.jpg", "C:\\bull\\tmp\\");
      }
   }

   @Test
   public void createDocument() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         AlfrescoDocumentContent document = new AlfrescoDocumentContentImpl();
         document.setParentPath("/Sites/reference_AlfrescoReferentiel/");
         document.setType("cmis:document");
         document.setName("Hiver.jpg");
         document.setContentStreamFileName("Hiver.jpg");
         document.setContentStreamLength(106496L);
         document.setContentStreamMimeType("image/jpg;charset=UTF-8");
         document.setLocalFile("C:\\Documents and Settings\\All Users\\Documents\\Mes images\\Échantillons d'images\\Hiver.jpg");
         assertTrue(client.createDocument(connector, document));
      }
   }

   @Test
   public void updateDocument() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         AlfrescoDocumentContent document = new AlfrescoDocumentContentImpl();
         document.setParentPath("/Sites/reference_AlfrescoReferentiel/");
         document.setPath("/Sites/reference_AlfrescoReferentiel/Hiver.jpg");
         document.setType("cmis:document");
         document.setName("Nenuph.jpg");
         document.setContentStreamFileName("Nenuph.jpg");
         document.setContentStreamLength(106496L);
         document.setContentStreamMimeType("image/jpg;charset=UTF-8");
         document.setLocalFile("C:\\Documents and Settings\\All Users\\Documents\\Mes images\\Échantillons d'images\\Nénuphars.jpg");
         boolean updated = client.updateDocument(connector, document, "Checkin comment");

         assertTrue(updated);
      }
   }

   @Test
   public void deleteDocument() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         boolean deleted = client.deleteDocument(connector, "/Sites/reference_AlfrescoReferentiel/Nenuph.jpg");
         assertTrue(deleted);
      }
   }

   @Test
   public void getFolder() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         AlfrescoFolder folder = client.getFolder(connector, "/Sites/reference_AlfrescoReferentiel/documentLibrary/Analyse des exigences/");
         assertNotNull(folder);
      }
   }

   @Test
   public void createFolder() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         AlfrescoFolder folder = new AlfrescoFolderImpl();
         folder.setName("new pretty hot folder");
         folder.setParentPath("/Sites/reference_AlfrescoReferentiel/documentLibrary/");
         folder.setType("cmis:folder");

         assertTrue(client.createFolder(connector, folder));
      }
   }

   @Test
   public void updateFolder() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         AlfrescoFolder folder = new AlfrescoFolderImpl();
         folder.setPath("/Sites/reference_AlfrescoReferentiel/documentLibrary/new pretty hot folder");
         folder.setName("updated folder");
         folder.setParentPath("/Sites/reference_AlfrescoReferentiel/documentLibrary/");
         folder.setType("cmis:folder");

         boolean updated = client.updateFolder(connector, folder);
         assertTrue(updated);
      }
   }

   @Test
   public void deleteFolder() throws AlfrescoCMISException
   {
      if (alfrescoProfileActivated)
      {
         AlfrescoCMISClient client = new AlfrescoCMISClientImpl();
         AlfrescoCMISHelper connector = client.getConnector(endpoint + "/cmis", admin_user, admin_pwd);

         boolean deleted = client.deleteFolder(connector, "/Sites/reference_AlfrescoReferentiel/documentLibrary/updated folder");
         assertTrue(deleted);
      }
   }

   private void displayFolder(final Tree<FileableCmisObject> pTarget) throws ECMServiceException
   {
      for (Tree<FileableCmisObject> obj : pTarget.getChildren())
      {
         System.out.println("node type: " + obj.getItem().getBaseTypeId().name());
         if (BaseTypeId.CMIS_FOLDER.equals(obj.getItem().getBaseTypeId()))
         {
            System.out.println("folder id: " + (obj.getItem()).getId());
            System.out.println("folder name: " + (obj.getItem()).getName());
            System.out.println("folder path: " + ((Folder) obj.getItem()).getPath());
            System.out.println("folder last modification date: " + obj.getItem().getLastModificationDate().getTime().toString());
            System.out.println("\n\n");
            displayFolder(obj);
         }
         else if (BaseTypeId.CMIS_DOCUMENT.equals(obj.getItem().getBaseTypeId()))
         {
            List<Property<?>> props = obj.getItem().getProperties();

            for (Property<?> prop : props)
            {
               System.out.println(String.format("property name: %s", prop.getDefinition().getLocalName()));
               System.out.println(String.format("property value: %s", prop.getValueAsString()));
            }

            System.out.println("document id: " + (obj.getItem()).getId());
            System.out.println("document name: " + (obj.getItem()).getName());
            System.out.println("folder last modification date: " + obj.getItem().getLastModificationDate().getTime().toString());
            ContentStream contentStream = ((Document) obj.getItem()).getContentStream();
            System.out.println("content file name: " + contentStream.getFileName());
            copyDocument(buildFinalName("C:\\bull\\novaforge\\tmp", contentStream.getFileName()),
                  contentStream.getStream());
            System.out.println("\n\n");
         }
      }
   }

   private void copyDocument(final String pTarget, final InputStream pInputStream) throws ECMServiceException
   {
      try
      {
         OutputStream outpuStream = new FileOutputStream(new File(pTarget));

         byte buf[] = new byte[1024];
         int len;
         while ((len = pInputStream.read(buf)) > 0)
         {
            outpuStream.write(buf, 0, len);
         }
         outpuStream.close();
         pInputStream.close();
      }
      catch (FileNotFoundException e)
      {
         throw new ECMServiceException(
               String.format("The target file is not found with [target=%s]", pTarget), e);
      }
      catch (IOException e)
      {
         throw new ECMServiceException(String.format("Unable to write the target file with [target=%s]",
               pTarget), e);
      }
   }

   /**
    * @param pPath
    * @param pDocName
    * @return
    */
   private String buildFinalName(String pPath, String pDocName)
   {
      StringBuilder build = new StringBuilder(pPath);
      String fileSeparator = "\\";
      if (!pPath.endsWith(fileSeparator))
      {
         build.append(fileSeparator);
      }
      build.append(pDocName);

      return build.toString();
   }
}
