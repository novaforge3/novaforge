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

package org.novaforge.forge.tools.managementmodule.ui.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.HomePresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeRightsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;

import java.util.Set;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 * 
 * @author VIGO-V
 */
public class ManagementModuleEntryPoint implements EntryPoint {

   private static final String PARAMETER_NAME_INSTANCEID = "instance_id";
   private static HomePresenter homePresenter;
   AbstractManagementRPCCall<Set<DisciplineDTO>>        getAllowedDisciplines          = new AbstractManagementRPCCall<Set<DisciplineDTO>>()
   {
      @Override
      protected void callService(final AsyncCallback<Set<DisciplineDTO>> asyncCallback)
      {
         Common.COMMON_SERVICE.getDisciplinesOfConnectedUser(SessionData.projectId, asyncCallback);
      }

      @Override
      public void onFailure(final Throwable caught)
      {
         ErrorManagement.displayErrorMessage(caught);
      }      @Override
      public void onSuccess(final Set<DisciplineDTO> setDisciplines)
      {
         SessionData.disciplinesOfConnectedUser = setDisciplines;
         // if all the rights are loaded
         // we can draw the panels
         if (rightsLoaded())
         {
            drawPanel();
         }
      }


   };
   AbstractManagementRPCCall<Set<ApplicativeRightsDTO>> getApplicativeRights           = new AbstractManagementRPCCall<Set<ApplicativeRightsDTO>>()
   {
      @Override
      protected void callService(final AsyncCallback<Set<ApplicativeRightsDTO>> asyncCallback)
      {
         Common.COMMON_SERVICE.getAbilitiesOfConnectedUser(SessionData.projectId, asyncCallback);
      }

      @Override
      public void onSuccess(final Set<ApplicativeRightsDTO> setApplicativeRights)
      {
         SessionData.applicativesRightsOfConnectedUser = setApplicativeRights;
         // if all the rights are loaded
         // we can draw the panels
         if (rightsLoaded())
         {
            drawPanel();
         }
      }

      @Override
      public void onFailure(final Throwable caught)
      {
         ErrorManagement.displayErrorMessage(caught);
      }
   };
   AbstractManagementRPCCall<String>                    checkAndRegisterUser           = new AbstractManagementRPCCall<String>()
   {
      @Override
      protected void callService(final AsyncCallback<String> asyncCallback)
      {
         Common.COMMON_SERVICE.checkAndRegisterUser(asyncCallback);
      }

      @Override
      public void onFailure(final Throwable caught)
      {
         ErrorManagement.displayErrorMessage(caught);
      }

      @Override
      public void onSuccess(final String pLogin)
      {
         SessionData.userLogin = pLogin;
         // if the user and the project
         // are valid and set, we look
         // for the rights
         if (initializationDone())
         {
            getAllowedDisciplines.retry(0);
            getApplicativeRights.retry(0);
         }
      }

   };
   AbstractManagementRPCCall<String>                    controlInstanceAndGetProjectId = new AbstractManagementRPCCall<String>()
   {
      @Override
      protected void callService(final AsyncCallback<String> asyncCallback)
      {
         Common.COMMON_SERVICE.controlInstanceAndGetProjectId(Window.Location.getParameter(PARAMETER_NAME_INSTANCEID),
                                                              asyncCallback);
      }

      @Override
      public void onSuccess(final String projectId)
      {
         SessionData.projectId = projectId;
         // if the user and the project
         // are valid and set, we look
         // for the rights
         if (initializationDone())
         {
            getAllowedDisciplines.retry(0);
            getApplicativeRights.retry(0);
         }
      }

      @Override
      public void onFailure(final Throwable caught)
      {
         ErrorManagement.displayErrorMessage(caught);
      }
   };

   /**
    * Get the homePresenter
    *
    * @return the homePresenter
    */
   public static HomePresenter getHomePresenter()
   {
      return homePresenter;
   }

   /**
    * This method reset the current home presenter, its needed when home
    * information can become obsolete, project plan validation, current
    * iteration closure...
    */
   public static void resetHomePresenter()
   {
      homePresenter = new HomePresenter(Common.GLOBAL_EVENT_BUS);
   }

   /**
    * This is the entry point method.
    */
   @Override
   public void onModuleLoad()
   {
      checkAndRegisterUser.retry(0);
      controlInstanceAndGetProjectId.retry(0);
   }

   /**
    * This methods returns true if the initialization is Ok
    *
    * @return true if Ok else otherwise
    */
   public synchronized boolean initializationDone()
   {
      // TODO check projectPlanId
      return (SessionData.projectId != null) && (SessionData.userLogin != null);
   }

   /**
    * This methods returns true if the rights are loaded
    *
    * @return true if Ok else otherwise
    */
   public synchronized boolean rightsLoaded()
   {
      return (SessionData.disciplinesOfConnectedUser != null) && (SessionData.applicativesRightsOfConnectedUser
                                                                      != null);
   }

   /**
    * Draw the first panel of the module
    */
   public void drawPanel()
   {
      homePresenter = new HomePresenter(Common.GLOBAL_EVENT_BUS);
      homePresenter.go(RootLayoutPanel.get());
   }

}
