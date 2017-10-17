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
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;

/**
 * DTO of the task category
 */
public class TaskCategoryDTO implements Serializable {

   /** UID for serialization */
   private static final long serialVersionUID = -9097697934870454480L;

   /** The category identifier */
   private long id;
   
   /** The category label */
   private String name;

   /**
    * Get the id
    * @return the id
    */
   public long getId() {
      return id;
   }

   /**
    * Set the id
    * @param id the id to set
    */
   public void setId(long id) {
      this.id = id;
   }

   /**
    * Get the name
    * @return the name
    */
   public String getName() {
      return name;
   }

   /**
    * Set the name
    * @param name the name to set
    */
   public void setName(String name) {
      this.name = name;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((name == null) ? 0 : name.hashCode());
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
      TaskCategoryDTO other = (TaskCategoryDTO) obj;
      if (name == null) {
         if (other.name != null) {
            return false;
         }
      } else if (!name.equals(other.name)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "TaskCategoryDTO [id=" + id + ", name=" + name + "]";
   }
   
   
}
