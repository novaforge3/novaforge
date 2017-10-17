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

public interface EstimationComponentSimple extends Serializable{

	/**
	 * @return the idProjet
	 */
	String getIdProjet();

	/**
	 * @param idProjet the idProjet to set
	 */
	void setIdProjet(String idProjet);

	/**
	 * @return the valueGDI
	 */
	Float getValueGDI();

	/**
	 * @param valueGDI the valueGDI to set
	 */
	void setValueGDI(Float valueGDI);

	/**
	 * @return the valueINT
	 */
	Float getValueINT();

	/**
	 * @param valueINT the valueINT to set
	 */
	void setValueINT(Float valueINT);

	/**
	 * @return the valueENT
	 */
	Float getValueENT();

	/**
	 * @param valueENT the valueENT to set
	 */
	void setValueENT(Float valueENT);

	/**
	 * @return the valueSOR
	 */
	Float getValueSOR();

	/**
	 * @param valueSOR the valueSOR to set
	 */
	void setValueSOR(Float valueSOR);

	/**
	 * @return the valueGDE
	 */
	Float getValueGDE();

	/**
	 * @param valueGDE the valueGDE to set
	 */
	void setValueGDE(Float valueGDE);
	
	/**
	 * clone this Object
	 * @return
	 * @throws CloneNotSupportedException
	 */
	Object clone() throws CloneNotSupportedException;

	Long getId();

}