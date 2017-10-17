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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.marker;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowListEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.Validator;
import org.novaforge.forge.tools.managementmodule.ui.client.view.marker.MarkerEditView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.marker.MarkerEditViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MarkerEditPresenter implements TabPresenter {
   private static final String EMPTY_TEXT = "";
   private final SimpleEventBus eventBus;
   private final MarkerEditView display;
   private ProjectPlanDTO projectPlanDTO;
   private ViewEnum viewMode;
   private MarkerDTO markerDTO;
   private Long markerId;

   public MarkerEditPresenter(final SimpleEventBus eventBus, ProjectPlanDTO projectPlanDTO) {
      super();
      this.eventBus = eventBus;
      this.display = new MarkerEditViewImpl();
      this.projectPlanDTO = projectPlanDTO;
      this.viewMode = getAppropriateViewModeFromProjectPlanStatus();
      bind();
   }

   @Override
   public void go(HasWidgets container) {
      container.clear();
      container.add(this.display.asWidget());
   }

   public void bind() {
      display.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            executeActionAddSaveButton();
         }
      });
      display.getButtonAddMarker().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            display.getValidateDialogBox().setDetails(
                  Common.getProjectPlanMessages().addValidationCreateMarkerMessage());
            validateActionOnAddSaveButton();
         }
      });

      display.getButtonSaveMarker().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            display.getValidateDialogBox().setDetails(
                  Common.getProjectPlanMessages().addValidationCreateMarkerMessage());
            validateActionOnAddSaveButton();
         }
      });

      display.getButtonCancelMarker().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {

            eventBus.fireEvent(new ShowListEvent(ShowListEvent.EnumList.MARKER_LIST));
         }
      });

      display.getMarkerNameValidator().setValidator(new Validator() {

         @Override
         public boolean isValid(String pValue) {
            return !(pValue == null || EMPTY_TEXT.equals(pValue.trim()));
         }

         @Override
         public String getErrorMessage() {
            return Common.getGlobal().messageRequiredField();
         }
      });

      display.getMarkerDatePicker().setValidator(new Validator() {

         @Override
         public boolean isValid(String pValue) {
            return (!(pValue == null || EMPTY_TEXT.equals(pValue)));
         }

         @Override
         public String getErrorMessage() {
            return Common.getGlobal().messageRequiredField();
         }
      });

      display.getMarkerDescValidator().setValidator(new Validator() {

         @Override
         public boolean isValid(String pValue) {
            boolean valid = false;
            if ((pValue != null) && (!EMPTY_TEXT.equals(pValue.trim())) && pValue.trim().length() < 2000)
            {
               valid = true;
            }
            return valid;
         }

         @Override
         public String getErrorMessage() {
            return Common.getGlobal().messageRequiredFieldOrDataToLong();
         }
      });
   }

   void validateActionOnAddSaveButton() {
      if (display.getMarkerNameValidator().isValid() && display.getMarkerDatePicker().isValid()
            && display.getMarkerDescValidator().isValid()) {

         if (editionIsACreation()) {
            display.getValidateDialogBox().getDialogPanel().center();
            display.getValidateDialogBox().getDialogPanel().show();
         } else {
            executeActionAddSaveButton();
         }
      } else {
         display.getMarkerNameValidator().isValid();
         display.getMarkerDatePicker().isValid();
         display.getMarkerDescValidator().isValid();
         InfoDialogBox info = new InfoDialogBox(Common.getProjectPlanMessages()
               .markerErrorValidation(), InfoTypeEnum.KO);
         info.getDialogPanel().center();
         info.getDialogPanel().show();
      }
   }

   void executeActionAddSaveButton() {
      majMarkerDTOFromForm();
      if (editionIsACreation()) {
         creeteMarker();
      } else {
         saveMarker();
      }
      display.getValidateDialogBox().getDialogPanel().hide();
   }

   private void creeteMarker() {
      new AbstractManagementRPCCall<MarkerDTO>()
      {
         @Override
         protected void callService(AsyncCallback<MarkerDTO> pCb) {
            Common.PROJECT_PLAN_SERVICE.creeteMarker(markerDTO.getpPlanId(),
                  markerDTO.getName(), markerDTO.getDate(), markerDTO.getDesc(),
                  markerDTO.getMarkerTypeFunctionalId(), pCb);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(MarkerDTO pResult) {
            InfoDialogBox info = new InfoDialogBox(Common.getProjectPlanMessages()
                  .createMarkerSuccessed(), InfoTypeEnum.OK);
            info.getDialogPanel().center();
            info.getDialogPanel().show();
            if (pResult != null) {
               markerDTO = pResult;
               loadFormFromMarkerDTO();
               eventBus.fireEvent(new ShowListEvent(ShowListEvent.EnumList.MARKER_LIST));
            }
         }


      }.retry(0);
   }

   private void saveMarker() {
      new AbstractManagementRPCCall<MarkerDTO>()
      {
         @Override
         protected void callService(AsyncCallback<MarkerDTO> pCb) {
            Common.PROJECT_PLAN_SERVICE.saveMarker(markerDTO.getId(), markerDTO,
                  pCb);
         }

         @Override
         public void onSuccess(MarkerDTO pResult) {
            InfoDialogBox info = new InfoDialogBox(Common.getProjectPlanMessages()
                  .saveMarkerSuccessed(), InfoTypeEnum.OK);
            info.getDialogPanel().center();
            info.getDialogPanel().show();
            if (pResult != null) {
               eventBus.fireEvent(new ShowListEvent(ShowListEvent.EnumList.MARKER_LIST));
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   private void majMarkerDTOFromForm() {
      markerDTO.setName(display.getMarkerNameValidator().getValue());
      markerDTO.setDesc(display.getMarkerDescValidator().getValue());
      markerDTO.setDate(display.getMarkerDatePicker().getValue());
      markerDTO.setMarkerTypeFunctionalId(display.getMarkerTypeList().getValue(
            display.getMarkerTypeList().getSelectedIndex()));
      markerDTO.setMarkerTypeName(display.getMarkerTypeList().getItemText(
            display.getMarkerTypeList().getSelectedIndex()));
   }

   public void majMarkerData(Long markerId, ProjectPlanDTO projectPlanDTO)
   {
      setMarkerId(markerId);
      this.projectPlanDTO = projectPlanDTO;
      this.viewMode = getAppropriateViewModeFromProjectPlanStatus();
      getMarkerTypeList();
   }

   /**
    * Get the appropriate view mode from evaluation of project plan status
    */
   private ViewEnum getAppropriateViewModeFromProjectPlanStatus()
   {
      if (ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT.equals(projectPlanDTO.getStatus()))
      {
         return ViewEnum.EDIT;
      } else {
         return ViewEnum.READ;
      }
   }

   public void getMarkerTypeList()
   {
      new AbstractManagementRPCCall<List<MarkerTypeDTO>>()
      {
         @Override
         protected void callService(AsyncCallback<List<MarkerTypeDTO>> pCb)
         {
            Common.REFERENTIAL_SERVICE.getMarkerTypeDTOList(pCb);
         }

         @Override
         public void onSuccess(List<MarkerTypeDTO> pResult)
         {
            if (pResult != null) {
               fillMarkerTypeList(pResult);
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   private void fillMarkerTypeList(List<MarkerTypeDTO> list)
   {
      List<MarkerTypeDTO> markerTypes = new ArrayList<MarkerTypeDTO>();
      markerTypes.addAll(list);
      Collections.sort(markerTypes, new Comparator<MarkerTypeDTO>()
      {

         @Override
         public int compare(MarkerTypeDTO o1, MarkerTypeDTO o2)
         {
            return o1.getFunctionalId().compareTo(o2.getFunctionalId());
         }
      });

      display.getMarkerTypeList().clear();

      for (MarkerTypeDTO markerType : markerTypes)
      {
         display.getMarkerTypeList().addItem(markerType.getName(), markerType.getFunctionalId());
      }

      loadDTOAfterLoadingParentList();
   }

   private void loadDTOAfterLoadingParentList() {
      //creation
      if(markerId == null) {
         markerDTO = new MarkerDTO();
         markerDTO.setpPlanId(projectPlanDTO.getProjectPlanId());
         loadFormFromMarkerDTO();
         enableFieldsForModeAddOrEdit();
      }
      //modification
      else {
         getMarker();
         enableFieldsForModeAddOrEdit();
      }
      //readonly
      if(viewMode.equals(ViewEnum.READ)) {
         getMarker();
         disableFieldsForModeRead();
      }
   }

   private void loadFormFromMarkerDTO()
   {
      display.getMarkerNameValidator().setValue(markerDTO.getName());
      display.getMarkerDescValidator().setValue(markerDTO.getDesc());
      display.getMarkerDatePicker().setValue(markerDTO.getDate());

      if (markerDTO.getMarkerTypeFunctionalId() == null)
      {
         display.getMarkerTypeList().setSelectedIndex(0);
      }
      else
      {
         boolean finded = false;
         for (int i = 0; i < display.getMarkerTypeList().getItemCount(); i++)
         {
            if (markerDTO.getMarkerTypeFunctionalId().equals(display.getMarkerTypeList().getValue(i)))
            {
               finded = true;
               display.getMarkerTypeList().setSelectedIndex(i);
               break;
            }
            if (!finded)
            {
               display.getMarkerTypeList().setSelectedIndex(0);
            }
         }
      }
   }

   public void enableFieldsForModeAddOrEdit() {
      display.getButtonCancelMarker().setEnabled(true);
      display.getButtonAddMarker().setEnabled(true);
      display.getButtonSaveMarker().setEnabled(true);
      display.getMarkerDatePicker().setEnabled(true);
      display.getMarkerNameValidator().setEnabled(true);
      display.getMarkerDescValidator().setEnabled(true);
      display.getMarkerTypeList().setEnabled(true);
      if (editionIsACreation()) {
         display.getButtonAddMarker().setVisible(true);
         display.getButtonSaveMarker().setVisible(false);
      } else {
         display.getButtonAddMarker().setVisible(false);
         display.getButtonSaveMarker().setVisible(true);
      }
   }

   private void getMarker()
   {
      new AbstractManagementRPCCall<MarkerDTO>()
      {
         @Override
         protected void callService(AsyncCallback<MarkerDTO> pCb)
         {
            Common.PROJECT_PLAN_SERVICE.getMarker(markerId, pCb);
         }

         @Override
         public void onSuccess(MarkerDTO pResult)
         {
            if (pResult != null) {
               markerDTO = pResult;
               loadFormFromMarkerDTO();
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   public void disableFieldsForModeRead()
   {
      display.getButtonCancelMarker().setEnabled(true);
      display.getButtonAddMarker().setEnabled(false);
      display.getButtonSaveMarker().setVisible(false);
      display.getMarkerDatePicker().setEnabled(false);
      display.getMarkerNameValidator().setEnabled(false);
      display.getMarkerDescValidator().setEnabled(false);
      display.getMarkerTypeList().setEnabled(false);
   }

   /**
    * Return true if the edition is a creation, false otherwise
    *
    * @return true if creation
    */
   private boolean editionIsACreation()
   {
      return markerId == null;
   }

   public Long getMarkerId() {
      return markerId;
   }

   public void setMarkerId(Long markerId) {
      this.markerId = markerId;
   }

   @Override
   public void loadDataOnSelectionTab()
   {
   }
   
   @Override
   public IsWidget getDisplay()
   {
      return this.display.asWidget();
   }
}
