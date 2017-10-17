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


package org.novaforge.forge.plugins.ecm.alfresco.client.internal.model;

import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoDocument;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoFolder;
import org.novaforge.forge.plugins.ecm.alfresco.cmis.AlfrescoRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author salvat-a
 */
public class AlfrescoRepositoryImpl implements AlfrescoRepository
{
   private String repositoryId;
   private List<AlfrescoDocument> documents;
   private List<AlfrescoFolder> folders;

   @Override
   public String getRepositoryId()
   {
      return repositoryId;
   }

   @Override
   public void setRepositoryId(String repositoryId)
   {
      this.repositoryId = repositoryId;
   }

   @Override
   public List<AlfrescoFolder> getFolders()
   {
      if (folders == null)
      {
         folders = new ArrayList<AlfrescoFolder>();
      }
      return folders;
   }

   @Override
   public void setFolders(List<AlfrescoFolder> folders)
   {
      this.folders = folders;
   }

   @Override
   public List<AlfrescoDocument> getDocuments()
   {
      if (documents == null)
      {
         documents = new ArrayList<AlfrescoDocument>();
      }
      return documents;
   }

   @Override
   public void setDocuments(List<AlfrescoDocument> documents)
   {
      this.documents = documents;
   }
}
