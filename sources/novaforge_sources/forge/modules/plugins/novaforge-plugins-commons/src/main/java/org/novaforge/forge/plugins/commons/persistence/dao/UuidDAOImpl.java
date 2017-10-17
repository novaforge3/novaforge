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

import org.novaforge.forge.core.plugins.dao.UuidDAO;
import org.novaforge.forge.core.plugins.domain.plugin.Uuid;
import org.novaforge.forge.plugins.commons.persistence.entity.UuidEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

/**
 * This class allows to manage uuid data
 * 
 * @author lamirang
 */
public class UuidDAOImpl implements UuidDAO
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

	@Override
	public UUID generateUUID()
	{
		final UUID uuid = UUID.randomUUID();
		entityManager.persist(new UuidEntity(uuid));
		return uuid;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Uuid> findAll()
	{
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Uuid> allCriteria = builder.createQuery(Uuid.class);
		final Root<UuidEntity> entityRoot = allCriteria.from(UuidEntity.class);
		allCriteria.select(entityRoot);
		final TypedQuery<Uuid> q = entityManager.createQuery(allCriteria);
		return q.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public long countAll()
	{
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		final Root<UuidEntity> entityRoot = countCriteria.from(UuidEntity.class);
		countCriteria.select(builder.count(entityRoot));
		return entityManager.createQuery(countCriteria).getSingleResult();
	}
}
