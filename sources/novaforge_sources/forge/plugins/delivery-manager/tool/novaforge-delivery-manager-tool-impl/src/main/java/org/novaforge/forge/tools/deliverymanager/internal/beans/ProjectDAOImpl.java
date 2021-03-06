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
package org.novaforge.forge.tools.deliverymanager.internal.beans;

import org.novaforge.forge.tools.deliverymanager.dao.ProjectDAO;
import org.novaforge.forge.tools.deliverymanager.entity.ProjectEntity;
import org.novaforge.forge.tools.deliverymanager.model.Project;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * @author sbenoist
 */

public class ProjectDAOImpl implements ProjectDAO
{
  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager entityManager;

  /**
   * Use by container to inject {@link EntityManager}
   * 
   * @param pEntityManager
   *          the entityManager to set
   */
  public void setEntityManager(final EntityManager pEntityManager)
  {
    this.entityManager = pEntityManager;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project newProject(final String pProjectId, final String pName, final String pDescription)

  {
    return new ProjectEntity(pProjectId, pName, pDescription);
  }

  @Override
  public Project findProjectById(final String pProjectId)
  {
    TypedQuery<Project> q = entityManager.createNamedQuery("ProjectEntity.findProjectById", Project.class);
    q.setParameter("projectId", pProjectId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project persist(final Project pProject)
  {
    entityManager.persist(pProject);
    entityManager.flush();
    return pProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project update(final Project pProject)
  {
    entityManager.merge(pProject);
    entityManager.flush();
    return pProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Project pProject)
  {
    entityManager.remove(pProject);
    entityManager.flush();

  }

}
