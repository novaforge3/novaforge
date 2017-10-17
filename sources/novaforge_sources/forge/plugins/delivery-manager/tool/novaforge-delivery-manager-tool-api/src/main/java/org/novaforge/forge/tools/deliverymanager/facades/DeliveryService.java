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
package org.novaforge.forge.tools.deliverymanager.facades;

import org.novaforge.forge.tools.deliverymanager.exceptions.DeliveryServiceException;
import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;
import org.novaforge.forge.tools.deliverymanager.model.Folder;

import java.util.List;

/**
 * This interface describes a service which will deal with persistence delivery information.
 * 
 * @author Guillaume Lamirand
 */
public interface DeliveryService
{

	/**
	 * This method will return you an instance of the persistence object. It will be empty.
	 * 
	 * @return an new instance
	 * @see Delivery
	 */
	Delivery newDelivery();

	/**
	 * This method will :
	 * <ul>
	 * <li>Check if the type given in parameter exists, otherwise it will create it.</li>
	 * <li>Associate the delivery to the type given</li>
	 * <li>Associate the delivery to the status given</li>
	 * <li>Persiste the delivery object given</li>
	 * </ul>
	 * 
	 * @param pDelivery
	 *          represents the delivery which will be persisted
	 * @param pStatus
	 *          represents the status of the delivery from {@link DeliveryStatus}
	 * @param pTypeLabel
	 *          represents the type choosen for the delivery
	 * @return the new persist {@link Delivery}
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Delivery
	 * @see DeliveryStatus
	 */
	Delivery createDelivery(final Delivery pDelivery, final DeliveryStatus pStatus, final String pTypeLabel)
	    throws DeliveryServiceException;

	/**
	 * This method will :
	 * <ul>
	 * <li>Get the existing delivery from the parameters given</li>
	 * <li>Update its status</li>
	 * <li>Persiste the delivery</li>
	 * </ul>
	 * 
	 * @param pProjectId
	 *          represents the project id of the delivery we want to change
	 * @param pReference
	 *          represents the reference of the delivery we want to change
	 * @param pDeliveryStatus
	 *          represents new status of the delivery
	 * @return the updated {@link Delivery}
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Delivery
	 * @see DeliveryStatus
	 */
	Delivery changeStatus(final String pProjectId, final String pReference, final DeliveryStatus pDeliveryStatus)
	    throws DeliveryServiceException;

	/**
	 * This method will return the {@link Delivery} found with the project id and reference given
	 * 
	 * @param pProjectId
	 *          represents the project id of the delivery we want to found
	 * @param pReference
	 *          represents the reference of the delivery we want to found
	 * @return the persist {@link Delivery}
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Delivery
	 */
	Delivery getDelivery(final String pProjectId, final String pReference) throws DeliveryServiceException;

	/**
	 * This method will return all {@link Delivery} found with the project id
	 * 
	 * @param pProjectId
	 *          represents the project id of the deliveries we want to found
	 * @return list of deliveries found
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Delivery
	 */
	List<Delivery> getDeliveries(final String pProjectId) throws DeliveryServiceException;

	/**
	 * This method will return the type found for the project id given
	 * 
	 * @param pProjectId
	 *          represents the project id of the type
	 * @return list of type as string object
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 */
	List<String> getTypes(final String pProjectId) throws DeliveryServiceException;

	/**
	 * This method will :
	 * <ul>
	 * <li>Get the existing delivery from the parameters given</li>
	 * <li>Delete it</li>
	 * </ul>
	 * 
	 * @param pProjectId
	 *          represents the project id of the delivery we want to delete
	 * @param pReference
	 *          represents the reference of the delivery we want to delete
	 * @return true if succeed, otherwise an exception is thrown
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 */
	boolean deleteDelivery(final String pProjectId, final String pReference) throws DeliveryServiceException;

	/**
	 * This method allow to check if a reference is already existing for a project id in the persistence
	 * context
	 * 
	 * @param pProjectId
	 *          represents the project id of the delivery we want to delete
	 * @param pReference
	 *          represents the reference of the delivery we want to delete
	 * @return true if the reference is already used, flase otherwise
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 */
	boolean existReference(final String pProjectId, final String pReference) throws DeliveryServiceException;

	/**
	 * This method will only update the following element :
	 * <ul>
	 * <li>delivery's name</li>
	 * <li>delivery's reference</li>
	 * <li>delivery's version</li>
	 * <li>delivery's type (If the new type is not existing, it will be created.)</li>
	 * </ul>
	 * 
	 * @param pDelivery
	 *          represents the new delivery object
	 * @param pType
	 *          represents the new delivery type
	 * @return the updated {@link Delivery}
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Delivery
	 */
	Delivery updateDelivery(final Delivery pDelivery, final String pType) throws DeliveryServiceException;

	/**
	 * This method will return a list of {@link Content} available for a delivery.
	 * 
	 * @param pProjectId
	 *          represents the project id of the delivery
	 * @param pReference
	 *          represents the reference of the delivery
	 * @return the list of content available
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Content
	 */
	List<Content> getContents(final String pProjectId, final String pReference) throws DeliveryServiceException;

	/**
	 * This method will return the {@link Content} available for a delivery with a specific {@link ContentType}
	 * 
	 * @param pProjectId
	 *          represents the project id of the delivery
	 * @param pReference
	 *          represents the reference of the delivery
	 * @param pType
	 *          represents the type of content we are looking for
	 * @return {@link Content} found
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Content
	 * @see ContentType
	 */
	Content getContent(final String pProjectId, final String pReference, final ContentType pType)
	    throws DeliveryServiceException;

	/**
	 * This method should be used to update the root node of a content and its children. It will just ereased
	 * the existing one by the new one.
	 * 
	 * @param pProjectId
	 *          represents the project id of the delivery
	 * @param pReference
	 *          represents the reference of the delivery
	 * @param pContent
	 *          represents the new content which contains the new node
	 * @return {@link Content} updated
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Content
	 */
	Content updateContentNode(final String pProjectId, final String pReference, final Content pContent)
	    throws DeliveryServiceException;

	/**
	 * This method will delete a content for a specific delivery according to the {@link ContentType} given
	 * 
	 * @param pProjectId
	 *          represents the project id of the delivery
	 * @param pReference
	 *          represents the reference of the delivery
	 * @param pType
	 *          represents the type of content we want to delete
	 * @return true if succeed, otherwise an exception is thrown
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Content
	 * @see ContentType
	 */
	boolean deleteContent(final String pProjectId, final String pReference, final ContentType pType)
	    throws DeliveryServiceException;

	/**
	 * This method will craete a new content for a delivery according to the parameters given. The new content
	 * will be added to the delivery but it won't have any root node.
	 * 
	 * @param pProjectId
	 *          represents the project id of the delivery
	 * @param pReference
	 *          represents the reference of the delivery
	 * @param pType
	 *          represents the type of content we want to create
	 * @return {@link Content} created
	 * @throws DeliveryServiceException
	 *           exception thrown if a error occured during persitence process
	 * @see Content
	 * @see ContentType
	 */
	void createContent(final String pProjectId, final String pReference, final ContentType pType)
	    throws DeliveryServiceException;

	/**
	 * This method will return you an instance of the persistence object. It will be empty.
	 * 
	 * @return an new instance
	 * @see Artefact
	 */
	Artefact newArtefact();

	/**
	 * This method will return you an instance of the persistence object. It will be empty.
	 * 
	 * @return an new instance
	 * @see Folder
	 */
	Folder newFolder();

	/**
	 * This method will return you an instance of the persistence object. It will be empty.
	 * 
	 * @return an new instance
	 * @see ArtefactParameter
	 */
	ArtefactParameter newArtefactParameter();

}