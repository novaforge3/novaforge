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

import org.novaforge.forge.tools.managementmodule.dao.IterationDAO;
import org.novaforge.forge.tools.managementmodule.domain.Iteration;
import org.novaforge.forge.tools.managementmodule.entity.IterationEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fdemange
 */
public class IterationDAOImpl implements IterationDAO
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
	public List<Iteration> getIterationsList(final Long projectPlanId)
	{
		Query q = entityManager.createNamedQuery("IterationEntity.getIterationsList");
		q.setParameter("projectPlanId", projectPlanId);
		return new LinkedList<Iteration>(q.getResultList());
	}

	@Override
	public Iteration findOlderNotFinished(final Long pProjectPlanId)
	{
		Query q = entityManager.createNamedQuery("IterationEntity.findOlderNotFinished");
		q.setMaxResults(1);
		List<?> resultList = new LinkedList(q.getResultList());
		Iteration result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (Iteration) resultList.get(0);
		}
		return result;
	}

	@Override
	public Object[] getAdvancementIndicators(final Long iterationId, final Long statusId)
	{
		Query q = entityManager.createNamedQuery("IterationEntity.getTimeIndicators");
		q.setParameter("iterationId", iterationId);
		q.setParameter("statusId", statusId);
		List<?> resultList = new LinkedList(q.getResultList());
		Object[] result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (Object[]) resultList.get(0);
		}

		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iteration save(final Iteration pIteration)
	{
		entityManager.persist(pIteration);
		entityManager.flush();
		return pIteration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iteration findById(final long pIterationId)
	{
		return entityManager.find(IterationEntity.class, pIterationId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final Iteration pIteration)
	{
		entityManager.remove(pIteration);
		entityManager.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Iteration merge(final Iteration pIteration)
	{
		entityManager.merge(pIteration);
		entityManager.flush();
		return pIteration;
	}

}
