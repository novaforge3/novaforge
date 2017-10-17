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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.projectmonitoringgraph;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.tools.managementmodule.ui.client.ManagementModuleEntryPoint;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.projectmonitoringgraph.ProjectMonitoringGraphView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.projectmonitoringgraph.ProjectMonitoringGraphViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;

import java.util.Date;
import java.util.List;

/**
 * @author FALSQUELLE-E
 * 
 */
public class ProjectMonitoringGraphPresenter implements TabPresenter {

   private ProjectMonitoringGraphView display;
   /**
    * Get user's disciplines by his access level
    */
   AbstractManagementRPCCall<List<DisciplineDTO>> getDisciplines = new AbstractManagementRPCCall<List<DisciplineDTO>>()
   {

      @Override
      protected void callService(AsyncCallback<List<DisciplineDTO>> pCb)
      {
         Common.REFERENTIAL_SERVICE.getAllPhareDTOList(pCb);
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
      }

      @Override
      public void onSuccess(List<DisciplineDTO> pResult)
      {
         display.getDisciplineLB().clear();
         DisciplineDTO allDTO = new DisciplineDTO();
         allDTO.setFunctionalId(Constants.ALL);
         allDTO.setLibelle(Common.MESSAGES_TASK.allDisciplines());
         display.getDisciplineLB().addItem(allDTO.getLibelle(), allDTO.getFunctionalId(), allDTO);

         if (pResult != null)
         {
            for (final DisciplineDTO discipline : pResult)
            {
               if (SessionData.disciplinesOfConnectedUser.contains(discipline))
               {
                  display.getDisciplineLB().addItem(discipline.getLibelle(), discipline.getFunctionalId(), discipline);
               }
            }
            display.getDisciplineLB().setSelectedIndex(0);
         }
      }

   };
   private List<LotDTO> listLots;
   /**
    * Get user's disciplines by his access level
    */
   AbstractManagementRPCCall<List<LotDTO>> getLots = new AbstractManagementRPCCall<List<LotDTO>>()
   {

      @Override
      protected void callService(AsyncCallback<List<LotDTO>> pCb)
      {
         Common.PROJECT_PLAN_SERVICE.getLotList(SessionData.currentValidatedProjectPlanId, pCb);
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
      }

      @Override
      public void onSuccess(List<LotDTO> pResult)
      {

         listLots = pResult;

         setFormFromDTO();

         display.getLotLB().clear();
         LotDTO allDTO = new LotDTO();
         allDTO.setLotId(0l);
         allDTO.setName(Common.GLOBAL.all());
         display.getLotLB().addItem(allDTO.getName(), "" + allDTO.getLotId(), allDTO);
         if (pResult != null)
         {
            for (LotDTO dto : pResult)
            {
               display.getLotLB().addItem(dto.getName(), "" + dto.getLotId(), dto);
            }
         }
         display.getLotLB().setSelectedIndex(0);

         setSubLotLBList();
      }

   };
   private ProjectPlanDTO projectPlan;
   /**
    * Get the currentValidated ProjectPlan
    */
   AbstractManagementRPCCall<ProjectPlanDTO> getProjectPlan = new AbstractManagementRPCCall<ProjectPlanDTO>()
   {

      @Override
      protected void callService(AsyncCallback<ProjectPlanDTO> pCb)
      {
         Common.PROJECT_PLAN_SERVICE.getProjectPlan(SessionData.currentValidatedProjectPlanId, pCb);
      }

      @Override
      public void onSuccess(ProjectPlanDTO pResult)
      {

         projectPlan = pResult;
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
      }
   };

   /**
    * @param eventBus
    */
   public ProjectMonitoringGraphPresenter()
   {
      super();
      display = new ProjectMonitoringGraphViewImpl();
      bind();
   }

   private void bind()
   {
      display.getButtonHomeReturn().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            ManagementModuleEntryPoint.getHomePresenter().go(RootLayoutPanel.get());
         }
      });
      /* buttons action */
      display.getButtonGenerateDiagram().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            generateDiagram();
         }
      });

      /* ListBox Selection actions*/
      display.getLotLB().addChangeHandler(new ChangeHandler()
      {

         @Override
         public void onChange(ChangeEvent event)
         {
            setSubLotLBList();
         }
      });
   }

   private void generateDiagram()
   {

      // effacer les precedents diagram.
      display.getDiagramsPanel().clear();
      display.getDiagramsPanel().clear();

      int frameHeight = display.getDiagramsPanel().getParent().getParent().getParent().getOffsetHeight() - 50;

      int frameWidth = display.getDiagramsPanel().getParent().getParent().getParent().getOffsetWidth() - 50;

      Frame firstFrame = new Frame(showStandardDeviationDiagram());
      firstFrame.setHeight(frameHeight + "px");
      firstFrame.setWidth(frameWidth + "px");

      display.getDiagramsPanel().add(firstFrame);

      Frame secondFrame = new Frame(showFocusingFactorDiagram());
      secondFrame.setHeight(frameHeight + "px");
      secondFrame.setWidth(frameWidth + "px");

      display.getDiagramsPanel().add(secondFrame);

      Frame thirdFrame = new Frame(showIdealWorkingPointDiagram());
      thirdFrame.setHeight(frameHeight + "px");
      thirdFrame.setWidth(frameWidth + "px");

      display.getDiagramsPanel().add(thirdFrame);
   }

   private void setSubLotLBList()
   {
      display.getSubLotLB().clear();
      LotDTO allDTO = new LotDTO();
      allDTO.setLotId(0l);
      allDTO.setName(Common.GLOBAL.all());
      display.getSubLotLB().addItem(allDTO.getName(), "" + allDTO.getLotId(), allDTO);
      if (display.getLotLB().getSelectedIndex() != 0)
      {
         for (LotDTO dto : display.getLotLB().getSelectedAssociatedObject().getChilds())
         {
            display.getSubLotLB().addItem(dto.getName(), "" + dto.getLotId(), dto);
         }
      }
      display.getSubLotLB().setSelectedIndex(0);
   }

   /**
    *
    * @return
    */
   private String showStandardDeviationDiagram()
   {

      return GWT.getModuleBaseURL() + Constants.REPORT_SERVLET_NAME + "?" + Constants.BIRT_DOWNLOAD_FILE_PARAMETER + "="
                 + Constants.FALSE + "&&" + Constants.BIRT_REPORT_NAME_PARAMETER + "="
                 + Constants.BIRT_STANDARDDEVIATION_REPORT_NAME + "&&" + Constants.BIRT_PROJECT_PLAN_ID_PARAMETER + "="
                 + SessionData.currentValidatedProjectPlanId + "&&" + Constants.BIRT_PROJECT_NAME_PARAMETER + "="
                 + projectPlan.getProjectName() + "&&" + Constants.BIRT_LOTID_PARAMETER + "=" + display.getLotLB()
                                                                                                       .getSelectedAssociatedObject()
                                                                                                       .getLotId()
                 + "&&" + Constants.BIRT_SUBLOTID_PARAMETER + "=" + display.getSubLotLB().getSelectedAssociatedObject()
                                                                           .getLotId() + "&&"
                 + Constants.BIRT_DISCIPLINE_FUNCTIONAL_ID_PARAMETER + "=" + display.getDisciplineLB()
                                                                                    .getSelectedAssociatedObject()
                                                                                    .getFunctionalId();
   }

   private String showFocusingFactorDiagram()
   {

      return GWT.getModuleBaseURL() + Constants.REPORT_SERVLET_NAME + "?" + Constants.BIRT_DOWNLOAD_FILE_PARAMETER + "="
                 + Constants.FALSE + "&&" + Constants.BIRT_REPORT_NAME_PARAMETER + "="
                 + Constants.BIRT_FOCUSINGFACTOR_REPORT_NAME + "&&" + Constants.BIRT_PROJECT_PLAN_ID_PARAMETER + "="
                 + SessionData.currentValidatedProjectPlanId + "&&" + Constants.BIRT_PROJECT_NAME_PARAMETER + "="
                 + projectPlan.getProjectName() + "&&" + Constants.BIRT_LOTID_PARAMETER + "=" + display.getLotLB()
                                                                                                       .getSelectedAssociatedObject()
                                                                                                       .getLotId()
                 + "&&" + Constants.BIRT_SUBLOTID_PARAMETER + "=" + display.getSubLotLB().getSelectedAssociatedObject()
                                                                           .getLotId() + "&&"
                 + Constants.BIRT_DISCIPLINE_FUNCTIONAL_ID_PARAMETER + "=" + display.getDisciplineLB()
                                                                                    .getSelectedAssociatedObject()
                                                                                    .getFunctionalId();
   }

   private String showIdealWorkingPointDiagram()
   {

      return GWT.getModuleBaseURL() + Constants.REPORT_SERVLET_NAME + "?" + Constants.BIRT_DOWNLOAD_FILE_PARAMETER + "="
                 + Constants.FALSE + "&&" + Constants.BIRT_REPORT_NAME_PARAMETER + "="
                 + Constants.BIRT_IDEALWORKINGPOINT_REPORT_NAME + "&&" + Constants.BIRT_PROJECT_PLAN_ID_PARAMETER + "="
                 + SessionData.currentValidatedProjectPlanId + "&&" + Constants.BIRT_PROJECT_NAME_PARAMETER + "="
                 + projectPlan.getProjectName() + "&&" + Constants.BIRT_LOTID_PARAMETER + "=" + display.getLotLB()
                                                                                                       .getSelectedAssociatedObject()
                                                                                                       .getLotId()
                 + "&&" + Constants.BIRT_SUBLOTID_PARAMETER + "=" + display.getSubLotLB().getSelectedAssociatedObject()
                                                                           .getLotId() + "&&"
                 + Constants.BIRT_DISCIPLINE_FUNCTIONAL_ID_PARAMETER + "=" + display.getDisciplineLB()
                                                                                    .getSelectedAssociatedObject()
                                                                                    .getFunctionalId();
   }

   @Override
   public void go(HasWidgets container)
   {
      container.clear();
      container.add(display.asWidget());
   }

   @Override
   public void loadDataOnSelectionTab()
   {

      final AccessRight accessRightSuiviGlobal = SessionData
                                                     .getAccessRight(ApplicativeFunction.FUNCTION_GLOBAL_MONITORING);
      if (!AccessRight.NONE.equals(accessRightSuiviGlobal))
      {
         getLots.retry(0);
         getProjectPlan.retry(0);

         /* get user's disciplines */
         getDisciplines.retry(0);
      }
      else
      {
         display.getDiagramsPanel().clear();
      }
   }

   @Override
   public IsWidget getDisplay()
   {
      return display.asWidget();
   }
   
   /**
    * initialise fied from the IterationDTO
    */
   private void setFormFromDTO() {
     if (listLots != null){

        Date firstDate = null;

        for (LotDTO lot : listLots){
           if (firstDate == null || firstDate.after(lot.getStartDate())){
              firstDate = lot.getStartDate();
           }
        }
        display.getStartDate().setText(Common.FR_DATE_FORMAT_ONLY_DAY.format(firstDate));
     }else{
        display.getStartDate().setText("");
     }
   }
}
