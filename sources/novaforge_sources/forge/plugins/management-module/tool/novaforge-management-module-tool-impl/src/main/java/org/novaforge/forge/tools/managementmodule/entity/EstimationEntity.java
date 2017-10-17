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

import org.novaforge.forge.tools.managementmodule.domain.Estimation;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * @author fdemange
 * @author BILET-JC
 */
@Entity
@Table(name = "estimation")
@NamedQuery(name = "EstimationEntity.getByScopeUnitId", query = "SELECT e FROM EstimationEntity e WHERE e.scopeUnit.unitId = :scopeUnitId ")
public class EstimationEntity implements Estimation, Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3448756519920734070L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@OneToOne(targetEntity = ScopeUnitEntity.class, optional = false)
	private ScopeUnit				 scopeUnit;

	@Column(name = "GDI_S", nullable = false)
	private Integer					 gdiSimple;

	@Column(name = "GDI_M", nullable = false)
	private Integer					 gdiMedian;

	@Column(name = "GDI_C", nullable = false)
	private Integer					 gdiComplex;

	@Column(name = "GDE_S", nullable = false)
	private Integer					 gdeSimple;

	@Column(name = "GDE_M", nullable = false)
	private Integer					 gdeMedian;

	@Column(name = "GDE_C", nullable = false)
	private Integer					 gdeComplex;

	@Column(name = "IN_S", nullable = false)
	private Integer					 inSimple;

	@Column(name = "IN_M", nullable = false)
	private Integer					 inMedian;

	@Column(name = "IN_C", nullable = false)
	private Integer					 inComplex;

	@Column(name = "OUT_S", nullable = false)
	private Integer					 outSimple;

	@Column(name = "OUT_M", nullable = false)
	private Integer					 outMedian;

	@Column(name = "OUT_C", nullable = false)
	private Integer					 outComplex;

	@Column(name = "INT_S", nullable = false)
	private Integer					 intSimple;

	@Column(name = "INT_M", nullable = false)
	private Integer					 intMedian;

	@Column(name = "INT_C", nullable = false)
	private Integer					 intComplex;

	@Column(name = "global_S", nullable = false)
	private Integer					 globalSimple;

	@Column(name = "global_M", nullable = false)
	private Integer					 globalMedian;

	@Column(name = "global_C", nullable = false)
	private Integer					 globalComplex;

	@Column(name = "FP_raw", nullable = false)
	private Integer					 fpRaw;

	@Column(name = "benefit", nullable = false)
	private Integer					 benefit;

	@Column(name = "injury", nullable = false)
	private Integer					 injury;

	@Column(name = "risk", nullable = false)
	private Integer					 risk;

	@Column(name = "weight", nullable = false)
	private Integer					 weight;

	@Column(name = "manual", nullable = false)
	private boolean					 isManual;

	@Column(name = "simple")
	private String						simple;

	@Column(name = "charge", nullable = false)
	private Integer					 charge;

	@Column(name = "remaining")
	private Float						 remaining;

	@Column(name = "last_date", nullable = false)
	private Date							lastDate;

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
	 *          the id to set
	 */
	@Override
	public void setId(final Long id)
	{
		this.id = id;
	}

	/**
	 * @return the scopeUnit
	 */
	@Override
	public ScopeUnit getScopeUnit()
	{
		return scopeUnit;
	}

	/**
	 * @param scopeUnit
	 *          the scopeUnit to set
	 */
	@Override
	public void setScopeUnit(final ScopeUnit scopeUnit)
	{
		this.scopeUnit = scopeUnit;
	}

	/**
	 * @return the gdiSimple
	 */
	@Override
	public Integer getGDIsimple()
	{
		return gdiSimple;
	}

	/**
	 * @param gdiSimple
	 *          the gdiSimple to set
	 */
	@Override
	public void setGDIsimple(final Integer gDIsimple)
	{
		this.gdiSimple = gDIsimple;
	}

	/**
	 * @return the gDImedian
	 */
	@Override
	public Integer getGDImedian()
	{
		return gdiMedian;
	}

	/**
	 * @param gDImedian
	 *          the gDImedian to set
	 */
	@Override
	public void setGDImedian(final Integer gDImedian)
	{
		gdiMedian = gDImedian;
	}

	/**
	 * @return the gDIcomplex
	 */
	@Override
	public Integer getGDIcomplex()
	{
		return gdiComplex;
	}

	/**
	 * @param gDIcomplex
	 *          the gDIcomplex to set
	 */
	@Override
	public void setGDIcomplex(final Integer gDIcomplex)
	{
		gdiComplex = gDIcomplex;
	}

	/**
	 * @return the gDEsimple
	 */
	@Override
	public Integer getGDEsimple()
	{
		return gdeSimple;
	}

	/**
	 * @param gDEsimple
	 *          the gDEsimple to set
	 */
	@Override
	public void setGDEsimple(final Integer gDEsimple)
	{
		gdeSimple = gDEsimple;
	}

	/**
	 * @return the gDEmedian
	 */
	@Override
	public Integer getGDEmedian()
	{
		return gdeMedian;
	}

	/**
	 * @param gDEmedian
	 *          the gDEmedian to set
	 */
	@Override
	public void setGDEmedian(final Integer gDEmedian)
	{
		gdeMedian = gDEmedian;
	}

	/**
	 * @return the gDEcomplex
	 */
	@Override
	public Integer getGDEcomplex()
	{
		return gdeComplex;
	}

	/**
	 * @param gDEcomplex
	 *          the gDEcomplex to set
	 */
	@Override
	public void setGDEcomplex(final Integer gDEcomplex)
	{
		gdeComplex = gDEcomplex;
	}

	/**
	 * @return the iNsimple
	 */
	@Override
	public Integer getINsimple()
	{
		return inSimple;
	}

	/**
	 * @param iNsimple
	 *          the iNsimple to set
	 */
	@Override
	public void setINsimple(final Integer iNsimple)
	{
		inSimple = iNsimple;
	}

	/**
	 * @return the iNmedian
	 */
	@Override
	public Integer getINmedian()
	{
		return inMedian;
	}

	/**
	 * @param iNmedian
	 *          the iNmedian to set
	 */
	@Override
	public void setINmedian(final Integer iNmedian)
	{
		inMedian = iNmedian;
	}

	/**
	 * @return the iNcomplex
	 */
	@Override
	public Integer getINcomplex()
	{
		return inComplex;
	}

	/**
	 * @param iNcomplex
	 *          the iNcomplex to set
	 */
	@Override
	public void setINcomplex(final Integer iNcomplex)
	{
		inComplex = iNcomplex;
	}

	/**
	 * @return the oUTsimple
	 */
	@Override
	public Integer getOUTsimple()
	{
		return outSimple;
	}

	/**
	 * @param oUTsimple
	 *          the oUTsimple to set
	 */
	@Override
	public void setOUTsimple(final Integer oUTsimple)
	{
		outSimple = oUTsimple;
	}

	/**
	 * @return the oUTmedian
	 */
	@Override
	public Integer getOUTmedian()
	{
		return outMedian;
	}

	/**
	 * @param oUTmedian
	 *          the oUTmedian to set
	 */
	@Override
	public void setOUTmedian(final Integer oUTmedian)
	{
		outMedian = oUTmedian;
	}

	/**
	 * @return the oUTcomplex
	 */
	@Override
	public Integer getOUTcomplex()
	{
		return outComplex;
	}

	/**
	 * @param oUTcomplex
	 *          the oUTcomplex to set
	 */
	@Override
	public void setOUTcomplex(final Integer oUTcomplex)
	{
		outComplex = oUTcomplex;
	}

	/**
	 * @return the iNTsimple
	 */
	@Override
	public Integer getINTsimple()
	{
		return intSimple;
	}

	/**
	 * @param iNTsimple
	 *          the iNTsimple to set
	 */
	@Override
	public void setINTsimple(final Integer iNTsimple)
	{
		intSimple = iNTsimple;
	}

	/**
	 * @return the iNTmedian
	 */
	@Override
	public Integer getINTmedian()
	{
		return intMedian;
	}

	/**
	 * @param iNTmedian
	 *          the iNTmedian to set
	 */
	@Override
	public void setINTmedian(final Integer iNTmedian)
	{
		intMedian = iNTmedian;
	}

	/**
	 * @return the iNTcomplex
	 */
	@Override
	public Integer getINTcomplex()
	{
		return intComplex;
	}

	/**
	 * @param iNTcomplex
	 *          the iNTcomplex to set
	 */
	@Override
	public void setINTcomplex(final Integer iNTcomplex)
	{
		intComplex = iNTcomplex;
	}

	/**
	 * @return the globalSimple
	 */
	@Override
	public Integer getGlobalSimple()
	{
		return globalSimple;
	}

	/**
	 * @param globalSimple
	 *          the globalSimple to set
	 */
	@Override
	public void setGlobalSimple(final Integer globalSimple)
	{
		this.globalSimple = globalSimple;
	}

	/**
	 * @return the globalMedian
	 */
	@Override
	public Integer getGlobalMedian()
	{
		return globalMedian;
	}

	/**
	 * @param globalMedian
	 *          the globalMedian to set
	 */
	@Override
	public void setGlobalMedian(final Integer globalMedian)
	{
		this.globalMedian = globalMedian;
	}

	/**
	 * @return the globalComplex
	 */
	@Override
	public Integer getGlobalComplex()
	{
		return globalComplex;
	}

	/**
	 * @param globalComplex
	 *          the globalComplex to set
	 */
	@Override
	public void setGlobalComplex(final Integer globalComplex)
	{
		this.globalComplex = globalComplex;
	}

	/**
	 * @return the fpRaw
	 */
	@Override
	public Integer getFPRaw()
	{
		return fpRaw;
	}

	/**
	 * @param fpRaw
	 *          the fpRaw to set
	 */
	@Override
	public void setFPRaw(final Integer fpRaw)
	{
		this.fpRaw = fpRaw;
	}

	/**
	 * @return the benefit
	 */
	@Override
	public Integer getBenefit()
	{
		return benefit;
	}

	/**
	 * @param benefit
	 *          the benefit to set
	 */
	@Override
	public void setBenefit(final Integer benefit)
	{
		this.benefit = benefit;
	}

	/**
	 * @return the injury
	 */
	@Override
	public Integer getInjury()
	{
		return injury;
	}

	/**
	 * @param injury
	 *          the injury to set
	 */
	@Override
	public void setInjury(final Integer injury)
	{
		this.injury = injury;
	}

	/**
	 * @return the risk
	 */
	@Override
	public Integer getRisk()
	{
		return risk;
	}

	/**
	 * @param risk
	 *          the risk to set
	 */
	@Override
	public void setRisk(final Integer risk)
	{
		this.risk = risk;
	}

	/**
	 * @return the weight
	 */
	@Override
	public Integer getWeight()
	{
		return weight;
	}

	/**
	 * @param weight
	 *          the weight to set
	 */
	@Override
	public void setWeight(final Integer weight)
	{
		this.weight = weight;
	}

	/**
	 * @return the isManual
	 */
	@Override
	public boolean isManual()
	{
		return isManual;
	}

	/**
	 * @param isManual
	 *          the isManual to set
	 */
	@Override
	public void setManual(final boolean isManual)
	{
		this.isManual = isManual;
	}

	/**
	 * @return the isSimple
	 */
	@Override
	public String getSimple()
	{
		return simple;
	}

	/**
	 * @param simple
	 *          the simple to set
	 */
	@Override
	public void setSimple(final String isSimple)
	{
		this.simple = isSimple;
	}

	/**
	 * @return the charge
	 */
	@Override
	public Integer getLastCharge()
	{
		return charge;
	}

	/**
	 * @param charge
	 *          the charge to set
	 */
	@Override
	public void setCharge(final Integer charge)
	{
		this.charge = charge;
	}

	/**
	 * @return the lastDate
	 */
	@Override
	public Date getLastDate()
	{
		return lastDate;
	}

	/**
	 * @param lastDate
	 *          the lastDate to set
	 */
	@Override
	public void setLastDate(final Date lastDate)
	{
		this.lastDate = lastDate;
	}

	/**
	 * @return the remaining
	 */
	@Override
	public Float getRemaining()
	{
		return remaining;
	}

	/**
	 * @param remaining
	 *          the remaining to set
	 */
	@Override
	public void setRemaining(final Float remaining)
	{
		this.remaining = remaining;
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
		result = prime * result + ((gdeComplex == null) ? 0 : gdeComplex.hashCode());
		result = prime * result + ((gdeMedian == null) ? 0 : gdeMedian.hashCode());
		result = prime * result + ((gdeSimple == null) ? 0 : gdeSimple.hashCode());
		result = prime * result + ((gdiComplex == null) ? 0 : gdiComplex.hashCode());
		result = prime * result + ((gdiMedian == null) ? 0 : gdiMedian.hashCode());
		result = prime * result + ((gdiSimple == null) ? 0 : gdiSimple.hashCode());
		result = prime * result + ((intComplex == null) ? 0 : intComplex.hashCode());
		result = prime * result + ((intMedian == null) ? 0 : intMedian.hashCode());
		result = prime * result + ((intSimple == null) ? 0 : intSimple.hashCode());
		result = prime * result + ((inComplex == null) ? 0 : inComplex.hashCode());
		result = prime * result + ((inMedian == null) ? 0 : inMedian.hashCode());
		result = prime * result + ((inSimple == null) ? 0 : inSimple.hashCode());
		result = prime * result + ((outComplex == null) ? 0 : outComplex.hashCode());
		result = prime * result + ((outMedian == null) ? 0 : outMedian.hashCode());
		result = prime * result + ((outSimple == null) ? 0 : outSimple.hashCode());
		result = prime * result + ((benefit == null) ? 0 : benefit.hashCode());
		result = prime * result + ((charge == null) ? 0 : charge.hashCode());
		result = prime * result + ((globalComplex == null) ? 0 : globalComplex.hashCode());
		result = prime * result + ((globalMedian == null) ? 0 : globalMedian.hashCode());
		result = prime * result + ((globalSimple == null) ? 0 : globalSimple.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((injury == null) ? 0 : injury.hashCode());
		result = prime * result + (isManual ? 1231 : 1237);
		result = prime * result + ((lastDate == null) ? 0 : lastDate.hashCode());
		result = prime * result + ((risk == null) ? 0 : risk.hashCode());
		result = prime * result + ((scopeUnit == null) ? 0 : scopeUnit.hashCode());
		result = prime * result + ((simple == null) ? 0 : simple.hashCode());
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
		EstimationEntity other = (EstimationEntity) obj;
		if (gdeComplex == null)
		{
			if (other.gdeComplex != null)
			{
				return false;
			}
		}
		else if (!gdeComplex.equals(other.gdeComplex))
		{
			return false;
		}
		if (gdeMedian == null)
		{
			if (other.gdeMedian != null)
			{
				return false;
			}
		}
		else if (!gdeMedian.equals(other.gdeMedian))
		{
			return false;
		}
		if (gdeSimple == null)
		{
			if (other.gdeSimple != null)
			{
				return false;
			}
		}
		else if (!gdeSimple.equals(other.gdeSimple))
		{
			return false;
		}
		if (gdiComplex == null)
		{
			if (other.gdiComplex != null)
			{
				return false;
			}
		}
		else if (!gdiComplex.equals(other.gdiComplex))
		{
			return false;
		}
		if (gdiMedian == null)
		{
			if (other.gdiMedian != null)
			{
				return false;
			}
		}
		else if (!gdiMedian.equals(other.gdiMedian))
		{
			return false;
		}
		if (gdiSimple == null)
		{
			if (other.gdiSimple != null)
			{
				return false;
			}
		}
		else if (!gdiSimple.equals(other.gdiSimple))
		{
			return false;
		}
		if (intComplex == null)
		{
			if (other.intComplex != null)
			{
				return false;
			}
		}
		else if (!intComplex.equals(other.intComplex))
		{
			return false;
		}
		if (intMedian == null)
		{
			if (other.intMedian != null)
			{
				return false;
			}
		}
		else if (!intMedian.equals(other.intMedian))
		{
			return false;
		}
		if (intSimple == null)
		{
			if (other.intSimple != null)
			{
				return false;
			}
		}
		else if (!intSimple.equals(other.intSimple))
		{
			return false;
		}
		if (inComplex == null)
		{
			if (other.inComplex != null)
			{
				return false;
			}
		}
		else if (!inComplex.equals(other.inComplex))
		{
			return false;
		}
		if (inMedian == null)
		{
			if (other.inMedian != null)
			{
				return false;
			}
		}
		else if (!inMedian.equals(other.inMedian))
		{
			return false;
		}
		if (inSimple == null)
		{
			if (other.inSimple != null)
			{
				return false;
			}
		}
		else if (!inSimple.equals(other.inSimple))
		{
			return false;
		}
		if (outComplex == null)
		{
			if (other.outComplex != null)
			{
				return false;
			}
		}
		else if (!outComplex.equals(other.outComplex))
		{
			return false;
		}
		if (outMedian == null)
		{
			if (other.outMedian != null)
			{
				return false;
			}
		}
		else if (!outMedian.equals(other.outMedian))
		{
			return false;
		}
		if (outSimple == null)
		{
			if (other.outSimple != null)
			{
				return false;
			}
		}
		else if (!outSimple.equals(other.outSimple))
		{
			return false;
		}
		if (benefit == null)
		{
			if (other.benefit != null)
			{
				return false;
			}
		}
		else if (!benefit.equals(other.benefit))
		{
			return false;
		}
		if (charge == null)
		{
			if (other.charge != null)
			{
				return false;
			}
		}
		else if (!charge.equals(other.charge))
		{
			return false;
		}
		if (globalComplex == null)
		{
			if (other.globalComplex != null)
			{
				return false;
			}
		}
		else if (!globalComplex.equals(other.globalComplex))
		{
			return false;
		}
		if (globalMedian == null)
		{
			if (other.globalMedian != null)
			{
				return false;
			}
		}
		else if (!globalMedian.equals(other.globalMedian))
		{
			return false;
		}
		if (globalSimple == null)
		{
			if (other.globalSimple != null)
			{
				return false;
			}
		}
		else if (!globalSimple.equals(other.globalSimple))
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
		if (injury == null)
		{
			if (other.injury != null)
			{
				return false;
			}
		}
		else if (!injury.equals(other.injury))
		{
			return false;
		}
		if (isManual != other.isManual)
		{
			return false;
		}
		if (lastDate == null)
		{
			if (other.lastDate != null)
			{
				return false;
			}
		}
		else if (!lastDate.equals(other.lastDate))
		{
			return false;
		}
		if (risk == null)
		{
			if (other.risk != null)
			{
				return false;
			}
		}
		else if (!risk.equals(other.risk))
		{
			return false;
		}
		if (scopeUnit == null)
		{
			if (other.scopeUnit != null)
			{
				return false;
			}
		}
		else if (!scopeUnit.equals(other.scopeUnit))
		{
			return false;
		}
		if (simple == null)
		{
			if (other.simple != null)
			{
				return false;
			}
		}
		else if (!simple.equals(other.simple))
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
		return "EstimationEntity [id="
				+ id
				+ ", scopeUnit=" // + scopeUnit
				+ ", gdiSimple=" + gdiSimple + ", gdiMedian=" + gdiMedian + ", gdiComplex=" + gdiComplex
				+ ", gdeSimple=" + gdeSimple + ", gdeMedian=" + gdeMedian + ", gdeComplex=" + gdeComplex
				+ ", inSimple=" + inSimple + ", inMedian=" + inMedian + ", inComplex=" + inComplex + ", outSimple="
				+ outSimple + ", outMedian=" + outMedian + ", outComplex=" + outComplex + ", intSimple=" + intSimple
				+ ", intMedian=" + intMedian + ", intComplex=" + intComplex + ", globalSimple=" + globalSimple
				+ ", globalMedian=" + globalMedian + ", globalComplex=" + globalComplex + ", benefit=" + benefit
				+ ", injury=" + injury + ", risk=" + risk + ", isManual=" + isManual + ", isSimple=" + simple
				+ ", charge=" + charge + ", lastDate=" + lastDate + "]";
	}

}
