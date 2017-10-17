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

package org.novaforge.forge.tools.managementmodule.ui.client.view.estimation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.novaforge.forge.tools.managementmodule.ui.client.event.estimation.DisplayScopeUnitDetailsEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.event.estimation.RefreshEstimationChargeEvent;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.CheckboxDisabledCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.SelectionDisabledCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableTextCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TextInputCustomCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltree.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ComponentEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.EstimationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PriorizationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.PriorizationEnum;

import com.google.gwt.cell.client.ActionCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.Header;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.cellview.client.TextHeader;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author BILET-JC This class displays estimation interface
 */
public class EstimationViewImpl extends ResizeComposite implements
		EstimationView {

	private static EstimationViewImplUiBinder uiBinder = GWT
			.create(EstimationViewImplUiBinder.class);
	private static int pageSizefp = 5;
	private final InfoDialogBox infoDB;
	private final Comparator<EstimationDTO> SCOPE_UNIT_COMPARATOR = new Comparator<EstimationDTO>()
	{
		@Override
		public int compare(EstimationDTO o1, EstimationDTO o2)
		{
			return o1.getScopeUnit().getName().compareTo(o2.getScopeUnit().getName());
		}
	};
	@UiField
	Panel scrollPanel;
	@UiField
	Panel leftPanel;
	@UiField
	Panel rightPanel;
	@UiField
	Label estimationTitle;
	@UiField
	Label scopeUnitsTitle;
	@UiField
	Label fpTitle;
	@UiField
	Label fpIndicatorsTitle;
	@UiField
	Label priorizationTitle;
	@UiField
	Label totalTitle;
	@UiField
	Label lotL;
	@UiField
	Label subLotL;
	@UiField
	Label totalL;
	@UiField
	Label fpTypeL;
	@UiField
	ListBox fpType;
	@UiField
	ListBox componentType;
	@UiField
	ListBox lotLB;
	@UiField
	ListBox subLotLB;
	@UiField
	TextBox totalTB;
	@UiField
	Button validateEstimationB;
	@UiField
	Button disciplineSharingB;
	@UiField
	Button acronymB;
	// fp's indicators
	@UiField
	Label fpRawL;
	@UiField
	TextBox fpRawTB;
	@UiField
	Label fpAdjustedL;
	@UiField
	TextBox fpAdjustedTB;
	@UiField
	Label adjustementCoefL;
	@UiField
	TextBox adjustementCoefTB;
	@UiField(provided = true)
	CellTable<EstimationDTO> estimationCT;
	@UiField(provided = true)
	SimplePager estimationPager;
	@UiField(provided = true)
	CellTable<PriorizationDTO> priorizationCT;
	@UiField(provided = true)
	CellTable<ComponentDTO> fpCT;
	private ListDataProvider<EstimationDTO> dataProvider;
	private ListDataProvider<ComponentDTO> fpDataProvider;
	private ListDataProvider<PriorizationDTO> priorizationDataProvider;
	private SingleSelectionModel<EstimationDTO> selectionModel;
	// about the scope unit
	private Column<EstimationDTO, String> lotColumn;
	private Column<EstimationDTO, String> parentLotColumn;
	private Column<EstimationDTO, String> scopeUnitParentColumn;
	private Column<EstimationDTO, String> scopeUnitColumn;
	private Column<EstimationDTO, String> scopeUnitVersionColumn;
	private Column<EstimationDTO, String> lastChargeColumn;
	private Column<EstimationDTO, String> chargeBisColumn;
	private Column<EstimationDTO, String> chargeColumn;
	private Column<EstimationDTO, Boolean> isManualColumn;
	private CheckboxDisabledCell isManualCheckboxCell;
	private TextInputCustomCell chargeCell;
	// fp
	private TextInputCustomCell simpleStylableEditTextCell;
	private TextInputCustomCell mediumStylableEditTextCell;
	private TextInputCustomCell complexStylableEditTextCell;
	private Column<ComponentDTO, String> labelComponantC;
	private Column<ComponentDTO, String> simpleC;
	private Column<ComponentDTO, String> medianC;
	private Column<ComponentDTO, String> complexC;
	// priorization
	private PriorizationDTO benefit;
	private PriorizationDTO risk;
	private PriorizationDTO injury;
	private Column<PriorizationDTO, String> priorizationLabelColumn;
	private SelectionDisabledCell priorityzationCell;
	private Column<PriorizationDTO, String> priorizationValueColumn;
	private List<EstimationDTO> estimationList;
	private Float nbHourByDay;
	private Float adjustementCoef;

	/**
	 * Constructor
	 */
	public EstimationViewImpl() {
		estimationList = new ArrayList<EstimationDTO>();
		Common.RESOURCE.css().ensureInjected();
		initEstimationCT();
		initfpCT();
		initPriorizationCT();
		initWidget(uiBinder.createAndBindUi(this));
		displayFP();
		validateEstimationB.setText(Common.GLOBAL.buttonValidate());
		disciplineSharingB.setText(Common.MESSAGES_ESTIMATION
				.disciplineSharingTitle());
		acronymB.setText(Common.MESSAGES_ESTIMATION.acronym());
		estimationTitle.setText(Common.MESSAGES_ESTIMATION.estimationTitle());
		scopeUnitsTitle.setText(Common.MESSAGES_ESTIMATION.scopeUnitsTitle());
		fpTitle.setText(Common.MESSAGES_ESTIMATION.FPTitle());
		fpIndicatorsTitle.setText(Common.MESSAGES_ESTIMATION
				.FPIndicatorsTitle());
		priorizationTitle.setText(Common.MESSAGES_ESTIMATION
				.priorizationTitle());
		// fp's indicators
		fpRawL.setText(Common.MESSAGES_ESTIMATION.pf() + " "
				+ Common.MESSAGES_ESTIMATION.raw());
		fpAdjustedL.setText(Common.MESSAGES_ESTIMATION.pf() + " "
				+ Common.MESSAGES_ESTIMATION.adjusted());
		adjustementCoefL.setText(Common.MESSAGES_ESTIMATION.factor() + " "
				+ Common.MESSAGES_ESTIMATION.adjustment());
		fpRawTB.setEnabled(false);
		fpAdjustedTB.setEnabled(false);
		adjustementCoefTB.setEnabled(false);
		// priorization
		benefit = new PriorizationDTO(PriorizationEnum.BENEFIT, 0);
		risk = new PriorizationDTO(PriorizationEnum.RISK, 0);
		injury = new PriorizationDTO(PriorizationEnum.INJURY, 0);
		// fp TYPE
		fpTypeL.setText(Common.MESSAGES_ESTIMATION.fpType());
		// total charge
		totalTitle.setText(Common.MESSAGES_ESTIMATION.totalTitle());
		lotL.setText(Common.MESSAGES_ESTIMATION.lotL());
		subLotL.setText(Common.MESSAGES_ESTIMATION.subLotL());
		totalL.setText(Common.MESSAGES_ESTIMATION.totalL());
		totalTB.setEnabled(false);
		subLotLB.setEnabled(false);
		infoDB = new InfoDialogBox(
				Common.MESSAGES_ESTIMATION.validationMessage(), InfoTypeEnum.OK);
	}

	/**
	 * This method initializes the estimation cellTable
	 */
	private void initEstimationCT() {

		estimationCT = new CellTable<EstimationDTO>(Common.PAGE_SIZE,
				(Resources) GWT.create(TableResources.class),
				CellKey.ESTIMATION_KEY_PROVIDER);
		estimationCT.setWidth("100%", false);

		// Init empty widget
		Label emptyLabel = new Label(
				Common.MESSAGES_ESTIMATION.emptyEstimationMessage());
		emptyLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
		estimationCT.setEmptyTableWidget(emptyLabel);

		// Create a Pager to control the CellTable
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		estimationPager = new SimplePager(TextLocation.CENTER, pagerResources,
				false, 0, true);
		estimationPager.setDisplay(estimationCT);

		// Initialize the columns.
		initEstimationTableColumns();

		// Add the CellTable to the adapter
		dataProvider = new ListDataProvider<EstimationDTO>();
		dataProvider.addDataDisplay(estimationCT);

		selectionModel = new SingleSelectionModel<EstimationDTO>();
		estimationCT.setSelectionModel(selectionModel);

		// Add the CellTable to the adapter
		ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(
				estimationCT);
		estimationCT.addColumnSortHandler(columnSortHandler);

		estimationCT.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		
		// Manage selectionModel with key up & down
		// estimationCT.sinkEvents(Event.ONKEYDOWN);
		// estimationCT.addDomHandler(new KeyDownHandler() {
		//
		// @Override
		// public void onKeyDown(KeyDownEvent event) {
		// if (selectionModel.getSelectedObject() != null) {
		// int pos;
		// EstimationDTO object;
		// int key = event.getNativeKeyCode();
		// switch (key) {
		// case KeyCodes.KEY_UP:
		// pos =
		// estimationCT.getVisibleItems().indexOf(selectionModel.getSelectedObject());
		// if (pos > 0) {
		// object = estimationCT.getVisibleItems().get(pos - 1);
		// selectionModel.setSelected(object, true);
		// event.stopPropagation();
		// event.preventDefault();
		// }
		// break;
		// case KeyCodes.KEY_DOWN:
		// pos =
		// estimationCT.getVisibleItems().indexOf(selectionModel.getSelectedObject());
		// if (pos != estimationCT.getVisibleItems().size() - 1) {
		// object = estimationCT.getVisibleItems().get(pos + 1);
		// selectionModel.setSelected(object, true);
		// event.stopPropagation();
		// event.preventDefault();
		// }
		// break;
		// }
		// }
		// }
		// }, KeyDownEvent.getType());

		// estimationCT.sinkEvents(Event.ONCLICK);
		//
		// estimationCT.addDomHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// //TODO
		//
		// }
		// }, ClickEvent.getType());
	}

	/**
	 * Initializes the fp table
	 */
	private void initfpCT()
	{
		fpCT = new CellTable<ComponentDTO>(pageSizefp, (Resources) GWT.create(TableResources.class),
																			 CellKey.COMPOSANT_KEY_PROVIDER);
		fpCT.setWidth("100%", false);

		// Add the CellTable to the adapter
		fpDataProvider = new ListDataProvider<ComponentDTO>();
		fpDataProvider.addDataDisplay(fpCT);
		initfpColumns();

		fpCT.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		fpCT.sinkEvents(Event.ONKEYDOWN);
		fpCT.addDomHandler(new FpCTKeyDownHandler(), KeyDownEvent.getType());
	}

	/**
	 * This method initializes the table of priorization
	 */
	private void initPriorizationCT()
	{
		priorizationCT = new CellTable<PriorizationDTO>(4, (Resources) GWT.create(TableResources.class));
		priorizationCT.setWidth("100%", false);

		// Initialize the columns.
		initPriorizationColumns();

		priorizationDataProvider = new ListDataProvider<PriorizationDTO>();
		priorizationDataProvider.addDataDisplay(priorizationCT);

	}

	/**
	 * This method initializes the estimation columns
	 */
	private void initEstimationTableColumns() {
		final Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("align", "center");
		// lot Column
		lotColumn = new LotColumn(new TextCell());
		lotColumn.setSortable(true);
		// parent lot Column
		parentLotColumn = new ParentLotColumn(new TextCell());
		parentLotColumn.setSortable(true);
		// scope unit parent Column
		scopeUnitParentColumn = new ScopeUnitParentColumn(new TextCell());
		scopeUnitParentColumn.setSortable(true);
		// scope unit Column
		scopeUnitColumn = new ScopeUnitColumn(new TextCell());
		scopeUnitColumn.setSortable(true);
		// version Column
		scopeUnitVersionColumn = new ScopeUnitVersionColumn(new StylableTextCell(attributes));
		scopeUnitVersionColumn.setSortable(true);
		// last charge Column
		lastChargeColumn = new LastChargeColumn(new StylableTextCell(attributes));
		lastChargeColumn.setSortable(true);
		// charge Column
		chargeCell = new TextInputCustomCell(null, null, 4, true);
		// chargeCell = new TextInputCell();
		chargeColumn = new ChargeColumn(chargeCell);
		chargeColumn.setSortable(true);
		chargeColumn.setFieldUpdater(new ChargeColumnFieldUpdater());
		// charge Column (H/h)
		chargeBisColumn = new ChargeBisColumn(new StylableTextCell(attributes));
		chargeBisColumn.setSortable(true);
		// details Column
		final SafeHtmlBuilder safeHtml = new SafeHtmlBuilder();
		safeHtml.appendHtmlConstant("<img src='" + Common.RESOURCE.details().getURL() + "'/>");

		final Cell<EstimationDTO> detailsCell = new ActionCell<EstimationDTO>(
				safeHtml.toSafeHtml(),
				new ActionCell.Delegate<EstimationDTO>() {
					@Override
					public void execute(EstimationDTO pObject) {
						fireEvent(new DisplayScopeUnitDetailsEvent(
								pObject.getScopeUnit()));
					}
				});
		Column<EstimationDTO, EstimationDTO> detailsColumn = new DetailsColumn(detailsCell);
		// IsManual column
		isManualCheckboxCell = new CheckboxDisabledCell();
		isManualColumn = new IsManualColumn(isManualCheckboxCell);
		isManualColumn
				.setFieldUpdater(new IsManualColumnFieldUpdater());
		estimationCT.setColumnWidth(isManualColumn, 80, Unit.PX);

		// add columns
		estimationCT.addColumn(scopeUnitColumn, Common.GLOBAL.SU());
		estimationCT.addColumn(scopeUnitParentColumn, Common.GLOBAL.SUParent());
		estimationCT.addColumn(scopeUnitVersionColumn,
				Common.MESSAGES_ESTIMATION.version());
		estimationCT.addColumn(lotColumn, Common.MESSAGES_ESTIMATION.lot());
		estimationCT.addColumn(parentLotColumn,
				Common.GLOBAL.parentLot());
		estimationCT.addColumn(lastChargeColumn,
				Common.MESSAGES_ESTIMATION.lastCharge());
		estimationCT.addColumn(chargeColumn,
				Common.MESSAGES_ESTIMATION.charge() + " en "
						+ Common.MESSAGES_ESTIMATION.HD());
		estimationCT.addColumn(chargeBisColumn,
				Common.MESSAGES_ESTIMATION.charge() + " en "
						+ Common.MESSAGES_ESTIMATION.HH());
		estimationCT.addColumn(detailsColumn,
				Common.MESSAGES_ESTIMATION.details());
		estimationCT.addColumn(isManualColumn,
				Common.MESSAGES_ESTIMATION.manual());
	}

	/**
	 * Initializes the fp columns
	 */
	private void initfpColumns() {
		// label column
		labelComponantC = new LabelComponantCColumn(new TextCell());
		labelComponantC.setSortable(false);
		fpCT.setColumnWidth(labelComponantC, 125, Unit.PX);

		// simple column
		simpleStylableEditTextCell = new TextInputCustomCell(null, 3, 3, null);
		simpleC = new SimpleCColumn(simpleStylableEditTextCell);
		setCommonfpColumnProperties(simpleC);
		simpleC.setFieldUpdater(new SimpleCFieldUpdater());
		// median column
		mediumStylableEditTextCell = new TextInputCustomCell(null, 3, 3, null);
		medianC = new MedianCColumn(mediumStylableEditTextCell);
		setCommonfpColumnProperties(medianC);
		medianC.setFieldUpdater(new MediumFieldUpdater());
		// complex column
		complexStylableEditTextCell = new TextInputCustomCell(null, 3, 3, null);
		complexC = new ComplexCColumn(complexStylableEditTextCell);
		setCommonfpColumnProperties(complexC);
		complexC.setFieldUpdater(new ComplexFieldUpdater());

		// add columns to CellTable
		fpCT.addColumn(labelComponantC, "");
		fpCT.addColumn(simpleC, Common.MESSAGES_ESTIMATION.S());
		fpCT.addColumn(medianC, Common.MESSAGES_ESTIMATION.M());
		fpCT.addColumn(complexC, Common.MESSAGES_ESTIMATION.C());

	}

	/**
	 * This method initializes priorization columns
	 */
	private void initPriorizationColumns() {
		// list of possibles value of priorization elements
		List<String> priorityzationUnities = new ArrayList<String>();
		for (int i = 1; i < 6; i++) {
			priorityzationUnities.add(""+i);
		}
		priorityzationCell = new SelectionDisabledCell(priorityzationUnities);

		// label column
		priorizationLabelColumn = new PriorizationLabelColumn(new TextCell());
		priorizationLabelColumn.setSortable(false);
		priorizationLabelColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		priorizationCT.addColumn(priorizationLabelColumn,
				Common.MESSAGES_ESTIMATION.priorizationType(),
				Common.MESSAGES_ESTIMATION.weight());
		priorizationCT.setColumnWidth(priorizationLabelColumn, 150, Unit.PX);
		// footer value column
		final Header<String> headerWeightColumn = new TextHeader(
				Common.MESSAGES_ESTIMATION.priorizationValue());
		final Header<String> footerWeightColumn = new FooterWeightColumnTextHeader("0");
		// value column
		priorizationValueColumn = new PriorizationValueColumn(priorityzationCell);
		priorizationValueColumn.setFieldUpdater(new PriorizationUpdater());
		priorizationValueColumn.setSortable(false);
		priorizationValueColumn
				.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		priorizationCT.addColumn(priorizationValueColumn, headerWeightColumn,
				footerWeightColumn);
		priorizationCT.setColumnWidth(priorizationValueColumn, 150, Unit.PX);

	}

	/**
	 * Refresh the priorization table with the given estimation
	 */
	private void refreshPriorization(EstimationDTO e)
	{
		Set<PriorizationDTO> priorizations = new HashSet<PriorizationDTO>();
		benefit.setValue(e.getBenefit());
		risk.setValue(e.getRisk());
		injury.setValue(e.getInjury());
		priorizations.add(benefit);
		priorizations.add(risk);
		priorizations.add(injury);
		priorizationDataProvider.setList(new ArrayList<PriorizationDTO>(priorizations));

	}

	private void refreshfpIndicator(EstimationDTO e)
	{
		if (e.getPfRaw() != null)
		{
			fpRawTB.setText(e.getPfRaw().toString());
		}
		if (e.getPfAdjusted() != null)
		{
			fpAdjustedTB.setText(e.getPfAdjusted().toString());
		}
		if (adjustementCoef != null)
		{
			adjustementCoefTB.setText(Common.floatFormat(adjustementCoef, 2).toString());
		}
	}

	/**
	 * Set the common properties of the composant's column
	 *
	 * @param column
	 *     the column to modify
	 */
	private void setCommonfpColumnProperties(Column<ComponentDTO, String> column)
	{
		column.setSortable(false);
		column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		fpCT.setColumnWidth(column, 75, Unit.PX);
	}

	/**
	 * Display the error pop up with not an inter message
	 */
	private void displayNotAnIntegerMessage()
	{
		InfoDialogBox info = new InfoDialogBox(Common.GLOBAL.messageNotAnInteger(), InfoTypeEnum.KO);
		info.getDialogPanel().center();
		info.getDialogPanel().show();
	}

	@Override
	public Button getValidateB()
	{
		return validateEstimationB;
	}

	@Override
	public ListDataProvider<EstimationDTO> getDataProvider() {
		return dataProvider;
	}

	@Override
	public ListDataProvider<ComponentDTO> getFPDataProvider()
	{
		return fpDataProvider;
	}
 
   @Override
   public void updateSortHandler() {
      // Add the Sort Handler to the CellTable
      ListHandler<EstimationDTO> sortHandler = new ListHandler<EstimationDTO>(dataProvider.getList());
      estimationCT.addColumnSortHandler(sortHandler);

      // Lot sort
      sortHandler.setComparator(lotColumn, new LotColumnComparator());
      // Sublot sort
      sortHandler.setComparator(parentLotColumn, new ParentLotComparator());
      // ScopeUnit sort
      sortHandler.setComparator(scopeUnitColumn, SCOPE_UNIT_COMPARATOR);
      // ScopeUnitParent sort
      sortHandler.setComparator(scopeUnitParentColumn, new ScopeUnitParentComparator());
      // ScopeUnit version sort
      sortHandler.setComparator(scopeUnitVersionColumn, new VersionComparator());
      // LasCharge sort
      sortHandler.setComparator(lastChargeColumn, new LastChargeColumnComparator());
      //current charge sort (day/man)
      sortHandler.setComparator(chargeColumn, new ChargeColumnComparator());
      //current charge sort (day/hour)
      sortHandler.setComparator(chargeBisColumn, new BisChargeColumnComparator());
      // IsManual sort
      sortHandler.setComparator(isManualColumn, new IsManualComparator());

      if ( estimationCT.getColumnSortList().size() > 0)
      {
        ColumnSortEvent.fire( estimationCT, estimationCT.getColumnSortList());
      }
   }

	@Override
	public SingleSelectionModel<EstimationDTO> getSelectionModel() {
		return selectionModel;
	}

	@Override
	public CellTable<EstimationDTO> getEstimationCT()
	{
		return estimationCT;
	}

	@Override
	public CellTable<ComponentDTO> getfpCT()
	{
		return fpCT;
	}

	@Override
	public void displayFP() {
		EstimationDTO e = selectionModel.getSelectedObject();
		if (e != null) {
			// charge manual, no fp
			if (e.isManual()) {
				displayfpCT(false, false);
			}
			// fp
			else {
				List<ComponentDTO> fps = new ArrayList<ComponentDTO>();
				// detailled way
				if (e.getSimple() == ComponentEnum.NONE) {
					ComponentDTO gdi = new ComponentDTO();
					ComponentDTO gde = new ComponentDTO();
					ComponentDTO in = new ComponentDTO();
					ComponentDTO out = new ComponentDTO();
					ComponentDTO interrogation = new ComponentDTO();
					gdi.setComposantLibelle(Common.MESSAGES_ESTIMATION.gdi());
					gdi.setComponent(ComponentEnum.GDI);
					gdi.setComposantValeurSimple(e.getGDIsimple());
					gdi.setComposantValeurMoyen(e.getGDImedian());
					gdi.setComposantValeurComplexe(e.getGDIcomplex());
					gde.setComposantLibelle(Common.MESSAGES_ESTIMATION.gde());
					gde.setComponent(ComponentEnum.GDE);
					gde.setComposantValeurSimple(e.getGDEsimple());
					gde.setComposantValeurMoyen(e.getGDEmedian());
					gde.setComposantValeurComplexe(e.getGDEcomplex());
					in.setComposantLibelle(Common.MESSAGES_ESTIMATION.in());
					in.setComponent(ComponentEnum.IN);
					in.setComposantValeurSimple(e.getINsimple());
					in.setComposantValeurMoyen(e.getINmedian());
					in.setComposantValeurComplexe(e.getINcomplex());
					out.setComposantLibelle(Common.MESSAGES_ESTIMATION.out());
					out.setComponent(ComponentEnum.OUT);
					out.setComposantValeurSimple(e.getOUTsimple());
					out.setComposantValeurMoyen(e.getOUTmedian());
					out.setComposantValeurComplexe(e.getOUTcomplex());
					interrogation
							.setComposantLibelle(Common.MESSAGES_ESTIMATION
									.interrogation());
					interrogation.setComponent(ComponentEnum.INT);
					interrogation.setComposantValeurSimple(e
							.getInterrogationSimple());
					interrogation.setComposantValeurMoyen(e
							.getInterrogationMedian());
					interrogation.setComposantValeurComplexe(e
							.getInterrogationComplex());
          fps.add(gdi);
          fps.add(in);
          fps.add(interrogation);
          fps.add(out);
					fps.add(gde);
					// display adapted UI
					displayfpCT(true, false);
				}
				// simpleway
				else {
					// add global fp to the list set to the data provider
					ComponentDTO c = new ComponentDTO();
					c.setComposantLibelle(Common.MESSAGES_ESTIMATION.global());
					c.setComponent(ComponentEnum.NONE);
					c.setComposantValeurSimple(e.getGlobalSimple());
					c.setComposantValeurMoyen(e.getGlobalMedian());
					c.setComposantValeurComplexe(e.getGlobalComplex());
					fps.add(c);
					// display adapted UI
					displayfpCT(true, true);

				}
				refreshPriorization(e);
				refreshfpIndicator(e);
				fpDataProvider.setList(fps);
			}
		} else {
			displayfpCT(false, false);
		}
	}

	@Override
	public CellTable<EstimationDTO> getTable() {
		return estimationCT;
	}

	@Override
	public ListBox getfpType() {
		return fpType;
	}

	@Override
	public ListBox getComponentType() {
		return componentType;
	}

	@Override
	public ListBox getLotLB() {
		return lotLB;
	}

	@Override
	public ListBox getSubLotLB() {
		return subLotLB;
	}

	@Override
	public TextBox getTotalCharge() {
		return totalTB;
	}

	@Override
	public HasWidgets getContentPanel()
	{
		return scrollPanel;
	}

	@Override
	public List<EstimationDTO> getEstimations()
	{
		return estimationList;
	}

	@Override
	public Column<EstimationDTO, String> getChargeC()
	{
		return chargeColumn;
	}

	@Override
	public void refreshEstimationList(List<EstimationDTO> pEstimationList) {
		estimationList = new ArrayList<EstimationDTO>();
		estimationList.addAll(pEstimationList);
		
		// int col = estimationCT.getColumnIndex(chargeColumn);
		// for (EstimationDTO estimation : estimationList) {
		// int row = estimationCT.getVisibleItems().indexOf(estimation);
		// Window.alert(String.valueOf(col) + "." + String.valueOf(row));
		// Element el = estimationCT.getKeyboardSelectedElement(col, row);
		// if (el != null) {
		//
		// Window.alert(el.toString() + " - " +
		// estimation.getScopeUnit().getName());
		// }
		// else {
		// Window.alert("null");
		// }
		// }
	}

	@Override
	public Button getAcronymB()
	{
		return acronymB;
	}

	@Override
	public void displayfpCT(boolean pDisplayElements, boolean isSimple)
	{
		fpTitle.setVisible(pDisplayElements);
		fpType.setVisible(pDisplayElements);
		leftPanel.setVisible(pDisplayElements);
		rightPanel.setVisible(pDisplayElements);
		if (pDisplayElements)
		{
			componentType.setVisible(isSimple);
		}
		else
		{
			componentType.setVisible(pDisplayElements);
		}
	}

	@Override
	public InfoDialogBox getInfoDB()
	{
		return infoDB;
	}

	@Override
	public void disabledCells(boolean disabled) {
		chargeCell.setDisabled(disabled);
		priorityzationCell.setDisabled(disabled);
		isManualCheckboxCell.setDisabled(disabled);
		simpleStylableEditTextCell.setDisabled(disabled);
		mediumStylableEditTextCell.setDisabled(disabled);
		complexStylableEditTextCell.setDisabled(disabled);
	}

	@Override
	public void setNbHourByDay(Float pNbHourByDay)
	{
		nbHourByDay = pNbHourByDay;

	}

	@Override
	public void setAdjustementCoef(Float adjustementCoef)
	{
		this.adjustementCoef = adjustementCoef;
	}

	@Override
	public Button getDisciplineSharingB()
	{
		return disciplineSharingB;
	}

	@Override
	public ListDataProvider<PriorizationDTO> getPriorizationDataProvider()
	{
		return priorizationDataProvider;
	}

	@Override
	public CellTable<PriorizationDTO> getPriorizationCT()
	{
		return priorizationCT;
	}

	@Override
	public PriorizationDTO getBenefit() {
		return benefit;
	}

	@Override
	public PriorizationDTO getRisk() {
		return risk;
	}

	@Override
	public PriorizationDTO getInjury() {
		return injury;
	}

	@Override
	public Comparator<EstimationDTO> getScopeUnitComparator() {
		return SCOPE_UNIT_COMPARATOR;
	}

	interface EstimationViewImplUiBinder extends UiBinder<Widget, EstimationViewImpl>
	{
	}

	private final class IsManualColumn extends Column<EstimationDTO, Boolean>
	{
		private IsManualColumn(Cell<Boolean> cell)
		{
			super(cell);
		}

		@Override
		public Boolean getValue(EstimationDTO object)
		{
			return object.isManual();
		}
	}

	private final class FooterWeightColumnTextHeader extends TextHeader
	{
		private FooterWeightColumnTextHeader(String text)
		{
			super(text);
		}

		@Override
		public String getValue()
		{
			final EstimationDTO e = selectionModel.getSelectedObject();
			if (e != null)
			{
				Integer w = estimationList.get(estimationList.indexOf(e)).getWeight();
				return String.valueOf(w);
			}
			else
			{
				return "0";
			}
		}
	}

	private final class LastChargeColumnComparator implements Comparator<EstimationDTO>
	{
		@Override
		public int compare(EstimationDTO o1, EstimationDTO o2)
		{
			return o1.getLastCharge().compareTo(o2.getLastCharge());
		}
	}

 private final class ChargeColumnComparator implements Comparator<EstimationDTO>
  {
    @Override
    public int compare(EstimationDTO o1, EstimationDTO o2)
    {
      return o1.getCharge().compareTo(o2.getCharge());
    }
  }

	
 private final class BisChargeColumnComparator implements Comparator<EstimationDTO>
 {
   @Override
   public int compare(EstimationDTO o1, EstimationDTO o2)
   {
     return o1.getCharge().compareTo(o2.getCharge());
   }
 }
 
	private final class PriorizationUpdater implements FieldUpdater<PriorizationDTO, String>
	{
		@Override
		public void update(int index, PriorizationDTO object, String value)
		{
			EstimationDTO e      = selectionModel.getSelectedObject();
			int           eIndex = estimationList.indexOf(e);
			Integer       i      = Integer.parseInt(value);
			if (object.getLabel().equals(PriorizationEnum.BENEFIT))
			{
				estimationList.get(eIndex).setBenefit(i);
				benefit.setValue(i);
			}
			else if (object.getLabel().equals(PriorizationEnum.RISK))
			{
				estimationList.get(eIndex).setRisk(i);
				risk.setValue(i);
			}
			else if (object.getLabel().equals(PriorizationEnum.INJURY))
			{
				estimationList.get(eIndex).setInjury(i);
				injury.setValue(i);
			}
			final Integer w = benefit.getValue() + injury.getValue() + risk.getValue();
			estimationList.get(eIndex).setWeight(w);
			priorizationCT.redraw();
		}
	}

	private final class ComplexCColumn extends Column<ComponentDTO, String>
	{
		private ComplexCColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(ComponentDTO object)
		{
			return String.valueOf(object.getComposantValeurComplexe());
		}
	}

	private final class ComplexFieldUpdater implements FieldUpdater<ComponentDTO, String>
	{
		@Override
		public void update(int index, ComponentDTO object, String value)
		{
			try
			{
				int intValue = Integer.valueOf(value);
				EstimationDTO e = selectionModel.getSelectedObject();
				switch (object.getComponent())
				{
					case NONE:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setGlobalComplex(intValue);
						break;
					case GDI:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setGDIcomplex(intValue);
						break;
					case GDE:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setGDEcomplex(intValue);
						break;
					case IN:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setINcomplex(intValue);
						break;
					case OUT:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setOUTcomplex(intValue);
						break;
					case INT:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setInterrogationComplex(intValue);
						break;
				}
				fpDataProvider.getList().get(index).setComposantValeurComplexe(intValue);
				fireEvent(new RefreshEstimationChargeEvent(dataProvider.getList().get(dataProvider.getList().indexOf(e))));

			}
			catch (NumberFormatException e)
			{
				displayNotAnIntegerMessage();
				// pass out GWT edit text cell cache
				complexStylableEditTextCell.clearViewData(object.getComposantLibelle());
				fpCT.redraw();
			}
		}
	}

	private final class MedianCColumn extends Column<ComponentDTO, String>
	{
		private MedianCColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(ComponentDTO object)
		{
			return String.valueOf(object.getComposantValeurMoyen());
		}
	}

	private final class MediumFieldUpdater implements FieldUpdater<ComponentDTO, String>
	{
		@Override
		public void update(int index, ComponentDTO object, String value)
		{
			try
			{
				int intValue = Integer.valueOf(value);
				EstimationDTO e = selectionModel.getSelectedObject();
				switch (object.getComponent())
				{
					case NONE:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setGlobalMedian(intValue);
						break;
					case GDI:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setGDImedian(intValue);
						break;
					case GDE:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setGDEmedian(intValue);
						break;
					case IN:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setINmedian(intValue);
						break;
					case OUT:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setOUTmedian(intValue);
						break;
					case INT:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setInterrogationMedian(intValue);
						break;
				}
				fpDataProvider.getList().get(index).setComposantValeurMoyen(intValue);
				fireEvent(new RefreshEstimationChargeEvent(dataProvider.getList().get(dataProvider.getList().indexOf(e))));
			}
			catch (NumberFormatException e)
			{
				displayNotAnIntegerMessage();
				// pass out GWT edit text cell cache
				mediumStylableEditTextCell.clearViewData(object.getComposantLibelle());
				fpCT.redraw();
			}
		}
	}

	private final class LabelComponantCColumn extends Column<ComponentDTO, String>
	{
		private LabelComponantCColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(ComponentDTO object)
		{
			return object.getComposantLibelle();
		}
	}

	private final class SimpleCFieldUpdater implements FieldUpdater<ComponentDTO, String>
	{
		@Override
		public void update(int index, ComponentDTO object, String value)
		{
			try
			{
				int intValue = Integer.valueOf(value);
				EstimationDTO e = selectionModel.getSelectedObject();
				switch (object.getComponent())
				{
					case NONE:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setGlobalSimple(intValue);
						break;
					case GDI:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setGDIsimple(intValue);
						break;
					case GDE:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setGDEsimple(intValue);
						break;
					case IN:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setINsimple(intValue);
						break;
					case OUT:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setOUTsimple(intValue);
						break;
					case INT:
						dataProvider.getList().get(dataProvider.getList().indexOf(e)).setInterrogationSimple(intValue);
						break;
				}
				// estimations
				fpDataProvider.getList().get(index).setComposantValeurSimple(intValue);
				fireEvent(new RefreshEstimationChargeEvent(dataProvider.getList().get(dataProvider.getList().indexOf(e))));
			}
			catch (NumberFormatException e)
			{
				displayNotAnIntegerMessage();
				// pass out GWT edit text cell cache
				simpleStylableEditTextCell.clearViewData(object.getComposantLibelle());
				fpCT.redraw();
			}
		}
	}

	private final class SimpleCColumn extends Column<ComponentDTO, String>
	{
		private SimpleCColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(ComponentDTO object)
		{
			return String.valueOf(object.getComposantValeurSimple());
		}
	}

	private final class IsManualColumnFieldUpdater implements FieldUpdater<EstimationDTO, Boolean>
	{
		@Override
		public void update(int index, EstimationDTO object, Boolean value)
		{
			EstimationDTO e = estimationList.get(estimationList.indexOf(object));
			e.setManual(value);
			displayFP();
			if (selectionModel.getSelectedObject() != null && selectionModel.getSelectedObject().equals(e))
			{
				fireEvent(new RefreshEstimationChargeEvent(e));
			}
			else
			{
				selectionModel.setSelected(object, true);
			}
			estimationCT.redraw();
		}
	}

	private final class ChargeColumnFieldUpdater implements FieldUpdater<EstimationDTO, String>
	{
		@Override
		public void update(int index, EstimationDTO object, String value)
		{
			EstimationDTO e = estimationList.get(estimationList.indexOf(object));
			e.setCharge(Common.makeInt(value));
			fireEvent(new RefreshEstimationChargeEvent(e));
		}
	}

	private final class FpCTKeyDownHandler implements KeyDownHandler
	{
		@Override
		public void onKeyDown(KeyDownEvent event)
		{
			if (KeyCodes.KEY_RIGHT == event.getNativeKeyCode() || KeyCodes.KEY_TAB == event.getNativeKeyCode())
			{
				// TODO refaire un celltable
				// NativeEvent e = Document.get().createKeyDownEvent(false,
				// false, false, false, KeyCodes.KEY_RIGHT);
				// DomEvent.fireNativeEventa(e, e., fpCT.getElement());
				// fpCT.fireEvent(e);
			}
		}
	}

	private class DetailsColumn extends Column<EstimationDTO, EstimationDTO>
	{
		private DetailsColumn(Cell<EstimationDTO> cell)
		{
			super(cell);
		}

		@Override
		public EstimationDTO getValue(EstimationDTO object)
		{
			return object;
		}
	}

	private class ChargeBisColumn extends Column<EstimationDTO, String>
	{
		private ChargeBisColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(EstimationDTO object)
		{
			String ret = Common.DEFAULT_FUNCTION_POINTS;
			if (object.getCharge() != null)
			{
				Integer charge = (int) (object.getCharge() * nbHourByDay);
				ret = charge.toString();
			}
			return ret;
		}
	}

	private class ChargeColumn extends Column<EstimationDTO, String>
	{
		private ChargeColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(EstimationDTO object)
		{
			String ret = Common.DEFAULT_FUNCTION_POINTS;

			if (object.getCharge() != null)
			{
				ret = object.getCharge().toString();
			}
			else if (object.getLastCharge() != null)
			{
				ret = object.getLastCharge().toString();
			}
			else
			{
				fireEvent(new RefreshEstimationChargeEvent(object));
			}
			return ret;
		}
	}

	private class LastChargeColumn extends Column<EstimationDTO, String>
	{
		private LastChargeColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(EstimationDTO object)
		{
			String ret = Common.EMPTY_TEXT;
			if (object.getLastCharge() != null)
			{
				ret = object.getLastCharge().toString();
			}
			return ret;
		}
	}

	private class ScopeUnitVersionColumn extends Column<EstimationDTO, String>
	{
		private ScopeUnitVersionColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(EstimationDTO object)
		{
			String ret = Common.EMPTY_TEXT;
			if (object.getScopeUnit().getVersion() != null)
			{
				ret = object.getScopeUnit().getVersion();
			}
			return ret;
		}
	}

	private class ScopeUnitColumn extends Column<EstimationDTO, String>
	{
		private ScopeUnitColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(EstimationDTO object)
		{
			return object.getScopeUnit().getName();
		}
	}

	private class ScopeUnitParentColumn extends Column<EstimationDTO, String>
	{
		private ScopeUnitParentColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(EstimationDTO object)
		{
			String ret = Common.EMPTY_TEXT;
			if (object.getScopeUnit().getParentScopeUnit() != null)
			{
				ret = object.getScopeUnit().getParentScopeUnit().getName();
			}
			return ret;
		}
	}

	private class ParentLotColumn extends Column<EstimationDTO, String>
	{
		private ParentLotColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(EstimationDTO object)
		{
			String ret = Common.EMPTY_TEXT;
			if (object.getScopeUnit().getParentLotName() != null)
			{
				ret = object.getScopeUnit().getParentLotName();
			}
			return ret;
		}
	}

	private class LotColumn extends Column<EstimationDTO, String>
	{
		private LotColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(EstimationDTO object)
		{
			return object.getScopeUnit().getLotName();
		}
	}

	private class PriorizationLabelColumn extends Column<PriorizationDTO, String>
	{
		private PriorizationLabelColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(PriorizationDTO object)
		{
			return object.getLabel().getLabel();
		}
	}

	private class PriorizationValueColumn extends Column<PriorizationDTO, String>
	{
		private PriorizationValueColumn(Cell<String> cell)
		{
			super(cell);
		}

		@Override
		public String getValue(PriorizationDTO object)
		{
			String ret = Common.DEFAULT_FUNCTION_POINTS;
			if (null != object)
			{
				ret = object.getValue().toString();
			}
			return ret;
		}
	}

	private class LotColumnComparator implements Comparator<EstimationDTO>
	{
		@Override
		public int compare(EstimationDTO o1, EstimationDTO o2)
		{
			return o1.getScopeUnit().getLotName().compareTo(o2.getScopeUnit().getLotName());
		}
	}

	private class ParentLotComparator implements Comparator<EstimationDTO>
	{
		@Override
		public int compare(EstimationDTO o1, EstimationDTO o2)
		{
			int ret = 0;
			// if both scopeUnit have sublot
			if (o1.getScopeUnit().getParentLotName() != null && o2.getScopeUnit().getParentLotName() != null)
			{
				ret = o1.getScopeUnit().getParentLotName().compareTo(o2.getScopeUnit().getParentLotName());
			}
			else if (o1.getScopeUnit().getParentLotName() == null)
			{
				ret = -1;
			}
			else if (o2.getScopeUnit().getParentLotName() == null)
			{
				ret = 1;
			}
			return ret;
		}
	}

	private class ScopeUnitParentComparator implements Comparator<EstimationDTO>
	{
		@Override
		public int compare(EstimationDTO o1, EstimationDTO o2)
		{
			int ret = 0;
			// if both scopeUnit have parent
			if (o1.getScopeUnit().getParentScopeUnit() != null && o2.getScopeUnit().getParentScopeUnit() != null)
			{
				ret = o1.getScopeUnit().getParentScopeUnit().getName().compareTo(o2.getScopeUnit().getParentScopeUnit()
																																					 .getName());
			}
			else if (o1.getScopeUnit().getParentScopeUnit() == null)
			{
				ret = -1;
			}
			else if (o2.getScopeUnit().getParentScopeUnit() == null)
			{
				ret = 1;
			}
			return ret;
		}
	}

	private class VersionComparator implements Comparator<EstimationDTO>
	{
		@Override
		public int compare(EstimationDTO o1, EstimationDTO o2)
		{
			int ret = 0;
			// if both scopeUnit have version
			if (o1.getScopeUnit().getVersion() != null && o2.getScopeUnit().getVersion() != null)
			{
				ret = o1.getScopeUnit().getVersion().compareTo(o2.getScopeUnit().getVersion());
			}
			else if (o1.getScopeUnit().getVersion() == null)
			{
				ret = -10;
			}
			else if (o2.getScopeUnit().getVersion() == null)
			{
				ret = 10;
			}
			return ret;
		}
	}

	private class IsManualComparator implements Comparator<EstimationDTO>
	{
		@Override
		public int compare(EstimationDTO o1, EstimationDTO o2)
		{
			return o1.isManual().toString().compareTo(o2.isManual().toString());
		}
	}

}
