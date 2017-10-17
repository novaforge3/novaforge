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

import java.util.HashMap;
import java.util.Map;

import org.novaforge.forge.tools.managementmodule.ui.client.ManagementModuleEntryPoint;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.EstimationModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.EstimationModifiedEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.IterationReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.LotReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.LotReferentialModifiedHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ProjectPlanReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowIterationEditViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowIterationEditViewEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowListEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowListEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowLotEditViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowLotEditViewEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowMarkerEditViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowMarkerEditViewEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.administration.ProjectPlanPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.administration.ProjectPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.chargeplan.ChargePlanPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.estimation.EstimationPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.iteration.IterationEditPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.iteration.IterationListPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.lotsettings.LotEditPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.lotsettings.LotListPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.marker.MarkerEditPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.marker.MarkerListPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.scope.ScopeTabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.projectplan.ProjectPlanTabView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.projectplan.ProjectPlanTabViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.AnchorsEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class ProjectPlanTabPresenter implements Presenter {
	private final SimpleEventBus eventBus;
	private final ProjectPlanTabView display;
	private final ProjectPlanDTO currentProjectPlan;
	private final ViewEnum mode;

	private LotListPresenter lotListPresenter;
	private LotEditPresenter lotEditPresenter;
	private IterationListPresenter iterationListPresenter;
	private IterationEditPresenter iterationEditPresenter;
	private MarkerListPresenter markerListPresenter;
	private MarkerEditPresenter markerEditPresenter;
	private ScopeTabPresenter scopeTabPresenter;
	private EstimationPresenter estimationPresenter;
	private ChargePlanPresenter chargePlanPresenter;
	private ProjectPlanPresenter projectPlanPresenter;
		
	// the indexes of the tab
	private int lotTab;
	private int iterationTab;
	private int markerTab;
	private int scopeTab;
	private int estimationTab;
	private int chargePlanTab;
	private int settingsTab;
	private Map<Integer, TabPresenter> presentersMap = new HashMap<Integer, TabPresenter>();

	/**
	 * @param projectPlanService
	 * @param eventBus
	 * @param display
	 */
	public ProjectPlanTabPresenter(SimpleEventBus eventBus, ProjectPlanDTO projectPlan) {
		super();
		this.eventBus = eventBus;
		this.display = new ProjectPlanTabViewImpl();
		this.currentProjectPlan = projectPlan;
		this.mode = getAppropriateViewModeFromProjectPlanStatus();
		initFields();
		bind();
		refreshPanels();
	}

	/**
	 * Get the appropriate view mode from evaluation of project plan status
	 */
	private ViewEnum getAppropriateViewModeFromProjectPlanStatus() {
		if (ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT.equals(currentProjectPlan.getStatus())) {
			return ViewEnum.EDIT;
		} else {
			return ViewEnum.READ;
		}
	}

	private void initFields() {
		ProjectPlanTabPresenter.this.display.getProjectName().setText(
				ProjectPlanTabPresenter.this.currentProjectPlan.getProjectName());
		ProjectPlanTabPresenter.this.display.getLastUpdate().setText(
				Common.FR_DATE_FORMAT_ONLY_DAY.format(currentProjectPlan.getDate()));
		ProjectPlanTabPresenter.this.display.getVersion().setText(
				"" + ProjectPlanTabPresenter.this.currentProjectPlan.getVersion());
		ProjectPlanTabPresenter.this.display.getStatus().setText("" + currentProjectPlan.getStatusLabel());
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
		display.getButtonProjectPlanList().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new ProjectPlanListPresenter(eventBus).go(RootLayoutPanel.get());
			}
		});
		display.getButtonValidate().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
		    display.getProjectPlanValidateDialogBox().getValidate().addClickHandler(new ClickHandler() {
          @Override
          public void onClick(ClickEvent event) {
            display.getProjectPlanValidateDialogBox().getDialogPanel().hide();
            if (currentProjectPlan != null && currentProjectPlan.getProjectId() != null) {
              validateProjectPlan(currentProjectPlan.getProjectId(), mode);
            }
          }
			    });
        display.getProjectPlanValidateDialogBox().getDialogPanel().center();
        display.getProjectPlanValidateDialogBox().getDialogPanel().show();
			}
		});
		display.getButtonReferential().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// to avoid multiple presenter for the same screen, we use
				// home's presenter one
				final ProjectPresenter projectPresenter = ManagementModuleEntryPoint
						.getHomePresenter().getProjectSettingsPresenter();
				projectPresenter.go(RootLayoutPanel.get());
			}
		});

		display.getTabPanel().addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				onSelectionTab(event);
			}
		});

		// EVENTBUS
		eventBus.addHandler(ShowLotEditViewEvent.TYPE, new ShowLotEditViewEventHandler() {
			@Override
			public void onShowLotEditView(ShowLotEditViewEvent event) {
				getLotEditPresenter().majLotData(event.getLotId(), event.getProjectPlanDTO());
				replaceTabDatas(lotTab, getLotEditPresenter(), Common.getProjectPlanMessages().headerLotTab());
			}
		});

		eventBus.addHandler(ShowIterationEditViewEvent.TYPE, new ShowIterationEditViewEventHandler() {
			@Override
			public void onShowIterationEditView(ShowIterationEditViewEvent event) {
				final IterationEditPresenter iterationEditPresenter = getIterationEditPresenter();
				iterationEditPresenter.setIterationToUpdate(event.getIterationDTO());
				replaceTabDatas(iterationTab, iterationEditPresenter, Common.getProjectPlanMessages()
						.headerIterationTab());
			}
		});

		eventBus.addHandler(ShowMarkerEditViewEvent.TYPE, new ShowMarkerEditViewEventHandler() {
			@Override
			public void onShowMarkerEditView(ShowMarkerEditViewEvent event) {
				getMarkerEditPresenter().majMarkerData(event.getMarkerId(), event.getProjectPlanDTO());
				replaceTabDatas(markerTab, getMarkerEditPresenter(), Common.getProjectPlanMessages()
						.headerMarkerTab());
			}
		});

		eventBus.addHandler(ShowListEvent.TYPE, new ShowListEventHandler() {
			@Override
			public void onShowListEvent(ShowListEvent event) {
				switch (event.getListToShow()) {
				case LOT_LIST:
					replaceTabDatas(lotTab, getLotListPresenter(), Common.getProjectPlanMessages()
							.headerLotTab());
					break;
				case ITERATION_LIST:
				  IterationListPresenter itListPres =getIterationListPresenter();
				  itListPres.resetSelectedIteration();
					replaceTabDatas(iterationTab, itListPres, Common
							.getProjectPlanMessages().headerIterationTab());
					break;
				case MARKER_LIST:
					replaceTabDatas(markerTab, getMarkerListPresenter(), Common.getProjectPlanMessages()
							.headerMarkerTab());
					break;
				case ESTIMATION_LIST:
					display.getTabPanel().selectTab(estimationTab);
					break;
				case CHARGEPLAN_LIST:
					display.getTabPanel().selectTab(chargePlanTab);
					break;
				}
			}
		});

		//Estimation Modification
		eventBus.addHandler(EstimationModifiedEvent.TYPE, new EstimationModifiedEventHandler() {
			@Override
			public void onEstimationModified(EstimationModifiedEvent event) {
				reloadChargePlan();
			}
		});

		//Lot Modification
		eventBus.addHandler(LotReferentialModifiedEvent.TYPE, new LotReferentialModifiedHandler() {
			@Override
			public void onModifyLotReferential(LotReferentialModifiedEvent event) {
				reloadChargePlan();
			}
		});

		//Iteration Modification
		eventBus.addHandler(IterationReferentialModifiedEvent.TYPE, new IterationReferentialModifiedEvent.Handler() {
         @Override
         public void onModifyIterationReferential(IterationReferentialModifiedEvent event) {
            reloadChargePlan();
         }
      });

		//Referential Modification
		eventBus.addHandler(ProjectPlanReferentialModifiedEvent.TYPE,
				new ProjectPlanReferentialModifiedEvent.Handler() {
					@Override
					public void onProjectPlanReferentialModified(ProjectPlanReferentialModifiedEvent event) {
						reloadEstimation();
						reloadChargePlan();
					}
				});
	}

	/**
	 *
	 */
	private void refreshPanels()
	{
		insertPanels();
		switch (mode)
		{
			case EDIT:
			   final AccessRight accessRightAdmin = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PROJECT_PLAN);
			    if (accessRightAdmin.equals(AccessRight.WRITE))
			    {
			      ProjectPlanTabPresenter.this.display.getButtonValidate().setVisible(true);
			    }
			    else
			    {
			      ProjectPlanTabPresenter.this.display.getButtonValidate().setVisible(false);
			    }
				break;
			case ADD:
			case READ:
				ProjectPlanTabPresenter.this.display.getButtonValidate().setVisible(false);
				break;
		}
	}

	private void validateProjectPlan(final String projectId, final ViewEnum mode) {
		new AbstractManagementRPCCall<ProjectPlanDTO>()
		{
			@Override
			protected void callService(AsyncCallback<ProjectPlanDTO> pCb) {
				Common.PROJECT_PLAN_SERVICE.validateProjectPlan(projectId, pCb);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(ProjectPlanDTO projectPlanResult) {
				InfoDialogBox idb = new InfoDialogBox(Common.getProjectPlanMessages().projectPlanValidated(),
						InfoTypeEnum.OK);
				idb.getDialogPanel().center();
				idb.getDialogPanel().show();
				
				
				// the currentProjectPlan is changed so we update it on
				// SessionData
				SessionData.currentValidatedProjectPlanId = currentProjectPlan.getProjectPlanId();
				// home and view mode are changed on validating a project plan
				// so we update both
				ManagementModuleEntryPoint.resetHomePresenter();
				ProjectPlanTabPresenter projectPlanTabPresenter = new ProjectPlanTabPresenter(eventBus,
						projectPlanResult);
				// if validation its the last so we put it in homepresenter to
				// avoid having two instances of a
				// ProjectPlanPresenter on the same projectplan
				ManagementModuleEntryPoint.getHomePresenter().setProjectPlanTabPresenter(
						projectPlanTabPresenter);
				projectPlanTabPresenter.go(RootLayoutPanel.get());
				projectPlanTabPresenter.display.getTabPanel().selectTab(
						display.getTabPanel().getSelectedIndex());
			}
		}.retry(0);
	}

	private void onSelectionTab(SelectionEvent<Integer> event) {
		// null at insertPanels
		if (presentersMap.get(event.getSelectedItem()) != null) {
			presentersMap.get(event.getSelectedItem()).loadDataOnSelectionTab();
		}
	}

	public LotEditPresenter getLotEditPresenter() {
		if (lotEditPresenter == null) {
			lotEditPresenter = new LotEditPresenter(eventBus, currentProjectPlan);
		}
		return lotEditPresenter;
	}

	private void replaceTabDatas(int tabNumber, TabPresenter tabPresenter, String message)
	{
		replaceTabDatas(tabNumber, tabPresenter, message, true);
	}

	public IterationEditPresenter getIterationEditPresenter() {
	  if (iterationEditPresenter == null) {
	    iterationEditPresenter = new IterationEditPresenter(eventBus, currentProjectPlan,
					getIterationListPresenter());
	  }	    
		return iterationEditPresenter;
	}

	public MarkerEditPresenter getMarkerEditPresenter() {
		if (markerEditPresenter == null) {
			markerEditPresenter = new MarkerEditPresenter(eventBus, currentProjectPlan);
		}
		return markerEditPresenter;
	}

	public LotListPresenter getLotListPresenter()
	{
		if (lotListPresenter == null)
		{
			lotListPresenter = new LotListPresenter(eventBus, currentProjectPlan);
		}
		return lotListPresenter;
	}

	public IterationListPresenter getIterationListPresenter()
	{
		if (iterationListPresenter == null)
		{
			iterationListPresenter = new IterationListPresenter(eventBus, currentProjectPlan);
		} 
		return iterationListPresenter;
	}

	public MarkerListPresenter getMarkerListPresenter()
	{
		if (markerListPresenter == null)
		{
			markerListPresenter = new MarkerListPresenter(eventBus, currentProjectPlan);
		}
		return markerListPresenter;
	}

	/**
	 * Reload the charge plan
	 */
	private void reloadChargePlan()
	{
		chargePlanPresenter = null;
		replaceTabDatas(chargePlanTab, getChargePlanPresenter(), Common.getProjectPlanMessages().headerChargePlanTab(),
										false);
	}

	/**
	 * Reload the estimation tab
	 */
	private void reloadEstimation()
	{
		estimationPresenter = null;
		replaceTabDatas(estimationTab, getEstimationPresenter(), Common.MESSAGES_ESTIMATION.headerEstimationTab(), false);

	}

	private void insertPanels()
	{
		this.display.getTabPanel().clear();
		final AccessRight accessRightPerimetre = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PERIMETER);
		if (!accessRightPerimetre.equals(AccessRight.NONE))
		{
			// L'onglet d'ouverture par dÃ©faut est l'onglet de DÃ©finition du
			// pÃ©rimÃªtre
			this.display.getTabPanel().add(getScopeTabPresenter().getDisplay(),
																		 Common.getProjectPlanMessages().headerScopeTab());
			scopeTab = this.display.getTabPanel().getWidgetIndex(getScopeTabPresenter().getDisplay());
			presentersMap.put(scopeTab, getScopeTabPresenter());

			this.display.getTabPanel().add(getLotListPresenter().getDisplay(),
																		 Common.getProjectPlanMessages().headerLotTab());
			lotTab = this.display.getTabPanel().getWidgetIndex(getLotListPresenter().getDisplay());
			presentersMap.put(lotTab, getLotListPresenter());

			this.display.getTabPanel().add(getIterationListPresenter().getDisplay(),
																		 Common.getProjectPlanMessages().headerIterationTab());
			iterationTab = this.display.getTabPanel().getWidgetIndex(getIterationListPresenter().getDisplay());
			presentersMap.put(iterationTab, getIterationListPresenter());

			this.display.getTabPanel().add(getMarkerListPresenter().getDisplay(),
																		 Common.getProjectPlanMessages().headerMarkerTab());
			markerTab = this.display.getTabPanel().getWidgetIndex(getMarkerListPresenter().getDisplay());
			presentersMap.put(markerTab, getMarkerListPresenter());
		}
		final AccessRight accessRightEstimation = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_ESTIMATION);
		if (!accessRightEstimation.equals(AccessRight.NONE))
		{
			this.display.getTabPanel().add(getEstimationPresenter().getDisplay(),
																		 Common.MESSAGES_ESTIMATION.headerEstimationTab());
			estimationTab = this.display.getTabPanel().getWidgetIndex(getEstimationPresenter().getDisplay());
			presentersMap.put(estimationTab, getEstimationPresenter());
		}
		final AccessRight accessRightChargePlan = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_CHARGE_PLAN);
		if (!accessRightChargePlan.equals(AccessRight.NONE))
		{
			this.display.getTabPanel().add(getChargePlanPresenter().getDisplay(),
																		 Common.getProjectPlanMessages().headerChargePlanTab());
			chargePlanTab = this.display.getTabPanel().getWidgetIndex(getChargePlanPresenter().getDisplay());
			presentersMap.put(chargePlanTab, getChargePlanPresenter());
		}
		if (!accessRightPerimetre.equals(AccessRight.NONE))
		{
			this.display.getTabPanel().add(getProjectPlanSettingsPresenter().getDisplay(),
																		 Common.getProjectPlanMessages().headerProjectPlanSettingsTab());
			settingsTab = this.display.getTabPanel().getWidgetIndex(getProjectPlanSettingsPresenter().getDisplay());
			presentersMap.put(settingsTab, getProjectPlanSettingsPresenter());
		}
	}

	/**
	 * @inheritDoc
	 */
	@Override
	public void go(HasWidgets container)
	{
		container.clear();
		container.add(display.asWidget());
	}

	/**
	 * This methods replace the datas of a tab (changing presenter)
	 *
	 * @param tabNumber
	 *            the tab index to change
	 * @param tabPresenter
	 *            the presenter to set
	 * @param message
	 *            the label of the tab
	 */
	private void replaceTabDatas(int tabNumber, TabPresenter tabPresenter, String message, boolean select) {
		presentersMap.put(tabNumber, tabPresenter);
		display.getTabPanel().insert(tabPresenter.getDisplay(), message, tabNumber);
		if (select) {
			display.getTabPanel().selectTab(tabNumber);
		}
		// remove after the insert to avoid first tab selection and loading
		display.getTabPanel().remove(tabNumber + 1);
	}

	private ChargePlanPresenter getChargePlanPresenter()
	{
		if (null == chargePlanPresenter)
		{
			chargePlanPresenter = new ChargePlanPresenter(currentProjectPlan, mode);
		}
		return chargePlanPresenter;
	}

	private EstimationPresenter getEstimationPresenter()
	{
		if (null == estimationPresenter)
		{
			estimationPresenter = new EstimationPresenter(eventBus, currentProjectPlan);
		}
		return estimationPresenter;
	}

	public ScopeTabPresenter getScopeTabPresenter()
	{
		if (scopeTabPresenter == null)
		{
			scopeTabPresenter = new ScopeTabPresenter(eventBus, currentProjectPlan.getProjectId(), currentProjectPlan);
		}
		return scopeTabPresenter;
	}

	/**
	 * Get the project plan setting presenter
	 *
	 * @return the presenter
	 */
	private ProjectPlanPresenter getProjectPlanSettingsPresenter()
	{
		if (projectPlanPresenter == null)
		{
			projectPlanPresenter = new ProjectPlanPresenter(eventBus);
		}
		return projectPlanPresenter;
	}

	@Override
	public IsWidget getDisplay() {
		return this.display.asWidget();
	}

	public void setTab(final AnchorsEnum anchor) {
		switch (anchor) {
		case MARKER_TAB:
			display.getTabPanel().selectTab(markerTab);
			break;
		case LOT_TAB:
			display.getTabPanel().selectTab(lotTab);
			break;
		case SCOPE_TAB:
			display.getTabPanel().selectTab(scopeTab);
			break;
		case ESTIMATION_TAB:
			display.getTabPanel().selectTab(estimationTab);
			break;
		case CHARGE_PLAN_TAB:
			display.getTabPanel().selectTab(chargePlanTab);
			break;
		case PROJECT_PLAN_SETTINGS_TAB:
			display.getTabPanel().selectTab(settingsTab);
			break;
		case ITERATION_TAB:
		default:
			display.getTabPanel().selectTab(iterationTab);
			break;
		}
	}

	/**
	 * Get the currentProjectPlan
	 * 
	 * @return the currentProjectPlan
	 */
	public ProjectPlanDTO getCurrentProjectPlan() {
		return currentProjectPlan;
	}

}
