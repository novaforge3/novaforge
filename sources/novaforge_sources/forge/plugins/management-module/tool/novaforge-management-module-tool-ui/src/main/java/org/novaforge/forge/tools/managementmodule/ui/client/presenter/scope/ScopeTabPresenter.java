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

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.scope.ScopeUnitEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.scope.SelectScopeUnitEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.service.scope.ScopeServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.ScopeTabView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.ScopeTabViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author vvigo
 */
public class ScopeTabPresenter implements TabPresenter {

   private final ScopeServiceAsync scopeService;
   private final SimpleEventBus eventBus;
   private final ScopeTabView display;
   private final ProjectPlanDTO projectPlan;
   private final ViewEnum viewMode;
   private String projectId;
   private ScopeUnitDTO currentScope;

   private List<ScopeUnitDTO> refScopeUnitList;

   private ScopeListPresenter scopeListPresenter;

   private ScopeEditPresenter scopeEditPresenter;

   private ReplaceManualScopeUnitPresenter replaceManualScopeUnitPresenter;

   public ScopeTabPresenter(final SimpleEventBus eventBus, final String pProjectId,
         ProjectPlanDTO projectPlanDTO) {
      super();
      this.scopeService = Common.SCOPE_SERVICE;
      this.eventBus = eventBus;
      this.display = new ScopeTabViewImpl();

      this.refScopeUnitList = new ArrayList<ScopeUnitDTO>();

      this.projectId = pProjectId;
      this.projectPlan = projectPlanDTO;
      this.viewMode = getAppropriateViewModeFromProjectPlanStatus();
      getLotScopeListPresenter().go(ScopeTabPresenter.this.display.getContentPanel());
      refreshRefScopeUnitList(projectId);
      enableButtons();
      bind();
   }

   /**
    * Get the appropriate view mode from evaluation of project plan status
    */
   private ViewEnum getAppropriateViewModeFromProjectPlanStatus()
   {
      final AccessRight accessRight = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PERIMETER);
      if (ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT.equals(projectPlan.getStatus()) && accessRight
                                                                                            .equals(AccessRight.WRITE))
      {
         return ViewEnum.EDIT;
      }
      else
      {
         return ViewEnum.READ;
      }
   }

   public ScopeListPresenter getLotScopeListPresenter()
   {
      if (scopeListPresenter == null)
      {
         scopeListPresenter = new ScopeListPresenter(scopeService, eventBus, projectPlan.getProjectPlanId());
      }
      return scopeListPresenter;
   }

   private void refreshRefScopeUnitList(final String projectId)
   {
      refScopeUnitList = new ArrayList<ScopeUnitDTO>();
      new AbstractManagementRPCCall<List<ScopeUnitDTO>>()
      {
         @Override
         protected void callService(AsyncCallback<List<ScopeUnitDTO>> pCb)
         {
            ScopeTabPresenter.this.scopeService.getProjectRefScopeUnitList(projectId, pCb);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(List<ScopeUnitDTO> pResult)
         {
            if (pResult != null)
            {
               refScopeUnitList.addAll(pResult);
               refreshRefScopeList(refScopeUnitList);

               getInfoScopeRef();
            }
         }

      }.retry(0);
   }

   private void enableButtons()
   {
      if (ViewEnum.READ.equals(viewMode))
      {
         display.getButtonAddScopeUnit().setEnabled(false);
         display.getCreateScopeUnit().setEnabled(false);
         display.getReplaceScopeUnit().setEnabled(false);
         display.getEditScopeUnit().setEnabled(false);
         display.getUnlinkScopeUnit().setEnabled(false);
         display.getDeleteScopeUnit().setEnabled(false);
      }
      else
      {
         display.getButtonAddScopeUnit().setEnabled(true);
         display.getCreateScopeUnit().setEnabled(true);
         if (currentScope == null)
         {
            display.getReplaceScopeUnit().setEnabled(false);
            display.getEditScopeUnit().setEnabled(false);
            display.getUnlinkScopeUnit().setEnabled(false);
            display.getDeleteScopeUnit().setEnabled(false);
         }
         else
         {
            display.getEditScopeUnit().setEnabled(true);
            display.getDeleteScopeUnit().setEnabled(true);

            if (currentScope.isManual())
            {
               display.getReplaceScopeUnit().setEnabled(true);
               display.getUnlinkScopeUnit().setEnabled(false);
            }
            else
            {
               display.getReplaceScopeUnit().setEnabled(false);
               display.getUnlinkScopeUnit().setEnabled(true);
            }
            if (!currentScope.getListTaskId().isEmpty())
            {
               display.getEditScopeUnit().setEnabled(false);
               display.getReplaceScopeUnit().setEnabled(false);
               display.getDeleteScopeUnit().setEnabled(false);
            }
         }
      }
   }

   public void bind() {
      bindSearchEvent();

      display.getCreateScopeUnit().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            getScopeManualCreationPresenter().setParams(projectId, projectPlan.getProjectPlanId(), null,
                  ViewEnum.ADD);
            getScopeManualCreationPresenter().go(ScopeTabPresenter.this.display.getContentPanel());
         }
      });

      display.getEditScopeUnit().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            if (currentScope != null) {
               getScopeManualCreationPresenter().setParams(projectId, projectPlan.getProjectPlanId(), currentScope,
                     ViewEnum.EDIT);              
               getScopeManualCreationPresenter().go(
                     ScopeTabPresenter.this.display.getContentPanel());
            } else {
               // veuillez selectionner une scope unit
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().center();
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().show();
            }
         }
      });

      display.getReplaceScopeUnit().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            if (currentScope != null) {
              replaceManualScopeUnitPresenter = new ReplaceManualScopeUnitPresenter(scopeService, eventBus, projectId,
                  projectPlan.getProjectPlanId(),
                  refScopeUnitList, currentScope);
              replaceManualScopeUnitPresenter.go(
                     ScopeTabPresenter.this.display.getContentPanel());
            } else {
               // veuillez selectionner une scope unit
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().center();
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().show();
            }
         }
      });

      display.getButtonAddScopeUnit().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            if (display.getSelectionModel().getSelectedObject() != null) {
               if (display.getSelectionModel().getSelectedObject().getParentScopeUnit() != null) {
                  // you must select parent ref scope unit

                  display.getInfoDialogBoxParentRefScopeUnit().getDialogPanel().center();
                  display.getInfoDialogBoxParentRefScopeUnit().getDialogPanel().show();
               } else {
                  AddScopeRefToLotPresenter addScopeRefToLotPresenter = new AddScopeRefToLotPresenter(
                        scopeService, eventBus, projectPlan.getProjectPlanId(), display
                              .getSelectionModel().getSelectedObject());

                  addScopeRefToLotPresenter.go(ScopeTabPresenter.this.display.getContentPanel());
               }
            } else {
               // veuillez selectionner une ref scope unit
               display.getInfoDialogBoxSelectRefScopeUnit().getDialogPanel().center();
               display.getInfoDialogBoxSelectRefScopeUnit().getDialogPanel().show();
            }
         }
      });

      display.getDeleteScopeUnit().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            if (currentScope != null) {

               display.getValidateDeleteDialogBox().getDialogPanel().center();
               display.getValidateDeleteDialogBox().getDialogPanel().show();

            } else {
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().center();
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().show();
            }
         }
      });

      display.getUnlinkScopeUnit().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            if (currentScope != null) {
               display.getValidateUnlinkDialogBox().getDialogPanel().center();
               display.getValidateUnlinkDialogBox().getDialogPanel().show();
            } else {
               // veuillez selectionner une scope unit
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().center();
               display.getInfoDialogBoxSelectScopeUnit().getDialogPanel().show();
            }
         }
      });

      display.getValidateDeleteDialogBox().getValidate().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            deleteSelectedManualScopeUnit();
         }
      });

      display.getValidateUnlinkDialogBox().getValidate().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            unlinkSelectedScopeUnit();
         }
      });

      // EVENT BUS
      this.eventBus.addHandler(SelectScopeUnitEvent.TYPE, new SelectScopeUnitEvent.Handler() {

         @Override
         public void selectScopeUnit(final SelectScopeUnitEvent pEvent) {
            currentScope = pEvent.getScopeUnitDTO();
            enableButtons();
         }
      });
   }

   private void refreshRefScopeList(List<ScopeUnitDTO> pList)
   {
      display.getScopeUnitDataProvider().setList(pList);
   }

   private void getInfoScopeRef()
   {
      new AbstractManagementRPCCall<List<String>>()
      {
         @Override
         protected void callService(AsyncCallback<List<String>> pCb)
         {
            ScopeTabPresenter.this.scopeService.getScopeTypeList(pCb);
         }

         @Override
         public void onSuccess(List<String> pResult)
         {
            if (pResult != null)
            {
               display.getScopeTypeSearchTB().clear();
               display.getScopeTypeSearchTB().addItem("");
               for (String itType : pResult)
               {
                  display.getScopeTypeSearchTB().addItem(itType);
               }

            }
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   protected void bindSearchEvent() {

      display.getScopeNameSearchTB().addKeyUpHandler(new KeyUpHandler() {
         @Override
         public void onKeyUp(KeyUpEvent event) {
            filteredList(display.getScopeNameSearchTB().getValue(), display.getScopeTypeSearchTB()
                  .getItemText(display.getScopeTypeSearchTB().getSelectedIndex()), display
                  .getScopeVersionSearchTB().getValue());
         }
      });
      display.getScopeNameSearchTB().addValueChangeHandler(new ValueChangeHandler<String>() {
         @Override
         public void onValueChange(ValueChangeEvent<String> pEvent) {
            filteredList(display.getScopeNameSearchTB().getValue(), display.getScopeTypeSearchTB()
                  .getItemText(display.getScopeTypeSearchTB().getSelectedIndex()), display
                  .getScopeVersionSearchTB().getValue());
         }
      });
      display.getScopeVersionSearchTB().addKeyUpHandler(new KeyUpHandler() {
         @Override
         public void onKeyUp(KeyUpEvent event) {
            filteredList(display.getScopeNameSearchTB().getValue(), display.getScopeTypeSearchTB()
                  .getItemText(display.getScopeTypeSearchTB().getSelectedIndex()), display
                  .getScopeVersionSearchTB().getValue());
         }
      });
      display.getScopeVersionSearchTB().addValueChangeHandler(new ValueChangeHandler<String>() {
         @Override
         public void onValueChange(ValueChangeEvent<String> pEvent) {
            filteredList(display.getScopeNameSearchTB().getValue(), display.getScopeTypeSearchTB()
                  .getItemText(display.getScopeTypeSearchTB().getSelectedIndex()), display
                  .getScopeVersionSearchTB().getValue());
         }
      });
      display.getScopeTypeSearchTB().addKeyUpHandler(new KeyUpHandler() {
         @Override
         public void onKeyUp(KeyUpEvent event) {
            filteredList(display.getScopeNameSearchTB().getValue(), display.getScopeTypeSearchTB()
                  .getItemText(display.getScopeTypeSearchTB().getSelectedIndex()), display
                  .getScopeVersionSearchTB().getValue());
         }
      });
      display.getScopeTypeSearchTB().addChangeHandler(new ChangeHandler() {
         @Override
         public void onChange(ChangeEvent event) {
            filteredList(display.getScopeNameSearchTB().getValue(), display.getScopeTypeSearchTB()
                  .getItemText(display.getScopeTypeSearchTB().getSelectedIndex()), display
                  .getScopeVersionSearchTB().getValue());
         }
      });
   }

   public ScopeEditPresenter getScopeManualCreationPresenter()
   {
      if (scopeEditPresenter == null)
      {
         scopeEditPresenter = new ScopeEditPresenter(scopeService, eventBus, projectPlan.getProjectPlanId(), projectId,
                                                     null, ViewEnum.EDIT);
      }
      return scopeEditPresenter;
   }

   private void deleteSelectedManualScopeUnit()
   {
      new AbstractManagementRPCCall<Boolean>()
      {
         @Override
         protected void callService(AsyncCallback<Boolean> pCb)
         {
            ScopeTabPresenter.this.scopeService.deleteManualScopeUnit(currentScope.getUnitId(), pCb);
         }

         @Override
         public void onSuccess(Boolean pResult)
         {
            if (pResult != null && pResult)
            {
               display.getValidateDeleteDialogBox().getDialogPanel().hide();
               eventBus.fireEvent(new ScopeUnitEvent());
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            display.getValidateDeleteDialogBox().getDialogPanel().hide();
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   private void unlinkSelectedScopeUnit()
   {
      new AbstractManagementRPCCall<Boolean>()
      {
         @Override
         protected void callService(AsyncCallback<Boolean> pCb)
         {
            ScopeTabPresenter.this.scopeService.unlinkScopeUnitFromRef(currentScope.getUnitId(), pCb);
         }

         @Override
         public void onSuccess(Boolean pResult)
         {
            if (pResult != null && pResult)
            {
               display.getValidateUnlinkDialogBox().getDialogPanel().hide();
               eventBus.fireEvent(new ScopeUnitEvent());
            }
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   public void filteredList(final String pScopeName, final String pType, final String pVersion) {
      Collection<ScopeUnitDTO> filter = filteredOnScope(refScopeUnitList, pScopeName, pType,
            pVersion);
      refreshRefScopeList(new ArrayList<ScopeUnitDTO>(filter));
   }

   private Collection<ScopeUnitDTO> filteredOnScope(final List<ScopeUnitDTO> pScopeLotList,
         final String pScopeName, final String pType, final String pVersion) {
      return Collections2.filter(pScopeLotList,
            new Predicate<ScopeUnitDTO>() {
               @Override
               public boolean apply(ScopeUnitDTO pInput) {
                  boolean ret = false;
                  if (pInput == null) {
                     return false;
                  }
                  if ((pInput.getName().contains(pScopeName))
                        && (pInput.getRefScopeUnitVersion().contains(pVersion))
                        && (pType.equalsIgnoreCase("") || pInput.getType().contains(pType))) {
                     ret = true;
                  } else {
                     if (pInput.getChildrenScopeUnit() != null) {
                        for (ScopeUnitDTO child : pInput.getChildrenScopeUnit()) {
                           if ((child.getName().contains(pScopeName))
                                 && (child.getRefScopeUnitVersion().contains(pVersion))
                                 && (pType.equalsIgnoreCase("") || child.getType().contains(pType))) {
                              ret = true;
                              break;
                           }
                        }
                     } else {
                        ret = false;
                     }
                  }
                  return ret;
               }
            });
   }

   @Override
   public void go(final HasWidgets container)
   {
      container.clear();
      container.add(this.display.asWidget());
   }

   @Override
   public void loadDataOnSelectionTab()
   {
      refreshRefScopeUnitList(projectId);
      getScopeTypeList();
      getLotScopeListPresenter().refreshScopeLots(projectPlan.getProjectPlanId());
   }

   @Override
   public IsWidget getDisplay()
   {
      return this.display.asWidget();
   }

   private void getScopeTypeList() {
      new AbstractManagementRPCCall<List<String>>()
      {
         @Override
         protected void callService(AsyncCallback<List<String>> pCb) {
            ScopeTabPresenter.this.scopeService.getScopeTypeList(pCb);
         }

         @Override
         public void onSuccess(List<String> pResult) {
            if (pResult != null) {
               display.getScopeTypeSearchTB().clear();
               for (String itType : pResult) {
                  // TODO les types d'unité de périmètres devraient utiliser les fonctionalId
                  display.getScopeTypeSearchTB().addItem(itType);
               }
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }
   
}
