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
package org.novaforge.forge.tools.requirements.common.internal.services;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.facades.RequirementFunctionalTestService;
import org.novaforge.forge.tools.requirements.common.model.ETestStatus;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.model.ITest;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerService;

import java.util.HashSet;
import java.util.Set;

public class RequirementFunctionalTestServiceImpl implements RequirementFunctionalTestService
{

  private static final Log          LOGGER = LogFactory.getLog(RequirementFunctionalTestServiceImpl.class);
  private RequirementManagerService requirementManagerService;

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<IRequirement> findRequirementsTestByStatus(final String pProjectId, final String pRepositoryURI,
      final ETestStatus pTestStatus) throws RequirementManagerServiceException
  {
    final IRepository repository = getRepository(pProjectId, pRepositoryURI);
    if (repository == null)
    {
      throw new RequirementManagerServiceException(String.format(
          "Unable to find repository with [projectId=%s, uri=%s]", pProjectId, pRepositoryURI));
    }

    try
    {
      if (LOGGER.isDebugEnabled())
      {
        LOGGER.debug("load requirements[RepoURI: " + pRepositoryURI + ", Filter: " + pTestStatus + "]");
      }

      final Set<IRequirement> requirementReturned = new HashSet<IRequirement>();
      final Set<IRequirement> requirements = requirementManagerService
          .findRequirementsByRepository(repository);
      if (requirements != null)
      {
        if (ETestStatus.ALL.equals(pTestStatus))
        {
          requirementReturned.addAll(requirements);
        }
        else
        {
          for (final IRequirement requirement : requirements)
          {
            final ETestStatus status = requirementManagerService.getRequirementTestStatus(requirement);
            if (status.equals(pTestStatus))
            {
              requirementReturned.add(requirement);
            }
          }
        }
      }
      return requirementReturned;
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException("Error occurred during the requirement research  ["
          + pRepositoryURI + " ]", e);
    }

  }

  private IRepository getRepository(final String pProjectId, final String pRepositoryURI)
      throws RequirementManagerServiceException
  {
    final IProject project = requirementManagerService.findProjectByID(pProjectId);
    return project.getRepository(pRepositoryURI);
  }

  @Override
  public void updateTest(final String pRequirementReference, final int pRequirementVersion,
      final Set<ITest> pTests) throws RequirementManagerServiceException
  {
    try
    {
      final IRequirement requirement = requirementManagerService.loadRequirementTree(pRequirementReference);
      if (requirement != null)
      {
        final IRequirementVersion requirementVersion = requirement
            .findRequirementVersion(pRequirementVersion);
        if (requirementVersion != null)
        {
          for (final ITest test : pTests)
          {
            requirementVersion.addTest(test);
          }
        }
      }

      // update the requirement with tests
      requirementManagerService.updateRequirement(requirement);
    }
    catch (final RequirementManagerServiceException e)
    {
      throw new RequirementManagerServiceException(String.format(
          "Error occurred during updating tests for requirement with reference=%s and version=%s",
          pRequirementReference, pRequirementVersion), e);
    }
  }

  @Override
  public void clearTestsForLastRequirementVersion(final String pRequirementReference)
      throws RequirementManagerServiceException
  {
    final IRequirement requirement = requirementManagerService.loadRequirementTree(pRequirementReference);
    if (requirement != null)
    {
      final IRequirementVersion lastVersion = requirement.findLastRequirementVersion();
      if (lastVersion != null)
      {
        lastVersion.removeAllTests();
        requirementManagerService.updateRequirement(requirement);
      }
    }
  }

  public void setRequirementManagerService(final RequirementManagerService pRequirementManagerService)
  {
    requirementManagerService = pRequirementManagerService;
  }

}
