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

package org.novaforge.forge.tools.managementmodule.ui.client.presenter.projectplan;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.ManagementModuleEntryPoint;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.projectplan.ProjectPlanListView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.projectplan.ProjectPlanListViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;

import java.util.ArrayList;
import java.util.List;

public class ProjectPlanListPresenter implements Presenter {

   private final SimpleEventBus eventBus;
   private final ProjectPlanListView display;
   private List<ProjectPlanDTO> projectPlanList;
   private ProjectPlanDTO currentProjectPlan;
   private ValidateDialogBox validate;

   /**
    * @param projectPlanService
    * @param eventBus
    * @param display
    */
   public ProjectPlanListPresenter(SimpleEventBus eventBus) {
      super();
      this.eventBus = eventBus;
      this.display = new ProjectPlanListViewImpl();
      this.currentProjectPlan = null;
      bind();
   }

   /**
    * This method is the interface between the presenter and the view
    */
   private void bind() {

      display.getButtonHomeReturn().addClickHandler(new ClickHandler() {

         @Override
         public void onClick(ClickEvent event) {
            ManagementModuleEntryPoint.getHomePresenter().go(RootLayoutPanel.get());
         }
      });

      display.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
         @Override
         public void onSelectionChange(final SelectionChangeEvent pEvent) {
           ViewEnum viewMode = getAppropriateViewMode();
            currentProjectPlan = display.getSelectionModel().getSelectedObject();
            if (currentProjectPlan != null) {
               if (currentProjectPlan.getStatus().equals(ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT)) {
                  display.getButtonEditProjectPlan().setText(Common.getGlobal().buttonEdit());
                  if ( viewMode.equals(ViewEnum.EDIT) ) {
                    display.getButtonEditProjectPlan().setEnabled(true);
                  } else {
                    display.getButtonEditProjectPlan().setEnabled(false);
                  }
               } else {
                  if (viewMode.equals(ViewEnum.EDIT) || viewMode.equals(ViewEnum.READ)) {
                    display.getButtonEditProjectPlan().setEnabled(true);
                    display.getButtonEditProjectPlan().setText(Common.getGlobal().buttonVisualize());
                  }
               }
            } else {
               display.getButtonEditProjectPlan().setEnabled(false);
            }
         }
      });
      display.getButtonCreateProjectPlan().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            onClickCreateProjectPlan();
         }
      });
      display.getButtonEditProjectPlan().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            displayProjectPlanTab();
         }
      });
    
      if ( getAppropriateViewMode().equals(ViewEnum.EDIT)) {
        display.getButtonCreateProjectPlan().setEnabled(true);
        display.getButtonEditProjectPlan().setEnabled(false);
      }
      else
      {
        display.getButtonCreateProjectPlan().setEnabled(false);
        display.getButtonEditProjectPlan().setEnabled(false);
      }
   }

   protected void onClickCreateProjectPlan() {

      if (projectPlanList != null) {
         ProjectPlanDTO projectPlanDraft = null;
         for (ProjectPlanDTO ppdto : projectPlanList) {
            if (ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT.equals(ppdto.getStatus())) {
               projectPlanDraft = ppdto;
               break;
            }
         }
         if (projectPlanDraft != null) {
            validate = new ValidateDialogBox(Common.getProjectPlanMessages().validateRAZProjectPlan());
            validate.getValidate().addClickHandler(new ClickHandler() {
               @Override
               public void onClick(ClickEvent event) {
                  validate.getDialogPanel().hide();
                  createProjectPlan(SessionData.projectId);
               }
            });
            validate.getDialogPanel().center();
            validate.getDialogPanel().show();
         } else {
            createProjectPlan(SessionData.projectId);
         }
      }
   }

   /**
    * Display the project plan selected
    */
   private ProjectPlanTabPresenter displayProjectPlanTab()
   {
      ProjectPlanTabPresenter projectPlanTabPresenter;
      // if the selected project plan tab is the current validated one, we use
      // the presenter of the home to
      // avoid having to instance of the same presenter
      if (currentProjectPlan.getProjectPlanId().equals(ManagementModuleEntryPoint.getHomePresenter().getProjectPlan()
                                                                                 .getProjectPlanId()))
      {
         projectPlanTabPresenter = ManagementModuleEntryPoint.getHomePresenter().getProjectPlanTabPresenter();
      }
      else
      {
         projectPlanTabPresenter = new ProjectPlanTabPresenter(eventBus, currentProjectPlan);
      }
      projectPlanTabPresenter.go(RootLayoutPanel.get());
      return projectPlanTabPresenter;
   }

   private void createProjectPlan(final String projectId) {
      new AbstractManagementRPCCall<ProjectPlanDTO>()
      {
         @Override
         protected void callService(AsyncCallback<ProjectPlanDTO> pCb) {
            Common.PROJECT_PLAN_SERVICE.creeteProjectPlan(projectId, pCb);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(ProjectPlanDTO pResult) {
            currentProjectPlan = pResult;
            final ProjectPlanTabPresenter projectPlanTabPresenter = displayProjectPlanTab();
            // the last projectPlan changes on creating a project plan so we
            // have to update home
            // we put this presenter in home to avoid having two instances of a
            // ProjectPlanPresenter
            // on the same projectplan
            ManagementModuleEntryPoint.resetHomePresenter();
            ManagementModuleEntryPoint.getHomePresenter().setProjectPlanTabPresenter(projectPlanTabPresenter);
         }


      }.retry(0);
   }

   /**
    * @inheritDoc
    */
   @Override
   public void go(HasWidgets container)
   {
      container.clear();
      container.add(display.asWidget());
      fillProjectPlanList(SessionData.projectId);
   }

   private void fillProjectPlanList(final String idProject)
   {
      projectPlanList = new ArrayList<ProjectPlanDTO>();
      new AbstractManagementRPCCall<List<ProjectPlanDTO>>()
      {
         @Override
         protected void callService(AsyncCallback<List<ProjectPlanDTO>> pCb)
         {
            Common.PROJECT_PLAN_SERVICE.getProjectPlanList(idProject, pCb);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(List<ProjectPlanDTO> pResult)
         {
            if (pResult != null)
            {
               projectPlanList.addAll(pResult);
               refreshProjectPlanList(projectPlanList);
            }
         }

      }.retry(0);
   }

   private void refreshProjectPlanList(List<ProjectPlanDTO> pList)
   {
      display.getDataProjectPlanProvider().setList(pList);
      display.getProjectPlanCellTable().redraw();
      display.projectPlanListSortHandler();
   }

   @Override
   public IsWidget getDisplay() {
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
