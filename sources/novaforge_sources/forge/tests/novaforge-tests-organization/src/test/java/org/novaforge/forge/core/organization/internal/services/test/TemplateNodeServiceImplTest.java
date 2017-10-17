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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.novaforge.forge.core.organization.exceptions.TemplateServiceException;
import org.novaforge.forge.core.organization.model.Space;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateApplication;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;
import org.novaforge.forge.core.organization.services.SpaceService;
import org.novaforge.forge.core.organization.services.TemplateNodeService;
import org.novaforge.forge.core.organization.services.TemplateService;
import org.ops4j.pax.exam.junit.ExamReactorStrategy;
import org.ops4j.pax.exam.junit.JUnit4TestRunner;
import org.ops4j.pax.exam.spi.reactors.AllConfinedStagedReactorFactory;

/**
 * Test class for TemplateNodeServiceImpl
 * 
 * @author blachon-m
 */
@RunWith(JUnit4TestRunner.class)
@ExamReactorStrategy(AllConfinedStagedReactorFactory.class)
public class TemplateNodeServiceImplTest extends BaseUtil
{
   // template
   private static final String TEMPLATE_ID1 = "template_id1";

   /** name test 1. */
   private static final String TEMPLATE_NAME1 = "name1";

   /** description test 1. */
   private static final String TEMPLATE_DESC1 = "description1";

   /** Status test 1 */
   private static final TemplateProjectStatus TEMPLATE_STATUS_IN_PROGRESS = TemplateProjectStatus.IN_PROGRESS;

   /** project **/
   private static final String PROJECT1ID           = "project1";
   
   /** space1 **/
   private static final String SPACE_NAME1 = "domaine1";
   private static final String SPACE_DESC1 = "domaine 1";
   
   private static final String SPACE_DESC1_MODIFIED = "domaine 1 modified";
   
   /** space2 **/
   private static final String SPACE_NAME2 = "domaine2";
   private static final String SPACE_DESC2 = "domaine 2";
   
   private static final String SPACE_WRONG_NAME = "titi";
   
   /** Parent Node URI. */
   //private static final String PARENTNODEURI = PROJECT1ID + "/" + SPACE_NAME1;
   private static final String PARENTNODEURI = SPACE_NAME1;

   /** Application label. */
   private static final String APPLICATION1LABEL = "application1";
   private static final String APPLICATION2LABEL = "application2";
   
   /** application URI **/
   private static final String APPLICATION1URI = SPACE_NAME1 + "/" + APPLICATION1LABEL;
   private static final String APPLICATION_WRONG_URI = "toto";

   /** Plugin UUID. */
   private static final UUID PLUGINUUID = new UUID(0, 1);
   
   /* project1 roles */
   private static final String PROJ1_ADMIN="Administrateur";
   private static final String PROJ1_DEV="developpeur";
   private static final String PROJ1_TEST="testeur";
   
   /* app1 roles */
   private static final String APP1_ADMIN="admin1";
   private static final String APP1_DEV="dev1";
   private static final String APP1_TEST="test1";
   
   /* app2 roles */
   private static final String APP2_ADMIN="admin2";
   private static final String APP2_DEV="dev2";
   private static final String APP2_TEST="test2";

  

   

  

   /** Roles Mapping. */
   /*
    *    Role mapping ex. for mantis
    * "<projectId>, <application name>, <proj role>, <app role>
    * "projettest1","mantis appli","Administrator","Administrator"
    * "projettest1","mantis appli","developpeur","Developer"
    * "projettest1","mantis appli","testeur","Tester"
    * 
    * "projettest1","CMS appli","Administrator","Administrator"
    * "projettest1","CMS appli","developpeur","Author"
    * "projettest1","CMS appli","testeur","Visitor"
    */

   /** Template Service. */
   @Inject
   private TemplateService templateService;

   /** Template Node Service to test. */
   @Inject
   private TemplateNodeService templateNodeService;
   
   @Inject
   private SpaceService                       spaceService;

   /**
    * Test method for {@link
    * org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#addApplication(String,
    * String, String, UUID, Map<String, String>)}
    * 
    * @throws TemplateServiceException
    */
   
   
   @Test
    // @Ignore
   public void testAddApplication() throws TemplateServiceException
   {
      initTemplate();
      Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
      templateNodeService.addSpace(TEMPLATE_ID1, space1);

      // Adding Application
      Map<String, String> roleMapping1 = init1RoleMapping();
      Map<String, String> roleMapping2 = init2RoleMapping();
      
      // 1 st application
      TemplateApplication templateApp1 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
            APPLICATION1LABEL, PLUGINUUID, roleMapping1);
      // 2 nd application
      TemplateApplication templateApp2 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
          APPLICATION2LABEL, PLUGINUUID, roleMapping2);
      
      // Checking template 1
      Assert.assertEquals(PLUGINUUID, templateApp1.getPluginUUID());
      Assert.assertEquals(APPLICATION1LABEL, templateApp1.getName());
      Assert.assertEquals(roleMapping1, templateApp1.getRolesMapping());  
      
      // Checking template 2
      Assert.assertEquals(PLUGINUUID, templateApp2.getPluginUUID());
      Assert.assertEquals(APPLICATION2LABEL, templateApp2.getName());
      Assert.assertEquals(roleMapping2, templateApp2.getRolesMapping()); 
      
      //TODO: test raised exception:
      /*
       * throw new TemplateServiceException(String.format(
          "Unable to add application to the space given with [parent=%s, app=%s]", parentNodeUri,
       */
   }

   /**
    * Test method for
    * {@link org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#addSpace(String, org.novaforge.forge.core.organization.model.Space)}
    * 
    * @throws TemplateServiceException
    */
   @Test
   // @Ignore
   public void testAddSpace() throws TemplateServiceException
   {
     initTemplate();
     //space1
     Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
     templateNodeService.addSpace(TEMPLATE_ID1, space1);
     //space2
     Space space2 = createSpace(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);
     templateNodeService.addSpace(TEMPLATE_ID1, space2);
     
     ArrayList<String> expectedSpace = new ArrayList<String>();
     expectedSpace.add(SPACE_NAME1);
     expectedSpace.add(SPACE_NAME2);
     
     ArrayList<String> currentSpace = new ArrayList<String>();
     
     //check
     List<Space> templateSpaces = templateNodeService.getAllSpaces(TEMPLATE_ID1);
     Assert.assertEquals(2, templateSpaces.size());
     for (Space space : templateSpaces){
       currentSpace.add(space.getName());
     }
     Assert.assertEquals(expectedSpace, currentSpace);
   }

   /**
    * Test method for
    * {@link org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#getAllTemplateApplications(String)}
    * 
    * @throws TemplateServiceException
    */
   @Test
   // @Ignore
   public void testGetAllTemplateApplications() throws TemplateServiceException
   {
     initTemplate();
     Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
     templateNodeService.addSpace(TEMPLATE_ID1, space1);

     // Adding Application
     Map<String, String> roleMapping1 = init1RoleMapping();
     Map<String, String> roleMapping2 = init2RoleMapping();
     
     // 1 st application
     TemplateApplication templateApp1 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
           APPLICATION1LABEL, PLUGINUUID, roleMapping1);
     // 2 nd application
     TemplateApplication templateApp2 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
         APPLICATION2LABEL, PLUGINUUID, roleMapping2);
     
         
     // get AllTemplateApplications
     List<TemplateApplication> templateApplications = templateNodeService.getAllTemplateApplications(TEMPLATE_ID1);
     
     //checking
     //TODO: this assert is failing : investigate to know why ?  0 template applications were found.
     // not understood the tested method !
     Assert.assertEquals(2, templateApplications.size());
   }

   /**
    * Test method for
    * {@link org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#getAllSpaces(String)}
    * 
    * @throws TemplateServiceException
    */
   @Test
   // @Ignore
   public void testGetAllSpaces() throws TemplateServiceException
   {
     initTemplate();
     //space1
     Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
     templateNodeService.addSpace(TEMPLATE_ID1, space1);
     //space2
     Space space2 = createSpace(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);
     templateNodeService.addSpace(TEMPLATE_ID1, space2);
     
     ArrayList<String> expectedSpace = new ArrayList<String>();
     expectedSpace.add(SPACE_NAME1);
     expectedSpace.add(SPACE_NAME2);
     
     ArrayList<String> currentSpace = new ArrayList<String>();
     
     //get all spaces
     List<Space> templateSpaces = templateNodeService.getAllSpaces(TEMPLATE_ID1);
     
     // check
     Assert.assertEquals(2, templateSpaces.size());
     for (Space space : templateSpaces){
       currentSpace.add(space.getName());
     }
     Assert.assertEquals(expectedSpace, currentSpace);
   }

   /**
    * Test method for
    * {@link org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#getApplication(String, String)}
    * 
    * @throws TemplateServiceException
    */
   @Test
   // @Ignore
   public void testGetApplication() throws TemplateServiceException
   {
     initTemplate();
     Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
     templateNodeService.addSpace(TEMPLATE_ID1, space1);

     // Adding Application
     Map<String, String> roleMapping1 = init1RoleMapping();
     Map<String, String> roleMapping2 = init2RoleMapping();
     
     // 1 st application
     TemplateApplication templateApp1 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
           APPLICATION1LABEL, PLUGINUUID, roleMapping1);
     // 2 nd application
     TemplateApplication templateApp2 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
         APPLICATION2LABEL, PLUGINUUID, roleMapping2);
     
     //get application
     templateApp1 = templateNodeService.getApplication(TEMPLATE_ID1,APPLICATION1URI);
     Assert.assertNotNull(templateApp1);     
   }

   /**
    * Test method for
    * {@link org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#getSpace(String, String)}
    * 
    * @throws TemplateServiceException
    */
   @Test
 // @Ignore
   public void testGetSpace() throws TemplateServiceException
   {
     initTemplate();
     //space1
     Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
     templateNodeService.addSpace(TEMPLATE_ID1, space1);
     //space2
     Space space2 = createSpace(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);
     templateNodeService.addSpace(TEMPLATE_ID1, space2);
     
     // get space
     Space foundSpace1 = templateNodeService.getSpace(TEMPLATE_ID1, SPACE_NAME1);
     
     //check
     Assert.assertNotNull(foundSpace1);
     Assert.assertEquals(SPACE_NAME1, foundSpace1.getName());
     Assert.assertEquals(SPACE_DESC1, foundSpace1.getDescription());
     Assert.assertEquals(SPACE_NAME1, foundSpace1.getUri().toString());               
   }

   /**
    * Test method for
    * {@link org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#deleteApplication(String, String, String)}
    * 
    * @throws TemplateServiceException
    */
   @Test
   // @Ignore
   public void testDeleteApplication() throws TemplateServiceException
   {
     initTemplate();
     Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
     templateNodeService.addSpace(TEMPLATE_ID1, space1);

     // Adding Application
     Map<String, String> roleMapping1 = init1RoleMapping();
     Map<String, String> roleMapping2 = init2RoleMapping();
     
     // 1 st application
     TemplateApplication templateApp1 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
           APPLICATION1LABEL, PLUGINUUID, roleMapping1);
     // 2 nd application
     TemplateApplication templateApp2 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
         APPLICATION2LABEL, PLUGINUUID, roleMapping2);
     
     templateNodeService.deleteApplication(TEMPLATE_ID1, SPACE_NAME1, APPLICATION1URI);
     
     //try get deleted application
     try
     {
       templateApp1 = templateNodeService.getApplication(TEMPLATE_ID1,APPLICATION1URI);
       fail("should has raised an exception");
     }
     catch (final Exception ne)
     {
       System.out.println("expected exception!");
     }
     
     //test raised exception
     try
     {
       templateNodeService.deleteApplication(TEMPLATE_ID1, SPACE_NAME1, APPLICATION_WRONG_URI);
       fail("should has raised an exception");
     }
     catch (final Exception ne)
     {
       System.out.println("expected exception!");
     }
   }

   /**
    * Test method for
    * {@link org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#deleteSpace(String, String)}
    * 
    * @throws TemplateServiceException
    */
   @Test
   // @Ignore
   public void testDeleteSpace() throws TemplateServiceException
   {
     initTemplate();
     //space1
     Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
     templateNodeService.addSpace(TEMPLATE_ID1, space1);
     //space2
     Space space2 = createSpace(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);
     templateNodeService.addSpace(TEMPLATE_ID1, space2);
     
     
     //check after adding 2 spaces
     ArrayList<String> expectedSpace = new ArrayList<String>();
     expectedSpace.add(SPACE_NAME1);
     expectedSpace.add(SPACE_NAME2);
     
     ArrayList<String> currentSpace = new ArrayList<String>();
     
     
     List<Space> templateSpaces = templateNodeService.getAllSpaces(TEMPLATE_ID1);
     Assert.assertEquals(2, templateSpaces.size());
     for (Space space : templateSpaces){
       currentSpace.add(space.getName());
     }
     Assert.assertEquals(expectedSpace, currentSpace);
     
     //deleting space1
     templateNodeService.deleteSpace(TEMPLATE_ID1, SPACE_NAME1);
     
     //check after deleting space1
     ArrayList<String> expectedSpaceAD = new ArrayList<String>();
     expectedSpaceAD.add(SPACE_NAME2);
     
     ArrayList<String> currentSpaceAD = new ArrayList<String>();
          
     List<Space> templateSpacesAD = templateNodeService.getAllSpaces(TEMPLATE_ID1);
     
     //TODO: fixe why following assertion error: expected 1 but was 2  (although space1 has normally been deleted).
     // bug or not bug ????
     Assert.assertEquals(1, templateSpacesAD.size());
     
     for (Space space : templateSpacesAD){
       currentSpaceAD.add(space.getName());
     }
     Assert.assertEquals(expectedSpaceAD, currentSpaceAD);
     
     //test raised exception
     /*
      * throw new TemplateServiceException(String.format(
            "The given uri doesn't referer a space element [uri=%s]", pSpaceUri));
      */   
     try
     {
       templateNodeService.deleteSpace(TEMPLATE_ID1, SPACE_WRONG_NAME);
       fail("should has raised an exception");
     }
     catch (final Exception ne)
     {
       System.out.println("expected exception!");
     }
   }

   /**
    * Test method for
    * {@link org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#updateSpace(String, org.novaforge.forge.core.organization.model.Space)}
    * 
    * @throws TemplateServiceException
    */
   @Test
   // @Ignore
   public void testUpdateSpace() throws TemplateServiceException
   {
     initTemplate();
     //space1
     Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
     templateNodeService.addSpace(TEMPLATE_ID1, space1);
     //space2
     Space space2 = createSpace(PROJECT1ID, SPACE_NAME2, SPACE_DESC2);
     templateNodeService.addSpace(TEMPLATE_ID1, space2);
     
     // get space
     Space foundSpace1 = templateNodeService.getSpace(TEMPLATE_ID1, SPACE_NAME1);
     
     //check
     Assert.assertNotNull(foundSpace1);
     Assert.assertEquals(SPACE_NAME1, foundSpace1.getName());
     Assert.assertEquals(SPACE_DESC1, foundSpace1.getDescription());
     Assert.assertEquals(SPACE_NAME1, foundSpace1.getUri().toString());      
     
     // update space 
     space1.setDescription(SPACE_DESC1_MODIFIED);
     templateNodeService.updateSpace(TEMPLATE_ID1, space1);
     
     // check update
     // get space
     Space foundModifiedSpace1 = templateNodeService.getSpace(TEMPLATE_ID1, SPACE_NAME1);
     
     //check
     Assert.assertNotNull(foundModifiedSpace1);
     Assert.assertEquals(SPACE_NAME1, foundModifiedSpace1.getName());
     //TODO:  the space description has not been modified !!
     //         bug or not ???
     Assert.assertEquals(SPACE_DESC1_MODIFIED, foundModifiedSpace1.getDescription());
     Assert.assertEquals(SPACE_NAME1, foundModifiedSpace1.getUri().toString()); 
   }

   /**
    * Test method for {@link
    * org.novaforge.forge.core.organization.internal.services.TemplateNodeServiceImpl#(String, String)}
    * 
    * @throws TemplateServiceException
    */
   @Test
   // @Ignore
   public void testGetAllSpaceApplications() throws TemplateServiceException
   {
     initTemplate();
     Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
     templateNodeService.addSpace(TEMPLATE_ID1, space1);

     // Adding Application
     Map<String, String> roleMapping1 = init1RoleMapping();
     Map<String, String> roleMapping2 = init2RoleMapping();
     
     // 1 st application
     TemplateApplication templateApp1 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
           APPLICATION1LABEL, PLUGINUUID, roleMapping1);
     // 2 nd application
     TemplateApplication templateApp2 = templateNodeService.addApplication(TEMPLATE_ID1, PARENTNODEURI,
         APPLICATION2LABEL, PLUGINUUID, roleMapping2);
     
         
     // get AlSpaceApplications
     List<TemplateApplication> templateApplications = templateNodeService.getAllSpaceApplications(SPACE_NAME1, TEMPLATE_ID1);
     
     //checking
     Assert.assertEquals(2, templateApplications.size());
   }

   /**
    * Method designed to create dummy objects.
    * 
    * @param templateId
    *           template Id
    * @param templateName
    *           template Name
    * @param templateDescription
    *           template Description
    * @param templateStatus
    *           template Status
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
      
      // setting space1      
      Space space1 = createSpace(PROJECT1ID, SPACE_NAME1, SPACE_DESC1);
      ArrayList<Space> spaces = new ArrayList<Space>();
      spaces.add(space1);
      template.setSpaces(spaces);
      
      return template;
   }   
   
   private Space createSpace(String projectId, String spaceName, String spaceDesc){
     final Space space = spaceService.newSpace();
     space.setName(spaceName);
     space.setDescription(spaceDesc);
     //space.setUri(projectId + "/" + spaceName);
     space.setUri(spaceName);
     return space;
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
      final Template newTemplate = createTemplate(TEMPLATE_ID1, TEMPLATE_NAME1, TEMPLATE_DESC1, TEMPLATE_STATUS_IN_PROGRESS);
      templateService.createTemplate(newTemplate);
   }
   
   private Map<String, String> init1RoleMapping(){
     Map<String, String> roleMapping = new HashMap<String, String>();
     roleMapping.put(PROJ1_ADMIN, APP1_ADMIN);
     roleMapping.put(PROJ1_DEV, APP1_DEV);
     roleMapping.put(PROJ1_TEST, APP1_TEST);
     return roleMapping;
   }
   
   private Map<String, String> init2RoleMapping(){
     Map<String, String> roleMapping = new HashMap<String, String>();
     roleMapping.put(PROJ1_ADMIN, APP2_ADMIN);
     roleMapping.put(PROJ1_DEV, APP2_DEV);
     roleMapping.put(PROJ1_TEST, APP2_TEST);
     return roleMapping;
   }
}
