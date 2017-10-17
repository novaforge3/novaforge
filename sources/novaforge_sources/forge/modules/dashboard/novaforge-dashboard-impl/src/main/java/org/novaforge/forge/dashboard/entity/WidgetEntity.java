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
package org.novaforge.forge.dashboard.entity;

import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.dashboard.model.Widget;

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
import java.util.UUID;

/**
 * Define an entity to persist {@link Widget} information
 * 
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "DASHBOARD_WIDGET")
@NamedQueries({
    @NamedQuery(name = "WidgetEntity.findByUUID", query = "SELECT w FROM WidgetEntity w WHERE w.uuid = :uuid"),
    @NamedQuery(
        name = "WidgetEntity.increaseIndexBetween",
        query = "UPDATE WidgetEntity w SET w.areaIndex = w.areaIndex + 1 WHERE w.areaId = :areaId and w.areaIndex < :previousIndex AND w.areaIndex >= :newIndex"),
    @NamedQuery(
        name = "WidgetEntity.decreaseIndexBetween",
        query = "UPDATE WidgetEntity w SET w.areaIndex = w.areaIndex - 1 WHERE w.areaId = :areaId and w.areaIndex > :previousIndex AND w.areaIndex <= :newIndex"),
    @NamedQuery(
        name = "WidgetEntity.decreaseIndex",
        query = "UPDATE WidgetEntity w SET w.areaIndex = w.areaIndex - 1 WHERE w.areaId = :areaId and w.areaIndex > :index"),
    @NamedQuery(
        name = "WidgetEntity.increaseIndex",
        query = "UPDATE WidgetEntity w SET w.areaIndex = w.areaIndex + 1 WHERE w.areaId = :areaId and w.areaIndex >= :index") })
public class WidgetEntity implements Widget
{

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long      id;

  // Has to refer explicitly ProjectEntity in order to generate correct metamodel
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = TabEntity.class)
  @JoinColumn(name = "tab_id")
  private TabEntity tab;

  @Column(name = "uuid", nullable = false, updatable = false)
  private String    uuid;

  @Column(name = "name", nullable = false)
  private String    name;
  @Column(name = "module_key", nullable = false)
  private String    key;

  @Column(name = "area_id", nullable = false)
  private int       areaId;
  @Column(name = "area_index", nullable = false)
  private int       areaIndex;

  @Lob
  @Column(name = "datasource", length = 65535)
  // type sql TEXT
  private String    dataSource;

  @Lob
  @Column(name = "properties", length = 65535)
  // type sql TEXT
  private String    properties;

  /**
   * Default constructor: will initialize the uuid
   */
  public WidgetEntity()
  {
    uuid = UUID.randomUUID().toString();
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
   * @param pUUID
   *          the uuid to set
   */
  public void setUuid(final UUID pUUID)
  {
    uuid = pUUID.toString();
  }

  /**
   * Return the tab parent
   *
   * @return the tab parent
   */
  public Tab getTab()
  {
    return tab;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setKey(final String pKey)
  {
    key = pKey;

  }

  /**
   * Set the parent {@link Tab}
   *
   * @param pTabEntity
   *          the parent {@link Tab}
   */
  public void setTab(final TabEntity pTabEntity)
  {
    tab = pTabEntity;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getKey()
  {
    return key;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAreaId()
  {
    return areaId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAreaId(final int pAreaId)
  {
    areaId = pAreaId;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getAreaIndex()
  {
    return areaIndex;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAreaIndex(final int pIndex)
  {
    areaIndex = pIndex;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDataSource()
  {
    return dataSource;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setDataSource(final String pDataSource)
  {
    dataSource = pDataSource;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getProperties()
  {
    return properties;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setProperties(final String pProperties)
  {
    properties = pProperties;

  }

}
