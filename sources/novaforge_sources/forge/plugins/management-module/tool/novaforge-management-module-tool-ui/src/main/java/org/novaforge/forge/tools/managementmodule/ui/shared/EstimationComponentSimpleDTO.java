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
package org.novaforge.forge.tools.managementmodule.ui.shared;

import java.io.Serializable;



public class EstimationComponentSimpleDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8034936987435905623L;

	private Long id;

	private String idProjet;

	private Float valueGDI;

	private Float valueINT;
	
	private Float valueENT;
	
	private Float valueSOR;
	
	private Float valueGDE;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the idProjet
	 */
	public String getIdProjet() {
		return idProjet;
	}

	/**
	 * @param idProjet the idProjet to set
	 */
	public void setIdProjet(String idProjet) {
		this.idProjet = idProjet;
	}

	/**
	 * @return the valueGDI
	 */
	public Float getValueGDI() {
		return valueGDI;
	}

	/**
	 * @param valueGDI the valueGDI to set
	 */
	public void setValueGDI(Float valueGDI) {
		this.valueGDI = valueGDI;
	}

	/**
	 * @return the valueINT
	 */
	public Float getValueINT() {
		return valueINT;
	}

	/**
	 * @param valueINT the valueINT to set
	 */
	public void setValueINT(Float valueINT) {
		this.valueINT = valueINT;
	}

	/**
	 * @return the valueENT
	 */
	public Float getValueENT() {
		return valueENT;
	}

	/**
	 * @param valueENT the valueENT to set
	 */
	public void setValueENT(Float valueENT) {
		this.valueENT = valueENT;
	}

	/**
	 * @return the valueSOR
	 */
	public Float getValueSOR() {
		return valueSOR;
	}

	/**
	 * @param valueSOR the valueSOR to set
	 */
	public void setValueSOR(Float valueSOR) {
		this.valueSOR = valueSOR;
	}

	/**
	 * @return the valueGDE
	 */
	public Float getValueGDE() {
		return valueGDE;
	}

	/**
	 * @param valueGDE the valueGDE to set
	 */
	public void setValueGDE(Float valueGDE) {
		this.valueGDE = valueGDE;
	}


	
	@Override
	public int hashCode() {
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
		EstimationComponentSimpleDTO other = (EstimationComponentSimpleDTO) obj;
		if (idProjet == null) {
			if (other.idProjet != null) {
            return false;
         }
		} else if (!idProjet.equals(other.idProjet)) {
         return false;
      }
		if (valueENT == null) {
			if (other.valueENT != null) {
            return false;
         }
		} else if (!valueENT.equals(other.valueENT)) {
         return false;
      }
		if (valueGDE == null) {
			if (other.valueGDE != null) {
            return false;
         }
		} else if (!valueGDE.equals(other.valueGDE)) {
         return false;
      }
		if (valueGDI == null) {
			if (other.valueGDI != null) {
            return false;
         }
		} else if (!valueGDI.equals(other.valueGDI)) {
         return false;
      }
		if (valueINT == null) {
			if (other.valueINT != null) {
            return false;
         }
		} else if (!valueINT.equals(other.valueINT)) {
         return false;
      }
		if (valueSOR == null) {
			if (other.valueSOR != null) {
            return false;
         }
		} else if (!valueSOR.equals(other.valueSOR)) {
         return false;
      }
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EstimationComponentSimpleDTO [id=" + id + ", idProjet=" + idProjet
				+ ", valueGDI=" + valueGDI + ", valueINT=" + valueINT
				+ ", valueENT=" + valueENT + ", valueSOR=" + valueSOR
				+ ", valueGDE=" + valueGDE + "]";
	}
	
	

}
