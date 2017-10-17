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
package org.novaforge.forge.tools.deliverymanager.ui.client.view.tabs;

import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
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
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import org.novaforge.forge.tools.deliverymanager.ui.client.DeliveryCommon;
import org.novaforge.forge.tools.deliverymanager.ui.client.presenter.commons.CellKey;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ArtefactNode;
import org.novaforge.forge.tools.deliverymanager.ui.shared.ParameterKey;
import org.novaforge.forge.tools.deliverymanager.ui.shared.SourceFileDTO;
import org.novaforge.forge.ui.commons.client.Common;
import org.novaforge.forge.ui.commons.client.celltable.TableResources;
import org.novaforge.forge.ui.commons.client.mimetype.MimeType;
import org.novaforge.forge.ui.commons.client.upload.CustomSingleUploader;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

/**
 * @author Guillaume Lamirand
 */
public class BinariesViewImpl extends Composite implements BinariesView
{

	private static BinariesViewImplUiBinder	uiBinder	= GWT.create(BinariesViewImplUiBinder.class);
	@UiField
	Label														binariesTitle;
	@UiField
	Label														binariesInfo;
	@UiField
	VerticalPanel											uploadPanel;
	CustomSingleUploader									singleUploader;
	@UiField
	Label														binariesExtLabel;
	@UiField
	TextBox													binariesExt;
	@UiField
	Label														fileNameExtLabel;
	@UiField
	TextBox													fileNameExtBox;
	@UiField
	Button													binariesExtButton;
	@UiField
	Label														binariesListTitle;
	@UiField
	Label														editFileNameLabel;
	@UiField(provided = true)
	CellTable<ArtefactNode>								binariesCellTable;
	@UiField(provided = true)
	SimplePager												binariesPager;
	private ListDataProvider<ArtefactNode>			dataDeliveryProvider;
	private Column<ArtefactNode, String>			nameColumn;
	private Column<ArtefactNode, ImageResource>	typeColumn;
	private Column<ArtefactNode, String>			sizeColumn;
	private Column<ArtefactNode, String>			sourceColumn;
	/**
	 * Default constructor
	 */
	public BinariesViewImpl()
	{
		initBinariesTable();
		DeliveryCommon.getResources().css().ensureInjected();

		// Image setting
		initWidget(uiBinder.createAndBindUi(this));

		binariesTitle.setText(DeliveryCommon.getMessages().deliveryBinariesTitle());
		binariesInfo.setText(DeliveryCommon.getMessages().deliveryBinariesUpload());
		binariesListTitle.setText(DeliveryCommon.getMessages().deliveryBinariesList());
		editFileNameLabel.setText(DeliveryCommon.getMessages().editFileName());
		binariesExtLabel.setText(DeliveryCommon.getMessages().deliveryBinariesExt());
		fileNameExtLabel.setText(DeliveryCommon.getMessages().deliveryBinariesExtFileName());
		binariesExtButton.setText(DeliveryCommon.getMessages().uploadButton());
		binariesExtButton.setEnabled(false);

		singleUploader = new CustomSingleUploader();

		uploadPanel.add(singleUploader);
	}

	private void initBinariesTable()
	{
		binariesCellTable = new CellTable<ArtefactNode>(5, (Resources) GWT.create(TableResources.class),
				CellKey.ARTEFACT_KEY_PROVIDER);
		binariesCellTable.setWidth("100%", false);

		// Init empty widget
		final Label emptyDeliveryLabel = new Label(DeliveryCommon.getMessages().emptyBinariesMessage());
		emptyDeliveryLabel.setStyleName(Common.getResources().css().emptyLabel());
		binariesCellTable.setEmptyTableWidget(emptyDeliveryLabel);

		// Create a Pager to control the CellTable
		final SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		binariesPager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		binariesPager.setDisplay(binariesCellTable);

		// Initialize the columns.
		initBinariesTableColumns();

		// Add the CellTable to the adapter
		dataDeliveryProvider = new ListDataProvider<ArtefactNode>(CellKey.ARTEFACT_KEY_PROVIDER);
		dataDeliveryProvider.addDataDisplay(binariesCellTable);

		// Add the CellTable to the adapter
		final ColumnSortEvent.AsyncHandler columnSortHandler = new ColumnSortEvent.AsyncHandler(
				binariesCellTable);
		binariesCellTable.addColumnSortHandler(columnSortHandler);

	}

	private void initBinariesTableColumns()
	{
		// Image Column
		typeColumn = new Column<ArtefactNode, ImageResource>(new ImageResourceCell())
		{
			@Override
			public ImageResource getValue(final ArtefactNode object)
			{
				return MimeType.getMimeTypeImageResource(object.getName());
			}
		};
		binariesCellTable.setColumnWidth(typeColumn, 30, Unit.PX);
		binariesCellTable.addColumn(typeColumn, DeliveryCommon.getMessages().type());
		// Title Column
		nameColumn = new Column<ArtefactNode, String>(new EditTextCell())
		{
			@Override
			public String getValue(final ArtefactNode object)
			{
				return object.getName();
			}
		};
		nameColumn.setSortable(true);
		binariesCellTable.setColumnWidth(nameColumn, 30, Unit.PC);
		binariesCellTable.addColumn(nameColumn, DeliveryCommon.getMessages().name());

		// Size column
		sizeColumn = new Column<ArtefactNode, String>(new TextCell())
		{
			@Override
			public String getValue(final ArtefactNode object)
			{
			  Long size = Long.valueOf(0);
        if (object.getFields().containsKey(ParameterKey.SIZE.getKey()))
        {
          size = Long.valueOf(object.getFields().get(ParameterKey.SIZE.getKey()));
        }

        final StringBuilder sizeBuilder = new StringBuilder();
				final BigDecimal bigDecimal = new BigDecimal(size);
				if (size < 1200)
				{
					sizeBuilder.append(String.valueOf(size));
					sizeBuilder.append(Common.SPACE_STRING);
					sizeBuilder.append(DeliveryCommon.getMessages().sizeO());
				}
				else if ((size > 1200) && (size < 1048576))
				{
					final BigDecimal result = bigDecimal.divide(new BigDecimal(1024));
					sizeBuilder.append(NumberFormat.getDecimalFormat().format(result));
					sizeBuilder.append(Common.SPACE_STRING);
					sizeBuilder.append(DeliveryCommon.getMessages().sizeKo());

				}
				else
				{
					final BigDecimal result = bigDecimal.divide(new BigDecimal(1048576));
					sizeBuilder.append(NumberFormat.getDecimalFormat().format(result));
					sizeBuilder.append(Common.SPACE_STRING);
					sizeBuilder.append(DeliveryCommon.getMessages().sizeMo());

				}
				return sizeBuilder.toString();
			}
		};
		binariesCellTable.setColumnWidth(sizeColumn, 30, Unit.PC);
		binariesCellTable.addColumn(sizeColumn, DeliveryCommon.getMessages().size());

		// Source column
		sourceColumn = new Column<ArtefactNode, String>(new TextCell()
		{

			@Override
			public void render(final Context pContext, final SafeHtml pValue, final SafeHtmlBuilder pSb)
			{
				if (pValue != null)
				{
					if (pValue.asString().contains(DeliveryCommon.getMessages().remote()))
					{
						final ArtefactNode rendered = getRendered(pContext);
						final StringBuilder html = new StringBuilder();
						html.append("<span title=\"");
						if ((rendered != null) && (rendered.getFields().containsKey(ParameterKey.URL.getKey())))
						{

							html.append(rendered.getFields().get(ParameterKey.URL.getKey()));
						}
						else
						{
							html.append(DeliveryCommon.getMessages().urlUnknown());

						}
						html.append("\">");
						pSb.appendHtmlConstant(html.toString());
					}
					pSb.append(pValue);

					if (pValue.asString().contains(DeliveryCommon.getMessages().remote()))
					{
						pSb.appendHtmlConstant("</span>");
					}
				}
			}

			private ArtefactNode getRendered(final Context context)
			{
				ArtefactNode returnArtefact = null;
				final List<ArtefactNode> list = dataDeliveryProvider.getList();
				for (final ArtefactNode artefactNode : list)
				{
					final Object key = dataDeliveryProvider.getKey(artefactNode);
					if ((key != null) && (key.equals(context.getKey())))
					{
						returnArtefact = artefactNode;
						break;
					}
				}
				return returnArtefact;

			}
		})
		{
			@Override
			public String getValue(final ArtefactNode object)
			{
				final StringBuilder sourceReturned = new StringBuilder();
				if (object.getFields().containsKey(ParameterKey.SOURCE.getKey()))
				{
					final String source = object.getFields().get(ParameterKey.SOURCE.getKey());
					final SourceFileDTO sourceFile = SourceFileDTO.getById(source);
					if (SourceFileDTO.REMOTE.equals(sourceFile))
					{
						sourceReturned.append(DeliveryCommon.getMessages().remote());
						if (object.getFields().containsKey(ParameterKey.URL.getKey()))
						{
							final String url = object.getFields().get(ParameterKey.URL.getKey());
							if (url.length() > 20)
							{
								sourceReturned.append(url.substring(0, 20));
							}
							else
							{
								sourceReturned.append(url);

							}
							sourceReturned.append(" ...");
						}

					}
					else
					{
						sourceReturned.append(DeliveryCommon.getMessages().locale());
					}
				}
				else
				{
					sourceReturned.append(DeliveryCommon.getMessages().locale());
				}
				return sourceReturned.toString();
			}

		};
		binariesCellTable.setColumnWidth(sizeColumn, 30, Unit.PC);
		binariesCellTable.addColumn(sourceColumn, DeliveryCommon.getMessages().source());

	}

	@Override
	public void binariesListSortHandler()
	{
		final ListHandler<ArtefactNode> sortHandler = new ListHandler<ArtefactNode>(
				dataDeliveryProvider.getList());
		binariesCellTable.addColumnSortHandler(sortHandler);

		sortHandler.setComparator(nameColumn, new Comparator<ArtefactNode>()
		{
			@Override
			public int compare(final ArtefactNode o1, final ArtefactNode o2)
			{
				return o1.getName().compareTo(o2.getName());
			}
		});
	}

	@Override
	public CellTable<ArtefactNode> getBinariesTable()
	{
		return binariesCellTable;
	}

	@Override
	public ListDataProvider<ArtefactNode> getListDataProvider()
	{
		return dataDeliveryProvider;
	}

	@Override
	public CustomSingleUploader getUploader()
	{
		return singleUploader;
	}

	@Override
	public Button getExtButton()
	{
		return binariesExtButton;
	}

	@Override
	public TextBox getExtBox()
	{
		return binariesExt;
	}

	@Override
	public TextBox getExtFileNameBox()
	{
		return fileNameExtBox;
	}

	@Override
	public Button getUploaderButton()
	{
		return singleUploader.getButton();
	}

	@Override
	public Column<ArtefactNode, String> getFileNameColumn()
	{
		return nameColumn;
	}

	interface BinariesViewImplUiBinder extends UiBinder<Widget, BinariesViewImpl>
	{
	}

}
