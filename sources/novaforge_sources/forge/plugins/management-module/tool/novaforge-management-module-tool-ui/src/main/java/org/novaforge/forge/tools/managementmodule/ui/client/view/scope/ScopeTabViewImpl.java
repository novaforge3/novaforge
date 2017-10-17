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
package org.novaforge.forge.tools.managementmodule.ui.client.view.scope;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltree.TreeResources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;

/**
 * @author vvigo
 */
public class ScopeTabViewImpl extends Composite implements ScopeTabView
{
   private static ScopeTabViewImplUiBinder   uiBinder      = GWT.create(ScopeTabViewImplUiBinder.class);
   private static TreeResources              treeResources = GWT.create(TreeResources.class);
   private final ValidateDialogBox  validateDeleteDialogBox;
   private final ValidateDialogBox  validateUnlinkDialogBox;
   private final InfoDialogBox      infoDialogBoxSelectScopeUnit;
   private final InfoDialogBox      infoDialogBoxSelectRefScopeUnit;
   private final InfoDialogBox      infoDialogBoxParentRefScopeUnit;
   private final DocumentsTreeModel scopeUnitModel;
   @UiField
   Label                            scopeSearchTitle;
   @UiField
   Label                            scopeNameSearchLabel;
   @UiField
   TextBox                          scopeNameSearchTB;
   @UiField
   Label                            scopeTypeSearchLabel;
   @UiField
   ListBox                          scopeTypeSearchTB;
   @UiField
   Label                            scopeVersionSearchLabel;
   @UiField
   TextBox                          scopeVersionSearchTB;
   @UiField
   Label                            scopeUnitListTitle;
   @UiField
   VerticalPanel verticalPanelTree;
   @UiField
   SimplePanel                      rightPanel;
   @UiField
   Button                           buttonAddScopeUnit;
   @UiField
   Button                           buttonDeleteScopeUnit;
   @UiField
   Button                           buttonCreateScopeUnit;
   @UiField
   Button                           buttonReplaceScopeUnit;
   @UiField
   Button                           buttonEditScopeUnit;
   @UiField
   Button                           buttonUnlinkScopeUnit;
   CellTree                         scopeUnitTree;

   public ScopeTabViewImpl()
   {
      Common.getResource().css().ensureInjected();

      // Init tree cell
      scopeUnitModel = new DocumentsTreeModel();
      scopeUnitTree = new CellTree(scopeUnitModel, null, treeResources);
      scopeUnitTree.setAnimationEnabled(true);
      initWidget(uiBinder.createAndBindUi(this));

      verticalPanelTree.add(scopeUnitTree);

      scopeSearchTitle.setText(Common.MESSAGES_SCOPE.scopeSearchTitle());
      scopeNameSearchLabel.setText(Common.MESSAGES_SCOPE.scopeNameSearchLabel());
      scopeTypeSearchLabel.setText(Common.MESSAGES_SCOPE.scopeTypeSearchLabel());
      scopeVersionSearchLabel.setText(Common.MESSAGES_SCOPE.scopeVersionSearchLabel());
      scopeUnitListTitle.setText(Common.MESSAGES_SCOPE.scopeUnitListTitle());
      buttonAddScopeUnit.setText(Common.MESSAGES_SCOPE.buttonAddScopeUnit());

      buttonDeleteScopeUnit.setText(Common.MESSAGES_SCOPE.buttonDeleteScopeUnit());
      buttonCreateScopeUnit.setText(Common.MESSAGES_SCOPE.buttonCreateScopeUnit());
      buttonReplaceScopeUnit.setText(Common.MESSAGES_SCOPE.buttonReplaceScopeUnit());
      buttonEditScopeUnit.setText(Common.MESSAGES_SCOPE.buttonEditScopeUnit());
      buttonUnlinkScopeUnit.setText(Common.MESSAGES_SCOPE.buttonUnlinkScopeUnit());


      // Initialization of validation popup
      validateDeleteDialogBox = new ValidateDialogBox(Common.MESSAGES_SCOPE.deleteManualScoped());
      validateUnlinkDialogBox = new ValidateDialogBox(Common.MESSAGES_SCOPE.validateUnlinkDialogBox());

      infoDialogBoxSelectScopeUnit = new InfoDialogBox(Common.MESSAGES_SCOPE.selectScopeUnit(),
            InfoTypeEnum.WARNING);

      infoDialogBoxSelectRefScopeUnit = new InfoDialogBox(Common.MESSAGES_SCOPE.selectRefScopeUnit(),
            InfoTypeEnum.WARNING);

      infoDialogBoxParentRefScopeUnit = new InfoDialogBox(Common.MESSAGES_SCOPE.parentRefScopeUnit(),
            InfoTypeEnum.WARNING);

      buttonAddScopeUnit.setEnabled(true);
      buttonCreateScopeUnit.setEnabled(true);
      buttonReplaceScopeUnit.setEnabled(false);
      buttonUnlinkScopeUnit.setEnabled(false);
      buttonEditScopeUnit.setEnabled(false);
      buttonDeleteScopeUnit.setEnabled(false);
   }

   @Override
   public SimplePanel getContentPanel()
   {
      return rightPanel;
   }

   @Override
   public ListDataProvider<ScopeUnitDTO> getScopeUnitDataProvider()
   {
      return scopeUnitModel.getDocumentsDataProvider();
   }

   @Override
   public SingleSelectionModel<ScopeUnitDTO> getSelectionModel()
   {
      return scopeUnitModel.getSelectionModel();
   }

   @Override
   public CellTree getScopeUnitTree()
   {
      return scopeUnitTree;
   }

   @Override
   public TextBox getScopeNameSearchTB()
   {
      return scopeNameSearchTB;
   }

   @Override
   public ListBox getScopeTypeSearchTB()
   {
      return scopeTypeSearchTB;
   }

   @Override
   public TextBox getScopeVersionSearchTB()
   {
      return scopeVersionSearchTB;
   }

   @Override
   public Button getButtonAddScopeUnit()
   {
      return buttonAddScopeUnit;
   }

   @Override
   public Button getDeleteScopeUnit()
   {
      return buttonDeleteScopeUnit;
   }

   @Override
   public Button getCreateScopeUnit()
   {
      return buttonCreateScopeUnit;
   }

   @Override
   public Button getReplaceScopeUnit()
   {
      return buttonReplaceScopeUnit;
   }

   @Override
   public Button getEditScopeUnit()
   {
      return buttonEditScopeUnit;
   }

   @Override
   public Button getUnlinkScopeUnit()
   {
      return buttonUnlinkScopeUnit;
   }

   @Override
   public ValidateDialogBox getValidateDeleteDialogBox()
   {
      return validateDeleteDialogBox;
   }

   @Override
   public ValidateDialogBox getValidateUnlinkDialogBox()
   {
      return validateUnlinkDialogBox;
   }

   @Override
   public InfoDialogBox getInfoDialogBoxSelectScopeUnit()
   {
      return infoDialogBoxSelectScopeUnit;
   }

   @Override
   public InfoDialogBox getInfoDialogBoxSelectRefScopeUnit()
   {
      return infoDialogBoxSelectRefScopeUnit;
   }

   @Override
   public InfoDialogBox getInfoDialogBoxParentRefScopeUnit()
   {
      return infoDialogBoxParentRefScopeUnit;
   }

   interface ScopeTabViewImplUiBinder extends UiBinder<Widget, ScopeTabViewImpl>
   {
   }

   private static class DocumentsTreeModel implements TreeViewModel
   {
      private final ListDataProvider<ScopeUnitDTO>     availableScopeUnit = new ListDataProvider<ScopeUnitDTO>();
      private final SingleSelectionModel<ScopeUnitDTO> selectionModel     = new SingleSelectionModel<ScopeUnitDTO>();

      @Override
      public <T> NodeInfo<?> getNodeInfo(T value)
      {
         if (value == null)
         {
            return new DefaultNodeInfo<ScopeUnitDTO>(availableScopeUnit, new ScopeNodeCell(), selectionModel, null);
         }
         else if (value instanceof ScopeUnitDTO)
         {
            ScopeUnitDTO document = (ScopeUnitDTO) value;
            return new DefaultNodeInfo<ScopeUnitDTO>(new ListDataProvider<ScopeUnitDTO>(document
                                                                                            .getChildrenScopeUnit()),
                                                     new ScopeNodeCell(), selectionModel, null);
         }
         // Unhandled TYPE.
         String type = value.getClass().getName();
         throw new IllegalArgumentException("Unsupported object TYPE: " + type);
      }

      // Check if the specified value represents a leaf node. Leaf nodes cannot be opened.
      @Override
      public boolean isLeaf(Object value)
      {
         boolean isLeaf = false;
         if (value != null && value instanceof ScopeUnitDTO && ((ScopeUnitDTO) value).getChildrenScopeUnit().isEmpty())
         {
            isLeaf = true;
         }
         return isLeaf;
      }

      /**
       * @return the spaceSelectionModel
       */
      public SingleSelectionModel<ScopeUnitDTO> getSelectionModel()
      {
         return selectionModel;
      }

      public ListDataProvider<ScopeUnitDTO> getDocumentsDataProvider()
      {
         return availableScopeUnit;
      }

      /**
       * A Cell used to render the space.
       */
      private static class ScopeNodeCell extends AbstractCell<ScopeUnitDTO>
      {
         @Override
         public void render(Context context, ScopeUnitDTO value, SafeHtmlBuilder sb)
         {
            if (value != null)
            {
               sb.appendEscaped(value.getName());
               if (value.getVersion() != null && !value.getVersion().trim().equalsIgnoreCase(""))
               {
                  sb.appendEscaped(" (" + value.getRefScopeUnitVersion() + ")");
               }
            }
         }
      }

   }

}
