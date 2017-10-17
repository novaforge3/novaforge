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
package org.novaforge.forge.plugins.bugtracker.jira.rest;

import com.atlassian.jira.rest.client.api.AuditRestClient;
import com.atlassian.jira.rest.client.api.ComponentRestClient;
import com.atlassian.jira.rest.client.api.IssueRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.MetadataRestClient;
import com.atlassian.jira.rest.client.api.MyPermissionsRestClient;
import com.atlassian.jira.rest.client.api.ProjectRestClient;
import com.atlassian.jira.rest.client.api.ProjectRolesRestClient;
import com.atlassian.jira.rest.client.api.SearchRestClient;
import com.atlassian.jira.rest.client.api.SessionRestClient;
import com.atlassian.jira.rest.client.api.UserRestClient;
import com.atlassian.jira.rest.client.api.VersionRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClient;
import com.atlassian.jira.rest.client.internal.async.DisposableHttpClient;

import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;

public class AsynchronousJiraRestClientCustom implements JiraRestClientCustom
{
  private final JiraRestClient                     jiraRestClient;

  private final AsynchronousSearchRestClientCustom searchRestClient;

  private final DisposableHttpClient               httpClient;

  private final URI                                baseUri;

  public AsynchronousJiraRestClientCustom(final URI serverUri, final DisposableHttpClient httpClient)
  {
    jiraRestClient = new AsynchronousJiraRestClient(serverUri, httpClient);

    baseUri = UriBuilder.fromUri(serverUri).path("/rest/api/latest").build();
    this.httpClient = httpClient;
    searchRestClient = new AsynchronousSearchRestClientCustom(baseUri, httpClient);

  }

  @Override
  public IssueRestClient getIssueClient()
  {
    return jiraRestClient.getIssueClient();
  }

  @Override
  public SessionRestClient getSessionClient()
  {
    return jiraRestClient.getSessionClient();
  }

  @Override
  public UserRestClient getUserClient()
  {
    return jiraRestClient.getUserClient();
  }

  @Override
  public ProjectRestClient getProjectClient()
  {
    return jiraRestClient.getProjectClient();
  }

  @Override
  public ComponentRestClient getComponentClient()
  {
    return jiraRestClient.getComponentClient();
  }

  @Override
  public MetadataRestClient getMetadataClient()
  {
    return jiraRestClient.getMetadataClient();
  }

  @Override
  public SearchRestClient getSearchClient()
  {
    return searchRestClient;
  }

  @Override
  public VersionRestClient getVersionRestClient()
  {
    return jiraRestClient.getVersionRestClient();
  }

  @Override
  public ProjectRolesRestClient getProjectRolesRestClient()
  {
    return jiraRestClient.getProjectRolesRestClient();
  }

  @Override
  public AuditRestClient getAuditRestClient()
  {
    return jiraRestClient.getAuditRestClient();
  }

  @Override
  public MyPermissionsRestClient getMyPermissionsRestClient()
  {
    return jiraRestClient.getMyPermissionsRestClient();
  }

  @Override
  public void close() throws IOException
  {
    jiraRestClient.close();
    try
    {
      httpClient.destroy();
    }
    catch (final Exception e)
    {
      throw (e instanceof IOException) ? ((IOException) e) : new IOException(e);
    }
  }

  @Override
  public AsynchronousSearchRestClientCustom getSearchClientCustom()
  {
    return searchRestClient;
  }

  @Override
  public URI getBaseUri()
  {
    return baseUri;
  }
}
