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
package org.novaforge.forge.tools.deliverymanager.ui.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TemplateDTO implements Serializable
{

   /**
    * 
    */
   private static final long      serialVersionUID = -6324840686135242505L;
   private String                 name;
   private String                 description;
   private String                 fileName;
   private List<TemplateFieldDTO> fields;

   public TemplateDTO()
   {
      super();
   }

   public String getName()
   {
      return this.name;
   }

   public void setName(final String pName)
   {
      this.name = pName;
   }

   public String getDescription()
   {
      return this.description;
   }

   public void setDescription(final String pDescription)
   {
      this.description = pDescription;
   }

   public String getFileName()
   {
      return this.fileName;
   }

   public void setFileName(final String pFileName)
   {
      this.fileName = pFileName;
   }

   public List<TemplateFieldDTO> getFields()
   {
      if (this.fields == null)
      {
         this.fields = new ArrayList<TemplateFieldDTO>();
      }
      return this.fields;
   }

   public void setFields(final List<TemplateFieldDTO> pFields)
   {
      this.fields = pFields;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = (prime * result) + ((this.description == null) ? 0 : this.description.hashCode());
      result = (prime * result) + ((this.fields == null) ? 0 : this.fields.hashCode());
      result = (prime * result) + ((this.fileName == null) ? 0 : this.fileName.hashCode());
      result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
      return result;
   }

   @Override
   public boolean equals(final Object obj)
   {
      if (this == obj)
      {
         return true;
      }
      if (obj == null)
      {
         return false;
      }
      if (this.getClass() != obj.getClass())
      {
         return false;
      }
      final TemplateDTO other = (TemplateDTO) obj;
      if (this.description == null)
      {
         if (other.description != null)
         {
            return false;
         }
      }
      else if (!this.description.equals(other.description))
      {
         return false;
      }
      if (this.fields == null)
      {
         if (other.fields != null)
         {
            return false;
         }
      }
      else if (!this.fields.equals(other.fields))
      {
         return false;
      }
      if (this.fileName == null)
      {
         if (other.fileName != null)
         {
            return false;
         }
      }
      else if (!this.fileName.equals(other.fileName))
      {
         return false;
      }
      if (this.name == null)
      {
         if (other.name != null)
         {
            return false;
         }
      }
      else if (!this.name.equals(other.name))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "TemplateDTO [name=" + this.name + ", description=" + this.description + ", fileName=" + this.fileName
                 + ", fields=" + this.fields + "]";
   }

}
