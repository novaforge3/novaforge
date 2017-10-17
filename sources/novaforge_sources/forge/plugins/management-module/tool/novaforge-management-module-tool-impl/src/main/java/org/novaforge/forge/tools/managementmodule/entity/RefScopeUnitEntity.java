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

import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.domain.StatusScope;

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
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ppr
 */
@Entity
@Table(name = "ref_scope_unit")
@NamedQueries({
		@NamedQuery(name = "RefScopeUnitEntity.findByUnitId", query = "SELECT s FROM RefScopeUnitEntity s WHERE s.unitId = :unitId AND s.version = :version"),
		@NamedQuery(name = "RefScopeUnitEntity.findCompleteByUnitId", query = "SELECT s FROM RefScopeUnitEntity s JOIN FETCH s.childscopeunit WHERE s.unitId = :unitId AND s.version = :version"),
		@NamedQuery(name = "RefScopeUnitEntity.findByNameAndVersion", query = "SELECT s FROM RefScopeUnitEntity s WHERE s.name = :name AND s.version = :version"),
		@NamedQuery(name = "RefScopeUnitEntity.findLastVersion", query = "SELECT s FROM RefScopeUnitEntity s WHERE s.project.projectId = :projectId AND s.parentScopeUnit is null AND s.state.functionalId <> :stateFunctionalId AND  s.dateModified =(SELECT max(ff.dateModified) FROM RefScopeUnitEntity ff WHERE ff.project.projectId = :projectId AND ff.unitId = s.unitId)"),
		@NamedQuery(name = "RefScopeUnitEntity.findLastVersionUnitId", query = "SELECT s FROM RefScopeUnitEntity s WHERE s.unitId = :unitId AND s.parentScopeUnit is null AND s.dateModified =(SELECT max(ff.dateModified) FROM RefScopeUnitEntity ff WHERE ff.unitId = :unitId)"),
		@NamedQuery(name = "RefScopeUnitEntityFils.findLastVersionUnitId", query = "SELECT s FROM RefScopeUnitEntity s WHERE s.unitId = :unitId AND s.dateModified =(SELECT max(ff.dateModified) FROM RefScopeUnitEntity ff WHERE ff.unitId = :unitId)"),
		@NamedQuery(name = "RefScopeUnitEntity.findAllFromProjectWithChilds", query = "SELECT s FROM RefScopeUnitEntity s WHERE s.project.projectId = :projectId and s.cdoParameters.id = :idCDOParameters"),
		@NamedQuery(name = "RefScopeUnitEntity.findAllFromProject", query = "SELECT s FROM RefScopeUnitEntity s WHERE s.project.projectId = :projectId and s.cdoParameters.id = :idCDOParameters and s.parentScopeUnit is null") })
public class RefScopeUnitEntity implements RefScopeUnit, Serializable
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

	@Column(name = "name", nullable = false)
	private String						name;

	@Column(name = "description", nullable = false)
	private String						description;

	@Column(name = "version", nullable = true, unique = false)
	private String						version;

	@ManyToOne(targetEntity = StatusScopeEntity.class)
	private StatusScope			 state;

	@Column(name = "dateCreated", nullable = false)
	private Date							dateCreated;

	@Column(name = "dateModified", nullable = true)
	private Date							dateModified;

	@Column(name = "benefit", nullable = false)
	private Integer					 benefit;

	@Column(name = "injury", nullable = false)
	private Integer					 injury;

	@Column(name = "risk", nullable = false)
	private Integer					 risk;

	@ManyToOne(targetEntity = ScopeTypeEntity.class)
	private ScopeType				 type;

	@ManyToOne(targetEntity = RefScopeUnitEntity.class, fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	private RefScopeUnit			parentScopeUnit;

	@OneToMany(mappedBy = "parentScopeUnit", fetch = FetchType.EAGER, cascade = CascadeType.ALL, targetEntity = RefScopeUnitEntity.class, orphanRemoval = false)
	private Set<RefScopeUnit> childscopeunit	 = new HashSet<RefScopeUnit>();

	@ManyToOne(targetEntity = ProjectEntity.class)
	private Project					 project;

	@ManyToOne(targetEntity = CDOParametersEntity.class)
	private CDOParameters		 cdoParameters;

	public Long getId()
	{
		return id;
	}

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
	public Set<RefScopeUnit> getChildScopeUnit()
	{
		if (childscopeunit == null)
		{
		  return Collections.unmodifiableSet(new HashSet<RefScopeUnit>());
		} 
		else
		{
		  return Collections.unmodifiableSet(childscopeunit);
		}
	}

	@Override
	public void setChildScopeUnit(final Set<RefScopeUnit> childsScopeUnit)
	{
		this.childscopeunit = childsScopeUnit;

	}

	@Override
	public void addChildScopeUnit(final RefScopeUnit refScopeUnit)
	{
		this.childscopeunit.add(refScopeUnit);

	}

	@Override
	public void removeChildScopeUnit(final RefScopeUnit refScopeUnit)
	{
		this.childscopeunit.remove(refScopeUnit);
	}

	@Override
	public Project getProject()
	{
		return this.project;
	}

	@Override
	public void setProject(final Project pProject)
	{
		this.project = pProject;
	}

	@Override
	public RefScopeUnit getParentScopeUnit()
	{
		return parentScopeUnit;
	}

	@Override
	public void setParentScopeUnit(final RefScopeUnit parentScopeUnit)
	{
		this.parentScopeUnit = parentScopeUnit;
	}

	@Override
	public Integer getBenefit()
	{
		return benefit;
	}

	@Override
	public void setBenefit(final Integer benefit)
	{
		this.benefit = benefit;
	}

	@Override
	public Integer getInjury()
	{
		return injury;
	}

	@Override
	public void setInjury(final Integer injury)
	{
		this.injury = injury;
	}

	@Override
	public Integer getRisk()
	{
		return risk;
	}

	@Override
	public void setRisk(final Integer risk)
	{
		this.risk = risk;
	}

	@Override
	public Date getDateCreated()
	{
		return dateCreated;
	}

	@Override
	public void setDateCreated(final Date dateCreated)
	{
		this.dateCreated = dateCreated;
	}

	@Override
	public Date getDateModified()
	{
		return dateModified;
	}

	@Override
	public void setDateModified(final Date dateModified)
	{
		this.dateModified = dateModified;
	}

	@Override
	public StatusScope getState()
	{
		return state;
	}

	@Override
	public void setState(final StatusScope state)
	{
		this.state = state;
	}

	@Override
	public CDOParameters getCdoParameters()
	{
		return cdoParameters;
	}

	@Override
	public void setCdoParameters(final CDOParameters cdoParameters)
	{
		this.cdoParameters = cdoParameters;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((benefit == null) ? 0 : benefit.hashCode());
		result = prime * result + ((dateCreated == null) ? 0 : dateCreated.hashCode());
		result = prime * result + ((dateModified == null) ? 0 : dateModified.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((injury == null) ? 0 : injury.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((risk == null) ? 0 : risk.hashCode());
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
		RefScopeUnitEntity other = (RefScopeUnitEntity) obj;
		if (benefit == null)
		{
			if (other.benefit != null)
			{
				return false;
			}
		}
		else if (!benefit.equals(other.benefit))
		{
			return false;
		}
		if (dateCreated == null)
		{
			if (other.dateCreated != null)
			{
				return false;
			}
		}
		else if (!dateCreated.equals(other.dateCreated))
		{
			return false;
		}
		if (dateModified == null)
		{
			if (other.dateModified != null)
			{
				return false;
			}
		}
		else if (!dateModified.equals(other.dateModified))
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
		if (injury == null)
		{
			if (other.injury != null)
			{
				return false;
			}
		}
		else if (!injury.equals(other.injury))
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
		if (risk == null)
		{
			if (other.risk != null)
			{
				return false;
			}
		}
		else if (!risk.equals(other.risk))
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
		return "RefScopeUnitEntity [id=" + id + ", name=" + name + ", description=" + description + ", version="
				+ version + ", dateCreated=" + dateCreated + ", dateModified=" + dateModified + ", benefit="
				+ benefit + ", injury=" + injury + ", risk=" + risk + "]";
	}

}
