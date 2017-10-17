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


public class EstimationComponentDetailDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1970475917775923697L;

	private Long id;
	
	private String functionalId;

	private int valueSimpleGDI;

	private int valueMoyenGDI;

	private int valueComplexGDI;

	private int valueSimpleGDE;
	
	private int valueMoyenGDE;
	
	private int valueComplexGDE;
	
	private int valueSimpleIN;
	
	private int valueMoyenIN;
	
	private int valueComplexIN;
	
	private int valueSimpleINT;
	
	private int valueMoyenINT;
	
	private int valueComplexINT;
	
	private int valueSimpleOUT;

	private int valueMoyenOUT;
	
	private int valueComplexOUT;
	
	private float valueAbaChgHomJour;

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
    * Get the functionalId
    * @return the functionalId
    */
   public String getFunctionalId() {
      return functionalId;
   }



   /**
    * Set the functionalId
    * @param functionalId the functionalId to set
    */
   public void setFunctionalId(String functionalId) {
      this.functionalId = functionalId;
   }



   /**
	 * @return the valueSimpleGDI
	 */
	public int getValueSimpleGDI() {
		return valueSimpleGDI;
	}



	/**
	 * @param valueSimpleGDI the valueSimpleGDI to set
	 */
	public void setValueSimpleGDI(int valueSimpleGDI) {
		this.valueSimpleGDI = valueSimpleGDI;
	}



	/**
	 * @return the valueMoyenGDI
	 */
	public int getValueMoyenGDI() {
		return valueMoyenGDI;
	}



	/**
	 * @param valueMoyenGDI the valueMoyenGDI to set
	 */
	public void setValueMoyenGDI(int valueMoyenGDI) {
		this.valueMoyenGDI = valueMoyenGDI;
	}



	/**
	 * @return the valueComplexGDI
	 */
	public int getValueComplexGDI() {
		return valueComplexGDI;
	}



	/**
	 * @param valueComplexGDI the valueComplexGDI to set
	 */
	public void setValueComplexGDI(int valueComplexGDI) {
		this.valueComplexGDI = valueComplexGDI;
	}



	/**
	 * @return the valueSimpleGDE
	 */
	public int getValueSimpleGDE() {
		return valueSimpleGDE;
	}



	/**
	 * @param valueSimpleGDE the valueSimpleGDE to set
	 */
	public void setValueSimpleGDE(int valueSimpleGDE) {
		this.valueSimpleGDE = valueSimpleGDE;
	}



	/**
	 * @return the valueMoyenGDE
	 */
	public int getValueMoyenGDE() {
		return valueMoyenGDE;
	}



	/**
	 * @param valueMoyenGDE the valueMoyenGDE to set
	 */
	public void setValueMoyenGDE(int valueMoyenGDE) {
		this.valueMoyenGDE = valueMoyenGDE;
	}



	/**
	 * @return the valueComplexGDE
	 */
	public int getValueComplexGDE() {
		return valueComplexGDE;
	}



	/**
	 * @param valueComplexGDE the valueComplexGDE to set
	 */
	public void setValueComplexGDE(int valueComplexGDE) {
		this.valueComplexGDE = valueComplexGDE;
	}



	/**
	 * @return the valueSimpleIN
	 */
	public int getValueSimpleIN() {
		return valueSimpleIN;
	}



	/**
	 * @param valueSimpleIN the valueSimpleIN to set
	 */
	public void setValueSimpleIN(int valueSimpleIN) {
		this.valueSimpleIN = valueSimpleIN;
	}



	/**
	 * @return the valueMoyenIN
	 */
	public int getValueMoyenIN() {
		return valueMoyenIN;
	}



	/**
	 * @param valueMoyenIN the valueMoyenIN to set
	 */
	public void setValueMoyenIN(int valueMoyenIN) {
		this.valueMoyenIN = valueMoyenIN;
	}



	/**
	 * @return the valueComplexIN
	 */
	public int getValueComplexIN() {
		return valueComplexIN;
	}



	/**
	 * @param valueComplexIN the valueComplexIN to set
	 */
	public void setValueComplexIN(int valueComplexIN) {
		this.valueComplexIN = valueComplexIN;
	}



	/**
	 * @return the valueSimpleINT
	 */
	public int getValueSimpleINT() {
		return valueSimpleINT;
	}



	/**
	 * @param valueSimpleINT the valueSimpleINT to set
	 */
	public void setValueSimpleINT(int valueSimpleINT) {
		this.valueSimpleINT = valueSimpleINT;
	}



	/**
	 * @return the valueMoyenINT
	 */
	public int getValueMoyenINT() {
		return valueMoyenINT;
	}



	/**
	 * @param valueMoyenINT the valueMoyenINT to set
	 */
	public void setValueMoyenINT(int valueMoyenINT) {
		this.valueMoyenINT = valueMoyenINT;
	}



	/**
	 * @return the valueComplexINT
	 */
	public int getValueComplexINT() {
		return valueComplexINT;
	}



	/**
	 * @param valueComplexINT the valueComplexINT to set
	 */
	public void setValueComplexINT(int valueComplexINT) {
		this.valueComplexINT = valueComplexINT;
	}



	/**
	 * @return the valueSimpleOUT
	 */
	public int getValueSimpleOUT() {
		return valueSimpleOUT;
	}



	/**
	 * @param valueSimpleOUT the valueSimpleOUT to set
	 */
	public void setValueSimpleOUT(int valueSimpleOUT) {
		this.valueSimpleOUT = valueSimpleOUT;
	}



	/**
	 * @return the valueMoyenOUT
	 */
	public int getValueMoyenOUT() {
		return valueMoyenOUT;
	}



	/**
	 * @param valueMoyenOUT the valueMoyenOUT to set
	 */
	public void setValueMoyenOUT(int valueMoyenOUT) {
		this.valueMoyenOUT = valueMoyenOUT;
	}



	/**
	 * @return the valueComplexOUT
	 */
	public int getValueComplexOUT() {
		return valueComplexOUT;
	}



	/**
	 * @param valueComplexOUT the valueComplexOUT to set
	 */
	public void setValueComplexOUT(int valueComplexOUT) {
		this.valueComplexOUT = valueComplexOUT;
	}

	/**
	 * @return the valueAbaChgHomJour
	 */
	public float getValueAbaChgHomJour() {
		return valueAbaChgHomJour;
	}



	/**
	 * @param valueAbaChgHomJour the valueAbaChgHomJour to set
	 */
	public void setValueAbaChgHomJour(float valueAbaChgHomJour) {
		this.valueAbaChgHomJour = valueAbaChgHomJour;
	}

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((functionalId == null) ? 0 : functionalId.hashCode());
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
      EstimationComponentDetailDTO other = (EstimationComponentDetailDTO) obj;
      if (functionalId == null) {
         if (other.functionalId != null) {
            return false;
         }
      } else if (!functionalId.equals(other.functionalId)) {
         return false;
      }
      return true;
   }

   @Override
   public String toString() {
      return "EstimationComponentDetailDTO [id=" + id + ", functionalId=" + functionalId
            + ", valueSimpleGDI=" + valueSimpleGDI + ", valueMoyenGDI=" + valueMoyenGDI
            + ", valueComplexGDI=" + valueComplexGDI + ", valueSimpleGDE=" + valueSimpleGDE
            + ", valueMoyenGDE=" + valueMoyenGDE + ", valueComplexGDE=" + valueComplexGDE
            + ", valueSimpleIN=" + valueSimpleIN + ", valueMoyenIN=" + valueMoyenIN
            + ", valueComplexIN=" + valueComplexIN + ", valueSimpleINT=" + valueSimpleINT
            + ", valueMoyenINT=" + valueMoyenINT + ", valueComplexINT=" + valueComplexINT
            + ", valueSimpleOUT=" + valueSimpleOUT + ", valueMoyenOUT=" + valueMoyenOUT
            + ", valueComplexOUT=" + valueComplexOUT + ", valueAbaChgHomJour=" + valueAbaChgHomJour + "]";
   }


}
