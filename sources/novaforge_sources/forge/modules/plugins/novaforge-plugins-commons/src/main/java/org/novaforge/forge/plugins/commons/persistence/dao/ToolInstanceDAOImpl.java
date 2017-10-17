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

import org.novaforge.forge.core.plugins.dao.ToolInstanceDAO;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstanceStatus;
import org.novaforge.forge.plugins.commons.persistence.entity.ToolInstanceEntity;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * @author sbenoist
 */
public class ToolInstanceDAOImpl implements ToolInstanceDAO
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
  public List<ToolInstance> findAllInstances()
  {
    final TypedQuery<ToolInstance> q = entityManager.createNamedQuery("ToolInstanceEntity.findAll", ToolInstance.class);

    final List<ToolInstance> instances = q.getResultList();
    for (final ToolInstance instance : instances)
    {
      evaluateStatus(instance);
    }

    return instances;
  }

  @Override
  public ToolInstance findInstanceByName(final String pName)
  {
    ToolInstance returned = null;
    try
    {
      final TypedQuery<ToolInstance> q = entityManager.createNamedQuery("ToolInstanceEntity.findByName",
          ToolInstance.class);
      q.setParameter("name", pName);
      final ToolInstance result = q.getSingleResult();
      evaluateStatus(result);
      returned = result;
    }
    catch (final NoResultException e)
    {
      // if no result is found, null is returned
    }

    return returned;
  }

  @Override
  public ToolInstance findInstanceByUUID(final UUID pUUID)
  {
    ToolInstance returned = null;
    try
    {
      final TypedQuery<ToolInstance> q = entityManager.createNamedQuery("ToolInstanceEntity.findByUUID",
          ToolInstance.class);
      q.setParameter("toolId", pUUID.toString());
      final ToolInstance result = q.getSingleResult();
      evaluateStatus(result);
      returned = result;
    }
    catch (final NoResultException e)
    {
      // if no result is found, null is returned
    }

    return returned;
  }

  @Override
  public ToolInstance findInstanceByHost(final String pHost)
  {
    ToolInstance returned = null;
    try
    {
      final TypedQuery<ToolInstance> q = entityManager.createNamedQuery("ToolInstanceEntity.findByHost",
          ToolInstance.class);
      q.setParameter("host", "%" + pHost + "%");
      final ToolInstance result = q.getSingleResult();
      evaluateStatus(result);
      returned = result;
    }
    catch (final NoResultException e)
    {
      // if no result is found, null is returned
    }

    return returned;
  }

  @Override
  public Set<InstanceConfiguration> getApplicationsByName(final String pName)
  {
    final TypedQuery<InstanceConfiguration> q = entityManager.createNamedQuery(
        "ToolInstanceEntity.getApplicationsByName", InstanceConfiguration.class);
    q.setParameter("name", pName);
    return new HashSet<InstanceConfiguration>(q.getResultList());
  }

  @Override
  public Set<InstanceConfiguration> getApplicationsByUUID(final UUID pUUID)
  {
    final TypedQuery<InstanceConfiguration> q = entityManager.createNamedQuery(
        "ToolInstanceEntity.getApplicationsByUUID", InstanceConfiguration.class);
    q.setParameter("toolId", pUUID.toString());
    return new HashSet<InstanceConfiguration>(q.getResultList());
  }

  @Override
  public long countApplicationsByInstance(final UUID pUUID)
  {
    final TypedQuery<Long> q = entityManager.createNamedQuery(
        "ToolInstanceEntity.countApplicationsByInstance", Long.class);
    q.setParameter("toolId", pUUID.toString());
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstance persist(final ToolInstance pToolInstance)
  {
    entityManager.persist(pToolInstance);
    entityManager.flush();
    return pToolInstance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstance update(final ToolInstance pToolInstance)
  {
    entityManager.merge(pToolInstance);
    entityManager.flush();
    return pToolInstance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final ToolInstance pToolInstance)
  {
    // Attach entity to current persistence context
    final ToolInstance attachedEntity = entityManager.merge(pToolInstance);
    entityManager.remove(attachedEntity);
    entityManager.flush();
  }

  private void evaluateStatus(final ToolInstance pToolInstance)
  {
    final ToolInstanceEntity entity = (ToolInstanceEntity) pToolInstance;
    if (!entity.isShareable() && (entity.getApplications().size() > 0))
    {
      entity.setStatus(ToolInstanceStatus.BUSY);
    }
    else
    {
      entity.setStatus(ToolInstanceStatus.AVAILABLE);
    }
  }

}
