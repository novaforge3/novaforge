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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

import java.util.ArrayList;
import java.util.List;

import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltree.TreeResources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;

/**
 * @author vvigo
 */
public class ReplaceManualScopeUnitViewImpl extends PopupPanel implements
      ReplaceManualScopeUnitView {
   public static final String EDIT_MODE = "EDIT_MODE";
   public static final String CREATE_MODE = "CREATE_MODE";
   public static final String READONLY_MODE = "READONLY_MODE";
   private final static TreeResources TREE_RESOURCES = GWT.create(TreeResources.class);
   private static DeliveryManagementViewImplUiBinder uiBinder = GWT
         .create(DeliveryManagementViewImplUiBinder.class);
   private final DocumentsTreeModel scopeUnitModel;
   private final InfoDialogBox      infoDialogBoxSelectScopeUnit;
   @UiField
   Label addScopeRefToLotTitle;

   @UiField
   Label scopeUnitListTitle;
   @UiField(provided = true)
   CellTree scopeUnitTree;

   @UiField
   Button buttonSave;
   @UiField
   Button buttonCancel;

   public ReplaceManualScopeUnitViewImpl() {

      Common.getResource().css().ensureInjected();
      // Init tree cell
      scopeUnitModel = new DocumentsTreeModel();
      scopeUnitTree = new CellTree(scopeUnitModel, null, TREE_RESOURCES);
      scopeUnitTree.setAnimationEnabled(true);

      add(uiBinder.createAndBindUi(this));

      addScopeRefToLotTitle.setText(Common.MESSAGES_SCOPE.addScopeRefToLotTitle());
      scopeUnitListTitle.setText(Common.MESSAGES_SCOPE.buttonReplaceScopeUnit());

      buttonSave.setText(Common.MESSAGES_SCOPE.saveButtonRemplace());
      buttonCancel.setText(Common.MESSAGES_SCOPE.cancelButton());

      // Initialization of validation popup
      infoDialogBoxSelectScopeUnit = new InfoDialogBox(Common.MESSAGES_SCOPE
            .selectRefScopeUnit(), InfoTypeEnum.KO);

      setModal(true);
      setGlassEnabled(true);
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
   public void showWidget() {
      center();
      show();
   }

   @Override
   public void closeWidget() {
      hide();
   }

   @Override
   public Button getButtonSave() {
      return buttonSave;
   }

   @Override
   public Button getButtonCancel() {
      return buttonCancel;
   }

  /**
   * @param planProjectScopeUnitList 
   *		the planProjectScopeUnitList to set
   */
  public void setPlanProjectScopeUnitList(List<ScopeUnitDTO> planProjectScopeUnitList)
  {
    scopeUnitModel.setPlanProjectScopeUnitList(planProjectScopeUnitList);
  }

  @Override
   public InfoDialogBox getInfoDialogBoxSelectScopeUnit()
   {
      return infoDialogBoxSelectScopeUnit;
   }

   interface DeliveryManagementViewImplUiBinder extends UiBinder<Widget, ReplaceManualScopeUnitViewImpl>
   {
   }

   private static class DocumentsTreeModel implements TreeViewModel
   {
      private final ListDataProvider<ScopeUnitDTO>     availableScopeUnit = new ListDataProvider<ScopeUnitDTO>();
      private final SingleSelectionModel<ScopeUnitDTO> selectionModel     = new SingleSelectionModel<ScopeUnitDTO>();
      private List<ScopeUnitDTO> planProjectScopeUnitList;

      @Override
      public <T> NodeInfo<?> getNodeInfo(T value)
      {
        
         if (value == null)
         {
            return new DefaultNodeInfo<ScopeUnitDTO>(availableScopeUnit, new ScopeNodeCell(), selectionModel, null);
         }
         if (value instanceof ScopeUnitDTO)
         {
            ScopeUnitDTO document = (ScopeUnitDTO) value;
            List<ScopeUnitDTO> childrenScopeUnits = document.getChildrenScopeUnit();
            List<ScopeUnitDTO> filteredChildrenScopeUnits = new ArrayList<ScopeUnitDTO>(); 
            if (childrenScopeUnits != null && childrenScopeUnits.size()>0 )
            {
              for (ScopeUnitDTO childScopeUnit : childrenScopeUnits)
              {
                //if it's a node or if it's aleaf which doesnt belong to scope => we add it, otherwise it s filtered
                if (!isScopeUnitInProjectPlan(childScopeUnit) || !isLeaf(childScopeUnit) )
                {
                  filteredChildrenScopeUnits.add(childScopeUnit);
                }
              }
            }
            
            return new DefaultNodeInfo<ScopeUnitDTO>(new ListDataProvider<ScopeUnitDTO>(filteredChildrenScopeUnits),
                                                     new ScopeNodeCell(), selectionModel, null);
         }
         // Unhandled TYPE.
         String type = value.getClass().getName();
         throw new IllegalArgumentException("Unsupported object TYPE: " + type);
      }

      // Check if the specified value represents a leaf node. Leaf nodes cannot
      // be opened.
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
               sb.appendEscaped(value.getName() + " (" + value.getRefScopeUnitVersion() + ")");
            }
         }
      }
      
      /**
       * set plan project scope unit list
       * @param planProjectScopeUnitList 
       *    the planProjectScopeUnitList to set
       */
      public void setPlanProjectScopeUnitList(List<ScopeUnitDTO> planProjectScopeUnitList)
      {
        this.planProjectScopeUnitList = planProjectScopeUnitList;
      }
      
      /**
       * returns true if provided scopeUnit is already defined in project plan 
       * @param searchedScopeUnit
       * @return
       */
      public boolean isScopeUnitInProjectPlan(ScopeUnitDTO searchedScopeUnit)
      {
        boolean result = false;
        if (planProjectScopeUnitList != null )
        {
          for (ScopeUnitDTO scopeUnit : planProjectScopeUnitList)
          {
            if (searchedScopeUnit.getRefScopeUnitId().equals(scopeUnit.getRefScopeUnitId()))
            {
              result = true;
              break;
            }
          }
        }
        return result;
      }
   }
}
