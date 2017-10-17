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
package org.novaforge.forge.commons.technical.historization.dao;

import org.novaforge.forge.commons.technical.historization.model.Event;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public interface EventDAO
{
	/**
	 * This method returns events which occured between begin and end dates
	 * 
	 * @param pBeginDate
	 * @param pEndDate
	 * @return List<Event>
	 */
	List<Event> findEventsByDate(Date pBeginDate, Date pEndDate);

	/**
	 * This method returns events by actor
	 * 
	 * @param pLogin
	 * @return List<Event>
	 */
	List<Event> findEventsByActor(String pLogin);

	/**
	 * This method returns events by status
	 * 
	 * @param pLevel
	 * @return List<Event>
	 */
	List<Event> findEventsByLevel(EventLevel pLevel);

	/**
	 * This method returns events by type
	 * 
	 * @param pType
	 * @return List<Event>
	 */
	List<Event> findEventsByType(EventType pType);

	/**
	 * This method returns events by keyword
	 * 
	 * @param pKeyword
	 * @return List<Event>
	 */
	List<Event> findEventsByKeyword(String pKeyword);

	/**
	 * This method return events by multiple criterias
	 * 
	 * @param likeCriterias
	 * @param equalCriterias
	 * @param pBeginDate
	 * @param pEndDate
	 * @param pFirstResult
	 * @param pMaxResults
	 * @return List<Event>
	 */
	List<Event> findEventsByCriterias(final Map<String, Object> likeCriterias,
	    final Map<String, Object> equalCriterias, final Date pBeginDate, final Date pEndDate, int pFirstResult,
	    int pMaxResults);

	/**
	 * This method returns the number of events found by criterias
	 * 
	 * @param likeCriterias
	 * @param equalCriterias
	 * @param pBeginDate
	 * @param pEndDate
	 * @return number of events
	 */
	int countEventsByCriterias(final Map<String, Object> likeCriterias,
	    final Map<String, Object> equalCriterias, final Date pBeginDate, final Date pEndDate);

	/**
	 * This method allows to save an event
	 * 
	 * @param pEvent
	 * @return Event
	 */
	Event save(Event pEvent);

	/**
	 * This method returns all the events inferior to a specified date
	 * 
	 * @param pFirstResult
	 * @param pMaxResults
	 * @param pDateMax
	 * @return List<Event>
	 */
	List<Event> findAllEvents(int pFirstResult, int pMaxResults, Date pDateMax);

	/**
	 * This method instanciates an event
	 * 
	 * @param pActor
	 * @param pType
	 * @param pLevel
	 * @param pDetails
	 * @return {@link Event} created
	 */
	Event createEvent(String pActor, EventType pType, EventLevel pLevel, String pDetails);

	/**
	 * This method delete all the events which occured before the argued date
	 * 
	 * @param pDate
	 * @return int the nb of deleted events
	 */
	int deleteEventsBeforeDate(Date pDate);
}
