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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.web.bindery.event.shared.HandlerRegistration;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryManagement;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ArtefactNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.FolderNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.NodeDTO;
import org.novaforge.forge.ui.commons.client.loading.LoadingPanel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author caseryj
 */
public class NodesSelectionTrees extends Composite implements NodesSelection
{

  private static final String                 ACTION_BUTTON_SIZE         = "20px";

  private static NodesSelectionTreesResources ressources                 = GWT
                                                                             .create(NodesSelectionTreesResources.class);
  private static NodesSelectionMessage        messages                   = GWT
                                                                             .create(NodesSelectionMessage.class);
  private static FilesSelectionTreesUiBinder  uiBinder                   = GWT
                                                                             .create(FilesSelectionTreesUiBinder.class);
  private final boolean                       isFolderNodeSelectable;
  private final EditFolderDialogBox folderNamePopUp;
  CellTree.Resources cellTreeRessources = GWT.create(CellTree.BasicResources.class);
  @UiField
  Label                             availablesDocumentsLabel;
  @UiField
  Label                             selectedDocumentsLabel;
  @UiField
  Grid                              grid;
  @UiField(provided = true)
  LoadingPanel                      availablesLoadingPanel;
  @UiField(provided = true)
  LoadingPanel                      selectedLoadingPanel;
  @UiField
  ScrollPanel                       availablesScrollPanel;
  @UiField
  ScrollPanel                       selectedScrollPanel;
  @UiField(provided = true)
  CellTree                          availablesDocumentsTree;
  @UiField(provided = true)
  CellTree                          selectedDocumentsTree;
  @UiField(provided = true)
  Image                             addDocumentImage;
  @UiField(provided = true)
  Image                             buttonCollapseAvailableTree;
  @UiField(provided = true)
  Image                             buttonExpandAvailableTree;
  @UiField(provided = true)
  Image                             buttonCollapseSelectedTree;
  @UiField(provided = true)
  Image                             buttonExpandSelectedTree;
  @UiField(provided = true)
  Image                             buttonAddFolderSelected;
  @UiField(provided = true)
  Image                             buttonEditFolderSelected;
  @UiField(provided = true)
  Image                             buttonRemoveSelected;
  // Define Handler and register
  HandlerRegistration               addNodeHandlerRegistration;
  boolean                           addNodeHasCurrentlyHandler;
  HandlerRegistration               removeNodeHandlerRegistration;
  boolean                           removeNodeHasCurrentlyHandler;
  HandlerRegistration               editFolderHandlerRegistration;
  boolean                           editHasCurrentlyHandler;
  ClickHandler                      editFolderHandler   = new ClickHandler()
                                                        {
                                                          @Override
                                                          public void onClick(final ClickEvent event)
                                                          {
                                                            final NodeDTO folderToEdit = new ArrayList<NodeDTO>(
                                                                NodesSelectionTrees.this
                                                                    .getSelectedSelectionModel()
                                                                    .getSelectedSet()).get(0);
                                                            NodesSelectionTrees.this.initAndShowFolderPopUp(
                                                                false, folderToEdit);
                                                          }
                                                        };
  HandlerRegistration               addFolderHandlerRegistration;
  boolean                           addHasCurrentlyHandler;
  private boolean    createSelectedFolderEnable = true;
  ClickHandler removeNodeHandler = new ClickHandler()
  {
    @Override
    public void onClick(final ClickEvent event)
    {
      NodesSelectionTrees.this.removeSelectedFromSelectedDocuments();
    }
  };
  private FolderNode rootSelectedNode           = new FolderNode();
  ClickHandler                      createFolderHandler = new ClickHandler()
                                                        {
                                                          @Override
                                                          public void onClick(final ClickEvent event)
                                                          {
                                                            final List<NodeDTO> destinationNodes = new ArrayList<NodeDTO>(
                                                                NodesSelectionTrees.this
                                                                    .getSelectedSelectionModel()
                                                                    .getSelectedSet());
                                                            final NodeDTO newFolder = new FolderNode();
                                                            if (destinationNodes.isEmpty()
                                                                || (destinationNodes.size() > 1))
                                                            {
                                                              newFolder.setPath(rootSelectedNode
                                                                  .getChildPath());
                                                            }
                                                            else
                                                            {
                                                              final NodeDTO destinationNode = destinationNodes
                                                                  .get(0);
                                                              newFolder.setPath(NodesSelectionTrees.this
                                                                  .getChildPath(destinationNode));
                                                            }
                                                            NodesSelectionTrees.this.initAndShowFolderPopUp(
                                                                true, newFolder);
                                                          }
                                                        };
  ClickHandler addNodeHandler    = new ClickHandler()
  {
    @Override
    public void onClick(final ClickEvent event)
    {
      NodesSelectionTrees.this.addAvailableSelectedToSelectedDocuments();
      NodesSelectionTrees.this.getAvailableSelectionModel().clear();
    }
  };
  /**
   *
    */
  public NodesSelectionTrees()
  {
    this(true, false);
  }

  /**
   *
    */
  public NodesSelectionTrees(final boolean pCreateSelectedFolderEnable, final boolean pFolderNodeSelectable)
  {
    createSelectedFolderEnable = pCreateSelectedFolderEnable;
    isFolderNodeSelectable = pFolderNodeSelectable;

    ressources.css().ensureInjected();

    initAvailablesDocumentsTree();
    initSelectedDocumentsTree();
    availablesLoadingPanel = new LoadingPanel();
    selectedLoadingPanel = new LoadingPanel();

    // Init buttons
    // // Add Remove Nodes
    addDocumentImage = new Image(ressources.docAdd());
    // // Collapse Expand buttons
    buttonCollapseAvailableTree = new Image(ressources.collapse());
    buttonCollapseAvailableTree.setTitle(messages.buttonCollapseAllNode());
    buttonCollapseSelectedTree = new Image(ressources.collapse());
    buttonCollapseSelectedTree.setTitle(messages.buttonCollapseAllNode());
    buttonExpandAvailableTree = new Image(ressources.expand());
    buttonExpandAvailableTree.setTitle(messages.buttonExpandAllNode());
    buttonExpandSelectedTree = new Image(ressources.expand());
    buttonExpandSelectedTree.setTitle(messages.buttonExpandAllNode());
    // // Create and Edit tree folder
    buttonAddFolderSelected = new Image(ressources.foldernew());
    buttonAddFolderSelected.setWidth(ACTION_BUTTON_SIZE);
    buttonAddFolderSelected.setHeight(ACTION_BUTTON_SIZE);
    buttonEditFolderSelected = new Image(ressources.foldereditdisable());
    buttonEditFolderSelected.setWidth(ACTION_BUTTON_SIZE);
    buttonEditFolderSelected.setHeight(ACTION_BUTTON_SIZE);
    // // Remove Selected Node
    buttonRemoveSelected = new Image(ressources.removeNodeDisable());

    initWidget(uiBinder.createAndBindUi(this));

    availablesDocumentsLabel.setText(messages.labelAvailablesDocuments());
    selectedDocumentsLabel.setText(messages.labelSelectedDocuments(""));
    enableAddNodeButton(false);
    clearCurrentSelection();

    folderNamePopUp = new EditFolderDialogBox(messages.buttonAddFolderSelectedTree());

    // Add style to second cell for each row
    grid.getCellFormatter().setWidth(0, 0, "40%");
    grid.getCellFormatter().setWidth(1, 0, "40%");
    grid.getCellFormatter().addStyleName(0, 1, ressources.css().buttonAddRemoveTable());
    grid.getCellFormatter().addStyleName(1, 1, ressources.css().buttonAddRemoveTable());
    grid.getCellFormatter().setWidth(0, 2, "40%");
    grid.getCellFormatter().setWidth(1, 2, "40%");

    bind();
  }

  private void bind()
  {
    if (createSelectedFolderEnable)
    {
      buttonAddFolderSelected.addClickHandler(new ClickHandler()
      {
        @Override
        public void onClick(final ClickEvent event)
        {
          final List<NodeDTO> destinationNodes = new ArrayList<NodeDTO>(NodesSelectionTrees.this
              .getSelectedSelectionModel().getSelectedSet());
          final NodeDTO newFolder = new FolderNode();
          if (destinationNodes.isEmpty() || (destinationNodes.size() > 1))
          {
            newFolder.setPath("/");
          }
          else
          {
            final NodeDTO destinationNode = destinationNodes.get(0);
            newFolder.setPath(NodesSelectionTrees.this.getChildPath(destinationNode));
          }
          NodesSelectionTrees.this.initAndShowFolderPopUp(true, newFolder);
        }
      });
    }
    folderNamePopUp.getValidate().addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        folderNamePopUp.getDialogPanel().hide();
        final String value = folderNamePopUp.getTextBox().getValue();
        folderNamePopUp.getTextBox().setText(null);
        if (folderNamePopUp.isCreateMode())
        {
          NodesSelectionTrees.this.createNewFolderOnDocumentsSelectedTree(value);
        }
        else
        {
          NodesSelectionTrees.this.renameFolder(value);
        }
        NodesSelectionTrees.this.clearCurrentSelection();
      }
    });
    buttonEditFolderSelected.addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        final NodeDTO folderToEdit = new ArrayList<NodeDTO>(NodesSelectionTrees.this
            .getSelectedSelectionModel().getSelectedSet()).get(0);
        NodesSelectionTrees.this.initAndShowFolderPopUp(false, folderToEdit);
      }
    });

    buttonCollapseAvailableTree.addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        NodesSelectionTrees.this.collapseAllAvailableNodes();
      }
    });

    buttonCollapseSelectedTree.addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        NodesSelectionTrees.this.collapseAllSelectedNodes();
      }
    });

    buttonExpandAvailableTree.addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        NodesSelectionTrees.this.expandAllAvailableNodes();
      }
    });

    buttonExpandSelectedTree.addClickHandler(new ClickHandler()
    {
      @Override
      public void onClick(final ClickEvent event)
      {
        NodesSelectionTrees.this.expandAllSelectedNodes();
      }
    });

    getAvailableSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
    {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event)
      {
        if (!isFolderNodeSelectable)
        {
          unselectFolderNode(getAvailableSelectionModel());
        }
        checkIfAddNodeIsPossible();
      }
    });

    getSelectedSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
    {
      @Override
      public void onSelectionChange(final SelectionChangeEvent event)
      {
        final List<NodeDTO> destinationNodes = new ArrayList<NodeDTO>(NodesSelectionTrees.this
            .getSelectedSelectionModel().getSelectedSet());
        if (!destinationNodes.isEmpty())
        {
          NodesSelectionTrees.this.enableRemoveNodeButton(true);
          if (destinationNodes.size() == 1)
          {
            NodesSelectionTrees.this.enableAddFolderButton(destinationNodes.get(0) instanceof FolderNode);
            NodesSelectionTrees.this.enableEditFolderButton(destinationNodes.get(0) instanceof FolderNode);
          }
          else
          {
            NodesSelectionTrees.this.enableAddFolderButton(false);
            NodesSelectionTrees.this.enableEditFolderButton(false);
          }
        }
        else
        {
          NodesSelectionTrees.this.clearCurrentSelection();
        }
        checkIfAddNodeIsPossible();
      }
    });
  }

  private void unselectFolderNode(final MultiSelectionModel<NodeDTO> pDocuments)
  {
    for (final NodeDTO selected : pDocuments.getSelectedSet())
    {
      if (selected instanceof FolderNode)
      {
        pDocuments.setSelected(selected, false);
      }
    }
  }

  private void checkIfAddNodeIsPossible()
  {
    enableAddNodeButton(((!getAvailableSelectionModel().getSelectedSet().isEmpty()) && ((getSelectedSelectionModel()
        .getSelectedSet().isEmpty()) || ((getSelectedSelectionModel().getSelectedSet().size() == 1) && (getSelectedSelectionModel()
        .getSelectedSet().iterator().next() instanceof FolderNode)))));
  }

  private void initAvailablesDocumentsTree()
  {
    availablesDocumentsTree = new CellTree(new NodeTreeViewModel(isFolderNodeSelectable), null,
        cellTreeRessources);
    availablesDocumentsTree.setAnimationEnabled(true);

  }

  private void initSelectedDocumentsTree()
  {
    selectedDocumentsTree = new CellTree(new NodeTreeViewModel(), null, cellTreeRessources);
    selectedDocumentsTree.setAnimationEnabled(true);
  }

  private void createNewFolderOnDocumentsSelectedTree(final String pName)
  {
    final NodeDTO newFolder = new FolderNode();
    newFolder.setName(pName);
    final List<NodeDTO> destinationNodes = new ArrayList<NodeDTO>(getSelectedSelectionModel()
        .getSelectedSet());
    if (destinationNodes.isEmpty())
    {
      newFolder.setPath(rootSelectedNode.getChildPath());
      getSelectedNodes().add(newFolder);
    }
    else
    {
      final FolderNode destinationNode = (FolderNode) destinationNodes.get(0);
      newFolder.setPath(getChildPath(destinationNode));
      destinationNode.getChildren().add(newFolder);
      refreshSelectedNodeView(destinationNode, true);
    }
  }

  private void enableEditFolderButton(final boolean pIsEnable)
  {
    if (createSelectedFolderEnable)
    {
      if (pIsEnable)
      {
        editFolderHandlerRegistration = NodesSelectionTrees.this.buttonEditFolderSelected
            .addClickHandler(editFolderHandler);
        NodesSelectionTrees.this.buttonEditFolderSelected.setResource(ressources.folderedit());
        NodesSelectionTrees.this.buttonEditFolderSelected.setStyleName(ressources.css().buttonMouseLike());
        NodesSelectionTrees.this.buttonEditFolderSelected.setTitle(messages.buttonEditFolderSelected());
      }
      else
      {
        if ((editFolderHandlerRegistration != null) && editHasCurrentlyHandler)
        {
          editFolderHandlerRegistration.removeHandler();
        }
        NodesSelectionTrees.this.buttonEditFolderSelected.setResource(ressources.foldereditdisable());
        NodesSelectionTrees.this.buttonEditFolderSelected.removeStyleName(ressources.css().buttonMouseLike());
        NodesSelectionTrees.this.buttonEditFolderSelected
            .setTitle(messages.buttonEditFolderSelectedDisable());

      }
      NodesSelectionTrees.this.editHasCurrentlyHandler = pIsEnable;
    }
  }

  private void enableAddFolderButton(final boolean pIsEnable)
  {
    if (createSelectedFolderEnable)
    {
      if (pIsEnable)
      {
        addFolderHandlerRegistration = NodesSelectionTrees.this.buttonAddFolderSelected
            .addClickHandler(createFolderHandler);
        NodesSelectionTrees.this.buttonAddFolderSelected.setResource(ressources.foldernew());
        NodesSelectionTrees.this.buttonAddFolderSelected.setStyleName(ressources.css().buttonMouseLike());
        NodesSelectionTrees.this.buttonAddFolderSelected.setTitle(messages.buttonAddFolderSelectedTree());
      }
      else
      {
        if ((addFolderHandlerRegistration != null) && addHasCurrentlyHandler)
        {
          addFolderHandlerRegistration.removeHandler();
        }
        NodesSelectionTrees.this.buttonAddFolderSelected.setResource(ressources.foldernewdisable());
        NodesSelectionTrees.this.buttonAddFolderSelected.removeStyleName(ressources.css().buttonMouseLike());
        NodesSelectionTrees.this.buttonAddFolderSelected.setTitle(messages
            .buttonAddFolderSelectedTreeDisable());

      }
      NodesSelectionTrees.this.addHasCurrentlyHandler = pIsEnable;
    }
  }

  private void enableRemoveNodeButton(final boolean pIsEnable)
  {
    if (pIsEnable)
    {
      removeNodeHandlerRegistration = NodesSelectionTrees.this.buttonRemoveSelected
          .addClickHandler(removeNodeHandler);
      NodesSelectionTrees.this.buttonRemoveSelected.setResource(ressources.removeNode());
      NodesSelectionTrees.this.buttonRemoveSelected.setStyleName(ressources.css().buttonMouseLike());
      NodesSelectionTrees.this.buttonRemoveSelected.setTitle(messages.buttonRemoveNode());
    }
    else
    {
      if ((removeNodeHandlerRegistration != null) && removeNodeHasCurrentlyHandler)
      {
        removeNodeHandlerRegistration.removeHandler();
      }
      NodesSelectionTrees.this.buttonRemoveSelected.setResource(ressources.removeNodeDisable());
      NodesSelectionTrees.this.buttonRemoveSelected.removeStyleName(ressources.css().buttonMouseLike());
      NodesSelectionTrees.this.buttonRemoveSelected.setTitle(messages.buttonRemoveNodeDisable());

    }
    NodesSelectionTrees.this.removeNodeHasCurrentlyHandler = pIsEnable;
  }

  private void enableAddNodeButton(final boolean pIsEnable)
  {
    if (pIsEnable)
    {
      addNodeHandlerRegistration = NodesSelectionTrees.this.addDocumentImage.addClickHandler(addNodeHandler);
      NodesSelectionTrees.this.addDocumentImage.setResource(ressources.docAdd());
      NodesSelectionTrees.this.addDocumentImage.setStyleName(ressources.css().buttonMouseLike());
      NodesSelectionTrees.this.addDocumentImage.setTitle(messages.buttonAddNode());
    }
    else
    {
      if ((addNodeHandlerRegistration != null) && addHasCurrentlyHandler)
      {
        addNodeHandlerRegistration.removeHandler();
      }
      NodesSelectionTrees.this.addDocumentImage.setResource(ressources.docAddDisabled());
      NodesSelectionTrees.this.addDocumentImage.removeStyleName(ressources.css().buttonMouseLike());
      NodesSelectionTrees.this.addDocumentImage.setTitle(messages.buttonAddNodeDisabled());

    }
    NodesSelectionTrees.this.addHasCurrentlyHandler = pIsEnable;
  }

  private void renameFolder(final String pName)
  {
    final NodeDTO folderTorename = new ArrayList<NodeDTO>(getSelectedSelectionModel().getSelectedSet())
        .get(0);
    folderTorename.setName(pName);
    updateChlidrenPath(folderTorename);
    refreshSelectedNodeView(folderTorename, false);
  }

  private void updateChlidrenPath(final NodeDTO pNode)
  {
    if (pNode instanceof FolderNode)
    {
      for (final NodeDTO child : ((FolderNode) pNode).getChildren())
      {
        String nodePathWithoutDoubleSlash = "";
        if (pNode.getPath().endsWith("/"))
        {
          nodePathWithoutDoubleSlash = pNode.getPath();
        }
        else
        {
          nodePathWithoutDoubleSlash = pNode.getPath() + "/";
        }
        child.setPath(nodePathWithoutDoubleSlash + pNode.getName());
        if (child instanceof FolderNode)
        {
          updateChlidrenPath(child);
        }
      }
    }
  }

  private String getChildPath(final NodeDTO pNode)
  {
    String childPath = "";
    if (pNode.getPath().endsWith("/") || (pNode.getPath() == null))
    {
      childPath = pNode.getPath() + pNode.getName() + "/";
    }
    else
    {
      childPath = pNode.getPath() + "/" + pNode.getName() + "/";
    }
    return childPath;
  }

  private void removeSelectedFromSelectedDocuments()
  {
    final List<NodeDTO> nodesToDelete = new ArrayList<NodeDTO>(getSelectedSelectionModel().getSelectedSet());
    for (final NodeDTO node : nodesToDelete)
    {
      deleteSelectedNode(getSelectedNodes(), node);
    }
    clearCurrentSelection();
  }

  private boolean deleteSelectedNode(final List<NodeDTO> nodesList, final NodeDTO nodeToDelete)
  {
    boolean isDeleted = false;
    if (nodesList != null)
    {
      if (nodesList.remove(nodeToDelete))
      {
        isDeleted = true;
      }
      else
      {
        final Iterator<NodeDTO> it = nodesList.iterator();
        while (it.hasNext() && (!isDeleted))
        {
          final NodeDTO node = it.next();
          if (node instanceof FolderNode)
          {
            isDeleted = deleteSelectedNode(((FolderNode) node).getChildren(), nodeToDelete);
            if (isDeleted)
            {
              refreshSelectedNodeView(node, true);
            }
          }
        }
      }
    }
    return isDeleted;
  }

  private void addAvailableSelectedToSelectedDocuments()
  {
    final List<NodeDTO> destinationNodes = new ArrayList<NodeDTO>(getSelectedSelectionModel()
        .getSelectedSet());
    final List<NodeDTO> currentNodesToMove = new ArrayList<NodeDTO>(getAvailableSelectionModel()
        .getSelectedSet());
    String destinationPath = null;
    List<NodeDTO> destinationList = null;
    FolderNode destinationFolder = null;
    if (destinationNodes.isEmpty() || (destinationNodes.size() > 1))
    {
      destinationPath = rootSelectedNode.getChildPath();
      destinationList = getSelectedNodes();
    }
    else
    {
      if (destinationNodes.get(0) instanceof FolderNode)
      {
        destinationFolder = (FolderNode) destinationNodes.get(0);
        destinationPath = destinationFolder.getChildPath();
        destinationList = destinationFolder.getChildren();
      }
      else
      {
        destinationPath = rootSelectedNode.getChildPath();
        destinationList = getSelectedNodes();
      }
    }
    final List<NodeDTO> newCurrentNodesToMode = duplicateNodeTree(currentNodesToMove);
    addNodesToList(destinationList, newCurrentNodesToMode, destinationPath);

    clearCurrentSelection();
    if (destinationFolder != null)
    {
      refreshSelectedNodeView(destinationFolder, true);
    }
  }

  private boolean addNodesToList(final List<NodeDTO> pList, final List<NodeDTO> pNodesToAdd,
      final String pCurrentPath)
  {
    boolean result = true;
    for (final NodeDTO node : pNodesToAdd)
    {
      node.setPath(pCurrentPath);
      updateChlidrenPath(node);
      final boolean currentResult = addNodeToList(pList, node);
      if (!currentResult)
      {
        result = currentResult;
      }
    }
    return result;
  }

  private boolean addNodeToList(final List<NodeDTO> pList, final NodeDTO pNode)
  {
    if (pList.contains(pNode))
    {
      return false;
    }
    else
    {
      return pList.add(pNode);
    }
  }

  private List<NodeDTO> duplicateNodeTree(final List<NodeDTO> pNodeTree)
  {
    final List<NodeDTO> treeResult = new ArrayList<NodeDTO>();
    NodeDTO newNode = null;
    for (final NodeDTO currentNode : pNodeTree)
    {

      if (currentNode instanceof FolderNode)
      {
        newNode = new FolderNode(currentNode.getName(), currentNode.getPath(),
            duplicateNodeTree(((FolderNode) currentNode).getChildren()));
      }
      else if (currentNode instanceof ArtefactNode)
      {
        final ArtefactNode currentArtefact = (ArtefactNode) currentNode;
        newNode = new ArtefactNode(currentArtefact.getName(), currentArtefact.getPath(),
            currentArtefact.getID(), currentArtefact.getFields());
      }
      treeResult.add(newNode);
    }
    return treeResult;
  }

  private void initAndShowFolderPopUp(final boolean pIsCreateMode, final NodeDTO pFolder)
  {
    if (pIsCreateMode)
    {
      folderNamePopUp.getDialogBox().setText(messages.buttonAddFolderSelectedTree());
      folderNamePopUp.getValidateButton().setText(messages.buttonAddFolderValidateLabel());
      folderNamePopUp.getFolderPath().setText(pFolder.getPath());
      folderNamePopUp.setCreateMode(pIsCreateMode);
      folderNamePopUp.getTextBox().setCursorPos(0);
    }
    else
    {
      folderNamePopUp.getDialogBox().setText(messages.buttonEditFolderSelected());
      folderNamePopUp.getValidateButton().setText(messages.buttonEditFolderValidateLabel());
      folderNamePopUp.getTextBox().setText(pFolder.getName());
      folderNamePopUp.getFolderPath().setText(pFolder.getPath());
      folderNamePopUp.setCreateMode(pIsCreateMode);
      folderNamePopUp.getTextBox().setSelectionRange(0, folderNamePopUp.getTextBox().getValue().length());
    }
    folderNamePopUp.getDialogPanel().center();
    folderNamePopUp.getDialogPanel().show();
  }

  private void collapseOrExpand(final TreeNode pRootNode, final boolean pIsExpand,
      final boolean pDoItForAllChildren)
  {
    if (pRootNode != null)
    {
      for (int i = 0; i < pRootNode.getChildCount(); i++)
      {
        final TreeNode child = pRootNode.setChildOpen(i, pIsExpand, true);
        if (pIsExpand && pDoItForAllChildren && (child != null))
        {
          collapseOrExpand(child, pIsExpand, pDoItForAllChildren);
        }
      }
    }
  }

  private void refreshAvailableNodeView(final NodeDTO pNode, final boolean pIsThisNodeToRefresh)
  {

    TreeNode toRefresh = getAvailableTreeNodeToRefreshForNode(pNode);
    if (toRefresh != null)
    {
      if (pIsThisNodeToRefresh)
      {
        toRefresh = toRefresh.getParent();
      }
      refreshTreeChildView(toRefresh, pNode);
    }
  }

  private void refreshSelectedNodeView(final NodeDTO pNode, final boolean pIsThisNodeToRefresh)
  {
    TreeNode toRefresh;
    if (pNode == null)
    {
      toRefresh = selectedDocumentsTree.getRootTreeNode();
    }
    else
    {
      toRefresh = getSelectedTreeNodeToRefreshForNode(pNode);
    }
    if (toRefresh != null)
    {
      if (pIsThisNodeToRefresh)
      {
        toRefresh = toRefresh.getParent();
      }
      refreshTreeChildView(toRefresh, pNode);
    }
  }

  private void refreshTreeChildView(final TreeNode pParent, final NodeDTO pChild)
  {
    for (int i = 0; i < pParent.getChildCount(); i++)
    {
      final TreeNode child = pParent.setChildOpen(i, pParent.isChildOpen(i), false);

      if ((child != null) && (child.getValue() instanceof NodeDTO) && (child.getValue().equals(pChild)))
      {
        pParent.setChildOpen(i, false, false);
        pParent.setChildOpen(i, true, false);
        break;
      }
    }
  }

  private TreeNode getAvailableTreeNodeToRefreshForNode(final NodeDTO pNode)
  {
    return findTreeNodeForNode(availablesDocumentsTree.getRootTreeNode(), pNode);
  }

  private TreeNode getSelectedTreeNodeToRefreshForNode(final NodeDTO pNode)
  {
    return findTreeNodeForNode(selectedDocumentsTree.getRootTreeNode(), pNode);
  }

  private TreeNode findTreeNodeForNode(final TreeNode pRootNode, final NodeDTO pNode)
  {
    if (pRootNode != null)
    {
      if ((pRootNode.getValue() instanceof NodeDTO) && (pRootNode.getValue().equals(pNode)))
      {
        return pRootNode;
      }
      else
      {
        TreeNode result = null;
        int i = 0;
        while ((i < pRootNode.getChildCount()) && (result == null))
        {
          if (pRootNode.isChildOpen(i))
          {
            final TreeNode child = pRootNode.setChildOpen(i, pRootNode.isChildOpen(i), false);
            if (child != null)
            {
              result = findTreeNodeForNode(child, pNode);
            }
          }
          i++;

        }
        return result;
      }
    }
    else
    {
      return null;
    }
  }

  private MultiSelectionModel<NodeDTO> getAvailableSelectionModel()
  {
    return ((NodeTreeViewModel) availablesDocumentsTree.getTreeViewModel()).getSelectionModel();
  }

  private MultiSelectionModel<NodeDTO> getSelectedSelectionModel()
  {
    return ((NodeTreeViewModel) selectedDocumentsTree.getTreeViewModel()).getSelectionModel();
  }

  private ListDataProvider<NodeDTO> getSelectedDocumentsDataProvider()
  {
    return ((NodeTreeViewModel) selectedDocumentsTree.getTreeViewModel()).getDataProvider();
  }

  private ListDataProvider<NodeDTO> getAvailablesDocumentsDataProvider()
  {
    return ((NodeTreeViewModel) availablesDocumentsTree.getTreeViewModel()).getDataProvider();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAvailableNodes(final List<NodeDTO> pNodes)
  {
    if (DeliveryManagement.isDebugMode() && ((pNodes == null) || (pNodes.isEmpty())))
    {
      final List<NodeDTO> newSpace = new ArrayList<NodeDTO>();
      newSpace.add(new FolderNode("debugging space", "/", null));
      getAvailablesDocumentsDataProvider().setList(newSpace);
    }
    else
    {
      getAvailablesDocumentsDataProvider().getList().clear();
      getAvailablesDocumentsDataProvider().setList(pNodes);
      for (final NodeDTO nodeDTO : getAvailablesDocumentsDataProvider().getList())
      {
        if (nodeDTO instanceof FolderNode)
        {
          refreshAvailableNodeView(nodeDTO, true);
        }
      }
    }
  }

  /**
   * Get the current selected nodes
   *
   * @return The list of selected nodes
   */
  private List<NodeDTO> getSelectedNodes()
  {
    return getSelectedDocumentsDataProvider().getList();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedNodes(final FolderNode pRootFolder)
  {
    rootSelectedNode = pRootFolder;
    setSelectedNodesTitle(rootSelectedNode.getChildPath());
    final List<NodeDTO> children = pRootFolder.getChildren();
    if (DeliveryManagement.isDebugMode() && ((pRootFolder == null) || (children == null) || (children.isEmpty())))
    {
      final NodeDTO emptyFolder = new FolderNode("debugging space", "/", null);
      children.add(emptyFolder);
    }
    getSelectedDocumentsDataProvider().getList().clear();
    getSelectedDocumentsDataProvider().setList(children);

    for (final NodeDTO nodeDTO : children)
    {
      if (nodeDTO instanceof FolderNode)
      {
        refreshSelectedNodeView(nodeDTO, true);
      }
    }

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAvailableNodesTitle(final String pTitle)
  {
    availablesDocumentsLabel.setText(pTitle);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedNodesTitle(final String pTitle)
  {
    selectedDocumentsLabel.setText(messages.labelSelectedDocuments(pTitle));
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void collapseAllAvailableNodes()
  {
    collapseOrExpand(availablesDocumentsTree.getRootTreeNode(), false, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void expandAllAvailableNodes()
  {
    collapseOrExpand(availablesDocumentsTree.getRootTreeNode(), true, true);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void collapseAllSelectedNodes()
  {
    collapseOrExpand(selectedDocumentsTree.getRootTreeNode(), false, true);
    clearCurrentSelection();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void expandAllSelectedNodes()
  {
    collapseOrExpand(selectedDocumentsTree.getRootTreeNode(), true, true);
    clearCurrentSelection();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clearCurrentSelection()
  {
    getSelectedSelectionModel().clear();
    enableAddFolderButton(createSelectedFolderEnable);
    enableEditFolderButton(false);
    enableRemoveNodeButton(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public FolderNode getRootSelectedNodes()
  {
    return rootSelectedNode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAvailableTreeLoading(final boolean pValue)
  {
    getAvailablesScrollPanel().setVisible(!pValue);
    getAvailablesLoadingPanel().setErrorStatus(!pValue);
    getAvailablesLoadingPanel().setVisible(pValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAvailableErrorLoading(final boolean pValue)
  {
    getAvailablesLoadingPanel().setErrorStatus(pValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setAvailableErrorMessageLoading(final String pValue)
  {
    getAvailablesLoadingPanel().setErrorText(pValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setSelectedTreeLoading(final boolean pValue)
  {
    getSelectedScrollPanel().setVisible(!pValue);
    getSelectedLoadingPanel().setVisible(pValue);
  }

  /**
   * @return the selectedDocumentsTree
   */
  private ScrollPanel getSelectedScrollPanel()
  {
    return selectedScrollPanel;
  }

  /**
   * @return the selectedLoadingPanel
   */
  private LoadingPanel getSelectedLoadingPanel()
  {
    return selectedLoadingPanel;
  }

  /**
   * @return the availablesDocumentsTree
   */
  private ScrollPanel getAvailablesScrollPanel()
  {
    return availablesScrollPanel;
  }

  /**
   * @return the availablesLoadingPanel
   */
  private LoadingPanel getAvailablesLoadingPanel()
  {
    return availablesLoadingPanel;
  }

  public boolean checkIfSelectedNodeStillExist()
  {
    return isNodesExistInAvailableNodes(getSelectedNodes());
  }

  private boolean isNodesExistInAvailableNodes(final List<NodeDTO> pNodes)
  {
    boolean isExist = true;
    for (final NodeDTO currentNode : pNodes)
    {
      if (currentNode instanceof FolderNode)
      {

        isExist = isNodesExistInAvailableNodes(((FolderNode) currentNode).getChildren());
      }
      else if (currentNode instanceof ArtefactNode)
      {
        if (!isTreeContainNode(getAvailablesDocumentsDataProvider().getList(), currentNode))
        {
          isExist = false;
          currentNode.setExist(isExist);
        }
      }
    }
    return isExist;
  }

  private boolean isTreeContainNode(final List<NodeDTO> pTree, final NodeDTO pNode)
  {
    boolean result = false;
    if (pTree != null)
    {
      final Iterator<NodeDTO> it = pTree.iterator();
      while (it.hasNext() && !result)
      {
        final NodeDTO treeNode = it.next();
        if (treeNode instanceof ArtefactNode)
        {
          result = pNode.equalsWithoutPath(treeNode);
        }
        else if (treeNode instanceof FolderNode)
        {
          result = isTreeContainNode(((FolderNode) treeNode).getChildren(), pNode);
        }
        else
        {
          result = false;
        }
      }
    }
    return result;
  }

  interface FilesSelectionTreesUiBinder extends UiBinder<Widget, NodesSelectionTrees>
  {
  }

}
