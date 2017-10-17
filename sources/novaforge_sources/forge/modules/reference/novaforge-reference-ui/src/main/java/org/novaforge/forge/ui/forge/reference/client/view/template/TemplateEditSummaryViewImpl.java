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
package org.novaforge.forge.ui.forge.reference.client.view.template;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.CellKey;
import org.novaforge.forge.ui.forge.reference.client.properties.TemplateMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;
import org.novaforge.forge.ui.forge.reference.client.view.project.resources.TreeResources;
import org.novaforge.forge.ui.forge.reference.shared.NodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.RoleDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateApplicationNodeDTO;
import org.novaforge.forge.ui.forge.reference.shared.template.TemplateSpaceNodeDTO;

import java.util.Comparator;

/**
 * @author lamirang
 */
public class TemplateEditSummaryViewImpl extends Composite implements TemplateEditSummaryView
{

	private static TemplateEditSummaryViewImplUiBinder uiBinder         = GWT
	                                                                        .create(TemplateEditSummaryViewImplUiBinder.class);
	private static ReferenceResources                  ressources       = GWT.create(ReferenceResources.class);
	private static TreeResources                       treeResources    = GWT.create(TreeResources.class);
	private final TemplateMessage templateMessages = (TemplateMessage) GWT.create(TemplateMessage.class);
	private final TemplateTreeModel viewModel;
	@UiField
	Label                             templateSummaryTitle;
	@UiField
	Grid                              templateDetailGrid;
	@UiField
	Label                             templateNameLabel;
	@UiField
	Label                             templateName;
	@UiField
	Label                             templateIdLabel;
	@UiField
	Label                             templateId;
	@UiField
	Label                             templateDescLabel;
	@UiField
	Label                             templateDesc;
	@UiField(provided = true)
	CellTable<RoleDTO>                rolesCellTable;
	@UiField(provided = true)
	SimplePager                       rolePager;
	@UiField(provided = true)
	CellTree                          templateTree;
	ListDataProvider<String> provider;
	ListDataProvider<String> rolesProvider;
	private ListDataProvider<RoleDTO> dataRoleProvider;
	private Column<RoleDTO, String>   roleNameColumn;
	private Column<RoleDTO, String>   roleDescriptionColumn;

	public TemplateEditSummaryViewImpl()
	{
		ressources.style().ensureInjected();

		// Init tree cell
		viewModel = new TemplateTreeModel();
		templateTree = new CellTree(viewModel, null, treeResources);
		templateTree.setAnimationEnabled(true);

		initCellTable();

		// Generate ui
		initWidget(uiBinder.createAndBindUi(this));

		// Initialization of group detail form
		templateSummaryTitle.setText(templateMessages.summaryTitle());
		templateNameLabel.setText(templateMessages.name());
		templateIdLabel.setText(templateMessages.id());
		templateDescLabel.setText(templateMessages.description());

		// Initialization row style
		for (int row = 0; row < templateDetailGrid.getRowCount(); row++)
		{
			if ((row % 2) == 0)
			{
				templateDetailGrid.getRowFormatter().addStyleName(row, ressources.style().gridRowPair());
			}
			templateDetailGrid.getCellFormatter().addStyleName(row, 0, ressources.style().labelCell());
		}

	}

	/**
	 *
	 */
	protected void initCellTable()
	{
		final Resources res = GWT.create(TableResources.class);
		rolesCellTable = new CellTable<RoleDTO>(8, res, CellKey.ROLE_KEY_PROVIDER);
		rolesCellTable.setWidth("100%", true);
		// Init empty widget
		final Label emptyUsersLabel = new Label(templateMessages.emptyRoleMessage());
		emptyUsersLabel.setStyleName(ressources.style().emptyLabel());
		rolesCellTable.setEmptyTableWidget(emptyUsersLabel);

		// Create a Pager to control the CellTable
		final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		rolePager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		rolePager.setDisplay(rolesCellTable);

		// Initialize the columns.
		initTableColumns();

		// Add the CellTable to the adapter
		dataRoleProvider = new ListDataProvider<RoleDTO>();
		dataRoleProvider.addDataDisplay(rolesCellTable);

		// Add the CellTable to the adapter
		final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(rolesCellTable);
		rolesCellTable.addColumnSortHandler(columnSortHandler);

	}

	/**
	 * Add the columns to the table.
	 */
	private void initTableColumns()
	{
		// Name role column
		roleNameColumn = new Column<RoleDTO, String>(new TextCell())
		{
			@Override
			public String getValue(final RoleDTO object)
			{
				return object.getName();
			}
		};
		roleNameColumn.setSortable(true);
		rolesCellTable.addColumn(roleNameColumn, templateMessages.name());

		// Description role column
		roleDescriptionColumn = new Column<RoleDTO, String>(new TextCell())
		{
			@Override
			public String getValue(final RoleDTO object)
			{
				return object.getDescription();
			}
		};
		roleDescriptionColumn.setSortable(true);
		rolesCellTable.addColumn(roleDescriptionColumn, templateMessages.description());
	}

	@Override
	public Label getTemplateSummaryTitle()
	{
		return templateSummaryTitle;
	}

	@Override
	public Label getTemplateName()
	{
		return templateName;
	}

	@Override
	public Label getTemplateId()
	{
		return templateId;
	}

	@Override
	public Label getTemplateDescription()
	{
		return templateDesc;
	}

	@Override
	public void updateSortHandler()
	{
		// Add the Sort Handler to the CellTable
		final ListHandler<RoleDTO> sortHandler = new ListHandler<RoleDTO>(dataRoleProvider.getList());
		rolesCellTable.addColumnSortHandler(sortHandler);

		// description sort
		sortHandler.setComparator(roleDescriptionColumn, new Comparator<RoleDTO>()
		{
			@Override
			public int compare(final RoleDTO o1, final RoleDTO o2)
			{
				return o1.getDescription().compareTo(o2.getDescription());
			}
		});

		// name sort
		sortHandler.setComparator(roleNameColumn, new Comparator<RoleDTO>()
		{
			@Override
			public int compare(final RoleDTO o1, final RoleDTO o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	@Override
	public CellTable<RoleDTO> getRolesCellTable()
	{
		return rolesCellTable;
	}

	@Override
	public ListDataProvider<RoleDTO> getDataRoleProvider()
	{
		return dataRoleProvider;
	}

	@Override
	public CellTree getTemplateTree()
	{
		return templateTree;
	}

	@Override
	public ListDataProvider<TemplateSpaceNodeDTO> getSpacesDataProvider()
	{
		return viewModel.getSpacesDataProvider();
	}

	interface TemplateEditSummaryViewImplUiBinder extends UiBinder<Widget, TemplateEditSummaryViewImpl>
	{
	}

	private static class TemplateTreeModel implements TreeViewModel
	{
		private final ListDataProvider<TemplateSpaceNodeDTO> spacesDataProvider = new ListDataProvider<TemplateSpaceNodeDTO>();
		private final SingleSelectionModel<NodeDTO>          selectionModel     = new SingleSelectionModel<NodeDTO>();

		@Override
		public <T> NodeInfo<?> getNodeInfo(final T value)
		{
			if (value == null)
			{
				return new DefaultNodeInfo<TemplateSpaceNodeDTO>(spacesDataProvider, new SpaceNodeCell(), selectionModel, null);
			}
			else if (value instanceof TemplateSpaceNodeDTO)
			{
				final TemplateSpaceNodeDTO space = (TemplateSpaceNodeDTO) value;
				return new DefaultNodeInfo<TemplateApplicationNodeDTO>(new ListDataProvider<TemplateApplicationNodeDTO>(space
																																																										.getApplications()),
																															 new ApplicationNodeCell(), selectionModel, null);
			}
			// Unhandled type.
			final String type = value.getClass().getName();
			throw new IllegalArgumentException("Unsupported object type: " + type);
		}

		// Check if the specified value represents a leaf node. Leaf nodes
		// cannot be opened.
		@Override
		public boolean isLeaf(final Object value)
		{
			boolean isLeaf = false;
			if (value instanceof TemplateApplicationNodeDTO)
			{
				isLeaf = true;
			}
			else if ((value instanceof TemplateSpaceNodeDTO) && (((TemplateSpaceNodeDTO) value).getApplications().isEmpty()))
			{
				isLeaf = true;
			}
			return isLeaf;
		}

		/**
		 * @return the spacesDataProvider
		 */
		public ListDataProvider<TemplateSpaceNodeDTO> getSpacesDataProvider()
		{
			return spacesDataProvider;
		}

		/**
		 * A Cell used to render the space.
		 */
		private static class SpaceNodeCell extends AbstractCell<TemplateSpaceNodeDTO>
		{

			@Override
			public void render(final Context context, final TemplateSpaceNodeDTO value, final SafeHtmlBuilder sb)
			{
				if (value != null)
				{
					sb.appendEscaped(value.getName());
				}
			}
		}

		/**
		 * A Cell used to render the application.
		 */
		private static class ApplicationNodeCell extends AbstractCell<TemplateApplicationNodeDTO>
		{

			@Override
			public void render(final Context context, final TemplateApplicationNodeDTO value, final SafeHtmlBuilder sb)
			{
				if (value != null)
				{
					sb.appendEscaped(value.getName());
				}
			}
		}
	}
}
