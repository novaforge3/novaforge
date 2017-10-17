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
package org.novaforge.forge.core.security.cxf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.interceptor.SoapHeaderInterceptor;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Exchange;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.Conduit;
import org.apache.cxf.ws.addressing.EndpointReferenceType;
import org.novaforge.forge.core.security.authentification.AuthentificationService;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * CXF Interceptor that provides HTTP Basic Authentication validation.
 * Based on the concepts outline here:
 * http://chrisdail.com/2008/03/31/apache-cxf-with-http-basic-authentication
 *
 * @author Guillaume Lamirand
 */
public class BasicAuthAuthorizationInterceptor extends SoapHeaderInterceptor
{

  private static final Log LOGGER = LogFactory.getLog(BasicAuthAuthorizationInterceptor.class);

  @Override
  public void handleMessage(final Message message) throws Fault
  {
    // This is set by CXF
    AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);

    // If the policy is not set, the user did not specify credentials
    // A 401 is sent to the client to indicate that authentication is required
    if (policy == null)
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("User attempted to log in with no credentials");
      }
      sendErrorResponse(message, HttpURLConnection.HTTP_UNAUTHORIZED);
      return;
    }

    if (LOGGER.isDebugEnabled())
    {
      LOGGER.debug("Logging in use: " + policy.getUserName());
    }

    // Verify the password
    try
    {
      getAuthentificationService().login(policy.getUserName(), policy.getPassword());
    }
    catch (Exception e)
    {
      LOGGER.warn("Invalid username or password for user: " + policy.getUserName(), e);
      sendErrorResponse(message, HttpURLConnection.HTTP_FORBIDDEN);
    }
  }

  @SuppressWarnings("unchecked")
  private void sendErrorResponse(final Message message, final int responseCode)
  {
    Message outMessage = getOutMessage(message);
    outMessage.put(Message.RESPONSE_CODE, responseCode);

    // Set the response headers
    Map<String, List<String>> responseHeaders = (Map<String, List<String>>) message.get(Message.PROTOCOL_HEADERS);
    if (responseHeaders != null)
    {
      responseHeaders.put("WWW-Authenticate", Collections.singletonList("Basic realm=realm"));
      responseHeaders.put("Content-Length", Collections.singletonList("0"));
    }
    message.getInterceptorChain().abort();
    try
    {
      getConduit(message).prepare(outMessage);
      close(outMessage);
    }
    catch (IOException e)
    {
      LOGGER.warn(e.getMessage(), e);
    }
  }

  private AuthentificationService getAuthentificationService()
  {
    return getService(AuthentificationService.class);
  }

  private Message getOutMessage(final Message inMessage)
  {
    Exchange exchange   = inMessage.getExchange();
    Message  outMessage = exchange.getOutMessage();
    if (outMessage == null)
    {
      Endpoint endpoint = exchange.get(Endpoint.class);
      outMessage = endpoint.getBinding().createMessage();
      exchange.setOutMessage(outMessage);
    }
    outMessage.putAll(inMessage);
    return outMessage;
  }

  private Conduit getConduit(final Message inMessage) throws IOException
  {
    Exchange              exchange = inMessage.getExchange();
    EndpointReferenceType target   = exchange.get(EndpointReferenceType.class);
    Conduit               conduit  = exchange.getDestination().getBackChannel(inMessage, null, target);
    exchange.setConduit(conduit);
    return conduit;
  }

  private void close(final Message outMessage) throws IOException
  {
    OutputStream os = outMessage.getContent(OutputStream.class);
    os.flush();
    os.close();
  }

  /**
   * This method allows to obtain a service implementation available in the
   * OSGi context.
   *
   * @param pClassService
   *     represents the instance of the service you are looking for @
   * @param <T>
   *     represents the class of the service
   *
   * @return implementation of the service
   */
  @SuppressWarnings("unchecked")
  public static <T> T getService(final Class<T> pClassService)
  {
    String canonicalName = pClassService.getCanonicalName();
    T      service;
    try
    {
      InitialContext initialContext = new InitialContext();
      service = (T) initialContext.lookup(String.format("osgi:service/%s", canonicalName));

    }
    catch (NamingException e)
    {
      throw new IllegalArgumentException(String.format("Unable to get OSGi service with [interface=%s]", canonicalName),
                                         e);
    }
    return service;
  }
}