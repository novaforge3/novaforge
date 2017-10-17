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
/**
 * 
 */
package org.novaforge.forge.core.organization.dao;

import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.GroupInfo;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.UUID;

/**
 * This class defines methods to access to {@link Group} data from persistence
 * 
 * @author BILET-JC
 * @author Guillaume Lamirand
 */
public interface GroupDAO
{
  /**
   * This method will return a new entity detach of persistence context
   * 
   * @return new {@link Group}
   */
  Group newGroup();

  /**
   * Find a {@link Group} by its project reference and its name
   * 
   * @param pProjectId
   *          the project reference
   * @param pName
   *          the group name
   * @return {@link Group} found
   * @throws NoResultException
   *           thrown if no {@link Group} are existing for the login given
   */
  Group findByProjectIdAndName(final String pProjectId, final String pName) throws NoResultException;

  /**
   * Find a {@link Group} by its uuid
   * 
   * @param pUUID
   *          the uuid to search
   * @return {@link Group} found
   * @throws NoResultException
   *           thrown if no {@link Group} are existing for the uuid given
   */
  Group findByUUID(final UUID pUUID) throws NoResultException;

  /**
   * Find {@link Group} list regarding visibility
   * 
   * @param pVisibility
   *          the visiblity used to retrieve groups
   * @return {@link List} of {@link Group} with the visibility required
   */
  List<Group> findByVisibility(final boolean pVisibility);

  /**
   * Check if a {@link Group} with the given name is existing on the project given
   * 
   * @param pProjectId
   *          the project reference
   * @param pName
   *          the group name
   * @return <code>true</code> if a {@link Group} exists for the parameters given, <code>false</code>
   *         otherwise
   */
  boolean existGroup(final String pProjectId, final String pName);

  /**
   * Check if a {@link Group} with the given uuid is existing
   * 
   * @param pUUID
   *          the uuid to ssek group
   * @return <code>true</code> if a {@link Group} exists for the parameter given, <code>false</code> otherwise
   */
  boolean existGroup(final UUID pUUID);

  /**
   * This method returns all the groups in which the user belongs
   * 
   * @param pUserUUID
   *          the user unique identifier used for the research
   * @return {@link List} of {@link Group} in which the user belongs
   */
  List<Group> findGroupsForUser(final UUID pUserUUID);

  /**
   * This method returns all the groups with their prinformations in which the user belongs
   * 
   * @param pUserUUID
   *          the user unique identifier used for the research
   * @return {@link List} of {@link GroupInfo} in which the user belongs
   */
  List<GroupInfo> findGroupsInfosForUser(final UUID pUserUUID);

  /**
   * This method returns all a list of {@link Group#getUuid()} in which the user belongs
   * 
   * @param pUserUUID
   *          the user unique identifier used for the research
   * @return {@link List} of {@link Group}'s {@link UUID} in which the user belongs
   */
  List<UUID> findGroupsUUIDForUser(final UUID pUserUUID);

  /**
   * This method will returns all groups for a project, ie group attach to the project id given and public
   * group attach to another project
   * 
   * @param pProjectId
   *          the project id used to seek groups
   * @return {@link List} of {@link Group} link to the project id given
   */
  List<Group> findByProjectWithPublic(final String pProjectId);

  /**
   * This method will returns groups for a project, ie group attach to the project id given without public
   * group
   * 
   * @param pProjectId
   *          the project id used to seek groups
   * @return {@link List} of {@link Group} link to the project id given
   */
  List<Group> findByProjectWithoutPublic(final String pProjectId);

  /**
   * Will update the {@link Group} given into persistence context
   * 
   * @param pGroup
   *          the group to update
   * @return {@link Group} updated and attached to persistence context
   */
  Group update(final Group pGroup);

}
