/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you may redistribute and/or 
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation, version 3 of the License.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see http://www.gnu.org/licenses.
 *
 * Additional permission under GNU AGPL version 3 (AGPL-3.0) section 7
 *
 * If you modify this Program, or any covered work,
 * by linking or combining it with libraries listed
 * in COPYRIGHT file at the top-level directof of this
 * distribution (or a modified version of that libraries),
 * containing parts covered by the terms of licenses cited
 * in the COPYRIGHT file, the licensors of this Program
 * grant you additional permission to convey the resulting work.
 */
package org.novaforge.forge.core.organization.internal.services.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.novaforge.forge.core.organization.exceptions.ExceptionCode;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.services.CommonUserService;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.novaforge.forge.core.organization.services.MembershipService;
import org.novaforge.forge.core.organization.services.ProjectRoleService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

/**
 * @author Marc Blachon
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class ProjectRoleServiceImplTest extends BaseUtil
{
  private static final String LANGUAGE_FR_NAME     = "FR";

  // user1
  private static final String NAME1                = "name1";
  private static final String FIRSTNAME1           = "firstname1";
  private static final String LOGIN1               = "name1-f";
  private static final String EMAIL1               = "mailUser1@toto.fr";
  private static final String PASSWORD1            = "password1";

  // user2
  private static final String NAME2                = "name2";
  private static final String FIRSTNAME2           = "firstname2";
  private static final String LOGIN2               = "name2-f";
  private static final String EMAIL2               = "mailUser2@toto.fr";
  private static final String PASSWORD2            = "password2";

  // project1
  private static final String PROJECT1NAME         = "project 1";
  private static final String PROJECT_DESCRIPTION1 = "description 1";
  private static final String PROJECT1ID           = "project1";
  private static final String LGPL                 = "LGPL";

  // project role
  private static final String ROLE1NAME            = "roleName1";
  private static final String ROLE1DESC            = "roleDesc1";
  private static final String ROLE2NAME            = "roleName2";
  private static final String ROLE2DESC            = "roleDesc2";
  private static final String ADMINISTRATOR        = "Administrator";

  @Inject
  private ProjectService      projectService;

  @Inject
  private ProjectRoleService  projectRoleService;

  @Inject
  private CommonUserService   commonUserService;

  @Inject
  private LanguageService     languageService;

  @Inject
  private MembershipService   membershipService;

  @Test
  // @Ignore
  public void testAddPermissionToRole() throws ProjectServiceException, LanguageServiceException,
      UserServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    // project1
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);

    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    final List<Project> projects = projectService.getAllProjects(false);
    assertNotNull(projects);
    assertEquals(2, projects.size());
    assertEquals(PROJECT1ID, projects.get(1).getProjectId());

    final ProjectInfo projectInfo = projectService.getProjectInfo(PROJECT1ID);
    assertNotNull(projectInfo);
    assertEquals(PROJECT1ID, projectInfo.getProjectId());
    assertEquals(PROJECT1NAME, projectInfo.getName());
    assertEquals(PROJECT_DESCRIPTION1, projectInfo.getDescription());
  }

  @Test
  // @Ignore
  public void testCreateRole() throws LanguageServiceException, UserServiceException, ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // create role
    final ProjectRole projectRole = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createRole(projectRole, PROJECT1ID);

    // check
    final List<ProjectRole> roles = projectRoleService.getAllRoles(PROJECT1ID);
    assertNotNull(roles);
    assertEquals(2, roles.size());
    assertEquals(roles.get(1).getName(), ROLE1NAME);

    // check raised exception when
    // the role already exists for the project
    try
    {
      projectRoleService.createRole(projectRole, PROJECT1ID);
      fail("should has raised an exception");
    }
    catch (final Exception pe)
    {
      System.out.println("expected exception!");
    }
  }

  @Test
  // @Ignore
  public void testCreateSystemRole() throws LanguageServiceException, UserServiceException,
      ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // create role
    final ProjectRole projectRole = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createSystemRole(projectRole, PROJECT1ID);

    // check
    final List<ProjectRole> roles = projectRoleService.getAllRoles(PROJECT1ID);
    assertNotNull(roles);
    assertEquals(2, roles.size());
    assertEquals(roles.get(1).getName(), ROLE1NAME);
  }

  @Test
  // @Ignore
  public void testDeleteRole() throws LanguageServiceException, UserServiceException, ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // create role
    final ProjectRole projectRole1 = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createRole(projectRole1, PROJECT1ID);

    // delete role
    projectRoleService.deleteRole(ROLE1NAME, PROJECT1ID);

    // check
    final List<ProjectRole> roles = projectRoleService.getAllRoles(PROJECT1ID);
    assertNotNull(roles);
    assertEquals(1, roles.size());

    // role with SYSTEM realm
    // check
    final Role adminRole = projectRoleService.getRole(ADMINISTRATOR, PROJECT1ID);
    assertNotNull(adminRole);
    System.out.println("***************** adminRole.getRealmType().toString() = "
        + adminRole.getRealmType().toString());

    // try delete role
    try
    {
      projectRoleService.deleteRole(ADMINISTRATOR, PROJECT1ID);
      fail("should has raised an exception");
    }
    catch (final ProjectServiceException pe)
    {
      System.out.println("expected exception!");
      assertTrue(pe.getCode().equals(ExceptionCode.ERR_UPDATE_OR_DELETE_SYSTEM_ROLE));
    }

  }

  @Test
  // @Ignore
  public void testFindAllRolesByActorAndProject() throws LanguageServiceException, UserServiceException,
      ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1 and user2
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    final User user2 = createUser(NAME2, FIRSTNAME2, EMAIL2, PASSWORD2);
    commonUserService.createUser(user2);

    // create project1 with user1 as administrator
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // create roles
    final ProjectRole projectRole1 = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createRole(projectRole1, PROJECT1ID);

    final ProjectRole projectRole2 = createRole(ROLE2NAME, ROLE2DESC, RealmType.USER);
    projectRoleService.createRole(projectRole2, PROJECT1ID);

    final HashSet<String> rolesNameForLogin1 = new HashSet<String>();
    rolesNameForLogin1.add(ROLE1NAME);
    rolesNameForLogin1.add(ROLE2NAME);

    final HashSet<String> rolesNameForLogin2 = new HashSet<String>();
    rolesNameForLogin2.add(ROLE1NAME);

    // associate members and roles for the project
    membershipService.addUserMembership(PROJECT1ID, LOGIN1, rolesNameForLogin1, ROLE1NAME, NAME1, false);
    membershipService.addUserMembership(PROJECT1ID, LOGIN2, rolesNameForLogin2, ROLE1NAME, NAME2, false);

    // check for user1 (3 roles)
    final HashSet<String> expectedRolesName1 = new HashSet<String>();
    final HashSet<String> currentRolesName1 = new HashSet<String>();
    expectedRolesName1.add(ROLE1NAME);
    expectedRolesName1.add(ROLE2NAME);
    expectedRolesName1.add(ADMINISTRATOR);

    final Set<ProjectRole> rolesForLogin1 = projectRoleService.findAllRolesByActorAndProject(PROJECT1ID,
        LOGIN1);
    assertNotNull(rolesForLogin1);
    assertEquals(3, rolesForLogin1.size());
    for (final ProjectRole projectRole : rolesForLogin1)
    {
      currentRolesName1.add(projectRole.getName());
    }
    assertEquals(expectedRolesName1, currentRolesName1);

    // check for user2 (only 1 role)
    final HashSet<String> expectedRolesName2 = new HashSet<String>();
    final HashSet<String> currentRolesName2 = new HashSet<String>();
    expectedRolesName2.add(ROLE1NAME);

    final Set<ProjectRole> rolesForLogin2 = projectRoleService.findAllRolesByActorAndProject(PROJECT1ID,
        LOGIN2);
    assertNotNull(rolesForLogin2);
    assertEquals(1, rolesForLogin2.size());
    for (final ProjectRole projectRole : rolesForLogin2)
    {
      currentRolesName2.add(projectRole.getName());
    }
    assertEquals(expectedRolesName2, currentRolesName2);
  }

  @Test
  // @Ignore
  public void testGetAllRoles() throws LanguageServiceException, UserServiceException,
      ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // create role
    final ProjectRole projectRole1 = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createRole(projectRole1, PROJECT1ID);

    final ProjectRole projectRole2 = createRole(ROLE2NAME, ROLE2DESC, RealmType.USER);
    projectRoleService.createRole(projectRole2, PROJECT1ID);

    // check
    final HashSet<String> expectedRoles = new HashSet<String>();
    final HashSet<String> currentRoles = new HashSet<String>();
    expectedRoles.add(ROLE1NAME);
    expectedRoles.add(ROLE2NAME);
    expectedRoles.add(ADMINISTRATOR);

    final List<ProjectRole> roles = projectRoleService.getAllRoles(PROJECT1ID);
    assertNotNull(roles);
    assertEquals(3, roles.size());
    for (final ProjectRole projectRole : roles)
    {
      currentRoles.add(projectRole.getName());
    }
    assertEquals(expectedRoles, currentRoles);
  }

  @Test
  // @Ignore
  public void testGetRole() throws LanguageServiceException, UserServiceException, ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // create role
    final ProjectRole projectRole = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createRole(projectRole, PROJECT1ID);

    final ProjectRole role = projectRoleService.getRole(ROLE1NAME, PROJECT1ID);

    // check
    assertNotNull(role);
    assertEquals(role.getName(), ROLE1NAME);
  }

  @Test
  // @Ignore
  public void testNewRole() throws LanguageServiceException, UserServiceException, ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // new role
    final ProjectRole projectRole = projectRoleService.newRole();

    // check
    assertNotNull(projectRole);
    assertEquals(null, projectRole.getName());
    assertEquals(null, projectRole.getDescription());

    System.out.println("*********  order= " + projectRole.getOrder());
    System.out.println("***************** projectRole dump= " + projectRole.toString());
  }

  @Test
  // @Ignore
  public void testChangeOrder() throws UserServiceException, LanguageServiceException,
      ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1 and user2
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    // create project1 with user1 as administrator
    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // create roles
    final ProjectRole projectRole1 = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createRole(projectRole1, PROJECT1ID);

    final ProjectRole projectRole2 = createRole(ROLE2NAME, ROLE2DESC, RealmType.USER);
    projectRoleService.createRole(projectRole2, PROJECT1ID);

    System.out.println("******************** projectRole1 " + projectRole1.toString());
    final int initialOrder = projectRole1.getOrder();

    // Increase role order
    final ProjectRole increasedRole = projectRoleService.changeOrder(PROJECT1ID, ROLE1NAME, true, LOGIN1);

    // check
    assertTrue((initialOrder + 1) == increasedRole.getOrder());

    // Decrease role order
    final ProjectRole decreasedRole = projectRoleService.changeOrder(PROJECT1ID, ROLE1NAME, false, LOGIN1);

    // check
    assertTrue(initialOrder == decreasedRole.getOrder());
  }

  @Test
  // @Ignore
  public void testUpdateRole() throws LanguageServiceException, UserServiceException, ProjectServiceException
  {
    assertNotNull(projectService);
    assertNotNull(projectRoleService);
    // user1
    assertNotNull(commonUserService);
    assertNotNull(languageService);
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    final Project project1 = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(project1, LOGIN1, null);
    projectService.validateProject(PROJECT1ID);

    // create role
    final ProjectRole projectRole = createRole(ROLE1NAME, ROLE1DESC, RealmType.USER);
    projectRoleService.createRole(projectRole, PROJECT1ID);

    // update role
    final ProjectRole pRole = projectRoleService.getRole(ROLE1NAME, PROJECT1ID);
    pRole.setDescription(ROLE2DESC);
    pRole.setRealmType(RealmType.USER);
    projectRoleService.updateRole(ROLE1NAME, pRole, PROJECT1ID, LOGIN1);

    // check
    final ProjectRole updatedRole = projectRoleService.getRole(ROLE1NAME, PROJECT1ID);
    assertNotNull(updatedRole);
    assertEquals(updatedRole.getName(), ROLE1NAME);
    assertEquals(updatedRole.getDescription(), ROLE2DESC);
    assertEquals(updatedRole.getRealmType(), RealmType.USER);

    // check raised exception if the new role already exists for the project
    final ProjectRole projectRole2 = createRole(ROLE2NAME, ROLE2DESC, RealmType.USER);
    projectRoleService.createRole(projectRole2, PROJECT1ID);
    try
    {
      // update role
      final ProjectRole p2Role = projectRoleService.getRole(ROLE1NAME, PROJECT1ID);
      p2Role.setName(ROLE2NAME);
      projectRoleService.updateRole(ROLE1NAME, p2Role, PROJECT1ID, LOGIN1);
      fail("should has raised an exception");
    }
    catch (final Exception pe)
    {
      System.out.println("expected exception!");
    }

    // check if the role cannot be updated (the system roles can't...)
    try
    {
      // update role
      final ProjectRole adminRole = projectRoleService.getRole(ADMINISTRATOR, PROJECT1ID);
      adminRole.setName(ADMINISTRATOR + "2");
      projectRoleService.updateRole(ADMINISTRATOR, adminRole, PROJECT1ID, LOGIN1);
      fail("should has raised an exception");
    }
    catch (final Exception pe)
    {
      System.out.println("expected exception!");
    }
  }

  // private methods:
  /**
   * @return
   * @throws LanguageServiceException
   */
  private User createUser(final String name, final String firstName, final String eMail, final String password)
      throws LanguageServiceException
  {
    final User user = commonUserService.newUser();
    assertNotNull(user);
    user.setFirstName(firstName);
    user.setName(name);
    user.setEmail(eMail);
    final Language langFR = languageService.getLanguage(LANGUAGE_FR_NAME);
    user.setLanguage(langFR);
    user.setPassword(password);
    return user;
  }

  /**
   * @return
   */
  private Project createProject(final String projectId, final String projectName, final String licence,
      final String description)
  {
    final Project project1 = projectService.newProject();
    project1.setProjectId(PROJECT1ID);
    project1.setLicenceType(LGPL);
    project1.setName(PROJECT1NAME);
    project1.setDescription(PROJECT_DESCRIPTION1);
    return project1;
  }

  /**
   * @return
   */
  private ProjectRole createRole(final String roleName, final String roleDesc, final RealmType realmType)
  {
    final ProjectRole projectRole = projectRoleService.newRole();
    projectRole.setName(roleName);
    projectRole.setDescription(roleDesc);
    projectRole.setRealmType(realmType);
    return projectRole;
  }

}
