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
package org.novaforge.forge.plugins.categories.beans;

import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.categories.commons.Attachment;

import java.util.List;

/**
 * @author sbenoist
 */
public class BugTrackerIssueBeanImpl implements BugTrackerIssueBean
{
  /**
   * Serial Version Id for serialization
   */
  private static final long serialVersionUID = 4970614567314261341L;

  /**
   * The Id of issue
   * 
   * @see BugTrackerIssueBeanImpl#getId()
   * @see BugTrackerIssueBeanImpl#setId(String)
   */
  private String            id;

  /**
   * The title of issue
   * 
   * @see BugTrackerIssueBeanImpl#getTitle()
   * @see BugTrackerIssueBeanImpl#setTitle(String)
   */
  private String            title;

  /**
   * The description of issue
   * 
   * @see BugTrackerIssueBeanImpl#getDescription()
   * @see BugTrackerIssueBeanImpl#setDescription(String)
   */
  private String            description;

  /**
   * The additionalInfo of issue
   * 
   * @see BugTrackerIssueBeanImpl#getAdditionalInfo()
   * @see BugTrackerIssueBeanImpl#setAdditionalInfo(String)
   */
  private String            additionalInfo;

  /**
   * The category of issue
   * 
   * @see BugTrackerIssueBeanImpl#getCategory()
   * @see BugTrackerIssueBeanImpl#setCategory(String)
   */
  private String            category;

  /**
   * The reporter of issue
   * 
   * @see BugTrackerIssueBeanImpl#getReporter()
   * @see BugTrackerIssueBeanImpl#setReporter(String)
   */
  private String            reporter;
  /**
   * The severity of issue
   * 
   * @see BugTrackerIssueBeanImpl#getSeverity()
   * @see BugTrackerIssueBeanImpl#setSeverity(String)
   */
  private String            severity;

  private String            assignedTo;

  private String            priority;

  private String            resolution;

  private String            status;

  private String            fixedInVersion;
  private String            productVersion;
  private String            reproducibility;
  private String            targetVersion;
  private List<Attachment>  attachments;
  /**
   * The notes of issue
   * 
   * @see BugTrackerIssueBeanImpl#getNotes()
   * @see BugTrackerIssueBeanImpl#setNotes(String)
   */
  private String            notes;

  /**
   * {@inheritDoc}
   *
   * @see BugTrackerIssueBeanImpl#id
   */
  @Override
  public String getId()
  {
    return id;
  }

  @Override
  public String getFixedInVersion()
  {
    return fixedInVersion;
  }

  /**
   * {@inheritDoc}
   *
   * @see BugTrackerIssueBeanImpl#title
   */
  @Override
  public void setId(final String pId)
  {
    id = pId;
  }

  @Override
  public String getProductVersion()
  {
    return productVersion;
  }

  /**
   * {@inheritDoc}
   *
   * @see BugTrackerIssueBeanImpl#title
   */
  @Override
  public String getTitle()
  {
    return title;
  }

  @Override
  public String getReproducibility()
  {
    return reproducibility;
  }

  /**
   * {@inheritDoc}
   *
   * @see BugTrackerIssueBeanImpl#title
   */
  @Override
  public void setTitle(final String pTitle)
  {
    title = pTitle;
  }

  @Override
  public String getTargetVersion()
  {
    return targetVersion;
  }

  /**
   * {@inheritDoc}
   *
   * @see BugTrackerIssueBeanImpl#description
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  @Override
  public void setFixedInVersion(final String pFixedInVersion)
  {
    fixedInVersion = pFixedInVersion;

  }

  /**
   * {@inheritDoc}
   *
   * @see BugTrackerIssueBeanImpl#description
   */
  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  @Override
  public void setProductVersion(final String pProductVersion)
  {
    productVersion = pProductVersion;
  }

  /**
   * {@inheritDoc}
   *
   * @see BugTrackerIssueBeanImpl#additionalInfo
   */
  @Override
  public String getAdditionalInfo()
  {
    return additionalInfo;
  }

  @Override
  public void setReproducibility(final String pReproducibility)
  {
    reproducibility = pReproducibility;
  }

  /**
   * {@inheritDoc}
   *
   * @see BugTrackerIssueBeanImpl#additionalInfo
   */
  @Override
  public void setAdditionalInfo(final String pInfo)
  {
    additionalInfo = pInfo;
  }

  @Override
  public void setTargetVersion(final String pTargetVersion)
  {
    targetVersion = pTargetVersion;
  }

  /**
   * {@inheritDoc}
   * 
   * @see BugTrackerIssueBeanImpl#category
   */
  @Override
  public String getCategory()
  {
    return category;
  }

  /**
   * {@inheritDoc}
   * 
   * @see BugTrackerIssueBeanImpl#category
   */
  @Override
  public void setCategory(final String pCategory)
  {
    category = pCategory;
  }

  /**
   * {@inheritDoc}
   * 
   * @see BugTrackerIssueBeanImpl#reporter
   */
  @Override
  public void setReporter(final String pReporter)
  {
    reporter = pReporter;
  }

  /**
   * {@inheritDoc}
   * 
   * @see BugTrackerIssueBeanImpl#reporter
   */
  @Override
  public String getReporter()
  {
    return reporter;
  }

  /**
   * {@inheritDoc}
   * 
   * @see BugTrackerIssueBeanImpl#severity
   */
  @Override
  public void setSeverity(final String pSeverity)
  {
    severity = pSeverity;
  }

  /**
   * {@inheritDoc}
   * 
   * @see BugTrackerIssueBeanImpl#severity
   */
  @Override
  public String getSeverity()
  {
    return severity;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAssignedTo()
  {
    return assignedTo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPriority()
  {
    return priority;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getResolution()
  {
    return resolution;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAssignedTo(final String pAssignedTo)
  {
    assignedTo = pAssignedTo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPriority(final String pPriority)
  {
    priority = pPriority;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setResolution(final String pResolution)
  {
    resolution = pResolution;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final String pStatus)
  {
    status = pStatus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getNotes()
  {
    return notes;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setNotes(final String pNotes)
  {
    notes = pNotes;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Attachment> getAttachments()
  {
    return attachments;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAttachments(final List<Attachment> pAttachments)
  {
    attachments = pAttachments;

  }
}
