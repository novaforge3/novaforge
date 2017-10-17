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
import java.util.List;
import java.util.Set;

public class ProjectDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6149348412863185817L;

	private Long id;
	private String projectId;
	private String name;
	private String description;
	private TransformationDTO transformation;
	private EstimationComponentSimpleDTO estimationComponentSimple;
	private List<ProjectDisciplineDTO> disciplines;
	private List<TaskCategoryDTO> taskCategories;
	private Set<UserDTO> userList;
	private UnitTimeEnum unitTimeEnum;
	private Integer lastVersionProjectPlan;
	
   /**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the projectId
	 */
	public String getProjectId() {
		return projectId;
	}
	/**
	 * @param projectId the projectId to set
	 */
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the transformation
	 */
	public TransformationDTO getTransformation() {
		return transformation;
	}
	/**
	 * @param transformation the transformation to set
	 */
	public void setTransformation(TransformationDTO transformation) {
		this.transformation = transformation;
	}
	
	
	/**
	 * @return the estimationComponentSimple
	 */
	public EstimationComponentSimpleDTO getEstimationComponentSimple() {
		return estimationComponentSimple;
	}
	/**
	 * @param estimationComponentSimple the estimationComponentSimple to set
	 */
	public void setEstimationComponentSimple(
			EstimationComponentSimpleDTO estimationComponentSimple) {
		this.estimationComponentSimple = estimationComponentSimple;
	}
	
	
	
	/**
	 * @return the disciplines
	 */
	public List<ProjectDisciplineDTO> getProjectDisciplines() {
		return disciplines;
	}
	/**
	 * @param disciplines the disciplines to set
	 */
	public void setDisciplines(List<ProjectDisciplineDTO> disciplines) {
		this.disciplines = disciplines;
	}
	
	
	/**
	 * @return the taskCategories
	 */
	public List<TaskCategoryDTO> getTaskCategories() {
		return taskCategories;
	}
	/**
	 * @param taskCategories the taskCategories to set
	 */
	public void setTaskCategories(List<TaskCategoryDTO> taskCategories) {
		this.taskCategories = taskCategories;
	}

   /**
    * Get the unitTimeEnum
    * @return the unitTimeEnum
    */
   public UnitTimeEnum getUnitTime() {
      return unitTimeEnum;
   }
   /**
    * Set the unitTimeEnum
    * @param unitTimeEnum the unitTimeEnum to set
    */
   public void setUnitTime(UnitTimeEnum unitTimeEnum) {
      this.unitTimeEnum = unitTimeEnum;
   }

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime
				* result
				+ ((estimationComponentSimple == null) ? 0
						: estimationComponentSimple.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((projectId == null) ? 0 : projectId.hashCode());
		result = prime * result
				+ ((transformation == null) ? 0 : transformation.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		ProjectDTO other = (ProjectDTO) obj;
		if (description == null) {
			if (other.description != null) {
            return false;
         }
		} else if (!description.equals(other.description)) {
         return false;
      }
		if (estimationComponentSimple == null) {
			if (other.estimationComponentSimple != null) {
            return false;
         }
		} else if (!estimationComponentSimple
				.equals(other.estimationComponentSimple)) {
         return false;
      }
		if (name == null) {
			if (other.name != null) {
            return false;
         }
		} else if (!name.equals(other.name)) {
         return false;
      }
		if (projectId == null) {
			if (other.projectId != null) {
            return false;
         }
		} else if (!projectId.equals(other.projectId)) {
         return false;
      }
		if (transformation == null) {
			if (other.transformation != null) {
            return false;
         }
		} else if (!transformation.equals(other.transformation)) {
         return false;
      }
		return true;
	}

	@Override
	public String toString()
	{
		return "ProjectDTO [id=" + id + ", projectId=" + projectId + ", name=" + name + ", description=" + description
							 + ", transformation=" + transformation + ", estimationComponentSimple=" + estimationComponentSimple
							 + "]";
	}

   /**
    * Get the userList
    * @return the userList
    */
   public Set<UserDTO> getUserList() {
      return userList;
   }
   /**
    * Set the userList
    * @param userList the userList to set
    */
   public void setUserList(Set<UserDTO> userList) {
      this.userList = userList;
   }
   
   /**
    * Return Last Version of projectPlan
    * @return
    */
   public Integer getLastVersionProjectPlan() {
      return lastVersionProjectPlan;
   }
   
   /**
    * set last version of projectPlan
    * @param lastVersionProjectPlan
    */
   public void setLastVersionProjectPlan(Integer lastVersionProjectPlan) {
      this.lastVersionProjectPlan = lastVersionProjectPlan;
   }

}
