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
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;

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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author fdemange
 * @author BILET-JC
 */
@Entity
@Table(name = "iteration")
@NamedQueries({
		@NamedQuery(name = "IterationEntity.getIterationsList", query = "SELECT p FROM IterationEntity p WHERE p.lot.projectPlan.id = :projectPlanId "),
		@NamedQuery(name = "IterationEntity.getTimeIndicators", query = "SELECT SUM(t.initialEstimation) AS estimed, SUM(itask.consumedTime) AS consumed, SUM(itask.remainingTime) AS remaining "
				+ "FROM IterationEntity AS it "
				+ "INNER JOIN it.iterationTasks AS itask "
				+ "INNER JOIN itask.task AS t " + "WHERE itask.status.id = :statusId " + "AND it.id = :iterationId "),
		@NamedQuery(name = "IterationEntity.findOlderNotFinished", query = "SELECT p FROM IterationEntity p WHERE p.isFinished = 0 ORDER BY p.endDate ASC") })
public class IterationEntity implements Iteration, Serializable
{
	/**
	 * 
	 */
	private static final long	serialVersionUID = -3448756519920734070L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							 id;

	@Column(name = "label", nullable = false)
	private String						 label;

	@Column(name = "numIteration", nullable = false)
	private int								numIteration;

	@Column(name = "start_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date							 startDate;

	@Column(name = "end_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date							 endDate;

	@Column(name = "is_finished", nullable = false)
	private boolean						isFinished;

	@ManyToOne(targetEntity = LotEntity.class )
	@JoinColumn(name = "LOT_ID", referencedColumnName = "id", nullable = false)
	private Lot								lot;

	@ManyToOne(targetEntity = PhaseTypeEntity.class)
	private PhaseType					phaseType;

	@OneToMany(mappedBy = "iteration", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = IterationTaskEntity.class, orphanRemoval = true)
	private Set<IterationTask> iterationTasks	 = new HashSet<IterationTask>();

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
		result = prime * result + ((lot == null) ? 0 : lot.hashCode());
		result = prime * result + ((phaseType == null) ? 0 : phaseType.hashCode());
		result = prime * result + numIteration;
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
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
		IterationEntity other = (IterationEntity) obj;
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
		if (label == null)
		{
			if (other.label != null)
			{
				return false;
			}
		}
		else if (!label.equals(other.label))
		{
			return false;
		}
		if (lot == null)
		{
			if (other.lot != null)
			{
				return false;
			}
		}
		else if (!lot.equals(other.lot))
		{
			return false;
		}
		if (phaseType == null)
		{
			if (other.phaseType != null)
			{
				return false;
			}
		}
		else if (!phaseType.equals(other.phaseType))
		{
			return false;
		}
		if (numIteration != other.numIteration)
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
		return true;
	}

	@Override
	public String toString()
	{
		return "IterationEntity [id=" + id + ", label=" + label + ", numIteration=" + numIteration
				+ ", startDate=" + startDate + ", endDate=" + endDate + ", isFinished=" + isFinished + ", lotId="
				+ lot.getId() + ", phaseType=" + phaseType + "]";
	}

	@Override
	public int compareTo(final Iteration other)
	{
		if (other == null)
		{
			return -1;
		}
		if (getStartDate() == null)
		{
			if (other.getStartDate() == null)
			{
				return 0;
			}
			else
			{
				return 1;
			}
		}
		return getStartDate().compareTo(other.getStartDate());
	}

	@Override
	public Date getStartDate()
	{
		return startDate;
	}

	@Override
	public void setStartDate(final Date startDate)
	{
		this.startDate = startDate;
	}

	@Override
	public Date getEndDate()
	{
		return endDate;
	}

	@Override
	public void setEndDate(final Date endDate)
	{
		this.endDate = endDate;
	}

	@Override
	public boolean isFinished()
	{
		return isFinished;
	}

	@Override
	public void setFinished(final boolean isFinished)
	{
		this.isFinished = isFinished;
	}

	@Override
	public Lot getLot()
	{
		return lot;
	}

	@Override
	public void setLot(final Lot my_lot)
	{
		this.lot = my_lot;
	}

	@Override
	public Set<IterationTask> getIterationTasks()
	{
		Set<IterationTask> newList = new HashSet<IterationTask>(iterationTasks);
		iterationTasks.clear();
		iterationTasks.addAll(newList);
		return iterationTasks;
	}

	@Override
	public void setIterationTasks(final Set<IterationTask> iterationTasks)
	{

		this.iterationTasks = iterationTasks;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addIterationTask(final IterationTask task)
	{
		this.iterationTasks.add(task);
		IterationTaskEntity taskEntity = (IterationTaskEntity) task;
		taskEntity.setIteration(this);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeIterationTask(final IterationTask task)
	{
		this.iterationTasks.remove(task);

	}

	@Override
	public String getLabel()
	{
		return label;
	}

	@Override
	public void setLabel(final String label)
	{
		this.label = label;
	}

	@Override
	public int getNumIteration()
	{
		return numIteration;
	}

	@Override
	public void setNumIteration(final int numIteration)
	{
		this.numIteration = numIteration;
	}

	@Override
	public PhaseType getPhaseType()
	{
		return phaseType;
	}

	@Override
	public void setPhaseType(final PhaseType phaseType)
	{
		this.phaseType = phaseType;
	}

	@Override
	public Long getId()
	{
		return id;
	}

}
