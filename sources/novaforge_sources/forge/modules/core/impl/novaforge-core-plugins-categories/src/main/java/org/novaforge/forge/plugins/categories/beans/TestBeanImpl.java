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
import org.novaforge.forge.core.plugins.categories.commons.Attachment;
import org.novaforge.forge.core.plugins.categories.testmanagement.TestBean;

import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class TestBeanImpl implements TestBean
{
  /**
    * 
    */
  private static final long serialVersionUID = 5377784076628327894L;
  @FieldProperty(type = String.class, size = 20, label_key = "test.id.name",
      description_key = "test.id.desc", example_key = "test.id.example")
  private String            id;

  @FieldProperty(type = String.class, size = 100, label_key = "test.title.name",
      description_key = "test.title.desc", example_key = "test.title.example")
  private String            title;

  @FieldProperty(type = String.class, size = 20, label_key = "test.status.name",
      description_key = "test.status.desc", example_key = "test.status.example")
  private String            status;

  @FieldProperty(type = String.class, size = 100, label_key = "test.plateform.name",
      description_key = "test.plateform.desc", example_key = "test.plateform.example")
  private String            plateForm;

  @FieldProperty(type = String.class, size = 100, label_key = "test.build.name",
      description_key = "test.build.desc", example_key = "test.build.example")
  private String            build;

  @FieldProperty(type = String.class, size = 500, label_key = "test.summary.name",
      description_key = "test.summary.desc", example_key = "test.summary.example")
  private String            summary;

  @FieldProperty(type = String.class, size = 500, label_key = "test.notes.name",
      description_key = "test.notes.desc", example_key = "test.notes.example")
  private String            notes;

  @FieldProperty(type = String.class, size = 100, label_key = "test.testPlan.name",
      description_key = "test.testPlan.desc", example_key = "test.testPlan.example")
  private String            testPlan;

  @FieldProperty(type = Boolean.class, label_key = "test.attachments.name",
      description_key = "test.attachments.desc", example_key = "test.attachments.example")
  private List<Attachment>  attachments;

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
  public String getStatus()
  {
    return status;
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
  public String getTestPlan()
  {
    return testPlan;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTestPlan(final String pTestPlan)
  {
    testPlan = pTestPlan;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getBuild()
  {
    return build;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBuild(final String pBuild)
  {
    build = pBuild;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getPlateform()
  {
    return plateForm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setPlateform(final String pPlateform)
  {
    plateForm = pPlateform;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getSummary()
  {
    return summary;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSummary(final String pSummary)
  {
    summary = pSummary;
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
    return "TestBeanImpl [id=" + id + ", title=" + title + ", status=" + status + ", plateForm=" + plateForm
               + ", build=" + build + ", summary=" + summary + ", notes=" + notes + ", testPlan=" + testPlan + "]";
  }

}
