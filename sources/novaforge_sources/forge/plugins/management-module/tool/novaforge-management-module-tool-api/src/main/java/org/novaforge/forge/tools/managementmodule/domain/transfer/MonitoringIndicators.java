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

public interface MonitoringIndicators {

	/**
	 * Get the activeUsersNumber
	 * 
	 * @return the activeUsersNumber
	 */
	int getActiveUsersNumber();

	/**
	 * Set the activeUsersNumber
	 * 
	 * @param activeUsersNumber
	 *            the activeUsersNumber to set
	 */
	void setActiveUsersNumber(int activeUsersNumber);

	/**
	 * Return the estimate
	 * 
	 * @return the estimate
	 */
	Float getEstimate();

	/**
	 * Set the estimate
	 * 
	 * @param estimate
	 *            the estimate to set
	 */
	void setEstimate(Float estimate);

	/**
	 * Get the consumed
	 * 
	 * @return the consumed
	 */
	float getConsumed();

	/**
	 * Set the consumed
	 * 
	 * @param consumed
	 *            the consumed to set
	 */
	void setConsumed(float consumed);

	/**
	 * Return the remaining
	 * 
	 * @return the remaining
	 */
	Float getRemaining();

	/**
	 * Set the remaining
	 * 
	 * @param remaining
	 *            the remaining to set
	 */
	void setRemaining(Float remaining);

	/**
	 * Return he reestimate
	 * 
	 * @return the reestimate
	 */
	Float getReestimate();

	/**
	 * Set the reestimate
	 * 
	 * @param reestimate
	 *            the reestimate to set
	 */
	void setReestimate(Float reestimate);

	/**
	 * Get the focalisation
	 * 
	 * @return the focalisation
	 */
	float getFocalisation();

	/**
	 * Set the focalisation
	 * 
	 * @param focalisation
	 *            the focalisation to set
	 */
	void setFocalisation(float focalisation);

	/**
	 * Get the velocity
	 * 
	 * @return the velocity
	 */
	float getVelocity();

	/**
	 * Set the velocity
	 * 
	 * @param velocity
	 *            the velocity to set
	 */
	void setVelocity(float velocity);

	/**
	 * Get the AverageEstimationError
	 * 
	 * @return the errorStandardDeviation
	 */
	float getAverageEstimationError();

	/**
	 * Set the AverageEstimationError
	 * 
	 * @param errorStandardDeviation
	 *            the errorStandardDeviation to set
	 */
	void setAverageEstimationError(float errorStandardDeviation);

	/**
	 * Get the advancement
	 * 
	 * @return the advancement
	 */
	float getAdvancement();

	/**
	 * Set the advancement
	 * 
	 * @param advancement
	 *            the advancement to set
	 */
	void setAdvancement(float advancement);

}