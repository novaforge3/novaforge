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
package org.novaforge.forge.tools.deliverymanager.internal.model;

import org.novaforge.forge.tools.deliverymanager.model.FileElement;

/**
 * @author Guillaume Lamirand
 */
public class FileElementImpl implements FileElement
{
   /**
    * 
    */
   private static final long serialVersionUID = 1144985808856011458L;

   private String fileName;

   private String size;
   private String type;
   private String source;
   private String url;

   /**
    * @inheritDoc
    */
   @Override
   public String getFileName()
   {
      return fileName;
   }

   /**
    * @param pFileName
    *           the fileName to set
    */
   public void setFileName(String pFileName)
   {
      fileName = pFileName;
   }

   /**
    * @inheritDoc
    */
   @Override
   public String getSize()
   {
      return size;
   }

   /**
    * @param pSize
    *           the size to set
    */
   public void setSize(String pSize)
   {
      size = pSize;
   }

   /**
    * @inheritDoc
    */
   @Override
   public String getType()
   {
      return type;
   }

   /**
    * @param pType
    *           the type to set
    */
   public void setType(String pType)
   {
      type = pType;
   }

   /**
    * @inheritDoc
    */
   @Override
   public String getSource()
   {
      return source;
   }

   /**
    * @param pSource
    *           the source to set
    */
   public void setSource(String pSource)
   {
      source = pSource;
   }

   /**
    * @inheritDoc
    */
   @Override
   public String getUrl()
   {
      return url;
   }

   /**
    * @param pUrl
    *           the url to set
    */
   public void setUrl(String pUrl)
   {
      url = pUrl;
   }

}
