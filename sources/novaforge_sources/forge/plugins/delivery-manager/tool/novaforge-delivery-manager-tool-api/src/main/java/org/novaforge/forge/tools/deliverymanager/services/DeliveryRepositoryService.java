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
package org.novaforge.forge.tools.deliverymanager.services;

import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;

/**
 * Define a service which will handler file repository for a delivery application
 * 
 * @author Guillaume Lamirand
 */
public interface DeliveryRepositoryService
{

	/**
	 * Create the root directory for a project delivery
	 * 
	 * @param pProjectId
	 * @throws DeliveryServiceException
	 */
	void createProjectDirectory(final String pProjectId) throws DeliveryServiceException;

	/**
	 * Create the template directory for a project delivery
	 * 
	 * @param pProjectId
	 * @return
	 */
	String getTemplateDirectory(final String pProjectId);

	/**
	 * Create root delivery directory, temp and final one too.
	 * 
	 * @param pProjectId
	 * @param pReference
	 * @throws DeliveryServiceException
	 */
	void createDeliveryDirectories(final String pProjectId, final String pReference)
			throws DeliveryServiceException;

	/**
	 * create delivery archive
	 * 
	 * @param pProjectId
	 * @param pReference
	 * @throws DeliveryServiceException
	 */
	void createArchive(String pProjectId, String pReference) throws DeliveryServiceException;

	/**
	 * return the archive full name for a given project and delivery reference
	 * 
	 * @param pProjectId
	 * @param pReference
	 * @return archive full name
	 * @throws DeliveryServiceException
	 */
	String getDeliveryArchivePath(String pProjectId, String pReference) throws DeliveryServiceException;

	/**
	 * return the path of a delivery temporary path
	 * 
	 * @param pProjectId
	 * @param pDeliveryId
	 * @return archive Full Name
	 * @throws DeliveryServiceException
	 */
	String getDeliveryTemporaryPath(String pProjectId, String pDeliveryId) throws DeliveryServiceException;

	/**
	 * @param pProjectId
	 * @return
	 */
	String getProjectDirectory(String pProjectId);

	/**
	 * @param pProjectId
	 * @param pReferenceId
	 * @param pContentPath
	 * @throws DeliveryServiceException
	 */
	void emptyDeliveryTempContentDirectory(final String pProjectId, final String pReferenceId,
			final String pContentPath) throws DeliveryServiceException;

	/**
	 * @param pProjectId
	 * @param pReferenceId
	 * @param pContentPath
	 * @throws DeliveryServiceException
	 */
	void deleteDeliveryTempContentDirectory(final String pProjectId, final String pReferenceId,
			final String pContentPath) throws DeliveryServiceException;

	/**
	 * @param pProjectId
	 * @param pReferenceId
	 * @param pDirectoryPath
	 * @throws DeliveryServiceException
	 */
	void createDirectoryInDeliveryContent(String pProjectId, String pReferenceId, String pDirectoryPath)
			throws DeliveryServiceException;

	/**
	 * Get the delivery archive filename
	 * 
	 * @param pProjectId
	 * @param pReference
	 * @return The delivery archive filename
	 * @throws DeliveryServiceException
	 */
	String getDeliveryArchiveFileName(String pProjectId, String pReference) throws DeliveryServiceException;

	/**
	 * Get the delivery template sample project path
	 * 
	 * @return The delivery template sample project path
	 * @throws DeliveryServiceException
	 */
	String getDeliveryTemplateSamplePath() throws DeliveryServiceException;

	/**
	 * Get the default template filename
	 * 
	 * @return The default template filename
	 */
	String getTemplateDefaultFilename();

	/**
	 * Create default template file in delivery directory.
	 * 
	 * @param pProjectId
	 * @throws DeliveryServiceException
	 */
	void createDefaultTemplateFile(String pProjectId) throws DeliveryServiceException;

	/**
	 * Update delivery directory name with new delivery reference
	 * 
	 * @param pProjectId
	 *           the project id
	 * @param pReference
	 *           the delivery reference
	 * @param pNewReference
	 *           the delivery new reference
	 * @throws DeliveryServiceException
	 */
	void updateDeliveryReferenceDirectoryName(String pProjectId, String pReference, String pNewReference)
			throws DeliveryServiceException;

}
