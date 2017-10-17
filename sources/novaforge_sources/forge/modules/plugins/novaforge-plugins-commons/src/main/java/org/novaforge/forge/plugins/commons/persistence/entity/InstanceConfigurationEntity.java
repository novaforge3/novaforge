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
package org.novaforge.forge.plugins.commons.persistence.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.core.plugins.domain.plugin.InstanceConfiguration;
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstance;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Thhis entity is stored information about plugin instance
 * 
 * @author lamirang
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "InstanceConfigurationEntity.findByForge",
        query = "SELECT i FROM InstanceConfigurationEntity i WHERE i.forgeId = :forge"),
    @NamedQuery(name = "InstanceConfigurationEntity.findByInstance",
        query = "SELECT i FROM InstanceConfigurationEntity i WHERE i.instanceId = :instance"),
    @NamedQuery(name = "InstanceConfigurationEntity.findByForgeProject",
        query = "SELECT i FROM InstanceConfigurationEntity i WHERE i.forgeProjectId = :project"),
    @NamedQuery(name = "InstanceConfigurationEntity.findByToolProject",
        query = "SELECT i FROM InstanceConfigurationEntity i WHERE i.toolProjectId = :project") })
@Table(name = "plugin_instance")
public class InstanceConfigurationEntity implements InstanceConfiguration
{

  /**
   * Thid field is the main technical ID
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, updatable = false)
  private long         id;

  /**
   * Field representing forge identification (UUID)
   */
  @Column(name = "forge_id", nullable = false)
  private String       forgeId;

  /**
   * Field representing instance identification (UUID)
   */
  @Column(name = "instance_id", unique = true, nullable = false)
  private String       instanceId;

  /**
   * Field representing specific configuration identification (simple string without any special caracteres)
   */
  @Column(name = "configuration_id", nullable = false)
  private String       configurationId;

  /**
   * Field representing project id used by forge
   */
  @Column(name = "forge_project_id", nullable = false)
  private String       forgeProjectId;

  /**
   * Field representing project id used by tool
   */
  @Column(name = "tool_project_id")
  private String       toolProjectId;

  /**
   * Field representing the tool associated
   */
  // @ManyToOne(targetEntity = ToolInstanceEntity.class, cascade = { CascadeType.PERSIST, CascadeType.MERGE,
  // CascadeType.REMOVE, CascadeType.REFRESH })
  @ManyToOne(targetEntity = ToolInstanceEntity.class)
  @JoinColumn(name = "tool_instance_id", nullable = false, insertable = true, updatable = true)
  private ToolInstance toolInstance;

  /**
   * Constructor public needed by JPA2
   */
  public InstanceConfigurationEntity()
  {
    // Used by JPA2
  }

  /**
   * {{@inheritDoc}
   */
  @Override
  public long getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setId(final long pId)
  {
    id = pId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getForgeId()
  {
    return forgeId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setForgeId(final String pForgeId)
  {
    forgeId = pForgeId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getInstanceId()
  {
    return instanceId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInstanceId(final String pInstanceId)
  {
    instanceId = pInstanceId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getConfigurationId()
  {
    return configurationId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setConfigurationId(final String pConfigurationId)
  {
    configurationId = pConfigurationId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getForgeProjectId()
  {
    return forgeProjectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setForgeProjectId(final String pForgeProjectId)
  {
    forgeProjectId = pForgeProjectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getToolProjectId()
  {
    return toolProjectId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setToolProjectId(final String pToolProjectId)
  {
    toolProjectId = pToolProjectId;
  }

  @Override
  public ToolInstance getToolInstance()
  {
    return toolInstance;
  }

  @Override
  public void setToolInstance(final ToolInstance pTool)
  {
    toolInstance = pTool;
    ToolInstanceEntity entity = (ToolInstanceEntity) pTool;
    entity.addApplication(this);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(id).append(forgeId).append(instanceId).append(configurationId)
                                .append(forgeProjectId).append(toolProjectId).toHashCode();
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof InstanceConfigurationEntity))
    {
      return false;
    }
    final InstanceConfigurationEntity castOther = (InstanceConfigurationEntity) other;
    return new EqualsBuilder().append(id, castOther.id).append(forgeId, castOther.forgeId)
        .append(instanceId, castOther.instanceId).append(configurationId, castOther.configurationId)
        .append(forgeProjectId, castOther.forgeProjectId).append(toolProjectId, castOther.toolProjectId)
        .isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "InstanceConfigurationEntity [id=" + id + ", forgeId=" + forgeId + ", instanceId=" + instanceId
               + ", configurationId=" + configurationId + ", forgeProjectId=" + forgeProjectId + ", toolProjectId="
               + toolProjectId + ", toolInstance=" + toolInstance + "]";
  }
}
