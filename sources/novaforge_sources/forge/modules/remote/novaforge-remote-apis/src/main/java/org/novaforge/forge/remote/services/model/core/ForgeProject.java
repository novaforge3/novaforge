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
package org.novaforge.forge.remote.services.model.core;

import java.io.Serializable;
import java.util.Date;


/**
 * @author blachonm
 *
 */
public class ForgeProject implements Serializable
{
   private static final long serialVersionUID = 8698217970056689109L;
   private String            id;
   private String            name;
   private String            description;
   private Date              lastModified;

   public ForgeProject()
   {
      super();
   }


   /**
    * {@inheritDoc}
    */
   public String getId()
   {
      return id;
   }

   /**
    * {@inheritDoc}
    */
   public void setId(String id)
   {
      this.id = id;
   }

   /**
    * {@inheritDoc}
    */
   public String getName()
   {
      return name;
   }

   /**
    * {@inheritDoc}
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * {@inheritDoc}
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * {@inheritDoc}
    */
   public void setDescription(String description)
   {
      this.description = description;
   }

   /**
    * {@inheritDoc}
    */
   public Date getLastModified()
   {
      return lastModified;
   }

   /**
    * {@inheritDoc}
    */
   public void setLastModified(Date lastModified)
   {
      this.lastModified = lastModified;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (getClass() != obj.getClass())
      {
         return false;
      }
      ForgeProject other = (ForgeProject) obj;
      if (id == null)
      {
         if (other.id != null)
         {
            return false;
         }
      }
      else if (!id.equals(other.id))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "ForgeProject [id=" + id + ", name=" + name + ", description=" + description + ", lastModified="
                 + lastModified + "]";
   }

}
