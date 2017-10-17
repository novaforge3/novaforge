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
package org.novaforge.forge.tools.deliverymanager.internal.model;

import org.novaforge.forge.core.plugins.categories.ecm.DocumentNodeBean;
import org.novaforge.forge.tools.deliverymanager.model.ECMNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class ECMNodeImpl implements ECMNode
{
   /**
    * 
    */
   private static final long   serialVersionUID = -459162803105002311L;
   private final String        id;
   private final String        parentId;
   private final String        name;
   private final String        path;
   private final String        author;
   private final Date          createdDate;
   private final String        lastModified;
   private final Date          lastModifiedDate;
   private final boolean       isDocument;

   private final List<ECMNode> children         = new ArrayList<ECMNode>();

   /**
    * Build a {@link ECMNode} from a {@link DocumentNodeBean}
    * 
    * @param pNode
    */
   public ECMNodeImpl(final DocumentNodeBean pNode)
   {
      this.id = pNode.getId();
      this.parentId = pNode.getParentId();
      this.name = pNode.getName();
      this.path = pNode.getPath();
      this.author = pNode.getAuthor();
      this.createdDate = pNode.getCreatedDate();
      this.lastModified = pNode.getLastModified();
      this.lastModifiedDate = pNode.getLastModifiedDate();
      this.isDocument = pNode.isDocument();
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getId()
   {
      return this.id;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getName()
   {
      return this.name;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getPath()
   {
      return this.path;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public List<ECMNode> getChildren()
   {
      return Collections.unmodifiableList(this.children);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void addChild(final ECMNode pChild)
   {
      this.children.add(pChild);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public void removeChild(final ECMNode pChild)
   {
      this.children.remove(pChild);
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getAuthor()
   {
      return this.author;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Date getCreatedDate()
   {
      return this.createdDate;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getLastModified()
   {
      return this.lastModified;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public Date getLastModifiedDate()
   {
      return this.lastModifiedDate;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public boolean isDocument()
   {
      return this.isDocument;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String getParentId()
   {
      return this.parentId;
   }

   /**
    * {@inheritDoc}
    */
   @Override
   public String toString()
   {
      return "ECMNodeImpl [id=" + this.id + ", parentId=" + this.parentId + ", name=" + this.name + ", path="
                 + this.path + ", isDocument=" + this.isDocument + "]";
   }
}
