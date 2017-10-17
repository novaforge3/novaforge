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
package org.novaforge.forge.tools.requirements.common.internal.dao;

import org.apache.openjpa.persistence.OpenJPAPersistence;
import org.apache.openjpa.persistence.OpenJPAQuery;
import org.novaforge.forge.tools.requirements.common.dao.DirectoryDAO;
import org.novaforge.forge.tools.requirements.common.model.IDirectory;
import org.novaforge.forge.tools.requirements.common.model.IRepository;
import org.novaforge.forge.tools.requirements.common.model.IRequirement;
import org.novaforge.forge.tools.requirements.common.model.IRequirementVersion;
import serp.bytecode.Project;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

public class DirectoryDAOImpl implements DirectoryDAO
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
    entityManager = pEntityManager;
  }

  @Override
  public Set<IDirectory> findAllRootDirectoriesByRepository(final IRepository pRepository)
  {
    final TypedQuery<IDirectory> q = entityManager.createNamedQuery(
        "DirectoryEntity.findAllRootDirectoriesByRepository", IDirectory.class);

    final OpenJPAQuery<Project> openJPAQuery = OpenJPAPersistence.cast(q);
    openJPAQuery.getFetchPlan().setMaxFetchDepth(-1).addFetchGroup("directories_all");
    q.setParameter("repositoryURI", pRepository.getURI());
    q.setParameter("projectID", pRepository.getProject().getProjectId());
    return new HashSet<IDirectory>(q.getResultList());
  }

  @Override
  public IDirectory findDirectoryByReference(final String pReference)
  {
    final TypedQuery<IDirectory> q = entityManager.createNamedQuery(
        "DirectoryEntity.findDirectoryByReference", IDirectory.class);
    q.setParameter("reference", pReference);
    return q.getSingleResult();
  }

  @Override
  public Set<IDirectory> loadAllRootDirectoryTreesByRepository(final IRepository pRepository)
  {
    final TypedQuery<IDirectory> q = entityManager.createNamedQuery(
        "DirectoryEntity.findAllRootDirectoriesByRepository", IDirectory.class);
    q.setParameter("repositoryURI", pRepository.getURI());
    q.setParameter("projectID", pRepository.getProject().getProjectId());

    final Set<IDirectory> directories = new HashSet<IDirectory>(q.getResultList());
    for (final IDirectory directory : directories)
    {
      loadVersions(directory);
    }
    return directories;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IDirectory persist(final IDirectory pDirectory)
  {
    entityManager.persist(pDirectory);
    entityManager.flush();
    return pDirectory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IDirectory update(final IDirectory pDirectory)
  {
    entityManager.merge(pDirectory);
    entityManager.flush();
    return pDirectory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final IDirectory pDirectory)
  {
    entityManager.remove(pDirectory);
    entityManager.flush();

  }

  private void loadVersions(final IDirectory pDirectory)
  {
    final Set<IRequirement> requirements = pDirectory.getRequirements();
    for (final IRequirement requirement : requirements)
    {
      final Set<IRequirementVersion> versions = requirement.getRequirementVersions();
      versions.size();
    }

    final Set<IDirectory> directories = pDirectory.getChildrenDirectories();
    for (final IDirectory directory : directories)
    {
      loadVersions(directory);
    }
  }

}
