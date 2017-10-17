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
package org.novaforge.forge.plugins.continuousintegration.jenkins.internal.client;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.novaforge.forge.plugins.continuousintegration.jenkins.model.JenkinsClientConnector;

import java.net.URL;

/**
 * @author Guillaume Lamirand
 */
public class JenkinsClientConnectorImpl implements JenkinsClientConnector
{

	/**
	 * HttpClient
	 */
	private final HttpClient httpClient;
	/**
	 * Endpoint used to contact client
	 */
	private final URL        baseUrl;
	/**
	 * Administrator login used to contact client
	 */
	private final String     adminLogin;
	/**
	 * Administrator pwd used to contact client
	 */
	private final String     adminToken;

	/**
	 * @param pURL
	 * @param pAdmin
	 * @param pToken
	 */
	public JenkinsClientConnectorImpl(final URL pURL, final String pAdmin, final String pToken)
	{
		adminLogin = pAdmin;
		adminToken = pToken;
		baseUrl = pURL;
		httpClient = buildHttpClient();
	}

	private HttpClient buildHttpClient()
	{
		final HttpClient httpClient = new HttpClient();

		final HttpState httpState = new HttpState();

		// set the authentication scope in function of the use of proxy
		AuthScope authScope = null;
		if (baseUrl.getPort() != -1)
		{
			authScope = new AuthScope(baseUrl.getHost(), baseUrl.getPort());
		}
		else
		{
			authScope = new AuthScope(baseUrl.getHost(), 80);
		}
		httpState.setCredentials(authScope, new UsernamePasswordCredentials(adminLogin, adminToken));
		httpClient.setState(httpState);
		httpClient.getParams().setAuthenticationPreemptive(true);
		return httpClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public HttpClient getHttpClient()
	{
		return httpClient;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAdminLogin()
	{
		return adminLogin;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getAdminToken()
	{
		return adminToken;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public URL getBaseUrl()
	{
		return baseUrl;
	}

}
