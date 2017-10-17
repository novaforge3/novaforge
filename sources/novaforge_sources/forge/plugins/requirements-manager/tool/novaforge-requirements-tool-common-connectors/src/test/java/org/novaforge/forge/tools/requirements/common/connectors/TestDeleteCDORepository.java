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
import org.eclipse.emf.cdo.view.CDOQuery;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.obeonetwork.dsl.requirement.Category;
import org.obeonetwork.dsl.requirement.RequirementPackage;

import java.util.List;

/**
 * @author sbenoist
 *
 */
public class TestDeleteCDORepository extends TestCase
{
   private String  host                      = "vm-infra-11";

   private String            port       = "2036";

   private String  repository                = "test_requirements";

   private boolean testCDOconnectorActivated = false;



   public TestDeleteCDORepository()
   {
      super();
      String property = System.getProperty("cdo.test.profile");
      if ("true".equals(property))
      {
         testCDOconnectorActivated = true;
      }
   }

   public void testDelete()
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

            // open a transaction
            CDOTransaction transaction = cdoSession.openTransaction();
            CDOResource resource = transaction.getOrCreateResource("/myResource");
            CDOQuery query = transaction.createQuery("ocl",
                  "Category.allInstances()->select(id=category)");
            query.setParameter("category", "UC35T23");
            List<Category> categories = query.getResult();
            for (Category category : categories)
            {
               System.out.println("\n id = " + category.getId());
            }

            Category parent = categories.get(0);

            query = transaction.createQuery("ocl", "Category.allInstances()->select(id=category)");
            query.setParameter("category", "UC35T25");
            List<Category> categoriesToRemove = query.getResult();

            parent.getSubCategories().removeAll(parent.getSubCategories());
            resource.getContents().add(parent);
            /*
             * for (Repository repository : repositories) {
             * resource.getContents().remove(repositories.get(0)); }
             */

            // commit
            transaction.commit();
            cdoSession.close();
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
}
