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
/**
 * 
 */
package org.novaforge.forge.tools.deliverymanager.internal.beans;

import org.novaforge.forge.tools.deliverymanager.dao.TemplateReportDAO;
import org.novaforge.forge.tools.deliverymanager.entity.TemplateReportEntity;
import org.novaforge.forge.tools.deliverymanager.model.TemplateReport;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class defines methods to access to {@link TemplateReportEntity} in database
 * 
 * @author Guillaume Lamirand
 */
public class TemplateReportDAOImpl implements TemplateReportDAO
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
	 * 
	 * @see TemplateReportEntity
	 */
	@Override
	public TemplateReport findByProjectAndName(final String pProjectId, final String pName)
	{
		final TypedQuery<TemplateReport> q = entityManager.createNamedQuery(
		    "TemplateReportEntity.findByProjectAndName", TemplateReport.class);
		q.setParameter("projectId", pProjectId);
		q.setParameter("name", pName);

		return q.getSingleResult();

	}

	/**
	 * {@inheritDoc}
	 *
	 * @see TemplateReportEntity
	 */
	@Override
	public List<TemplateReport> findByProject(final String pProjectId)
	{
		final TypedQuery<TemplateReport> q = entityManager.createNamedQuery("TemplateReportEntity.findByProject",
																																				TemplateReport.class);
		q.setParameter("projectId", pProjectId);

		return q.getResultList();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see TemplateReportEntity
	 */
	@Override
	public TemplateReport findByProjectAndFileName(final String pProjectId, final String pFileName)
	{
		final TypedQuery<TemplateReport> q = entityManager.createNamedQuery("TemplateReportEntity.findByProjectAndFileName",
																																				TemplateReport.class);
		q.setParameter("projectId", pProjectId);
		q.setParameter("fileName", pFileName);

		return q.getSingleResult();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TemplateReport persist(final TemplateReport pReport)
	{
		entityManager.persist(pReport);
		entityManager.flush();
		return pReport;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(final TemplateReport pReport)
	{
		entityManager.remove(pReport);
		entityManager.flush();
	}

}
