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

import org.novaforge.forge.tools.deliverymanager.dao.ContentDAO;
import org.novaforge.forge.tools.deliverymanager.entity.ContentEntity;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class defines methods to access to {@link ContentEntity} entity in database
 * 
 * @author Guillaume Lamirand
 */
public class ContentDAOImpl implements ContentDAO
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
	public List<Content> findByDeliveryReference(final String pProjectId, final String pReference)
	{
		final TypedQuery<Content> q = entityManager.createNamedQuery("ContentEntity.findByDeliveryReference",
		    Content.class);

		q.setParameter("projectId", pProjectId);
		q.setParameter("deliveryReference", pReference);
		return q.getResultList();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Content findByDeliveryAndType(final String pProjectId, final String pReference,
	    final ContentType pContentType) throws NoResultException
	{
		final TypedQuery<Content> q = entityManager.createNamedQuery(
		    "ContentEntity.findByDeliveryReferenceAndType", Content.class);
		q.setParameter("projectId", pProjectId);
		q.setParameter("deliveryReference", pReference);
		q.setParameter("type", pContentType.getId());
		return q.getSingleResult();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Content update(final Content pContent)
	{
		entityManager.merge(pContent);
		entityManager.flush();
		return pContent;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(final Content pContent)
	{
		entityManager.remove(pContent);
		entityManager.flush();

	}

}
