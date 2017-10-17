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
package org.novaforge.forge.plugins.bugtracker.jira.client;

import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Component;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Priority;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.Transition;
import com.atlassian.jira.rest.client.api.domain.input.ComponentInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;

import java.net.URISyntaxException;
import java.util.List;

/**
 * @author Gauthier Cart
 */
public interface JiraRestClient
{

  /**
   * @param pConnector
   * @param pComponentURI
   * @return
   * @throws JiraRestException
   */
  Project getProjectByKey(JiraRestConnector pConnector, String pProjectKey) throws JiraRestException;

  /**
   * @param pConnector
   * @param pComponentId
   * @return
   * @throws JiraRestException
   * @throws URISyntaxException
   */
  Component getComponent(JiraRestConnector pConnector, String pComponentId) throws JiraRestException,
      URISyntaxException;

  /**
   * @return Use to connect to jira instance with specific url, username and password.
   * @param pRestUrl
   *          represents the url of jira instance REST API
   * @param pUsername
   *          represents username used to log in
   * @param pPassword
   *          represents password used to log in
   * @throws JiraRestException
   *           can occured if jira action failed or client can be built can occured if connection failed
   *           or client can be built
   * @throws
   */
  JiraRestConnector getConnector(String pRestUrl, String pUsername, String pPassword)
      throws JiraRestException;

  /**
   * @param pConnector
   * @param pComponentId
   * @param pComponentInput
   * @return
   * @throws JiraRestException
   * @throws URISyntaxException
   */
  Component updateComponent(JiraRestConnector pConnector, String pComponentId, ComponentInput pComponentInput)
      throws JiraRestException, URISyntaxException;

  /**
   * @param pConnector
   * @param pIssue
   * @return
   * @throws JiraRestException
   */
  BasicIssue createIssue(JiraRestConnector pConnector, IssueInput pIssue) throws JiraRestException;

  /**
   * @param pConnector
   * @return
   * @throws JiraRestException
   */
  Iterable<Priority> getPriorities(JiraRestConnector pConnector) throws JiraRestException;

  /**
   * @param pConnector
   * @return
   * @throws JiraRestException
   */
  Iterable<Status> getStatuses(JiraRestConnector pConnector) throws JiraRestException;

  /**
   * @param pConnector
   * @param issueKey
   * @return
   * @throws JiraRestException
   */
  Issue getIssue(JiraRestConnector pConnector, String issueKey) throws JiraRestException;

  Iterable<Transition> getTransitions(JiraRestConnector pConnector, Issue pIssue) throws JiraRestException,
      URISyntaxException;

  /**
   * @param pConnector
   * @param pJql
   * @return
   * @throws JiraRestException
   */
  List<Issue> searchJqlWithChangeLog(JiraRestConnector pConnector, String pJql);

}
