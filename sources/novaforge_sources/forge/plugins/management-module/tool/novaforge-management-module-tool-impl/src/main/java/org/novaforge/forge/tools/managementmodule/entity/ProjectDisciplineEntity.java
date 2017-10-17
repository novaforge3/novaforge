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
import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.ProjectDiscipline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * JPA implementation of a ProjectDiscipline (join table beween a project and its disciplines)
 */
@Entity
@Table(name = "project_discipline")
public class ProjectDisciplineEntity implements ProjectDiscipline, Serializable
{

	/** UID for serialization */
	private static final long serialVersionUID = -2918414589188298213L;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
	@JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
	private Project					 project;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = DisciplineEntity.class)
	@JoinColumn(name = "discipline_id", referencedColumnName = "id", nullable = false)
	private Discipline				discipline;

	@Column(name = "repartition", nullable = false)
	private int							 repartition;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	/**
	 * Get the repartition
	 * 
	 * @return the repartition
	 */
	@Override
	public int getRepartition()
	{
		return repartition;
	}

	/**
	 * Set the repartition
	 * 
	 * @param repartition
	 *          the repartition to set
	 */
	@Override
	public void setRepartition(final int repartition)
	{
		this.repartition = repartition;
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
	 * Get the project
	 * 
	 * @return the project
	 */
	@Override
	public Project getProject()
	{
		return project;
	}

	/**
	 * Set the project
	 * 
	 * @param project
	 *          the project to set
	 */
	@Override
	public void setProject(final Project project)
	{
		this.project = project;
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((discipline == null) ? 0 : discipline.hashCode());
		result = prime * result + ((project == null || project.getId() == null) ? 0 : project.getId().hashCode());
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
		ProjectDisciplineEntity other = (ProjectDisciplineEntity) obj;
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
		if (project == null)
		{
			if (other.project != null)
			{
				return false;
			}
		}
		else if (project.getId() == null)
		{
			if (other.project.getId() != null)
			{
				return false;
			}
		}
		else if (!project.getId().equals(other.project.getId()))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "ProjectDisciplineEntity [project=" + project.getName() + ", discipline=" + discipline.getName()
				+ ", repartition=" + repartition + ", id=" + id + "]";
	}

}
