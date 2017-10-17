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
import java.util.UUID;

/**
 * This interface describes an composition between two application
 * 
 * @author Guillaume Lamirand
 * @see User
 * @see Group
 */
public interface Composition extends Serializable
{

  /**
   * Retrieve composition name
   * 
   * @return the uuid value
   */
  UUID getUUID();

  /**
   * Set uuid value
   * 
   * @param pUUID
   *          the uuid to set
   */
  void setUUID(final UUID pUUID);

  /**
   * Retrieve {@link Project} associated to this composition
   * 
   * @return {@link Project} object
   */
  Project getProject();

  /**
   * Retrieve composition type
   * 
   * @return the {@link CompositionType} of the current composition
   */
  CompositionType getType();

  /**
   * Set type value
   * 
   * @param pType
   *          represents the type to set
   */
  void setType(final CompositionType pType);

  /**
   * Retrieve source {@link ProjectApplication}
   * 
   * @return the source
   */
  ProjectApplication getSource();

  /**
   * Set the source application
   *
   * @param pSource
   */
  void setSource(ProjectApplication pSource);

  /**
   * Retrieve source name
   *
   * @return the name of the source association
   */
  String getSourceName();

  /**
   * Set source value
   *
   * @param pName
   *          the name to set
   */
  void setSourceName(final String pName);

  /**
   * Retrieve target {@link ProjectApplication}
   *
   * @return the target
   */
  ProjectApplication getTarget();

  /**
   * Set the target application
   *
   * @param pTarget
   */
  void setTarget(ProjectApplication pTarget);

  /**
   * Retrieve target name
   *
   * @return the name of the target association
   */
  String getTargetName();

  /**
   * Set target value
   *
   * @param pName
   */
  void setTargetName(final String pName);

  /**
   * Retrieve template value
   *
   * @return the template
   */
  String getTemplate();

  /**
   * Set template value
   *
   * @param pTemplate
   *          the template to set
   */
  void setTemplate(final String pTemplate);

  /**
   * Return activation value
   *
   * @return true is composition is activated
   */
  boolean isActivated();

  /**
   * Define if composition is activated or not
   *
   * @param pActivated
   *          the value of activated boolean
   */
  void setActivated(boolean pActivated);

}