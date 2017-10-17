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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Gauthier Cart
 */
public class JiraIssueStatusHistory
{

  /*
     * Define date format for jira
     */
  private static DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
  /**
   * Contains the Jira Issue Key
   */
  private String                          key;
  /**
   * Contains the status history values
   */
  private Map<Date, BugTrackerStatusBean> history;

  /**
   * Constructor
   *
   * @param pKey
   *          represents the Jira Issue Key
   * @param pHistory
   *          represents the status history values
   */
  public JiraIssueStatusHistory(String pKey, Map<Date, BugTrackerStatusBean> pHistory)
  {
    super();
    this.key = pKey;
    this.history = pHistory;
  }

  /**
   * Constructor
   *
   * @param pId
   *          represents the Jira Issue Id
   * @param pDate
   *          represents a date linked to a status
   * @param pStatus
   *          represent a status
   */
  public JiraIssueStatusHistory(String pKey, Date pDate, BugTrackerStatusBean pBugTrackerStatusBean)
  {
    super();
    this.key = pKey;
    Map<Date, BugTrackerStatusBean> history = new HashMap<>();
    history.put(pDate, pBugTrackerStatusBean);
    this.history = history;
  }

  /**
   * @return Issue Key value
   */
  public String getKey()
  {
    return this.key;
  }

  /**
   * @param pKey
   *          represents the Issue Key to set
   */
  public void setKey(String pKey)
  {
    this.key = pKey;
  }

  /**
   * @return status history linked to the issue
   */
  public Map<Date, BugTrackerStatusBean> getStatusesHistory()
  {
    return history;
  }

  /**
   * {@inheritDoc}
   */
  public void setStatusesHistory(Map<Date, BugTrackerStatusBean> pStatusesHistory)
  {
    this.history = pStatusesHistory;
  }

  /**
   * @param pStatus
   *          represents the status to set
   * @param pDate
   *          represents the date linked to the status
   */
  public void addStatus(Date pDate, BugTrackerStatusBean pBugTrackerStatusBean)
  {
    this.history.put(pDate, pBugTrackerStatusBean);
  }

  /**
   * @param pDate
   *          represents the date linked to the status
   * @return the status value
   */
  public BugTrackerStatusBean getStatusesHistoryByDate(Date pDate)
  {
    BugTrackerStatusBean bugTrackerStatusBean = null;

    if (this.history.containsKey(pDate))
    {
      bugTrackerStatusBean = this.history.get(pDate);
    }
    return bugTrackerStatusBean;
  }

  /**
   * Remove a date
   *
   * @param pDate
   *          represents the date linked to the status
   */
  public void removeStatusesHistoryByDate(Date pDate)
  {
    if (this.history.containsKey(pDate))
    {
      this.history.remove(pDate);
    }
  }

  /**
   * {@inheritDoc}
   */
  public String toString()
  {
    final StringBuilder jiraIssueStatusHistoryToString = new StringBuilder();
    jiraIssueStatusHistoryToString.append("Issue Key = ").append(this.key)
        .append(" -> History Statuses : [ ");

    for (Map.Entry<Date, BugTrackerStatusBean> entry : this.history.entrySet())
    {
      jiraIssueStatusHistoryToString.append(formatter.format(entry.getKey())).append(" -> ")
          .append(entry.getValue().getLabel()).append(" ");
    }
    jiraIssueStatusHistoryToString.append(" ]");

    return jiraIssueStatusHistoryToString.toString();
  }
}
