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

import org.novaforge.forge.tools.managementmodule.dao.ScopeUnitDisciplineDAO;
import org.novaforge.forge.tools.managementmodule.domain.Discipline;
import org.novaforge.forge.tools.managementmodule.domain.ScopeUnitDiscipline;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * JPA Implementation of the DAO ScopeUnitDiscipline
 */
public class ScopeUnitDisciplineDAOImpl implements ScopeUnitDisciplineDAO
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
	public ScopeUnitDiscipline findScopeUnitDiscipline(final long scopeUnitId, final Discipline discipline)
	{
		final Query query = entityManager.createNamedQuery("ScopeUnitDisciplineEntity.findByScopeAndDiscipline");
		query.setParameter("scopeId", scopeUnitId);
		query.setParameter("disciplineFunctionalId", discipline.getFunctionalId());
		List<ScopeUnitDiscipline> results = query.getResultList();
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
	public List<ScopeUnitDiscipline> findScopeUnitDisciplinesByScopeUnit(final long scopeUnitId)
	{
		final Query query = entityManager.createNamedQuery("ScopeUnitDisciplineEntity.findByScopeUnit");
		query.setParameter("scopeUnitId", scopeUnitId);
		return query.getResultList();
	}

	@Override
	public void deleteByProjectPlanId(final long projectPlanId)
	{
		final Query q = entityManager.createNamedQuery("ScopeUnitDisciplineEntity.deleteByProjectPlanId");
		q.setParameter("projectPlanId", projectPlanId);
		q.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ScopeUnitDiscipline> findScopeUnitDisciplinesByScopeUnit(final List<Long> scopeUnitIds)
	{

		if (scopeUnitIds == null || scopeUnitIds.size() < 1)
		{
			return new ArrayList<ScopeUnitDiscipline>();
		}
		final Query query = entityManager.createNamedQuery("ScopeUnitDisciplineEntity.findByScopeUnitIds");
		query.setParameter("scopeUnitIds", scopeUnitIds);
		return query.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final ScopeUnitDiscipline pScopeUnitDiscipline)
	{
		entityManager.remove(pScopeUnitDiscipline);
		entityManager.flush();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopeUnitDiscipline merge(final ScopeUnitDiscipline pScopeUnitDiscipline)
	{
		entityManager.merge(pScopeUnitDiscipline);
		entityManager.flush();
		return pScopeUnitDiscipline;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ScopeUnitDiscipline save(final ScopeUnitDiscipline pScopeUnitDiscipline)
	{
		entityManager.persist(pScopeUnitDiscipline);
		entityManager.flush();
		return pScopeUnitDiscipline;
	}

}
