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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.marker;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowMarkerEditViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.marker.MarkerListView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.marker.MarkerListViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.MarkerDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;

import java.util.ArrayList;
import java.util.List;

public class MarkerListPresenter implements TabPresenter {

	private final SimpleEventBus eventBus;
	private final MarkerListView display;
	private final ProjectPlanDTO currentProjectPlan;
	private final ViewEnum viewMode;
	private List<MarkerDTO> markerList;
	private MarkerDTO currentMarkerDTO;
	private ValidateDialogBox validate;
	

   public MarkerListPresenter(final SimpleEventBus eventBus, final ProjectPlanDTO projectPlan) {
      super();
		this.eventBus = eventBus;
		this.display = new MarkerListViewImpl();
		this.markerList = new ArrayList<MarkerDTO>();
		this.currentProjectPlan = projectPlan;
		this.viewMode = getAppropriateViewMode();
		bind();
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(this.display.asWidget());
	}

	public void bind() {
		display.getButtonCreationMarker().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ShowMarkerEditViewEvent(currentProjectPlan, null, ViewEnum.ADD));
			}
		});

		display.getButtonModifyMarker().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ShowMarkerEditViewEvent(currentProjectPlan, currentMarkerDTO.getId(), ViewEnum.EDIT));
			}
		});

		display.getButtonDeleteMarker().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				validate = new ValidateDialogBox(Common
						.getProjectPlanMessages().confirmDeleteMarkerMessage());
				validate.getValidate().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						validate.getDialogPanel().hide();
						deleteMarker();
					}
				});
				validate.getDialogPanel().center();
				validate.getDialogPanel().show();
			}
		});

		display.getSingleSelectionModel().addSelectionChangeHandler(
				new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(
							final SelectionChangeEvent pEvent) {
						setCurrentMarkerDTO(display.getSingleSelectionModel()
								.getSelectedObject());

					}
				});
	}

	private void deleteMarker()
	{
		new AbstractManagementRPCCall<Boolean>()
		{
			@Override
			protected void callService(AsyncCallback<Boolean> pCb)
			{
				Common.PROJECT_PLAN_SERVICE.deleteMarker(MarkerListPresenter.this.display.getSingleSelectionModel()
																																								 .getSelectedObject().getId(), pCb);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(Boolean pResult)
			{
				fillMarkerList();
			}


		}.retry(0);
	}

	@Override
	public void loadDataOnSelectionTab()
	{
		fillMarkerList();
		display.updateCellTableSortHandler();
	}

	public void fillMarkerList()
	{
		new AbstractManagementRPCCall<List<MarkerDTO>>()
		{
			@Override
			protected void callService(AsyncCallback<List<MarkerDTO>> pCb)
			{
				Common.PROJECT_PLAN_SERVICE.getMarkerList(currentProjectPlan.getProjectPlanId(), pCb);
			}

			@Override
			public void onFailure(Throwable caught) {
			   ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(List<MarkerDTO> pResult)
			{
				if (pResult != null)
				{
					refreshMarkerList(pResult);
				}
			}

		}.retry(0);
	}

	private void refreshMarkerList(List<MarkerDTO> pList) {
		markerList = new ArrayList<MarkerDTO>();
		markerList.addAll(pList);
		display.getMarkerDataProvider().getList().clear();
		display.getMarkerDataProvider().setList(markerList);
		display.updateCellTableSortHandler();
		display.getMarkerTable().redraw();

		if (currentMarkerDTO != null && markerList.contains(currentMarkerDTO)){
			display.getMarkerTable().getSelectionModel().setSelected(currentMarkerDTO, true);
		}
		else
		{
			setCurrentMarkerDTO(null);
		}
	}

	protected void setCurrentMarkerDTO(MarkerDTO marker) {
		currentMarkerDTO = marker;
		enableButtons();
	}

	public void enableButtons()
	{
		if (currentMarkerDTO == null || ViewEnum.READ.equals(viewMode))
		{
			display.getButtonModifyMarker().setEnabled(false);
			display.getButtonDeleteMarker().setEnabled(false);
		}
		else
		{
			display.getButtonModifyMarker().setEnabled(true);
			display.getButtonDeleteMarker().setEnabled(true);
		}
		if (ViewEnum.READ.equals(viewMode))
		{
			display.getButtonCreationMarker().setEnabled(false);
		}
	}
	
	@Override
   public IsWidget getDisplay() {
		return this.display.asWidget();
	}
	
   /**
    * Get the appropriate view mode from evaluation of project plan status and of user rights
    */
   private ViewEnum getAppropriateViewMode() {
      final AccessRight accessRight = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PERIMETER);
      if (ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT.equals(currentProjectPlan.getStatus())
            && accessRight.equals(AccessRight.WRITE)) {
         return ViewEnum.EDIT;
      } else {
         return ViewEnum.READ;
      }
   }
}
