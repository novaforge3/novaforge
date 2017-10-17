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
package org.novaforge.forge.plugins.bugtracker.jira.datamapper;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Priority;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProject;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteUser;

/**
 * This class is used to defined a service which will be user in order to build object needed to communicate
 * with Jira WS.
 * 
 * @author Gauthier Cart
 */
public interface JiraResourceBuilder
{

  /**
   * @param pPluginUser
   * @return
   */
  RemoteUser buildRemoteUser(final PluginUser pPluginUser);

  /**
   * @param pPluginProject
   * @param pInstanceName
   * @param pKey
   * @param pConnector
   * @return
   * @throws JiraSoapException
   */
  RemoteProject buildRemoteProject(PluginProject pPluginProject, String pInstanceName, String pKey,
      JiraSoapConnector pConnector) throws JiraSoapException;

  /**
   * @param pIssue
   * @return
   */
  BugTrackerIssueBean buildBugTrackerIssueBean(Issue pIssue);

  /**
   * @param pBugBean
   * @param pId
   * @param pUserName
   * @param pProject
   * @param pPriorities
   * @param pStatuses
   * @return
   */
  IssueInput buildIssue(BugTrackerIssueBean pBugBean, String pId, String pUserName, Project pProject,
      Iterable<Priority> pPriorities, Iterable<Status> pStatuses);
}
