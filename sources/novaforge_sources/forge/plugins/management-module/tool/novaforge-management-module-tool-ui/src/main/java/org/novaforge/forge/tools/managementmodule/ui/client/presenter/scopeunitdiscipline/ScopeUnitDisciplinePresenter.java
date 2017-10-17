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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.scopeunitdiscipline;

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
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowScopeUnitDisciplineViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scopeunitdiscipline.ScopeUnitDisciplineView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scopeunitdiscipline.ScopeUnitDisciplineViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineStatusEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Bilet-jc
 * 
 */
public class ScopeUnitDisciplinePresenter implements TabPresenter {

	private final SimpleEventBus scopeUnitDisciplineEventBus;
	private final ScopeUnitDisciplineView display;
	/**
	 * Call service to get the current iteration
	 */
	AbstractManagementRPCCall<IterationDTO> getCurrentIteration = new AbstractManagementRPCCall<IterationDTO>()
	{

		@Override
		protected void callService(AsyncCallback<IterationDTO> pCb)
		{
			Common.ITERATION_SERVICE.getCurrentOrLastFinishedIteration(SessionData.currentValidatedProjectPlanId, true, pCb);
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}

		@Override
		public void onSuccess(IterationDTO pResult)
		{
			display.setCurrentIteration(pResult);
		}

	};
	private StartScopeUnitDisciplinePresenter startScopeUnitOnDisciplinePresenter;
	/**
	 * RPC call service to get the list of ScopeUnitDiscipline
	 */
	AbstractManagementRPCCall<Set<ScopeUnitDisciplineDTO>> getScopeUnitDisciplines = new AbstractManagementRPCCall<Set<ScopeUnitDisciplineDTO>>()
	{

		@Override
		protected void callService(AsyncCallback<Set<ScopeUnitDisciplineDTO>> pCb)
		{
			Common.TASK_SERVICE.getScopeUnitDiscipline(SessionData.currentValidatedProjectPlanId, pCb);
		}

		@Override
		public void onSuccess(Set<ScopeUnitDisciplineDTO> pResult)
		{
			if (pResult != null)
			{
				List<ScopeUnitDisciplineDTO> list = new ArrayList<ScopeUnitDisciplineDTO>(pResult);
				refreshList(list);
				startScopeUnitOnDisciplinePresenter.setScopeUnitDisciplineList(list);
			}
		}

		@Override
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}
	};

	/**
	 * @param globalEventBus
	 */
	public ScopeUnitDisciplinePresenter()
	{
		super();
		display = new ScopeUnitDisciplineViewImpl();
		scopeUnitDisciplineEventBus = new SimpleEventBus();
		startScopeUnitOnDisciplinePresenter = new StartScopeUnitDisciplinePresenter(scopeUnitDisciplineEventBus);
		bind();
		enableUI();
	}

	private void bind()
	{
		/* selection model */
		display.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{

			@Override
			public void onSelectionChange(SelectionChangeEvent event)
			{
				/* check the selectionModel has selected object */
				ScopeUnitDisciplineDTO sud    = display.getSelectionModel().getSelectedObject();
				boolean                enable = false;
				if (sud != null && display.getSelectionModel().isSelected(sud) && !ScopeUnitDisciplineStatusEnum.CLOSED
																																							 .getFunctionnalId()
																																							 .equals(sud.getStatus()
																																													.getFunctionalId()))
				{
					enable = true;
				}
				display.getStopScopeUnitDisciplineButton().setEnabled(enable);
				display.getCancelScopeUnitDisciplineButton().setEnabled(enable);
			}

		});
		/* bus */
		scopeUnitDisciplineEventBus.addHandler(ShowScopeUnitDisciplineViewEvent.TYPE,
																					 new ShowScopeUnitDisciplineViewEvent.Handler()
																					 {

																						 @Override
																						 public void onShowScopeUnitDisciplineView(ShowScopeUnitDisciplineViewEvent event)
																						 {
																							 loadDatas();
																						 }
																					 });
		/* buttons action */
		display.getStartScopeUnitDisciplineButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				startScopeUnitOnDisciplinePresenter.go(display.getContentPanel());
			}
		});
		display.getCancelScopeUnitDisciplineButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				cancelScopeUnitDiscipline(display.getSelectionModel().getSelectedObject().getScopeUnit().getUnitId(),
																	display.getSelectionModel().getSelectedObject().getDiscipline().getFunctionalId());
			}
		});
		display.getStopScopeUnitDisciplineButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				terminateScopeUnitDiscipline(display.getSelectionModel().getSelectedObject());

			}
		});
		display.getButtonHomeReturn().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				ManagementModuleEntryPoint.getHomePresenter().go(RootLayoutPanel.get());
			}
		});

	}

	/**
	 * This method disable all actions if mode if READ
	 */
	private void enableUI()
	{
		boolean bool = true;
		if (AccessRight.READ.equals(SessionData.getAccessRight(ApplicativeFunction.FUNCTION_PREPARATION_ITERATION)))
		{
			bool = false;
		}
		display.getStartScopeUnitDisciplineButton().setEnabled(bool);
		display.getCancelScopeUnitDisciplineButton().setEnabled(bool);
		display.getStopScopeUnitDisciplineButton().setEnabled(bool);
	}

	public void loadDatas()
	{
		if (display.getSelectionModel().getSelectedObject() != null)
		{
			display.getSelectionModel().setSelected(display.getSelectionModel().getSelectedObject(), false);
		}
		display.getCancelScopeUnitDisciplineButton().setEnabled(false);
		display.getStopScopeUnitDisciplineButton().setEnabled(false);
		getScopeUnitDisciplines.retry(0);
		getCurrentIteration.retry(0);
	}

	/**
	 * RPC call service to cancel a ScopeUnitDiscipline
	 */
	private void cancelScopeUnitDiscipline(final String scopeUnitId, final String discipline)
	{
		new AbstractManagementRPCCall<Boolean>()
		{

			@Override
			protected void callService(AsyncCallback<Boolean> pCb)
			{
				Common.TASK_SERVICE.deleteScopeUnitDiscipline(scopeUnitId, discipline, pCb);
			}

			@Override
			public void onSuccess(Boolean pResult)
			{
				InfoDialogBox box;
				if (pResult)
				{
					loadDatas();
					box = new InfoDialogBox(Common.MESSAGES_BACKLOG.successCancelScopeUnitDisciplineMessage(), InfoTypeEnum.OK);
				}
				else
				{
					box = new InfoDialogBox(Common.MESSAGES_BACKLOG.failCancelScopeUnitDisciplineMessage(), InfoTypeEnum.OK);

				}
				box.getDialogPanel().center();
				box.getDialogPanel().show();
			}

			@Override
			public void onFailure(Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}
		}.retry(0);
	}

	/**
	 * RPC call service to end a ScopeUnitDiscipline
	 */
	private void terminateScopeUnitDiscipline(final ScopeUnitDisciplineDTO scopeUnitDiscipline) {
		new AbstractManagementRPCCall<Boolean>()
		{

			@Override
			protected void callService(AsyncCallback<Boolean> pCb)
			{
				Common.TASK_SERVICE.terminateScopeUnitDiscipline(scopeUnitDiscipline.getScopeUnit().getUnitId(),
																												 scopeUnitDiscipline.getDiscipline().getFunctionalId(), pCb);
			}

			@Override
			public void onFailure(Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}

			@Override
			public void onSuccess(Boolean pResult) {
				InfoDialogBox box;
				if (pResult) {
					loadDatas();
					box = new InfoDialogBox(Common.MESSAGES_BACKLOG.successTerminateScopeUnitDisciplineMessage(),
							InfoTypeEnum.OK);
				} else
				{
					box = new InfoDialogBox(Common.MESSAGES_BACKLOG.failTerminateScopeUnitDisciplineMessage(),
																	InfoTypeEnum.WARNING);
				}
				box.getDialogPanel().center();
				box.getDialogPanel().show();
			}


		}.retry(0);
	}

	/**
	 * Refresh the ScopeUnitDiscipline list in the view
	 *
	 * @param pList
	 */

	public void refreshList(List<ScopeUnitDisciplineDTO> pList)
	{
		display.getDataProvider().setList(pList);
		display.updateSortHandler();
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public void loadDataOnSelectionTab()
	{
		loadDatas();
	}

	@Override
	public IsWidget getDisplay()
	{
		return display.asWidget();
	}

}
