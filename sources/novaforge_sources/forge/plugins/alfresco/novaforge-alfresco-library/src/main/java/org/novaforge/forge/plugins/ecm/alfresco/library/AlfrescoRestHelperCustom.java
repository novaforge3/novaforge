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
package org.novaforge.forge.plugins.ecm.alfresco.library;

import org.json.JSONException;
import org.json.JSONObject;
import org.novaforge.forge.plugins.ecm.alfresco.rest.AlfrescoRestHelper;
import org.novaforge.forge.plugins.ecm.alfresco.rest.exceptions.AlfrescoRestConnectionException;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.Representation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This helper is used to launcher rest web service. It is based on RestClientHelper from Alfresco original
 * client, expected we have deleted reference to log4j dependency and change AlfrescoResponse to Serializable
 * object for some method.
 * 
 * @see org.restlet.client.rest.RestClientHelper
 * @author cadetr
 */
public class AlfrescoRestHelperCustom implements AlfrescoRestHelper
{
   private final Client            restClient;

   private final ChallengeResponse challenge;

   private final String            baseUrl;
   private final String            username;
   private final String            password;
   private String ticket;

   public AlfrescoRestHelperCustom(final String baseUrl, final String username, final String password)
         throws AlfrescoRestConnectionException
         {
      ChallengeScheme scheme = ChallengeScheme.HTTP_BASIC;
      this.challenge = new ChallengeResponse(scheme, username, password);
      this.baseUrl = baseUrl;
      this.username = username;
      this.password = password;
      Context restContext = new Context();
      this.restClient = new Client(restContext, Protocol.HTTP);
      // assign the alfresco's ticket
      this.setTicket();

         }

   /**
    * {@inheritDoc}
    */
   @Override
   public Object getJSON(String pServiceURI, Map<String, String> args) throws AlfrescoRestConnectionException
   {
      // Check if the alfresco's ticket is still valid
      this.setTicket();

      String url = this.buildUrl(pServiceURI, args);
      return this.sendMessage(Method.GET, url, (JSONObject) null);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Object postJSON(final String pServiceURI, JSONObject pJSON) throws AlfrescoRestConnectionException
   {
      // Check if the alfresco's ticket is still valid
      this.setTicket();
      String url = this.buildUrl(pServiceURI, null);
      return this.sendMessage(Method.POST, url, pJSON);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Object deleteJSON(final String pServiceURI, JSONObject pJSON) throws AlfrescoRestConnectionException
   {
      // Check if the alfresco's ticket is still valid
      this.setTicket();

      String url = this.buildUrl(pServiceURI, null);
      return this.sendMessage(Method.DELETE, url, pJSON);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Object putJSON(final String pServiceURI, JSONObject pJSON) throws AlfrescoRestConnectionException
   {
      // Check if the alfresco's ticket is still valid
      this.setTicket();
      String url = this.buildUrl(pServiceURI, null);
      return this.sendMessage(Method.PUT, url, pJSON);
   }

   private void setTicket() throws AlfrescoRestConnectionException
   {
      if ((ticket == null) || (!checkValidateTicket()))
      {
         String data = "data";
         String ticket = "ticket";
         try
         {
            Map<String, String> args = new HashMap<String, String>();
            args.put("u", this.username);
            args.put("pw", this.password);
            String url = this.buildUrl("api/login.json", args, false);
            JSONObject jsonObj = (JSONObject) this.sendMessage(Method.GET, url, (JSONObject) null);

            this.ticket = jsonObj.getJSONObject("data").getString("ticket");
         }
         catch (JSONException e)
         {
            throw new AlfrescoRestConnectionException(String.format(
                  "Unable to build JSONObject with [key=%s, object=%s]", data, ticket), e);
         }
      }
   }

   private boolean checkValidateTicket() throws AlfrescoRestConnectionException
   {
      // Check if the ticket is still valid
      String url = this.buildUrl("api/login/ticket/" + "ticket" + "/" + ticket, new HashMap<String, String>(), false);

      // get the response
      JsonRepresentation representation = new JsonRepresentation("");
      Response response = this.sendRequest(Method.GET, url, representation);
      return response.getStatus().isSuccess();
   }

   private Object sendMessage(Method method, String url, JSONObject pMessage)
         throws AlfrescoRestConnectionException
         {
      JsonRepresentation representation;
      if (pMessage != null)
      {
         representation = new JsonRepresentation(pMessage);
      }
      else
      {
         representation = new JsonRepresentation("");
      }

      return this.sendMessage(method, url, representation);

         }

   private Object sendMessage(Method method, String url, Representation representation)
         throws AlfrescoRestConnectionException
         {

      // get the response
      Response response = this.sendRequest(method, url, representation);

      // always expect a success
      if (!response.getStatus().isSuccess())
      {
         throw new AlfrescoRestConnectionException(String.format(
               "Server has returned an error with status =%s", response.getStatus()), response.getStatus());
      }

      Object result = null;
      JsonRepresentation responseJson = null;
      try
      {
         responseJson = new JsonRepresentation(response.getEntity().getText());
         if (responseJson != null)
         {
            JSONObject jsonObj = responseJson.toJsonObject();
            if (jsonObj != null && !jsonObj.isNull("error"))
            {
               List<String> errors = new ArrayList<String>();
               JSONObject jsonError = jsonObj.getJSONObject("error");
               errors.add(jsonError.getString("error_detail"));
               errors.add(jsonError.getString("error"));
               throw new AlfrescoRestConnectionException(jsonError.getString("error_message"),
                     response.getStatus(), errors);
            }
            else
            {
               result = responseJson.toJsonObject();
            }
         }
      }
      catch (Exception e)
      {
         try
         {
            if (responseJson != null)
            {
               result = responseJson.toJsonArray();
            }
            else if (!(e instanceof AlfrescoRestConnectionException))
            {
               throw new AlfrescoRestConnectionException("Unable to get entity text from response");
            }
         }
         catch (JSONException e1)
         {
            throw new AlfrescoRestConnectionException("Response is neither a JSON Object or a JSONArray", e1);
         }
      }
      return result;
         }

   /**
    * {@inheritDoc}
    */
   private Response sendRequest(Method method, String url, Representation representation)
   {
      Request request = new Request();
      request.setResourceRef(url);
      request.setMethod(method);

      if (!Method.GET.equals(method) && !Method.DELETE.equals(method))
      {
         request.setEntity(representation);
      }

      request.setChallengeResponse(this.challenge);

      return this.restClient.handle(request);
   }

   private String buildUrl(String service, Map<String, String> args) throws AlfrescoRestConnectionException
   {
      return buildUrl(service, args, true);
   }

   private String buildUrl(String service, Map<String, String> args, boolean pAddTicket)
         throws AlfrescoRestConnectionException
         {
      // build the url
      StringBuilder urlBuffer = new StringBuilder();

            if (!service.startsWith(baseUrl))
      {
         urlBuffer.append(baseUrl);
         // make sure we have a trailing / in the baseurl
         if (!urlBuffer.toString().endsWith("/"))
         {
            urlBuffer.append("/");
         }
         urlBuffer.append(service);
      }
      else
      {
         urlBuffer.append(service);
      }

      try
      {
         urlBuffer.append(buildParamString(args, pAddTicket));
      }
      catch (UnsupportedEncodingException e)
      {
         throw new AlfrescoRestConnectionException(String.format(
               "Unable to build the correct url with [parameter=%s", args));
      }
      return urlBuffer.toString();
         }

   private String buildParamString(Map<String, String> args, boolean pAddTicket)
         throws UnsupportedEncodingException, AlfrescoRestConnectionException
         {
      StringBuffer params = new StringBuffer("?");

      // Add the alfresco's ticket to the args
      if (args == null)
      {
         args = new HashMap<String, String>();
      }
            if (pAddTicket)
      {
         args.put("ticket", ticket);
      }

      Set<Entry<String, String>> entrySet = args.entrySet();
      for (Entry<String, String> entry : entrySet)
      {
         if (entry.getKey() != null && entry.getValue() != null)
         {
            params.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"))
            .append("&");
         }
      }
      return params.toString();
         }

}
