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
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.ShowTaskEditViewEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.task.TaskEditPresenter.Mode;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scopeunitdiscipline.ScopeUnitDisciplinePopupView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scopeunitdiscipline.ScopeUnitDisciplinePopupViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineStatusEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.TaskDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Bilet-jc
 * 
 *         Popup presenter of ScopeUnitDiscipline selection before creating task
 *         in an iteration
 */
public class ScopeUnitDisciplinePopupPresenter implements Presenter {

	private final ScopeUnitDisciplinePopupView display;
	/* bus event */
	private final SimpleEventBus globalEventBus;
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
		public void onFailure(Throwable pCaught)
		{
			ErrorManagement.displayErrorMessage(pCaught);
		}

		@Override
		public void onSuccess(Set<ScopeUnitDisciplineDTO> pResult)
		{
			if (pResult != null)
			{
				List<ScopeUnitDisciplineDTO> list = new ArrayList<ScopeUnitDisciplineDTO>();
				for (ScopeUnitDisciplineDTO scopeUnitDiscipline : pResult)
				{
					if (!scopeUnitDiscipline.getStatus().getFunctionalId().equals(ScopeUnitDisciplineStatusEnum.CLOSED
																																						.getFunctionnalId()))
					{
						list.add(scopeUnitDiscipline);
					}
				}
				refreshList(list);
			}
		}
	};
	private TaskDTO                task;
	private ScopeUnitDisciplineDTO selectedScopeUnitDiscipline;

	public ScopeUnitDisciplinePopupPresenter(SimpleEventBus globalEventBus)
	{
		super();
		this.globalEventBus = globalEventBus;
		display = new ScopeUnitDisciplinePopupViewImpl();
		bind();
	}

	private void bind()
	{
		display.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler()
		{

			@Override
			public void onSelectionChange(SelectionChangeEvent event)
			{
				display.getValidateButton().setEnabled(true);
				selectedScopeUnitDiscipline = display.getSelectionModel().getSelectedObject();
			}
		});
		display.getValidateButton().addClickHandler(new ClickHandler()
		{

			@Override
			public void onClick(ClickEvent event)
			{
				final ShowTaskEditViewEvent showTaskEditViewEvent = new ShowTaskEditViewEvent();
				task.setScopeUnit(selectedScopeUnitDiscipline.getScopeUnit());
				task.setDiscipline(selectedScopeUnitDiscipline.getDiscipline());
				showTaskEditViewEvent.setTask(task);
				showTaskEditViewEvent.setMode(Mode.CREATION);
				globalEventBus.fireEvent(showTaskEditViewEvent);
				display.hideWidget();
			}
		});
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
	public void go(HasWidgets container)
	{
		getScopeUnitDisciplines.retry(0);
		display.showWidget();
	}

	@Override
	public IsWidget getDisplay()
	{
		return display;
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
