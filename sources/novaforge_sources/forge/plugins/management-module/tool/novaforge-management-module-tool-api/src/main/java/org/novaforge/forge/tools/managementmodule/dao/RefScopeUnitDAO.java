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

import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;
import org.novaforge.forge.tools.managementmodule.exceptions.ManagementModuleException;

import java.util.List;

/**
 * @author vvigo
 */
public interface RefScopeUnitDAO
{

	RefScopeUnit findByRefScopeUnitId(String refScopeUnitId, String refVersion);

	RefScopeUnit findByRefScopeUnitName(String refScopeUnitName, String refScopeUnitVersion);

	/**
	 * Return all refScopeUnit non-obsolete linked to a project in there last version.
	 * 
	 * @param projectId
	 * @return
	 * @throws ManagementModuleException
	 */
	List<RefScopeUnit> getLastVersionRefScopeUnit(String projectId);

	RefScopeUnit getCompleteRefScopeUnit(String refScopeUnitId, String refVersion);

	RefScopeUnit getLastVersionofThisRefScopeUnit(String unitId);

	RefScopeUnit getLastVersionofThisRefScopeUnitFils(String unitId);

	List<RefScopeUnit> getCompleteAllFromProject(String projectId, CDOParameters cdoParameters);

	List<RefScopeUnit> getCompleteAllFromProject(String projectId, CDOParameters cdoParameters,
			boolean withChild);

	/**
	 * @param pRefScope
	 * @return
	 */
	RefScopeUnit save(RefScopeUnit pRefScope);

	/**
	 * @param pRefScopeUnitFinded
	 * @return
	 */
	RefScopeUnit merge(RefScopeUnit pRefScopeUnitFinded);

	/**
	 * @param pRefScopeUnitfromSGBD
	 */
	void delete(RefScopeUnit pRefScopeUnitfromSGBD);
}
