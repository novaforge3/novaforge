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
package org.novaforge.forge.core.plugins.entity;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceMetadata;
import org.novaforge.forge.core.plugins.domain.core.PluginPersistenceQueues;
import org.novaforge.forge.core.plugins.domain.core.PluginStatus;
import org.novaforge.forge.core.plugins.domain.core.PluginViewEnum;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author lamirang @date Mar 23, 2011
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "PluginMetadataEntity.findByUUID",
        query = "SELECT p FROM PluginMetadataEntity p WHERE p.uuid = :uuid"),
    @NamedQuery(name = "PluginMetadataEntity.findByCategory",
        query = "SELECT p FROM PluginMetadataEntity p WHERE p.category IN :categories"),
    @NamedQuery(name = "PluginMetadataEntity.findByType",
        query = "SELECT p FROM PluginMetadataEntity p WHERE p.type = :label"),
    @NamedQuery(name = "PluginMetadataEntity.findByStatus",
        query = "SELECT p FROM PluginMetadataEntity p WHERE p.status = :status"),
    @NamedQuery(
        name = "PluginMetadataEntity.findByStatusAndAvailability",
        query = "SELECT p FROM PluginMetadataEntity p WHERE p.availability = :available and p.status = :status"),
    @NamedQuery(
        name = "PluginMetadataEntity.findByCategoryAndStatusAndAvailability",
        query = "SELECT p FROM PluginMetadataEntity p WHERE p.category = :category and p.availability = :available and p.status = :status"),
    @NamedQuery(name = "PluginMetadataEntity.findCategories",
        query = "SELECT distinct p.category FROM PluginMetadataEntity p"),
    @NamedQuery(name = "PluginMetadataEntity.findByViewId",
        query = "SELECT p FROM PluginMetadataEntity p, IN(p.views) v WHERE v = :view ") })
@Table(name = "PLUGIN_METADATA")
public class PluginMetadataEntity implements PluginPersistenceMetadata
{

  /**
   * Serial version id
   */
  private static final long       serialVersionUID = 4572263328051448102L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private long                    id;

  @Column(name = "uuid", updatable = false)
  private String                  uuid;

  @Column(name = "description")
  private String                  description;

  @Column(name = "version")
  private String                  version;

  @Column(name = "type")
  private String                  type;

  @Column(name = "category")
  private String                  category;

  @ElementCollection(fetch = FetchType.EAGER)
  @Enumerated(EnumType.STRING)
  @CollectionTable(name = "PLUGIN_VIEW", joinColumns = @JoinColumn(name = "plugin_metadata_id"))
  @Column(name = "view")
  private List<PluginViewEnum>    views            = new ArrayList<PluginViewEnum>();

  @ManyToOne(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, targetEntity = PluginQueuesEntity.class)
  @JoinColumn(name = "queues_id", referencedColumnName = "id")
  private PluginPersistenceQueues queues;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private PluginStatus            status;

  @Column(name = "available", nullable = false)
  private boolean                 availability;

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    return new HashCodeBuilder().append(uuid).toHashCode();
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
    if (!(other instanceof PluginMetadataEntity))
    {
      return false;
    }
    final PluginMetadataEntity castOther = (PluginMetadataEntity) other;
    return new EqualsBuilder().append(uuid, castOther.getUUID()).isEquals();
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  public String getType()
  {
    return type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setType(final String pType)
  {
    type = pType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getVersion()
  {
    return version;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setVersion(final String pVersion)
  {
    version = pVersion;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getCategory()
  {
    return category;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setCategory(final String pCategory)
  {
    category = pCategory;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginPersistenceQueues getJMSQueues()
  {
    return queues;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setJMSQueues(final PluginPersistenceQueues pPluginQueues)
  {
    queues = pPluginQueues;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public PluginStatus getStatus()
  {
    return status;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setStatus(final PluginStatus pStatus)
  {
    status = pStatus;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isAvailable()
  {
    return availability;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAvailable(final boolean pAvailability)
  {
    availability = pAvailability;
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
  public List<PluginViewEnum> getViews()
  {
    return views;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setViews(final List<PluginViewEnum> views)
  {
    this.views = views;
  }

}
