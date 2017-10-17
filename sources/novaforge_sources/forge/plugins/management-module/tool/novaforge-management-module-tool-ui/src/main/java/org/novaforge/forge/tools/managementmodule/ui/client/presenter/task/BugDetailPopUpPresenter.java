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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.task;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.task.BugDetailPopUpView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.task.BugDetailPopUpViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.BugDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;

/**
 * Le presenter de la pop up qui présente le détail d'un bug
 */
public class BugDetailPopUpPresenter implements Presenter {

   /** The view managed by this presenter */
   private final BugDetailPopUpView view = new BugDetailPopUpViewImpl();
   /**
    * the bug to detail
    */
   private BugDTO bug;
   /**
    * RPC call service to get the details of the bug
    */
   AbstractManagementRPCCall<BugDTO> getBugDetail = new AbstractManagementRPCCall<BugDTO>()
   {

      @Override
      protected void callService(AsyncCallback<BugDTO> callBack)
      {
         Common.TASK_SERVICE.getBugDetail(bug.getBugTrackerId(), SessionData.projectId, SessionData.userLogin,
                                          callBack);
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
      }

      @Override
      public void onSuccess(BugDTO pResult)
      {
         fillTextBoxes(pResult);
      }
   };

   public BugDetailPopUpPresenter()
   {
      super();
      bind();
   }

   private void bind()
   {
      view.getCloseButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            view.hide();
         }
      });
   }

   @Override
   public void go(HasWidgets container)
   {
      //fill a first time no hide previous datas
      fillTextBoxes(bug);
      getBugDetail.retry(0);
      view.center();
   }

   @Override
   public IsWidget getDisplay()
   {
      return view;
   }

   /**
    * Fill the text boxes with a bug
    * @param bug the bug to use
    */
   private void fillTextBoxes(BugDTO bug)
   {
      view.getAssignedToTB().setText(bug.getAssignedTo());
      view.getBugTrackerIdTB().setText(bug.getBugTrackerId());
      view.getCategoryTB().setText(bug.getCategory());
      view.getPriorityTB().setText(bug.getPriority());
      view.getReporterTB().setText(bug.getReporter());
      view.getSeverityTB().setText(bug.getSeverity());
      view.getStatusTB().setText(bug.getStatus());
      view.getTitleTB().setText(bug.getTitle());
   }

   /**
    * Get the bug
    * @return the bug
    */
   public BugDTO getBug()
   {
      return bug;
   }

   /**
    * Set the bug
    * @param bug the bug to set
    */
   public void setBug(BugDTO bug)
   {
      this.bug = bug;
   }

}

