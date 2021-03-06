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

import org.novaforge.forge.tools.deliverymanager.dao.DeliveryDAO;
import org.novaforge.forge.tools.deliverymanager.entity.DeliveryEntity;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class defines methods to access to {@link DeliveryEntity} in database
 * 
 * @author Guillaume Lamirand
 */
public class DeliveryDAOImpl implements DeliveryDAO
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
	public Delivery findByProjectAndReference(final String pProject, final String pReference)
	    throws NoResultException
	{
		final TypedQuery<Delivery> q = entityManager.createNamedQuery("DeliveryEntity.findByProjectAndReference",
		    Delivery.class);
		q.setParameter("projectId", pProject);
		q.setParameter("reference", pReference);

		return q.getSingleResult();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Delivery> findByProject(final String pProjectId)
	{
		final TypedQuery<Delivery> q = entityManager.createNamedQuery("DeliveryEntity.findByProject",
		    Delivery.class);
		q.setParameter("projectId", pProjectId);
		return q.getResultList();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Delivery> findByProjectAndStatus(final String pProjectId, final DeliveryStatus pStatus)
	{
		final TypedQuery<Delivery> q = entityManager.createNamedQuery("DeliveryEntity.findByProjectAndStatus",
		    Delivery.class);
		q.setParameter("projectId", pProjectId);
		q.setParameter("status", pStatus.name());
		return q.getResultList();

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Delivery update(final Delivery pDelivery)
	{
		entityManager.merge(pDelivery);
		entityManager.flush();
		return pDelivery;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void remove(final Delivery pDelivery)
	{
		entityManager.remove(pDelivery);
		entityManager.flush();

	}

}
