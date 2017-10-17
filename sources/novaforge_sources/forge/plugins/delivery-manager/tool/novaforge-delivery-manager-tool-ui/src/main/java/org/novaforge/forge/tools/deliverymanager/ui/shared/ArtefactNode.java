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
import java.util.HashMap;
import java.util.Map;

/**
 * @author caseryj
 */
public class ArtefactNode implements NodeDTO, Serializable
{

   /**
    * 
    */
   private static final long   serialVersionUID = -5835506045452866735L;
   private String              id;
   private String              name;
   private String              path;
   private boolean             isExist;
   private Map<String, String> fields;

   /**
    * 
    */
   public ArtefactNode()
   {
      this.isExist = true;
      this.fields = new HashMap<String, String>();
   }

   public ArtefactNode(final String pName, final String pPath, final String pId,
         final Map<String, String> pFields)
   {
      super();
      this.name = pName;
      this.path = pPath;
      this.id = pId;
      this.isExist = true;
      this.fields = pFields;
   }

   /**
    * @return the id
    */
   public String getID()
   {
      return this.id;
   }

   /**
    * @param pId
    *           the id to set
    */
   public void setID(final String pId)
   {
      this.id = pId;
   }

   @Override
   public String getName()
   {
      return this.name;
   }

   @Override
   public void setName(final String pName)
   {
      this.name = pName;
   }

   @Override
   public String getPath()
   {
      return this.path;
   }

   @Override
   public void setPath(final String pPath)
   {
      this.path = pPath;
   }

   /**
    * @return the isExist
    */
   @Override
   public boolean isExist()
   {
      return this.isExist;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setExist(final boolean pIsExist)
   {
      this.isExist = pIsExist;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean equalsWithoutPath(final Object obj)
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
      final ArtefactNode other = (ArtefactNode) obj;
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
      if (this.id == null)
      {
         if (other.id != null)
         {
            return false;
         }
      }
      else if (!this.id.equals(other.id))
      {
         return false;
      }
      if (this.isExist != other.isExist)
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

   public Map<String, String> getFields()
   {
      return this.fields;
   }

   public void setFields(final Map<String, String> pFields)
   {
      this.fields = pFields;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      final int prime  = 31;
      int       result = 1;
      result = (prime * result) + ((this.fields == null) ? 0 : this.fields.hashCode());
      result = (prime * result) + ((this.id == null) ? 0 : this.id.hashCode());
      result = (prime * result) + (this.isExist ? 1231 : 1237);
      result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode());
      result = (prime * result) + ((this.path == null) ? 0 : this.path.hashCode());
      return result;
   }

   /**
    * {@inheritDoc}
    */
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
      final ArtefactNode other = (ArtefactNode) obj;
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
      if (this.id == null)
      {
         if (other.id != null)
         {
            return false;
         }
      }
      else if (!this.id.equals(other.id))
      {
         return false;
      }
      if (this.isExist != other.isExist)
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
      if (this.path == null)
      {
         if (other.path != null)
         {
            return false;
         }
      }
      else if (!this.path.equals(other.path))
      {
         return false;
      }
      return true;
   }

}
