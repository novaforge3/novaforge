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
package org.novaforge.forge.tools.managementmodule.business;

import org.novaforge.forge.tools.managementmodule.domain.Bug;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.TaskCriteria;
import org.novaforge.forge.tools.managementmodule.domain.TaskType;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.List;
import java.util.Set;

/**
 * This manager is used to manage task, the task status and the affectation of tasks on iterations
 */
public interface TaskManager {

   /************************* IterationTask ********************************************************************/

   /**
    * Create and return a new IterationTask instance (not persistent)
    * 
    * @return the new IterationTask
    */
   IterationTask newIterationTask();

   /**
    * Get all the IterationTask (with their tasks datas) and business fields filled (consumed time, remaining
    * time...)
    * 
    * @param iteration
    *           the iteration to use for data recovery
    * @return the appropriate Set
    * @throws ManagementModuleException
    *            problem during data recovery
    */
   Set<IterationTask> getFullIterationTasksList(Iteration iteration) throws ManagementModuleException;

   /**
    * Get an iteration with all its iterationsTasks / tasks loaded with business fields
    * 
    * @param iterationId
    *           the iteration id to identify the iteration to return
    * @return the appropriate iteration
    * @throws ManagementModuleException
    *            problem during data recovery
    */
   Iteration getIterationWithFullTaskList(long iterationId) throws ManagementModuleException;
   
   /************************* Task ********************************************************************/

   /**
    * This methods create a new Task Object (not persistent)
    * 
    * @return the new task
    */
   Task newTask();

   /**
    * This methods persists a new task in data layer
    * 
    * @param task
    *           the task to insert
    * @return the new persistent task (with id)
    * @throws ManagementModuleException
    *            problem during creation/insertion
    */
   Task persistTask(Task task) throws ManagementModuleException;

   /**
    * Modify a task in data layer
    * 
    * @param task
    *           the task to modify
    * @return the modified task
    * @throws ManagementModuleException
    *            problem during modification
    */
   Task updateTask(Task task) throws ManagementModuleException;

   /**
    * Delete the task identified by the given id
    * 
    * @param taskId
    *           the task identifier to use
    * @throws ManagementModuleException
    *            problem during suppression
    */
   void deleteTask(long taskId) throws ManagementModuleException;

   /**
    * Return the task identified by the argument
    * 
    * @param taskId
    *           the id to use for the search
    * @return the task or null if not found
    * @throws ManagementModuleException
    *            problem during data recovery
    */
   Task getTask(long taskId) throws ManagementModuleException;

   /**
    * Return all task of a project plan with an optional filter on disciplineFunctionalId
    * 
    * @param projectPlanId
    * @param disciplineFunctionalId Filter on disciplineFunctionId, optional (set to null to disable)
    * @throws ManagementModuleException
    * @return
    */
   Set<Task> findByProjectPlanIdAndDisciplineId(Long projectPlanId, String disciplineId) throws ManagementModuleException;
   
   /**
    * Return the tasks linked to a scopeUnit
    * 
    * @param scopeUnitId
    * @return
    * @throws ManagementModuleException
    */
   List<Task> findByScopeUnitId(Long scopeUnitId) throws ManagementModuleException;

   
   /**
    * Return the tasks linked to a taskCategory
    * 
    * @param taskCategoryId
    * @return
    * @throws ManagementModuleException
    */
   List<Task> findByTaskCategoryId(Long taskCategoryId) throws ManagementModuleException;
   
   /**
    * Return tasks linked to the given ScopeUnit and Discipline
    * @param scopeUnitId
    * @param disciplineId
    * @return
	* @throws ManagementModuleException 
    */
   List<Task> findByScopeUnitIdAndDisciplineId(Long scopeUnitId, Long disciplineId) throws ManagementModuleException;

   /**
    * Returns tasks for an iteration with status from iterationTask
    * @param iterationId
    * @param disciplineId
    * @return
    * @throws ManagementModuleException
    */
   Set<Task> getIterationTaskListMergedWithIterationTaskAndDisciplineFunctionalId(long iterationId, String disciplineId) throws ManagementModuleException;
   
   /************************* TaskCategory ********************************************************************/

   /**
    * Create and return a new TaskCategory instance (not persistent - without id)
    * 
    * @return the new TaskCategory
    */
   TaskCategory newTaskCategory();

   /**
    * Control and persist a taskCategory in data layer
    * 
    * @param taskCategory
    * @return
    * @throws ManagementModuleException
    */
   TaskCategory creeteTaskCategory(TaskCategory taskCategory) throws ManagementModuleException;

   /**
    * Control and modify the TaskCategory
    * 
    * @param taskCategory
    *           the TaskCategory to modify
    * @return the modified task category
    * @throws ManagementModuleException
    */
   TaskCategory updateTaskCategory(TaskCategory taskCategory) throws ManagementModuleException;

   /**
    * Delete a task category
    * @param taskCategory the TaskCategory to delete
    * @throws ManagementModuleException if problem during suppression
    */
   void deleteTaskCategory(TaskCategory taskCategory) throws ManagementModuleException;
   
   /**
    * Get a task category using its id
    * @param id the id to use
    * 
    * @return the appropriate TaskCategory or null if not found
    * @throws ManagementModuleException problem during data getting
    */
   TaskCategory getTaskCategory(long id) throws ManagementModuleException;
   
   /************************* TaskType ********************************************************************/

   TaskType newTaskType();

   TaskType createTaskType(TaskType taskType) throws ManagementModuleException;

   TaskType updateTaskType(TaskType taskType) throws ManagementModuleException;

   void deleteTaskType(String taskTypeFunctionalId) throws ManagementModuleException;

   TaskType getTaskType(String functionalId) throws ManagementModuleException;

   /************************* StatusTask ********************************************************************/

   StatusTask newStatusTask();

   StatusTask createStatusTask(final StatusTask st) throws ManagementModuleException;

   boolean updateStatusTask(final StatusTask st) throws ManagementModuleException;

   boolean deleteStatusTask(final String functionalId) throws ManagementModuleException;

   StatusTask getStatusTask(final String functionalId) throws ManagementModuleException;

   List<StatusTask> getAllStatusTask() throws ManagementModuleException;

   /**
    * Get the default taskstatus
    * @return the default taskstatus
    * @throws ManagementModuleException problem during recovery
    */
   StatusTask getDefaultTaskStatus() throws ManagementModuleException;

   /**
    * Close the iteration identified by the parameter id and do the required business actions
    * @param iterationId the identifier of the iteration to close
    */
   Iteration closeIteration(long iterationId) throws ManagementModuleException;

   /**
    * Get the current iteration for a task
    * @param task the task
    * @return the current iteration of the task
    */
   Iteration getCurrentIteration(Task task);

   /**
    * Get a task list using criteria in TaskCriteria object
    * @param taskCriteria the object containing task criteria
    * @return the appropriate tasks
    * @throws ManagementModuleException problem during operation
    */
   Set<Task> getTasksByCriteria(TaskCriteria taskCriteria) throws ManagementModuleException;

   
   /**
    * Get the additionals infos from the bug tracker on the bug passed in arguments
    * @param bug the bug to update
    * @param toolProjectId the tool project id
    * @param user the user
    * @throws ManagementModuleException problem during information recuperation from bug tracker
    */
   void fillBugWithBugTrackerInformations(Bug bug, String toolProjectId, String login) throws ManagementModuleException;

   /**
    * GEt all the bugs from the bug tracker
    * @param toolProjectId the tool project id
    * @param login the user login
    * @return the list
    * @throws ManagementModuleException problem during data get
    */
   List<Bug> getAllBugsFromBugTracker(String toolProjectId, String login) throws ManagementModuleException;
   
   /**
    * Get a bug from persistence layer from bug tracker id
    * @param bugTrackerId the id of the bug we searched in database
    * @return the bug or null if not found
    * @throws ManagementModuleException problem during data get
    */
   Bug getBug(String bugTrackerId) throws ManagementModuleException;
   
   
   /**
    * This methods persists a new bug in data layer
    * 
    * @param bug the bug to insert
    * @return the new bug (with id)
    * @throws ManagementModuleException problem during creation/insertion
    */
   Bug persistBug(Bug bug) throws ManagementModuleException;

   /**
    * Modify a bug in data layer
    * 
    * @param bug the bug to modify
    * @return the modified bug
    * @throws ManagementModuleException problem during modification
    */
   Bug updateBug(Bug bug) throws ManagementModuleException;
   
   /**
    * This methods create a new Bug Object (not persistent)
    * 
    * @return the new Bug
    */
   Bug newBug();

   /**
    * Delete the iteration tasks in a project plan
    * @param projectPlanId the project plan id
    * @throws ManagementModuleException problem during suppression
    */
   void deleteIterationTaskByProjectPlanId(Long projectPlanId) throws ManagementModuleException;

   /**
    * Delete the tasks in a project plan
    * @param projectPlanId the project plan id
    * @throws ManagementModuleException problem during suppression
    */
   void deleteTasksByProjectPlanId(Long id) throws ManagementModuleException;

   /**
    * This methods initialize the transient consumed and remaining times using
    * its IterationTask
    * 
    * @param task the task
 * @return Task
    */
   Task initializeBusinessFieldsOnTask(Task task);
}
