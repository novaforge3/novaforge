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

import org.novaforge.forge.tools.managementmodule.dao.StatusTaskDAO;
import org.novaforge.forge.tools.managementmodule.domain.StatusTask;
import org.novaforge.forge.tools.managementmodule.entity.StatusTaskEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

/**
 * @author sbenoist
 */
public class StatusTaskDAOImpl implements StatusTaskDAO
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
	public StatusTask findByFunctionalId(final String functionaId)
	{
		Query q = entityManager.createNamedQuery("StatusTaskEntity.findByFunctionalId");
		q.setParameter("functionalId", functionaId);
		StatusTask result = null;
		List<?> resultList = q.getResultList();
		if (resultList != null && resultList.size() == 1)
		{
			result = (StatusTask) resultList.get(0);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StatusTask save(final StatusTask pSt)
	{
		entityManager.persist(pSt);
		entityManager.flush();
		return pSt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final StatusTask pSt)
	{
		entityManager.remove(pSt);
		entityManager.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public StatusTask merge(final StatusTask pSt)
	{
		entityManager.merge(pSt);
		entityManager.flush();
		return pSt;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<StatusTask> findAll()
	{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<StatusTask> countCriteria = builder.createQuery(StatusTask.class);
		Root<StatusTaskEntity> entityRoot = countCriteria.from(StatusTaskEntity.class);
		countCriteria.select(entityRoot);
		return new LinkedList<StatusTask>(entityManager.createQuery(countCriteria).getResultList());
	}

}
