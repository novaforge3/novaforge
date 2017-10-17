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
package org.novaforge.forge.remote.services.core;

import org.novaforge.forge.remote.services.exception.RemoteServiceException;
import org.novaforge.forge.remote.services.model.core.ForgeNode;
import org.novaforge.forge.remote.services.model.core.ForgeProject;

import javax.jws.WebService;
import java.util.List;

@WebService
public interface RemoteProjectService extends RemoteService
{
	String REMOTE_PROJECT_SERVICE_NAME = "RemoteProjectService";

	/**
	 * Get info from forge project by its projectId.
	 * 
	 * @param projectId
	 * @return a project with some info by its projectId
	 * @throws RemoteServiceException
	 */
	ForgeProject getForgeProject(String projectId) throws RemoteServiceException;

	/**
	 * Get the list of forge projects for which the user has access (at least readonly access). In other terms,
	 * returns the Project on which the user has some MemberShip including the Forge and Reference Projects.
	 * 
	 * @return a not-null list of forge projects
	 * @throws RemoteServiceException
	 */
	List<ForgeProject> getForgeProjects() throws RemoteServiceException;

	/**
	 * Get the list of Nodes for the given project Id
	 * 
	 * @return a not-null list list of Nodes
	 * @throws RemoteServiceException
	 */
	List<ForgeNode> getAllForgeProjectNodes(String projectId) throws RemoteServiceException;

}
