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
package org.novaforge.forge.ui.forge.reference.client.view.tool;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;
import org.novaforge.forge.ui.commons.client.dialogbox.ValidateDialogBox;
import org.novaforge.forge.ui.forge.reference.client.presenter.commons.CellKey;
import org.novaforge.forge.ui.forge.reference.client.properties.UploadMessage;
import org.novaforge.forge.ui.forge.reference.client.resources.ReferenceResources;
import org.novaforge.forge.ui.forge.reference.shared.FileInfoDTO;

import java.util.Comparator;

public class DownloadToolTabViewImpl extends Composite implements DownloadToolTabView

{
	private static DownloadToolTabViewImplUiBinder uiBinder   = GWT
	                                                              .create(DownloadToolTabViewImplUiBinder.class);
	private static ReferenceResources              ressources = GWT.create(ReferenceResources.class);
	private final UploadMessage messages = (UploadMessage) GWT.create(UploadMessage.class);
	private final ValidateDialogBox confimeDeleteBox;
	@UiField
	Label                                 toolSearchTitle;
	@UiField
	Label                                 toolListTitle;

	@UiField(provided = true)
	CellTable<FileInfoDTO>                toolCellTable;

	@UiField(provided = true)
	SimplePager                           toolPager;

	@UiField
	Label                                 nameSearchLabel;

	@UiField
	TextBox                               nameSearchTB;

	private ListDataProvider<FileInfoDTO> dataTargetTool;

	private Column<FileInfoDTO, String>   nameColumn;
	private Column<FileInfoDTO, String>   versionColumn;
	private Column<FileInfoDTO, String>   sizeColumn;

	public DownloadToolTabViewImpl()
	{
		ressources.style().ensureInjected();
		confimeDeleteBox = new ValidateDialogBox(messages.deleteFileValidationMessage());
		initToolTable();
		initWidget(uiBinder.createAndBindUi(this));
		toolListTitle.setText(messages.toolListTitle());
		toolSearchTitle.setText(messages.filterTitle());
		nameSearchLabel.setText(messages.name());
	}

	private void initToolTable()
	{
		toolCellTable = new CellTable<FileInfoDTO>(10, (Resources) GWT.create(TableResources.class),
		    CellKey.DOWLOAD_TOOL_KEY_PROVIDER);
		toolCellTable.setWidth("100%", false);
		// Init empty widget
		final Label emptyUsersLabel = new Label(messages.emptyToolMessage());
		emptyUsersLabel.setStyleName(ressources.style().emptyLabel());
		toolCellTable.setEmptyTableWidget(emptyUsersLabel);

		// Create a Pager to control the CellTable
		final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		toolPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		toolPager.setDisplay(toolCellTable);
		// Initialize the columns.
		initToolTableColumns();

		// Add the CellTable to the adapter
		dataTargetTool = new ListDataProvider<FileInfoDTO>();

		dataTargetTool.addDataDisplay(toolCellTable);
		// Add the CellTable to the adapter
		final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(toolCellTable);
		toolCellTable.addColumnSortHandler(columnSortHandler);
	}

	private void initToolTableColumns()
	{
		// LabelColumn
		nameColumn = new Column<FileInfoDTO, String>(new TextCell())
		{
			@Override
			public String getValue(final FileInfoDTO object)
			{
				return object.getName();
			}
		};
		nameColumn.setSortable(true); // colone triable
		toolCellTable.addColumn(nameColumn, messages.fileNameLabel());

		// versionName Column
		versionColumn = new Column<FileInfoDTO, String>(new TextCell())
		{
			@Override
			public String getValue(final FileInfoDTO object)
			{
				return object.getVersion();
			}
		};
		versionColumn.setSortable(true);
		toolCellTable.addColumn(versionColumn, messages.fileVersionLabel());
		// sizeName Column
		sizeColumn = new Column<FileInfoDTO, String>(new TextCell())
		{
			@Override
			public String getValue(final FileInfoDTO object)
			{
				return String.valueOf(object.getSize());
			}
		};
		sizeColumn.setSortable(true);
		toolCellTable.addColumn(sizeColumn, messages.fileSizeLabel());
	}

	@Override
	public ListDataProvider<FileInfoDTO> getToolsDataProvider()
	{
		return dataTargetTool;
	}

	@Override
	public CellTable<FileInfoDTO> getToolTable()
	{
		return toolCellTable;
	}

	@Override
	public void toolListSortHandler()
	{
		final ListHandler<FileInfoDTO> sortHandler = new ListHandler<FileInfoDTO>(dataTargetTool.getList());
		toolCellTable.addColumnSortHandler(sortHandler);

		sortHandler.setComparator(nameColumn, new Comparator<FileInfoDTO>()
		{
			@Override
			public int compare(final FileInfoDTO o1, final FileInfoDTO o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});

		sortHandler.setComparator(versionColumn, new Comparator<FileInfoDTO>()
		{
			@Override
			public int compare(final FileInfoDTO o1, final FileInfoDTO o2)
			{
				final int compV = o1.getVersion().compareTo(o2.getVersion());
				if (compV == 0)
				{
					return o1.getName().compareTo(o2.getName());
				}
				else
				{
					return compV;
				}
			}
		});
		sortHandler.setComparator(versionColumn, new Comparator<FileInfoDTO>()
		{
			@Override
			public int compare(final FileInfoDTO o1, final FileInfoDTO o2)
			{
				final int compV = o1.getVersion().compareTo(o2.getVersion());
				if (compV == 0)
				{
					return o1.getName().compareTo(o2.getName());
				}
				else
				{
					return compV;
				}
			}
		});
		sortHandler.setComparator(sizeColumn, new Comparator<FileInfoDTO>()
		{
			@Override
			public int compare(final FileInfoDTO o1, final FileInfoDTO o2)
			{
				return o1.getSize().compareTo(o2.getSize());
			}
		});
	}

	@Override
	public ValidateDialogBox getConfimeDeleteBox()
	{
		return confimeDeleteBox;
	}

	@Override
	public TextBox getNameSearchTB()
	{
		return nameSearchTB;
	}

	interface DownloadToolTabViewImplUiBinder extends UiBinder<Widget, DownloadToolTabViewImpl>
	{
	}
}
