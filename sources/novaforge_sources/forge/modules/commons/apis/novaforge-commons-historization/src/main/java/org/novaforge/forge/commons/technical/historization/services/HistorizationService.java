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
package org.novaforge.forge.commons.technical.historization.services;

import org.novaforge.forge.commons.technical.historization.exceptions.HistorizationException;
import org.novaforge.forge.commons.technical.historization.model.Event;
import org.novaforge.forge.commons.technical.historization.model.EventLevel;
import org.novaforge.forge.commons.technical.historization.model.EventType;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author sbenoist
 */
public interface HistorizationService
{
	String ACTOR_SEARCH_CRITERIA_KEY = "actor";

	String LEVEL_SEARCH_CRITERIA_KEY = "level";

	String TYPE_SEARCH_CRITERIA_KEY = "type";

	String DETAILS_SEARCH_CRITERIA_KEY = "details";

	/**
	 * This method returns all registered events inferior to a specified date
	 * 
	 * @param pFirstResult
	 * @param pMaxResults
	 * @param pDateMax
	 *          not taken in account if = 0
	 * @return
	 * @throws HistorizationException
	 */
	List<Event> getAllRegisteredEvents(int pFirstResult, int pMaxResults, Date pDateMax)
	    throws HistorizationException;

	/**
	 * This method returns events which occured between begin and end dates
	 * 
	 * @param pBeginDate
	 * @param pEndDate
	 * @return List<Event>
	 */
	List<Event> findEventsByDate(Date pBeginDate, Date pEndDate) throws HistorizationException;

	/**
	 * This method returns events by actor
	 * 
	 * @param pLogin
	 * @return List<Event>
	 * @throws HistorizationException
	 */
	List<Event> findEventsByActor(String pLogin) throws HistorizationException;

	/**
	 * This method returns events by level
	 * 
	 * @param pLevel
	 * @return List<Event>
	 * @throws HistorizationException
	 */
	List<Event> findEventsByLevel(EventLevel pLevel) throws HistorizationException;

	/**
	 * This method returns events by type
	 * 
	 * @param pType
	 * @return List<Event>
	 * @throws HistorizationException
	 */
	List<Event> findEventsByType(EventType pType) throws HistorizationException;

	/**
	 * This method returns events by keyword on details event
	 * 
	 * @param pKeyword
	 * @return List<Event>
	 * @throws HistorizationException
	 */
	List<Event> findEventsByKeyword(String pKeyword) throws HistorizationException;

	/**
	 * This method delete all the events which occured before the argued date
	 * 
	 * @param pDate
	 * @return int the nb of deleted events
	 * @throws HistorizationException
	 */
	int deleteEventsBeforeDate(Date pDate) throws HistorizationException;

	/**
	 * This methos allows to export in CSV file the events
	 * 
	 * @param pEvents
	 *          <Event> the events to export
	 * @param pFileOut
	 *          the absolute path of the file to export
	 * @param pLocale
	 *          the locale used for date and time format
	 * @throws HistorizationException
	 */
	void exportEventsInCsv(final List<Event> pEvents, final String pFileOut, final Locale pLocale)
	    throws HistorizationException;

	/**
	 * This methos allows to export in CSV file the events found from criterias
	 * 
	 * @param likeCriterias
	 *          can be null
	 * @param equalCriterias
	 *          can be null
	 * @param pBeginDate
	 *          can be null
	 * @param pEndDate
	 *          can be null
	 * @param pFileOut
	 * @param pLocale
	 * @throws HistorizationException
	 */
	void exportEventsInCsvFromCriterias(final Map<String, Object> likeCriterias,
	    final Map<String, Object> equalCriterias, final Date pBeginDate, final Date pEndDate,
	    final String pFileOut, final Locale pLocale) throws HistorizationException;

	/**
	 * This method return events by multiple criterias queries
	 * 
	 * @param likeCriterias
	 *          can be null
	 * @param equalCriterias
	 *          can be null
	 * @param pBeginDate
	 *          can be null
	 * @param pEndDate
	 *          can be null
	 * @param pFirstResult
	 * @param pMaxResults
	 *          not taken in account if = 0
	 * @return List<Event>
	 * @throws HistorizationException
	 */
	List<Event> findEventsByCriterias(final Map<String, Object> likeCriterias,
	    final Map<String, Object> equalCriterias, final Date pBeginDate, final Date pEndDate, int pFirstResult,
	    int pMaxResults) throws HistorizationException;

	/**
	 * @param likeCriterias
	 * @param equalCriterias
	 * @param pBeginDate
	 * @param pEndDate
	 * @return number of {@link Event} found
	 * @throws HistorizationException
	 */
	int countEventsByCriterias(final Map<String, Object> likeCriterias,
	    final Map<String, Object> equalCriterias, final Date pBeginDate, final Date pEndDate)
	    throws HistorizationException;

	/**
	 * Verify if {@link HistorizationService} is activated.
	 * <p>
	 * By default, this service should be activated
	 * </p>
	 * 
	 * @return true if enabled false otherwise.
	 */
	boolean isActivated();

	/**
	 * Set activated mode.
	 * 
	 * @param pActivated
	 *          tur to activate the {@link HistorizationService} or false to disactivate it.
	 */
	void setActivatedMode(final boolean pActivated);

	/**
	 * @param pAuthor
	 * @param pType
	 * @param pLevel
	 * @param pHistorizables
	 */
	void registerEvent(final String pAuthor, final EventType pType, final EventLevel pLevel,
	    final Map<String, Object> pHistorizables);
}
