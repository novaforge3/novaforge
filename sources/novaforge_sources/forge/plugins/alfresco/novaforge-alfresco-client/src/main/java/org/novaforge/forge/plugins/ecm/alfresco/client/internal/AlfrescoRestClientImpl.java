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
package org.novaforge.forge.plugins.ecm.alfresco.client.internal;

/**
 * @author cadetr
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.novaforge.forge.plugins.ecm.alfresco.library.AlfrescoRestHelperCustom;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestClient;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestHelper;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestConnectionException;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class is an implementation of AlfrescoRestClientCustom interface.
 * 
 * @see org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestClient
 * @author cadetr
 */
public class AlfrescoRestClientImpl implements AlfrescoRestClient
{

   /**
    * {@inheritDoc}
    */
   @Override
   public AlfrescoRestHelper getConnector(final String pBaseUrl, final String pUsername,
         final String pPassword) throws AlfrescoRestException
         {
      AlfrescoRestHelper clientHelper;
      try
      {
         clientHelper = new AlfrescoRestHelperCustom(pBaseUrl, pUsername, pPassword);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException(e.getMessage(), e);
      }
      return clientHelper;
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONObject createProject(final AlfrescoRestHelper pConnector, final Map<String, String> pProject)
         throws AlfrescoRestException
         {
      Object tempObj;
      try
      {
         tempObj = pConnector.getJSON("org/novaforge/forge/createProject", pProject);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException(String.format("Cannot create project with [name=%s]",
               pProject.get("projectName")), e);
      }
      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONObject updateProject(final AlfrescoRestHelper pConnector, final Map<String, String> pProject)
         throws AlfrescoRestException
         {
            Object tempObj;
      try
      {
         tempObj = pConnector.getJSON("org/novaforge/forge/updateProject", pProject);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException(String.format("Cannot update project with [name=%s, newName=%s]",
                                                       pProject.get("name"), pProject.get("newName")), e);
      }
            // Check the return type
            checkType(tempObj, JSONObject.class);

            return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteProject(final AlfrescoRestHelper pConnector, final Map<String, String> pProject)
         throws AlfrescoRestException
         {
      try
      {
         pConnector.getJSON("org/novaforge/forge/deleteProject", pProject);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException(String.format("Cannot delete project with [name=%s]",
                                                       pProject.get("projectName")), e);
      }
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONObject createUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUser)
         throws AlfrescoRestException
         {
      Object tempObj;
      try
      {
         tempObj = pConnector.getJSON("org/novaforge/forge/createUser", pUser);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException(String.format(
               "Cannot create user with [login=%s, firstname=%s, lastname=%s, email=%s, pwd=%s]",
               pUser.get("login"), pUser.get("firstName"), pUser.get("lastName"), pUser.get("mail"),
               pUser.get("password")), e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;

         }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONObject updateUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUser)
         throws AlfrescoRestException
         {
      Object tempObj;
      try
      {
         tempObj = pConnector.getJSON("org/novaforge/forge/updateUser", pUser);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException(String.format(
               "Cannot update user with [login=%s, firstname=%s, lastname=%s, email=%s, pwd=%s]",
               pUser.get("login"), pUser.get("firstName"), pUser.get("lastName"), pUser.get("mail"),
               pUser.get("password")), e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;

         }

   /**
    * {@inheritDoc}
    */
   @Override
   public void deleteUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUser)
         throws AlfrescoRestException
         {
      try
      {
         pConnector.getJSON("org/novaforge/forge/deleteUser", pUser);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException(String.format("Cannot delete user with [login=%s]",
               pUser.get("login")), e);
      }
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONObject getUser(final AlfrescoRestHelper pConnector, final String userName)
         throws AlfrescoRestException
         {
      Object tempObj;
      try
      {
         tempObj = pConnector.getJSON("api/people/" + userName, null);
      }
      catch (AlfrescoRestConnectionException e)
      {
         return null;
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;

         }

   /**
    * {@inheritDoc}
    */
   @Override
   public Set<String> getRoles(final AlfrescoRestHelper pConnector) throws AlfrescoRestException
   {
      Set<String> rolesList = new TreeSet<String>();
      Object tempObj;
      try
      {
         tempObj = pConnector.getJSON("org/novaforge/forge/getSiteRoles", null);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException("Cannot get the list of roles available on alfresco instance.", e);
      }

      try
      {
         // Check the return type
         checkType(tempObj, JSONObject.class);
         // Get the result and stock it in a list
         String roles = ((JSONObject) tempObj).getJSONObject("project").getString("roles");
         String[] rolesArray = roles.substring(1, roles.length() - 1).split(",");
         for (String element : rolesArray)
         {
            rolesList.add(element.trim());
         }
      }
      catch (JSONException e)
      {
         throw new AlfrescoRestException("Cannot get the list of roles available on alfresco instance.", e);
      }

      return rolesList;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONObject inviteUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUserProject)
         throws AlfrescoRestException
         {
      Object tempObj;
      try
      {
         tempObj = pConnector.getJSON("org/novaforge/forge/inviteUser", pUserProject);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException(String.format("Cannot invite user with [username=%s, projectname=%s, role=%s]",
               pUserProject.get("userName"), pUserProject.get("projectName"), pUserProject.get("role")), e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONObject disinviteUser(final AlfrescoRestHelper pConnector, final Map<String, String> pUserProject)
       throws AlfrescoRestException
   {
      Object tempObj;
      try
      {
         tempObj = pConnector.getJSON("org/novaforge/forge/disinviteUser", pUserProject);
      }
      catch (AlfrescoRestConnectionException e)
      {
         throw new AlfrescoRestException(String
                                             .format("Cannot uninvite user with [username=%s, projectname=%s, role=%s]",
                                                     pUserProject.get("userName"), pUserProject.get("projectName"),
                                                     pUserProject.get("role")), e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    *
    * @throws AlfrescoRestException
    */
   @Override
   public JSONObject createSiteProject(final AlfrescoRestHelper pConnector, final JSONObject pProject)
         throws AlfrescoRestException
         {
      Object tempObj;
      try
      {
         try
         {
            tempObj = pConnector.postJSON("api/sites", pProject);
         }
         catch (AlfrescoRestConnectionException e)
         {

            throw new AlfrescoRestException(String.format("Cannot create project with [name=%s]",
                  pProject.getString("shortName")), e);
         }
      }
      catch (JSONException e)
      {
         throw new AlfrescoRestException("Unable to build JSONObject for createSiteProject method", e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    *
    * @throws AlfrescoRestException
    */
   @Override
   public JSONObject updateSiteProject(final AlfrescoRestHelper pConnector, final JSONObject pProject)
         throws AlfrescoRestException
         {
            Object tempObj;
      try
      {
         try
         {
            tempObj = pConnector.putJSON("api/sites/" + pProject.getString("shortName"), pProject);

         }
         catch (AlfrescoRestConnectionException e)
         {

            throw new AlfrescoRestException(String.format("Cannot update project site with [name=%s]",
                                                          pProject.getString("shortName")), e);
         }
      }
      catch (JSONException e)
      {
         throw new AlfrescoRestException("Unable to build JSONObject for updateSiteProject method", e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    *
    * @throws AlfrescoRestException
    */
   @Override
   public JSONObject deleteSiteProject(final AlfrescoRestHelper pConnector, final JSONObject pProject)
         throws AlfrescoRestException
         {
            Object tempObj;
      try
      {
         try
         {
            tempObj = pConnector.deleteJSON("api/sites/" + pProject.getString("shortName"), pProject);

         }
         catch (AlfrescoRestConnectionException e)
         {

            throw new AlfrescoRestException(String.format("Cannot delete project site with [name=%s]",
                  pProject.getString("shortName")), e);
         }
      }
      catch (JSONException e)
      {
         throw new AlfrescoRestException("Unable to build JSONObject for deleteSiteProject method", e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    *
    * @throws AlfrescoRestException
    */
   @Override
   public JSONObject inviteSiteUser(final AlfrescoRestHelper pConnector, final JSONObject pProjectUser)
         throws AlfrescoRestException
         {
            Object tempObj;
      try
      {
         try
         {
            tempObj = pConnector.postJSON("api/sites/" + pProjectUser.getString("shortName") + "/memberships",
                                          pProjectUser);

         }
         catch (AlfrescoRestConnectionException e)
         {

            throw new AlfrescoRestException(String.format("Cannot invite user to project with [user=%s, project=%s]",
                                                          pProjectUser.getJSONObject("person").getString("userName"),
                                                          pProjectUser.getString("shortName")), e);
         }
      }
      catch (JSONException e)
      {
         throw new AlfrescoRestException("Unable to build JSONObject for inviteSiteUser method", e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONObject disinviteSiteUser(final AlfrescoRestHelper pConnector, final JSONObject pUserProject)
         throws AlfrescoRestException
         {
            Object tempObj;
      try
      {

         try
         {
            tempObj = pConnector.deleteJSON("api/sites/" + pUserProject.getString("shortName")
                  + "/memberships/" + pUserProject.getJSONObject("person").getString("userName"),
                  pUserProject);

         }
         catch (AlfrescoRestConnectionException e)
         {
            throw new AlfrescoRestException(String.format(
                  "Cannot disinvite user site project with [name=%s]", pUserProject.getJSONObject("person")
                  .getString("userName")), e);
         }
      }
      catch (JSONException e)
      {
         throw new AlfrescoRestException("Unable to build JSONObject for disinviteSiteUser method", e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONObject createUserAdmin(final AlfrescoRestHelper pConnector, final Map<String, String> pUser)
         throws AlfrescoRestException
         {
      Object tempObj;
      try
      {
         tempObj = pConnector.postJSON("api/groups/ALFRESCO_ADMINISTRATORS/children/" + pUser.get("login"),
               null);
      }
      catch (AlfrescoRestConnectionException e)
      {

         throw new AlfrescoRestException(String.format("Cannot create administrator user with [name=%s]",
               pUser.get("login")), e);
      }
      catch (Exception e)
      {
         throw new AlfrescoRestException("Unable to build JSONObject for createUserAdmin method", e);
      }

      // Check the return type
      checkType(tempObj, JSONObject.class);

      return (JSONObject) tempObj;
         }

   /**
    * {@inheritDoc}
    */
   @Override
   public JSONArray getUserAdmin(final AlfrescoRestHelper pConnector) throws AlfrescoRestException
   {
      JSONArray jsonArray;
      try
      {
         Map<String, String> parameters = new HashMap<String, String>();
         parameters.put("authorityType", "USER");
         Object tempObj = pConnector.getJSON("api/groups/ALFRESCO_ADMINISTRATORS/children", parameters);
         // Check the return type
         checkType(tempObj, JSONObject.class);
         jsonArray = ((JSONObject) tempObj).getJSONArray("data");
      }
      catch (AlfrescoRestConnectionException e)
      {

         throw new AlfrescoRestException("Cannot get administrators user ", e);
      }
      catch (Exception e)
      {
         throw new AlfrescoRestException("Unable to build JSONObject for gettingUserAdmin method", e);
      }
      return jsonArray;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getSiteId(AlfrescoRestHelper pConnector, String pSiteName) throws AlfrescoRestException
   {

      String storeId = null;
      try
      {
         try
         {
            JSONObject project = new JSONObject();
            project.put("shortName", pSiteName);
            Map<String, String> parameters = new HashMap<String, String>();
            Object tempObj = pConnector.getJSON("api/sites/" + pSiteName,
                  parameters);

            if (tempObj != null)
            {
               JSONObject myJson = (JSONObject) tempObj;

               if (myJson != null)
               {
                  String myUrlNode = (String) myJson.get("node");

                  int spaceStorePos = myUrlNode.indexOf("SpacesStore");
                  String spaceStoreString = myUrlNode.substring(spaceStorePos + 1);
                  int separatorPos = spaceStoreString.indexOf("/");
                  storeId = spaceStoreString.substring(separatorPos + 1);
               }

            }

         }
         catch (AlfrescoRestConnectionException e)
         {

            throw new AlfrescoRestException(String.format("Cannot get project site id with [name=%s]",
                  pSiteName), e);
         }
      }
      catch (JSONException e)
      {
         throw new AlfrescoRestException("Unable to build JSONObject for deleteSiteProject method", e);
      }

      return storeId;
   }

   /**
    * Used to check type of alfresco response object.
    *
    * @param obj
    *     represents the object to check
    * @param expectedType
    *     represents the instance class expected
    *
    * @throws AlfrescoRestException
    *     occured if the object is not a instance of class expected
    */
   private void checkType(final Object obj, final Class<?> expectedType) throws AlfrescoRestException
   {
      if (obj != null)
      {
         if (!expectedType.isInstance(obj))
         {
            throw new AlfrescoRestException("Response from server returned an unexpected object.  Expected: "
                                                + expectedType + ", actual: " + obj.getClass());
         }
      }
   }

}
