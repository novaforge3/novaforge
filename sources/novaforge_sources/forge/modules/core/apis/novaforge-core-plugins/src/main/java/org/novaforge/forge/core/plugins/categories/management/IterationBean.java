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
 * An iteration is a period of time containing tasks to realize
 */
public interface IterationBean extends PluginExchangeableBean
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
   * Get the iterationNumber
   * 
   * @return the iterationNumber
   */
  long getIterationNumber();

  /**
   * Set the iterationNumber
   * 
   * @param iterationNumber
   *          the iterationNumber to set
   */
  void setIterationNumber(long iterationNumber);

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

}
