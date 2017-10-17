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
import java.util.Date;

/**
 * Generic data Transfer Object which represents a task to realize on a project
 */
public class TaskInfo implements Serializable
{

   /** UID for serialization */
   private static final long serialVersionUID = -1293552627023801382L;

   /** The identifier of the task */
   private String id;

   /** The title of the task */
   private String title;

   /** Description of the task */
   private String description;

   /** The type of the task (work or bug)*/
   private TaskType type;

   /** The date of the last update on this task */
   private Date              lastUpdateDate;

   /** The status of the task */
   private TaskStatus        status;

   /** Initial estimation in days (planning poker estimation in scrum projects) */
   private float             initialEstimation;

   /** The start date of the task */
   private Date              startDate;

   /** The end date of the task */
   private Date              endDate;

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
    * Get the title
    * @return the title
    */
   public String getTitle() {
      return title;
   }

   /**
    * Set the title
    * @param title the title to set
    */
   public void setTitle(String title)
   {
      this.title = title;
   }

   /**
    * Get the status
    *
    * @return the status
    */
   public TaskStatus getStatus()
   {
      return status;
   }

   /**
    * Set the status
    *
    * @param status
    *           the status to set
    */
   public void setStatus(TaskStatus status)
   {
      this.status = status;
   }

   /**
    * Get the initialEstimation
    *
    * @return the initialEstimation
    */
   public float getInitialEstimation()
   {
      return initialEstimation;
   }

   /**
    * Set the initialEstimation
    *
    * @param initialEstimation
    *           the initialEstimation to set
    */
   public void setInitialEstimation(float initialEstimation)
   {
      this.initialEstimation = initialEstimation;
   }

   /**
    * Get the startDate
    *
    * @return the startDate
    */
   public Date getStartDate()
   {
      return startDate;
   }

   /**
    * Set the startDate
    *
    * @param startDate
    *           the startDate to set
    */
   public void setStartDate(Date startDate)
   {
      this.startDate = startDate;
   }

   /**
    * Get the endDate
    *
    * @return the endDate
    */
   public Date getEndDate()
   {
      return endDate;
   }

   /**
    * Set the endDate
    *
    * @param endDate
    *           the endDate to set
    */
   public void setEndDate(Date endDate)
   {
      this.endDate = endDate;
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

   /**
    * Get the type
    * @return the type
    */
   public TaskType getType() {
      return type;
   }

   /**
    * Set the type
    * @param type the type to set
    */
   public void setType(TaskType type) {
      this.type = type;
   }

   /**
    * Get the lastUpdateDate
    * 
    * @return the lastUpdateDate
    */
   public Date getLastUpdateDate()
   {
      return lastUpdateDate;
   }

   /**
    * Set the lastUpdateDate
    * 
    * @param lastUpdateDate
    *           the lastUpdateDate to set
    */
   public void setLastUpdateDate(Date lastUpdateDate)
   {
      this.lastUpdateDate = lastUpdateDate;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((id == null) ? 0 : id.hashCode());
      return result;
   }

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
      TaskInfo other = (TaskInfo) obj;
      if (id == null)
      {
         if (other.id != null)
         {
            return false;
         }
      }
      else if (!id.equals(other.id))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "TaskInfo [id=" + id + ", title=" + title + ", description=" + description + ", type=" + type
                 + ", lastUpdateDate=" + lastUpdateDate + ", status=" + status + ", initialEstimation="
                 + initialEstimation + ", startDate=" + startDate + ", endDate=" + endDate + "]";
   }

}
