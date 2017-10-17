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

package org.novaforge.forge.tools.managementmodule.ui.client.presenter.lotsettings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.LotReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowLotEditViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.lotsettings.LotSettingsListView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.lotsettings.LotSettingsListViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author FALSQUELLE-E
 */
public class LotListPresenter implements TabPresenter {

	private final SimpleEventBus eventBus;
	private final LotSettingsListView display;
	private final ProjectPlanDTO currentProjectPlan;
	private final ViewEnum viewMode;
	private List<LotDTO> lotList;
	private LotDTO currentLotDTO;
	private ValidateDialogBox validate;
   

	public LotListPresenter(final SimpleEventBus eventBus, final ProjectPlanDTO projectPlan) {
		super();
		this.eventBus = eventBus;
		this.display = new LotSettingsListViewImpl();
		this.lotList = new ArrayList<LotDTO>();
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
		display.getButtonCreationLot().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ShowLotEditViewEvent(currentProjectPlan, null));
			}
		});

		display.getButtonModifyLot().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				eventBus.fireEvent(new ShowLotEditViewEvent(currentProjectPlan, currentLotDTO.getLotId()));
			}
		});

		display.getButtonDeleteLot().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				validate = new ValidateDialogBox(Common
						.getProjectPlanMessages().confirmDeleteLotMessage());
				validate.getValidate().addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						validate.getDialogPanel().hide();
						deleteLot();
					}
				});
				validate.getDialogPanel().center();
				validate.getDialogPanel().show();
			}
		});
		
		display.getButtonShowDiagram().addClickHandler(new ClickHandler() {
         @Override
         public void onClick(ClickEvent event) {
            showGanttDiagram();
         }
      });

		display.getSingleSelectionModel().addSelectionChangeHandler(
				new SelectionChangeEvent.Handler() {
					@Override
					public void onSelectionChange(
							final SelectionChangeEvent pEvent) {
						setCurrentLotDTO(display.getSingleSelectionModel()
								.getSelectedObject());
					}
				});
	}

	private void deleteLot() {
		new AbstractManagementRPCCall<Boolean>()
		{
			@Override
			protected void callService(AsyncCallback<Boolean> pCb) {
            Common.PROJECT_PLAN_SERVICE.deleteLot(LotListPresenter.this.display
                  .getSingleSelectionModel().getSelectedObject().getLotId(), pCb);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(Boolean pResult) {
				fillLotList();
				eventBus.fireEvent(new LotReferentialModifiedEvent());
			}


		}.retry(0);
	}

	@Override
	public void loadDataOnSelectionTab()
	{
		fillLotList();
    display.updateCellTableSortHandler();
	}

	public void fillLotList()
	{
		new AbstractManagementRPCCall<List<LotDTO>>()
		{
			@Override
			protected void callService(AsyncCallback<List<LotDTO>> pCb)
			{
				Common.PROJECT_PLAN_SERVICE.getLotList(currentProjectPlan.getProjectPlanId(), pCb);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(List<LotDTO> pResult)
			{
				if (pResult != null)
				{
					refreshLotList(pResult);
				}
			}

		}.retry(0);
	}
	
	private void refreshLotList(List<LotDTO> pList) {
		lotList = new ArrayList<LotDTO>();
		lotList.addAll(pList);
		display.getLotDataProvider().getList().clear();
		display.getLotDataProvider().setList(lotList);
		display.updateCellTableSortHandler();
		display.getLotTable().redraw();
		if (currentLotDTO != null && lotList.contains(currentLotDTO)){
			display.getLotTable().getSelectionModel().setSelected(currentLotDTO, true);
		}
		else
		{
			setCurrentLotDTO(null);
		}
	}

	protected void setCurrentLotDTO(LotDTO lot) {
		currentLotDTO = lot;
		manageButtons();
	}

  public void manageButtons(){
     if(ViewEnum.READ.equals(viewMode)) {
        display.getButtonCreationLot().setEnabled(false);
     }
     if (currentLotDTO == null || ViewEnum.READ.equals(viewMode)) {
        display.getButtonModifyLot().setEnabled(false);
        display.getButtonDeleteLot().setEnabled(false);
     } else {
        Date currentDate = new Date();
        //NVF_FCT_50T0724 : Un bouton permet d'accéder au formulaire de modification d'un lot uniquement si la date de fin du lot est postérieure à la date du jour et si un seul lot a été sélectionné sinon le bouton est grisé
        if(currentLotDTO.getEndDate().getTime() > currentDate.getTime()){
           display.getButtonModifyLot().setEnabled(true);
        }else{
           display.getButtonModifyLot().setEnabled(false);
        }

        //NVF_FCT_50T0725 : Un bouton permet de supprimer un lot uniquement si la date de début du lot n'est pas antérieure à la date du jour et si un seul lot a été sélectionné sinon le bouton est grisé
        if(currentLotDTO.getStartDate().getTime() > currentDate.getTime()){
           display.getButtonDeleteLot().setEnabled(true);
        }else{
           display.getButtonDeleteLot().setEnabled(false);
        }
     }
  }

	@Override
	public IsWidget getDisplay()
	{
		return this.display.asWidget();
	}

	public void showGanttDiagram(){

		Window.open(GWT.getModuleBaseURL() + Constants.REPORT_SERVLET_NAME + "?" + Constants.BIRT_REPORT_NAME_PARAMETER
										+ "=GanttDiagramOfLots&&" + Constants.BIRT_PROJECT_PLAN_ID_PARAMETER + "=" + currentProjectPlan
																																																		 .getProjectPlanId()
										+ "&&" + Constants.BIRT_PROJECT_NAME_PARAMETER + "=" + currentProjectPlan.getProjectName() + "&&"
										+ Constants.BIRT_PROJECT_PLAN_VERSION_PARAMETER + "=" + currentProjectPlan.getVersion(), "_blank",
								"");
	}
  
   /**
    * Get the appropriate view mode from evaluation of project plan status and of the rights
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
