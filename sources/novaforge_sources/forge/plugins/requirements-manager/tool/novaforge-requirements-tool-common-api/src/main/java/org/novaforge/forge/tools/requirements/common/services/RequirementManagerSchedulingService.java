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


package org.novaforge.forge.tools.requirements.common.services;

import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerSchedulingException;
import org.novaforge.forge.tools.requirements.common.model.scheduling.SchedulingConfiguration;

import java.util.List;

/**
 * High level service that handles the manual launch of requirements synchronization and scheduling
 * configuration management.
 */
public interface RequirementManagerSchedulingService
{
   /**
    * @param projectId
    *           the novaforge project ID.
    * @return true if there is a synchronization ongoing for the given project, false otherwise.
    * @throws RequirementManagerSchedulingException
    */
   boolean isSynchronizationOngoing(String projectId) throws RequirementManagerSchedulingException;

   /**
    * Launches a requirements synchronization fir the given project.
    * 
    * @param projectId
    *           the project ID.
    * @param userId
    *           the user launching the synchronization.
    * @param items
    *           the list of project items to synchronize: - repositories - testlink - code
    * @throws RequirementManagerSchedulingException
    */
   void launchSynchronization(String projectId, String userId, List<String> items)
       throws RequirementManagerSchedulingException;

   /**
    * @param projectId
    *           the project ID
    * @return the scheduling configuration for the given project.
    * @throws RequirementManagerSchedulingException
    *            if the projectid does not exist
    */
   SchedulingConfiguration getSchedulingConfiguration(String projectId) throws RequirementManagerSchedulingException;

   /**
    * Saves the new scheduling configuration for the given project.
    * 
    * @param newConfiguration
    *           the new configuration to save.
    * @throws RequirementManagerSchedulingException
    */
   void saveSchedulingConfiguration(SchedulingConfiguration newConfiguration)
       throws RequirementManagerSchedulingException;
}
