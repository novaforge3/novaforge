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

package org.novaforge.forge.tools.managementmodule.domain;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents the project we manage
 */
public interface Project extends Serializable
{
   Long getId();

   /**
    * This method returns the projectId of the project
    * 
    * @return String
    */
   String getProjectId();

   /**
    * This method allows to set the projectId of the project
    * 
    * @param projectId
    */
   void setProjectId(String projectId);

   String getName();

   /**
    * This method allows to set the name of the project
    * 
    * @param name
    */
   void setName(String name);

   /**
    * This method returns the description of the project
    * 
    * @return String
    */
   String getDescription();

   /**
    * This method allows to set the description of the project
    * 
    * @param description
    */
   void setDescription(String description);

   Set<Membership> getMemberships();
   
   void setMemberships(Set<Membership> memberships);

   void addMembership(Membership m);

   void removeMembership(Membership m);

   Set<ProjectPlan> getProjectPlans();

   void setProjectPlans(Set<ProjectPlan> projectPlans);
   
   void addProjectPlan(ProjectPlan projectPlan);

   void removeProjectPlan(ProjectPlan m);

   Set<RefScopeUnit> getRefScopeUnitList();
   
   void setRefScopeUnits(Set<RefScopeUnit> refScopeUnits);

   void addRefScopeUnit(RefScopeUnit pRefScopeUnit);

   void removeRefScopeUnit(RefScopeUnit pRefScopeUnit);
   
	Transformation getTransformation();
	
	void setTransformation(Transformation transformation);

	EstimationComponentSimple getEstimationComponentSimple();

	void setEstimationComponentSimple(
			EstimationComponentSimple estimationComponentSimple);
	
	Set<ProjectDiscipline> getDisciplines();

	void setDisciplines(Set<ProjectDiscipline> disciplines);

	void addDiscipline(ProjectDiscipline m);

	void removeDiscipline(ProjectDiscipline m);

	Set<TaskCategory> getTaskCategories();

	void setTaskCategories(Set<TaskCategory> taskCategories);

	void addTaskCategory(TaskCategory m);

	void removeTaskCategory(TaskCategory m);

	/**
    * Get the unitTime
    * @return the unitTime
    */
   UnitTime getUnitTime();

   /**
    * Set the unitTime
    * @param unitTime the unitTime to set
    */
   void setUnitTime(UnitTime unitTime);

}
