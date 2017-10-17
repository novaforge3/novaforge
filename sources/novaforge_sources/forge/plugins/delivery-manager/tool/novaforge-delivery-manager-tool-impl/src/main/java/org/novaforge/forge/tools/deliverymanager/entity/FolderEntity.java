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

import org.apache.openjpa.persistence.ElementDependent;
import org.novaforge.forge.tools.deliverymanager.model.Folder;
import org.novaforge.forge.tools.deliverymanager.model.Node;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This describes a folder persistence node.
 * 
 * @author Guillaume Lamirand
 */
@Entity
@Table(name = "NODE")
public class FolderEntity extends AbstractNodeEntity implements Serializable, Folder
{

  /**
   * Serial Version Id used for serialization
   */
  private static final long serialVersionUID = 6876094039292535239L;
  /**
   * @see FolderEntity#getChildNodes()
   * @see FolderEntity#addChildNode(Node)
   * @see FolderEntity#removeChildNode(Node)
   */
  @ElementDependent
  @OneToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
      CascadeType.REFRESH }, targetEntity = AbstractNodeEntity.class)
  @JoinTable(name = "NODE_NODE", joinColumns = @JoinColumn(name = "node_parent_id"),
      inverseJoinColumns = @JoinColumn(name = "node_child_id"))
  private List<Node>        childNodes       = new ArrayList<Node>();

  /**
   * {@inheritDoc}
   * 
   * @see FolderEntity#childNodes
   */
  @Override
  public List<Node> getChildNodes()
  {
    return Collections.unmodifiableList(childNodes);
  }

  /**
   * {@inheritDoc}
   * 
   * @see FolderEntity#childNodes
   */
  @Override
  public void addChildNode(final Node pNode)
  {
    childNodes.add(pNode);
  }

  /**
   * {@inheritDoc}
   * 
   * @see FolderEntity#childNodes
   */
  @Override
  public void removeChildNode(final Node pNode)
  {
    childNodes.remove(pNode);
  }

  /**
   * @param childNodes
   *          the childNodes to set
   */
  public void setChildNodes(final List<Node> childNodes)
  {
    this.childNodes = childNodes;
  }

  /**
   * Remove all children
   */
  public void removeAll()
  {
    childNodes.clear();
  }

  /**
   * Remove all children
   * 
   * @param pNodes
   *          represents new nodes
   */
  public void addAll(final List<Node> pNodes)
  {
    childNodes.addAll(pNodes);
  }

  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = super.hashCode();
    result = (prime * result) + ((childNodes == null) ? 0 : childNodes.hashCode());
    return result;
  }

  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (!super.equals(obj))
    {
      return false;
    }
    if (this.getClass() != obj.getClass())
    {
      return false;
    }
    final FolderEntity other = (FolderEntity) obj;
    if (childNodes == null)
    {
      if (other.childNodes != null)
      {
        return false;
      }
    }
    else if (!childNodes.equals(other.childNodes))
    {
      return false;
    }
    return true;
  }

}
