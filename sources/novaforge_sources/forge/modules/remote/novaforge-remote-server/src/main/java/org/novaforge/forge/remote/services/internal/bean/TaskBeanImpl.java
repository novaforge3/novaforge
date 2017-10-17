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
package org.novaforge.forge.remote.services.internal.bean;

import org.novaforge.forge.core.plugins.categories.management.DisciplineBean;
import org.novaforge.forge.core.plugins.categories.management.IssueBean;
import org.novaforge.forge.core.plugins.categories.management.IterationBean;
import org.novaforge.forge.core.plugins.categories.management.ScopeUnitBean;
import org.novaforge.forge.core.plugins.categories.management.TaskBean;
import org.novaforge.forge.core.plugins.categories.management.TaskCategoryBean;
import org.novaforge.forge.core.plugins.categories.management.TaskStatusEnum;
import org.novaforge.forge.core.plugins.categories.management.TaskTypeEnum;

import java.util.Date;

/**
 * Locale implementation of TaskBean
 * 
 * @author rols-p
 */
public class TaskBeanImpl implements TaskBean
{

  private static final long serialVersionUID = 8585880629716971617L;

  /** The identifier of the task */
  private String            id;

  /** The title of the task */
  private String            title;

  /** Description of the task */
  private String            description;

  /** Initial estimation in days (planning poker estimation in scrum projects) */
  private float             initialEstimation;

  /** The time consumed on this task (in days) */
  private float             consumedTime;

  /**
   * The time remaining to complete this task (base on realizer estimation, not the difference between
   * initial one and consumed time)
   */
  private float             remainingTime;

  /** An eventual comment */
  private String            comment;

  /** The start date of the task */
  private Date              startDate;

  /** The end date of the task */
  private Date              endDate;

  /** The date of the last update on this task */
  private Date              lastUpdateDate;

  /** The type of the task (work or bug) */
  private TaskTypeEnum      type;

  /** The discipline of the task (implementation, receipt, project management...) */
  private DisciplineBean    discipline;

  /**
   * The scope unit (use case, user story...) of the task - can be null (depends of taskType and
   * implementation)
   */
  private ScopeUnitBean     scopeUnit;

  /** The category of the task */
  private TaskCategoryBean  category;

  /** The bug associated to the task */
  private IssueBean         issue;

  /** The iteration containing this task */
  private IterationBean     iteration;

  /** The status of the task */
  private TaskStatusEnum    status;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setId(final String id)
  {
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTitle()
  {
    return title;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTitle(final String title)
  {
    this.title = title;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDescription(final String description)
  {
    this.description = description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TaskTypeEnum getType()
  {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setType(final TaskTypeEnum type)
  {
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getLastUpdateDate()
  {
    return lastUpdateDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLastUpdateDate(final Date lastUpdateDate)
  {
    this.lastUpdateDate = lastUpdateDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TaskStatusEnum getStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final TaskStatusEnum status)
  {
    this.status = status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getStartDate()
  {
    return startDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStartDate(final Date startDate)
  {
    this.startDate = startDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getEndDate()
  {
    return endDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEndDate(final Date endDate)
  {
    this.endDate = endDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public float getInitialEstimation()
  {
    return initialEstimation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInitialEstimation(final float initialEstimation)
  {
    this.initialEstimation = initialEstimation;
  }

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
  public void setConsumedTime(final float consumedTime)
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
  public void setRemainingTime(final float remainingTime)
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
  public void setComment(final String comment)
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
  public void setDiscipline(final DisciplineBean discipline)
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
   *          the scopeUnit to set
   */
  @Override
  public void setScopeUnit(final ScopeUnitBean scopeUnit)
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
  public void setCategory(final TaskCategoryBean category)
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
  public void setIssue(final IssueBean issue)
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
  public void setIteration(final IterationBean iteration)
  {
    this.iteration = iteration;
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((id == null) ? 0 : id.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj)
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
    final TaskBeanImpl other = (TaskBeanImpl) obj;
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
    return "TaskBeanImpl [id=" + id + ", title=" + title + ", description=" + description + ", initialEstimation="
               + initialEstimation + ", consumedTime=" + consumedTime + ", remainingTime=" + remainingTime
               + ", comment=" + comment + ", startDate=" + startDate + ", endDate=" + endDate + ", lastUpdateDate="
               + lastUpdateDate + ", type=" + type + ", discipline=" + discipline + ", scopeUnit=" + scopeUnit
               + ", category=" + category + ", issue=" + issue + ", iteration=" + iteration + ", status=" + status
               + "]";
  }

}
