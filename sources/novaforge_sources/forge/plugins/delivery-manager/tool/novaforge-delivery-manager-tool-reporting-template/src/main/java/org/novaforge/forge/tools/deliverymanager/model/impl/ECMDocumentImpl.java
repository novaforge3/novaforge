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
package org.novaforge.forge.tools.deliverymanager.model.impl;

import org.novaforge.forge.tools.deliverymanager.model.ECMDocument;

import java.util.Date;

/**
 * @author Guillaume Lamirand
 */
public class ECMDocumentImpl implements ECMDocument
{
   /**
    * 
    */
   private static final long serialVersionUID = 7155112640903148217L;
   private String            id;
   private String            name;
   private String            path;
   private String            author;
   private Date              createdDate;
   private String            lastModifiedAuthor;
   private Date              lastModifiedDate;

   /**
    * @inheritDoc
    */
   @Override
   public String getId()
   {
      return id;
   }

   /**
    * @inheritDoc
    */
   @Override
   public String getName()
   {
      return name;
   }

   /**
    * @inheritDoc
    */
   @Override
   public String getPath()
   {
      return path;
   }

   /**
    * @inheritDoc
    */
   @Override
   public String getAuthor()
   {
      return author;

   }

   /**
    * @inheritDoc
    */
   @Override
   public Date getCreatedDate()
   {
      return createdDate;
   }

   /**
    * @inheritDoc
    */
   @Override
   public String getLastModifiedAuthor()
   {
      return lastModifiedAuthor;
   }

   /**
    * @inheritDoc
    */
   @Override
   public Date getLastModifiedDate()
   {
      return lastModifiedDate;
   }

   /**
    * @param pLastModifiedDate
    *           the lastModifiedDate to set
    */
   public void setLastModifiedDate(final Date pLastModifiedDate)
   {
      lastModifiedDate = pLastModifiedDate;
   }

   /**
    * @param pLastModifiedAuthor
    *           the lastModifiedAuthor to set
    */
   public void setLastModifiedAuthor(final String pLastModifiedAuthor)
   {
      lastModifiedAuthor = pLastModifiedAuthor;
   }

   /**
    * @param pCreatedDate
    *           the createdDate to set
    */
   public void setCreatedDate(final Date pCreatedDate)
   {
      createdDate = pCreatedDate;
   }

   /**
    * @param pAuthor
    *           the author to set
    */
   public void setAuthor(final String pAuthor)
   {
      author = pAuthor;
   }

   /**
    * @param pPath
    *           the path to set
    */
   public void setPath(final String pPath)
   {
      path = pPath;
   }

   /**
    * @param pName
    *           the name to set
    */
   public void setName(final String pName)
   {
      name = pName;
   }

   /**
    * @param pId
    *           the id to set
    */
   public void setId(final String pId)
   {
      id = pId;
   }

}
