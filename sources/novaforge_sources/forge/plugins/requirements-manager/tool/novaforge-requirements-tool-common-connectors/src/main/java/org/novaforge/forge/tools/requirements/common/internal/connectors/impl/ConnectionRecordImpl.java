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
package org.novaforge.forge.tools.requirements.common.internal.connectors.impl;

import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.tools.requirements.common.model.connectors.ConnectionRecord;

import java.util.Date;
import java.util.Locale;

/**
 * @author sbenoist
 */
public class ConnectionRecordImpl implements ConnectionRecord
{
	private Date			 date;

	private String		 actor;

	private EventType	type;

	private EventLevel level;

	private String		 details;

	public ConnectionRecordImpl(final Date pDate, final EventType pType, final EventLevel pLevel)
	{
		super();
		date = pDate;
		type = pType;
		level = pLevel;
	}

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
	public String getActor()
	{
		return actor;
	}

	@Override
	public void setActor(final String pActor)
	{
		actor = pActor;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getType(final Locale pLocale)
	{
		return type.getLabel(pLocale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLevel(final Locale pLocale)
	{
		return level.getLabel(pLocale);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDetails()
	{
		return details;
	}

	@Override
	public void setDetails(final String pDetails)
	{
		details = pDetails;
	}

	@Override
	public String toString()
	{
		return "ConnectionRecordImpl [date=" + date + ", actor=" + actor + ", type=" + type.getLabel() + ", level=" + level
																																																											.getLabel()
							 + ", details=" + details + "]";
	}

}
