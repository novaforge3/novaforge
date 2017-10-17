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

import org.novaforge.forge.tools.managementmodule.domain.transfer.GlobalMonitoringIndicators;

import java.io.Serializable;

public class GlobalMonitoringIndicatorsImpl implements Serializable, GlobalMonitoringIndicators
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3048886165874408147L;

	/** The start date of the first iteration of the project */
	private String						projectStartDate;

	// TODO JCB comment
	private Float						 velocity;

	// TODO JCB comment
	private Float						 focalisationFactor;

	// TODO JCB comment
	private Float						 estimationError;

	// TODO JCB comment
	private Float						 lastCountFP;

	/**
	 * @return the projectStartDate
	 */
	@Override
	public String getProjectStartDate()
	{
		return projectStartDate;
	}

	/**
	 * @param projectStartDate
	 *          the projectStartDate to set
	 */
	@Override
	public void setProjectStartDate(final String projectStartDate)
	{
		this.projectStartDate = projectStartDate;
	}

	/**
	 * @return the velocity
	 */
	@Override
	public Float getVelocity()
	{
		return velocity;
	}

	/**
	 * @param velocity
	 *          the velocity to set
	 */
	@Override
	public void setVelocity(final Float velocity)
	{
		this.velocity = velocity;
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

	/**
	 * @return the lastCountFP
	 */
	@Override
	public Float getLastCountFP()
	{
		return lastCountFP;
	}

	/**
	 * @param lastCountFP
	 *          the lastCountFP to set
	 */
	@Override
	public void setLastCountFP(final Float lastCountFP)
	{
		this.lastCountFP = lastCountFP;
	}

	@Override
	public String toString()
	{
		return "GlobalMonitoringIndicatorsImpl [projectStartDate=" + projectStartDate + ", velocity=" + velocity
				+ ", focalisationFactor=" + focalisationFactor + ", estimationError=" + estimationError
				+ ", lastCountFP=" + lastCountFP + "]";
	}

}
