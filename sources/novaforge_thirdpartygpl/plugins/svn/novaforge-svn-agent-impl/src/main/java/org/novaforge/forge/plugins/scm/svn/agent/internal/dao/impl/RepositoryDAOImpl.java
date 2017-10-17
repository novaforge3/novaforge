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
package org.novaforge.forge.plugins.scm.svn.agent.internal.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.novaforge.forge.plugins.scm.svn.agent.dao.RepositoryDAO;
import org.novaforge.forge.plugins.scm.svn.domain.Repository;

/**
 * @author sbenoist
 */
public class RepositoryDAOImpl implements RepositoryDAO
{

	/**
	 * {@link EntityManager} injected by container
	 */
	private EntityManager entityManager;

	/**
	 * Use by container to inject {@link EntityManager}
	 * 
	 * @param pEntityManager
	 *          the entityManager to set
	 */
	public void setEntityManager(final EntityManager pEntityManager)
	{
		entityManager = pEntityManager;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Repository findByName(final String pName)
	{
		TypedQuery<Repository> q = entityManager
		    .createNamedQuery("RepositoryEntity.findByName", Repository.class);
		q.setParameter("name", pName);
		return q.getSingleResult();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final Repository pRepository)
	{
		entityManager.remove(pRepository);
		entityManager.flush();
	}

}
