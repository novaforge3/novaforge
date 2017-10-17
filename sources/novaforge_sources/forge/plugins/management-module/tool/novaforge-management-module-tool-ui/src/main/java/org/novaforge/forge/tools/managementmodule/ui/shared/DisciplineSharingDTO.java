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
 * @author BILET-JC
 * 
 */
public class DisciplineSharingDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2972031389721943557L;

	private String lotName;
	private String sousLotName;
	   private String             scopeUnitId;
	private String scopeUnitName;
	private Integer charge;
	private Float architectureDesign;
	private Float businessModeling;
	private Float changeDriving;
	private Float configurationManagement;
	private Float implementation;
	private Float projectManagement;
	private Float qualityAssurance;
	private Float receipts;
	private Float requirementsAnalysis;
	
	/**
	 * @return the lotName
	 */
	public String getLotName() {
		return lotName;
	}
	/**
	 * @param lotName the lotName to set
	 */
	public void setLotName(String lotName) {
		this.lotName = lotName;
	}

	/**
	 * @return the sousLotName
	 */
	public String getSousLotName() {
		return sousLotName;
	}
	/**
	 * @param sousLotName the sousLotName to set
	 */
	public void setSousLotName(String sousLotName) {
		this.sousLotName = sousLotName;
	}
	/**
	 * @return the scopeUnitId
	 */
	public String getScopeUnitId() {
		return scopeUnitId;
	}
	/**
	 * @param scopeUnitId the unitId to set
	 */
	public void setScopeUnitId(String scopeUnitId) {
		this.scopeUnitId = scopeUnitId;
	}
	/**
	 * @return the scopeUnitName
	 */
	public String getScopeUnitName() {
		return scopeUnitName;
	}
	/**
	 * @param scopeUnitName the scopeUnitName to set
	 */
	public void setScopeUnitName(String scopeUnitName) {
		this.scopeUnitName = scopeUnitName;
	}
	/**
	 * @return the charge
	 */
	public Integer getCharge() {
		return charge;
	}
	/**
	 * @param charge the charge to set
	 */
	public void setCharge(Integer charge) {
		this.charge = charge;
	}
	/**
	 * @return the architectureDesign
	 */
	public Float getArchitectureDesign() {
		return architectureDesign;
	}
	/**
	 * @param architectureDesign the architectureDesign to set
	 */
	public void setArchitectureDesign(Float architectureDesign) {
		this.architectureDesign = architectureDesign;
	}
	/**
	 * @return the businessModeling
	 */
	public Float getBusinessModeling() {
		return businessModeling;
	}
	/**
	 * @param businessModeling the businessModeling to set
	 */
	public void setBusinessModeling(Float businessModeling) {
		this.businessModeling = businessModeling;
	}
	/**
	 * @return the changeDriving
	 */
	public Float getChangeDriving() {
		return changeDriving;
	}
	/**
	 * @param changeDriving the changeDriving to set
	 */
	public void setChangeDriving(Float changeDriving) {
		this.changeDriving = changeDriving;
	}
	/**
	 * @return the configurationManagement
	 */
	public Float getConfigurationManagement() {
		return configurationManagement;
	}
	/**
	 * @param configurationManagement the configurationManagement to set
	 */
	public void setConfigurationManagement(Float configurationManagement) {
		this.configurationManagement = configurationManagement;
	}
	/**
	 * @return the implementation
	 */
	public Float getImplementation() {
		return implementation;
	}
	/**
	 * @param implementation the implementation to set
	 */
	public void setImplementation(Float implementation) {
		this.implementation = implementation;
	}
	/**
	 * @return the projectManagement
	 */
	public Float getProjectManagement() {
		return projectManagement;
	}
	/**
	 * @param projectManagement the projectManagement to set
	 */
	public void setProjectManagement(Float projectManagement) {
		this.projectManagement = projectManagement;
	}
	/**
	 * @return the qualityAssurance
	 */
	public Float getQualityAssurance() {
		return qualityAssurance;
	}
	/**
	 * @param qualityAssurance the qualityAssurance to set
	 */
	public void setQualityAssurance(Float qualityAssurance) {
		this.qualityAssurance = qualityAssurance;
	}
	/**
	 * @return the receipts
	 */
	public Float getReceipts() {
		return receipts;
	}
	/**
	 * @param receipts the receipts to set
	 */
	public void setReceipts(Float receipts) {
		this.receipts = receipts;
	}
	/**
	 * @return the requirementsAnalysis
	 */
	public Float getRequirementsAnalysis() {
		return requirementsAnalysis;
	}
	/**
	 * @param requirementsAnalysis the requirementsAnalysis to set
	 */
	public void setRequirementsAnalysis(Float requirementsAnalysis) {
		this.requirementsAnalysis = requirementsAnalysis;
	}
	
	
}
