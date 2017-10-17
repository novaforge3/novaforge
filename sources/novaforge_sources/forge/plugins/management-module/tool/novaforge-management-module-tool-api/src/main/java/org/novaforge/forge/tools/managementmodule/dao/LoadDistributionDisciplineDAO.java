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

import org.novaforge.forge.tools.managementmodule.domain.LoadDistributionDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.report.DayLoad;

import java.util.List;

/**
 * @author falsquelle-e
 */
public interface LoadDistributionDisciplineDAO
{

	/**
	 * Find LoadDistributionDisciplines linked to a projectPlan
	 * 
	 * @param projectPlanId
	 * @return a list of LoadDistributionDiscipline
	 *         @
	 */
	List<LoadDistributionDiscipline> findByProjectPlanId(Long projectPlanId);

	/**
	 * return loads by date for a projectPlan
	 * 
	 * @param projectPlanId
	 * @param durationInDays
	 * @return a list of DayLoad
	 *         @
	 */
	List<DayLoad> getLoadsByDateByProjectPlanId(Long projectPlanId, Integer durationInDays);

	/**
	 * Delete the load distribution discipline affected to a project
	 * 
	 * @param projectPlanId
	 *          the project plan identifier
	 *          @ database connection problem
	 */
	void deleteByProjectPlanId(long projectPlanId);

	/**
	 * @param pLoadDistributionDiscipline
	 * @return
	 */
	LoadDistributionDiscipline save(LoadDistributionDiscipline pLoadDistributionDiscipline);

	/**
	 * @param pLddId
	 * @return
	 */
	Object findById(Long pLddId);

	/**
	 * @param pFindById
	 */
	void delete(Object pFindById);

	/**
	 * @param pLoadDistributionDiscipline
	 * @return
	 */
	LoadDistributionDiscipline merge(LoadDistributionDiscipline pLoadDistributionDiscipline);

}
