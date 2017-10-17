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

import org.novaforge.forge.tools.managementmodule.dao.ApplicativeRightsDAO;
import org.novaforge.forge.tools.managementmodule.domain.ApplicativeRights;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

/**
 * @author fdemange
 */
public class ApplicativeRightsDAOImpl implements ApplicativeRightsDAO
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
	public ApplicativeRights findRightsByRoleAndFunction(final String toolRoleName, final String functionName)
	{
		Query q = entityManager.createNamedQuery("ApplicativeRightsEntity.findRightsByRoleAndFunction");
		q.setParameter("functionName", functionName);
		q.setParameter("roleName", toolRoleName);
		ApplicativeRights result = null;
		@SuppressWarnings({ "unchecked", "rawtypes" })
		List<?> resultList = new LinkedList(q.getResultList());
		if (resultList != null && resultList.size() == 1)
		{
			result = (ApplicativeRights) resultList.get(0);
		}
		return result;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ApplicativeRights save(final ApplicativeRights pS)
	{
		entityManager.persist(pS);
		entityManager.flush();
		return pS;
	}

}
