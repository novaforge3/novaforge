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

import org.novaforge.forge.tools.managementmodule.domain.EstimationComponentDetail;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author ppr
 */
@Entity
@Table(name = "estimation_component_detail")
@NamedQuery(name = "EstimationComponentDetailEntity.findByFunctionalId", query = "SELECT p FROM EstimationComponentDetailEntity p WHERE p.functionalId = :functionalId")
public class EstimationComponentDetailEntity implements EstimationComponentDetail, Serializable
{

	/**
	 * UID for serialization
	 */
	private static final long serialVersionUID = -8474458499870337687L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "functionalId", nullable = false, unique = true)
	private String						functionalId;

	@Column(name = "valueSimpleGDI", nullable = false, unique = false)
	private int							 valueSimpleGDI;

	@Column(name = "valueMoyenGDI", nullable = false, unique = false)
	private int							 valueMoyenGDI;

	@Column(name = "valueComplexGDI", nullable = false, unique = false)
	private int							 valueComplexGDI;

	@Column(name = "valueSimpleGDE", nullable = false, unique = false)
	private int							 valueSimpleGDE;

	@Column(name = "valueMoyenGDE", nullable = false, unique = false)
	private int							 valueMoyenGDE;

	@Column(name = "valueComplexGDE", nullable = false, unique = false)
	private int							 valueComplexGDE;

	@Column(name = "valueSimpleIN", nullable = false, unique = false)
	private int							 valueSimpleIN;

	@Column(name = "valueMoyenIN", nullable = false, unique = false)
	private int							 valueMoyenIN;

	@Column(name = "valueComplexIN", nullable = false, unique = false)
	private int							 valueComplexIN;

	@Column(name = "valueSimpleINT", nullable = false, unique = false)
	private int							 valueSimpleINT;

	@Column(name = "valueMoyenINT", nullable = false, unique = false)
	private int							 valueMoyenINT;

	@Column(name = "valueComplexINT", nullable = false, unique = false)
	private int							 valueComplexINT;

	@Column(name = "valueSimpleOUT", nullable = false, unique = false)
	private int							 valueSimpleOUT;

	@Column(name = "valueMoyenOUT", nullable = false, unique = false)
	private int							 valueMoyenOUT;

	@Column(name = "valueComplexOUT", nullable = false, unique = false)
	private int							 valueComplexOUT;

	@Column(name = "abacusChargeMenDay", nullable = false, unique = false)
	private float						 valueAbaChgHomJour;

	@Column(name = "adjustementCoef", nullable = false, unique = false)
	private float						 adjustementCoef;

	@Override
	public int getValueSimpleGDI()
	{
		return valueSimpleGDI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueSimpleGDI(int)
	 */
	@Override
	public void setValueSimpleGDI(final int valueSimpleGDI)
	{
		this.valueSimpleGDI = valueSimpleGDI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueMoyenGDI()
	 */
	@Override
	public int getValueMoyenGDI()
	{
		return valueMoyenGDI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueMoyenGDI(int)
	 */
	@Override
	public void setValueMoyenGDI(final int valueMoyenGDI)
	{
		this.valueMoyenGDI = valueMoyenGDI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueComplexGDI()
	 */
	@Override
	public int getValueComplexGDI()
	{
		return valueComplexGDI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueComplexGDI(int)
	 */
	@Override
	public void setValueComplexGDI(final int valueComplexGDI)
	{
		this.valueComplexGDI = valueComplexGDI;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueSimpleGDE()
	 */
	@Override
	public int getValueSimpleGDE()
	{
		return valueSimpleGDE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueSimpleGDE(int)
	 */
	@Override
	public void setValueSimpleGDE(final int valueSimpleGDE)
	{
		this.valueSimpleGDE = valueSimpleGDE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueMoyenGDE()
	 */
	@Override
	public int getValueMoyenGDE()
	{
		return valueMoyenGDE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueMoyenGDE(int)
	 */
	@Override
	public void setValueMoyenGDE(final int valueMoyenGDE)
	{
		this.valueMoyenGDE = valueMoyenGDE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueComplexGDE()
	 */
	@Override
	public int getValueComplexGDE()
	{
		return valueComplexGDE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueComplexGDE(int)
	 */
	@Override
	public void setValueComplexGDE(final int valueComplexGDE)
	{
		this.valueComplexGDE = valueComplexGDE;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueSimpleIN()
	 */
	@Override
	public int getValueSimpleIN()
	{
		return valueSimpleIN;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueSimpleIN(int)
	 */
	@Override
	public void setValueSimpleIN(final int valueSimpleIN)
	{
		this.valueSimpleIN = valueSimpleIN;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueMoyenIN()
	 */
	@Override
	public int getValueMoyenIN()
	{
		return valueMoyenIN;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueMoyenIN(int)
	 */
	@Override
	public void setValueMoyenIN(final int valueMoyenIN)
	{
		this.valueMoyenIN = valueMoyenIN;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueComplexIN()
	 */
	@Override
	public int getValueComplexIN()
	{
		return valueComplexIN;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueComplexIN(int)
	 */
	@Override
	public void setValueComplexIN(final int valueComplexIN)
	{
		this.valueComplexIN = valueComplexIN;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueSimpleINT()
	 */
	@Override
	public int getValueSimpleINT()
	{
		return valueSimpleINT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueSimpleINT(int)
	 */
	@Override
	public void setValueSimpleINT(final int valueSimpleINT)
	{
		this.valueSimpleINT = valueSimpleINT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueMoyenINT()
	 */
	@Override
	public int getValueMoyenINT()
	{
		return valueMoyenINT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueMoyenINT(int)
	 */
	@Override
	public void setValueMoyenINT(final int valueMoyenINT)
	{
		this.valueMoyenINT = valueMoyenINT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueComplexINT()
	 */
	@Override
	public int getValueComplexINT()
	{
		return valueComplexINT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueComplexINT(int)
	 */
	@Override
	public void setValueComplexINT(final int valueComplexINT)
	{
		this.valueComplexINT = valueComplexINT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueSimpleOUT()
	 */
	@Override
	public int getValueSimpleOUT()
	{
		return valueSimpleOUT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueSimpleOUT(int)
	 */
	@Override
	public void setValueSimpleOUT(final int valueSimpleOUT)
	{
		this.valueSimpleOUT = valueSimpleOUT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueMoyenOUT()
	 */
	@Override
	public int getValueMoyenOUT()
	{
		return valueMoyenOUT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueMoyenOUT(int)
	 */
	@Override
	public void setValueMoyenOUT(final int valueMoyenOUT)
	{
		this.valueMoyenOUT = valueMoyenOUT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#getValueComplexOUT()
	 */
	@Override
	public int getValueComplexOUT()
	{
		return valueComplexOUT;
	}

	/*
	 * (non-Javadoc)
	 * @see org.novaforge.forge.plugins.managementmodule.entity.toto#setValueComplexOUT(int)
	 */
	@Override
	public void setValueComplexOUT(final int valueComplexOUT)
	{
		this.valueComplexOUT = valueComplexOUT;
	}

	@Override
	public float getValueAbaChgHomJour()
	{
		return valueAbaChgHomJour;
	}

	@Override
	public void setValueAbaChgHomJour(final float valueAbaChgHomJour)
	{
		this.valueAbaChgHomJour = valueAbaChgHomJour;
	}

	/**
	 * @return the adjustementCoef
	 */
	@Override
	public float getAdjustementCoef()
	{
		return adjustementCoef;
	}

	/**
	 * @param adjustementCoef
	 *          the adjustementCoef to set
	 */
	@Override
	public void setAdjustementCoef(final float adjustementCoef)
	{
		this.adjustementCoef = adjustementCoef;
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
	 * Get the functionalId
	 *
	 * @return the functionalId
	 */
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
		EstimationComponentDetailEntity other = (EstimationComponentDetailEntity) obj;
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
	public Object clone()
	{
		EstimationComponentDetailEntity newEstimation = new EstimationComponentDetailEntity();
		// functionalId put to respect the clone() contract, but must be changed imperatively
		newEstimation.functionalId = this.functionalId;
		newEstimation.adjustementCoef = this.adjustementCoef;
		newEstimation.valueAbaChgHomJour = this.valueAbaChgHomJour;
		newEstimation.valueComplexGDE = this.valueComplexGDE;
		newEstimation.valueComplexGDI = this.valueComplexGDI;
		newEstimation.valueComplexIN = this.valueComplexIN;
		newEstimation.valueComplexINT = this.valueComplexINT;
		newEstimation.valueComplexOUT = this.valueComplexOUT;
		newEstimation.valueMoyenGDE = this.valueMoyenGDE;
		newEstimation.valueMoyenGDI = this.valueMoyenGDI;
		newEstimation.valueMoyenIN = this.valueMoyenIN;
		newEstimation.valueMoyenINT = this.valueMoyenINT;
		newEstimation.valueMoyenOUT = this.valueMoyenOUT;
		newEstimation.valueSimpleGDE = this.valueSimpleGDE;
		newEstimation.valueSimpleGDI = this.valueSimpleGDI;
		newEstimation.valueSimpleIN = this.valueSimpleIN;
		newEstimation.valueSimpleINT = this.valueSimpleINT;
		newEstimation.valueSimpleOUT = this.valueSimpleOUT;
		return newEstimation;
	}

	@Override
	public String toString()
	{
		return "EstimationComponentDetailEntity [id=" + id + ", valueSimpleGDI=" + valueSimpleGDI + ", valueMoyenGDI="
							 + valueMoyenGDI + ", valueComplexGDI=" + valueComplexGDI + ", valueSimpleGDE=" + valueSimpleGDE
							 + ", valueMoyenGDE=" + valueMoyenGDE + ", valueComplexGDE=" + valueComplexGDE + ", valueSimpleIN="
							 + valueSimpleIN + ", valueMoyenIN=" + valueMoyenIN + ", valueComplexIN=" + valueComplexIN
							 + ", valueSimpleINT=" + valueSimpleINT + ", valueMoyenINT=" + valueMoyenINT + ", valueComplexINT="
							 + valueComplexINT + ", valueSimpleOUT=" + valueSimpleOUT + ", valueMoyenOUT=" + valueMoyenOUT
							 + ", valueComplexOUT=" + valueComplexOUT + ", valueAbaChgHomJour=" + valueAbaChgHomJour + "]";
	}

}
