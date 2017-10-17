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
package org.novaforge.forge.plugins.bugtracker.jira.internal.client;

import org.hamcrest.CoreMatchers;
import org.junit.Test;
import org.novaforge.forge.plugins.bugtracker.jira.soap.JiraSoapServiceServiceLocator;
import org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Gauthier Cart
 */
public class JiraSoapConnectorImplTest
{

  private static final String AUTHENTICATION_TOKEN = "2c584a1d4fc18ad2e66885f971b58ea71da1ced2";
  private static final String ENDPOINT             = "http://vm-infra-8:8080/rpc/soap/jirasoapservice-v2";

  private boolean             jiraProfileActivated = false;

  public JiraSoapConnectorImplTest()
  {
    final String property = System.getProperty("jira.profile");
    if ("true".equals(property))
    {
      jiraProfileActivated = true;
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapConnectorImpl#JiraSoapConnectorImpl(java.lang.String, org.novaforge.forge.plugins.bugtracker.jira.soap.JirasoapserviceV2SoapBindingStub)}
   * .
   * 
   * @throws MalformedURLException
   * @throws ServiceException
   */
  @Test
  public void testJiraSoapConnectorImpl() throws MalformedURLException, ServiceException
  {
    if (jiraProfileActivated)
    {
      final URL url = new URL(ENDPOINT);
      final JirasoapserviceV2SoapBindingStub connectBindingStub = (JirasoapserviceV2SoapBindingStub) new JiraSoapServiceServiceLocator()
          .getJirasoapserviceV2(url);
      final JiraSoapConnectorImpl jiraSoapConnectorImpl = new JiraSoapConnectorImpl(AUTHENTICATION_TOKEN,
          connectBindingStub);
      assertThat(jiraSoapConnectorImpl, notNullValue());
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapConnectorImpl#getAuthenticationToken()}
   * .
   * 
   * @throws MalformedURLException
   * @throws ServiceException
   */
  @Test
  public void testGetAuthenticationToken() throws MalformedURLException, ServiceException
  {
    if (jiraProfileActivated)
    {
      final URL url = new URL(ENDPOINT);
      final JirasoapserviceV2SoapBindingStub connectBindingStub = (JirasoapserviceV2SoapBindingStub) new JiraSoapServiceServiceLocator()
          .getJirasoapserviceV2(url);
      final JiraSoapConnectorImpl jiraSoapConnectorImpl = new JiraSoapConnectorImpl(AUTHENTICATION_TOKEN,
          connectBindingStub);
      assertThat(jiraSoapConnectorImpl.getAuthenticationToken(), notNullValue());
      assertThat(jiraSoapConnectorImpl.getAuthenticationToken(), CoreMatchers.is(AUTHENTICATION_TOKEN));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraSoapConnectorImpl#getConnectBindingStub()}
   * .
   * 
   * @throws MalformedURLException
   * @throws ServiceException
   */
  @Test
  public void testGetConnectBindingStub() throws MalformedURLException, ServiceException
  {
    if (jiraProfileActivated)
    {
      final URL url = new URL(ENDPOINT);
      final JirasoapserviceV2SoapBindingStub connectBindingStub = (JirasoapserviceV2SoapBindingStub) new JiraSoapServiceServiceLocator()
          .getJirasoapserviceV2(url);
      final JiraSoapConnectorImpl jiraSoapConnectorImpl = new JiraSoapConnectorImpl(AUTHENTICATION_TOKEN,
          connectBindingStub);
      assertThat(jiraSoapConnectorImpl.getConnectBindingStub(), notNullValue());
    }
  }

}
