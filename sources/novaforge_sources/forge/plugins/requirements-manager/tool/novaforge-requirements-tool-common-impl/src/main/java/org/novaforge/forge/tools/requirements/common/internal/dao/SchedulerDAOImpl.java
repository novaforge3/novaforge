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

import org.novaforge.forge.tools.requirements.common.dao.SchedulerDAO;
import org.novaforge.forge.tools.requirements.common.model.scheduling.SchedulingConfiguration;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Guillaume Morin
 */

public class SchedulerDAOImpl implements SchedulerDAO
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
   * 
   * @return SchedulingConfiguration or null otherwise
   */
  @Override
  public SchedulingConfiguration findScheduleConfigurationByProjectID(final String pProjectID)
  {
    SchedulingConfiguration returnedSchedulerConfuguration = null;
    try
    {
      TypedQuery<SchedulingConfiguration> query = entityManager
          .createNamedQuery("SchedulingConfigurationEntity.findScheduleConfigurationByProjectID",
              SchedulingConfiguration.class);
      query.setParameter("REF", pProjectID);
      returnedSchedulerConfuguration = query.getSingleResult();
    }
    catch (NoResultException e)
    {
      /**
       * Do nothing, we return null in this case;
       */
    }
    return returnedSchedulerConfuguration;
  }

  @Override
  public Set<SchedulingConfiguration> findAllScheduleConfiguration()
  {
    TypedQuery<SchedulingConfiguration> query = entityManager.createNamedQuery(
        "SchedulingConfigurationEntity.findAllScheduleConfiguration", SchedulingConfiguration.class);
    return new HashSet<SchedulingConfiguration>(query.getResultList());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SchedulingConfiguration persist(final SchedulingConfiguration pSchedulingConfiguration)
  {
    entityManager.persist(pSchedulingConfiguration);
    entityManager.flush();
    return pSchedulingConfiguration;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public SchedulingConfiguration update(final SchedulingConfiguration pSchedulingConfiguration)
  {
    entityManager.merge(pSchedulingConfiguration);
    entityManager.flush();
    return pSchedulingConfiguration;
  }

}
