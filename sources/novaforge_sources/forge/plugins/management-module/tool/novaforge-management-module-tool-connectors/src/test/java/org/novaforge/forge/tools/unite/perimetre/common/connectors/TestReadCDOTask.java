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
package org.novaforge.forge.tools.unite.perimetre.common.connectors;

import junit.framework.TestCase;
import org.eclipse.emf.cdo.eresource.CDOResource;
import org.eclipse.emf.cdo.net4j.CDONet4jUtil;
import org.eclipse.emf.cdo.net4j.CDOSession;
import org.eclipse.emf.cdo.net4j.CDOSessionConfiguration;
import org.eclipse.emf.cdo.view.CDOView;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.net4j.Net4jUtil;
import org.eclipse.net4j.connector.IConnector;
import org.eclipse.net4j.tcp.TCPUtil;
import org.eclipse.net4j.util.container.IPluginContainer;
import org.obeonetwork.graal.AbstractTask;
import org.obeonetwork.graal.GraalPackage;
import org.obeonetwork.graal.System;

import java.util.logging.Level;
import java.util.logging.Logger;





/**
 * @author sbenoist
 *
 */
public class TestReadCDOTask extends TestCase
{
  private static final Logger LOG = Logger.getLogger("TestReadCDOTask");
  private String            host       = "safranforgefille4";
	   private String            port       = "2036";
	   private String            repository = "safran3";
	   //private String            repository = "safranrep";
	   private String            projet = "javatest";
	   //private String            projet = "essai";
	   private String systemeGraal = "MonProjet";
	   
   public TestReadCDOTask()
   {
      super();
   }

   public void testRead2()
   {

         CDOSession cdoSession = null;
         try
         {
             // get an opened CDOSession
             cdoSession = getSession();

            // initialize the task graal modelpackage
            cdoSession.getPackageRegistry().putEPackage(GraalPackage.eINSTANCE);

            // get a view and make the query
            CDOView view2 = cdoSession.openView();
            CDOResource resource2 = view2.getResource(projet+"/"+systemeGraal+".graal");
            EList<EObject> eol = resource2.getContents();
            for (EObject eo1 : eol)
            {
              System s = (System)eo1;
            	   EList<AbstractTask>  listTask = s.getTasks();
            	   for (AbstractTask t : listTask) {
            		   java.lang.System.out.println("Name = " + t.getName());
            		   java.lang.System.out.println("Description = " + t.getDescription());
            		   java.lang.System.out.println("Id = " + t.getId());
            		   java.lang.System.out.println("UseCase = " + t.getUseCase());
                }
            }

         }
         catch (Exception e)
         {
            LOG.log(Level.SEVERE, "Error in TestReadCDOTask",e);
         }
         finally
         {
            cdoSession.close();
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

/*
   public void testRead1()
   {

         CDOSession cdoSession = null;
         try
         {
            // get an opened CDOSession
            cdoSession = getSession();

            // initialize the task graal modelpackage
            cdoSession.getPackageRegistry().putEPackage(GraalPackage.eINSTANCE);

            // get a view and make the query
            CDOView view = cdoSession.openView();
            CDOQuery query = view.createQuery("ocl", "System.allInstances()->select(system=toto)");
            query.setParameter("toto", systemeGraal);
            //CDOQuery query = view.createQuery("ocl", ""ocl", "System.allInstances()->select(name=system)"");
            //query.setParameter("resource", "CDO_AGO_1");
            List<System> systems = query.getResult();
            System systemVoulu = null;
            for (System system : systems) {
            	java.lang.System.out.println(system.eResource().getURI());
            	if (system.eResource().getURI().toString().indexOf(projet) != -1) {
            		systemVoulu = system;
            	}
            	java.lang.System.out.println(system.getName() + " " + system.cdoID() + " " + system.getDescription());
            	
            }
            //systemVoulu.
            	EList<Task> tasksList =  systemVoulu.getOwnedTasks();
            	 for(Task task : tasksList) {
                 	java.lang.System.out.println(task.getName() + "cdoID=" + systemVoulu.cdoID());
                 }
            	EList<TasksGroup> tasksGroupList =  systemVoulu.getOwnedGroups();
                for(TasksGroup taskgroups : tasksGroupList) {
                	EList<Task> taskList2 = taskgroups.getOwnedTasks();
                	java.lang.System.out.println(taskgroups.getName());
	               	for(Task task : taskList2) {
	                  java.lang.System.out.println(task.getName());
	                 }
                }

                System systmvouluCopie = (System)EcoreUtil.copy(systemVoulu);	
                saveToFile(systmvouluCopie);
            
    
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


public void saveToFile(System sys) {

   try {
	   
	   
	   ResourceSet resourceSet = new ResourceSetImpl();
       resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("system", new XMIResourceFactoryImpl());
       resourceSet.getPackageRegistry().put("system",GraalPackage.eINSTANCE);
       //File xmlDocument = File.createTempFile("xmi", ".system");
       File xmlDocument = new File("c://xmi.system");
       URI fileURI = URI.createFileURI(xmlDocument.getCanonicalPath());
       Resource poResource = resourceSet.createResource(fileURI);
       poResource.getContents().add(sys);
       poResource.save(new HashMap<Object, Object>());
       String expression  = "//tasks";
       java.lang.System.out.println("requete xpath="+expression);
       XPathFactory  factory=XPathFactory.newInstance();
       XPath xPath=factory.newXPath();

       InputSource inputSource =  new InputSource(new FileInputStream(xmlDocument));
       
       DTMNodeList nodes =  (DTMNodeList) xPath.evaluate(expression, inputSource, XPathConstants.NODESET);
       NodeList nodeList=(NodeList)nodes;

       
	   } catch (Exception e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
	   }
   }

*/
}
