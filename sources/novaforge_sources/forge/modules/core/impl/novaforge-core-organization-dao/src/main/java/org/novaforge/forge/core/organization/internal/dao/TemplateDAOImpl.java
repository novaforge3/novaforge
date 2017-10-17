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

import org.novaforge.forge.core.organization.dao.TemplateDAO;
import org.novaforge.forge.core.organization.entity.TemplateEntity;
import org.novaforge.forge.core.organization.entity.TemplateInstanceEntity;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateInstance;
import org.novaforge.forge.core.organization.model.enumerations.TemplateProjectStatus;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * JPA2 implementation of {@link TemplateDAO}
 * 
 * @author Guillaume Lamirand
 */
public class TemplateDAOImpl implements TemplateDAO
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

  /****************************************
   * The following methods will manage Template
   ****************************************/
  /**
   * {@inheritDoc}
   */
  @Override
  public Template newTemplate()
  {
    return new TemplateEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Template> getTemplatesByStatus(final TemplateProjectStatus pTemplateProjectStatus)
  {
    final TypedQuery<Template> q = entityManager.createNamedQuery("TemplateEntity.findByStatus",
        Template.class);
    q.setParameter("status", pTemplateProjectStatus);
    return q.getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Template> findAll()
  {
    final CriteriaBuilder         builder     = entityManager.getCriteriaBuilder();
    final CriteriaQuery<Template> allCriteria = builder.createQuery(Template.class);
    final Root<TemplateEntity>    entityRoot  = allCriteria.from(TemplateEntity.class);
    allCriteria.select(entityRoot);
    return entityManager.createQuery(allCriteria).getResultList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Template findByTemplateId(final String pTemplateId)
  {
    final TypedQuery<TemplateEntity> q = entityManager.createNamedQuery("ProjectElementEntity.findByElementId",
                                                                        TemplateEntity.class);
    q.setParameter("elementId", pTemplateId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Template persist(final Template pTemplate)
  {

    entityManager.persist(pTemplate);
    entityManager.flush();
    return pTemplate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Template update(final Template pTemplate)
  {
    entityManager.merge(pTemplate);
    entityManager.flush();
    return pTemplate;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final Template pTemplate)
  {
    entityManager.remove(pTemplate);
    entityManager.flush();

  }

  /****************************************
   * The following methods will manage TemplateInstance
   ****************************************/
  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateInstance newTemplateInstance()
  {
    return new TemplateInstanceEntity();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateInstance findByProjectId(final String pProjectId)
  {
    final TypedQuery<TemplateInstance> q = entityManager.createNamedQuery(
        "TemplateInstanceEntity.findByProject", TemplateInstance.class);
    q.setParameter("elementId", pProjectId);
    return q.getSingleResult();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public TemplateInstance persist(final TemplateInstance pTemplateInstance)
  {

    entityManager.persist(pTemplateInstance);
    entityManager.flush();
    return pTemplateInstance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void delete(final TemplateInstance pTemplateInstance)
  {
    entityManager.remove(pTemplateInstance);
    entityManager.flush();

  }

}
