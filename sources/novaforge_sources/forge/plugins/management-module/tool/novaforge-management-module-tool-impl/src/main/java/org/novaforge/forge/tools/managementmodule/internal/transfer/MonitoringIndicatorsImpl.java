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

import org.novaforge.forge.tools.managementmodule.domain.transfer.MonitoringIndicators;

import java.io.Serializable;

/**
 * The transfer object for monitoring indicators
 */
public class MonitoringIndicatorsImpl implements Serializable, MonitoringIndicators
{

	/** UID for serialization */
	private static final long serialVersionUID = 4469716264639163626L;

	/** The number of people which have worked */
	private int							 activeUsersNumber;

	/** The initial estimation time in days */
	private float						 estimate;

	/** The total consumed time in days */
	private float						 consumed;

	/** The remaining time in days */
	private float						 remaining;

	/** The reestimate time in days */
	private float						 reEstimate;

	/** The average focalisation factor */
	private float						 focalisation;

	/** The average velocity */
	private float						 velocity;

	/** The error standard deviation */
	private float						 errorStandardDeviation;

	/** The advancement in % */
	private float						 advancement;

	/**
	 * Get the activeUsersNumber
	 * 
	 * @return the activeUsersNumber
	 */
	@Override
	public int getActiveUsersNumber()
	{
		return activeUsersNumber;
	}

	/**
	 * Set the activeUsersNumber
	 * 
	 * @param activeUsersNumber
	 *          the activeUsersNumber to set
	 */
	@Override
	public void setActiveUsersNumber(final int activeUsersNumber)
	{
		this.activeUsersNumber = activeUsersNumber;
	}

	@Override
	public Float getEstimate()
	{
		return estimate;
	}

	@Override
	public void setEstimate(final Float estimate)
	{
		this.estimate = estimate;
	}

	/**
	 * Get the consumed
	 * 
	 * @return the consumed
	 */
	@Override
	public float getConsumed()
	{
		return consumed;
	}

	/**
	 * Set the consumed
	 *
	 * @param consumed
	 *     the consumed to set
	 */
	@Override
	public void setConsumed(final float consumed)
	{
		this.consumed = consumed;
	}

	@Override
	public Float getRemaining()
	{
		return remaining;
	}

	@Override
	public void setRemaining(final Float remaining)
	{
		this.remaining = remaining;
	}

	@Override
	public Float getReestimate()
	{
		return reEstimate;
	}

	@Override
	public void setReestimate(final Float reEstimate)
	{
		this.reEstimate = reEstimate;
	}

	/**
	 * Get the focalisation
	 * 
	 * @return the focalisation
	 */
	@Override
	public float getFocalisation()
	{
		return focalisation;
	}

	/**
	 * Set the focalisation
	 * 
	 * @param focalisation
	 *          the focalisation to set
	 */
	@Override
	public void setFocalisation(final float focalisation)
	{
		this.focalisation = focalisation;
	}

	/**
	 * Get the velocity
	 * 
	 * @return the velocity
	 */
	@Override
	public float getVelocity()
	{
		return velocity;
	}

	/**
	 * Set the velocity
	 * 
	 * @param velocity
	 *          the velocity to set
	 */
	@Override
	public void setVelocity(final float velocity)
	{
		this.velocity = velocity;
	}

	/**
	 * Get the errorStandardDeviation
	 * 
	 * @return the errorStandardDeviation
	 */
	@Override
	public float getAverageEstimationError()
	{
		return errorStandardDeviation;
	}

	/**
	 * Set the errorStandardDeviation
	 * 
	 * @param errorStandardDeviation
	 *          the errorStandardDeviation to set
	 */
	@Override
	public void setAverageEstimationError(final float errorStandardDeviation)
	{
		this.errorStandardDeviation = errorStandardDeviation;
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

	@Override
	public String toString()
	{
		return "MonitoringIndicatorsImpl [activeUsersNumber=" + activeUsersNumber + ", consumed=" + consumed
				+ ", focalisation=" + focalisation + ", velocity=" + velocity + ", errorStandardDeviation="
				+ errorStandardDeviation + ", advancement=" + advancement + "]";
	}

}
