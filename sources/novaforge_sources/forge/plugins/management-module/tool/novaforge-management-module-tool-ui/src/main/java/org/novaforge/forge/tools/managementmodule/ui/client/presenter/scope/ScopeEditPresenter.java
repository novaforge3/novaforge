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

package org.novaforge.forge.tools.managementmodule.ui.client.presenter.scope;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.event.scope.ScopeUnitEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.Presenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.service.scope.ScopeServiceAsync;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.ScopeEditView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.scope.ScopeEditViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author vvigo
 */
public class ScopeEditPresenter implements Presenter {

	private final ScopeServiceAsync scopeService;
	private final SimpleEventBus eventBus;
	private final ScopeEditView display;

	private String projectId;
	private Long projectPlanId;
	private ViewEnum mode;
	private Map<String, Long> lotMap;
	private ScopeUnitDTO currentScope;

	public ScopeEditPresenter(final ScopeServiceAsync scopeService, final SimpleEventBus eventBus,
			final Long pProjectPlanId, final String projectId, final ScopeUnitDTO curentScope,
			final ViewEnum pMode) {
		super();
		this.scopeService = scopeService;
		this.eventBus = eventBus;
		this.display = new ScopeEditViewImpl(pMode);
		this.mode = pMode;
		this.projectPlanId = pProjectPlanId;
		this.projectId = projectId;
		this.currentScope = curentScope;

		bind();
	}

	public void bind() {
		ScopeEditPresenter.this.display.getButtonSave().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (ViewEnum.ADD.equals(mode)) {
					createScopeUnit();
				} else {
					editScopeUnit();
				}
			}
		});
		ScopeEditPresenter.this.display.getButtonCancel().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				cancelCreateScope();
			}
		});

		ScopeEditPresenter.this.display.getScopeLotLB().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String completeLotName = display.getScopeLotLB().getItemText(
						display.getScopeLotLB().getSelectedIndex());

				if (completeLotName.equals("")) {
					display.getParentScopeLB().clear();
					display.getParentScopeLB().setEnabled(false);

				} else {
					refreshParentScope(projectPlanId, lotMap.get(completeLotName));
				}
			}
		});
	}

	private void createScopeUnit() {
		final ScopeUnitDTO newScope = getScopeUnit();
		new AbstractManagementRPCCall<ScopeUnitDTO>()
		{
			@Override
			protected void callService(AsyncCallback<ScopeUnitDTO> pCb) {
				ScopeEditPresenter.this.scopeService.createScopeUnit(newScope, projectId, pCb);
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(ScopeUnitDTO pResult) {
				if (pResult != null) {
				   ScopeEditPresenter.this.display.closeWidget();
					eventBus.fireEvent(new ScopeUnitEvent());
				}
			}


		}.retry(0);
	}

	private void editScopeUnit() {
		final ScopeUnitDTO editedScope = getScopeUnit();
		new AbstractManagementRPCCall<ScopeUnitDTO>()
		{
			@Override
			protected void callService(AsyncCallback<ScopeUnitDTO> pCb) {
				ScopeEditPresenter.this.scopeService.editScopeUnit(editedScope, projectId, pCb);
			}

			@Override
			public void onSuccess(ScopeUnitDTO pResult) {
				if (pResult != null) {
					eventBus.fireEvent(new ScopeUnitEvent());
					ScopeEditPresenter.this.display.closeWidget();
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ErrorManagement.displayErrorMessage(caught);
			}
		}.retry(0);
	}

	private void cancelCreateScope() {
		ScopeEditPresenter.this.display.closeWidget();
	}

	private void refreshParentScope(final Long projectPlanId, final Long lotId)
	{
		new AbstractManagementRPCCall<List<ScopeUnitDTO>>()
		{
			@Override
			protected void callService(AsyncCallback<List<ScopeUnitDTO>> pCb)
			{
				ScopeEditPresenter.this.scopeService.getProjectPlanScope(projectPlanId, lotId, false, false, pCb);
			}

			@Override
			public void onSuccess(List<ScopeUnitDTO> pResult)
			{

				resetParentScopeLB();

				if (pResult != null && pResult.size() > 0)
				{
					int index = 0;
					for (ScopeUnitDTO itScopeUnitDTO : pResult)
					{
						String scopeName = itScopeUnitDTO.getName();
						if (itScopeUnitDTO.getParentScopeUnit() != null || (currentScope != null && itScopeUnitDTO.getUnitId()
																																																			.equalsIgnoreCase(currentScope
																																																														.getUnitId())))
						{
							continue;
						}
						display.getParentScopeLB().addItem(scopeName, itScopeUnitDTO.getUnitId(), itScopeUnitDTO);
						if (currentScope != null && ViewEnum.EDIT.equals(mode) && currentScope.getParentScopeUnit() != null
										&& currentScope.getParentScopeUnit().getUnitId().equalsIgnoreCase(itScopeUnitDTO.getUnitId()))
						{
							index = display.getParentScopeLB().getItemCount() - 1;
							if (currentScope.isManual() && currentScope.getListTaskId().isEmpty())
							{
								display.getParentScopeLB().setEnabled(true);
							}
						}
					}
					display.getParentScopeLB().setSelectedIndex(index);
				}
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}
		}.retry(0);
	}

	private ScopeUnitDTO getScopeUnit()
	{
		ScopeUnitDTO scope = new ScopeUnitDTO();

		if (currentScope != null)
		{
			scope.setUnitId(currentScope.getUnitId());
		}

		scope.setName(ScopeEditPresenter.this.display.getScopeNameTB().getValue());

		scope.setDescription(ScopeEditPresenter.this.display.getScopeDescriptionTB().getValue());

		scope.setVersion(ScopeEditPresenter.this.display.getScopeVersionTB().getValue());

		String completeLotName = display.getScopeLotLB().getItemText(display.getScopeLotLB().getSelectedIndex());

		scope.setLotName(completeLotName);

		if (lotMap.get(completeLotName) != null)
		{
			scope.setLotId(lotMap.get(completeLotName).toString());
			scope.setType(display.getScopeTypeLB().getItemText(display.getScopeTypeLB().getSelectedIndex()));
		}
		if (display.getParentScopeLB().getSelectedIndex() != -1)
		{
			scope.setParentScopeUnit(display.getParentScopeLB().getSelectedAssociatedObject());
		}

		return scope;
	}

	private void resetParentScopeLB()
	{

		display.getParentScopeLB().clear();
		display.getParentScopeLB().addItem("", "none", null);
		display.getParentScopeLB().setSelectedIndex(0);
	}

	@Override
	public void go(final HasWidgets container)
	{
		ScopeEditPresenter.this.display.showWidget();
	}

	@Override
	public IsWidget getDisplay() {
		return this.display.asWidget();
	}

	public void setParams(String projectId, Long projectPlanId, ScopeUnitDTO currentScope, ViewEnum mode)
	{
		this.mode = mode;
		this.projectId = projectId;
		this.projectPlanId = projectPlanId;
		this.currentScope = currentScope;
		if (currentScope == null)
		{
			this.display.setMode(mode, null);
		}
		else
		{
			if (currentScope.isManual())
			{
				this.display.setMode(mode, true);
			}
			else
			{
				this.display.setMode(mode, false);
			}
		}

		refreshDatas();
	}

	/**
	 *
	 */
	private void refreshDatas()
	{
		refreshLots(projectPlanId);
		getScopeTypeList();

		displayScopeUnit();
		enableFields();
	}

	private void refreshLots(final Long projectPlanId) {
		new AbstractManagementRPCCall<List<LotDTO>>()
		{
			@Override
			protected void callService(AsyncCallback<List<LotDTO>> pCb) {
				ScopeEditPresenter.this.scopeService.getLotList(projectPlanId, pCb);
			}

			@Override
			public void onSuccess(List<LotDTO> pResult) {
				if (pResult != null) {
					lotMap = new HashMap<String, Long>();
					display.getScopeLotLB().clear();
					display.getScopeLotLB().addItem("");
					int index = 0;
					for (LotDTO itLotDTO : pResult) {
						String lotName = "";
						if (itLotDTO.getParentLotName() != null) {
							lotName = itLotDTO.getParentLotName() + " - ";
						}
						lotName = lotName + itLotDTO.getName();
						display.getScopeLotLB().addItem(lotName);
						if (currentScope != null && ViewEnum.EDIT.equals(mode)) {
							// If Scope is linked to a subLot
							if (currentScope.getLotId() != null
									&& currentScope.getLotId().equals(itLotDTO.getLotId().toString())) {
								index = display.getScopeLotLB().getItemCount() - 1;
								refreshParentScope(projectPlanId, Long.valueOf((currentScope.getLotId())));
							}
						}
						lotMap.put(lotName, itLotDTO.getLotId());
					}
					display.getScopeLotLB().setSelectedIndex(index);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ErrorManagement.displayErrorMessage(caught);
			}
		}.retry(0);
	}

	private void getScopeTypeList() {
		new AbstractManagementRPCCall<List<String>>()
		{
			@Override
			protected void callService(AsyncCallback<List<String>> pCb) {
				ScopeEditPresenter.this.scopeService.getScopeTypeList(pCb);
			}

			@Override
			public void onSuccess(List<String> pResult) {
				if (pResult != null) {
					display.getScopeTypeLB().clear();
					int index = 0;
					for (String itType : pResult) {
						display.getScopeTypeLB().addItem(itType);
						if (currentScope != null && ViewEnum.EDIT.equals(mode)
								&& currentScope.getType().equals(itType)) {
							index = display.getScopeTypeLB().getItemCount() - 1;
						}
					}
					display.getScopeTypeLB().setSelectedIndex(index);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				ErrorManagement.displayErrorMessage(caught);
			}
		}.retry(0);
	}

	private void displayScopeUnit()
	{
		if (currentScope != null)
		{
			display.getScopeNameTB().setValue(currentScope.getName());
			display.getScopeDescriptionTB().setValue(currentScope.getDescription());
			display.getScopeVersionTB().setValue(currentScope.getVersion());
		}
		else
		{
			display.getScopeNameTB().setValue(null);
			display.getScopeDescriptionTB().setValue(null);
			display.getScopeVersionTB().setValue(null);
			resetParentScopeLB();
		}
	}
	
	/**
	 * Enable fields
	 */
	private void enableFields(){
	   if (currentScope != null){
	      if (currentScope.getParentScopeUnit() != null){
	         display.getScopeLotLB().setEnabled(false);
	      }else{
	         display.getScopeLotLB().setEnabled(true);
	      }
	      if (currentScope.getChildrenScopeUnit() != null && currentScope.getChildrenScopeUnit().size() > 0){
	         display.getParentScopeLB().setEnabled(false);
	      }else{
	         display.getParentScopeLB().setEnabled(true);
	      }
	      if (currentScope.isManual()){
	         display.getScopeNameTB().setEnabled(true);
	         display.getScopeDescriptionTB().setEnabled(true);
	         display.getScopeVersionTB().setEnabled(true);
	         display.getScopeTypeLB().setEnabled(true);
	      }else{
	         display.getScopeNameTB().setEnabled(false);
	         display.getScopeDescriptionTB().setEnabled(false);
	         display.getScopeVersionTB().setEnabled(false);
	         display.getScopeTypeLB().setEnabled(false);
	         display.getParentScopeLB().setEnabled(false);
	      }
	   }else{
	      display.getScopeNameTB().setEnabled(true);
         display.getScopeDescriptionTB().setEnabled(true);
         display.getScopeVersionTB().setEnabled(true);
         display.getScopeLotLB().setEnabled(true);
         display.getParentScopeLB().setEnabled(true);
         display.getScopeTypeLB().setEnabled(true);
	   }
	}
}
