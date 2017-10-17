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
package org.novaforge.forge.tools.requirements.common.connectors;

import junit.framework.TestCase;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSession;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.emf.cdo.util.CommitException;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.obeonetwork.dsl.requirement.Category;
import org.obeonetwork.dsl.requirement.Repository;
import org.obeonetwork.dsl.requirement.Requirement;
import org.obeonetwork.dsl.requirement.RequirementFactory;
import org.obeonetwork.dsl.requirement.RequirementPackage;

/**
 * @author sbenoist
 *
 */
public class TestPopulate extends TestCase
{
   public static final String repositoryURI = "/myTestingResource";
   private String  host                      = "vm-infra-11";
   private String  port                      = "2036";
   private String  repository                = "test_requirements";
   private boolean testCDOconnectorActivated = false;

   public TestPopulate()
   {
      super();
      String property = System.getProperty("cdo.test.profile");
      if ("true".equals(property))
      {
         testCDOconnectorActivated = true;
      }
   }

   public void testPopulateFromNothing()
   {
      if (testCDOconnectorActivated)
      {
         try
         {
            CDOSession cdoSession = getSession();
            CDOTransaction transaction = cdoSession.openTransaction();
            CDOResource resource = transaction.getOrCreateResource(repositoryURI);

            // initialize the requirement modelpackage
            cdoSession.getPackageRegistry().putEPackage(RequirementPackage.eINSTANCE);

            // get all the repositories into the resource
            EList<EObject> objects = resource.getContents();
            Repository repo = null;
            Repository repositoryToRemove = null;
            for (EObject object : objects)
            {
               if (object instanceof Repository)
               {
                  repo = (Repository) object;
                  if (repo.getName().equals("Root Directory B"))
                  {
                     repositoryToRemove = repo;
                  }
               }
            }
            resource.getContents().remove(repositoryToRemove);
            transaction.commit();
            cdoSession.close();
         }
         catch (CommitException e)
         {
            e.printStackTrace();
         }
      }
   }

   private CDOSession getSession()
   {
      Net4jUtil.prepareContainer(IPluginContainer.INSTANCE);
      TCPUtil.prepareContainer(IPluginContainer.INSTANCE);

      // create a connector
      IConnector              connector = TCPUtil.getConnector(IPluginContainer.INSTANCE, host + ":" + port);
      CDOSessionConfiguration config    = CDONet4jUtil.createSessionConfiguration();
      config.setConnector(connector);
      config.setRepositoryName(repository);
      return config.openSession();
   }

   private void populateCDO() throws CommitException
   {
      CDOSession cdoSession = getSession();
      CDOTransaction transaction = getSession().openTransaction();
      CDOResource resource = transaction.getOrCreateResource(repositoryURI);

      // Create a root directory A
      Repository repository = RequirementFactory.eINSTANCE.createRepository();
      repository.setName("Root Directory A");
      resource.getContents().add(repository);

      // Create a parent category Use Case 1
      Category category1 = RequirementFactory.eINSTANCE.createCategory();
      category1.setId("UC-A1");
      category1.setName("Use Case A1");
      category1.setRepository(repository);
      resource.getContents().add(category1);

      // Create a child category Use Case 11
      Category category11 = RequirementFactory.eINSTANCE.createCategory();
      category11.setId("UC-A11");
      category11.setName("Use Case A11");
      category11.setRepository(repository);
      category11.setParentCategory(category1);
      resource.getContents().add(category11);

      // Create a child requirement REQ 111
      Requirement requirement111 = RequirementFactory.eINSTANCE.createRequirement();
      requirement111.setCategory(category11);
      requirement111.setId("REQ-A111");
      requirement111.setName("Requirement A111");
      requirement111.setVersion(1);
      resource.getContents().add(requirement111);

      // Create a child requirement REQ 112
      Requirement requirement112 = RequirementFactory.eINSTANCE.createRequirement();
      requirement112.setCategory(category11);
      requirement112.setId("REQ-A112");
      requirement112.setName("Requirement A112");
      requirement112.setVersion(1);
      resource.getContents().add(requirement112);

      // Create a child category Use Case 12
      Category category12 = RequirementFactory.eINSTANCE.createCategory();
      category12.setId("UC-A12");
      category12.setName("Use Case A12");
      category12.setRepository(repository);
      category12.setParentCategory(category1);
      resource.getContents().add(category12);

      // Create a child requirement REQ 121
      Requirement requirement121 = RequirementFactory.eINSTANCE.createRequirement();
      requirement121.setCategory(category12);
      requirement121.setId("REQ-A121");
      requirement121.setName("Requirement A121");
      requirement121.setVersion(1);
      resource.getContents().add(requirement121);

      // Create a child category Use Case 13
      Category category13 = RequirementFactory.eINSTANCE.createCategory();
      category13.setId("UC-A13");
      category13.setName("Use Case A13");
      category13.setRepository(repository);
      category13.setParentCategory(category1);
      resource.getContents().add(category13);

      // Create a child requirement REQ 131
      Requirement requirement131 = RequirementFactory.eINSTANCE.createRequirement();
      requirement131.setCategory(category12);
      requirement131.setId("REQ-A121");
      requirement131.setName("Requirement A131");
      requirement131.setVersion(1);
      resource.getContents().add(requirement131);

      // Create a parent category Use Case 2
      Category category2 = RequirementFactory.eINSTANCE.createCategory();
      category2.setId("UC-A2");
      category2.setName("Use Case A2");
      category2.setRepository(repository);
      resource.getContents().add(category2);

      // Create a child category Use Case 21
      Category category21 = RequirementFactory.eINSTANCE.createCategory();
      category21.setId("UC-A21");
      category21.setName("Use Case A21");
      category21.setRepository(repository);
      category21.setParentCategory(category2);
      resource.getContents().add(category21);

      // Create a child requirement REQ 211
      Requirement requirement211 = RequirementFactory.eINSTANCE.createRequirement();
      requirement211.setCategory(category21);
      requirement211.setId("REQ-A211");
      requirement211.setName("Requirement A211");
      requirement211.setVersion(1);
      resource.getContents().add(requirement211);

      // Create a root directory B
      Repository repositoryB = RequirementFactory.eINSTANCE.createRepository();
      repositoryB.setName("Root Directory B");
      resource.getContents().add(repositoryB);

      // Create a parent category Use Case 1
      Category categoryB1 = RequirementFactory.eINSTANCE.createCategory();
      categoryB1.setId("UC-B1");
      categoryB1.setName("Use Case B1");
      categoryB1.setRepository(repositoryB);
      resource.getContents().add(categoryB1);

      // Create a child category Use Case 11
      Category categoryB11 = RequirementFactory.eINSTANCE.createCategory();
      categoryB11.setId("UC-B11");
      categoryB11.setName("Use Case B11");
      categoryB11.setRepository(repositoryB);
      categoryB11.setParentCategory(categoryB1);
      resource.getContents().add(categoryB11);

      // Create a child requirement REQ 111
      Requirement requirementB111 = RequirementFactory.eINSTANCE.createRequirement();
      requirementB111.setCategory(categoryB11);
      requirementB111.setId("REQ-B111");
      requirementB111.setName("Requirement B111");
      requirementB111.setVersion(1);
      resource.getContents().add(requirementB111);

      // Create a child requirement REQ 112
      Requirement requirementB112 = RequirementFactory.eINSTANCE.createRequirement();
      requirementB112.setCategory(categoryB11);
      requirementB112.setId("REQ-B112");
      requirementB112.setName("Requirement B112");
      requirementB112.setVersion(1);
      resource.getContents().add(requirementB112);

      // Create a child category Use Case 12
      Category categoryB12 = RequirementFactory.eINSTANCE.createCategory();
      categoryB12.setId("UC-B12");
      categoryB12.setName("Use Case B12");
      categoryB12.setRepository(repository);
      categoryB12.setParentCategory(categoryB1);
      resource.getContents().add(categoryB12);

      // Create a child requirement REQ 121
      Requirement requirementB121 = RequirementFactory.eINSTANCE.createRequirement();
      requirementB121.setCategory(categoryB12);
      requirementB121.setId("REQ-B121");
      requirementB121.setName("Requirement B121");
      requirementB121.setVersion(1);
      resource.getContents().add(requirementB121);

      transaction.commit();
      cdoSession.close();
   }
}
