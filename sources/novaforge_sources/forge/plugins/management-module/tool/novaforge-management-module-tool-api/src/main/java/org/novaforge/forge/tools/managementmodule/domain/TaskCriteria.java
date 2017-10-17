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

/**
 * This class contains different criteria usable on a task to act as a filter to look of tasks
 */
public class TaskCriteria implements Serializable {

   /** Serial UID */
   private static final long serialVersionUID = 7686412208623898908L;

   /** The login of the user affected to the task */
   private String userLogin;
   
   /** The functional id of the task type */
   private String taskTypeFunctionalId;
   
   /** The identifier of the task's iteration*/
   private Long iterationId;
   
   /** The functional id of the task status */
   private String taskStatusFunctionalId;

   /** The functional id of the discipline */
   private String disciplineFunctionalId;

   /**
    * Get the userLogin
    * @return the userLogin
    */
   public String getUserLogin() {
      return userLogin;
   }

   /**
    * Set the userLogin
    * @param userLogin the userLogin to set
    */
   public void setUserLogin(String userLogin) {
      this.userLogin = userLogin;
   }

   /**
    * Set the user
    * @param user the user
    */
   public void setUser(User user){
      this.userLogin = user.getLogin();
   }
   
   /**
    * Get the taskTypeFunctionalId
    * @return the taskTypeFunctionalId
    */
   public String getTaskTypeFunctionalId() {
      return taskTypeFunctionalId;
   }

   /**
    * Set the taskTypeFunctionalId
    * @param taskTypeFunctionalId the taskTypeFunctionalId to set
    */
   public void setTaskTypeFunctionalId(String taskTypeFunctionalId) {
      this.taskTypeFunctionalId = taskTypeFunctionalId;
   }

   /**
    * Set the taskTypeFunctionalId by its TaskType
    * @param taskType the TaskType to set
    */
   public void setTaskType(TaskType taskType) {
      this.taskTypeFunctionalId = taskType.getFunctionalId();
   }
   
   /**
    * Get the iterationId
    * @return the iterationId
    */
   public Long getIterationId() {
      return iterationId;
   }

   /**
    * Set the iterationId
    * @param iterationId the iterationId to set
    */
   public void setIterationId(Long iterationId) {
      this.iterationId = iterationId;
   }

   /**
    * Set the iterationId via its iteration
    * @param iteration the iteration
    */
   public void setIteration(Iteration iteration) {
      this.iterationId = iteration.getId();
   }
   
   /**
    * Get the taskStatusFunctionalId
    * @return the taskStatusFunctionalId
    */
   public String getTaskStatusFunctionalId() {
      return taskStatusFunctionalId;
   }

   /**
    * Set the taskStatusFunctionalId
    * @param taskStatusFunctionalId the taskStatusFunctionalId to set
    */
   public void setTaskStatusFunctionalId(String taskStatusFunctionalId) {
      this.taskStatusFunctionalId = taskStatusFunctionalId;
   }
   
   /**
    * Set the taskTypeFunctionalId by its TaskType
    * @param taskType the TaskType to set
    */
   public void setTaskStatus(StatusTask taskStatus) {
      this.taskStatusFunctionalId = taskStatus.getFunctionalId();
   }

   /**
    * Get the disciplineFunctionalId
    * @return the disciplineFunctionalId
    */
   public String getDisciplineFunctionalId() {
      return disciplineFunctionalId;
   }

   /**
    * Set the disciplineFunctionalId
    * @param disciplineFunctionalId the disciplineFunctionalId to set
    */
   public void setDisciplineFunctionalId(String disciplineFunctionalId) {
      this.disciplineFunctionalId = disciplineFunctionalId;
   }
   
   /**
    * Reset all criteria
    */
   public void resetCriteria(){
      this.disciplineFunctionalId = null;
      this.iterationId = null;
      this.taskStatusFunctionalId = null;
      this.taskTypeFunctionalId = null;
      this.userLogin = null;
   }
   
}
