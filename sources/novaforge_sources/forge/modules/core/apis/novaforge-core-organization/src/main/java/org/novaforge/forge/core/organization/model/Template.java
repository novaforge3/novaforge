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

import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;

import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public interface Template extends ProjectElement
{

  /**
   * This method returns the template's id
   * 
   * @return String
   */
  String getTemplateId();

  /**
   * This method allows to set the id of the template
   * 
   * @param pTemplateId
   */
  void setTemplateId(final String pTemplateId);

  /**
   * This method returns the template's status
   * 
   * @return TemplateProjectStatus
   */
  TemplateProjectStatus getStatus();

  /**
   * This method sets the template's status
   * 
   * @param pStatus
   *          the status to set
   */
  void setStatus(final TemplateProjectStatus pStatus);

  /**
   * This will return the roles list
   * 
   * @return a {@link List} of {@link Role}
   */
  List<Role> getRoles();

  /**
   * This method adds a role to this tempalte
   * 
   * @param pRole
   *          the role to add
   */
  void removeRole(final Role pRole);

  /**
   * This method removes a role to this tempalte
   * 
   * @param pRole
   *          the removes to add
   */
  void addRole(final Role pRole);

}