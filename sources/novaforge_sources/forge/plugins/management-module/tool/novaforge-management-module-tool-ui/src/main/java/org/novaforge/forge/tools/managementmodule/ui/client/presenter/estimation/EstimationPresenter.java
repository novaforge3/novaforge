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
package org.novaforge.forge.tools.managementmodule.ui.client.presenter.estimation;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.SessionData;
import org.novaforge.forge.tools.managementmodule.ui.client.event.EstimationModifiedEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.estimation.DisplayScopeUnitDetailsEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.estimation.RefreshEstimationChargeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.helper.AbstractManagementRPCCall;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.TabPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.AcronymBoxPresenter;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ErrorManagement;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.ViewEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.estimation.EstimationView;
import org.novaforge.forge.tools.managementmodule.ui.client.view.estimation.EstimationViewImpl;
import org.novaforge.forge.tools.managementmodule.ui.shared.AccessRight;
import org.novaforge.forge.tools.managementmodule.ui.shared.ApplicativeFunction;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.FunctionPointsTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectPlanStatus;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDTO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author BILET-JC
 * 
 */
public class EstimationPresenter implements TabPresenter {

	private final SimpleEventBus eventBus;
	private final EstimationView display;
	private final ProjectPlanDTO projectPlan;
	private final ViewEnum viewMode;
	private final ScopeUnitDetailsBoxPresenter scopeUnitDetailsPresenter;
	private final AcronymBoxPresenter acronymPresenter;
	private final DisciplineSharingPresenter disciplineSharingPresenter;
	private List<EstimationDTO> estimationList;
	private List<String>        functionPointTypes;
	private List<String>        componentTypes;
	private EstimationComponentDTO estimationComponent;
	private Map<String, Set<String>> scopeUnitRelationship;

	/**
	 * @param estimationService
	 * @param eventBus
	 * @param display
	 */
	public EstimationPresenter(final SimpleEventBus eventBus, final ProjectPlanDTO projectPlanDTO) {
		super();
		this.display = new EstimationViewImpl();
		this.eventBus = eventBus;
		this.projectPlan = projectPlanDTO;
		this.viewMode = getAppropriateViewModeFromProjectPlanStatus();
		// these are presenter of popup views, so they have to be constructed
		// here to be centered
		scopeUnitDetailsPresenter = new ScopeUnitDetailsBoxPresenter();
		acronymPresenter = new AcronymBoxPresenter(getAcronyms());
		disciplineSharingPresenter = new DisciplineSharingPresenter();

		enableUI();
		bind();
	}

	/**
	 * Create and return the list of acronyms
	 * 
	 * @return
	 */
	private Map<String, String> getAcronyms() {
		Map<String, String> ret = new HashMap<String, String>();
		ret.put(Common.MESSAGES_ACRONYM.upL(), Common.MESSAGES_ACRONYM.upDef());
		ret.put(Common.MESSAGES_ACRONYM.gdiL(), Common.MESSAGES_ACRONYM.gdiDef());
		ret.put(Common.MESSAGES_ACRONYM.gdeL(), Common.MESSAGES_ACRONYM.gdeDef());
		ret.put(Common.MESSAGES_ACRONYM.inL(), Common.MESSAGES_ACRONYM.inDef());
		ret.put(Common.MESSAGES_ACRONYM.outL(), Common.MESSAGES_ACRONYM.outDef());
		ret.put(Common.MESSAGES_ACRONYM.interrogationL(), Common.MESSAGES_ACRONYM.interrogationDef());
		ret.put(Common.MESSAGES_ACRONYM.pfL(), Common.MESSAGES_ACRONYM.pfDef());
		ret.put(Common.MESSAGES_ACRONYM.SL(), Common.MESSAGES_ACRONYM.sDef());
		ret.put(Common.MESSAGES_ACRONYM.ML(), Common.MESSAGES_ACRONYM.mDef());
		ret.put(Common.MESSAGES_ACRONYM.CL(), Common.MESSAGES_ACRONYM.cDef());
		ret.put(Common.MESSAGES_ACRONYM.aeL(), Common.MESSAGES_ACRONYM.aeDef());
		ret.put(Common.MESSAGES_ACRONYM.aaL(), Common.MESSAGES_ACRONYM.aaDef());
		ret.put(Common.MESSAGES_ACRONYM.uaL(), Common.MESSAGES_ACRONYM.uaDef());
		ret.put(Common.MESSAGES_ACRONYM.uvL(), Common.MESSAGES_ACRONYM.uvDef());
		return ret;
	}

	/**
	 * This method disable all actions if mode if READ
	 */
	private void enableUI() {
		boolean bool = true;
		if (AccessRight.READ.equals(SessionData.getAccessRight(ApplicativeFunction.FUNCTION_ESTIMATION))
				|| ViewEnum.READ.equals(viewMode)) {
			bool = false;
		}
		display.getValidateB().setEnabled(bool);
		display.getfpType().setEnabled(bool);
		display.getComponentType().setEnabled(bool);
		display.disabledCells(!bool);
	}

	/**
	 * This method is the interface between the presenter and the view
	 */
	private void bind() {
		/*
		 * Displays scope unit's details in a popup
		 */
		display.asWidget().addHandler(new DisplayScopeUnitDetailsEvent.Handler() {

			@Override
			public void displayScopeUnitDetails(DisplayScopeUnitDetailsEvent event) {
				scopeUnitDetailsPresenter.setScopeUnit(event.getScopeUnit());
				scopeUnitDetailsPresenter.go(display.getContentPanel());
			}
		}, DisplayScopeUnitDetailsEvent.TYPE);
		/*
		 * Displays acronyms definition and some informations about components
		 */
		display.getAcronymB().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				acronymPresenter.go(display.getContentPanel());
			}
		});
		/*
		 * Displays discipline sharing
		 */
		display.getDisciplineSharingB().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (display.getEstimations() != null && display.getEstimations().size() > 0) {
					List<EstimationDTO> parentEstimations = new ArrayList<EstimationDTO>();
					for (EstimationDTO estimation : display.getEstimations()) {
						if (estimation.getScopeUnit().getParentScopeUnit() == null) {
							parentEstimations.add(estimation);
						}
					}
					disciplineSharingPresenter.setEstimations(parentEstimations);
					disciplineSharingPresenter.go(display.getContentPanel());
				} else {
					InfoDialogBox box = new InfoDialogBox(
							Common.MESSAGES_ESTIMATION.emptyEstimationMessage(), InfoTypeEnum.WARNING);
					box.getDialogPanel().center();
					box.getDialogPanel().show();
				}
			}
		});
		/*
		 * Event from view when a value has been change in a column. Refresh the
		 * total charge of the scope unit
		 */
		display.asWidget().addHandler(new RefreshEstimationChargeEvent.Handler() {

			@Override
			public void refreshEstimationCharge(RefreshEstimationChargeEvent event) {
				refreshCharge(display.getEstimations().get(
						display.getEstimations().indexOf(event.getEstimation())));
			}
		}, RefreshEstimationChargeEvent.TYPE);
		/*
		 * Selection change
		 */
		display.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				EstimationDTO e = display.getSelectionModel().getSelectedObject();

				if (!isParentUp(e.getScopeUnit()))
				{
				  if (e.getSimple() != ComponentEnum.NONE) {
				    display.displayfpCT(true, true);
  					display.getfpType().setSelectedIndex(
  							functionPointTypes.indexOf(Common.MESSAGES_ESTIMATION.simplified()));
  					display.getComponentType().setSelectedIndex(
  							componentTypes.indexOf(e.getSimple().getLabel()));
  				} else {
  				  display.displayfpCT(true, false);
  					display.getfpType().setSelectedIndex(
  							functionPointTypes.indexOf(Common.MESSAGES_ESTIMATION.detailled()));
  				}
  				refreshCharge(e);
				} else {
				  //parent UP => we hide FP charging screen
				  display.displayfpCT(false, false);
				}
			}
		});
		/*
		 * Validation
		 */
		display.getValidateB().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				validate();
			}
		});
		/*
		 * DETAIL or SIMPLE way to set function points
		 */
		display.getfpType().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				String selectedType = display.getfpType().getItemText(display.getfpType().getSelectedIndex());
				EstimationDTO e = display.getSelectionModel().getSelectedObject();
				if (FunctionPointsTypeEnum.DETAIL.getLabel().equals(selectedType)) {
					updateSimple(e, ComponentEnum.NONE);
				} else if (FunctionPointsTypeEnum.SIMPLE.getLabel().equals(selectedType)) {
					updateSimple(e, ComponentEnum.GDI);
				}
				refreshCharge(e);
			}
		});
		/*
		 * the TYPE of component when SIMPLE way to set function points
		 */
		display.getComponentType().addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				String selectedSimple = display.getComponentType().getItemText(
						display.getComponentType().getSelectedIndex());
				ComponentEnum simple = ComponentEnum.GDI;
				EstimationDTO e = display.getSelectionModel().getSelectedObject();

				if (ComponentEnum.GDE.getLabel().equals(selectedSimple)) {
					simple = ComponentEnum.GDE;
				} else if (ComponentEnum.IN.getLabel().equals(selectedSimple)) {
					simple = ComponentEnum.IN;
				} else if (ComponentEnum.OUT.getLabel().equals(selectedSimple)) {
					simple = ComponentEnum.OUT;
				} else if (ComponentEnum.INT.getLabel().equals(selectedSimple)) {
					simple = ComponentEnum.INT;
				}
				updateSimple(e, simple);
				refreshCharge(e);
			}
		});
		display.getLotLB().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				refreshSubLot();
				checkSelectedLot();
			}
		});
		display.getSubLotLB().addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkSelectedLot();
			}
		});
	}

	/**
	 * This method updates in the estimation list in the view the simple
	 * parameter
	 *
	 * @param selectedObject
	 * @param simple
	 */
	private void updateSimple(EstimationDTO selectedObject, ComponentEnum simple) {
		display.getEstimations().get(display.getEstimations().indexOf(selectedObject)).setSimple(simple);

	}

	/**
	 * This method checks which lot and sublot are selected to display
	 * corresponding charge
	 */
	private void checkSelectedLot() {
		// no lot selected, unselect sublot
		if (Common.GLOBAL.select().equals(
				display.getLotLB().getItemText(display.getLotLB().getSelectedIndex()))) {
			display.getSubLotLB().setItemSelected(0, true);
			display.getSubLotLB().setEnabled(false);
			display.getTotalCharge().setText(Common.EMPTY_TEXT);
		}
		// no sublot selected
		else if (Common.GLOBAL.select().equals(
				display.getSubLotLB().getItemText(display.getSubLotLB().getSelectedIndex()))) {
			countLotCharge(display.getLotLB().getItemText(display.getLotLB().getSelectedIndex()));
		}
		// sublot selected
		else {
			countSubLotCharge(display.getLotLB().getItemText(display.getLotLB().getSelectedIndex()), display
					.getSubLotLB().getItemText(display.getSubLotLB().getSelectedIndex()));
		}
	}

	/**
	 * This method counts charge for the lot in parameter
	 *
	 * @param pLot
	 */
	private void countLotCharge(String pLot) {
		Integer charge = 0;
		for (EstimationDTO estimation : display.getEstimations()) {
			if (pLot.equals(estimation.getScopeUnit().getLotName()) && !estimation.getScopeUnit().hasChild()) {
				String displayedCharge = display.getEstimationCT()
						.getColumn(display.getEstimationCT().getColumnIndex(display.getChargeC()))
						.getValue(estimation).toString();
				if (Common.isInt(displayedCharge)) {
					charge += Integer.parseInt(displayedCharge);
				}
			}
		}
		display.getTotalCharge().setText(charge.toString());
	}

	/**
	 * This method counts charge for the subLot in parameter
	 *
	 * @param pLot
	 * @param pSubLot
	 */
   private void countSubLotCharge(String pLot, String pSubLot) {
      Integer charge = 0;
      for (EstimationDTO estimation : display.getEstimations()) {
         if (estimation.getScopeUnit().getLotName().equals(pSubLot)
               || (estimation.getScopeUnit().getLotName().equals(pLot) && (estimation.getScopeUnit()
                     .getParentLotName() == null || estimation.getScopeUnit().getParentLotName()
                     .equals(Common.EMPTY_TEXT)))) {
            charge += estimation.getCharge();
         }
      }
      display.getTotalCharge().setText(charge.toString());
   }
   
   /**
    * returns true if provided scopeUnit is a parent UP
    * @param scopeUnit scope unit
    * @return true if provided scopeUnit is a parent UP
    */
   private boolean isParentUp(ScopeUnitDTO scopeUnit)
   {
     boolean result = false;
     if (scopeUnitRelationship == null)
     {
       createRelationshipMap();
     }

     if (scopeUnitRelationship != null && scopeUnitRelationship.get(scopeUnit.getUnitId()) != null 
         && scopeUnitRelationship.get(scopeUnit.getUnitId()).size() > 0){
       result = true;
     }
     return result;     
   }

	/**
	 * This method validate displaying estimations
	 */
	private void validate() {
		new AbstractManagementRPCCall<Void>()
		{
			@Override
			protected void callService(AsyncCallback<Void> cb) {
				Common.PROJECT_PLAN_SERVICE.validateEstimation(display.getEstimations(), cb);
			}

			@Override
			public void onFailure(Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}

			@Override
			public void onSuccess(Void arg0) {
				display.getInfoDB().getDialogPanel().center();
				display.getInfoDB().getDialogPanel().show();
				eventBus.fireEvent(new EstimationModifiedEvent());
			}


		}.retry(0);
	}

	/**
	 * Calculate the simple raw function point. This way,extrems values (i.e 0) are manage
	 * @param fpRaw
	 * @param componentValue
	 * @return the calculate FPRAw
	 */
	private Float calculateSimpleRawFP(Float fpRaw, Float componentValue) {
		Float ret = 0f;
		Float cent = new Float(100);
		if (fpRaw != 0 && componentValue != 0) {
			ret = fpRaw/componentValue*cent;
		}
		return ret;
	}

	/**
	 * This method count the charge thanks to the function points and update it
	 * in the estimation list
	 *
	 * @param pEstimation
	 */
	@SuppressWarnings("incomplete-switch")
  private void refreshCharge(EstimationDTO pEstimation) {
		float pfRaw = 0;
		float pfAdjusted = 0;
		float charge;
		if (!pEstimation.isManual()) {
			// SIMPLE way
			if (pEstimation.getSimple() != ComponentEnum.NONE) {
				Integer simple = pEstimation.getGlobalSimple();
				Integer median = pEstimation.getGlobalMedian();
				Integer complex = pEstimation.getGlobalComplex();
				switch (pEstimation.getSimple()) {
				case GDI:  
					pfRaw += simple * estimationComponent.getGDIsimple();
					pfRaw += median * estimationComponent.getGDImedian();
					pfRaw += complex * estimationComponent.getGDIcomplex();
					pfRaw = calculateSimpleRawFP(pfRaw, estimationComponent.getGDI());
					break;
				case GDE:
					pfRaw += simple * estimationComponent.getGDEsimple();
					pfRaw += median * estimationComponent.getGDEmedian();
					pfRaw += complex * estimationComponent.getGDEcomplex();
					pfRaw = calculateSimpleRawFP(pfRaw, estimationComponent.getGDE());
					break;
				case IN:
					pfRaw += simple * estimationComponent.getINsimple();
					pfRaw += median * estimationComponent.getINmedian();
					pfRaw += complex * estimationComponent.getINcomplex();
					pfRaw = calculateSimpleRawFP(pfRaw, estimationComponent.getIN());
					break;
				case OUT:
					pfRaw += simple * estimationComponent.getOUTsimple();
					pfRaw += median * estimationComponent.getOUTmedian();
					pfRaw += complex * estimationComponent.getOUTcomplex();
					pfRaw = calculateSimpleRawFP(pfRaw, estimationComponent.getOUT());
					break;
				case INT:
					pfRaw += simple * estimationComponent.getINTsimple();
					pfRaw += median * estimationComponent.getINTmedian();
					pfRaw += complex * estimationComponent.getINTcomplex();
					pfRaw = calculateSimpleRawFP(pfRaw, estimationComponent.getINT());
					break;
				}
			}
			// DETAIL way
			else {
				pfRaw += pEstimation.getGDIsimple() * estimationComponent.getGDIsimple();
				pfRaw += pEstimation.getGDImedian() * estimationComponent.getGDImedian();
				pfRaw += pEstimation.getGDIcomplex() * estimationComponent.getGDIcomplex();
				pfRaw += pEstimation.getGDEsimple() * estimationComponent.getGDEsimple();
				pfRaw += pEstimation.getGDEmedian() * estimationComponent.getGDEmedian();
				pfRaw += pEstimation.getGDEcomplex() * estimationComponent.getGDEcomplex();
				pfRaw += pEstimation.getINsimple() * estimationComponent.getINsimple();
				pfRaw += pEstimation.getINmedian() * estimationComponent.getINmedian();
				pfRaw += pEstimation.getINcomplex() * estimationComponent.getINcomplex();
				pfRaw += pEstimation.getOUTsimple() * estimationComponent.getOUTsimple();
				pfRaw += pEstimation.getOUTmedian() * estimationComponent.getOUTmedian();
				pfRaw += pEstimation.getOUTcomplex() * estimationComponent.getOUTcomplex();
				pfRaw += pEstimation.getInterrogationSimple() * estimationComponent.getINTsimple();
				pfRaw += pEstimation.getInterrogationMedian() * estimationComponent.getINTmedian();
				pfRaw += pEstimation.getInterrogationComplex() * estimationComponent.getINTcomplex();
			}
			// display.getFPCT().redraw();
			pfRaw = Math.round(pfRaw);
			pfAdjusted = Math.round(pfRaw * estimationComponent.getAdjustementCoef());
			charge = Math.round(pfAdjusted * estimationComponent.getAbacusChargeMenByDay());

      display.getEstimations().get(display.getEstimations().indexOf(pEstimation))
       .setPfRaw(Integer.valueOf((int) pfRaw));
      display.getEstimations().get(display.getEstimations().indexOf(pEstimation))
       .setPfAdjusted(Integer.valueOf((int) pfAdjusted));
      display.getEstimations().get(display.getEstimations().indexOf(pEstimation))
       .setCharge(Integer.valueOf((int) charge));
		} else {
			charge = pEstimation.getCharge();
		}
		refreshParentCharge(pEstimation);
		checkSelectedLot();

    Collections.sort(estimationList, display.getScopeUnitComparator());
    display.getDataProvider().setList(estimationList);
    display.refreshEstimationList(display.getEstimations());
		display.displayFP();
		display.updateSortHandler();
	}

	/**
	 * Count the new charge of the parent's estimation in parameter
	 *
	 * @param pEstimation
	 */
	private void refreshParentCharge(EstimationDTO pEstimation) {
		EstimationDTO parentEstimation = null;
		Integer parentCharge = 0;
		Iterator<String> it = scopeUnitRelationship.keySet().iterator();
		String      key;
		Set<String> value;
		boolean found = false;
		while (it.hasNext() && !found) {
			key = it.next();
			value = scopeUnitRelationship.get(key);
			if (value.contains(pEstimation.getScopeUnit().getUnitId())) {
				found = true;
				for (EstimationDTO e : display.getEstimations()) {
					if (key.equals(e.getScopeUnit().getUnitId())) {
						parentEstimation = e;
					}
					if (value.contains(e.getScopeUnit().getUnitId())) {
						parentCharge += e.getCharge();
					}
				}
				display.getEstimations().get(display.getEstimations().indexOf(parentEstimation))
						.setCharge(parentCharge);
			}
		}

	}

	@Override
	public void go(HasWidgets container)
	{
		// do nothing here, datas are upload when tab is selected
		// (loadDataOnSelectionTab() method)
		container.clear();
		container.add(display.asWidget());
	}

	@Override
	public void loadDataOnSelectionTab()
	{
		getEstimationComponant();
	}
	
	/**
	 * This method get abacus charge from the repository
	 */
	private void getEstimationComponant()
	{
		new AbstractManagementRPCCall<EstimationComponentDTO>()
		{

			@Override
			protected void callService(AsyncCallback<EstimationComponentDTO> cb)
			{
				Common.PROJECT_PLAN_SERVICE.getEstimationComponent(SessionData.projectId, cb);
			}

			@Override
			public void onSuccess(EstimationComponentDTO result)
			{
				estimationComponent = new EstimationComponentDTO();
				if (result != null)
				{
					estimationComponent = result;
					display.setNbHourByDay(estimationComponent.getNbHourByDay());
					display.setAdjustementCoef(estimationComponent.getAdjustementCoef());
					createFPTypeListBox();
					createComponentListBox();
					updateEstimationList();
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
	 * Add datas to the functionPoints' TYPE listbox
	 */
	private void createFPTypeListBox()
	{
		functionPointTypes = new ArrayList<String>();
		functionPointTypes.add(FunctionPointsTypeEnum.SIMPLE.getLabel());
		functionPointTypes.add(FunctionPointsTypeEnum.DETAIL.getLabel());
		display.getfpType().clear();
		for (int i = 0; i < functionPointTypes.size(); i++)
		{
			display.getfpType().addItem(functionPointTypes.get(i));
		}
	}

	/**
	 * Add datas to the components listbox
	 */
	private void createComponentListBox()
	{
		componentTypes = new ArrayList<String>();
		componentTypes.add(ComponentEnum.GDI.getLabel());
		componentTypes.add(ComponentEnum.IN.getLabel());
		componentTypes.add(ComponentEnum.INT.getLabel());
		componentTypes.add(ComponentEnum.OUT.getLabel());
		componentTypes.add(ComponentEnum.GDE.getLabel());
		display.getComponentType().clear();
		for (int i = 0; i < componentTypes.size(); i++)
		{
			display.getComponentType().addItem(componentTypes.get(i));
		}

	}

	/**
	 * This method get back the list of estimations
	 */
	public void updateEstimationList()
	{
		estimationList = new ArrayList<EstimationDTO>();
		new AbstractManagementRPCCall<List<EstimationDTO>>()
		{

			@Override
			protected void callService(AsyncCallback<List<EstimationDTO>> pCb)
			{
				Common.PROJECT_PLAN_SERVICE.getEstimationList(projectPlan.getProjectPlanId(), pCb);
			}

			@Override
			public void onFailure(Throwable pCaught)
			{
				ErrorManagement.displayErrorMessage(pCaught);
			}

			@Override
			public void onSuccess(List<EstimationDTO> pResult)
			{
				if (pResult != null)
				{
					estimationList = new ArrayList<EstimationDTO>();
					estimationList.addAll(pResult);
					createRelationshipMap();
					display.displayfpCT(false, false);
					refreshEstimationList();
					refreshLot();
					refreshSubLot();
				}
			}

		}.retry(0);
	}

	private void createRelationshipMap()
	{
		ScopeUnitDTO scopeUnit;
		scopeUnitRelationship = new HashMap<String, Set<String>>();
		for (EstimationDTO e : estimationList)
		{
			scopeUnit = e.getScopeUnit();
			if (scopeUnit.getParentScopeUnit() != null)
			{
				// parent not referenced yet
				if (!scopeUnitRelationship.containsKey(scopeUnit.getParentScopeUnit().getUnitId()))
				{
					Set<String> childs = new HashSet<String>();
					childs.add(scopeUnit.getUnitId());
					scopeUnitRelationship.put(scopeUnit.getParentScopeUnit().getUnitId(), childs);
				}
				// parent already referenced
				else
				{
					Set<String> childs = scopeUnitRelationship.get(scopeUnit.getParentScopeUnit().getUnitId());
					childs.add(scopeUnit.getUnitId());
					scopeUnitRelationship.put(scopeUnit.getParentScopeUnit().getUnitId(), childs);
				}
			}

		}
	}

	/**
	 * This method refreshes the dataProvider and the sortHandler with the last
	 * list of estimations obtained
	 */
	private void refreshEstimationList() {
		//default sort
	    Collections.sort(estimationList, display.getScopeUnitComparator());
		display.getDataProvider().setList(estimationList);
		display.refreshEstimationList(estimationList);
		display.updateSortHandler();
	}

   /**
    * This method updates the listbox of lot with the last list of estimations obtained
    */
   private void refreshLot() {
      List<String> lots = new ArrayList<String>();
      lots.add(Common.GLOBAL.select());
      display.getTotalCharge().setText(Common.EMPTY_TEXT);
      display.getLotLB().clear();
      display.getLotLB().addItem(Common.GLOBAL.select());
      for (EstimationDTO estimation : estimationList) {
         String lot = estimation.getScopeUnit().getLotName();
         //we take lot which doesn't have parent
         boolean hasParent = estimation.getScopeUnit().getParentLotId() != null
               && !estimation.getScopeUnit().getParentLotId().equals(Common.EMPTY_TEXT);
         if (lot != null && !hasParent && !lots.contains(lot)) {
            lots.add(lot);
            display.getLotLB().addItem(lot);
         }
      }
   }

	/**
	 * This method updates the listbox of sublot depending on the selected lot
	 */
	private void refreshSubLot() {
		List<String> subLots = new ArrayList<String>();
		subLots.add(Common.GLOBAL.select());
		display.getSubLotLB().clear();
		display.getSubLotLB().addItem(Common.GLOBAL.select());
		for (EstimationDTO estimation : estimationList) {
		   //for sublot we take lot which parent is equal to other combo
			if (estimation.getScopeUnit().getParentLotName() != null && estimation.getScopeUnit().getParentLotName()
					.equals(display.getLotLB().getItemText(display.getLotLB().getSelectedIndex()))) {
				String subLot = estimation.getScopeUnit().getLotName();
				if (subLot != null && !subLots.contains(subLot)) {
					subLots.add(subLot);
					display.getSubLotLB().addItem(subLot);
				}
			}
		}
		if (subLots.size() > 1) {
			display.getSubLotLB().setEnabled(true);
		}
	}

	@Override
   public IsWidget getDisplay() {
		return display.asWidget();
	}

	/**
	 * Get the appropriate view mode from evaluation of project plan status
	 */
	private ViewEnum getAppropriateViewModeFromProjectPlanStatus() {
		if (ProjectPlanStatus.PROJECTPLAN_STATUS_DRAFT.equals(projectPlan.getStatus())) {
			return ViewEnum.EDIT;
		} else {
			return ViewEnum.READ;
		}
	}

}
