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

import org.novaforge.forge.tools.managementmodule.domain.report.GanttLotInfo;

import java.util.Date;

/**
 * This class is a concrete implementation used to generate a Gantt diagram for the lots
 * 
 * @author falsquelle-e
 */
public class GanttLotInfoImpl implements GanttLotInfo
{
	/**
    * 
    */
	private static final long serialVersionUID = 7899797838855026267L;

	private String						description;

	private Date							startDateLot;

	private Date							endDateLot;

	private Date							startDateSubLot;

	private Date							endDateSubLot;

	private Long							id;

	private Long							order;

	private String						markerName;

	private Date							markerDate;

	@Override
	public String getDescription()
	{
		return description;
	}

	@Override
	public void setDescription(final String description)
	{
		this.description = description;
	}

	@Override
	public Date getStartDateLot()
	{
		return startDateLot;
	}

	@Override
	public void setStartDateLot(final Date startDateLot)
	{
		this.startDateLot = startDateLot;
	}

	@Override
	public Date getEndDateLot()
	{
		return endDateLot;
	}

	@Override
	public void setEndDateLot(final Date endDateLot)
	{
		this.endDateLot = endDateLot;
	}

	@Override
	public Date getStartDateSubLot()
	{
		return startDateSubLot;
	}

	@Override
	public void setStartDateSubLot(final Date startDateSubLot)
	{
		this.startDateSubLot = startDateSubLot;
	}

	@Override
	public Date getEndDateSubLot()
	{
		return endDateSubLot;
	}

	@Override
	public void setEndDateSubLot(final Date endDateSubLot)
	{
		this.endDateSubLot = endDateSubLot;
	}

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public void setId(final Long id)
	{
		this.id = id;
	}

	@Override
	public Long getOrder()
	{
		return order;
	}

	@Override
	public void setOrder(final Long order)
	{
		this.order = order;
	}

	@Override
	public String getMarkerName()
	{
		return markerName;
	}

	@Override
	public void setMarkerName(final String markerName)
	{
		this.markerName = markerName;
	}

	@Override
	public Date getMarkerDate()
	{
		return markerDate;
	}

	@Override
	public void setMarkerDate(final Date date)
	{
		this.markerDate = date;
	}
}