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
package org.novaforge.forge.tools.requirements.common.internal.dao;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.Membership;
import org.novaforge.forge.tools.requirements.common.model.Role;
import org.novaforge.forge.tools.requirements.common.model.User;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * @author sbenoist
 */
public class MembershipDAOImplTest extends RequirementJPATestCase
{
  private static final String USER_LOGIN    = "user_login";

  private static final String PROJECT_ID    = "projectId";

  private static final String ROLE_NAME     = "role_name";

  private static final String PROJECT_NAME  = "project_name";

  private static final String NEW_ROLE_NAME = "new_role_name";

  private MembershipDAOImpl   membershipDAOImpl;

  private UserDAOImpl         userDAOImpl;

  private ProjectDAOImpl      projectDAOImpl;

  private RoleDAOImpl         roleDAOImpl;

  /**
   * {@inheritDoc}
   */
  @Override
  @Before
  public void setUp()
  {
    super.setUp();
    membershipDAOImpl = new MembershipDAOImpl();
    membershipDAOImpl.setEntityManager(em);

    userDAOImpl = new UserDAOImpl();
    userDAOImpl.setEntityManager(em);

    projectDAOImpl = new ProjectDAOImpl();
    projectDAOImpl.setEntityManager(em);

    roleDAOImpl = new RoleDAOImpl();
    roleDAOImpl.setEntityManager(em);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @After
  public void tearDown()
  {
    super.tearDown();
    membershipDAOImpl = null;
    userDAOImpl = null;
    projectDAOImpl = null;
    roleDAOImpl = null;
  }

  @Test
  public void testPersist()
  {
    Membership membership = buildMembership();
    em.getTransaction().begin();
    membershipDAOImpl.persist(membership);
    em.getTransaction().commit();

    membership = membershipDAOImpl.findByUserAndProject(USER_LOGIN, PROJECT_ID);
    assertNotNull(membership);
    assertThat(membership.getUser().getLogin(), is(USER_LOGIN));
  }

  private Membership buildMembership()
  {
    User user = userDAOImpl.newUser();
    user.setLogin(USER_LOGIN);
    em.getTransaction().begin();
    userDAOImpl.persist(user);
    em.getTransaction().commit();

    IProject project = projectDAOImpl.newProject();
    project.setProjectId(PROJECT_ID);
    project.setName(PROJECT_NAME);
    em.getTransaction().begin();
    projectDAOImpl.persist(project);
    em.getTransaction().commit();

    Role role = roleDAOImpl.newRole();
    role.setName(ROLE_NAME);
    em.getTransaction().begin();
    roleDAOImpl.persist(role);
    em.getTransaction().commit();

    return membershipDAOImpl.newMembership(user, project, role);
  }

  @Test
  public void testUpdate()
  {
    Membership membership = buildMembership();
    em.getTransaction().begin();
    membershipDAOImpl.persist(membership);
    em.getTransaction().commit();

    membership = membershipDAOImpl.findByUserAndProject(USER_LOGIN, PROJECT_ID);
    assertNotNull(membership);
    assertThat(membership.getUser().getLogin(), is(USER_LOGIN));

    // Change the role
    Role role = roleDAOImpl.newRole();
    role.setName(NEW_ROLE_NAME);
    em.getTransaction().begin();
    roleDAOImpl.persist(role);
    em.getTransaction().commit();
    membership.setRole(role);

    em.getTransaction().begin();
    membershipDAOImpl.update(membership);
    em.getTransaction().commit();

    membership = membershipDAOImpl.findByUserAndProject(USER_LOGIN, PROJECT_ID);
    assertNotNull(membership);
    assertThat(membership.getRole().getName(), is(NEW_ROLE_NAME));
  }

  @Test
  public void testDelete()
  {
    Membership membership = buildMembership();
    em.getTransaction().begin();
    membershipDAOImpl.persist(membership);
    em.getTransaction().commit();

    membership = membershipDAOImpl.findByUserAndProject(USER_LOGIN, PROJECT_ID);
    assertNotNull(membership);
    assertThat(membership.getUser().getLogin(), is(USER_LOGIN));

    em.getTransaction().begin();
    membershipDAOImpl.delete(membership);
    em.getTransaction().commit();

    boolean exist = membershipDAOImpl.exist(USER_LOGIN, PROJECT_ID);
    assertThat(exist, is(false));
  }

  @Test(expected = org.apache.openjpa.persistence.EntityExistsException.class)
  public void testMembershipUnicity()
  {
    Membership membership = buildMembership();
    em.getTransaction().begin();
    membershipDAOImpl.persist(membership);
    em.getTransaction().commit();

    membership = membershipDAOImpl.findByUserAndProject(USER_LOGIN, PROJECT_ID);
    assertNotNull(membership);
    assertThat(membership.getUser().getLogin(), is(USER_LOGIN));

    // Create a new mebership for the user on the project with a new role
    Role role = roleDAOImpl.newRole();
    role.setName(NEW_ROLE_NAME);
    em.getTransaction().begin();
    roleDAOImpl.persist(role);
    em.getTransaction().commit();

    Membership newMembership = membershipDAOImpl.newMembership(membership.getUser(), membership.getProject(),
        role);
    em.getTransaction().begin();
    membershipDAOImpl.persist(newMembership);
    em.getTransaction().commit();
  }
}
