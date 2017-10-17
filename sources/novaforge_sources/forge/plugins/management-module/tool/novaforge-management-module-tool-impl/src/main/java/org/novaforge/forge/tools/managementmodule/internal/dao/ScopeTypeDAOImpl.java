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

import org.novaforge.forge.tools.managementmodule.dao.ScopeTypeDAO;
import org.novaforge.forge.tools.managementmodule.domain.ScopeType;
import org.novaforge.forge.tools.managementmodule.entity.ScopeTypeEntity;

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
public class ScopeTypeDAOImpl implements ScopeTypeDAO
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
	public ScopeType findByName(final String pName)
	{
		Query q = entityManager.createNamedQuery("ScopeTypeEntity.findByName");
		q.setParameter("name", pName);
		List<?> resultList = q.getResultList();
		ScopeType result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (ScopeType) resultList.get(0);
		}
		return result;
	}

	@Override
	public ScopeType findByfunctionalId(final String pFunctionalId)
	{
		Query q = entityManager.createNamedQuery("ScopeTypeEntity.findByFunctionalId");
		q.setParameter("functionalId", pFunctionalId);
		List<?> resultList = q.getResultList();
		ScopeType result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (ScopeType) resultList.get(0);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ScopeType> findAll()
	{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ScopeType> countCriteria = builder.createQuery(ScopeType.class);
		Root<ScopeTypeEntity> entityRoot = countCriteria.from(ScopeTypeEntity.class);
		countCriteria.select(entityRoot);
		return new LinkedList<ScopeType>(entityManager.createQuery(countCriteria).getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopeType merge(final ScopeType pSt)
	{
		entityManager.merge(pSt);
		entityManager.flush();
		return pSt;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopeType save(final ScopeType pSt)
	{
		entityManager.persist(pSt);
		entityManager.flush();
		return pSt;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final ScopeType pSs)
	{
		entityManager.remove(pSs);
		entityManager.flush();
	}

}
