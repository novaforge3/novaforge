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

import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * This class allow any X509 certificates to be used to authenticate the remote side of a secure socket, including
 * self-signed certificates.
 */
public class TrustAllX509TrustManager
    implements X509TrustManager
{

    /** Empty array of certificate authority certificates. */
    private static final X509Certificate[] acceptedIssuers = new X509Certificate[]{ };

    /**
     * Always trust for client SSL chain peer certificate chain with any authType authentication types.
     *
     * @param chain the peer certificate chain.
     * @param authType the authentication type based on the client certificate.
     */
    public void checkClientTrusted( X509Certificate[] chain, String authType )
    {}

    /**
     * Always trust for server SSL chain peer certificate chain with any authType exchange algorithm types.
     *
     * @param chain the peer certificate chain.
     * @param authType the key exchange algorithm used.
     */
    public void checkServerTrusted( X509Certificate[] chain, String authType )
    {}

    /**
     * Return an empty array of certificate authority certificates which are trusted for authenticating peers.
     *
     * @return a empty array of issuer certificates.
     */
    public X509Certificate[] getAcceptedIssuers()
    {
        return ( acceptedIssuers );
    }
}