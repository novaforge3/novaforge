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

import org.novaforge.forge.tools.requirements.common.business.rules.BusinessStatusException;
import org.novaforge.forge.tools.requirements.common.business.rules.BusinessStatusService;
import org.novaforge.forge.tools.requirements.common.dao.DirectoryDAO;
import org.novaforge.forge.tools.requirements.common.dao.ProjectDAO;
import org.novaforge.forge.tools.requirements.common.dao.RequirementDAO;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.ECodeStatus;
import org.novaforge.forge.tools.requirements.common.model.ETestStatus;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.services.RequirementManagerService;

import java.util.Set;

/**
 * @author Guillaume Morin
 */

public class RequirementManagerServiceJPAImpl implements RequirementManagerService
{
  /**
   * I dont want EJB injection here, we dont have any transaction, in addition, i can use function with a
   * correct semantic (create, remove ...)
   */
  private RequirementDAO        requirementDAO;
  private DirectoryDAO          directoryDAO;
  private ProjectDAO            projectDAO;
  private BusinessStatusService businessStatusService;

  public RequirementManagerServiceJPAImpl()
  {

  }

  @Override
  public IProject createProject(final IProject pProject) throws RequirementManagerServiceException
  {
    return projectDAO.persist(pProject);
  }

  @Override
  public IProject updateProject(final IProject pProject) throws RequirementManagerServiceException
  {
    return projectDAO.update(pProject);
  }

  @Override
  public void deleteProject(final String pProjectId) throws RequirementManagerServiceException
  {
    projectDAO.delete(projectDAO.findProjectByID(pProjectId));
  }

  @Override
  public IProject findProjectByID(final String pProjectID) throws RequirementManagerServiceException
  {
    return projectDAO.findProjectByID(pProjectID);
  }

  @Override
  public IRequirement updateRequirement(final IRequirement pRequirement) throws RequirementManagerServiceException
  {
    try
    {
      return requirementDAO.update(pRequirement);
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException("Error with the service findRequirementByID(pRequirement) "
                                                       + e.getMessage(), e);
    }
  }

  @Override
  public void createRequirement(final IRequirement pRequirement) throws RequirementManagerServiceException
  {
    try
    {
      requirementDAO.persist(pRequirement);
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException("Error with the service createRequirement(pRequirement) "
                                                       + e.getMessage(), e);
    }
  }

  @Override
  public IDirectory createDirectory(final IDirectory pDirectory) throws RequirementManagerServiceException
  {
    try
    {
      return directoryDAO.persist(pDirectory);
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException("Error with the service createDirectory(IDirectory) "
          + e.getMessage(), e);
    }
  }

  @Override
  public Set<IDirectory> findAllRootDirectoriesByRepository(final IRepository pRepository)
      throws RequirementManagerServiceException
  {
    try
    {
      return directoryDAO.findAllRootDirectoriesByRepository(pRepository);
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException(
          "Error with the service findRootDirectoryByRepository (IRepository) " + e.getMessage(), e);
    }
  }

  @Override
  public Set<IDirectory> loadAllRootDirectoryTreesByRepository(final IRepository pRepository)
      throws RequirementManagerServiceException
  {
    return directoryDAO.loadAllRootDirectoryTreesByRepository(pRepository);
  }

  @Override
  public Set<IRequirement> findRequirementsByRepository(final IRepository pRepository)
      throws RequirementManagerServiceException
  {
    return requirementDAO.loadRequirementsByRepository(pRepository);
  }

  @Override
  public IDirectory updateDirectory(final IDirectory pRootFound) throws RequirementManagerServiceException
  {
    try
    {
      return directoryDAO.update(pRootFound);
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException("Error with the service updateDirectory (IDirectory) "
          + e.getMessage(), e);
    }
  }

  @Override
  public IRequirement loadRequirementTree(final IRequirement pRequirement)
      throws RequirementManagerServiceException
  {
    try
    {
      return requirementDAO.loadRequirementTree(pRequirement);
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException(
          "Error with the service loadRequirementTree (IRequirement) " + e.getMessage(), e);
    }
  }

  @Override
  public IRequirement loadRequirementTree(final String pFunctionalReference)
      throws RequirementManagerServiceException
  {
    try
    {
      return requirementDAO.loadRequirementTree(pFunctionalReference);
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException("Error with the service loadRequirementTree (String) "
          + e.getMessage(), e);
    }
  }

  @Override
  public IDirectory findDirectoryByReference(final String pReference)
      throws RequirementManagerServiceException
  {
    try
    {
      return directoryDAO.findDirectoryByReference(pReference);
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException(
          "Error with the service findDirectoryByReference (IDirectory) " + e.getMessage(), e);
    }
  }

  @Override
  public void deleteRequirementTree(final IRequirement pRequirement)
      throws RequirementManagerServiceException
  {
    try
    {
      requirementDAO.deleteRequirementByRef(pRequirement.getReference());
    }
    catch (final Exception e)
    {
      throw new RequirementManagerServiceException(
          "Error with the service deleteRequirementTree (IRequirement) " + e.getMessage(), e);
    }
  }

  @Override
  public ECodeStatus getRequirementCodeStatus(final IRequirement pRequirement)
      throws RequirementManagerServiceException
  {
    try
    {
      return businessStatusService.evaluateCodeStatus(pRequirement);
    }
    catch (final BusinessStatusException e)
    {
      throw new RequirementManagerServiceException(
          "Error with the service getRequirementCodeStatus(Requirment) " + e.getMessage(), e);
    }
  }

  @Override
  public ETestStatus getRequirementTestStatus(final IRequirement pRequirement)
      throws RequirementManagerServiceException
  {
    try
    {
      return businessStatusService.evaluateTestStatus(pRequirement);
    }
    catch (final BusinessStatusException e)
    {
      throw new RequirementManagerServiceException(
          "Error with the service getRequirementTestStatus(requirement) " + e.getMessage(), e);
    }
  }

  @Override
  public void deleteRootDirectoryTree(final IDirectory pDirectory) throws RequirementManagerServiceException
  {
    directoryDAO.delete(pDirectory);
  }

  @Override
  public IRequirement loadRequirementTreeByName(final String pProjectId, final String pName)
      throws RequirementManagerServiceException
  {
    try
    {
      return requirementDAO.loadRequirementTreeByName(pProjectId, pName);
    }
    catch (Exception e)
    {
      throw new RequirementManagerServiceException(e.getMessage());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRequirement loadRequirementTreeByID(final String pRequirementId)
      throws RequirementManagerServiceException
  {
    return requirementDAO.loadRequirementTreeById(pRequirementId);
  }

  public void setRequirementDAO(final RequirementDAO pRequirementDAO)
  {
    requirementDAO = pRequirementDAO;
  }

  public void setDirectoryDAO(final DirectoryDAO pDirectoryDao)
  {
    directoryDAO = pDirectoryDao;
  }

  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  public void setBusinessStatusService(final BusinessStatusService pBusinessStatusService)
  {
    businessStatusService = pBusinessStatusService;
  }

}
