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

/**
 * @author salvat-a
 */
public class AlfrescoDocumentImpl extends AbstractAlfrescoItem implements AlfrescoDocument
{
   private String contentStreamFileName;
   private String contentStreamMimeType;
   private long contentStreamLength;
   private String versionLabel;
   private String parentPath;

   @Override
   public String getContentStreamFileName()
   {
      return contentStreamFileName;
   }

   @Override
   public void setContentStreamFileName(String contentStreamFileName)
   {
      this.contentStreamFileName = contentStreamFileName;
   }

   @Override
   public String getContentStreamMimeType()
   {
      return contentStreamMimeType;
   }

   @Override
   public void setContentStreamMimeType(String contentStreamMimeType)
   {
      this.contentStreamMimeType = contentStreamMimeType;
   }

   @Override
   public long getContentStreamLength()
   {
      return contentStreamLength;
   }

   @Override
   public void setContentStreamLength(long contentStreamLength)
   {
      this.contentStreamLength = contentStreamLength;
   }

   @Override
   public String getVersionLabel()
   {
      return versionLabel;
   }

   @Override
   public void setVersionLabel(String versionLabel)
   {
      this.versionLabel = versionLabel;
   }

   @Override
   public String getParentPath()
   {
      return parentPath;
   }

   @Override
   public void setParentPath(String parentPath)
   {
      this.parentPath = parentPath;
   }
}
