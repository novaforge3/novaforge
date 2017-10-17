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
package org.novaforge.forge.plugins.bucktracker.mantisstd.services;

import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerHistoryValueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssuesBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerProjectVersionsBean;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerServiceException;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerStatusBean;
import org.novaforge.forge.core.plugins.exceptions.PluginServiceException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Gauthier Cart
 */
public interface MantisFunctionalService
{

  /**
   * Allow to create a bug on Mantis instance.
   * 
   * @param pUserName
   * @param pForgeId
   * @param pInstanceId
   * @param pBug
   * @return true if succeed otherwise false
   * @throws PluginServiceException
   */
  boolean createBug(final String pUserName, final String pForgeId, final String pInstanceId,
      final BugTrackerIssueBean pBug) throws PluginServiceException;

  /**
   * This method returns the list of versions of a bugtracker project
   * 
   * @param pCurrentUser
   * @param pForgeId
   * @param pInstanceId
   * @return BugTrackerProjectVersionsBean
   * @throws BugTrackerServiceException
   */
  BugTrackerProjectVersionsBean getAllProjectVersions(final String pForgeId, final String pInstanceId,
      final String pCurrentUser) throws BugTrackerServiceException;

  /**
   * This method returns the list of issues of a bugtracker project found by parameters
   * 
   * @param pCurrentUser
   * @param pForgeId
   * @param pInstanceId
   * @param pMantisGetIssuesFilter
   * @return BugTrackerIssuesBean
   * @throws BugTrackerServiceException
   */
  BugTrackerIssuesBean getProjectIssuesByParameters(final String pForgeId, final String pInstanceId,
      final String pCurrentUser, final MantisGetIssuesFilter pFilter) throws BugTrackerServiceException;

  /**
   * This method returns an issue of a bugtracker project found by it's id
   * 
   * @param pForgeId
   * @param pInstanceId
   * @param pCurrentUser
   * @param pBugTrackerId
   *          the BugTrackerId
   * @return BugTrackerIssuesBean
   * @throws BugTrackerServiceException
   */
  BugTrackerIssueBean getIssue(final String pForgeId, final String pInstanceId, final String pCurrentUser,
      final String pBugTrackerId) throws BugTrackerServiceException;

  /**
   * This methods returns for each status of the project a history list of issues number
   * 
   * @param pForgeId
   *          the forge id
   * @param pInstanceId
   *          the instance id
   * @param pCurrentUser
   *          the user login making the request
   * @param pStart
   *          the date of start to build history
   * @param pEnd
   *          the date of end to build history
   * @param pIncrement
   *          the increment used between to history value (has to be in second)
   * @param pFilter
   *          filter used to retrieve issues
   * @return for each status of the project a history list of issues number
   * @throws BugTrackerServiceException
   */
  Map<BugTrackerStatusBean, List<BugTrackerHistoryValueBean>> getProjectIssueHistoryByStatus(String pForgeId,
      String pInstanceId, String pCurrentUser, Date pStart, Date pEnd, Long pIncrement,
      MantisGetIssuesFilter pFilter) throws BugTrackerServiceException;
}