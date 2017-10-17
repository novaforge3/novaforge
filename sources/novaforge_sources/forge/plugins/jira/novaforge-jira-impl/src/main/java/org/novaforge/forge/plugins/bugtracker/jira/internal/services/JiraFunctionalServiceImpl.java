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
package org.novaforge.forge.plugins.bugtracker.jira.internal.services;

import com.atlassian.jira.rest.client.api.domain.ChangelogGroup;
import com.atlassian.jira.rest.client.api.domain.ChangelogItem;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.Priority;
import com.atlassian.jira.rest.client.api.domain.Project;
import com.atlassian.jira.rest.client.api.domain.Status;
import com.atlassian.jira.rest.client.api.domain.input.IssueInput;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.sf.json.JSONException;
import org.joda.time.DateTime;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerHistoryValueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssuesBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerProjectVersionsBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerServiceException;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerStatusBean;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraRestException;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapClient;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapConnector;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapException;
import org.novaforge.forge.plugins.bugtracker.jira.datamapper.JiraResourceBuilder;
import org.novaforge.forge.plugins.bugtracker.jira.models.impl.JiraIssueStatusHistory;
import org.novaforge.forge.plugins.bugtracker.jira.models.impl.JiraIssuesStatusHistory;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraConfigurationService;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraFunctionalService;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraGetIssuesFilter;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteIssue;
import org.novaforge.forge.plugins.bugtracker.jira.soap.RemoteVersion;
import org.novaforge.forge.plugins.categories.beans.BugTrackerHistoryValueBeanImpl;
import org.novaforge.forge.plugins.categories.beans.BugTrackerIssueBeanImpl;
import org.novaforge.forge.plugins.categories.beans.BugTrackerIssuesBeanImpl;
import org.novaforge.forge.plugins.categories.beans.BugTrackerProjectVersionsBeanImpl;
import org.novaforge.forge.plugins.categories.beans.BugTrackerStatusBeanImpl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gauthier Cart
 */
public class JiraFunctionalServiceImpl implements JiraFunctionalService
{

  /*
   * Define jira status field label
   */
  private final static String     JIRA_STATUS_FIELD   = "status";
  /*
   * Define date format for jira
   */
  private static       DateFormat formatter           = new SimpleDateFormat("yyyy/MM/dd");
  /*
   * Define jira open status id
   */
  private final        Long       JIRA_OPEN_STATUS_ID = 1L;
  /*
   * Define days in seconds
   */
  private final        Long       DAY_IN_SECONDS      = 86400L;
  /*
   * Define month in seconds
   */
  private final        Long       MONTH_IN_SECONDS    = 604800L;
  /**
   * Reference to service implementation of {@link JiraSoapClient}
   */
  private JiraSoapClient             jiraSoapClient;
  /**
   * Reference to service implementation of {@link JiraRestClient}
   */
  private JiraRestClient             jiraRestClient;
  /**
   * Reference to service implementation of {@link JiraResourceBuilder}
   */
  private JiraResourceBuilder        jiraResourceBuilder;
  /**
   * Reference to service implementation of {@link InstanceConfigurationDAO}
   */
  private InstanceConfigurationDAO   instanceConfigurationDAO;
  /**
   * Reference to service implementation of {@link PluginConfigurationService}
   */
  private PluginConfigurationService pluginConfigurationService;

  /**
   * {@inheritDoc}
   *
   * @throws JiraRestException
   * @throws
   */
  @Override
  public boolean createBug(final String pUserName, final String pForgeId, final String pInstanceId,
                           final BugTrackerIssueBean pBug) throws PluginServiceException, JiraRestException
  {
    final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
    try
    {

      // Get jira connector
      final JiraRestConnector connector = jiraRestClient
                                              .getConnector(((JiraConfigurationService) pluginConfigurationService)
                                                                .getRestClientURL(instance.getToolInstance()
                                                                                          .getBaseURL()),
                                                            pluginConfigurationService.getClientAdmin(),
                                                            pluginConfigurationService.getClientPwd());

      // Get the project to give the project leader to the data builder
      final Project project = jiraRestClient.getProjectByKey(connector, instance.getToolProjectId());

      // Get all the priorities
      final Iterable<Priority> priorities = jiraRestClient.getPriorities(connector);

      // Get all the statuses
      final Iterable<Status> statuses = jiraRestClient.getStatuses(connector);

      // Build the jira issue
      final IssueInput issueInput = jiraResourceBuilder.buildIssue(pBug, instance.getToolProjectId(), pUserName,
                                                                   project, priorities, statuses);

      jiraRestClient.createIssue(connector, issueInput);
    }
    catch (final JiraRestException e)
    {
      throw new PluginServiceException(String
                                           .format("Unable to create issue on jira instance with [tool_project_id=%s, bug_bean=%s]",
                                                   instance.getToolProjectId(), pBug.toString()), e);
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerProjectVersionsBean getAllProjectVersions(final String pForgeId, final String pInstanceId,
                                                             final String pCurrentUser)
      throws BugTrackerServiceException
  {
    final BugTrackerProjectVersionsBean requestBean = new BugTrackerProjectVersionsBeanImpl();
    InstanceConfiguration               instance    = null;
    try
    {
      instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

      final String toolProjectId = instance.getToolProjectId();

      // Obtain jira connector
      final JiraSoapConnector connector = jiraSoapClient.getConnector(pluginConfigurationService.getClientURL(instance
                                                                                                                  .getToolInstance()
                                                                                                                  .getBaseURL()),
                                                                      pluginConfigurationService.getClientAdmin(),
                                                                      pluginConfigurationService.getClientPwd());

      final RemoteVersion[] projectVersionDatas = jiraSoapClient.getVersions(connector, toolProjectId);
      for (final RemoteVersion projectVersionData : projectVersionDatas)
      {
        requestBean.addVersion(projectVersionData.getName());
      }

    }
    catch (final JiraSoapException e)
    {
      throw new BugTrackerServiceException(String
                                               .format("Unable to get all product versions on jira instance with [tool_project_id=%s]",
                                                       instance.getToolProjectId()), e);
    }
    catch (final PluginServiceException e)
    {
      throw new BugTrackerServiceException(String.format("Unable to get build jira connector with [instance=%s]",
                                                         instance), e);
    }
    return requestBean;
  }

  /**
   * {@inheritDoc}
   *
   * @throws JiraRestException
   */
  @Override
  public BugTrackerIssuesBean getProjectIssuesByParameters(final String pForgeId, final String pInstanceId,
                                                           final String pCurrentUser, final JiraGetIssuesFilter pFilter)
      throws BugTrackerServiceException
  {
    final BugTrackerIssuesBean requestBean = new BugTrackerIssuesBeanImpl();
    InstanceConfiguration      instance    = null;
    try
    {
      instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

      // Obtain jira connector
      final JiraSoapConnector connector = jiraSoapClient.getConnector(pluginConfigurationService.getClientURL(instance
                                                                                                                  .getToolInstance()
                                                                                                                  .getBaseURL()),
                                                                      pluginConfigurationService.getClientAdmin(),
                                                                      pluginConfigurationService.getClientPwd());

      // Get jira rest connector
      final JiraRestConnector restConnector = jiraRestClient
                                                  .getConnector(((JiraConfigurationService) pluginConfigurationService)
                                                                    .getRestClientURL(instance.getToolInstance()
                                                                                              .getBaseURL()),
                                                                pluginConfigurationService.getClientAdmin(),
                                                                pluginConfigurationService.getClientPwd());

      // Build the jql query to get the issues filtered by target version and by category.
      final StringBuilder jqlSearch = new StringBuilder();

      if ((pFilter.getLabel() != null) && (pFilter.getFixVersion() != null)
          && (pFilter.getAffectedVersion() == null))
      {
        // Build JQL Search with category, target version and project key
        jqlSearch.append("project = ").append(instance.getToolProjectId()).append(" AND fixVersion = \"")
            .append(pFilter.getFixVersion()).append("\" AND labels = ").append(pFilter.getLabel());
      }
      else if ((pFilter.getLabel() != null) && (pFilter.getFixVersion() == null)
          && (pFilter.getAffectedVersion() == null))
      {
        // Build JQL Search with category and project key
        jqlSearch.append("project = ").append(instance.getToolProjectId()).append(" AND labels = ")
            .append(pFilter.getLabel());
      }
      else if ((pFilter.getLabel() == null) && (pFilter.getFixVersion() != null)
          && (pFilter.getAffectedVersion() == null))
      {
        // Build JQL Search with target version and project key
        jqlSearch.append("project = ").append(instance.getToolProjectId()).append(" AND fixVersion =\"")
            .append(pFilter.getFixVersion()).append("\"");
      }
      else if ((pFilter.getLabel() == null) && (pFilter.getFixVersion() == null)
          && (pFilter.getAffectedVersion() != null))
      {
        // Build JQL Search with version and project key
        jqlSearch.append("project = ").append(instance.getToolProjectId()).append(" AND affectedVersion =\"")
            .append(pFilter.getAffectedVersion()).append("\"");
      }
      else
      {
        // Build JQL Search with project key
        jqlSearch.append("project = ").append(instance.getToolProjectId());
      }

      final List<Issue> issues = jiraRestClient.searchJqlWithChangeLog(restConnector, jqlSearch.toString());

      for (final Issue issue : issues)
      {
        requestBean.addIssue(jiraResourceBuilder.buildBugTrackerIssueBean(issue));
      }
    }
    catch (final JiraRestException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to find project bugs on jira instance with [tool_project_id=%s]",
          instance.getToolProjectId()), e);
    }
    catch (final JiraSoapException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to find project bugs on jira instance with [tool_project_id=%s]",
          instance.getToolProjectId()), e);
    }
    catch (final JSONException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Invalid JSON parameters to find project bugs on jira instance with [tool_project_id=%s]",
          instance.getToolProjectId()), e);
    }
    catch (final PluginServiceException e)
    {
      throw new BugTrackerServiceException(String.format("Unable to get build connecter with [instance=%s]",
          instance), e);
    }
    return requestBean;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssueBean getIssue(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pBugTrackerId) throws BugTrackerServiceException
  {
    BugTrackerIssueBean requestBean = new BugTrackerIssueBeanImpl();
    InstanceConfiguration instance = null;
    try
    {
      instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

      // Obtain jira connector
      final JiraSoapConnector connector = jiraSoapClient.getConnector(
          pluginConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // Get jira rest connector
      final JiraRestConnector restConnector = jiraRestClient.getConnector(
          ((JiraConfigurationService) pluginConfigurationService).getRestClientURL(instance.getToolInstance()
              .getBaseURL()), pluginConfigurationService.getClientAdmin(), pluginConfigurationService
              .getClientPwd());

      // Build the jql query to get the issues filtered by target version and by category.

      // Build JQL Search with key and project key

      // maxNumResults is set to 1000 by default, the user should be in jira.search.views.max.unlimited.group
      // to get all the results
      final RemoteIssue[] issuesData = jiraSoapClient.getIssuesFromJqlSearch(connector,
                                                                             "project = " + instance.getToolProjectId()
                                                                                 + " AND key = " + pBugTrackerId,
          1000);
      final Issue issue = jiraRestClient.getIssue(restConnector, issuesData[0].getKey());
      requestBean = jiraResourceBuilder.buildBugTrackerIssueBean(issue);

    }
    catch (final JiraSoapException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to find project bugs on jira instance with [bugtrakerid=%s]", instance.getToolProjectId()),
          e);
    }
    catch (final JiraRestException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to find project bugs on jira instance with [bugtrakerid=%s]", instance.getToolProjectId()),
          e);
    }
    catch (final JSONException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Invalid JSON parameters to find project bugs on jira instance with [tool_project_id=%s]",
          instance.getToolProjectId()), e);
    }
    catch (final PluginServiceException e)
    {
      throw new BugTrackerServiceException(String.format("Unable to get build connecter with [instance=%s]",
          instance), e);
    }
    return requestBean;
  }

  @Override
  public Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> getProjectIssueHistoryByStatus(
      final String pForgeId, final String pInstanceId, final String pCurrentUser, final Date pStart,
      final Date pEnd, final Long pIncrement, final JiraGetIssuesFilter pFilter)
      throws BugTrackerServiceException
  {

    // Build a Map of Jira Statuses
    final List<Status> statuses = getAllStatuses(pForgeId, pInstanceId, pCurrentUser);
    final Map<String, BugTrackerStatusBean> mapStatuses = new HashMap<>();
    BugTrackerStatusBean statusOpen = null;
    for (final Status status : statuses)
    {
      final BugTrackerStatusBean bugTrackerStatusBean = convertStatusToBugTrackerStatus(status);
      mapStatuses.put(bugTrackerStatusBean.getId().toString(), bugTrackerStatusBean);
      // Get Jira default status by its default ID
      if (JIRA_OPEN_STATUS_ID.equals(status.getId()))
      {
        statusOpen = bugTrackerStatusBean;
      }
    }

    // Gets dates to print on the widget depending on the increment
    final List<Date> dates = getIncrementalDates(pStart, pEnd, pIncrement);

    // Get Issues created before the pEnd date
    final List<Issue> issues = getIssuesInPeriod(pInstanceId, pEnd, pFilter.getAffectedVersion());

    // Build Issues Status History

    JiraIssuesStatusHistory issuesStatusHistory = new JiraIssuesStatusHistory(statusOpen);
    if (issues != null)
    {
      issuesStatusHistory = buildIssuesStatusHistory(statusOpen, mapStatuses, issues, dates);
    }

    // Build the map to return depending on statusDatas
    final Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> statusHistoryBean = new HashMap<>();

    for (final BugTrackerStatusBean bugTrackerStatusBeanUsed : issuesStatusHistory
        .getUsedBugTrackerStatusBean())
    {
      final List<BugTrackerHistoryValueBean> bugTrackerHistoryValueBeanList = new ArrayList<>();
      for (final Date date : dates)
      {
        Long statusIncrement = 0L;
        for (final Map.Entry<String, JiraIssueStatusHistory> entryIssueStatusHistory : issuesStatusHistory
            .getIssuesStatusHistory().entrySet())
        {
          if ((entryIssueStatusHistory.getValue().getStatusesHistoryByDate(date) != null)
              && entryIssueStatusHistory.getValue().getStatusesHistoryByDate(date)
                  .equals(bugTrackerStatusBeanUsed))
          {
            statusIncrement++;
          }
        }
        final BugTrackerHistoryValueBean bugTrackerHistoryValueBean = new BugTrackerHistoryValueBeanImpl();
        bugTrackerHistoryValueBean.setDate(date);
        bugTrackerHistoryValueBean.setValue(statusIncrement);
        bugTrackerHistoryValueBeanList.add(bugTrackerHistoryValueBean);
      }
      statusHistoryBean.put(bugTrackerStatusBeanUsed, bugTrackerHistoryValueBeanList);
    }

    return statusHistoryBean;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Status> getAllStatuses(final String pForgeId, final String pInstanceId, final String pCurrentUser)
      throws BugTrackerServiceException
  {
    ArrayList<Status>     listStatus = new ArrayList<>();
    InstanceConfiguration instance   = null;
    try
    {
      instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

      // Get jira rest connector
      final JiraRestConnector restConnector = jiraRestClient
                                                  .getConnector(((JiraConfigurationService) pluginConfigurationService)
                                                                    .getRestClientURL(instance.getToolInstance()
                                                                                              .getBaseURL()),
                                                                pluginConfigurationService.getClientAdmin(),
                                                                pluginConfigurationService.getClientPwd());

      final Iterable<Status> status = jiraRestClient.getStatuses(restConnector);

      listStatus = Lists.newArrayList(status);
    }
    catch (final JiraRestException e)
    {
      throw new BugTrackerServiceException("Unable to get all product status on jira instance", e);
    }
    return listStatus;
  }

  /**
   * Convert a Jira Status to a BugTrackerStatusBean
   *
   * @param pStatus
   * @return
   */
  private BugTrackerStatusBean convertStatusToBugTrackerStatus(final Status pStatus)
  {
    final BugTrackerStatusBean bugTrackerStatusBean = new BugTrackerStatusBeanImpl();
    bugTrackerStatusBean.setId(pStatus.getId());
    bugTrackerStatusBean.setLabel(pStatus.getName());
    return bugTrackerStatusBean;
  }

  /**
   * Method to calculate a list of dates between two given dates with a given time interval of day or month
   *
   * @param pStart
   * @param pEnd
   * @param pIncrement
   * @return a List of dates formated as "yyyy:MM/dd 00:00:00",
   */
  private List<Date> getIncrementalDates(final Date pStart, final Date pEnd, final Long pIncrement)
  {

    // Define le list of dates to return
    final List<Date> dates = new ArrayList<Date>();

    // Define a date today at 00:00:01
    final Calendar today = new GregorianCalendar();
    today.set(Calendar.HOUR_OF_DAY, 0);
    today.set(Calendar.MINUTE, 0);
    today.set(Calendar.SECOND, 1);
    today.set(Calendar.MILLISECOND, 0);

    // Define the time in second between two date depending on the given time interval
    int incrementInSecond = 0;
    if (DAY_IN_SECONDS.equals(pIncrement))
    {
      incrementInSecond = DAY_IN_SECONDS.intValue();
    }
    else
    {
      incrementInSecond = MONTH_IN_SECONDS.intValue();
    }

    // Build the list of dates to return. The last date should be before today 00:00:01 AND before pEnd
    Date incrementDate = pStart;
    while (incrementDate.before(pEnd) && incrementDate.before(today.getTime()))
    {
      dates.add(incrementDate);

      final Calendar c = new GregorianCalendar();
      c.setTime(incrementDate);
      c.add(Calendar.SECOND, incrementInSecond);
      c.set(Calendar.HOUR_OF_DAY, 0);
      c.set(Calendar.MINUTE, 0);
      c.set(Calendar.SECOND, 0);
      c.set(Calendar.MILLISECOND, 0);
      incrementDate = c.getTime();
    }

    return dates;
  }

  /**
   * Method to get all Issues depending on a given time period and a given version
   *
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pStart
   * @param pEnd
   * @param pVersion
   * @return
   * @throws BugTrackerServiceException
   */
  private List<Issue> getIssuesInPeriod(final String pInstanceId, final Date pEnd, final String pVersion)
      throws BugTrackerServiceException
  {

    // Init the list of issues to return
    List<Issue> issues = new ArrayList<Issue>();

    // Get the instance configurations
    final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

    try
    {

      // Get jira rest connector
      final JiraRestConnector restConnector = jiraRestClient.getConnector(
          ((JiraConfigurationService) pluginConfigurationService).getRestClientURL(instance.getToolInstance()
              .getBaseURL()), pluginConfigurationService.getClientAdmin(), pluginConfigurationService
              .getClientPwd());

      // Get the day after pEnd date and format it to inject into JQL query. If pEnd is 15/09/2014 the JQL
      // will query "created < 16/09/2014"
      final DateTime endDateDayAfter = new DateTime(pEnd).plusDays(1);
      final String endDateFormated = formatter.format(endDateDayAfter.toDate());

      // Construct the JQL Search Query
      final StringBuilder jqlSearch = new StringBuilder();
      jqlSearch.append("project = ").append(instance.getToolProjectId()).append(" AND created < \"")
          .append(endDateFormated).append("\"");

      // Set affected version if needed
      if (pVersion != null)
      {
        jqlSearch.append(" AND affectedVersion =\"").append(pVersion).append("\"");
      }

      issues = jiraRestClient.searchJqlWithChangeLog(restConnector, jqlSearch.toString());

    }

    catch (final JiraRestException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to find project bugs on jira instance with [bugtrakerid=%s]", instance.getToolProjectId()),
          e);
    }

    return issues;
  }

  private JiraIssuesStatusHistory buildIssuesStatusHistory(final BugTrackerStatusBean pBugTrackerStatusBean,
                                                           final Map<String, BugTrackerStatusBean> pMapStatuses,
                                                           final List<Issue> pIssues, final List<Date> pDates)
  {
    // Initialize a Issues History
    final JiraIssuesStatusHistory issuesStatusHistory = new JiraIssuesStatusHistory(pBugTrackerStatusBean);

    for (final Issue issue : pIssues)
    {
      // If the Issue History do not exist, create it
      if (!issuesStatusHistory.getIssuesStatusHistory().containsKey(issue.getKey()))
      {

        // Init the Issue history, for each date from "dates" set the status at the default status : OPEN
        final Map<Date, BugTrackerStatusBean> initHistory = new HashMap<>();
        for (final Date date : pDates)
        {
          // History begin when the Issue is created
          final Calendar c = new GregorianCalendar();
          c.setTime(date);
          c.set(Calendar.HOUR_OF_DAY, 23);
          c.set(Calendar.MINUTE, 59);
          c.set(Calendar.SECOND, 59);
          if (issue.getCreationDate().toDate().before(c.getTime()))
          {
            initHistory.put(date, pBugTrackerStatusBean);
          }
        }

        // Create the Issue history the map
        final JiraIssueStatusHistory jiraIssueStatusHistory = new JiraIssueStatusHistory(issue.getKey(), initHistory);

        // Add the Issue history into the
        issuesStatusHistory.addIssuesStatusHistory(issue.getKey(), jiraIssueStatusHistory);
      }

      // If there is no changelogs, create the status history
      if (!Iterables.isEmpty(issue.getChangelog()))
      {
        // Browse Issue Changelogs
        for (final ChangelogGroup changelogGroup : issue.getChangelog())
        {
          // Browse Issue ChangelogItems
          for (final ChangelogItem changelogItem : changelogGroup.getItems())
          {
            // If the current ChangelogItems is linked to the status field create or add an status history
            if (JIRA_STATUS_FIELD.equals(changelogItem.getField()))
            {
              final JiraIssueStatusHistory jiraIssueStatusHistory = issuesStatusHistory.getIssueStatusHistory(issue
                                                                                                                  .getKey());
              jiraIssueStatusHistory.setStatusesHistory(buildHistory(changelogGroup.getCreated().toDate(),
                                                                     jiraIssueStatusHistory.getStatusesHistory(),
                                                                     pMapStatuses.get(changelogItem.getTo())));
              if (!issuesStatusHistory.getUsedBugTrackerStatusBean().contains(pMapStatuses.get(changelogItem.getTo())))
              {
                issuesStatusHistory.addBugTrackerStatusBean(pMapStatuses.get(changelogItem.getTo()));
              }
            }
          }
        }
      }
    }
    return issuesStatusHistory;
  }

  /**
   * Update an Issue history by replacing all the status after a given date
   *
   * @param pDate
   * @param pStatusHistory
   * @param status
   * @return
   */
  private Map<Date, BugTrackerStatusBean> buildHistory(final Date pDate,
                                                       final Map<Date, BugTrackerStatusBean> pStatusHistory,
                                                       final BugTrackerStatusBean pBugTrackerStatusBean)
  {
    // Init the map to return

    // Browse the given map and update the status for each entry after the given date
    for (final Map.Entry<Date, BugTrackerStatusBean> entry : pStatusHistory.entrySet())
    {
      final Calendar c = new GregorianCalendar();
      c.setTime(entry.getKey());
      c.set(Calendar.HOUR_OF_DAY, 23);
      c.set(Calendar.MINUTE, 59);
      c.set(Calendar.SECOND, 59);

      if (pDate.before(c.getTime()))
      {
        pStatusHistory.put(entry.getKey(), pBugTrackerStatusBean);
      }
    }

    return pStatusHistory;
  }

  /**
   * @return the jiraSoapClient
   */
  public JiraSoapClient getJiraSoapClient()
  {
    return jiraSoapClient;
  }

  /**
   * @param jiraSoapClient
   *          the jiraSoapClient to set
   */
  public void setJiraSoapClient(final JiraSoapClient pJiraSoapClient)
  {
    jiraSoapClient = pJiraSoapClient;
  }

  /**
   * @return the jiraRestCustomClient
   */
  public JiraRestClient getJiraRestClient()
  {
    return jiraRestClient;
  }

  /**
   * @param pjiraRestClient
   *          the jiraRestCustomClient to set
   */
  public void setJiraRestClient(final JiraRestClient pjiraRestClient)
  {
    jiraRestClient = pjiraRestClient;
  }

  /**
   * @return the jiraResourceBuilder
   */
  public JiraResourceBuilder getJiraResourceBuilder()
  {
    return jiraResourceBuilder;
  }

  /**
   * @param jiraResourceBuilder
   *          the jiraResourceBuilder to set
   */
  public void setJiraResourceBuilder(final JiraResourceBuilder pJiraResourceBuilder)
  {
    jiraResourceBuilder = pJiraResourceBuilder;
  }

  /**
   * @return the instanceConfigurationDAO
   */
  public InstanceConfigurationDAO getInstanceConfigurationDAO()
  {
    return instanceConfigurationDAO;
  }

  /**
   * @param instanceConfigurationDAO
   *          the instanceConfigurationDAO to set
   */
  public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    instanceConfigurationDAO = pInstanceConfigurationDAO;
  }

  /**
   * @return the pluginConfigurationService
   */
  public PluginConfigurationService getPluginConfigurationService()
  {
    return pluginConfigurationService;
  }

  /**
   * @param pluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

}
