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
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.BugAssociatedToTaskEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.task.ChooseBugPopUpView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.task.ChooseBugPopUpViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.BugDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * The presenter for the pop up which permits to choose a bug
 */
public class ChooseBugPopUpPresenter implements Presenter {

   private final ChooseBugPopUpView view;
   /**
    * The parent bus listens by the presenter which call the popup
    */
   private final SimpleEventBus parentEventBus;
   /**
    * RPC call service to get the list of BugDTO
    */
   AbstractManagementRPCCall<Set<BugDTO>> getBugListAndFillDataProvider = new AbstractManagementRPCCall<Set<BugDTO>>()
   {

      @Override
      protected void callService(AsyncCallback<Set<BugDTO>> callBack)
      {
         Common.TASK_SERVICE.getBugList(SessionData.projectId, SessionData.userLogin, callBack);
      }

      @Override
      public void onFailure(Throwable pCaught)
      {
         ErrorManagement.displayErrorMessage(pCaught);
      }

      @Override
      public void onSuccess(Set<BugDTO> pResult)
      {
         final List<BugDTO> list = new ArrayList<BugDTO>(pResult);
         view.getDataProvider().setList(list);
         view.updateSortHandler();
      }
   };
   /** the task which will be link to the task */
   private TaskDTO task;
   /** the bug selected in cell table */
   private BugDTO  selectedBug;

   /**
    * Constructor
    * @param parentEventBus the local event bus
    */
   public ChooseBugPopUpPresenter(SimpleEventBus parentEventBus)
   {
      super();
      this.parentEventBus = parentEventBus;
      view = new ChooseBugPopUpViewImpl();
      bind();
   }

   private void bind()
   {
      //handler on cancel button
      view.getCancelButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            view.hide();
         }
      });
      //handler on selection model
      view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
      {
         @Override
         public void onSelectionChange(SelectionChangeEvent event)
         {
            view.getValidateButton().setEnabled(true);
            selectedBug = view.getSelectionModel().getSelectedObject();
         }
      });
      //handler on choose button
      view.getValidateButton().addClickHandler(new ClickHandler()
      {
         @Override
         public void onClick(ClickEvent event)
         {
            final BugAssociatedToTaskEvent bugAssociatedToTaskEvent = new BugAssociatedToTaskEvent();
            task.setBug(selectedBug);
            bugAssociatedToTaskEvent.setTask(task);
            parentEventBus.fireEvent(bugAssociatedToTaskEvent);
            view.hide();
         }
      });
   }

   @Override
   public void go(HasWidgets container)
   {
      view.show();
      getBugListAndFillDataProvider.retry(0);
   }

   @Override
   public IsWidget getDisplay()
   {
      return view;
   }

   /**
    * Set the task
    * @param task the task to set
    */
   public void setTask(TaskDTO task)
   {
      this.task = task;
   }
}
