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
package org.novaforge.forge.tools.managementmodule.business;

import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.domain.Lot;
import org.novaforge.forge.tools.managementmodule.domain.PhaseType;
import org.novaforge.forge.tools.managementmodule.domain.report.AdvancementIndicators;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.List;
import java.util.Set;

/**
 * This manager is used to manage the creation, modification and suppression of
 * iterations, the affectation of tasks is manager on TaskManager
 */
public interface IterationManager {

	/**
	 * Returns the lots wich can be used for the next iterations of a project
	 * plan
	 * 
	 * @param projectPlanId
	 *            the identifier of the project plan
	 * @return the appropriate lots
	 * @throws ManagementModuleException
	 *             exception
	 */
	Set<Lot> getsLotForNextIteration(final long projectPlanId) throws ManagementModuleException;

	Iteration newIteration();

	Iteration creeteIteration(Iteration iteration) throws ManagementModuleException;

	/**
	 * This method returns all the iterations for a project plan
	 * 
	 * @return List<Iteration>
	 * @throws ManagementModuleException
	 */
	List<Iteration> getIterationsList(Long projectPlanId) throws ManagementModuleException;

	/**
	 * This method returns finished iteration(s) and current iteration(s) (2
	 * max)
	 * 
	 * @param projectPlanId
	 * @return List<Iteration>
	 * @throws ManagementModuleException
	 */
	List<Iteration> getFinishedAndCurrentIterationList(Long projectPlanId) throws ManagementModuleException;

	/**
	 * Return an iteration with given iteration id
	 * 
	 * @param iterationId
	 * @return Iteration
	 * @throws ManagementModuleException
	 */
	Iteration getIteration(long iterationId) throws ManagementModuleException;

	/**
	 * This method allows to update an iteration
	 * 
	 * @param iter the iteration to update
	 * @return the updated iteration
	 * @throws ManagementModuleException
	 */
	Iteration updateIteration(Iteration iter) throws ManagementModuleException;

	/**
	 * This method allowed to delete an iteration
	 * 
	 * @param iterationId
	 * @throws ManagementModuleException
	 */
	boolean deleteIteration(long iterationId) throws ManagementModuleException;

	/**
	 * Get the appropriates phasesType for the next iteration
	 * 
	 * @param currentIteration
	 *            the current iteration
	 * @return the appropriates phases types
	 * @throws ManagementModuleException
	 *             problem during
	 */
	Set<PhaseType> getPhasesTypesForNextIteration(final Iteration currentIteration)
			throws ManagementModuleException;

	/**
	 * Return the next iteration
	 * 
	 * @param pProjectPlanId
	 * @return Iteration
	 * @throws ManagementModuleException
	 */
	Iteration getNextIteration(Long pProjectPlanId) throws ManagementModuleException;

	/**
	 * Get the older not finished iteration
	 * 
	 * @param pProjectPlanId
	 * @return the older current Iteration
	 * @throws ManagementModuleException
	 */
	Iteration getCurrentIteration(Long pProjectPlanId) throws ManagementModuleException;

	/**
	 * Get current iterations
	 * 
	 * @param currentValidatedProjectPlanId
	 * @return List<Iteration>
	 * @throws ManagementModuleException
	 */
	List<Iteration> getCurrentIterationList(Long currentValidatedProjectPlanId)
			throws ManagementModuleException;
	/**
	 * Return an object containing some informations about advancement
	 * @param iterationId
	 * @return AdvancementIndicators
	 * @throws ManagementModuleException 
	 */
	AdvancementIndicators getAdvancementIndicators(Long iterationId) throws ManagementModuleException;

   /**
    * Get the closest finished iteration
    * @param projectPlanId the project plan id
    * @return the iteration (or null if not finished iteration)
    * @throws ManagementModuleException problem during operation
    */
   Iteration getLastFinishedIteration(long projectPlanId) throws ManagementModuleException;

   /**
    * Return a String containing informations of iteration and discipline for reports
    * @param iterationId
    * @param disciplineFunctionalId
    * @return
    * @throws ManagementModuleException
    */
   String getIterationsMetadatas(Long iterationId, String disciplineFunctionalId) throws ManagementModuleException;

}
