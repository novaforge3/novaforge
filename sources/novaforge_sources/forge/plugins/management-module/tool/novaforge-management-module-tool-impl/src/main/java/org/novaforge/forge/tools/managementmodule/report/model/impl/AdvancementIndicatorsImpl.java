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
package org.novaforge.forge.tools.managementmodule.report.model.impl;

import org.novaforge.forge.tools.managementmodule.domain.report.AdvancementIndicators;

/**
 * This object is a regroupement of differents datas about project's advancement for one given iteration
 * 
 * @author BILET-JC
 */
public class AdvancementIndicatorsImpl implements AdvancementIndicators
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -636402582150939494L;
	/* estimate = velocity */
	private Float						 estimate;
	private Float						 consumed;
	private Float						 remaining;
	/* reestimate = remaining + consumed */
	private Float						 reestimate;
	/* focalisationFactor = velocity / consumed */
	private Float						 focalisationFactor;
	/* estimationError = estimate / reestimate */
	private Float						 estimationError;

	/**
	 * 
	 */
	public AdvancementIndicatorsImpl()
	{
		super();
	}

	/**
	 * @return the estimate
	 */
	@Override
	public Float getEstimate()
	{
		return estimate;
	}

	/**
	 * @param estimate
	 *          the estimate to set
	 */
	@Override
	public void setEstimate(final Float estimate)
	{
		this.estimate = estimate;
	}

	/**
	 * @return the velocity
	 */
	@Override
	public Float getVelocity()
	{
		return estimate;
	}

	/**
	 * @param velocity
	 *          the velocity to set
	 */
	@Override
	public void setVelocity(final Float velocity)
	{
		this.estimate = velocity;

	}

	/**
	 * @return the consumed
	 */
	@Override
	public Float getConsumed()
	{
		return consumed;
	}

	/**
	 * @param consumed
	 *          the consumed to set
	 */
	@Override
	public void setConsumed(final Float consumed)
	{
		this.consumed = consumed;
	}

	/**
	 * @return the remaining
	 */
	@Override
	public Float getRemaining()
	{
		return remaining;
	}

	/**
	 * @param remaining
	 *          the remaining to set
	 */
	@Override
	public void setRemaining(final Float remaining)
	{
		this.remaining = remaining;
	}

	/**
	 * @return the reestimate
	 */
	@Override
	public Float getReestimate()
	{
		return reestimate;
	}

	/**
	 * @param reestimate
	 *          the reestimate to set
	 */
	@Override
	public void setReestimate(final Float reestimate)
	{
		this.reestimate = reestimate;
	}

	/**
	 * @return the focalisationFactor
	 */
	@Override
	public Float getFocalisationFactor()
	{
		return focalisationFactor;
	}

	/**
	 * @param focalisationFactor
	 *          the focalisationFactor to set
	 */
	@Override
	public void setFocalisationFactor(final Float focalisationFactor)
	{
		this.focalisationFactor = focalisationFactor;
	}

	/**
	 * @return the estimationError
	 */
	@Override
	public Float getEstimationError()
	{
		return estimationError;
	}

	/**
	 * @param estimationError
	 *          the estimationError to set
	 */
	@Override
	public void setEstimationError(final Float estimationError)
	{
		this.estimationError = estimationError;
	}

	@Override
	public String toString()
	{
		return "AdvancementIndicatorsImpl [estimate=" + estimate + ", consumed=" + consumed + ", remaining="
				+ remaining + ", reestimate=" + reestimate + ", focalisationFactor=" + focalisationFactor
				+ ", estimationError=" + estimationError + "]";
	}

}
