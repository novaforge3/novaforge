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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.core.organization.entity.BinaryFileEntity;
import org.novaforge.forge.core.organization.entity.GroupEntity;
import org.novaforge.forge.core.organization.entity.LanguageEntity;
import org.novaforge.forge.core.organization.entity.MembershipEntity;
import org.novaforge.forge.core.organization.entity.MembershipRequestEntity;
import org.novaforge.forge.core.organization.entity.PermissionEntity;
import org.novaforge.forge.core.organization.entity.ProjectEntity;
import org.novaforge.forge.core.organization.entity.ProjectRoleEntity;
import org.novaforge.forge.core.organization.entity.UserEntity;
import org.novaforge.forge.core.organization.model.Actor;
import org.novaforge.forge.core.organization.model.Group;
import org.novaforge.forge.core.organization.model.Membership;
import org.novaforge.forge.core.organization.model.MembershipInfo;
import org.novaforge.forge.core.organization.model.MembershipRequest;
import org.novaforge.forge.core.organization.model.Permission;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.MembershipRequestStatus;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.UserStatus;

import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author sbenoist
 */
public class MembershipDAOImplTest extends OrganizationJPATestCase
{
  /*
   * Constants declaration
   */
  private static final String     NAME         = "name";
  private static final String     FIRSTNAME    = "firstname";
  private static final String     PWD          = "pwd";
  private static final RealmType  TYPE         = RealmType.USER;
  private static final UserStatus STATUS       = UserStatus.ACTIVATED;
  private static final String     FR           = "fr";

  private static final String     LOGIN_1      = "login1";
  private static final String     LOGIN_2      = "login2";

  private static final String     GROUP_NAME_1 = "group_login1";
  private static final String     GROUP_NAME_2 = "group_login2";

  private static final String     EMAIL_1      = "login1@bull.fr";
  private static final String     EMAIL_2      = "login2@bull.fr";

  private static final String     PROJECT_ID_1 = "projectid1";
  private static final String     PROJECT_ID_2 = "projectid2";

  private static final String     ROLE_NAME_1  = "role1";
  private static final String     ROLE_NAME_2  = "role2";

  private MembershipDAOImpl       membershipDAOImpl;

  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    membershipDAOImpl = new MembershipDAOImpl();
    membershipDAOImpl.setEntityManager(em);
  }

  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    membershipDAOImpl = null;
  }

  @Test
  public final void testFindAllByProject()
  {
    // persist 2 users memberships
    buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1, ROLE_NAME_1);
    buildUserMembership(LOGIN_2, EMAIL_2, PROJECT_ID_1, ROLE_NAME_2);

    final List<Membership> membershipsFound = membershipDAOImpl.findByProject(PROJECT_ID_1);
    assertNotNull(membershipsFound);
    assertThat(new Long(membershipsFound.size()), is(new Long(2)));
    assertThat(((User) membershipsFound.get(0).getActor()).getLogin(), is(LOGIN_1));
    assertThat(((User) membershipsFound.get(1).getActor()).getLogin(), is(LOGIN_2));
    assertThat(membershipsFound.get(0).getProject().getElementId(), is(PROJECT_ID_1));
    assertThat(membershipsFound.get(1).getProject().getElementId(), is(PROJECT_ID_1));
    assertThat(membershipsFound.get(0).getRole().getName(), is(ROLE_NAME_1));
    assertThat(membershipsFound.get(1).getRole().getName(), is(ROLE_NAME_2));
  }

  private MembershipEntity buildUserMembership(final String pLogin, final String pEmail, final String pProjectId,
                                               final String pRoleName)
  {
    final TypedQuery<MembershipEntity> query = em.createQuery("SELECT l FROM MembershipEntity l, UserEntity u WHERE l.actor.id = u.id AND u.login = :login AND l.project.elementId = :projectId AND l.role.name = :roleName",
                                                              MembershipEntity.class);
    query.setParameter("login", pLogin);
    query.setParameter("projectId", pProjectId);
    query.setParameter("roleName", pRoleName);
    final List<MembershipEntity> resultList = query.getResultList();
    MembershipEntity             membership = null;
    if ((resultList == null) || (resultList.isEmpty()))
    {
      final Actor actor = buildUser(pLogin, pEmail);
      final Project project = buildProject(pProjectId);
      final ProjectRole role = buildProjectRole(pProjectId, pRoleName);

      membership = new MembershipEntity(actor, role, project, false);
      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      em.merge(membership);
      em.getTransaction().commit();
    }
    else
    {
      membership = resultList.get(0);
    }

    return membership;
  }

  private UserEntity buildUser(final String pLogin, final String pEmail)
  {
    final TypedQuery<UserEntity> query = em.createQuery("SELECT u FROM UserEntity u left join fetch u.language WHERE u.login = :login",
                                                        UserEntity.class);
    query.setParameter("login", pLogin);
    final List<UserEntity> resultList = query.getResultList();
    UserEntity             user       = null;
    if ((resultList == null) || (resultList.isEmpty()))
    {
      user = new UserEntity();
      user.setLogin(pLogin);
      user.setEmail(pEmail);
      user.setName(NAME);
      user.setFirstName(FIRSTNAME);
      user.setPassword(PWD);
      user.setCreated(new Date());
      user.setRealmType(TYPE);
      user.setStatus(STATUS);
      user.setLanguage(buildLanguage(FR));

      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      em.persist(user);
      em.getTransaction().commit();
      user = query.getSingleResult();
    }
    else
    {
      user = resultList.get(0);
    }

    return user;
  }

  private ProjectEntity buildProject(final String pProjectId)
  {
    final TypedQuery<ProjectEntity> query = em.createQuery("SELECT p FROM ProjectElementEntity p WHERE p.elementId = :elementId",
                                                           ProjectEntity.class);
    query.setParameter("elementId", pProjectId);
    final List<ProjectEntity> resultList = query.getResultList();
    ProjectEntity             project    = null;
    if ((resultList == null) || (resultList.isEmpty()))
    {
      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      project = new ProjectEntity();
      project.setElementId(pProjectId);
      project.setRealmType(RealmType.USER);
      project.setStatus(ProjectStatus.VALIDATED);
      project.setPrivateVisibility(false);
      project.setDescription("A description is required");
      project.setName("project_name" + pProjectId);
      final BinaryFileEntity image = new BinaryFileEntity();
      image.setMimeType("image/png");
      image.setFile(new byte[] { (byte) 0x80, 0x53, 0x1c, (byte) 0x87, (byte) 0xa0, 0x42, 0x69, 0x10, (byte) 0xa2,
                                 (byte) 0xea, 0x08, 0x00, 0x2b, 0x30, 0x30, (byte) 0x9d });
      image.setName(pProjectId);
      project.setImage(image);
      em.persist(project);
      em.getTransaction().commit();
    }
    else
    {
      project = resultList.get(0);
    }

    return project;
  }

  private ProjectRoleEntity buildProjectRole(final String pProjectId, final String pRoleName)
  {
    final TypedQuery<ProjectRoleEntity> query = em.createQuery("SELECT p FROM ProjectRoleEntity p WHERE p.element.elementId = :elementId AND p.name = :name",
                                                               ProjectRoleEntity.class);
    query.setParameter("elementId", pProjectId);
    query.setParameter("name", pRoleName);
    final List<ProjectRoleEntity> resultList = query.getResultList();
    ProjectRoleEntity             role       = null;
    if ((resultList == null) || (resultList.isEmpty()))
    {
      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      role = new ProjectRoleEntity();
      role.setName(pRoleName);
      role.setOrder(0);
      role.setElement(buildProject(pProjectId));
      final Permission perm = new PermissionEntity(UUID.randomUUID().toString());
      em.persist(perm);
      role.addPermission(perm);
      em.persist(role);
      em.getTransaction().commit();
    }
    else
    {
      role = resultList.get(0);
    }

    return role;
  }

  private LanguageEntity buildLanguage(final String pName)
  {
    final TypedQuery<LanguageEntity> query = em.createQuery("SELECT l FROM LanguageEntity l WHERE l.name = :name",
                                                            LanguageEntity.class);
    query.setParameter("name", pName);
    final List<LanguageEntity> resultList = query.getResultList();
    LanguageEntity             language   = null;
    if ((resultList == null) || (resultList.isEmpty()))
    {
      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      language = new LanguageEntity();
      language.setName(pName);
      em.persist(language);
      em.getTransaction().commit();
    }
    else
    {
      language = resultList.get(0);
    }

    return language;
  }

  @Test
  public void testFindUsersMembershipsOnProject()
  {
    // persist 2 user memberships
    buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1, ROLE_NAME_1);
    buildUserMembership(LOGIN_2, EMAIL_2, PROJECT_ID_1, ROLE_NAME_2);

    // persist 2 group memberships
    buildGroupMembership(GROUP_NAME_1, PROJECT_ID_1, ROLE_NAME_1, null);
    buildGroupMembership(GROUP_NAME_2, PROJECT_ID_1, ROLE_NAME_2, null);

    final List<MembershipInfo> membershipsFound = membershipDAOImpl.findUsersInfoByProject(PROJECT_ID_1);
    assertNotNull(membershipsFound);

    assertThat(new Long(membershipsFound.size()), is(new Long(2)));
    assertThat(((User) membershipsFound.get(0).getActor()).getLogin(), is(LOGIN_1));
    assertThat(((User) membershipsFound.get(1).getActor()).getLogin(), is(LOGIN_2));
    assertThat(membershipsFound.get(0).getProjectId(), is(PROJECT_ID_1));
    assertThat(membershipsFound.get(1).getProjectId(), is(PROJECT_ID_1));
    assertThat(membershipsFound.get(0).getRole().getName(), is(ROLE_NAME_1));
    assertThat(membershipsFound.get(1).getRole().getName(), is(ROLE_NAME_2));
  }

  @Test
  public void testFindGroupsMembershipsByProject()
  {
    // persist 2 user memberships
    buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1, ROLE_NAME_1);
    buildUserMembership(LOGIN_2, EMAIL_2, PROJECT_ID_1, ROLE_NAME_2);

    // persist 2 group memberships
    buildGroupMembership(GROUP_NAME_1, PROJECT_ID_1, ROLE_NAME_1, null);
    buildGroupMembership(GROUP_NAME_2, PROJECT_ID_1, ROLE_NAME_2, null);

    final List<MembershipInfo> membershipsFound = membershipDAOImpl.findGroupsInfoByProject(PROJECT_ID_1);
    assertThat(new Long(membershipsFound.size()), is(new Long(2)));
    assertThat(((Group) membershipsFound.get(0).getActor()).getName(), is(GROUP_NAME_1));
    assertThat(((Group) membershipsFound.get(1).getActor()).getName(), is(GROUP_NAME_2));
    assertThat(membershipsFound.get(0).getProjectId(), is(PROJECT_ID_1));
    assertThat(membershipsFound.get(1).getProjectId(), is(PROJECT_ID_1));
    assertThat(membershipsFound.get(0).getRole().getName(), is(ROLE_NAME_1));
    assertThat(membershipsFound.get(1).getRole().getName(), is(ROLE_NAME_2));
  }

  @Test
  public void testFindAllByProjectAndActor()
  {
    // persist 1 user membership
    final MembershipEntity buildUserMembership = buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1,
        ROLE_NAME_1);

    final List<Membership> membershipsFound = membershipDAOImpl.findByProjectAndActor(PROJECT_ID_1,
        buildUserMembership.getActor().getUuid());
    assertThat(new Long(membershipsFound.size()), is(new Long(1)));
    assertThat(((User) membershipsFound.get(0).getActor()).getLogin(), is(LOGIN_1));
    assertThat(membershipsFound.get(0).getProject().getElementId(), is(PROJECT_ID_1));
    assertThat(membershipsFound.get(0).getRole().getName(), is(ROLE_NAME_1));
  }

  @Test
  public void testFindAllActorsByProject()
  {
    // persist 2 users memberships
    buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1, ROLE_NAME_1);
    buildUserMembership(LOGIN_2, EMAIL_2, PROJECT_ID_1, ROLE_NAME_2);

    final List<Actor> actorsFound = membershipDAOImpl.findActorsByProject(PROJECT_ID_1);
    assertThat(new Long(actorsFound.size()), is(new Long(2)));
    assertThat(((User) actorsFound.get(0)).getLogin(), is(LOGIN_1));
    assertThat(((User) actorsFound.get(1)).getLogin(), is(LOGIN_2));
  }

  @Test
  public void testFindAllActorsByRoleAndProject()
  {
    // persist 2 users memberships
    buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1, ROLE_NAME_1);
    buildUserMembership(LOGIN_2, EMAIL_2, PROJECT_ID_1, ROLE_NAME_2);

    final List<Actor> actorsFound = membershipDAOImpl.findActorsByProjectAndRole(PROJECT_ID_1, ROLE_NAME_1);
    assertThat(new Long(actorsFound.size()), is(new Long(1)));
    assertThat(((User) actorsFound.get(0)).getLogin(), is(LOGIN_1));
  }

  @Test
  public void testFindAllActorsByRole()
  {
    // persist 2 users memberships
    buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1, ROLE_NAME_1);
    buildUserMembership(LOGIN_2, EMAIL_2, PROJECT_ID_1, ROLE_NAME_2);

    final List<Actor> actorsFound = membershipDAOImpl.findActorsByRole(ROLE_NAME_1);
    assertThat(new Long(actorsFound.size()), is(new Long(1)));
    assertThat(((User) actorsFound.get(0)).getLogin(), is(LOGIN_1));
  }

  @Test
  public void testFindProjectsIdForUser()
  {
    // persist 1 user membership and 1 group membership
    final MembershipEntity buildUserMembership = buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1,
        ROLE_NAME_1);
    buildGroupMembership(GROUP_NAME_1, PROJECT_ID_1, ROLE_NAME_1, null);

    final List<String> projectIds = membershipDAOImpl.findProjectsIdForActors(buildUserMembership.getActor()
        .getUuid());
    assertThat(new Long(projectIds.size()), is(new Long(1)));
    assertThat(projectIds.get(0), is(PROJECT_ID_1));
  }

  @Test
  public void testFindProjectsIdForGroups()
  {
    // persist 1 user membership and 1 group membership
    buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1, ROLE_NAME_1);
    final MembershipEntity buildGroupMembership = buildGroupMembership(GROUP_NAME_1, PROJECT_ID_1,
        ROLE_NAME_1, null);

    final List<String> logins = new ArrayList<String>();
    logins.add(GROUP_NAME_1);
    final List<String> projectIds = membershipDAOImpl.findProjectsIdForActors(buildGroupMembership.getActor()
        .getUuid());
    assertThat(new Long(projectIds.size()), is(new Long(1)));
    assertThat(projectIds.get(0), is(PROJECT_ID_1));
  }

  @Test
  public void testFindValidatedProjectsByUser()
  {
    // persist 1 user membership
    final MembershipEntity buildUserMembership = buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1,
        ROLE_NAME_1);

    final List<Project> projects = membershipDAOImpl.findValidatedProjectsForActors(null, buildUserMembership
        .getActor().getUuid());
    assertThat(new Long(projects.size()), is(new Long(1)));
    assertThat(projects.get(0).getProjectId(), is(PROJECT_ID_1));
  }

  @Test
  public void testFindAllRequestByProject()
  {
    // persist 1 user, 1 project and 1 request
    final UserEntity user = buildUser(LOGIN_1, EMAIL_1);
    final ProjectEntity project = buildProject(PROJECT_ID_1);
    buildRequestEntity(user, project);

    final List<MembershipRequest> requests = membershipDAOImpl.findAllRequestByProject(PROJECT_ID_1);
    assertThat(new Long(requests.size()), is(new Long(1)));
    assertThat(requests.get(0).getUser().getLogin(), is(LOGIN_1));
    assertThat(requests.get(0).getProject().getProjectId(), is(PROJECT_ID_1));
  }

  private MembershipRequestEntity buildRequestEntity(final User pUser, final Project pProject)
  {
    final TypedQuery<MembershipRequestEntity> query = em.createQuery("SELECT p FROM MembershipRequestEntity p WHERE p.project.elementId = :projectId and  p.user.login = :login",
                                                                     MembershipRequestEntity.class);
    query.setParameter("login", pUser.getLogin());
    query.setParameter("projectId", pProject.getProjectId());
    final List<MembershipRequestEntity> resultList = query.getResultList();
    MembershipRequestEntity             request    = null;
    if ((resultList == null) || (resultList.isEmpty()))
    {
      request = new MembershipRequestEntity();
      request.setUser(pUser);
      request.setProject(pProject);
      request.setStatus(MembershipRequestStatus.IN_PROGRESS);
      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      em.persist(request);
      em.getTransaction().commit();
    }
    else
    {
      request = resultList.get(0);
    }

    return request;
  }

  @Test
  public void testFindAllRequestByUser()
  {
    // persist 1 user, 1 project and 1 request
    final UserEntity user = buildUser(LOGIN_1, EMAIL_1);
    final ProjectEntity project = buildProject(PROJECT_ID_1);
    buildRequestEntity(user, project);

    final List<MembershipRequest> requests = membershipDAOImpl.findAllRequestByUser(LOGIN_1);
    assertThat(new Long(requests.size()), is(new Long(1)));
    assertThat(requests.get(0).getUser().getLogin(), is(LOGIN_1));
    assertThat(requests.get(0).getProject().getProjectId(), is(PROJECT_ID_1));
  }

  @Test
  public void testFindRequestByProjectAndUser()
  {
    // persist 1 user, 1 project and 1 request
    final UserEntity user = buildUser(LOGIN_1, EMAIL_1);
    final ProjectEntity project = buildProject(PROJECT_ID_1);
    buildRequestEntity(user, project);

    final MembershipRequest request = membershipDAOImpl.findRequestByProjectAndUser(PROJECT_ID_1, LOGIN_1);
    assertNotNull(request);
    assertThat(request.getUser().getLogin(), is(LOGIN_1));
    assertThat(request.getProject().getProjectId(), is(PROJECT_ID_1));
  }

  @Test
  public void testFindAllRequestByProjectAndStatus()
  {
    // persist 1 user, 1 project and 1 request
    final UserEntity user = buildUser(LOGIN_1, EMAIL_1);
    final ProjectEntity project = buildProject(PROJECT_ID_1);
    buildRequestEntity(user, project);

    final List<MembershipRequest> requests = membershipDAOImpl.findAllRequestByProjectAndStatus(PROJECT_ID_1,
        MembershipRequestStatus.IN_PROGRESS);
    assertThat(requests.size(), is(1));
    assertThat(requests.get(0).getUser().getLogin(), is(LOGIN_1));
    assertThat(requests.get(0).getProject().getProjectId(), is(PROJECT_ID_1));
  }

  @Test
  public void testFindPublicProjectsForUser()
  {
    // persist 1 project with no memberships
    buildProject(PROJECT_ID_1);
    final MembershipEntity buildUserMembership = buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_2,
        ROLE_NAME_1);

    // the user with login LOGIN_1 is not meber of the project PROJECT_ID_1 so it will returns this project
    final List<Project> projects = membershipDAOImpl.findPublicProjectsForActors(null, buildUserMembership
        .getActor().getUuid());
    assertThat(new Long(projects.size()), is(new Long(1)));
    assertThat(projects.get(0).getProjectId(), is(PROJECT_ID_1));
  }

  @Test
  public void testFindPublicProjectsForGroups()
  {
    // persist 1 project with no memberships
    buildProject(PROJECT_ID_1);
    final MembershipEntity buildGroupMembership = buildGroupMembership(GROUP_NAME_1, PROJECT_ID_2,
        ROLE_NAME_1, null);
    buildUserMembership(LOGIN_1, EMAIL_1, PROJECT_ID_1, ROLE_NAME_1);

    // the user with login LOGIN_1 is not member of the project PROJECT_ID so it will returns this project
    final List<Project> projects = membershipDAOImpl.findPublicProjectsForActors(null, buildGroupMembership
        .getActor().getUuid());
    assertThat(new Long(projects.size()), is(new Long(1)));
    assertThat(projects.get(0).getProjectId(), is(PROJECT_ID_1));
  }

  private MembershipEntity buildGroupMembership(final String pName, final String pProjectId,
      final String pRoleName, final User pGroupUser)
  {
    final TypedQuery<MembershipEntity> query = em
        .createQuery(
            "SELECT l FROM MembershipEntity l, GroupEntity g WHERE l.actor = g AND g.name = :name AND l.project.elementId = :projectId AND l.role.name = :roleName",
            MembershipEntity.class);
    query.setParameter("name", pName);
    query.setParameter("projectId", pProjectId);
    query.setParameter("roleName", pRoleName);
    final List<MembershipEntity> resultList = query.getResultList();
    MembershipEntity membership = null;
    if ((resultList == null) || (resultList.isEmpty()))
    {
      final Actor actor = buildGroup(pName, pProjectId, pGroupUser);
      final Project project = buildProject(pProjectId);
      final ProjectRole role = buildProjectRole(pProjectId, pRoleName);

      membership = new MembershipEntity(actor, role, project, false);

      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      em.persist(membership);
      em.getTransaction().commit();
    }
    else
    {
      membership = resultList.get(0);
    }

    return membership;
  }

  private GroupEntity buildGroup(final String pName, final String pProjectId, final User pUser)
  {
    final TypedQuery<GroupEntity> query = em.createQuery(
        "SELECT g FROM GroupEntity g WHERE g.name = :name and g.project.elementId = :projectId",
        GroupEntity.class);
    query.setParameter("name", pName);
    query.setParameter("projectId", pProjectId);
    final List<GroupEntity> resultList = query.getResultList();
    GroupEntity group = null;
    if ((resultList == null) || (resultList.isEmpty()))
    {
      group = new GroupEntity();
      group.setName(pName);
      group.setProject(buildProject(pProjectId));
      if (pUser != null)
      {
        group.addUser(pUser);
      }
      if (!em.getTransaction().isActive())
      {
        em.getTransaction().begin();
      }
      em.persist(group);
      em.getTransaction().commit();
    }
    else
    {
      group = resultList.get(0);
    }

    return group;
  }

}
