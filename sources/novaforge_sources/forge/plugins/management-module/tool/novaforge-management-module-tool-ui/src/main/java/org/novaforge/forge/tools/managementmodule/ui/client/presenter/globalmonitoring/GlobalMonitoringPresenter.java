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
/**
 * 
 */
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.globalmonitoring;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.ManagementModuleEntryPoint;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.UpdateGlobalAdvancementEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.AcronymBoxPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.globalmonitoring.GlobalMonitoringView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.globalmonitoring.GlobalMonitoringViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.Constants;
import org.novaforge.forge.tools.managementmodule.ui.shared.GlobalMonitoringIndicatorsDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitGlobalMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitMonitoringDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitMonitoringStatusEnum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author BILET-JC
 * 
 */
public class GlobalMonitoringPresenter implements TabPresenter {

	public static final String GLOBAL_MONITORING_CSV_NAME = "CSVGlobalMonitoring";
	private final GlobalMonitoringView display;
	private final AcronymBoxPresenter acronymPresenter;
	/** initial list, with all scopeUnitMonitoring, parents and childs */
	private List<ScopeUnitGlobalMonitoringDTO> scopeUnitMonitorings;
	/**
	 * parent scopeUnitMonitoring list, displayed by default an when
	 * scopeUnitParent filter is not used
	 */
	private List<ScopeUnitGlobalMonitoringDTO> parentScopeUnitMonitorings;
	/** filtered by lot & sublot scopeUnitMonitoringList */
	private List<ScopeUnitGlobalMonitoringDTO> scopeUnitMonitoringsFiltered;
	private List<LotDTO> lots;
	/** mapping between a scopeUnit (parent) and its remaining */
	private Map<String, Float> remainingsScopeUnit = new HashMap<String, Float>();
	private SimpleEventBus localEventBus = new SimpleEventBus();
	private Map<ScopeUnitMonitoringStatusEnum, String> scopeUnitMonitoringStatusTranslation;
	/**
	 * Get the current projectPlan
	 */
	private AbstractManagementRPCCall<GlobalMonitoringIndicatorsDTO>      getGlobalMonitoringInformations = new AbstractManagementRPCCall<GlobalMonitoringIndicatorsDTO>()
	{

		@Override
		protected void callService(AsyncCallback<GlobalMonitoringIndicatorsDTO> pCb)
		{
			Common.TASK_SERVICE.getGlobalMonitoringInformations(SessionData.currentValidatedProjectPlanId, pCb);
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}

		@Override
		public void onSuccess(GlobalMonitoringIndicatorsDTO pResult)
		{
			if (pResult != null)
			{
				display.getProjectStartDate().setText(pResult.getProjectStartDate());
				display.getIndicatorAverageVelocity().setText(Common.floatFormat(pResult.getVelocity(), 2).toString());
				display.getIndicatorAverageFocalisationFactor().setText(Common.floatFormat(pResult.getFocalisationFactor(), 2)
																																			.toString());
				display.getIndicatorAverageErrorEstimation().setText(Common.floatFormat(pResult.getEstimationError(), 2)
																																	 .toString());
				display.getIndicatorLastIdealFP().setText(Common.floatFormat(pResult.getLastCountFP(), 2).toString());
			}
		}

	};
	/**
	 * Get the scopeUnits
	 */
	private AbstractManagementRPCCall<List<ScopeUnitGlobalMonitoringDTO>> getScopeUnits                   = new AbstractManagementRPCCall<List<ScopeUnitGlobalMonitoringDTO>>()
	{

		@Override
		protected void callService(AsyncCallback<List<ScopeUnitGlobalMonitoringDTO>> pCb)
		{
			Common.SCOPE_SERVICE.getScopeUnitMonitoring(SessionData.currentValidatedProjectPlanId, pCb);
		}

		@Override
		public void onSuccess(List<ScopeUnitGlobalMonitoringDTO> pResult)
		{
			if (pResult != null)
			{
				// the result is the initial list of scopeUnitMonitoring and the
				// default scopeUnitMonitoringFiltered
				scopeUnitMonitorings = pResult;
				scopeUnitMonitoringsFiltered = pResult;
				refreshParentScopeUnitMonitorings();
				resetSelectionModel();
				refreshIndicators();
			}
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}
	};
	/**
	 * Get all the lots of the projectPlan
	 */
	private AbstractManagementRPCCall<List<LotDTO>>                       getLots                         = new AbstractManagementRPCCall<List<LotDTO>>()
	{

		@Override
		protected void callService(AsyncCallback<List<LotDTO>> pCb)
		{
			Common.PROJECT_PLAN_SERVICE.getLotList(SessionData.currentValidatedProjectPlanId, pCb);
		}

		@Override
		public void onSuccess(List<LotDTO> pResult)
		{
			if (pResult != null)
			{
				lots = pResult;
				createLotsFilter();
			}
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}
	};
	/**
	 * Update all parent remaining scopeUnit
	 */
	private AbstractManagementRPCCall<Boolean>                            updateRemainingsScopeUnit       = new AbstractManagementRPCCall<Boolean>()
	{

		@Override
		protected void callService(AsyncCallback<Boolean> pCb)
		{
			Common.PROJECT_PLAN_SERVICE.updateRemainingScopeUnit(remainingsScopeUnit, pCb);
		}

		@Override
		public void onSuccess(Boolean pResult)
		{
			if (pResult != null)
			{
				display.getSuccessUpdateRemainingScopeUnit().getDialogPanel().center();
				display.getSuccessUpdateRemainingScopeUnit().getDialogPanel().show();
			}
			else
			{
				display.getUnsuccessUpdateRemainingScopeUnit().getDialogPanel().center();
				display.getUnsuccessUpdateRemainingScopeUnit().getDialogPanel().show();
			}
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}
	};

	public GlobalMonitoringPresenter()
	{
		display = new GlobalMonitoringViewImpl(localEventBus);
		acronymPresenter = new AcronymBoxPresenter(getAcronyms());
		createStatus();
		bind();
		enableUI();
	}

	/**
	 * Create and return the list of acronyms
	 *
	 * @return
	 */
	private Map<String, String> getAcronyms()
	{
		Map<String, String> ret = new HashMap<String, String>();
		ret.put(Common.MESSAGES_ACRONYM.pfL(), Common.MESSAGES_ACRONYM.pfDef());
		ret.put(Common.MESSAGES_ACRONYM.upL(), Common.MESSAGES_ACRONYM.upDef());
		ret.put(Common.MESSAGES_ACRONYM.rafL(), Common.MESSAGES_ACRONYM.rafDef());
		ret.put(Common.MESSAGES_ACRONYM.jL(), Common.MESSAGES_ACRONYM.jDef());
		ret.put(Common.MESSAGES_ACRONYM.BL(), Common.MESSAGES_ACRONYM.BDef());
		ret.put(Common.MESSAGES_ACRONYM.PL(), Common.MESSAGES_ACRONYM.PDef());
		ret.put(Common.MESSAGES_ACRONYM.RL(), Common.MESSAGES_ACRONYM.RDef());
		return ret;
	}

	/**
	 * RPC call to finish the scopeUnit with the given id
	 *
	 * @param unitId
	 */
	private void finishScopeUnit(final String unitId)
	{
		new AbstractManagementRPCCall<Boolean>()
		{

			@Override
			protected void callService(AsyncCallback<Boolean> pCb)
			{
				Common.SCOPE_SERVICE.finishScopeUnit(unitId, pCb);
			}

			@Override
			public void onSuccess(Boolean pResult)
			{
				if (pResult)
				{
					getScopeUnits.retry(0);
					getGlobalMonitoringInformations.retry(0);
					display.getSuccessFinishScopeUnit().getDialogPanel().center();
					display.getSuccessFinishScopeUnit().getDialogPanel().show();
				}
			}

			@Override
			public void onFailure(Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}
		}.retry(0);
	}

	/**
	 * Create a CSV format of given data which is put in session. On success is
	 * called a servlet which create a file which session put in session
	 *
	 * @param displayedList
	 */
	private void createCSVFormat(final List<ScopeUnitGlobalMonitoringDTO> displayedList)
	{
		new AbstractManagementRPCCall<Boolean>()
		{

			@Override
			protected void callService(AsyncCallback<Boolean> cb)
			{
				Common.SCOPE_SERVICE.createCSVFormatWithScopeUnitGlobalMonitoringDTOList(displayedList,
																																								 getScopeUnitMonitoringStatusTranslation(),
																																								 cb);
			}

			@Override
			public void onSuccess(Boolean result)
			{
				Map<String, String> parameters = new HashMap<String, String>();
				parameters.put(Constants.EXPORT_CSV_NAME_PARAMETER, GLOBAL_MONITORING_CSV_NAME);
				Common.exportCSV(GWT.getModuleBaseURL() + Constants.EXPORT_CSV_SERVLET_NAME, parameters);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught.getMessage());

			}
		}.retry(0);
	}

	private Map<ScopeUnitMonitoringStatusEnum, String> getScopeUnitMonitoringStatusTranslation()
	{
		if (scopeUnitMonitoringStatusTranslation == null)
		{
			scopeUnitMonitoringStatusTranslation = new HashMap<ScopeUnitMonitoringStatusEnum, String>();
			scopeUnitMonitoringStatusTranslation.put(ScopeUnitMonitoringStatusEnum.FINISHED,
																							 Common.MESSAGES_MONITORING.statusFinished());
			scopeUnitMonitoringStatusTranslation.put(ScopeUnitMonitoringStatusEnum.IN_PROGRESS,
																							 Common.MESSAGES_MONITORING.statusInProgress());
			scopeUnitMonitoringStatusTranslation.put(ScopeUnitMonitoringStatusEnum.NOT_STARTED,
																							 Common.MESSAGES_MONITORING.statusNotStarted());
		}
		return scopeUnitMonitoringStatusTranslation;
	}

	private void bind()
	{
		// selection model
		display.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{

			@Override
			public void onSelectionChange(SelectionChangeEvent event)
			{
				ScopeUnitGlobalMonitoringDTO sugm = display.getSelectionModel().getSelectedObject();
		    if (!AccessRight.READ
		        .equals(SessionData
		            .getAccessRight(ApplicativeFunction.FUNCTION_GLOBAL_MONITORING))) {
  				if ((display.getSelectionModel().isSelected(sugm)) && (!sugm.getStatus()
  																																		.equals(ScopeUnitMonitoringStatusEnum.FINISHED)))
  				{
  					display.getButtonFinishScopeUnit().setEnabled(true);
  				}
  				else
  				{
  					display.getButtonFinishScopeUnit().setEnabled(false);
  				}
		    }
			}
		});
		// action
		display.getButtonHomeReturn().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				ManagementModuleEntryPoint.getHomePresenter().go(RootLayoutPanel.get());
			}
		});
		display.getButtonAcronym().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				acronymPresenter.go(display.getContentPanel());
			}
		});
		display.getButtonFinishScopeUnit().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				ScopeUnitGlobalMonitoringDTO scopeUnitMonitoring = display.getSelectionModel().getSelectedObject();
				// no child with unfinished tasks
				if (scopeUnitMonitoring.hasAllChildFinished())
				{
					// no unfinished tasks
					if (scopeUnitMonitoring.hasAllTaskFinished())
					{
						// no remaining scopeUnit
						if (remainingsScopeUnit.get(scopeUnitMonitoring.getUnitId()) == null
										|| remainingsScopeUnit.get(scopeUnitMonitoring.getUnitId()) == 0)
						{
							// information popup when there still is not
							// finished scopeUnitDiscipline
							if (scopeUnitMonitoring.hasAllScopeUnitDisciplineFinished())
							{
								finishScopeUnit(scopeUnitMonitoring.getUnitId());
							}
							else
							{
								display.getValidateFinishScopeUnit().getDialogPanel().center();
								display.getValidateFinishScopeUnit().getDialogPanel().show();
							}
						}
						else
						{
							display.getImpossibleFinishScopeUnitRemaining().getDialogPanel().center();
							display.getImpossibleFinishScopeUnitRemaining().getDialogPanel().show();
						}
					}
					else
					{
						display.getImpossibleFinishScopeUnitTask().getDialogPanel().center();
						display.getImpossibleFinishScopeUnitTask().getDialogPanel().show();
					}
				}
				else
				{
					display.getImpossibleFinishScopeUnitChild().getDialogPanel().center();
					display.getImpossibleFinishScopeUnitChild().getDialogPanel().show();
				}
			}

		});
		display.getButtonSave().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				updateRemainingsScopeUnit.retry(0);
			}
		});
		display.getButtonExportCSV().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				createCSVFormat(new ArrayList<ScopeUnitGlobalMonitoringDTO>(display.getDataProvider().getList()));
			}
		});
		// box
		display.getValidateFinishScopeUnit().getValidate().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				display.getValidateFinishScopeUnit().getDialogPanel().hide();
				finishScopeUnit(display.getSelectionModel().getSelectedObject().getUnitId());
			}
		});
		// filters
		display.getFilterLotParent().addChangeHandler(new ChangeHandler()
		{

			@Override
			public void onChange(ChangeEvent event)
			{
				filteredListByLotAndSubLot();
			}
		});
		display.getFilterLotParent().addKeyUpHandler(new KeyUpHandler()
		{

			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				filteredListByLotAndSubLot();
			}
		});
		display.getFilterLot().addChangeHandler(new ChangeHandler()
		{

			@Override
			public void onChange(ChangeEvent event)
			{
				filteredListByLotAndSubLot();
			}
		});
		display.getFilterLot().addKeyUpHandler(new KeyUpHandler()
		{

			@Override
			public void onKeyUp(KeyUpEvent event)
			{
				filteredListByLotAndSubLot();
			}
		});
		display.getFilterScopeUnitParent().addChangeHandler(new ChangeHandler() {

					@Override
					public void onChange(ChangeEvent event) {
						filteredListByStatusAndScopeUnitParent();
					}
				});
		display.getFilterScopeUnitParent().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				filteredListByStatusAndScopeUnitParent();
			}
		});
		display.getFilterStatus().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				filteredListByStatusAndScopeUnitParent();
			}
		});
		display.getFilterStatus().addKeyUpHandler(new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				filteredListByStatusAndScopeUnitParent();
			}
		});
		// event bus
		localEventBus.addHandler(UpdateGlobalAdvancementEvent.TYPE,
				new UpdateGlobalAdvancementEvent.Handler() {

					@Override
					public void onUpdateGlobalAdvancement(
							UpdateGlobalAdvancementEvent event) {
						refreshGlobalAdvancement();
						// Update the map of updated remaining scopeUnit
						remainingsScopeUnit.put(event.getUnitId(),
								Common.floatFormat(event.getRemaining(), 2));
					}
				});
	}

	/**
	 * This method disable all actions if mode if READ
	 */
	private void enableUI() {
		boolean bool = true;
		if (AccessRight.READ
				.equals(SessionData
						.getAccessRight(ApplicativeFunction.FUNCTION_GLOBAL_MONITORING))) {
			bool = false;
		}
		display.getButtonFinishScopeUnit().setEnabled(bool);
		display.getButtonSave().setEnabled(bool);
	}

	/**
 * 
 */
	private void refreshParentScopeUnitMonitorings() {
		parentScopeUnitMonitorings = new ArrayList<ScopeUnitGlobalMonitoringDTO>();
		// parent list is all scopeUnit which has no parent
		for (final ScopeUnitGlobalMonitoringDTO scopeUnitMonitoring : scopeUnitMonitoringsFiltered) {
			if (scopeUnitMonitoring.getParentScopeUnitName().equals(
					Common.EMPTY_TEXT)) {
				parentScopeUnitMonitorings.add(scopeUnitMonitoring);
			}
		}
		// refresh the parent filter
		refreshScopeUnitParentFilter();
		// refresh table list
		refreshList(parentScopeUnitMonitorings);
	}

	/**
	 * 
	 * Get the filters' value, filter and refresh the list. Only for lot,
	 * lotParent filter
	 */
	private void filteredListByLotAndSubLot() {
		final String lotParent = display.getFilterLotParent().getItemText(
				display.getFilterLotParent().getSelectedIndex());
		final String lot = display.getFilterLot().getItemText(
				display.getFilterLot().getSelectedIndex());
		Collection<ScopeUnitGlobalMonitoringDTO> filter = filtered1OnScopeUnitMonitoring(
				scopeUnitMonitorings, lotParent, lot);
		scopeUnitMonitoringsFiltered = new ArrayList<ScopeUnitGlobalMonitoringDTO>(
				filter);
		refreshParentScopeUnitMonitorings();
		refreshIndicators();
		filteredListByStatusAndScopeUnitParent();
	}

	/**
	 * Get the filters' value, filter and refresh the list. Only for scopeUnit,
	 * scopeUnitParent and status filter
	 */
	private void filteredListByStatusAndScopeUnitParent() {
		final String scopeUnitParent = display.getFilterScopeUnitParent()
				.getItemText(
						display.getFilterScopeUnitParent().getSelectedIndex());
		final String status = display.getFilterStatus().getItemText(
				display.getFilterStatus().getSelectedIndex());
		Collection<ScopeUnitGlobalMonitoringDTO> filter;
		// if scopeUnitParent filter is empty, filter on
		// parentScopeUnitMonitoring list
		if (scopeUnitParent.equals(Common.EMPTY_TEXT)) {
			filter = filtered2OnScopeUnitMonitoring(parentScopeUnitMonitorings,
					scopeUnitParent, status);
			display.displayParentDetails(null, null);
			display.removeRemainingScopeUnitColumn(false);
		}
		// if not, filter on filtered by lot scopeUnitMonitoring list
		else {
			filter = filtered2OnScopeUnitMonitoring(
					scopeUnitMonitoringsFiltered, scopeUnitParent, status);
			// display the parent details
			for (ScopeUnitGlobalMonitoringDTO parent : parentScopeUnitMonitorings) {
				if (parent.getScopeUnitName().equals(scopeUnitParent)) {
					Float f = 0f;
					// update remainingScopeUnit
					if (remainingsScopeUnit.get(parent.getUnitId()) != null) {
						f = remainingsScopeUnit.get(parent.getUnitId());
					}
					// db remainingScopeUnit
					else if (parent.getRemainingScopeUnit() != null) {
						f = parent.getRemainingScopeUnit();
					}
					display.displayParentDetails(parent, f);
					break;
				}
			}
			display.removeRemainingScopeUnitColumn(true);
		}
		refreshList(new ArrayList<ScopeUnitGlobalMonitoringDTO>(filter));
	}

   /**
    * Filter on scopeUnitMonitoring by Lot and SubLot
    * 
    * @param scopeUnitMonitorings
    * @param lotParent
    * @param lot
    * @return
    */
   private Collection<ScopeUnitGlobalMonitoringDTO> filtered1OnScopeUnitMonitoring(
         final List<ScopeUnitGlobalMonitoringDTO> scopeUnitMonitorings, final String lotParent,
         final String lot) {
		 return Collections2.filter(scopeUnitMonitorings, new Predicate<ScopeUnitGlobalMonitoringDTO>()
																{

																	@Override
																	public boolean apply(ScopeUnitGlobalMonitoringDTO input)
																	{
																		boolean ret = false;
																		if (!lotParent.equals(Common.EMPTY_TEXT))
																		{
																			if (!lot.equals(Common.EMPTY_TEXT))
																			{
																				if (input.getParentLotName().equals(lotParent) && input.getLotName()
																																															 .equals(lot))
																				{
																					ret = true;
																				}
																			}
																			else
																			{
																				if (input.getParentLotName().equals(lotParent) || input.getLotName()
																																															 .equals(lotParent))
																				{
																					ret = true;
																				}
																			}
																		}
																		else
																		{
																			if (!lot.equals(Common.EMPTY_TEXT))
																			{
																				if (input.getLotName().equals(lot))
																				{
																					ret = true;
																				}

																			}
																			else
																			{
																				ret = true;

																			}
																		}
																		return ret;
																	}

																});
	 }

	private Collection<ScopeUnitGlobalMonitoringDTO> filtered2OnScopeUnitMonitoring(
			final List<ScopeUnitGlobalMonitoringDTO> scopeUnitMonitorings,
			final String scopeUnitParent, final String status) {
		return Collections2.filter(
				scopeUnitMonitorings,
				new Predicate<ScopeUnitGlobalMonitoringDTO>() {

					@Override
					public boolean apply(ScopeUnitGlobalMonitoringDTO input) {
						boolean ret = false;
						if (!status.equals(Common.EMPTY_TEXT)) {
							if (!scopeUnitParent.equals(Common.EMPTY_TEXT)) {
								if (input.getStatus().getLabel().equals(status)
										&& input.getParentScopeUnitName()
												.equals(scopeUnitParent)) {
									ret = true;
								}
							} else {
								if (input.getStatus().getLabel().equals(status)) {
									ret = true;
								}
							}
						} else {
							if (!scopeUnitParent.equals(Common.EMPTY_TEXT)) {
								if (input.getParentScopeUnitName().equals(
										scopeUnitParent)) {
									ret = true;
								}
							} else {
								ret = true;

							}
						}
						return ret;
					}
				});
	}

	/**
	 * Refresh the displayed scopeUnitMonitoring table
	 * 
	 * @param list
	 */
	private void refreshList(List<ScopeUnitGlobalMonitoringDTO> list) {
		Collections.sort(list, display.getScopeUnitComparator());
		display.getDataProvider().flush();
		display.getDataProvider().setList(list);
		display.updateSortHandler();
	}

	/**
	 * Refresh all indicators with the scopeUnitMonitoringFiltered list
	 * 
	 * @param list
	 */
	private void refreshIndicators() {
		/*
		 * Default values
		 */
		Integer scopeUnitFinished = 0;
		Integer totalScopeUnit = Integer.valueOf(
				scopeUnitMonitoringsFiltered.size());
		String percentScopeUnitFinished = Common.EMPTY_TEXT;
		String globalAdvancement = Common.EMPTY_TEXT;
		String scopeUnitToDo = Common.EMPTY_TEXT;
		/*
		 * Count
		 */
		if (totalScopeUnit > 0) {
			// finished scopeUnit & global advancement
			float totalConsumed = 0f;
			float totalReestimate = 0f;
			float f1;
			for (ScopeUnitGlobalMonitoringDTO scopeUnitMonitoring : scopeUnitMonitoringsFiltered) {
				if (scopeUnitMonitoring.getStatus().equals(
						ScopeUnitMonitoringStatusEnum.FINISHED)) {
					scopeUnitFinished++;
				}
				if (scopeUnitMonitoring.getParentScopeUnitId() == null ) {
					totalConsumed += scopeUnitMonitoring.getConsumed();
					totalReestimate += scopeUnitMonitoring.getReestimate();					
				}

			}
			if (totalReestimate != 0f) {
				f1 = totalConsumed * 100 / totalReestimate;
			} else {
				f1 = 100f;
			}
			globalAdvancement = Common.floatFormat(f1, 2).toString() + "%";
			// percent scopeUnit done
			float f2 = scopeUnitFinished * 100 / totalScopeUnit;
			f2 = Common.floatFormat(f2, 1);
			percentScopeUnitFinished = Integer.valueOf((int) f2).toString() + "%";
			// to do scopeUnit
			scopeUnitToDo = Integer.valueOf(totalScopeUnit - scopeUnitFinished)
					.toString();
		}

		/*
		 * Indicators
		 */
		display.getIndicatorScopeUnitNumber()
				.setText(totalScopeUnit.toString());
		display.getIndicatorPercentScopeUnitDone().setText(
				percentScopeUnitFinished);
		display.getIndicatorScopeUnitToDo().setText(scopeUnitToDo);
		display.getIndicatorGlobalAdvancement().setText(globalAdvancement);

	}

	/**
	 * Refresh the global advancement indicator
	 * 
	 * @param list
	 */
	private void refreshGlobalAdvancement() {
		List<ScopeUnitGlobalMonitoringDTO> list;
		// filtered by lot
		if (scopeUnitMonitoringsFiltered != null) {
			list = scopeUnitMonitoringsFiltered;
		}
		// not filtered
		else {
			list = scopeUnitMonitorings;
		}
		String globalAdvancement = Common.EMPTY_TEXT;
		Integer totalScopeUnit = Integer.valueOf(list.size());
		if (totalScopeUnit > 0) {
			float f1 = 0;
			for (ScopeUnitGlobalMonitoringDTO scopeUnitMonitoring : list) {
				f1 += scopeUnitMonitoring.getAdvancement();
			}
			f1 = Common.floatFormat(f1 / totalScopeUnit * 100, 1);
			globalAdvancement = Integer.valueOf((int) f1).toString() + "%";
		}
		display.getIndicatorGlobalAdvancement().setText(globalAdvancement);
	}

	/**
	 * Reset the selectionModel
	 */
	private void resetSelectionModel() {
		if (display.getSelectionModel().getSelectedObject() != null) {
			display.getSelectionModel().setSelected(
					display.getSelectionModel().getSelectedObject(), false);
		}
	}

	/**
	 * Create the filter of lot
	 */
	private void createLotsFilter() {
		display.getFilterLotParent().clear();
		display.getFilterLot().clear();
		display.getFilterLotParent().addItem(Common.EMPTY_TEXT);
		display.getFilterLot().addItem(Common.EMPTY_TEXT);
		for (LotDTO lot : lots) {
			display.getFilterLotParent().addItem(lot.getName());
			if (lot.getChilds() != null && lot.getChilds().size() > 0) {
				for (final LotDTO subLot : lot.getChilds()) {
					display.getFilterLot().addItem(subLot.getName());
				}
			}
		}
	}

	/**
	 * Refresh the filter of scopeUnit parent with the
	 * scopeUnitMonitoringFiltered list
	 */
	private void refreshScopeUnitParentFilter() {
		boolean enable = false;
		display.getFilterScopeUnitParent().clear();
		display.getFilterScopeUnitParent().addItem(Common.EMPTY_TEXT);
		List<String> parentScopeUnitNames = new ArrayList<String>();
		for (final ScopeUnitMonitoringDTO parentScopeUnit : parentScopeUnitMonitorings) {
			enable = true;
			for (final ScopeUnitMonitoringDTO scopeUnit : scopeUnitMonitoringsFiltered) {
				if (scopeUnit.getParentScopeUnitId().equals(parentScopeUnit.getUnitId())) {
					parentScopeUnitNames.add(parentScopeUnit.getScopeUnitName());
					break;
				}
			}			
		}
		for (String parentScopeUnitName : parentScopeUnitNames) {
			display.getFilterScopeUnitParent().addItem(parentScopeUnitName);
		}
		display.getFilterScopeUnitParent().setEnabled(enable);
	}

	/**
	 * Create the filter of status
	 */
	private void createStatus() {
		display.getFilterStatus().clear();
		display.getFilterStatus().addItem(Common.EMPTY_TEXT);
		display.getFilterStatus().addItem(
				ScopeUnitMonitoringStatusEnum.IN_PROGRESS.getLabel());
		display.getFilterStatus().addItem(
				ScopeUnitMonitoringStatusEnum.FINISHED.getLabel());
		display.getFilterStatus().addItem(
            ScopeUnitMonitoringStatusEnum.NOT_STARTED.getLabel());
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public void loadDataOnSelectionTab() {
		getLots.retry(0);
		getScopeUnits.retry(0);
		getGlobalMonitoringInformations.retry(0);
		display.displayParentDetails(null, null);
	}

	@Override
	public IsWidget getDisplay() {
		return display.asWidget();
	}

}
