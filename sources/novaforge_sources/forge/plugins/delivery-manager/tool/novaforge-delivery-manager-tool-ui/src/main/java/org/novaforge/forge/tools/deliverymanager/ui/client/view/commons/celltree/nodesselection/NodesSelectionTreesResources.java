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

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;

/**
 * @author caseryj
 */
public interface NodesSelectionTreesResources extends ClientBundle
{

   /**
    * The styles used in this widget.
    */
   @NotStrict
   @Source("NodesSelectionTreesResources.css")
   Style css();

   @Source("img/folder-edit.png")
   ImageResource folderedit();

   @Source("img/folder-edit-disable.png")
   ImageResource foldereditdisable();

   @Source("img/folder-new.png")
   ImageResource foldernew();

   @Source("img/folder-new-disable.png")
   ImageResource foldernewdisable();

   @ImageOptions(height = 20, width = 20)
   @Source("img/removenode.png")
   ImageResource removeNode();

   @ImageOptions(height = 20, width = 20)
   @Source("img/removenode-disable.png")
   ImageResource removeNodeDisable();

   @Source("img/docAdd.png")
   ImageResource docAdd();

   @Source("img/docAdd-disabled.png")
   ImageResource docAddDisabled();

   @Source("img/collapse.png")
   ImageResource collapse();

   @Source("img/expand.png")
   ImageResource expand();

   @Source("img/warning.png")
   ImageResource warning();

   @ImageOptions(height = 24, width = 24)
   @Source("img/folder.png")
   ImageResource folder();

   interface Style extends CssResource
   {
      String zoneTitle();

      String actionPanel();

      String zonePanel();

      String cellTable();

      String zoneTable();

      String cellPanel();

      String bottomButonPanel();

      String important();

      String emptyLabel();

      String gridLabel();

      String gridTB();

      String infoGrid();

      String subZoneTitle();

      String borderCell();

      String buttonMouseLike();

      String buttonAddRemoveTable();

      String buttonActionPanelTree();

      String separatorPanelTree();

      String treeContentPanelTree();

      String documentTree();

   }

}
