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

import org.novaforge.forge.tools.managementmodule.dao.MarkerTypeDAO;
import org.novaforge.forge.tools.managementmodule.domain.MarkerType;
import org.novaforge.forge.tools.managementmodule.entity.MarkerTypeEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.LinkedList;
import java.util.List;

public class MarkerTypeDAOImpl implements MarkerTypeDAO
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
	public MarkerType newPhaseType()
	{
		return new MarkerTypeEntity();
	}

	@Override
	public MarkerType findByName(final String name)
	{

		Query q = entityManager.createNamedQuery("MarkerTypeEntity.findByName");
		q.setParameter("name", name);
		MarkerType result = null;
		List<?> resultList = new LinkedList(q.getResultList());
		if (resultList != null && resultList.size() == 1)
		{
			result = (MarkerType) resultList.get(0);
		}
		return result;
	}

	@Override
	public MarkerType findByFunctionalId(final String functionalId)
	{

		Query q = entityManager.createNamedQuery("MarkerTypeEntity.findByFunctionalId");
		q.setParameter("functionalId", functionalId);
		MarkerType result = null;
		List<?> resultList = new LinkedList(q.getResultList());
		if (resultList != null && resultList.size() == 1)
		{
			result = (MarkerType) resultList.get(0);
		}
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MarkerType save(final MarkerType pMarkerType)
	{
		entityManager.persist(pMarkerType);
		entityManager.flush();
		return pMarkerType;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MarkerType> findAll()
	{
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MarkerType> countCriteria = builder.createQuery(MarkerType.class);
		Root<MarkerTypeEntity> entityRoot = countCriteria.from(MarkerTypeEntity.class);
		countCriteria.select(entityRoot);
		return new LinkedList<MarkerType>(entityManager.createQuery(countCriteria).getResultList());
	}

}
