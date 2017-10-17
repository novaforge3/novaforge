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

import java.util.Date;

/**
 * Generic Bean interface which represents the minimum attributes of a Task.
 */
public interface TaskInfoBean extends PluginExchangeableBean
{
  /**
   * Get the id
   * 
   * @return the id
   */
  String getId();

  /**
   * Set the id
   * 
   * @param id
   *          the id to set
   */
  void setId(String id);

  /**
   * Get the title
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
  void setTitle(String title);

  /**
   * Get the description
   * 
   * @return the description
   */
  String getDescription();

  /**
   * Set the description
   * 
   * @param description
   *          the description to set
   */
  void setDescription(String description);

  /**
   * Get the type
   * 
   * @return the type
   */
  TaskTypeEnum getType();

  /**
   * Set the type
   * 
   * @param type
   *          the type to set
   */
  void setType(TaskTypeEnum type);

  /**
   * Get the lastUpdateDate
   * 
   * @return the lastUpdateDate
   */
  Date getLastUpdateDate();

  /**
   * Set the lastUpdateDate
   * 
   * @param lastUpdateDate
   *          the lastUpdateDate to set
   */
  void setLastUpdateDate(Date lastUpdateDate);

  /**
   * Get the status
   * 
   * @return the status
   */
  TaskStatusEnum getStatus();

  /**
   * Set the status
   * 
   * @param status
   *          the status to set
   */
  void setStatus(TaskStatusEnum status);

  /**
   * Get the startDate
   * 
   * @return the startDate
   */
  Date getStartDate();

  /**
   * Set the startDate
   * 
   * @param startDate
   *          the startDate to set
   */
  void setStartDate(Date startDate);

  /**
   * Get the endDate
   * 
   * @return the endDate
   */
  Date getEndDate();

  /**
   * Set the endDate
   * 
   * @param endDate
   *          the endDate to set
   */
  void setEndDate(Date endDate);

  /**
   * Get the initialEstimation
   * 
   * @return the initialEstimation
   */
  float getInitialEstimation();

  /**
   * Set the initialEstimation
   * 
   * @param initialEstimation
   *          the initialEstimation to set
   */
  void setInitialEstimation(float initialEstimation);

}
