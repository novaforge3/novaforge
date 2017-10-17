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

import org.novaforge.forge.core.organization.model.Composition;
import org.novaforge.forge.core.organization.model.CompositionType;
import org.novaforge.forge.core.organization.model.Project;
import org.novaforge.forge.core.organization.model.ProjectApplication;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

/**
 * This describes a persisted {@link Composition}
 * 
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "COMPOSITION")
@NamedQueries({
    @NamedQuery(name = "CompositionEntity.findByUUID",
        query = "SELECT c FROM CompositionEntity c WHERE c.uuid = :uuid"),
    @NamedQuery(name = "CompositionEntity.findByProjectID",
        query = "SELECT c FROM CompositionEntity c WHERE c.project.elementId = :project"),
    @NamedQuery(name = "CompositionEntity.findBySource",
        query = "SELECT c FROM CompositionEntity c WHERE c.sourceApplication.pluginInstanceUUID = :instance"),
    @NamedQuery(name = "CompositionEntity.findBySourceAndAssociation",
        query = "SELECT c FROM CompositionEntity c WHERE c.sourceApplication.pluginInstanceUUID = :instance "
            + "AND c.type = :type AND c.sourceAssociation = :name") })
public class CompositionEntity implements Composition
{

  /**
   * Serial version id
   */
  private static final long  serialVersionUID = -2632737754405846342L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long               id;

  @Column(name = "uuid", nullable = false, unique = false, updatable = false)
  private String             uuid;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
  @JoinColumn(name = "project_id", nullable = false)
  private Project            project;

  @Column(name = "type", nullable = false)
  private String             type;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectApplicationEntity.class)
  @JoinColumn(name = "application_source", nullable = false)
  private ProjectApplication sourceApplication;

  @Column(name = "source_name", nullable = false)
  private String             sourceAssociation;

  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectApplicationEntity.class)
  @JoinColumn(name = "application_target", nullable = false)
  private ProjectApplication targetApplication;

  @Column(name = "target_name", nullable = false)
  private String             targetAssociation;

  @Lob
  @Column(name = "template", nullable = true)
  private String             template;

  @Column(name = "activated", nullable = false)
  private boolean            activated;

  /**
   * @return the id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public UUID getUUID()
  {
    return UUID.fromString(uuid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setUUID(final UUID pUUID)
  {
    uuid = pUUID.toString();
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
   * @param pProject
   */
  public void setProject(final Project pProject)
  {
    project = pProject;
  }

  /**
   * {@inheritDoc}
   */
  @NotNull
  @Override
  public CompositionType getType()
  {
    return CompositionType.valueOf(type);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setType(final CompositionType pType)
  {
    type = pType.name();

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public ProjectApplication getSource()
  {
    return sourceApplication;
  }

  /**
   * @param pSource
   *          the source to set
   */
  @Override
  public void setSource(final ProjectApplication pSource)
  {
    sourceApplication = pSource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  @Size(min = 1)
  public String getSourceName()
  {
    return sourceAssociation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSourceName(final String pName)
  {
    sourceAssociation = pName;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public ProjectApplication getTarget()
  {
    return targetApplication;
  }

  /**
   * @param pTarget
   *          the target to set
   */
  @Override
  public void setTarget(final ProjectApplication pTarget)
  {
    targetApplication = pTarget;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTargetName()
  {
    return targetAssociation;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTargetName(final String pName)
  {
    targetAssociation = pName;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTemplate()
  {
    return template;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTemplate(final String template)
  {
    this.template = template;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public boolean isActivated()
  {
    return activated;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActivated(final boolean pActivated)
  {
    activated = pActivated;
  }

  @Override
  public String toString()
  {
    return "CompositionEntity [id=" + id + ", uuid=" + uuid + ", type=" + type + ", sourceAssociation="
               + sourceAssociation + ", targetAssociation=" + targetAssociation + ", template=" + template
               + ", activated=" + activated + "]";
  }
}