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
package org.novaforge.forge.remote.services.model.management;

import java.io.Serializable;

/**
 * The discipline of a task (project management, analysis, implementation...)
 *
 */
public class Discipline implements Serializable
{

   /** UID for serialization */
   private static final long serialVersionUID = -4198656753237079424L;

   /** The functional identifier of the Discipline */
   private String id;

   /** The description of the Discipline */
   private String description;

   /**
    * Get the id
    * @return the id
    */
   public String getId() {
      return id;
   }

   /**
    * Set the id
    * @param id the id to set
    */
   public void setId(String id) {
      this.id = id;
   }

   /**
    * Get the description
    * @return the description
    */
   public String getDescription() {
      return description;
   }

   /**
    * Set the description
    * @param description the description to set
    */
   public void setDescription(String description) {
      this.description = description;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null) {
         return false;
      }
      if (getClass() != obj.getClass()) {
         return false;
      }
      Discipline other = (Discipline) obj;
      if (id == null) {
         if (other.id != null) {
            return false;
         }
      } else if (!id.equals(other.id)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "DisciplineBean [id=" + id + ", description=" + description + "]";
   }

}
