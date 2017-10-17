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

import org.novaforge.forge.tools.managementmodule.constant.ManagementModuleConstants;
import org.novaforge.forge.tools.managementmodule.dao.ProjectPlanDAO;
import org.novaforge.forge.tools.managementmodule.domain.ProjectPlan;
import org.novaforge.forge.tools.managementmodule.entity.ProjectPlanEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

/**
 * @author vvigo
 */
public class ProjectPlanDAOImpl implements ProjectPlanDAO
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
	public ProjectPlan findDraftByProjectId(final String pProjectId)
	{
		Query q = entityManager.createNamedQuery("ProjectPlanEntity.findDraftByProjectId");
		q.setParameter("projectId", pProjectId);
		q.setParameter("statusName", ManagementModuleConstants.PROJECT_PLAN_DRAFT);
		List<?> resultList = q.getResultList();
		ProjectPlan result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (ProjectPlan) resultList.get(0);
		}
		return result;
	}

	@Override
	public ProjectPlan findByProjectIdAndVersion(final String pProjectId, final Integer pVersion)
	{
		Query q = entityManager.createNamedQuery("ProjectPlanEntity.findByProjectIdAndVersion");
		q.setParameter("projectId", pProjectId);
		q.setParameter("version", pVersion);
		List<?> resultList = q.getResultList();
		ProjectPlan result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (ProjectPlan) resultList.get(0);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProjectPlan> findByProjectId(final String pProjectId)
	{
		Query q = entityManager.createNamedQuery("ProjectPlanEntity.findByProjectId");
		q.setParameter("projectId", pProjectId);
		return new LinkedList<ProjectPlan>(q.getResultList());
	}

	@Override
	public Long findMaxNumVersion(final String pProjectId)
	{
		Query q = entityManager.createNamedQuery("ProjectPlanEntity.findMaxNumVersion");
		q.setParameter("projectId", pProjectId);
		q.setParameter("statusName", ManagementModuleConstants.PROJECT_PLAN_VALIDATE);
		Integer singleResult = (Integer) q.getSingleResult();
		Long result;
		if (singleResult == null)
		{
			result = 0l;
		}
		else
		{
			result = singleResult.longValue();
		}
		return result;
	}

	@Override
	public ProjectPlan findLastValidated(final String pProjectId)
	{
		Query q = entityManager.createNamedQuery("ProjectPlanEntity.findLastValidated");
		q.setMaxResults(1);
		q.setParameter("projectId", pProjectId);
		q.setParameter("statusName", ManagementModuleConstants.PROJECT_PLAN_VALIDATE);
		List<?> resultList = new LinkedList(q.getResultList());
		ProjectPlan result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (ProjectPlan) resultList.get(0);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ProjectPlan findLastFullById(final String pProjectId)
	{
		Query q = entityManager.createNamedQuery("ProjectPlanEntity.findLastFull");
		q.setParameter("projectId", pProjectId);
		final List<ProjectPlan> results = new LinkedList(q.getResultList());
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
	public ProjectPlan findById(final Long pProjectPlanId)
	{
		return entityManager.find(ProjectPlanEntity.class, pProjectPlanId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProjectPlan merge(final ProjectPlan pProjectPlan)
	{
		entityManager.merge(pProjectPlan);
		entityManager.flush();
		return pProjectPlan;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProjectPlan save(final ProjectPlan pProjectPlan)
	{
		entityManager.persist(pProjectPlan);
		entityManager.flush();
		return pProjectPlan;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final ProjectPlan pProjectPlan)
	{
		entityManager.remove(pProjectPlan);
		entityManager.flush();
	}

}
