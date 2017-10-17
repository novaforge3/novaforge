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
package org.novaforge.forge.plugins.bucktracker.mantisstd.internal.services;

import net.sf.json.JSONException;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerHistoryValueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssuesBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerProjectVersionsBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerServiceException;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerStatusBean;
import org.novaforge.forge.core.plugins.categories.commons.Attachment;
import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.core.plugins.services.PluginConfigurationService;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapClient;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapConnector;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapException;
import org.novaforge.forge.plugins.bucktracker.mantis.constants.MantisLang;
import org.novaforge.forge.plugins.bucktracker.mantis.datamapper.MantisResourceBuilder;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.AccountData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.FilterData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueHistoryData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.IssueHistoryStatusData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ObjectRef;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectStatusData;
import org.novaforge.forge.plugins.bucktracker.mantis.soap.ProjectVersionData;
import org.novaforge.forge.plugins.bucktracker.mantisstd.services.MantisFunctionalService;
import org.novaforge.forge.plugins.bucktracker.mantisstd.services.MantisGetIssuesFilter;
import org.novaforge.forge.plugins.categories.beans.BugTrackerHistoryValueBeanImpl;
import org.novaforge.forge.plugins.categories.beans.BugTrackerIssuesBeanImpl;
import org.novaforge.forge.plugins.categories.beans.BugTrackerProjectVersionsBeanImpl;
import org.novaforge.forge.plugins.categories.beans.BugTrackerStatusBeanImpl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class MantisFunctionalServiceImpl implements MantisFunctionalService
{

  /**
   * Reference to service implementation of {@link MantisSoapClient}
   */
  private MantisSoapClient           mantisSoapClient;
  /**
   * Reference to service implementation of {@link MantisResourceBuilder}
   */
  private MantisResourceBuilder      mantisResourceBuilder;
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
   */
  @Override
  public boolean createBug(final String pUserName, final String pForgeId, final String pInstanceId,
      final BugTrackerIssueBean pBug) throws PluginServiceException
  {
    final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);
    try
    {
      final IssueData issueData = mantisResourceBuilder.buildIssueData(pBug, instance.getToolProjectId(),
          pUserName);
      // Obtain mantis connector

      final MantisSoapConnector connector = mantisSoapClient.getConnector(
          pluginConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
      final BigInteger issueId = mantisSoapClient.mc_issue_add(connector, issueData);

      if (pBug.getAttachments() != null)
      {
        final AccountData uploaderAccount = new AccountData();
        uploaderAccount.setName(pUserName);
        for (final Attachment attachment : pBug.getAttachments())
        {
          mantisSoapClient.mc_issue_attachment_uploader_add(connector, issueId, uploaderAccount,
              attachment.getName(), attachment.getFileType(), attachment.getContent());
        }
      }
    }
    catch (final MantisSoapException e)
    {
      throw new PluginServiceException(String.format(
          "Unable to create issue on mantis instance with [tool_project_id=%s, bug_bean=%s]",
          instance.getToolProjectId(), pBug.toString()), e);
    }

    return true;
  }

  @Override
  public BugTrackerProjectVersionsBean getAllProjectVersions(final String pForgeId, final String pInstanceId,
      final String pCurrentUser) throws BugTrackerServiceException
  {

    final BugTrackerProjectVersionsBean requestBean = new BugTrackerProjectVersionsBeanImpl();
    InstanceConfiguration instance = null;
    try
    {
      instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

      final BigInteger toolProjectId = new BigInteger(instance.getToolProjectId());

      // Obtain mantis connector

      final MantisSoapConnector connector = mantisSoapClient.getConnector(
          pluginConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      final ProjectVersionData[] projectVersionDatas = mantisSoapClient.mc_project_get_versions(connector,
          toolProjectId);
      for (final ProjectVersionData projectVersionData : projectVersionDatas)
      {
        requestBean.addVersion(projectVersionData.getName());
      }

    }
    catch (final MantisSoapException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to get all product versions on mantis instance with [tool_project_id=%s]",
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
  public BugTrackerIssuesBean getProjectIssuesByParameters(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final MantisGetIssuesFilter pFilter) throws BugTrackerServiceException
  {
    final BugTrackerIssuesBean requestBean = new BugTrackerIssuesBeanImpl();
    InstanceConfiguration instance = null;
    try
    {
      instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

      final BigInteger toolProjectId = new BigInteger(instance.getToolProjectId());

      // Obtain mantis connector
      final MantisSoapConnector connector = mantisSoapClient.getConnector(
          pluginConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // The 2 last parameters reference the way to get all the projects bug without pagination filters
      final IssueData[] issuesData = mantisSoapClient.mc_project_get_issues(connector, toolProjectId,
          BigInteger.valueOf(1), BigInteger.valueOf(-1));

      // Get all Mantis statut for the current project
      final ProjectStatusData[] projectStatusDatas = mantisSoapClient.mc_project_get_statuses(connector,
          toolProjectId, MantisLang.getMantisLang(pFilter.getUserLocale()).getLang());

      for (final IssueData issueData : issuesData)
      {
        if (filterIssue(issueData, pFilter.getCategory(), pFilter.getFixedInVersion(), pFilter.getProductVersion()))
        {
          for (final ProjectStatusData projectStatusData : projectStatusDatas)
          {
            if (projectStatusData.getId().equals(issueData.getStatus().getId()))
            {
              final ObjectRef statusUpdated = new ObjectRef(projectStatusData.getId(),
                  projectStatusData.getTitle());
              issueData.setStatus(statusUpdated);
            }
          }
          requestBean.addIssue(mantisResourceBuilder.buildBugTrackerIssueBean(issueData));
        }
      }

    }
    catch (final MantisSoapException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to find project bugs on mantis instance with [tool_project_id=%s]",
          instance.getToolProjectId()), e);
    }
    catch (final JSONException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Invalid JSON parameters to find project bugs on mantis instance with [tool_project_id=%s]",
          instance.getToolProjectId()), e);
    }
    catch (final PluginServiceException e)
    {
      throw new BugTrackerServiceException(String.format("Unable to get build connecter with [instance=%s]",
          instance), e);
    }
    return requestBean;
  }

  private boolean filterIssue(final IssueData pIssue, final String pCategory, final String pFixedInVersion,
      final String pVersion)
  {
    boolean ret = true;
    if (pCategory != null)
    {
      ret = pCategory.equals(pIssue.getCategory());
    }

    if ((ret) && (pFixedInVersion != null))
    {
      ret = pFixedInVersion.equals(pIssue.getFixed_in_version());
    }

    if ((ret) && (pVersion != null))
    {
      ret = pVersion.equals(pIssue.getVersion());
    }

    return ret;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssueBean getIssue(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pBugTrackerId) throws BugTrackerServiceException
  {
    BugTrackerIssueBean issueBean = null;
    InstanceConfiguration instance = null;
    try
    {
      instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

      final BigInteger bugTrackerId = new BigInteger(pBugTrackerId);

      // Obtain mantis connector

      final MantisSoapConnector connector = mantisSoapClient.getConnector(
          pluginConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());

      // The 2 last parameters reference the way to get all the projects bug without pagination filters
      final IssueData issueData = mantisSoapClient.mc_issue_get(connector, bugTrackerId);
      issueBean = mantisResourceBuilder.buildBugTrackerIssueBean(issueData);
    }
    catch (final MantisSoapException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to find project bugs on mantis instance with [tool_project_id=%s]",
          instance.getToolProjectId()), e);
    }
    catch (final PluginServiceException e)
    {
      throw new BugTrackerServiceException(String.format("Unable to get build connecter with [instance=%s]",
          instance), e);
    }
    return issueBean;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> getProjectIssueHistoryByStatus(
      final String pForgeId, final String pInstanceId, final String pCurrentUser, final Date pStart,
      final Date pEnd, final Long pIncrement, final MantisGetIssuesFilter pFilter)
      throws BugTrackerServiceException
  {
    final Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> statusHistoryBean = new HashMap<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>>();
    try
    {
      final InstanceConfiguration instance = instanceConfigurationDAO.findByInstanceId(pInstanceId);

      final BigInteger toolProjectId = new BigInteger(instance.getToolProjectId());

      // Obtain mantis connector
      final MantisSoapConnector connector = mantisSoapClient.getConnector(
          pluginConfigurationService.getClientURL(instance.getToolInstance().getBaseURL()),
          pluginConfigurationService.getClientAdmin(), pluginConfigurationService.getClientPwd());
      // Building start and end Calendar object
      Calendar startInstance = null;
      if (pStart != null)
      {
        startInstance = Calendar.getInstance();
        startInstance.setTime(pStart);
      }
      Calendar endInstance = null;
      if (pEnd != null)
      {
        endInstance = Calendar.getInstance();
        endInstance.setTime(pEnd);
      }
      BigInteger incr = null;
      if (pIncrement != null)
      {
        incr = BigInteger.valueOf(pIncrement);
      }
      final IssueHistoryStatusData[] historyData = mantisSoapClient.mc_project_get_issues_history_by_status(
          connector, toolProjectId, startInstance, endInstance, incr, buildDateFilter(pFilter));

      // Get all Mantis statut for the current project
      final ProjectStatusData[] projectStatusDatas = mantisSoapClient.mc_project_get_statuses(connector,
          toolProjectId, MantisLang.getMantisLang(pFilter.getUserLocale()).getLang());

      for (final IssueHistoryStatusData historyStatus : historyData)
      {
        final BugTrackerStatusBean bugTrackerStatusBean = new BugTrackerStatusBeanImpl();
        final BigInteger status = historyStatus.getStatus();
        bugTrackerStatusBean.setId(status.longValue());
        bugTrackerStatusBean.setLabel(getStatusTitle(projectStatusDatas, status));
        final List<BugTrackerHistoryValueBean> historyValues = new ArrayList<BugTrackerHistoryValueBean>();
        for (final IssueHistoryData issueHistoryData : historyStatus.getHistory())
        {
          final BugTrackerHistoryValueBean bugTrackerHistoryValueBean = new BugTrackerHistoryValueBeanImpl();
          final Calendar time = issueHistoryData.getTime();
          bugTrackerHistoryValueBean.setDate(time.getTime());
          bugTrackerHistoryValueBean.setValue(issueHistoryData.getValue().longValue());
          historyValues.add(bugTrackerHistoryValueBean);
        }
        statusHistoryBean.put(bugTrackerStatusBean, historyValues);
      }

    }
    catch (final MantisSoapException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to find project bugs on mantis instance with [instance_id=%s]", pInstanceId), e);
    }
    catch (final JSONException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Invalid JSON parameters to find project bugs on mantis instance with [instance_id=%s]",
          pInstanceId), e);
    }
    catch (final PluginServiceException e)
    {
      throw new BugTrackerServiceException(String.format(
          "Unable to get build connecter with [instance_id=%s]", pInstanceId), e);
    }
    return statusHistoryBean;

  }

  private FilterData[] buildDateFilter(final MantisGetIssuesFilter pFilter)
  {
    final List<FilterData> filters = new ArrayList<FilterData>();
    if ((pFilter.getProductVersion() != null) && (!"".equals(pFilter.getProductVersion())))
    {
      final FilterData showVersionFilter = new FilterData();
      showVersionFilter.setName("show_version");
      showVersionFilter.setFilter_string(pFilter.getProductVersion());
      filters.add(showVersionFilter);
    }
    if ((pFilter.getFixedInVersion() != null) && (!"".equals(pFilter.getFixedInVersion())))
    {
      final FilterData fixVersionFilter = new FilterData();
      fixVersionFilter.setName("show_version");
      fixVersionFilter.setFilter_string(pFilter.getFixedInVersion());
      filters.add(fixVersionFilter);
    }
    return filters.toArray(new FilterData[filters.size()]);
  }

  private String getStatusTitle(final ProjectStatusData[] pProjectStatusData, final BigInteger pStatusId)
  {
    String statusTitle = "@" + pStatusId + "@";
    for (final ProjectStatusData projectStatusData : pProjectStatusData)
    {
      if (projectStatusData.getId().equals(pStatusId))
      {
        statusTitle = projectStatusData.getTitle();
        break;
      }
    }
    return statusTitle;
  }

  /**
   * Use by container to inject {@link InstanceConfigurationDAO}
   * 
   * @param pInstanceConfigurationDAO
   *          the instanceConfigurationDAO to set
   */
  public void setInstanceConfigurationDAO(final InstanceConfigurationDAO pInstanceConfigurationDAO)
  {
    instanceConfigurationDAO = pInstanceConfigurationDAO;
  }

  /**
   * Use by container to inject {@link MantisSoapClient}
   * 
   * @param pMantisSoapClient
   *          the mantisSoapClient to set
   */
  public void setMantisSoapClient(final MantisSoapClient pMantisSoapClient)
  {
    mantisSoapClient = pMantisSoapClient;
  }

  /**
   * Use by container to inject {@link MantisResourceBuilder}
   * 
   * @param pMantisResourceBuilder
   *          the mantisResourceBuilder to set
   */
  public void setMantisResourceBuilder(final MantisResourceBuilder pMantisResourceBuilder)
  {
    mantisResourceBuilder = pMantisResourceBuilder;
  }

  /**
   * Use by container to inject {@link PluginConfigurationService}
   * 
   * @param pPluginConfigurationService
   *          the pluginConfigurationService to set
   */
  public void setPluginConfigurationService(final PluginConfigurationService pPluginConfigurationService)
  {
    pluginConfigurationService = pPluginConfigurationService;
  }

}