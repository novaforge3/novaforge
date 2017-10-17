/**
 * Copyright (c) 2011-2015, BULL SAS, NovaForge Version 3 and above.
 *
 * This file is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This file is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this file.  If not, see http://www.gnu.org/licenses/.
 */
package org.novaforge.forge.plugins.scm.svn.agent.services;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNNodeEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNSearchResultDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.ScmLogEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.internal.services.SVNRepositoryServiceImpl;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNAgentException;
import org.novaforge.forge.plugins.scm.svn.agent.services.SVNRepositoryService;

public class SVNRepositoryClientTest extends TestCase
{
   public static String REPOSITORY_PATH     = "https://novaforge7.bull.fr:443/svn/nfsafran/sandboxes/CellTreeExemple";
   public static String USER                = "sbenoist";

   private boolean      svnProfileActivated = false;

   public SVNRepositoryClientTest(final String name)
   {
      super(name);
      String property = System.getProperty("svn.profile");
      if ("true".equals(property))
      {
         svnProfileActivated = true;
      }
   }

   public void testLastCommit() throws Exception
   {
      if (isSvnProfileActivated() == true)
      {
         SVNRepositoryService repositoryClient = new SVNRepositoryServiceImpl();

         List<ScmLogEntryDTO> listComit = repositoryClient.getLastCommit(REPOSITORY_PATH, USER, 5);
         assertTrue(!listComit.isEmpty());
      }
   }


   public void testGetNodeEntry() throws Exception
   {
      if (isSvnProfileActivated() == true)
      {
         SVNRepositoryService repositoryClient = new SVNRepositoryServiceImpl();

         SVNNodeEntryDTO nodeEntry = repositoryClient.getRepositoryTree(REPOSITORY_PATH, USER);
         assertTrue(nodeEntry != null);
         System.out.println(nodeEntry.toString());
      }
   }

   public void testExportRepositoryNodes() throws SVNAgentException
   {
      if (isSvnProfileActivated() == true)
      {
         SVNRepositoryService repositoryClient = new SVNRepositoryServiceImpl();
         List<String> nodes = new ArrayList<String>();
         nodes.add("");
         nodes.add("src/com/elazzouzi/exemple/client/CellTreeExemple.java");
         nodes.add("src/com/elazzouzi/exemple/server/GreetingServiceImpl.java");
         nodes.add("src/com/elazzouzi/exemple/client/GreetingService.java");
         nodes.add("src/com/elazzouzi/exemple");
         repositoryClient.exportRepositoryNodes(nodes, REPOSITORY_PATH, USER);
      }
   }

   public void testSearch() throws SVNAgentException
   {
      if (isSvnProfileActivated() == true)
      {
         SVNRepositoryService repositoryClient = new SVNRepositoryServiceImpl();
         List<SVNSearchResultDTO> results = repositoryClient.searchInSourceCode("interface",
               "GreetingService.java", REPOSITORY_PATH, USER);
         for (SVNSearchResultDTO result : results)
         {
            System.out.println(result.toString());
         }
      }

   }

   /**
    * @return the mantisProfileActivated
    */
   public boolean isSvnProfileActivated()
   {
      return svnProfileActivated;
   }

}