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
package org.novaforge.forge.core.organization.dao;

import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MembershipRequestStatus;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This class defines methods to access to {@link Membership},{@link MembershipRequest} and
 * {@link MembershipInfo} data from persistence
 * 
 * @author sbenoist
 * @author BILET-JC
 * @author Guillaume Lamirand
 * @see Membership
 * @see MembershipInfo
 * @see MembershipRequest
 */
public interface MembershipDAO
{
  /**********************************************************
   * The following methods manage Membership data
   **********************************************************/
  /**
   * This method will return a new entity detach of persistence context
   * 
   * @param pActor
   *          represents the actor
   * @param pProject
   *          represents the project
   * @param pRole
   *          represents the role
   * @return new {@link Membership}
   */
  Membership newMembership(final Actor pActor, final Project pProject, final ProjectRole pRole);

  /**
   * Find list of {@link Membership} according a project id
   * 
   * @param pProjectId
   *          the project id used to retrieve membership
   * @return {@link List} of {@link Membership} associated to the project given, can contain either
   *         {@link User} or {@link Group}
   */
  List<Membership> findByProject(final String pProjectId);

  /**
   * Find list of {@link Membership} according to an actor's login and a project id
   * 
   * @param pProjectId
   *          the project id used to retrieve membership
   * @param pActorUUID
   *          the actor's uuid used to retrieve membership
   * @return {@link List} of {@link Membership} associated to the project and actor given, can contain either
   *         {@link User} or {@link Group}
   */
  List<Membership> findByProjectAndActor(final String pProjectId, final UUID pActorUUID);

  /**
   * Retrieve direct or indirect(throw a group) {@link MembershipInfo} for an {@link User} and a
   * {@link Project}
   * 
   * @param pProjectId
   *          the project id used to retrieve membership
   * @param pActorUUIDs
   *          list of actor's uuid used to retrieve membership
   * @return {@link List} of {@link MembershipInfo} only for {@link User}
   */
  List<MembershipInfo> findInfoByProjectAndActors(final String pProjectId, final UUID... pActorUUIDs);

  /**
   * Find list of {@link Membership} for {@link User} according to a project id
   * 
   * @param pProjectId
   *          the project id used to retrieve membership
   * @return {@link List} of {@link MembershipInfo} associated to the project given, can contain only
   *         {@link User}
   */
  List<MembershipInfo> findUsersInfoByProject(final String pProjectId);

  /**
   * Find list of {@link Membership} for {@link Group} according to a project id
   * 
   * @param pProjectId
   *          the project id used to retrieve membership
   * @return {@link List} of {@link MembershipInfo} associated to the project given, can contain only
   *         {@link Group}
   */
  List<MembershipInfo> findGroupsInfoByProject(final String pProjectId);

  /**********************************************************
   * The following methods manage Actor data
   **********************************************************/
  /**
   * This method allows to count the memberships of an {@link Actor}
   * 
   * @param pUUID
   *          actor's identifier
   * @return the nb of memberships for the given {@link Actor}
   */
  long countForActor(final UUID pUUID);

  /**
   * Find list of {@link Actor} according to a project id and a role name
   * 
   * @param pProjectId
   *          the project id used to retrieve actor
   * @param pRoleName
   *          the role name used to retrieve actor
   * @return {@link List} of {@link Actor} associated to the project and role name given, can contain either
   *         {@link User} or {@link Group}
   */
  List<Actor> findActorsByProjectAndRole(final String pProjectId, final String pRoleName);

  /**
   * Find list of {@link Actor} according to a project id
   * 
   * @param pProjectId
   *          the project id used to retrieve actor
   * @return {@link List} of {@link Actor} associated to the project given, can contain either {@link User} or
   *         {@link Group}
   */
  List<Actor> findActorsByProject(final String pProjectId);

  /**
   * Find list of {@link Actor} according to a rol ename
   * 
   * @param pRoleName
   *          the role name used to retrieve actor
   * @return {@link List} of {@link Actor} associated to role name given, can contain either {@link User} or
   *         {@link Group}
   */
  List<Actor> findActorsByRole(final String pRoleName);

  /**
   * Find map which contains for a list of {@link Actor} a list of {@link Role} by project
   * 
   * @param pGroupsUUIDs
   *          the actor's uuids
   * @return {@link Map} containing for a list of {@link Actor} a list of {@link Role} by project, can referer
   *         either {@link User} or {@link Group}
   */
  Map<String, List<ProjectRole>> findRolesByActors(final UUID... pGroupsUUIDs);

  /**********************************************************
   * The following methods manage ProjectElement data
   **********************************************************/

  /**
   * Find list of {@link Project} for which groups are member
   * 
   * @param pActorsUUIDs
   *          the actor's uuid used to retrieve projects
   * @return {@link List} of project id
   */
  List<String> findProjectsIdForActors(final UUID... pActorsUUIDs);

  /**
   * Find list of {@link Project} validated for which an {@link Group} is member
   * 
   * @param pActorsUUIDs
   *          the actor's uuid used to retrieve projects
   * @param pProjectOptions
   *          the options used to retrieve project details
   * @return {@link List} of {@link Project}
   */
  List<Project> findValidatedProjectsForActors(final ProjectOptions pProjectOptions,
      final UUID... pActorsUUIDs);

  /**
   * Find public {@link Project} on which on {@link Group} has no memberships
   * 
   * @param pActorsUUIDs
   *          the actor's uuid used to retrieve projects
   * @param pProjectOptions
   *          the options used to retrieve project details
   * @return {@link List} of public {@link Project}
   */
  List<Project> findPublicProjectsForActors(final ProjectOptions pProjectOptions, final UUID... pActorsUUIDs);

  /**
   * Will persist the {@link Membership} given from persistence context
   * 
   * @param pMembership
   *          the membership to persist
   * @return {@link Membership} persist and attach to persistence context
   */
  Membership persist(Membership pMembership);

  /**
   * Will delete the {@link Membership} given from persistence context
   * 
   * @param pMembership
   *          the membership to delete
   */
  void delete(final Membership pMembership);

  /**********************************************************
   * The following methods manage MembershipRequest data
   **********************************************************/
  /**
   * Instanciate a {@link MembershipRequest}
   * 
   * @return new {@link MembershipRequest}
   */
  MembershipRequest newRequest();

  /**
   * Return a {@link List} of {@link MembershipRequest} for a project id given in parameter.
   * 
   * @param pProjectId
   *          the project id used to retrieve request
   * @return {@link List} of {@link MembershipRequest} found
   */
  List<MembershipRequest> findAllRequestByProject(final String pProjectId);

  /**
   * Return a {@link List} of {@link MembershipRequest} for a user login given.
   * 
   * @param pUserLogin
   *          the user login used to retrieve request
   * @return {@link List} of {@link MembershipRequest} found
   */
  List<MembershipRequest> findAllRequestByUser(final String pUserLogin);

  /**
   * Return a {@link List} of {@link MembershipRequest} for a user login given.
   * 
   * @param pUserLogin
   *          the user login used to retrieve request
   * @param pStatus
   *          status requested
   * @return {@link List} of {@link MembershipRequest} found
   */
  List<MembershipRequest> findAllRequestByUserAndStatus(final String pUserLogin,
      final MembershipRequestStatus pStatus);

  /**
   * Return a {@link List} of {@link MembershipRequest} for a project id and user's login given in parameter.
   * 
   * @param pProjectId
   *          the project id used to retrieve request
   * @param pUserLogin
   *          the user login used to retrieve request
   * @return {@link List} of {@link MembershipRequest} found
   * @throws NoResultException
   *           thrown if no {@link MembershipRequest} are existing for the parameters given
   */
  MembershipRequest findRequestByProjectAndUser(final String pProjectId, final String pUserLogin)
      throws NoResultException;

  /**
   * This method returns {@link List} of {@link MembershipRequest} for a specific status given in parameter
   * 
   * @param pProjectId
   *          the project id used to retrieve request
   * @param pStatus
   *          represents request's {@link MembershipRequestStatus}
   * @return {@link List} of {@link MembershipRequest} found
   */
  List<MembershipRequest> findAllRequestByProjectAndStatus(final String pProjectId,
      final MembershipRequestStatus pStatus);

  /**
   * Will update the {@link MembershipRequest} given into persistence context
   * 
   * @param pMembershipRequest
   *          the request to update
   * @return {@link MembershipRequest} updated and attached to persistence context
   */
  MembershipRequest update(final MembershipRequest pMembershipRequest);

  /**
   * Will delete the {@link MembershipRequest} given into persistence context
   * 
   * @param pMembershipRequest
   *          the request to delete
   */
  void delete(MembershipRequest pMembershipRequest);

}
