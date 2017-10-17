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

import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;

import java.io.Serializable;

public interface ScopeUnitIndicators extends Serializable {

	/**
	 * Get the consumedTime
	 * 
	 * @return the consumedTime
	 */
	float getConsumedTime();

	/**
	 * Set the consumedTime
	 * 
	 * @param consumedTime
	 *            the consumedTime to set
	 */
	void setConsumedTime(float consumedTime);

	/**
	 * Get the remainingTime
	 * 
	 * @return the remainingTime
	 */
	float getRemainingTime();

	/**
	 * Set the remainingTime
	 * 
	 * @param remainingTime
	 *            the remainingTime to set
	 */
	void setRemainingTime(float remainingTime);

	float getRemainingScopeUnit();

	void setRemainingScopeUnit(float remainingScopeUnit);

	/**
	 * Get the reestimate
	 * 
	 * @return the reestimate
	 */
	float getReestimate();

	/**
	 * Set the reestimate
	 * 
	 * @param reestimate
	 *            the reestimate to set
	 */
	void setReestimate(float reestimate);

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

	/**
	 * Get the scopeUnit
	 * 
	 * @return the scopeUnit
	 */
	ScopeUnit getScopeUnit();

	/**
	 * Set the scopeUnit
	 * 
	 * @param scopeUnit
	 *            the scopeUnit to set
	 */
	void setScopeUnit(ScopeUnit scopeUnit);

	boolean isAllScopeUnitDisciplineFinished();

	void setAllScopeUnitDisciplineFinished(boolean allScopeUnitDisciplineFinished);

	boolean isAllTaskfinished();

	void setAllTaskfinished(boolean allTaskfinished);

	boolean isAllChildFinished();

	void setAllChildFinished(boolean allChildFinished);

	/**
	 * Get the periodBeginingEstimation : The sum of the estimation of the tasks
	 * at the beginning of the period (always initialEstimation of task on
	 * global, initial estimation OR previous IterationTask remaining time for
	 * Iteration
	 * 
	 * @return the periodBeginingEstimation
	 */
	float getPeriodBeginningEstimation();

	/**
	 * Set the periodBeginingEstimation
	 * 
	 * @param periodBeginingEstimation
	 *            the periodBeginingEstimation to set
	 */
	void setPeriodBeginningEstimation(float periodBeginingEstimation);

}
