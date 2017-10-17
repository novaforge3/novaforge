/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.dao;

import java.util.List;

import javax.persistence.NoResultException;

import org.novaforge.forge.plugins.scm.svn.domain.RepositoryPath;

/**
 * @author sbenoist
 */
public interface RepositoryPathDAO
{
	List<RepositoryPath> findAllPathsByRepository(String repositoryName);

	RepositoryPath findRepositoryPathByRepositoryAndPath(String repositoryName, String path)
	    throws NoResultException;

	/**
	 * @param pRepositoryPathEntity
	 * @return
	 */
	RepositoryPath save(RepositoryPath pRepositoryPath);

	/**
	 * @param pRepositoryPath
	 * @return
	 */
	RepositoryPath update(RepositoryPath pRepositoryPath);

	/**
	 * @param pRepositoryPath
	 */
	void delete(RepositoryPath pRepositoryPath);
}
