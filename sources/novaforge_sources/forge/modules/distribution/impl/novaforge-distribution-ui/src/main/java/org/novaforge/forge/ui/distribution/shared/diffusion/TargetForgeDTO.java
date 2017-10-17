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

package org.novaforge.forge.ui.distribution.shared.diffusion;

import java.io.Serializable;

/**
 * @author rols-p
 *
 */
public class TargetForgeDTO implements Serializable
{
   /**
    * 
    */
   private static final long serialVersionUID = -2230111837047742686L;

   private String label;

   private String description;

   private int    forgeLevel;

   /**
    * 
    */
   public TargetForgeDTO()
   {
      super();
   }

   /**
    * @param forgeId
    * @param label
    * @param description
    * @param forgeLevel
    */
   public TargetForgeDTO(String label, String description, int forgeLevel)
   {
      super();
      this.label = label;
      this.description = description;
      this.forgeLevel = forgeLevel;
   }

   /**
    * @return the label
    */
   public String getLabel()
   {
      return label;
   }

   /**
    * @param label
    *           the label to set
    */
   public void setLabel(String label)
   {
      this.label = label;
   }

   /**
    * @return the description
    */
   public String getDescription()
   {
      return description;
   }

   /**
    * @param description
    *           the description to set
    */
   public void setDescription(String description)
   {
      this.description = description;
   }

   /**
    * @return the forgeLevel
    */
   public int getForgeLevel()
   {
      return forgeLevel;
   }

   /**
    * @param forgeLevel
    *           the forgeLevel to set
    */
   public void setForgeLevel(int forgeLevel)
   {
      this.forgeLevel = forgeLevel;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((description == null) ? 0 : description.hashCode());
      result = prime * result + ((label == null) ? 0 : label.hashCode());
      return result;
   }

   /**
    * {@inheritDoc}
    */
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
      TargetForgeDTO other = (TargetForgeDTO) obj;
      if (description == null)
      {
         if (other.description != null)
         {
            return false;
         }
      }
      else if (!description.equals(other.description))
      {
         return false;
      }
      if (label == null)
      {
         if (other.label != null)
         {
            return false;
         }
      }
      else if (!label.equals(other.label))
      {
         return false;
      }
      return true;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return "TargetForgeDTO [label=" + label + ", description=" + description + ", forgeLevel=" + forgeLevel
            + "]";
   }
}

