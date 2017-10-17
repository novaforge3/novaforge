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
package org.novaforge.forge.plugins.testmanager.testlink.internal.client;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.common.XmlRpcHttpRequestConfig;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcClient;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcException;
import org.novaforge.forge.plugins.testmanager.testlink.contants.TestlinkRolePrivilege;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.RequirementDataMapper;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestCaseDatamapper;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkParameter;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkRPCStatus;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestlinkMethod;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Mohamed IBN EL AZZOUZI, Trolat Florent, Lamirand Guillaume
 * @date 26 juil. 2011
 */

/**
 * @author trolatf
 */
public class TestlinkXmlRpcClientImpl implements TestlinkXmlRpcClient
{

	// TODO rewrite client side to avoid this constant
	private static String	SUCCESS	= "uccess";
	private static String	STATUS	= "status";
	private static String	STATUSM	= "STATUS";
	private static String	MESSAGE	= "message";
	private static String	MESSAGEM	= "MESSAGE";
	private String adminRole;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> createUser(final XmlRpcClient pConnector, final Map<String, Object> pAccountData)
			throws TestlinkXmlRpcException
	{
		return this.executeRPC(pConnector, pAccountData, TestlinkMethod.CREATE_USER.toString());
	}

	/**
	 * cf private <T> TestLinkRPCStatus<T> executeRPC(final HashMap<String, Object> Data, String rpcMethod,
	 * ResultExtractor<T> statusExtractor) throws TestlinkXmlRpcException
	 */
	private TestLinkRPCStatus<Object> executeRPC(final XmlRpcClient connector,
			final Map<String, Object> pData, final String pRpcMethod) throws TestlinkXmlRpcException
	{
		return this.executeRPC(connector, pData, pRpcMethod, new ResultExtractor<Object>());
	}

	/**
	 * @param pData
	 *           rpc paramters
	 * @param rpcMethod
	 * @return a complexe object, isSuccess() on this object give a status, getMessage() the teslink message
	 *         etc...
	 * @throws TestlinkXmlRpcException
	 */
	private <T> TestLinkRPCStatus<T> executeRPC(final XmlRpcClient pConnector,
			final Map<String, Object> pData, final String rpcMethod, final ResultExtractor<T> statusExtractor)
			throws TestlinkXmlRpcException
	{
		final ArrayList<Object> params = new ArrayList<Object>();
		final Map<String, Object> accountDataWithAdmin = setAdminCredentials(pConnector, pData);
		params.add(accountDataWithAdmin);
		try
		{
			final Object response = pConnector.execute(rpcMethod, params);
			return statusExtractor.extract(response);
		}
		catch (final XmlRpcException e)
		{
			throw new TestlinkXmlRpcException("Error executing :" + rpcMethod, e);
		}
	}

	private Map<String, Object> setAdminCredentials(final XmlRpcClient connector, final Map<String, Object> parameterers)
	{
		final XmlRpcHttpRequestConfig requestConfig = (XmlRpcHttpRequestConfig) connector.getConfig();
		parameterers.put(TestLinkParameter.ADMIN_LOGIN.toString(), requestConfig.getBasicUserName());
		parameterers.put(TestLinkParameter.ADMIN_PASSWORD.toString(), requestConfig.getBasicPassword());
		return parameterers;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> isExistingLogin(final XmlRpcClient pConnector, final String pLogin)
			throws TestlinkXmlRpcException
	{
		final Map<String, Object> executionDataUser = new HashMap<String, Object>();
		executionDataUser.put(TestLinkParameter.USER_NAME.toString(), pLogin);

		return this.executeRPC(pConnector, executionDataUser, TestlinkMethod.GET_USER.toString());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> updateUser(final XmlRpcClient pConnector,
			final Map<String, Object> pAccountData) throws TestlinkXmlRpcException
	{
		return this.executeRPC(pConnector, pAccountData, TestlinkMethod.UPDATE_USER.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<Boolean> deleteUser(final XmlRpcClient pConnector,
			final Map<String, Object> pAccountData) throws TestlinkXmlRpcException
	{
		final ResultExtractor<Boolean> statusExtractor = new ResultExtractor<Boolean>()
		{
			@Override
			protected Boolean extractReturnValue(final Object response, final String msg)
					throws TestlinkXmlRpcException
			{
				// TODO delete this code
				return !msg.contains("t exist");
			}
		};
		return this
				.executeRPC(pConnector, pAccountData, TestlinkMethod.DELETE_USER.toString(), statusExtractor);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<String> createProject(final XmlRpcClient pConnector,
			final Map<String, Object> pProjectData) throws TestlinkXmlRpcException
	{

		// retrieve the new project ID
		final ResultExtractor<String> statusExtractor = new ResultExtractor<String>()
		{
			@Override
			protected TestLinkRPCStatus<String> extract(final Object response) throws TestlinkXmlRpcException
			{
				final String  msg         = TestlinkXmlRpcClientImpl.this.extractRPCMessage(response);
				final boolean success     = msg.contains(SUCCESS);
				String        returnValue = null;
				returnValue = extractReturnValue(response, msg);
				return new TestLinkRPCStatus<String>(success, msg, returnValue);
			}

			@Override
			protected String extractReturnValue(final Object response, final String msg)
					throws TestlinkXmlRpcException
			{
				final Object[] responseArray = (Object[]) response;
				final Map<String, Object> responseMap = (Map<String, Object>) responseArray[0];
				final Object id = responseMap.get(TestLinkParameter.ID.toString());
				if (id != null)
				{
					return id.toString();
				}
				else
				{
					throw new TestlinkXmlRpcException("createProject: connot extract the project id");
				}
			}

		};

		return this.executeRPC(pConnector, pProjectData, TestlinkMethod.CREATE_PROJECT.toString(),
				statusExtractor);

	}

	/**
	 * {@inheritDoc}
	 *
	 * @return
	 */
	@Override
	public TestLinkRPCStatus<?> addUserToProject(final XmlRpcClient pConnector, final String pProjectId,
			final String pLogin, final String pRoleId) throws TestlinkXmlRpcException
	{

		// user
		final ArrayList<Object> usersMapping = new ArrayList<Object>();
		final HashMap<String, Object> userMapping = new HashMap<String, Object>();
		userMapping.put(TestLinkParameter.USER_NAME.toString(), pLogin);
		userMapping.put(TestLinkParameter.ROLE_NAME.toString(), pRoleId);
		usersMapping.add(userMapping);

		// project
		final HashMap<String, Object> executionData = new HashMap<String, Object>();
		executionData.put(TestLinkParameter.PROJECT_ID.toString(), pProjectId);
		executionData.put(TestLinkParameter.USER_MAPPING_ROLE.toString(), usersMapping);

		return this.executeRPC(pConnector, executionData, TestlinkMethod.PROJECT_ADD_USER.toString());

	}

	/**
	 * {@inheritDoc}
	 *
	 * @throws TestlinkXmlRpcException
	 */
	@Override
	public TestLinkRPCStatus<Set<String>> getRoles(final XmlRpcClient pConnector)
			throws TestlinkXmlRpcException
	{

		// retrieve the list of roles available on teslink
		final ResultExtractor<Set<String>> statusExtractor = new ResultExtractor<Set<String>>()
		{
			@Override
			protected Set<String> extractReturnValue(final Object response, final String msg)
					throws TestlinkXmlRpcException
			{
				final Object[] responseArray = (Object[]) response;
				final Set<String> roles = new HashSet<String>();
				Map<String, Object> responseMap;
				for (final Object object : responseArray)
				{
					responseMap = (Map<String, Object>) object;
					final String role = (String) responseMap.get(TestLinkParameter.Name.toString());
					if ((role != null) && (role.length() > 0))
					{
						roles.add(role);
					}

				}
				return roles;

			}

		};
		return this.executeRPC(pConnector, new HashMap<String, Object>(), TestlinkMethod.GET_ROLES.toString(),
				statusExtractor);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @return
	 */
	@Override
	public TestLinkRPCStatus<?> removeUserFromProject(final XmlRpcClient pConnector, final String pProjectId,
																										final String pLogin) throws TestlinkXmlRpcException
	{
		final HashMap<String, Object> executionData = new HashMap<String, Object>();
		executionData.put(TestLinkParameter.PROJECT_ID.toString(), pProjectId);
		executionData.put(TestLinkParameter.USER_NAME.toString(), pLogin);
		return this.executeRPC(pConnector, executionData, TestlinkMethod.PROJECT_REMOVE_USER.toString());

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> createAdminUser(final XmlRpcClient pConnector, final Map<String, Object> pAccountData)
			throws TestlinkXmlRpcException
	{
		pAccountData.put(TestLinkParameter.ADMIN_ROLE.toString(), TestlinkRolePrivilege.ADMINISTRATOR.getName());
		final TestLinkRPCStatus<?> result = isExistingLogin(pConnector,
																												(String) pAccountData.get(TestLinkParameter.USER_NAME
																																											.toString()));
		if (result.isSucces())
		{
			return this.executeRPC(pConnector, pAccountData, TestlinkMethod.UPDATE_USER.toString());
		}
		else
		{
			return this.executeRPC(pConnector, pAccountData, TestlinkMethod.CREATE_USER.toString());
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> updateProject(final XmlRpcClient pConnector, final Map<String, Object> pProjectData,
																						final String pProjectId) throws TestlinkXmlRpcException
	{
		pProjectData.put(TestLinkParameter.PROJECT_ID.toString(), pProjectId);
		return this.executeRPC(pConnector, pProjectData, TestlinkMethod.UPDATE_PROJECT.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> deleteProject(final XmlRpcClient pConnector, final String pProjectId)
			throws TestlinkXmlRpcException
	{
		final Map<String, Object> projectData = new HashMap<String, Object>();
		projectData.put(TestLinkParameter.PROJECT_ID.toString(), pProjectId);
		return this.executeRPC(pConnector, projectData, TestlinkMethod.DELETE_PROJECT.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> updateRequirements(final XmlRpcClient pConnector,
			final Map<String, Object> pRequirementsData, final String pToolProjectId, final String pLogin)
			throws TestlinkXmlRpcException
	{
		pRequirementsData.put(TestLinkParameter.PROJECT_ID.toString(), pToolProjectId);
		pRequirementsData.put(TestLinkParameter.USER_NAME.toString(), pLogin);

		return this.executeRPC(pConnector, pRequirementsData,
				TestlinkMethod.REQUIREMENTS_IMPORT_FROM_XML.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> updateDirectory(final XmlRpcClient pConnector,
			final Map<String, Object> pDirectoryData) throws TestlinkXmlRpcException
	{
		return this.executeRPC(pConnector, pDirectoryData, TestlinkMethod.UPDATE_DIRECTORY.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> updateRequirement(final XmlRpcClient pConnector,
			final Map<String, Object> pRequirementData) throws TestlinkXmlRpcException
	{
		return this.executeRPC(pConnector, pRequirementData, TestlinkMethod.UPDATE_REQUIREMENT.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TestLinkRPCStatus<?> deleteRequirement(final XmlRpcClient pConnector,
			final Map<String, Object> pRequirementData) throws TestlinkXmlRpcException
	{
		return this.executeRPC(pConnector, pRequirementData, TestlinkMethod.DELETE_REQUIREMENT.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public RequirementDataMapper[] getRequirementsCoverageByTest(final XmlRpcClient pConnector,
			final Map<String, Object> pProjectData, final String pToolProjectId) throws TestlinkXmlRpcException
	{
		RequirementDataMapper[] requirements = new RequirementDataMapper[0];
		pProjectData.put(TestLinkParameter.PROJECT_ID.toString(), pToolProjectId);

		final Object requirementsObject = executeRPCReturnObject(pConnector, pProjectData,
				TestlinkMethod.GET_REQUIREMENTS_BY_PROJECT.toString());
		final Object[] requirementsObjectMap = castToArray(requirementsObject);

		if (requirementsObjectMap.length > 0)
		{
			final Map<String, Object> testRequirementReturn = (Map<String, Object>) requirementsObjectMap[0];
			if (testRequirementReturn.get("STATUS") == null)
			{
				requirements = new RequirementDataMapper[requirementsObjectMap.length];
				for (int i = 0; i < requirementsObjectMap.length; i++)
				{
					final Map<String, Object> requirementMap = (Map<String, Object>) requirementsObjectMap[i];
					final RequirementDataMapper requirement = getRequirement(requirementMap);
					requirements[i] = requirement;
				}
				// Get Test in req_coverage
				for (final RequirementDataMapper requirement : requirements)
				{
					final Map<String, Object> requirementData = new HashMap<String, Object>();
					requirementData.put(TestLinkParameter.REQUIREMENT_ID.toString(), requirement.getId());
					requirementData.put(TestLinkParameter.PROJECT_ID.toString(), pToolProjectId);

					final Object testsObject = executeRPCReturnObject(pConnector, requirementData,
							TestlinkMethod.GET_TESTS_BY_REQUIREMENT.toString());
					final Object[] testsObjectMap = castToArray(testsObject);

					if (testsObjectMap.length > 0)
					{
						final Map<String, Object> testReturn = (Map<String, Object>) testsObjectMap[0];
						if (testReturn.get("STATUS") == null)
						{
							for (final Object element : testsObjectMap)
							{
								final Map<String, Object> testCaseMap = (Map<String, Object>) element;
								final TestCaseDatamapper testCase = getTestCase(testCaseMap);
								requirement.addTestCase(testCase);
							}
						}
					}
				}
			}
			else
			{
				System.out.println("STATUS = " + testRequirementReturn.get("STATUS"));
			}
		}
		return requirements;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public XmlRpcClient getConnector(final String pBaseUrl, final String pUsername, final String pPassword)
			throws TestlinkXmlRpcException
	{
		final XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
		try
		{
			config.setServerURL(new URL(pBaseUrl));
		}
		catch (final MalformedURLException e)
		{
			throw new TestlinkXmlRpcException(String.format("Unable to build server url with [baseurl=%s]", pBaseUrl));
		}
		config.setBasicUserName(pUsername);
		config.setBasicPassword(pPassword);
		config.setBasicEncoding(XmlRpcStreamConfig.UTF8_ENCODING);

		final XmlRpcClient xmlRpcClient = new XmlRpcClient();
		xmlRpcClient.setConfig(config);
		return xmlRpcClient;
	}

	/**
	 * @param Data
	 *     rpc paramters
	 * @param rpcMethod
	 *
	 * @return a complexe object, isSuccess() on this object give a status, getMessage() the teslink message
	 * etc...
	 *
	 * @throws TestlinkXmlRpcException
	 */
	private Object executeRPCReturnObject(final XmlRpcClient pConnector, final Map<String, Object> pData,
																				final String rpcMethod) throws TestlinkXmlRpcException
	{
		final ArrayList<Object>   params               = new ArrayList<Object>();
		final Map<String, Object> accountDataWithAdmin = setAdminCredentials(pConnector, pData);
		params.add(accountDataWithAdmin);
		try
		{
			return pConnector.execute(rpcMethod, params);
		}
		catch (final XmlRpcException e)
		{
			throw new TestlinkXmlRpcException("Error executing :" + rpcMethod, e);
		}
	}

	/**
	 * @param object
	 * @return Array of objects
	 */
	private Object[] castToArray(final Object object)
	{
		Object[] array = null;
		if (object != null)
		{
			if (object instanceof String)
			{
				array = new Object[0];
			}
			else
			{
				array = (Object[]) object;
			}
		}
		return array;
	}

	/**
	 * Extracts a Requirement from a Map.
	 *
	 * @param map
	 *           Map with properties of a Test Project.
	 * @return Requirement.
	 */
	private RequirementDataMapper getRequirement(final Map<String, Object> requirementMap)
	{
		RequirementDataMapper requirement = null;
		if ((requirementMap != null) && (requirementMap.size() > 0))
		{
			requirement = new RequirementDataMapper();
			final Integer id = Integer.parseInt(requirementMap.get("id").toString());
			final String name = (requirementMap.get("name").toString());
			final Integer version = Integer.parseInt(requirementMap.get("version").toString());
			final String docId = (requirementMap.get("req_doc_id").toString());
			requirement.setId(id);
			requirement.setReqDocId(docId);
			requirement.setName(name);
			requirement.setVersion(version);
		}

		return requirement;
	}

	/**
	 * Extracts a Test Case from a Map.
	 *
	 * @param map
	 *     Map with properties of a Test Case.
	 *
	 * @return Test Case.
	 */
	private TestCaseDatamapper getTestCase(final Map<String, Object> testCaseMap)
	{
		TestCaseDatamapper testCase = null;
		if ((testCaseMap != null) && (testCaseMap.size() > 0))
		{
			testCase = new TestCaseDatamapper();
			final Integer id = Integer.parseInt(testCaseMap.get("testcase_id").toString());
			final String name = testCaseMap.get("name").toString();
			final Integer externalId = Integer.parseInt(testCaseMap.get("tc_external_id").toString());
			final Integer version = Integer.parseInt(testCaseMap.get("version").toString());
			final String prefix = testCaseMap.get("prefix").toString();
			testCase.setId(id);
			testCase.setName(name);
			testCase.setExternalId(externalId);
			testCase.setVersion(version);
			testCase.setProjectPrefix(prefix);
		}

		return testCase;
	}

	// TODO : change the php status and message keys to have the same keys for all the methods
	private String extractRPCMessage(final Object resultRPC)
	{
		Map<String, Object> responseMap;
		final StringBuilder resultMsg = new StringBuilder("Original message->");
		if (resultRPC instanceof Object[])
		{
			final Object[] responses = (Object[]) resultRPC;
			responseMap = (HashMap<String, Object>) responses[0];

		}
		else if (resultRPC instanceof Map)
		{
			responseMap = (HashMap<String, Object>) resultRPC;
		}
		else
		{
			return resultMsg.toString();
		}

		if (responseMap.containsKey(STATUSM))
		{
			resultMsg.append(responseMap.get(STATUSM)).append(" ");
		}
		if (responseMap.containsKey(STATUS))
		{
			resultMsg.append(responseMap.get(STATUS)).append(" ");
		}
		if (responseMap.containsKey(MESSAGEM))
		{
			resultMsg.append(responseMap.get(MESSAGEM)).append(" ");
		}
		if (responseMap.containsKey(MESSAGE))
		{
			resultMsg.append(responseMap.get(MESSAGE)).append(" ");
		}
		return resultMsg.toString();

	}

	/**
	 * This class is used to provide a way for the exuteRPC method to extract the rpc response result.
	 *
	 * @param <T>
	 *     the return value class
	 *
	 * @author trolatf
	 */
	private class ResultExtractor<T>
	{
		protected TestLinkRPCStatus<T> extract(final Object response) throws TestlinkXmlRpcException
		{
			final String  msg         = extractRPCMessage(response);
			final boolean success     = msg.contains(SUCCESS);
			T             returnValue = null;
			if (success)
			{
				returnValue = this.extractReturnValue(response, msg);
			}
			return new TestLinkRPCStatus<T>(success, msg, returnValue);
		}

		protected T extractReturnValue(final Object response, final String msg) throws TestlinkXmlRpcException
		{
			return null;
		}
	}

}
