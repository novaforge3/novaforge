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
package org.novaforge.forge.tools.deliverymanager.dao;

import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to delivery from database
 * 
 * @author Guillaume Lamirand
 */
public interface DeliveryDAO
{
	/**
	 * This method allows to get a unique delivery form its reference id
	 * 
	 * @param pProjectId
	 * @param pReference
	 * @return the delivery entity regarding reference given
	 * @throws NoResultException
	 */
	Delivery findByProjectAndReference(final String pProjectId, String pReference) throws NoResultException;

	/**
	 * This method allows to get a list of delivery available for the given project id
	 * 
	 * @param pProjectId
	 * @return the delivery entity regarding project id given
	 * @throws DataAccessException
	 */
	List<Delivery> findByProject(final String pProjectId);

	/**
	 * This method allows to get a list of delivery available for the given project id and the status
	 * 
	 * @param pProjectId
	 * @param pStatus
	 * @return the delivery entity regarding project id and status given
	 * @throws DataAccessException
	 */
	List<Delivery> findByProjectAndStatus(final String pProjectId, final DeliveryStatus pStatus);

	/**
	 * Will update in persitence context the given {@link Delivery}
	 * 
	 * @param pDelivery
	 * @return {@link Delivery} attached to persistence context
	 */
	Delivery update(Delivery pDelivery);

	/**
	 * Will remove from persitence context the given {@link Delivery}
	 * 
	 * @param pDelivery
	 */
	void remove(Delivery pDelivery);

}
