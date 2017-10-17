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

import org.novaforge.forge.tools.managementmodule.domain.TaskType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * The JPA implementation of a TaskType
 */
@Entity
@Table(name = "task_type")
@NamedQueries({ @NamedQuery(name = "TaskTypeEntity.findByFunctionalId", query = "SELECT p FROM TaskTypeEntity p WHERE p.functionalId = :functionalId") })
public class TaskTypeEntity implements TaskType
{

	/** UID */
	private static final long serialVersionUID = -3584370421838666766L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "description", nullable = false, unique = true)
	private String						description;

	@Column(name = "functionalId", nullable = false, unique = true)
	private String						functionalId;

	/**
	 * @return the functionalId
	 */
	@Override
	public String getFunctionalId()
	{
		return functionalId;
	}

	/**
	 * @param functionalId
	 *          the functionalId to set
	 */
	@Override
	public void setFunctionalId(final String functionalId)
	{
		this.functionalId = functionalId;
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

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((functionalId == null) ? 0 : functionalId.hashCode());
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
		TaskTypeEntity other = (TaskTypeEntity) obj;
		if (functionalId == null)
		{
			if (other.functionalId != null)
			{
				return false;
			}
		}
		else if (!functionalId.equals(other.functionalId))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "TaskTypeEntity [id=" + id + ", description=" + description + ", functionalId=" + functionalId
				+ "]";
	}

}
