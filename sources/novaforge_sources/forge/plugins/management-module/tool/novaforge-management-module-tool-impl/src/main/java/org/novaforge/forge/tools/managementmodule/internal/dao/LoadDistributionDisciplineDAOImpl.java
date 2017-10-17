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

import org.novaforge.forge.tools.managementmodule.business.BusinessObjectFactory;
import org.novaforge.forge.tools.managementmodule.dao.LoadDistributionDisciplineDAO;
import org.novaforge.forge.tools.managementmodule.domain.LoadDistributionDiscipline;
import org.novaforge.forge.tools.managementmodule.domain.report.DayLoad;
import org.novaforge.forge.tools.managementmodule.entity.LoadDistributionDisciplineEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author falsquelle-e
 */
public class LoadDistributionDisciplineDAOImpl implements LoadDistributionDisciplineDAO
{

	/**
	 * {@link EntityManager} injected by container
	 */
	private EntityManager entityManager;
	private BusinessObjectFactory businessObjectFactory;

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
	public List<LoadDistributionDiscipline> findByProjectPlanId(final Long projectPlanId)
	{
		Query q = entityManager.createNamedQuery("LoadDistributionDisciplineEntity.findByProjectPlanId");
		q.setParameter("projectPlanId", projectPlanId);
		return new LinkedList<LoadDistributionDiscipline>(q.getResultList());
	}

	@Override
	public List<DayLoad> getLoadsByDateByProjectPlanId(final Long projectPlanId, final Integer durationInDays)
	{

		List<DayLoad> list = new ArrayList<DayLoad>();

		Query q = entityManager
				.createNamedQuery("LoadDistributionDisciplineEntity.getLoadsByDateByProjectPlanId");
		q.setParameter("projectPlanId", projectPlanId);

		@SuppressWarnings("unchecked")
		List<Object[]> result = new LinkedList(q.getResultList());

		for (Object[] o : result)
		{
			DayLoad dl = businessObjectFactory.getInstanceDayLoad();
			float load = ((Float) o[1]) / durationInDays.floatValue();
			dl.setLoad(load);
			Date date = (Date) o[0];
			dl.setDate( date );

			list.add(dl);
		}

		return list;
	}

	@Override
	public void deleteByProjectPlanId(final long projectPlanId)
	{
		final Query q = entityManager
				.createNamedQuery("LoadDistributionDisciplineEntity.deleteLoadsByProjectPlanId");
		q.setParameter("projectPlanId", projectPlanId);
		q.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoadDistributionDiscipline save(final LoadDistributionDiscipline pLoadDistributionDiscipline)
	{
		entityManager.persist(pLoadDistributionDiscipline);
		entityManager.flush();
		return pLoadDistributionDiscipline;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoadDistributionDiscipline findById(final Long pLddId)
	{
		return entityManager.find(LoadDistributionDisciplineEntity.class, pLddId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final Object pFindById)
	{
		entityManager.remove(pFindById);
		entityManager.flush();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public LoadDistributionDiscipline merge(final LoadDistributionDiscipline pLoadDistributionDiscipline)
	{
		entityManager.merge(pLoadDistributionDiscipline);
		entityManager.flush();
		return pLoadDistributionDiscipline;
	}

	public void setBusinessObjectFactory(final BusinessObjectFactory pBusinessObjectFactory)
	{
		businessObjectFactory = pBusinessObjectFactory;
	}

}
