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
import org.novaforge.forge.core.organization.model.ProjectApplication;
import org.novaforge.forge.core.organization.model.ProjectApplicationRequest;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapKeyColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "APP_REQUEST")
@NamedQueries({
    @NamedQuery(
        name = "ProjectApplicationRequestEntity.findOldest",
        query = "SELECT r FROM ProjectApplicationRequestEntity r WHERE r.created = "
            + "(SELECT MIN(r2.created) FROM ProjectApplicationRequestEntity r2 WHERE r2.app.pluginUUID = :pluginUUID)"),
    @NamedQuery(name = "ProjectApplicationRequestEntity.findByProjectAndApp",
        query = "SELECT r FROM ProjectApplicationRequestEntity r "
            + "WHERE r.project.elementId = :projectId AND r.app.pluginInstanceUUID = :instanceUUID"),
    @NamedQuery(name = "ProjectApplicationRequestEntity.findByProject",
        query = "SELECT r FROM ProjectApplicationRequestEntity r WHERE r.project.elementId = :projectId"),
    @NamedQuery(
        name = "ProjectApplicationRequestEntity.findByProjectAndPlugin",
        query = "SELECT r FROM ProjectApplicationRequestEntity r WHERE r.project.elementId = :projectId AND r.app.pluginUUID = :pluginUUID"),
    @NamedQuery(
        name = "ProjectApplicationRequestEntity.findByPlugin",
        query = "SELECT r FROM ProjectApplicationRequestEntity r WHERE r.app.pluginUUID = :pluginUUID ORDER BY r.created ASC") })
public class ProjectApplicationRequestEntity implements ProjectApplicationRequest, Serializable
{

  /**
   * Serialization version
   */
  private static final long        serialVersionUID = 4408095713251258879L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long                     id;

  // Has to refer explicitly ProjectEntity in order to generate correct metamodel
  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectEntity.class)
  @JoinColumn(name = "element_id", nullable = true)
  private ProjectEntity            project;

  // Has to refer explicitly ProjectEntity in order to generate correct metamodel
  @ManyToOne(fetch = FetchType.EAGER, targetEntity = ProjectApplicationEntity.class)
  @JoinColumn(name = "app_id", nullable = true)
  private ProjectApplicationEntity app;

  @ElementCollection(fetch = FetchType.EAGER)
  @MapKeyColumn(name = "forge_role")
  @Column(name = "application_role", nullable = true)
  @CollectionTable(name = "REQUEST_ROLES_MAPPING", joinColumns = @JoinColumn(name = "request_id"))
  private Map<String, String>      rolesMapping     = new HashMap<String, String>();

  @Column(name = "created", nullable = false, insertable = true, updatable = false)
  @Temporal(TemporalType.TIMESTAMP)
  private Date                     created;

  @Column(name = "login", nullable = true)
  private String                   login;

  /**
   * This will be called before a persist and flush event
   */
  @PrePersist
  public void onPersist()
  {
    setCreated(new Date());
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
   *          the project to set
   */
  @Override
  public void setProject(final Project pProject)
  {
    project = (ProjectEntity) pProject;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ProjectApplication getApplication()
  {
    return app;
  }

  /**
   * @param pApp
   *          the app to set
   */
  @Override
  public void setApplication(final ProjectApplication pApp)
  {
    app = (ProjectApplicationEntity) pApp;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Map<String, String> getRolesMapping()
  {
    return rolesMapping;
  }

  /**
   * Set roles mapping
   *
   * @param pRoleMapping
   *          represents the mapping to set
   */
  @Override
  public void setRolesMapping(final Map<String, String> pRoleMapping)
  {
    rolesMapping = pRoleMapping;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Date getCreated()
  {
    return created;
  }

  /**
   * @param pCreated
   *          the created to set
   */
  public void setCreated(final Date pCreated)
  {
    created = pCreated;
  }

  @Override
  public String getLogin()
  {
    return login;
  }

  @Override
  public void setLogin(final String login)
  {
    this.login = login;
  }

}