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
package org.novaforge.forge.plugins.bugtracker.jira.internal;

import net.sf.json.JSONObject;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerCategoryService;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerHistoryValueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssuesBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerProjectVersionsBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerServiceException;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerStatusBean;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.bugtracker.jira.client.JiraSoapClient;
import org.novaforge.forge.plugins.bugtracker.jira.internal.services.JiraGetIssuesFilterImpl;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraFunctionalService;
import org.novaforge.forge.plugins.bugtracker.jira.services.JiraGetIssuesFilter;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
public class JiraCategoryServiceImpl extends AbstractPluginCategoryService implements
    BugTrackerCategoryService
{

  private static final String   PROPERTY_FILE = "jira";

  /**
   * Reference to service implementation of {@link JiraSoapClient}
   */
  private JiraFunctionalService jiraFunctionalService;

  @Override
  public String getApplicationAccessInfo(final String pInstanceId, final Locale pLocale)
      throws PluginServiceException
  {
    return getMessage(KEY_ACCESS_INFO, pLocale);
  }

  @Override
  protected String getPropertyFileName()
  {
    return PROPERTY_FILE;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerProjectVersionsBean getAllProjectVersions(final String pForgeId, final String pInstanceId,
      final String pCurrentUser) throws BugTrackerServiceException
  {
    return jiraFunctionalService.getAllProjectVersions(pForgeId, pInstanceId, pCurrentUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssuesBean findProjectIssuesByParameters(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pJSONParameter) throws BugTrackerServiceException
  {

    final JiraGetIssuesFilter filter = new JiraGetIssuesFilterImpl();

    // Get JSON Object
    final JSONObject json = JSONObject.fromObject(pJSONParameter);

    // Get the filters parameters
    if (json.has("category"))
    {
      filter.setLabel(json.getString("category"));
    }

    if (json.has("fixed_in_version"))
    {
      filter.setFixVersion(json.getString("fixed_in_version"));
    }

    return jiraFunctionalService.getProjectIssuesByParameters(pForgeId, pInstanceId, pCurrentUser, filter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssuesBean getAllProjectIssues(final String pForgeId, final String pInstanceId,
                                                  final String pCurrentUser) throws BugTrackerServiceException
  {
    final JiraGetIssuesFilter filter = new JiraGetIssuesFilterImpl();

    return jiraFunctionalService.getProjectIssuesByParameters(pForgeId, pInstanceId, pCurrentUser, filter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssuesBean getAllProjectIssuesByVersion(final String pForgeId, final String pInstanceId,
                                                           final String pCurrentUser, final String pVersion,
                                                           final Locale pLocale) throws BugTrackerServiceException
  {
    final JiraGetIssuesFilter filter = new JiraGetIssuesFilterImpl();

    filter.setAffectedVersion(pVersion);

    return jiraFunctionalService.getProjectIssuesByParameters(pForgeId, pInstanceId, pCurrentUser, filter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssueBean getIssue(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pBugTrackerId) throws BugTrackerServiceException
  {
    return jiraFunctionalService.getIssue(pForgeId, pInstanceId, pCurrentUser, pBugTrackerId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> getProjectIssueHistoryByStatus(
      final String pForgeId, final String pInstanceId, final String pCurrentUser, final Date pStart,
      final Date pEnd, final Long pIncrement, final String pVersion, final Locale pLocale)
      throws BugTrackerServiceException
  {
    // Check the parameters
    if ((pForgeId == null) || (pForgeId.trim().length() == 0) || (pInstanceId == null)
        || (pInstanceId.trim().length() == 0) || (pCurrentUser == null)
        || (pCurrentUser.trim().length() == 0))
    {
      throw new BugTrackerServiceException(
          "One of the following mandatory parameters [forge_id, instance_id, current_user] is null.");
    }
    final JiraGetIssuesFilter filter = new JiraGetIssuesFilterImpl();
    filter.setAffectedVersion(pVersion);

    return jiraFunctionalService.getProjectIssueHistoryByStatus(pForgeId, pInstanceId, pCurrentUser, pStart,
        pEnd, pIncrement, filter);
  }

  /**
   * Use by container to inject {@link JiraFunctionalService}
   *
   * @param pJiraFunctionalService
   *     the jiraFunctionalService to set
   */
  public void setJiraFunctionalService(final JiraFunctionalService pJiraFunctionalService)
  {
    jiraFunctionalService = pJiraFunctionalService;
  }

}
