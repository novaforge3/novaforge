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
package org.novaforge.forge.tools.managementmodule.internal.dao;

import org.novaforge.forge.tools.managementmodule.dao.RoleDAO;
import org.novaforge.forge.tools.managementmodule.domain.Role;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

/**
 * @author vvigo
 */
public class RoleDAOImpl implements RoleDAO
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
		this.entityManager = pEntityManager;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Role findByRoleName(final String roleName)
	{

		Query q = entityManager.createNamedQuery("RoleEntity.findByName");
		q.setParameter("name", roleName);

		List<Role> results = q.getResultList();

		if (results.size() > 0)
		{
			return results.get(0);
		}
		else
		{
			return null;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Role> findAllRoles()
	{
		Query      q       = entityManager.createNamedQuery("RoleEntity.getAllRoles");
		return new LinkedList(q.getResultList());
	}

	@Override
	@SuppressWarnings("unchecked")
	public Role findByFunctionalId(final String functionalId)
	{
		Query q = entityManager.createNamedQuery("RoleEntity.findByFunctionalId");
		q.setParameter("functionalId", functionalId);
		List<Role> results = q.getResultList();
		if (results.size() > 0)
		{
			return results.get(0);
		}
		else
		{
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Role save(final Role pRole)
	{
		entityManager.persist(pRole);
		entityManager.flush();
		return pRole;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Role merge(final Role pRole)
	{
		entityManager.merge(pRole);
		entityManager.flush();
		return pRole;
	}
}
