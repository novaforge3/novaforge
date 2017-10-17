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

import org.novaforge.forge.core.organization.dao.OrganizationDAO;
import org.novaforge.forge.core.organization.entity.OrganizationEntity;
import org.novaforge.forge.core.organization.entity.OrganizationEntity_;
import org.novaforge.forge.core.organization.model.Organization;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * JPA2 implementation of {@link OrganizationDAO}
 */
public class OrganizationDAOImpl implements OrganizationDAO
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
  public List<Organization> findAll()
  {
    final CriteriaBuilder             builder     = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Organization> allCriteria = builder.createQuery(Organization.class);
    final Root<OrganizationEntity>    entityRoot  = allCriteria.from(OrganizationEntity.class);
    allCriteria.orderBy(builder.asc(entityRoot.get(OrganizationEntity_.name)));
    allCriteria.select(entityRoot);
    return entityManager.createQuery(allCriteria).getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Organization findByName(final String pName)
  {
    final Query q = entityManager.createNamedQuery("OrganizationEntity.findByName");
    q.setParameter("name", pName);
    return (Organization) q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Organization newOrganization()
  {
    return new OrganizationEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Organization persist(final Organization pOrganization)
  {
    entityManager.persist(pOrganization);
    entityManager.flush();
    return pOrganization;
  }

}
