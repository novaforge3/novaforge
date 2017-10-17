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
package org.novaforge.forge.plugins.devops.novadeploy.client;

import java.util.List;

import org.novaforge.forge.plugins.devops.novadeploy.client.data.AccountState;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.CustomerDescriptor;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.CustomerEnvironment;
import org.novaforge.forge.plugins.devops.novadeploy.client.data.Membership;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.CloudConfigResponse;
import org.novaforge.forge.plugins.devops.novadeploy.client.response.DescriptorResponse;
import org.novaforge.forge.plugins.devops.novadeploy.model.NovadeployUser;

import com.sun.jersey.api.client.Client;

/**	
 * @author dekimpea
 */	


public interface NovadeployRestClient
{

	/**
	 * Gets a Connector to the Novadeploy Application.
	 * 
	 * @param pToolUrl
	 *            Novadeploy url
	 * @param pUsername
	 *            administrator username
	 * @param pPassword
	 *            administrator password
	 * @param pUserAgent
	 *            specify a custom user agent. If null a default value
	 *            will be set
	 * @return a {@link Client } built from the parameter given
	 * @throws NovadeployClientException
	 */
	NovadeployClient getConnector(final String pToolUrl, final String pUsername, final String pPassword) throws NovadeployClientException;

	/**
	 * @param connector
	 * @param pdeployusers
	 * @return
	 * @throws NovadeployClientException
	 */
	boolean deleteUser(NovadeployClient connector, final NovadeployUser pdeployusers) throws NovadeployClientException;

	/**
	 * @param connector
	 * @param pdeployUser
	 * @return
	 * @throws NovadeployClientException
	 */
	boolean updateUser(NovadeployClient connector, final String pUserName, final NovadeployUser pdeployUser)
			throws NovadeployClientException;

	/**
	 * @param connector
	 * @param pdeployusers
	 * @throws NovadeployClientException
	 */
	void createUser(final NovadeployClient connector, final NovadeployUser pdeployusers)
			throws NovadeployClientException;

	/**
	 * 
	 * @param pClient
	 * @param pBaseUrl
	 * @param pProjectId
	 * @throws NovadeployClientException
	 */
	void createAccount(NovadeployClient pClient, String pProjectId) throws NovadeployClientException;

	/**
	 * @param connector
	 * @param pScope
	 * @param pUserName
	 * @return
	 * @throws NovadeployClientException
	 */
	boolean deleteUserPermission(NovadeployClient connector, final String pScope, final String pUserName)
			throws NovadeployClientException;

	/**
	 * @param connector
	 * @param pScope
	 * @param pUserName
	 * @param pPermissionId
	 * @throws NovadeployClientException
	 */
	void setUserPermissionToNameSpace(final NovadeployClient connector, final String pScope, final String pUserName,
			final int pPermissionId) throws NovadeployClientException;

	/**
	 * The method lock the project
	 * 
	 * @param connector
	 * @param pBaseUrl
	 * @param pProjectId
	 * @throws NovadeployClientException
	 */
	void lockAccount(NovadeployClient connector, String pProject) throws NovadeployClientException;

	/**
	 * The method unlock the project
	 * 
	 * @param connector
	 * @param pBaseUrl
	 * @param pProjectId
	 * @throws NovadeployClientException
	 */
	void unlockAccount(NovadeployClient connector, String pProject) throws NovadeployClientException;

	/**
	 * 
	 * @param connector
	 * @param pProject
	 * @return
	 * @throws NovadeployClientException
	 */
	AccountState accountStatus(NovadeployClient connector, String pProject) throws NovadeployClientException;

	/**
	 * The method return the memberships of an account
	 * 
	 * @param connector
	 * @param pBaseUrl
	 * @param pProjectId
	 * @throws NovadeployClientException
	 */
	List<Membership> accountMemberships(NovadeployClient connector, String pProject) throws NovadeployClientException;

	/**
	 * 
	 * @param connector
	 * @param project
	 * @param login
	 * @param cloudname
	 * @param password
	 * @param provider
	 * @return
	 * @throws NovadeployClientException
	 */
	CloudConfigResponse validateCloud(NovadeployClient connector, String project, String login, String cloudname,
			String password, String provider) throws NovadeployClientException;

	/**
	 * 
	 * @param connector
	 * @param project
	 * @param vCloudVDCName
	 * @param vCloudNetWork
	 * @throws NovadeployClientException
	 */
	void configCloud(NovadeployClient connector, String project, String vCloudVDCName, String vCloudNetWork)
			throws NovadeployClientException;

	/**
	 * Return descriptors attached to a project
	 * 
	 * @param connector
	 * @param project
	 * @return
	 * @throws NovadeployClientException
	 */
	CustomerDescriptor[] getDescriptors(NovadeployClient connector, String project) throws NovadeployClientException;

	/**
	 * 
	 * @param connector
	 * @param project
	 * @param descriptorName
	 * @param descriptorContent
	 * @return 
	 * @throws NovadeployClientException
	 */
	void createDescriptor(NovadeployClient connector, String descriptorName, String descriptorContent, String project)
			throws NovadeployClientException;

	/**
	 * Allows to fork a descriptor on Novadeploy
	 * 
	 * @param connector
	 * @param project
	 * @param descriptorName
	 * @param descriptorVersion
	 *            must be != -1
	 * @param descriptorContent
	 * @param forkName
	 * @throws NovadeployClientException
	 */
	void forkDescriptor(NovadeployClient connector, String project, String descriptorName, int descriptorVersion,
			String forkName) throws NovadeployClientException;

	/**
	 * 
	 * @param connector
	 * @param project
	 * @param descriptorName
	 * @param descriptorContent
	 * @throws NovadeployClientException
	 */
	void updateDescriptor(NovadeployClient connector, String descriptorName, String descriptorContent, String project)
			throws NovadeployClientException;

	/**
	 * Validate an unversioned descriptor on NovaDeploy
	 * 
	 * @param connector
	 * @param project
	 * @param descriptorName
	 * @throws NovadeployClientException
	 */
	void validateDescriptor(NovadeployClient connector, String project, String descriptorName)
			throws NovadeployClientException;

	/**
	 * Launch a deployment on NovaDeploy
	 * 
	 * @param connector
	 * @param project
	 * @param descriptorName
	 * @param descriptorVersion
	 * @return timerefid of the environment being deployed
	 * @throws NovadeployClientException
	 */
	long deployEnvironment(NovadeployClient connector, String descriptorName, int descriptorVersion, String project)
			throws NovadeployClientException;

	/**
	 * Fetch log on NovaDeploy about deployment identified by the
	 * timeRefId
	 * 
	 * @param connector
	 * @param timeRefId
	 * @return logs
	 * @throws NovadeployClientException
	 */
	String[] deploymentStatus(NovadeployClient connector, long timeRefId) throws NovadeployClientException;

	/**
	 * Delete an environment already deployed by novadeploy and
	 * identified by its timeRefId
	 * 
	 * @param connector
	 * @param project
	 * @param timeRefId
	 *            identifier of an environment on NovaDeploy
	 * @throws NovadeployClientException
	 */
	void deleteEnvironment(NovadeployClient connector, String project, long timeRefId) throws NovadeployClientException;

	/**
	 * List the environment of a project on NovaDeploy
	 * (deployed,failed,deploying...)
	 * 
	 * @param connector
	 * @param project
	 * @return
	 * @throws NovadeployClientException
	 */
	CustomerEnvironment[] listEnvironments(NovadeployClient connector, String project) throws NovadeployClientException;

	/**
	 * 
	 * @param connector
	 * @param descriptorVersion
	 * @param descriptorName
	 * @return
	 * @throws NovadeployClientException
	 */
	DescriptorResponse getDescriptor(NovadeployClient connector, int descriptorVersion, String descriptorName, String project)
			throws NovadeployClientException;

	/**
	 * 
	 * @param connector
	 * @return
	 * @throws NovadeployClientException
	 */
	CustomerDescriptor[] getDescriptorsTree(NovadeployClient connector, String project)
			throws NovadeployClientException;
	/**
	 * 
	 * @param connector
	 * @param project
	 * @param timeRefId
	 * @return
	 * @throws NovadeployClientException
	 */
	CustomerEnvironment getEnvironment(NovadeployClient connector, String project, long timeRefId)
			throws NovadeployClientException;

}
