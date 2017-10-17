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
package org.novaforge.forge.core.organization.dao;

import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectOptions;
import org.novaforge.forge.core.organization.model.enumerations.ProjectStatus;

import javax.persistence.NoResultException;
import java.util.List;

/**
 * This class defines methods to access to {@link Project} data from persistence
 * 
 * @author sbenoist
 * @author Guillaume Lamirand
 * @see Project
 */
public interface ProjectDAO
{
  /**
   * This method will return a new entity detach of persistence context
   * 
   * @return new {@link Project}
   */
  Project newProject();

  /**
   * Find a {@link Project} by its project id
   * 
   * @param pProjectId
   *          the project id
   * @return {@link Project} found
   * @throws NoResultException
   *           thrown if no {@link Project} are existing for the id given
   */
  Project findByProjectId(final String pProjectId) throws NoResultException;

  /**
   * Find a {@link Project} by its project id
   * 
   * @param pProjectId
   *          the project id
   * @param pProjectOptions
   *          options used to retrieve children object
   * @return {@link Project} found
   * @throws NoResultException
   *           thrown if no {@link Project} are existing for the id given
   */
  Project findById(final String pProjectId, final ProjectOptions pProjectOptions);

  /**
   * Find {@link Project} list according their status
   * 
   * @param pProjectStatus
   *          the status used to retrieve projects
   * @return {@link List} of {@link Project} with the status required
   */
  List<Project> findAllByStatus(final ProjectStatus... pProjectStatus);

  /**
   * Find {@link Project} list and sub object according the options
   * 
   * @param pProjectOptions
   *          options used to retrieve children object
   * @param pProjectStatus
   *          the status used to retrieve projects
   * @return {@link List} of {@link Project} and icons with the status required
   */
  List<Project> findAllByStatus(ProjectOptions pProjectOptions, ProjectStatus... pProjectStatus);

  /**
   * Will persist the {@link Project} given from persistence context
   * 
   * @param pProject
   *          the project to persist
   * @return {@link Project} persist and attach to persistence context
   */
  Project persist(Project pProject);

  /**
   * Will update the {@link Project} given into persistence context
   * 
   * @param pProject
   *          the project to persist
   * @return {@link Project} updated and attached to persistence context
   */
  Project update(final Project pProject);

  /**
   * Will delete the {@link Project} given from persistence context
   * 
   * @param pProject
   *          the project to delete
   */
  void delete(final Project pProject);

}
