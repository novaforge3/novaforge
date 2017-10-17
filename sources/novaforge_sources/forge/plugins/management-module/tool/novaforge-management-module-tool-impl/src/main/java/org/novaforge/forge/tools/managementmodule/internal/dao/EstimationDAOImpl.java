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

import org.novaforge.forge.tools.managementmodule.dao.EstimationDAO;
import org.novaforge.forge.tools.managementmodule.domain.Estimation;

import javax.persistence.EntityManager;
import javax.persistence.Query;

/**
 * @author BILET-JC
 */
public class EstimationDAOImpl implements EstimationDAO
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

	@Override
	public Estimation getByScopeUnit(final String pScopeUnitId)
	{
		Estimation ret = null;
		Query q = entityManager.createNamedQuery("EstimationEntity.getByScopeUnitId");
		q.setParameter("scopeUnitId", pScopeUnitId);
		Estimation e = (Estimation) q.getSingleResult();
		if (e != null)
		{
			ret = e;
		}
		return ret;
	}

	@Override
	public boolean existEstimation(final String pScopeUnitId)
	{
		boolean ret = false;
		Query q = entityManager.createNamedQuery("EstimationEntity.getByScopeUnitId");
		q.setParameter("scopeUnitId", pScopeUnitId);
		Estimation e = (Estimation) q.getSingleResult();
		if (e != null)
		{
			ret = true;
		}
		return ret;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Estimation merge(final Estimation pEstimation)
	{
		entityManager.merge(pEstimation);
		entityManager.flush();
		return pEstimation;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Estimation save(final Estimation pEstimation)
	{
		entityManager.persist(pEstimation);
		entityManager.flush();
		return pEstimation;
	}

}
