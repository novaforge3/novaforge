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
package org.novaforge.forge.distribution.register.server.internal.dao;

import org.novaforge.forge.distribution.register.dao.ForgeDAO;
import org.novaforge.forge.distribution.register.domain.Forge;
import org.novaforge.forge.distribution.register.server.entity.ForgeEntity;
import org.novaforge.forge.distribution.register.server.entity.ForgeEntity_;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.UUID;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 29 déc. 2011
 */
public class ForgeDAOImpl implements ForgeDAO
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
	public Forge newForge()
	{
		return new ForgeEntity();
	}

	@Override
	public Forge findById(final UUID forgeId)
	{

		if (forgeId == null)
		{
			throw new IllegalArgumentException("the given forge id should not be null");
		}

		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<ForgeEntity> query = builder.createQuery(ForgeEntity.class);
		final Root<ForgeEntity> from = query.from(ForgeEntity.class);
		query.select(from);

		final Predicate uuidPredicate = builder.equal(from.get(ForgeEntity_.forgeId), forgeId.toString());

		query.where(uuidPredicate);

		try
		{
			return entityManager.createQuery(query).getSingleResult();
		}
		catch (final NoResultException e)
		{
			return null;
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Forge save(final Forge pForge)
	{
		entityManager.persist(pForge);
		entityManager.flush();
		return pForge;
	}

	@Override
	public void delete(final UUID forgeId)
	{
		if (forgeId == null)
		{
			throw new IllegalArgumentException("the given forge id should not be null");
		}

		final Forge forge = findById(forgeId);
		if (forge == null)
		{
			throw new NoResultException("Not found forge id: " + forgeId.toString());
		}

		entityManager.remove(forge);
		entityManager.flush();

	}

	@Override
	public List<Forge> findByLevel(final int level)
	{
		final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		final CriteriaQuery<Forge> query = builder.createQuery(Forge.class);
		final Root<ForgeEntity> from = query.from(ForgeEntity.class);
		query.select(from);

		final Predicate uuidPredicate = builder.equal(from.get(ForgeEntity_.forgeLevel), level);

		query.where(uuidPredicate);

		return entityManager.createQuery(query).getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Forge update(final Forge pForge)
	{
		entityManager.merge(pForge);
		entityManager.flush();
		return pForge;
	}

}
