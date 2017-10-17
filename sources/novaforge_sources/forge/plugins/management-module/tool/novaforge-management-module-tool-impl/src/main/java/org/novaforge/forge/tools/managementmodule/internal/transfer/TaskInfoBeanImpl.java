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
package org.novaforge.forge.tools.managementmodule.internal.transfer;

import org.novaforge.forge.core.plugins.categories.management.TaskInfoBean;
import org.novaforge.forge.core.plugins.categories.management.TaskStatusEnum;
import org.novaforge.forge.core.plugins.categories.management.TaskTypeEnum;

import java.util.Date;

public class TaskInfoBeanImpl implements TaskInfoBean {

   private static final long serialVersionUID = 8585880629716971617L;

   /** The identifier of the task */
   private String           id;

   /** The title of the task */
   private String           title;

   /** Description of the task */
   private String           description;
   
   /** The date of the last update on this task */
   private Date             lastUpdateDate;
   
   /** The type of the task (work or bug) */
   private TaskTypeEnum     type;

   /** Initial estimation in days (planning poker estimation in scrum projects) */
   private float            initialEstimation;
   
   /** The start date of the task */
   private Date             startDate;

   /** The end date of the task */
   private Date             endDate;
   
   /** The status of the task */
   private TaskStatusEnum   status;
   
   /**
    * Get the id
    * @return the id
    */
   @Override
   public String getId() {
      return id;
   }

   /**
    * Set the id
    * @param id the id to set
    */
   @Override
   public void setId(String id) {
      this.id = id;
   }

   /**
    * Get the title
    * @return the title
    */
   @Override
   public String getTitle() {
      return title;
   }

   /**
    * Set the title
    * @param title the title to set
    */
   @Override
   public void setTitle(String title) {
      this.title = title;
   }

   /**
    * Get the description
    * @return the description
    */
   @Override
   public String getDescription() {
      return description;
   }

   /**
    * Set the description
    * @param description the description to set
    */
   @Override
   public void setDescription(String description) {
      this.description = description;
   }

   /**
    * Get the type
    * @return the type
    */
   @Override
   public TaskTypeEnum getType()
   {
      return type;
   }

   /**
    * Set the type
    * @param type the type to set
    */
   @Override
   public void setType(TaskTypeEnum type)
   {
      this.type = type;
   }

   /**
    * Get the lastUpdateDate
    * @return the lastUpdateDate
    */
   @Override
   public Date getLastUpdateDate()
   {
      return lastUpdateDate;
   }

   /**
    * Set the lastUpdateDate
    * @param lastUpdateDate the lastUpdateDate to set
    */
   @Override
   public void setLastUpdateDate(Date lastUpdateDate)
   {
      this.lastUpdateDate = lastUpdateDate;
   }

   /**
    * Get the status
    * @return the status
    */
   @Override
   public TaskStatusEnum getStatus()
   {
      return status;
   }

   /**
    * Set the status
    * @param status the status to set
    */
   @Override
   public void setStatus(TaskStatusEnum status)
   {
      this.status = status;
   }

   /**
    * Get the startDate
    * @return the startDate
    */
   @Override
   public Date getStartDate() {
      return startDate;
   }

   /**
    * Set the startDate
    * @param startDate the startDate to set
    */
   @Override
   public void setStartDate(Date startDate) {
      this.startDate = startDate;
   }

   /**
    * Get the endDate
    * @return the endDate
    */
   @Override
   public Date getEndDate() {
      return endDate;
   }

   /**
    * Set the endDate
    * @param endDate the endDate to set
    */
   @Override
   public void setEndDate(Date endDate) {
      this.endDate = endDate;
   }

   /**
    * Get the initialEstimation
    * @return the initialEstimation
    */
   @Override
   public float getInitialEstimation()
   {
      return initialEstimation;
   }

   /**
    * Set the initialEstimation
    * @param initialEstimation the initialEstimation to set
    */
   @Override
   public void setInitialEstimation(float initialEstimation)
   {
      this.initialEstimation = initialEstimation;
   }

   @Override
   public String toString() {
      return "TaskInfoBeanImpl [id=" + id + ", title=" + title + ", description=" + description
            + ", lastUpdateDate=" + lastUpdateDate + ", type=" + type + ", initialEstimation="
            + initialEstimation + ", startDate=" + startDate + ", endDate=" + endDate + ", status=" + status
            + "]";
   }
   
}
