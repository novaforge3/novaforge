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
package org.novaforge.forge.tools.managementmodule.domain.transfer;

public interface GlobalMonitoringIndicators {

	/**
	 * @return the projectStartDate
	 */
	String getProjectStartDate();

	/**
	 * @param projectStartDate the projectStartDate to set
	 */
	void setProjectStartDate(String projectStartDate);

	/**
	 * @return the velocity
	 */
	Float getVelocity();

	/**
	 * @param velocity the velocity to set
	 */
	void setVelocity(Float velocity);

	/**
	 * @return the focalisationFactor
	 */
	Float getFocalisationFactor();

	/**
	 * @param focalisationFactor the focalisationFactor to set
	 */
	void setFocalisationFactor(Float focalisationFactor);

	/**
	 * @return the estimationError
	 */
	Float getEstimationError();

	/**
	 * @param estimationError the estimationError to set
	 */
	void setEstimationError(Float estimationError);

	/**
	 * @return the lastCountFP
	 */
	Float getLastCountFP();

	/**
	 * @param lastCountFP the lastCountFP to set
	 */
	void setLastCountFP(Float lastCountFP);

}