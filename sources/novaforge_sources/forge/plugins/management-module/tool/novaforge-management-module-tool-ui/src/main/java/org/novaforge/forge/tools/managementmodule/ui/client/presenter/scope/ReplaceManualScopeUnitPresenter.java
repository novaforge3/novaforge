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

package org.novaforge.forge.tools.managementmodule.ui.client.presenter.scope;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.event.scope.ScopeUnitEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.service.scope.ScopeServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.ReplaceManualScopeUnitView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.ReplaceManualScopeUnitViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vvigo
 */
public class ReplaceManualScopeUnitPresenter implements Presenter {

   private final ScopeServiceAsync scopeService;
   private final SimpleEventBus eventBus;
   private final ReplaceManualScopeUnitView display;

   private final Long projectPlanId;
   private final ScopeUnitDTO curentScope;

   private final List<ScopeUnitDTO> refScopeUnitList;
   private List<ScopeUnitDTO> scopedUnitList;

   public ReplaceManualScopeUnitPresenter(final ScopeServiceAsync scopeService,
         final SimpleEventBus eventBus, final String pProjectId, final Long pProjectPlanId,
         final List<ScopeUnitDTO> refScopeUnitList, final ScopeUnitDTO curentScope) {
      super();
      this.scopeService = scopeService;
      this.eventBus = eventBus;
      this.display = new ReplaceManualScopeUnitViewImpl();

      this.projectPlanId = pProjectPlanId;
      this.curentScope = curentScope;
      this.refScopeUnitList = refScopeUnitList;
      bind();
   }

   public void bind() {

      ReplaceManualScopeUnitPresenter.this.display.getButtonSave().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            if (display.getSelectionModel().getSelectedObject() == null) {
               // a ref scope must be selected
               display.getInfoDialogBoxSelectScopeUnit().setText(Common.MESSAGES_SCOPE.selectScopeUnit());
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().center();
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().show();
            } else if (display.getSelectionModel().getSelectedObject().getType()
                  .equalsIgnoreCase(Constants.ROOT_REPOSITORY_KEY)) {
               display.getInfoDialogBoxSelectScopeUnit().setText(
                     Common.MESSAGES_SCOPE.cdoRepositorySelected());
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().center();
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().show();
            } else {
               replaceManualScopeUnit();
//               islinkedRefScopeUnit();
            }
         }
      });
      ReplaceManualScopeUnitPresenter.this.display.getButtonCancel().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            cancelCreateScope();
         }
      });
   }

   /**
    * Launch the linkExistingScopeUnit service
    */
   private void replaceManualScopeUnit() {
      new AbstractManagementRPCCall<Boolean>()
      {
         @Override
         protected void callService(AsyncCallback<Boolean> pCb) {
            ReplaceManualScopeUnitPresenter.this.scopeService.linkExistingScopeUnit(display
                  .getSelectionModel().getSelectedObject().getRefScopeUnitId(), display.getSelectionModel()
                  .getSelectedObject().getRefScopeUnitVersion(), curentScope.getUnitId(), pCb);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(Boolean pResult) {
            if (pResult != null && pResult) {
               ReplaceManualScopeUnitPresenter.this.display.closeWidget();
               eventBus.fireEvent(new ScopeUnitEvent());
            }
         }


      }.retry(0);
   }

   /**
    * Close Popup
    */
   private void cancelCreateScope()
   {
      ReplaceManualScopeUnitPresenter.this.display.closeWidget();
   }

   @Override
   public void go(final HasWidgets container)
   {
      initScopedUnitList();
      ReplaceManualScopeUnitPresenter.this.display.showWidget();
   }

   @Override
   public IsWidget getDisplay()
   {
      return this.display.asWidget();
   }

   /**
    * Get the list of ScopeUnitDTO for refresh CellTable
    */
   private void initScopedUnitList() {
      scopedUnitList = new ArrayList<ScopeUnitDTO>();
      new AbstractManagementRPCCall<List<ScopeUnitDTO>>()
      {
         @Override
         protected void callService(AsyncCallback<List<ScopeUnitDTO>> pCb) {
            ReplaceManualScopeUnitPresenter.this.scopeService.getProjectPlanScope(projectPlanId, null, true,
                  true, pCb);
         }

         @Override
         public void onSuccess(List<ScopeUnitDTO> pResult) {
           if (pResult != null) {
               scopedUnitList.addAll(pResult);
               ((ReplaceManualScopeUnitViewImpl)ReplaceManualScopeUnitPresenter.this.display).setPlanProjectScopeUnitList(scopedUnitList);
               for (ScopeUnitDTO itScope : scopedUnitList) {
                 if (!itScope.isManual() && itScope.getRefScopeUnitId() != null) {
                     for (ScopeUnitDTO itRef : refScopeUnitList) {
                        if (itScope.getRefScopeUnitId().equals(itRef.getUnitId())) {
                           refScopeUnitList.remove(itRef);
                        }
                     }
                  }
               }
               refreshRefScopeList(refScopeUnitList);
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   /**
    * Call isAlreadyLinkedRef service
    */
//      private void islinkedRefScopeUnit() {
//   
//         new AbstractManagementRPCCall<Boolean>() {
//            @Override
//            protected void callService(AsyncCallback<Boolean> pCb) {
//               ReplaceManualScopeUnitPresenter.this.scopeService.isAlreadyLinkedRef(display.getSelectionModel()
//                     .getSelectedObject().getRefScopeUnitId(), pCb);
//            }
//   
//            @Override
//            public void onSuccess(Boolean pResult) {
//               if (pResult != null) {
//                  if (pResult) {
//                     display.getValidateDialogBox().getDialogPanel().center();
//                     display.getValidateDialogBox().getDialogPanel().show();
//                  } else {
//                     replaceManualScopeUnit();
//                  }
//               }
//            }
//   
//            @Override
//            public void onFailure(Throwable caught) {
//               ErrorManagement.displayErrorMessage(caught);
//            }
//         }.retry(0);
//      }
   private void refreshRefScopeList(List<ScopeUnitDTO> pList)
   {
      display.getScopeUnitDataProvider().setList(pList);

   }
}
