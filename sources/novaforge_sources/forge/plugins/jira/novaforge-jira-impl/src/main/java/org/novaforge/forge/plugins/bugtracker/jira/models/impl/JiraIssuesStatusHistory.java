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
package org.novaforge.forge.plugins.bugtracker.jira.models.impl;

import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerStatusBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Gauthier Cart
 */
public class JiraIssuesStatusHistory
{

  /**
   * Contains the Jira Issues History
   */
  private Map<String, JiraIssueStatusHistory> issuesStatusHistory      = new HashMap<String, JiraIssueStatusHistory>();
  /**
   * Contains the status used in the Issues History
   */
  private List<BugTrackerStatusBean>          usedBugTrackerStatusBean = new ArrayList<>();

  /**
   * @param pBugTrackerStatusBean
   */
  public JiraIssuesStatusHistory(BugTrackerStatusBean pBugTrackerStatusBean)
  {
    super();
    this.usedBugTrackerStatusBean.add(pBugTrackerStatusBean);
  }

  /**
   * @param pBugTrackerStatusBean
   */
  public void addBugTrackerStatusBean(BugTrackerStatusBean pBugTrackerStatusBean)
  {
    this.usedBugTrackerStatusBean.add(pBugTrackerStatusBean);
  }

  /**
   * @param pIssueKey
   * @param pJiraIssueStatusHistory
   */
  public void addIssuesStatusHistory(String pIssueKey, JiraIssueStatusHistory pJiraIssueStatusHistory)
  {
    this.issuesStatusHistory.put(pIssueKey, pJiraIssueStatusHistory);
  }

  /**
   * 
   */
  public Map<String, JiraIssueStatusHistory> getIssuesStatusHistory()
  {
    return this.issuesStatusHistory;
  }

  /**
   * 
   */
  public List<BugTrackerStatusBean> getUsedBugTrackerStatusBean()
  {
    return this.usedBugTrackerStatusBean;
  }

  /**
   * 
   */
  public JiraIssueStatusHistory getIssueStatusHistory(String pIssueKey)
  {

    JiraIssueStatusHistory issueStatusHistory = null;
    if (this.issuesStatusHistory.containsKey(pIssueKey))
    {
      issueStatusHistory = this.issuesStatusHistory.get(pIssueKey);
    }

    return issueStatusHistory;
  }
}
