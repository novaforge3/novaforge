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
 * Use to filter the request for projects
 * 
 * @author Guillaume Lamirand
 */
public interface ProjectOptions
{

  /**
   * Defines if the method should return the system project as forge project, reference project
   * 
   * @return true to retrieve system project also, false otherwise
   */
  boolean isRetrievedSystem();

  /**
   * Set if the method associated should return the system projects
   *
   * @param pSystem
   *          the system to set
   * @return itself
   */
  ProjectOptions setRetrievedSystem(boolean pSystem);

  /**
   * Defines if the method should return the organization entity attached to the project
   *
   * @return true to retrieve organization, false otherwise
   */
  boolean isRetrievedOrganization();

  /**
   * Set if the method associated should return the organization entity attached to the project
   *
   * @param pOrganization
   *          the organization to set
   * @return itself
   */
  ProjectOptions setRetrievedOrganization(boolean pOrganization);

  /**
   * Defines if the method should return the image binary entity attached to the project
   *
   * @return true to retrieve image binary, false otherwise
   */
  boolean isRetrievedImage();

  /**
   * Set if the method associated should return the image binary entity
   *
   * @param pImage
   *          the image to set
   * @return itself
   */
  ProjectOptions setRetrievedImage(boolean pImage);
}
