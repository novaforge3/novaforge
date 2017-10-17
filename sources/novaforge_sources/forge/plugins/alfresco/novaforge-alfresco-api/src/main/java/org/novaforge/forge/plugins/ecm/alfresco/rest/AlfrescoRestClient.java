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
package org.novaforge.forge.plugins.ecm.alfresco.rest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestException;

import java.util.Map;
import java.util.Set;

/**
 * This interface describes client service used to communicate with alfresco instance.
 * 
 * @author cadetr
 */
public interface AlfrescoRestClient
{

   /**
    * Use to connect to alfresco instance with specific url, username and password.
    * 
    * @param baseUrl
    *           represents the url of alfresco instance
    * @param username
    *           represents username used to log in
    * @param password
    *           represents password used to log in
    * @throws AlfrescoRestException
    *            can occured if connection failed or client can be built
    */
   AlfrescoRestHelper getConnector(String baseUrl, String username, String password)
         throws AlfrescoRestException;

   /**
    * Allow to create a repository on alfresco instance.
    * 
    * @param pProject
    *           represents the repository resource to create
    * @return repository resource which came from alfresco response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject createProject(final AlfrescoRestHelper pConnector, final Map<String, String> pProject)
         throws AlfrescoRestException;

   /**
    * Allow to update a project on alfresco instance.
    * 
    * @param pProject
    *           represents the project resource to update
    * @return jsonobject which came from alfresco response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject updateProject(final AlfrescoRestHelper pConnector, final Map<String, String> pProject)
         throws AlfrescoRestException;

   /**
    * Allow to delete a repository on alfresco instance.
    * 
    * @param pProject
    *           represents the repository'id used to delete a repository
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   void deleteProject(final AlfrescoRestHelper pConnector, final Map<String, String> pProject)
         throws AlfrescoRestException;

   /**
    * Allow to create user on alfresco instance.
    * 
    * @param pUser
    *           represents user to create
    * @return UserResource response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject createUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUser)
         throws AlfrescoRestException;

   /**
    * Allow to update user on alfresco instance.
    * 
    * @param pUser
    *           represents user to update
    * @return UserResource response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject updateUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUser)
         throws AlfrescoRestException;

   /**
    * Allow to delete user on alfresco instance.
    * 
    * @param pUserId
    *           represents user id to delete
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   void deleteUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUserId)
         throws AlfrescoRestException;

   /**
    * Allow to get user resource from alfresco instance.
    * 
    * @param pUser
    *           represents user to obtain
    * @return UserResource got
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject getUser(final AlfrescoRestHelper pConnector, final String pUser) throws AlfrescoRestException;

   /**
    * Allow to get roles from alfresco instance.
    * 
    * @return list of roles resource
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   Set<String> getRoles(final AlfrescoRestHelper pConnector) throws AlfrescoRestException;

   /**
    * Allow to associate a user to a project on alfresco instance.
    * 
    * @param pUserProject
    *           a map that represents a user and the associated project
    * @return UserResource response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject inviteUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUserProject)
         throws AlfrescoRestException;

   /**
    * Allow to disassociate a user to a project on alfresco instance.
    * 
    * @param pUserProject
    *           a map that represents a user and the associated project
    * @return UserResource response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject disinviteUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUserProject)
         throws AlfrescoRestException;

   /**
    * Allow to create a site project on alfresco instance.
    * 
    * @param pProject
    *           represents the project resource to create
    * @return json resource which came from alfresco response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject createSiteProject(final AlfrescoRestHelper pConnector, final JSONObject pProject)
         throws AlfrescoRestException;

   /**
    * Allow to update a site project on alfresco instance.
    * 
    * @param pProject
    *           represents the project resource to update
    * @return jsonobject which came from alfresco response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject updateSiteProject(final AlfrescoRestHelper pConnector, final JSONObject pProject)
         throws AlfrescoRestException;

   /**
    * Allow to delete a site project on alfresco instance.
    * 
    * @param pProject
    *           represents the repository'id used to delete a repository
    * @return jsonobject which came from alfresco response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject deleteSiteProject(final AlfrescoRestHelper pConnector, final JSONObject pProject)
         throws AlfrescoRestException;

   /**
    * Allow to associate a user to a site project on alfresco instance with a specific role
    * 
    * @param pUserProject
    *           a map that represents a user and the associated project
    * @return json response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject inviteSiteUser(final AlfrescoRestHelper pConnector, final JSONObject pProjectUser)
         throws AlfrescoRestException;

   /**
    * Allow to disassociate a user to a site project on alfresco instance.
    * 
    * @param pUserProject
    *           a map that represents a user and the associated project
    * @return json response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject disinviteSiteUser(final AlfrescoRestHelper pConnector, final JSONObject pUserProject)
         throws AlfrescoRestException;

   /**
    * Allow to create an administrator user on alfresco instance.
    * 
    * @param pUser
    *           represents the admin user resource to create
    * @return json resource which came from alfresco response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   JSONObject createUserAdmin(final AlfrescoRestHelper pConnector, final Map<String, String> pUser)
         throws AlfrescoRestException;

   JSONArray getUserAdmin(final AlfrescoRestHelper pConnector) throws AlfrescoRestException;

   /**
    * Allow to get site id from site name
    * 
    * @param pSiteName
    *           a project site short name
    * @return json response
    * @throws AlfrescoRestException
    *            can occured if the communication with alfresco instance failed
    */
   String getSiteId(final AlfrescoRestHelper pConnector, final String pSiteName)
         throws AlfrescoRestException;

}