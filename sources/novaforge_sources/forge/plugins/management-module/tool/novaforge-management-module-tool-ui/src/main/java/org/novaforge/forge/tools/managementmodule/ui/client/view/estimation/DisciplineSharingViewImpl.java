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
package org.novaforge.forge.tools.managementmodule.ui.client.view.estimation;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
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
import org.novaforge.forge.tools.managementmodule.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltable.StylableTextCell;
import org.novaforge.forge.tools.managementmodule.ui.client.view.commons.celltree.TableResources;
import org.novaforge.forge.tools.managementmodule.ui.shared.Common;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplinePhareEnum;
import org.novaforge.forge.tools.managementmodule.ui.shared.DisciplineSharingDTO;
import org.novaforge.forge.tools.managementmodule.ui.shared.ProjectDisciplineDTO;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author BILET-JC
 * 
 */
public class DisciplineSharingViewImpl extends PopupPanel implements DisciplineSharingView {

	private static DisciplineSharingViewImplUiBinder uiBinder = GWT
			.create(DisciplineSharingViewImplUiBinder.class);
	@UiField
	Label disciplineSharingTitle;
	@UiField(provided = true)
	CellTable<DisciplineSharingDTO> disciplineSharingCT;
	@UiField(provided = true)
	SimplePager disciplineSharingPager;
	@UiField
	Button backB;
	private ListDataProvider<DisciplineSharingDTO> dataProvider;
	private Column<DisciplineSharingDTO, String> scopeUnitColumn;
	private Column<DisciplineSharingDTO, String> chargeColumn;
	private Column<DisciplineSharingDTO, String> architectureDesignColumn;
	private Column<DisciplineSharingDTO, String> businessModelingColumn;
	private Column<DisciplineSharingDTO, String> changeDrivingColumn;
	private Column<DisciplineSharingDTO, String> configurationManagementColumn;
	private Column<DisciplineSharingDTO, String> implementationColumn;
	private Column<DisciplineSharingDTO, String> projectManagementColumn;
	private Column<DisciplineSharingDTO, String> qualityAssuranceColumn;
	private Column<DisciplineSharingDTO, String> receiptsColumn;
	private Column<DisciplineSharingDTO, String> requirementsAnalysisColumn;
	public DisciplineSharingViewImpl() {
		Common.RESOURCE.css().ensureInjected();
		initDisciplineSharingCT();
		add(uiBinder.createAndBindUi(this));
		disciplineSharingTitle.setText(Common.MESSAGES_ESTIMATION.disciplineSharingTitle());
		backB.setText(Common.GLOBAL.buttonClose());
		backB.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		setGlassEnabled(true);
	}

	/**
	 * Initialize the discipline sharing cell table
	 */
	private void initDisciplineSharingCT() {
		// init cell table
		disciplineSharingCT = new CellTable<DisciplineSharingDTO>(Common.PAGE_SIZE,
				(Resources) GWT.create(TableResources.class), CellKey.DISCIPLINE_SHARING_KEY_PROVIDER);
		disciplineSharingCT.setWidth("100%", false);

		// Init empty widget
		Label emptyLabel = new Label(Common.MESSAGES_ESTIMATION.emptyEstimationMessage());
		emptyLabel.setStyleName(Common.RESOURCE.css().emptyLabel());
		disciplineSharingCT.setEmptyTableWidget(emptyLabel);

		// Create a Pager to control the CellTable
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		disciplineSharingPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		disciplineSharingPager.setDisplay(disciplineSharingCT);

		// Initialize the columns.
		initDisciplineSharingCTColumns();

		// Add the CellTable to the adapter
		dataProvider = new ListDataProvider<DisciplineSharingDTO>();
		dataProvider.addDataDisplay(disciplineSharingCT);

		// Add the CellTable to the adapter
		ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(disciplineSharingCT);
		disciplineSharingCT.addColumnSortHandler(columnSortHandler);

	}

	/**
	 * Initialize column of discipline sharing table
	 */
	private void initDisciplineSharingCTColumns() {
		final Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("align", "center");
		// ScopeUnit Column
		scopeUnitColumn = new Column<DisciplineSharingDTO, String>(new TextCell()) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getScopeUnitName();
			}
		};
		scopeUnitColumn.setSortable(true);
		disciplineSharingCT.addColumn(scopeUnitColumn,
				Common.getLinesHeader(Common.MESSAGES_ESTIMATION.scopeUnit()));
		disciplineSharingCT.setColumnWidth(scopeUnitColumn, 1, Unit.PX);
		// estimation Column
		chargeColumn = new Column<DisciplineSharingDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getCharge().toString();
			}
		};
		chargeColumn.setSortable(true);
		disciplineSharingCT.addColumn(chargeColumn,
				Common.getLinesHeader(Common.MESSAGES_ESTIMATION.charge()));
		disciplineSharingCT.setColumnWidth(chargeColumn, 1, Unit.PX);
		// architecture design Column
		architectureDesignColumn = new Column<DisciplineSharingDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getArchitectureDesign().toString();
			}
		};
		architectureDesignColumn.setSortable(true);
		// businessModeling Column
		businessModelingColumn = new Column<DisciplineSharingDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getBusinessModeling().toString();
			}
		};
		businessModelingColumn.setSortable(true);
		// changeDriving Column
		changeDrivingColumn = new Column<DisciplineSharingDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getChangeDriving().toString();
			}
		};
		changeDrivingColumn.setSortable(true);
		// configurationManagement Column
		configurationManagementColumn = new Column<DisciplineSharingDTO, String>(new StylableTextCell(
				attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getConfigurationManagement().toString();
			}
		};
		configurationManagementColumn.setSortable(true);
		// implementation Column
		implementationColumn = new Column<DisciplineSharingDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getImplementation().toString();
			}
		};
		implementationColumn.setSortable(true);
		// projectManagement Column
		projectManagementColumn = new Column<DisciplineSharingDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getProjectManagement().toString();
			}
		};
		projectManagementColumn.setSortable(true);
		// qualityAssurance Column
		qualityAssuranceColumn = new Column<DisciplineSharingDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getQualityAssurance().toString();
			}
		};
		qualityAssuranceColumn.setSortable(true);
		// receipts Column
		receiptsColumn = new Column<DisciplineSharingDTO, String>(new StylableTextCell(attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getReceipts().toString();
			}
		};
		receiptsColumn.setSortable(true);
		// requirementsAnalysis Column
		requirementsAnalysisColumn = new Column<DisciplineSharingDTO, String>(
				new StylableTextCell(attributes)) {
			@Override
			public String getValue(DisciplineSharingDTO object) {
				return object.getRequirementsAnalysis().toString();
			}
		};
		requirementsAnalysisColumn.setSortable(true);

	}

	@Override
	public ListDataProvider<DisciplineSharingDTO> getDataProvider() {
		return dataProvider;
	}

	@Override
	public void updateSortHandler() {

		// Add the Sort Handler to the CellTable
		ListHandler<DisciplineSharingDTO> sortHandler = new ListHandler<DisciplineSharingDTO>(
				dataProvider.getList());
		disciplineSharingCT.addColumnSortHandler(sortHandler);

		// scopeUnit sort
		sortHandler.setComparator(scopeUnitColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getScopeUnitName().compareTo(o2.getScopeUnitName());
			}
		});

		// charge sort
		sortHandler.setComparator(chargeColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getCharge().toString().compareTo(o2.getCharge().toString());
			}
		});

		// architectureDesign sort
		sortHandler.setComparator(architectureDesignColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getArchitectureDesign().toString().compareTo(o2.getArchitectureDesign().toString());
			}
		});

		// businessModeling sort
		sortHandler.setComparator(businessModelingColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getBusinessModeling().toString().compareTo(o2.getBusinessModeling().toString());
			}
		});

		// changeDriving sort
		sortHandler.setComparator(changeDrivingColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getChangeDriving().toString().compareTo(o2.getChangeDriving().toString());
			}
		});

		// configurationManagement sort
		sortHandler.setComparator(configurationManagementColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getConfigurationManagement().toString()
						.compareTo(o2.getConfigurationManagement().toString());
			}
		});

		// implementation sort
		sortHandler.setComparator(implementationColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getImplementation().toString().compareTo(o2.getImplementation().toString());
			}
		});

		// projectManagement sort
		sortHandler.setComparator(projectManagementColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getProjectManagement().toString().compareTo(o2.getProjectManagement().toString());
			}
		});

		// qualityAssurance sort
		sortHandler.setComparator(qualityAssuranceColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getQualityAssurance().toString().compareTo(o2.getQualityAssurance().toString());
			}
		});

		// receipts sort
		sortHandler.setComparator(receiptsColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getReceipts().toString().compareTo(o2.getReceipts().toString());
			}
		});

		// requirementsAnalysis sort
		sortHandler.setComparator(requirementsAnalysisColumn, new Comparator<DisciplineSharingDTO>() {

			@Override
			public int compare(DisciplineSharingDTO o1, DisciplineSharingDTO o2) {
				return o1.getRequirementsAnalysis().toString()
						.compareTo(o2.getRequirementsAnalysis().toString());
			}
		});
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
	public void setSharingHeader(Set<ProjectDisciplineDTO> projectDisciplines) {
		for (ProjectDisciplineDTO projectDisciplineDTO : projectDisciplines) {
			String percent = addSharing(projectDisciplineDTO.getDisciplinePourcentage());
			if (projectDisciplineDTO.getDisciplineDTO().getFunctionalId()
					.equals(DisciplinePhareEnum.architectureDesign.name())) {
				if (!(disciplineSharingCT.getColumnIndex(architectureDesignColumn) < 0)) {
               disciplineSharingCT.removeColumn(architectureDesignColumn);
            }
				disciplineSharingCT.addColumn(architectureDesignColumn,
						Common.getLinesHeader(Common.MESSAGES_ESTIMATION.architectureDesign(), percent));
				disciplineSharingCT.setColumnWidth(architectureDesignColumn, 1, Unit.PX);
			}
			if (projectDisciplineDTO.getDisciplineDTO().getFunctionalId()
					.equals(DisciplinePhareEnum.businessModeling.name())) {
				if (!(disciplineSharingCT.getColumnIndex(businessModelingColumn) < 0)) {
               disciplineSharingCT.removeColumn(businessModelingColumn);
            }
				disciplineSharingCT.addColumn(businessModelingColumn,
						Common.getLinesHeader(Common.MESSAGES_ESTIMATION.businessModeling(), percent));
				disciplineSharingCT.setColumnWidth(businessModelingColumn, 1, Unit.PX);
			}
			if (projectDisciplineDTO.getDisciplineDTO().getFunctionalId()
					.equals(DisciplinePhareEnum.changeDriving.name())) {
				if (!(disciplineSharingCT.getColumnIndex(changeDrivingColumn) < 0)) {
               disciplineSharingCT.removeColumn(changeDrivingColumn);
            }
				disciplineSharingCT.addColumn(changeDrivingColumn,
						Common.getLinesHeader(Common.MESSAGES_ESTIMATION.changeDriving(), percent));
				disciplineSharingCT.setColumnWidth(changeDrivingColumn, 1, Unit.PX);
			}
			if (projectDisciplineDTO.getDisciplineDTO().getFunctionalId()
					.equals(DisciplinePhareEnum.configurationManagement.name())) {
				if (!(disciplineSharingCT.getColumnIndex(configurationManagementColumn) < 0)) {
               disciplineSharingCT.removeColumn(configurationManagementColumn);
            }
				disciplineSharingCT.addColumn(configurationManagementColumn,
						Common.getLinesHeader(Common.MESSAGES_ESTIMATION.configurationManagement(), percent));
				disciplineSharingCT.setColumnWidth(configurationManagementColumn, 1, Unit.PX);
			}
			if (projectDisciplineDTO.getDisciplineDTO().getFunctionalId()
					.equals(DisciplinePhareEnum.implementation.name())) {
				if (!(disciplineSharingCT.getColumnIndex(implementationColumn) < 0)) {
               disciplineSharingCT.removeColumn(implementationColumn);
            }
				disciplineSharingCT.addColumn(implementationColumn,
						Common.getLinesHeader(Common.MESSAGES_ESTIMATION.implementation(), percent));
				disciplineSharingCT.setColumnWidth(implementationColumn, 1, Unit.PX);
			}
			if (projectDisciplineDTO.getDisciplineDTO().getFunctionalId()
					.equals(DisciplinePhareEnum.projectManagement.name())) {
				if (!(disciplineSharingCT.getColumnIndex(projectManagementColumn) < 0)) {
               disciplineSharingCT.removeColumn(projectManagementColumn);
            }
				disciplineSharingCT.addColumn(projectManagementColumn,
						Common.getLinesHeader(Common.MESSAGES_ESTIMATION.projectManagement(), percent));
				disciplineSharingCT.setColumnWidth(projectManagementColumn, 1, Unit.PX);
			}
			if (projectDisciplineDTO.getDisciplineDTO().getFunctionalId()
					.equals(DisciplinePhareEnum.qualityAssurance.name())) {
				if (!(disciplineSharingCT.getColumnIndex(qualityAssuranceColumn) < 0)) {
               disciplineSharingCT.removeColumn(qualityAssuranceColumn);
            }
				disciplineSharingCT.addColumn(qualityAssuranceColumn,
						Common.getLinesHeader(Common.MESSAGES_ESTIMATION.qualityAssurance(), percent));
				disciplineSharingCT.setColumnWidth(qualityAssuranceColumn, 1, Unit.PX);
			}
			if (projectDisciplineDTO.getDisciplineDTO().getFunctionalId()
					.equals(DisciplinePhareEnum.receipts.name())) {
				if (!(disciplineSharingCT.getColumnIndex(receiptsColumn) < 0)) {
               disciplineSharingCT.removeColumn(receiptsColumn);
            }
				disciplineSharingCT.addColumn(receiptsColumn,
						Common.getLinesHeader(Common.MESSAGES_ESTIMATION.receipts(), percent));
				disciplineSharingCT.setColumnWidth(receiptsColumn, 1, Unit.PX);
			}
			if (projectDisciplineDTO.getDisciplineDTO().getFunctionalId()
					.equals(DisciplinePhareEnum.requirementsAnalysis.name())) {
				if (!(disciplineSharingCT.getColumnIndex(requirementsAnalysisColumn) < 0)) {
               disciplineSharingCT.removeColumn(requirementsAnalysisColumn);
            }
				disciplineSharingCT.addColumn(requirementsAnalysisColumn,
						Common.getLinesHeader(Common.MESSAGES_ESTIMATION.requirementsAnalysis(), percent));
				disciplineSharingCT.setColumnWidth(requirementsAnalysisColumn, 1, Unit.PX);
			}
		}
	}

	private String addSharing(int sharing) {
		return " (" + Integer.valueOf(sharing).toString() + "%)";
	}

	interface DisciplineSharingViewImplUiBinder extends UiBinder<Widget, DisciplineSharingViewImpl>
	{
	}
}
