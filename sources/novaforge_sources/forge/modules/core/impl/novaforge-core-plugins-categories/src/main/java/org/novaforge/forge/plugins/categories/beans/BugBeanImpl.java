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

import org.novaforge.forge.core.plugins.categories.FieldProperty;
import org.novaforge.forge.core.plugins.categories.bugtracker.BugTrackerIssueBean;
import org.novaforge.forge.core.plugins.categories.commons.Attachment;

import java.io.Serializable;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class BugBeanImpl implements BugTrackerIssueBean, Serializable
{

  private static final long serialVersionUID = -9184330189004372939L;
  @FieldProperty(type = String.class, size = 20, label_key = "bug.id.name", description_key = "bug.id.desc",
      example_key = "bug.id.example", required = false, id = true)
  private String            id;

  @FieldProperty(type = String.class, size = 128, label_key = "bug.title.name",
      description_key = "bug.title.desc", example_key = "bug.title.example", required = true)
  private String            title;

  @FieldProperty(type = String.class, size = 500, label_key = "bug.description.name",
      description_key = "bug.description.desc", example_key = "bug.description.example", required = true)
  private String            description;

  @FieldProperty(type = String.class, size = 500, label_key = "bug.addionnalinfo.name",
      description_key = "bug.addionnalinfo.desc", example_key = "bug.addionnalinfo.example")
  private String            addionnalInfo;

  @FieldProperty(type = String.class, size = 128, label_key = "bug.category.name",
      description_key = "bug.category.desc", example_key = "bug.category.example", required = true)
  private String            category;

  @FieldProperty(type = String.class, size = 500, label_key = "bug.notes.name",
      description_key = "bug.notes.desc", example_key = "bug.notes.example")
  private String            notes;

  @FieldProperty(type = Boolean.class, label_key = "bug.attachments.name",
      description_key = "bug.attachments.desc", example_key = "bug.attachments.example")
  private List<Attachment>  attachments;

  public BugBeanImpl()
  {
    // Default constructor
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId()
  {

    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setId(final String pId)
  {
    id = pId;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTitle()
  {

    return title;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTitle(final String pTitle)
  {
    title = pTitle;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription()
  {

    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDescription(final String pDescription)
  {

    description = pDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAdditionalInfo()
  {
    return addionnalInfo;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAdditionalInfo(final String pInfo)
  {
    addionnalInfo = pInfo;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCategory()
  {
    return category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCategory(final String category)
  {
    this.category = category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getReporter()
  {
    // Not implemented
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setReporter(final String pReporter)
  {
    // Not implemented

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSeverity()
  {
    // Not implemented
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSeverity(final String pSeverity)
  {
    // Not implemented
  }

  @Override
  public String getPriority()
  {
    // Not implemented
    return null;
  }

  @Override
  public void setPriority(final String arg0)
  {
    // Not implemented

  }

  @Override
  public String getResolution()
  {
    // Not implemented
    return null;
  }

  @Override
  public void setResolution(final String arg0)
  {
    // Not implemented

  }

  @Override
  public String getStatus()
  {
    // Not implemented
    return null;
  }

  @Override
  public void setStatus(final String arg0)
  {
    // Not implemented

  }

  @Override
  public String getAssignedTo()
  {
    // Not implemented
    return null;
  }

  @Override
  public void setAssignedTo(final String arg0)
  {
    // Not implemented

  }

  @Override
  public String getProductVersion()
  {
    // Not implemented
    return null;
  }

  @Override
  public void setProductVersion(final String arg0)
  {
    // Not implemented

  }

  @Override
  public String getTargetVersion()
  {
    // Not implemented
    return null;
  }

  @Override
  public void setTargetVersion(final String arg0)
  {
    // Not implemented

  }

  @Override
  public String getFixedInVersion()
  {
    // Not implemented
    return null;
  }

  @Override
  public void setFixedInVersion(final String arg0)
  {
    // Not implemented

  }

  @Override
  public String getReproducibility()
  {
    // Not implemented
    return null;
  }

  @Override
  public void setReproducibility(final String arg0)
  {
    // Not implemented

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

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "BugBeanImpl [id=" + id + ", title=" + title + ", description=" + description + ", addionnalInfo="
               + addionnalInfo + ", category=" + category + ", notes=" + notes + "]";
  }

}
