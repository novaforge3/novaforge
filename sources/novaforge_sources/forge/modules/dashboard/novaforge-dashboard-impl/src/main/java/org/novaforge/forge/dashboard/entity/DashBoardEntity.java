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

import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.Tab;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Define an entity to persist {@link DashBoard} information
 * 
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "DASHBOARD")
@NamedQueries({ @NamedQuery(name = "DashBoardEntity.findByType",
    query = "SELECT d FROM DashBoardEntity d WHERE d.type = :type and d.typeId = :type_id") })
public class DashBoardEntity implements DashBoard
{

  /**
   * Serial version id
   */
  private static final long serialVersionUID = -4945615531290197742L;

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long              id;

  @Column(name = "uuid", nullable = false)
  private String            uuid;

  @Column(name = "type", nullable = false)
  @Enumerated
  private Type              type;

  @Column(name = "type_id", nullable = false)
  private String            typeId;

  @OneToMany(mappedBy = "dashboard", fetch = FetchType.EAGER, cascade = CascadeType.ALL,
      targetEntity = TabEntity.class, orphanRemoval = true)
  @OrderBy("index ASC")
  private List<Tab>         tabs             = new ArrayList<Tab>();

  /**
   * Constructor a dashboard for a given type
   * 
   * @param pType
   *          the type
   * @param pTypeId
   *          the id related
   */
  public DashBoardEntity(final Type pType, final String pTypeId)
  {
    type = pType;
    typeId = pTypeId;
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
  public Type getType()
  {
    return type;
  }

  /**
   * @param type
   *          the type to set
   */
  public void setType(final Type type)
  {
    this.type = type;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTypeId()
  {
    return typeId;
  }

  /**
   * @param typeId
   *          the typeId to set
   */
  public void setTypeId(final String typeId)
  {
    this.typeId = typeId;
  }

  /**
   * @param pUUID
   *     the uuid to set
   */
  public void setUuid(final UUID pUUID)
  {
    uuid = pUUID.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setTabs(final List<Tab> pTabs)
  {
    if (tabs == null)
    {
      tabs = new ArrayList<Tab>();
    }
    else
    {
      tabs.clear();
    }
    for (final Tab tab : pTabs)
    {
      final TabEntity tabEntity = (TabEntity) tab;
      tabEntity.setDashboard(this);
      tabs.add(tabEntity);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Tab> getTabs()
  {
    return Collections.unmodifiableList(tabs);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTab(final Tab pTab)
  {
    final TabEntity tabEntity = (TabEntity) pTab;
    tabEntity.setDashboard(this);
    if (tabs == null)
    {
      tabs = new ArrayList<Tab>();
    }
    tabs.add(tabEntity);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTab(final Tab pTab)
  {
    final TabEntity tabEntity = (TabEntity) pTab;
    tabs.remove(tabEntity);
    tabEntity.setDashboard(null);

  }

}
