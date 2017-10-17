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
package org.novaforge.forge.tools.requirements.common.internal.connectors;

import org.novaforge.forge.commons.technical.historization.exceptions.HistorizationException;
import org.novaforge.forge.commons.technical.historization.model.Event;
import org.novaforge.forge.commons.technical.historization.model.EventType;
import org.novaforge.forge.commons.technical.historization.services.HistorizationService;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementConnectorException;
import org.novaforge.forge.tools.requirements.common.internal.connectors.impl.ConnectionRecordImpl;
import org.novaforge.forge.tools.requirements.common.model.connectors.ConnectionRecord;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sbenoist This class is responsible for handling history for Requirements Connectors
 */
public abstract class AbstractRequirementConnector
{
	public List<ConnectionRecord> getHistory(final String pProjectId, final Date pBeginDate, final Date pEndDate)
			throws RequirementConnectorException
	{
		List<ConnectionRecord> records = new ArrayList<ConnectionRecord>();
		records.addAll(getHistory(pProjectId, getEventType(), pBeginDate, pEndDate));

		return records;
	}

	private List<ConnectionRecord> getHistory(final String pProjectId, final EventType pType,
			final Date pBeginDate, final Date pEndDate) throws RequirementConnectorException
	{
		List<ConnectionRecord> records = new ArrayList<ConnectionRecord>();

		Map<String, Object> equalCriterias = new HashMap<String, Object>();
		equalCriterias.put(HistorizationService.TYPE_SEARCH_CRITERIA_KEY, pType);

		Map<String, Object> likeCriterias = new HashMap<String, Object>();
		likeCriterias.put(HistorizationService.DETAILS_SEARCH_CRITERIA_KEY, "projectId=" + pProjectId);

		try
		{
			List<Event> events = getHistorizationService().findEventsByCriterias(likeCriterias, equalCriterias,
					pBeginDate, pEndDate, 0, 0);

			if (events != null)
			{
				for (Event event : events)
				{
					records.add(toConnectionRecord(event));
				}
			}
		}
		catch (HistorizationException e)
		{
			throw new RequirementConnectorException(String.format(
					"an error occurred during searching synchronization events from historization for projectId=%s",
					pProjectId), e);
		}

		return records;
	}

	protected abstract EventType getEventType();

	protected abstract HistorizationService getHistorizationService();

	private ConnectionRecord toConnectionRecord(final Event pEvent)
	{
		ConnectionRecord record = new ConnectionRecordImpl(pEvent.getDate(), pEvent.getType(), pEvent.getLevel());
		record.setActor(pEvent.getActor());
		record.setDetails(pEvent.getDetails());
		return record;
	}
}
