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

import org.novaforge.forge.tools.deliverymanager.model.DeliveryType;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to delivery type from database
 * 
 * @see DeliveryType
 * @author Guillaume Lamirand
 */
public interface DeliveryTypeDAO
{
	/**
	 * This method allows to get a unique delivery type form its label and projectid
	 * 
	 * @param pProjectId
	 *          represents the project id
	 * @param pLabel
	 *          represents the type label we want to find
	 * @return the delivery entity regarding reference given
	 * @throws NoResultException
	 *           if type cannot be found
	 * @see DeliveryType
	 */
	DeliveryType findByProjectAndLabel(final String pProjectId, final String pLabel) throws NoResultException;

	/**
	 * This method allows to get delivery types form their projectid
	 * 
	 * @param pProjectId
	 * @return a list of delivery type entity regarding reference given
	 * @throws DataAccessException
	 *           if persistence error occured
	 * @see DeliveryType
	 */
	List<DeliveryType> findByProject(final String pProjectId);

	/**
	 * wil persist {@link DeliveryType} into peristence context
	 * 
	 * @param pDeliveryType
	 * @return {@link DeliveryType} attached
	 */
	DeliveryType persist(DeliveryType pDeliveryType);

}
