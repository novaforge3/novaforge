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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.iteration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.IterationReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.LotReferentialModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.LotReferentialModifiedHandler;
import org.novaforge.forge.tools.managementmodule.ui.client.event.ShowListEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.DateBoxValidation;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.validation.Validator;
import org.novaforge.forge.tools.managementmodule.ui.client.view.iteration.IterationFormView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.iteration.IterationFormViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PhaseTypeDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;

public class IterationEditPresenter implements TabPresenter {
	private static final String EMPTY_TEXT = "";
	private final SimpleEventBus eventBus;
	private final IterationFormView view = new IterationFormViewImpl();
	private IterationListPresenter iterationListPresenter;
	private List<PhaseTypeDTO>                 phaseTypesList;
	private List<LotDTO>                 lotsList;
	private IterationDTO iterationToUpdate;
	private ProjectPlanDTO currentProjectPlan;

   public IterationEditPresenter(final SimpleEventBus eventBus, final ProjectPlanDTO currentProjectPlan,
         IterationListPresenter iterationListPresenter) {
      super();
		this.eventBus = eventBus;
		this.iterationListPresenter = iterationListPresenter;
		this.currentProjectPlan = currentProjectPlan;
		bind();
	}

	/**
	 * This method is the interface between the presenter and the view
	 */
	private void bind()
	{

		view.getLibelleTB().setValidator(new Validator() {

			@Override
			public boolean isValid(String pValue)
			{
				boolean result = (!(pValue == null || EMPTY_TEXT.equals(pValue)));
				return result;
			}

			@Override
			public String getErrorMessage()
			{
			  return Common.getGlobal().messageRequiredField();
			}
		});
		
		view.getStartDateDB().setValidator(new Validator(){

      @Override
      public boolean isValid(String pValue) {
        boolean result = (!(pValue == null || EMPTY_TEXT.equals(pValue)));
        return result;
      }

      @Override
      public String getErrorMessage() {
        return Common.getGlobal().messageRequiredField();
      }
    });
		
		view.getEndDateDB().setValidator(new Validator(){

      @Override
      public boolean isValid(String pValue) {
        boolean result = (!(pValue == null || EMPTY_TEXT.equals(pValue)));
        return result;
      }

      @Override
      public String getErrorMessage() {
        return Common.getGlobal().messageRequiredField();
      }
    });

		view.getCreateIterationButton().addClickHandler(new ClickHandler()
		{
			@Override
			public void onClick(ClickEvent event)
			{
				
			  if (view.getLibelleTB().isValid() &&
			      view.getStartDateDB().isValid() &&
			      view.getEndDateDB().isValid() ){
			    
		        final ListBox lotListBox = view.getLotLB();
		        final DateBoxValidation endDateDateBox = view.getEndDateDB();
		        final int selectedIndexLotLB = lotListBox.getSelectedIndex();
		        final String lotName = lotListBox.getItemText(selectedIndexLotLB);
		        final LotDTO lotDTO = getLotFromName(lotName);
		        if (endDateDateBox.getValue() != null && endDateDateBox.getValue().after(lotDTO.getEndDate())) {
		           InfoDialogBox info = new InfoDialogBox(Common.MESSAGES_CHARGE_PLAN
		                 .eWrongEndDateWithLotEndDate() + " (" + Common.FR_DATE_FORMAT_ONLY_DAY.format(lotDTO.getEndDate()) + ")",
		                 InfoTypeEnum.KO);
		           info.getDialogPanel().center();
		        info.getDialogPanel().show();
		        }
		        else if (!areValidStartEndDates(view.getStartDateDB().getValue(),endDateDateBox.getValue()))
		        {
		          InfoDialogBox info = new InfoDialogBox(Common.MESSAGES_CHARGE_PLAN.eWrongStartEndDates(), InfoTypeEnum.KO);
		          info.getDialogPanel().center();
		          info.getDialogPanel().show();
		        }
		        else
		        {
		          if(iterationToUpdate != null){
		            view.getValidateDialogBoxUpdate().getDialogPanel().center();
		            view.getValidateDialogBoxUpdate().getDialogPanel().show();
		          }
		          else {
		            view.getValidateDialogBox().getDialogPanel().center();
		            view.getValidateDialogBox().getDialogPanel().show();
		          }
		        }	    
			  }
			  else {
			      view.getLibelleTB().isValid();
			      view.getStartDateDB().isValid();
            view.getEndDateDB().isValid();
            InfoDialogBox info = new InfoDialogBox(Common
                .getProjectPlanMessages().iterationErrorValidation(),
                InfoTypeEnum.KO);
            info.getDialogPanel().center();
            info.getDialogPanel().show();    
			  }
			}
		});

		// "Yes" to create box
		view.getValidateDialogBox().getValidate().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
            IterationDTO iterationDTO = new IterationDTO();
            PhaseTypeDTO phaseTypeDTO = new PhaseTypeDTO();
            final String phareFunctionalId = view.getPhaseLB().getValue(
                  view.getPhaseLB().getSelectedIndex());
            phaseTypeDTO.setFunctionalId(phareFunctionalId);
            iterationDTO.setPhaseType(phaseTypeDTO);

				IterationDTO lastIteration = iterationListPresenter.getIterationWithLastIterationNumber();
				iterationDTO.setIterationId(lastIteration.getNumIteration() + 1);
				iterationDTO.setNumIteration(lastIteration.getNumIteration() + 1);
				iterationDTO.setLabel(view.getLibelleTB().getValue().trim());
				iterationDTO.setStartDate(view.getStartDateDB().getValue());
				iterationDTO.setEndDate(view.getEndDateDB().getValue());

            if (getLotFromName(view.getLotLB().getItemText(view.getLotLB().getSelectedIndex())) != null) {
               iterationDTO.setLot(getLotFromName(view.getLotLB().getItemText(
                     view.getLotLB().getSelectedIndex())));
               createIteration(iterationDTO);
            }
				view.getValidateDialogBox().getDialogPanel().hide();
			}
		});
		// "Yes" to update box
		view.getValidateDialogBoxUpdate().getValidate().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
			   PhaseTypeDTO phaseTypeDTO = new PhaseTypeDTO();
            final String phareFunctionalId = view.getPhaseLB().getValue(
                  view.getPhaseLB().getSelectedIndex());
            phaseTypeDTO.setFunctionalId(phareFunctionalId);
            final String lotName = view.getLotLB().getItemText(view.getLotLB().getSelectedIndex());
            iterationToUpdate.setLot(getLotFromName(lotName));
            iterationToUpdate.setPhaseType(phaseTypeDTO);
				iterationToUpdate.setLabel(view.getLibelleTB().getValue());
				iterationToUpdate.setStartDate(view.getStartDateDB().getValue());
				iterationToUpdate.setEndDate(view.getEndDateDB().getValue());
				updateIteration(iterationToUpdate);
				view.getValidateDialogBoxUpdate().getDialogPanel().hide();
			}
		});

		view.getCancelIterationButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
			   eventBus.fireEvent(new ShowListEvent(ShowListEvent.EnumList.ITERATION_LIST));
			}
		});

    view.getStartDateDB().addValueChangeHandler(new ValueChangeHandler<Date>() {
      public void onValueChange(ValueChangeEvent<Date> event) {
        if ( event.getValue() != null )
        {
          view.getEndDateDB().setCurrentMonth( view.getStartDateDB().getValue() );
        }
      }
    });
		eventBus.addHandler(LotReferentialModifiedEvent.TYPE, new LotReferentialModifiedHandler() {
         @Override
         public void onModifyLotReferential(LotReferentialModifiedEvent event) {
            if(iterationToUpdate == null || iterationListPresenter.getLastIterationNumber() == iterationToUpdate.getNumIteration()) {
               fillLotsList();
            }
         }
      });
	}

	public LotDTO getLotFromName(String name)
	{

		for (int i = 0; i < lotsList.size(); i++)
		{
			if (lotsList.get(i).getName().equals(name))
			{
				return lotsList.get(i);
			}
		}
		return null;
	}

	private boolean areValidStartEndDates(Date value, Date value2)
	{
		return (value.compareTo(value2) <= 0);
	}

	private void createIteration(final IterationDTO iteration)
	{
		new AbstractManagementRPCCall<Boolean>()
		{
			@Override
			protected void callService(AsyncCallback<Boolean> iCb) {
			   Common.ITERATION_SERVICE.createIteration(iteration, iCb);

			}

			@Override
			public void onFailure(Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}			@Override
			public void onSuccess(Boolean arg0)
			{
				InfoDialogBox info = new InfoDialogBox(Common.MESSAGES_CHARGE_PLAN.createSuccessful(), InfoTypeEnum.OK);
				info.getDialogPanel().center();
				eventBus.fireEvent(new ShowListEvent(ShowListEvent.EnumList.ITERATION_LIST));
				Common.GLOBAL_EVENT_BUS.fireEvent(new IterationReferentialModifiedEvent());
			}


		}.retry(3);
	}

	private void updateIteration(final IterationDTO iteration)
	{
		new AbstractManagementRPCCall<IterationDTO>()
		{
			@Override
			protected void callService(AsyncCallback<IterationDTO> callBack)
			{
			   Common.ITERATION_SERVICE.updateIteration(currentProjectPlan.getProjectPlanId(), iteration, callBack);
			}

			@Override
			public void onSuccess(IterationDTO updatedIteration)
			{
				InfoDialogBox info;
				info = new InfoDialogBox(Common.MESSAGES_CHARGE_PLAN.updateSuccessful(), InfoTypeEnum.OK);
				info.getDialogPanel().center();
	         eventBus.fireEvent(new ShowListEvent(ShowListEvent.EnumList.ITERATION_LIST));
	         Common.GLOBAL_EVENT_BUS.fireEvent(new IterationReferentialModifiedEvent(updatedIteration));
			}

			@Override
			public void onFailure(Throwable pCaught)
			{
			   ErrorManagement.displayErrorMessage(pCaught);
			}
		}.retry(3);
	}

	private void fillLotsList() {

		lotsList = new ArrayList<LotDTO>();
		view.getLotLB().clear();

		new AbstractManagementRPCCall<List<LotDTO>>()
		{
			@Override
         protected void callService(AsyncCallback<List<LotDTO>> pCb)
			{
			   long projectplanId = currentProjectPlan.getProjectPlanId();
            if (currentProjectPlan.getStatus().equals(ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT)
                  && SessionData.currentValidatedProjectPlanId != null) {
               projectplanId = SessionData.currentValidatedProjectPlanId;
            }
			   Common.ITERATION_SERVICE.getLotsList(projectplanId, pCb);
			}

         @Override
         public void onSuccess(final List<LotDTO> pResult) {
            lotsList.addAll(pResult);
            LotDTO selectedLot = null;
            int selectedIndex = 0;
            for (final LotDTO lotDTO : lotsList) {
               view.getLotLB().addItem(lotDTO.getName());
               // if there is an iteration we selected the old one
               if (iterationToUpdate != null
                     && lotDTO.getName().equals(iterationToUpdate.getLot().getName())) {
                  selectedIndex = view.getLotLB().getItemCount() - 1;
               }
               // otherwise the oldest
               if (iterationToUpdate == null
                     && (selectedLot == null || lotDTO.getEndDate()
                           .before(selectedLot.getEndDate()))) {
                  selectedLot = lotDTO;
                  selectedIndex = view.getLotLB().getItemCount() - 1;
               }
            }
            view.getLotLB().setSelectedIndex(selectedIndex);
            // for the first iteration, we take the biggest between date of
            // the day and start of the oldest lot to put in the startdatebox
            if (iterationListPresenter.getLastIterationNumber() == 0) {
               final DateBoxValidation startDateDateBox = view.getStartDateDB();
               if (getFirstLotOccuring().getStartDate().after(new Date())) {
                  startDateDateBox.setValue(getFirstLotOccuring().getStartDate());
               } else {
                  startDateDateBox.setValue(new Date());
               }
               // To have the end date in the same month as the start date
               view.getEndDateDB().setCurrentMonth(startDateDateBox.getValue());
            }
         }

         @Override
         public void onFailure(Throwable caught) {
            ErrorManagement.displayErrorMessage(caught);
         }
      }.retry(0);

	}

	/**
	 * Get 1st lot, according to dates
	 * @return
	 */
	public LotDTO getFirstLotOccuring() {
		Date firstDate = lotsList.get(0).getStartDate();
		LotDTO lotToReturn = lotsList.get(0);
		for(int i = 1; i < lotsList.size(); i++){
			if(lotsList.get(i).getStartDate().before(firstDate)){
				firstDate = lotsList.get(i).getStartDate();
				lotToReturn = lotsList.get(i);
			}
		}
		return lotToReturn;
	}

	public IterationDTO getIterationToUpdate()
	{
		return iterationToUpdate;
	}

	public void setIterationToUpdate(IterationDTO iterationToUpdate)
	{
		this.iterationToUpdate = iterationToUpdate;
		//new iteration selected so we reload datas
		loadDatas();
	}

	/**
	 * Datas loading
	 */
	private void loadDatas()
	{
		if (iterationToUpdate != null)
		{
			this.view.getLibelleTB().setValue(iterationToUpdate.getLabel());
			this.view.getStartDateDB().setValue(iterationToUpdate.getStartDate());
			this.view.getEndDateDB().setValue(iterationToUpdate.getEndDate());
			this.view.getPhaseLB().clear();
			phaseTypesList = new ArrayList<PhaseTypeDTO>();
			lotsList = new ArrayList<LotDTO>();
			this.view.getPhaseLB().setEnabled(true);
			this.view.getLotLB().setEnabled(true);
			//if its the last we can modify phaseType and lot, otherwise no
			if (iterationListPresenter.getLastIterationNumber() == iterationToUpdate.getNumIteration())
			{
				fillPhasesList();
				fillLotsList();
			}
			else
			{
				phaseTypesList.add(iterationToUpdate.getPhaseType());
				this.view.getPhaseLB().addItem(iterationToUpdate.getPhaseType().getName(),
																			 iterationToUpdate.getPhaseType().getFunctionalId());
				this.view.getPhaseLB().setEnabled(false);
				this.view.getLotLB().clear();
				lotsList.add(iterationToUpdate.getLot());
				this.view.getLotLB().addItem(iterationToUpdate.getLot().getName());
				this.view.getLotLB().setEnabled(false);
			}
			this.view.getIterationCreationTitle().setText(Common.MESSAGES_CHARGE_PLAN.updateOneIterationTitle());
			this.view.getCreateIterationButton().setText(Common.MESSAGES_CHARGE_PLAN.iterationUpdateButtonLabel());
		}
		// Creation
		else
		{
			this.view.getLibelleTB().setValue("I" + (iterationListPresenter.getLastIterationNumber() + 1));
			this.view.getStartDateDB().setValue(null);
			this.view.getEndDateDB().setValue(null);
      this.view.getPhaseLB().clear();
			this.view.getLotLB().clear();
		
			fillPhasesList();
			fillLotsList();
			fillStartDateWithPreviousIteration();

			this.view.getLotLB().setEnabled(true);
			this.view.getPhaseLB().setEnabled(true);

			this.view.getIterationCreationTitle().setText(Common.MESSAGES_CHARGE_PLAN.createOneIterationTitle());
			this.view.getCreateIterationButton().setText(Common.MESSAGES_CHARGE_PLAN.iterationCreationButtonLabel());

		}
	}

	private void fillPhasesList()
	{

		phaseTypesList = new ArrayList<PhaseTypeDTO>();
		new AbstractManagementRPCCall<List<PhaseTypeDTO>>()
		{
			@Override
			protected void callService(AsyncCallback<List<PhaseTypeDTO>> pCb)
			{
				//if creation we take the phasesType allowed for iteration after the last one
				if (iterationToUpdate == null)
				{
					Common.ITERATION_SERVICE.getPhasesTypesForNextIteration(iterationListPresenter
																																			.getIterationWithLastIterationNumber(), pCb);
				}
				// otherwise we take the phasesTypes allowed for the first previous iteration
				else
				{
					IterationDTO firstPreviousSibling = null;
					for (final IterationDTO iterationDTO : iterationListPresenter.getIterationsList())
					{
						if (iterationDTO.getIterationId() != iterationToUpdate.getIterationId() && (firstPreviousSibling == null
																																														|| (iterationDTO
																																																		.getNumIteration()
																																																		< iterationToUpdate
																																																					.getNumIteration()
																																																		&& iterationDTO
																																																					 .getNumIteration()
																																																					 > firstPreviousSibling
																																																								 .getNumIteration())))
						{
							firstPreviousSibling = iterationDTO;
						}
					}
					Common.ITERATION_SERVICE.getPhasesTypesForNextIteration(firstPreviousSibling, pCb);
				}
			}

			@Override
			public void onSuccess(List<PhaseTypeDTO> pResult)
			{
				if (pResult != null)
				{
					phaseTypesList.addAll(pResult);
					refreshPhasesList(phaseTypesList);
				}
			}

			@Override
			public void onFailure(Throwable caught)
			{
				ErrorManagement.displayErrorMessage(caught);
			}
		}.retry(0);

		refreshPhasesList(phaseTypesList);
	}

	private void fillStartDateWithPreviousIteration()
	{
		// Other Iterations than 1st one : we fill start date with end date of
		// previous iteration
		final DateBoxValidation startDateDateBox = view.getStartDateDB();
		if (iterationListPresenter.getLastIterationNumber() != 0)
		{
			//last iteration date + 1 day
			final Date endDateLastIteration = iterationListPresenter.getIterationWithLastIterationNumber().getEndDate();
			final int nbMillisOneDay = 24 * 60 * 60 * 1000;
			final Date dateNextIteration = new Date(endDateLastIteration.getTime() + nbMillisOneDay);
			startDateDateBox.setValue(dateNextIteration);
			view.getEndDateDB().setCurrentMonth(startDateDateBox.getValue());
		}
	}

	private void refreshPhasesList(List<PhaseTypeDTO> pList)
	{
		int indexToSelect = 0;
		for (PhaseTypeDTO element : pList)
		{
			view.getPhaseLB().addItem(element.getName(), element.getFunctionalId());
			if (iterationToUpdate != null && element.getFunctionalId().equals(iterationToUpdate.getPhaseType()
																																												 .getFunctionalId()))
			{
				indexToSelect = view.getPhaseLB().getItemCount() - 1;
			}
		}
		view.getPhaseLB().setSelectedIndex(indexToSelect);
	}

	@Override
	public void go(HasWidgets container)
	{
		//this method isnt used in tab mod
		container.clear();
		container.add(this.view.asWidget());
		// Datas loading
		loadDatas();
	}

	@Override
	public void loadDataOnSelectionTab() {
		//nothing to do, datas are loaded on selection of an iteration
	}

	@Override
	public IsWidget getDisplay()
	{
		return this.view.asWidget();
	}

}
