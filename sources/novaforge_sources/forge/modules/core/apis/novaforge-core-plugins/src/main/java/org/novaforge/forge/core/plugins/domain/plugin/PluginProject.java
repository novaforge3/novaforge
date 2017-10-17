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
package org.novaforge.forge.core.plugins.domain.plugin;

import java.io.Serializable;

/**
 * This interface describe an project which will be used during communication between forge core and a plugin
 * 
 * @author lamirang
 */
public interface PluginProject extends Serializable
{

  /**
   * Return the project Id
   * 
   * @return the projectId
   */
  String getProjectId();

  /**
   * That allows to set forge project Id
   * 
   * @param pProjectId
   *          the projectId to set
   */
  void setProjectId(String pProjectId);

  /**
   * Return the project name
   * 
   * @return the name
   */
  String getName();

  /**
   * That allows to set project name
   * 
   * @param pName
   *          the name to set
   */
  void setName(String pName);

  /**
   * Return project's description
   * 
   * @return the description
   */
  String getDescription();

  /**
   * That allows to set project description
   * 
   * @param pDescription
   *          the description to set
   */
  void setDescription(String pDescription);

  /**
   * Return license name
   * 
   * @return the license
   */
  String getLicense();

  /**
   * That allows to set license name
   * 
   * @param pLicense
   *          the license to set
   */
  void setLicense(String pLicense);

  /**
   * Return project status
   * 
   * @return the status
   */
  String getStatus();

  /**
   * That allows to set project status
   * 
   * @param pStatus
   *          the status to set
   */
  void setStatus(String pStatus);

  /**
   * Return project author
   * 
   * @return the author
   */
  String getAuthor();

  /**
   * That allows to set project author
   * 
   * @param pAuthor
   *          the author to set
   */
  void setAuthor(String pAuthor);

}