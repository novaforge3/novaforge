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

package org.novaforge.forge.tools.managementmodule.domain;

import java.io.Serializable;

public interface EstimationComponentDetail extends Serializable, Cloneable {
   
	/**
	 * @return the valueSimpleGDI
	 */
	int getValueSimpleGDI();

	/**
	 * @param valueSimpleGDI the valueSimpleGDI to set
	 */
	void setValueSimpleGDI(int valueSimpleGDI);

	/**
	 * @return the valueMoyenGDI
	 */
	int getValueMoyenGDI();

	/**
	 * @param valueMoyenGDI the valueMoyenGDI to set
	 */
	void setValueMoyenGDI(int valueMoyenGDI);

	/**
	 * @return the valueComplexGDI
	 */
	int getValueComplexGDI();

	/**
	 * @param valueComplexGDI the valueComplexGDI to set
	 */
	void setValueComplexGDI(int valueComplexGDI);

	/**
	 * @return the valueSimpleGDE
	 */
	int getValueSimpleGDE();

	/**
	 * @param valueSimpleGDE the valueSimpleGDE to set
	 */
	void setValueSimpleGDE(int valueSimpleGDE);

	/**
	 * @return the valueMoyenGDE
	 */
	int getValueMoyenGDE();

	/**
	 * @param valueMoyenGDE the valueMoyenGDE to set
	 */
	void setValueMoyenGDE(int valueMoyenGDE);

	/**
	 * @return the valueComplexGDE
	 */
	int getValueComplexGDE();

	/**
	 * @param valueComplexGDE the valueComplexGDE to set
	 */
	void setValueComplexGDE(int valueComplexGDE);

	/**
	 * @return the valueSimpleIN
	 */
	int getValueSimpleIN();

	/**
	 * @param valueSimpleIN the valueSimpleIN to set
	 */
	void setValueSimpleIN(int valueSimpleIN);

	/**
	 * @return the valueMoyenIN
	 */
	int getValueMoyenIN();

	/**
	 * @param valueMoyenIN the valueMoyenIN to set
	 */
	void setValueMoyenIN(int valueMoyenIN);

	/**
	 * @return the valueComplexIN
	 */
	int getValueComplexIN();

	/**
	 * @param valueComplexIN the valueComplexIN to set
	 */
	void setValueComplexIN(int valueComplexIN);

	/**
	 * @return the valueSimpleINT
	 */
	int getValueSimpleINT();

	/**
	 * @param valueSimpleINT the valueSimpleINT to set
	 */
	void setValueSimpleINT(int valueSimpleINT);

	/**
	 * @return the valueMoyenINT
	 */
	int getValueMoyenINT();

	/**
	 * @param valueMoyenINT the valueMoyenINT to set
	 */
	void setValueMoyenINT(int valueMoyenINT);

	/**
	 * @return the valueComplexINT
	 */
	int getValueComplexINT();

	/**
	 * @param valueComplexINT the valueComplexINT to set
	 */
	void setValueComplexINT(int valueComplexINT);

	/**
	 * @return the valueSimpleOUT
	 */
	int getValueSimpleOUT();

	/**
	 * @param valueSimpleOUT the valueSimpleOUT to set
	 */
	void setValueSimpleOUT(int valueSimpleOUT);

	/**
	 * @return the valueMoyenOUT
	 */
	int getValueMoyenOUT();

	/**
	 * @param valueMoyenOUT the valueMoyenOUT to set
	 */
	void setValueMoyenOUT(int valueMoyenOUT);

	/**
	 * @return the valueComplexOUT
	 */
	int getValueComplexOUT();

	/**
	 * @param valueComplexOUT the valueComplexOUT to set
	 */
	void setValueComplexOUT(int valueComplexOUT);

	/**
	 * @return the valueAbaChgHomJour
	 */
	float getValueAbaChgHomJour();

	/**
	 * @param valueAbaChgHomJour the valueAbaChgHomJour to set
	 */
	void setValueAbaChgHomJour(float valueAbaChgHomJour);

	/**
	 * @return the adjustementCoef
	 */
	float getAdjustementCoef();

	/**
	 * @param adjustementCoef the adjustementCoef to set
	 */
	void setAdjustementCoef(float adjustementCoef);

   /**
    * {@inheritDoc}
    */
   Object clone();

   /**
    * Get the id
    * @return the id
    */
   Long getId();


   /**
    * Get the functionalId
    * @return the functionalId
    */
   String getFunctionalId();

   /**
    * Set the functionalId
    * @param functionalId the functionalId to set
    */
   void setFunctionalId(String functionalId);

}
