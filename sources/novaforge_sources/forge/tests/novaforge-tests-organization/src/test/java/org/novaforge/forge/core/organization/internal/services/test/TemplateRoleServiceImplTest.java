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

import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Role;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.enumerations.RealmType;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;
import org.novaforge.forge.core.organization.services.SpaceService;
import org.novaforge.forge.core.organization.services.TemplateRoleService;
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
public class TemplateRoleServiceImplTest extends BaseUtil
{
  /** template Id test 1. */
  private static final String                ID1                 = "id1";

  /** template name test 1. */
  private static final String                NAME1               = "name1";

  /** template name test 2. */
  private static final String                NAME2               = "name2";

  /** template name test 3. */
  private static final String                NAME3               = "name3";

  /** description test 1. */
  private static final String                DESCRIPTION1        = "description1";

  /** description test 2. */
  private static final String                DESCRIPTION2        = "description2";

  /** description test 3. */
  private static final String                DESCRIPTION3        = "description3";

  /** realm type user */
  private static final RealmType             REALMTYPE_USER      = RealmType.USER;

  /** realm type system */
  private static final RealmType             REALMTYPE_SYSTEM    = RealmType.SYSTEM;

  /** realm type anonymous */
  private static final RealmType             REALMTYPE_ANONYMOUS = RealmType.ANONYMOUS;

  /** Status test 1 */
  private static final TemplateProjectStatus STATUS_ENABLE       = TemplateProjectStatus.ENABLE;

  /** Role test order 1. */
  private static final Integer               ORDER1              = 1;

  /** Role test order 2. */
  private static final Integer               ORDER2              = 2;

  /** Role test order 3. */
  private static final Integer               ORDER3              = 3;

  // spaces
  private static final String                SPACE_NAME1         = "space1";
  private static final String                SPACE_NAME2         = "space2";

  private static final String                SPACE_DESC1         = "space1 description";
  private static final String                SPACE_DESC2         = "space2 description";

  /** project test id. */
  private static final String                PROJECT1ID          = "project1";

  /** Template Service. */
  @Inject
  private TemplateService                    templateService;

  /** Template Role Service to test. */
  @Inject
  private TemplateRoleService                templateRoleService;

  @Inject
  private SpaceService                       spaceService;

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateRoleServiceImpl#createRole(org.novaforge.forge.core.organization.model.Role, String)}
   * 
   * @throws TemplateServiceException
   */
  @Test
// @Ignore
  public void testCreateRole() throws TemplateServiceException
  {
    initTemplate();

    // Creating the new Role
    Role newRole = createRole(NAME1, DESCRIPTION1, ORDER1, REALMTYPE_USER);
    templateRoleService.createRole(newRole, ID1);

    // Getting the role and checking its props
    final Role createdRole = templateRoleService.getRole(NAME1, ID1);
    Assert.assertNotNull(createdRole);
    Assert.assertEquals(NAME1, createdRole.getName());
    Assert.assertEquals(DESCRIPTION1, createdRole.getDescription());
    Assert.assertEquals(ORDER1, createdRole.getOrder());
    Assert.assertEquals(RealmType.USER, createdRole.getRealmType());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateRoleServiceImpl#createSystemRole(org.novaforge.forge.core.organization.model.Role, String)}
   * 
   * @throws TemplateServiceException
   */
  @Test
// @Ignore
  public void testCreateSystemRole() throws TemplateServiceException
  {
    initTemplate();

    // Creating the new Role
    Role newRole = createRole(NAME1, DESCRIPTION1, ORDER1, REALMTYPE_USER);
    templateRoleService.createSystemRole(newRole, ID1);

    // Getting the role and checking its props
    final Role createdRole = templateRoleService.getRole(NAME1, ID1);
    Assert.assertNotNull(createdRole);
    Assert.assertEquals(NAME1, createdRole.getName());
    Assert.assertEquals(DESCRIPTION1, createdRole.getDescription());
    Assert.assertEquals(ORDER1, createdRole.getOrder());
    Assert.assertEquals(RealmType.SYSTEM, createdRole.getRealmType());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateRoleServiceImpl#deleteRole(org.novaforge.forge.core.organization.model.Role , String )}
   * 
   * @throws TemplateServiceException
   */
  @Test(expected = NoResultException.class)
// @Ignore
  public void testDeleteRole() throws TemplateServiceException
  {
    initTemplate();

    // Creating the new Role
    Role newRole = createRole(NAME1, DESCRIPTION1, ORDER1, REALMTYPE_USER);
    templateRoleService.createRole(newRole, ID1);

    // Deleting the created role
    templateRoleService.deleteRole(NAME1, ID1);

    // No Role => NoResulExpected
    templateRoleService.getRole(NAME1, ID1);
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateRoleServiceImpl#getAllRoles(String)}
   * 
   * @throws TemplateServiceException
   */
  @Test
// @Ignore
  public void testGetAllRoles() throws TemplateServiceException
  {
    initTemplate();
    // Getting the initial size
    int initialSize = templateRoleService.getAllRoles(ID1).size();

    // Creating the new Role
    Role newRole = createRole(NAME1, DESCRIPTION1, ORDER1, REALMTYPE_USER);
    templateRoleService.createRole(newRole, ID1);

    // Checking size
    Assert.assertEquals(initialSize + 1, templateRoleService.getAllRoles(ID1).size());

    // Creating the new Role
    newRole = createRole(NAME2, DESCRIPTION2, ORDER2, REALMTYPE_SYSTEM);
    templateRoleService.createRole(newRole, ID1);

    // Checking size
    Assert.assertEquals(initialSize + 2, templateRoleService.getAllRoles(ID1).size());

    // Creating the new Role
    newRole = createRole(NAME3, DESCRIPTION3, ORDER3, REALMTYPE_ANONYMOUS);
    templateRoleService.createRole(newRole, ID1);

    // Checking size
    Assert.assertEquals(initialSize + 3, templateRoleService.getAllRoles(ID1).size());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateRoleServiceImpl#getRole(org.novaforge.forge. core.organization.model.Role , String )}
   * 
   * @throws TemplateServiceException
   */
  @Test
// @Ignore
  public void testGetRole() throws TemplateServiceException
  {
    initTemplate();

    // Creating the new Role
    Role newRole = createRole(NAME1, DESCRIPTION1, ORDER1, REALMTYPE_USER);
    templateRoleService.createRole(newRole, ID1);

    // Getting the role and checking its props
    final Role createdRole = templateRoleService.getRole(NAME1, ID1);
    Assert.assertNotNull(createdRole);
    Assert.assertEquals(NAME1, createdRole.getName());
    Assert.assertEquals(DESCRIPTION1, createdRole.getDescription());
    Assert.assertEquals(ORDER1, createdRole.getOrder());
    Assert.assertEquals(RealmType.USER, createdRole.getRealmType());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateRoleServiceImpl#changeOrder(String , org.novaforge.forge.core.organization.model.Role , boolean )}
   * 
   * @throws TemplateServiceException
   */
  @Test
// @Ignore
  public void testChangeOrder() throws TemplateServiceException
  {
    initTemplate();

    // Creating the new Role
    Role newRole1 = createRole(NAME1, DESCRIPTION1, ORDER1, REALMTYPE_USER);
    templateRoleService.createRole(newRole1, ID1);

    Role newRole2 = createRole(NAME2, DESCRIPTION2, ORDER2, REALMTYPE_USER);
    templateRoleService.createRole(newRole2, ID1);

    // Changing order (+1)
    templateRoleService.changeOrder(ID1, NAME1, true);

    // Getting the role and checking its props
    Role createdRole = templateRoleService.getRole(NAME1, ID1);
    Assert.assertEquals(ORDER1.intValue() + 1, createdRole.getOrder().intValue());

    // Changing order (-1)
    templateRoleService.changeOrder(ID1, NAME1, false);

    // Getting the role and checking its props
    createdRole = templateRoleService.getRole(NAME1, ID1);
    Assert.assertEquals(ORDER1.intValue(), createdRole.getOrder().intValue());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateRoleServiceImpl#updateRole(String, org.novaforge.forge.core.organization.model.Role , String )}
   * 
   * @throws TemplateServiceException
   */
  @Test
// @Ignore
  public void testUpdateRole() throws TemplateServiceException
  {
    initTemplate();

    // Creating the new Role
    Role newRole1 = createRole(NAME1, DESCRIPTION1, ORDER1, REALMTYPE_USER);
    templateRoleService.createRole(newRole1, ID1);

    // Creating the new Role
    Role newRole2 = createRole(NAME2, DESCRIPTION2, ORDER2, REALMTYPE_USER);
    templateRoleService.createRole(newRole2, ID1);

    // Getting the role and checking its props
    Role createdRole = templateRoleService.getRole(NAME1, ID1);
    Assert.assertNotNull(createdRole);
    Assert.assertEquals(NAME1, createdRole.getName());
    Assert.assertEquals(DESCRIPTION1, createdRole.getDescription());
    Assert.assertEquals(ORDER1, createdRole.getOrder());
    Assert.assertEquals(RealmType.USER, createdRole.getRealmType());

    // updating role
    createdRole.setDescription(DESCRIPTION2);
    createdRole.setOrder(ORDER2);
    createdRole.setRealmType(REALMTYPE_ANONYMOUS);
    templateRoleService.updateRole(NAME1, createdRole, ID1);

    // Checking updated role
    Role updatedRole = templateRoleService.getRole(NAME1, ID1);
    Assert.assertNotNull(updatedRole);
    Assert.assertEquals(NAME1, updatedRole.getName());
    Assert.assertEquals(DESCRIPTION2, updatedRole.getDescription());
    Assert.assertEquals(ORDER2, updatedRole.getOrder());
    Assert.assertEquals(REALMTYPE_ANONYMOUS, updatedRole.getRealmType());
  }

  /**
   * Test method for
   * {@link org.novaforge.forge.core.organization.internal.services.TemplateRoleServiceImpl#getDefaultRoles()}
   * 
   * @throws TemplateServiceException
   */
  @Test
  // @Ignore
  public void testGetDefaultRoles() throws TemplateServiceException
  {
    // Getting the default role
    List<Role> defaultRoles = templateRoleService.getDefaultRoles();
    Assert.assertTrue(defaultRoles.size() > 0);
    Role defaultRole = defaultRoles.get(0);
    // Checking props
    Assert.assertNotNull(defaultRole);
    Assert.assertNotNull(defaultRole.getName());
    Assert.assertEquals(ORDER1, defaultRole.getOrder());
    Assert.assertEquals(RealmType.SYSTEM, defaultRole.getRealmType());
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
  private Template createTemplate(String templateId, String templateName, String templateDescription,
      TemplateProjectStatus templateStatus)
  {
    Template template = templateService.newTemplate();
    template.setElementId(templateId);
    template.setName(templateName);
    template.setDescription(templateDescription);
    template.setStatus(templateStatus);
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
   * @param pName
   *          name
   * @param pDescription
   *          description
   * @param pOrder
   *          order
   * @param pRealmType
   *          Realm type
   * @return a new Role
   */
  private Role createRole(final String pName, final String pDescription, Integer pOrder, RealmType pRealmType)
  {
    final Role role = templateRoleService.newRole();
    role.setName(pName);
    role.setDescription(pDescription);
    role.setOrder(pOrder);
    role.setRealmType(pRealmType);
    return role;
  }

  /**
   * Create a template.
   * 
   * @throws TemplateServiceException
   */
  private void initTemplate() throws TemplateServiceException
  {
    Assert.assertNotNull(templateService);

    // template1
    final Template newTemplate = createTemplate(ID1, NAME1, DESCRIPTION1, STATUS_ENABLE);
    templateService.createTemplate(newTemplate);
  }
}
