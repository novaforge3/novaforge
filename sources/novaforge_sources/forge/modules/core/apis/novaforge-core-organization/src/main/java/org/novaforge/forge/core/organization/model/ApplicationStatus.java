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
package org.novaforge.forge.core.organization.model;

/**
 * This enum describes the application's status available.
 * 
 * @author Guillaume Lamirand
 */
public enum ApplicationStatus
{
  /**
   * Define an application which has been providing and which not creating yet
   */
  CREATE_IN_PROGRESS,
  /**
   * Define an application which not deleting yet
   */
  DELETE_IN_PROGRESS,
  /**
   * Define an application which is waiting to be provided
   */
  PROVIDING_PENDING,
  /**
   * Define an application which has been created but with errors
   */
  CREATE_ON_ERROR,
  /**
   * Define an application which has been deleted but with errors
   */
  DELETE_ON_ERROR,
  /**
   * Define an application which has been successfuly created
   */
  ACTIVE
}
