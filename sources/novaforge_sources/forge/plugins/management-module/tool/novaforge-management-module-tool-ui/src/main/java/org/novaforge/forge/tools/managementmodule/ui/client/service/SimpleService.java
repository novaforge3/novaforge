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
package org.novaforge.forge.tools.managementmodule.ui.client.service;

import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeRightsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.exceptions.ManagementModuleException;

import java.util.Set;

public interface SimpleService {

	/**
	 * This methods controls the user and register him in the http session
	 * 
	 * @return login of current authenticated user
	 * @throws ManagementModuleException
	 *            if a error occurred
	 */
   String checkAndRegisterUser() throws ManagementModuleException;

	/**
	 * This methods gets the disciplines allowed for the connected user and the projet Id in argument
    * @param projectId the projectId
	 * @return the discipline list
    * @throws ManagementModuleException if an error occurred
	 */
   Set<DisciplineDTO> getDisciplinesOfConnectedUser(String projectId)
         throws ManagementModuleException;

	/**
	 * This methods returns the applicative rights of the connected user for the project id in argument
    * @param projectId the projectId to identify the project
	 * @return the applicative rights
    * @throws ManagementModuleException error during operation
	 */
	Set<ApplicativeRightsDTO> getAbilitiesOfConnectedUser(String projectId)
			throws ManagementModuleException;

}