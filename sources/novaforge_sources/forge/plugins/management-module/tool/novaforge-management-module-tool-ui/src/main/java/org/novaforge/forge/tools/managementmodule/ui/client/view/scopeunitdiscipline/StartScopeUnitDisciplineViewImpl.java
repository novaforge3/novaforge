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
package org.novaforge.forge.tools.managementmodule.ui.client.view.scopeunitdiscipline;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableTextCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltree.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoDialogBox;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.dialogbox.InfoTypeEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitLightDTO;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Bilet-jc
 * 
 */
public class StartScopeUnitDisciplineViewImpl extends PopupPanel implements StartScopeUnitDisciplineView {

	static final Comparator<ScopeUnitLightDTO> SCOPE_UNIT_COMPARATOR = new Comparator<ScopeUnitLightDTO>()
	{
		@Override
		public int compare(ScopeUnitLightDTO o1, ScopeUnitLightDTO o2)
		{
			return o1.getScopeUnitName().compareTo(o2.getScopeUnitName());
		}
	};
	static final Comparator<DisciplineDTO>     DISCIPLINE_COMPARATOR = new Comparator<DisciplineDTO>()
	{
		@Override
		public int compare(DisciplineDTO o1, DisciplineDTO o2)
		{
			return o1.getLibelle().compareTo(o2.getLibelle());
		}
	};
	private static StartScopeUnitOnDisciplineViewImplUiBinder uiBinder = GWT
			.create(StartScopeUnitOnDisciplineViewImplUiBinder.class);
	@UiField
	Label manageScopeUnitTitle;
	/* scope unit list */
	@UiField(provided = true)
	CellTable<ScopeUnitLightDTO> scopeUnitCT;
	@UiField(provided = true)
	SimplePager scopeUnitPager;
	/* discipline list */
	@UiField
	Label disciplineTitle;
	@UiField(provided = true)
	CellTable<DisciplineDTO> disciplineCT;
	@UiField
	Button validateB;
	@UiField
	Button backB;
	private ListDataProvider<ScopeUnitLightDTO> scopeUnitDataProvider;
	private ListDataProvider<DisciplineDTO> disciplineDataProvider;
	private MultiSelectionModel<ScopeUnitLightDTO> scopeUnitSelectionModel;
	private MultiSelectionModel<DisciplineDTO> disciplineSelectionModel;
	private Column<ScopeUnitLightDTO, String> scopeUnitParentColumn;
	private Column<ScopeUnitLightDTO, String> scopeUnitColumn;
	private Column<ScopeUnitLightDTO, String> weightColumn;
	private Column<ScopeUnitLightDTO, String> benefitColumn;
	private Column<ScopeUnitLightDTO, String> riskColumn;
	private Column<ScopeUnitLightDTO, String> injuryColumn;
	private Column<DisciplineDTO, String> disciplineColumn;
	private InfoDialogBox successCreationBox;
	public StartScopeUnitDisciplineViewImpl() {
		Common.RESOURCE.css().ensureInjected();
		initScopeUnitCT();
		initDisciplineCT();
		add(uiBinder.createAndBindUi(this));
		manageScopeUnitTitle.setText(Common.MESSAGES_BACKLOG.manageScopeUnitTitle());
		disciplineTitle.setText(Common.MESSAGES_BACKLOG.notFinishedDiscipline());
		validateB.setText(Common.GLOBAL.buttonValidate());
		backB.setText(Common.GLOBAL.buttonCancel());
		successCreationBox = new InfoDialogBox(Common.MESSAGES_BACKLOG.createScopeUnitDisciplineMessage(),
				InfoTypeEnum.OK);
		backB.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		setGlassEnabled(true);
	}

	/**
	 * Initialize the scope unit cell table
	 */
	private void initScopeUnitCT() {
		// init cell table
		scopeUnitCT = new CellTable<ScopeUnitLightDTO>(Common.PAGE_SIZE,
				(Resources) GWT.create(TableResources.class), CellKey.SCOPE_UNIT_LIGHT_KEY_PROVIDER);
		scopeUnitCT.setWidth("100%", false);

		// Init empty widget
		Label emptyLabel = new Label(Common.MESSAGES_BACKLOG.emptyScopeUnitLightMessage());
		emptyLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
		scopeUnitCT.setEmptyTableWidget(emptyLabel);

		// Create a Pager to control the CellTable
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		scopeUnitPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		scopeUnitPager.setDisplay(scopeUnitCT);

		// Initialize the columns.
		initScopeUnitTableColumns();

		// Add the CellTable to the adapter
		scopeUnitDataProvider = new ListDataProvider<ScopeUnitLightDTO>();
		scopeUnitDataProvider.addDataDisplay(scopeUnitCT);

		scopeUnitSelectionModel = new MultiSelectionModel<ScopeUnitLightDTO>();
		scopeUnitCT.setSelectionModel(scopeUnitSelectionModel);

		// Add the CellTable to the adapter
		ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(scopeUnitCT);
		scopeUnitCT.addColumnSortHandler(columnSortHandler);

	}

	private void initDisciplineCT() {
		// init cell table
		disciplineCT = new CellTable<DisciplineDTO>(Common.PAGE_SIZE, (Resources) GWT.create(TableResources.class), CellKey.DISCIPLINE_KEY_PROVIDER);
		disciplineCT.setWidth("100%", false);

		// Init empty widget
		Label emptyLabel = new Label(Common.GLOBAL.noData());
		emptyLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
		disciplineCT.setEmptyTableWidget(emptyLabel);

		// Initialize the discipline column.
		disciplineColumn = new Column<DisciplineDTO, String>(new TextCell()) {
			@Override
			public String getValue(DisciplineDTO object) {
				return object.getLibelle();
			}
		};
		disciplineColumn.setSortable(true);
		disciplineCT.addColumn(disciplineColumn, Common.GLOBAL.discipline());

		// Add the CellTable to the adapter
		disciplineDataProvider = new ListDataProvider<DisciplineDTO>();
		disciplineDataProvider.addDataDisplay(disciplineCT);

		disciplineSelectionModel = new MultiSelectionModel<DisciplineDTO>();
		disciplineCT.setSelectionModel(disciplineSelectionModel);

		// Add the CellTable to the adapter
		ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(disciplineCT);
		disciplineCT.addColumnSortHandler(columnSortHandler);

	}

	private void initScopeUnitTableColumns() {
		final Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("align", "center");
		// scope unit column
		scopeUnitColumn = new Column<ScopeUnitLightDTO, String>(new TextCell()) {
			@Override
			public String getValue(ScopeUnitLightDTO object) {
				return object.getScopeUnitName();
			}
		};
		scopeUnitColumn.setSortable(true);
		scopeUnitCT.addColumn(scopeUnitColumn, Common.GLOBAL.SU());
		// scope unit parent column
		scopeUnitParentColumn = new Column<ScopeUnitLightDTO, String>(new TextCell()) {
			@Override
			public String getValue(ScopeUnitLightDTO object) {
				String ret = null;
				if (object.getParentScopeUnitName() != null) {
					ret = object.getParentScopeUnitName();
				}
				return ret;
			}
		};
		scopeUnitParentColumn.setSortable(true);
		scopeUnitCT.addColumn(scopeUnitParentColumn, Common.GLOBAL.SUParent());
		// weight column
		weightColumn = new Column<ScopeUnitLightDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitLightDTO object) {
				return object.getWeight().toString();
			}
		};
		weightColumn.setSortable(true);
		scopeUnitCT.addColumn(weightColumn, Common.MESSAGES_ESTIMATION.weight());
		// benefit column
		benefitColumn = new Column<ScopeUnitLightDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitLightDTO object) {
				return object.getBenefit().toString();
			}
		};
		benefitColumn.setSortable(true);
		scopeUnitCT.addColumn(benefitColumn, Common.MESSAGES_ESTIMATION.benefit());
		// risk column
		riskColumn = new Column<ScopeUnitLightDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitLightDTO object) {
				return object.getRisk().toString();
			}
		};
		riskColumn.setSortable(true);
		scopeUnitCT.addColumn(riskColumn, Common.MESSAGES_ESTIMATION.risk());
		// injury column
		injuryColumn = new Column<ScopeUnitLightDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(ScopeUnitLightDTO object) {
				return object.getInjury().toString();
			}
		};
		injuryColumn.setSortable(true);
		scopeUnitCT.addColumn(injuryColumn, Common.MESSAGES_ESTIMATION.injury());

	}

	@Override
	public void showWidget() {
		center();
		show();
	}

	@Override
	public Button getButtonValidate() {
		return validateB;
	}

	@Override
	public ListDataProvider<ScopeUnitLightDTO> getDataProviderScopeUnit() {
		return scopeUnitDataProvider;
	}

	@Override
	public ListDataProvider<DisciplineDTO> getDataProviderDiscipline() {
		return disciplineDataProvider;
	}

	@Override
	public void updateSortHandler() {
		// Add the Sort Handler to the CellTable
		ListHandler<ScopeUnitLightDTO> sortHandler = new ListHandler<ScopeUnitLightDTO>(
				scopeUnitDataProvider.getList());
		scopeUnitCT.addColumnSortHandler(sortHandler);

		// scope unit parent sort
		sortHandler.setComparator(scopeUnitParentColumn, new Comparator<ScopeUnitLightDTO>() {

			@Override
			public int compare(ScopeUnitLightDTO o1, ScopeUnitLightDTO o2) {
				return o1.getParentScopeUnitName().compareTo(o2.getParentScopeUnitName());
			}
		});

		// scope unit sort
		sortHandler.setComparator(scopeUnitColumn, SCOPE_UNIT_COMPARATOR);

		// weight sort
		sortHandler.setComparator(weightColumn, new Comparator<ScopeUnitLightDTO>() {

			@Override
			public int compare(ScopeUnitLightDTO o1, ScopeUnitLightDTO o2) {
				return o1.getWeight().toString().compareTo(o2.getWeight().toString());
			}
		});

		// benefit sort
		sortHandler.setComparator(benefitColumn, new Comparator<ScopeUnitLightDTO>() {

			@Override
			public int compare(ScopeUnitLightDTO o1, ScopeUnitLightDTO o2) {
				return o1.getBenefit().toString().compareTo(o2.getBenefit().toString());
			}
		});

		// risk sort
		sortHandler.setComparator(riskColumn, new Comparator<ScopeUnitLightDTO>() {

			@Override
			public int compare(ScopeUnitLightDTO o1, ScopeUnitLightDTO o2) {
				return o1.getRisk().toString().compareTo(o2.getRisk().toString());
			}
		});

		// injury sort
		sortHandler.setComparator(injuryColumn, new Comparator<ScopeUnitLightDTO>() {

			@Override
			public int compare(ScopeUnitLightDTO o1, ScopeUnitLightDTO o2) {
				return o1.getInjury().toString().compareTo(o2.getInjury().toString());
			}
		});
	}

	@Override
	public InfoDialogBox getSuccessCreationBox() {
		return successCreationBox;
	}

	@Override
	public void hideWidget()
	{
		hide();
	}

	@Override
	public Comparator<ScopeUnitLightDTO> getComparatorScopeUnit() {
		return SCOPE_UNIT_COMPARATOR;
	}

	@Override
	public MultiSelectionModel<ScopeUnitLightDTO> getSelectionModelScopeUnit()
	{
		return scopeUnitSelectionModel;
	}

	@Override
	public MultiSelectionModel<DisciplineDTO> getSelectionModelDiscipline()
	{
		return disciplineSelectionModel;
	}

	@Override
	public Comparator<DisciplineDTO> getComparatorDiscipline() {
		return DISCIPLINE_COMPARATOR;
	}

	interface StartScopeUnitOnDisciplineViewImplUiBinder extends UiBinder<Widget, StartScopeUnitDisciplineViewImpl>
	{
	}

}
