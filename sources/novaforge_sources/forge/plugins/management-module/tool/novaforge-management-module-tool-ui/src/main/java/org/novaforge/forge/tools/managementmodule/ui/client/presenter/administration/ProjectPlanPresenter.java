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

package org.novaforge.forge.tools.managementmodule.ui.client.presenter.administration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.novaforge.forge.tools.managementmodule.ui.client.ManagementModuleEntryPoint;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ProjectPlanReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.ProjectRepositorySettingsMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.view.administration.ProjectPlanView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.administration.ProjectPlanViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.ParameterizableSelectionCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustFactorJointureDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.AdjustWeightDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentDetailDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Identifiable;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * @author qsivan
 */
public class ProjectPlanPresenter implements TabPresenter {
   /** The adjust weight comparator */
   private static Comparator<AdjustWeightDTO> comparatorAdjustWeight = new Comparator<AdjustWeightDTO>() {
      @Override
      public int compare(final AdjustWeightDTO o1, final AdjustWeightDTO o2) {
         return o1.getWeight().compareTo(o2.getWeight());
      }
   };
   /** The adjust factor junction comparator */
   private static Comparator<AdjustFactorJointureDTO> comparatorAdjustFactorJunction = new Comparator<AdjustFactorJointureDTO>()
   {
      @Override
      public int compare(final AdjustFactorJointureDTO o1, final AdjustFactorJointureDTO o2)
      {
         return o1.getAdjustFactor().getName().compareTo(o2.getAdjustFactor().getName());
      }
   };
   private final  ProjectRepositorySettingsMessage projectRepositorySettingsMessages = (ProjectRepositorySettingsMessage) GWT.create(ProjectRepositorySettingsMessage.class);
   private final SimpleEventBus                eventBus;
   private final ProjectPlanView               display;
   private       List<AdjustWeightDTO>         listPonderationAdjustWeightDTO;
   private       List<AdjustFactorJointureDTO> listAdjustFactorJointureDTO;
   private       ProjectPlanDTO                currentProjectPlan;

   public ProjectPlanPresenter(final SimpleEventBus eventBus)
   {
      super();
      this.eventBus = eventBus;
      this.display = new ProjectPlanViewImpl();
      bind();
   }

   public void bind()
   {
      display.getButtonCancelModifications().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            loadDatas();
         }
      });

      display.getButtonSaveSettings().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            saveSettings();
         }

      });

      display.getButtonHomeReturn().addClickHandler(new ClickHandler()
      {

         @Override
         public void onClick(ClickEvent event)
         {
            ManagementModuleEntryPoint.getHomePresenter().go(RootLayoutPanel.get());
         }
      });

      display.getInputAbaqueChargeHJBox().addValueChangeHandler(new ValueChangeHandler<String>()
      {
         @Override
         public void onValueChange(ValueChangeEvent<String> event)
         {
            String value = event.getValue().replace(",", ".").trim();
            try
            {
               Float.parseFloat(value);
               display.getInputAbaqueChargeHJBox().setText(value);
            }
            catch (Exception ex)
            {
               showNotAnFloatMessage();
               display.getInputAbaqueChargeHJBox().setText(String.valueOf(currentProjectPlan
                                                                              .getEstimationComponentDetail()
                                                                              .getValueAbaChgHomJour()));
            }
         }
      });
   }

   private void loadDatas()
   {
      new AbstractManagementRPCCall<List<AdjustWeightDTO>>()
      {
         @Override
         protected void callService(final AsyncCallback<List<AdjustWeightDTO>> pCb)
         {
            Common.PROJECT_PLAN_SERVICE.getAllAdjustWeightDTOList(pCb);
         }

         @Override
         public void onFailure(final Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(final List<AdjustWeightDTO> pResult)
         {
            listPonderationAdjustWeightDTO = pResult;
            // in case of a reloading we delete the ponderationColumn if
            // exist
            if (display.getFacteursAjustementList().getColumnCount() == 2)
            {
               display.getFacteursAjustementList().removeColumn(1);
            }
            //result sort and pu in map for render
            Collections.sort(listPonderationAdjustWeightDTO, comparatorAdjustWeight);
            Map<Identifiable, String> map = new LinkedHashMap<Identifiable, String>();
            for (final AdjustWeightDTO ponderation : listPonderationAdjustWeightDTO)
            {
               map.put(ponderation, ponderation.getWeight() + " - " + ponderation.getName());
            }

            //column managing
            Column<AdjustFactorJointureDTO, AdjustWeightDTO> ponderationColumn = new Column<AdjustFactorJointureDTO, AdjustWeightDTO>(new ParameterizableSelectionCell<AdjustWeightDTO>(map))
            {
               @Override
               public AdjustWeightDTO getValue(final AdjustFactorJointureDTO pObject)
               {
                  return pObject.getAdjustWeight();
               }
            };
            FieldUpdater<AdjustFactorJointureDTO, AdjustWeightDTO> ponderationUpdater = new FieldUpdater<AdjustFactorJointureDTO, AdjustWeightDTO>()
            {
               @Override
               public void update(int index, AdjustFactorJointureDTO object, AdjustWeightDTO value)
               {
                  display.getDataFacteursAjustementProvider().getList().get(index).setAdjustWeight(value);
               }
            };
            ponderationColumn.setFieldUpdater(ponderationUpdater);
            display.getFacteursAjustementList().addColumn(ponderationColumn, projectRepositorySettingsMessages
                                                                                 .valeurFacteurAjustementColumnLabel());

            if (isDataLoadingTerminated())
            {
               loadDTOsInDataProvider();
            }
         }

      }.retry(0);

      new AbstractManagementRPCCall<ProjectPlanDTO>()
      {
         @Override
         protected void callService(final AsyncCallback<ProjectPlanDTO> pCb)
         {
            String idProjet = SessionData.projectId;
            Common.PROJECT_PLAN_SERVICE.getLastProjectPlanFull(idProjet, pCb);
         }

         @Override
         public void onSuccess(final ProjectPlanDTO pResult) {
            currentProjectPlan = pResult;
            EstimationComponentDetailDTO ecd = pResult.getEstimationComponentDetail();
            display.getInputAbaqueChargeHJBox().setText(String.valueOf(Common.floatFormat(ecd.getValueAbaChgHomJour(),2)));

            // on fait la liste a la main car table pas generique
            List<ComponentDTO> listComponentDTO = new LinkedList<ComponentDTO>();
            EstimationComponentDetailDTO estDTO = pResult.getEstimationComponentDetail();
            ComponentDTO cDTO1 = new ComponentDTO();
            cDTO1.setFunctionalId(Constants.GDI);
            cDTO1.setComposantLibelle(projectRepositorySettingsMessages.GDI());
            cDTO1.setComposantValeurComplexe(estDTO.getValueComplexGDI());
            cDTO1.setComposantValeurMoyen(estDTO.getValueMoyenGDI());
            cDTO1.setComposantValeurSimple(estDTO.getValueSimpleGDI());
            listComponentDTO.add(cDTO1);
            ComponentDTO cDTO2 = new ComponentDTO();
            cDTO2.setFunctionalId(Constants.IN);
            cDTO2.setComposantLibelle(projectRepositorySettingsMessages.IN());
            cDTO2.setComposantValeurComplexe(estDTO.getValueComplexIN());
            cDTO2.setComposantValeurMoyen(estDTO.getValueMoyenIN());
            cDTO2.setComposantValeurSimple(estDTO.getValueSimpleIN());
            listComponentDTO.add(cDTO2);
            ComponentDTO cDTO3 = new ComponentDTO();
            cDTO3.setFunctionalId(Constants.INT);
            cDTO3.setComposantLibelle(projectRepositorySettingsMessages.INT());
            cDTO3.setComposantValeurComplexe(estDTO.getValueComplexINT());
            cDTO3.setComposantValeurMoyen(estDTO.getValueMoyenINT());
            cDTO3.setComposantValeurSimple(estDTO.getValueSimpleINT());
            listComponentDTO.add(cDTO3);
            ComponentDTO cDTO4 = new ComponentDTO();
            cDTO4.setFunctionalId(Constants.OUT);
            cDTO4.setComposantLibelle(projectRepositorySettingsMessages.OUT());
            cDTO4.setComposantValeurComplexe(estDTO.getValueComplexOUT());
            cDTO4.setComposantValeurMoyen(estDTO.getValueMoyenOUT());
            cDTO4.setComposantValeurSimple(estDTO.getValueSimpleOUT());
            listComponentDTO.add(cDTO4);
            ComponentDTO cDTO5 = new ComponentDTO();
            cDTO5.setFunctionalId(Constants.GDE);
            cDTO5.setComposantLibelle(projectRepositorySettingsMessages.GDE());
            cDTO5.setComposantValeurComplexe(estDTO.getValueComplexGDE());
            cDTO5.setComposantValeurMoyen(estDTO.getValueMoyenGDE());
            cDTO5.setComposantValeurSimple(estDTO.getValueSimpleGDE());
            listComponentDTO.add(cDTO5);
            //if not the first time, we clear the view data cache
            if(!display.getDataComposantsProvider().getList().isEmpty()) {
               for(final ComponentDTO composant : listComponentDTO) {
                  display.getSimpleStylableEditTextCell().clearViewData(composant.getComposantLibelle());
                  display.getMediumStylableEditTextCell().clearViewData(composant.getComposantLibelle());
                  display.getComplexStylableEditTextCell().clearViewData(composant.getComposantLibelle());
               }
            }
            display.getDataComposantsProvider().setList(listComponentDTO);
            display.getComposantsList().redraw();
            listAdjustFactorJointureDTO = pResult.getAdjustFactorJointureList();
            if (isDataLoadingTerminated()) {
               loadDTOsInDataProvider();
            }
         }

         @Override
         public void onFailure(final Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
      
      if (getAppropriateViewMode() != ViewEnum.EDIT)
      {
        display.getButtonSaveSettings().setEnabled(false);
        display.getButtonCancelModifications().setEnabled(false);
      }

   }

   private void saveSettings() {
      new AbstractManagementRPCCall<ProjectPlanDTO>()
      {
         @Override
         protected void callService(final AsyncCallback<ProjectPlanDTO> pCb) {
            // textbox update
            currentProjectPlan.getEstimationComponentDetail().setValueAbaChgHomJour(
                  Float.parseFloat(display.getInputAbaqueChargeHJBox().getValue()));
            // ajustment update
				currentProjectPlan.setAdjustFactorJointureList(new ArrayList<AdjustFactorJointureDTO>(display
						.getDataFacteursAjustementProvider().getList()));
            //function point update
            Map<String, ComponentDTO> composantByKey = new HashMap<String, ComponentDTO>();
            for(final ComponentDTO composantDTO : display.getDataComposantsProvider().getList()) {
               composantByKey.put(composantDTO.getFunctionalId(), composantDTO);
            }
            currentProjectPlan.getEstimationComponentDetail().setValueSimpleGDI(
                  composantByKey.get(Constants.GDI).getComposantValeurSimple());
            currentProjectPlan.getEstimationComponentDetail().setValueMoyenGDI(
                  composantByKey.get(Constants.GDI).getComposantValeurMoyen());
            currentProjectPlan.getEstimationComponentDetail().setValueComplexGDI(
                  composantByKey.get(Constants.GDI).getComposantValeurComplexe());

            currentProjectPlan.getEstimationComponentDetail().setValueSimpleIN(
                  composantByKey.get(Constants.IN).getComposantValeurSimple());
            currentProjectPlan.getEstimationComponentDetail().setValueMoyenIN(
                  composantByKey.get(Constants.IN).getComposantValeurMoyen());
            currentProjectPlan.getEstimationComponentDetail().setValueComplexIN(
                  composantByKey.get(Constants.IN).getComposantValeurComplexe());

            currentProjectPlan.getEstimationComponentDetail().setValueSimpleINT(
                  composantByKey.get(Constants.INT).getComposantValeurSimple());
            currentProjectPlan.getEstimationComponentDetail().setValueMoyenINT(
                  composantByKey.get(Constants.INT).getComposantValeurMoyen());
            currentProjectPlan.getEstimationComponentDetail().setValueComplexINT(
                  composantByKey.get(Constants.INT).getComposantValeurComplexe());

            currentProjectPlan.getEstimationComponentDetail().setValueSimpleOUT(
                  composantByKey.get(Constants.OUT).getComposantValeurSimple());
            currentProjectPlan.getEstimationComponentDetail().setValueMoyenOUT(
                  composantByKey.get(Constants.OUT).getComposantValeurMoyen());
            currentProjectPlan.getEstimationComponentDetail().setValueComplexOUT(
                  composantByKey.get(Constants.OUT).getComposantValeurComplexe());

            currentProjectPlan.getEstimationComponentDetail().setValueSimpleGDE(
                  composantByKey.get(Constants.GDE).getComposantValeurSimple());
            currentProjectPlan.getEstimationComponentDetail().setValueMoyenGDE(
                  composantByKey.get(Constants.GDE).getComposantValeurMoyen());
            currentProjectPlan.getEstimationComponentDetail().setValueComplexGDE(
                  composantByKey.get(Constants.GDE).getComposantValeurComplexe());

            try {
               Common.PROJECT_PLAN_SERVICE.saveProjectPlanWithSettings(currentProjectPlan, pCb);
				} catch (final Exception ex) {
               ErrorManagement.displayErrorMessage(ex);
            }
         }

         @Override
         public void onSuccess(final ProjectPlanDTO pResult) {
				eventBus.fireEvent(new ProjectPlanReferentialModifiedEvent());
				InfoDialogBox info = new InfoDialogBox(projectRepositorySettingsMessages.settingsSaved(),
						InfoTypeEnum.OK);
            info.getDialogPanel().center();
            info.getDialogPanel().show();
            loadDatas();
         }

         @Override
         public void onFailure(final Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);

   }
   
   /**
    * Show the error pop up with not an inter message
    */
   private void showNotAnFloatMessage()
   {
      InfoDialogBox info = new InfoDialogBox(Common.GLOBAL.messageNotAFloat(), InfoTypeEnum.KO);
      info.getDialogPanel().center();
      info.getDialogPanel().show();
   }

   /**
    * @return true if data loading is terminated, false otherwise
    */
   private boolean isDataLoadingTerminated(){
      return listAdjustFactorJointureDTO != null && listPonderationAdjustWeightDTO != null;
   }
   
   /**
    * Load the DTOs in the data provider
    */
   private void loadDTOsInDataProvider() {
      Collections.sort(listAdjustFactorJointureDTO, comparatorAdjustFactorJunction);
      display.getDataFacteursAjustementProvider().setList(listAdjustFactorJointureDTO);
   }

   @Override
   public void go(final HasWidgets container)
   {
      container.clear();
      container.add(this.display.asWidget());
   }

   @Override
   public void loadDataOnSelectionTab() {
      //if the datas havent be loaded, we load it
      if(!isDataLoadingTerminated()) {
         loadDatas();
      }
   }

   @Override
   public IsWidget getDisplay()
   {
      return this.display.asWidget();
   }
   
   /**
    * Get the appropriate view mode from evaluation of project plan status and of the rights
    */
   private ViewEnum getAppropriateViewMode() {
      final AccessRight accessRight = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PROJECT_PLAN);
      if ( accessRight.equals(AccessRight.WRITE)) {
         return ViewEnum.EDIT;
      } else {
         return ViewEnum.READ;
      }
   }
}
