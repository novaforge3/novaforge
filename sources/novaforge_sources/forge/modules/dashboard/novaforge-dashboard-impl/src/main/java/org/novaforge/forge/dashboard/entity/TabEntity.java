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

import org.novaforge.forge.core.organization.entity.BinaryFileEntity;
import org.novaforge.forge.core.organization.model.BinaryFile;
import org.novaforge.forge.dashboard.model.DashBoard;
import org.novaforge.forge.dashboard.model.Tab;
import org.novaforge.forge.dashboard.model.Widget;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Define an entity to persist {@link Tab} information
 * 
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "DASHBOARD_TAB")
@NamedQueries({
    @NamedQuery(name = "TabEntity.findByUUID", query = "SELECT t FROM TabEntity t WHERE t.uuid = :uuid"),
    @NamedQuery(name = "TabEntity.findByIndex", query = "SELECT t FROM TabEntity t WHERE t.index = :index") })
public class TabEntity implements Tab
{

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long            id;

  // Has to refer explicitly ProjectEntity in order to generate correct metamodel
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = DashBoardEntity.class)
  @JoinColumn(name = "dashboard_id")
  private DashBoardEntity dashboard;

  @Column(name = "uuid", nullable = false, updatable = false)
  private String          uuid;

  @Column(name = "name", nullable = false)
  private String          name;

  @OneToOne(targetEntity = BinaryFileEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
  private BinaryFile      image;

  @Column(name = "index", nullable = false)
  private int             index;

  @Column(name = "layout_key", nullable = false)
  private String          layoutKey;

  @OneToMany(mappedBy = "tab", fetch = FetchType.EAGER, cascade = CascadeType.ALL,
      targetEntity = WidgetEntity.class, orphanRemoval = true)
  private List<Widget>    widgets = new ArrayList<Widget>();

  /**
   * Default constructor: will initialize the uuid
   */
  public TabEntity()
  {
    uuid = UUID.randomUUID().toString();
  }

  /**
   * Return the dashboard parent
   *
   * @return the dashboard parent
   */
  public DashBoard getDashboard()
  {
    return dashboard;
  }

  /**
   * Set the dashboard parent
   *
   * @param pDashboard
   *          the dashboard to set
   */
  public void setDashboard(final DashBoardEntity pDashboard)
  {
    dashboard = pDashboard;
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
   * {@inheritDoc}
   */
  @Override
  public BinaryFile getImage()
  {
    return image;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setImage(final BinaryFile pImage)
  {
    image = pImage;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getIndex()
  {
    return index;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setIndex(final int pIndex)
  {
    index = pIndex;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getLayoutKey()
  {
    return layoutKey;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setLayoutKey(final String pLayoutKey)
  {
    layoutKey = pLayoutKey;

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public List<Widget> getWidgets()
  {
    return Collections.unmodifiableList(widgets);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setWidgets(final List<Widget> pWidgets)
  {
    if (widgets == null)
    {
      widgets = new ArrayList<Widget>();
    }
    else
    {
      widgets.clear();
    }
    for (final Widget widget : pWidgets)
    {
      final WidgetEntity widgetEntity = (WidgetEntity) widget;
      widgetEntity.setTab(this);
      widgets.add(widgetEntity);
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addWidget(final Widget pWidget)
  {
    final WidgetEntity widgetEntity = (WidgetEntity) pWidget;
    widgetEntity.setTab(this);
    if (widgets == null)
    {
      widgets = new ArrayList<Widget>();
    }
    widgets.add(widgetEntity);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeWidget(final Widget pWidget)
  {
    final WidgetEntity widgetEntity = (WidgetEntity) pWidget;
    widgets.remove(widgetEntity);
    widgetEntity.setTab(null);

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
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = (prime * result) + ((id == null) ? 0 : id.hashCode());
    result = (prime * result) + index;
    result = (prime * result) + ((layoutKey == null) ? 0 : layoutKey.hashCode());
    result = (prime * result) + ((name == null) ? 0 : name.hashCode());
    result = (prime * result) + ((uuid == null) ? 0 : uuid.hashCode());
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof TabEntity))
    {
      return false;
    }
    final TabEntity other = (TabEntity) obj;
    if (id == null)
    {
      if (other.id != null)
      {
        return false;
      }
    }
    else if (!id.equals(other.id))
    {
      return false;
    }
    if (index != other.index)
    {
      return false;
    }
    if (layoutKey == null)
    {
      if (other.layoutKey != null)
      {
        return false;
      }
    }
    else if (!layoutKey.equals(other.layoutKey))
    {
      return false;
    }
    if (name == null)
    {
      if (other.name != null)
      {
        return false;
      }
    }
    else if (!name.equals(other.name))
    {
      return false;
    }
    if (uuid == null)
    {
      if (other.uuid != null)
      {
        return false;
      }
    }
    else if (!uuid.equals(other.uuid))
    {
      return false;
    }
    return true;
  }
}
