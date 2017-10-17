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
public class GlobalMonitoringIndicatorsDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2090669490542822159L;
	
	/** The start date of the first iteration of the project */
	private String projectStartDate;
	
	//TODO JCB comment
	private Float velocity;

	//TODO JCB comment
	private Float focalisationFactor;

	//TODO JCB comment
	private Float estimationError;

	//TODO JCB comment
	private Float lastCountFP;

	/**
	 * @return the projectStartDate
	 */
	public String getProjectStartDate() {
		return projectStartDate;
	}

	/**
	 * @param projectStartDate the projectStartDate to set
	 */
	public void setProjectStartDate(String projectStartDate) {
		this.projectStartDate = projectStartDate;
	}

	/**
	 * @return the velocity
	 */
	public Float getVelocity() {
		return velocity;
	}

	/**
	 * @param velocity the velocity to set
	 */
	public void setVelocity(Float velocity) {
		this.velocity = velocity;
	}

	/**
	 * @return the focalisationFactor
	 */
	public Float getFocalisationFactor() {
		return focalisationFactor;
	}

	/**
	 * @param focalisationFactor the focalisationFactor to set
	 */
	public void setFocalisationFactor(Float focalisationFactor) {
		this.focalisationFactor = focalisationFactor;
	}

	/**
	 * @return the estimationError
	 */
	public Float getEstimationError() {
		return estimationError;
	}

	/**
	 * @param estimationError the estimationError to set
	 */
	public void setEstimationError(Float estimationError) {
		this.estimationError = estimationError;
	}

	/**
	 * @return the lastCountFP
	 */
	public Float getLastCountFP() {
		return lastCountFP;
	}

	/**
	 * @param lastCountFP the lastCountFP to set
	 */
	public void setLastCountFP(Float lastCountFP) {
		this.lastCountFP = lastCountFP;
	}
	
	
}
