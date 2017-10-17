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
package org.novaforge.forge.plugins.commons.persistence.dao;

import org.novaforge.forge.core.plugins.dao.InstanceConfigurationDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.plugins.commons.persistence.entity.ToolInstanceEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class allows to manage instance data
 * 
 * @author lamirang
 */
public class InstanceConfigurationDAOImpl implements InstanceConfigurationDAO
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

  /**
   * {@inheritDoc}
   */
  @Override
  public List<InstanceConfiguration> findByForgeId(final String pForge)
  {
    final TypedQuery<InstanceConfiguration> q = entityManager.createNamedQuery(
        "InstanceConfigurationEntity.findByForge", InstanceConfiguration.class);
    q.setParameter("forge", pForge);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InstanceConfiguration findByInstanceId(final String pInstanceID) throws NoResultException
  {
    final TypedQuery<InstanceConfiguration> q = entityManager.createNamedQuery(
        "InstanceConfigurationEntity.findByInstance", InstanceConfiguration.class);
    q.setParameter("instance", pInstanceID);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<InstanceConfiguration> findByProject(final String pForgeProject)
  {
    final TypedQuery<InstanceConfiguration> q = entityManager.createNamedQuery(
        "InstanceConfigurationEntity.findByForgeProject", InstanceConfiguration.class);
    q.setParameter("project", pForgeProject);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InstanceConfiguration findByToolProject(final String pToolProject) throws NoResultException
  {
    final TypedQuery<InstanceConfiguration> q = entityManager.createNamedQuery(
        "InstanceConfigurationEntity.findByToolProject", InstanceConfiguration.class);
    q.setParameter("project", pToolProject);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InstanceConfiguration persist(final InstanceConfiguration pInstanceConfiguration)
  {
    entityManager.persist(pInstanceConfiguration);
    entityManager.flush();
    return pInstanceConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public InstanceConfiguration update(final InstanceConfiguration pInstanceConfiguration)
  {
    entityManager.merge(pInstanceConfiguration);
    entityManager.flush();
    return pInstanceConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final InstanceConfiguration pInstanceConfiguration)
  {
    // To avoid remove a detach entity, we merge it beforge
    final InstanceConfiguration merge = entityManager.merge(pInstanceConfiguration);

    // Remove the application from the tool
    ToolInstanceEntity tool = (ToolInstanceEntity) merge.getToolInstance();
    tool.removeApplication(merge);

    entityManager.remove(merge);
    entityManager.flush();
  }

}
