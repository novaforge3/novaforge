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

import org.novaforge.forge.core.plugins.categories.requirementsmanagement.RequirementsManagementServiceException;
import org.novaforge.forge.tools.requirements.common.facades.RequirementCodeService;

/**
 * @author sbenoist
 */
public interface RequirementManagerCodeHandler
{
  /**
   * This method returns the code resources found in function of requirements tracking
   *
   * @param pProjectId
   * @param pCodeRepositoryPath
   * @param pCurrentUser
   * @param pEnumClassName
   * @throws RequirementsManagementServiceException
   */
  void getCodeResources(String pProjectId, String pCodeRepositoryPath, String pCurrentUser,
      String pEnumClassName) throws RequirementsManagementServiceException;

  /**
   * This method sets the requirementManagerFunctionalService used to analyse SCM repository
   *
   * @param pRequirementManagerFunctionalService
   */
  void setRequirementManagerFunctionalService(
      RequirementManagerFunctionalService pRequirementManagerFunctionalService);

  /**
   * This method sets the requirementCodeService used to update the requirements database
   *
   * @param pRequirementCodeService
   */
  void setRequirementCodeService(RequirementCodeService pRequirementCodeService);

  /**
   * This method sets the RequirementConfigurationService
   *
   * @param pRequirementConfigurationService
   */
  void setRequirementConfigurationService(RequirementConfigurationService pRequirementConfigurationService);

  /**
   * This method return the type of the {@link RequirementManagerCodeHandler}
   * @return The type of the {@link RequirementManagerCodeHandler}
   */
  String getType();
}