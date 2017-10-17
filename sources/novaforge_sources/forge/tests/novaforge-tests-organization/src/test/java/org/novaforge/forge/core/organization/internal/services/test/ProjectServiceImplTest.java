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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.OrganizationServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.SpaceServiceException;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.Organization;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectInfo;
import org.novaforge.forge.core.organization.model.ProjectRole;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.services.CommonUserService;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.novaforge.forge.core.organization.services.MembershipService;
import org.novaforge.forge.core.organization.services.OrganizationService;
import org.novaforge.forge.core.organization.services.ProjectRoleService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.organization.services.SpaceService;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

/**
 * @author blachonm
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class ProjectServiceImplTest extends BaseUtil
{

  private static final String LANGUAGE_FR_NAME             = "FR";

  // admin1
  private static final String ADMIN1                       = "admin1";

  // user1
  private static final String NAME1                        = "name1";
  private static final String FIRSTNAME1                   = "firstname1";
  private static final String LOGIN1                       = "name1-f";
  private static final String EMAIL1                       = "mailUser1@toto.fr";
  private static final String PASSWORD1                    = "password1";

  // user2
  private static final String LOGIN2                       = "name1-f";

  private static final String FORGE_PROJECT                = "forge";

  // project1 project
  private static final String PROJECT1NAME                 = "project 1";
  private static final String PROJECT_DESCRIPTION1         = "description 1";
  private static final String PROJECT1ID                   = "project1";
  private static final String LGPL                         = "LGPL";

  // project2 project
  private static final String PROJECT2NAME                 = "project 2";
  private static final String PROJECT_DESCRIPTION2         = "description 2";
  private static final String PROJECT2ID                   = "project2";

  // project3 project
  private static final String PROJECT3NAME                 = "project 3";
  private static final String PROJECT_DESCRIPTION3         = "description 3";
  private static final String PROJECT3ID                   = "project3";

  // forge project
  private static final String FORGEID                      = FORGE_PROJECT;

  // project role
  private static final String ROLE1NAME                    = "roleName1";
  private static final String ROLE1DESC                    = "roleDesc1";

  private static final String ROLE2NAME                    = "roleName2";
  private static final String ROLE2DESC                    = "roleDesc2";

  private static final String ROLE3NAME                    = "roleName3";
  private static final String ROLE3DESC                    = "roleDesc3";

  private static final String ADMINISTRATOR                = "Administrator";
  private static final String MEMBER                       = "Member";

  // organization
  private static final String ORGANIZATION_NAME1           = "organization1";

  // spaces
  private static final String SPACE_NAME1                  = "space1";
  private static final String SPACE_NAME2                  = "space2";

  private static final String SPACE_DESC1                  = "space1 description";
  private static final String SPACE_DESC2                  = "space2 description";

  private static final String URI_PROJECT1_SPACE1          = PROJECT1ID + "/" + SPACE_NAME1;
  private static final String URI_PROJECT1_SPACE2          = PROJECT1ID + "/" + SPACE_NAME2;

  private static final long   WAIT_CHANGE_ROLE_PROPAGATION = 5000;

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

  @Inject
  private OrganizationService organizationService;

  @Inject
  private SpaceService        spaceService;

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#createProject(org.novaforge.forge.core.organization.model.Project, java.lang.String, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testCreateProject() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createProject(project2, LOGIN1, null);

    // spaces for project1
    Space space1 = create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    Space space2 = create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // // roles for project1
    ProjectRole role1 = createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    ProjectRole role2 = createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // organization for project1
    Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    project1.setOrganization(org1);

    // validate both
    projectService.validateProject(PROJECT1ID);
    projectService.validateProject(PROJECT2ID);

    List<Project> projects = projectService.getAllProjects(false);
    assertNotNull(projects);
    assertEquals(3, projects.size());

    // check raised exception when validating the bean
    Project badBeanproject = prepareProject(ORGANIZATION_NAME1, "", PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(badBeanproject);
    try
    {
      projectService.createProject(badBeanproject, LOGIN1, null);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised exception when check another project with the same project id doesn't already exist
    Project alreadyExistproject = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME,
        PROJECT_DESCRIPTION1);
    assertNotNull(alreadyExistproject);
    try
    {
      projectService.createProject(alreadyExistproject, LOGIN1, null);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised exception when check the author username is existing
    Project authorNotExistingproject = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME,
        PROJECT_DESCRIPTION1);
    assertNotNull(authorNotExistingproject);
    try
    {
      projectService.createProject(authorNotExistingproject, LOGIN2, null);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#createSystemProject(org.novaforge.forge.core.organization.model.Project, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testCreateSystemProject() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createSystemProject(project1, LOGIN1);
    projectService.createSystemProject(project2, LOGIN1);

    // spaces for project1
    Space space1 = create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    Space space2 = create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // // roles for project1
    ProjectRole role1 = createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    ProjectRole role2 = createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // organization for project1
    Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    project1.setOrganization(org1);

    // validate both
    projectService.validateProject(PROJECT1ID);
    projectService.validateProject(PROJECT2ID);

    List<Project> projects = projectService.getAllProjects(false);

    assertNotNull(projects);
    assertEquals(3, projects.size());

    Project project = projectService.getProject(PROJECT1ID, false);
    assertEquals(RealmType.SYSTEM, project.getRealmType());

    // check raised exception when validating the bean
    Project badBeanproject = prepareProject(ORGANIZATION_NAME1, "", PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(badBeanproject);
    try
    {
      projectService.createSystemProject(badBeanproject, LOGIN1);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised exception when check another project with the same project id doesn't already exist
    Project alreadyExistproject = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME,
        PROJECT_DESCRIPTION1);
    assertNotNull(alreadyExistproject);
    try
    {
      projectService.createSystemProject(alreadyExistproject, LOGIN1);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised exception when check the author username is existing
    Project authorNotExistingproject = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME,
        PROJECT_DESCRIPTION1);
    assertNotNull(authorNotExistingproject);
    try
    {
      projectService.createSystemProject(authorNotExistingproject, LOGIN2);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#deleteProject(java.lang.String, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testDeleteProject() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createProject(project2, LOGIN1, null);

    // spaces for project1
    Space space1 = create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    Space space2 = create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // roles for project1
    ProjectRole role1 = createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    ProjectRole role2 = createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // organization for project1
    Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    project1.setOrganization(org1);

    // validate both
    projectService.validateProject(PROJECT1ID);
    projectService.validateProject(PROJECT2ID);

    List<Project> projects = projectService.getAllProjects(false);
    assertNotNull(projects);
    assertEquals(3, projects.size());

    // delete project
    projectService.deleteProject(PROJECT1ID, LOGIN1);

    List<Project> projectsAfterDelete = projectService.getAllProjects(false);
    assertNotNull(projectsAfterDelete);
    assertEquals(2, projectsAfterDelete.size());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#getAllProjects(boolean)}
   * .
   */
  @Test
  // @Ignore
  public void testGetAllProjects() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createSystemProject(project2, LOGIN1);

    // spaces for project1
    Space space1 = create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    Space space2 = create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // // roles for project1
    ProjectRole role1 = createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    ProjectRole role2 = createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // organization for project1
    Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    project1.setOrganization(org1);

    // validate PROJECT1ID only
    projectService.validateProject(PROJECT1ID);

    // get all validated projects without icon
    List<Project> projectsWithFalse = projectService.getAllProjects(false);
    assertNotNull(projectsWithFalse);
    assertEquals(2, projectsWithFalse.size());
    for (Project projet : projectsWithFalse)
    {
      if (projet.getProjectId().equals(PROJECT1ID))
      {
        BinaryFile image = projet.getImage();
        assertNull(image);
      }
    }

    // get all validated projects with icon
    List<Project> projectsWithTrue = projectService.getAllProjects(true);
    assertNotNull(projectsWithTrue);
    assertEquals(2, projectsWithTrue.size());
    for (Project projet : projectsWithTrue)
    {
      if (projet.getProjectId().equals(PROJECT1ID))
      {
        BinaryFile image = projet.getImage();
        assertNotNull(image);
      }
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#getAllProjectsByStatus(org.novaforge.forge.core.organization.model.enumerations.ProjectStatus, boolean)}
   * .
   */
  @Test
  // @Ignore
  public void testGetAllProjectsByStatus() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);
    Project project3 = prepareProject(ORGANIZATION_NAME1, PROJECT3ID, PROJECT3NAME, PROJECT_DESCRIPTION3);
    assertNotNull(project3);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createProject(project2, LOGIN1, null);
    projectService.createProject(project3, LOGIN1, null);

    // spaces for project1
    Space space1 = create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    Space space2 = create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // // roles for project1
    ProjectRole role1 = createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    ProjectRole role2 = createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // organization for project1
    Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    project1.setOrganization(org1);

    // validate PROJECT1ID only
    projectService.validateProject(PROJECT1ID);

    // reject project3
    projectService.rejectProject(PROJECT3ID, "not authorized");

    // get all "to be validated" projects without icon
    List<Project> projectsToBeValidated = projectService.getAllProjectsByStatus(
        ProjectStatus.TO_BE_VALIDATED, true);
    assertNotNull(projectsToBeValidated);
    assertEquals(1, projectsToBeValidated.size());
    for (Project projet : projectsToBeValidated)
    {
      if (projet.getProjectId().equals(PROJECT1ID))
      {
        BinaryFile image = projet.getImage();
        assertNotNull(image);
      }
    }

    // get all "validated" projects with icon
    List<Project> projectsValidated = projectService.getAllProjectsByStatus(ProjectStatus.VALIDATED, true);
    assertNotNull(projectsValidated);
    assertEquals(2, projectsValidated.size());
    for (Project projet : projectsValidated)
    {
      if (projet.getProjectId().equals(PROJECT1ID))
      {
        BinaryFile image = projet.getImage();
        assertNotNull(image);
      }
    }

    // get all "rejected" projects with icon
    List<Project> projectsRejected = projectService.getAllProjectsByStatus(ProjectStatus.REJECTED, true);
    assertNotNull(projectsRejected);
    assertEquals(1, projectsRejected.size());
    for (Project projet : projectsRejected)
    {
      if (projet.getProjectId().equals(PROJECT3ID))
      {
        BinaryFile image = projet.getImage();
        assertNotNull(image);
      }
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#getProject(java.lang.String, boolean)}
   * .
   */
  @Test
  // @Ignore
  public void testGetProject() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createProject(project2, LOGIN1, null);

    // validate project1
    projectService.validateProject(PROJECT1ID);

    // spaces for project1
    create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // roles for project1
    createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // get project1 with icon
    Project project_1_true = projectService.getProject(PROJECT1ID, true);
    assertNotNull(project_1_true);
    BinaryFile image = project_1_true.getImage();
    assertNotNull(image);
    assertEquals(PROJECT_DESCRIPTION1, project_1_true.getDescription());
    assertEquals(PROJECT1NAME, project_1_true.getName());
    assertEquals(LGPL, project_1_true.getLicenceType());
    assertEquals(PROJECT1ID, project_1_true.getProjectId());
    assertEquals(ProjectStatus.VALIDATED, project_1_true.getStatus());
    assertEquals(RealmType.USER, project_1_true.getRealmType());

    // get project1 without icon
    Project project_1_false = projectService.getProject(PROJECT1ID, false);
    assertNotNull(project_1_false);
    BinaryFile image_false = project_1_false.getImage();
    assertNull(image_false);
    assertEquals(PROJECT_DESCRIPTION1, project_1_true.getDescription());
    assertEquals(PROJECT1NAME, project_1_true.getName());
    assertEquals(LGPL, project_1_true.getLicenceType());
    assertEquals(PROJECT1ID, project_1_true.getProjectId());
    assertEquals(ProjectStatus.VALIDATED, project_1_true.getStatus());
    assertEquals(RealmType.USER, project_1_true.getRealmType());

    // get project2 (not validated)
    Project project_2_true = projectService.getProject(PROJECT2ID, true);
    assertNotNull(project_2_true);
    BinaryFile image2 = project_2_true.getImage();
    assertNotNull(image2);

  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#isForgeProject(java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testIsForgeProject() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createProject(project2, LOGIN1, null);

    // spaces for project1
    Space space1 = create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    Space space2 = create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // // roles for project1
    ProjectRole role1 = createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    ProjectRole role2 = createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // organization for project1
    Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    project1.setOrganization(org1);

    // is forge project ?
    boolean isNotForgeProject = projectService.isForgeProject(PROJECT1ID);
    assertFalse(isNotForgeProject);

    boolean isForgeProject = projectService.isForgeProject("forge");
    assertTrue(isForgeProject);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#updateProject(java.lang.String, org.novaforge.forge.core.organization.model.Project, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testUpdateProject() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createProject(project2, LOGIN1, null);

    // validate project1
    projectService.validateProject(PROJECT1ID);

    // spaces for project1
    create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // roles for project1
    createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // get project1
    Project project_1 = projectService.getProject(PROJECT1ID, true);

    // updating got bean
    project_1.setDescription(PROJECT_DESCRIPTION2);
    project_1.setName("project1new");
    project_1.setLicenceType("GPL");

    // TODO ?? updating for roles, ...

    // updating project
    projectService.updateProject(PROJECT1ID, project_1, LOGIN1);

    // check
    assertNotNull(project_1);
    assertEquals(PROJECT_DESCRIPTION2, project_1.getDescription());
    assertEquals("project1new", project_1.getName());
    assertEquals("GPL", project_1.getLicenceType());
    assertEquals(PROJECT1ID, project_1.getProjectId());
    assertEquals(ProjectStatus.VALIDATED, project_1.getStatus());
    assertEquals(RealmType.USER, project_1.getRealmType());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#validateProject(java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testValidateProject() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createProject(project2, LOGIN1, null);

    // spaces for project1
    Space space1 = create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    Space space2 = create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // // roles for project1
    ProjectRole role1 = createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    ProjectRole role2 = createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // organization for project1
    Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    project1.setOrganization(org1);

    // validate PROJECT1ID only
    projectService.validateProject(PROJECT1ID);

    // get all validated projects without icon
    List<Project> projects_1 = projectService.getAllProjects(false);
    assertNotNull(projects_1);
    assertEquals(2, projects_1.size());

    // validate PROJECT2ID also
    projectService.validateProject(PROJECT2ID);

    // get all validated projects without icon
    List<Project> projects_2 = projectService.getAllProjects(false);
    assertNotNull(projects_2);
    assertEquals(3, projects_2.size());

  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#rejectProject(java.lang.String, java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testRejectProject() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createProject(project2, LOGIN1, null);

    // spaces for project1
    Space space1 = create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    Space space2 = create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // // roles for project1
    ProjectRole role1 = createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    ProjectRole role2 = createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // organization for project1
    Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    project1.setOrganization(org1);

    // validate PROJECT1ID only
    projectService.rejectProject(PROJECT1ID, "Not conformed");

    // get all validated projects without icon
    List<Project> projects_1 = projectService.getAllProjects(false);
    assertNotNull(projects_1);
    assertEquals(1, projects_1.size());

    List<Project> projectsRejected = projectService.getAllProjectsByStatus(ProjectStatus.REJECTED, false);
    assertEquals(1, projectsRejected.size());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.ProjectServiceImpl#getProjectInfo(java.lang.String)}
   * .
   */
  @Test
  // @Ignore
  public void testGetProjectInfo() throws Exception
  {
    // user1
    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    Project project1 = prepareProject(ORGANIZATION_NAME1, PROJECT1ID, PROJECT1NAME, PROJECT_DESCRIPTION1);
    assertNotNull(project1);
    Project project2 = prepareProject(ORGANIZATION_NAME1, PROJECT2ID, PROJECT2NAME, PROJECT_DESCRIPTION2);
    assertNotNull(project2);

    // create project
    projectService.createProject(project1, LOGIN1, null);
    projectService.createProject(project2, LOGIN1, null);

    // spaces for project1
    Space space1 = create_space(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
    Space space2 = create_space(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);

    // // roles for project1
    ProjectRole role1 = createRole(PROJECT1ID, ROLE1NAME, ROLE1DESC);
    ProjectRole role2 = createRole(PROJECT1ID, ROLE2NAME, ROLE2DESC);

    // organization for project1
    Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    project1.setOrganization(org1);

    // validate project1
    projectService.validateProject(PROJECT1ID);

    // get projectinfo from project1
    ProjectInfo projectInfo = projectService.getProjectInfo(PROJECT1ID);
    assertNotNull(projectInfo);
    assertEquals(PROJECT_DESCRIPTION1, projectInfo.getDescription());
    assertEquals(PROJECT1NAME, projectInfo.getName());
    assertEquals(LGPL, projectInfo.getLicenceType());
    assertEquals(PROJECT1ID, projectInfo.getProjectId());
  }

  // private methods

  /**
   * @param pOrganizationName
   * @param pProjectName
   * @param pProjectDesc
   * @param pProjectId1
   * @return
   * @throws Exception
   */
  private Project prepareProject(String pOrganizationName, String pProjectId, String pProjectName,
      String pProjectDesc) throws Exception
  {
    // project1
    final Project project1 = projectService.newProject();
    project1.setProjectId(pProjectId);
    project1.setLicenceType(LGPL);
    project1.setName(pProjectName);
    project1.setDescription(pProjectDesc);

    // organization for project1
    // Organization org1 = createOrganozation(ORGANIZATION_NAME1);
    // project1.setOrganization(org1);

    // TODO: add image ??
    byte buffer[] = { 1, 2, 3, 4 };
    BinaryFile binaryFile = project1.getImage();
    binaryFile.setFile(buffer);
    project1.setImage(binaryFile);

    project1.setPrivateVisibility(false);
    project1.setRealmType(RealmType.USER);

    // TODO ad request ?
    // project1.setRequests(null);

    project1.setStatus(ProjectStatus.TO_BE_VALIDATED);
    return project1;
  }

  /**
   * @param pOrganizationName
   * @return
   * @throws OrganizationServiceException
   */
  private Organization createOrganozation(String pOrganizationName) throws OrganizationServiceException
  {
    Organization org = organizationService.newOrganization();
    org.setName(pOrganizationName);
    Organization organization1 = organizationService.createOrganization(org);
    return organization1;
  }

  /**
   * @throws ProjectServiceException
   */
  private ProjectRole createRole(String pProjectId, String pRoleName, String pRoleDesc)
      throws ProjectServiceException
  {
    ProjectRole role = createRole(pRoleName, pRoleDesc, RealmType.USER);
    projectRoleService.createRole(role, pProjectId);
    return role;
  }

  /**
   * @param pProjectId
   * @return
   * @throws SpaceServiceException
   */
  private Space create_space(String pProjectId, String pSpaceName, String pSpaceDesc)
      throws SpaceServiceException
  {
    Space space1 = spaceService.newSpace();
    space1.setName(pSpaceName);
    space1.setDescription(pSpaceDesc);
    space1.setUri(pProjectId + "/" + pSpaceName);
    spaceService.addSpace(PROJECT1ID, space1);
    return space1;
  }

  /**
   * @param roleName
   * @param roleDesc
   * @param realmType
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

  /**
   * @param name
   * @param firstName
   * @param eMail
   * @param password
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

}
