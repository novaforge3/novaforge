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
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.novaforge.forge.plugins.continuousintegration.jenkins.client.JenkinsXMLClient;
import org.novaforge.forge.plugins.continuousintegration.jenkins.client.JenkinsXMLException;
import org.novaforge.forge.plugins.continuousintegration.jenkins.model.JenkinsClientConnector;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public class JenkinsXMLClientImpl implements JenkinsXMLClient
{

	private static final String               CREATE_METHOD        = "createItem";

	private static final String               CREATE_PARAM         = "name";

	private static final Log                  log                  = LogFactory
	                                                                   .getLog(JenkinsXMLClientImpl.class);

	private static final Object               DELETE_METHOD        = "doDelete";

  private static final Object               BUILD_METHOD         = "build";

	private static final String               API_TOKEN            = "api";

	private static final String               XML_TOKEN            = "xml";

	private static final Object               JOB_TOKEN            = "job";

	private static final Object               USER_TOKEN           = "user";

	private static final Object               CONFIG_TOKEN         = "config.xml";

	private final JenkinsConfigurationHandler configurationHandler = new JenkinsConfigurationHandler();

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deleteJob(final JenkinsClientConnector pConnector, final String pJobName) throws JenkinsXMLException
	{
		final String method = pConnector.getBaseUrl().toExternalForm() + JOB_TOKEN + "/" + pJobName + "/" + DELETE_METHOD;

		final PostMethod postMethod = new PostMethod(method);
		return executePostMethod(pConnector.getHttpClient(), postMethod);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean createJob(final JenkinsClientConnector pConnector, final String pName, final String pDescription,
													 final Map<String, String> pMemberships) throws JenkinsXMLException
	{
		// build the http method
		final String method = pConnector.getBaseUrl().toExternalForm() + CREATE_METHOD + "?" + CREATE_PARAM + "=" + pName;
		final PostMethod postMethod = new PostMethod(method);

		// build the config
		try
		{
			final String input = configurationHandler.buildConfig(pDescription, pMemberships);
			RequestEntity entity;
			entity = new StringRequestEntity(input, "text/xml", "UTF-8");
			postMethod.setRequestEntity(entity);
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new JenkinsXMLException(String.format("Unable to encode XML stream for creating job with name=%s", pName), e);
		}
		catch (final DocumentException e)
		{
			throw new JenkinsXMLException(String.format("Unable to build XML document for creating job with name=%s", pName),
																		e);
		}

		return executePostMethod(pConnector.getHttpClient(), postMethod);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean updateJobPermissions(final JenkinsClientConnector pConnector, final String pJobName,
																			final String pUserName, final String pRole) throws JenkinsXMLException
	{
		return updateJob(pConnector, JenkinsConfigurationHandler.Action.UPDATE_PERMISSIONS, pJobName, pUserName, pRole);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addJobPermissions(final JenkinsClientConnector pConnector, final String pJobName,
																	 final String pUserName, final String pRole) throws JenkinsXMLException
	{
		return updateJob(pConnector, JenkinsConfigurationHandler.Action.ADD_PERMISSIONS, pJobName, pUserName, pRole);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeJobPermissions(final JenkinsClientConnector pConnector, final String pJobName,
																			final String pUserName) throws JenkinsXMLException
	{
		return updateJob(pConnector, JenkinsConfigurationHandler.Action.DELETE_PERMISSIONS, pJobName, pUserName, null);
	}

	@Override
	public boolean removeJenkinsUser(final JenkinsClientConnector pConnector, final String pUserName) throws JenkinsXMLException
	{
		/**
		 * Jenkins does not have special API to delete user A simple solution is to use Rest service
		 * http://<host>/jenkins/user/<username>/doDelete to remove data on File system
		 */
		PostMethod postMethod    = null;
		boolean    returnedValue = false;
		try
		{
			final String method =
					pConnector.getBaseUrl().toExternalForm() + USER_TOKEN + File.separatorChar + pUserName + File.separatorChar
							+ DELETE_METHOD;
			postMethod = new PostMethod(method);
			returnedValue = executePostMethod(pConnector.getHttpClient(), postMethod);
			return returnedValue;
		}
		catch (final Exception e)
		{
			throw new JenkinsXMLException("The system can't remove the user" + pUserName + " on Jenkins ", e);
		}
		finally
		{
			if (postMethod != null)
			{
				postMethod.releaseConnection();
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getAllProjectJobs(final JenkinsClientConnector pConnector, final String pToken) throws JenkinsXMLException
	{
		return getJobs(pConnector, pToken);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getAllJobs(final JenkinsClientConnector pConnector) throws JenkinsXMLException
	{
		return getJobs(pConnector, null);
	}

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean buildJob(final JenkinsClientConnector pConnector, final String pJobName)
      throws JenkinsXMLException
  {
		final String method = pConnector.getBaseUrl().toExternalForm() + JOB_TOKEN + "/" + pJobName + "/" + BUILD_METHOD;

    final PostMethod postMethod = new PostMethod(method);
    return executePostMethod(pConnector.getHttpClient(), postMethod);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public JenkinsClientConnector getConnector(final String pBaseUrl, final String pAdminLogin, final String pAdminPwd)
			throws JenkinsXMLException
	{
		try
		{
			return new JenkinsClientConnectorImpl(new URL(pBaseUrl), pAdminLogin, pAdminPwd);
		}
		catch (final MalformedURLException e)
		{
			throw new JenkinsXMLException(String.format("Unable to build the base url with (url=%s]", pBaseUrl), e);
		}
	}

	private List<String> getJobs(final JenkinsClientConnector pConnector, final String pToken) throws JenkinsXMLException
	{
		// build the http method
		final String method = pConnector.getBaseUrl().toExternalForm() + API_TOKEN + "/" + XML_TOKEN;

		// Get the global config by http GET request
		final GetMethod getMethod = new GetMethod(method);

		try
		{
			final InputStream input = executeGetMethod(pConnector.getHttpClient(), getMethod);
			return configurationHandler.getAllJobsByToken(input, pToken);
		}
		catch (final DocumentException e)
		{
			throw new JenkinsXMLException(String.format("Unable to parse XML stream for getting jobs with token=%s", pToken), e);
		}
		finally
		{
			// the inputStream is closed when the connextion is released
			getMethod.releaseConnection();
		}
	}

	private boolean updateJob(final JenkinsClientConnector pConnector, final JenkinsConfigurationHandler.Action pAction,
														final String pName, final String pUserName, final String pRole) throws JenkinsXMLException
	{
		// build the http method
		final String method = pConnector.getBaseUrl().toExternalForm() + JOB_TOKEN + "/" + pName + "/" + CONFIG_TOKEN;

		// get the initial job config before update
		final GetMethod   getMethod = new GetMethod(method);
		final InputStream config    = executeGetMethod(pConnector.getHttpClient(), getMethod);

		// update the config
		final PostMethod postMethod = new PostMethod(method);
		try
		{
			final String input = configurationHandler.updateConfig(pAction, config, pUserName, pRole);
			RequestEntity entity;
			entity = new StringRequestEntity(input, "text/xml", "UTF-8");
			postMethod.setRequestEntity(entity);
		}
		catch (final DocumentException e)
		{
			throw new JenkinsXMLException(String.format("Unable to encode XML stream for updating job with name=%s", pName),
																		e);
		}
		catch (final UnsupportedEncodingException e)
		{
			throw new JenkinsXMLException(String.format("Unable to build XML document for updating job with name=%s", pName),
																		e);
		}
		finally
		{
			// the inputStream is closed when the connextion is released
			getMethod.releaseConnection();
		}

		return executePostMethod(pConnector.getHttpClient(), postMethod);
	}

	private InputStream executeGetMethod(final HttpClient pHttpClient, final GetMethod pMethod) throws JenkinsXMLException
	{
		if (log.isDebugEnabled())
		{
			log.debug(String.format("path=%s", pMethod.getPath()));
		}
		pMethod.setDoAuthentication(true);
		try
		{
			pHttpClient.executeMethod(pMethod);
			if (log.isDebugEnabled())
			{
				log.debug(String.format("Jenkins Response Message = %s", pMethod.getResponseBodyAsString()));
				log.debug(String.format("Jenkins Response Code Status = %s", pMethod.getStatusCode()));
			}

			final boolean result = checkResult(pMethod.getStatusCode());
			if (!result)
			{
				throw new JenkinsXMLException(String.format("Unable to get response ok from method to hostname=%s, port=%s",
																										pHttpClient.getHostConfiguration().getHost(),
																										pHttpClient.getHostConfiguration().getPort()));
			}
			return pMethod.getResponseBodyAsStream();
		}
		catch (final Exception e)
		{
			throw new JenkinsXMLException(String.format("Unable to execute get method to hostname=%s, port=%s",
																									pHttpClient.getHostConfiguration().getHost(),
																									pHttpClient.getHostConfiguration().getPort()), e);
		}
	}

	private boolean executePostMethod(final HttpClient pHttpClient, final PostMethod pMethod) throws JenkinsXMLException
	{
		boolean success = false;
		if (log.isDebugEnabled())
		{
			log.debug(String.format("path=%s", pMethod.getPath()));
		}

		pMethod.setDoAuthentication(true);

		try
		{
			pHttpClient.executeMethod(pMethod);
			if (log.isDebugEnabled())
			{
				log.debug(String.format("Jenkins Response Code Status = %s", pMethod.getStatusCode()));
			}
			success = checkResult(pMethod.getStatusCode());
		}
		catch (final Exception e)
		{
			throw new JenkinsXMLException(String.format("Unable to post method to hostname=%s, port=%s",
																									pHttpClient.getHostConfiguration().getHost(),
																									pHttpClient.getHostConfiguration().getPort()), e);
		}
		finally
		{
			pMethod.releaseConnection();
		}

		return success;
	}

	private boolean checkResult(final int pCode)
	{
		boolean result = true;
		if (((pCode / 100) == 4) || ((pCode / 100) == 5))
		{
			result = false;
		}
		return result;
	}

}
