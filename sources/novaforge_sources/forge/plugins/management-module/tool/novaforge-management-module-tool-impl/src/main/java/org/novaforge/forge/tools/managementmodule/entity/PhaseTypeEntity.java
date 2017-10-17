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

import org.novaforge.forge.tools.managementmodule.domain.PhaseType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "phase_type")
@NamedQueries({
		@NamedQuery(name = "PhaseTypeEntity.findByFunctionalId", query = "SELECT p FROM PhaseTypeEntity p WHERE p.functionalId = :functionalId"),
		@NamedQuery(name = "PhaseTypeEntity.findAllPhaseTypes", query = "SELECT p FROM PhaseTypeEntity p") })
public class PhaseTypeEntity implements PhaseType, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5489789477111917050L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "functionalId", nullable = false, unique = true)
	private String						functionalId;

	@Column(name = "name", nullable = false, unique = false)
	private String						name;

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
	public Long getId()
	{
		return id;
	}

	@Override
	public String getFunctionalId()
	{
		return functionalId;
	}

	@Override
	public void setFunctionalId(final String functionalId)
	{
		this.functionalId = functionalId;
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
		PhaseTypeEntity other = (PhaseTypeEntity) obj;
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
		return "PhaseTypeEntity [id=" + id + ", name=" + name + ", functionalId=" + functionalId + "]";
	}

}
