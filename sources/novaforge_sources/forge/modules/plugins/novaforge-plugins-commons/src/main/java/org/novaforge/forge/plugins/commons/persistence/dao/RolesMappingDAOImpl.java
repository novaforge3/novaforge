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
package org.novaforge.forge.plugins.commons.persistence.dao;

import org.novaforge.forge.core.plugins.dao.RolesMappingDAO;
import org.novaforge.forge.core.plugins.domain.plugin.RolesMapping;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is dao allowing to manage role mapping for a plugin instance
 * 
 * @author lamirang
 */
public class RolesMappingDAOImpl implements RolesMappingDAO
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
	public Map<String, String> findByInstance(final String pInstanceID)
	{
		final Map<String, String> resultMap = new HashMap<String, String>();
		final TypedQuery<RolesMapping> q = entityManager.createNamedQuery("RolesMappingEntity.findByInstance",
		    RolesMapping.class);
		q.setParameter("id", pInstanceID);
		final List<RolesMapping> results = q.getResultList();
		for (final RolesMapping rolesMapping : results)
		{
			resultMap.put(rolesMapping.getForgeRole(), rolesMapping.getToolRole());
		}
		return resultMap;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeByInstanceAndForgeRole(final String pInstanceID, final String pForgeRole)
	{
		final TypedQuery<RolesMapping> q = entityManager.createNamedQuery(
		    "RolesMappingEntity.findByInstanceAndForgeRole", RolesMapping.class);
		q.setParameter("id", pInstanceID);
		q.setParameter("forgeRole", pForgeRole);
		final RolesMapping singleResult = q.getSingleResult();
		entityManager.remove(singleResult);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteByInstance(final String pInstanceID)
	{
		final TypedQuery<RolesMapping> q = entityManager.createNamedQuery("RolesMappingEntity.findByInstance",
		    RolesMapping.class);
		q.setParameter("id", pInstanceID);
		final List<RolesMapping> results = q.getResultList();
		for (final RolesMapping rolesMapping : results)
		{
			entityManager.remove(rolesMapping);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String findByInstanceAndForgeRole(final String pInstanceID, final String pForgeRole)
	{
		final TypedQuery<String> q = entityManager.createNamedQuery("RolesMappingEntity.findToolRoleByInstanceAndForgeRole",
																																String.class);
		q.setParameter("id", pInstanceID);
		q.setParameter("forgeRole", pForgeRole);
		return q.getSingleResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RolesMapping persist(final RolesMapping pRolesMapping)
	{
		entityManager.persist(pRolesMapping);
		entityManager.flush();
		return pRolesMapping;
	}
}
