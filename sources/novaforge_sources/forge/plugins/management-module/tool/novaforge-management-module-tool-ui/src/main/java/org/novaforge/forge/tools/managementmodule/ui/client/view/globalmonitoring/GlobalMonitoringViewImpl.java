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
package org.novaforge.forge.tools.managementmodule.ui.client.view.globalmonitoring;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.novaforge.forge.tools.managementmodule.ui.client.event.pilotageexecution.UpdateGlobalAdvancementEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.CustomListBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableEditTextCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableTextCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.ValidateDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.LotDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitGlobalMonitoringDTO;

import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author BILET-JC
 * 
 */
public class GlobalMonitoringViewImpl extends Composite implements GlobalMonitoringView {

	public static final Comparator<ScopeUnitGlobalMonitoringDTO> SCOPE_UNIT_COMPARATOR = new Comparator<ScopeUnitGlobalMonitoringDTO>()
	{

		@Override
		public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2)
		{
			return o1.getScopeUnitName().compareTo(o2.getScopeUnitName());
		}
	};
	private static GlobalMonitoringViewImplUiBinder uiBinder = GWT
			.create(GlobalMonitoringViewImplUiBinder.class);
	private final SimpleEventBus localEventBus;
	private final ValidateDialogBox validateFinishScopeUnit            = new ValidateDialogBox(Common.MESSAGES_MONITORING
																																																 .messageFinishScopeUnitWarning());
	private final InfoDialogBox     impossibleFinishScopeUnitTask      = new InfoDialogBox(Common.MESSAGES_MONITORING
																																														 .messageFinishScopeUnitImpossibleTask(),
																																												 InfoTypeEnum.WARNING);
	private final InfoDialogBox     impossibleFinishScopeUnitChild     = new InfoDialogBox(Common.MESSAGES_MONITORING
																																														 .messageFinishScopeUnitImpossibleChild(),
																																												 InfoTypeEnum.WARNING);
	private final InfoDialogBox     impossibleFinishScopeUnitRemaining = new InfoDialogBox(Common.MESSAGES_MONITORING
																																														 .messageFinishScopeUnitImpossibleRemaining(),
																																												 InfoTypeEnum.WARNING);
	private final InfoDialogBox     successUpdateRemainingScopeUnit    = new InfoDialogBox(Common.MESSAGES_MONITORING
																																														 .messageUpdateRemainingScopeUnitSuccess(),
																																												 InfoTypeEnum.OK);
	private final InfoDialogBox     unsucessUpdateRemainingScopeUnit   = new InfoDialogBox(Common.MESSAGES_MONITORING
																																														 .messageUpdateRemainingScopeUnitUnsuccess(),
																																												 InfoTypeEnum.WARNING);
	private final InfoDialogBox     successFinishScopeUnit             = new InfoDialogBox(Common.MESSAGES_MONITORING
																																														 .messageFinishScopeUnitSuccess(),
																																												 InfoTypeEnum.OK);
	@UiField
	Panel scrollPanel;
	@UiField
	Panel parentDetails;
	@UiField
	Label globalMonitoringTitle;
	// action
	@UiField
	Button acronymB;
	@UiField
	Button finishScopeUnitB;
	@UiField
	Button saveB;
	@UiField
	Button exportCSVB;
	@UiField
	Button buttonHomeReturn;
	// indicators
	@UiField
	Label scopeUnitNumberL;
	@UiField
	Label scopeUnitNumber;
	@UiField
	Label percentScopeUnitDoneL;
	@UiField
	Label percentScopeUnitDone;
	@UiField
	Label scopeUnitToDoL;
	@UiField
	Label scopeUnitToDo;
	@UiField
	Label averageFocalisationFactorL;
	@UiField
	Label averageFocalisationFactor;
	@UiField
	Label averageErrorEstimationL;
	@UiField
	Label averageErrorEstimation;
	@UiField
	Label lastIdealFPL;
	@UiField
	Label lastIdealFP;
	@UiField
	Label averageVelocityL;
	@UiField
	Label averageVelocity;
	@UiField
	Label globalAdvancementL;
	@UiField
	Label globalAdvancement;
	// filter
	@UiField
	Label filterTitle;
	@UiField
	Label resultTitle;
	@UiField
	Label projectStartDateL;
	@UiField
	Label projectStartDate;
	@UiField
	Label lotParentL;
	@UiField
	CustomListBox<LotDTO> lotParent;
	@UiField
	Label lotL;
	@UiField
	CustomListBox<LotDTO> lot;
	@UiField
	Label statusL;
	@UiField
	ListBox status;
	@UiField
	Label scopeUnitParentL;
	@UiField
	ListBox scopeUnitParent;
	// parent details
	@UiField
	Label parentDetailsTitle;
	@UiField
	Label parentScopeUnitL;
	@UiField
	TextBox parentScopeUnit;
	@UiField
	Label parentEstimationL;
	@UiField
	TextBox parentEstimation;
	@UiField
	Label parentAdvancementL;
	@UiField
	TextBox parentAdvancement;
	@UiField
	Label parentStatusL;
	@UiField
	TextBox parentStatus;
	@UiField
	Label parentConsumedL;
	@UiField
	TextBox parentConsumed;
	@UiField
	Label parentRemainingScopeUnitL;
	@UiField
	TextBox parentRemainingScopeUnit;
	@UiField
	Label parentRemainingL;
	@UiField
	TextBox parentRemaining;
	@UiField
	Label parentReestimateL;
	@UiField
	TextBox parentReestimate;
	// table
	@UiField(provided = true)
	CellTable<ScopeUnitGlobalMonitoringDTO> globalMonitoringCT;
	@UiField(provided = true)
	SimplePager globalMonitoringPager;
	private ListDataProvider<ScopeUnitGlobalMonitoringDTO> dataProvider;
	private SingleSelectionModel<ScopeUnitGlobalMonitoringDTO> selectionModel = new SingleSelectionModel<ScopeUnitGlobalMonitoringDTO>();
	// all column displayed in that order
	private Column<ScopeUnitGlobalMonitoringDTO, String> scopeUnitParentC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> scopeUnitC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> estimateC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> consumedC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> remainingScopeUnitC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> remainingTaskC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> reestimateC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> advancementC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> statusC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> lotParentC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> lotC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> priorityC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> benefitC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> riskC;
	private Column<ScopeUnitGlobalMonitoringDTO, String> injuryC;

	/**
	 * Constructor
	 * @param localEventBus
	 */
	public GlobalMonitoringViewImpl(SimpleEventBus localEventBus) {
		this.localEventBus = localEventBus;
		Common.RESOURCE.css().ensureInjected();
		initGlobalMonitoringCT();
		initWidget(uiBinder.createAndBindUi(this));
		globalMonitoringTitle.setText(Common.MESSAGES_MONITORING.scopeUnitMonitorings());
		// action
		acronymB.setText(Common.MESSAGES_MONITORING.acronym());
		saveB.setText(Common.GLOBAL.buttonSave());
		finishScopeUnitB.setText(Common.MESSAGES_MONITORING.finishScopeUnit());
		exportCSVB.setText(Common.GLOBAL.buttonExportCSV());
		finishScopeUnitB.setEnabled(false);
		// context filter lot
		projectStartDateL.setText(Common.MESSAGES_MONITORING.projectStartDateL());
		lotParentL.setText(Common.MESSAGES_MONITORING.lotL());
		lotL.setText(Common.MESSAGES_MONITORING.subLotL());
		// indicators
		scopeUnitNumberL.setText(Common.MESSAGES_MONITORING.scopeUnitNumberL());
		percentScopeUnitDoneL.setText(Common.MESSAGES_MONITORING.percentScopeUnitDoneL());
		scopeUnitToDoL.setText(Common.MESSAGES_MONITORING.scopeUnitToDoL());
		averageFocalisationFactorL.setText(Common.MESSAGES_MONITORING.averageFocalisationFactorL());
		averageErrorEstimationL.setText(Common.MESSAGES_MONITORING.averageErrorEstimationL());
		lastIdealFPL.setText(Common.MESSAGES_MONITORING.lastIdealFPL());
		averageVelocityL.setText(Common.MESSAGES_MONITORING.averageVelocityL());
		globalAdvancementL.setText(Common.MESSAGES_MONITORING.globalAdvancement());
		// filter
		filterTitle.setText(Common.GLOBAL.filterTitle());
		resultTitle.setText(Common.GLOBAL.resultTitle());
		scopeUnitParentL.setText(Common.MESSAGES_MONITORING.scopeUnitParentL());
		statusL.setText(Common.MESSAGES_MONITORING.scopeUnitStatusL());
		buttonHomeReturn.setText(Common.GLOBAL.homeReturn());
		// parent details
		parentDetails.setVisible(false);
		parentDetailsTitle.setVisible(false);
		parentDetailsTitle.setText(Common.MESSAGES_MONITORING.parentDetailsTitle());
		parentScopeUnitL.setText(Common.MESSAGES_MONITORING.scopeUnitParentL());
		parentEstimationL.setText(Common.MESSAGES_MONITORING.estimationL());
		parentAdvancementL.setText(Common.MESSAGES_MONITORING.advancementLabel());
		parentStatusL.setText(Common.MESSAGES_MONITORING.statusL());
		parentConsumedL.setText(Common.MESSAGES_MONITORING.consumedLabel());
		parentRemainingScopeUnitL.setText(Common.MESSAGES_MONITORING.remainingScopeUnitL());
		parentRemainingL.setText(Common.MESSAGES_MONITORING.remainingTaskL());
		parentReestimateL.setText(Common.MESSAGES_MONITORING.reestimateL());
	}

	/**
	 * Init the globalMonitoring table
	 */
	private void initGlobalMonitoringCT() {
//FIXME
//		final Label loadingLabel = new Label(Common.GLOBAL.loadingMessage());
//		loadingLabel.setStyleName(Common.RESOURCE.css().emptyLabel());

		globalMonitoringCT = new CellTable<ScopeUnitGlobalMonitoringDTO>(Common.PAGE_SIZE,
				(Resources) GWT.create(TableResources.class), CellKey.GLOBAL_MONITORING_KEY_PROVIDER);
		globalMonitoringCT.setWidth("100%", false);

		// Init empty widget
		final Label emptyLabel = new Label(Common.MESSAGES_ESTIMATION.emptyEstimationMessage());
		emptyLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
		globalMonitoringCT.setEmptyTableWidget(emptyLabel);

		// Create a Pager to control the CellTable
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		globalMonitoringPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		globalMonitoringPager.setDisplay(globalMonitoringCT);

		// Add the CellTable to the adapter
		dataProvider = new ListDataProvider<ScopeUnitGlobalMonitoringDTO>();
		dataProvider.addDataDisplay(globalMonitoringCT);

		globalMonitoringCT.setSelectionModel(selectionModel);

		// Add the CellTable to the adapter
		final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(globalMonitoringCT);
		globalMonitoringCT.addColumnSortHandler(columnSortHandler);

		// Manage selectionModel with key up & down
		globalMonitoringCT.sinkEvents(Event.ONKEYDOWN);
		globalMonitoringCT.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (selectionModel.getSelectedObject() != null) {
					int pos;
					ScopeUnitGlobalMonitoringDTO object;
					final int key = event.getNativeKeyCode();
					if (KeyCodes.KEY_UP == key){
	                pos = globalMonitoringCT.getVisibleItems()
                         .indexOf(selectionModel.getSelectedObject());
                   if (pos > 0) {
                      object = globalMonitoringCT.getVisibleItems().get(pos - 1);
                      selectionModel.setSelected(object, true);
                      event.stopPropagation();
                      event.preventDefault();
                   }
					}else if (KeyCodes.KEY_DOWN == key){
					   pos = globalMonitoringCT.getVisibleItems()
                        .indexOf(selectionModel.getSelectedObject());
                  if (pos != globalMonitoringCT.getVisibleItems().size() - 1) {
                     object = globalMonitoringCT.getVisibleItems().get(pos + 1);
                     selectionModel.setSelected(object, true);
                     event.stopPropagation();
                     event.preventDefault();
                  }
					}
				}
			}
		}, KeyDownEvent.getType());

		// Initialize the columns.
		initGlobalMonitoringCTColumns();
	}

	private void initGlobalMonitoringCTColumns() {
		final Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("align", "center");
		// scope unit Column
		scopeUnitC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new TextCell()) {
			@Override
			public String getValue(final ScopeUnitGlobalMonitoringDTO object) {
				return object.getScopeUnitName();
			}
		};
		scopeUnitC.setSortable(true);
		globalMonitoringCT.addColumn(scopeUnitC, Common.MESSAGES_MONITORING.scopeUnit());
		// scope unit parent Column
		scopeUnitParentC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new TextCell()) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return object.getParentScopeUnitName();
			}
		};
		scopeUnitParentC.setSortable(true);
		// estimation Column
		estimateC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return object.getProjectPlanEstimation().toString();
			}
		};
		estimateC.setSortable(true);
		globalMonitoringCT.addColumn(estimateC,
				Common.getLinesHeader(Common.MESSAGES_MONITORING.estimation()));
		// consumed Column
		consumedC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return String.valueOf(Common.floatFormat(object.getConsumed(), 2));
			}
		};
		consumedC.setSortable(true);
		globalMonitoringCT.addColumn(consumedC, Common.getLinesHeader(Common.MESSAGES_MONITORING.consumed()));
		// remaining scope unit Column
		final Map<String, String> attributes2 = new HashMap<String, String>();
		attributes.put("size", "3");
		attributes.put("maxLength", "5");
		attributes.put("align", "center");
		// final TextInputCustomCell textInputCustomCell = new
		// TextInputCustomCell(null, 3, 5, null);
		final StylableEditTextCell stylableEditTextCell = new StylableEditTextCell(attributes2);
		final FieldUpdater<ScopeUnitGlobalMonitoringDTO, String> fieldUpdater = new FieldUpdater<ScopeUnitGlobalMonitoringDTO, String>() {
			@Override
			public void update(int index, ScopeUnitGlobalMonitoringDTO object, String value) {
				Float newValue = null;
//				check filled data
				if (value == null || value.equalsIgnoreCase(Common.EMPTY_TEXT)) {
					newValue = 0f;
				} else {
					try {
						newValue = Float.parseFloat(value);
					} catch (NumberFormatException nfe) {
						final InfoDialogBox popupWrongValue = new InfoDialogBox(
								Common.GLOBAL.messageWrongInputValue(), InfoTypeEnum.KO);
						popupWrongValue.getDialogPanel().center();
						popupWrongValue.getDialogPanel().show();
					}
				}
				if (newValue != null) {
					List<ScopeUnitGlobalMonitoringDTO> list = dataProvider.getList();
					int i = list.indexOf(object);
					ScopeUnitGlobalMonitoringDTO scopeUnitMonitoring = list.get(i);
					scopeUnitMonitoring.setRemainingScopeUnit(newValue);
					scopeUnitMonitoring.setReestimate(scopeUnitMonitoring.getConsumed()
							+ scopeUnitMonitoring.getRemainingScopeUnit()
							+ scopeUnitMonitoring.getRemainingTasks());
					scopeUnitMonitoring.setAdvancement(Common.floatFormat(scopeUnitMonitoring.getConsumed()
							/ scopeUnitMonitoring.getReestimate(), 2));
					list.set(i, scopeUnitMonitoring);
					dataProvider.setList(list);
					// pass out GWT edit text cell cache
					stylableEditTextCell.clearViewData(object.getUnitId());
					globalMonitoringCT.redraw();
					localEventBus.fireEvent(new UpdateGlobalAdvancementEvent(scopeUnitMonitoring.getUnitId(),
							newValue));
				}
			}
		};
		remainingScopeUnitC = new Column<ScopeUnitGlobalMonitoringDTO, String>(stylableEditTextCell) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				String ret = "";
				if (object.getRemainingScopeUnit() != null) {
					ret = Common.floatFormat(object.getRemainingScopeUnit(), 2).toString();
				}
				return ret;
			}
		};
		remainingScopeUnitC.setSortable(true);
		globalMonitoringCT.addColumn(remainingScopeUnitC, Common.MESSAGES_MONITORING.remainingScopeUnit());
		remainingScopeUnitC.setFieldUpdater(fieldUpdater);
		// remaining task Column
		remainingTaskC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return String.valueOf(Common.floatFormat(object.getRemainingTasks(), 2));
			}
		};
		remainingTaskC.setSortable(true);
		globalMonitoringCT.addColumn(remainingTaskC,
				Common.getLinesHeader(Common.MESSAGES_MONITORING.remainingTask()));
		// reestimate Column
		reestimateC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return String.valueOf(Common.floatFormat(object.getReestimate(), 2));
			}
		};
		reestimateC.setSortable(true);
		globalMonitoringCT.addColumn(reestimateC,
				Common.getLinesHeader(Common.MESSAGES_MONITORING.reestimate()));
		// advancement Column
		advancementC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return String.valueOf(Common.floatFormat(object.getAdvancement()*100, 2));
			}
		};
		advancementC.setSortable(true);
		globalMonitoringCT.addColumn(advancementC,
				Common.getLinesHeader(Common.MESSAGES_MONITORING.advancement()));
		// status Column
		statusC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new TextCell()) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return object.getStatus().getLabel();
			}
		};
		statusC.setSortable(true);
		globalMonitoringCT.addColumn(statusC, Common.MESSAGES_MONITORING.status());
		// lot parent Column
		lotParentC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new TextCell()) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return object.getLotName();
			}
		};
		lotParentC.setSortable(true);
		globalMonitoringCT.addColumn(lotParentC, Common.MESSAGES_MONITORING.lot());
		// lot Column
		lotC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new TextCell()) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return object.getParentLotName();
			}
		};
		lotC.setSortable(true);
		globalMonitoringCT.addColumn(lotC, Common.MESSAGES_MONITORING.parentLot());
		// priority Column
		priorityC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return object.getWeight().toString();
			}
		};
		priorityC.setSortable(true);
		globalMonitoringCT.addColumn(priorityC, Common.getLinesHeader(Common.MESSAGES_MONITORING.priority()));
		// benefit Column
		benefitC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return object.getBenefit().toString();
			}
		};
		benefitC.setSortable(true);
		globalMonitoringCT.addColumn(benefitC, Common.getLinesHeader(Common.MESSAGES_MONITORING.benefit()));
		// risk Column
		riskC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return object.getRisk().toString();
			}
		};
		riskC.setSortable(true);
		globalMonitoringCT.addColumn(riskC, Common.getLinesHeader(Common.MESSAGES_MONITORING.risk()));
		// injury Column
		injuryC = new Column<ScopeUnitGlobalMonitoringDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitGlobalMonitoringDTO object) {
				return object.getInjury().toString();
			}
		};
		injuryC.setSortable(true);
		globalMonitoringCT.addColumn(injuryC, Common.getLinesHeader(Common.MESSAGES_MONITORING.injury()));
	}

	@Override
	public ListDataProvider<ScopeUnitGlobalMonitoringDTO> getDataProvider()
	{
		return dataProvider;
	}

	@Override
	public void updateSortHandler() {
		// Add the Sort Handler to the CellTable
		final ListHandler<ScopeUnitGlobalMonitoringDTO> sortHandler = new ListHandler<ScopeUnitGlobalMonitoringDTO>(
				dataProvider.getList());
				globalMonitoringCT.addColumnSortHandler(sortHandler);

		// scope unit parent sort
		sortHandler.setComparator(scopeUnitParentC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getParentScopeUnitName().compareTo(o2.getParentScopeUnitName());
			}
		});
		// scope unit sort
		sortHandler.setComparator(scopeUnitC, SCOPE_UNIT_COMPARATOR);
		// estimation sort
		sortHandler.setComparator(estimateC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getProjectPlanEstimation().compareTo(o2.getProjectPlanEstimation());
			}
		});
		// consumed sort
		sortHandler.setComparator(consumedC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getConsumed().compareTo(o2.getConsumed());
			}
		});
		// remaining scope unit sort
		sortHandler.setComparator(remainingScopeUnitC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getRemainingScopeUnit().compareTo(o2.getRemainingScopeUnit());
			}
		});
		// remaining tache sort
		sortHandler.setComparator(remainingTaskC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getRemainingTasks().compareTo(o2.getRemainingTasks());
			}
		});
		// reestimate sort
		sortHandler.setComparator(reestimateC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return ((Float) o1.getReestimate()).compareTo(o2.getReestimate());
			}
		});
		// advancement sort
		sortHandler.setComparator(advancementC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return ((Float) o1.getAdvancement()).compareTo(o2.getAdvancement());
			}
		});
		// status sort
		sortHandler.setComparator(statusC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getStatus().compareTo(o2.getStatus());
			}
		});
		// parent lot sort
		sortHandler.setComparator(lotParentC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getParentLotName().compareTo(o2.getParentLotName());
			}
		});
		// lot sort
		sortHandler.setComparator(lotC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getLotName().compareTo(o2.getLotName());
			}
		});
		// priority sort
		sortHandler.setComparator(priorityC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getWeight().compareTo(o2.getWeight());
			}
		});
		// benefit sort
		sortHandler.setComparator(benefitC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getBenefit().compareTo(o2.getBenefit());
			}
		});
		// risk sort
		sortHandler.setComparator(riskC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getRisk().compareTo(o2.getRisk());
			}
		});
		// injury sort
		sortHandler.setComparator(injuryC, new Comparator<ScopeUnitGlobalMonitoringDTO>() {

			@Override
			public int compare(ScopeUnitGlobalMonitoringDTO o1, ScopeUnitGlobalMonitoringDTO o2) {
				return o1.getInjury().compareTo(o2.getInjury());
			}
		});
    
    if ( globalMonitoringCT.getColumnSortList().size() > 0)
    {
      ColumnSortEvent.fire( globalMonitoringCT, globalMonitoringCT.getColumnSortList());
    }

	}

	@Override
	public ListBox getFilterLot()
	{
		return lot;
	}

	@Override
	public ListBox getFilterLotParent()
	{
		return lotParent;
	}

	@Override
	public ListBox getFilterScopeUnitParent() {
		return scopeUnitParent;
	}

	@Override
	public ListBox getFilterStatus() {
		return status;
	}

	@Override
	public Label getIndicatorScopeUnitNumber() {
		return scopeUnitNumber;
	}

	@Override
	public Label getIndicatorPercentScopeUnitDone() {
		return percentScopeUnitDone;
	}

	@Override
	public Label getIndicatorScopeUnitToDo() {
		return scopeUnitToDo;
	}

	@Override
	public Label getIndicatorAverageFocalisationFactor() {
		return averageFocalisationFactor;
	}

	@Override
	public Label getIndicatorAverageErrorEstimation() {
		return averageErrorEstimation;
	}

	@Override
	public Label getIndicatorLastIdealFP() {
		return lastIdealFP;
	}

	@Override
	public Label getIndicatorAverageVelocity() {
		return averageVelocity;
	}

	@Override
	public Label getIndicatorGlobalAdvancement() {
		return globalAdvancement;
	}

	@Override
	public Button getButtonAcronym() {
		return acronymB;
	}

	@Override
	public Button getButtonSave() {
		return saveB;
	}

	@Override
	public Button getButtonFinishScopeUnit() {
		return finishScopeUnitB;
	}

	@Override
	public SingleSelectionModel<ScopeUnitGlobalMonitoringDTO> getSelectionModel()
	{
		return selectionModel;
	}

	@Override
	public Label getProjectStartDate()
	{
		return projectStartDate;
	}

	@Override
	public HasWidgets getContentPanel() {
		return scrollPanel;
	}

	@Override
	public Button getButtonHomeReturn() {
		return buttonHomeReturn;
	}

	@Override
	public ValidateDialogBox getValidateFinishScopeUnit() {
		return validateFinishScopeUnit;
	}

	@Override
	public InfoDialogBox getImpossibleFinishScopeUnitTask() {
		return impossibleFinishScopeUnitTask;
	}

	@Override
	public InfoDialogBox getImpossibleFinishScopeUnitChild() {
		return impossibleFinishScopeUnitChild;
	}

	@Override
	public InfoDialogBox getImpossibleFinishScopeUnitRemaining() {
		return impossibleFinishScopeUnitRemaining;
	}

	@Override
	public InfoDialogBox getSuccessUpdateRemainingScopeUnit() {
		return successUpdateRemainingScopeUnit;
	}

	@Override
	public InfoDialogBox getUnsuccessUpdateRemainingScopeUnit() {
		return unsucessUpdateRemainingScopeUnit;
	}

	@Override
	public void removeRemainingScopeUnitColumn(final Boolean b) {
		if (globalMonitoringCT.getColumnIndex(remainingScopeUnitC) != -1 && b) {
			globalMonitoringCT.insertColumn(globalMonitoringCT.getColumnIndex(estimateC),
					scopeUnitParentC, Common.MESSAGES_MONITORING.scopeUnitParent());
			globalMonitoringCT.removeColumn(remainingScopeUnitC);
		} else if (globalMonitoringCT.getColumnIndex(remainingScopeUnitC) == -1 && !b) {
			globalMonitoringCT.insertColumn(globalMonitoringCT.getColumnIndex(remainingTaskC),
					remainingScopeUnitC, Common.MESSAGES_MONITORING.remainingScopeUnit());
			globalMonitoringCT.removeColumn(scopeUnitParentC);
		}
	}

	@Override
	public void displayParentDetails(final ScopeUnitGlobalMonitoringDTO parent, final Float remainingScopeUnit) {
		boolean b = false;
		if (parent != null) {
			b = true;
			parentScopeUnit.setText(parent.getScopeUnitName());
			parentEstimation.setText(parent.getProjectPlanEstimation().toString());
			final Integer p = (int) (parent.getAdvancement() * 100);
			parentAdvancement.setText(p.toString());
			parentStatus.setText(parent.getStatus().getLabel());
			parentConsumed.setText(parent.getConsumed().toString());
			parentRemaining.setText(parent.getRemainingTasks().toString());
			parentReestimate.setText(String.valueOf(parent.getReestimate()));
			parentRemainingScopeUnit.setText(remainingScopeUnit.toString());
		}
		parentDetailsTitle.setVisible(b);
		parentDetails.setVisible(b);

	}

	@Override
	public InfoDialogBox getSuccessFinishScopeUnit()
	{
		return successFinishScopeUnit;
	}

	@Override
	public Button getButtonExportCSV()
	{
		return exportCSVB;
	}

	@Override
	public CellTable<ScopeUnitGlobalMonitoringDTO> getCT()
	{
		//TODO JCB use?
		return globalMonitoringCT;
	}

	@Override
	public Comparator<ScopeUnitGlobalMonitoringDTO> getScopeUnitComparator() {
		return SCOPE_UNIT_COMPARATOR;
	}

	interface GlobalMonitoringViewImplUiBinder extends UiBinder<Widget, GlobalMonitoringViewImpl>
	{
	}

}
