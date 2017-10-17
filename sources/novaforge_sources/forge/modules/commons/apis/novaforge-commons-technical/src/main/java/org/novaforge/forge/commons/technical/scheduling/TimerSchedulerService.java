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
package org.novaforge.forge.commons.technical.scheduling;

import javax.jws.WebService;

/**
 * Service used to configure a Timer. The timer can be active or not active. If active, it is configure with a
 * start time in a day (Start Hour, Start Min) and a period which indicates (in hours) the next time in the
 * day the timer will execute it's task?
 * 
 * @author rols-p
 */
@WebService
public interface TimerSchedulerService
{

	String REF_EXTRACTION_SCHEDULER_SVC_NAME = "ExtractionScheduler";

	/**
	 * Set the Timer to inactive.
	 * 
	 * @throws Exception
	 */
	void disableScheduling() throws Exception;

	/**
	 * Set the Timer to active and configure it.
	 * 
	 * @param hours
	 * @param minutes
	 * @param period
	 * @throws Exception
	 */
	void configureScheduling(final String hours, final String minutes, final String period) throws Exception;

	/**
	 * @return true if the Timer is active.
	 */
	boolean isActive();

	/**
	 * @return the hour at which the first Timer task will be executed each day
	 */
	String getStartHour();

	/**
	 * @return the minute (to be used with startHour) at which the first Timer task will be executed each day
	 */
	String getStartMin();

	/**
	 * @return the time in hours between two Timer tasks executions (after the prvious task execution and
	 *         before the end of the day)
	 */
	String getPeriod();
}
