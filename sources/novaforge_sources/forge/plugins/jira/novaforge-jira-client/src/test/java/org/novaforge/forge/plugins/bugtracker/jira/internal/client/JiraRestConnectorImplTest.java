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

import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import org.junit.Test;
import org.novaforge.forge.plugins.bugtracker.jira.rest.AsynchronousJiraRestClientCustom;
import org.novaforge.forge.plugins.bugtracker.jira.rest.AsynchronousJiraRestClientFactoryCustom;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * @author Gauthier Cart
 */
public class JiraRestConnectorImplTest
{

  private static final String REST_URL             = "http://vm-infra-8:8080/jira/rest/api/latest";
  private static final String USERNAME             = "cart-g";
  private static final String PASSWORD             = "totopouet";

  private boolean             jiraProfileActivated = false;

  public JiraRestConnectorImplTest()
  {
    final String property = System.getProperty("jira.profile");
    if ("true".equals(property))
    {
      jiraProfileActivated = true;
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraRestConnectorImpl#JiraRestConnectorImpl(com.atlassian.jira.rest.client.JiraRestClient)}
   * .
   * 
   * @throws URISyntaxException
   * @throws MalformedURLException
   */
  @Test
  public void testJiraRestConnectorImpl() throws URISyntaxException, MalformedURLException
  {
    if (jiraProfileActivated)
    {
      final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactoryCustom();
      final URI jiraServerURI = new URI(REST_URL);
      final AsynchronousJiraRestClientCustom jiraRestClient = (AsynchronousJiraRestClientCustom) factory
          .createWithBasicHttpAuthentication(jiraServerURI, USERNAME, PASSWORD);
      final JiraRestConnectorImpl jiraRestConnectorImpl = new JiraRestConnectorImpl(jiraRestClient);
      assertThat(jiraRestConnectorImpl, notNullValue());
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraRestConnectorImpl#getJiraRestClient()}
   * .
   * 
   * @throws URISyntaxException
   * @throws MalformedURLException
   */
  @Test
  public void testGetJiraRestClient() throws URISyntaxException, MalformedURLException
  {
    if (jiraProfileActivated)
    {
      final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactoryCustom();
      final URI jiraServerUri = new URI(REST_URL);
      final AsynchronousJiraRestClientCustom jiraRestClient = (AsynchronousJiraRestClientCustom) factory
          .createWithBasicHttpAuthentication(jiraServerUri, USERNAME, PASSWORD);
      final JiraRestConnectorImpl jiraRestConnectorImpl = new JiraRestConnectorImpl(jiraRestClient);
      assertThat(jiraRestConnectorImpl.getJiraRestClient(), notNullValue());
    }
  }

}
