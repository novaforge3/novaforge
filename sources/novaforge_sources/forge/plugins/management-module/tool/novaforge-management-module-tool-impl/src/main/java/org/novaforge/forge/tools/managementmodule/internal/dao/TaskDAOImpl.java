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

import org.novaforge.forge.tools.managementmodule.dao.TaskDAO;
import org.novaforge.forge.tools.managementmodule.domain.Task;
import org.novaforge.forge.tools.managementmodule.domain.TaskCriteria;
import org.novaforge.forge.tools.managementmodule.entity.TaskEntity;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * EJB/JPA Task implementation
 */
public class TaskDAOImpl implements TaskDAO
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
	public List<Task> findByScopeUnitId(final Long scopeUnitId)
	{
		Query q = entityManager.createNamedQuery("TaskEntity.findByScopeUnitId");
		q.setParameter("scopeUnitId", scopeUnitId);
		List<Task> result = new ArrayList<Task>();
		List<?> resultList = new LinkedList(q.getResultList());
		if (resultList != null)
		{
			for (Object o : resultList)
			{
				result.add((Task) o);
			}
		}
		return result;
	}
	
	 @Override
   public List<Task> findByTaskCategoryId(final Long taskCategoryId)
	  {
	    Query q = entityManager.createNamedQuery("TaskEntity.findByTaskCategoryId");
	    q.setParameter("taskCategoryId", taskCategoryId);
	    List<Task> result = new ArrayList<Task>();
	    List<?> resultList = new LinkedList(q.getResultList());
	    if (resultList != null)
	    {
	      for (Object o : resultList)
	      {
	        result.add((Task) o);
	      }
	    }
	    return result;
	  }


	@Override
	public List<Task> findByScopeUnitIdAndDisciplineId(final Long scopeUnitId, final Long disciplineId)
	{
		Query q = entityManager.createNamedQuery("TaskEntity.findByScopeUnitIdAndDisciplineId");
		q.setParameter("scopeUnitId", scopeUnitId);
		q.setParameter("disciplineId", disciplineId);
		List<Task> result = new ArrayList<Task>();
		List<?> resultList = new LinkedList(q.getResultList());
		if (resultList != null)
		{
			for (Object o : resultList)
			{
				result.add((Task) o);
			}
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Task> findByProjectPlanIdAndDisciplineId(final Long projectPlanId, final String disciplineId)
	{
		if (projectPlanId == null)
		{
			throw new IllegalArgumentException("ProjectPlan id must be not null");
		}

		final StringBuilder querySB = new StringBuilder();

		// Construction de la requete
		querySB.append("SELECT task FROM TaskEntity task ");
		querySB.append(" INNER JOIN task.iterationsTasks iterationTask ");
		querySB.append(" INNER JOIN iterationTask.iteration iteration ");
		querySB.append(" INNER JOIN iteration.lot lot ");
		querySB.append(" INNER JOIN lot.projectPlan projectPlan ");
		querySB.append(" WHERE projectPlan.id = :projectPlanId ");
		if (disciplineId != null && !disciplineId.isEmpty())
		{
			querySB.append(" AND task.discipline.functionalId = :disciplineId");
		}

		final Query query = entityManager.createQuery(querySB.toString());

		// Ajout des parametres
		query.setParameter("projectPlanId", projectPlanId);
		if (disciplineId != null && !disciplineId.isEmpty())
		{
			query.setParameter("disciplineId", disciplineId);
		}

		return new HashSet<Task>(query.getResultList());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Set<Task> getTasksByCriteria(final TaskCriteria taskCriteria)
	{
		final StringBuilder querySB = new StringBuilder();
		querySB.append("SELECT task FROM TaskEntity task ");
		final QueryConjunctionHelper queryBuildingHelper = new QueryConjunctionHelper();
		final Map<String, Object> parametersMap = new HashMap<String, Object>();
		if (taskCriteria.getIterationId() != null)
		{
			querySB.append(" INNER JOIN task.iterationsTasks iterationTask ");
			querySB.append(queryBuildingHelper.getAppropriateValue());
			querySB.append(" iterationTask.iteration.id = :iterationId ");
			parametersMap.put("iterationId", taskCriteria.getIterationId());
		}
		if (taskCriteria.getUserLogin() != null)
		{
			querySB.append(queryBuildingHelper.getAppropriateValue());
			querySB.append(" task.user.login = :login ");
			parametersMap.put("login", taskCriteria.getUserLogin());
		}
		if (taskCriteria.getTaskStatusFunctionalId() != null)
		{
			querySB.append(queryBuildingHelper.getAppropriateValue());
			querySB.append(" task.status.functionalId = :statusFunctionalId ");
			parametersMap.put("statusFunctionalId", taskCriteria.getTaskStatusFunctionalId());
		}
		if (taskCriteria.getTaskTypeFunctionalId() != null)
		{
			querySB.append(queryBuildingHelper.getAppropriateValue());
			querySB.append(" task.taskType.functionalId = :typeFunctionalId ");
			parametersMap.put("typeFunctionalId", taskCriteria.getTaskTypeFunctionalId());
		}
		if (taskCriteria.getDisciplineFunctionalId() != null)
		{
			querySB.append(queryBuildingHelper.getAppropriateValue());
			querySB.append(" task.discipline.functionalId = :disciplineFunctionalId ");
			parametersMap.put("disciplineFunctionalId", taskCriteria.getDisciplineFunctionalId());
		}
		final Query query = entityManager.createQuery(querySB.toString());
		// we set the parameters
		for (final Entry<String, Object> entry : parametersMap.entrySet())
		{
			query.setParameter(entry.getKey(), entry.getValue());
		}
		return new HashSet<Task>(query.getResultList());
	}

	@Override
	public void deleteByProjectPlanId(final Long projectPlanId)
	{
		final Query q = entityManager.createNamedQuery("TaskEntity.deleteByProjectPlanId");
		q.setParameter("projectPlanId", projectPlanId);
		q.executeUpdate();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task findById(final long pTaskId)
	{
		return entityManager.find(TaskEntity.class, pTaskId);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task save(final Task pTask)
	{
		entityManager.persist(pTask);
		entityManager.flush();
		return pTask;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Task merge(final Task pTask)
	{
		entityManager.merge(pTask);
		entityManager.flush();
		return pTask;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final Task pFindById)
	{
		entityManager.remove(pFindById);
		entityManager.flush();
	}

	/**
	 * A little helper to facilitate query creation with "where" and "and"
	 */
	private class QueryConjunctionHelper
	{
		boolean first = true;

		public String getAppropriateValue()
		{
			if (first)
			{
				first = false;
				return " WHERE ";
			}
			return " AND ";
		}
	}
}
