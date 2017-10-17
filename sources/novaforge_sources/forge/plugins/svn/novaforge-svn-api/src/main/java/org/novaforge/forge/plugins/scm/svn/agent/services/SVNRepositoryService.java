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

package org.novaforge.forge.plugins.scm.svn.agent.services;

import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNNodeEntryDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.SVNSearchResultDTO;
import org.novaforge.forge.plugins.scm.svn.agent.dto.ScmLogEntryDTO;

import javax.activation.DataHandler;
import java.io.IOException;
import java.util.List;

/**
 * Service used only on the agent side. This service is not exposed as a Webservice to the forge. (=client
 * side).
 *
 * @author rols-p
 */
public interface SVNRepositoryService
{

   /**
    * Creates a Repository in SVN.
    *
    * @param pRepositoriesPath
    * @param pRepositoryName
    * @throws IOException
    * @throws SVNRepositoryException
    */
   void createRepository(String pRepositoriesPath, final String pRepositoryName) throws IOException,
   SVNRepositoryException;

   /**
    * Deletes a Repository from SVN.
    *
    * @param pRepositoriesPath
    * @param pRepositoryName
    * @throws IOException
    */
   void deleteRepository(String pRepositoriesPath, final String pRepositoryName) throws IOException;

   /**
    * Retreives the (pNbComit) last commits from a SVN repository.
    *
    * @param pRepositoriesPath
    * @param pUserName
    * @param pNbComit
    *           the number of commit to be rereived.
    * @return
    * @throws SVNAgentException
    */
   List<ScmLogEntryDTO> getLastCommit(String pRepositoriesPath, final String pUserName, final int pNbComit)
         throws SVNAgentException;

   /**
    * This method returns the full node tree of a repository path
    *
    * @param pRepositoryUrl
    * @param pUserName
    * @return SVNNodeEntryDTO
    * @throws SVNAgentException
    */
   SVNNodeEntryDTO getRepositoryTree(final String pRepositoryUrl, final String pUserName)
         throws SVNAgentException;

   /**
    * This method makes an export of svn repository nodes into FileSystem
    *
    * @param pRepositoryNodes
    * @param pRepositoryPath
    * @param pUserName
    * @return DataHandler
    * @throws SVNAgentException
    */
   DataHandler exportRepositoryNodes(final List<String> pRepositoryNodes, final String pRepositoryPath,
         final String pUserName) throws SVNAgentException;

   /**
    * This method allows to search a regex into a SVN repository
    *
    * @param pRegex
    * @param pFileRegex
    * @param pRepositoryPath
    * @param pUserName
    * @return List<SVNSearchResultDTO>
    * @throws SVNAgentException
    */
   List<SVNSearchResultDTO> searchInSourceCode(String pRegex, String pFileRegex, String pRepositoryPath,
         String pUserName, String... pFileExtensions)
               throws SVNAgentException;

}
