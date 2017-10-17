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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author fdemange
 */
@Entity
@Table(name = "estimation_component_simple")
@NamedQuery(name = "EstimationComponentSimpleEntity.findByIdProject", query = "SELECT p FROM EstimationComponentSimpleEntity p WHERE p.idProjet = :idProjet")
public class EstimationComponentSimpleEntity implements Serializable, EstimationComponentSimple
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2520671768310463156L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "idProjet", nullable = true, unique = true)
	private String						idProjet;

	@Column(name = "valueGDI", nullable = false, unique = false)
	private Float						 valueGDI;

	@Column(name = "valueINT", nullable = false, unique = false)
	private Float						 valueINT;

	@Column(name = "valueENT", nullable = false, unique = false)
	private Float						 valueENT;

	@Column(name = "valueSOR", nullable = false, unique = false)
	private Float						 valueSOR;

	@Column(name = "valueGDE", nullable = false, unique = false)
	private Float						 valueGDE;

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#getIdProjet()
	 */
	@Override
	public String getIdProjet()
	{
		return idProjet;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#setIdProjet(java.lang.String
	 * )
	 */
	@Override
	public void setIdProjet(final String idProjet)
	{
		this.idProjet = idProjet;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#getValueGDI()
	 */
	@Override
	public Float getValueGDI()
	{
		return valueGDI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#setValueGDI(int)
	 */
	@Override
	public void setValueGDI(final Float valueGDI)
	{
		this.valueGDI = valueGDI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#getValueINT()
	 */
	@Override
	public Float getValueINT()
	{
		return valueINT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#setValueINT(int)
	 */
	@Override
	public void setValueINT(final Float valueINT)
	{
		this.valueINT = valueINT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#getValueENT()
	 */
	@Override
	public Float getValueENT()
	{
		return valueENT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#setValueENT(int)
	 */
	@Override
	public void setValueENT(final Float valueENT)
	{
		this.valueENT = valueENT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#getValueSOR()
	 */
	@Override
	public Float getValueSOR()
	{
		return valueSOR;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#setValueSOR(int)
	 */
	@Override
	public void setValueSOR(final Float valueSOR)
	{
		this.valueSOR = valueSOR;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#getValueGDE()
	 */
	@Override
	public Float getValueGDE()
	{
		return valueGDE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.EstimationComponentSimple#setValueGDE(int)
	 */
	@Override
	public void setValueGDE(final Float valueGDE)
	{
		this.valueGDE = valueGDE;
	}

	/**
	 * @return the id
	 */
	@Override
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id
	 *     the id to set
	 */
	public void setId(final Long id)
	{
		this.id = id;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((idProjet == null) ? 0 : idProjet.hashCode());
		result = prime * result + ((valueENT == null) ? 0 : valueENT.hashCode());
		result = prime * result + ((valueGDE == null) ? 0 : valueGDE.hashCode());
		result = prime * result + ((valueGDI == null) ? 0 : valueGDI.hashCode());
		result = prime * result + ((valueINT == null) ? 0 : valueINT.hashCode());
		result = prime * result + ((valueSOR == null) ? 0 : valueSOR.hashCode());
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
		EstimationComponentSimpleEntity other = (EstimationComponentSimpleEntity) obj;
		if (idProjet == null)
		{
			if (other.idProjet != null)
			{
				return false;
			}
		}
		else if (!idProjet.equals(other.idProjet))
		{
			return false;
		}
		if (valueENT == null)
		{
			if (other.valueENT != null)
			{
				return false;
			}
		}
		else if (!valueENT.equals(other.valueENT))
		{
			return false;
		}
		if (valueGDE == null)
		{
			if (other.valueGDE != null)
			{
				return false;
			}
		}
		else if (!valueGDE.equals(other.valueGDE))
		{
			return false;
		}
		if (valueGDI == null)
		{
			if (other.valueGDI != null)
			{
				return false;
			}
		}
		else if (!valueGDI.equals(other.valueGDI))
		{
			return false;
		}
		if (valueINT == null)
		{
			if (other.valueINT != null)
			{
				return false;
			}
		}
		else if (!valueINT.equals(other.valueINT))
		{
			return false;
		}
		if (valueSOR == null)
		{
			if (other.valueSOR != null)
			{
				return false;
			}
		}
		else if (!valueSOR.equals(other.valueSOR))
		{
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException
	{
		EstimationComponentSimpleEntity estimationComponentSimpleEntity = new EstimationComponentSimpleEntity();
		estimationComponentSimpleEntity.setValueENT(valueENT);
		estimationComponentSimpleEntity.setValueGDE(valueGDE);
		estimationComponentSimpleEntity.setValueINT(valueINT);
		estimationComponentSimpleEntity.setValueGDI(valueGDI);
		estimationComponentSimpleEntity.setValueSOR(valueSOR);
		estimationComponentSimpleEntity.setIdProjet(idProjet);
		return estimationComponentSimpleEntity;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "EstimationComponentSimpleEntity [id=" + id + ", idProjet=" + idProjet + ", valueGDI=" + valueGDI
							 + ", valueINT=" + valueINT + ", valueENT=" + valueENT + ", valueSOR=" + valueSOR + ", valueGDE="
							 + valueGDE + "]";
	}

}
