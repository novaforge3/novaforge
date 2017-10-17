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
 * The DTO for the join table ProjectDiscipline which gives the info for the couple project/discipline
 */
public class ProjectDisciplineDTO implements Serializable{

   /** UID for serialization */
   private static final long serialVersionUID = 3985459456441248669L;
   
   /** Business identifier */
   private long id;
   
   /** Percent on this discipline on the project*/
   private int disciplinePourcentage;
   
   /** the discipline  */
   private DisciplineDTO disciplineDTO;
   
   private String projectId;
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
    * Get the disciplinePourcentage
    * @return the disciplinePourcentage
    */
   public int getDisciplinePourcentage() {
      return disciplinePourcentage;
   }
   /**
    * Set the disciplinePourcentage
    * @param disciplinePourcentage the disciplinePourcentage to set
    */
   public void setDisciplinePourcentage(int disciplinePourcentage) {
      this.disciplinePourcentage = disciplinePourcentage;
   }
   /**
    * Get the disciplineDTO
    * @return the disciplineDTO
    */
   public DisciplineDTO getDisciplineDTO() {
      return disciplineDTO;
   }
   /**
    * Set the disciplineDTO
    * @param disciplineDTO the disciplineDTO to set
    */
   public void setDisciplineDTO(DisciplineDTO disciplineDTO) {
      this.disciplineDTO = disciplineDTO;
   }
   /**
    * Get the projectId
    * @return the projectId
    */
   public String getProjectId() {
      return projectId;
   }
   /**
    * Set the projectId
    * @param projectId the projectId to set
    */
   public void setProjectId(String projectId) {
      this.projectId = projectId;
   }

   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((disciplineDTO == null) ? 0 : disciplineDTO.hashCode());
      result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
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
      ProjectDisciplineDTO other = (ProjectDisciplineDTO) obj;
      if (disciplineDTO == null) {
         if (other.disciplineDTO != null) {
            return false;
         }
      } else if (!disciplineDTO.equals(other.disciplineDTO)) {
         return false;
      }
      if (projectId == null) {
         if (other.projectId != null) {
            return false;
         }
      } else if (!projectId.equals(other.projectId)) {
         return false;
      }
      return true;
   }
   
   
}
