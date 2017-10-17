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
package org.novaforge.forge.core.organization.entity;

import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.Template;
import org.novaforge.forge.core.organization.model.TemplateInstance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author lamirang
 */
@Entity
@Table(name = "TEMPLATE_INSTANCE")
@NamedQuery(name = "TemplateInstanceEntity.findByProject",
    query = "SELECT t FROM TemplateInstanceEntity t WHERE t.project.elementId = :elementId")
public class TemplateInstanceEntity implements TemplateInstance, Serializable
{

  /**
    * 
    */
  private static final long serialVersionUID = -3446692451145261365L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
  @JoinColumn(name = "project_id", unique = true)
  private Project           project;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = TemplateEntity.class)
  @JoinColumn(name = "template_id")
  private Template          template;

  /**
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * @param id
   *          the id to set
   */
  public void setId(final Long id)
  {
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Project getProject()
  {
    return project;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProject(final Project pProject)
  {
    project = pProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Template getTemplate()
  {
    return template;
  }

  /**
   * @param pTemplate
   *          the template to set
   */
  @Override
  public void setTemplate(final Template pTemplate)
  {
    template = pTemplate;
  }

}