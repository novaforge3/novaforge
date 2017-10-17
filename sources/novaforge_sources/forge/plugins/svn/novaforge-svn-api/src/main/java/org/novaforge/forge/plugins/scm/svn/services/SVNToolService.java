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
package org.novaforge.forge.plugins.scm.svn.services;

import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;

/**
 * @author sbenoist
 */
public interface SVNToolService
{
	/**
	 * This method returns the file repository path for a repository id
	 * 
	 * @param pRepositoryId
	 * @param pRepositoryPath
	 * @return the file repository path
	 * @throws SVNAgentException
	 */
	String getRepositoryPath(final String pRepositoryId, final String pRepositoryPath) throws SVNAgentException;

	/**
	 * This method returns the url repository for a repository id
	 * 
	 * @param pRepositoryId
	 * @return the url repository
	 * @throws SVNAgentException
	 */
	String getRepositoryUrl(final String pRepositoryId) throws SVNAgentException;

}
