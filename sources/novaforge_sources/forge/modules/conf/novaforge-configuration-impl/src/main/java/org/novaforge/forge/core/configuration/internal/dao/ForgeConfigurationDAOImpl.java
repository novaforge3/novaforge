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
package org.novaforge.forge.core.configuration.internal.dao;

import org.novaforge.forge.core.configuration.dao.ForgeConfigurationDAO;
import org.novaforge.forge.core.configuration.entity.ForgeConfigurationEntity;
import org.novaforge.forge.core.configuration.model.ForgeConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;

/**
 * Persistence implementation of {@link ForgeConfigurationDAO}
 * 
 * @author Jeremy Casery <>
 */
public class ForgeConfigurationDAOImpl implements ForgeConfigurationDAO
{

  /**
   * {@link EntityManagerFactory} injected by container
   */
  private EntityManagerFactory entityManagerFactory;

  /**
   * Use by container to inject {@link EntityManagerFactory}
   * 
   * @param pEntityManagerFactory
   *          the entityManagerFactory to set
   */
  public void setEntityManagerFactory(final EntityManagerFactory pEntityManagerFactory)
  {
    entityManagerFactory = pEntityManagerFactory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeConfiguration findByKey(final String pKey)
  {
    final Query q = getEntityManager().createNamedQuery("ForgeConfigurationEntity.findByKey");
    q.setParameter("key", pKey);
    return (ForgeConfiguration) q.getSingleResult();
  }

  private EntityManager getEntityManager()
  {
    return entityManagerFactory.createEntityManager();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeConfiguration createOrupdate(final ForgeConfiguration pForgeConfiguration)
  {
    final EntityManager entityManager = getEntityManager();
    if (!entityManager.getTransaction().isActive())
    {
      entityManager.getTransaction().begin();
    }
    final ForgeConfiguration forgeConfiguration = entityManager.merge(pForgeConfiguration);
    entityManager.getTransaction().commit();
    return forgeConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ForgeConfiguration newForgeConfiguration(final String pKey, final String pValue)
  {
    return new ForgeConfigurationEntity(pKey, pValue);
  }


}
