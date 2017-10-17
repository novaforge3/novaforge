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
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSession;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.transaction.CDOTransaction;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.obeonetwork.dsl.requirement.Category;
import org.obeonetwork.dsl.requirement.Repository;
import org.obeonetwork.dsl.requirement.Requirement;
import org.obeonetwork.dsl.requirement.RequirementFactory;
import org.obeonetwork.dsl.requirement.RequirementPackage;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * @author sbenoist
 *
 */
public class TestPopulateCDORepositoryFromXMI extends TestCase
{
   private static final String XMI_MAIN_DIRECTORY_TAG  = "mainCategories";

   private static final String XMI_DIRECTORY_TAG       = "subCategories";

   private static final String XMI_REQUIREMENT_TAG     = "requirements";

   private static final String XMI_NAME_ATTRIBUTE      = "name";

   private static final String XMI_ID_ATTRIBUTE        = "id";

   private static final String XMI_STATEMENT_ATTRIBUTE = "statement";

   private static final String XMI_RATIONALE_ATTRIBUTE = "rationale";

   private String              host                      = "vm-infra-11";

   private String            port       = "2036";

   private String              repository                = "novaforge_requirements";

   private boolean             testCDOconnectorActivated = false;

   public TestPopulateCDORepositoryFromXMI()
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

   public void testPopulate()
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
            CDOResource resource = transaction.getOrCreateResource("/Safran_Test/SAFRAN_M6.requirement");

            // Get the file to parse
            InputStream is = new FileInputStream("src/test/resources/SAFRAN_M6.requirement");
            Document document = new SAXReader().read(is);

            // Get the repository node as the root element
            Element root = document.getRootElement();
            String repositoryName = root.attributeValue(XMI_NAME_ATTRIBUTE);

            // Create the repository
            Repository repository = RequirementFactory.eINSTANCE.createRepository();
            repository.setName(repositoryName);
            resource.getContents().add(repository);

            // build the tree
            addMainDirectories(resource, repository, root);

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

   private String getID(final Element pElement)
   {
      String name = null;
      for (Object obj : pElement.attributes())
      {
         Attribute attribute = (Attribute) obj;
         if (XMI_ID_ATTRIBUTE.equals(attribute.getName()) && "".equals(attribute.getNamespacePrefix()))
         {
            name = attribute.getValue();
         }
      }
      return name;
   }


   private void addMainDirectories(final CDOResource resource, final Repository pRepository,
         final Element pElementDirectory)
               throws Exception
               {
      @SuppressWarnings("unchecked")
      List<Element> elmnts = pElementDirectory.elements(XMI_MAIN_DIRECTORY_TAG);
      for (Element elmnt : elmnts)
      {
         // Create a category
         Category category = RequirementFactory.eINSTANCE.createCategory();
         category.setId(getID(elmnt));
         category.setName(elmnt.attributeValue(XMI_NAME_ATTRIBUTE));
         category.setRepository(pRepository);
         resource.getContents().add(category);
         addSubDirectories(resource, elmnt, category);
      }
               }

   private void addSubDirectories(final CDOResource resource, final Element pElementDirectory,
         final Category pParentCategory)
               throws Exception
               {
      @SuppressWarnings("unchecked")
      List<Element> elmnts = pElementDirectory.elements(XMI_DIRECTORY_TAG);
      for (Element elmnt : elmnts)
      {
         // Create a category
         Category category = RequirementFactory.eINSTANCE.createCategory();
         category.setId(getID(elmnt));
         category.setName(elmnt.attributeValue(XMI_NAME_ATTRIBUTE));
         category.setParentCategory(pParentCategory);
         resource.getContents().add(category);
         addSubDirectories(resource, elmnt, category);
      }

      addRequirements(resource, pElementDirectory, pParentCategory);
               }

   private void addRequirements(final CDOResource resource, final Element pElementDirectory,
         final Category pParentCategory) throws Exception
         {
      @SuppressWarnings("unchecked")
      List<Element> elmnts = pElementDirectory.elements(XMI_REQUIREMENT_TAG);
      for (Element elmnt : elmnts)
      {
         Requirement requirement = RequirementFactory.eINSTANCE.createRequirement();
         requirement.setAcceptanceCriteria(elmnt.attributeValue("acceptanceCriteria"));
         requirement.setCategory(pParentCategory);
         requirement.setId(getID(elmnt));
         requirement.setName(elmnt.attributeValue(XMI_NAME_ATTRIBUTE));
         requirement.setRationale(elmnt.attributeValue(XMI_RATIONALE_ATTRIBUTE));
         requirement.setStatement(elmnt.attributeValue(XMI_STATEMENT_ATTRIBUTE));
         resource.getContents().add(requirement);
      }
         }

}
