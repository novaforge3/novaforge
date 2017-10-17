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

package org.novaforge.forge.tools.managementmodule.ui.client.presenter.iteration;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.IterationReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowIterationEditViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.iteration.IterationListView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.iteration.IterationListViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class IterationListPresenter implements TabPresenter {
   private static final Comparator<IterationDTO> ITERATION_NUMBER_COMPARATOR = new Comparator<IterationDTO>()
   {
      @Override
      public int compare(IterationDTO o1, IterationDTO o2)
      {
         return ((Integer) o1.getNumIteration()).compareTo(o2.getNumIteration());
      }
   };
  private final SimpleEventBus eventBus;
	private final IterationListView view;
	private List<IterationDTO>	iterationsList;
	private ProjectPlanDTO currentProjectPlan;
	private IterationDTO selectedIteration;

   public IterationListPresenter(final SimpleEventBus eventBus, final ProjectPlanDTO projectPlan) {
      super();
      this.eventBus = eventBus;
		this.view = new IterationListViewImpl();
		this.currentProjectPlan = projectPlan;
		bind();
		manageRights();
	}

	/**
	 * This method is the interface between the presenter and the view
	 */
	private void bind()
  {
    //handler on selection model
      view.getSelectionModel().addSelectionChangeHandler(
            new SelectionChangeEvent.Handler() {
         @Override
         public void onSelectionChange(SelectionChangeEvent event) {
            selectedIteration = view.getSelectionModel().getSelectedObject();
            manageButtonsState();
         }

      });
     view.getCreationButton().addClickHandler(new ClickHandler()
     {
       @Override
			public void onClick(ClickEvent event) {
			   eventBus.fireEvent(new ShowIterationEditViewEvent(null));
			}
		});
     view.getModificationButton().addClickHandler(new ClickHandler()
     {
         @Override
         public void onClick(ClickEvent event) {
            eventBus.fireEvent(new ShowIterationEditViewEvent(selectedIteration));
         }
      });
	   view.getDeleteButton().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            view.getValidateDialogBox().getDialogPanel().center();
         }
      });
	   view.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent clickEvent) {
           if (selectedIteration != null )
           {
            view.getValidateDialogBox().getDialogPanel().hide();
            delete(selectedIteration.getIterationId());
           }
         }
      });
	   resetSelectedIteration();
	}
	
	public void resetSelectedIteration() {
	  selectedIteration = null;
    manageButtonsState();
	}
	
   /**
    * This method manages the rights for the iteration's views
    */
   private void manageRights()
   {
      final AccessRight accessRight = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PERIMETER);
      if (accessRight.equals(AccessRight.READ))
      {
         setViewReadOnly();
      }
   }

   private void manageButtonsState()
   {
      boolean canBeModified = selectedIteration != null && !selectedIteration.isFinished();
      boolean canBeDeleted  = selectedIteration != null && !selectedIteration.isFinished();
      view.getModificationButton().setEnabled(canBeModified);
      view.getDeleteButton().setEnabled(canBeDeleted);
   }

	private void delete(final long pIterationIdToDelete)
	{
    new AbstractManagementRPCCall<Boolean>()
    {

         @Override
         protected void callService(AsyncCallback<Boolean> cb) {
            Common.ITERATION_SERVICE.deleteIteration(currentProjectPlan.getProjectPlanId(),
                  pIterationIdToDelete, cb);
         }

       @Override
       public void onFailure(Throwable caught)
       {
          ErrorManagement.displayErrorMessage(caught);
       }

       @Override
       public void onSuccess(Boolean arg0) {
         if (arg0)
         {
               InfoDialogBox info = new InfoDialogBox(
                     Common.MESSAGES_CHARGE_PLAN.deleteSuccessful(), InfoTypeEnum.OK);
               info.getDialogPanel().center();
               info.getDialogPanel().show();
               fillIterationList();
               Common.GLOBAL_EVENT_BUS.fireEvent(new IterationReferentialModifiedEvent());
            }
         }


		}.retry(3);

	}

   /**
    * Set the view in readonly mode
    */
   private void setViewReadOnly()
   {
      view.getCreationButton().setEnabled(false);
      view.getModificationButton().setEnabled(false);
      view.getDeleteButton().setEnabled(false);
   }

   public void fillIterationList() {
      iterationsList = new ArrayList<IterationDTO>();
     new AbstractManagementRPCCall<List<IterationDTO>>()
     {
         @Override
         protected void callService(AsyncCallback<List<IterationDTO>> iCb) {
            long projectplanId = currentProjectPlan.getProjectPlanId();
            if (currentProjectPlan.getStatus().equals(ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT)
                  && SessionData.currentValidatedProjectPlanId != null) {
               projectplanId = SessionData.currentValidatedProjectPlanId;
            }
            Common.ITERATION_SERVICE.getIterationList(projectplanId, iCb);
         }

         @Override
         public void onSuccess(List<IterationDTO> pResult) {
            if (pResult != null) {
               iterationsList.addAll(pResult);
               Collections.sort(iterationsList, ITERATION_NUMBER_COMPARATOR);
               refreshIterationsList(iterationsList);
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(3);
   }

	private void refreshIterationsList(List<IterationDTO> iList)
	{
		view.getIterationsDataProvider().setList(iList);
		view.updateIterationsSortHandler();
	}

   public List<IterationDTO> getIterationsList()
   {
      return iterationsList;
   }

   public void setIterationsList(List<IterationDTO> iterationsList)
   {
      this.iterationsList = iterationsList;
   }

   @Override
   public void go(HasWidgets container)
   {
      container.clear();
      container.add(this.view.asWidget());
   }

   public int getLastIterationNumber()
   {
      return getIterationWithLastIterationNumber().getNumIteration();
   }

   public IterationDTO getIterationWithLastIterationNumber(){
		int iterationNumberMax = 0;
		IterationDTO iteration = null;
		for(int i = 0; i < iterationsList.size(); i++){
			if(iterationsList.get(i).getNumIteration() > iterationNumberMax){
				iterationNumberMax = iterationsList.get(i).getNumIteration();
				iteration = iterationsList.get(i);
			}
		}

		if(iterationNumberMax == 0){
			iteration = new IterationDTO();
			iteration.setNumIteration(0);
		}
		return iteration;
	}

   @Override
   public void loadDataOnSelectionTab()
   {
      fillIterationList();
      view.updateIterationsSortHandler();
   }

   @Override
   public IsWidget getDisplay()
	   {
	      return this.view.asWidget();
	   }

}
