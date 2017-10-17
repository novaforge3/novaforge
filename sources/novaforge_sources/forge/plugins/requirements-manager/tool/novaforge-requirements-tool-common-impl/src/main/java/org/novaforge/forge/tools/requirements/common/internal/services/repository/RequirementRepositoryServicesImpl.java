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
package org.novaforge.forge.tools.requirements.common.internal.services.repository;

import org.novaforge.forge.plugins.requirements.requirementmanager.services.RequirementConfigurationService;
import org.novaforge.forge.tools.requirements.common.dao.DirectoryDAO;
import org.novaforge.forge.tools.requirements.common.dao.ProjectDAO;
import org.novaforge.forge.tools.requirements.common.dao.RepositoryDAO;
import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.ERepositoryType;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.services.RequirementRepositoryService;

import javax.persistence.NoResultException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public class RequirementRepositoryServicesImpl implements RequirementRepositoryService
{
  private RequirementConfigurationService requirementConfigurationService;
  private RepositoryDAO                   repositoryDAO;
  private ProjectDAO                      projectDAO;
  private DirectoryDAO                    directoryDAO;

  /**
   * {@inheritDoc}
   */
  @Override
  public Set<IRepository> findRepositoriesByType(final String pProjectId, final ERepositoryType pType)
      throws RequirementManagerServiceException
  {
    final Set<IRepository> returnSet = new HashSet<IRepository>();
    if (pType != null)
    {
      returnSet.addAll(repositoryDAO.findRepositoriesByType(pProjectId, pType.toString()));
    }
    return returnSet;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRepository findRepositoryByURI(final String pProjectId, final String pRepositoryUri)
      throws RequirementManagerServiceException
  {
    try
    {
      return repositoryDAO.findRepositoryByURI(pProjectId, pRepositoryUri);
    }
    catch (final NoResultException e)
    {
      throw new RequirementManagerServiceException(String.format(
          "There is not repository for project = %s and repository=%s", pProjectId, pRepositoryUri), e);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void deleteRepository(final String pProjectId, final String pRepositoryUri)
      throws RequirementManagerServiceException
  {
    // Retrieve repository to delete
    final IProject    project            = projectDAO.findProjectByID(pProjectId);
    final IRepository repositoryToDelete = project.getRepository(pRepositoryUri);

    // Clean directories and requirements for this repository
    final Set<IDirectory> directories = directoryDAO.findAllRootDirectoriesByRepository(repositoryToDelete);
    for (final IDirectory iDirectory : directories)
    {
      final Set<IRequirement> requirements = iDirectory.getRequirements();
      if (requirements != null)
      {
        for (final IRequirement iRequirement : requirements)
        {
          iDirectory.deleteRequirement(iRequirement);
        }
      }
      directoryDAO.delete(iDirectory);
    }

    // Clean repository from project
    project.deleteRepository(repositoryToDelete);
    projectDAO.update(project);

    // Clean repository content from file system
    if (ERepositoryType.EXCEL.equals(repositoryToDelete.getType()))
    {
      ExcelRepositoryFacade.delete(repositoryToDelete.getURI());
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRepository addRepository(final String pProjectId, final IRepository pRepository)
      throws RequirementManagerServiceException
  {

    // Add repository to persistence context
    final IProject project = projectDAO.findProjectByID(pProjectId);
    project.addRepository(pRepository);
    final IProject persistedProject = projectDAO.update(project);
    return persistedProject.getRepository(pRepository.getURI());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addExcelRepository(final String pProjectId, final IRepository pRepository,
      final String pExcelPath, final String pFileName) throws RequirementManagerServiceException
  {
    // Add repository to persistence context
    final IRepository persistedRepo = addRepository(pProjectId, pRepository);

    if (ERepositoryType.EXCEL.equals(pRepository.getType()))
    {
      // Moving excel file from temp directory to final directory
      // ie : /datas/forge/datas/requirements/projetid/repository/idEntrepot/nomFichier.xls or xlsx
      final Path tempExcelPath = Paths.get(pExcelPath);

      final Path excelParentPath = ExcelRepositoryFacade.prepareExcelParent(
          requirementConfigurationService.getDataPath(), pProjectId, String.valueOf(persistedRepo.getId()));

      final Path newExcelPath = excelParentPath.resolve(pFileName);
      try
      {
        Files.move(tempExcelPath, newExcelPath, StandardCopyOption.REPLACE_EXISTING);
      }
      catch (final IOException e)
      {
        throw new RequirementManagerServiceException(
            String.format(
                "Unable to move tmp excel file to final excel directory for repository with [project=%s, repository=%s",
                pProjectId, pRepository), e);
      }

      // Updating Repository URL
      persistedRepo.setURI(newExcelPath.toAbsolutePath().toString());
      repositoryDAO.update(persistedRepo);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRepository prepareEditRepository(final IRepository pRepository)
      throws RequirementManagerServiceException
  {
    if (ERepositoryType.EXCEL.equals(pRepository.getType()))
    {
      final Path path = ExcelRepositoryFacade.copyToTmp(pRepository.getURI());
      pRepository.setURI(path.toAbsolutePath().toString());
    }
    return pRepository;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void cancelRepository(final String pURI, final ERepositoryType pType)
      throws RequirementManagerServiceException
  {
    if (ERepositoryType.EXCEL.equals(pType))
    {
      ExcelRepositoryFacade.delete(pURI);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void editRepository(final String pProjectId, final String pPreviousURI,
      final IRepository pRepositoryToUpdate) throws RequirementManagerServiceException
  {
    final IRepository oldrepository = repositoryDAO.findRepositoryByURI(pProjectId, pPreviousURI);
    oldrepository.setDescription(pRepositoryToUpdate.getDescription());
    oldrepository.setType(pRepositoryToUpdate.getType());
    oldrepository.setURI(pRepositoryToUpdate.getURI());
    repositoryDAO.update(oldrepository);
  }

  /**
   * {@inheritDoc}
   *
   * @throws RequirementManagerServiceException
   */
  @Override
  public void editExcelRepository(final String pProjectId, final String pPreviousURI,
      final IRepository pRepositoryToUpdate, final String pExcelPath, final String pFileName)
      throws RequirementManagerServiceException
  {

    final IRepository oldRepository = repositoryDAO.findRepositoryByURI(pProjectId, pPreviousURI);
    if (ERepositoryType.EXCEL.equals(pRepositoryToUpdate.getType()))
    {
      // Moving excel file from temp directory to final directory
      // ie : /datas/forge/datas/requirements/projetid/repository/idEntrepot/nomFichier.xls or xlsx
      final Path tempExcelPath = Paths.get(pExcelPath);

      final Path excelParentPath = ExcelRepositoryFacade.prepareExcelParent(
          requirementConfigurationService.getDataPath(), pProjectId, String.valueOf(oldRepository.getId()));

      final Path newExcelPath = excelParentPath.resolve(pFileName);
      try
      {
        final Path oldPath = Paths.get(pPreviousURI);
        if (!newExcelPath.equals(oldPath))
        {
          Files.deleteIfExists(oldPath);
        }
        Files.move(tempExcelPath, newExcelPath, StandardCopyOption.REPLACE_EXISTING);
        Files.deleteIfExists(tempExcelPath.getParent());
      }
      catch (final IOException e)
      {
        throw new RequirementManagerServiceException(
            String.format(
                "Unable to move tmp excel file to final excel directory for repository with [project=%s, repository=%s",
                pProjectId, pRepositoryToUpdate), e);
      }
      // Updating Repository URL
      pRepositoryToUpdate.setURI(newExcelPath.toString());

    }
    oldRepository.setDescription(pRepositoryToUpdate.getDescription());
    oldRepository.setType(pRepositoryToUpdate.getType());
    oldRepository.setURI(pRepositoryToUpdate.getURI());
    repositoryDAO.update(oldRepository);

  }

  public void setProjectDAO(final ProjectDAO pProjectDAO)
  {
    projectDAO = pProjectDAO;
  }

  public void setDirectoryDAO(final DirectoryDAO pDirectoryDAO)
  {
    directoryDAO = pDirectoryDAO;
  }

  public void setRepositoryDAO(final RepositoryDAO pRepositoryDAO)
  {
    repositoryDAO = pRepositoryDAO;
  }

  /**
   * @param pRequirementConfigurationService
   *          the requirementConfigurationService to set
   */
  public void setRequirementConfigurationService(
      final RequirementConfigurationService pRequirementConfigurationService)
  {
    requirementConfigurationService = pRequirementConfigurationService;
  }
}
