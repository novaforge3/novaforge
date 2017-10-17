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
package org.novaforge.forge.tools.deliverymanager.dao;

import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to {@link ContentEntity} in database
 * 
 * @author Guillaume Lamirand
 */
public interface ContentDAO
{

	/**
	 * This method allows to get a list of contents available for the reference given as parameter
	 * 
	 * @param pProjectId
	 * @param pReference
	 * @return the list of content available for the delivery reference
	 */
	List<Content> findByDeliveryReference(final String pProjectId, final String pReference);

	/**
	 * This method allows to get a content available for the given reference and type
	 * 
	 * @param pProjectId
	 * @param pReference
	 * @param pContentType
	 * @return content object available for the delivery reference and type
	 * @throws NoResultException
	 */
	Content findByDeliveryAndType(final String pProjectId, String pReference, final ContentType pContentType)
	    throws NoResultException;

	/**
	 * Update {@link Content} into persistence context
	 * 
	 * @param pContent
	 * @return {@link Content} attached
	 */
	Content update(Content pContent);

	/**
	 * Remove {@link Content} from persistence context
	 * 
	 * @param pContent
	 */
	void remove(Content pContent);

}
