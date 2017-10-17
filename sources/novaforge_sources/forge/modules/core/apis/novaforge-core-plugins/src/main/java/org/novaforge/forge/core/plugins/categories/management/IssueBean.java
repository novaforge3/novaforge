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
package org.novaforge.forge.core.plugins.categories.management;

import org.novaforge.forge.core.plugins.categories.PluginExchangeableBean;

/**
 * This object represents a link to a bug in a bug tracker
 */
public interface IssueBean extends PluginExchangeableBean
{

  /**
   * Get the internal bugId (managed by the Management Tool)
   * 
   * @return the bugId
   */
  String getBugId();

  /**
   * Set the bugId
   * 
   * @param bugId
   *          the bugId to set
   */
  void setBugId(final String bugId);

  /**
   * Get the bugTrackerId defined in the BugTracker tool.
   * 
   * @return the bugTrackerId
   */
  String getBugTrackerId();

  /**
   * Set the bugTrackerId
   * 
   * @param bugTrackerId
   *          the bugTrackerId to set
   */
  void setBugTrackerId(final String bugTrackerId);

  /**
   * Get the title of the issue (=Summary)
   * 
   * @return the title
   */
  String getTitle();

  /**
   * Set the title
   * 
   * @param title
   *          the title to set
   */
  void setTitle(final String title);

  /**
   * @return description value
   */
  String getDescription();

  /**
   * @param pDescription
   *          represents the description to set
   */
  void setDescription(final String pDescription);

  /**
   * @return additional information value
   */
  String getAdditionalInfo();

  /**
   * @param pInfo
   *          represents the info to set
   */
  void setAdditionalInfo(final String pInfo);

  /**
   * @return category
   */
  String getCategory();

  /**
   * @param pCategory
   *          represents the category to set
   */
  void setCategory(final String pCategory);

  /**
   * @return reporter
   */
  String getReporter();

  /**
   * @param pReporter
   *          represents the reporter to set
   */
  void setReporter(String pReporter);

  /**
   * @return severity
   */
  String getSeverity();

  /**
   * @param pSeverity
   *          represents the severity to set
   */
  void setSeverity(String pSeverity);

  /**
   * @return Priority
   */
  String getPriority();

  /**
   * @param pPriority
   *          represents the Priority to set
   */
  void setPriority(String pPriority);

  /**
   * @return Resolution
   */
  String getResolution();

  /**
   * @param pResolution
   *          represents the Resolution to set
   */
  void setResolution(String pResolution);

  /**
   * @return Status
   */
  String getStatus();

  /**
   * @param pStatus
   *          represents the Status to set
   */
  void setStatus(String pStatus);

  /**
   * @return ProductVersion
   */
  String getProductVersion();

  /**
   * @param pProductVersion
   *          represents the ProductVersion to set
   */
  void setProductVersion(String pProductVersion);

  /**
   * @return TargetVersion
   */
  String getTargetVersion();

  /**
   * @param pTargetVersion
   *          represents the TargetVersion to set
   */
  void setTargetVersion(String pTargetVersion);

  /**
   * @return FixedInVersion
   */
  String getFixedInVersion();

  /**
   * @param pFixedInVersion
   *          represents the FixedInVersion to set
   */
  void setFixedInVersion(String pFixedInVersion);

  /**
   * @return the Assignee
   */
  String getAssignedTo();

  /**
   * @param pAssignedTo
   *          represents the assignee to set
   */
  void setAssignedTo(String pAssignedTo);

  /**
   * @return the Reproducibility
   */
  String getReproducibility();

  /**
   * @param Reproducibility
   *          represents the reproducibility to set
   */
  void setReproducibility(String Reproducibility);

}
