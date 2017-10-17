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

import java.util.Collections;
import java.util.Comparator;

import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.IterationDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ScopeUnitDisciplineDTO;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
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
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * @author Bilet-jc
 * 
 */
public class ScopeUnitDisciplineViewImpl extends Composite implements
		ScopeUnitDisciplineView {

	private static final Comparator<ScopeUnitDisciplineDTO> NAME_COMPARATOR = new Comparator<ScopeUnitDisciplineDTO>()
	{
		@Override
		public int compare(final ScopeUnitDisciplineDTO o1, final ScopeUnitDisciplineDTO o2)
		{
			return o1.getScopeUnit().getName().compareTo(o2.getScopeUnit().getName());
		}
	};
	private static ScopeUnitDisciplineViewImplUiBinder uiBinder = GWT
			.create(ScopeUnitDisciplineViewImplUiBinder.class);
	/* ScopeUnit Discipline */
	@UiField
	Panel scrollPanel;
	@UiField
	Panel scopeUnitDisciplinePanel;
	@UiField
	Panel actionButtons;
	@UiField
	Label scopeUnitDisciplineTitle;
	@UiField
	Label currentIteration;
	@UiField
	Label currentIterationL;
	@UiField(provided = true)
	CellTable<ScopeUnitDisciplineDTO> scopeUnitDisciplineTable;
	@UiField(provided = true)
	SimplePager scopeUnitDisciplinePager;
	@UiField
	Button startScopeUnitDisciplineB;
	@UiField
	Button cancelScopeUnitDisciplineB;
	@UiField
	Button stopScopeUnitDisciplineB;
	@UiField
	Button buttonHomeReturn;

	private ListDataProvider<ScopeUnitDisciplineDTO> dataProvider;
	private SingleSelectionModel<ScopeUnitDisciplineDTO> selectionModel;
	private Column<ScopeUnitDisciplineDTO, String> scopeUnitParentColumn;
	private Column<ScopeUnitDisciplineDTO, String> scopeUnitColumn;
	private Column<ScopeUnitDisciplineDTO, String> disciplineColumn;
	private Column<ScopeUnitDisciplineDTO, String> statusColumn;

	public ScopeUnitDisciplineViewImpl() {
		/* inject CSS */
		Common.RESOURCE.css().ensureInjected();
		/* initialize table before binding ui */
		initScopeUnitDisciplineTable();
		/* bind ui */
		initWidget(uiBinder.createAndBindUi(this));
		/* initialize view components */
		stopScopeUnitDisciplineB.setEnabled(false);
		cancelScopeUnitDisciplineB.setEnabled(false);
		currentIterationL.setText(Common.MESSAGES_BACKLOG.currentIterationL());
		scopeUnitDisciplineTitle.setText(Common.MESSAGES_BACKLOG
				.scopeUnitDisciplineAllowed());
		startScopeUnitDisciplineB.setText(Common.MESSAGES_BACKLOG
				.buttonStartScopeUnitDiscipline());
		cancelScopeUnitDisciplineB.setText(Common.MESSAGES_BACKLOG
				.buttonCancelScopeUnitDiscipline());
		stopScopeUnitDisciplineB.setText(Common.MESSAGES_BACKLOG
				.buttonStopScopeUnitDiscipline());
		buttonHomeReturn.setText(Common.GLOBAL.homeReturn());
	}

	private void initScopeUnitDisciplineTable() {
		scopeUnitDisciplineTable = new CellTable<ScopeUnitDisciplineDTO>(
				Common.PAGE_SIZE, (Resources) GWT.create(TableResources.class),
				CellKey.SCOPE_UNIT_DISCIPLINE_KEY_PROVIDER);
		scopeUnitDisciplineTable.setWidth("100%", false);

		// Init empty widget
		final Label emptyLabel = new Label(
				Common.MESSAGES_BACKLOG.emptyScopeUnitDisciplineMessage());
		emptyLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
		scopeUnitDisciplineTable.setEmptyTableWidget(emptyLabel);

		// Create a Pager to control the CellTable
		final SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		scopeUnitDisciplinePager = new SimplePager(TextLocation.CENTER,
				pagerResources, false, 0, true);
		scopeUnitDisciplinePager.setDisplay(scopeUnitDisciplineTable);

		// Initialize the columns.
		initScopeUnitDisciplineTableColumns();

		// Add the CellTable to the adapter
		dataProvider = new ListDataProvider<ScopeUnitDisciplineDTO>();
		dataProvider.addDataDisplay(scopeUnitDisciplineTable);

		// Add the CellTable to the adapter
		final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(
				scopeUnitDisciplineTable);
		scopeUnitDisciplineTable.addColumnSortHandler(columnSortHandler);

		// Add selection model
		selectionModel = new SingleSelectionModel<ScopeUnitDisciplineDTO>();
		scopeUnitDisciplineTable.setSelectionModel(selectionModel);

		//Manage selectionModel with key up & down
		scopeUnitDisciplineTable.sinkEvents(Event.ONKEYDOWN);
		scopeUnitDisciplineTable.addDomHandler(new KeyDownHandler() {

			@Override
			public void onKeyDown(final KeyDownEvent event) {
				if (selectionModel.getSelectedObject() != null) {
					int pos;
					ScopeUnitDisciplineDTO object;
					final int key = event.getNativeKeyCode();
					if (KeyCodes.KEY_UP == key){
                  pos = scopeUnitDisciplineTable.getVisibleItems().indexOf(selectionModel.getSelectedObject());
                  if (pos > 0) {
                     object = scopeUnitDisciplineTable.getVisibleItems().get(pos - 1);
                     selectionModel.setSelected(object, true);
                     event.stopPropagation();
                     event.preventDefault();
									}
					}else if (KeyCodes.KEY_DOWN == key){
					   pos = scopeUnitDisciplineTable.getVisibleItems().indexOf(selectionModel.getSelectedObject());
                  if (pos != scopeUnitDisciplineTable.getVisibleItems().size() - 1) {
                     object = scopeUnitDisciplineTable.getVisibleItems().get(pos + 1);
                     selectionModel.setSelected(object, true);
                     event.stopPropagation();
                     event.preventDefault();
                  }
					}
				}
			}
		}, KeyDownEvent.getType());

	}

	private void initScopeUnitDisciplineTableColumns() {

	   // ScopeUnit Column
      scopeUnitColumn = new Column<ScopeUnitDisciplineDTO, String>(
            new TextCell()) {
         @Override
         public String getValue(ScopeUnitDisciplineDTO object) {
            return object.getScopeUnit().getName();
         }
      };
      scopeUnitColumn.setSortable(true);
      scopeUnitDisciplineTable.addColumn(scopeUnitColumn,
            Common.GLOBAL.SU());

	   // ScopeUnit parent Column
		scopeUnitParentColumn = new Column<ScopeUnitDisciplineDTO, String>(
				new TextCell()) {
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
		scopeUnitDisciplineTable.addColumn(scopeUnitParentColumn,
				Common.GLOBAL.SUParent());

		// Discipline Column
		disciplineColumn = new Column<ScopeUnitDisciplineDTO, String>(
				new TextCell()) {
			@Override
			public String getValue(ScopeUnitDisciplineDTO object) {
				return object.getDiscipline().getLibelle();
			}
		};
		disciplineColumn.setSortable(true);
		scopeUnitDisciplineTable.addColumn(disciplineColumn,
				Common.MESSAGES_BACKLOG.discipline());
		// Status Column
		statusColumn = new Column<ScopeUnitDisciplineDTO, String>(
				new TextCell()) {
			@Override
			public String getValue(ScopeUnitDisciplineDTO object) {
				return object.getStatus().getLabel();
			}
		};
		statusColumn.setSortable(true);
		scopeUnitDisciplineTable.addColumn(statusColumn,
				Common.MESSAGES_BACKLOG.status());
	}

	@Override
	public ListDataProvider<ScopeUnitDisciplineDTO> getDataProvider() {
		return dataProvider;
	}

	@Override
	public void updateSortHandler() {
		// Add the Sort Handler to the CellTable
	   Collections.sort(dataProvider.getList(), NAME_COMPARATOR);
		ListHandler<ScopeUnitDisciplineDTO> sortHandler = new ListHandler<ScopeUnitDisciplineDTO>(
				dataProvider.getList());
		scopeUnitDisciplineTable.addColumnSortHandler(sortHandler);

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
      sortHandler.setComparator(scopeUnitColumn, NAME_COMPARATOR);

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
      if ( scopeUnitDisciplineTable.getColumnSortList().size() > 0)
      {
        ColumnSortEvent.fire( scopeUnitDisciplineTable, scopeUnitDisciplineTable.getColumnSortList());
      }
	}

	@Override
	public SingleSelectionModel<ScopeUnitDisciplineDTO> getSelectionModel() {
		return selectionModel;
	}

	@Override
	public Button getStartScopeUnitDisciplineButton() {
		return startScopeUnitDisciplineB;
	}

	@Override
	public Button getCancelScopeUnitDisciplineButton() {
		return cancelScopeUnitDisciplineB;
	}

	@Override
	public HasWidgets getContentPanel()
	{
		return scrollPanel;
	}

	@Override
	public Button getStopScopeUnitDisciplineButton() {
		return stopScopeUnitDisciplineB;
	}

	@Override
	public Button getButtonHomeReturn() {
		return buttonHomeReturn;
	}

	@Override
	public void setCurrentIteration(IterationDTO iteration) {
		if (iteration != null) {
			currentIteration.setText(iteration.getLabel());
		} else {
			currentIteration.setText(Common.MESSAGES_BACKLOG
					.noCurrentIteration());
		}
	}

	interface ScopeUnitDisciplineViewImplUiBinder extends UiBinder<Widget, ScopeUnitDisciplineViewImpl>
	{
	}

}
