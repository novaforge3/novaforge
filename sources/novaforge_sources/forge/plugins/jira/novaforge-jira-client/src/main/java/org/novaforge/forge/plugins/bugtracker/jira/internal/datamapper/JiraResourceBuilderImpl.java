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
package org.novaforge.forge.plugins.bugtracker.jira.internal.datamapper;

import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.IssueFieldId;
import com.atlassian.jira.rest.client.api.domain.Priority;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.Version;
import com.atlassian.jira.rest.client.api.domain.input.FieldInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.atlassian.jira.rest.client.api.domain.input.IssueInputBuilder;
import org.apache.commons.lang.StringEscapeUtils;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.domain.plugin.PluginProject;
import org.novaforge.forge.core.plugins.domain.plugin.PluginUser;
import org.novaforge.forge.plugins.bugtracker.constants.JiraConstant;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;
import org.novaforge.forge.plugins.bugtracker.jira.datamapper.JiraResourceBuilder;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteProject;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteUser;
import org.novaforge.forge.plugins.categories.beans.BugTrackerIssueBeanImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used in order to build object used by Jira WS.
 * 
 * @author Gauthier Cart
 */
public class JiraResourceBuilderImpl implements JiraResourceBuilder
{

  private JiraSoapClient jiraSoapClient;

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteUser buildRemoteUser(final PluginUser pPluginUser)
  {
    final RemoteUser remoteUser = new RemoteUser();

    remoteUser.setEmail(pPluginUser.getEmail());
    remoteUser.setFullname(pPluginUser.getName() + JiraConstant.SPACE_SEPARATOR + pPluginUser.getFirstName());
    remoteUser.setName(pPluginUser.getLogin());

    return remoteUser;
  }

  /**
   * {@inheritDoc}
   *
   * @throws JiraSoapException
   */
  @Override
  public RemoteProject buildRemoteProject(final PluginProject pPluginProject, final String pInstanceName,
      final String pKey, final JiraSoapConnector pConnector) throws JiraSoapException
  {
    final RemoteProject remoteProject = new RemoteProject();

    remoteProject.setName(pPluginProject.getName() + JiraConstant.BRACKET_OPEN + pInstanceName
                              + JiraConstant.BRACKET_CLOSE);
    remoteProject.setDescription(pPluginProject.getDescription());
    remoteProject.setIssueSecurityScheme(jiraSoapClient.getSecuritySchemeById(pConnector,
        JiraConstant.SECURITY_SCHEME_ID));
    remoteProject.setKey(pKey);
    remoteProject.setLead(pPluginProject.getAuthor());
    remoteProject.setNotificationScheme(jiraSoapClient.getNotificationSchemeById(pConnector,
        JiraConstant.NOTIFICATION_SCHEME_ID));
    remoteProject.setPermissionScheme(jiraSoapClient.getPermissionSchemeById(pConnector,
        JiraConstant.PERMISSION_SCHEME_ID));

    return remoteProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssueBean buildBugTrackerIssueBean(final Issue pIssue)
  {
    final BugTrackerIssueBean bugBean = new BugTrackerIssueBeanImpl();

    // Set Id
    bugBean.setId(pIssue.getKey());

    // Set title
    bugBean.setTitle(pIssue.getSummary());

    // Set description
    bugBean.setDescription(pIssue.getDescription());

    // Additional information are already into the description
    bugBean.setAdditionalInfo(null);

    // Set the category
    if ((pIssue.getLabels() != null) && (pIssue.getLabels().size() > 0))
    {
      bugBean.setCategory((String) pIssue.getLabels().toArray()[0]);
    }

    // Set reporter if someone is reported to the Issue
    if (pIssue.getReporter() != null && !pIssue.getReporter().getName().isEmpty())
    {
      bugBean.setReporter(pIssue.getReporter().getName());
    }

    // Set severity : field not available yet in Jira
    bugBean.setSeverity(null);

    // Set assignee if someone is assigned to the Issue
    if (pIssue.getAssignee() != null && !pIssue.getAssignee().getName().isEmpty())
    {
      bugBean.setAssignedTo(pIssue.getAssignee().getName());
    }

    // Set priority
    if (pIssue.getPriority() != null)
    {
      bugBean.setPriority(pIssue.getPriority().getName());
    }

    // Set resolution : field not available yet in Jira
    bugBean.setResolution(null);

    // Set status
    bugBean.setStatus(pIssue.getStatus().getName());

    // Set fixed version if exist
    if ((pIssue.getFixVersions() != null) && pIssue.getFixVersions().iterator().hasNext())
    {
      final String versionName = pIssue.getFixVersions().iterator().next().getName();
      bugBean.setFixedInVersion(versionName);
      bugBean.setTargetVersion(versionName);
    }

    // Set product version if exist
    if ((pIssue.getAffectedVersions() != null) && pIssue.getAffectedVersions().iterator().hasNext())
    {
      bugBean.setFixedInVersion(pIssue.getAffectedVersions().iterator().next().getName());
    }

    // Set reproducibility : field not available yet in Jira
    bugBean.setReproducibility(null);

    return bugBean;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IssueInput buildIssue(final BugTrackerIssueBean pBugBean, final String pId, final String pUserName,
      final Project pProject, final Iterable<Priority> pPriorities, final Iterable<Status> pStatuses)
  {

    // Build the issue, set the project and the issue type
    final IssueInputBuilder issueBuilder = new IssueInputBuilder(pId, JiraConstant.BUG_TYPE_ID);

    // Set the summary of the issue with the title of the bug
    issueBuilder.setSummary(pBugBean.getTitle());

    // Build the description by concatenating both bug description and additional information

    // Set the description of the issue
    issueBuilder.setDescription(StringEscapeUtils.unescapeJava(pBugBean.getDescription()) + System
                                                                                                .getProperty("line.separator")
                                    + StringEscapeUtils.unescapeJava(pBugBean.getAdditionalInfo()));

    // Set the label of the issue
    final List<String> labels = new ArrayList<String>();
    labels.add(pBugBean.getCategory().replace(JiraConstant.SPACE, JiraConstant.UNDERSCORE));
    issueBuilder.setFieldInput(new FieldInput(IssueFieldId.LABELS_FIELD, labels));

    // Set the current user as reporter
    issueBuilder.setReporterName(pUserName);

    // Set the project lead as assignee
    issueBuilder.setAssigneeName(pProject.getLead().getName());

    for (final Version version : pProject.getVersions())
    {
      // Test if the bug is fixed in version, if yes, find the name of the version and set it as fixed
      // version, if not find the name of the targeted version and set it as fixed version
      if ((version.getName().equals(pBugBean.getFixedInVersion()))
          || ((pBugBean.getFixedInVersion() == null) && version.getName().equals(pBugBean.getTargetVersion())))
      {
        final List<Version> fixVersion = new ArrayList<Version>();
        fixVersion.add(version);
        issueBuilder.setFixVersions(fixVersion);
      }

      if (version.getName().equals(pBugBean.getProductVersion()))
      {
        final List<Version> affectedVersion = new ArrayList<Version>();
        affectedVersion.add(version);
        issueBuilder.setAffectedVersions(affectedVersion);
      }
    }

    // Set the status
    for (final Status status : pStatuses)
    {
      if (status.getName().equals(pBugBean.getStatus()))
      {
        issueBuilder.setFieldInput(new FieldInput(IssueFieldId.STATUS_FIELD, status));
      }
    }

    // Set the priority
    for (final Priority priority : pPriorities)
    {
      if (priority.getName().equals(pBugBean.getPriority()))
      {
        issueBuilder.setFieldInput(new FieldInput(IssueFieldId.STATUS_FIELD, priority));
      }
    }

    if (pBugBean.getResolution() != null)
    {
      issueBuilder.setFieldInput(new FieldInput(IssueFieldId.RESOLUTION_FIELD, pBugBean.getResolution()));
    }

    // Set the severity : field not available yet in Jira

    // Set the reproducibility : field not available yet in Jira

    return issueBuilder.build();
  }

  /**
   * @param jiraSoapClient
   *          the jiraSoapClient to set
   */
  public void setJiraSoapClient(final JiraSoapClient jiraSoapClient)
  {
    this.jiraSoapClient = jiraSoapClient;
  }
}
