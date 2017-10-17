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
import java.util.ArrayList;
import java.util.List;

/**
 * @author blachonm
 *
 */
public class ForgeNode implements Serializable
{
   private static final long serialVersionUID = 1793666819816994967L;

   private String name;
   private String uri;
   private ForgeApplication application;
   private List<ForgeNode>   childs           = new ArrayList<ForgeNode>();

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getUri()
   {
      return uri;
   }

   public void setUri(String uri)
   {
      this.uri = uri;
   }

   public ForgeApplication getApplication()
   {
      return application;
   }

   public void setApplication(ForgeApplication application)
   {
      this.application = application;
   }

   public List<ForgeNode> getChilds()
   {
      return childs;
   }

   public void setChilds(List<ForgeNode> childs)
   {
      this.childs = childs;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((uri == null) ? 0 : uri.hashCode());
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
      ForgeNode other = (ForgeNode) obj;
      if (uri == null)
      {
         if (other.uri != null)
         {
            return false;
         }
      }
      else if (!uri.equals(other.uri))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "ForgeNode [name=" + name + ", uri=" + uri + ", application=" + application + ", childs=" + childs + "]";
   }

}
