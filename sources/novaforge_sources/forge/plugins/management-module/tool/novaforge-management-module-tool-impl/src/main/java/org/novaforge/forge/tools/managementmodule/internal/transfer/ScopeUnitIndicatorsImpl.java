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
package org.novaforge.forge.tools.managementmodule.internal.transfer;

import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;
import org.novaforge.forge.tools.managementmodule.domain.transfer.ScopeUnitIndicators;

import java.io.Serializable;

/**
 * Implementation of the scope unit indicator
 */
public class ScopeUnitIndicatorsImpl implements ScopeUnitIndicators, Serializable
{

	/** Serial Version UID */
	private static final long serialVersionUID = 8639591252915639714L;

	private float						 consumedTime;
	private float						 remainingTime;
	private float						 remainingScopeUnit;
	private float						 reestimate;
	private float						 advancement;
	private ScopeUnit				 scopeUnit;
	/** true if all attached scopeUnitDiscipline couples are finished */
	private boolean					 allScopeUnitDisciplineFinished;
	/** true if all attached tasks are finished */
	private boolean					 allTaskfinished;
	/** true if all childs scopeUnit are finished */
	private boolean					 allChildFinished;
	/**
	 * The sum of the estimation of the tasks at the begining of the period
	 * (always initialEstimation of task on global, initial estimation OR
	 * previous IterationTask remaining time for Iteration
	 */
	private float						 periodBeginingEstimation;

	/**
	 * Get the consumedTime
	 * 
	 * @return the consumedTime
	 */
	@Override
	public float getConsumedTime()
	{
		return consumedTime;
	}

	/**
	 * Set the consumedTime
	 * 
	 * @param consumedTime
	 *          the consumedTime to set
	 */
	@Override
	public void setConsumedTime(final float consumedTime)
	{
		this.consumedTime = consumedTime;
	}

	/**
	 * Get the remainingTime
	 * 
	 * @return the remainingTime
	 */
	@Override
	public float getRemainingTime()
	{
		return remainingTime;
	}

	/**
	 * Set the remainingTime
	 * 
	 * @param remainingTime
	 *          the remainingTime to set
	 */
	@Override
	public void setRemainingTime(final float remainingTime)
	{
		this.remainingTime = remainingTime;
	}

	/**
	 * @return the remainingScopeUnit
	 */
	@Override
	public float getRemainingScopeUnit()
	{
		return remainingScopeUnit;
	}

	/**
	 * @param remainingScopeUnit
	 *          the remainingScopeUnit to set
	 */
	@Override
	public void setRemainingScopeUnit(final float remainingScopeUnit)
	{
		this.remainingScopeUnit = remainingScopeUnit;
	}

	/**
	 * Get the reestimate
	 * 
	 * @return the reestimate
	 */
	@Override
	public float getReestimate()
	{
		return reestimate;
	}

	/**
	 * Set the reestimate
	 * 
	 * @param reestimate
	 *          the reestimate to set
	 */
	@Override
	public void setReestimate(final float reestimate)
	{
		this.reestimate = reestimate;
	}

	/**
	 * Get the advancement
	 * 
	 * @return the advancement
	 */
	@Override
	public float getAdvancement()
	{
		return advancement;
	}

	/**
	 * Set the advancement
	 * 
	 * @param advancement
	 *          the advancement to set
	 */
	@Override
	public void setAdvancement(final float advancement)
	{
		this.advancement = advancement;
	}

	/**
	 * Get the scopeUnit
	 * 
	 * @return the scopeUnit
	 */
	@Override
	public ScopeUnit getScopeUnit()
	{
		return scopeUnit;
	}

	/**
	 * Set the scopeUnit
	 * 
	 * @param scopeUnit
	 *          the scopeUnit to set
	 */
	@Override
	public void setScopeUnit(final ScopeUnit scopeUnit)
	{
		this.scopeUnit = scopeUnit;
	}

	/**
	 * @return the allScopeUnitDisciplineFinished
	 */
	@Override
	public boolean isAllScopeUnitDisciplineFinished()
	{
		return allScopeUnitDisciplineFinished;
	}

	/**
	 * @param allScopeUnitDisciplineFinished
	 *          the allScopeUnitDisciplineFinished to set
	 */
	@Override
	public void setAllScopeUnitDisciplineFinished(final boolean allScopeUnitDisciplineFinished)
	{
		this.allScopeUnitDisciplineFinished = allScopeUnitDisciplineFinished;
	}

	/**
	 * @return the allTaskfinished
	 */
	@Override
	public boolean isAllTaskfinished()
	{
		return allTaskfinished;
	}

	/**
	 * @param allTaskfinished
	 *          the allTaskfinished to set
	 */
	@Override
	public void setAllTaskfinished(final boolean allTaskfinished)
	{
		this.allTaskfinished = allTaskfinished;
	}

	/**
	 * @return the allChildFinished
	 */
	@Override
	public boolean isAllChildFinished()
	{
		return allChildFinished;
	}

	/**
	 * @param allChildFinished
	 *          the allChildFinished to set
	 */
	@Override
	public void setAllChildFinished(final boolean allChildFinished)
	{
		this.allChildFinished = allChildFinished;
	}

	/**
	 * Get the periodBeginingEstimation
	 * 
	 * @return the periodBeginingEstimation
	 */
	@Override
	public float getPeriodBeginningEstimation()
	{
		return periodBeginingEstimation;
	}

	/**
	 * Set the periodBeginingEstimation
	 * 
	 * @param periodBeginingEstimation
	 *          the periodBeginingEstimation to set
	 */
	@Override
	public void setPeriodBeginningEstimation(final float periodBeginingEstimation)
	{
		this.periodBeginingEstimation = periodBeginingEstimation;
	}

}
