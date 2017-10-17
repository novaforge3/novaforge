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
package org.novaforge.forge.core.organization.services;

import org.novaforge.forge.core.organization.exceptions.GroupServiceException;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.GroupInfo;

import java.util.List;
import java.util.UUID;

/**
 * @author sbenoist
 */
public interface GroupService
{
  Group newGroup();

  /**
   * Creates a group
   * 
   * @param pGroup
   * @param pProjectId
   * @param pAuthor
   * @throws GroupServiceException
   */
  void createGroup(final Group pGroup, final String pProjectId) throws GroupServiceException;

  /**
   * Gets a group of a project
   * 
   * @param pProjectId
   * @param pName
   * @return {@link Group} found
   * @throws GroupServiceException
   */
  Group getGroup(final String pProjectId, final String pName) throws GroupServiceException;

  /**
   * Updates a group of a project by an author
   * 
   * @param pProjectId
   * @param pOldName
   * @param pGroup
   * @param pCurrentUser
   * @throws GroupServiceException
   */
  void updateGroup(final String pProjectId, final String pOldName, final Group pGroup, String pCurrentUser)
      throws GroupServiceException;

  /**
   * Deletes a group of a project by an author
   * 
   * @param pProjectId
   * @param pGroupUUID
   * @return {@link Group} deleted
   * @throws GroupServiceException
   */
  Group deleteGroup(final String pProjectId, final UUID pGroupUUID) throws GroupServiceException;

  /**
   * Gets all groups of a project
   * 
   * @param projectId
   * @param pWithPublic
   * @return the list of group for the project + public group if pWithPublic equals true
   * @throws GroupServiceException
   */
  List<Group> getAllGroups(final String projectId, final boolean pWithPublic) throws GroupServiceException;

  /**
   * @param pUserUUID
   *          the user UUID
   * @return the list of groups the user is member
   * @throws GroupServiceException
   */
  List<GroupInfo> getAllUserGroups(UUID pUserUUID) throws GroupServiceException;
}
