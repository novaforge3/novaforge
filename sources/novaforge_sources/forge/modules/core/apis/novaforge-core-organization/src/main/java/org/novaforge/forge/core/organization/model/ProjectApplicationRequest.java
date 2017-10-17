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

import java.util.Date;
import java.util.Map;

/**
 * Defines a application which has been requested by an user
 * 
 * @author Guillaume Lamirand
 */
public interface ProjectApplicationRequest
{
  /**
   * This will return the {@link Project} related to this request
   * 
   * @return {@link Project} related
   */
  Project getProject();

  /**
   * This method allows to set the project linked to the request
   *
   * @param pProject
   */
  void setProject(Project pProject);

  /**
   * This will return the {@link ProjectApplication} related to this request
   *
   * @return {@link ProjectApplication} related
   */
  ProjectApplication getApplication();

  /**
   * This method allows to set the application linked to the request
   *
   * @param pApp
   */
  void setApplication(ProjectApplication pApp);

  /**
   * This method allows to get the role mapping for the application
   *
   * @return roles mapping declared by the user
   */
  Map<String, String> getRolesMapping();

  /**
   * @param pRoleMapping
   */
  void setRolesMapping(Map<String, String> pRoleMapping);

  /**
   * This method returns the created date
   *
   * @return create {@link Date}
   */
  Date getCreated();

  /**
   * @return the login of the user who mades the request
   */
  String getLogin();

  /**
   * @param pLogin
   */
  void setLogin(String pLogin);
}
