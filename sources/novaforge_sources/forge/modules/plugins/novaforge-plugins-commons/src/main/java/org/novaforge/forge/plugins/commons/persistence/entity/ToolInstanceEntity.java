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
import org.novaforge.forge.core.plugins.domain.plugin.ToolInstanceStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * This entity represents the plugin tool
 * 
 * @author sbenoist
 */
@Entity
@Table(name = "plugin_tool")
@NamedQueries({
    @NamedQuery(name = "ToolInstanceEntity.findAll", query = "SELECT p FROM ToolInstanceEntity p"),
    @NamedQuery(name = "ToolInstanceEntity.findByName",
        query = "SELECT p FROM ToolInstanceEntity p WHERE p.name = :name"),
    @NamedQuery(name = "ToolInstanceEntity.getApplicationsByName",
        query = "SELECT apps FROM ToolInstanceEntity p JOIN p.applications AS apps WHERE p.name = :name"),
    @NamedQuery(name = "ToolInstanceEntity.findByUUID",
        query = "SELECT p FROM ToolInstanceEntity p WHERE p.toolId = :toolId"),
    @NamedQuery(name = "ToolInstanceEntity.getApplicationsByUUID",
        query = "SELECT apps FROM ToolInstanceEntity p JOIN p.applications AS apps WHERE p.toolId = :toolId"),
    @NamedQuery(name = "ToolInstanceEntity.findByHost",
        query = "SELECT p FROM ToolInstanceEntity p WHERE p.baseURL LIKE :host"),
    @NamedQuery(name = "ToolInstanceEntity.countApplicationsByInstance",
        query = "SELECT SIZE(p.applications) FROM ToolInstanceEntity p WHERE p.toolId = :toolId") })
public class ToolInstanceEntity implements ToolInstance
{
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false, updatable = false)
  private long                       id;

  /**
   * Field representing tool identification (UUID)
   */
  @Column(name = "tool_id", nullable = false, unique = true)
  private String                     toolId;

  @Column(name = "name", nullable = false, unique = true)
  private String                     name;

  @Column(name = "description", nullable = true)
  private String                     description;

  // this field is calculated
  @Transient
  private ToolInstanceStatus         status;

  @Column(name = "internal", nullable = false)
  private boolean                    internal     = false;

  @Column(name = "shareable", nullable = false)
  private boolean                    shareable    = true;

  @Column(name = "base_url")
  private String                     baseURL;

  @Column(name = "alias")
  private String                     alias;

  // Linked to many applications : this field is not used in the API but it allows to calculate the status of
  // the tool instance
  @OneToMany(mappedBy = "toolInstance", fetch = FetchType.EAGER,
      targetEntity = InstanceConfigurationEntity.class)
  private Set<InstanceConfiguration> applications = new HashSet<InstanceConfiguration>();

  /**
   * @return id element
   */
  public long getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public UUID getUUID()
  {
    return UUID.fromString(toolId);
  }

  /**
   * @param pID
   *          the toolId to set
   */
  public void setUUID(final UUID pID)
  {
    toolId = pID.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getName()
  {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setName(final String pName)
  {
    name = pName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDescription(final String pDescription)
  {
    description = pDescription;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ToolInstanceStatus getToolInstanceStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isInternal()
  {
    return internal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setInternal(final boolean pInternal)
  {
    internal = pInternal;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isShareable()
  {
    return shareable;
  }

  /**
   * @return the internal
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public void setShareable(final boolean pShareable)
  {
    shareable = pShareable;
  }

  /**
   * @param pInternal
   *          the internal to set
   */

  /**
   * {@inheritDoc}
   */
  @Override
  public String getAlias()
  {
    return alias;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAlias(final String pAlias)
  {
    alias = pAlias;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public URL getBaseURL()
  {
    URL url = null;
    if (baseURL != null)
    {
      try
      {
        url = new URL(baseURL);
      }
      catch (final MalformedURLException e)
      {
        e.printStackTrace();
      }
    }
    return url;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBaseURL(final URL pBaseURL)
  {
    baseURL = pBaseURL.toString();
  }

  public void setStatus(final ToolInstanceStatus pStatus)
  {
    status = pStatus;
  }

  /**
   * @return an unmodifiable {@link Set}
   */
  public Set<InstanceConfiguration> getApplications()
  {
    return Collections.unmodifiableSet(applications);
  }

  /**
   * @param pApplications
   *          the applications to set
   */
  public void setApplications(final Set<InstanceConfiguration> pApplications)
  {
    applications = pApplications;
  }

  public void addApplication(final InstanceConfiguration pInstanceConfiguration)
  {
    applications.add(pInstanceConfiguration);
  }

  public void removeApplication(final InstanceConfiguration pInstanceConfiguration)
  {
    applications.remove(pInstanceConfiguration);
  }

  /*
   * (non-Javadoc)
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(toolId).append(name).append(baseURL).toHashCode();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object other)
  {
    if (this == other)
    {
      return true;
    }
    if (!(other instanceof ToolInstanceEntity))
    {
      return false;
    }
    final ToolInstanceEntity castOther = (ToolInstanceEntity) other;
    return new EqualsBuilder().append(toolId, castOther.toolId).append(name, castOther.name)
        .append(baseURL, castOther.baseURL).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString()
  {
    return "ToolInstanceEntity [id=" + id + ", toolId=" + toolId + ", name=" + name + ", description=" + description
               + ", status=" + status + ", shareable=" + shareable + ", baseURL=" + baseURL + ", alias=" + alias + "]";
  }

}
