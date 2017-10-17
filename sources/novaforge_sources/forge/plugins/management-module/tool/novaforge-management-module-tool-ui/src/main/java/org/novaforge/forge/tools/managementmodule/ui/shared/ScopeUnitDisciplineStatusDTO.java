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

public class ScopeUnitDisciplineStatusDTO implements Serializable{
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -2227645594323312302L;

	private Long id;

	private String label;

	private String functionalId;

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
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return the functionalId
	 */
	public String getFunctionalId() {
		return functionalId;
	}

	/**
	 * @param functionalId the functionalId to set
	 */
	public void setFunctionalId(String functionalId) {
		this.functionalId = functionalId;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((functionalId == null) ? 0 : functionalId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((label == null) ? 0 : label.hashCode());
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
		ScopeUnitDisciplineStatusDTO other = (ScopeUnitDisciplineStatusDTO) obj;
		if (functionalId == null) {
			if (other.functionalId != null) {
            return false;
         }
		} else if (!functionalId.equals(other.functionalId)) {
         return false;
      }
		if (id == null) {
			if (other.id != null) {
            return false;
         }
		} else if (!id.equals(other.id)) {
         return false;
      }
		if (label == null) {
			if (other.label != null) {
            return false;
         }
		} else if (!label.equals(other.label)) {
         return false;
      }
		return true;
	}
	
	@Override
	public String toString() {
		return "ScopeUnitDisciplineStatusDTO [id=" + id + ", label=" + label
				+ ", functionalId=" + functionalId + "]";
	}

	

}
