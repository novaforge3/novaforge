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
package org.novaforge.forge.tools.managementmodule.entity;

import org.novaforge.forge.tools.managementmodule.domain.Bug;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.TaskType;
import org.novaforge.forge.tools.managementmodule.domain.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * JPA implementation of a Task
 */
@Entity
@Table(name = "task")
@NamedQueries({
		@NamedQuery(name = "TaskEntity.findByScopeUnitId", query = "SELECT t FROM TaskEntity t WHERE t.scopeUnit.id = :scopeUnitId"),
		@NamedQuery(name = "TaskEntity.findByScopeUnitIdAndDisciplineId", query = "SELECT t FROM TaskEntity t WHERE t.scopeUnit.id = :scopeUnitId AND t.discipline.id = :disciplineId"),
		@NamedQuery(name = "TaskEntity.deleteByProjectPlanId", query = "DELETE FROM TaskEntity task WHERE task.scopeUnit.id IN (SELECT s.id FROM ScopeUnitEntity s WHERE s.lot.projectPlan.id = :projectPlanId)"),
		@NamedQuery(name = "TaskEntity.findByTaskCategoryId", query = "SELECT t FROM TaskEntity t WHERE t.taskCategory.id = :taskCategoryId")})
public class TaskEntity implements Task, Serializable
{

	/** UID for serialization */
	private static final long	serialVersionUID = -3448756519920734070L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							 id;

	@Column(name = "name", nullable = false, unique = false)
	private String						 name;

	@ManyToOne(targetEntity = StatusTaskEntity.class)
	@JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
	private StatusTask				 status;

	@ManyToOne(targetEntity = UserEntity.class)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = true)
	private User							 user;

	@OneToMany(mappedBy = "task", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = IterationTaskEntity.class)
	private Set<IterationTask> iterationsTasks	= new HashSet<IterationTask>();

	@Column(name = "initial_estimation", nullable = false)
	private float							initialEstimation;

	/** The type of the task : work or bug */
	@ManyToOne(targetEntity = TaskTypeEntity.class)
	@JoinColumn(name = "type_id", referencedColumnName = "id", nullable = false)
	private TaskType					 taskType;

	@ManyToOne(targetEntity = DisciplineEntity.class)
	@JoinColumn(name = "discipline_id", referencedColumnName = "id", nullable = false)
	private Discipline				 discipline;

	@ManyToOne(targetEntity = ScopeUnitEntity.class)
	@JoinColumn(name = "scope_unit_id", referencedColumnName = "id", nullable = true)
	private ScopeUnit					scopeUnit;

	@ManyToOne(targetEntity = TaskCategoryEntity.class)
	@JoinColumn(name = "category_id", referencedColumnName = "id", nullable = true)
	private TaskCategory			 taskCategory;

	@OneToOne(targetEntity = BugEntity.class)
	@JoinColumn(name = "bug_id", referencedColumnName = "id", nullable = true)
	private Bug								bug;

	@Column(name = "start_date", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date							 startDate;

	@Column(name = "end_date", nullable = true)
	@Temporal(TemporalType.DATE)
	private Date							 endDate;

	@Column(name = "description", nullable = true, length = 2000)
	private String						 description;

	@Column(name = "comment", nullable = true)
	private String						 comment;

	@Column(name = "last_update_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date							 lastUpdateDate;

	@Transient
	private Float							consumedTime;

	@Transient
	private Float							remainingTime;

	@Transient
	private Iteration					currentIteration;

	/**
	 * Get the initialEstimation
	 *
	 * @return the initialEstimation
	 */
	@Override
	public float getInitialEstimation()
	{
		return initialEstimation;
	}

	/**
	 * Set the initialEstimation
	 *
	 * @param initialEstimation
	 *     the initialEstimation to set
	 */
	@Override
	public void setInitialEstimation(final float initialEstimation)
	{
		this.initialEstimation = initialEstimation;
	}

	/**
	 * Get the id
	 *
	 * @return the id
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * Get the name
	 *
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Set the name
	 *
	 * @param name
	 *          the name to set
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Get the status
	 *
	 * @return the status
	 */
	@Override
	public StatusTask getStatus()
	{
		return status;
	}

	/**
	 * Set the status
	 *
	 * @param status
	 *          the status to set
	 */
	@Override
	public void setStatus(final StatusTask status)
	{
		this.status = status;
	}

	/**
	 * Get the user
	 *
	 * @return the user
	 */
	@Override
	public User getUser()
	{
		return user;
	}

	/**
	 * Set the user
	 *
	 * @param user
	 *          the user to set
	 */
	@Override
	public void setUser(final User user)
	{
		this.user = user;
	}

	/**
	 * Get the iterationsTasks
	 *
	 * @return the iterationsTasks
	 */
	@Override
	public Set<IterationTask> getIterationsTasks()
	{
		Set<IterationTask> newList = new HashSet<IterationTask>(iterationsTasks);
		iterationsTasks.clear();
		iterationsTasks.addAll(newList);
		return iterationsTasks;
	}

	/**
	 * Set the iterationsTasks
	 *
	 * @param iterationsTasks
	 *          the iterationsTasks to set
	 */
	@Override
	public void setIterationsTasks(final Set<IterationTask> iterationsTasks)
	{
		this.iterationsTasks = iterationsTasks;
	}

	/**
	 * Get the discipline
	 * 
	 * @return the discipline
	 */
	@Override
	public Discipline getDiscipline()
	{
		return discipline;
	}

	/**
	 * Set the discipline
	 * 
	 * @param discipline
	 *          the discipline to set
	 */
	@Override
	public void setDiscipline(final Discipline discipline)
	{
		this.discipline = discipline;
	}

	/**
	 * Get the scopeUnit
	 * 
	 * @return the scopeUnit
	 */
	@Override
	public ScopeUnit getScopeUnit()
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
	public void setScopeUnit(final ScopeUnit scopeUnit)
	{
		this.scopeUnit = scopeUnit;
	}

	/**
	 * @return the taskCategory
	 */
	@Override
	public TaskCategory getTaskCategory()
	{
		return taskCategory;
	}

	/**
	 * @param taskCategory
	 *          the taskCategory to set
	 */
	@Override
	public void setTaskCategory(final TaskCategory taskCategory)
	{
		this.taskCategory = taskCategory;
	}

	/**
	 * @return the bug
	 */
	@Override
	public Bug getBug()
	{
		return bug;
	}

	/**
	 * @param bug
	 *          the bug to set
	 */
	@Override
	public void setBug(final Bug bug)
	{
		this.bug = bug;
	}

	/**
	 * @return the startDate
	 */
	@Override
	public Date getStartDate()
	{
		return startDate;
	}

	/**
	 * @param startDate
	 *          the startDate to set
	 */
	@Override
	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	@Override
	public Date getEndDate()
	{
		return endDate;
	}

	/**
	 * @param endDate
	 *          the endDate to set
	 */
	@Override
	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

	/**
	 * @return the description
	 */
	@Override
	public String getDescription()
	{
		return description;
	}

	/**
	 * @param description
	 *          the description to set
	 */
	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	/**
	 * @return the comment
	 */
	@Override
	public String getComment()
	{
		return comment;
	}

	/**
	 * @param comment
	 *          the comment to set
	 */
	@Override
	public void setComment(final String comment)
	{
		this.comment = comment;
	}

	/**
	 * @return the lastUpdateDate
	 */
	@Override
	public Date getLastUpdateDate()
	{
		return lastUpdateDate;
	}

	/**
	 * @param lastUpdateDate
	 *          the lastUpdateDate to set
	 */
	@Override
	public void setLastUpdateDate(final Date lastUpdateDate)
	{
		this.lastUpdateDate = lastUpdateDate;
	}

	/**
	 * Get the taskType
	 * 
	 * @return the taskType
	 */
	@Override
	public TaskType getTaskType()
	{
		return taskType;
	}

	/**
	 * Set the taskType
	 * 
	 * @param taskType
	 *          the taskType to set
	 */
	@Override
	public void setTaskType(final TaskType taskType)
	{
		this.taskType = taskType;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bug == null) ? 0 : bug.hashCode());
		result = prime * result + ((comment == null) ? 0 : comment.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((discipline == null) ? 0 : discipline.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + Float.floatToIntBits(initialEstimation);
		result = prime * result + ((lastUpdateDate == null) ? 0 : lastUpdateDate.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((scopeUnit == null) ? 0 : scopeUnit.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((taskCategory == null) ? 0 : taskCategory.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/**
	 * Get the consumedTime
	 *
	 * @return the consumedTime
	 */
	@Override
	public float getConsumedTime()
	{
		if (consumedTime == null)
		{
			throw new IllegalStateException("Illegal state : you try to access a business / non persistent property without making business initialization");
		}
		return consumedTime;
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
		TaskEntity other = (TaskEntity) obj;
		if (bug == null)
		{
			if (other.bug != null)
			{
				return false;
			}
		}
		else if (!bug.equals(other.bug))
		{
			return false;
		}
		if (comment == null)
		{
			if (other.comment != null)
			{
				return false;
			}
		}
		else if (!comment.equals(other.comment))
		{
			return false;
		}
		if (description == null)
		{
			if (other.description != null)
			{
				return false;
			}
		}
		else if (!description.equals(other.description))
		{
			return false;
		}
		if (discipline == null)
		{
			if (other.discipline != null)
			{
				return false;
			}
		}
		else if (!discipline.equals(other.discipline))
		{
			return false;
		}
		if (endDate == null)
		{
			if (other.endDate != null)
			{
				return false;
			}
		}
		else if (!endDate.equals(other.endDate))
		{
			return false;
		}
		if (Float.floatToIntBits(initialEstimation) != Float.floatToIntBits(other.initialEstimation))
		{
			return false;
		}
		if (lastUpdateDate == null)
		{
			if (other.lastUpdateDate != null)
			{
				return false;
			}
		}
		else if (!lastUpdateDate.equals(other.lastUpdateDate))
		{
			return false;
		}
		if (name == null)
		{
			if (other.name != null)
			{
				return false;
			}
		}
		else if (!name.equals(other.name))
		{
			return false;
		}
		if (scopeUnit == null)
		{
			if (other.scopeUnit != null)
			{
				return false;
			}
		}
		else if (!scopeUnit.equals(other.scopeUnit))
		{
			return false;
		}
		if (startDate == null)
		{
			if (other.startDate != null)
			{
				return false;
			}
		}
		else if (!startDate.equals(other.startDate))
		{
			return false;
		}
		if (status == null)
		{
			if (other.status != null)
			{
				return false;
			}
		}
		else if (!status.equals(other.status))
		{
			return false;
		}
		if (taskCategory == null)
		{
			if (other.taskCategory != null)
			{
				return false;
			}
		}
		else if (!taskCategory.equals(other.taskCategory))
		{
			return false;
		}
		if (user == null)
		{
			if (other.user != null)
			{
				return false;
			}
		}
		else if (!user.equals(other.user))
		{
			return false;
		}
		return true;
	}

	/**
	 * Set the consumedTime
	 *
	 * @param consumedTime
	 *     the consumedTime to set
	 */
	@Override
	public void setConsumedTime(final float consumedTime)
	{
		this.consumedTime = consumedTime;
	}

	/**
	 * Get the remainingTime
	 *
	 * @return the remainingTime
	 */
	@Override
	public float getRemainingTime()
	{
		if (remainingTime == null)
		{
		  throw new IllegalStateException("Illegal state : you try to access a business / non persistent property without making business initialization");
		}
		return remainingTime;
	}

	/**
	 * Set the remainingTime
	 *
	 * @param remainingTime
	 *     the remainingTime to set
	 */
	@Override
	public void setRemainingTime(final float remainingTime)
	{
		this.remainingTime = remainingTime;
	}

	/**
	 * Get the currentIteration
	 *
	 * @return the currentIteration
	 */
	@Override
	public Iteration getCurrentIteration()
	{
		if (currentIteration == null)
		{
			throw new IllegalStateException("Illegal state : you try to access a business / non persistent property without making business initialization");
		}
		return currentIteration;
	}

	/**
	 * Set the currentIteration
	 *
	 * @param currentIteration
	 *     the currentIteration to set
	 */
	@Override
	public void setCurrentIteration(final Iteration newIteration)
	{
		if (newIteration == null)
		{
			throw new IllegalStateException("Illegal state : this property must not be set to null");
		}
		this.currentIteration = newIteration;
	}





}
