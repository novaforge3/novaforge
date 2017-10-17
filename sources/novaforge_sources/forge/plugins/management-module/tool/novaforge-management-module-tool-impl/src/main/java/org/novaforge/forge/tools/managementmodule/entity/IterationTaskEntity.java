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

import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.IterationTask;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.domain.Task;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fdemange
 */
@Entity
@Table(name = "iteration_task", uniqueConstraints = @UniqueConstraint(columnNames = { "iteration_id",
		"task_id" }))
@NamedQueries({ @NamedQuery(name = "IterationTaskEntity.deleteByProjectPlanId", query = "DELETE FROM IterationTaskEntity ite WHERE ite.iteration.id IN (SELECT it.id FROM IterationEntity it WHERE it.lot.projectPlan.id = :projectPlanId)") })
public class IterationTaskEntity implements IterationTask, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3448756519920734070L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@ManyToOne(targetEntity = IterationEntity.class)
	@JoinColumn(name = "iteration_id", referencedColumnName = "id", nullable = false)
	private Iteration				 iteration;

	@ManyToOne(targetEntity = TaskEntity.class)
	@JoinColumn(name = "task_id", referencedColumnName = "id", nullable = false)
	private Task							task;

	@Column(name = "last_update_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date							lastUpdateDate;

	@ManyToOne(targetEntity = StatusTaskEntity.class)
	@JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
	private StatusTask				status;

	@Column(name = "consumed_time", nullable = false)
	private float						 consumedTime;

	@Column(name = "remaining_time", nullable = false)
	private float						 remainingTime;

	@Override
	public Iteration getIteration()
	{
		return iteration;
	}

	@Override
	public void setIteration(final Iteration iteration)
	{
		this.iteration = iteration;
	}

	@Override
	public Task getTask()
	{
		return task;
	}

	@Override
	public void setTask(final Task task)
	{
		this.task = task;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public StatusTask getStatus()
	{
		return status;
	}

	@Override
	public void setStatus(final StatusTask status)
	{
		this.status = status;
	}

	@Override
	public Date getLastUpdateDate()
	{
		return lastUpdateDate;
	}

	@Override
	public void setLastUpdateDate(final Date lastUpdateDate)
	{
		this.lastUpdateDate = lastUpdateDate;
	}

	@Override
	public float getConsumedTime()
	{
		return consumedTime;
	}

	@Override
	public void setConsumedTime(final float consumedTime)
	{
		this.consumedTime = consumedTime;
	}

	@Override
	public float getRemainingTime()
	{
		return remainingTime;
	}

	@Override
	public void setRemainingTime(final float remainingTime)
	{
		this.remainingTime = remainingTime;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((iteration == null || iteration.getId() == null) ? 0 : iteration.getId().hashCode());
		result = prime * result + ((task == null || task.getId() == null) ? 0 : task.getId().hashCode());
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
		IterationTaskEntity other = (IterationTaskEntity) obj;
		if (iteration == null || other.iteration == null)
		{
			if (other.iteration != iteration)
			{
				return false;
			}
		}
		else if (iteration.getId() == null)
		{
			if (other.iteration.getId() != null)
			{
				return false;
			}
		}
		else if (!iteration.getId().equals(other.iteration.getId()))
		{
			return false;
		}
		if (task == null || other.task == null)
		{
			if (other.task != task)
			{
				return false;
			}
		}
		else if (task.getId() == null)
		{
			if (other.task.getId() != null)
			{
				return false;
			}
		}
		else if (!task.getId().equals(other.task.getId()))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "IterationTaskEntity [id=" + id + ", iterationId=" + iteration.getId() + ", taskId="
				+ task.getId() + ", lastUpdateDate=" + lastUpdateDate + ", statusId=" + status.getId()
				+ ", consumedTime=" + consumedTime + ", remainingTime=" + remainingTime + "]";
	}

}
