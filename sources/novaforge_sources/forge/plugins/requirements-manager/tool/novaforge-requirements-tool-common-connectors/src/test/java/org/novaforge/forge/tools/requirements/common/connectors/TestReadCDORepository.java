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
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSession;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.view.CDOQuery;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.obeonetwork.dsl.requirement.Category;
import org.obeonetwork.dsl.requirement.Repository;
import org.obeonetwork.dsl.requirement.Requirement;
import org.obeonetwork.dsl.requirement.RequirementPackage;

import java.util.List;

/**
 * @author sbenoist
 *
 */
public class TestReadCDORepository extends TestCase
{
   private String  host                      = "vm-infra-11";

   private String            port       = "2036";

   private String  repository                = "test_requirements";

   private boolean testCDOconnectorActivated = false;

   public TestReadCDORepository()
   {
      super();
      String property = System.getProperty("cdo.test.profile");
      if ("true".equals(property))
      {
         testCDOconnectorActivated = true;
      }
   }


   private CDOSession getSession()
   {
      Net4jUtil.prepareContainer(IPluginContainer.INSTANCE);
      TCPUtil.prepareContainer(IPluginContainer.INSTANCE);

      // create a connector
      IConnector connector = TCPUtil.getConnector(IPluginContainer.INSTANCE, host + ":" + port);
      CDOSessionConfiguration config = CDONet4jUtil.createSessionConfiguration();
      config.setConnector(connector);
      config.setRepositoryName(repository);
      return config.openSession();
   }

   public void testRead()
   {
      if (testCDOconnectorActivated)
      {
         CDOSession cdoSession = null;
         try
         {
            // get an opened CDOSession
            cdoSession = getSession();

            // initialize the requirement modelpackage
            cdoSession.getPackageRegistry().putEPackage(RequirementPackage.eINSTANCE);

            // get a view and make the query
            CDOView view = cdoSession.openView();
            CDOQuery query = view.createQuery("ocl", "Repository.allInstances()->select(name=repository)");
            query.setParameter("repository", "safran");
            List<Repository> repositories = query.getResult();

            // Get the repository object
            assertNotNull(repositories);
            assertEquals(1, repositories.size());
            System.out.println(repositories.get(0).getName());

            // Get the main categories
            List<Category> categories = repositories.get(0).getMainCategories();
            for (Category mainCategory : categories)
            {
               System.out.println(mainCategory.getId() + " - " + mainCategory.getName());
               readCategories(mainCategory, 1);
            }
         }
         catch (Exception e)
         {
            e.printStackTrace();
         }
         finally
         {
            cdoSession.close();
         }
      }
   }

   private String getTabulation(final int level)
   {
      StringBuilder tab = new StringBuilder();
      for (int i = 0; i < level; i++)
      {
         tab.append("\t");
      }

      return tab.toString();
   }

   private void readCategories(final Category pParentCategory, final int level)
   {
      List<Category> categories = pParentCategory.getSubCategories();
      for (Category category : categories)
      {
         System.out.println(getTabulation(level) + category.getId() + " - "
               + category.getName());
         readRequirements(category, level + 1);
         readCategories(category, level + 2);
      }
   }

   private void readRequirements(final Category pCategory, final int level)
   {
      List<Requirement> requirements = pCategory.getRequirements();
      for (Requirement requirement : requirements)
      {
         System.out.println(getTabulation(level) + "\t***SPEC***=" + requirement.getName());
      }
   }
}
