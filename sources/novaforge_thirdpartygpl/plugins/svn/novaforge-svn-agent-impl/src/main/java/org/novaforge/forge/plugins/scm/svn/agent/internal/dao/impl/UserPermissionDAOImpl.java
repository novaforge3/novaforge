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

import org.novaforge.forge.plugins.scm.svn.agent.dao.UserPermissionDAO;
import org.novaforge.forge.plugins.scm.svn.domain.UserPermission;

/**
 * @author sbenoist
 */

public class UserPermissionDAOImpl implements UserPermissionDAO
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
	public List<UserPermission> findAllPermissionsByRepositoryPath(final Long repositoryPathId)
	{
		TypedQuery<UserPermission> q = entityManager.createNamedQuery(
		    "UserPermissionEntity.findAllPermissionsByRepositoryPath", UserPermission.class);
		q.setParameter("repositoryPathId", repositoryPathId);
		return q.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<UserPermission> findAllPermissionsByUser(final String pName)
	{
		TypedQuery<UserPermission> q = entityManager.createNamedQuery(
		    "UserPermissionEntity.findAllPermissionsByUser", UserPermission.class);
		q.setParameter("username", pName);
		return q.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserPermission findPermissionsByUserAndPath(final String username, final Long repositoryPathId)
	    throws NoResultException
	{
		TypedQuery<UserPermission> q = entityManager.createNamedQuery(
		    "UserPermissionEntity.findPermissionsByUserAndPath", UserPermission.class);
		q.setParameter("username", username);
		q.setParameter("repositoryPathId", repositoryPathId);
		return q.getSingleResult();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserPermission save(final UserPermission pUserPermission)
	{
		entityManager.persist(pUserPermission);
		entityManager.flush();
		return pUserPermission;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public UserPermission update(final UserPermission pUserPermission)
	{
		entityManager.merge(pUserPermission);
		entityManager.flush();
		return pUserPermission;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final UserPermission pUserPermission)
	{
		entityManager.remove(pUserPermission);
		entityManager.flush();

	}
}
