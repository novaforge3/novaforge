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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.burndowniteration;

import java.util.Date;
import java.util.List;

import org.novaforge.forge.tools.managementmodule.ui.client.ManagementModuleEntryPoint;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowIterationListEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.burndowniteration.BurnDownIterationView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.burndowniteration.BurnDownIterationViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;

/**
 * @author FALSQUELLE-E
 * 
 */
public class BurnDownPresenter implements TabPresenter {

   private final SimpleEventBus globalEventBus;
   private BurnDownIterationView display;
   AbstractManagementRPCCall<List<DisciplineDTO>> getAllDisciplines = new AbstractManagementRPCCall<List<DisciplineDTO>>()
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
         if (pResult != null)
         {

            for (final DisciplineDTO discipline : pResult)
            {

               if (SessionData.disciplinesOfConnectedUser.contains(discipline))
               {
                  display.getDisciplineLB().addItem(discipline.getLibelle(), discipline.getFunctionalId(), discipline);
               }
            }
         }
      }

   };
   private List<IterationDTO> iterations;
   /**
    * Get the next iterations to be prepared
    */
   AbstractManagementRPCCall<List<IterationDTO>> getListIteration = new AbstractManagementRPCCall<List<IterationDTO>>()
   {

      @Override
      protected void callService(AsyncCallback<List<IterationDTO>> pCb)
      {
         Common.ITERATION_SERVICE.getIterationList(SessionData.currentValidatedProjectPlanId, pCb);
      }

      @Override
      public void onSuccess(List<IterationDTO> pResult)
      {
         if (pResult != null)
         {
            iterations = pResult;

            int selected = display.getIterationLB().getSelectedIndex();

            display.getIterationLB().clear();
            final IterationDTO allDTO = new IterationDTO();
            allDTO.setIterationId(0);
            allDTO.setLabel(Common.MESSAGES_TASK.allDisciplines());
            display.getIterationLB().addItem(allDTO.getLabel(), "" + allDTO.getIterationId(), allDTO);
            for (IterationDTO dto : pResult)
            {
              if (!dto.getStartDate().after(new Date()))
              {
                 display.getIterationLB().addItem(dto.getLabel(), "" + dto.getIterationId(), dto);
              }
            }
            if (selected == -1 || display.getIterationLB().getItemCount() <= selected)
            {
               selected = 0;
            }
            display.getIterationLB().setSelectedIndex(selected);

            setFormFromDTO();
         }
         else
         {
            final InfoDialogBox dialogBox = new InfoDialogBox(Common.MESSAGES_BACKLOG.noMorePreparationMessage(),
                                                              InfoTypeEnum.WARNING);
            dialogBox.getDialogPanel().center();
            dialogBox.getDialogPanel().show();
            globalEventBus.fireEvent(new ShowIterationListEvent());
         }
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
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
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
      }

      @Override
      public void onSuccess(ProjectPlanDTO pResult)
      {

         projectPlan = pResult;
      }

   };
   private int frameWidth;
   private int frameHeight;
   /**
    * Check if it is allowed to preparate a new iterations. Only 2 not finished
    * current iterations are allowed.
    */
   AbstractManagementRPCCall<Integer> showTaskByCategoriesDiagram = new AbstractManagementRPCCall<Integer>()
   {

      @Override
      protected void callService(AsyncCallback<Integer> pCb)
      {
         Common.TASK_SERVICE.getCategoriesCountForAnIterationAndADiscipline(display.getIterationLB()
                                                                                   .getSelectedAssociatedObject()
                                                                                   .getIterationId(),
                                                                            display.getDisciplineLB()
                                                                                   .getSelectedAssociatedObject()
                                                                                   .getFunctionalId(), pCb);
      }

      @Override
      public void onSuccess(Integer pResult)
      {
         if (pResult != null)
         {
            if (pResult.intValue() > 1)
            {
               Frame thirdFrame = new Frame(showTaskCategoriesPieDiagram());
               thirdFrame.setHeight(frameHeight + "px");
               thirdFrame.setWidth(frameWidth + "px");

               display.getDiagramsPanel().add(thirdFrame);
            }
         }
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
   public BurnDownPresenter(SimpleEventBus eventBus)
   {
      super();
      this.globalEventBus = eventBus;
      display = new BurnDownIterationViewImpl();
      bind();
      getListIteration.retry(0);
      getProjectPlan.retry(0);
      fillDisciplinesListBox();
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

      /* listBox action */
      display.getIterationLB().addChangeHandler(new ChangeHandler()
      {

         @Override
         public void onChange(ChangeEvent event)
         {
            setFormFromDTO();
         }
      });
   }

   /**
    * Fill the disciplines list box
    */
   private void fillDisciplinesListBox()
   {
      display.getDisciplineLB().clear();
      final DisciplineDTO allDTO = new DisciplineDTO();
      allDTO.setFunctionalId(Constants.ALL);
      allDTO.setLibelle(Common.MESSAGES_TASK.allDisciplines());
      display.getDisciplineLB().addItem(allDTO.getLibelle(), allDTO.getFunctionalId(), allDTO);

      getAllDisciplines.retry(0);
   }

   private void generateDiagram()
   {

      // effacer les precedents diagram.
      display.getDiagramsPanel().clear();

      frameHeight = display.getDiagramsPanel().getParent().getParent().getOffsetHeight() - 50;
      frameWidth = display.getDiagramsPanel().getParent().getParent().getOffsetWidth() - 50;

      Frame firstFrame = new Frame(showBurnDownChartPoints());
      firstFrame.setHeight(frameHeight + "px");
      firstFrame.setWidth(frameWidth + "px");

      display.getDiagramsPanel().add(firstFrame);

      Frame secondFrame = new Frame(showBurnDownChartTaskNumber());
      secondFrame.setHeight(frameHeight + "px");
      secondFrame.setWidth(frameWidth + "px");

      display.getDiagramsPanel().add(secondFrame);

      if (!projectMode())
      {
         showTaskByCategoriesDiagram.retry(0);
      }
   }

   /**
    * initialise fied from the IterationDTO
    */
   private void setFormFromDTO()
   {
      if (iterations != null)
      {
         display.getIterationName().setText(display.getIterationLB().getSelectedAssociatedObject().getLabel());
         if (display.getIterationLB().getSelectedAssociatedObject().getIterationId() > 0)
         {
            display.getStartDate().setText(Common.FR_DATE_FORMAT_ONLY_DAY.format(display.getIterationLB()
                                                                                        .getSelectedAssociatedObject()
                                                                                        .getStartDate()));
            display.getEndDate().setText(Common.FR_DATE_FORMAT_ONLY_DAY.format(display.getIterationLB()
                                                                                      .getSelectedAssociatedObject()
                                                                                      .getEndDate()));
            display.getLot().setText(display.getIterationLB().getSelectedAssociatedObject().getLot().getName());
            if (display.getIterationLB().getSelectedAssociatedObject().getLot().getParentLotName() != null)
            {
               display.getParentLot().setText(display.getIterationLB().getSelectedAssociatedObject().getLot()
                                                     .getParentLotName());
            }
         }
         else
         {
            display.getParentLot().setText("");
            display.getStartDate().setText("");
            display.getEndDate().setText("");
            display.getLot().setText("");
         }
      }
   }

   /**
    *
    * @return
    */
   private String showBurnDownChartPoints()
   {
      StringBuilder sb = new StringBuilder(GWT.getModuleBaseURL());
      sb.append(Constants.REPORT_SERVLET_NAME);
      sb.append("?");
      sb.append(Constants.BIRT_DOWNLOAD_FILE_PARAMETER);
      sb.append("=" + Constants.FALSE + "&&");
      sb.append(Constants.BIRT_REPORT_NAME_PARAMETER);
      sb.append("=" + Constants.BIRT_BURNDOWNCHART_REPORT_NAME + "&&");
      sb.append(Constants.BIRT_PROJECT_NAME_PARAMETER);
      sb.append("=").append(projectPlan.getProjectName()).append("&&");
      if (projectMode())
      {
         sb.append(Constants.BIRT_PROJECT_PLAN_ID_PARAMETER);
         sb.append("=").append(SessionData.currentValidatedProjectPlanId).append("&&");
      }
      sb.append(Constants.BIRT_ITERATIONID_PARAMETER);
      sb.append("=").append(display.getIterationLB().getSelectedAssociatedObject().getIterationId()).append("&&");
      sb.append(Constants.BIRT_DISCIPLINE_FUNCTIONAL_ID_PARAMETER);
      sb.append("=").append(display.getDisciplineLB().getSelectedAssociatedObject().getFunctionalId()).append("&&");
      sb.append(Constants.BIRT_BURNDOWNCHART_TASKUNIT_PARAMETER);
      sb.append("=" + Constants.FALSE + "&&");
      return sb.toString();
   }

   private String showBurnDownChartTaskNumber()
   {
      StringBuilder sb = new StringBuilder(GWT.getModuleBaseURL());
      sb.append(Constants.REPORT_SERVLET_NAME);
      sb.append("?");
      sb.append(Constants.BIRT_DOWNLOAD_FILE_PARAMETER);
      sb.append("=" + Constants.FALSE + "&&");
      sb.append(Constants.BIRT_REPORT_NAME_PARAMETER);
      sb.append("=" + Constants.BIRT_BURNDOWNCHART_REPORT_NAME + "&&");
      sb.append(Constants.BIRT_PROJECT_NAME_PARAMETER);
      sb.append("=").append(projectPlan.getProjectName()).append("&&");
      if (projectMode())
      {
         sb.append(Constants.BIRT_PROJECT_PLAN_ID_PARAMETER);
         sb.append("=").append(SessionData.currentValidatedProjectPlanId).append("&&");
      }
      sb.append(Constants.BIRT_ITERATIONID_PARAMETER);
      sb.append("=").append(display.getIterationLB().getSelectedAssociatedObject().getIterationId()).append("&&");
      sb.append(Constants.BIRT_DISCIPLINE_FUNCTIONAL_ID_PARAMETER);
      sb.append("=").append(display.getDisciplineLB().getSelectedAssociatedObject().getFunctionalId()).append("&&");
      sb.append(Constants.BIRT_BURNDOWNCHART_TASKUNIT_PARAMETER);
      sb.append("=" + Constants.TRUE);
      return sb.toString();
   }

   private boolean projectMode()
   {
      return display.getIterationLB().getSelectedAssociatedObject() != null
                 && display.getIterationLB().getSelectedAssociatedObject().getIterationId() == 0;
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
      manageRight();
   }

   private void manageRight()
   {
      final AccessRight accessRightIteration = SessionData
                                                   .getAccessRight(ApplicativeFunction.FUNCTION_ITERATION_MONITORING);
      final AccessRight accessRightGlobal = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_GLOBAL_MONITORING);
      if (accessRightIteration.equals(AccessRight.NONE) && accessRightGlobal.equals(AccessRight.NONE))
      {
         final InfoDialogBox dialogBox = new InfoDialogBox(Common.GLOBAL.insufficientRights(), InfoTypeEnum.KO);
         dialogBox.getDialogPanel().center();
         dialogBox.getDialogPanel().show();
         globalEventBus.fireEvent(new ShowIterationListEvent());
      }
   }

   @Override
   public IsWidget getDisplay()
   {
      return display.asWidget();
   }

   private String showTaskCategoriesPieDiagram()
   {

      return GWT.getModuleBaseURL() + Constants.REPORT_SERVLET_NAME + "?" + Constants.BIRT_DOWNLOAD_FILE_PARAMETER + "="
                 + Constants.FALSE + "&&" + Constants.BIRT_REPORT_NAME_PARAMETER + "="
                 + Constants.BIRT_TASKCATEGORIESPIE_REPORT_NAME + "&&" + Constants.BIRT_PROJECT_NAME_PARAMETER + "="
                 + projectPlan.getProjectName() + "&&" + Constants.BIRT_ITERATIONID_PARAMETER + "=" + display
                                                                                                          .getIterationLB()
                                                                                                          .getSelectedAssociatedObject()
                                                                                                          .getIterationId()
                 + "&&" + Constants.BIRT_DISCIPLINE_FUNCTIONAL_ID_PARAMETER + "=" + display.getDisciplineLB()
                                                                                           .getSelectedAssociatedObject()
                                                                                           .getFunctionalId();
   }
}
