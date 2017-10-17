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
package org.novaforge.forge.tools.deliverymanager.entity;

import org.novaforge.forge.tools.deliverymanager.model.Content;
import org.novaforge.forge.tools.deliverymanager.model.ContentType;
import org.novaforge.forge.tools.deliverymanager.model.Delivery;
import org.novaforge.forge.tools.deliverymanager.model.Node;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "CONTENT", uniqueConstraints = @UniqueConstraint(columnNames = { "type", "delivery_id" }))
@NamedQueries({ @NamedQuery(
                               name = "ContentEntity.findByDeliveryReference",
                               query = "SELECT c FROM ContentEntity c WHERE  c.delivery.projectId = :projectId AND c.delivery.reference = :deliveryReference"),
                @NamedQuery(
                               name = "ContentEntity.findByDeliveryReferenceAndType",
                               query = "SELECT c FROM ContentEntity c WHERE c.delivery.projectId = :projectId AND  c.delivery.reference = :deliveryReference and c.type = :type") })
public class ContentEntity implements Serializable, Content
{

  /**
   *
   */
  private static final long serialVersionUID = 6425118615233356095L;

  /**
   * @see ContentEntity#getId()
   */
  @Id @Column(name = "id", nullable = false, updatable = false) @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /**
   * @see ContentEntity#getType()
   * @see ContentEntity#setType(ContentType)
   * @see ContentType
   */
  @Column(name = "type", nullable = false) private String type;

  /**
   * @see ContentEntity#getNode()
   * @see ContentEntity#setNode(Node)
   */
  @OneToOne(fetch = FetchType.EAGER, cascade = { CascadeType.ALL }, targetEntity = FolderEntity.class)
  @JoinColumn(nullable = true, name = "root_node") private Node node;

  /**
   * @see ContentEntity#getDelivery()
   * @see ContentEntity#setDelivery(Delivery)
   */
  @ManyToOne(fetch = FetchType.LAZY, targetEntity = DeliveryEntity.class)
  @JoinColumn(name = "delivery_id", nullable = false) private Delivery delivery;

  /**
   * Default implementation
   */
  public ContentEntity()
  {
    // Used by JPA
  }

  /**
   * @return delviery id
   *
   * @see ContentEntity#id
   */
  public Long getId()
  {
    return id;
  }

  /**
   * {@inheritDoc}
   *
   * @see ContentEntity#type
   */
  @Override
  public ContentType getType()
  {
    return ContentType.getById(type);
  }

  /**
   * {@inheritDoc}
   *
   * @see ContentEntity#type
   */
  @Override
  public void setType(final ContentType pType)
  {
    type = pType.getId();

  }

  /**
   * {@inheritDoc}
   *
   * @see ContentEntity#node
   */
  @Override
  public Node getNode()
  {
    return node;
  }

  /**
   * {@inheritDoc}
   *
   * @see ContentEntity#node
   */
  @Override
  public void setNode(final Node pNode)
  {
    node = pNode;
  }

  /**
   * Allow to get the delivery regarding the artefact. It has to be called in a JPA context
   *
   * @return delivery regarding the artefact
   *
   * @see ContentEntity#delivery
   */
  public Delivery getDelivery()
  {
    return delivery;
  }

  /**
   * Allow to set the delivery regarding the artefact
   *
   * @param pDelivery
   *
   * @see ContentEntity#delivery
   */
  public void setDelivery(final Delivery pDelivery)
  {
    delivery = pDelivery;
  }

  @Override
  public int hashCode()
  {
    return Objects.hash(id, type, node);
  }

  @Override
  public boolean equals(final Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    final ContentEntity that = (ContentEntity) o;
    return Objects.equals(id, that.id) &&
               Objects.equals(type, that.type) &&
               Objects.equals(node, that.node);
  }
}
