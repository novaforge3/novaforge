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
 * This is the behavior of a forge membership : it describes the role of a user into a project (ternary
 * association)
 * 
 * @author sbenoist
 */
public interface Membership
{
  /**
   * this method returns the actor of the membership
   * 
   * @return User
   */
  Actor getActor();

  /**
   * this method allows to set the actor of the membership
   * 
   * @param user
   */
  void setActor(Actor actor);

  /**
   * this method returns the role of the membership
   * 
   * @return Role
   */
  ProjectRole getRole();

  /**
   * this method allows to set the role of the membership
   * 
   * @param role
   */
  void setRole(ProjectRole role);

  /**
   * this method returns the project of the membership
   *
   * @return
   */
  Project getProject();

  /**
   * this method allows to set the project of the membership
   *
   * @param project
   */
  void setProject(Project project);

  /**
   * this method returns the priority of the membership
   *
   * @return boolean
   */
  boolean getPriority();

  /**
   * this method allows to set the priority of the membership
   *
   * @param priority
   */
  void setPriority(boolean priority);

}
