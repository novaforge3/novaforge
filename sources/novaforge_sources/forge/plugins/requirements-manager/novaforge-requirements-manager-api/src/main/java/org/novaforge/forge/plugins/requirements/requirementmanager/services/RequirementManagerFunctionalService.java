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
package org.novaforge.forge.plugins.requirements.requirementmanager.services;

import org.novaforge.forge.core.plugins.categories.requirementsmanagement.DirectoryBean;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsManagementServiceException;
import org.novaforge.forge.core.plugins.categories.scm.SCMSearchResultBean;
import org.novaforge.forge.core.plugins.categories.testmanagement.CoveredRequirementBean;

import java.util.List;

/**
 * Define a functional service which allows a requirement tool to contact forge
 *
 * @author Guillaume Lamirand
 */
public interface RequirementManagerFunctionalService
{
   /**
    * This method will return the project id refered to the instance id given in parameter
    *
    * @param pInstanceId
    * @return project id
    * @throws RequirementsManagementServiceException
    */
   String getProjectId(final String pInstanceId) throws RequirementsManagementServiceException;

   /**
    * This method allows requirement tool to send a notification to forge with a new requirements list.
    *
    * @param pProjectId
    *           represents tool project id used in requirement side
    * @param pDirectories
    *           represents the requirement structure
    * @param pUserName
    *           represents the user name who is doing the notification
    * @return true otherwise an exception is thrown
    * @throws RequirementsManagementServiceException
    */
   boolean updateRequirements(final String pProjectId, final List<DirectoryBean> pDirectories,
         final String pUserName) throws RequirementsManagementServiceException;

   /**
    * This method allows requirement tool to request the forge for getting the list of requirement reference
    * and test associated
    *
    * @param pProjectId
    *           represents tool project id used in requirement side
    * @param pUserName
    *           represents the user name who is doing the notification
    * @return list of requirement and test reference associated
    * @throws RequirementsManagementServiceException
    */
   List<CoveredRequirementBean> getRequirementsWithTestCoverage(final String pProjectId,
         final String pUserName) throws RequirementsManagementServiceException;

   /**
    * This method allows requirement tool to request the forge for searching the requirements references in
    * source code of SCM.
    *
    * @param pProjectId
    *           represents tool project id used in requirement side
    * @param pRegex
    *           represents the regex to search for
    * @param pFileRegex
    *           represents the file regex : can be null
    * @param pUserName
    *           represents the user name who is doing the notification
    * @return List<SCMSearchResultBean> a list of SCM search results
    * @throws RequirementsManagementServiceException
    */
   List<SCMSearchResultBean> searchRequirementInSourceCode(String pProjectId, String pRegex,
         String pFileRegex, String pCodeRepositoryPath, String pUserName, String... pFileExtensions)
               throws RequirementsManagementServiceException;

}