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

import org.novaforge.forge.core.plugins.categories.management.DisciplineBean;
import org.novaforge.forge.core.plugins.categories.management.IssueBean;
import org.novaforge.forge.core.plugins.categories.management.IterationBean;
import org.novaforge.forge.core.plugins.categories.management.ScopeUnitBean;
import org.novaforge.forge.core.plugins.categories.management.TaskBean;
import org.novaforge.forge.core.plugins.categories.management.TaskCategoryBean;

/**
 * Locale implementation of TaskBean
 */
public class TaskBeanImpl extends TaskInfoBeanImpl implements TaskBean
{

   private static final long serialVersionUID = 8585880629716971617L;

   /** The time consumed on this task (in days) */
   private float            consumedTime;

   /**
    * The time remaining to complete this task (base on realizer estimation, not the difference between
    * initial one and consumed time)
    */
   private float            remainingTime;

   /** An eventual comment */
   private String           comment;

   /** The discipline of the task (implementation, receipt, project management...) */
   private DisciplineBean   discipline;

   /**
    * The scope unit (use case, user story...) of the task - can be null (depends of taskType and
    * implementation)
    */
   private ScopeUnitBean    scopeUnit;

   /** The category of the task */
   private TaskCategoryBean category;

   /** The bug associated to the task */
   private IssueBean        issue;

   /** The iteration containing this task */
   private IterationBean    iteration;

   /**
    * {@inheritDoc}
    */
   @Override
   public float getConsumedTime()
   {
      return consumedTime;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setConsumedTime(float consumedTime)
   {
      this.consumedTime = consumedTime;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public float getRemainingTime()
   {
      return remainingTime;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setRemainingTime(float remainingTime)
   {
      this.remainingTime = remainingTime;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getComment()
   {
      return comment;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setComment(String comment)
   {
      this.comment = comment;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public DisciplineBean getDiscipline()
   {
      return discipline;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setDiscipline(DisciplineBean discipline)
   {
      this.discipline = discipline;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public ScopeUnitBean getScopeUnit()
   {
      return scopeUnit;
   }

   /**
    * Set the scopeUnit
    * 
    * @param scopeUnit
    *           the scopeUnit to set
    */
   @Override
   public void setScopeUnit(ScopeUnitBean scopeUnit)
   {
      this.scopeUnit = scopeUnit;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public TaskCategoryBean getCategory()
   {
      return category;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setCategory(TaskCategoryBean category)
   {
      this.category = category;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IssueBean getIssue()
   {
      return issue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setIssue(IssueBean issue)
   {
      this.issue = issue;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public IterationBean getIteration()
   {
      return iteration;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void setIteration(IterationBean iteration)
   {
      this.iteration = iteration;
   }

   @Override
   public int hashCode()
   {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
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
      TaskBeanImpl other = (TaskBeanImpl) obj;
      if (getId() == null)
      {
         if (other.getId() != null)
         {
            return false;
         }
      }
      else if (!getId().equals(other.getId()))
      {
         return false;
      }
      return true;
   }

   @Override
   public String toString()
   {
      return "TaskBeanImpl [TaskInfoBeanImpl=" + super.toString() + ", consumedTime=" + consumedTime
                 + ", remainingTime=" + remainingTime + ", comment=" + comment + ", discipline=" + discipline
                 + ", scopeUnit=" + scopeUnit + ", category=" + category + ", issue=" + issue + ", iteration="
                 + iteration + "]";
   }

}
