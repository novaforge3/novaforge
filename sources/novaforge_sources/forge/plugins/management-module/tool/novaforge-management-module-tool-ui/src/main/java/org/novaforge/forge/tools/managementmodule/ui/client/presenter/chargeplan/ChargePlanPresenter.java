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

package org.novaforge.forge.tools.managementmodule.ui.client.presenter.chargeplan;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.chargeplan.ChargePlanView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.chargeplan.ChargePlanViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.chargeplan.ChargePlanMainDataDTO;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;

/**
 * @author FALSQUELLE-E
 */
public class ChargePlanPresenter implements TabPresenter {

   /**
    * The logger
    */
   private static final Logger LOGGER = Logger.getLogger(ChargePlanPresenter.class.getName());
   private ChargePlanView display;
   private ProjectPlanDTO currentProjectPlan;
   private ViewEnum       mode;
   private String         unitTimeName;
   private boolean firstLoad        = true;
   private boolean saveButtonEnable = true;

   public ChargePlanPresenter(final ProjectPlanDTO projectPlan, final ViewEnum mode)
   {
      super();
      this.currentProjectPlan = projectPlan;
      this.display = new ChargePlanViewImpl();
      setMode(mode);
      getChargePlanMainDataAndCreateView();
      bind();
   }

   public void setMode(ViewEnum mode)
   {
      this.mode = mode;
      manageRights();
      enableButtons();
   }

   /**
    * Get all needed data and create the view
    */
   private void getChargePlanMainDataAndCreateView()
   {
      new AbstractManagementRPCCall<ChargePlanMainDataDTO>()
      {
         @Override
         protected void callService(AsyncCallback<ChargePlanMainDataDTO> pCb)
         {
            Common.CHARGEPLAN_SERVICE.getChargePlanMainData(currentProjectPlan.getProjectPlanId(),
                                                            SessionData.projectId, pCb);
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }

         @Override
         public void onSuccess(ChargePlanMainDataDTO pResult)
         {
            if (pResult != null)
            {
               firstLoad = false;
               ChargePlanPresenter.this.display.setChargePlanMainDataDTO(pResult,  ChargePlanPresenter.this.mode == ViewEnum.READ);
            }
         }

      }.retry(0);
   }

   public void bind()
   {

      // BUTTONS EVENT
      display.getButtonSave().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {

            if (saveButtonEnable)
            {
               saveButtonEnable = false;
               showConfirmSave();
            }
         }
      });

      display.getButtonShowDiagram().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            showDiagram();
         }
      });

      display.getChargePlanSaveValidateDialogBox().getValidate().addClickHandler(new ClickHandler()
      {

         @Override
         public void onClick(ClickEvent event)
         {
            saveChargePlanMainData();
            display.getChargePlanSaveValidateDialogBox().getDialogPanel().hide();
         }
      });
   }

   /**
    * This method manages the rights for the iteration's views
    */
   private void manageRights()
   {
      final AccessRight accessRight = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_CHARGE_PLAN);
      if (accessRight.equals(AccessRight.READ))
      {
         this.mode = ViewEnum.READ;
      }
   }

   public void enableButtons()
   {
      display.getButtonShowDiagram().setEnabled(true);
      if (ViewEnum.READ.equals(mode))
      {
         display.getButtonSave().setEnabled(false);
      }
      else
      {
         display.getButtonSave().setEnabled(true);
      }
   }
   
   public void showConfirmSave()
   {
      display.getChargePlanSaveValidateDialogBox().getDialogPanel().center();
      display.getChargePlanSaveValidateDialogBox().getDialogPanel().show();
   }
   
   public void showDiagram(){

      Window.open(GWT.getModuleBaseURL() + Constants.REPORT_SERVLET_NAME + "?" + Constants.BIRT_REPORT_NAME_PARAMETER
                      + "=" + Constants.BIRT_CHARGE_PLAN_REPORT_NAME + "&&" + Constants.BIRT_PROJECT_PLAN_ID_PARAMETER
                      + "=" + currentProjectPlan.getProjectPlanId() + "&&" + Constants.BIRT_PROJECT_NAME_PARAMETER + "="
                      + currentProjectPlan.getProjectName() + "&&" + Constants.BIRT_UNIT_TIME_NAME_PARAMETER + "="
                      + unitTimeName + "&&" + Constants.BIRT_PROJECT_PLAN_VERSION_PARAMETER + "=" + currentProjectPlan
                                                                                                        .getVersion(),
                  "_blank", "");
   }

   private void saveChargePlanMainData()
   {
      LOGGER.log(Level.SEVERE, "-------------------------Dans la fonction saveChargePlanMainData");
      new AbstractManagementRPCCall<Void>()
      {
         @Override
         protected void callService(AsyncCallback<Void> pCb)
         {
            Common.CHARGEPLAN_SERVICE.saveChargePlan(ChargePlanPresenter.this.display.getChargePlanMainDataDTO()
                                                                                     .getListLines(),
                                                     currentProjectPlan.getProjectPlanId(), SessionData.projectId, pCb);
         }

         @Override
         public void onSuccess(Void pResult)
         {
            saveButtonEnable = true;
            ChargePlanPresenter.this.display.showChargePlanSaved();
         }

         @Override
         public void onFailure(Throwable caught)
         {
            saveButtonEnable = true;
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);

   }

   @Override
   public void go(HasWidgets container)
   {
      container.clear();
      container.add(this.display.asWidget());
   }

   @Override
   public void loadDataOnSelectionTab()
   {
      getUnitTimeName();
      if (firstLoad)
      {
         getChargePlanMainDataAndCreateView();
      }

   }

   /**
    * Get all needed data and create the view
    */
   private void getUnitTimeName()
   {
      new AbstractManagementRPCCall<String>()
      {
         @Override
         protected void callService(AsyncCallback<String> pCb)
         {
            Common.COMMON_SERVICE.getUnitTimeName(SessionData.projectId, pCb);
         }

         @Override
         public void onSuccess(String pResult)
         {
            if (pResult != null)
            {
               unitTimeName = pResult;
            }
         }

         @Override
         public void onFailure(Throwable caught)
         {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);
   }

   @Override
   public IsWidget getDisplay()
   {
      return this.display.asWidget();
   }
   
}
