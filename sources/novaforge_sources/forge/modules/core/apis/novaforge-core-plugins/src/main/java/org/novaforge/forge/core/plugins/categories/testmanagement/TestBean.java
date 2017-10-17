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
package org.novaforge.forge.core.plugins.categories.testmanagement;

import org.novaforge.forge.core.plugins.categories.PluginExchangeableBean;
import org.novaforge.forge.core.plugins.categories.commons.Attachment;

import java.util.List;

/**
 * @author lamirang
 */
public interface TestBean extends PluginExchangeableBean
{

  /**
   * @return id value
   */
  String getId();

  /**
   * @param pId
   *          represents the id to set
   */
  void setId(String pId);

  /**
   * @return title value
   */
  String getTitle();

  /**
   * @param pTitle
   *          represents the title to set
   */
  void setTitle(String pTitle);

  /**
   * @return status value
   */
  String getStatus();

  /**
   * @param pStatus
   *          represents the status to set
   */
  void setStatus(String pStatus);

  /**
   * @return test plan
   */
  String getTestPlan();

  /**
   * @param pTestPlan
   *          represents the test plan to set
   */
  void setTestPlan(String pTestPlan);

  /**
   * @return build
   */
  String getBuild();

  /**
   * @param pBuild
   *          represents the build to set
   */
  void setBuild(String pBuild);

  /**
   * @return plateform
   */
  String getPlateform();

  /**
   * @param pPlateform
   *          represents the platform to set
   */
  void setPlateform(String pPlateform);

  /**
   * @return summary
   */
  String getSummary();

  /**
   * @param pSummary
   *          represents the summary to set
   */
  void setSummary(String pSummary);

  /**
   * @return notes
   */
  String getNotes();

  /**
   * @param pNotes
   *          represents the notes to set
   */
  void setNotes(String pNotes);

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