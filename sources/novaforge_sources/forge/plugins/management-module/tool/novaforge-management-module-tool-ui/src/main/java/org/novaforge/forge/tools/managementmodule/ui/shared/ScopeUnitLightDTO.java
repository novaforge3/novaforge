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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;

/**
 * @author Bilet-jc
 * 
 *         This class is a light scopeUnit use for UI which does not need all
 *         normal scopeUnit's datas
 */
public class ScopeUnitLightDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7970840732004564864L;
	private String unitId;
	private String scopeUnitName;
	private String parentScopeUnitName;
	private boolean isFinished;
	private Integer benefit;
	private Integer injury;
	private Integer risk;
	private Integer weight;
	private ScopeUnitDisciplineStatusDTO status;

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getScopeUnitName() {
		return scopeUnitName;
	}

	public void setScopeUnitName(String scopeUnitName) {
		this.scopeUnitName = scopeUnitName;
	}

	public String getParentScopeUnitName() {
		if (parentScopeUnitName != null) {
			return parentScopeUnitName;			
		}
		return "";
	}

	public void setParentScopeUnitName(String parentScopeUnitName) {
		this.parentScopeUnitName = parentScopeUnitName;
	}

	/**
	 * @return the isFinished
	 */
	public boolean isFinished()
	{
		return isFinished;
	}

	/**
	 * @param isFinished
	 *            the isFinished to set
	 */
	public void setFinished(boolean isFinished)
	{
		this.isFinished = isFinished;
	}

	public Integer getBenefit() {
		return benefit;
	}

	public void setBenefit(Integer benefit) {
		this.benefit = benefit;
	}

	public Integer getInjury() {
		return injury;
	}

	public void setInjury(Integer injury) {
		this.injury = injury;
	}

	public Integer getRisk() {
		return risk;
	}

	public void setRisk(Integer risk) {
		this.risk = risk;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public ScopeUnitDisciplineStatusDTO getStatus() {
		return status;
	}

	public void setStatus(ScopeUnitDisciplineStatusDTO status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((benefit == null) ? 0 : benefit.hashCode());
		result = prime * result + ((injury == null) ? 0 : injury.hashCode());
		result = prime * result + ((parentScopeUnitName == null) ? 0 : parentScopeUnitName.hashCode());
		result = prime * result + ((risk == null) ? 0 : risk.hashCode());
		result = prime * result + ((scopeUnitName == null) ? 0 : scopeUnitName.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((unitId == null) ? 0 : unitId.hashCode());
		result = prime * result + ((weight == null) ? 0 : weight.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
         return true;
      }
		if (obj == null) {
         return false;
      }
		if (getClass() != obj.getClass()) {
         return false;
      }
		ScopeUnitLightDTO other = (ScopeUnitLightDTO) obj;
		if (benefit == null) {
			if (other.benefit != null) {
            return false;
         }
		} else if (!benefit.equals(other.benefit)) {
         return false;
      }
		if (injury == null) {
			if (other.injury != null) {
            return false;
         }
		} else if (!injury.equals(other.injury)) {
         return false;
      }
		if (parentScopeUnitName == null) {
			if (other.parentScopeUnitName != null) {
            return false;
         }
		} else if (!parentScopeUnitName.equals(other.parentScopeUnitName)) {
         return false;
      }
		if (risk == null) {
			if (other.risk != null) {
            return false;
         }
		} else if (!risk.equals(other.risk)) {
         return false;
      }
		if (scopeUnitName == null) {
			if (other.scopeUnitName != null) {
            return false;
         }
		} else if (!scopeUnitName.equals(other.scopeUnitName)) {
         return false;
      }
		if (status == null) {
			if (other.status != null) {
            return false;
         }
		} else if (!status.equals(other.status)) {
         return false;
      }
		if (unitId == null) {
			if (other.unitId != null) {
            return false;
         }
		} else if (!unitId.equals(other.unitId)) {
         return false;
      }
		if (weight == null) {
			if (other.weight != null) {
            return false;
         }
		} else if (!weight.equals(other.weight)) {
         return false;
      }
		return true;
	}

	@Override
	public String toString() {
		return "ScopeUnitLightDTO [unitId=" + unitId + ", scopeUnitName=" + scopeUnitName
				+ ", parentScopeUnitName=" + parentScopeUnitName + ", benefit=" + benefit + ", injury="
				+ injury + ", risk=" + risk + ", weight=" + weight + ", status=" + status + "]";
	}

}
