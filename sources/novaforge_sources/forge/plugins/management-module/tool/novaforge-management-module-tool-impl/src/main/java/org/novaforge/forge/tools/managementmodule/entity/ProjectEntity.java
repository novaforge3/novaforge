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

import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentSimple;
import org.novaforge.forge.tools.managementmodule.domain.Membership;
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;
import org.novaforge.forge.tools.managementmodule.domain.Transformation;
import org.novaforge.forge.tools.managementmodule.domain.UnitTime;

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
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author vvigo
 */
@Entity
@Table(name = "project")
@NamedQueries({
		@NamedQuery(name = "ProjectEntity.findById", query = "SELECT p FROM ProjectEntity p WHERE p.projectId = :projectId"),
		@NamedQuery(name = "ProjectEntity.findFullById", query = "SELECT p FROM ProjectEntity p LEFT JOIN FETCH p.memberships LEFT JOIN FETCH p.projectPlans LEFT JOIN FETCH p.refScopeUnits LEFT JOIN FETCH p.disciplines LEFT JOIN FETCH p.taskCategories WHERE p.projectId = :projectId") })
public class ProjectEntity implements Project, Serializable
{

	private static final long			serialVersionUID = -3448756519920734070L;
	@OneToOne(cascade = CascadeType.ALL, targetEntity = TransformationEntity.class, optional = false)
	Transformation            transformation;
	@OneToOne(cascade = CascadeType.ALL, targetEntity = EstimationComponentSimpleEntity.class, optional = false)
	EstimationComponentSimple estimationComponentSimple;
	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long									 id;
	@Column(name = "project_id", nullable = false, unique = true, updatable = false)
	private String								 projectId;
	@Column(name = "name", nullable = false, unique = false)
	private String								 name;
	@Column(name = "description", nullable = false)
	private String								 description;
	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE,
			CascadeType.REFRESH }, targetEntity = MembershipEntity.class, orphanRemoval = true)
	private Set<Membership>				memberships			= new HashSet<Membership>();

	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE,
			CascadeType.REFRESH }, targetEntity = ProjectPlanEntity.class, orphanRemoval = true)
	private Set<ProjectPlan>			 projectPlans		 = new HashSet<ProjectPlan>();

	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE,
			CascadeType.REFRESH }, targetEntity = RefScopeUnitEntity.class, orphanRemoval = true)
	private Set<RefScopeUnit>			refScopeUnits		= new HashSet<RefScopeUnit>();

	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = { CascadeType.ALL }, targetEntity = ProjectDisciplineEntity.class, orphanRemoval = true)
	private Set<ProjectDiscipline> disciplines			= new HashSet<ProjectDiscipline>();

	@OneToMany(mappedBy = "project", fetch = FetchType.LAZY, cascade = { CascadeType.MERGE, CascadeType.REMOVE,
			CascadeType.REFRESH }, targetEntity = TaskCategoryEntity.class, orphanRemoval = true)
	private Set<TaskCategory>			taskCategories	 = new HashSet<TaskCategory>();

	@ManyToOne(targetEntity = UnitTimeEntity.class)
	@JoinColumn(name = "unit_time_id", nullable = false)
	private UnitTime							 unitTime;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getProjectId()
	{
		return projectId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProjectId(final String pProjectId)
	{
		this.projectId = pProjectId;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Size(max = 250)
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
	public Set<Membership> getMemberships()
	{
		Set<Membership> newList = new HashSet<Membership>(memberships);
		memberships.clear();
		memberships.addAll(newList);
		return memberships;
	}

	@Override
	public void setMemberships(final Set<Membership> memberships)
	{
		this.memberships = memberships;
	}

	@Override
	public void addMembership(final Membership m)
	{
		this.memberships.add(m);
	}

	@Override
	public void removeMembership(final Membership m)
	{
		this.memberships.remove(m);

	}

	@Override
	public Set<ProjectPlan> getProjectPlans()
	{
		Set<ProjectPlan> newList = new HashSet<ProjectPlan>(projectPlans);
		projectPlans.clear();
		projectPlans.addAll(newList);
		return projectPlans;
	}

	@Override
	public void setProjectPlans(final Set<ProjectPlan> projectPlans)
	{
		this.projectPlans = projectPlans;
	}

	@Override
	public void addProjectPlan(final ProjectPlan projectPlan)
	{
		this.projectPlans.add(projectPlan);
		ProjectPlanEntity projectPlanEntity = (ProjectPlanEntity) projectPlan;
		projectPlanEntity.setProject(this);
	}

	@Override
	public void removeProjectPlan(final ProjectPlan m)
	{
		this.projectPlans.remove(m);

	}

	@Override
	public Set<RefScopeUnit> getRefScopeUnitList()
	{
		Set<RefScopeUnit> newList = new HashSet<RefScopeUnit>(refScopeUnits);
		refScopeUnits.clear();
		refScopeUnits.addAll(newList);
		return refScopeUnits;
	}

	@Override
	public void setRefScopeUnits(final Set<RefScopeUnit> refScopeUnits)
	{
		this.refScopeUnits = refScopeUnits;
	}

	@Override
	public void addRefScopeUnit(final RefScopeUnit pRefScopeUnit)
	{
		this.refScopeUnits.add(pRefScopeUnit);
		RefScopeUnitEntity refScopeUnitEntity = (RefScopeUnitEntity) pRefScopeUnit;
		refScopeUnitEntity.setProject(this);
	}

	@Override
	public void removeRefScopeUnit(final RefScopeUnit pRefScopeUnit)
	{
		this.refScopeUnits.remove(pRefScopeUnit);

	}

	/**
	 * @return the transformation
	 */
	@Override
	public Transformation getTransformation()
	{
		return transformation;
	}

	/**
	 * @param transformation
	 *     the transformation to set
	 */
	@Override
	public void setTransformation(final Transformation transformation)
	{
		this.transformation = transformation;
	}

	/**
	 * @return the estimationComponentSimple
	 */
	@Override
	public EstimationComponentSimple getEstimationComponentSimple()
	{
		return estimationComponentSimple;
	}

	/**
	 * @param estimationComponentSimple
	 *     the estimationComponentSimple to set
	 */
	@Override
	public void setEstimationComponentSimple(final EstimationComponentSimple estimationComponentSimple)
	{
		this.estimationComponentSimple = estimationComponentSimple;
	}

	/**
	 * @return the disciplines
	 */
	@Override
	public Set<ProjectDiscipline> getDisciplines()
	{
		Set<ProjectDiscipline> newList = new HashSet<ProjectDiscipline>(disciplines);
		disciplines.clear();
		disciplines.addAll(newList);
		return disciplines;
	}

	/**
	 * @param disciplines
	 *          the disciplines to set
	 */
	@Override
	public void setDisciplines(final Set<ProjectDiscipline> disciplines)
	{
		this.disciplines = disciplines;
	}

	@Override
	public void addDiscipline(final ProjectDiscipline m)
	{
		this.disciplines.add(m);
	}

	@Override
	public void removeDiscipline(final ProjectDiscipline m)
	{
		this.disciplines.remove(m);

	}

	/**
	 * @return the taskCategories
	 */
	@Override
	public Set<TaskCategory> getTaskCategories()
	{
		Set<TaskCategory> newList = new HashSet<TaskCategory>(taskCategories);
		taskCategories.clear();
		taskCategories.addAll(newList);
		return taskCategories;
	}

	/**
	 * @param taskCategories
	 *          the taskCategories to set
	 */
	@Override
	public void setTaskCategories(final Set<TaskCategory> taskCategories)
	{
		this.taskCategories = taskCategories;
	}

	@Override
	public void addTaskCategory(final TaskCategory m)
	{
		this.taskCategories.add(m);
	}

	@Override
	public void removeTaskCategory(final TaskCategory m)
	{
		this.taskCategories.remove(m);

	}

	/**
	 * Get the unitTime
	 * 
	 * @return the unitTime
	 */
	@Override
	public UnitTime getUnitTime()
	{
		return unitTime;
	}

	/**
	 * Set the unitTime
	 * 
	 * @param unitTime
	 *          the unitTime to set
	 */
	@Override
	public void setUnitTime(final UnitTime unitTime)
	{
		this.unitTime = unitTime;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((projectId == null) ? 0 : projectId.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		ProjectEntity other = (ProjectEntity) obj;
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
		if (projectId == null)
		{
			if (other.projectId != null)
			{
				return false;
			}
		}
		else if (!projectId.equals(other.projectId))
		{
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "ProjectEntity [id=" + id + ", projectId=" + projectId + ", name=" + name + ", description=" + description
							 + ", transformation=" + transformation + ", estimationComponentSimple=" + estimationComponentSimple
							 + ", memberships=" + memberships + ", projectPlans=" + projectPlans + ", refScopeUnits=" + refScopeUnits
							 + "]";
	}

}
