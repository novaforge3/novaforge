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

import junit.framework.TestCase;
import org.novaforge.forge.plugins.continuousintegration.jenkins.client.JenkinsXMLClient;
import org.novaforge.forge.plugins.continuousintegration.jenkins.constant.Constants;
import org.novaforge.forge.plugins.continuousintegration.jenkins.model.JenkinsClientConnector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sbenoist
 */
public class JenkinsXMLClientImplTest extends TestCase
{
	private static final String					JENKINS_USER				= "admin";

	private static final String					JENKINS_PASSWORD			= "novaforge_1";

	private static final String					TEST_JOB_NAME				= "testJob";

	private static final String					TEST_JOB_DESCRIPTION		= "description test job";

	private static final Map<String, String>	MEMBERSHIPS					= new HashMap<String, String>();

	private static final String					JENKINS_ENDPOINT			= "http://localhost:9999/jenkins/";
	private static JenkinsXMLClient				jenkinsClient;
	private static JenkinsClientConnector		connector;
	private boolean jenkinsProfileActivated = false;

	public JenkinsXMLClientImplTest(final String name) throws MalformedURLException
	{
		super(name);
		final String property = System.getProperty("jenkins.profile");
		if ("true".equals(property))
		{
			jenkinsProfileActivated = true;
			oneTimeSetUp();
		}
	}

	public static void oneTimeSetUp() throws MalformedURLException
	{
		connector = new JenkinsClientConnectorImpl(new URL(JENKINS_ENDPOINT), JENKINS_USER, JENKINS_PASSWORD);
		jenkinsClient = new JenkinsXMLClientImpl();

		MEMBERSHIPS.put("member1", Constants.JENKINS_ADMINISTRATOR_ROLE_ID);
	}

	public void testCreateJob() throws Exception
	{
		if (jenkinsProfileActivated)
		{

			jenkinsClient.createJob(connector, TEST_JOB_NAME, TEST_JOB_DESCRIPTION, MEMBERSHIPS);
		}
	}

	public void testAddJobPermissions() throws Exception
	{
		if (jenkinsProfileActivated)
		{
			jenkinsClient.updateJobPermissions(connector, TEST_JOB_NAME, "member2",
					Constants.JENKINS_ADMINISTRATOR_ROLE_ID);
		}
	}

	public void testUpdateJobPermissions() throws Exception
	{
		if (jenkinsProfileActivated)
		{
			jenkinsClient.updateJobPermissions(connector, TEST_JOB_NAME, "member2",
					Constants.JENKINS_VIEWER_ROLE_ID);
		}
	}

	public void testRemoveJobPermissions() throws Exception
	{
		if (jenkinsProfileActivated)
		{
			jenkinsClient.removeJobPermissions(connector, TEST_JOB_NAME, "member2");
		}
	}

	public void testGetAllProjectJob() throws Exception
	{
		if (jenkinsProfileActivated)
		{
			MEMBERSHIPS.put("member2", Constants.JENKINS_VIEWER_ROLE_ID);
			final List<String> jobs = jenkinsClient.getAllProjectJobs(connector, TEST_JOB_NAME.substring(0, 5));
			assertEquals(1, jobs.size());
		}
	}

	/*
	 * public void testDeleteJob() throws Exception { if (jenkinsProfileActivated) {
	 * jenkinsClient.deleteJob(TEST_JOB_NAME); } }
	 */

}
