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
package org.novaforge.forge.tools.managementmodule.dao;

import org.novaforge.forge.tools.managementmodule.domain.Iteration;

import java.util.List;

/**
 * Iteration data access object
 */
public interface IterationDAO
{

	List<Iteration> getIterationsList(Long projectPlanId);

	Iteration findOlderNotFinished(Long pProjectPlanId);

	/**
	 * Return the average velocity of finished task of passed iteration of the projectPlan
	 * 
	 * @param pProjectPlanId
	 * @param statusId
	 * @return a Float, the average
	 * @throws DataAccessException
	 */
	// Float getAverageVelocity(Long pProjectPlanId, Long statusId) throws DataAccessException;

	/**
	 * Return the focalisation factor of finished task for the given iteration
	 * 
	 * @param iterationId
	 * @param statusId
	 * @return a Float, the focalisation factor
	 * @throws DataAccessException
	 */
	// Float getFocalisationFactor(Long iterationId, Long statusId) throws DataAccessException;

	/**
	 * Return a list of different time indicators
	 * 
	 * @param iterationId
	 * @param statusId
	 * @return a table of Object
	 * @throws DataAccessException
	 */
	Object[] getAdvancementIndicators(Long iterationId, Long statusId);

	/**
	 * @param pIteration
	 * @return
	 */
	Iteration save(Iteration pIteration);

	/**
	 * @param pIterationId
	 * @return
	 */
	Iteration findById(long pIterationId);

	/**
	 * @param pIteration
	 */
	void delete(Iteration pIteration);

	/**
	 * @param pIteration
	 * @return
	 */
	Iteration merge(Iteration pIteration);

}
