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
package org.novaforge.forge.plugins.requirements.requirementmanager.internal.handlers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsManagementServiceException;
import org.novaforge.forge.core.plugins.categories.scm.SCMSearchResultBean;
import org.novaforge.forge.plugins.requirements.requirementmanager.exception.RequirementManagerException;
import org.novaforge.forge.plugins.requirements.requirementmanager.internal.compiler.RequirementCompiler;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementConfigurationService;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementManagerCodeHandler;
import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementManagerFunctionalService;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.facades.RequirementCodeService;

import java.util.List;

/**
 * @author sbenoist
 */
public class RequirementManagerAnyFileCodeHandler implements RequirementManagerCodeHandler
{
  private static final Log log = LogFactory.getLog(RequirementManagerAnyFileCodeHandler.class);
  private RequirementManagerFunctionalService requirementManagerFunctionalService;
  private RequirementCodeService              requirementCodeService;
  /** Access to the requirement configuration service. */
  private RequirementConfigurationService     requirementConfigurationService;
  private String                              type;

  // We assume :
  // - that requirements are defined in the enumeration Requirement_Enum
  // - that requirement is coded like this :@Requirement(id=T_D2_SD1_ES_001-1, version=1)

  /** {@inheritDoc} */
  @Override
  public void getCodeResources(final String pProjectId, final String pCodeRepositoryPath,
      final String pCurrentUser, final String pEnumClassName) throws RequirementsManagementServiceException
  {
    try
    {
      String regExp = requirementConfigurationService.getParserRegexp();
      String[] fileExtensions = requirementConfigurationService.getFileExtensionsArray();
      List<SCMSearchResultBean> results = requirementManagerFunctionalService.searchRequirementInSourceCode(
          pProjectId, regExp, null, pCodeRepositoryPath, pCurrentUser, fileExtensions);

      // clear precedent code resources for last requirement version
      requirementCodeService.clearCodeResourcesForLastVersion(pProjectId);

      if (results != null)
      {
        // extract all the requirements id/name from the occurrences
        for (SCMSearchResultBean result : results)
        {
          RequirementCompiler compiler = new RequirementCompiler();
          compiler.compile(result.getOccurrence());
          String id = compiler.getRequirementId();
          String version = compiler.getRequirementVersion();
          log.debug("requirement: { id:" + id + ", version: " + version + '}');

          requirementCodeService.updateCodeResources(pProjectId, id, version, result.getClassName(),
              result.getPackageName(), result.getPath());
        }
      }
    }
    catch (RequirementManagerException e)
    {
      throw new RequirementsManagementServiceException(
          "an error occured during searching resources code for requirements", e);
    }
    catch (RequirementManagerServiceException e)
    {
      throw new RequirementsManagementServiceException(
          "an error occured during searching resources code for requirements", e);
    }
  }

  /** {@inheritDoc} */
  @Override
  public void setRequirementManagerFunctionalService(
      final RequirementManagerFunctionalService pRequirementManagerFunctionalService)
  {
    requirementManagerFunctionalService = pRequirementManagerFunctionalService;
  }

  /** {@inheritDoc} */
  @Override
  public void setRequirementCodeService(final RequirementCodeService pRequirementCodeService)
  {
    requirementCodeService = pRequirementCodeService;
  }

  /** {@inheritDoc} */
  @Override
  public void setRequirementConfigurationService(
      final RequirementConfigurationService pRequirementConfigurationService)
  {
    requirementConfigurationService = pRequirementConfigurationService;
  }

  @Override
  public String getType()
  {
    return type;
  }

  public void setType(String pType)
  {
    type = pType;
  }

}
