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
package org.novaforge.forge.tools.managementmodule.internal.transfer;

import org.novaforge.forge.tools.managementmodule.domain.transfer.IterationTaskIndicators;

import java.io.Serializable;

/**
 * @author BILET-JC
 */
public class IterationTaskIndicatorsImpl implements Serializable, IterationTaskIndicators
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 403645096894408888L;

	/** The reestimate time in days */
	private float						 reestimate;
	/** The advancement percentage */
	private float						 advancement;
	/** The estimation error percentage */
	private float						 errorEstimation;

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.internal.transfer.IterationTaskIndicators#getReestimate()
	 */
	@Override
	public float getReestimate()
	{
		return reestimate;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.internal.transfer.IterationTaskIndicators#setReestimate(
	 * float)
	 */
	@Override
	public void setReestimate(final float reestimate)
	{
		this.reestimate = reestimate;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.internal.transfer.IterationTaskIndicators#getAdvancement()
	 */
	@Override
	public float getAdvancement()
	{
		return advancement;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.internal.transfer.IterationTaskIndicators#setAdvancement
	 * (float)
	 */
	@Override
	public void setAdvancement(final float advancement)
	{
		this.advancement = advancement;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.internal.transfer.IterationTaskIndicators#getErrorEstimation
	 * ()
	 */
	@Override
	public float getErrorEstimation()
	{
		return errorEstimation;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * org.novaforge.forge.plugins.managementmodule.internal.transfer.IterationTaskIndicators#setErrorEstimation
	 * (float)
	 */
	@Override
	public void setErrorEstimation(final float errorEstimation)
	{
		this.errorEstimation = errorEstimation;
	}

}
