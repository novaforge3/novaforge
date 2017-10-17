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
package org.novaforge.forge.core.plugins.categories.bugtracker;

import org.novaforge.forge.core.plugins.categories.PluginExchangeableBean;
import org.novaforge.forge.core.plugins.categories.commons.Attachment;

import java.util.List;

/**
 * Represents the complete BugTracker Issue. For now we do not manage the Attachements, links, notes but it
 * will be possible to add this kind of info here.
 * 
 * @author lamirang
 */
public interface BugTrackerIssueBean extends PluginExchangeableBean
{
  /**
   * @return id value
   */
  String getId();

  /**
   * @param pId
   *          represents the id to set
   */
  void setId(final String pId);

  /**
   * @return title value
   */
  String getTitle();

  /**
   * @param pTitle
   *          represents the title to set
   */
  void setTitle(final String pTitle);

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
   * @return the Assignee
   */
  String getAssignedTo();

  /**
   * @param pAssignedTo
   *          represents the assignee to set
   */
  void setAssignedTo(String pAssignedTo);

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
   * @return the Reproducibility
   */
  String getReproducibility();

  /**
   * @param Reproducibility
   *          represents the reproducibility to set
   */
  void setReproducibility(String Reproducibility);

  /**
   * @return notes value
   */
  String getNotes();

  /**
   * @param pNotes
   *          represents the notes to set
   */
  void setNotes(final String pNotes);

  /**
   * @return atachements
   */
  List<Attachment> getAttachments();

  /**
   * @param pAttachments
   *          represents the attachments to set
   */
  void setAttachments(List<Attachment> pAttachments);
}