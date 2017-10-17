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
import com.google.gwt.view.client.SingleSelectionModel;
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltree.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;

import java.util.Comparator;

public class ScopeUnitDisciplinePopupViewImpl extends PopupPanel implements ScopeUnitDisciplinePopupView {

	private static ScopeUnitDisciplinePopupViewImplUiBinder uiBinder = GWT
			.create(ScopeUnitDisciplinePopupViewImplUiBinder.class);
	@UiField
	Label selectScopeUnitDisciplineTitle;
	/* scope unit discipline list */
	@UiField
	Label scopeUnitDisciplineTitle;
	@UiField(provided = true)
	CellTable<ScopeUnitDisciplineDTO> scopeUnitDisciplineCT;
	@UiField(provided = true)
	SimplePager scopeUnitDisciplinePager;
	@UiField
	Button validateB;
	@UiField
	Button backB;
	private ListDataProvider<ScopeUnitDisciplineDTO> dataProvider;
	private SingleSelectionModel<ScopeUnitDisciplineDTO> selectionModel;
	private Column<ScopeUnitDisciplineDTO, String> scopeUnitParentColumn;
	private Column<ScopeUnitDisciplineDTO, String> scopeUnitColumn;
	private Column<ScopeUnitDisciplineDTO, String> disciplineColumn;
	private Column<ScopeUnitDisciplineDTO, String> statusColumn;
	public ScopeUnitDisciplinePopupViewImpl() {
		Common.RESOURCE.css().ensureInjected();
		initScopeUnitDisciplineCT();
		add(uiBinder.createAndBindUi(this));
		selectScopeUnitDisciplineTitle.setText(Common.MESSAGES_BACKLOG.selectScopeUnitDisciplineTitle());
		scopeUnitDisciplineTitle.setText(Common.MESSAGES_BACKLOG.scopeUnitDisciplineTitle());
		validateB.setText(Common.GLOBAL.buttonValidate());
		validateB.setEnabled(false);
		backB.setText(Common.GLOBAL.buttonCancel());
		backB.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		setGlassEnabled(true);
	}

	/**
	 * Initialize the scope unit discipline cell table
	 */
	private void initScopeUnitDisciplineCT() {
		// init cell table
		scopeUnitDisciplineCT = new CellTable<ScopeUnitDisciplineDTO>(Common.PAGE_SIZE,
				(Resources) GWT.create(TableResources.class), CellKey.SCOPE_UNIT_DISCIPLINE_KEY_PROVIDER);
		scopeUnitDisciplineCT.setWidth("100%", false);

		// Init empty widget
		Label emptyLabel = new Label(Common.MESSAGES_BACKLOG.emptyScopeUnitLightMessage());
		emptyLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
		scopeUnitDisciplineCT.setEmptyTableWidget(emptyLabel);

		// Create a Pager to control the CellTable
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		scopeUnitDisciplinePager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		scopeUnitDisciplinePager.setDisplay(scopeUnitDisciplineCT);

		// Initialize the columns.
		initScopeUnitDisciplineTableColumns();

		// Add the CellTable to the adapter
		dataProvider = new ListDataProvider<ScopeUnitDisciplineDTO>();
		dataProvider.addDataDisplay(scopeUnitDisciplineCT);

		selectionModel = new SingleSelectionModel<ScopeUnitDisciplineDTO>();
		scopeUnitDisciplineCT.setSelectionModel(selectionModel);

		// Add the CellTable to the adapter
		ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(scopeUnitDisciplineCT);
		scopeUnitDisciplineCT.addColumnSortHandler(columnSortHandler);
	}

	/**
	 * Initialize column of scopeUnitDiscipline table
	 */
	private void initScopeUnitDisciplineTableColumns() {
		// ScopeUnit Column
		scopeUnitColumn = new Column<ScopeUnitDisciplineDTO, String>(new TextCell()) {
			@Override
			public String getValue(ScopeUnitDisciplineDTO object) {
				return object.getScopeUnit().getName();
			}
		};
		scopeUnitColumn.setSortable(true);
		scopeUnitDisciplineCT.addColumn(scopeUnitColumn, Common.MESSAGES_BACKLOG.scopeUnit());

		// ScopeUnit parent Column
      scopeUnitParentColumn = new Column<ScopeUnitDisciplineDTO, String>(new TextCell()) {
         @Override
         public String getValue(ScopeUnitDisciplineDTO object) {
            String ret = Common.EMPTY_TEXT;
            if (object.getScopeUnit().getParentScopeUnit() != null) {
               ret = object.getScopeUnit().getParentScopeUnit().getName();
            }
            return ret;
         }
      };
      scopeUnitParentColumn.setSortable(true);
      scopeUnitDisciplineCT.addColumn(scopeUnitParentColumn, Common.MESSAGES_BACKLOG.scopeUnitParent());

		// Discipline Column
		disciplineColumn = new Column<ScopeUnitDisciplineDTO, String>(new TextCell()) {
			@Override
			public String getValue(ScopeUnitDisciplineDTO object) {
				return object.getDiscipline().getLibelle();
			}
		};
		disciplineColumn.setSortable(true);
		scopeUnitDisciplineCT.addColumn(disciplineColumn, Common.MESSAGES_BACKLOG.discipline());

		// Status Column
		statusColumn = new Column<ScopeUnitDisciplineDTO, String>(new TextCell()) {
			@Override
			public String getValue(ScopeUnitDisciplineDTO object) {
				return object.getStatus().getLabel();
			}
		};
		statusColumn.setSortable(true);
		scopeUnitDisciplineCT.addColumn(statusColumn, Common.MESSAGES_BACKLOG.status());
	}

	@Override
	public ListDataProvider<ScopeUnitDisciplineDTO> getDataProvider() {
		return dataProvider;
	}

	@Override
	public void updateSortHandler() {
		// Add the Sort Handler to the CellTable
		ListHandler<ScopeUnitDisciplineDTO> sortHandler = new ListHandler<ScopeUnitDisciplineDTO>(
				dataProvider.getList());
		scopeUnitDisciplineCT.addColumnSortHandler(sortHandler);

		// scope unit parent sort
		sortHandler.setComparator(scopeUnitParentColumn, new Comparator<ScopeUnitDisciplineDTO>() {

			@Override
			public int compare(ScopeUnitDisciplineDTO o1, ScopeUnitDisciplineDTO o2) {
				int ret = 0;
				if (o1.getScopeUnit().getParentScopeUnit() == null) {
					if (o2.getScopeUnit().getParentScopeUnit() != null) {
						ret = 1;
					}
				} else {
					if (o2.getScopeUnit().getParentScopeUnit() == null) {
						ret = -1;
					} else {
						ret = o1.getScopeUnit().getParentScopeUnit().getName()
								.compareTo(o2.getScopeUnit().getParentScopeUnit().getName());
					}
				}
				return ret;
			}
		});

		// scope unit sort
		sortHandler.setComparator(scopeUnitColumn, new Comparator<ScopeUnitDisciplineDTO>() {

			@Override
			public int compare(ScopeUnitDisciplineDTO o1, ScopeUnitDisciplineDTO o2) {
				return o1.getScopeUnit().getName().compareTo(o2.getScopeUnit().getName());
			}
		});

		// discipline sort
		sortHandler.setComparator(disciplineColumn, new Comparator<ScopeUnitDisciplineDTO>() {

			@Override
			public int compare(ScopeUnitDisciplineDTO o1, ScopeUnitDisciplineDTO o2) {
				return o1.getDiscipline().getLibelle().compareTo(o2.getDiscipline().getLibelle());
			}
		});

		// status sort
		sortHandler.setComparator(statusColumn, new Comparator<ScopeUnitDisciplineDTO>() {

			@Override
			public int compare(ScopeUnitDisciplineDTO o1, ScopeUnitDisciplineDTO o2) {
				return o1.getStatus().getLabel().compareTo(o2.getStatus().getLabel());
			}
		});
	}

	@Override
	public SingleSelectionModel<ScopeUnitDisciplineDTO> getSelectionModel()
	{
		return selectionModel;
	}

	@Override
	public Button getBackButton()
	{
		return backB;
	}

	@Override
	public void showWidget() {
		center();
		show();
	}

	@Override
	public void hideWidget() {
		hide();
	}

	@Override
	public Button getValidateButton() {
		return validateB;
	}

	interface ScopeUnitDisciplinePopupViewImplUiBinder extends UiBinder<Widget, ScopeUnitDisciplinePopupViewImpl>
	{
	}

}
