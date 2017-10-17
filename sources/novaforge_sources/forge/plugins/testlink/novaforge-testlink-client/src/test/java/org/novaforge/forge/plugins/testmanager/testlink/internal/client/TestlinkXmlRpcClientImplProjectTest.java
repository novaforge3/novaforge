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

import org.apache.xmlrpc.client.XmlRpcClient;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcClient;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcException;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.RequirementDataMapper;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkParameter;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkRPCStatus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.assertTrue;

/**
 * @author Mohamed IBN EL AZZOUZI, trolatf
 * @date 11 août 2011
 */

public class TestlinkXmlRpcClientImplProjectTest
{
	private static final String			SERVER_URL	= "http://localhost/testlink/lib/api/xmlrpc.php";
	private static final String			ADMIN			= "admin";
	private final TestlinkXmlRpcClient	client		= new TestlinkXmlRpcClientImpl();
	private boolean testlinkProfileActivated;
	private XmlRpcClient						connector;

	/**
	 * Defualt constructor.
	 */
	public TestlinkXmlRpcClientImplProjectTest()
	{
		final String property = System.getProperty("testlink.profile");
		if ("true".equals(property))
		{
			testlinkProfileActivated = true;
		}
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception
	{

		connector = client.getConnector(SERVER_URL, ADMIN, ADMIN);
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#createProject(java.util.HashMap)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testCreateProject() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			assertTrue(createProjectAndGetId() != null);
		}
	}

	private String createProjectAndGetId() throws TestlinkXmlRpcException
	{
		String returnId = null;

		TestLinkRPCStatus<String> result = client.createProject(connector, ProjectHelper.getProject1Data());

		if (!result.isSucces())
		{
			final boolean resultDelete = client.deleteProject(connector, result.getReturnValue()).isSucces();
			assertTrue(resultDelete);
		}
		final TestLinkRPCStatus<String> result1 = client.createProject(connector,
				ProjectHelper.getProject1Data());
		returnId = result1.getReturnValue();
		assertTrue(result1.isSucces());

		result = client.createProject(connector, ProjectHelper.getProject1Data());
		assertTrue(result.getMessage().contains("already"));

		return returnId;
	}

	@Test
	public void testGetRoles() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			final TestLinkRPCStatus<Set<String>> roles = client.getRoles(connector);
			assertTrue(roles.isSucces());
			assertTrue(roles.getReturnValue().size() > 0);
		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#addUserToProject(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testAddUsersToProject() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			final TestLinkRPCStatus<?> result = client.createUser(connector, UserHelper.getUser1AccountData());
			assertTrue(result.isSucces() || result.getMessage().contains("already"));

			final TestLinkRPCStatus<Set<String>> roles = client.getRoles(connector);
			assertTrue(roles.isSucces());

			final String pId = createProjectAndGetId();
			TestLinkRPCStatus<?> linkRPCStatus = client.addUserToProject(connector, pId, "login1", roles
					.getReturnValue().iterator().next());
			assertTrue(linkRPCStatus.isSucces());

			// add a user already added
			linkRPCStatus = client.addUserToProject(connector, pId, "login1", roles.getReturnValue().iterator()
					.next());
			assertTrue(linkRPCStatus.isSucces());

		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#addUserToProject(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testUpdateUserOnProject() throws TestlinkXmlRpcException
	{

		if (testlinkProfileActivated)
		{
			final TestLinkRPCStatus<?> result = client.createUser(connector, UserHelper.getUser1AccountData());
			assertTrue(result.isSucces() || result.getMessage().contains("already"));

			TestLinkRPCStatus<Set<String>> roles = client.getRoles(connector);
			assertTrue(roles.isSucces());
			final String pId = createProjectAndGetId();

			TestLinkRPCStatus<?> linkRPCStatus = client.addUserToProject(connector, pId, "login1", roles
					.getReturnValue().iterator().next());
			assertTrue(linkRPCStatus.isSucces());

			// add a user already added
			linkRPCStatus = client.addUserToProject(connector, pId, "login1", roles.getReturnValue().iterator()
					.next());
			assertTrue(linkRPCStatus.isSucces());

			// add a user already added with a different rôle
			roles = client.getRoles(connector);
			assertTrue(roles.isSucces());
			final Iterator<String> iterator = roles.getReturnValue().iterator();
			iterator.next();
			iterator.next();
			linkRPCStatus = client.addUserToProject(connector, pId, "login1", iterator.next());
			assertTrue(linkRPCStatus.isSucces());

		}

	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#addUserToProject(java.lang.String, java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testRemoveUsersFromProject() throws TestlinkXmlRpcException
	{

		if (testlinkProfileActivated)
		{

			// Create a user
			final TestLinkRPCStatus<?> result = client.createUser(connector, UserHelper.getUser1AccountData());
			assertTrue(result.isSucces() || result.getMessage().contains("already"));

			final TestLinkRPCStatus<Set<String>> roles = client.getRoles(connector);
			assertTrue(roles.isSucces());
			final String pId = createProjectAndGetId();
			// add a user to project
			TestLinkRPCStatus<?> linkRPCStatus = client.addUserToProject(connector, pId, "login1", roles
					.getReturnValue().iterator().next());
			assertTrue(linkRPCStatus.isSucces());

			// remove this user
			linkRPCStatus = client.removeUserFromProject(connector, pId, "login1");
			assertTrue(linkRPCStatus.isSucces());

			// remove this user twice
			linkRPCStatus = client.removeUserFromProject(connector, pId, "login1");
			assertTrue(linkRPCStatus.getMessage().contains("No role"));

		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#updateProject(java.util.HashMap)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testUpdateProject() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			final String pId = createProjectAndGetId();

			ProjectHelper.getProject1DataUpdated();
			final TestLinkRPCStatus<?> resultUpdated = client.updateProject(connector,
					ProjectHelper.getProject1DataUpdated(), pId);
			assertTrue(resultUpdated.isSucces());
		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#deleteProject(java.util.HashMap)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testDeleteProject() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			boolean result = client.deleteProject(connector, "###").isSucces();
			assertTrue(result);

			// the project already exist
			result = client.deleteProject(connector, "###").isSucces();
			assertTrue(!result);
		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#testUpdateRequirements(java.util.HashMap)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testUpdateRequirements() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			final HashMap<String, Object> params = new HashMap<String, Object>();

			final String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
														 + "<requirement-specification><req_spec title=\"Les Exigences Produit\"  doc_id=\"BR\" >"
														 + "<type><![CDATA[1]]></type><node_order><![CDATA[0]]></node_order>"
														 + "<total_req><![CDATA[0]]></total_req><scope><![CDATA[<p>Les exigences produit pour la release 1.8.0.</p>"
														 + "<p>Ce chapitre contient les exigences de haut niveau qui seront prise en compte pour la livraison 2.8.0.</p>"
														 + "<p>Le d&eacute;tail de ces fonctionnalit&eacute;es sera fait dans le chapitre &quot;Exigences Fonctionnelles&quot;.</p>"
														 + "<p>&nbsp;</p><p>&nbsp;</p>]]></scope><requirement><docid><![CDATA[REQ-BR-1]]></docid><title>"
														 + "<![CDATA[Gestion des Roles ca marche ? Predefinis NovaForge new version if (testlinkProfileActivated)]]></title>"
														 + "<node_order><![CDATA[0]]></node_order><description>"
														 + "<![CDATA[<p>Update new version Liste de r&ocirc;les pr&eacute;d&eacute;finis qui sont int&eacute;gr&eacute;s &agrave;"
														 + "NovaForge et cr&eacute;&eacute;s <br />&nbsp;&nbsp;&nbsp;&nbsp; lors de l'installation</p>"
														 + "<p>En fichier attach&eacute; la liste des nouveaux r&ocirc;les et le mapping par default pour les outils"
														 + "(Mantis, Svn, TestLink, ...).</p>]]></description><status><![CDATA[R]]></status><type><![CDATA[1]]></type>"
														 + "<expected_coverage><![CDATA[0]]></expected_coverage>"
														 + "<custom_fields><custom_field><name><![CDATA[priority]]></name><value><![CDATA[high]]></value></custom_field>"
														 + "</custom_fields></requirement></req_spec></requirement-specification>";

			params.put(TestLinkParameter.REQUIREMENTS_XML.toString(), xml);
			// 59 = TestLink project id / admin1 = Novaforge user name
			final TestLinkRPCStatus<?> result = client.updateRequirements(connector, params, "130", "admin1");

			assertTrue(result.isSucces());
		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#testGetRequirementsCoverageByTest(java.util.HashMap)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testGetRequirementsCoverageByTest() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			final HashMap<String, Object> params = new HashMap<String, Object>();

			// Put the good TestLink project id to get requirements and testcase
			final RequirementDataMapper[] requirements = client.getRequirementsCoverageByTest(connector, params,
					"1");
			System.out.println("testGetRequirementsCoverageByTest requirements.length : " + requirements.length);
			for (final RequirementDataMapper requirement : requirements)
			{
				System.out.println("Requirement : " + requirement.getName() + " | docId : "
						+ requirement.getReqDocId() + " | version : " + requirement.getVersion());
				final Iterator iterator = requirement.getTestCaseList().iterator();
				while (iterator.hasNext())
				{
					System.out.println("  test : " + iterator.next());
				}

			}
			assertTrue(requirements.length > 0);
		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#testUpdateDirectory(java.util.HashMap)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testUpdateDirectory() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			final HashMap<String, Object> directoryData = new HashMap<String, Object>();

			System.out.println("testUpdateDirectory ");
			directoryData.put(TestLinkParameter.PROJECT_ID.toString(), "1");
			directoryData.put(TestLinkParameter.USER_NAME.toString(), "admin");
			directoryData.put(TestLinkParameter.DIRECTORY_NAME.toString(), "testRequirement");
			directoryData.put(TestLinkParameter.DIRECTORY_DESCRIPTION.toString(), "Titre test directory1");
			directoryData.put(TestLinkParameter.DIRECTORY_SCOPE.toString(), "test scope1");
			directoryData.put(TestLinkParameter.DIRECTORY_PARENT_ID.toString(), "17");

			final TestLinkRPCStatus<?> result = client.updateDirectory(connector, directoryData);
			System.out.println(result.getMessage());
			System.out.println(result.isSucces());

			assertTrue(result.isSucces());
		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#testUpdateRequirement(java.util.HashMap)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testUpdateRequirement() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			final HashMap<String, Object> requirementData = new HashMap<String, Object>();

			System.out.println("testUpdateRequirement ");

			requirementData.put(TestLinkParameter.PROJECT_ID.toString(), "1");
			requirementData.put(TestLinkParameter.USER_NAME.toString(), "admin");
			requirementData.put(TestLinkParameter.REQUIREMENT_ID.toString(), "REQ1");
			requirementData.put(TestLinkParameter.REQUIREMENT_NAME.toString(), "Name requirement1");
			requirementData.put(TestLinkParameter.REQUIREMENT_DESCRIPTION.toString(), "Desc requirement1");
			requirementData.put(TestLinkParameter.REQUIREMENT_VERSION.toString(), "2");
			requirementData.put(TestLinkParameter.REQUIREMENT_PARENT_ID.toString(), "2");

			final TestLinkRPCStatus<?> result = client.updateRequirement(connector, requirementData);
			System.out.println(result.getMessage());
			System.out.println(result.isSucces());

			assertTrue(result.isSucces());
		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#testDeleteRequirement(java.util.HashMap)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	@Test
	public void testDeleteRequirement() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			final HashMap<String, Object> requirementData = new HashMap<String, Object>();

			System.out.println("testDeleteRequirement ");

			requirementData.put(TestLinkParameter.REQUIREMENT_ID.toString(), "REQ1");

			final TestLinkRPCStatus<?> result = client.deleteRequirement(connector, requirementData);
			System.out.println(result.getMessage());
			System.out.println(result.isSucces());

			assertTrue(result.isSucces());
		}
	}
}
