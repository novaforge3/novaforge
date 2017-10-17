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

import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueFieldId;
import com.atlassian.jira.rest.client.api.domain.Priority;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.input.ComponentInput;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import org.hamcrest.CoreMatchers;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * @author Gauthier Cart
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class JiraRestClientImplTest
{
  /*
   * private static final String BASE_URL = "http://vm-infra-8:8080/";
   * private static final String ENDPOINT = "http://vm-infra-8:8080/rest/api/latest";
   * private static final String USERNAME = "cart-g";
   * private static final String PASSWORD = "totopouet";
   */

  private static final String REST_URL             = "http://gauthier-Precision-T1650:2990/jira/rest/api/latest";
  private static final String USERNAME             = "admin";
  private static final String PASSWORD             = "admin";

  private static final String COMPONENT_ID         = "10000";
  private static final String COMPONENT_NAME       = "UI";
  private static final String PROJECT_KEY          = "MONPRJIRA";
  private static final String PROJECT_NAME         = "Mon projet admin (jira)";

  private static final String ISSUE_DESCRIPTION    = "DESCRIPTION ISSUE DE TEST";
  private static final String ISSUE_SUMMARY        = "SUMMARY ISSUE DE TEST";
  private static final String ISSUE_LABEL          = "ISSUE LABEL";
  private static final Long   ISSUE_TYPE           = (long) 3;

  private boolean             jiraProfileActivated = false;

  public JiraRestClientImplTest()
  {
    final String property = System.getProperty("jira.profile");
    if ("true".equals(property))
    {
      jiraProfileActivated = true;
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraRestCustomClientImpl#getConnector(java.lang.String, java.lang.String, java.lang.String)}
   * .
   * 
   * @throws JiraRestException
   */
  @Test
  public void test00GetConnector() throws JiraRestException
  {
    if (jiraProfileActivated)
    {
      final JiraRestClient jiraRestClient = new JiraRestClientImpl();
      final JiraRestConnector connector = jiraRestClient.getConnector(REST_URL, USERNAME, PASSWORD);
      assertThat(connector, notNullValue());
      assertThat(connector.getJiraRestClient(), notNullValue());
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraRestCustomClientImpl#getComponent(org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestConnector, java.net.URI)}
   * .
   * 
   * @throws JiraRestException
   * @throws URISyntaxException
   */
  @Test
  public void test01GetComponent() throws JiraRestException, URISyntaxException
  {
    if (jiraProfileActivated)
    {
      final JiraRestClient jiraRestClient = new JiraRestClientImpl();
      final JiraRestConnector connector = jiraRestClient.getConnector(REST_URL, USERNAME, PASSWORD);

      final Component component = jiraRestClient.getComponent(connector, COMPONENT_ID);

      assertThat(component, notNullValue());
      assertThat(component.getName(), CoreMatchers.is(COMPONENT_NAME));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraRestCustomClientImpl#getProjectByKey(org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestConnector, String)}
   * .
   * 
   * @throws JiraRestException
   */
  @Test
  public void test02GetProjectByKey() throws JiraRestException
  {
    if (jiraProfileActivated)
    {
      final JiraRestClient jiraRestClient = new JiraRestClientImpl();
      final JiraRestConnector connector = jiraRestClient.getConnector(REST_URL, USERNAME, PASSWORD);

      final Project project = jiraRestClient.getProjectByKey(connector, PROJECT_KEY);

      assertThat(project, notNullValue());
      assertThat(project.getName(), CoreMatchers.is(PROJECT_NAME));
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.plugins.bugtracker.jira.internal.client.JiraRestCustomClientImpl#getProjectByKey(org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestConnector, String)}
   * .
   * 
   * @throws JiraRestException
   * @throws URISyntaxException
   */
  @Test
  public void test03UpdateComponent() throws JiraRestException, URISyntaxException
  {
    if (jiraProfileActivated)
    {
      final JiraRestClient jiraRestClient = new JiraRestClientImpl();
      final JiraRestConnector connector = jiraRestClient.getConnector(REST_URL, USERNAME, PASSWORD);

      Component component = jiraRestClient.getComponent(connector, COMPONENT_ID);

      final ComponentInput componentInput = new ComponentInput(component.getName(),
          component.getDescription(), USERNAME, component.getAssigneeInfo().getAssigneeType());

      component = jiraRestClient.updateComponent(connector, COMPONENT_ID, componentInput);

      assertThat(component, notNullValue());
      assertThat(component.getLead().getName(), CoreMatchers.is(USERNAME));
    }
  }

  /**
   * Test method for
   * 
   * @throws JiraRestException
   */
  @Test
  public void test04CreateIssue() throws JiraRestException
  {
    if (jiraProfileActivated)
    {

      // Build the connector
      final JiraRestClient jiraRestClient = new JiraRestClientImpl();
      final JiraRestConnector connector = jiraRestClient.getConnector(REST_URL, USERNAME, PASSWORD);

      // Build the issue
      final IssueInputBuilder issueBuilder = new IssueInputBuilder(PROJECT_KEY, ISSUE_TYPE);
      issueBuilder.setDescription(ISSUE_DESCRIPTION);
      issueBuilder.setSummary(ISSUE_SUMMARY);
      issueBuilder.setAssigneeName(USERNAME);
      issueBuilder.setReporterName(USERNAME);
      final List<String> labels = new ArrayList<String>();
      labels.add(ISSUE_LABEL.replace(" ", "_"));
      issueBuilder.setFieldInput(new FieldInput(IssueFieldId.LABELS_FIELD, labels));
      final IssueInput inputIssue = issueBuilder.build();

      // Create the issue
      final BasicIssue basicIssue = jiraRestClient.createIssue(connector, inputIssue);

      // Validation
      assertThat(basicIssue, notNullValue());
      assertTrue(basicIssue.getKey().contains(PROJECT_KEY));
    }
  }

  /**
   * Test method for
   * 
   * @throws JiraRestException
   */
  @Test
  public void test05GetIssue() throws JiraRestException
  {
    if (jiraProfileActivated)
    {

      // Build the connector
      final JiraRestClient jiraRestClient = new JiraRestClientImpl();
      final JiraRestConnector connector = jiraRestClient.getConnector(REST_URL, USERNAME, PASSWORD);

      // Build the issue
      final IssueInputBuilder issueBuilder = new IssueInputBuilder(PROJECT_KEY, ISSUE_TYPE);
      issueBuilder.setDescription(ISSUE_DESCRIPTION);
      issueBuilder.setSummary(ISSUE_SUMMARY);
      issueBuilder.setAssigneeName(USERNAME);
      issueBuilder.setReporterName(USERNAME);
      final IssueInput inputIssue = issueBuilder.build();

      // Create the issue
      final BasicIssue basicIssue = jiraRestClient.createIssue(connector, inputIssue);

      final Issue issue = jiraRestClient.getIssue(connector, basicIssue.getKey());

      issue.getTransitionsUri();

      // Validation
      assertThat(issue, notNullValue());
      assertTrue(issue.getKey().contains(PROJECT_KEY));
    }
  }

  /**
   * Test method for
   * 
   * @throws JiraRestException
   */
  @Test
  public void test06GetStatuses() throws JiraRestException
  {
    if (jiraProfileActivated)
    {

      // Build the connector
      final JiraRestClient jiraRestClient = new JiraRestClientImpl();
      final JiraRestConnector connector = jiraRestClient.getConnector(REST_URL, USERNAME, PASSWORD);

      // Get all the statuses
      final Iterable<Status> statuses = jiraRestClient.getStatuses(connector);

      // Validation
      assertThat(statuses, notNullValue());
      for (final Status status : statuses)
      {
        assertThat(status, notNullValue());
      }
    }
  }

  /**
   * Test method for
   * 
   * @throws JiraRestException
   */
  @Test
  public void test07GetPriorities() throws JiraRestException
  {
    if (jiraProfileActivated)
    {

      // Build the connector
      final JiraRestClient jiraRestClient = new JiraRestClientImpl();
      final JiraRestConnector connector = jiraRestClient.getConnector(REST_URL, USERNAME, PASSWORD);

      // Get all the priorities
      final Iterable<Priority> priorities = jiraRestClient.getPriorities(connector);

      // Validation
      assertThat(priorities, notNullValue());
      for (final Priority priority : priorities)
      {
        assertThat(priority, notNullValue());
      }
    }
  }
}
