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
package org.novaforge.forge.tools.deliverymanager.dao;

import org.novaforge.forge.tools.deliverymanager.model.Membership;
import org.novaforge.forge.tools.deliverymanager.model.Project;
import org.novaforge.forge.tools.deliverymanager.model.Role;
import org.novaforge.forge.tools.deliverymanager.model.User;

import java.util.List;

/**
 * @author sbenoist
 */
public interface MembershipDAO
{
  /**
   * This method will return a new entity detach of persiste context
   * 
   * @param pUser
   * @param pProject
   * @param pRole
   * @return new {@link Membership}
   */
  Membership newMembership(User pUser, Project pProject, Role pRole);

  /**
   * This will persist {@link Membership} into persistence context
   * 
   * @param pMembership
   *          the Membership to persist
   * @return {@link Membership} persisted and attach to persistence context
   */
  Membership persist(Membership pMembership);

  /**
   * This will update {@link Membership} into persistence context
   * 
   * @param pMembership
   *          the Membership to update
   * @return {@link Membership} updated and attach to persistence context
   */
  Membership update(Membership pMembership);

  /**
   * This will delete {@link Membership} from persistence context
   * 
   * @param pMembership
   *          the Membership to delete
   */
  void delete(Membership pMembership);

  /**
   * This method returns the membership for all users on a specified project
   * 
   * @param pProjectId
   *          The projectId argued
   * @return {@link List} of {@link Membership}
   */
  List<Membership> findAllByProject(String pProjectId);

  /**
   * This method returns all the memberships for a user
   * 
   * @param pLogin
   *          The login argued
   * @return {@link List} of {@link Membership}
   */
  List<Membership> findAllByUser(String pLogin);

  /**
   * This method returns the membership for a project and a user
   * 
   * @param pLogin
   * @param pProjectId
   * @return {@link Membership}
   */
  Membership findByUserAndProject(String pLogin, String pProjectId);

  /**
   * This method returns true if any membership exists for a user and a project
   * 
   * @param pLogin
   * @param pProjectId
   * @return true if any membership exists for the user and the project
   */
  boolean exist(String pLogin, String pProjectId);
}
