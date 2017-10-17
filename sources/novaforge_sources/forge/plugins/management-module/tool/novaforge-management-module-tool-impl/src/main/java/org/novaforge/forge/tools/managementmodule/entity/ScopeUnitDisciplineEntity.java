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

import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDisciplineStatus;

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
 * JPA implementation of a ScopeUnitDiscipline
 */
@Entity
@Table(name = "scopeunit_discipline", uniqueConstraints = @UniqueConstraint(columnNames = { "scope_unit_id",
		"discipline_id" }))
@NamedQueries({
		@NamedQuery(name = "ScopeUnitDisciplineEntity.findByScopeAndDiscipline", query = "SELECT s FROM ScopeUnitDisciplineEntity s WHERE s.scopeUnit.id = :scopeId and s.discipline.functionalId = :disciplineFunctionalId"),
		@NamedQuery(name = "ScopeUnitDisciplineEntity.findByScopeUnit", query = "SELECT s FROM ScopeUnitDisciplineEntity s WHERE s.scopeUnit.id = :scopeUnitId"),
		@NamedQuery(name = "ScopeUnitDisciplineEntity.findByScopeUnitIds", query = "SELECT s FROM ScopeUnitDisciplineEntity s WHERE s.scopeUnit.id in (:scopeUnitIds)"),
		@NamedQuery(name = "ScopeUnitDisciplineEntity.deleteByProjectPlanId", query = "DELETE FROM ScopeUnitDisciplineEntity scu WHERE scu.scopeUnit.id IN (SELECT s.id FROM ScopeUnitEntity s WHERE s.lot.projectPlan.id = :projectPlanId)") })
public class ScopeUnitDisciplineEntity implements Serializable, ScopeUnitDiscipline
{

	/** UID for serialization */
	private static final long				 serialVersionUID = -6558902854646181637L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long											id;

	@ManyToOne(targetEntity = ScopeUnitEntity.class)
	@JoinColumn(name = "scope_unit_id", referencedColumnName = "id", nullable = false)
	private ScopeUnit								 scopeUnit;

	@ManyToOne(targetEntity = DisciplineEntity.class)
	@JoinColumn(name = "discipline_id", referencedColumnName = "id", nullable = false)
	private Discipline								discipline;

	@ManyToOne(targetEntity = ScopeUnitDisciplineStatusEntity.class)
	@JoinColumn(name = "status_id", referencedColumnName = "id", nullable = false)
	private ScopeUnitDisciplineStatus status;

	@Column(name = "status_updated_date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date											statusUpdatedDate;

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public ScopeUnit getScopeUnit()
	{
		return scopeUnit;
	}

	@Override
	public void setScopeUnit(final ScopeUnit scopeUnit)
	{
		this.scopeUnit = scopeUnit;
	}

	@Override
	public Discipline getDiscipline()
	{
		return discipline;
	}

	@Override
	public void setDiscipline(final Discipline discipline)
	{
		this.discipline = discipline;
	}

	@Override
	public ScopeUnitDisciplineStatus getStatus()
	{
		return status;
	}

	@Override
	public void setStatus(final ScopeUnitDisciplineStatus status)
	{
		this.status = status;
	}

	@Override
	public Date getStatusUpdatedDate()
	{
		return statusUpdatedDate;
	}

	@Override
	public void setStatusUpdatedDate(final Date statusUpdatedDate)
	{
		this.statusUpdatedDate = statusUpdatedDate;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discipline == null) ? 0 : discipline.hashCode());
		result = prime * result
				+ ((scopeUnit == null || scopeUnit.getId() == null) ? 0 : scopeUnit.getId().hashCode());
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
		ScopeUnitDisciplineEntity other = (ScopeUnitDisciplineEntity) obj;
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
		if (scopeUnit == null || other.scopeUnit == null)
		{
			if (other.scopeUnit != scopeUnit)
			{
				return false;
			}
		}
		else if (scopeUnit.getId() == null)
		{
			if (other.scopeUnit.getId() != null)
			{
				return false;
			}
		}
		else if (!scopeUnit.getId().equals(other.scopeUnit.getId()))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "ScopeUnitDisciplineEntity [id=" + id + ", scopeUnit=" + scopeUnit + ", discipline=" + discipline
				+ ", status=" + status + ", statusUpdatedDate=" + statusUpdatedDate + "]";
	}

}
