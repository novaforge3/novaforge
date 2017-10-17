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

import org.novaforge.forge.tools.managementmodule.dao.ApplicativeFunctionDAO;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.entity.ApplicativeFunctionEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public class ApplicativeFunctionDAOImpl implements ApplicativeFunctionDAO
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
	@SuppressWarnings("unchecked")
	public ApplicativeFunction findByFunctionalId(final String pName)
	{
		Query q = entityManager.createNamedQuery("ApplicativeFunctionEntity.findByName");
		q.setParameter("name", pName);
		List<ApplicativeFunction> results = new LinkedList<ApplicativeFunction>(q.getResultList());
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
	public ApplicativeFunction save(final ApplicativeFunction pSf)
	{
		entityManager.persist(pSf);
		entityManager.flush();
		return pSf;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<ApplicativeFunction> findAll()
	{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<ApplicativeFunction> countCriteria = builder.createQuery(ApplicativeFunction.class);
		Root<ApplicativeFunctionEntity> entityRoot = countCriteria.from(ApplicativeFunctionEntity.class);
		countCriteria.select(entityRoot);
		return new LinkedList<ApplicativeFunction>(entityManager.createQuery(countCriteria).getResultList());
	}

}
