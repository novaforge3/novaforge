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
import org.novaforge.forge.tools.deliverymanager.model.BugTrackerIssue;
import org.novaforge.forge.tools.deliverymanager.model.ECMNode;
import org.novaforge.forge.tools.deliverymanager.model.SCMNode;

import java.util.List;

/**
 * Defined a service which allow to communicate with delivery plugin or forge
 * 
 * @author Guillaume Lamirand
 */
public interface DeliveryPluginService
{

   /**
    * This method will return the project id refered to the instance id given in parameter
    * 
    * @param pInstanceId
    * @return project id
    * @throws DeliveryServiceException
    */
   String getProjectId(final String pInstanceId) throws DeliveryServiceException;

   /**
    * This method will return the instance id refered to the project id given in parameter
    * 
    * @param pProjectId
    * @return instance id
    * @throws DeliveryServiceException
    */
   String getInstanceId(String pProjectId) throws DeliveryServiceException;

   /**
    * This method will return the versions available on the bugtracker associated to the delivery manager
    * 
    * @param pProjectId
    *           represents project id used by delivery application
    * @param pUser
    *           represents the user who is sending the request
    * @return lsit of version as string
    * @throws DeliveryServiceException
    */
   List<String> getIssuesVersion(final String pProjectId, final String pUser) throws DeliveryServiceException;

   /**
    * This method will return the issues available on the bugtracker for the version given associated to the
    * delivery manager
    * 
    * @param pProjectId
    *           represents project id used by delivery application
    * @param pVersion
    * @param pUser
    *           represents the user who is sending the request
    * @return a list of {@link BugTrackerIssue} available for the version given
    * @throws DeliveryServiceException
    */
   List<BugTrackerIssue> getIssues(final String pProjectId, final String pVersion, final String pUser)
         throws DeliveryServiceException;

   /**
    * This method will return the root node on <code>Category.ECM</code> application associated to the
    * delivery manager
    * 
    * @param pProjectId
    *           represents project id used by delivery application
    * @param pUser
    *           represents the user who is sending the request
    * @return root {@link ECMNode}
    * @throws DeliveryServiceException
    */
   ECMNode getECMNode(final String pProjectId, final String pUser) throws DeliveryServiceException;

   /**
    * This method will return the root node on <code>Category.SCM</code> application associated to the
    * delivery manager
    * 
    * @param pProjectId
    *           represents project id used by delivery application
    * @param pUser
    *           represents the user who is sending the request
    * @return root {@link SCMNode}
    * @throws DeliveryServiceException
    */
   SCMNode getSCMNode(final String pProjectId, final String pUser) throws DeliveryServiceException;

   /**
    * @param pProjectId
    *           represents project id used by delivery application
    * @return true if a scm is available
    * @throws DeliveryServiceException
    */
   boolean hasSCMAvailable(final String pProjectId) throws DeliveryServiceException;

   /**
    * @param pProjectId
    *           represents project id used by delivery application
    * @return true if a ecm is available
    * @throws DeliveryServiceException
    */
   boolean hasECMAvailable(final String pProjectId) throws DeliveryServiceException;

   /**
    * @param pProjectId
    *           represents project id used by delivery application
    * @return true if a bugtracker is available
    * @throws DeliveryServiceException
    */
   boolean hasBugtrackerAvailable(final String pProjectId) throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pDestinationPath
    * @param pDocuments
    * @param pUser
    * @return
    * @throws DeliveryServiceException
    */
   boolean getSCMDocumentToDelivery(String pProjectId, String pReference, String pDestinationPath,
         List<Artefact> pDocuments, String pUser) throws DeliveryServiceException;

   /**
    * @param pProjectId
    * @param pReference
    * @param pDestinationPath
    * @param pDocuments
    * @param pUser
    * @return
    * @throws DeliveryServiceException
    */
   boolean getECMDocumentToDelivery(String pProjectId, String pReference, String pDestinationPath,
         List<Artefact> pDocuments, String pUser) throws DeliveryServiceException;

}