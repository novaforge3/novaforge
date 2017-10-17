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
package org.novaforge.forge.tools.requirements.common.services;

import org.novaforge.forge.tools.requirements.common.exceptions.RequirementManagerServiceException;
import org.novaforge.forge.tools.requirements.common.model.ERepositoryType;
import org.novaforge.forge.tools.requirements.common.model.IRepository;

import java.util.Set;

/**
 * @author Guillaume Lamirand
 */
public interface RequirementRepositoryService
{
  /**
   * This method returns repositories of a project filtered by type
   * 
   * @param pProjectId
   * @param pType
   * @return the list of repositories
   * @throws RequirementManagerServiceException
   */
  Set<IRepository> findRepositoriesByType(String pProjectId, ERepositoryType pType)
      throws RequirementManagerServiceException;

  /**
   * This method returns repositories of a project and its uri
   * 
   * @param pProjectId
   * @param pRepositoryUri
   * @return the repository found
   * @throws RequirementManagerServiceException
   */
  IRepository findRepositoryByURI(String pProjectId, String pRepositoryUri)
      throws RequirementManagerServiceException;

  /**
   * @param pProjectId
   * @param pRepositoryUri
   * @throws RequirementManagerServiceException
   */
  void deleteRepository(String pProjectId, String pRepositoryUri) throws RequirementManagerServiceException;

  /**
   * @param pProjectId
   * @param pRepository
   * @return
   * @throws RequirementManagerServiceException
   */
  IRepository addRepository(String pProjectId, IRepository pRepository)
      throws RequirementManagerServiceException;

  /**
   * @param pProjectId
   * @param pRepository
   * @param pExcelPath
   * @param pFileName
   * @throws RequirementManagerServiceException
   */
  void addExcelRepository(String pProjectId, IRepository pRepository, String pExcelPath,
      final String pFileName) throws RequirementManagerServiceException;

  /**
   * @param pRepository
   * @return
   * @throws RequirementManagerServiceException
   */
  IRepository prepareEditRepository(IRepository pRepository) throws RequirementManagerServiceException;

  /**
   * @param pURI
   * @param pType
   * @throws RequirementManagerServiceException
   */
  void cancelRepository(final String pURI, final ERepositoryType pType)
      throws RequirementManagerServiceException;

  /**
   * @param pProjectId
   * @param pPreviousURI
   * @param pRepositoryToUpdate
   * @throws RequirementManagerServiceException
   */
  void editRepository(String pProjectId, String pPreviousURI, IRepository pRepositoryToUpdate)
      throws RequirementManagerServiceException;

  /**
   * @param pProjectId
   * @param pPreviousURI
   * @param pRepositoryToUpdate
   * @param pExcelPath
   * @param pFileName
   * @throws RequirementManagerServiceException
   */
  void editExcelRepository(String pProjectId, String pPreviousURI, IRepository pRepositoryToUpdate,
      String pExcelPath, String pFileName) throws RequirementManagerServiceException;

}
