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
package org.novaforge.forge.tools.managementmodule.entity;

import org.novaforge.forge.tools.managementmodule.domain.RefCalendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ref_calendar")
public class RefCalendarEntity implements RefCalendar, Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1195552672909423624L;

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long							id;

	@Column(name = "date", nullable = false, unique = true, updatable = false)
	private Date							date;

	@Column(name = "dayoff", nullable = false, unique = true)
	private String						dayoff;

	@Override
	public Long getId()
	{
		return id;
	}

	@Override
	public Date getDate()
	{
		return date;
	}

	@Override
	public void setDate(final Date date)
	{
		this.date = date;
	}

	@Override
	public String getDayoff()
	{
		return dayoff;
	}

	@Override
	public void setDayoff(final String dayoff)
	{
		this.dayoff = dayoff;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((dayoff == null) ? 0 : dayoff.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
		{
			return true;
		}
		if (obj == null)
		{
			return false;
		}
		if (getClass() != obj.getClass())
		{
			return false;
		}
		RefCalendarEntity other = (RefCalendarEntity) obj;
		if (date == null)
		{
			if (other.date != null)
			{
				return false;
			}
		}
		else if (!date.equals(other.date))
		{
			return false;
		}
		if (dayoff == null)
		{
			if (other.dayoff != null)
			{
				return false;
			}
		}
		else if (!dayoff.equals(other.dayoff))
		{
			return false;
		}
		if (id == null)
		{
			if (other.id != null)
			{
				return false;
			}
		}
		else if (!id.equals(other.id))
		{
			return false;
		}
		return true;
	}

	@Override
	public String toString()
	{
		return "RefCalendarEntity [id=" + id + ", date=" + date + ", dayoff=" + dayoff + "]";
	}

}
