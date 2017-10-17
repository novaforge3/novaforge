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

import org.novaforge.forge.tools.managementmodule.domain.AdjustWeight;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @author fdemange
 *         Ponderation facteur d'ajustement
 */
@Entity
@Table(name = "adjust_weight")
@NamedQueries({ @NamedQuery(name = "AdjustWeightEntity.findByFunctionalId", query = "SELECT p FROM AdjustWeightEntity p WHERE p.functionalId = :functionalId") })
public class AdjustWeightEntity implements AdjustWeight, Serializable
{
	/**
    * 
    */
	private static final long serialVersionUID = -3448756519920734070L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "weight", nullable = false)
	private String						weight;

	@Column(name = "name", nullable = false)
	private String						name;

	@Column(name = "functionalId", nullable = false, unique = true)
	private String						functionalId;

	public Long getId()
	{
		return id;
	}

	@Override
	@Size(max = 255)
	public String getWeight()
	{
		return weight;
	}

	@Override
	public void setWeight(final String weight)
	{
		this.weight = weight;
	}

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
	 * @return the name
	 */
	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * @param name
	 *          the name to set
	 */
	@Override
	public void setName(final String name)
	{
		this.name = name;
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
		result = prime * result + ((functionalId == null) ? 0 : functionalId.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
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
		AdjustWeightEntity other = (AdjustWeightEntity) obj;
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
		if (weight == null)
		{
			if (other.weight != null)
			{
				return false;
			}
		}
		else if (!weight.equals(other.weight))
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
		return "AdjustWeightEntity [id=" + id + ", weight=" + weight + ", name=" + name + ", functionalId="
				+ functionalId + "]";
	}

}
