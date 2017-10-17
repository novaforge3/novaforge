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
import org.junit.After;
import org.junit.Before;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcClient;
import org.novaforge.forge.plugins.testmanager.testlink.client.TestlinkXmlRpcException;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkParameter;
import org.novaforge.forge.plugins.testmanager.testlink.datamapper.TestLinkRPCStatus;

import java.util.HashMap;

import static org.junit.Assert.assertTrue;

/**
 * @author Mohamed IBN EL AZZOUZI
 * @date 10 août 2011
 */
public class TestlinkXmlRpcClientImplUserTest
{
	private static final String			SERVER_URL	= "http://localhost/testlink/lib/api/xmlrpc.php";
	private static final String			ADMIN			= "admin";

	private final TestlinkXmlRpcClient	client		= new TestlinkXmlRpcClientImpl();
	private XmlRpcClient						connector;
	private boolean							testlinkProfileActivated;

	/**
	 * Defualt constructor.
	 */
	public TestlinkXmlRpcClientImplUserTest()
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
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception
	{
		if (testlinkProfileActivated)
		{
			deleteUser();
		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#deleteUser(java.util.HashMap)}
	 * .
	 */

	// @Test
	public TestLinkRPCStatus<Boolean> deleteUser() throws TestlinkXmlRpcException
	{
		TestLinkRPCStatus<Boolean> result = null;

		final HashMap<String, Object> accountData = new HashMap<String, Object>();
		accountData.put(TestLinkParameter.USER_NAME.toString(),
										UserHelper.getUser1AccountData().get(TestLinkParameter.USER_NAME.toString()));
		result = client.deleteUser(connector, accountData);
		return result;

	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#createUser(java.util.HashMap)}
	 * .
	 */
	// @Test
	public void testCreateUser() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			try
			{
				TestLinkRPCStatus<?> result = client.createUser(connector, UserHelper.getUser1AccountData());
				assertTrue(result.isSucces());
				result = client.createUser(connector, UserHelper.getUser1AccountData());
				assertTrue(result.getMessage().contains("already"));
			}
			catch (final Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// @Test
	public void testCreateAdminUser() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			TestLinkRPCStatus<?> result = client.createAdminUser(connector, UserHelper.getUser1AccountData());
			assertTrue(result.isSucces());
			result = client.createAdminUser(connector, UserHelper.getUser1AccountData());
			assertTrue(result.getMessage().contains("already"));

		}
	}

	// @Test
	public void testUpdateAdminUser() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			final boolean result = client.createUser(connector, UserHelper.getUser1AccountData()).isSucces();
			assertTrue(result);
			final TestLinkRPCStatus<?> result2 = client.createAdminUser(connector,
					UserHelper.getUser1AccountData());
			assertTrue(result2.isSucces());

		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#updateUser(java.util.HashMap)}
	 * .
	 */
	// @Test
	public void testUpdateUser() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			boolean result = client.createUser(connector, UserHelper.getUser1AccountData()).isSucces();
			assertTrue(result);

			result = client.updateUser(connector, UserHelper.getUser1UpdateData()).isSucces();
			assertTrue(result);

		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#deleteUser(java.util.HashMap)}
	 * .
	 */
	// @Test
	public void testDeleteUser() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			boolean result = client.createUser(connector, UserHelper.getUser1AccountData()).isSucces();
			assertTrue(result);

			final TestLinkRPCStatus<Boolean> linkRPCStatus = deleteUser();
			result = linkRPCStatus.isSucces() && linkRPCStatus.getReturnValue();
			assertTrue(result);
		}
	}

	/**
	 * Test method for
	 * {@link org.novaforge.forge.plugins.testmanager.testlink.internal.client.TestlinkXmlRpcClientImpl#isExistingLogin(java.lang.String)}
	 * .
	 * 
	 * @throws TestlinkXmlRpcException
	 */
	// @Test
	public void testIsExistingLogin() throws TestlinkXmlRpcException
	{
		if (testlinkProfileActivated)
		{
			boolean result = client.createUser(connector, UserHelper.getUser1AccountData()).isSucces();
			result = client.isExistingLogin(connector, "login1").isSucces();
			assertTrue(result);
		}
	}

}
