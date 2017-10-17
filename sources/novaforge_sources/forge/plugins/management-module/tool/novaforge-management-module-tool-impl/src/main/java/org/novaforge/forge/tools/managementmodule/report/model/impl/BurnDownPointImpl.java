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

import org.novaforge.forge.tools.managementmodule.domain.report.BurnDownPoint;

import java.util.Date;

public class BurnDownPointImpl implements BurnDownPoint
{

	/**
    * 
    */
	private static final long serialVersionUID = -2589539622544262455L;

	/**
	 * The real endDate of the task (empty if no date)
	 * 
	 * @see BurnDownPointImpl#getEndDate()
	 * @see BurnDownPointImpl#setEndDate(Date)
	 */
	private Date							date;

	/**
	 * The load associated
	 * 
	 * @see BurnDownPointImpl#getPrevisionnalRemainingLoad()
	 * @see BurnDownPointImpl#setPrevisionnalRemainingLoad(Float)
	 */
	private Float						 previsionnalRemainingLoad;

	/**
	 * The load associated
	 * 
	 * @see BurnDownPointImpl#getRealRemainingLoad()
	 * @see BurnDownPointImpl#setRealRemainingLoad(Float)
	 */
	private Float						 realRemainingLoad;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Date getDate()
	{
		return date;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setDate(final Date date)
	{
		this.date = date;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float getPrevisionnalRemainingLoad()
	{
		return previsionnalRemainingLoad;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setPrevisionnalRemainingLoad(final Float load)
	{
		previsionnalRemainingLoad = load;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Float getRealRemainingLoad()
	{
		return realRemainingLoad;
	}

	@Override
	public void setRealRemainingLoad(final Float load)
	{
		realRemainingLoad = load;

	}

}
