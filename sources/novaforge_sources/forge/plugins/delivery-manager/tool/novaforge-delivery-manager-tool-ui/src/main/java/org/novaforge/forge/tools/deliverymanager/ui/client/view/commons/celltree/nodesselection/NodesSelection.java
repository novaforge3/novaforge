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

import org.novaforge.forge.tools.deliverymanager.ui.shared.FolderNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.NodeDTO;

import java.util.List;

/**
 * @author caseryj
 */
public interface NodesSelection
{

   /**
    * Set available Nodes for selection
    * 
    * @param pNodes
    *           List of nodes
    */
   void setAvailableNodes(List<NodeDTO> pNodes);

   /**
    * Set selected Root folder node
    * 
    * @param pRootFolder
    *           the Folder root node
    */
   void setSelectedNodes(FolderNode pRootFolder);

   /**
    * Define title for available nodes' tree
    * 
    * @param pTitle
    *           The title to set
    */
   void setAvailableNodesTitle(String pTitle);

   /**
    * Define title for selected nodes' tree
    * 
    * @param pTitle
    *           The title to set
    */
   void setSelectedNodesTitle(String pTitle);

   /**
    * Collapse all available nodes
    */
   void collapseAllAvailableNodes();

   /**
    * Expand all available nodes
    */
   void expandAllAvailableNodes();

   /**
    * Collapse all selected nodes
    */
   void collapseAllSelectedNodes();

   /**
    * Expand all selected nodes
    */
   void expandAllSelectedNodes();

   /**
    * Clear selection on Selected Tree
    */
   void clearCurrentSelection();

   /**
    * Get the root Folder node of Selected Tree
    * 
    * @return the root Folder Node
    */
   FolderNode getRootSelectedNodes();

   /**
    * Show the tree or a loading panel
    * 
    * @param pValue
    *           false to enable and show tree, true to show a loading panel
    */
   void setAvailableTreeLoading(boolean pValue);

   /**
    * Show the error message in loading panel
    * 
    * @param pValue
    */
   void setAvailableErrorLoading(boolean pValue);

   /**
    * Set the error message in loading panel
    * 
    * @param pValue
    */
   void setAvailableErrorMessageLoading(String pValue);

   /**
    * Show the tree or a loading panel
    * 
    * @param false to enable and show tree, true to show a loading panel
    */
   void setSelectedTreeLoading(boolean pValue);

}
