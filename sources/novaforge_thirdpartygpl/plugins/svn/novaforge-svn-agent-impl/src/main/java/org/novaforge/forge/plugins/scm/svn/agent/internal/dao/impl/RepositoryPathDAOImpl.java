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

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.novaforge.forge.plugins.scm.svn.agent.dao.RepositoryPathDAO;
import org.novaforge.forge.plugins.scm.svn.domain.RepositoryPath;

/**
 * @author sbenoist
 */
public class RepositoryPathDAOImpl implements RepositoryPathDAO
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
	public List<RepositoryPath> findAllPathsByRepository(final String repositoryName)
	{
		TypedQuery<RepositoryPath> q = entityManager.createNamedQuery(
		    "RepositoryPathEntity.findAllPathsByRepository", RepositoryPath.class);
		q.setParameter("repositoryName", repositoryName);
		return q.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RepositoryPath findRepositoryPathByRepositoryAndPath(final String repositoryName, final String path)
	    throws NoResultException
	{
		TypedQuery<RepositoryPath> q = entityManager.createNamedQuery(
		    "RepositoryPathEntity.findRepositoryPathByRepositoryAndPath", RepositoryPath.class);
		q.setParameter("repositoryName", repositoryName);
		q.setParameter("path", path);
		return q.getSingleResult();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RepositoryPath save(final RepositoryPath pRepositoryPath)
	{
		entityManager.persist(pRepositoryPath);
		entityManager.flush();
		return pRepositoryPath;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RepositoryPath update(final RepositoryPath pRepositoryPath)
	{
		entityManager.merge(pRepositoryPath);
		entityManager.flush();
		return pRepositoryPath;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final RepositoryPath pRepositoryPath)
	{
		entityManager.remove(pRepositoryPath);
		entityManager.flush();

	}

}
