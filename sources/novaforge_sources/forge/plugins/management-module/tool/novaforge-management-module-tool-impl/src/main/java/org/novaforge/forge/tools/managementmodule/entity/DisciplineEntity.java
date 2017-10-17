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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * JPA implementation of a Discipline
 */
@Entity
@Table(name = "discipline")
@NamedQueries({
		@NamedQuery(name = "DisciplineEntity.findByName", query = "SELECT p FROM DisciplineEntity p WHERE p.name = :name"),
		@NamedQuery(name = "DisciplineEntity.findByFunctionalId", query = "SELECT p FROM DisciplineEntity p WHERE p.functionalId = :functionalId") })
public class DisciplineEntity implements Discipline, Serializable
{
	/**
    * 
    */
	private static final long serialVersionUID = -3448756519920734070L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "name", nullable = false)
	private String						name;

	@Column(name = "default_repartition", nullable = false)
	private int							 defaultRepartition;

	@Column(name = "functionalId", nullable = false, unique = true)
	private String						functionalId;

	@Column(name = "disciplineOrder", nullable = false)
	private int							 disciplineOrder;

	/**
	 * Get the name
	 * 
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Set the name
	 * 
	 * @param name
	 *          the name to set
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
	}

	/**
	 * Get the defaultRepartition
	 * 
	 * @return the defaultRepartition
	 */
	@Override
	public int getDefaultRepartition()
	{
		return defaultRepartition;
	}

	/**
	 * Set the defaultRepartition
	 * 
	 * @param defaultRepartition
	 *          the defaultRepartition to set
	 */
	@Override
	public void setDefaultRepartition(final int defaultRepartition)
	{
		this.defaultRepartition = defaultRepartition;
	}

	/**
	 * Get the functionalId
	 * 
	 * @return the functionalId
	 */
	@Override
	public String getFunctionalId()
	{
		return functionalId;
	}

	/**
	 * Set the functionalId
	 * 
	 * @param functionalId
	 *          the functionalId to set
	 */
	@Override
	public void setFunctionalId(final String functionalId)
	{
		this.functionalId = functionalId;
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
	 * Get the order
	 * 
	 * @return the order
	 */
	@Override
	public int getOrder()
	{
		return disciplineOrder;
	}

	/**
	 * Set the order
	 * 
	 * @param order
	 *          the order to set
	 */
	@Override
	public void setOrder(final int order)
	{
		this.disciplineOrder = order;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + defaultRepartition;
		result = prime * result + ((functionalId == null) ? 0 : functionalId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + disciplineOrder;
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
		DisciplineEntity other = (DisciplineEntity) obj;
		if (defaultRepartition != other.defaultRepartition)
		{
			return false;
		}
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
		if (disciplineOrder != other.disciplineOrder)
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "DisciplineEntity [id=" + id + ", name=" + name + ", defaultRepartition=" + defaultRepartition
							 + ", functionalId=" + functionalId + ", order=" + disciplineOrder + "]";
	}

}
