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
package org.novaforge.forge.tools.requirements.common.facades;

import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.ECodeStatus;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;

import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Morin
 */
public interface RequirementCodeService
{
   /**
    * This method returns requirements filtered by code status
    *
    * @param pProjectId
    * @param pRepositoryName
    * @param pCodeStatus
    * @return
    * @throws RequirementManagerServiceException
    */
   Set<IRequirement> findRequirementsCodeByStatus(String pProjectId, String pRepositoryName,
         ECodeStatus pCodeStatus)
               throws RequirementManagerServiceException;

   List<IRequirementVersion> findRequirementVersionsCodeByStatusAndVersion(int pFirstResult, int pMaxResults,
         String pProjectId, String pRepositoryName,
         ECodeStatus pCodeStatus,
         EVersionFilter pVersion)
               throws RequirementManagerServiceException;

   int countRequirementVersionsCodeByStatusAndVersion(String pProjectId, String pRepositoryName,
         ECodeStatus pCodeStatus,
         EVersionFilter pVersionFilter)
               throws RequirementManagerServiceException;

   /**
    * This method allows to update resources for a requirement
    *
    * @param pProjectId
    * @param pRequirementName
    * @param pRequirementVersion
    * @param pResourceName
    * @param pResourceComponentName
    * @param pResourceLocation
    * @throws RequirementManagerServiceException
    */
   void updateCodeResources(String pProjectId, final String pRequirementName,
         final String pRequirementVersion,
         final String pResourceName, final String pResourceComponentName, final String pResourceLocation)
               throws RequirementManagerServiceException;

   /**
    * This method allows to clear code resources for all last versions of requirements
    *
    * @param pProjectId
    * @throws RequirementManagerServiceException
    */
   void clearCodeResourcesForLastVersion(String pProjectId) throws RequirementManagerServiceException;

  enum EVersionFilter
  {
    ALL_VERSIONS, LAST_VERSION
  }
}
