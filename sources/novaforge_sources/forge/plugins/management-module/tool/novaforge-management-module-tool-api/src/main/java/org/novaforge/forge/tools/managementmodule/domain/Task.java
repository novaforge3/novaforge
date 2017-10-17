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
import java.util.Date;
import java.util.Set;

/**
 * A task represents a work do produce for an user on an (or two) iterations, it can be attached to a scope
 * unit or to a mantis bug
 */
public interface Task extends Serializable {
	
   /**
    * Get the initial estimation (planningPokerEstimation in scrum)
    * @return the initialEstimation
    */
   float getInitialEstimation();

   /**
    * Set the initial estimation (planningPokerEstimation in scrum)
    * @param initialEstimation the initialEstimation to set
    */
   void setInitialEstimation(float initialEstimation);
   
   /**
    * Get the id
    * @return the id
    */
   Long getId();

   /**
    * Get the name
    * @return the name
    */
   String getName();

   /**
    * Set the name
    * @param name the name to set
    */
   void setName(String name);

   /**
    * Get the status
    * @return the status
    */
   StatusTask getStatus();

   /**
    * Set the status
    * @param status the status to set
    */
   void setStatus(StatusTask status);

   /**
    * Get the user
    * @return the user
    */
   User getUser();

   /**
    * Set the user
    * @param user the user to set
    */
   void setUser(User user);

   /**
    * Get the iterationsTasks
    * @return the iterationsTasks
    */
   Set<IterationTask> getIterationsTasks();

   /**
    * Set the iterationsTasks
    * @param iterationsTasks the iterationsTasks to set
    */
   void setIterationsTasks(Set<IterationTask> iterationsTasks);

   /**
    * Get the discipline
    * @return the discipline
    */
   Discipline getDiscipline();

   /**
    * Set the discipline
    * @param discipline the discipline to set
    */
   void setDiscipline(Discipline discipline);

   /**
    * Get the scopeUnit
    * @return the scopeUnit
    */
   ScopeUnit getScopeUnit();

   /**
    * Set the scopeUnit
    * @param scopeUnit the scopeUnit to set
    */
   void setScopeUnit(ScopeUnit scopeUnit);

   /**
    * @return the taskCategory
    */
   TaskCategory getTaskCategory();

   /**
    * @param taskCategory the taskCategory to set
    */
   void setTaskCategory(TaskCategory taskCategory);

   /**
    * @return the bug
    */
   Bug getBug();

   /**
    * @param bug the bug to set
    */
   void setBug(Bug bug);

   /**
    * @return the startDate
    */
   Date getStartDate();

   /**
    * @param startDate the startDate to set
    */
   void setStartDate(Date startDate);

   /**
    * @return the endDate
    */
   Date getEndDate();

   /**
    * @param endDate the endDate to set
    */
   void setEndDate(Date endDate);

   /**
    * @return the description
    */
   String getDescription();

   /**
    * @param description the description to set
    */
   void setDescription(String description);

   /**
    * @return the comment
    */
   String getComment();

   /**
    * @param comment the comment to set
    */
   void setComment(String comment);

   /**
    * @return the lastUpdateDate
    */
   Date getLastUpdateDate();

   /**
    * @param lastUpdateDate the lastUpdateDate to set
    */
   void setLastUpdateDate(Date lastUpdateDate);

   /**
    * Get the taskType
    * @return the taskType
    */
   TaskType getTaskType();

   /**
    * Set the taskType
    * @param taskType the taskType to set
    */
   void setTaskType(TaskType taskType);

   @Override
   int hashCode();

   @Override
   boolean equals(Object obj);

   /**
    * Get the current remaining time of the task (history can be found on IterationTask
    * Be careful this data is not persistent and will not be set if you use collection navigation
    * without full initialization of this task, you can fill it using TaskManager.getTask(long id)
    * @return the remaining time
    * @throws IllegalStateException if the remainingTime field has not be initialized
    */
   float getRemainingTime();

   /**
    * Set the remaining time of the task
    * @param newRemainingTime the new remaining time of the task
    */
   void setRemainingTime(float newRemainingTime);

   /**
    * Get the total (and not for the currentIteration) consumed time for a task
    * Be careful this data is not persistent and will not be set if you use collection navigation
    * without full initialization of this task, you can fill it using TaskManager.getTask(long id)
    *
    * @return the amount of consumed time
    * @throws IllegalStateException if the consumedTime field has not be initialized
    */
   float getConsumedTime();

   /**
    * Set the consumed time of the task
    * @param newConsumedTime the new consumed time of the task
    */
   void setConsumedTime(float newConsumedTime);

   /**
    * Get the current iteration of a task
    * Be careful this data is not persistent and will not be set if you use collection navigation
    * without full initialization of this task, you can fill it using TaskManager.getTask(long id)
    * @return the current iteration
    * @throws IllegalStateException if the currentIteration field has not be initialized
    */
   Iteration getCurrentIteration();

   /**
    * Set the current iteration - must not be null
    *
    * @param newIteration
    *     the new Iteration
    *
    * @throws IllegalStateException
    *     if the new Iteration is null
    */
   void setCurrentIteration(Iteration newIteration);
   
}
