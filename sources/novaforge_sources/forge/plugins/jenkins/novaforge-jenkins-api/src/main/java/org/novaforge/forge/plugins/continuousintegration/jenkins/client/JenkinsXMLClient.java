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
package org.novaforge.forge.plugins.continuousintegration.jenkins.client;

import org.novaforge.forge.plugins.continuousintegration.jenkins.model.JenkinsClientConnector;

import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 * @author Guillaume Lamirand
 */
public interface JenkinsXMLClient
{

	boolean deleteJob(final JenkinsClientConnector pConnector, final String pJobName)
			throws JenkinsXMLException;

	boolean createJob(final JenkinsClientConnector pConnector, final String pName, final String pDescription,
			final Map<String, String> pMemberships) throws JenkinsXMLException;

	boolean updateJobPermissions(final JenkinsClientConnector pConnector, final String pJobName,
			String pUsername, String pRole) throws JenkinsXMLException;

	boolean addJobPermissions(final JenkinsClientConnector pConnector, final String pJobName,
			String pUsername, String pRole) throws JenkinsXMLException;

	boolean removeJobPermissions(final JenkinsClientConnector pConnector, final String pJobName,
			String pUsername) throws JenkinsXMLException;

	boolean removeJenkinsUser(final JenkinsClientConnector pConnector, final String pUserName)
			throws JenkinsXMLException;

	List<String> getAllProjectJobs(final JenkinsClientConnector pConnector, final String pToken)
			throws JenkinsXMLException;

	List<String> getAllJobs(final JenkinsClientConnector pConnector) throws JenkinsXMLException;

  boolean buildJob(final JenkinsClientConnector pConnector, final String pJobName) throws JenkinsXMLException;

	JenkinsClientConnector getConnector(final String pBaseUrl, final String pAdminLogin, final String pAdminPwd)
			throws JenkinsXMLException;

}
