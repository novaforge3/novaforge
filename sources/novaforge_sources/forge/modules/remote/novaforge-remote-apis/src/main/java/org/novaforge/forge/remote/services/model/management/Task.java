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
 * Generic data Transfer Object which represents a task to realize on a project
 */
public class Task extends TaskInfo implements Serializable
{

   /** UID for serialization */
   private static final long serialVersionUID = -1293552627023801382L;

   /** The time consumed on this task (in days) */
   private float             consumedTime;

   /**
    * The time remaining to complete this task (base on realizer estimation, not the difference between
    * initial one and consumed time)
    */
   private float             remainingTime;

   /** An eventual comment */
   private String            comment;

   /** The discipline of the task (implementation, receipt, project management...) */
   private Discipline        discipline;

   /**
    * The scope unit (use case, user story...) of the task - can be null (depends of taskType and
    * implementation)
    */
   private ScopeUnit         scopeUnit;

   /** The category of the task */
   private TaskCategory      category;

   /** The bug associated to the task */
   private BugTrackerIssue   issue;

   /** The iteration containing this task */
   private Iteration         iteration;

   /**
    * Get the consumedTime
    * 
    * @return the consumedTime
    */
   public float getConsumedTime()
   {
      return consumedTime;
   }

   /**
    * Set the consumedTime
    * 
    * @param consumedTime
    *           the consumedTime to set
    */
   public void setConsumedTime(float consumedTime)
   {
      this.consumedTime = consumedTime;
   }

   /**
    * Get the remainingTime
    * 
    * @return the remainingTime
    */
   public float getRemainingTime()
   {
      return remainingTime;
   }

   /**
    * Set the remainingTime
    * 
    * @param remainingTime
    *           the remainingTime to set
    */
   public void setRemainingTime(float remainingTime)
   {
      this.remainingTime = remainingTime;
   }

   /**
    * Get the comment
    * 
    * @return the comment
    */
   public String getComment()
   {
      return comment;
   }

   /**
    * Set the comment
    * 
    * @param comment
    *           the comment to set
    */
   public void setComment(String comment)
   {
      this.comment = comment;
   }

   /**
    * Get the discipline
    * 
    * @return the discipline
    */
   public Discipline getDiscipline()
   {
      return discipline;
   }

   /**
    * Set the discipline
    * 
    * @param discipline
    *           the discipline to set
    */
   public void setDiscipline(Discipline discipline)
   {
      this.discipline = discipline;
   }

   /**
    * Get the scopeUnit
    * 
    * @return the scopeUnit
    */
   public ScopeUnit getScopeUnit()
   {
      return scopeUnit;
   }

   /**
    * Set the scopeUnit
    * 
    * @param scopeUnit
    *           the scopeUnit to set
    */
   public void setScopeUnit(ScopeUnit scopeUnit)
   {
      this.scopeUnit = scopeUnit;
   }

   /**
    * Get the category
    * 
    * @return the category
    */
   public TaskCategory getCategory()
   {
      return category;
   }

   /**
    * Set the category
    * 
    * @param category
    *           the category to set
    */
   public void setCategory(TaskCategory category)
   {
      this.category = category;
   }

   /**
    * Get the issue
    * 
    * @return the issue
    */
   public BugTrackerIssue getIssue()
   {
      return issue;
   }

   /**
    * Set the issue
    * 
    * @param issue
    *           the issue to set
    */
   public void setIssue(BugTrackerIssue issue)
   {
      this.issue = issue;
   }

   /**
    * Get the iteration
    * 
    * @return the iteration
    */
   public Iteration getIteration()
   {
      return iteration;
   }

   /**
    * Set the iteration
    * 
    * @param iteration
    *           the iteration to set
    */
   public void setIteration(Iteration iteration)
   {
      this.iteration = iteration;
   }

   @Override
   public String toString()
   {
      return "Task [initialEstimation=" + getInitialEstimation() + ", consumedTime=" + consumedTime + ", remainingTime="
                 + remainingTime + ", comment=" + comment + ", startDate=" + getStartDate() + ", endDate="
                 + getEndDate() + ", discipline=" + discipline + ", scopeUnit=" + scopeUnit + ", category=" + category
                 + ", issue=" + issue + ", iteration=" + iteration + ", status=" + getStatus() + ", getId()=" + getId()
                 + ", getTitle()=" + getTitle() + ", getDescription()=" + getDescription() + ", getType()=" + getType()
                 + "]";
   }

}
