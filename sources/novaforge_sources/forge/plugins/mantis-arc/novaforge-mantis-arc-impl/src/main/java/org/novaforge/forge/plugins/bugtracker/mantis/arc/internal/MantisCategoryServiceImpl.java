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
package org.novaforge.forge.plugins.bugtracker.mantis.arc.internal;

import net.sf.json.JSONObject;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerCategoryService;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerHistoryValueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssuesBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerProjectVersionsBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerServiceException;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerStatusBean;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;
import org.novaforge.forge.plugins.bucktracker.mantis.client.MantisSoapClient;
import org.novaforge.forge.plugins.bugtracker.mantis.arc.internal.services.MantisGetIssuesFilterImpl;
import org.novaforge.forge.plugins.bugtracker.mantis.arc.services.MantisFunctionalService;
import org.novaforge.forge.plugins.bugtracker.mantis.arc.services.MantisGetIssuesFilter;
import org.novaforge.forge.plugins.commons.services.AbstractPluginCategoryService;

import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Aimen Merkich
 */
public class MantisCategoryServiceImpl extends AbstractPluginCategoryService implements
    BugTrackerCategoryService
{

  private static final String     PROPERTY_FILE = "mantis_arc";

  /**
   * Reference to service implementation of {@link MantisSoapClient}
   */
  private MantisFunctionalService mantisArcFunctionalService;

  /**
   * {@inheritDoc}
   */
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
    return mantisArcFunctionalService.getAllProjectVersions(pForgeId, pInstanceId, pCurrentUser);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssuesBean findProjectIssuesByParameters(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pJSONParameter) throws BugTrackerServiceException
  {

    final MantisGetIssuesFilter filter = new MantisGetIssuesFilterImpl();

    // Get JSON Object
    final JSONObject json = JSONObject.fromObject(pJSONParameter);

    // Get the filters parameters
    if (json.has("category"))
    {
      filter.setCategory(json.getString("category"));
    }

    if (json.has("fixed_in_version"))
    {
      filter.setFixedInVersion(json.getString("fixed_in_version"));
    }

    filter.setUserLocale(Locale.ENGLISH);

    return mantisArcFunctionalService.getProjectIssuesByParameters(pForgeId, pInstanceId, pCurrentUser, filter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssuesBean getAllProjectIssues(final String pForgeId, final String pInstanceId,
      final String pCurrentUser) throws BugTrackerServiceException
  {
    final MantisGetIssuesFilter filter = new MantisGetIssuesFilterImpl();

    filter.setUserLocale(Locale.ENGLISH);

    return mantisArcFunctionalService.getProjectIssuesByParameters(pForgeId, pInstanceId, pCurrentUser, filter);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssuesBean getAllProjectIssuesByVersion(final String pForgeId, final String pInstanceId,
                                                           final String pCurrentUser, final String pVersion,
                                                           final Locale pLocale)
      throws BugTrackerServiceException
  {
    final MantisGetIssuesFilter filter = createFilter(pVersion, pLocale);

    return mantisArcFunctionalService.getProjectIssuesByParameters(pForgeId, pInstanceId, pCurrentUser, filter);
  }

  private MantisGetIssuesFilter createFilter(final String pVersion, final Locale pLocale)
  {
    final MantisGetIssuesFilter filter = new MantisGetIssuesFilterImpl();

    if (pLocale != null)
    {
      filter.setUserLocale(pLocale);
    }
    else
    {
      filter.setUserLocale(Locale.ENGLISH);
    }

    filter.setProductVersion(pVersion);
    return filter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BugTrackerIssueBean getIssue(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final String pBugTrackerId) throws BugTrackerServiceException
  {
    return mantisArcFunctionalService.getIssue(pForgeId, pInstanceId, pCurrentUser, pBugTrackerId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> getProjectIssueHistoryByStatus(final String pForgeId,
                                                                                                    final String pInstanceId,
                                                                                                    final String pCurrentUser,
                                                                                                    final Date pStart,
                                                                                                    final Date pEnd,
                                                                                                    final Long pIncrement,
                                                                                                    final String pVersion,
                                                                                                    final Locale pLocale)
      throws BugTrackerServiceException
  {
    // Check the parameters
    if ((pForgeId == null) || (pForgeId.trim().length() == 0) || (pInstanceId == null) || (pInstanceId.trim().length()
                                                                                               == 0) || (pCurrentUser
                                                                                                             == null)
            || (pCurrentUser.trim().length() == 0))
    {
      throw new BugTrackerServiceException("One of the following mandatory parameters [forge_id, instance_id, current_user] is null.");
    }
    final MantisGetIssuesFilter filter = createFilter(pVersion, pLocale);

    return mantisArcFunctionalService.getProjectIssueHistoryByStatus(pForgeId, pInstanceId, pCurrentUser, pStart, pEnd,
                                                                     pIncrement, filter);
  }

  /**
   * Use by container to inject {@link MantisFunctionalService}
   * 
   * @param pMantisFunctionalService
   *          the mantisFunctionalService to set
   */
  public void setMantisArcFunctionalService(final MantisFunctionalService pMantisFunctionalService)
  {
    mantisArcFunctionalService = pMantisFunctionalService;
  }

}
