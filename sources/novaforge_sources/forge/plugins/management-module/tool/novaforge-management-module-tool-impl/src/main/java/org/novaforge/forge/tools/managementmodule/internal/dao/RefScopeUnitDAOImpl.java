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
import org.novaforge.forge.tools.managementmodule.dao.RefScopeUnitDAO;
import org.novaforge.forge.tools.managementmodule.domain.CDOParameters;
import org.novaforge.forge.tools.managementmodule.domain.RefScopeUnit;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

/**
 * @author vvigo
 */
public class RefScopeUnitDAOImpl implements RefScopeUnitDAO
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
	public RefScopeUnit findByRefScopeUnitId(final String refScopeUnitId, final String version)
	{
		Query q = entityManager.createNamedQuery("RefScopeUnitEntity.findByUnitId");
		q.setParameter("unitId", refScopeUnitId);
		q.setParameter("version", version);
		List<?> resultList = q.getResultList();
		RefScopeUnit result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (RefScopeUnit) resultList.get(0);
		}
		return result;
	}

	@Override
	public RefScopeUnit findByRefScopeUnitName(final String refScopeUnitName, final String refScopeUnitVersion)
	{

		Query q = entityManager.createNamedQuery("RefScopeUnitEntity.findByNameAndVersion");
		q.setParameter("name", refScopeUnitName);
		q.setParameter("version", refScopeUnitVersion);
		List<?> resultList = q.getResultList();
		RefScopeUnit result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (RefScopeUnit) resultList.get(0);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RefScopeUnit> getLastVersionRefScopeUnit(final String projectId)
	{
		Query q = entityManager.createNamedQuery("RefScopeUnitEntity.findLastVersion");
		q.setParameter("projectId", projectId);
		q.setParameter("stateFunctionalId", ManagementModuleConstants.SCOPE_STATUS_OBSOLETE);

		return new LinkedList<RefScopeUnit>(q.getResultList());
	}

	@Override
	public RefScopeUnit getCompleteRefScopeUnit(final String refScopeUnitId, final String version)

	{
		Query q = entityManager.createNamedQuery("RefScopeUnitEntity.findCompleteByUnitId");
		q.setParameter("unitId", refScopeUnitId);
		q.setParameter("version", version);
		List<?> resultList = new LinkedList(q.getResultList());
		RefScopeUnit result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = (RefScopeUnit) resultList.get(0);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RefScopeUnit getLastVersionofThisRefScopeUnit(final String unitId)
	{
		Query q = entityManager.createNamedQuery("RefScopeUnitEntity.findLastVersionUnitId");
		q.setParameter("unitId", unitId);
		List<RefScopeUnit> resultList = q.getResultList();
		RefScopeUnit result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = resultList.get(0);
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RefScopeUnit getLastVersionofThisRefScopeUnitFils(final String unitId)
	{
		Query q = entityManager.createNamedQuery("RefScopeUnitEntityFils.findLastVersionUnitId");
		q.setParameter("unitId", unitId);
		List<RefScopeUnit> resultList = new LinkedList(q.getResultList());
		RefScopeUnit result = null;
		if (resultList != null && resultList.size() == 1)
		{
			result = resultList.get(0);
		}
		return result;
	}

	@Override
	public List<RefScopeUnit> getCompleteAllFromProject(final String projectId,
			final CDOParameters cdoParameters)

	{
		return getCompleteAllFromProject(projectId, cdoParameters, true);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RefScopeUnit> getCompleteAllFromProject(final String projectId,
			final CDOParameters cdoParameters, final boolean withChilds)

	{
		String query;
		if (withChilds)
		{
			query = "RefScopeUnitEntity.findAllFromProjectWithChilds";
		}
		else
		{
			query = "RefScopeUnitEntity.findAllFromProject";
		}

		Query q = entityManager.createNamedQuery(query);
		q.setParameter("projectId", projectId);
		q.setParameter("idCDOParameters", cdoParameters.getId());
		return new LinkedList(q.getResultList());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RefScopeUnit save(final RefScopeUnit pRefScope)
	{
		entityManager.persist(pRefScope);
		entityManager.flush();
		return pRefScope;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RefScopeUnit merge(final RefScopeUnit pRefScopeUnitFinded)
	{
		entityManager.merge(pRefScopeUnitFinded);
		entityManager.flush();
		return pRefScopeUnitFinded;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void delete(final RefScopeUnit pRefScopeUnitfromSGBD)
	{
		entityManager.remove(pRefScopeUnitfromSGBD);
		entityManager.flush();
	}
}
