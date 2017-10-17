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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.novaforge.forge.tools.managementmodule.ui.client.ManagementModuleEntryPoint;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ProjectPlanReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CustomValueChangeHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.properties.ProjectRepositorySettingsMessage;
import org.novaforge.forge.tools.managementmodule.ui.client.view.administration.ProjectView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.administration.ProjectViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.CDOParametersDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentSimpleDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskCategoryDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.TransformationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.UnitTimeEnum;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.TextBox;

/**
 * @author qsivan
 */
public class ProjectPresenter implements Presenter {
   private final ProjectRepositorySettingsMessage projectRepositorySettingsMessages = (ProjectRepositorySettingsMessage) GWT
         .create(ProjectRepositorySettingsMessage.class);

   private final SimpleEventBus eventBus;
   private final ProjectView display;
   private final String idProject;
   private ProjectDTO currentProject;
   private int countCDOParameters;

   public ProjectPresenter(final SimpleEventBus eventBus) {
      super();
      this.display = new ProjectViewImpl();
      this.idProject = SessionData.projectId;
      this.eventBus = eventBus;
      bind();
   }

   @Override
   public void go(final HasWidgets container) {
      loadDatas();
      container.clear();
      container.add(this.display.asWidget());
   }

   /**
    * Load the datas
    */
   private void loadDatas() {
      // projects datas
      loadProjectDatas();
      // CDO
      loadParametersCDOofProject();
   }

   void loadProjectDatas()
   {
      new AbstractManagementRPCCall<ProjectDTO>()
      {
         @Override
         protected void callService(final AsyncCallback<ProjectDTO> pCb)
         {
            Common.COMMON_SERVICE.getProject(idProject, pCb);
         }

         @Override
         public void onFailure(final Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(final ProjectDTO pResult)
         {
            setCurrentProject(pResult);
            // transformation
            final TransformationDTO transformation = pResult.getTransformation();
            display.getInputNbrDeJourParAnBox().setText(String.valueOf(transformation.getNbJoursAn()));
            display.getInputNbrDeJourParMoisBox().setText(String.valueOf(transformation.getNbJoursMois()));
            display.getInputNbrDeJourParSemaineBox().setText(String.valueOf(transformation.getNbJoursSemaine()));
            display.getInputNbrHeuresParJourBox().setText(String.valueOf(transformation.getNbHeuresJour()));
            display.getInputNbJoursNonTravailBox().setText(String.valueOf(transformation.getNbJoursNonTravail()));
            // simple component estimation
            final EstimationComponentSimpleDTO simpleEstimationDTO = pResult.getEstimationComponentSimple();
            final List<ComponentDTO>           listComponentDTO    = buildComponentDTOList(simpleEstimationDTO);
            // if not first time, clear cache (textinputcell cache bug)
            if (!display.getComponentProvider().getList().isEmpty())
            {
               for (final ComponentDTO compDTO : display.getComponentProvider().getList())
               {
                  display.getComponentPercentTextInputCell().clearViewData(compDTO.getComposantLibelle());
               }
            }
            display.getComponentProvider().setList(listComponentDTO);
            // if not first time, clear cache (textinputcell cache bug)
            if (!display.getDataDisciplinesProvider().getList().isEmpty())
            {
               for (final ProjectDisciplineDTO projectDisciplineDTO : pResult.getProjectDisciplines())
               {
                  display.getDisciplinePercentTextInputCell().clearViewData(projectDisciplineDTO.getDisciplineDTO()
                                                                                                .getLibelle());
               }
            }
            display.getDataDisciplinesProvider().setList(pResult.getProjectDisciplines());
            display.getCategoriesList().redraw();

            display.getDataCategoriesProvider().setList(pResult.getTaskCategories());
            if (UnitTimeEnum.UNIT_TIME_WEEK.equals(pResult.getUnitTime()))
            {
               display.getUniteTempsSemaineRadioButton().setValue(true);
               display.getUniteTempsMoisRadioButton().setValue(false);
            }
            else
            {
               display.getUniteTempsMoisRadioButton().setValue(true);
               display.getUniteTempsSemaineRadioButton().setValue(false);
            }

            if (pResult.getLastVersionProjectPlan() > 1)
            {
               display.getUniteTempsMoisRadioButton().setEnabled(false);
               display.getUniteTempsSemaineRadioButton().setEnabled(false);
            }
            else
            {
               display.getUniteTempsMoisRadioButton().setEnabled(true);
               display.getUniteTempsSemaineRadioButton().setEnabled(true);
            }
         }

         /**
          * This method builds the list of component to display in cell table
          *
          * @param simpleEstimationDTO
          *           the DTO which permits to build the DTOs
          * @return the list
          */
         private List<ComponentDTO> buildComponentDTOList(final EstimationComponentSimpleDTO simpleEstimationDTO)
         {
            final List<ComponentDTO> listComponentDTO = new ArrayList<ComponentDTO>();
            ComponentDTO             componentDTO     = new ComponentDTO();
            componentDTO.setFunctionalId(Constants.GDI);
            componentDTO.setComposantLibelle(projectRepositorySettingsMessages.GDI());
            componentDTO.setSimplifiedPercentForEstimation(simpleEstimationDTO.getValueGDI());
            listComponentDTO.add(componentDTO);
            componentDTO = new ComponentDTO();
            componentDTO.setFunctionalId(Constants.IN);
            componentDTO.setComposantLibelle(projectRepositorySettingsMessages.IN());
            componentDTO.setSimplifiedPercentForEstimation(simpleEstimationDTO.getValueENT());
            listComponentDTO.add(componentDTO);
            componentDTO = new ComponentDTO();
            componentDTO.setFunctionalId(Constants.INT);
            componentDTO.setComposantLibelle(projectRepositorySettingsMessages.INT());
            componentDTO.setSimplifiedPercentForEstimation(simpleEstimationDTO.getValueINT());
            listComponentDTO.add(componentDTO);
            componentDTO = new ComponentDTO();
            componentDTO.setFunctionalId(Constants.OUT);
            componentDTO.setComposantLibelle(projectRepositorySettingsMessages.OUT());
            componentDTO.setSimplifiedPercentForEstimation(simpleEstimationDTO.getValueSOR());
            listComponentDTO.add(componentDTO);
            componentDTO = new ComponentDTO();
            componentDTO.setFunctionalId(Constants.GDE);
            componentDTO.setComposantLibelle(projectRepositorySettingsMessages.GDE());
            componentDTO.setSimplifiedPercentForEstimation(simpleEstimationDTO.getValueGDE());
            listComponentDTO.add(componentDTO);
            return listComponentDTO;
         }



      }.retry(0);

   }

   private void loadParametersCDOofProject()
   {
      new AbstractManagementRPCCall<List<CDOParametersDTO>>()
      {
         @Override
         protected void callService(AsyncCallback<List<CDOParametersDTO>> pCb)
         {
            Common.COMMON_SERVICE.loadListParametersCDO(idProject, pCb);
         }

         @Override
         public void onSuccess(List<CDOParametersDTO> pResult)
         {
            display.getCDODataProvider().getList().clear();
            setCountCDOParameters(0);
            if (pResult != null)
            {
               setCountCDOParameters(pResult.size());
               display.getCDODataProvider().setList(pResult);
            }
         }

         @Override
         public void onFailure(Throwable caught)
         {
            setCountCDOParameters(0);
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   /**
    * Set the current projectDTO
    *
    * @param pResult
    */
   public void setCurrentProject(ProjectDTO pResult)
   {
      currentProject = pResult;
   }

   @Override
   public IsWidget getDisplay()
   {
      return this.display.asWidget();
   }

   /**
    * ChangeHandler for the transformation fields
    *
    * @param textBox
    *           the text box to control
    * @return the change handler
    */
   private ValueChangeHandler<String> getNewValueChangeHandler(final TextBox textBox) {
      return new CustomValueChangeHandler<String>(textBox) {
        @Override
        public void onValueChange(String value, String prevValue)
        {
          if (!Common.isInt(value)) {
            InfoDialogBox info = new InfoDialogBox(Common.GLOBAL.messageNotAnInteger(), InfoTypeEnum.KO);
            info.getDialogPanel().center();
            textBox.setText(prevValue);
         }
        }
      };
   }

   public void bind() {
      display.getButtonCancelReferentielProjet().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            loadDatas();
         }
      });

      display.getButtonSaveReferentielProjet().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            validateActionOnAddSaveButton();
         }
      });

      display.getButtonImportRefScopeUnit().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            if (getCountCDOParameters() > 0) {
               importRefScopeUnit();
            } else {
               InfoDialogBox noRepo = new InfoDialogBox(projectRepositorySettingsMessages.noRepoInDataBase(),
                     InfoTypeEnum.WARNING);
               noRepo.getDialogPanel().center();
               noRepo.getDialogPanel().show();
            }
         }
      });

      display.getButtonHomeReturn().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            ManagementModuleEntryPoint.getHomePresenter().go(RootLayoutPanel.get());
         }
      });

      display.getTauxRepartitionColumn().setFieldUpdater(new FieldUpdater<ProjectDisciplineDTO, String>() {
         @Override
         public void update(int index, ProjectDisciplineDTO object, String value) {
            try {
               int disciplinePourcentage = Integer.valueOf(value);
               display.getDataDisciplinesProvider().getList().get(index)
                     .setDisciplinePourcentage(disciplinePourcentage);
               display.getDisciplinesList().redrawFooters();
            } catch (NumberFormatException e) {
               InfoDialogBox info = new InfoDialogBox(Common.GLOBAL.messageNotAnInteger(), InfoTypeEnum.KO);
               info.getDialogPanel().center();
               // clear cache to pass out edittextcell cache bug
               display.getDisciplinePercentTextInputCell().clearViewData(
                     object.getDisciplineDTO().getLibelle());
               display.getDisciplinesList().redraw();
            }
         }
      });

      /**
       * CDOParameters CELLTABLE Columns
       */
      display.getHostCDOColumn().setFieldUpdater(new FieldUpdater<CDOParametersDTO, String>() {
         @Override
         public void update(int index, CDOParametersDTO object, String value) {
            display.getCDODataProvider().getList().get(index).setHost(value);
         }
      });

      display.getPortCDOColumn().setFieldUpdater(new FieldUpdater<CDOParametersDTO, String>() {
         @Override
         public void update(int index, CDOParametersDTO object, String value) {
            try {
               int port = Integer.valueOf(value);
               display.getCDODataProvider().getList().get(index).setPort(port);
            } catch (NumberFormatException e) {
               InfoDialogBox info = new InfoDialogBox(Common.GLOBAL.messageNotAnInteger(), InfoTypeEnum.KO);
               info.getDialogPanel().center();
            }
         }
      });

      display.getProjectCDOColumn().setFieldUpdater(new FieldUpdater<CDOParametersDTO, String>() {
         @Override
         public void update(int index, CDOParametersDTO object, String value) {
            display.getCDODataProvider().getList().get(index).setProjetCdo(value);
         }
      });

      display.getSystemGraalCDOColumn().setFieldUpdater(new FieldUpdater<CDOParametersDTO, String>() {
         @Override
         public void update(int index, CDOParametersDTO object, String value) {
            display.getCDODataProvider().getList().get(index).setSystemGraal(value);
         }
      });

      display.getRepositoryCDOColumn().setFieldUpdater(new FieldUpdater<CDOParametersDTO, String>() {
         @Override
         public void update(int index, CDOParametersDTO object, String value) {
            display.getCDODataProvider().getList().get(index).setRepository(value);
         }
      });

      display.getCronExpressionCDOColumn().setFieldUpdater(new FieldUpdater<CDOParametersDTO, String>() {
         @Override
         public void update(int index, CDOParametersDTO object, String value) {
            display.getCDODataProvider().getList().get(index).setCronExpression(value);
         }
      });

      display.getComponentRepartitionColumn().setFieldUpdater(new FieldUpdater<ComponentDTO, String>() {
         @Override
         public void update(int index, ComponentDTO object, String value) {
            try {
               if (value == null || value.trim().equals("")) {
                  displayErrorAndResetInput(object);
               } else {
                  String correctedValue = value.trim().replace(",", ".");
                  float correctedFloatValue = Float.valueOf(correctedValue);
                  if (correctedFloatValue <= 0F) {
                     displayErrorAndResetInput(object);
                  } else {
                     display.getComponentProvider().getList().get(index)
                           .setSimplifiedPercentForEstimation(correctedFloatValue);
                  }
               }
               display.getComponentCellTable().redrawFooters();
            } catch (NumberFormatException e) {
               displayErrorAndResetInput(object);
            }
         }

         /**
          * This method display an error on input of repartition column and
          * reset it
          *
          * @param object
          *           l'objet
          */
         private void displayErrorAndResetInput(ComponentDTO object) {
            InfoDialogBox info = new InfoDialogBox(projectRepositorySettingsMessages
                  .needToBeNotEmptyOrZeroInt(), InfoTypeEnum.KO);
            info.getDialogPanel().center();
            // clear cache to pass out edittextcell cache bug
            display.getComponentPercentTextInputCell().clearViewData(object.getComposantLibelle());
            display.getComponentCellTable().redraw();
         }
      });

      display.getInputNbrDeJourParAnBox().addValueChangeHandler(
            getNewValueChangeHandler(display.getInputNbrDeJourParAnBox()));
      display.getInputNbrDeJourParMoisBox().addValueChangeHandler(
            getNewValueChangeHandler(display.getInputNbrDeJourParMoisBox()));
      display.getInputNbrDeJourParSemaineBox().addValueChangeHandler(
            getNewValueChangeHandler(display.getInputNbrDeJourParSemaineBox()));
      display.getInputNbrHeuresParJourBox().addValueChangeHandler(
            getNewValueChangeHandler(display.getInputNbrHeuresParJourBox()));
      display.getInputNbJoursNonTravailBox().addValueChangeHandler(
            getNewValueChangeHandler(display.getInputNbJoursNonTravailBox()));

      display.getAjouterCategorieBouton().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            if (!display.getInputAjouterCategorieBox().getText().trim().equals("")) {
               TaskCategoryDTO categorie = new TaskCategoryDTO();
               categorie.setName(display.getInputAjouterCategorieBox().getText());
               display.getInputAjouterCategorieBox().setText(null);
               if (!display.getDataCategoriesProvider().getList().contains(categorie)) {
                  display.getDataCategoriesProvider().getList().add(categorie);
               }
            }
         }
      });

      display.getAjouterCDOBouton().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {

            CDOParametersDTO param = new CDOParametersDTO();

            List<CDOParametersDTO> list = display.getCDODataProvider().getList();
            long newId = 0;
            if (list.size() > 0) {
               long lastElement = list.get(display.getCDODataProvider().getList().size() - 1)
                     .getcDOParametersID();
               if (lastElement <= 0) {
                  newId = lastElement - 1;
               }
            }
            param.setcDOParametersID(newId);
            initCDOParametersDTOAllFieldButId(param);

            display.getCDODataProvider().getList().add(param);
         }
      });

      display.getUniteTempsSemaineRadioButton().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            // display.getUniteTempsSe
            InfoDialogBox info = new InfoDialogBox(projectRepositorySettingsMessages.changeUnitTimeMessage(),
                  InfoTypeEnum.WARNING);
            info.getDialogPanel().center();
            info.getDialogPanel().show();
         }
      });

      display.getUniteTempsMoisRadioButton().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            // display.getUniteTempsSe
            InfoDialogBox info = new InfoDialogBox(projectRepositorySettingsMessages.changeUnitTimeMessage(),
                  InfoTypeEnum.WARNING);
            info.getDialogPanel().center();
            info.getDialogPanel().show();
         }
      });
      
      if (getAppropriateViewMode() != ViewEnum.EDIT)
      {
        display.getButtonSaveReferentielProjet().setEnabled(false);
        display.getButtonCancelReferentielProjet().setEnabled(false);
        display.getButtonImportRefScopeUnit().setEnabled(false);
      }

   }

   /**
    * Verification that the total amount on disciplines = 100
    */
   void validateActionOnAddSaveButton() {
      final int totalValueDisciplines = display.getTotalValueDisciplines();
      final float totalValueComponentPercent = display.getTotalValueComponentPercent();
      if (totalValueDisciplines != 100) {
         InfoDialogBox info = new InfoDialogBox(
               projectRepositorySettingsMessages.pourcentageDisciplinesFalse() + " ("
                     + projectRepositorySettingsMessages.libelledisciplineTotal() + " "
                     + totalValueDisciplines + ")", InfoTypeEnum.KO);
         info.getDialogPanel().center();
      } else if (totalValueComponentPercent != 100F) {
         InfoDialogBox info = new InfoDialogBox(
               projectRepositorySettingsMessages.percentageSimpleComponentFalse() + " ("
                     + projectRepositorySettingsMessages.libelledisciplineTotal() + " "
                     + totalValueComponentPercent + ")", InfoTypeEnum.KO);
         info.getDialogPanel().center();
      } else {
         saveReferentielProjet();
      }
   }

   void saveReferentielProjet() {
      new AbstractManagementRPCCall<Boolean>()
      {
         @Override
         protected void callService(AsyncCallback<Boolean> pCb) {
            ProjectDTO projetDTO = beforeSave();
            List<CDOParametersDTO> list = new ArrayList<CDOParametersDTO>(display.getCDODataProvider()
                  .getList());
            Common.COMMON_SERVICE.updateProject(projetDTO, list, pCb);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            InfoDialogBox info = new InfoDialogBox(projectRepositorySettingsMessages.saveProjectParametersFailed(),
                                                   InfoTypeEnum.KO);
            info.getDialogPanel().center();
            info.getDialogPanel().show();
            loadParametersCDOofProject();
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(Boolean pResult) {
            eventBus.fireEvent(new ProjectPlanReferentialModifiedEvent());
            InfoDialogBox info = new InfoDialogBox(projectRepositorySettingsMessages.saveProjectParametersSuccessfull(),
                  InfoTypeEnum.OK);
            info.getDialogPanel().center();
            info.getDialogPanel().show();
            loadParametersCDOofProject();
            loadDatas();
         }


      }.retry(0);
   }

   private ProjectDTO beforeSave() {
      final ProjectDTO projectDTO = getCurrentProjet();
      // function point update
      final Map<String, ComponentDTO> composantByKey = new HashMap<String, ComponentDTO>();
      for (final ComponentDTO composantDTO : display.getComponentProvider().getList()) {
         composantByKey.put(composantDTO.getFunctionalId(), composantDTO);
      }
      projectDTO.getEstimationComponentSimple().setValueENT(
            composantByKey.get(Constants.IN).getSimplifiedPercentForEstimation());
      projectDTO.getEstimationComponentSimple().setValueGDE(
            composantByKey.get(Constants.GDE).getSimplifiedPercentForEstimation());
      projectDTO.getEstimationComponentSimple().setValueGDI(
            composantByKey.get(Constants.GDI).getSimplifiedPercentForEstimation());
      projectDTO.getEstimationComponentSimple().setValueINT(
            composantByKey.get(Constants.INT).getSimplifiedPercentForEstimation());
      projectDTO.getEstimationComponentSimple().setValueSOR(
            composantByKey.get(Constants.OUT).getSimplifiedPercentForEstimation());
      projectDTO.getTransformation().setNbHeuresJour(
            Integer.parseInt(display.getInputNbrHeuresParJourBox().getText()));
      projectDTO.getTransformation().setNbJoursAn(
            Integer.parseInt(display.getInputNbrDeJourParAnBox().getText()));
      projectDTO.getTransformation().setNbJoursMois(
            Integer.parseInt(display.getInputNbrDeJourParMoisBox().getText()));
      projectDTO.getTransformation().setNbJoursNonTravail(
            Integer.parseInt(display.getInputNbJoursNonTravailBox().getText()));
      projectDTO.getTransformation().setNbJoursSemaine(
            Integer.parseInt(display.getInputNbrDeJourParSemaineBox().getText()));
      if (display.getUniteTempsMoisRadioButton().getValue()) {
         projectDTO.setUnitTime(UnitTimeEnum.UNIT_TIME_MONTH);
      } else {
         projectDTO.setUnitTime(UnitTimeEnum.UNIT_TIME_WEEK);
      }
      List<ProjectDisciplineDTO> listDisciplineDTOihm = display.getDisciplinesList().getVisibleItems();
      List<TaskCategoryDTO> listTaskCategoryDTOihm = display.getCategoriesList().getVisibleItems();
      projectDTO.setDisciplines(new ArrayList<ProjectDisciplineDTO>(listDisciplineDTOihm));
      projectDTO.setTaskCategories(new ArrayList<TaskCategoryDTO>(listTaskCategoryDTOihm));

      return projectDTO;
   }

   /**
    * return the current projectDTO
    *
    * @return
    */
   public ProjectDTO getCurrentProjet() {
      return currentProject;
   }

   private void importRefScopeUnit() {
      new AbstractManagementRPCCall<Boolean>()
      {
         @Override
         protected void callService(AsyncCallback<Boolean> pCb) {
            ProjectDTO project = getCurrentProjet();
            Common.COMMON_SERVICE.updateFromCDORefScopeUnit(project, pCb);
         }

         @Override
         public void onSuccess(Boolean pResult) {
            if (pResult != null) {
               boolean resultatUpdate = pResult.booleanValue();
               if (!resultatUpdate) {
                  InfoDialogBox info = new InfoDialogBox(
                        projectRepositorySettingsMessages.connectCDOProblem(), InfoTypeEnum.KO);
                  info.getDialogPanel().center();
                  info.getDialogPanel().show();
               } else {
                  InfoDialogBox info = new InfoDialogBox(projectRepositorySettingsMessages.connectCDOOk(),
                        InfoTypeEnum.OK);
                  info.getDialogPanel().center();
                  info.getDialogPanel().show();
               }
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            InfoDialogBox info = new InfoDialogBox(projectRepositorySettingsMessages.connectCDOProblem(),
                  InfoTypeEnum.KO);
            info.getDialogPanel().center();
            info.getDialogPanel().show();
         }
      }.retry(0);
   }

   /**
    * init all fields of a new CDOParametersDTO but id's field
    * 
    * @param dto
    */
   private void initCDOParametersDTOAllFieldButId(CDOParametersDTO dto) {
      dto.setCronExpression("");
      dto.setHost("");
      dto.setPort(Constants.CDO_DEFAULT_PORT);
      dto.setProjetCdo("");
      dto.setRepository("");
      dto.setSystemGraal("");
   }
   
   private int getCountCDOParameters() {
      return countCDOParameters;
   }

   private void setCountCDOParameters(int size)
   {
      countCDOParameters = size;
   }
   
   /**
    * Get the appropriate view mode from evaluation of project plan status and of the rights
    */
   private ViewEnum getAppropriateViewMode() {
      final AccessRight accessRight = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_ADMINISTRATION);
      if ( accessRight.equals(AccessRight.WRITE)) {
         return ViewEnum.EDIT;
      } else {
         return ViewEnum.READ;
      }
   }
}
