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
package org.novaforge.forge.tools.deliverymanager.ui.client.view.commons.celltree.nodesselection;

/**
 * @author caseryj
 *
 */

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ArtefactNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.FolderNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.NodeDTO;
import org.novaforge.forge.ui.commons.client.mimetype.MimeType;

import java.util.ArrayList;

/**
 * The {@link TreeViewModel} used to organize contacts into a hierarchy.
 */
public class NodeTreeViewModel implements TreeViewModel
{

   private static NodesSelectionTreesResources ressources     = GWT.create(NodesSelectionTreesResources.class);
   private final boolean isFolderSelectable;
   private ListDataProvider<NodeDTO>           dataProvider   = new ListDataProvider<NodeDTO>(
         new ArrayList<NodeDTO>(),
         CellKey.NODE_KEY_PROVIDER);
   private MultiSelectionModel<NodeDTO>        selectionModel = new MultiSelectionModel<NodeDTO>(
         CellKey.NODE_KEY_PROVIDER);

   public NodeTreeViewModel()
   {
      this(true);
   }

   public NodeTreeViewModel(final boolean pFolderSelectable)
   {
      super();
      this.isFolderSelectable = pFolderSelectable;
   }

   public Boolean getValue(final NodeDTO object)
   {
      return this.getSelectionModel().isSelected(object);
   }

   /**
    * @return the selectionModel
    */
   public MultiSelectionModel<NodeDTO> getSelectionModel()
   {
      return this.selectionModel;
   }

   /**
    * @param pSelectionModel
    *     the selectionModel to set
    */
   public void setSelectionModel(final MultiSelectionModel<NodeDTO> pSelectionModel)
   {
      this.selectionModel = pSelectionModel;
   }

   @Override
   public <T> NodeInfo<?> getNodeInfo(final T value)
   {
      final ValueUpdater<NodeDTO> valueUpdater = new AbstractSelectValueUpdater()
      {

         @Override
         public MultiSelectionModel<NodeDTO> getSelectionModel()
         {
            return NodeTreeViewModel.this.selectionModel;
         }

      };
      if (value == null)
      {
         return new DefaultNodeInfo<NodeDTO>(this.dataProvider, new NodeCell(this.isFolderSelectable),
               this.selectionModel, valueUpdater);
      }
      else if (value instanceof FolderNode)
      {
         final FolderNode node = (FolderNode) value;
         return new DefaultNodeInfo<NodeDTO>(new ListDataProvider<NodeDTO>(node.getChildren()), new NodeCell(
               this.isFolderSelectable), this.selectionModel, valueUpdater);
      }
      else if (value instanceof ArtefactNode)
      {
         return new DefaultNodeInfo<NodeDTO>(null, new NodeCell(), this.selectionModel, valueUpdater);
      }

      // Unhandled type.
      final String type = value.getClass().getName();
      throw new IllegalArgumentException("Unsupported object type: " + type);
   }

   @Override
   public boolean isLeaf(final Object value)
   {
      boolean isLeaf = false;
      if ((value != null) && ((value instanceof ArtefactNode)))
      {
         isLeaf = true;
      }
      else if ((value != null) && ((value instanceof FolderNode) && (((FolderNode) value)).getChildren().isEmpty()))
      {
         isLeaf = true;
      }
      return isLeaf;
   }

   /**
    * @return the eCMNodeDataProvider
    */
   public ListDataProvider<NodeDTO> getDataProvider()
   {
      return this.dataProvider;
   }

   /**
    * @param pECMNodeDataProvider
    *           the eCMNodeDataProvider to set
    */
   public void setDataProvider(final ListDataProvider<NodeDTO> pDataProvider)
   {
      this.dataProvider = pDataProvider;
   }

   /**
    * The cell used to render folder.
    */
   private static class NodeCell extends AbstractCell<NodeDTO>
   {
      private static NodesSelectionMessage messages           = GWT.create(NodesSelectionMessage.class);
      private boolean                      isFolderSelectable = true;

      public NodeCell()
      {
         this(true);
      }

      public NodeCell(final boolean pFolderSelectable)
      {
         super("click", "keydown");
         this.isFolderSelectable = pFolderSelectable;
      }

      @Override
      public boolean handlesSelection()
      {
         return true;
      }

      @Override
      public void onBrowserEvent(final Context context, final Element parent, final NodeDTO value,
                                 final NativeEvent event, final ValueUpdater<NodeDTO> valueUpdater)
      {
         // Check that the value is not null.
         if (value == null)
         {
            return;
         }
         super.onBrowserEvent(context, parent, value, event, valueUpdater);
         // On click, perform the same action that we perform on enter.
         if ("click".equals(event.getType()))
         {
            this.onEnterKeyDown(context, parent, value, event, valueUpdater);
         }
      }

      @Override
      public void render(final com.google.gwt.cell.client.Cell.Context pContext, final NodeDTO pValue,
                         final SafeHtmlBuilder pSb)
      {
         if (pValue != null)
         {
            if (!pValue.isExist())

            {
               pSb.appendHtmlConstant(this.getTableForIconAndName(AbstractImagePrototype.create(ressources.warning())
                                                                                        .getHTML(),
                                                                  "<font color='#FF0000'>" + pValue.getName()
                                                                      + "</font>"));
            }
            else if (pValue instanceof ArtefactNode)
            {
               pSb.appendHtmlConstant(this.getTableForIconAndName(MimeType.getMimeTypeIcon(pValue.getName()).getHTML(),
                                                                  pValue.getName()));
            }
            else if (pValue instanceof FolderNode)
            {
               if (!this.isFolderSelectable)
               {
                  pSb.appendHtmlConstant("<span title=\"" + messages.selectionUnable() + "\">");

               }
               pSb.appendHtmlConstant(this.getTableForIconAndName(AbstractImagePrototype.create(ressources.folder())
                                                                                        .getHTML(), pValue.getName()));
               if (!this.isFolderSelectable)
               {
                  pSb.appendHtmlConstant("</span>");
               }
            }
         }
      }

      @Override
      protected void onEnterKeyDown(final Context context, final Element parent, final NodeDTO value,
                                    final NativeEvent event, final ValueUpdater<NodeDTO> valueUpdater)
      {
         if (valueUpdater != null)
         {
            if (valueUpdater instanceof AbstractSelectValueUpdater)
            {
               ((AbstractSelectValueUpdater) valueUpdater).update(value, event.getCtrlKey());
            }
            else
            {
               valueUpdater.update(value);

            }
         }
      }

      private String getTableForIconAndName(final String pImage, final String pName)
      {
         return "<table cellspacing='0' cellpadding='0'><tr><td>" + pImage + "</td><td>" + pName + "</td></tr></table>";
      }
   }
}
