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


public class AdjustFactorJointureDTO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3901429034622026015L;

	private Long id;
	
	private AdjustFactorDTO adjustFactor;
	
	private AdjustWeightDTO adjustWeight;

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
	 * @return the adjustFactor
	 */
	public AdjustFactorDTO getAdjustFactor() {
		return adjustFactor;
	}

	/**
	 * @param adjustFactor the adjustFactor to set
	 */
	public void setAdjustFactor(AdjustFactorDTO adjustFactor) {
		this.adjustFactor = adjustFactor;
	}

	/**
	 * @return the adjustWeight
	 */
	public AdjustWeightDTO getAdjustWeight() {
		return adjustWeight;
	}

	/**
	 * @param adjustWeight the adjustWeight to set
	 */
	public void setAdjustWeight(AdjustWeightDTO adjustWeight) {
		this.adjustWeight = adjustWeight;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((adjustFactor == null) ? 0 : adjustFactor.hashCode());
		result = prime * result
				+ ((adjustWeight == null) ? 0 : adjustWeight.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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
		AdjustFactorJointureDTO other = (AdjustFactorJointureDTO) obj;
		if (adjustFactor == null) {
			if (other.adjustFactor != null) {
            return false;
         }
		} else if (!adjustFactor.equals(other.adjustFactor)) {
         return false;
      }
		if (adjustWeight == null) {
			if (other.adjustWeight != null) {
            return false;
         }
		} else if (!adjustWeight.equals(other.adjustWeight)) {
         return false;
      }
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AdjustFactorJointureDTO [id=" + id + ", adjustFactor="
				+ adjustFactor + ", adjustWeight=" + adjustWeight + "]";
	}


	
	

}
