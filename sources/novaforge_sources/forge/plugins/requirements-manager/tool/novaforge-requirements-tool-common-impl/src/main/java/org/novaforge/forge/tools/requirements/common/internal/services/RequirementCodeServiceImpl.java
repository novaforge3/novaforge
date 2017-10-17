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
import org.novaforge.forge.tools.requirements.common.facades.RequirementCodeService;
import org.novaforge.forge.tools.requirements.common.factories.RequirementFactory;
import org.novaforge.forge.tools.requirements.common.model.ECodeStatus;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import org.novaforge.forge.tools.requirements.common.model.IResourceOOCode;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerService;
import org.novaforge.forge.tools.requirements.common.services.RequirementRepositoryService;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Guillaume Morin
 */
public class RequirementCodeServiceImpl implements RequirementCodeService
{
  private static final Log log = LogFactory.getLog(RequirementCodeServiceImpl.class);
  private RequirementManagerService    requirementManagerService;
  private RequirementRepositoryService requirementRepositoryService;
  private RequirementFactory           requirementFactory;

  @Override
  public Set<IRequirement> findRequirementsCodeByStatus(final String pProjectId, final String pRepositoryURI,
                                                        final ECodeStatus pCodeStatus)
      throws RequirementManagerServiceException
  {
    final IRepository repository = requirementRepositoryService.findRepositoryByURI(pProjectId, pRepositoryURI);
    if (repository == null)
    {
      throw new RequirementManagerServiceException(String
                                                       .format("Unable to find repository with [projectId=%s, uri=%s]",
                                                               pProjectId, pRepositoryURI));
    }

    try
    {
      log.info("load requirements[RepoURI: " + pRepositoryURI + ", Filter: " + pCodeStatus + "]");
      Set<IRequirement> requirementReturned = null;

      final Set<IRequirement> requirements = requirementManagerService.findRequirementsByRepository(repository);
      if (pCodeStatus.equals(ECodeStatus.ALL))
      {
        requirementReturned = requirements;
      }
      else
      {
        requirementReturned = new HashSet<IRequirement>();

        for (final IRequirement r : requirements)
        {
          final ECodeStatus status = requirementManagerService.getRequirementCodeStatus(r);
          if (pCodeStatus.equals(status))
          {
            requirementReturned.add(r);
          }
        }
      }
      return requirementReturned;
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException("Error occurred during the requirement research  [" + pRepositoryURI
                                                       + " ]", e);
    }
  }

  @Override
  public List<IRequirementVersion> findRequirementVersionsCodeByStatusAndVersion(final int pFirstResult,
                                                                                 final int pMaxResults,
                                                                                 final String pProjectId,
                                                                                 final String pRepositoryName,
                                                                                 final ECodeStatus pCodeStatus,
                                                                                 final EVersionFilter pVersionFilter)
      throws RequirementManagerServiceException
  {
    final List<IRequirementVersion> versions = loadRequirementVersionsCodeByStatusAndVersion(pProjectId,
                                                                                             pRepositoryName,
                                                                                             pCodeStatus,
                                                                                             pVersionFilter);
    return versions.subList(pFirstResult, pFirstResult + pMaxResults);
  }

  @Override
  public int countRequirementVersionsCodeByStatusAndVersion(final String pProjectId, final String pRepositoryName,
                                                            final ECodeStatus pCodeStatus,
                                                            final EVersionFilter pVersionFilter)
      throws RequirementManagerServiceException
  {
    final List<IRequirementVersion> versions = loadRequirementVersionsCodeByStatusAndVersion(pProjectId,
                                                                                             pRepositoryName,
                                                                                             pCodeStatus,
                                                                                             pVersionFilter);
    return versions.size();
  }

  @Override
  public void updateCodeResources(final String pProjectId, final String pRequirementName,
                                  final String pRequirementVersion, final String pResourceName,
                                  final String pResourceComponentName, final String pResourceLocation)
      throws RequirementManagerServiceException
  {
    try
    {
      // try to find the requirement
      IRequirement requirement = null;
      try
      {
        requirement = requirementManagerService.loadRequirementTreeByName(pProjectId, pRequirementName);
      }
      catch (final RequirementManagerServiceException e)
      {
        // No requirement found with this id : nothing to do ...
        // we only update the requirements present in the forge repository
      }
      if (requirement != null)
      {
        IRequirementVersion requirementversion = null;
        if (pRequirementVersion != null)
        {
          int version = -1;
          try
          {
            version = Integer.parseInt(pRequirementVersion);
          }
          catch (final Exception e)
          {
            // Nothing to do
          }
          if (version != -1)
          {
            requirementversion = requirement.findRequirementVersion(version);
          }
        }
        else
        {
          // FIXME next we will get the version in the requirement enumeration when it will be
          // implemented...now we get the last version of the requirement
          requirementversion = requirement.findLastRequirementVersion();
        }

        if (requirementversion != null)
        {
          final IResourceOOCode resource = requirementFactory.buildNewResourceCode();
          resource.setName(pResourceName);
          resource.setCompomentName(pResourceComponentName);
          resource.setLocation(new URL(pResourceLocation));
          requirementversion.addResources(resource);
          requirementManagerService.updateRequirement(requirement);
        }
      }
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException("an error occurred during updating requirements", e);
    }
  }

  @Override
  public void clearCodeResourcesForLastVersion(final String pProjectId) throws RequirementManagerServiceException
  {
    final IProject         project      = requirementManagerService.findProjectByID(pProjectId);
    final Set<IRepository> repositories = project.getRepositories();
    for (final IRepository repository : repositories)
    {
      final Set<IRequirement> requirements = requirementManagerService.findRequirementsByRepository(repository);
      clearResourcesForRequirements(requirements);
    }
  }

  private List<IRequirementVersion> loadRequirementVersionsCodeByStatusAndVersion(final String pProjectId,
                                                                                  final String pRepositoryURI,
                                                                                  final ECodeStatus pCodeStatus,
                                                                                  final EVersionFilter pVersionFilter)
      throws RequirementManagerServiceException
  {
    final IRepository repository = requirementRepositoryService.findRepositoryByURI(pProjectId, pRepositoryURI);
    if (repository == null)
    {
      throw new RequirementManagerServiceException(String
                                                       .format("Unable to find repository with [projectId=%s, uri=%s]",
                                                               pProjectId, pRepositoryURI));
    }

    try
    {
      Set<IRequirement> requirementReturned = new HashSet<>();
      final Set<IRequirement> requirements = requirementManagerService.findRequirementsByRepository(repository);
      if (ECodeStatus.ALL.equals(pCodeStatus))
      {
        requirementReturned.addAll(requirements);
      }
      else
      {
        for (final IRequirement r : requirements)
        {
          final ECodeStatus status = requirementManagerService.getRequirementCodeStatus(r);
          if ((status != null) && (status.equals(pCodeStatus)))
          {
            requirementReturned.add(r);
          }
        }
      }
      return filterVersions(requirementReturned, pVersionFilter);
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException("Error occurred during the requirement research  [" + pRepositoryURI
                                                       + " ]", e);
    }
  }

  private List<IRequirementVersion> filterVersions(final Set<IRequirement> pRequirements,
                                                   final EVersionFilter pVersionFilter)
  {
    final List<IRequirementVersion> versions = new ArrayList<IRequirementVersion>();

    if (pVersionFilter.equals(RequirementCodeService.EVersionFilter.ALL_VERSIONS))
    {
      for (final IRequirement requirement : pRequirements)
      {
        versions.addAll(requirement.getRequirementVersions());
      }
    }
    else if (pVersionFilter.equals(RequirementCodeService.EVersionFilter.LAST_VERSION))
    {
      for (final IRequirement requirement : pRequirements)
      {
        versions.add(requirement.findLastRequirementVersion());
      }
    }

    return versions;

  }

  /**
   * Clear resource recursively for all given requirements and childs
   *
   * @param pRequirements
   *     set of requirements to clear
   *
   * @throws RequirementManagerServiceException
   *     Exceptuion in clearing resources
   */
  private void clearResourcesForRequirements(Set<IRequirement> pRequirements) throws RequirementManagerServiceException
  {
    if (pRequirements != null && pRequirements.size() != 0)
    {
      for (IRequirement requirement : pRequirements)
      {
        if (requirement != null)
        {
          final IRequirementVersion childLastVersion = requirement.findLastRequirementVersion();
          if (childLastVersion != null)
          {
            childLastVersion.removeAllResources();
          }
          clearResourcesForRequirements(requirement.getChildren());
          requirementManagerService.updateRequirement(requirement);
        }
      }
    }
  }

  public void setRequirementManagerService(final RequirementManagerService pRequirementManagerService)
  {
    requirementManagerService = pRequirementManagerService;
  }

  public void setRequirementFactory(final RequirementFactory pRequirementFactory)
  {
    requirementFactory = pRequirementFactory;
  }

  public void setRequirementRepositoryService(final RequirementRepositoryService pRequirementRepositoryService)
  {
    requirementRepositoryService = pRequirementRepositoryService;
  }

}
