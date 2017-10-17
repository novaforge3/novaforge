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

import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;

import java.util.List;

/**
 * Expose methods to access data layer to get information on a scopeunit for a discipline
 */
public interface ScopeUnitDisciplineDAO
{
	/**
	 * Get the appropriate ScopeUnitDiscipline object using the arguments
	 * 
	 * @param scopeUnitId
	 *          the scopeUnit id to use
	 * @param discipline
	 *          the discipline to use
	 * @return the appropriate ScopeUnitDiscipline (or null)
	 *         @ problem during data access
	 */
	ScopeUnitDiscipline findScopeUnitDiscipline(long scopeUnitId, Discipline discipline);

	/**
	 * Get the appropriate ScopeUnitDiscipline object using the argument
	 * 
	 * @param scopeUnitId
	 *          the scopeUnit id to use
	 * @return the appropriate list of ScopeUnitDiscipline (or null)
	 *         @ problem during data access
	 */
	List<ScopeUnitDiscipline> findScopeUnitDisciplinesByScopeUnit(long scopeUnitId);

	/**
	 * Delete all the scope unit discipline of a project plan
	 * 
	 * @param projectPlanId
	 *          the id
	 *          @ problem during data access
	 */
	void deleteByProjectPlanId(long projectPlanId);

	/**
	 * return all ScopeUnitDisciplines linked to specified scopeUnits
	 * 
	 * @param scopeUnitIds
	 *          ids of scopeUnits
	 * @return
	 *         @
	 */
	List<ScopeUnitDiscipline> findScopeUnitDisciplinesByScopeUnit(List<Long> scopeUnitIds);

	/**
	 * @param pScopeUnitDiscipline
	 */
	void delete(ScopeUnitDiscipline pScopeUnitDiscipline);

	/**
	 * @param pScopeUnitDiscipline
	 * @return
	 */
	ScopeUnitDiscipline merge(ScopeUnitDiscipline pScopeUnitDiscipline);

	/**
	 * @param pScopeUnitDiscipline
	 * @return
	 */
	ScopeUnitDiscipline save(ScopeUnitDiscipline pScopeUnitDiscipline);
}
