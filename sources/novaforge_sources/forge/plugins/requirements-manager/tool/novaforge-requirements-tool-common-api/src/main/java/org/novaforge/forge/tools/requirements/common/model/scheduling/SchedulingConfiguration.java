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


package org.novaforge.forge.tools.requirements.common.model.scheduling;


/**
 * Requirements synchronization scheduling container.
 */
public interface SchedulingConfiguration
{

   /**
    * @return Return the project id that handles the delayed job
    */
   String getProjectId();

   /**
    * Set the project id used to launch this job
    */
   void setProjectId(String projectId);

   /**
    * @return the user who setup the scheduling configuration.
    */
   String getUserId();

   /**
    * Sets the user setting up the scheduling configuration.
    * 
    * @param userId
    */
   void setUserId(String userId);

   /**
    * @return true if the scheduling is activated.
    */
   boolean isActive();

   /**
    * Set/Unset the active flag for the current scheduling configuration.
    * 
    * @param active
    */
   void setActive(boolean active);

   /**
    * @return the scheduling launch hour.
    */
   Integer getLaunchHour();

   /**
    * Sets the next scheduling launch hour.
    * 
    * @param launchHour
    */
   void setLaunchHour(Integer launchHour);

   /**
    * @return the scheduling launch minute.
    */
   Integer getLaunchMinute();

   /**
    * Sets the next scheduling launch minute.
    * 
    * @param launchMinute
    */
   void setLaunchMinute(Integer launchMinute);

   /**
    * @return the launch period (in hours).
    */
   Integer getLaunchPeriod();

   /**
    * Sets the period between two launches (in hours).
    * 
    * @param period
    */
   void setLaunchPeriod(Integer period);
}
