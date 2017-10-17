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

/**
 * @author caseryj
 */
public class FolderNode implements NodeDTO, Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = -683780121096403937L;
   private String            name;
   private String            path;
   private boolean           isExist;
   private List<NodeDTO>     children;

   /**
    * Default constructor.
    */
   public FolderNode()
   {
      this.isExist = true;
      this.children = new ArrayList<NodeDTO>();
   }

   /**
    * @param pName
    *           represents the folder name
    * @param pPath
    *           represents the folder patch
    * @param pChildren
    */
   public FolderNode(final String pName, final String pPath, final List<NodeDTO> pChildren)
   {
      super();
      this.name = pName;
      this.path = pPath;
      this.isExist = true;
      if (pChildren != null)
      {
         this.children = pChildren;
      }
      else
      {
         this.children = new ArrayList<NodeDTO>();
      }
   }

   /**
    * @return the path of these children
    */
   public String getChildPath()
   {
      String childPath = "";
      if (this.getPath().endsWith("/") || (this.getPath() == null))
      {
         childPath = this.getPath() + this.getName() + "/";
      }
      else
      {
         childPath = this.getPath() + "/" + this.getName() + "/";
      }
      return childPath;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return this.name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setName(final String pName)
   {
      this.name = pName;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getPath()
   {
      return this.path;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setPath(final String pPath)
   {
      this.path = pPath;
   }

   /**
    * {@inheritDoc}
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
      final FolderNode other = (FolderNode) obj;
      if (this.children == null)
      {
         if (other.children != null)
         {
            return false;
         }
      }
      else if (!this.children.equals(other.children))
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

   /**
    * @return the children
    */
   public List<NodeDTO> getChildren()
   {
      return this.children;
   }

   /**
    * @param pChildren
    *     the childrens to set
    */
   public void setChildren(final List<NodeDTO> pChildren)
   {
      this.children = pChildren;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      final int prime  = 31;
      int       result = 1;
      result = (prime * result) + ((this.children == null) ? 0 : this.children.hashCode());
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
      final FolderNode other = (FolderNode) obj;
      if (this.children == null)
      {
         if (other.children != null)
         {
            return false;
         }
      }
      else if (!this.children.equals(other.children))
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
