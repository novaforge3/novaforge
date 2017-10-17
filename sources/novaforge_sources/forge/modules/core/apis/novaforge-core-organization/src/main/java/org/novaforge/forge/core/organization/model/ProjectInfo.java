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

import java.io.Serializable;
import java.util.Date;

/**
 * Represents a Project "lite" informaion set used to display some information when the user does not have the
 * "read" access for this project.
 * 
 * @author sbenoist
 */
public interface ProjectInfo extends Serializable
{

  /**
   * This method returns the projectId of the project
   * 
   * @return String
   */
  String getProjectId();

  /**
   * This method allows to set the projectId of the project
   * 
   * @param projectId
   */
  void setProjectId(String projectId);

  /**
   * This method returns the name of the project
   * 
   * @return String
   */
  String getName();

  /**
   * This method allows to set the name of the project
   * 
   * @param name
   */
  void setName(String name);

  /**
   * This method returns the description of the project
   * 
   * @return String
   */
  String getDescription();

  /**
   * This method allows to set the description of the project
   * 
   * @param description
   */
  void setDescription(String description);

  /**
   * This method returns the licence of the project
   * 
   * @return String
   */
  String getLicenceType();

  /**
   * This method allows to set the licence of the project
   * 
   * @param licenceType
   */
  void setLicenceType(String licenceType);

  /**
   * This method returns the date of the last modification of the project
   * 
   * @return Date
   */
  Date getLastModified();

  /**
   * This method allows to set the date of the last modification of the project
   * 
   * @return Date
   */
  void setLastModified(Date date);

}
