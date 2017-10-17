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
import java.util.Date;

/**
 * @author Bilet-jc
 *
 */
public class ScopeUnitDisciplineDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1858945083798164162L;
	
	private ScopeUnitDTO scopeUnit;
	private DisciplineDTO discipline;
	private ScopeUnitDisciplineStatusDTO status;
	private Date lastUpdate;
	/**
	 * @return the scopeUnit
	 */
	public ScopeUnitDTO getScopeUnit() {
		return scopeUnit;
	}
	/**
	 * @param scopeUnit the scopeUnit to set
	 */
	public void setScopeUnit(ScopeUnitDTO scopeUnit) {
		this.scopeUnit = scopeUnit;
	}
	/**
	 * @return the discipline
	 */
	public DisciplineDTO getDiscipline() {
		return discipline;
	}
	/**
	 * @param discipline the discipline to set
	 */
	public void setDiscipline(DisciplineDTO discipline) {
		this.discipline = discipline;
	}
	/**
	 * @return the status
	 */
	public ScopeUnitDisciplineStatusDTO getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(ScopeUnitDisciplineStatusDTO status) {
		this.status = status;
	}
	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return lastUpdate;
	}
	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((discipline == null) ? 0 : discipline.hashCode());
		result = prime * result
				+ ((lastUpdate == null) ? 0 : lastUpdate.hashCode());
		result = prime * result
				+ ((scopeUnit == null) ? 0 : scopeUnit.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
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
		ScopeUnitDisciplineDTO other = (ScopeUnitDisciplineDTO) obj;
		if (discipline == null) {
			if (other.discipline != null) {
            return false;
         }
		} else if (!discipline.equals(other.discipline)) {
         return false;
      }
		if (lastUpdate == null) {
			if (other.lastUpdate != null) {
            return false;
         }
		} else if (!lastUpdate.equals(other.lastUpdate)) {
         return false;
      }
		if (scopeUnit == null) {
			if (other.scopeUnit != null) {
            return false;
         }
		} else if (!scopeUnit.equals(other.scopeUnit)) {
         return false;
      }
		if (status == null) {
			if (other.status != null) {
            return false;
         }
		} else if (!status.equals(other.status)) {
         return false;
      }
		return true;
	}
	
	@Override
	public String toString() {
		return "ScopeUnitDisciplineDTO [scopeUnit=" + scopeUnit
				+ ", discipline=" + discipline + ", status=" + status
				+ ", lastUpdate=" + lastUpdate + "]";
	}
	

	

}
