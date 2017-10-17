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

import org.novaforge.forge.tools.managementmodule.domain.Estimation;

/**
 * @author BILET-JC
 *         Interface of EstimationBean
 *         An estimation object is attach to one and only one scopeUnit.
 *         Its value are estimation (in mens by day) of differents criterias.
 *         There is also differents ways to estimate a charge : manually or with function points, and simple
 *         or detailled.
 */
public interface EstimationDAO
{
	/**
	 * This method returns the {@link Estimation} found by its scopeUnitId
	 * 
	 * @param pScopeUnitId
	 * @return {@link Estimation}
	 * @throws DataAccessException
	 */
	Estimation getByScopeUnit(String pScopeUnitId);

	/**
	 * This method returns true if the scopeUnitId already exist
	 * 
	 * @param pScopeUnitId
	 * @return boolean
	 * @throws DataAccessException
	 */
	boolean existEstimation(String pScopeUnitId);

	/**
	 * @param pEstimation
	 * @return
	 */
	Estimation merge(Estimation pEstimation);

	/**
	 * @param pEstimation
	 * @return
	 */
	Estimation save(Estimation pEstimation);
}
