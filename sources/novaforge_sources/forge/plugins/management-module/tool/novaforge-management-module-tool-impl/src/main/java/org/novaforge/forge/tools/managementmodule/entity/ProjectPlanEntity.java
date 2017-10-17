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

import org.novaforge.forge.tools.managementmodule.domain.AdjustFactorJointure;
import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentDetail;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.Marker;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.StatusProjectPlan;

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
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "project_plan")
@NamedQueries({
		@NamedQuery(name = "ProjectPlanEntity.findDraftByProjectId", query = "SELECT p FROM ProjectPlanEntity p WHERE p.project.projectId = :projectId AND p.status.functionalId = :statusName "),
		@NamedQuery(name = "ProjectPlanEntity.findByProjectIdAndVersion", query = "SELECT p FROM ProjectPlanEntity p WHERE p.project.projectId = :projectId AND p.version = :version"),
		@NamedQuery(name = "ProjectPlanEntity.findMaxNumVersion", query = "SELECT max(p.version) FROM ProjectPlanEntity p WHERE p.project.projectId = :projectId AND p.status.functionalId = :statusName "),
		@NamedQuery(name = "ProjectPlanEntity.findByProjectId", query = "SELECT DISTINCT p FROM ProjectPlanEntity p WHERE p.project.projectId = :projectId"),
		@NamedQuery(name = "ProjectPlanEntity.findLastValidated", query = "SELECT p FROM ProjectPlanEntity p WHERE p.project.projectId = :projectId AND p.status.functionalId = :statusName ORDER BY p.date DESC"),
		@NamedQuery(name = "ProjectPlanEntity.findFullByIdAndStatus", query = "SELECT p FROM ProjectPlanEntity p WHERE p.project.projectId = :projectId AND p.status.functionalId = :statusName "),
		@NamedQuery(name = "ProjectPlanEntity.findLastFull", query = "SELECT DISTINCT p FROM ProjectPlanEntity p LEFT JOIN FETCH p.adjustFactorJointure WHERE p.project.projectId = :projectId ORDER BY p.date DESC") })
public class ProjectPlanEntity implements ProjectPlan, Serializable
{

	/**
    * 
    */
	private static final long				 serialVersionUID		 = -1104881173199735317L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long											id;

	@Column(name = "date", nullable = false)
	private Date											date;

	@Column(name = "version", nullable = false)
	private Integer									 version;

	@ManyToOne(targetEntity = StatusProjectPlanEntity.class)
	private StatusProjectPlan				 status;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
	private Project									 project;

	@OneToMany(mappedBy = "projectPlan", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = LotEntity.class)
	private Set<Lot>									lots								 = new HashSet<Lot>();

	@OneToMany(mappedBy = "projectPlan", fetch = FetchType.LAZY, cascade = CascadeType.ALL, targetEntity = MarkerEntity.class)
	private Set<Marker>							 markers							= new HashSet<Marker>();

	@OneToOne(targetEntity = EstimationComponentDetailEntity.class, optional = false, cascade = CascadeType.ALL)
	private EstimationComponentDetail estimationComponentDetail;

	@OneToMany(mappedBy = "projectPlan", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, targetEntity = AdjustFactorJointureEntity.class, orphanRemoval = true)
	private Set<AdjustFactorJointure> adjustFactorJointure = new HashSet<AdjustFactorJointure>();

	public ProjectPlanEntity()
	{
		super();
	}

	@Override
	public Long getId()
	{
		return id;
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
	public StatusProjectPlan getStatus()
	{
		return status;
	}

	@Override
	public void setStatus(final StatusProjectPlan status)
	{
		this.status = status;
	}

	@Override
	public Integer getVersion()
	{
		return version;
	}

	@Override
	public void setVersion(final Integer version)
	{
		this.version = version;
	}

	@Override
	public Project getProject()
	{
		return project;
	}

	@Override
	public void setProject(final Project project)
	{
		this.project = project;
	}

	@Override
	public Set<Marker> getMarkers()
	{
		Set<Marker> newList = new HashSet<Marker>(markers);
		markers.clear();
		markers.addAll(newList);
		return markers;
	}

	@Override
	public void setMarkers(final Set<Marker> markers)
	{
		this.markers = markers;
	}

	@Override
	public void addMarker(final Marker marker)
	{
		this.markers.add(marker);
	}

	@Override
	public void removeMarker(final Marker marker)
	{
		this.markers.remove(marker);
	}

	@Override
	public Set<Lot> getLots()
	{
		Set<Lot> newList = new HashSet<Lot>(lots);
		lots.clear();
		lots.addAll(newList);
		return lots;
	}

	@Override
	public void setLots(final Set<Lot> lots)
	{
		this.lots = lots;
	}

	@Override
	public void addLot(final Lot lot)
	{
		this.lots.add(lot);
		LotEntity lotEntity = (LotEntity) lot;
		lotEntity.setProjectPlan(this);
	}

	@Override
	public void removeLot(final Lot lot)
	{
		this.lots.remove(lot);
	}

	/**
	 * @return the estimationComponentDetail
	 */
	@Override
	public EstimationComponentDetail getEstimationComponentDetail()
	{
		return estimationComponentDetail;
	}

	/**
	 * @param estimationComponentDetail
	 *          the estimationComponentDetail to set
	 */
	@Override
	public void setEstimationComponentDetail(final EstimationComponentDetail estimationComponentDetail)
	{
		this.estimationComponentDetail = estimationComponentDetail;
	}

	/**
	 * @return the adjustFactorJointure
	 */
	@Override
	public Set<AdjustFactorJointure> getAdjustFactorJointure()
	{
		Set<AdjustFactorJointure> newList = new HashSet<AdjustFactorJointure>(adjustFactorJointure);
		adjustFactorJointure.clear();
		adjustFactorJointure.addAll(newList);
		return adjustFactorJointure;
	}

	/**
	 * @param adjustFactorJointure
	 *          the adjustFactorJointure to set
	 */
	@Override
	public void setAdjustFactorJointure(final Set<AdjustFactorJointure> adjustFactorJointure)
	{
		this.adjustFactorJointure = adjustFactorJointure;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ProjectPlanEntity other = (ProjectPlanEntity) obj;
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
		return "ProjectPlanEntity [id=" + id + ", date=" + date + ", version=" + version + ", status=" + status
				+ ", project=" + project.getProjectId() + "]";
	}

}
