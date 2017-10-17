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

import org.novaforge.forge.tools.requirements.common.dao.MembershipDAO;
import org.novaforge.forge.tools.requirements.common.entity.MembershipEntity;
import org.novaforge.forge.tools.requirements.common.model.IProject;
import org.novaforge.forge.tools.requirements.common.model.Membership;
import org.novaforge.forge.tools.requirements.common.model.Role;
import org.novaforge.forge.tools.requirements.common.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * @author sbenoist
 */
public class MembershipDAOImpl implements MembershipDAO
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
  public Membership newMembership(final User pUser, final IProject pProject, final Role pRole)

  {
    return new MembershipEntity(pUser, pProject, pRole);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Membership persist(final Membership pMembership)
  {
    entityManager.persist(pMembership);
    entityManager.flush();
    return pMembership;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Membership update(final Membership pMembership)
  {
    entityManager.merge(pMembership);
    entityManager.flush();
    return pMembership;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Membership pMembership)
  {
    entityManager.remove(pMembership);
    entityManager.flush();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Membership> findAllByProject(final String pProjectId)
  {
    final TypedQuery<Membership> q = entityManager.createNamedQuery("MembershipEntity.findAllByProject",
        Membership.class);
    q.setParameter("projectId", pProjectId);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Membership> findAllByUser(final String pLogin)
  {
    final TypedQuery<Membership> q = entityManager.createNamedQuery("MembershipEntity.findAllByUser", Membership.class);
    q.setParameter("login", pLogin);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Membership findByUserAndProject(final String pLogin, final String pProjectId)
  {
    final TypedQuery<Membership> q = entityManager.createNamedQuery("MembershipEntity.findByUserAndProject",
        Membership.class);
    q.setParameter("login", pLogin);
    q.setParameter("projectId", pProjectId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean exist(final String pLogin, final String pProjectId)
  {
    // it seeems to be difficult to use Criteri API with depth in arguments like for this query
    boolean exist = true;
    try
    {
      this.findByUserAndProject(pLogin, pProjectId);
    }
    catch (NoResultException e)
    {
      exist = false;
    }
    return exist;
  }

}
