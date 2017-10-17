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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import org.novaforge.forge.tools.managementmodule.ui.client.event.LotReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowListEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.Validator;
import org.novaforge.forge.tools.managementmodule.ui.client.view.lotsettings.LotSettingsCreationView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.lotsettings.LotSettingsCreationViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LotEditPresenter implements TabPresenter {
	private static final String EMPTY_TEXT = "";
	private final SimpleEventBus eventBus;
	private final LotSettingsCreationView display;
	private final String NO_PARENT_LOT = "-1";
	private ViewEnum viewMode;
	private ProjectPlanDTO projectPlanDTO;
	private LotDTO lotDTO;
	private Long lotId;

	public LotEditPresenter(final SimpleEventBus eventBus, final ProjectPlanDTO projectPlanDTO) {
		super();
		this.eventBus = eventBus;
		this.display = new LotSettingsCreationViewImpl();
		this.projectPlanDTO = projectPlanDTO;
		this.viewMode = getAppropriateViewModeFromProjectPlanStatus();
		bind();
	}

	@Override
	public void go(HasWidgets container) {
		container.clear();
		container.add(this.display.asWidget());
	}

	public final void bind() {
		display.getValidateDialogBox().getValidate()
				.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent event) {
						executeActionAddSaveButton();
					}
				});
		display.getButtonAddLot().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				display.getValidateDialogBox().setDetails(
						Common.getProjectPlanMessages()
								.addValidationCreateLotMessage());
				validateActionOnAddSaveButton();
			}
		});

		display.getButtonSaveLot().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				display.getValidateDialogBox().setDetails(
						Common.getProjectPlanMessages()
								.addValidationCreateLotMessage());
				validateActionOnAddSaveButton();
			}
		});

		display.getButtonCancelLot().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				eventBus.fireEvent(new ShowListEvent(ShowListEvent.EnumList.LOT_LIST));
			}
		});

		display.getLotNameValidator().setValidator(new Validator() {

			@Override
			public boolean isValid(String pValue) {
				return !(pValue == null || EMPTY_TEXT.equals(pValue.trim()));
			}

			@Override
			public String getErrorMessage() {
				return Common.getGlobal().messageRequiredField();
			}
		});

				
		display.getLotStartDatePicker().setValidator(new Validator() {

			@Override
			public boolean isValid(String pValue) {
			  boolean result = (!(pValue == null || EMPTY_TEXT.equals(pValue)));
			  if ( result )
			  {
			    display.getLotEndDatePicker().setCurrentMonth( display.getLotStartDatePicker().getValue() );
			  }
				return result;
			}

			@Override
			public String getErrorMessage() {
				return Common.getGlobal().messageRequiredField();
			}
		});

		display.getLotEndDatePicker().setValidator(new Validator() {

			@Override
			public boolean isValid(String pValue) {
				return (!(pValue == null || EMPTY_TEXT.equals(pValue)));
			}

			@Override
			public String getErrorMessage() {
				return Common.getGlobal().messageRequiredField();
			}
		});

		display.getLotDescValidator().setValidator(new Validator() {

			@Override
			public boolean isValid(String pValue) {
				boolean valid = false;
				if ((pValue != null) && (!EMPTY_TEXT.equals(pValue.trim())) && pValue.trim().length() < 2000)
				{
					valid = true;
				}
				return valid;
			}

			@Override
			public String getErrorMessage() {
				return Common.getGlobal().messageRequiredFieldOrDataToLong();
			}
		});
	}

	void validateActionOnAddSaveButton() {
		if (display.getLotNameValidator().isValid()
				&& display.getLotStartDatePicker().isValid()
				&& display.getLotEndDatePicker().isValid()
				&& display.getLotDescValidator().isValid()) {
		   //creation
			if (editionIsACreation()) {
				display.getValidateDialogBox().getDialogPanel().center();
				display.getValidateDialogBox().getDialogPanel().show();
			} else {
				executeActionAddSaveButton();
			}
		} else {
			display.getLotNameValidator().isValid();
			display.getLotStartDatePicker().isValid();
			display.getLotEndDatePicker().isValid();
			display.getLotDescValidator().isValid();
			InfoDialogBox info = new InfoDialogBox(Common
					.getProjectPlanMessages().lotErrorValidation(),
					InfoTypeEnum.KO);
			info.getDialogPanel().center();
			info.getDialogPanel().show();
		}
	}

	void executeActionAddSaveButton() {
		majLotDTOFromForm();
		if (editionIsACreation()) {
			creeteLot();
		} else {
			saveLot();
		}
		display.getValidateDialogBox().getDialogPanel().hide();
	}

	private void creeteLot() {
		new AbstractManagementRPCCall<LotDTO>()
		{
			@Override
         protected void callService(AsyncCallback<LotDTO> pCb) {
            Common.PROJECT_PLAN_SERVICE.creationLot(lotDTO.getName(), lotDTO.getStartDate(),
                  lotDTO.getEndDate(), lotDTO.getDesc(), lotDTO.getpPlanId(), lotDTO.getParentLotId(),
                  pCb);
         }

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}

			@Override
			public void onSuccess(LotDTO pResult) {
				InfoDialogBox info = new InfoDialogBox(Common
						.getProjectPlanMessages().createLotSuccessed(),
						InfoTypeEnum.OK);
				info.getDialogPanel().center();
				info.getDialogPanel().show();
				if (pResult != null) {
					lotDTO = pResult;
					loadFormFromLotDTO();
					eventBus.fireEvent(new ShowListEvent(ShowListEvent.EnumList.LOT_LIST));
					eventBus.fireEvent(new LotReferentialModifiedEvent());
				}
			}


		}.retry(0);
	}

	private void saveLot() {
		new AbstractManagementRPCCall<Boolean>()
		{
			@Override
         protected void callService(AsyncCallback<Boolean> pCb) {
            Common.PROJECT_PLAN_SERVICE.saveLot(lotDTO.getLotId(), lotDTO, pCb);
         }

			@Override
			public void onSuccess(Boolean pResult) {
				InfoDialogBox info = new InfoDialogBox(Common
						.getProjectPlanMessages().saveLotSuccessed(),
						InfoTypeEnum.OK);
				info.getDialogPanel().center();
				info.getDialogPanel().show();
				if (pResult != null) {
					eventBus.fireEvent(new ShowListEvent(ShowListEvent.EnumList.LOT_LIST));
					eventBus.fireEvent(new LotReferentialModifiedEvent());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
			   ErrorManagement.displayErrorMessage(caught);
			}
		}.retry(0);
	}

	private void majLotDTOFromForm() {
		lotDTO.setName(display.getLotNameValidator().getValue());
		lotDTO.setDesc(display.getLotDescValidator().getValue());
		lotDTO.setEndDate(display.getLotEndDatePicker().getValue());
		lotDTO.setStartDate(display.getLotStartDatePicker().getValue());
		if (display.getLotParentList().getSelectedIndex() == 0
				|| display.getLotParentList().getValue(
						display.getLotParentList().getSelectedIndex()) == NO_PARENT_LOT) {
			lotDTO.setParentLotId(null);
		} else {
			lotDTO.setParentLotId(Long.valueOf(display.getLotParentList().getValue(
					display.getLotParentList().getSelectedIndex())));
		}
	}

	public ViewEnum getMode()
	{
		return viewMode;
	}

	public void majLotData(Long lotId, ProjectPlanDTO projectPlanDTO)
	{
		setLotId(lotId);
		this.projectPlanDTO = projectPlanDTO;
		this.viewMode = getAppropriateViewModeFromProjectPlanStatus();
		if (editionIsACreation())
		{
			display.setLotFormTitleCreation();
		}
		else
		{
			display.setLotFormTitleModification();
		}
		getLotsParentList();
	}

	/**
	 * Get the appropriate view mode from evaluation of project plan status
	 */
	private ViewEnum getAppropriateViewModeFromProjectPlanStatus()
	{
		if (ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT.equals(projectPlanDTO.getStatus()))
		{
			return ViewEnum.EDIT;
		}
		else
		{
			return ViewEnum.READ;
		}
	}

	/**
	 * Return true if the edition is a creation, false otherwise
	 *
	 * @return true if creation
	 */
	private boolean editionIsACreation()
	{
		return lotId == null;
	}

	public void getLotsParentList()
	{
		new AbstractManagementRPCCall<List<LotDTO>>()
		{
			@Override
			protected void callService(AsyncCallback<List<LotDTO>> pCb)
			{
				Common.PROJECT_PLAN_SERVICE.getLotList(projectPlanDTO.getProjectPlanId(), pCb);
			}

			@Override
			public void onSuccess(List<LotDTO> pResult)
			{
				if (pResult != null) {
					fillLotParentList(pResult);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
			   ErrorManagement.displayErrorMessage(caught);
			}
		}.retry(0);
	}

	private void fillLotParentList(List<LotDTO> list)
	{
		List<LotDTO> lots = new ArrayList<LotDTO>();
		lots.addAll(list);
		Collections.sort(lots, new Comparator<LotDTO>()
		{

			@Override
			public int compare(LotDTO o1, LotDTO o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});

		display.getLotParentList().clear();
		display.getLotParentList().addItem("", NO_PARENT_LOT);

		for (LotDTO lot : lots)
		{
			if (getLotId() != null)
			{
				if (!getLotId().equals(lot.getLotId()))
				{
					display.getLotParentList().addItem(lot.getName(), String.valueOf(lot.getLotId()));
				}
			}
			else
			{
				display.getLotParentList().addItem(lot.getName(), String.valueOf(lot.getLotId()));
			}
		}

		loadDTOAfterLoadingParentList();
	}

	public Long getLotId()
	{
		return lotId;
	}

	private void loadDTOAfterLoadingParentList() {
	   //creation
	   if(editionIsACreation()) {
	      lotDTO = new LotDTO();
         lotDTO.setpPlanId(projectPlanDTO.getProjectPlanId());
         loadFormFromLotDTO();
         enableFields();
	   }
	   //modification
	   else {
	      getLot();
         enableFields();
	   }
	   //readonly
	   if(viewMode.equals(ViewEnum.READ)) {
	      getLot();
         disableFields();
	   }
	}

	private void loadFormFromLotDTO()
	{
		display.getLotNameValidator().setValue(lotDTO.getName());
		display.getLotDescValidator().setValue(lotDTO.getDesc());
		display.getLotEndDatePicker().setValue(lotDTO.getEndDate());
		display.getLotStartDatePicker().setValue(lotDTO.getStartDate());

		if (lotDTO.getParentLotId() == null)
		{
			display.getLotParentList().setSelectedIndex(0);
		}
		else
		{
			boolean finded = false;
			for (int i = 0; i < display.getLotParentList().getItemCount(); i++)
			{
				if (lotDTO.getParentLotId().equals(Long.decode(display.getLotParentList().getValue(i))))
				{
					finded = true;
					display.getLotParentList().setSelectedIndex(i);
					break;
				}
				if (!finded)
				{
					display.getLotParentList().setSelectedIndex(0);
				}
			}
		}
	}

	public void enableFields() {
		display.getButtonAddLot().setEnabled(true);
		display.getButtonSaveLot().setEnabled(true);
		display.getLotEndDatePicker().setEnabled(true);
		display.getLotNameValidator().setEnabled(true);
		display.getLotDescValidator().setEnabled(true);
		display.getLotStartDatePicker().setEnabled(true);
		display.getLotParentList().setEnabled(true);
		if (editionIsACreation()) {
			display.getButtonAddLot().setVisible(true);
			display.getButtonSaveLot().setVisible(false);
		} else {
			display.getButtonAddLot().setVisible(false);
			display.getButtonSaveLot().setVisible(true);
		}
	}

	private void getLot()
	{
		new AbstractManagementRPCCall<LotDTO>()
		{
			@Override
			protected void callService(AsyncCallback<LotDTO> pCb)
			{
				Common.PROJECT_PLAN_SERVICE.getLot(lotId, pCb);
			}

			@Override
			public void onSuccess(LotDTO pResult)
			{
				if (pResult != null)
				{
					lotDTO = pResult;
					loadFormFromLotDTO();
				}
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}
		}.retry(0);
	}

	public void disableFields() {
		display.getButtonAddLot().setEnabled(false);
		display.getLotEndDatePicker().setEnabled(false);
		display.getLotNameValidator().setEnabled(false);
		display.getLotDescValidator().setEnabled(false);
		display.getLotStartDatePicker().setEnabled(false);
		display.getLotParentList().setEnabled(false);
		if (editionIsACreation()) {
			display.getButtonAddLot().setVisible(true);
			display.getButtonSaveLot().setVisible(false);
		} else {
			display.getButtonAddLot().setVisible(false);
			display.getButtonSaveLot().setVisible(true);
		}
	}

	public void setLotId(Long lotId) {
		this.lotId = lotId;
	}
	
	@Override
	public void loadDataOnSelectionTab() {
	}

	@Override
	public IsWidget getDisplay()
	{
		return this.display.asWidget();
	}
}
