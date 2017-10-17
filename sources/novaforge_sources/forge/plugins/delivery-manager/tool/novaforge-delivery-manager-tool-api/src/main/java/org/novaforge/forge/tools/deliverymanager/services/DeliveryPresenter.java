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
import org.novaforge.forge.tools.deliverymanager.model.Artefact;
import org.novaforge.forge.tools.deliverymanager.model.ArtefactParameter;
import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.DeliveryStatus;
import org.novaforge.forge.tools.deliverymanager.model.Folder;

import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public interface DeliveryPresenter
{

   /**
    * @return a new instance of {@link Delivery}
    */
   Delivery newDelivery();

   /**
    * @return a new instance of {@link Artefact}
    */
   Artefact newArtefact();

   /**
    * @return a new instance of {@link ArtefactParameter}
    */
   ArtefactParameter newArtefactParameter();

   /**
    * @return a new instance of {@link Folder}
    */
   Folder newFolder();

   /**
    * Allow to get a list of delivery persisted for a projet
    * 
    * @param pProjectId
    *           represents the project id
    * @return a list of delivery available for a projet
    * @throws DeliveryServiceException
    */
   List<Delivery> getDeliveries(final String pProjectId) throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pUser
    * @return
    * @throws DeliveryServiceException
    */
   boolean generateDelivery(String pProjectId, String pReference, String pUser)
         throws DeliveryServiceException;

   /**
    * Allow to create a delivery. It will :
    * <ul>
    * <li>check the delivery object</li>
    * <li>create the temporary directory on file system</li>
    * <li>persiste the delivery object</li>
    * </ul>
    * 
    * @param pDelivery
    *           represents the delivery to create
    * @param pStatus
    *           represents the status of the delivery
    * @param pType
    *           represents type attribut of the delivery. It will be created if not existing for the project
    * @return the full delivery created
    * @throws DeliveryServiceException
    */
   Delivery createDelivery(final Delivery pDelivery, final DeliveryStatus pStatus, final String pType)
         throws DeliveryServiceException;

   /**
    * Allow to get types available for a project.
    * 
    * @param pProjectId
    *           represents the project id
    * @return list of types as string element
    * @throws DeliveryServiceException
    */
   List<String> getTypes(final String pProjectId) throws DeliveryServiceException;

   /**
    * Allow to delete all content about a delivery t will :
    * <ul>
    * <li>Check if the delivery is existing</li>
    * <li>Delete the temporary directory from file system</li>
    * <li>Delete the delivery object</li>
    * </ul>
    * 
    * @param pProjectId
    *           represents the project id
    * @param pReference
    *           represents the delivery reference to delete
    * @return true if succeed
    * @throws DeliveryServiceException
    */
   boolean deleteDelivery(final String pProjectId, final String pReference) throws DeliveryServiceException;

   /**
    * Allow to get the persisted delivery.
    * 
    * @param pProjectId
    *           represents the project id
    * @param pReference
    *           represents the delivery reference to get
    * @return delivery found
    * @throws DeliveryServiceException
    */
   Delivery getDelivery(final String pProjectId, final String pReference) throws DeliveryServiceException;

   /**
    * Allow to update the persisted delivery. It will only update meta information such as name, description
    * ,reference or type. <i>The contents won't be updated/i>
    * 
    * @param pReference
    *           represents the old reference of the delivery updated
    * @param pDelivery
    *           represents the delivery to update
    * @param pNewType
    *           represents the new type. If null or empty, nothing will be done about it
    * @return new delivery object persisted
    * @throws DeliveryServiceException
    */
   Delivery updateDelivery(final String pReference, final Delivery pDelivery, final String pNewType)
         throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pType
    * @return
    * @throws DeliveryServiceException
    */
   Content getContent(String pProjectId, String pReference, ContentType pType)
         throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pType
    * @return
    * @throws DeliveryServiceException
    */
   boolean updateDeliveryContents(final String pProjectId, final String pReference,
         final List<ContentType> pType) throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pPreviousNode
    * @param pContent
    * @return
    * @throws DeliveryServiceException
    */
   Content updateContentNode(final String pProjectId, final String pReference, final String pPreviousNode,
         final Content pContent) throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @return
    * @throws DeliveryServiceException
    */
   List<Content> getContents(final String pProjectId, final String pReference)
         throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pContentType
    * @return
    * @throws DeliveryServiceException
    */
   String getContentPath(final String pProjectId, final String pReference, ContentType pContentType)
         throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pUrl
    * @param pFileName
    * @return
    * @throws DeliveryServiceException
    */
   boolean getExternalFile(final String pProjectId, final String pReference, final String pUrl,
         final String pFileName) throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pFileName
    * @return
    * @throws DeliveryServiceException
    */
   boolean existFile(final String pProjectId, final String pReference, final String pFileName)
         throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pName
    * @param pNewName
    * @return
    * @throws DeliveryServiceException
    */
   boolean updateFileArtefact(final String pProjectId, final String pReference, final String pName,
         final String pNewName) throws DeliveryServiceException;

   /**
    * Change the delivery status
    * 
    * @param pProjectId
    * @param pReference
    * @param pStatus
    * @return true if success, false otherwise
    * @throws DeliveryServiceException
    */
   boolean changeDeliveryStatus(String pProjectId, String pReference, DeliveryStatus pStatus)
         throws DeliveryServiceException;

   /**
    * Create a default report template
    * 
    * @param pProjectId
    * @throws TemplateReportServiceException
    * @throws DeliveryServiceException
    */
   void createDefaultReportTemplate(String pProjectId) throws DeliveryServiceException;

   /**
    * Delete a default report template
    * 
    * @param pProjectId
    * @throws TemplateReportServiceException
    * @throws DeliveryServiceException
    */
void deleteDefaultReportTemplate(String pProjectId)
		throws DeliveryServiceException;

}
