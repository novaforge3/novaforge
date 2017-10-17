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
package org.novaforge.forge.tools.managementmodule.report.model.impl;

import org.novaforge.forge.tools.managementmodule.domain.report.FocalisationFactorIt;

import java.util.Date;

public class FocalisationFactorItImpl implements FocalisationFactorIt
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6709673949003643533L;

	private Float						 focusingFactor;

	private String						iterationName;

	private Float						 velocity;

	private Integer					 nombreRessources;

	private Date							beginingDate;

	private Float						 focusingFactorIdeal;

	private Float						 focusingFactorMedian;

	@Override
	public Float getFocusingFactor()
	{
		return focusingFactor;
	}

	@Override
	public void setFocusingFactor(final Float focusingFactor)
	{
		this.focusingFactor = focusingFactor;
	}

	@Override
	public String getIterationName()
	{
		return iterationName;
	}

	@Override
	public void setIterationName(final String iterationName)
	{
		this.iterationName = iterationName;
	}

	@Override
	public Float getVelocity()
	{
		return velocity;
	}

	@Override
	public void setVelocity(final Float velocity)
	{
		this.velocity = velocity;
	}

	@Override
	public Integer getNombreRessources()
	{
		return nombreRessources;
	}

	@Override
	public void setNombreRessources(final Integer nombreRessources)
	{
		this.nombreRessources = nombreRessources;
	}

	@Override
	public Date getBeginingDate()
	{
		return beginingDate;
	}

	@Override
	public void setBeginingDate(final Date beginingDate)
	{
		this.beginingDate = beginingDate;
	}

	@Override
	public Float getFocusingFactorIdeal()
	{
		return focusingFactorIdeal;
	}

	@Override
	public void setFocusingFactorIdeal(final Float focusingFactorIdeal)
	{
		this.focusingFactorIdeal = focusingFactorIdeal;
	}

	@Override
	public Float getFocusingFactorMedian()
	{
		return focusingFactorMedian;
	}

	@Override
	public void setFocusingFactorMedian(final Float focusingFactorMedian)
	{
		this.focusingFactorMedian = focusingFactorMedian;
	}

	@Override
	public String toString()
	{
		return "FocalisationFactorItImpl [focusingFactor=" + focusingFactor + ", iterationName=" + iterationName
				+ ", velocity=" + velocity + ", nombreRessources=" + nombreRessources + ", beginingDate="
				+ beginingDate + ", focusingFactorIdeal=" + focusingFactorIdeal + ", focusingFactorMedian="
				+ focusingFactorMedian + "]";
	}

}
