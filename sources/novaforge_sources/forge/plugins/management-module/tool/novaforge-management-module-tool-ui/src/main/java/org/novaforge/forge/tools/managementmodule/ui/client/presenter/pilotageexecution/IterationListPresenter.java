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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.pilotageexecution;

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
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowBacklogEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowIterationMonitoringEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution.IterationListView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.pilotageexecution.IterationListViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Presenter of the iteration list
 * @author BILET-JC
 *
 */
public class IterationListPresenter implements TabPresenter {

	private final SimpleEventBus eventBus;
	private final IterationListView display;
	private List<IterationDTO> iterationList;
	//the selected iteration
	private IterationDTO selectedIteration;	
	

   /**
    * Constructor
    * @param eventBus the event bus
    */
   public IterationListPresenter(SimpleEventBus eventBus) {
      super();
      this.eventBus = eventBus;
		this.display = new IterationListViewImpl();
		this.selectedIteration = null;
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
		display.getFollowB().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
			   Common.GLOBAL_EVENT_BUS.fireEvent(new ShowIterationMonitoringEvent(selectedIteration));
			}
		});
		//handler on iteration reporting
		display.getUpdateB().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
	            eventBus.fireEvent(new ShowBacklogEvent(selectedIteration));
			}
		});
		//handler on selection change on list
		display.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
         @Override
         public void onSelectionChange(SelectionChangeEvent event) {
            selectedIteration = display.getSelectionModel().getSelectedObject();
            boolean hasRightItReporting = !SessionData.getAccessRight(
                  ApplicativeFunction.FUNCTION_ITERATION_MONITORING).equals(AccessRight.NONE);
            boolean hasRightModifyIteration = !SessionData.getAccessRight(
                  ApplicativeFunction.FUNCTION_MANAGING_TASK).equals(AccessRight.NONE);
            display.getFollowB().setEnabled(hasRightItReporting);
            display.getUpdateB().setEnabled(hasRightModifyIteration);
         }
      });
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
		disableActionButtons();
		updateIterationList();
	}

	/**
    * Disable all the actions buttons
    */
   private void disableActionButtons() {
      display.getFollowB().setEnabled(false);
      display.getUpdateB().setEnabled(false);
   }

	private void updateIterationList() {
		iterationList = new ArrayList<IterationDTO>();
		new AbstractManagementRPCCall<List<IterationDTO>>()
		{
			@Override
			protected void callService(AsyncCallback<List<IterationDTO>> cb) {
			   Common.ITERATION_SERVICE.getFinishedAndCurrentIterationList(SessionData.currentValidatedProjectPlanId, cb);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(List<IterationDTO> pResult)
			{
				if (pResult != null) {
					iterationList.addAll(pResult);
		            refreshIterationList(iterationList);
		         }
			}
		}.retry(0);
	}

	private void refreshIterationList(List<IterationDTO> pList) {
		display.getIterationDataProvider().setList(pList);
		display.getIterationCT().redraw();
		display.iterationListSortHandler();
	}

	@Override
	public IsWidget getDisplay() {
		return this.display;
	}

}
