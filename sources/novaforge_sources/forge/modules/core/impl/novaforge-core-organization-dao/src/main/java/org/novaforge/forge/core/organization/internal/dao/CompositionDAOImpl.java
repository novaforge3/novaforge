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
package org.novaforge.forge.core.organization.internal.dao;

import org.novaforge.forge.core.organization.dao.CompositionDAO;
import org.novaforge.forge.core.organization.entity.CompositionEntity;
import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * JPA2 implementation of {@link CompositionDAO}
 * 
 * @author Guillaume Lamirand
 */
public class CompositionDAOImpl implements CompositionDAO
{

  /**
   * Constant for uuid paramater
   */
  private static final String UUID     = "uuid";
  /**
   * Constant for name paramater
   */
  private static final String NAME     = "name";
  /**
   * Constant for type paramater
   */
  private static final String TYPE     = "type";
  /**
   * Constant for instance paramater
   */
  private static final String INSTANCE = "instance";
  /**
   * Constant for project paramater
   */
  private static final String PROJECT  = "project";
  /**
   * {@link EntityManager} injected by container
   */
  private EntityManager       entityManager;

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
  public Composition newComposition()
  {
    return new CompositionEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Composition> findBySource(final String pInstance)
  {
    final TypedQuery<Composition> q = entityManager.createNamedQuery("CompositionEntity.findBySource",
        Composition.class);
    q.setParameter(INSTANCE, pInstance);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Composition> findByProject(final String pProjectId)
  {
    final TypedQuery<Composition> q = entityManager.createNamedQuery("CompositionEntity.findByProjectID",
                                                                     Composition.class);
    q.setParameter(PROJECT, pProjectId);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Composition> findBySourceAndAssociation(final String pInstance, final CompositionType pType,
      final String pName)
  {
    final TypedQuery<Composition> q = entityManager.createNamedQuery(
        "CompositionEntity.findBySourceAndAssociation", Composition.class);
    q.setParameter(INSTANCE, pInstance);
    q.setParameter(TYPE, pType.name());
    q.setParameter(NAME, pName);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Composition findByUUID(final String pUUID)
  {
    final TypedQuery<Composition> q = entityManager.createNamedQuery("CompositionEntity.findByUUID",
        Composition.class);
    q.setParameter(UUID, pUUID);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Composition update(final Composition pComposition)
  {
    entityManager.merge(pComposition);
    entityManager.flush();
    return pComposition;
  }

}
