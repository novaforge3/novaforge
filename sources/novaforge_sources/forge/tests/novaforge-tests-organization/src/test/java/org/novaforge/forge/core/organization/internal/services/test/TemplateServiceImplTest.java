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
import static org.junit.Assert.fail;

import java.util.List;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.novaforge.forge.core.organization.exceptions.LanguageServiceException;
import org.novaforge.forge.core.organization.exceptions.ProjectServiceException;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.exceptions.UserServiceException;
import org.novaforge.forge.core.organization.model.Language;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.User;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;
import org.novaforge.forge.core.organization.services.CommonUserService;
import org.novaforge.forge.core.organization.services.LanguageService;
import org.novaforge.forge.core.organization.services.ProjectService;
import org.novaforge.forge.core.organization.services.SpaceService;
import org.novaforge.forge.core.organization.services.TemplateService;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

/**
 * Test class for TemplateRoleServiceImpl
 * 
 * @author martinelli-b, blachon-m
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class TemplateServiceImplTest extends BaseUtil
{
  /** Id test 1. */
  private static final String                TEMPLATE_ID1             = "template_id1";

  /** Id test 2. */
  private static final String                TEMPLATE_ID2             = "template_id2";

  /** name test 1. */
  private static final String                TEMPLATE_NAME1           = "template_name1";

  /** name test 2. */
  private static final String                TEMPLATE_NAME2           = "template_name2";

  /** description test 1. */
  private static final String                TEMPLATE_DESCRIPTION1    = "description1";

  /** description test 2. */
  private static final String                TEMPLATE_DESCRIPTION2    = "description2";

  /** status test 1 */
  private static final TemplateProjectStatus TEMPLATE_PROJECT_STATUS1 = TemplateProjectStatus.IN_PROGRESS;

  /** status test 2. */
  private static final TemplateProjectStatus TEMPLATE_PROJECT_STATUS2 = TemplateProjectStatus.ENABLE;

  /** fake test id. */
  private static final String                FAKEID                   = "fjsdfsflmsfls4s13d4s6f";

  /** project test id. */
  private static final String                PROJECT1ID               = "project1";

  /** project test name. */
  private static final String                PROJECT1NAME             = "project 1";

  /** project test description. */
  private static final String                PROJECT_DESCRIPTION1     = "description 1";

  /** project test licence. */
  private static final String                LGPL                     = "LGPL";

  /** project test author. */
  private static final String                ADMIN1                   = "admin1";

  private static final String                LANGUAGE_FR_NAME         = "FR";

  // user1
  private static final String                NAME1                    = "name1";
  private static final String                FIRSTNAME1               = "firstname1";
  private static final String                LOGIN1                   = "name1-f";
  private static final String                EMAIL1                   = "mailUser1@toto.fr";
  private static final String                PASSWORD1                = "password1";

  // spaces
  private static final String                SPACE_NAME1              = "space1";
  private static final String                SPACE_NAME2              = "space2";

  private static final String                SPACE_DESC1              = "space1 description";
  private static final String                SPACE_DESC2              = "space2 description";

  /** Template Service to test. */
  @Inject
  private TemplateService                    templateService;

  /** Project Service. */
  @Inject
  private ProjectService                     projectService;

  @Inject
  private CommonUserService                  commonUserService;

  @Inject
  private LanguageService                    languageService;

  @Inject
  private SpaceService                       spaceService;

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#createTemplate(org.novaforge.forge.core.organization.model.Template)}
   * 
   * @throws TemplateServiceException
   */
  @Test
  // @Ignore
  public void testCreateTemplate() throws TemplateServiceException
  {
    assertNotNull(templateService);

    // new template
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    templateService.createTemplate(newTemplate);

    // Checking all properties of the created template
    final Template createdTemplate = templateService.getTemplate(TEMPLATE_ID1);
    assertNotNull(createdTemplate);
    // getElementId() and getTemplateId() refer to the same object
    assertEquals(TEMPLATE_ID1, createdTemplate.getElementId());
    assertEquals(TEMPLATE_ID1, createdTemplate.getTemplateId());
    assertEquals(TEMPLATE_DESCRIPTION1, createdTemplate.getDescription());
    assertEquals(TEMPLATE_NAME1, createdTemplate.getName());
    assertEquals(TEMPLATE_PROJECT_STATUS1, createdTemplate.getStatus());

    // check raised with code: ExceptionCode.ERR_VALIDATION_BEAN
    // empty ID
    try
    {
      createTemplate("", TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1, TEMPLATE_PROJECT_STATUS1);
      templateService.createTemplate(newTemplate);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised with code: ExceptionCode.ERR_VALIDATION_BEAN
    // empty name
    try
    {
      createTemplate(TEMPLATE_ID1, "", TEMPLATE_DESCRIPTION1, TEMPLATE_PROJECT_STATUS1);
      templateService.createTemplate(newTemplate);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised with code: ExceptionCode.ERR_VALIDATION_BEAN
    // empty description
    try
    {
      createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, "", TEMPLATE_PROJECT_STATUS1);
      templateService.createTemplate(newTemplate);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised with code: ExceptionCode.ERR_VALIDATION_BEAN
    // null status
    try
    {
      createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1, null);
      templateService.createTemplate(newTemplate);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised with code: ExceptionCode.ERR_CREATE_TEMPLATE_ID_ALREADY_EXIST
    try
    {
      createTemplate(TEMPLATE_ID1, TEMPLATE_NAME2, TEMPLATE_DESCRIPTION1, TEMPLATE_PROJECT_STATUS1);
      templateService.createTemplate(newTemplate);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }

    // check raised with code: ExceptionCode.ERR_CREATE_TEMPLATE_NAME_ALREADY_EXIST
    try
    {
      createTemplate(TEMPLATE_ID2, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1, TEMPLATE_PROJECT_STATUS1);
      templateService.createTemplate(newTemplate);
      fail("should has raised an exception");
    }
    catch (final Exception ne)
    {
      System.out.println("expected exception!");
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#deleteTemplate(String)}
   * 
   * @throws TemplateServiceException
   */
  @Test
  // @Ignore
  public void testDeleteTemplate() throws TemplateServiceException
  {
    // New template
    final Template template = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    templateService.createTemplate(template);
    final Template createdTemplate = templateService.getTemplate(TEMPLATE_ID1);
    assertNotNull(createdTemplate);
    templateService.deleteTemplate(TEMPLATE_ID1);

    // No result expected => TemplateServiceException
    try
    {
      templateService.getTemplate(TEMPLATE_ID1);
      fail("should has raised an exception");
    }
    catch (final TemplateServiceException ne)
    {
      System.out.println("expected exception!");
    }

    // check raised TemplateServiceException when trying to delete a none existing template
    try
    {
      templateService.deleteTemplate(FAKEID);
      fail("should has raised an exception");
    }
    catch (final TemplateServiceException ne)
    {
      System.out.println("expected exception!");
    }
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#deleteTemplateInstance(String)}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   * @throws LanguageServiceException
   * @throws UserServiceException
   */
  @Test
  // @Ignore
  public void testDeleteTemplateInstance() throws TemplateServiceException, ProjectServiceException,
      LanguageServiceException, UserServiceException
  {
    // New Template
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    templateService.createTemplate(newTemplate);

    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    // New Project instanciating the template
    final Project newProject = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(newProject, LOGIN1, newTemplate.getElementId());

    try
    {
      // Deleting the created template
      templateService.deleteTemplateInstance(PROJECT1ID);
    }
    catch (final Exception e)
    {
      Assert.fail("La suppression du template créé doit fonctionner.");
    }
    // Checking if there's no instance of the template
    Assert.assertNull(templateService.getTemplateForProject(PROJECT1ID));
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#getTemplateForProject(org.novaforge.forge.core.organization.model.Template)}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   * @throws LanguageServiceException
   * @throws UserServiceException
   */
  @Test
  // @Ignore
  public void testGetTemplateForProject() throws TemplateServiceException, ProjectServiceException,
      LanguageServiceException, UserServiceException
  {
    // New template
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    templateService.createTemplate(newTemplate);

    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    // New project
    final Project newProject = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(newProject, LOGIN1, newTemplate.getElementId());

    // Checking if there's a template in the created project
    Assert.assertEquals(TEMPLATE_ID1, templateService.getTemplateForProject(PROJECT1ID).getElementId());
    Assert.assertEquals(TEMPLATE_NAME1, templateService.getTemplateForProject(PROJECT1ID).getName());
    Assert.assertEquals(TEMPLATE_DESCRIPTION1, templateService.getTemplateForProject(PROJECT1ID)
        .getDescription());
    Assert.assertEquals(TEMPLATE_PROJECT_STATUS1, templateService.getTemplateForProject(PROJECT1ID)
        .getStatus());

    // case no template with the project
    templateService.deleteTemplateInstance(PROJECT1ID);

    Assert.assertNull(templateService.getTemplateForProject(PROJECT1ID));

  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#enableTemplate(String)}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test
  // @Ignore
  public void testEnableTemplate() throws TemplateServiceException, ProjectServiceException
  {
    // Creating a new template with a disabled status
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    newTemplate.setStatus(TemplateProjectStatus.IN_PROGRESS);

    templateService.createTemplate(newTemplate);
    // Enabling the template
    templateService.enableTemplate(TEMPLATE_ID1);

    // Checking status
    final Template getTemplate = templateService.getTemplate(TEMPLATE_ID1);
    Assert.assertEquals(getTemplate.getStatus(), TemplateProjectStatus.ENABLE);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#enableTemplate(String)}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test
  // @Ignore
  public void testEnableTemplateAlreadyEnabled() throws TemplateServiceException, ProjectServiceException
  {
    // Creating a new template with an enabled status
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    newTemplate.setStatus(TemplateProjectStatus.ENABLE);

    templateService.createTemplate(newTemplate);
    // Enabling the template (not supposed to change anything)
    templateService.enableTemplate(TEMPLATE_ID1);

    // Checking status
    final Template getTemplate = templateService.getTemplate(TEMPLATE_ID1);
    Assert.assertEquals(getTemplate.getStatus(), TemplateProjectStatus.ENABLE);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#enableTemplate(String)}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test(expected = TemplateServiceException.class)
  // @Ignore
  public void testEnableNonexistentTemplate() throws TemplateServiceException, ProjectServiceException
  {
    // Enabling a nonexistent template
    templateService.enableTemplate(FAKEID);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#disableTemplate(String)}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test
  // @Ignore
  public void testDisableTemplate() throws TemplateServiceException, ProjectServiceException
  {
    // Creating a new template with an enabled status
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    newTemplate.setStatus(TemplateProjectStatus.ENABLE);

    templateService.createTemplate(newTemplate);
    // Enabling the template
    templateService.disableTemplate(TEMPLATE_ID1);

    // Checking status
    final Template getTemplate = templateService.getTemplate(TEMPLATE_ID1);
    Assert.assertEquals(getTemplate.getStatus(), TemplateProjectStatus.IN_PROGRESS);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#disableTemplate(String)}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test
  // @Ignore
  public void testDisableTemplateAlreadyDisabled() throws TemplateServiceException, ProjectServiceException
  {
    // Creating a new template with a disabled status
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    newTemplate.setStatus(TemplateProjectStatus.IN_PROGRESS);

    templateService.createTemplate(newTemplate);
    // Enabling the template
    templateService.disableTemplate(TEMPLATE_ID1);

    // Checking status
    final Template getTemplate = templateService.getTemplate(TEMPLATE_ID1);
    Assert.assertEquals(getTemplate.getStatus(), TemplateProjectStatus.IN_PROGRESS);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#disableTemplate(String)}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test(expected = TemplateServiceException.class)
  // @Ignore
  public void testDisableNonexistentTemplate() throws TemplateServiceException, ProjectServiceException
  {
    // Disabling a nonexistent template
    templateService.disableTemplate(FAKEID);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#getEnableTemplates()}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   * @throws LanguageServiceException
   * @throws UserServiceException
   */
  @Test
  // @Ignore
  public void testGetEnableTemplates() throws TemplateServiceException, ProjectServiceException,
      LanguageServiceException, UserServiceException
  {
    // Getting the initial size of the list before adding one
    List<Template> listeTemplateInit = null;
    listeTemplateInit = templateService.getEnableTemplates();
    final int tailleListeInit = listeTemplateInit.size();

    // Adding a new enabled template
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    // newTemplate.setStatus(TemplateProjectStatus.ENABLE);
    templateService.createTemplate(newTemplate);

    templateService.enableTemplate(TEMPLATE_ID1);

    final TemplateProjectStatus templateStatus = templateService.getTemplate(TEMPLATE_ID1).getStatus();
    Assert.assertEquals(TemplateProjectStatus.ENABLE, templateStatus);

    final User user1 = createUser(NAME1, FIRSTNAME1, EMAIL1, PASSWORD1);
    commonUserService.createUser(user1);

    // New Project instanciating the template
    final Project newProject = createProject(PROJECT1ID, PROJECT1NAME, LGPL, PROJECT_DESCRIPTION1);
    projectService.createProject(newProject, LOGIN1, newTemplate.getElementId());

    // Checking returned size
    Assert.assertEquals(tailleListeInit + 1, templateService.getEnableTemplates().size());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#getEnableTemplates()}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test
  // @Ignore
  public void testGetEnableTemplatesAddingDisabledTemplate() throws TemplateServiceException,
      ProjectServiceException
  {
    // Getting the initial size of the list before adding one
    List<Template> listeTemplateInit = null;
    listeTemplateInit = templateService.getEnableTemplates();
    final int tailleListeInit = listeTemplateInit.size();

    // Adding a new disabled template
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    newTemplate.setStatus(TemplateProjectStatus.IN_PROGRESS);
    templateService.createTemplate(newTemplate);

    // Checking returned size
    Assert.assertEquals(tailleListeInit, templateService.getEnableTemplates().size());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#updateTemplate(org.novaforge.forge.core.organization.model.Template)}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test
  // @Ignore
  public void testUpdateTemplate() throws TemplateServiceException, ProjectServiceException
  {
    // Creating a template
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    templateService.createTemplate(newTemplate);

    // Changing props
    newTemplate.setName(TEMPLATE_NAME2);
    newTemplate.setDescription(TEMPLATE_DESCRIPTION2);
    newTemplate.setStatus(TEMPLATE_PROJECT_STATUS2);

    // Updating the template
    templateService.updateTemplate(newTemplate);

    // Checking if props have changed
    final Template getTemplate = templateService.getTemplate(TEMPLATE_ID1);
    Assert.assertEquals(TEMPLATE_NAME2, getTemplate.getName());
    Assert.assertEquals(TEMPLATE_DESCRIPTION2, getTemplate.getDescription());
    Assert.assertEquals(TEMPLATE_PROJECT_STATUS2, getTemplate.getStatus());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#getTemplates()}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test
  // @Ignore
  public void testGetTemplates() throws TemplateServiceException, ProjectServiceException
  {
    // Getting the initial size of the list before adding one
    List<Template> listeTemplateInit = null;
    listeTemplateInit = templateService.getTemplates();
    final int tailleListeInit = listeTemplateInit.size();

    // Adding a new template with a status
    Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    newTemplate.setStatus(TemplateProjectStatus.IN_PROGRESS);
    templateService.createTemplate(newTemplate);

    // Checking returned size
    Assert.assertEquals(tailleListeInit + 1, templateService.getTemplates().size());

    // Adding a new template with another status
    newTemplate = createTemplate(TEMPLATE_ID2, TEMPLATE_NAME2, TEMPLATE_DESCRIPTION2,
        TEMPLATE_PROJECT_STATUS2);
    newTemplate.setStatus(TemplateProjectStatus.ENABLE);
    templateService.createTemplate(newTemplate);

    // Checking returned size
    Assert.assertEquals(tailleListeInit + 2, templateService.getTemplates().size());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateServiceImpl#getTemplate(String))}
   * 
   * @throws TemplateServiceException
   * @throws ProjectServiceException
   */
  @Test
  // @Ignore
  public void testGetTemplate() throws TemplateServiceException, ProjectServiceException
  {
    assertNotNull(templateService);

    // New template
    final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESCRIPTION1,
        TEMPLATE_PROJECT_STATUS1);
    templateService.createTemplate(newTemplate);

    // check props
    final Template createdTemplate = templateService.getTemplate(TEMPLATE_ID1);
    assertNotNull(createdTemplate);
    assertEquals(TEMPLATE_ID1, createdTemplate.getElementId());
    assertEquals(TEMPLATE_DESCRIPTION1, createdTemplate.getDescription());
    assertEquals(TEMPLATE_NAME1, createdTemplate.getName());
    assertEquals(TEMPLATE_PROJECT_STATUS1, createdTemplate.getStatus());
  }

  /**
   * Method designed to create dummy objects.
   * 
   * @param templateId
   *          template Id
   * @param templateName
   *          template Name
   * @param templateDescription
   *          template Description
   * @param templateStatus
   *          template Status
   * @return a new template
   */
  private Template createTemplate(final String templateId, final String templateName,
      final String templateDescription, final TemplateProjectStatus templateStatus)
  {
    final Template template = templateService.newTemplate();
    template.setElementId(templateId);

    template.setName(templateName);
    template.setDescription(templateDescription);
    template.setStatus(templateStatus);
    template.setSpaces(null);
    // spaces
    final Space space1 = spaceService.newSpace();
    space1.setName(SPACE_NAME1);
    space1.setDescription(SPACE_DESC1);
    space1.setUri(PROJECT1ID + "/" + SPACE_NAME1);

    final Space space2 = spaceService.newSpace();
    space2.setName(SPACE_NAME2);
    space2.setDescription(SPACE_DESC2);
    space2.setUri(PROJECT1ID + "/" + SPACE_NAME2);
    return template;
  }

  /**
   * Method designed to create dummy objects.
   * 
   * @param projectId
   *          projectId
   * @param projectName
   *          projectName
   * @param licence
   *          licence
   * @param description
   *          description
   * @return a new project
   */
  private Project createProject(final String projectId, final String projectName, final String licence,
      final String description)
  {
    final Project project1 = projectService.newProject();
    project1.setProjectId(projectId);
    project1.setLicenceType(licence);
    project1.setName(projectName);
    project1.setDescription(description);
    return project1;
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
