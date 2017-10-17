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

/**
 * @author qsivan
 */
public class ComponentDTO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String composantLibelle;
	/**
	 * Use in estimation view to know which component has been update
	 */
	private ComponentEnum component;
	private float composantPourcentageApplication;
	private int composantValeurSimple;
	private int composantValeurMoyen;
	private int composantValeurComplexe;
	private String functionalId;
	
	
	public String getComposantLibelle() {
		return composantLibelle;
	}
	public void setComposantLibelle(String composantLibelle) {
		this.composantLibelle = composantLibelle;
	}
	public float getSimplifiedPercentForEstimation() {
		return composantPourcentageApplication;
	}
	public void setSimplifiedPercentForEstimation(
			float composantPourcentageApplication) {
		this.composantPourcentageApplication = composantPourcentageApplication;
	}
	public int getComposantValeurSimple() {
		return composantValeurSimple;
	}
	public void setComposantValeurSimple(int composantValeurSimple) {
		this.composantValeurSimple = composantValeurSimple;
	}
	public int getComposantValeurMoyen() {
		return composantValeurMoyen;
	}
	public void setComposantValeurMoyen(int composantValeurMoyen) {
		this.composantValeurMoyen = composantValeurMoyen;
	}
	public int getComposantValeurComplexe() {
		return composantValeurComplexe;
	}
	public void setComposantValeurComplexe(int composantValeurComplexe) {
		this.composantValeurComplexe = composantValeurComplexe;
	}
	
   /**
	 * @return the component
	 */
	public ComponentEnum getComponent() {
		return component;
	}
	/**
	 * @param component the component to set
	 */
	public void setComponent(ComponentEnum component) {
		this.component = component;
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
	
	
}
