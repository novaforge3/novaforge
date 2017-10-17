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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.LinkEventHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.OnClickLinkEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.administration.ProjectPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.pilotageexecution.PilotageExecutionTabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.projectplan.ProjectPlanListPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.projectplan.ProjectPlanTabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.view.HomeView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.HomeViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.AnchorsEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;

/**
 * @author BILET-JC
 * 
 */
public class HomePresenter implements Presenter {

	private final SimpleEventBus eventBus;
	private final HomeView display;
	private ProjectPlanListPresenter projectPlanListPresenter;
	private ProjectPlanTabPresenter projectPlanTabPresenter;
	private ProjectPresenter projectSettingsPresenter;
	private PilotageExecutionTabPresenter pilotageExecutionTabPresenter;
	/**
	 * Get the last validated project plan
	 */
	AbstractManagementRPCCall<ProjectPlanDTO> getLastValidatedProjectPlan = new AbstractManagementRPCCall<ProjectPlanDTO>()
	{
		@Override
		protected void callService(AsyncCallback<ProjectPlanDTO> pCb)
		{
			Common.PROJECT_PLAN_SERVICE.getLastValidatedProjectPlan(SessionData.projectId, pCb);
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}

		@Override
		public void onSuccess(ProjectPlanDTO pResult)
		{
			if (pResult != null)
			{
				SessionData.currentValidatedProjectPlanId = pResult.getProjectPlanId();
				getPilotageExecutionTabPresenter();
			}
			else
			{
				// if there is no validated project plan -> project
				// initialization, so no access to execution and
				// reporting
				SessionData.currentValidatedProjectPlanId = null;
				display.disableLinks();
				getProjectPlanListPresenter();
			}
		}

	};
	private ProjectPlanDTO lastProjectPlan;
	/**
	 * Get the current project plan, either the draft or the last validated
	 */
	AbstractManagementRPCCall<ProjectPlanDTO> getCurrentProjectPlan = new AbstractManagementRPCCall<ProjectPlanDTO>()
	{
		@Override
		protected void callService(AsyncCallback<ProjectPlanDTO> pCb)
		{
			Common.PROJECT_PLAN_SERVICE.getLastProjectPlan(SessionData.projectId, pCb);
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}

		@Override
		public void onSuccess(ProjectPlanDTO pResult)
		{
			if (pResult != null)
			{
				lastProjectPlan = pResult;
			}
		}

	};

	public HomePresenter(SimpleEventBus eventBus)
	{
		super();
		this.eventBus = eventBus;
		this.display = new HomeViewImpl();
		getCurrentProjectPlan.retry(0);
		getLastValidatedProjectPlan.retry(0);
		bind();
		manageRights();
	}

	private void bind()
	{

		eventBus.addHandler(OnClickLinkEvent.TYPE, new LinkEventHandler()
		{

			@Override
			public void onClickLinkEvent(OnClickLinkEvent onClickLinkEvent)
			{
				switch (onClickLinkEvent.getPresenter())
				{
					case PROJECT_PLAN_LIST_PRESENTER:
						getProjectPlanListPresenter().go(RootLayoutPanel.get());
						break;
					case PROJECT_PLAN_TAB_PRESENTER:
						getProjectPlanTabPresenter().go(RootLayoutPanel.get());
						getProjectPlanTabPresenter().setTab(onClickLinkEvent.getAnchor());
						break;
					case PILOTAGE_EXECUTION_TAB_PRESENTER:
						if (SessionData.currentValidatedProjectPlanId != null)
						{
							getPilotageExecutionTabPresenter().go(RootLayoutPanel.get());
							getPilotageExecutionTabPresenter().setTab(onClickLinkEvent.getAnchor());
						}
						break;
					case PROJECT_INITIAL_SETTINGS_PRESENTER:
						getProjectSettingsPresenter().go(RootLayoutPanel.get());
						break;
					default:
						break;
				}
			}
		});
	}

	/**
	 * This methods manage the rights -> disable the non allowed links and put
	 * handler to the others
	 */
	private void manageRights()
	{
		// Perimeter management
		final AccessRight accessRightPerimetre = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PERIMETER);
		if (accessRightPerimetre.equals(AccessRight.NONE))
		{
			display.getProjectPlanListLink().setStyleName(Common.RESOURCE.css().disabledLink());
			display.getProjectPlanLotLink().setStyleName(Common.RESOURCE.css().disabledLink());
			display.getProjectPlanIterationLink().setStyleName(Common.RESOURCE.css().disabledLink());
			display.getProjectPlanMarkerLink().setStyleName(Common.RESOURCE.css().disabledLink());
			display.getProjectPlanScopeLink().setStyleName(Common.RESOURCE.css().disabledLink());
			display.getInitialAdminLink().setStyleName(Common.RESOURCE.css().disabledLink());
			display.getProjectPlanAdminLink().setStyleName(Common.RESOURCE.css().disabledLink());
		}
		else
		{
			display.getProjectPlanListLink().addClickHandler(createClickHandler(PresenterEnum.PROJECT_PLAN_LIST_PRESENTER,
																																					AnchorsEnum.PROJECT_PLAN_LIST));
			display.getProjectPlanLotLink().addClickHandler(createClickHandler(PresenterEnum.PROJECT_PLAN_TAB_PRESENTER,
																																				 AnchorsEnum.LOT_TAB));
			display.getProjectPlanIterationLink().addClickHandler(createClickHandler(PresenterEnum.PROJECT_PLAN_TAB_PRESENTER,
																																							 AnchorsEnum.ITERATION_TAB));
			display.getProjectPlanMarkerLink().addClickHandler(createClickHandler(PresenterEnum.PROJECT_PLAN_TAB_PRESENTER,
																																						AnchorsEnum.MARKER_TAB));
			display.getProjectPlanScopeLink().addClickHandler(createClickHandler(PresenterEnum.PROJECT_PLAN_TAB_PRESENTER,
																																					 AnchorsEnum.SCOPE_TAB));
		}
		//administration management
		final AccessRight accessRightAdmin =SessionData.getAccessRight(ApplicativeFunction.FUNCTION_ADMINISTRATION);
    if (accessRightAdmin != AccessRight.NONE )
    {
      display.getInitialAdminLink().addClickHandler(createClickHandler(PresenterEnum.PROJECT_INITIAL_SETTINGS_PRESENTER,
                                                                     null));
      display.getProjectPlanAdminLink().addClickHandler(createClickHandler(PresenterEnum.PROJECT_PLAN_TAB_PRESENTER,
                                                                         AnchorsEnum.PROJECT_PLAN_SETTINGS_TAB));
    } else
    {
      display.getInitialAdminLink().setStyleName(Common.RESOURCE.css().disabledLink());
      display.getProjectPlanAdminLink().setStyleName(Common.RESOURCE.css().disabledLink());       
    }

		// Estimation management
		final AccessRight accessRightEstimation = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_ESTIMATION);
		if (accessRightEstimation.equals(AccessRight.NONE))
		{
			display.getProjectPlanEstimationLink().setStyleName(Common.RESOURCE.css().disabledLink());
		}
		else
		{
			display.getProjectPlanEstimationLink()
						 .addClickHandler(createClickHandler(PresenterEnum.PROJECT_PLAN_TAB_PRESENTER, AnchorsEnum.ESTIMATION_TAB));
		}
		// ChargePlan management
		final AccessRight accessRightChargePlan = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_CHARGE_PLAN);
		if (accessRightChargePlan.equals(AccessRight.NONE))
		{
			display.getProjectPlanChargePlanLink().setStyleName(Common.RESOURCE.css().disabledLink());
		}
		else
		{
			display.getProjectPlanChargePlanLink()
						 .addClickHandler(createClickHandler(PresenterEnum.PROJECT_PLAN_TAB_PRESENTER,
																								 AnchorsEnum.CHARGE_PLAN_TAB));
		}
		// Preparation and scopeUnitDiscipline management
		final AccessRight accessRightPreparation = SessionData
																									 .getAccessRight(ApplicativeFunction.FUNCTION_PREPARATION_ITERATION);
		if (accessRightPreparation.equals(AccessRight.NONE))
		{
			display.getPilotageExecutionPreparationLink().setStyleName(Common.RESOURCE.css().disabledLink());
			display.getPilotageExecutionScopeUnitDisciplineLink().setStyleName(Common.RESOURCE.css().disabledLink());
		}
		else
		{
			display.getPilotageExecutionPreparationLink().addClickHandler(
					createClickHandler(PresenterEnum.PILOTAGE_EXECUTION_TAB_PRESENTER,
							AnchorsEnum.PREPARATION_TAB));
			display.getPilotageExecutionScopeUnitDisciplineLink().addClickHandler(
					createClickHandler(PresenterEnum.PILOTAGE_EXECUTION_TAB_PRESENTER,
							AnchorsEnum.SCOPE_UNIT_DISCIPLINE_TAB));
		}
		// Backlog management
		final AccessRight accessRightManageTasks = SessionData
				.getAccessRight(ApplicativeFunction.FUNCTION_MANAGING_TASK);
		if (accessRightManageTasks.equals(AccessRight.NONE)) {
			display.getPilotageExecutionRefItLink().setStyleName(Common.RESOURCE.css().disabledLink());
		} else {
			display.getPilotageExecutionRefItLink().addClickHandler(
					createClickHandler(PresenterEnum.PILOTAGE_EXECUTION_TAB_PRESENTER,
							AnchorsEnum.REF_ITERATION_TAB));
		}
		// SuiviIt management
		final AccessRight accessRightSuiviIteration = SessionData
				.getAccessRight(ApplicativeFunction.FUNCTION_ITERATION_MONITORING);
		if (accessRightSuiviIteration.equals(AccessRight.NONE)) {
			display.getPilotageExecutionReportItLink().setStyleName(Common.RESOURCE.css().disabledLink());
		} else {
			display.getPilotageExecutionReportItLink().addClickHandler(
					createClickHandler(PresenterEnum.PILOTAGE_EXECUTION_TAB_PRESENTER,
							AnchorsEnum.ITERATION_MONITORING_TAB));
		}
		// SuiviGlobal management
		final AccessRight accessRightSuiviGlobal = SessionData
				.getAccessRight(ApplicativeFunction.FUNCTION_GLOBAL_MONITORING);
		if (accessRightSuiviGlobal.equals(AccessRight.NONE)) {
			display.getPilotageExecutionReportGlobalLink().setStyleName(Common.RESOURCE.css().disabledLink());
			display.getPilotageExecutionReportDrawLink().setStyleName(Common.RESOURCE.css().disabledLink());
		} else {
			display.getPilotageExecutionReportGlobalLink().addClickHandler(
					createClickHandler(PresenterEnum.PILOTAGE_EXECUTION_TAB_PRESENTER,
							AnchorsEnum.GLOBAL_MONITORING_TAB));
			display.getPilotageExecutionReportDrawLink().addClickHandler(
					createClickHandler(PresenterEnum.PILOTAGE_EXECUTION_TAB_PRESENTER,
							AnchorsEnum.PROJECT_MONITORING_GRAPH_TAB));
		}
		//Burndown management
    final AccessRight accessRightBurndown = SessionData.getAccessRight(ApplicativeFunction.FUNCTION_BURNDOWN);
    if (accessRightBurndown.equals(AccessRight.NONE)) {
      display.getPilotageExecutionBurnDownLink().setStyleName(
          Common.RESOURCE.css().disabledLink());
    } else {
      display.getPilotageExecutionBurnDownLink().addClickHandler(
          createClickHandler(PresenterEnum.PILOTAGE_EXECUTION_TAB_PRESENTER,
              AnchorsEnum.BURNDOWN_IT_TAB));
    }
    
		
		// iteration list management
		if (accessRightPreparation.equals(AccessRight.NONE)
				&& accessRightManageTasks.equals(AccessRight.NONE)
				&& accessRightSuiviIteration.equals(AccessRight.NONE)
				&& accessRightSuiviGlobal.equals(AccessRight.NONE)) {
			display.getPilotageExecutionListLink().setStyleName(Common.RESOURCE.css().disabledLink());
		} else {
			display.getPilotageExecutionListLink().addClickHandler(
					createClickHandler(PresenterEnum.PILOTAGE_EXECUTION_TAB_PRESENTER,
							AnchorsEnum.BACKLOG_LIST));
		}
	}

	private ProjectPlanListPresenter getProjectPlanListPresenter() {
		if (projectPlanListPresenter == null) {
			projectPlanListPresenter = new ProjectPlanListPresenter(eventBus);
		}
		return projectPlanListPresenter;
	}

	public ProjectPlanTabPresenter getProjectPlanTabPresenter() {
		if (projectPlanTabPresenter == null) {
			projectPlanTabPresenter = new ProjectPlanTabPresenter(eventBus, lastProjectPlan);
		}
		return projectPlanTabPresenter;
	}

	/**
	 * Set the projectPlanTabPresenter
	 *
	 * @param projectPlanTabPresenter
	 *            the projectPlanTabPresenter to set
	 */
	public void setProjectPlanTabPresenter(ProjectPlanTabPresenter projectPlanTabPresenter) {
		this.projectPlanTabPresenter = projectPlanTabPresenter;
	}

	private PilotageExecutionTabPresenter getPilotageExecutionTabPresenter() {
		if (pilotageExecutionTabPresenter == null) {
			pilotageExecutionTabPresenter = new PilotageExecutionTabPresenter(eventBus);
		}
		return pilotageExecutionTabPresenter;
	}

	public ProjectPresenter getProjectSettingsPresenter()
	{
		if (projectSettingsPresenter == null)
		{
			projectSettingsPresenter = new ProjectPresenter(eventBus);
		}
		return projectSettingsPresenter;

	}

	/**
	 * Create the appropriate click handler for a link, using a presenter and a
	 * anchor
	 * 
	 * @param presenter
	 *            the presenter
	 * @param anchor
	 *            the anchor
	 * @return the created ClickHandler
	 */
	private ClickHandler createClickHandler(final PresenterEnum presenter, final AnchorsEnum anchor) {
		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				final OnClickLinkEvent clickLinkEvent = new OnClickLinkEvent();
				clickLinkEvent.setPresenter(presenter);
				clickLinkEvent.setAnchor(anchor);
				eventBus.fireEvent(clickLinkEvent);
			}

		};
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public IsWidget getDisplay() {
		return this.display.asWidget();
	}

   /**
    * Get the lastProjectPlan
    * @return the lastProjectPlan
    */
   public ProjectPlanDTO getProjectPlan() {
      return lastProjectPlan;
   }

}
