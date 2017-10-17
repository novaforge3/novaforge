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

import org.novaforge.forge.tools.managementmodule.domain.ScopeUnit;

import java.util.List;

/**
 * @author fdemange
 */
public interface ScopeUnitDAO
{
	/**
	 * Return a scopeUnit for specified unitId
	 * 
	 * @param scopeUnitId
	 * @return
	 *         @
	 */
	ScopeUnit findByUnitId(String scopeUnitId);

	/**
	 * return a scopeUnit for specified unitId and version of RefScopeUnit
	 * 
	 * @param refScopeUnitId
	 * @param refVersion
	 * @param projectPlanId
	 * @return
	 *         @
	 */
	ScopeUnit findByRefVersion(String refScopeUnitId, String refVersion, Long projectPlanId);

	/**
	 * return a list of ScopeUnit link to a RefScopeUnit
	 * 
	 * @param refScopeUnitId
	 * @return
	 *         @
	 */
	List<ScopeUnit> findByRefUnitId(String refScopeUnitId);

	/**
	 * Return a list of scopeUnit linked to lot attached to a projectPlan
	 * 
	 * @param pProjectPlanId
	 * @return
	 *         @
	 */
	List<ScopeUnit> findByProjectPlanId(Long pProjectPlanId);

	/**
	 * Delete the scope units using a project plan id
	 * 
	 * @param projectPlanId
	 *          the id of the project plan
	 *          @ exception d'accès aux données
	 */
	void deleteByProjectPlanId(Long projectPlanId);

	/**
	 * Delete scope unit for a unitId
	 * 
	 * @param unitId
	 *          the unitId of the ScopeUnit
	 *          @ exception d'accès aux données
	 */
	void deleteScopeUnit(String unitId);

	/**
	 * @param pScope
	 * @return
	 */
	ScopeUnit save(ScopeUnit pScope);

	/**
	 * @param pCurrentScope
	 * @return
	 */
	ScopeUnit merge(ScopeUnit pCurrentScope);
}
