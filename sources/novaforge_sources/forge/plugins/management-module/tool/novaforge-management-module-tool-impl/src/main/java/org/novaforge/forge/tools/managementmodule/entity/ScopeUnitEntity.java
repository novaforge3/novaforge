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

import org.novaforge.forge.tools.managementmodule.domain.Estimation;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.Task;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author fdemange
 */
@Entity
@Table(name = "scope_unit")
@NamedQueries({
		@NamedQuery(name = "ScopeUnitEntity.findByUnitId", query = "SELECT s FROM ScopeUnitEntity s WHERE s.unitId = :unitId"),
		@NamedQuery(name = "ScopeUnitEntity.findByRefVersion", query = "SELECT s FROM ScopeUnitEntity s WHERE s.refScopeUnit.unitId = :unitId AND s.refScopeUnit.version = :version AND s.lot.projectPlan.id = :projectPlanId"),
		@NamedQuery(name = "ScopeUnitEntity.findByRefUnitId", query = "SELECT s FROM ScopeUnitEntity s WHERE s.refScopeUnit.unitId = :unitId"),
		@NamedQuery(name = "ScopeUnitEntity.findByProjectPlanId", query = "SELECT s FROM ScopeUnitEntity s WHERE s.lot.projectPlan.id = :projectPlanId") })
public class ScopeUnitEntity implements ScopeUnit, Serializable
{
	/**
    * 
    */
	private static final long serialVersionUID = -3448756519920734070L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "unit_id", nullable = false, unique = true)
	private String						unitId;

	@Column(name = "name", nullable = false, unique = false)
	private String						name;

	@Column(name = "description", nullable = true, length = 2000)
	private String						description;

	@Column(name = "version", nullable = true, unique = false)
	private String						version;

	@Column(name = "date", nullable = false)
	private Date							date;

	@ManyToOne(targetEntity = ScopeTypeEntity.class)
	private ScopeType				 type;

	@Column(name = "isManual", nullable = false)
	private boolean					 isManual;

	@Column(name = "isInScope", nullable = false)
	private boolean					 isInScope;

	@Column(name = "isFinished", nullable = false)
	private boolean					 isFinished;

	@ManyToOne(targetEntity = LotEntity.class, optional = false)
	private Lot							 lot;

	@OneToOne(cascade = CascadeType.ALL, targetEntity = EstimationEntity.class, orphanRemoval = true)
	private Estimation				estimation;

	@ManyToOne(targetEntity = ScopeUnitEntity.class, cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.MERGE} /*, cascade = CascadeType.ALL*/)
	private ScopeUnit				 parentScopeUnit;

	@OneToMany(mappedBy = "parentScopeUnit", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = ScopeUnitEntity.class, orphanRemoval = false)
	private List<ScopeUnit>	 childscopeunit	 = new ArrayList<ScopeUnit>();

	@OneToMany(mappedBy = "scopeUnit", cascade = CascadeType.ALL, targetEntity = TaskEntity.class, orphanRemoval = true)
	private Set<Task>				 tasks						= new HashSet<Task>();

	@ManyToOne(targetEntity = RefScopeUnitEntity.class)
	private RefScopeUnit			refScopeUnit;

	@Override
	public String getUnitId()
	{
		return unitId;
	}

	@Override
	public void setUnitId(final String unitId)
	{
		this.unitId = unitId;
	}

	@Override
	@Size(max = 255)
	public String getName()
	{
		return name;
	}

	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	@Override
	@Size(max = 255)
	public String getDescription()
	{
		return description;
	}

	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	@Override
	@Size(max = 25)
	public String getVersion()
	{
		return version;
	}

	@Override
	public void setVersion(final String version)
	{
		this.version = version;
	}

	@Override
	public Date getDate()
	{
		return date;
	}

	@Override
	public void setDate(final Date date)
	{
		this.date = date;
	}

	@Override
	public ScopeType getType()
	{
		return type;
	}

	@Override
	public void setType(final ScopeType type)
	{
		this.type = type;
	}

	@Override
	public boolean isManual()
	{
		return isManual;
	}

	@Override
	public void setManual(final boolean isManual)
	{
		this.isManual = isManual;
	}

	@Override
	public boolean isInScope()
	{
		return isInScope;
	}

	@Override
	public void setInScope(final boolean isInScope)
	{
		this.isInScope = isInScope;
	}

	@Override
	public boolean isFinished()
	{
		return isFinished;
	}

	@Override
	public void setIsFinished(final boolean isFinished)
	{
		this.isFinished = isFinished;
	}

	@Override
	public Lot getLot()
	{
		return lot;
	}

	@Override
	public void setLot(final Lot lot)
	{
		this.lot = lot;
	}

	@Override
	public Estimation getEstimation()
	{
		return estimation;
	}

	@Override
	public void setEstimation(final Estimation estimation)
	{
		this.estimation = estimation;
	}

	@Override
	public List<ScopeUnit> getChildscopeunit()
	{
		return childscopeunit;
	}

	@Override
	public void setChildscopeunit(final List<ScopeUnit> childscopeunit)
	{
		this.childscopeunit = childscopeunit;
	}

	@Override
	public Set<Task> getTasks()
	{
		Set<Task> newList = new HashSet<Task>(tasks);
		tasks.clear();
		tasks.addAll(newList);
		return tasks;
	}

	@Override
	public void setTasks(final Set<Task> tasks)
	{
		this.tasks = tasks;
	}

	@Override
	public RefScopeUnit getRefScopeUnit()
	{
		return refScopeUnit;
	}

	@Override
	public void setRefScopeUnit(final RefScopeUnit refScopeUnit)
	{
		this.refScopeUnit = refScopeUnit;
	}

	@Override
	public ScopeUnit getParentScopeUnit()
	{
		return parentScopeUnit;
	}

	@Override
	public void setParentScopeUnit(final ScopeUnit parentScopeUnit)
	{
		this.parentScopeUnit = parentScopeUnit;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + (isInScope ? 1231 : 1237);
		result = prime * result + (isManual ? 1231 : 1237);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
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
		ScopeUnitEntity other = (ScopeUnitEntity) obj;
		if (date == null)
		{
			if (other.date != null)
			{
				return false;
			}
		}
		else if (!date.equals(other.date))
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
		if (isInScope != other.isInScope)
		{
			return false;
		}
		if (isManual != other.isManual)
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
		if (type == null)
		{
			if (other.type != null)
			{
				return false;
			}
		}
		else if (!type.equals(other.type))
		{
			return false;
		}
		if (unitId == null)
		{
			if (other.unitId != null)
			{
				return false;
			}
		}
		else if (!unitId.equals(other.unitId))
		{
			return false;
		}
		if (version == null)
		{
			if (other.version != null)
			{
				return false;
			}
		}
		else if (!version.equals(other.version))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "ScopeUnitEntity [id=" + id + ", unitId=" + unitId + ", name=" + name + ", version=" + version
				+ ", date=" + date + ", type=" + type + ", isManual=" + isManual + ", isInScope=" + isInScope
				+ ", isFinished=" + isFinished + ", lot=" + lot + ", estimation=" + estimation + ", refScopeUnit="
				+ refScopeUnit + "]";
	}

}
