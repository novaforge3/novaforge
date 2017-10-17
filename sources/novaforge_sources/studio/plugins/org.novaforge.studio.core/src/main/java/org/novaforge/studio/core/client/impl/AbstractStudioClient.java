/**
 * NovaForge(TM) is a web-based forge offering a Collaborative Development and 
 * Project Management Environment.
 *
 * Copyright (C) 2007-2012  BULL SAS
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.studio.core.client.impl;

import javax.net.ssl.TrustManager;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.frontend.ClientProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.shiro.crypto.hash.Sha1Hash;
import org.eclipse.mylyn.commons.net.AbstractWebLocation;
import org.eclipse.mylyn.commons.net.AuthenticationType;

/**
 * @author rols-p
 */
public abstract class AbstractStudioClient
{

   @SuppressWarnings("unchecked")
   protected <T> T initRemoteService(AbstractWebLocation location, Class<T> serviceClass, String serviceUri)
   {
      ClientProxyFactoryBean factory = new ClientProxyFactoryBean();
      factory.setServiceClass(serviceClass);
      factory.setAddress(serviceUri);

      T remoteService = (T) factory.create();

      Client proxy = ClientProxy.getClient(remoteService);
      HTTPConduit httpConduit = (HTTPConduit) proxy.getConduit();

      AuthorizationPolicy authPolicy = new AuthorizationPolicy();
      authPolicy.setAuthorization("Authorization");
      authPolicy.setAuthorizationType("BASIC");
      authPolicy.setUserName(location.getCredentials(AuthenticationType.REPOSITORY).getUserName());
      String password = location.getCredentials(AuthenticationType.REPOSITORY).getPassword();
      String encodedShaPass = new Sha1Hash(password).toString();
      authPolicy.setPassword(encodedShaPass);
      httpConduit.setAuthorization(authPolicy);
      
      TLSClientParameters tcp = new TLSClientParameters();
      tcp.setTrustManagers( new TrustManager[]{ new TrustAllX509TrustManager() } );
      httpConduit.setTlsClientParameters( tcp );
      
      HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

      httpConduit.setClient(httpClientPolicy);

      return remoteService;
   }

}
