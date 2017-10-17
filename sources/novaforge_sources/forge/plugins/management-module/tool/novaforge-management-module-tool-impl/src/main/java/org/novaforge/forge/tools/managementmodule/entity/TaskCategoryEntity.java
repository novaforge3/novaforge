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

import org.novaforge.forge.tools.managementmodule.domain.Project;
import org.novaforge.forge.tools.managementmodule.domain.TaskCategory;

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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;

/**
 * JPA Implementation of a TaskCategory
 */
@Entity
@Table(name = "task_category", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "project_id" }))
@NamedQueries({ @NamedQuery(name = "TaskCategoryEntity.findByName", query = "SELECT t FROM TaskCategoryEntity t WHERE t.name = :name") })
public class TaskCategoryEntity implements TaskCategory, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3448756519920734070L;

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "name", nullable = false)
	private String						name;

	@ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
	@JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false)
	private Project					 project;

	@Override
	public Long getId()
	{
		return id;
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
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/**
	 * @return the project
	 */
	@Override
	public Project getProject()
	{
		return project;
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
		TaskCategoryEntity other = (TaskCategoryEntity) obj;
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
		return true;
	}

	/**
	 * @param project
	 *     the project to set
	 */
	@Override
	public void setProject(final Project project)
	{
		this.project = project;
		project.addTaskCategory(this);
	}

	@Override
	public String toString()
	{
		return "TaskCategoryEntity [id=" + id + ", name=" + name + "]";
	}

}
