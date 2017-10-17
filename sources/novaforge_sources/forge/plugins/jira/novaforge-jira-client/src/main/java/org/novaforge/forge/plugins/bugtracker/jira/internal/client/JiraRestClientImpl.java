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

import com.atlassian.jira.rest.client.api.IssueRestClient.Expandos;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Priority;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.input.ComponentInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.google.common.collect.Lists;
import org.novaforge.forge.plugins.bugtracker.constants.JiraConstant;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestException;
import org.novaforge.forge.plugins.bugtracker.jira.rest.AsynchronousJiraRestClientCustom;
import org.novaforge.forge.plugins.bugtracker.jira.rest.AsynchronousJiraRestClientFactoryCustom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is used in order to instantiate new connector to jira web-service.
 * 
 * @author Gauthier Cart
 */
public class JiraRestClientImpl implements JiraRestClient
{
  /**
   * {@inheritDoc}
   */
  @Override
  public Project getProjectByKey(final JiraRestConnector pConnector, final String pProjectKey)
      throws JiraRestException
  {

    return pConnector.getJiraRestClient().getProjectClient().getProject(pProjectKey).claim();

  }

  /**
   * {@inheritDoc}
   * 
   * @throws URISyntaxException
   */
  @Override
  public Component getComponent(final JiraRestConnector pConnector, final String pComponentId)
      throws JiraRestException, URISyntaxException
  {

    // Initialize variables needed to build component URI
    final StringBuilder componentURIBuilder = new StringBuilder(pConnector.getJiraRestClient().getBaseUri()
        .toString());

    // Test if the jira rest endpoint configuration end with /, if not add it
    if (!componentURIBuilder.toString().endsWith("/"))
    {
      componentURIBuilder.append("/");
    }

    // Build the component URI
    componentURIBuilder.append(JiraConstant.REST_COMPONENT_PATH).append("/").append(pComponentId);

    // Convert it into a java URI
    final URI componentURI = new URI(componentURIBuilder.toString());

    // Return the component
    return pConnector.getJiraRestClient().getComponentClient().getComponent(componentURI).claim();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public JiraRestConnector getConnector(final String pRestUrl, final String pUsername, final String pPassword)
      throws JiraRestException
  {
    JiraRestConnector connector = null;
    try
    {
      final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactoryCustom();
      final URI jiraServerUri = new URI(pRestUrl);
      final AsynchronousJiraRestClientCustom restClient = (AsynchronousJiraRestClientCustom) factory
                                                                                                 .createWithBasicHttpAuthentication(jiraServerUri,
                                                                                                                                    pUsername,
                                                                                                                                    pPassword);
      connector = new JiraRestConnectorImpl(restClient);
    }
    catch (final Exception e)
    {
      throw new JiraRestException(String.format("Unable to get the Jira Connector Binding with [URL=%s]", pRestUrl), e);
    }

    return connector;
  }

  /**
   * {@inheritDoc}
   *
   * @return
   * @throws URISyntaxException
   */
  @Override
  public Component updateComponent(final JiraRestConnector pConnector, final String pComponentId,
                                   final ComponentInput pComponentInput) throws JiraRestException, URISyntaxException
  {

    // Initialize variables needed to build component URI
    final StringBuilder componentURIBuilder = new StringBuilder(pConnector.getJiraRestClient().getBaseUri().toString());

    // Test if the jira rest endpoint configuration end with /, if not add it
    if (!componentURIBuilder.toString().endsWith("/"))
    {
      componentURIBuilder.append("/");
    }

    // Build the component URI
    componentURIBuilder.append(JiraConstant.REST_COMPONENT_PATH).append("/").append(pComponentId);

    // Convert it into a java URI
    final URI componentURI = new URI(componentURIBuilder.toString());

    return pConnector.getJiraRestClient().getComponentClient().updateComponent(componentURI, pComponentInput).claim();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicIssue createIssue(final JiraRestConnector pConnector, final IssueInput pIssue) throws JiraRestException
  {

    return pConnector.getJiraRestClient().getIssueClient().createIssue(pIssue).claim();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Priority> getPriorities(final JiraRestConnector pConnector) throws JiraRestException
  {

    return pConnector.getJiraRestClient().getMetadataClient().getPriorities().claim();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Iterable<Status> getStatuses(final JiraRestConnector pConnector) throws JiraRestException
  {

    return pConnector.getJiraRestClient().getMetadataClient().getStatuses().claim();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Issue getIssue(final JiraRestConnector pConnector, final String issueKey) throws JiraRestException
  {

    final Expandos[]     expandArr = new Expandos[] { Expandos.CHANGELOG };
    final List<Expandos> expand    = Arrays.asList(expandArr);

    return pConnector.getJiraRestClient().getIssueClient().getIssue(issueKey, expand).claim();

  }

  /**
   * {@inheritDoc}
   *
   * @return
   * @throws URISyntaxException
   */
  @Override
  public Iterable<Transition> getTransitions(final JiraRestConnector pConnector, final Issue pIssue)
      throws JiraRestException, URISyntaxException
  {

    return pConnector.getJiraRestClient().getIssueClient().getTransitions(pIssue.getTransitionsUri()).claim();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Issue> searchJqlWithChangeLog(final JiraRestConnector pConnector, final String pJql)
  {

    // Define Issue list to return
    final List<Issue> issues = new ArrayList<>();

    // Expand ChangeLogs to get the Issue status history
    final Expandos[] expandArr = new Expandos[] { Expandos.CHANGELOG };
    final List<Expandos> expand = Arrays.asList(expandArr);

    int startAt = 0;
    Iterable<Issue> searchResults = pConnector.getJiraRestClient().getSearchClientCustom()
        .searchJql(pJql, JiraConstant.REST_API_DEFAULT_MAX_RESULT, startAt, null, expand).claim().getIssues();

    issues.addAll(Lists.newArrayList(searchResults));

    int searchResultsSize = Lists.newArrayList(searchResults).size();

    while (searchResultsSize >= JiraConstant.REST_API_DEFAULT_MAX_RESULT)
    {
      startAt = startAt + JiraConstant.REST_API_DEFAULT_MAX_RESULT;
      searchResults = pConnector.getJiraRestClient().getSearchClientCustom()
          .searchJql(pJql, JiraConstant.REST_API_DEFAULT_MAX_RESULT, startAt, null, expand).claim()
          .getIssues();

      issues.addAll(Lists.newArrayList(searchResults));

      searchResultsSize = Lists.newArrayList(searchResults).size();
    }

    return issues;
  }

  public Iterable<Status> getProjectStatuses(final JiraRestConnector pConnector, final String pProjectKey)
      throws JiraRestException
  {
    return pConnector.getJiraRestClient().getMetadataClient().getStatuses().claim();
  }
}