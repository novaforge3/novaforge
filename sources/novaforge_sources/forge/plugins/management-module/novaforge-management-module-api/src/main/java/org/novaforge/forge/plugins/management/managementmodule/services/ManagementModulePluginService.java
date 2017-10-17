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
package org.novaforge.forge.plugins.management.managementmodule.services;

import org.novaforge.forge.tools.managementmodule.domain.Bug;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.List;

/**
 * The service to use to access the plugin part from the management module tool
 */
public interface ManagementModulePluginService
{

	/**
	 * This method will return the appropriate project id using the instance id given in parameter
	 * 
	 * @param pInstanceId
	 *          the instanceId
	 * @return the correct project id
	 * @throws ManagementModuleException
	 */
	String getProjectId(final String pInstanceId) throws ManagementModuleException;

	/**
	 * This method will return the appropriate instance id using the pProjectId given in parameter
	 * 
	 * @param pProjectId
	 * @return instance id
	 * @throws ManagementModuleException
	 */
	String getInstanceId(String pProjectId) throws ManagementModuleException;

	/**
	 * Get all the issues of a bug tracker
	 * 
	 * @param pProjectId
	 *          the project id
	 * @param pUser
	 *          the user
	 * @return the bug list
	 * @throws ManagementModuleException
	 *           problem during execution
	 */
	List<Bug> getAllIssues(String pProjectId, String pUser) throws ManagementModuleException;

	/**
	 * Return if the connection to the bug tracker is OK
	 * 
	 * @param pProjectId
	 *          the project id
	 * @return true if Ok false otherwise
	 * @throws ManagementModuleException
	 *           problem during execution
	 */
	boolean hasBugtrackerAvailable(String pProjectId) throws ManagementModuleException;

	/**
	 * Fill an existing bug with bug tracker informations
	 * 
	 * @param pProjectId
	 *          the project id
	 * @param pUser
	 *          the user
	 * @param bug
	 *          the bug to fill
	 * @return the bug
	 * @throws ManagementModuleException
	 *           problem during operation
	 */
	void fillBugWithBugTrackerInformations(String pProjectId, String pUser, Bug bug)
			throws ManagementModuleException;
}
