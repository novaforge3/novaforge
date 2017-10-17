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
package org.novaforge.forge.core.organization.internal.dao;

import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.novaforge.forge.core.organization.dao.MembershipDAO;
import org.novaforge.forge.core.organization.entity.MembershipEntity;
import org.novaforge.forge.core.organization.entity.MembershipRequestEntity;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.enumerations.MembershipRequestStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * JPA2 implementation of {@link MembershipDAO}
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see MembershipEntity
 * @see MembershipRequestEntity
 */
public class MembershipDAOImpl implements MembershipDAO
{

  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager entityManager;

  /**
   * Use by container to inject {@link EntityManager}
   * 
   * @param pEntityManager
   *          the entityManager to set
   */
  public void setEntityManager(final EntityManager pEntityManager)
  {
    entityManager = pEntityManager;
  }

  /**********************************************************
   * The following methods manage Membership data
   **********************************************************/
  /**
   * {@inheritDoc}
   */
  @Override
  public Membership newMembership(final Actor actor, final Project project, final ProjectRole role)
  {
    return new MembershipEntity(actor, role, project, false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Membership> findByProject(final String pProjectId)
  {
    final TypedQuery<Membership> q = entityManager.createNamedQuery("MembershipEntity.findByProject",
        Membership.class);
    q.setParameter("projectId", pProjectId);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Membership> findByProjectAndActor(final String pProjectId, final UUID pActorUUID)
  {
    final List<Membership> returnList = new ArrayList<Membership>();
    if (pActorUUID != null)
    {
      final TypedQuery<Membership> q = entityManager.createNamedQuery(
          "MembershipEntity.findByProjectAndActor", Membership.class);
      q.setParameter("uuid", pActorUUID.toString());
      q.setParameter("projectId", pProjectId);
      final List<Membership> resultList = q.getResultList();
      if (resultList != null)
      {
        returnList.addAll(resultList);
      }
    }
    return returnList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipInfo> findInfoByProjectAndActors(final String pProjectId, final UUID... pActorsUUIDs)

  {
    final List<MembershipInfo> returnList = new ArrayList<MembershipInfo>();
    if ((pActorsUUIDs != null) && (pActorsUUIDs.length > 0))
    {
      final TypedQuery<MembershipInfo> q = entityManager.createNamedQuery(
          "MembershipEntity.findInfoByProjectAndActors", MembershipInfo.class);
      q.setParameter("uuids", changeToStrings(pActorsUUIDs));
      q.setParameter("projectId", pProjectId);
      final List<MembershipInfo> resultList = q.getResultList();
      if (resultList != null)
      {
        returnList.addAll(resultList);
      }
    }

    return returnList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipInfo> findUsersInfoByProject(final String pProjectId)
  {
    final TypedQuery<MembershipInfo> query = entityManager.createNamedQuery(
        "MembershipEntity.findUsersInfoByProject", MembershipInfo.class);
    query.setParameter("projectId", pProjectId);
    return query.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipInfo> findGroupsInfoByProject(final String pProjectId)
  {
    final TypedQuery<MembershipInfo> query = entityManager.createNamedQuery(
        "MembershipEntity.findGroupsInfoByProject", MembershipInfo.class);
    query.setParameter("projectId", pProjectId);
    return query.getResultList();
  }

  /**********************************************************
   * The following methods manage Actor data
   **********************************************************/
  /**
   * {@inheritDoc}
   */
  @Override
  public long countForActor(final UUID pUUID)
  {
    long count = 0;

    if (pUUID != null)
    {
      final TypedQuery<Long> q = entityManager.createNamedQuery("MembershipEntity.countForActor", Long.class);
      q.setParameter("uuid", pUUID.toString());
      count = q.getSingleResult();
    }
    return count;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Actor> findActorsByProjectAndRole(final String pProjectId, final String pRoleName)

  {
    final TypedQuery<Actor> q = entityManager.createNamedQuery("MembershipEntity.findActorsByRoleAndProject",
        Actor.class);
    q.setParameter("projectId", pProjectId);
    q.setParameter("roleName", pRoleName);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Actor> findActorsByProject(final String pProjectId)
  {
    final TypedQuery<Actor> q = entityManager.createNamedQuery("MembershipEntity.findActorsByProject",
        Actor.class);
    q.setParameter("projectId", pProjectId);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Actor> findActorsByRole(final String pRoleName)
  {
    final TypedQuery<Actor> q = entityManager.createNamedQuery("MembershipEntity.findActorsByRole",
        Actor.class);
    q.setParameter("roleName", pRoleName);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, List<ProjectRole>> findRolesByActors(final UUID... pActorsUUIDs)
  {
    final Map<String, List<ProjectRole>> returnMap = new HashMap<String, List<ProjectRole>>();
    if ((pActorsUUIDs != null) && (pActorsUUIDs.length > 0))
    {
      final TypedQuery<Object[]> q = entityManager.createNamedQuery("MembershipEntity.findRolesByActors",
          Object[].class);
      // Needed to retrieve also project's image on this request
      final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
      openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("role_permission");
      q.setParameter("uuids", changeToStrings(pActorsUUIDs));
      final List<Object[]> resultList = q.getResultList();
      returnMap.putAll(buildReturnMap(resultList));
    }
    return returnMap;
  }

  private Map<String, List<ProjectRole>> buildReturnMap(final List<Object[]> pResultList)
  {
    final Map<String, List<ProjectRole>> returnMap = new HashMap<String, List<ProjectRole>>();
    for (final Object[] object : pResultList)
    {
      final String projectId = (String) object[0];
      final ProjectRole role = (ProjectRole) object[1];
      List<ProjectRole> list = null;
      if (returnMap.containsKey(projectId))
      {
        list = returnMap.get(projectId);
      }
      else
      {
        list = new ArrayList<ProjectRole>();
      }
      list.add(role);
      returnMap.put(projectId, list);
    }
    return returnMap;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<String> findProjectsIdForActors(final UUID... pActorsUUIDs)
  {
    final List<String> returnList = new ArrayList<String>();
    if ((pActorsUUIDs != null) && (pActorsUUIDs.length > 0))
    {
      final TypedQuery<String> groupQuery = entityManager.createNamedQuery(
          "MembershipEntity.findProjectsIdForActors", String.class);
      groupQuery.setParameter("uuids", changeToStrings(pActorsUUIDs));
      final List<String> resultList = groupQuery.getResultList();
      if (resultList != null)
      {
        returnList.addAll(resultList);
      }
    }
    return returnList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> findValidatedProjectsForActors(final ProjectOptions pProjectOptions,
      final UUID... pActorsUUIDs)
  {
    final List<Project> returnList = new ArrayList<Project>();
    if ((pActorsUUIDs != null) && (pActorsUUIDs.length > 0))
    {
      String queryToUse = "MembershipEntity.findValidatedProjectsForActors";
      TypedQuery<Project> groupQuery = null;
      if (pProjectOptions != null)
      {
        if (pProjectOptions.isRetrievedSystem())
        {
          queryToUse = "MembershipEntity.findValidatedProjectsForActorsWithSystem";
        }
        groupQuery = entityManager.createNamedQuery(queryToUse, Project.class);

        // Needed to retrieve also project's image on this request
        final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(groupQuery);
        if ((pProjectOptions.isRetrievedOrganization()) && (!pProjectOptions.isRetrievedImage()))
        {
          openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_organization");

        }
        else if ((pProjectOptions.isRetrievedImage()) && (!pProjectOptions.isRetrievedOrganization()))
        {
          openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_image");

        }
        else if ((pProjectOptions.isRetrievedOrganization()) && (pProjectOptions.isRetrievedImage()))
        {
          openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_all");
        }

      }
      else
      {
        groupQuery = entityManager.createNamedQuery(queryToUse, Project.class);
      }
      groupQuery.setParameter("uuids", changeToStrings(pActorsUUIDs));

      returnList.addAll(groupQuery.getResultList());
    }
    return returnList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Project> findPublicProjectsForActors(final ProjectOptions pProjectOptions,
      final UUID... pActorsUUIDs)
  {
    final List<Project> returnList = new ArrayList<Project>();
    if ((pActorsUUIDs != null) && (pActorsUUIDs.length > 0))
    {
      String queryToUse = "MembershipEntity.findPublicProjectsForActors";
      TypedQuery<Project> groupQuery = null;
      if (pProjectOptions != null)
      {
        if (pProjectOptions.isRetrievedSystem())
        {
          queryToUse = "MembershipEntity.findPublicProjectsForActorsWithSystem";
        }
        groupQuery = entityManager.createNamedQuery(queryToUse, Project.class);
        // Needed to retrieve also project's image on this request
        final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(groupQuery);
        if ((pProjectOptions.isRetrievedOrganization()) && (!pProjectOptions.isRetrievedImage()))
        {
          openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_organization");

        }
        else if ((pProjectOptions.isRetrievedImage()) && (!pProjectOptions.isRetrievedOrganization()))
        {
          openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_image");

        }
        else if ((pProjectOptions.isRetrievedOrganization()) && (pProjectOptions.isRetrievedImage()))
        {
          openJPAQuery.getFetchPlan().setMaxFetchDepth(1).addFetchGroup("project_all");
        }
      }
      else
      {
        groupQuery = entityManager.createNamedQuery(queryToUse, Project.class);
      }
      groupQuery.setParameter("uuids", changeToStrings(pActorsUUIDs));

      final List<Project> resultList = groupQuery.getResultList();
      if (resultList != null)
      {
        returnList.addAll(resultList);
      }
    }
    return returnList;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Membership persist(final Membership pMembership)
  {

    entityManager.persist(pMembership);
    entityManager.flush();
    return pMembership;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Membership pMembership)
  {
    final MembershipEntity entity = (MembershipEntity) entityManager.merge(pMembership);
    entityManager.remove(entity);
    entityManager.flush();
  }

  /**********************************************************
   * The following methods manage MembershipRequest data
   **********************************************************/
  /**
   * {@inheritDoc}
   */
  @Override
  public MembershipRequest newRequest()
  {
    return new MembershipRequestEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipRequest> findAllRequestByProject(final String pProjectId)
  {
    final TypedQuery<MembershipRequest> q = entityManager.createNamedQuery(
        "MembershipRequestEntity.findAllRequestByProject", MembershipRequest.class);
    q.setParameter("projectId", pProjectId);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipRequest> findAllRequestByUser(final String pUserLogin)
  {
    final TypedQuery<MembershipRequest> q = entityManager.createNamedQuery(
        "MembershipRequestEntity.findAllRequestByUser", MembershipRequest.class);
    q.setParameter("login", pUserLogin);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipRequest> findAllRequestByUserAndStatus(final String pUserLogin,
      final MembershipRequestStatus pStatus)
  {
    final TypedQuery<MembershipRequest> q = entityManager.createNamedQuery(
        "MembershipRequestEntity.findAllRequestByUserAndStatus", MembershipRequest.class);
    q.setParameter("login", pUserLogin);
    q.setParameter("status", pStatus);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MembershipRequest findRequestByProjectAndUser(final String pProjectId, final String pUserLogin)

  {
    final TypedQuery<MembershipRequest> q = entityManager.createNamedQuery(
        "MembershipRequestEntity.findCurrentRequestByProjectAndUser", MembershipRequest.class);
    q.setParameter("projectId", pProjectId);
    q.setParameter("login", pUserLogin);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<MembershipRequest> findAllRequestByProjectAndStatus(final String pProjectId,
      final MembershipRequestStatus pStatus)
  {
    final TypedQuery<MembershipRequest> q = entityManager.createNamedQuery(
        "MembershipRequestEntity.findAllRequestByProjectAndStatus", MembershipRequest.class);
    q.setParameter("projectId", pProjectId);
    q.setParameter("status", pStatus);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public MembershipRequest update(final MembershipRequest pMembershipRequest)
  {
    entityManager.merge(pMembershipRequest);
    entityManager.flush();
    return pMembershipRequest;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final MembershipRequest pMembershipRequest)
  {
    final MembershipRequestEntity entity = (MembershipRequestEntity) entityManager.merge(pMembershipRequest);
    entityManager.remove(entity);
    entityManager.flush();
  }

  private List<String> changeToStrings(final UUID... pUUIDs)
  {
    final List<String> returnList = new ArrayList<String>();
    if (pUUIDs != null)
    {
      for (final UUID uuid : pUUIDs)
      {
        returnList.add(uuid.toString());
      }
    }
    return returnList;
  }
}
