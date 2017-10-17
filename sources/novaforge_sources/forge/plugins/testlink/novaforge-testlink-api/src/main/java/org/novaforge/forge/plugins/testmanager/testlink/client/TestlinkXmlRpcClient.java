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
package org.novaforge.forge.plugins.testmanager.testlink.client;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.RequirementDataMapper;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkRPCStatus;

import java.util.Map;
import java.util.Set;

/**
 * @author Mohamed IBN EL AZZOUZI, Trolat Florent, Lamirand Guillaume
 * @date 26 juil. 2011
 */
public interface TestlinkXmlRpcClient
{

	/**
	 * @param accountData
	 *           see UserHelper for example see UserHelper for example
	 * @return isSuccess()
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<?> createUser(final XmlRpcClient connector, Map<String, Object> accountData)
			throws TestlinkXmlRpcException;

	/**
	 * @param login
	 * @return isSuccess()
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<?> isExistingLogin(final XmlRpcClient connector, String login)
			throws TestlinkXmlRpcException;

	/**
	 * @param accountData
	 *           see UserHelper for example see UserHelper for example
	 * @return isSuccess()
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<?> updateUser(final XmlRpcClient connector, Map<String, Object> accountData)
			throws TestlinkXmlRpcException;

	/**
	 * @param accountData
	 *           see UserHelper for example
	 * @return isSuccess() && returnValue()=user exist or not
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<Boolean> deleteUser(final XmlRpcClient connector, Map<String, Object> accountData)
			throws TestlinkXmlRpcException;

	/**
	 * @param projectData
	 * @return isSuccess() && project technical ID
	 */
	TestLinkRPCStatus<String> createProject(final XmlRpcClient connector, Map<String, Object> projectData)
			throws TestlinkXmlRpcException;

	/**
	 * @param projectId
	 * @param login
	 * @param roleId
	 * @return
	 */
	TestLinkRPCStatus<?> addUserToProject(final XmlRpcClient connector, String projectId, String login, String roleId)
			throws TestlinkXmlRpcException;

	/**
	 * @return the list of roles avalaible on testlink
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<Set<String>> getRoles(final XmlRpcClient connector)
			throws TestlinkXmlRpcException;

	/**
	 * @param projectId
	 * @param UserId
	 * @return
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<?> removeUserFromProject(final XmlRpcClient connector, String projectId, String UserId)
			throws TestlinkXmlRpcException;

	TestLinkRPCStatus<?> createAdminUser(final XmlRpcClient connector, Map<String, Object> accountData)
			throws TestlinkXmlRpcException;

	/**
	 * @param projectData
	 *           see ProjectHelper for example
	 * @return
	 */
	TestLinkRPCStatus<?> updateProject(final XmlRpcClient connector, Map<String, Object> projectData, String projectId)
			throws TestlinkXmlRpcException;

	/**
	 * @param projectData
	 *           see ProjectHelper for example
	 * @return
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<?> deleteProject(final XmlRpcClient connector, String projectId)
			throws TestlinkXmlRpcException;

	/**
	 * @param requirementsData
	 * @param toolProjectId
	 * @param login
	 * @return
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<?> updateRequirements(final XmlRpcClient connector, final Map<String, Object> requirementsData,
																					String toolProjectId, final String login)
			throws TestlinkXmlRpcException;

	/**
	 * @param directoryData
	 * @return
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<?> updateDirectory(final XmlRpcClient connector, final Map<String, Object> directoryData)
			throws TestlinkXmlRpcException;

	/**
	 * @param requirementsData
	 * @return
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<?> updateRequirement(final XmlRpcClient connector, final Map<String, Object> requirementData)
			throws TestlinkXmlRpcException;

	/**
	 * @param requirementsData
	 * @return
	 * @throws TestlinkXmlRpcException
	 */
	TestLinkRPCStatus<?> deleteRequirement(final XmlRpcClient connector, final Map<String, Object> requirementData)
			throws TestlinkXmlRpcException;

	/**
	 * @param projectData
	 * @param toolProjectId
	 * @return list of Requirement object
	 * @throws TestlinkXmlRpcException
	 */
	RequirementDataMapper[] getRequirementsCoverageByTest(final XmlRpcClient connector,
																												final Map<String, Object> projectData,
																												final String toolProjectId) throws TestlinkXmlRpcException;

	/**
	 * @param pBaseUrl
	 * @param pUsername
	 * @param pPassword
	 * @return
	 * @throws DokuwikiClientException
	 * @throws TestlinkXmlRpcException
	 */
	XmlRpcClient getConnector(String pBaseUrl, String pUsername, String pPassword)
			throws TestlinkXmlRpcException;

}
